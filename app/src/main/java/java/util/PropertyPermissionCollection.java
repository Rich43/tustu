package java.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamField;
import java.io.Serializable;
import java.security.Permission;
import java.security.PermissionCollection;

/* compiled from: PropertyPermission.java */
/* loaded from: rt.jar:java/util/PropertyPermissionCollection.class */
final class PropertyPermissionCollection extends PermissionCollection implements Serializable {
    private transient Map<String, PropertyPermission> perms = new HashMap(32);
    private boolean all_allowed = false;
    private static final long serialVersionUID = 7015263904581634791L;
    private static final ObjectStreamField[] serialPersistentFields = {new ObjectStreamField("permissions", Hashtable.class), new ObjectStreamField("all_allowed", Boolean.TYPE)};

    @Override // java.security.PermissionCollection
    public void add(Permission permission) {
        if (!(permission instanceof PropertyPermission)) {
            throw new IllegalArgumentException("invalid permission: " + ((Object) permission));
        }
        if (isReadOnly()) {
            throw new SecurityException("attempt to add a Permission to a readonly PermissionCollection");
        }
        PropertyPermission propertyPermission = (PropertyPermission) permission;
        String name = propertyPermission.getName();
        synchronized (this) {
            PropertyPermission propertyPermission2 = this.perms.get(name);
            if (propertyPermission2 != null) {
                int mask = propertyPermission2.getMask();
                int mask2 = propertyPermission.getMask();
                if (mask != mask2) {
                    this.perms.put(name, new PropertyPermission(name, PropertyPermission.getActions(mask | mask2)));
                }
            } else {
                this.perms.put(name, propertyPermission);
            }
        }
        if (!this.all_allowed && name.equals("*")) {
            this.all_allowed = true;
        }
    }

    @Override // java.security.PermissionCollection
    public boolean implies(Permission permission) {
        PropertyPermission propertyPermission;
        PropertyPermission propertyPermission2;
        PropertyPermission propertyPermission3;
        if (!(permission instanceof PropertyPermission)) {
            return false;
        }
        PropertyPermission propertyPermission4 = (PropertyPermission) permission;
        int mask = propertyPermission4.getMask();
        int mask2 = 0;
        if (this.all_allowed) {
            synchronized (this) {
                propertyPermission3 = this.perms.get("*");
            }
            if (propertyPermission3 != null) {
                mask2 = 0 | propertyPermission3.getMask();
                if ((mask2 & mask) == mask) {
                    return true;
                }
            }
        }
        String name = propertyPermission4.getName();
        synchronized (this) {
            propertyPermission = this.perms.get(name);
        }
        if (propertyPermission != null) {
            mask2 |= propertyPermission.getMask();
            if ((mask2 & mask) == mask) {
                return true;
            }
        }
        int length = name.length();
        while (true) {
            int iLastIndexOf = name.lastIndexOf(".", length - 1);
            if (iLastIndexOf != -1) {
                name = name.substring(0, iLastIndexOf + 1) + "*";
                synchronized (this) {
                    propertyPermission2 = this.perms.get(name);
                }
                if (propertyPermission2 != null) {
                    mask2 |= propertyPermission2.getMask();
                    if ((mask2 & mask) == mask) {
                        return true;
                    }
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
        objectOutputStream.writeFields();
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        ObjectInputStream.GetField fields = objectInputStream.readFields();
        this.all_allowed = fields.get("all_allowed", false);
        Hashtable hashtable = (Hashtable) fields.get("permissions", (Object) null);
        this.perms = new HashMap(hashtable.size() * 2);
        this.perms.putAll(hashtable);
    }
}
