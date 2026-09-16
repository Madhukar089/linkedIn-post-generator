package com.madhukar.lip.controller;

import com.madhukar.lip.dto.PostIdeaRequest;
import com.madhukar.lip.dto.PostIdeaResponse;
import com.madhukar.lip.service.PostIdeaService;
import com.madhukar.lip.service.RateLimitService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/post-ideas")
@CrossOrigin(origins = "http://localhost:5173")
public class PostIdeaController {

    private final PostIdeaService postIdeaService;
    private final RateLimitService rateLimitService;

    public PostIdeaController(
            PostIdeaService postIdeaService,
            RateLimitService rateLimitService) {

        this.postIdeaService = postIdeaService;
        this.rateLimitService = rateLimitService;
    }

    @PostMapping
    public ResponseEntity<List<PostIdeaResponse>> generateIdeas(
            HttpServletRequest request,
            @RequestBody PostIdeaRequest body) {

        String clientIp = request.getHeader("X-Real-IP");

        System.out.println("X-Real-IP: " + request.getHeader("X-Real-IP"));
        System.out.println("Remote Address: " + request.getRemoteAddr());
        System.out.println("X-Forwarded-For: " + request.getHeader("X-Forwarded-For"));
        System.out.println("Rate Limit IP: " + clientIp);

        if (clientIp == null || clientIp.isBlank()) {
            clientIp = request.getRemoteAddr();
        }

        if (!rateLimitService.isAllowed(clientIp)) {
            return ResponseEntity
                    .status(HttpStatus.TOO_MANY_REQUESTS)
                    .build();
        }

        return ResponseEntity.ok(
                postIdeaService.generateIdeas(body.topic())
        );
    }
}