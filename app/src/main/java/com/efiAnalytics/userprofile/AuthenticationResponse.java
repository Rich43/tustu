package com.efianalytics.userprofile;

import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "authenticationResponse", propOrder = {"authenticated", "authenticationServer", "expirationTime", "sessionId", "userId"})
/* loaded from: efiaServicesClient.jar:com/efianalytics/userprofile/AuthenticationResponse.class */
public class AuthenticationResponse {
    protected Boolean authenticated;
    protected String authenticationServer;

    @XmlSchemaType(name = SchemaSymbols.ATTVAL_DATETIME)
    protected XMLGregorianCalendar expirationTime;
    protected String sessionId;
    protected String userId;

    public Boolean isAuthenticated() {
        return this.authenticated;
    }

    public void setAuthenticated(Boolean value) {
        this.authenticated = value;
    }

    public String getAuthenticationServer() {
        return this.authenticationServer;
    }

    public void setAuthenticationServer(String value) {
        this.authenticationServer = value;
    }

    public XMLGregorianCalendar getExpirationTime() {
        return this.expirationTime;
    }

    public void setExpirationTime(XMLGregorianCalendar value) {
        this.expirationTime = value;
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public void setSessionId(String value) {
        this.sessionId = value;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String value) {
        this.userId = value;
    }
}
