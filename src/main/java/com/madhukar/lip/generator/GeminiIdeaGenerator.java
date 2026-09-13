package com.madhukar.lip.generator;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;
import com.madhukar.lip.config.AIProperties;
import com.madhukar.lip.dto.PostIdeaResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Component
@ConditionalOnProperty(
        name = "ai.provider",
        havingValue = "gemini"
)
public class GeminiIdeaGenerator implements IdeaGenerator {

    private static final Logger log =
            LoggerFactory.getLogger(GeminiIdeaGenerator.class);

    private static final String GEMINI_INTERACTIONS_URL =
            "https://generativelanguage.googleapis.com/v1beta/interactions";

    private final RestClient restClient;
    private final AIProperties aiProperties;
    private final JsonMapper jsonMapper;

    public GeminiIdeaGenerator(
            RestClient restClient,
            AIProperties aiProperties,
            JsonMapper jsonMapper) {

        this.restClient = restClient;
        this.aiProperties = aiProperties;
        this.jsonMapper = jsonMapper;
    }

    @Override
    public List<PostIdeaResponse> generateIdeas(String topic) {

        String prompt = """
                You are a professional LinkedIn content idea generator
                for software developers.

                Generate exactly 3 useful LinkedIn post ideas based on
                the developer's learning topic.

                TOPIC:
                %s

                RULES:
                - Make the ideas useful for a software engineering audience.
                - Avoid generic motivational posts.
                - Make each idea different from the others.
                - Keep titles concise and engaging.
                - Keep descriptions concise.
                - Do not invent personal experience.
                - Return only the requested JSON structure.
                """.formatted(topic);

        Map<String, Object> responseSchema = Map.of(
                "type", "object",
                "properties", Map.of(
                        "ideas", Map.of(
                                "type", "array",
                                "items", Map.of(
                                        "type", "object",
                                        "properties", Map.of(
                                                "id", Map.of(
                                                        "type", "integer"
                                                ),
                                                "title", Map.of(
                                                        "type", "string"
                                                ),
                                                "description", Map.of(
                                                        "type", "string"
                                                )
                                        ),
                                        "required",
                                        List.of(
                                                "id",
                                                "title",
                                                "description"
                                        )
                                )
                        )
                ),
                "required", List.of("ideas")
        );

        Map<String, Object> responseFormat = Map.of(
                "type", "text",
                "mime_type", "application/json",
                "schema", responseSchema
        );

        Map<String, Object> requestBody = Map.of(
                "model", aiProperties.model(),
                "input", prompt,
                "response_format", responseFormat
        );

        log.debug("Calling Gemini Interactions API");

        try {

            JsonNode response = restClient
                    .post()
                    .uri(GEMINI_INTERACTIONS_URL)
                    .header(
                            "x-goog-api-key",
                            aiProperties.apiKey()
                    )
                    .header(
                            "Content-Type",
                            "application/json"
                    )
                    .body(requestBody)
                    .retrieve()
                    .body(JsonNode.class);

            String generatedJson =
                    extractGeneratedText(response);

            JsonNode ideasNode =
                    jsonMapper.readTree(generatedJson)
                            .path("ideas");

            return jsonMapper.readerForListOf(PostIdeaResponse.class)
                    .readValue(ideasNode);

        } catch (Exception e) {

            log.error(
                    "Failed to generate LinkedIn post ideas using Gemini",
                    e
            );

            throw new RuntimeException(
                    "Failed to generate LinkedIn post ideas",
                    e
            );
        }
    }

    private String extractGeneratedText(JsonNode response) {

        JsonNode steps = response.path("steps");

        for (JsonNode step : steps) {

            if (!"model_output".equals(
                    step.path("type").asText())) {
                continue;
            }

            for (JsonNode content : step.path("content")) {

                if ("text".equals(
                        content.path("type").asText())) {

                    return content.path("text").asText();
                }
            }
        }

        throw new IllegalStateException(
                "Gemini response did not contain model output text"
        );
    }
}