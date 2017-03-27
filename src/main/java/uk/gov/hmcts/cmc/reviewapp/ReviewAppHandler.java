package uk.gov.hmcts.cmc.reviewapp;

import hudson.model.Job;
import hudson.model.ParametersAction;
import hudson.model.StringParameterValue;
import jenkins.model.Jenkins;
import jenkins.model.ParameterizedJobMixIn;
import org.kohsuke.github.GHEventPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.hmcts.cmc.reviewapp.utils.AppShutdownJob;
import uk.gov.hmcts.cmc.reviewapp.utils.ReviewAppHelper;

public class ReviewAppHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReviewAppHandler.class);

    private static final String CLOSED_EVENT = "closed";

    private Job appShutdownJob = AppShutdownJob.get(Jenkins.getInstance());

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
        shutdownReviewApp(ReviewAppHelper.getIdFrom(pullRequest));
    }

    public void shutdownReviewApp(String reviewAppId) {
        LOGGER.info("Shutting down {}", reviewAppId);
        ParameterizedJobMixIn
            .scheduleBuild2(
                appShutdownJob,
                0,
                new ParametersAction(
                        new StringParameterValue("reviewAppName", reviewAppId)
                )
            );
    }

}
