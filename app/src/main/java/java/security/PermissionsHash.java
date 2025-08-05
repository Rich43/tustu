package java.security;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamField;
import java.io.Serializable;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

/* compiled from: Permissions.java */
/* loaded from: rt.jar:java/security/PermissionsHash.class */
final class PermissionsHash extends PermissionCollection implements Serializable {
    private transient Map<Permission, Permission> permsMap = new HashMap(11);
    private static final long serialVersionUID = -8491988220802933440L;
    private static final ObjectStreamField[] serialPersistentFields = {new ObjectStreamField("perms", Hashtable.class)};

    PermissionsHash() {
    }

    @Override // java.security.PermissionCollection
    public void add(Permission permission) {
        synchronized (this) {
            this.permsMap.put(permission, permission);
        }
    }

    @Override // java.security.PermissionCollection
    public boolean implies(Permission permission) {
        synchronized (this) {
            if (this.permsMap.get(permission) == null) {
                Iterator<Permission> it = this.permsMap.values().iterator();
                while (it.hasNext()) {
                    if (it.next().implies(permission)) {
                        return true;
                    }
                }
                return false;
            }
            return true;
        }
    }

    @Override // java.security.PermissionCollection
    public Enumeration<Permission> elements() {
        Enumeration<Permission> enumeration;
        synchronized (this) {
            enumeration = Collections.enumeration(this.permsMap.values());
        }
        return enumeration;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        Hashtable hashtable = new Hashtable(this.permsMap.size() * 2);
        synchronized (this) {
            hashtable.putAll(this.permsMap);
        }
        objectOutputStream.putFields().put("perms", hashtable);
        objectOutputStream.writeFields();
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        Hashtable hashtable = (Hashtable) objectInputStream.readFields().get("perms", (Object) null);
        this.permsMap = new HashMap(hashtable.size() * 2);
        this.permsMap.putAll(hashtable);
    }
}
