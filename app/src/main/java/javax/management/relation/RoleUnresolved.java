package javax.management.relation;

import com.sun.jmx.mbeanserver.GetPropertyAction;
import com.sun.jmx.mbeanserver.Util;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamField;
import java.io.Serializable;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.management.ObjectName;

/* loaded from: rt.jar:javax/management/relation/RoleUnresolved.class */
public class RoleUnresolved implements Serializable {
    private static final long oldSerialVersionUID = -9026457686611660144L;
    private static final long newSerialVersionUID = -48350262537070138L;
    private static final ObjectStreamField[] oldSerialPersistentFields = {new ObjectStreamField("myRoleName", String.class), new ObjectStreamField("myRoleValue", ArrayList.class), new ObjectStreamField("myPbType", Integer.TYPE)};
    private static final ObjectStreamField[] newSerialPersistentFields = {new ObjectStreamField("roleName", String.class), new ObjectStreamField("roleValue", List.class), new ObjectStreamField("problemType", Integer.TYPE)};
    private static final long serialVersionUID;
    private static final ObjectStreamField[] serialPersistentFields;
    private static boolean compat;
    private String roleName = null;
    private List<ObjectName> roleValue = null;
    private int problemType;

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

    public RoleUnresolved(String str, List<ObjectName> list, int i2) throws IllegalArgumentException {
        if (str == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        setRoleName(str);
        setRoleValue(list);
        setProblemType(i2);
    }

    public String getRoleName() {
        return this.roleName;
    }

    public List<ObjectName> getRoleValue() {
        return this.roleValue;
    }

    public int getProblemType() {
        return this.problemType;
    }

    public void setRoleName(String str) throws IllegalArgumentException {
        if (str == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        this.roleName = str;
    }

    public void setRoleValue(List<ObjectName> list) {
        if (list != null) {
            this.roleValue = new ArrayList(list);
        } else {
            this.roleValue = null;
        }
    }

    public void setProblemType(int i2) throws IllegalArgumentException {
        if (!RoleStatus.isRoleStatus(i2)) {
            throw new IllegalArgumentException("Incorrect problem type.");
        }
        this.problemType = i2;
    }

    public Object clone() {
        try {
            return new RoleUnresolved(this.roleName, this.roleValue, this.problemType);
        } catch (IllegalArgumentException e2) {
            return null;
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("role name: " + this.roleName);
        if (this.roleValue != null) {
            sb.append("; value: ");
            Iterator<ObjectName> it = this.roleValue.iterator();
            while (it.hasNext()) {
                sb.append(it.next().toString());
                if (it.hasNext()) {
                    sb.append(", ");
                }
            }
        }
        sb.append("; problem type: " + this.problemType);
        return sb.toString();
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        if (compat) {
            ObjectInputStream.GetField fields = objectInputStream.readFields();
            this.roleName = (String) fields.get("myRoleName", (Object) null);
            if (fields.defaulted("myRoleName")) {
                throw new NullPointerException("myRoleName");
            }
            this.roleValue = (List) Util.cast(fields.get("myRoleValue", (Object) null));
            if (fields.defaulted("myRoleValue")) {
                throw new NullPointerException("myRoleValue");
            }
            this.problemType = fields.get("myPbType", 0);
            if (fields.defaulted("myPbType")) {
                throw new NullPointerException("myPbType");
            }
            return;
        }
        objectInputStream.defaultReadObject();
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        if (compat) {
            ObjectOutputStream.PutField putFieldPutFields = objectOutputStream.putFields();
            putFieldPutFields.put("myRoleName", this.roleName);
            putFieldPutFields.put("myRoleValue", this.roleValue);
            putFieldPutFields.put("myPbType", this.problemType);
            objectOutputStream.writeFields();
            return;
        }
        objectOutputStream.defaultWriteObject();
    }
}
