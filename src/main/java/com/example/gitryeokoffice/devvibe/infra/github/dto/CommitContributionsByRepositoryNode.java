package com.example.gitryeokoffice.devvibe.infra.github.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CommitContributionsByRepositoryNode(
        @JsonProperty("contributions") CommitContributionsNode contributions
) {
}
