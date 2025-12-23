package com.example.gitryeokoffice.devvibe.infra.github;

import com.example.gitryeokoffice.devvibe.infra.github.dto.GitHubUserResponse;
import com.example.gitryeokoffice.devvibe.infra.github.dto.GraphQLRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Component
public class GithubApiClient {

    private final WebClient webClient;
    private static final String GITHUB_GRAPHQL_URL = "https://api.github.com/graphql";
    private static final String FULL_PROFILE_QUERY = """
            query GetDevVibeFullProfile($username: String!, $from: DateTime!, $to: DateTime!) {
              user(login: $username) {
                totalIssues: issues {
                  totalCount
                }
                totalPRs: pullRequests {
                  totalCount
                }
                calendarData: contributionsCollection {
                  contributionCalendar {
                    totalContributions
                    weeks {
                      contributionDays {
                        date
                        contributionCount
                      }
                    }
                  }
                }
                timeData: contributionsCollection(from: $from, to: $to) {
                  commitContributionsByRepository {
                    contributions(first: 100, orderBy: {field: OCCURRED_AT, direction: DESC}) {
                      nodes {
                        occurredAt
                      }
                    }
                  }
                }
              }
            }
            """;


    public GithubApiClient(WebClient.Builder webClientBuilder, @Value("${github.api.token}") String apiToken) {
        this.webClient = webClientBuilder
                .baseUrl(GITHUB_GRAPHQL_URL)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiToken)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public Mono<GitHubUserResponse> fetchUserProfile(String username) {
        ZonedDateTime to = ZonedDateTime.now();
        ZonedDateTime from = to.minusMonths(3);

        Map<String, Object> variables = Map.of(
                "username", username,
                "from", from.toInstant().toString(),
                "to", to.toInstant().toString()
        );

        GraphQLRequest request = new GraphQLRequest(FULL_PROFILE_QUERY, variables);

        return webClient.post()
                .bodyValue(request)
                .retrieve()
                .bodyToMono(GitHubUserResponse.class);
    }
}
