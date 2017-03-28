package uk.gov.hmcts.cmc.reviewapp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kohsuke.github.GHEventPayload;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import uk.gov.hmcts.cmc.reviewapp.utils.ReviewAppHelper;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ReviewAppHandlerExtractAppIdTest {

    @Mock
    private GHEventPayload.PullRequest pullRequest;

    @Test
    public void shouldReturnExpectedIdentifierForPullRequestNumber123() {
        when(pullRequest.getNumber()).thenReturn(123);
        assertThat(ReviewAppHelper.getIdFrom(pullRequest)).isEqualTo("PR-123");
    }

    @Test
    public void shouldReturnExpectedIdentifierForPullRequestNumber2() {
        when(pullRequest.getNumber()).thenReturn(2);
        assertThat(ReviewAppHelper.getIdFrom(pullRequest)).isEqualTo("PR-2");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentWhenPullRequestNumberIsZero() {
        when(pullRequest.getNumber()).thenReturn(0);
        ReviewAppHelper.getIdFrom(pullRequest);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentWhenPullRequestNumberIsNegative() {
        when(pullRequest.getNumber()).thenReturn(-12);
        ReviewAppHelper.getIdFrom(pullRequest);
    }

}
