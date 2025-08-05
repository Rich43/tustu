package com.efianalytics.userprofile;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "resetPassword", propOrder = {"userIdOrEmail"})
/* loaded from: efiaServicesClient.jar:com/efianalytics/userprofile/ResetPassword.class */
public class ResetPassword {
    protected String userIdOrEmail;

    public String getUserIdOrEmail() {
        return this.userIdOrEmail;
    }

    public void setUserIdOrEmail(String value) {
        this.userIdOrEmail = value;
    }
}
