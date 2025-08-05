package com.efianalytics.serviceclient;

import com.efianalytics.reviews.UserReview;
import com.efianalytics.reviews.UserReviewService;

/* loaded from: efiaServicesClient.jar:com/efianalytics/serviceclient/ReviewActions.class */
public class ReviewActions {
    public static String submitUserReview(String uid, String email, String userId, String appName, String appEdition, String appVersion, int rating, String review) {
        UserReviewService service = new UserReviewService();
        UserReview port = service.getUserReviewPort();
        return port.submitUserReview(uid, email, userId, appName, appEdition, appVersion, rating, review);
    }
}
