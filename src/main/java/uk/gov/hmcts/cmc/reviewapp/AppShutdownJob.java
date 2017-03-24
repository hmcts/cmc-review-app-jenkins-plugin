package uk.gov.hmcts.cmc.reviewapp;

import hudson.model.Job;
import jenkins.model.Jenkins;

import java.util.Optional;

public class AppShutdownJob {

    private static final String SHUTDOWN_JOB_NAME = "cmc/review-app-pulveriser/master";

    private Jenkins jenkins;

    public AppShutdownJob(Jenkins jenkins) {
        this.jenkins = jenkins;
    }

    public Job get() {
        return Optional.ofNullable(
                (Job) jenkins.getItemByFullName(SHUTDOWN_JOB_NAME)
        ).orElseThrow(
                () -> new IllegalStateException("Cannot find job: " + SHUTDOWN_JOB_NAME)
        );
    }

}
