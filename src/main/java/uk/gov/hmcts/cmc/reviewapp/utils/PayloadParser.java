package uk.gov.hmcts.cmc.reviewapp.utils;

import org.kohsuke.github.GHEventPayload;
import org.kohsuke.github.GitHub;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringReader;

public class PayloadParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(PayloadParser.class);

    public static GHEventPayload.PullRequest parse(String payload) {
        try {
            return GitHub.offline().parseEventPayload(new StringReader(payload), GHEventPayload.PullRequest.class);
        } catch (IOException e) {
            LOGGER.error("Received malformed PullRequestEvent: " + payload, e);
            throw new RuntimeException(e);
        }
    }

}
