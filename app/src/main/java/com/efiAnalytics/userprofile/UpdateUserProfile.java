package com.efianalytics.userprofile;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "updateUserProfile", propOrder = {"userId", "password", "newPassword", "email", "sendEmail"})
/* loaded from: efiaServicesClient.jar:com/efianalytics/userprofile/UpdateUserProfile.class */
public class UpdateUserProfile {
    protected String userId;
    protected String password;
    protected String newPassword;
    protected String email;
    protected boolean sendEmail;

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

    public String getNewPassword() {
        return this.newPassword;
    }

    public void setNewPassword(String value) {
        this.newPassword = value;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String value) {
        this.email = value;
    }

    public boolean isSendEmail() {
        return this.sendEmail;
    }

    public void setSendEmail(boolean value) {
        this.sendEmail = value;
    }
}
