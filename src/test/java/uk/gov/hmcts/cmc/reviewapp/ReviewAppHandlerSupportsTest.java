package uk.gov.hmcts.cmc.reviewapp;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ReviewAppHandlerSupportsTest {

    private String[] OTHER_PULL_REQUEST_EVENTS = {
            "assigned", "unassigned", "review_requested", "review_request_removed",
            "labeled", "unlabeled", "opened", "edited", "reopened"};

    private ReviewAppHandler handler;

    @Before
    public void beforeEachTest() {
        handler = new ReviewAppHandler();
    }

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
        assertThat(handler.supports(null)).isFalse();
    }

}
