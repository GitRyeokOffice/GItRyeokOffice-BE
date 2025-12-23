package com.example.gitryeokoffice.devvibe.infra.github.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GraphQLError(
        String message
) {
}
