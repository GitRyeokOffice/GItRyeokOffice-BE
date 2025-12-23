package com.example.gitryeokoffice.devvibe.infra.github.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GitHubUserResponse(Data data, List<GraphQLError> errors) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Data(UserNode user) {}
}
