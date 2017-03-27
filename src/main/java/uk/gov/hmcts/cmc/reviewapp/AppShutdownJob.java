package uk.gov.hmcts.cmc.reviewapp;

import hudson.model.Job;
import jenkins.model.Jenkins;

import java.util.Optional;

public class AppShutdownJob {

    private static final String SHUTDOWN_JOB_VARIABLE_NAME = "CMC_REVIEW_APP_SHUTDOWN_JOB_NAME";
    private static final String SHUTDOWN_JOB_DEFAULT_NAME = "cmc/review-app-pulveriser/master";

    private Jenkins jenkins;

    public AppShutdownJob(Jenkins jenkins) {
        this.jenkins = jenkins;
    }

    public Job get() {
        String shutdownJobName =
            Optional
                .ofNullable(System.getenv(SHUTDOWN_JOB_VARIABLE_NAME))
                .orElse(SHUTDOWN_JOB_DEFAULT_NAME);

        return Optional
            .ofNullable(jenkins.getItemByFullName(shutdownJobName, Job.class))
            .orElseThrow(() -> new IllegalStateException("Cannot find job: " + shutdownJobName));
    }
}
