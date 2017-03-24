package uk.gov.hmcts.cmc.reviewapp;

import hudson.model.Job;
import hudson.model.ParametersAction;
import hudson.model.StringParameterValue;
import jenkins.model.Jenkins;
import jenkins.model.ParameterizedJobMixIn;
import org.kohsuke.github.GHEventPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class ReviewAppHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReviewAppHandler.class);

    private static final String CLOSED_EVENT = "closed";

    private AppShutdownJob appShutdownJob = new AppShutdownJob(Jenkins.getInstance());

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

    public void shutdownReviewAppFor(GHEventPayload.PullRequest pullRequest) {
        String reviewAppId = extractReviewAppId(pullRequest);
        shutdownReviewApp(reviewAppId);
    }

    public String extractReviewAppId(GHEventPayload.PullRequest pullRequest) {
        int pullRequestNumber = pullRequest.getNumber();
        if (pullRequestNumber <= 0) {
            throw new IllegalArgumentException("Pull request number should be a positive integer");
        }
        return String.format("PR-%d", pullRequestNumber);
    }

    public void shutdownReviewApp(String reviewAppId) {
        LOGGER.info("Shutting down {}", reviewAppId);
        ParametersAction paramsAction = new ParametersAction(Arrays.asList(
                new StringParameterValue("reviewAppName", reviewAppId)
        ));
        ParameterizedJobMixIn.scheduleBuild2(appShutdownJob.get(),0, paramsAction);
    }

}
