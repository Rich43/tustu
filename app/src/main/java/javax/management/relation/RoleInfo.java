package javax.management.relation;

import com.sun.jmx.mbeanserver.GetPropertyAction;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamField;
import java.io.Serializable;
import java.security.AccessController;
import javax.management.NotCompliantMBeanException;

/* loaded from: rt.jar:javax/management/relation/RoleInfo.class */
public class RoleInfo implements Serializable {
    private static final long oldSerialVersionUID = 7227256952085334351L;
    private static final long newSerialVersionUID = 2504952983494636987L;
    private static final ObjectStreamField[] oldSerialPersistentFields = {new ObjectStreamField("myName", String.class), new ObjectStreamField("myIsReadableFlg", Boolean.TYPE), new ObjectStreamField("myIsWritableFlg", Boolean.TYPE), new ObjectStreamField("myDescription", String.class), new ObjectStreamField("myMinDegree", Integer.TYPE), new ObjectStreamField("myMaxDegree", Integer.TYPE), new ObjectStreamField("myRefMBeanClassName", String.class)};
    private static final ObjectStreamField[] newSerialPersistentFields = {new ObjectStreamField("name", String.class), new ObjectStreamField("isReadable", Boolean.TYPE), new ObjectStreamField("isWritable", Boolean.TYPE), new ObjectStreamField("description", String.class), new ObjectStreamField("minDegree", Integer.TYPE), new ObjectStreamField("maxDegree", Integer.TYPE), new ObjectStreamField("referencedMBeanClassName", String.class)};
    private static final long serialVersionUID;
    private static final ObjectStreamField[] serialPersistentFields;
    private static boolean compat;
    public static final int ROLE_CARDINALITY_INFINITY = -1;
    private boolean isReadable;
    private boolean isWritable;
    private int minDegree;
    private int maxDegree;
    private String name = null;
    private String description = null;
    private String referencedMBeanClassName = null;

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

    public RoleInfo(String str, String str2, boolean z2, boolean z3, int i2, int i3, String str3) throws NotCompliantMBeanException, ClassNotFoundException, IllegalArgumentException, InvalidRoleInfoException {
        init(str, str2, z2, z3, i2, i3, str3);
    }

    public RoleInfo(String str, String str2, boolean z2, boolean z3) throws NotCompliantMBeanException, ClassNotFoundException, IllegalArgumentException {
        try {
            init(str, str2, z2, z3, 1, 1, null);
        } catch (InvalidRoleInfoException e2) {
        }
    }

    public RoleInfo(String str, String str2) throws NotCompliantMBeanException, ClassNotFoundException, IllegalArgumentException {
        try {
            init(str, str2, true, true, 1, 1, null);
        } catch (InvalidRoleInfoException e2) {
        }
    }

    public RoleInfo(RoleInfo roleInfo) throws IllegalArgumentException {
        if (roleInfo == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        try {
            init(roleInfo.getName(), roleInfo.getRefMBeanClassName(), roleInfo.isReadable(), roleInfo.isWritable(), roleInfo.getMinDegree(), roleInfo.getMaxDegree(), roleInfo.getDescription());
        } catch (InvalidRoleInfoException e2) {
        }
    }

    public String getName() {
        return this.name;
    }

    public boolean isReadable() {
        return this.isReadable;
    }

    public boolean isWritable() {
        return this.isWritable;
    }

    public String getDescription() {
        return this.description;
    }

    public int getMinDegree() {
        return this.minDegree;
    }

    public int getMaxDegree() {
        return this.maxDegree;
    }

    public String getRefMBeanClassName() {
        return this.referencedMBeanClassName;
    }

    public boolean checkMinDegree(int i2) {
        if (i2 < -1) {
            return false;
        }
        if (this.minDegree == -1 || i2 >= this.minDegree) {
            return true;
        }
        return false;
    }

    public boolean checkMaxDegree(int i2) {
        if (i2 < -1) {
            return false;
        }
        if (this.maxDegree == -1) {
            return true;
        }
        if (i2 != -1 && i2 <= this.maxDegree) {
            return true;
        }
        return false;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("role info name: " + this.name);
        sb.append("; isReadable: " + this.isReadable);
        sb.append("; isWritable: " + this.isWritable);
        sb.append("; description: " + this.description);
        sb.append("; minimum degree: " + this.minDegree);
        sb.append("; maximum degree: " + this.maxDegree);
        sb.append("; MBean class: " + this.referencedMBeanClassName);
        return sb.toString();
    }

    private void init(String str, String str2, boolean z2, boolean z3, int i2, int i3, String str3) throws IllegalArgumentException, InvalidRoleInfoException {
        if (str == null || str2 == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        this.name = str;
        this.isReadable = z2;
        this.isWritable = z3;
        if (str3 != null) {
            this.description = str3;
        }
        boolean z4 = false;
        StringBuilder sb = new StringBuilder();
        if (i3 != -1 && (i2 == -1 || i2 > i3)) {
            sb.append("Minimum degree ");
            sb.append(i2);
            sb.append(" is greater than maximum degree ");
            sb.append(i3);
            z4 = true;
        } else if (i2 < -1 || i3 < -1) {
            sb.append("Minimum or maximum degree has an illegal value, must be [0, ROLE_CARDINALITY_INFINITY].");
            z4 = true;
        }
        if (z4) {
            throw new InvalidRoleInfoException(sb.toString());
        }
        this.minDegree = i2;
        this.maxDegree = i3;
        this.referencedMBeanClassName = str2;
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        if (compat) {
            ObjectInputStream.GetField fields = objectInputStream.readFields();
            this.name = (String) fields.get("myName", (Object) null);
            if (fields.defaulted("myName")) {
                throw new NullPointerException("myName");
            }
            this.isReadable = fields.get("myIsReadableFlg", false);
            if (fields.defaulted("myIsReadableFlg")) {
                throw new NullPointerException("myIsReadableFlg");
            }
            this.isWritable = fields.get("myIsWritableFlg", false);
            if (fields.defaulted("myIsWritableFlg")) {
                throw new NullPointerException("myIsWritableFlg");
            }
            this.description = (String) fields.get("myDescription", (Object) null);
            if (fields.defaulted("myDescription")) {
                throw new NullPointerException("myDescription");
            }
            this.minDegree = fields.get("myMinDegree", 0);
            if (fields.defaulted("myMinDegree")) {
                throw new NullPointerException("myMinDegree");
            }
            this.maxDegree = fields.get("myMaxDegree", 0);
            if (fields.defaulted("myMaxDegree")) {
                throw new NullPointerException("myMaxDegree");
            }
            this.referencedMBeanClassName = (String) fields.get("myRefMBeanClassName", (Object) null);
            if (fields.defaulted("myRefMBeanClassName")) {
                throw new NullPointerException("myRefMBeanClassName");
            }
            return;
        }
        objectInputStream.defaultReadObject();
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        if (compat) {
            ObjectOutputStream.PutField putFieldPutFields = objectOutputStream.putFields();
            putFieldPutFields.put("myName", this.name);
            putFieldPutFields.put("myIsReadableFlg", this.isReadable);
            putFieldPutFields.put("myIsWritableFlg", this.isWritable);
            putFieldPutFields.put("myDescription", this.description);
            putFieldPutFields.put("myMinDegree", this.minDegree);
            putFieldPutFields.put("myMaxDegree", this.maxDegree);
            putFieldPutFields.put("myRefMBeanClassName", this.referencedMBeanClassName);
            objectOutputStream.writeFields();
            return;
        }
        objectOutputStream.defaultWriteObject();
    }
}
