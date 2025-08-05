package com.efianalytics.userprofile;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "authenticateSession", propOrder = {"sessionId"})
/* loaded from: efiaServicesClient.jar:com/efianalytics/userprofile/AuthenticateSession.class */
public class AuthenticateSession {
    protected String sessionId;

    public String getSessionId() {
        return this.sessionId;
    }

    public void setSessionId(String value) {
        this.sessionId = value;
    }
}
