package javax.security.auth.kerberos;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamField;
import java.io.Serializable;
import java.security.Permission;
import java.security.PermissionCollection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/* compiled from: ServicePermission.java */
/* loaded from: rt.jar:javax/security/auth/kerberos/KrbServicePermissionCollection.class */
final class KrbServicePermissionCollection extends PermissionCollection implements Serializable {
    private transient List<Permission> perms = new ArrayList();
    private static final long serialVersionUID = -4118834211490102011L;
    private static final ObjectStreamField[] serialPersistentFields = {new ObjectStreamField("permissions", Vector.class)};

    @Override // java.security.PermissionCollection
    public boolean implies(Permission permission) {
        if (!(permission instanceof ServicePermission)) {
            return false;
        }
        ServicePermission servicePermission = (ServicePermission) permission;
        int mask = servicePermission.getMask();
        if (mask == 0) {
            Iterator<Permission> it = this.perms.iterator();
            while (it.hasNext()) {
                if (((ServicePermission) it.next()).impliesIgnoreMask(servicePermission)) {
                    return true;
                }
            }
            return false;
        }
        int mask2 = 0;
        int i2 = mask;
        synchronized (this) {
            int size = this.perms.size();
            for (int i3 = 0; i3 < size; i3++) {
                ServicePermission servicePermission2 = (ServicePermission) this.perms.get(i3);
                if ((i2 & servicePermission2.getMask()) != 0 && servicePermission2.impliesIgnoreMask(servicePermission)) {
                    mask2 |= servicePermission2.getMask();
                    if ((mask2 & mask) == mask) {
                        return true;
                    }
                    i2 = mask ^ mask2;
                }
            }
            return false;
        }
    }

    @Override // java.security.PermissionCollection
    public void add(Permission permission) {
        if (!(permission instanceof ServicePermission)) {
            throw new IllegalArgumentException("invalid permission: " + ((Object) permission));
        }
        if (isReadOnly()) {
            throw new SecurityException("attempt to add a Permission to a readonly PermissionCollection");
        }
        synchronized (this) {
            this.perms.add(0, permission);
        }
    }

    @Override // java.security.PermissionCollection
    public Enumeration<Permission> elements() {
        Enumeration<Permission> enumeration;
        synchronized (this) {
            enumeration = Collections.enumeration(this.perms);
        }
        return enumeration;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        Vector vector = new Vector(this.perms.size());
        synchronized (this) {
            vector.addAll(this.perms);
        }
        objectOutputStream.putFields().put("permissions", vector);
        objectOutputStream.writeFields();
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        Vector vector = (Vector) objectInputStream.readFields().get("permissions", (Object) null);
        this.perms = new ArrayList(vector.size());
        this.perms.addAll(vector);
    }
}
