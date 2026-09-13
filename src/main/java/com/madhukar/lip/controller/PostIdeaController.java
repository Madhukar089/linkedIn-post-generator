package com.madhukar.lip.controller;

import com.madhukar.lip.dto.PostIdeaRequest;
import com.madhukar.lip.dto.PostIdeaResponse;
import com.madhukar.lip.service.PostIdeaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/post-ideas")
@CrossOrigin(origins = "http://localhost:5173")
public class PostIdeaController {

    private final PostIdeaService postIdeaService;

    public PostIdeaController(PostIdeaService postIdeaService) {
        this.postIdeaService = postIdeaService;
    }

    @PostMapping
    public ResponseEntity<List<PostIdeaResponse>> generateIdeas(
            @RequestBody PostIdeaRequest request) {

        return ResponseEntity.ok(
                postIdeaService.generateIdeas(request.topic())
        );
    }
}