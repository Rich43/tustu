package com.efianalytics.reviews;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "submitUserReview", propOrder = {"uid", "email", "userId", "appName", "appEdition", "appVersion", "rating", "review"})
/* loaded from: efiaServicesClient.jar:com/efianalytics/reviews/SubmitUserReview.class */
public class SubmitUserReview {
    protected String uid;
    protected String email;

    @XmlElement(name = "user_id")
    protected String userId;

    @XmlElement(name = "app_name")
    protected String appName;

    @XmlElement(name = "app_edition")
    protected String appEdition;

    @XmlElement(name = "app_version")
    protected String appVersion;
    protected int rating;
    protected String review;

    public String getUid() {
        return this.uid;
    }

    public void setUid(String value) {
        this.uid = value;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String value) {
        this.email = value;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String value) {
        this.userId = value;
    }

    public String getAppName() {
        return this.appName;
    }

    public void setAppName(String value) {
        this.appName = value;
    }

    public String getAppEdition() {
        return this.appEdition;
    }

    public void setAppEdition(String value) {
        this.appEdition = value;
    }

    public String getAppVersion() {
        return this.appVersion;
    }

    public void setAppVersion(String value) {
        this.appVersion = value;
    }

    public int getRating() {
        return this.rating;
    }

    public void setRating(int value) {
        this.rating = value;
    }

    public String getReview() {
        return this.review;
    }

    public void setReview(String value) {
        this.review = value;
    }
}
