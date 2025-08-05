package com.sun.rmi.rmid;

import java.io.FilePermission;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.security.Permission;
import java.security.PermissionCollection;
import java.util.Enumeration;
import java.util.Vector;
import sun.security.util.SecurityConstants;

/* loaded from: rt.jar:com/sun/rmi/rmid/ExecPermission.class */
public final class ExecPermission extends Permission {
    private static final long serialVersionUID = -6208470287358147919L;
    private transient FilePermission fp;

    public ExecPermission(String str) {
        super(str);
        init(str);
    }

    public ExecPermission(String str, String str2) {
        this(str);
    }

    @Override // java.security.Permission
    public boolean implies(Permission permission) {
        if (!(permission instanceof ExecPermission)) {
            return false;
        }
        return this.fp.implies(((ExecPermission) permission).fp);
    }

    @Override // java.security.Permission
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ExecPermission)) {
            return false;
        }
        return this.fp.equals(((ExecPermission) obj).fp);
    }

    @Override // java.security.Permission
    public int hashCode() {
        return this.fp.hashCode();
    }

    @Override // java.security.Permission
    public String getActions() {
        return "";
    }

    @Override // java.security.Permission
    public PermissionCollection newPermissionCollection() {
        return new ExecPermissionCollection();
    }

    private synchronized void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        init(getName());
    }

    private void init(String str) {
        this.fp = new FilePermission(str, SecurityConstants.FILE_EXECUTE_ACTION);
    }

    /* loaded from: rt.jar:com/sun/rmi/rmid/ExecPermission$ExecPermissionCollection.class */
    private static class ExecPermissionCollection extends PermissionCollection implements Serializable {
        private Vector<Permission> permissions = new Vector<>();
        private static final long serialVersionUID = -3352558508888368273L;

        @Override // java.security.PermissionCollection
        public void add(Permission permission) {
            if (!(permission instanceof ExecPermission)) {
                throw new IllegalArgumentException("invalid permission: " + ((Object) permission));
            }
            if (isReadOnly()) {
                throw new SecurityException("attempt to add a Permission to a readonly PermissionCollection");
            }
            this.permissions.addElement(permission);
        }

        @Override // java.security.PermissionCollection
        public boolean implies(Permission permission) {
            if (!(permission instanceof ExecPermission)) {
                return false;
            }
            Enumeration<Permission> enumerationElements = this.permissions.elements();
            while (enumerationElements.hasMoreElements()) {
                if (((ExecPermission) enumerationElements.nextElement2()).implies(permission)) {
                    return true;
                }
            }
            return false;
        }

        @Override // java.security.PermissionCollection
        public Enumeration<Permission> elements() {
            return this.permissions.elements();
        }
    }
}
