package uk.gov.hmcts.cmc.reviewapp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kohsuke.github.GHEventPayload;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ReviewAppHandlerExtractAppIdTest {

    @Mock
    private GHEventPayload.PullRequest pullRequest;

    private ReviewAppHandler handler = new ReviewAppHandler();

    @Test
    public void shouldReturnExpectedIdentifierForPullRequestNumber123() {
        when(pullRequest.getNumber()).thenReturn(123);
        assertThat(handler.extractReviewAppId(pullRequest)).isEqualTo("PR-123");
    }

    @Test
    public void shouldReturnExpectedIdentifierForPullRequestNumber2() {
        when(pullRequest.getNumber()).thenReturn(2);
        assertThat(handler.extractReviewAppId(pullRequest)).isEqualTo("PR-2");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentWhenPullRequestNumberIsZero() {
        when(pullRequest.getNumber()).thenReturn(0);
        handler.extractReviewAppId(pullRequest);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentWhenPullRequestNumberIsNegative() {
        when(pullRequest.getNumber()).thenReturn(-12);
        handler.extractReviewAppId(pullRequest);
    }

}
