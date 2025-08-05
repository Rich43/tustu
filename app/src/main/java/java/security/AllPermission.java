package java.security;

/* loaded from: rt.jar:java/security/AllPermission.class */
public final class AllPermission extends Permission {
    private static final long serialVersionUID = -2916474571451318075L;

    public AllPermission() {
        super("<all permissions>");
    }

    public AllPermission(String str, String str2) {
        this();
    }

    @Override // java.security.Permission
    public boolean implies(Permission permission) {
        return true;
    }

    @Override // java.security.Permission
    public boolean equals(Object obj) {
        return obj instanceof AllPermission;
    }

    @Override // java.security.Permission
    public int hashCode() {
        return 1;
    }

    @Override // java.security.Permission
    public String getActions() {
        return "<all actions>";
    }

    @Override // java.security.Permission
    public PermissionCollection newPermissionCollection() {
        return new AllPermissionCollection();
    }
}
