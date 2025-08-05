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

/* loaded from: rt.jar:javax/management/relation/Role.class */
public class Role implements Serializable {
    private static final long oldSerialVersionUID = -1959486389343113026L;
    private static final long newSerialVersionUID = -279985518429862552L;
    private static final ObjectStreamField[] oldSerialPersistentFields = {new ObjectStreamField("myName", String.class), new ObjectStreamField("myObjNameList", ArrayList.class)};
    private static final ObjectStreamField[] newSerialPersistentFields = {new ObjectStreamField("name", String.class), new ObjectStreamField("objectNameList", List.class)};
    private static final long serialVersionUID;
    private static final ObjectStreamField[] serialPersistentFields;
    private static boolean compat;
    private String name = null;
    private List<ObjectName> objectNameList = new ArrayList();

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

    public Role(String str, List<ObjectName> list) throws IllegalArgumentException {
        if (str == null || list == null) {
            throw new IllegalArgumentException("Invalid parameter");
        }
        setRoleName(str);
        setRoleValue(list);
    }

    public String getRoleName() {
        return this.name;
    }

    public List<ObjectName> getRoleValue() {
        return this.objectNameList;
    }

    public void setRoleName(String str) throws IllegalArgumentException {
        if (str == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        this.name = str;
    }

    public void setRoleValue(List<ObjectName> list) throws IllegalArgumentException {
        if (list == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        this.objectNameList = new ArrayList(list);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("role name: " + this.name + "; role value: ");
        Iterator<ObjectName> it = this.objectNameList.iterator();
        while (it.hasNext()) {
            sb.append(it.next().toString());
            if (it.hasNext()) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    public Object clone() {
        try {
            return new Role(this.name, this.objectNameList);
        } catch (IllegalArgumentException e2) {
            return null;
        }
    }

    public static String roleValueToString(List<ObjectName> list) throws IllegalArgumentException {
        if (list == null) {
            throw new IllegalArgumentException("Invalid parameter");
        }
        StringBuilder sb = new StringBuilder();
        for (ObjectName objectName : list) {
            if (sb.length() > 0) {
                sb.append("\n");
            }
            sb.append(objectName.toString());
        }
        return sb.toString();
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        if (compat) {
            ObjectInputStream.GetField fields = objectInputStream.readFields();
            this.name = (String) fields.get("myName", (Object) null);
            if (fields.defaulted("myName")) {
                throw new NullPointerException("myName");
            }
            this.objectNameList = (List) Util.cast(fields.get("myObjNameList", (Object) null));
            if (fields.defaulted("myObjNameList")) {
                throw new NullPointerException("myObjNameList");
            }
            return;
        }
        objectInputStream.defaultReadObject();
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        if (compat) {
            ObjectOutputStream.PutField putFieldPutFields = objectOutputStream.putFields();
            putFieldPutFields.put("myName", this.name);
            putFieldPutFields.put("myObjNameList", this.objectNameList);
            objectOutputStream.writeFields();
            return;
        }
        objectOutputStream.defaultWriteObject();
    }
}
