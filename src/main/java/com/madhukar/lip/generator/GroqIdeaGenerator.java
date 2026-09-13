package com.madhukar.lip.generator;

import com.madhukar.lip.config.AIProperties;
import com.madhukar.lip.dto.PostIdeaResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;
import java.util.Map;

@Component
@ConditionalOnProperty(
        name = "ai.provider",
        havingValue = "groq"
)
public class GroqIdeaGenerator implements IdeaGenerator {

    private static final Logger log =
            LoggerFactory.getLogger(GroqIdeaGenerator.class);

    private static final String GROQ_API_URL =
            "https://api.groq.com/openai/v1/chat/completions";

    private final RestClient restClient;
    private final AIProperties aiProperties;
    private final JsonMapper jsonMapper;

    public GroqIdeaGenerator(
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
                - Return only valid JSON.
                """.formatted(topic);

        Map<String, Object> responseFormat = Map.of(
                "type", "json_schema",
                "json_schema", Map.of(
                        "name", "linkedin_post_ideas",
                        "strict", true,
                        "schema", Map.of(
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
                                                        "required", List.of(
                                                                "id",
                                                                "title",
                                                                "description"
                                                        ),
                                                        "additionalProperties", false
                                                )
                                        )
                                ),
                                "required", List.of("ideas"),
                                "additionalProperties", false
                        )
                )
        );

        Map<String, Object> requestBody = Map.of(
                "model", aiProperties.model(),
                "messages", List.of(
                        Map.of(
                                "role", "system",
                                "content", "You generate professional LinkedIn post ideas."
                        ),
                        Map.of(
                                "role", "user",
                                "content", prompt
                        )
                ),
                "response_format", responseFormat,
                "temperature", 0.7
        );

        log.debug("Calling Groq API using model: {}", aiProperties.model());

        try {

            JsonNode response = restClient
                    .post()
                    .uri(GROQ_API_URL)
                    .header(
                            "Authorization",
                            "Bearer " + aiProperties.apiKey()
                    )
                    .header(
                            "Content-Type",
                            "application/json"
                    )
                    .body(requestBody)
                    .retrieve()
                    .body(JsonNode.class);

            String generatedJson =
                    response
                            .path("choices")
                            .get(0)
                            .path("message")
                            .path("content")
                            .asText();

            JsonNode ideasNode =
                    jsonMapper
                            .readTree(generatedJson)
                            .path("ideas");

            return jsonMapper
                    .readerForListOf(PostIdeaResponse.class)
                    .readValue(ideasNode);

        } catch (Exception e) {

            log.error(
                    "Failed to generate LinkedIn post ideas using Groq",
                    e
            );

            throw new RuntimeException(
                    "Failed to generate LinkedIn post ideas",
                    e
            );
        }
    }
}