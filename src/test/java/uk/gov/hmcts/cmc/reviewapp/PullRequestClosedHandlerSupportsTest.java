package uk.gov.hmcts.cmc.reviewapp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kohsuke.github.GHEventPayload;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class PullRequestClosedHandlerSupportsTest {

    private String[] OTHER_PULL_REQUEST_EVENTS = {
            "assigned", "unassigned", "review_requested", "review_request_removed",
            "labeled", "unlabeled", "opened", "edited", "reopened"};

    @Mock
    private GHEventPayload.PullRequest pullRequest;

    @Spy
    private PullRequestClosedHandler handler;

    @Test
    public void shouldSupportClosedEvent() {
        assertThat(handler.supports("closed")).isTrue();
    }

    @Test
    public void shouldNotSupportOtherEvents() {
        for (String event : OTHER_PULL_REQUEST_EVENTS) {
            assertThat(handler.supports(event)).isFalse();
        }
    }

    @Test
    public void shouldNotSupportEmptyEvent() {
        assertThat(handler.supports("")).isFalse();
    }

    @Test
    public void shouldNotSupportNullEvent() {
        assertThat(handler.supports((String)null)).isFalse();
    }

    @Test
    public void passingTheObjectShouldDelegateToStringMethod() {
        when(pullRequest.getAction()).thenReturn("closed");
        handler.supports(pullRequest);
        verify(handler).supports("closed");
    }

}
