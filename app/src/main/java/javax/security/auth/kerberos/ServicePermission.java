package javax.security.auth.kerberos;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.Permission;
import java.security.PermissionCollection;
import sun.security.util.SecurityConstants;

/* loaded from: rt.jar:javax/security/auth/kerberos/ServicePermission.class */
public final class ServicePermission extends Permission implements Serializable {
    private static final long serialVersionUID = -1227585031618624935L;
    private static final int INITIATE = 1;
    private static final int ACCEPT = 2;
    private static final int ALL = 3;
    private static final int NONE = 0;
    private transient int mask;
    private String actions;

    public ServicePermission(String str, String str2) {
        super(str);
        init(str, getMask(str2));
    }

    private void init(String str, int i2) {
        if (str == null) {
            throw new NullPointerException("service principal can't be null");
        }
        if ((i2 & 3) != i2) {
            throw new IllegalArgumentException("invalid actions mask");
        }
        this.mask = i2;
    }

    @Override // java.security.Permission
    public boolean implies(Permission permission) {
        if (!(permission instanceof ServicePermission)) {
            return false;
        }
        ServicePermission servicePermission = (ServicePermission) permission;
        return (this.mask & servicePermission.mask) == servicePermission.mask && impliesIgnoreMask(servicePermission);
    }

    boolean impliesIgnoreMask(ServicePermission servicePermission) {
        return getName().equals("*") || getName().equals(servicePermission.getName()) || (servicePermission.getName().startsWith("@") && getName().endsWith(servicePermission.getName()));
    }

    @Override // java.security.Permission
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ServicePermission)) {
            return false;
        }
        ServicePermission servicePermission = (ServicePermission) obj;
        return (this.mask & servicePermission.mask) == servicePermission.mask && getName().equals(servicePermission.getName());
    }

    @Override // java.security.Permission
    public int hashCode() {
        return getName().hashCode() ^ this.mask;
    }

    private static String getActions(int i2) {
        StringBuilder sb = new StringBuilder();
        boolean z2 = false;
        if ((i2 & 1) == 1) {
            if (0 != 0) {
                sb.append(',');
            } else {
                z2 = true;
            }
            sb.append("initiate");
        }
        if ((i2 & 2) == 2) {
            if (z2) {
                sb.append(',');
            }
            sb.append(SecurityConstants.SOCKET_ACCEPT_ACTION);
        }
        return sb.toString();
    }

    @Override // java.security.Permission
    public String getActions() {
        if (this.actions == null) {
            this.actions = getActions(this.mask);
        }
        return this.actions;
    }

    @Override // java.security.Permission
    public PermissionCollection newPermissionCollection() {
        return new KrbServicePermissionCollection();
    }

    int getMask() {
        return this.mask;
    }

    /* JADX WARN: Code restructure failed: missing block: B:95:0x01c3, code lost:
    
        throw new java.lang.IllegalArgumentException("invalid permission: " + r5);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static int getMask(java.lang.String r5) {
        /*
            Method dump skipped, instructions count: 584
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.security.auth.kerberos.ServicePermission.getMask(java.lang.String):int");
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        if (this.actions == null) {
            getActions();
        }
        objectOutputStream.defaultWriteObject();
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        init(getName(), getMask(this.actions));
    }
}
