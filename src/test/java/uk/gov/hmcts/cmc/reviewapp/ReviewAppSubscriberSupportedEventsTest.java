package uk.gov.hmcts.cmc.reviewapp;

import org.junit.Test;
import org.kohsuke.github.GHEvent;

import static org.assertj.core.api.Assertions.assertThat;

public class ReviewAppSubscriberSupportedEventsTest {

    private ReviewAppEventSubscriber subscriber = new ReviewAppEventSubscriber();

    @Test
    public void shouldSupportPullRequestEventOnly() {
        assertThat(subscriber.events()).containsOnly(GHEvent.PULL_REQUEST);
    }

}
