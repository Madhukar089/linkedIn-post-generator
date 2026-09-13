package com.madhukar.lip.generator;

import com.madhukar.lip.dto.PostIdeaResponse;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConditionalOnProperty(
        name = "ai.provider",
        havingValue = "mock"
)
public class MockIdeaGenerator implements IdeaGenerator {

    @Override
    public List<PostIdeaResponse> generateIdeas(String topic) {

        return List.of(
                new PostIdeaResponse(
                        1L,
                        "What I learned about " + topic,
                        "A personal reflection on what I learned while working with " + topic + "."
                ),
                new PostIdeaResponse(
                        2L,
                        "3 things " + topic + " taught me",
                        "Three practical lessons and takeaways from working with " + topic + "."
                ),
                new PostIdeaResponse(
                        3L,
                        "My experience with " + topic,
                        "A developer-focused story about solving problems and gaining experience with " + topic + "."
                )
        );
    }
}
