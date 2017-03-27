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

public class PullRequestClosedHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(PullRequestClosedHandler.class);

    private Job appShutdownJob = AppShutdownJob.get(Jenkins.getInstance());

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
