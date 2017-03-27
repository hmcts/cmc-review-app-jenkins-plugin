package uk.gov.hmcts.cmc.reviewapp;

import com.cloudbees.jenkins.GitHubPushTrigger;
import hudson.Extension;
import hudson.model.Item;
import hudson.security.ACL;
import org.jenkinsci.plugins.github.extension.GHEventsSubscriber;
import org.jenkinsci.plugins.github.extension.GHSubscriberEvent;
import org.kohsuke.github.GHEvent;
import org.kohsuke.github.GHEventPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

import static com.google.common.collect.Sets.immutableEnumSet;
import static org.jenkinsci.plugins.github.util.JobInfoHelpers.withTrigger;
import static org.kohsuke.github.GHEvent.PULL_REQUEST;
import static uk.gov.hmcts.cmc.reviewapp.utils.PayloadParser.parse;

/**
 * Subscribes to the PULL_REQUEST event specifically the 'closed' action on the event
 *
 */
@Extension
@SuppressWarnings("unused")
public class ReviewAppEventSubscriber extends GHEventsSubscriber {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReviewAppEventSubscriber.class);

    private PullRequestClosedHandler handler = new PullRequestClosedHandler();

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
     * @param event only PUSH event
     */
    @Override
    protected void onEvent(final GHSubscriberEvent event) {
        GHEventPayload.PullRequest pullRequest = parse(event.getPayload());
        if (handler.supports(pullRequest)) {
            ACL.impersonate(ACL.SYSTEM, () ->
                    handler.shutdownReviewAppFor(pullRequest)
            );
        } else {
            LOGGER.debug("Unsupported Pull Request action {}", pullRequest.getAction());
        }
    }

}
