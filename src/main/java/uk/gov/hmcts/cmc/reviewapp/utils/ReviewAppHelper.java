package uk.gov.hmcts.cmc.reviewapp.utils;

import org.kohsuke.github.GHEventPayload.PullRequest;

public class ReviewAppHelper {

    public static final String PREFIX = "PR-";

    public static String getIdFrom(PullRequest pullRequest) {
        int number = pullRequest.getNumber();
        if (number <= 0) {
            throw new IllegalArgumentException("Pull request number should be greater than zero");
        } else {
            return PREFIX + number;
        }
    }
}
