package uk.gov.hmcts.cmc.reviewapp;

public class ReviewAppHandler {

    private static final String CLOSED_EVENT = "closed";

    public boolean supports(String subscriberEventAction) {
        return CLOSED_EVENT.equals(subscriberEventAction);
    }

}
