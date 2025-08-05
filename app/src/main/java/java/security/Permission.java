package java.security;

import java.io.Serializable;

/* loaded from: rt.jar:java/security/Permission.class */
public abstract class Permission implements Guard, Serializable {
    private static final long serialVersionUID = -5636570222231596674L;
    private String name;

    public abstract boolean implies(Permission permission);

    public abstract boolean equals(Object obj);

    public abstract int hashCode();

    public abstract String getActions();

    public Permission(String str) {
        this.name = str;
    }

    @Override // java.security.Guard
    public void checkGuard(Object obj) throws SecurityException {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(this);
        }
    }

    public final String getName() {
        return this.name;
    }

    public PermissionCollection newPermissionCollection() {
        return null;
    }

    public String toString() {
        String actions = getActions();
        if (actions == null || actions.length() == 0) {
            return "(\"" + getClass().getName() + "\" \"" + this.name + "\")";
        }
        return "(\"" + getClass().getName() + "\" \"" + this.name + "\" \"" + actions + "\")";
    }
}
