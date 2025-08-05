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
import java.util.Map;

/* compiled from: BasicPermission.java */
/* loaded from: rt.jar:java/security/BasicPermissionCollection.class */
final class BasicPermissionCollection extends PermissionCollection implements Serializable {
    private static final long serialVersionUID = 739301742472979399L;
    private transient Map<String, Permission> perms = new HashMap(11);
    private boolean all_allowed = false;
    private Class<?> permClass;
    private static final ObjectStreamField[] serialPersistentFields = {new ObjectStreamField("permissions", Hashtable.class), new ObjectStreamField("all_allowed", Boolean.TYPE), new ObjectStreamField("permClass", Class.class)};

    public BasicPermissionCollection(Class<?> cls) {
        this.permClass = cls;
    }

    @Override // java.security.PermissionCollection
    public void add(Permission permission) {
        if (!(permission instanceof BasicPermission)) {
            throw new IllegalArgumentException("invalid permission: " + ((Object) permission));
        }
        if (isReadOnly()) {
            throw new SecurityException("attempt to add a Permission to a readonly PermissionCollection");
        }
        BasicPermission basicPermission = (BasicPermission) permission;
        if (this.permClass == null) {
            this.permClass = basicPermission.getClass();
        } else if (basicPermission.getClass() != this.permClass) {
            throw new IllegalArgumentException("invalid permission: " + ((Object) permission));
        }
        synchronized (this) {
            this.perms.put(basicPermission.getCanonicalName(), permission);
        }
        if (!this.all_allowed && basicPermission.getCanonicalName().equals("*")) {
            this.all_allowed = true;
        }
    }

    @Override // java.security.PermissionCollection
    public boolean implies(Permission permission) {
        Permission permission2;
        Permission permission3;
        if (!(permission instanceof BasicPermission)) {
            return false;
        }
        BasicPermission basicPermission = (BasicPermission) permission;
        if (basicPermission.getClass() != this.permClass) {
            return false;
        }
        if (this.all_allowed) {
            return true;
        }
        String canonicalName = basicPermission.getCanonicalName();
        synchronized (this) {
            permission2 = this.perms.get(canonicalName);
        }
        if (permission2 != null) {
            return permission2.implies(permission);
        }
        int length = canonicalName.length();
        while (true) {
            int iLastIndexOf = canonicalName.lastIndexOf(".", length - 1);
            if (iLastIndexOf != -1) {
                canonicalName = canonicalName.substring(0, iLastIndexOf + 1) + "*";
                synchronized (this) {
                    permission3 = this.perms.get(canonicalName);
                }
                if (permission3 != null) {
                    return permission3.implies(permission);
                }
                length = iLastIndexOf;
            } else {
                return false;
            }
        }
    }

    @Override // java.security.PermissionCollection
    public Enumeration<Permission> elements() {
        Enumeration<Permission> enumeration;
        synchronized (this) {
            enumeration = Collections.enumeration(this.perms.values());
        }
        return enumeration;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        Hashtable hashtable = new Hashtable(this.perms.size() * 2);
        synchronized (this) {
            hashtable.putAll(this.perms);
        }
        ObjectOutputStream.PutField putFieldPutFields = objectOutputStream.putFields();
        putFieldPutFields.put("all_allowed", this.all_allowed);
        putFieldPutFields.put("permissions", hashtable);
        putFieldPutFields.put("permClass", this.permClass);
        objectOutputStream.writeFields();
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        ObjectInputStream.GetField fields = objectInputStream.readFields();
        Hashtable hashtable = (Hashtable) fields.get("permissions", (Object) null);
        this.perms = new HashMap(hashtable.size() * 2);
        this.perms.putAll(hashtable);
        this.all_allowed = fields.get("all_allowed", false);
        this.permClass = (Class) fields.get("permClass", (Object) null);
        if (this.permClass == null) {
            Enumeration enumerationElements = hashtable.elements();
            if (enumerationElements.hasMoreElements()) {
                this.permClass = ((Permission) enumerationElements.nextElement2()).getClass();
            }
        }
    }
}
