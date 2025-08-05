package com.efianalytics.userprofile;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "isUserIdInUse", propOrder = {"userId"})
/* loaded from: efiaServicesClient.jar:com/efianalytics/userprofile/IsUserIdInUse.class */
public class IsUserIdInUse {
    protected String userId;

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String value) {
        this.userId = value;
    }
}
