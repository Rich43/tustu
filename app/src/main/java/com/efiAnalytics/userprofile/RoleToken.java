package com.efianalytics.userprofile;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "roleToken", propOrder = {"accessAttributes", "roleTokenPK"})
/* loaded from: efiaServicesClient.jar:com/efianalytics/userprofile/RoleToken.class */
public class RoleToken {
    protected String accessAttributes;
    protected RoleTokenPK roleTokenPK;

    public String getAccessAttributes() {
        return this.accessAttributes;
    }

    public void setAccessAttributes(String value) {
        this.accessAttributes = value;
    }

    public RoleTokenPK getRoleTokenPK() {
        return this.roleTokenPK;
    }

    public void setRoleTokenPK(RoleTokenPK value) {
        this.roleTokenPK = value;
    }
}
