package com.efianalytics.userprofile;

import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "roleTokenPK", propOrder = {"roleName", SchemaSymbols.ATTVAL_TOKEN})
/* loaded from: efiaServicesClient.jar:com/efianalytics/userprofile/RoleTokenPK.class */
public class RoleTokenPK {
    protected String roleName;
    protected String token;

    public String getRoleName() {
        return this.roleName;
    }

    public void setRoleName(String value) {
        this.roleName = value;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String value) {
        this.token = value;
    }
}
