package com.example.gitryeokoffice.devvibe.presentation;

import com.example.gitryeokoffice.devvibe.application.DevVibeService;
import com.example.gitryeokoffice.devvibe.presentation.dto.DevVibeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/dev-vibe")
@RequiredArgsConstructor
public class DevVibeController {

    private final DevVibeService devVibeService;

    @GetMapping("/{username}")
    public Mono<ResponseEntity<DevVibeResponse>> getDevVibe(@PathVariable String username) {
        return devVibeService.analyze(username)
                .map(DevVibeResponse::from)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
