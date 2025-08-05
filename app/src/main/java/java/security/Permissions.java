package java.security;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamField;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/* loaded from: rt.jar:java/security/Permissions.class */
public final class Permissions extends PermissionCollection implements Serializable {
    private static final long serialVersionUID = 4858622370623524688L;
    private static final ObjectStreamField[] serialPersistentFields = {new ObjectStreamField("perms", Hashtable.class), new ObjectStreamField("allPermission", PermissionCollection.class)};
    private transient boolean hasUnresolved = false;
    private transient Map<Class<?>, PermissionCollection> permsMap = new HashMap(11);
    PermissionCollection allPermission = null;

    @Override // java.security.PermissionCollection
    public void add(Permission permission) {
        PermissionCollection permissionCollection;
        if (isReadOnly()) {
            throw new SecurityException("attempt to add a Permission to a readonly Permissions object");
        }
        synchronized (this) {
            permissionCollection = getPermissionCollection(permission, true);
            permissionCollection.add(permission);
        }
        if (permission instanceof AllPermission) {
            this.allPermission = permissionCollection;
        }
        if (permission instanceof UnresolvedPermission) {
            this.hasUnresolved = true;
        }
    }

    @Override // java.security.PermissionCollection
    public boolean implies(Permission permission) {
        if (this.allPermission != null) {
            return true;
        }
        synchronized (this) {
            PermissionCollection permissionCollection = getPermissionCollection(permission, false);
            if (permissionCollection != null) {
                return permissionCollection.implies(permission);
            }
            return false;
        }
    }

    @Override // java.security.PermissionCollection
    public Enumeration<Permission> elements() {
        PermissionsEnumerator permissionsEnumerator;
        synchronized (this) {
            permissionsEnumerator = new PermissionsEnumerator(this.permsMap.values().iterator());
        }
        return permissionsEnumerator;
    }

    private PermissionCollection getPermissionCollection(Permission permission, boolean z2) {
        Class<?> cls = permission.getClass();
        PermissionCollection unresolvedPermissions = this.permsMap.get(cls);
        if (!this.hasUnresolved && !z2) {
            return unresolvedPermissions;
        }
        if (unresolvedPermissions == null) {
            unresolvedPermissions = this.hasUnresolved ? getUnresolvedPermissions(permission) : null;
            if (unresolvedPermissions == null && z2) {
                unresolvedPermissions = permission.newPermissionCollection();
                if (unresolvedPermissions == null) {
                    unresolvedPermissions = new PermissionsHash();
                }
            }
            if (unresolvedPermissions != null) {
                this.permsMap.put(cls, unresolvedPermissions);
            }
        }
        return unresolvedPermissions;
    }

    private PermissionCollection getUnresolvedPermissions(Permission permission) {
        List<UnresolvedPermission> unresolvedPermissions;
        UnresolvedPermissionCollection unresolvedPermissionCollection = (UnresolvedPermissionCollection) this.permsMap.get(UnresolvedPermission.class);
        if (unresolvedPermissionCollection == null || (unresolvedPermissions = unresolvedPermissionCollection.getUnresolvedPermissions(permission)) == null) {
            return null;
        }
        java.security.cert.Certificate[] certificateArr = null;
        Object[] signers = permission.getClass().getSigners();
        int i2 = 0;
        if (signers != null) {
            for (Object obj : signers) {
                if (obj instanceof java.security.cert.Certificate) {
                    i2++;
                }
            }
            certificateArr = new java.security.cert.Certificate[i2];
            int i3 = 0;
            for (int i4 = 0; i4 < signers.length; i4++) {
                if (signers[i4] instanceof java.security.cert.Certificate) {
                    int i5 = i3;
                    i3++;
                    certificateArr[i5] = (java.security.cert.Certificate) signers[i4];
                }
            }
        }
        PermissionCollection permissionCollectionNewPermissionCollection = null;
        synchronized (unresolvedPermissions) {
            int size = unresolvedPermissions.size();
            for (int i6 = 0; i6 < size; i6++) {
                Permission permissionResolve = unresolvedPermissions.get(i6).resolve(permission, certificateArr);
                if (permissionResolve != null) {
                    if (permissionCollectionNewPermissionCollection == null) {
                        permissionCollectionNewPermissionCollection = permission.newPermissionCollection();
                        if (permissionCollectionNewPermissionCollection == null) {
                            permissionCollectionNewPermissionCollection = new PermissionsHash();
                        }
                    }
                    permissionCollectionNewPermissionCollection.add(permissionResolve);
                }
            }
        }
        return permissionCollectionNewPermissionCollection;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        Hashtable hashtable = new Hashtable(this.permsMap.size() * 2);
        synchronized (this) {
            hashtable.putAll(this.permsMap);
        }
        ObjectOutputStream.PutField putFieldPutFields = objectOutputStream.putFields();
        putFieldPutFields.put("allPermission", this.allPermission);
        putFieldPutFields.put("perms", hashtable);
        objectOutputStream.writeFields();
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        ObjectInputStream.GetField fields = objectInputStream.readFields();
        this.allPermission = (PermissionCollection) fields.get("allPermission", (Object) null);
        Hashtable hashtable = (Hashtable) fields.get("perms", (Object) null);
        this.permsMap = new HashMap(hashtable.size() * 2);
        this.permsMap.putAll(hashtable);
        UnresolvedPermissionCollection unresolvedPermissionCollection = (UnresolvedPermissionCollection) this.permsMap.get(UnresolvedPermission.class);
        this.hasUnresolved = unresolvedPermissionCollection != null && unresolvedPermissionCollection.elements().hasMoreElements();
    }
}
