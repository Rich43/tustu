package sun.security.provider;

import java.security.CodeSource;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.Permissions;
import java.util.Enumeration;
import java.util.Vector;

/* compiled from: AuthPolicyFile.java */
/* loaded from: rt.jar:sun/security/provider/PolicyPermissions.class */
class PolicyPermissions extends PermissionCollection {
    private static final long serialVersionUID = -1954188373270545523L;
    private CodeSource codesource;
    private AuthPolicyFile policy;
    private Permissions perms = null;
    private boolean notInit = true;
    private Vector<Permission> additionalPerms = null;

    PolicyPermissions(AuthPolicyFile authPolicyFile, CodeSource codeSource) {
        this.codesource = codeSource;
        this.policy = authPolicyFile;
    }

    @Override // java.security.PermissionCollection
    public void add(Permission permission) {
        if (isReadOnly()) {
            throw new SecurityException(AuthPolicyFile.rb.getString("attempt.to.add.a.Permission.to.a.readonly.PermissionCollection"));
        }
        if (this.perms == null) {
            if (this.additionalPerms == null) {
                this.additionalPerms = new Vector<>();
            }
            this.additionalPerms.add(permission);
            return;
        }
        this.perms.add(permission);
    }

    private synchronized void init() {
        if (this.notInit) {
            if (this.perms == null) {
                this.perms = new Permissions();
            }
            if (this.additionalPerms != null) {
                Enumeration<Permission> enumerationElements = this.additionalPerms.elements();
                while (enumerationElements.hasMoreElements()) {
                    this.perms.add(enumerationElements.nextElement2());
                }
                this.additionalPerms = null;
            }
            this.policy.getPermissions(this.perms, this.codesource);
            this.notInit = false;
        }
    }

    @Override // java.security.PermissionCollection
    public boolean implies(Permission permission) {
        if (this.notInit) {
            init();
        }
        return this.perms.implies(permission);
    }

    @Override // java.security.PermissionCollection
    public Enumeration<Permission> elements() {
        if (this.notInit) {
            init();
        }
        return this.perms.elements();
    }

    @Override // java.security.PermissionCollection
    public String toString() {
        if (this.notInit) {
            init();
        }
        return this.perms.toString();
    }
}
