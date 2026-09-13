package com.madhukar.lip.generator;

import com.madhukar.lip.dto.PostIdeaResponse;

import java.util.List;

public interface IdeaGenerator {

    List<PostIdeaResponse> generateIdeas(String topic);
}