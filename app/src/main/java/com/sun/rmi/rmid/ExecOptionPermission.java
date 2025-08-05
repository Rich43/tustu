package com.sun.rmi.rmid;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.security.Permission;
import java.security.PermissionCollection;
import java.util.Enumeration;
import java.util.Hashtable;

/* loaded from: rt.jar:com/sun/rmi/rmid/ExecOptionPermission.class */
public final class ExecOptionPermission extends Permission {
    private transient boolean wildcard;
    private transient String name;
    private static final long serialVersionUID = 5842294756823092756L;

    public ExecOptionPermission(String str) {
        super(str);
        init(str);
    }

    public ExecOptionPermission(String str, String str2) {
        this(str);
    }

    @Override // java.security.Permission
    public boolean implies(Permission permission) {
        if (!(permission instanceof ExecOptionPermission)) {
            return false;
        }
        ExecOptionPermission execOptionPermission = (ExecOptionPermission) permission;
        if (this.wildcard) {
            if (execOptionPermission.wildcard) {
                return execOptionPermission.name.startsWith(this.name);
            }
            return execOptionPermission.name.length() > this.name.length() && execOptionPermission.name.startsWith(this.name);
        }
        if (execOptionPermission.wildcard) {
            return false;
        }
        return this.name.equals(execOptionPermission.name);
    }

    @Override // java.security.Permission
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }
        return getName().equals(((ExecOptionPermission) obj).getName());
    }

    @Override // java.security.Permission
    public int hashCode() {
        return getName().hashCode();
    }

    @Override // java.security.Permission
    public String getActions() {
        return "";
    }

    @Override // java.security.Permission
    public PermissionCollection newPermissionCollection() {
        return new ExecOptionPermissionCollection();
    }

    private synchronized void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        init(getName());
    }

    private void init(String str) {
        if (str == null) {
            throw new NullPointerException("name can't be null");
        }
        if (str.equals("")) {
            throw new IllegalArgumentException("name can't be empty");
        }
        if (str.endsWith(".*") || str.endsWith("=*") || str.equals("*")) {
            this.wildcard = true;
            if (str.length() == 1) {
                this.name = "";
                return;
            } else {
                this.name = str.substring(0, str.length() - 1);
                return;
            }
        }
        this.name = str;
    }

    /* loaded from: rt.jar:com/sun/rmi/rmid/ExecOptionPermission$ExecOptionPermissionCollection.class */
    private static class ExecOptionPermissionCollection extends PermissionCollection implements Serializable {
        private Hashtable<String, Permission> permissions = new Hashtable<>(11);
        private boolean all_allowed = false;
        private static final long serialVersionUID = -1242475729790124375L;

        @Override // java.security.PermissionCollection
        public void add(Permission permission) {
            if (!(permission instanceof ExecOptionPermission)) {
                throw new IllegalArgumentException("invalid permission: " + ((Object) permission));
            }
            if (isReadOnly()) {
                throw new SecurityException("attempt to add a Permission to a readonly PermissionCollection");
            }
            ExecOptionPermission execOptionPermission = (ExecOptionPermission) permission;
            this.permissions.put(execOptionPermission.getName(), permission);
            if (!this.all_allowed && execOptionPermission.getName().equals("*")) {
                this.all_allowed = true;
            }
        }

        @Override // java.security.PermissionCollection
        public boolean implies(Permission permission) {
            if (!(permission instanceof ExecOptionPermission)) {
                return false;
            }
            ExecOptionPermission execOptionPermission = (ExecOptionPermission) permission;
            if (this.all_allowed) {
                return true;
            }
            String name = execOptionPermission.getName();
            Permission permission2 = this.permissions.get(name);
            if (permission2 != null) {
                return permission2.implies(permission);
            }
            int length = name.length();
            while (true) {
                int iLastIndexOf = name.lastIndexOf(".", length - 1);
                if (iLastIndexOf != -1) {
                    name = name.substring(0, iLastIndexOf + 1) + "*";
                    Permission permission3 = this.permissions.get(name);
                    if (permission3 != null) {
                        return permission3.implies(permission);
                    }
                    length = iLastIndexOf;
                } else {
                    String name2 = execOptionPermission.getName();
                    int length2 = name2.length();
                    while (true) {
                        int iLastIndexOf2 = name2.lastIndexOf("=", length2 - 1);
                        if (iLastIndexOf2 != -1) {
                            name2 = name2.substring(0, iLastIndexOf2 + 1) + "*";
                            Permission permission4 = this.permissions.get(name2);
                            if (permission4 != null) {
                                return permission4.implies(permission);
                            }
                            length2 = iLastIndexOf2;
                        } else {
                            return false;
                        }
                    }
                }
            }
        }

        @Override // java.security.PermissionCollection
        public Enumeration<Permission> elements() {
            return this.permissions.elements();
        }
    }
}
