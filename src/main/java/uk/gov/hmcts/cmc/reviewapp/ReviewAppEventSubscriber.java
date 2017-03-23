package uk.gov.hmcts.cmc.reviewapp;

import com.cloudbees.jenkins.GitHubPushTrigger;
import hudson.Extension;
import hudson.model.Item;

import java.io.IOException;
import java.io.StringReader;

import hudson.model.Job;
import hudson.model.ParametersAction;
import hudson.model.StringParameterValue;
import hudson.security.ACL;
import jenkins.model.Jenkins;
import jenkins.model.ParameterizedJobMixIn;
import org.jenkinsci.plugins.github.extension.GHSubscriberEvent;
import org.jenkinsci.plugins.github.extension.GHEventsSubscriber;
import org.jenkinsci.plugins.github.webhook.subscriber.DefaultPushGHEventSubscriber;
import org.kohsuke.github.GHEvent;
import org.kohsuke.github.GHEventPayload;
import org.kohsuke.github.GitHub;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Set;

import static com.google.common.collect.Sets.immutableEnumSet;
import static org.jenkinsci.plugins.github.util.JobInfoHelpers.triggerFrom;
import static org.jenkinsci.plugins.github.util.JobInfoHelpers.withTrigger;
import static org.kohsuke.github.GHEvent.PULL_REQUEST;

/**
 * By default this plugin interested in push events only when job uses {@link GitHubPushTrigger}
 *
 * @author lanwen (Merkushev Kirill)
 * @since 1.12.0
 */
@Extension
@SuppressWarnings("unused")
public class ReviewAppEventSubscriber extends GHEventsSubscriber {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultPushGHEventSubscriber.class);

    @Override
    protected boolean isApplicable(Item project) {
        return withTrigger(GitHubPushTrigger.class).apply(project);
    }

    @Override
    protected Set<GHEvent> events() {
        return immutableEnumSet(PULL_REQUEST);
    }

    /**
     * Calls {@link GitHubPushTrigger} in all projects to handle this hook
     *
     * @param event   only PUSH event
     */
    @Override
    protected void onEvent(final GHSubscriberEvent event) {
        GHEventPayload.PullRequest pullRequest = parse(event);
        String action = pullRequest.getAction();
        String label = pullRequest.getPullRequest().getHead().getLabel();
        LOGGER.info("PR {} changed to {}", label, action);
        shutdownReviewApp(label);

    }

    private void shutdownReviewApp(String reviewAppId) {
        ACL.impersonate(ACL.SYSTEM, () -> {
            String jobName = "cmc/review-app-pulveriser/master";
            Job job = (Job) Jenkins.getInstance().getItemByFullName(jobName);
            if (job == null) {
                throw new RuntimeException("Cannot find job:" + jobName);
            }
            ParametersAction paramsAction = new ParametersAction(Arrays.asList(
                    new StringParameterValue("reviewAppName", reviewAppId)
            ));
            ParameterizedJobMixIn.scheduleBuild2(job,0, paramsAction);
        });
    }

    private GHEventPayload.PullRequest parse(GHSubscriberEvent event) {
        try {
            return GitHub.offline().parseEventPayload(new StringReader(event.getPayload()), GHEventPayload.PullRequest.class);
        } catch (IOException e) {
            LOGGER.error("Received malformed PullRequestEvent: " + event.getPayload(), e);
            throw new RuntimeException(e);
        }
    }
}
