package uk.gov.hmcts.cmc.reviewapp;

import org.kohsuke.github.GHEventPayload;

public class ReviewAppHandler {

    private static final String CLOSED_EVENT = "closed";

    public boolean supports(GHEventPayload.PullRequest pullRequest) {
        return supports(pullRequest.getAction());
    }

    public boolean supports(String subscriberEventAction) {
        return CLOSED_EVENT.equals(subscriberEventAction);
    }

}
