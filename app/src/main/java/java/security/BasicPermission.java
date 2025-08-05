package java.security;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

/* loaded from: rt.jar:java/security/BasicPermission.class */
public abstract class BasicPermission extends Permission implements Serializable {
    private static final long serialVersionUID = 6279438298436773498L;
    private transient boolean wildcard;
    private transient String path;
    private transient boolean exitVM;

    private void init(String str) {
        if (str == null) {
            throw new NullPointerException("name can't be null");
        }
        int length = str.length();
        if (length == 0) {
            throw new IllegalArgumentException("name can't be empty");
        }
        if (str.charAt(length - 1) == '*' && (length == 1 || str.charAt(length - 2) == '.')) {
            this.wildcard = true;
            if (length == 1) {
                this.path = "";
                return;
            } else {
                this.path = str.substring(0, length - 1);
                return;
            }
        }
        if (str.equals("exitVM")) {
            this.wildcard = true;
            this.path = "exitVM.";
            this.exitVM = true;
            return;
        }
        this.path = str;
    }

    public BasicPermission(String str) {
        super(str);
        init(str);
    }

    public BasicPermission(String str, String str2) {
        super(str);
        init(str);
    }

    @Override // java.security.Permission
    public boolean implies(Permission permission) {
        if (permission == null || permission.getClass() != getClass()) {
            return false;
        }
        BasicPermission basicPermission = (BasicPermission) permission;
        if (this.wildcard) {
            if (basicPermission.wildcard) {
                return basicPermission.path.startsWith(this.path);
            }
            return basicPermission.path.length() > this.path.length() && basicPermission.path.startsWith(this.path);
        }
        if (basicPermission.wildcard) {
            return false;
        }
        return this.path.equals(basicPermission.path);
    }

    @Override // java.security.Permission
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }
        return getName().equals(((BasicPermission) obj).getName());
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
        return new BasicPermissionCollection(getClass());
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        init(getName());
    }

    final String getCanonicalName() {
        return this.exitVM ? "exitVM.*" : getName();
    }
}
