package javax.management.relation;

import com.sun.jmx.mbeanserver.GetPropertyAction;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamField;
import java.io.Serializable;
import java.security.AccessController;
import java.util.Iterator;

/* loaded from: rt.jar:javax/management/relation/RoleResult.class */
public class RoleResult implements Serializable {
    private static final long oldSerialVersionUID = 3786616013762091099L;
    private static final long newSerialVersionUID = -6304063118040985512L;
    private static final ObjectStreamField[] oldSerialPersistentFields = {new ObjectStreamField("myRoleList", RoleList.class), new ObjectStreamField("myRoleUnresList", RoleUnresolvedList.class)};
    private static final ObjectStreamField[] newSerialPersistentFields = {new ObjectStreamField("roleList", RoleList.class), new ObjectStreamField("unresolvedRoleList", RoleUnresolvedList.class)};
    private static final long serialVersionUID;
    private static final ObjectStreamField[] serialPersistentFields;
    private static boolean compat;
    private RoleList roleList = null;
    private RoleUnresolvedList unresolvedRoleList = null;

    static {
        compat = false;
        try {
            String str = (String) AccessController.doPrivileged(new GetPropertyAction("jmx.serial.form"));
            compat = str != null && str.equals("1.0");
        } catch (Exception e2) {
        }
        if (compat) {
            serialPersistentFields = oldSerialPersistentFields;
            serialVersionUID = oldSerialVersionUID;
        } else {
            serialPersistentFields = newSerialPersistentFields;
            serialVersionUID = newSerialVersionUID;
        }
    }

    public RoleResult(RoleList roleList, RoleUnresolvedList roleUnresolvedList) throws IllegalArgumentException {
        setRoles(roleList);
        setRolesUnresolved(roleUnresolvedList);
    }

    public RoleList getRoles() {
        return this.roleList;
    }

    public RoleUnresolvedList getRolesUnresolved() {
        return this.unresolvedRoleList;
    }

    public void setRoles(RoleList roleList) throws IllegalArgumentException {
        if (roleList != null) {
            this.roleList = new RoleList();
            Iterator<Object> it = roleList.iterator();
            while (it.hasNext()) {
                this.roleList.add((Role) ((Role) it.next()).clone());
            }
            return;
        }
        this.roleList = null;
    }

    public void setRolesUnresolved(RoleUnresolvedList roleUnresolvedList) throws IllegalArgumentException {
        if (roleUnresolvedList != null) {
            this.unresolvedRoleList = new RoleUnresolvedList();
            Iterator<Object> it = roleUnresolvedList.iterator();
            while (it.hasNext()) {
                this.unresolvedRoleList.add((RoleUnresolved) ((RoleUnresolved) it.next()).clone());
            }
            return;
        }
        this.unresolvedRoleList = null;
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        if (compat) {
            ObjectInputStream.GetField fields = objectInputStream.readFields();
            this.roleList = (RoleList) fields.get("myRoleList", (Object) null);
            if (fields.defaulted("myRoleList")) {
                throw new NullPointerException("myRoleList");
            }
            this.unresolvedRoleList = (RoleUnresolvedList) fields.get("myRoleUnresList", (Object) null);
            if (fields.defaulted("myRoleUnresList")) {
                throw new NullPointerException("myRoleUnresList");
            }
            return;
        }
        objectInputStream.defaultReadObject();
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        if (compat) {
            ObjectOutputStream.PutField putFieldPutFields = objectOutputStream.putFields();
            putFieldPutFields.put("myRoleList", this.roleList);
            putFieldPutFields.put("myRoleUnresList", this.unresolvedRoleList);
            objectOutputStream.writeFields();
            return;
        }
        objectOutputStream.defaultWriteObject();
    }
}
