package com.example.gitryeokoffice.devvibe.infra.github.dto;

import java.util.Map;

public record GraphQLRequest(String query, Map<String, Object> variables) {
}
