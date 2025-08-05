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

/* compiled from: DelegationPermission.java */
/* loaded from: rt.jar:javax/security/auth/kerberos/KrbDelegationPermissionCollection.class */
final class KrbDelegationPermissionCollection extends PermissionCollection implements Serializable {
    private transient List<Permission> perms = new ArrayList();
    private static final long serialVersionUID = -3383936936589966948L;
    private static final ObjectStreamField[] serialPersistentFields = {new ObjectStreamField("permissions", Vector.class)};

    @Override // java.security.PermissionCollection
    public boolean implies(Permission permission) {
        if (!(permission instanceof DelegationPermission)) {
            return false;
        }
        synchronized (this) {
            Iterator<Permission> it = this.perms.iterator();
            while (it.hasNext()) {
                if (it.next().implies(permission)) {
                    return true;
                }
            }
            return false;
        }
    }

    @Override // java.security.PermissionCollection
    public void add(Permission permission) {
        if (!(permission instanceof DelegationPermission)) {
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
