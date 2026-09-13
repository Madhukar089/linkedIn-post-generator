package com.madhukar.lip.service;

import com.madhukar.lip.dto.PostIdeaResponse;
import com.madhukar.lip.generator.IdeaGenerator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostIdeaService {

    private final IdeaGenerator ideaGenerator;

    public PostIdeaService(IdeaGenerator ideaGenerator) {
        this.ideaGenerator = ideaGenerator;
    }

    public List<PostIdeaResponse> generateIdeas(String topic) {
        return ideaGenerator.generateIdeas(topic);
    }
}
