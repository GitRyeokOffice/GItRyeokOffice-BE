//package com.example.gitryeokoffice.devvibe.application;
//
//import com.example.gitryeokoffice.devvibe.domain.DevVibeResult;
//import com.example.gitryeokoffice.devvibe.domain.DevVibeResultRepository;
//import com.example.gitryeokoffice.devvibe.infra.github.GithubApiClient;
//import com.example.gitryeokoffice.devvibe.infra.github.dto.*;
//import com.example.gitryeokoffice.user.domain.User;
//import com.example.gitryeokoffice.user.domain.UserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import reactor.core.publisher.Mono;
//
//import java.time.ZonedDateTime;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//class DevVibeServiceTest {
//
//    @Mock
//    private GithubApiClient githubApiClient;
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private DevVibeResultRepository devVibeResultRepository;
//
//    @InjectMocks
//    private DevVibeService devVibeService;
//
//    private User testUser;
//
//    @BeforeEach
//    void setUp() {
//        testUser = User.create("testuser", "test@test.com", "password", "testnick", null, null, null, 0, false);
//    }
//
//    @Test
//    @DisplayName("PSM (계획-지속-아침) 타입 분석 테스트")
//    void analyze_PSM_Type() {
//        // given
//        when(userRepository.findByGithubLogin(anyString())).thenReturn(Optional.of(testUser));
//        GitHubUserResponse mockResponse = createMockResponse(100, 50, 200, 20); // P, S, M
//        when(githubApiClient.fetchUserProfile(anyString())).thenReturn(Mono.just(mockResponse));
//        when(devVibeResultRepository.findByUser_Id(anyLong())).thenReturn(Optional.empty());
//        when(devVibeResultRepository.save(any(DevVibeResult.class))).thenAnswer(invocation -> invocation.getArgument(0));
//
//
//        // when
//        Mono<DevVibeResult> resultMono = devVibeService.analyze("testuser");
//
//        // then
//        DevVibeResult result = resultMono.block();
//        assertThat(result).isNotNull();
//        assertThat(result.getVibeCode()).isEqualTo("PSM");
//    }
//2
//    @Test
//    @DisplayName("IFN (즉흥-몰입-저녁) 타입 분석 테스트")
//    void analyze_IFN_Type() {
//        // given
//        when(userRepository.findByGithubLogin(anyString())).thenReturn(Optional.of(testUser));
//        GitHubUserResponse mockResponse = createMockResponse(10, 50, 100, 80); // I, F, N
//        when(githubApiClient.fetchUserProfile(anyString())).thenReturn(Mono.just(mockResponse));
//        when(devVibeResultRepository.findByUser_Id(anyLong())).thenReturn(Optional.empty());
//        when(devVibeResultRepository.save(any(DevVibeResult.class))).thenAnswer(invocation -> invocation.getArgument(0));
//
//        // when
//        Mono<DevVibeResult> resultMono = devVibeService.analyze("testuser");
//
//        // then
//        DevVibeResult result = resultMono.block();
//        assertThat(result).isNotNull();
//        assertThat(result.getVibeCode()).isEqualTo("IFN");
//    }
//
//    private GitHubUserResponse createMockResponse(int issueCount, int prCount, int contributionDays, int nightCommits) {
//        // Axis A: Work Style
//        var totalIssues = new CountNode(issueCount);
//        var totalPRs = new CountNode(prCount);
//
//        // Axis B: Activity Pattern
//        List<ContributionDayNode> days = new ArrayList<>();
//        for (int i = 0; i < 365; i++) {
//            days.add(new ContributionDayNode("2025-01-01", i < contributionDays ? 1 : 0));
//        }
//        var weeks = List.of(new WeekNode(days));
//        var calendar = new ContributionCalendarNode(contributionDays, weeks);
//
//
//        // Axis C: Time Of Day
//        List<CommitNode> commits = new ArrayList<>();
//        for (int i = 0; i < 100; i++) {
//            ZonedDateTime time = i < nightCommits ? ZonedDateTime.parse("2025-12-24T20:00:00+09:00[Asia/Seoul]") : ZonedDateTime.parse("2025-12-24T10:00:00+09:00[Asia/Seoul]");
//            commits.add(new CommitNode(time));
//        }
//        var commitContributions = new CommitContributionsNode(commits);
//        var commitRepo = new CommitContributionsByRepositoryNode(commitContributions);
//
//        var contributionsCollectionCalendar = new ContributionsCollectionNode(calendar, null);
//        var contributionsCollectionTime = new ContributionsCollectionNode(null, List.of(commitRepo));
//
//
//        var userNode = new UserNode(totalIssues, totalPRs, contributionsCollectionCalendar, contributionsCollectionTime);
//        var data = new GitHubUserResponse.Data(userNode);
//        return new GitHubUserResponse(data, new ArrayList<>());
//    }
//}
