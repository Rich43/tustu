package java.io;

import java.security.Permission;
import java.security.PermissionCollection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/* compiled from: FilePermission.java */
/* loaded from: rt.jar:java/io/FilePermissionCollection.class */
final class FilePermissionCollection extends PermissionCollection implements Serializable {
    private transient List<Permission> perms = new ArrayList();
    private static final long serialVersionUID = 2202956749081564585L;
    private static final ObjectStreamField[] serialPersistentFields = {new ObjectStreamField("permissions", Vector.class)};

    @Override // java.security.PermissionCollection
    public void add(Permission permission) {
        if (!(permission instanceof FilePermission)) {
            throw new IllegalArgumentException("invalid permission: " + ((Object) permission));
        }
        if (isReadOnly()) {
            throw new SecurityException("attempt to add a Permission to a readonly PermissionCollection");
        }
        synchronized (this) {
            this.perms.add(permission);
        }
    }

    @Override // java.security.PermissionCollection
    public boolean implies(Permission permission) {
        if (!(permission instanceof FilePermission)) {
            return false;
        }
        FilePermission filePermission = (FilePermission) permission;
        int mask = filePermission.getMask();
        int mask2 = 0;
        int i2 = mask;
        synchronized (this) {
            int size = this.perms.size();
            for (int i3 = 0; i3 < size; i3++) {
                FilePermission filePermission2 = (FilePermission) this.perms.get(i3);
                if ((i2 & filePermission2.getMask()) != 0 && filePermission2.impliesIgnoreMask(filePermission)) {
                    mask2 |= filePermission2.getMask();
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
        Iterator it = vector.iterator();
        while (it.hasNext()) {
            this.perms.add((Permission) it.next());
        }
    }
}
