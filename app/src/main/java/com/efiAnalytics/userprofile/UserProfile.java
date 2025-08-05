package com.efianalytics.userprofile;

import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "userProfile", propOrder = {"email", "firstName", "hierarchyId", "lastLogon", "lastName", "middleInitial", "password", "sendEmail", "userId", "userRole"})
/* loaded from: efiaServicesClient.jar:com/efianalytics/userprofile/UserProfile.class */
public class UserProfile {
    protected String email;
    protected String firstName;
    protected String hierarchyId;

    @XmlSchemaType(name = SchemaSymbols.ATTVAL_DATETIME)
    protected XMLGregorianCalendar lastLogon;
    protected String lastName;
    protected String middleInitial;
    protected String password;
    protected String sendEmail;
    protected String userId;
    protected UserRole userRole;

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

    public String getHierarchyId() {
        return this.hierarchyId;
    }

    public void setHierarchyId(String value) {
        this.hierarchyId = value;
    }

    public XMLGregorianCalendar getLastLogon() {
        return this.lastLogon;
    }

    public void setLastLogon(XMLGregorianCalendar value) {
        this.lastLogon = value;
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

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String value) {
        this.password = value;
    }

    public String getSendEmail() {
        return this.sendEmail;
    }

    public void setSendEmail(String value) {
        this.sendEmail = value;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String value) {
        this.userId = value;
    }

    public UserRole getUserRole() {
        return this.userRole;
    }

    public void setUserRole(UserRole value) {
        this.userRole = value;
    }
}
