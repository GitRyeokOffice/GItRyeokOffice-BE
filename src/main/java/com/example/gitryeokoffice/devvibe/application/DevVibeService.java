package com.example.gitryeokoffice.devvibe.application;

import com.example.gitryeokoffice.devvibe.domain.ActivityPattern;
import com.example.gitryeokoffice.devvibe.domain.DevVibeResult;
import com.example.gitryeokoffice.devvibe.domain.DevVibeResultRepository;
import com.example.gitryeokoffice.devvibe.domain.TimeOfDay;
import com.example.gitryeokoffice.devvibe.domain.WorkStyle;
import com.example.gitryeokoffice.devvibe.exception.DevVibeErrorCode;
import com.example.gitryeokoffice.devvibe.exception.DevVibeException;
import com.example.gitryeokoffice.devvibe.infra.github.GithubApiClient;
import com.example.gitryeokoffice.devvibe.infra.github.dto.GitHubUserResponse;
import com.example.gitryeokoffice.devvibe.infra.github.dto.WeekNode;
import com.example.gitryeokoffice.user.domain.User;
import com.example.gitryeokoffice.user.domain.UserRepository;
import com.example.gitryeokoffice.user.exception.UserErrorCode;
import com.example.gitryeokoffice.user.exception.UserException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class DevVibeService {

    private final GithubApiClient githubApiClient;
    private final UserRepository userRepository;
    private final DevVibeResultRepository devVibeResultRepository;

    @Transactional
    public Mono<DevVibeResult> analyze(String githubUsername) {
        User user = userRepository.findByGithubLogin(githubUsername)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        return githubApiClient.fetchUserProfile(githubUsername)
                .flatMap(response -> {
                    if (response.errors() != null && !response.errors().isEmpty()) {
                        String errorMessage = response.errors().getFirst().message();
                        log.error("GitHub GraphQL API Error: {}", errorMessage);
                        return Mono.error(new DevVibeException(DevVibeErrorCode.GITHUB_API_FAIL));
                    }
                    if (response.data() == null || response.data().user() == null) {
                        return Mono.error(new DevVibeException(DevVibeErrorCode.GITHUB_USER_NOT_FOUND));
                    }
                    WorkStyle workStyle = analyzeWorkStyle(response);
                    ActivityPattern activityPattern = analyzeActivityPattern(response);
                    TimeOfDay timeOfDay = analyzeTimeOfDay(response);

                    // TODO: AI 한 줄 소개 및 분석 근거 데이터(JSON) 생성 로직 추가
                    String summary = "AI 한 줄 소개 (예정)";
                    String json = "{}";

                    DevVibeResult result = devVibeResultRepository.findByUser_Id(user.getId())
                            .map(existingResult -> {
                                existingResult.update(workStyle, activityPattern, timeOfDay, summary, json);
                                return existingResult;
                            })
                            .orElseGet(() -> DevVibeResult.create(user, workStyle, activityPattern, timeOfDay, summary, json));

                    DevVibeResult savedResult = devVibeResultRepository.save(result);
                    return Mono.just(savedResult);
                })
                .onErrorMap(WebClientResponseException.class, e -> {
                    log.error("GitHub API HTTP Error: status={}, body={}", e.getRawStatusCode(), e.getResponseBodyAsString(), e);
                    return new DevVibeException(DevVibeErrorCode.GITHUB_API_FAIL);
                });
    }

    private WorkStyle analyzeWorkStyle(GitHubUserResponse response) {
        var userNode = response.data().user();
        int issueCount = (userNode.totalIssues() != null) ? userNode.totalIssues().totalCount() : 0;
        int prCount = (userNode.totalPRs() != null) ? userNode.totalPRs().totalCount() : 0;

        if (prCount == 0) {
            return WorkStyle.PLANNED; // PR이 없으면 계획형으로 간주 (분석 불가)
        }

        double ratio = (double) issueCount / prCount;
        return ratio >= 0.5 ? WorkStyle.PLANNED : WorkStyle.IMPROVISATION;
    }

    private ActivityPattern analyzeActivityPattern(GitHubUserResponse response) {
        var calendarData = response.data().user().calendarData();
        if (calendarData == null || calendarData.contributionCalendar() == null || calendarData.contributionCalendar().weeks() == null) {
            return ActivityPattern.FOCUS;
        }

        List<WeekNode> weeks = calendarData.contributionCalendar().weeks();
        long totalDays = weeks.stream()
                .filter(Objects::nonNull)
                .mapToLong(week -> week.contributionDays() != null ? week.contributionDays().size() : 0)
                .sum();

        if (totalDays == 0) {
            return ActivityPattern.FOCUS;
        }

        long contributionDays = weeks.stream()
                .filter(Objects::nonNull)
                .flatMap(week -> week.contributionDays() != null ? week.contributionDays().stream() : Stream.empty())
                .filter(Objects::nonNull)
                .filter(day -> day.contributionCount() > 0)
                .count();

        double ratio = (double) contributionDays / totalDays;
        return ratio >= 0.3 ? ActivityPattern.STEADY : ActivityPattern.FOCUS;
    }

    private TimeOfDay analyzeTimeOfDay(GitHubUserResponse response) {
        var timeData = response.data().user().timeData();
        if (timeData == null || timeData.commitContributionsByRepository() == null || timeData.commitContributionsByRepository().isEmpty()) {
            return TimeOfDay.MORNING; // 데이터 없으면 기본값
        }

        List<ZonedDateTime> commitTimestamps = timeData.commitContributionsByRepository().stream()
                .filter(Objects::nonNull)
                .map(repoContributions -> repoContributions.contributions())
                .filter(contributions -> contributions != null && contributions.nodes() != null)
                .flatMap(contributions -> contributions.nodes().stream())
                .filter(Objects::nonNull)
                .map(commitNode -> commitNode.occurredAt())
                .filter(Objects::nonNull)
                .map(occurredAt -> occurredAt.withZoneSameInstant(ZoneId.of("Asia/Seoul")))
                .toList();

        long totalCommits = commitTimestamps.size();
        if (totalCommits == 0) {
            return TimeOfDay.MORNING;
        }

        long nightCommits = commitTimestamps.stream()
                .filter(timestamp -> {
                    int hour = timestamp.getHour();
                    return hour >= 18 || hour < 6;
                })
                .count();

        double ratio = (double) nightCommits / totalCommits;
        return ratio >= 0.5 ? TimeOfDay.NIGHT : TimeOfDay.MORNING;
    }
}
