package com.example.gitryeokoffice.devvibe.presentation.dto;

import com.example.gitryeokoffice.devvibe.domain.DevVibeResult;

public record DevVibeResponse(
        String vibeCode,
        String vibeTypeName,
        String summary,
        String explainJson
) {
    public static DevVibeResponse from(DevVibeResult result) {
        return new DevVibeResponse(
                result.getVibeCode(),
                result.getVibeTypeName(),
                result.getSummary(),
                result.getExplainJson()
        );
    }
}
