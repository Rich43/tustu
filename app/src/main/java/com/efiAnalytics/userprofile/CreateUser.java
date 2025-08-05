package com.efianalytics.userprofile;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "createUser", propOrder = {"userId", "email", "firstName", "lastName", "middleInitial"})
/* loaded from: efiaServicesClient.jar:com/efianalytics/userprofile/CreateUser.class */
public class CreateUser {
    protected String userId;
    protected String email;
    protected String firstName;
    protected String lastName;
    protected String middleInitial;

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String value) {
        this.userId = value;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String value) {
        this.email = value;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String value) {
        this.firstName = value;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String value) {
        this.lastName = value;
    }

    public String getMiddleInitial() {
        return this.middleInitial;
    }

    public void setMiddleInitial(String value) {
        this.middleInitial = value;
    }
}
