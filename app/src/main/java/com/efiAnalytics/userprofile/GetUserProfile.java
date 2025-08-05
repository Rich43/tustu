package com.efianalytics.userprofile;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getUserProfile", propOrder = {"userId", "password"})
/* loaded from: efiaServicesClient.jar:com/efianalytics/userprofile/GetUserProfile.class */
public class GetUserProfile {
    protected String userId;
    protected String password;

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String value) {
        this.userId = value;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String value) {
        this.password = value;
    }
}
