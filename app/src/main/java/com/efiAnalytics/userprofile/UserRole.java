package com.efianalytics.userprofile;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "userRole", propOrder = {"accessLevel", "parentRole", "roleDescription", "roleName"})
/* loaded from: efiaServicesClient.jar:com/efianalytics/userprofile/UserRole.class */
public class UserRole {
    protected Integer accessLevel;
    protected UserRole parentRole;
    protected String roleDescription;
    protected String roleName;

    public Integer getAccessLevel() {
        return this.accessLevel;
    }

    public void setAccessLevel(Integer value) {
        this.accessLevel = value;
    }

    public UserRole getParentRole() {
        return this.parentRole;
    }

    public void setParentRole(UserRole value) {
        this.parentRole = value;
    }

    public String getRoleDescription() {
        return this.roleDescription;
    }

    public void setRoleDescription(String value) {
        this.roleDescription = value;
    }

    public String getRoleName() {
        return this.roleName;
    }

    public void setRoleName(String value) {
        this.roleName = value;
    }
}
