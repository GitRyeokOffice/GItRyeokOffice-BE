package com.example.gitryeokoffice.devvibe.infra.github.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.ZonedDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CommitNode(ZonedDateTime occurredAt) {
}
