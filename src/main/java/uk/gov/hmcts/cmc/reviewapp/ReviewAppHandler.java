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

    private static final String SHUTDOWN_JOB_NAME = "cmc/review-app-pulveriser/master";
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

    public void shutdownReviewAppFor(GHEventPayload.PullRequest pullRequest) {
        String reviewAppId = pullRequest.getPullRequest().getHead().getLabel();
        shutdownReviewApp(reviewAppId);
    }

    public void shutdownReviewApp(String reviewAppId) {
        LOGGER.info("Shutting down {}", reviewAppId);
        Job job = (Job) Jenkins.getInstance().getItemByFullName(SHUTDOWN_JOB_NAME);
        if (job == null) {
            throw new RuntimeException("Cannot find job:" + SHUTDOWN_JOB_NAME);
        }
        ParametersAction paramsAction = new ParametersAction(Arrays.asList(
                new StringParameterValue("reviewAppName", reviewAppId)
        ));
        ParameterizedJobMixIn.scheduleBuild2(job,0, paramsAction);
    }

}
