package uk.gov.hmcts.cmc.reviewapp;

import org.kohsuke.github.GHEventPayload;

public class ReviewAppHandler {

    private static final String CLOSED_EVENT = "closed";

    public boolean supports(GHEventPayload.PullRequest pullRequest) {
        return supports(pullRequest.getAction());
    }

    /**
     * @param subscriberEventAction Pull Request action name
     * @return true if action is supported, false otherwise
     * @see <a href="https://developer.github.com/v3/activity/events/types/#pullrequestevent">GitHub API docs</a>
     */
    public boolean supports(String subscriberEventAction) {
        return CLOSED_EVENT.equals(subscriberEventAction);
    }

}
