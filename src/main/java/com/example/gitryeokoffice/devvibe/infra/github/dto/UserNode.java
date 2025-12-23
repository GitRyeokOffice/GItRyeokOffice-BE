package com.example.gitryeokoffice.devvibe.infra.github.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record UserNode(
        @JsonProperty("totalIssues") CountNode totalIssues,
        @JsonProperty("totalPRs") CountNode totalPRs,
        @JsonProperty("calendarData") ContributionsCollectionNode calendarData,
        @JsonProperty("timeData") ContributionsCollectionNode timeData
) {
}
