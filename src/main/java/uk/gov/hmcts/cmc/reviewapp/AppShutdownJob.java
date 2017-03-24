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
        String shutdownJobName = shutdownJobName();
        return Optional.ofNullable(
                (Job) jenkins.getItemByFullName(shutdownJobName)
        ).orElseThrow(
                () -> new IllegalStateException("Cannot find job: " + shutdownJobName)
        );
    }

    private String shutdownJobName() {
        System.out.println(System.getenv());
        String shutdownJobName = System.getenv(SHUTDOWN_JOB_VARIABLE_NAME);
        if (shutdownJobName == null || shutdownJobName.isEmpty()) {
            return SHUTDOWN_JOB_DEFAULT_NAME;
        } else {
            return shutdownJobName;
        }
    }

}
