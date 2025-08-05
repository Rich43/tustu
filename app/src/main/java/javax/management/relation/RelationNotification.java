package javax.management.relation;

import com.sun.jmx.mbeanserver.GetPropertyAction;
import com.sun.jmx.mbeanserver.Util;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamField;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import javax.management.Notification;
import javax.management.ObjectName;

/* loaded from: rt.jar:javax/management/relation/RelationNotification.class */
public class RelationNotification extends Notification {
    private static final long oldSerialVersionUID = -2126464566505527147L;
    private static final long newSerialVersionUID = -6871117877523310399L;
    private static final ObjectStreamField[] oldSerialPersistentFields = {new ObjectStreamField("myNewRoleValue", ArrayList.class), new ObjectStreamField("myOldRoleValue", ArrayList.class), new ObjectStreamField("myRelId", String.class), new ObjectStreamField("myRelObjName", ObjectName.class), new ObjectStreamField("myRelTypeName", String.class), new ObjectStreamField("myRoleName", String.class), new ObjectStreamField("myUnregMBeanList", ArrayList.class)};
    private static final ObjectStreamField[] newSerialPersistentFields = {new ObjectStreamField("newRoleValue", List.class), new ObjectStreamField("oldRoleValue", List.class), new ObjectStreamField("relationId", String.class), new ObjectStreamField("relationObjName", ObjectName.class), new ObjectStreamField("relationTypeName", String.class), new ObjectStreamField("roleName", String.class), new ObjectStreamField("unregisterMBeanList", List.class)};
    private static final long serialVersionUID;
    private static final ObjectStreamField[] serialPersistentFields;
    private static boolean compat;
    public static final String RELATION_BASIC_CREATION = "jmx.relation.creation.basic";
    public static final String RELATION_MBEAN_CREATION = "jmx.relation.creation.mbean";
    public static final String RELATION_BASIC_UPDATE = "jmx.relation.update.basic";
    public static final String RELATION_MBEAN_UPDATE = "jmx.relation.update.mbean";
    public static final String RELATION_BASIC_REMOVAL = "jmx.relation.removal.basic";
    public static final String RELATION_MBEAN_REMOVAL = "jmx.relation.removal.mbean";
    private String relationId;
    private String relationTypeName;
    private ObjectName relationObjName;
    private List<ObjectName> unregisterMBeanList;
    private String roleName;
    private List<ObjectName> oldRoleValue;
    private List<ObjectName> newRoleValue;

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

    public RelationNotification(String str, Object obj, long j2, long j3, String str2, String str3, String str4, ObjectName objectName, List<ObjectName> list) throws IllegalArgumentException {
        super(str, obj, j2, j3, str2);
        this.relationId = null;
        this.relationTypeName = null;
        this.relationObjName = null;
        this.unregisterMBeanList = null;
        this.roleName = null;
        this.oldRoleValue = null;
        this.newRoleValue = null;
        if (!isValidBasicStrict(str, obj, str3, str4) || !isValidCreate(str)) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        this.relationId = str3;
        this.relationTypeName = str4;
        this.relationObjName = safeGetObjectName(objectName);
        this.unregisterMBeanList = safeGetObjectNameList(list);
    }

    public RelationNotification(String str, Object obj, long j2, long j3, String str2, String str3, String str4, ObjectName objectName, String str5, List<ObjectName> list, List<ObjectName> list2) throws IllegalArgumentException {
        super(str, obj, j2, j3, str2);
        this.relationId = null;
        this.relationTypeName = null;
        this.relationObjName = null;
        this.unregisterMBeanList = null;
        this.roleName = null;
        this.oldRoleValue = null;
        this.newRoleValue = null;
        if (!isValidBasicStrict(str, obj, str3, str4) || !isValidUpdate(str, str5, list, list2)) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        this.relationId = str3;
        this.relationTypeName = str4;
        this.relationObjName = safeGetObjectName(objectName);
        this.roleName = str5;
        this.oldRoleValue = safeGetObjectNameList(list2);
        this.newRoleValue = safeGetObjectNameList(list);
    }

    public String getRelationId() {
        return this.relationId;
    }

    public String getRelationTypeName() {
        return this.relationTypeName;
    }

    public ObjectName getObjectName() {
        return this.relationObjName;
    }

    public List<ObjectName> getMBeansToUnregister() {
        List<ObjectName> listEmptyList;
        if (this.unregisterMBeanList != null) {
            listEmptyList = new ArrayList(this.unregisterMBeanList);
        } else {
            listEmptyList = Collections.emptyList();
        }
        return listEmptyList;
    }

    public String getRoleName() {
        String str = null;
        if (this.roleName != null) {
            str = this.roleName;
        }
        return str;
    }

    public List<ObjectName> getOldRoleValue() {
        List<ObjectName> listEmptyList;
        if (this.oldRoleValue != null) {
            listEmptyList = new ArrayList(this.oldRoleValue);
        } else {
            listEmptyList = Collections.emptyList();
        }
        return listEmptyList;
    }

    public List<ObjectName> getNewRoleValue() {
        List<ObjectName> listEmptyList;
        if (this.newRoleValue != null) {
            listEmptyList = new ArrayList(this.newRoleValue);
        } else {
            listEmptyList = Collections.emptyList();
        }
        return listEmptyList;
    }

    private boolean isValidBasicStrict(String str, Object obj, String str2, String str3) {
        if (obj == null) {
            return false;
        }
        return isValidBasic(str, obj, str2, str3);
    }

    private boolean isValidBasic(String str, Object obj, String str2, String str3) {
        if (str == null || str2 == null || str3 == null) {
            return false;
        }
        if (obj != null && !(obj instanceof RelationService) && !(obj instanceof ObjectName)) {
            return false;
        }
        return true;
    }

    private boolean isValidCreate(String str) {
        return new HashSet(Arrays.asList(RELATION_BASIC_CREATION, RELATION_MBEAN_CREATION, RELATION_BASIC_REMOVAL, RELATION_MBEAN_REMOVAL)).contains(str);
    }

    private boolean isValidUpdate(String str, String str2, List<ObjectName> list, List<ObjectName> list2) {
        if ((!str.equals(RELATION_BASIC_UPDATE) && !str.equals(RELATION_MBEAN_UPDATE)) || str2 == null || list2 == null || list == null) {
            return false;
        }
        return true;
    }

    private ArrayList<ObjectName> safeGetObjectNameList(List<ObjectName> list) {
        ArrayList<ObjectName> arrayList = null;
        if (list != null) {
            arrayList = new ArrayList<>();
            Iterator<ObjectName> it = list.iterator();
            while (it.hasNext()) {
                arrayList.add(ObjectName.getInstance(it.next()));
            }
        }
        return arrayList;
    }

    private ObjectName safeGetObjectName(ObjectName objectName) {
        ObjectName objectName2 = null;
        if (objectName != null) {
            objectName2 = ObjectName.getInstance(objectName);
        }
        return objectName2;
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        String str;
        String str2;
        String str3;
        ObjectName objectName;
        List<ObjectName> list;
        List<ObjectName> list2;
        List<ObjectName> list3;
        ObjectInputStream.GetField fields = objectInputStream.readFields();
        if (compat) {
            str = (String) fields.get("myRelId", (Object) null);
            str2 = (String) fields.get("myRelTypeName", (Object) null);
            str3 = (String) fields.get("myRoleName", (Object) null);
            objectName = (ObjectName) fields.get("myRelObjName", (Object) null);
            list = (List) Util.cast(fields.get("myNewRoleValue", (Object) null));
            list2 = (List) Util.cast(fields.get("myOldRoleValue", (Object) null));
            list3 = (List) Util.cast(fields.get("myUnregMBeanList", (Object) null));
        } else {
            str = (String) fields.get("relationId", (Object) null);
            str2 = (String) fields.get("relationTypeName", (Object) null);
            str3 = (String) fields.get("roleName", (Object) null);
            objectName = (ObjectName) fields.get("relationObjName", (Object) null);
            list = (List) Util.cast(fields.get("newRoleValue", (Object) null));
            list2 = (List) Util.cast(fields.get("oldRoleValue", (Object) null));
            list3 = (List) Util.cast(fields.get("unregisterMBeanList", (Object) null));
        }
        String type = super.getType();
        if (!isValidBasic(type, super.getSource(), str, str2) || (!isValidCreate(type) && !isValidUpdate(type, str3, list, list2))) {
            super.setSource(null);
            throw new InvalidObjectException("Invalid object read");
        }
        this.relationObjName = safeGetObjectName(objectName);
        this.newRoleValue = safeGetObjectNameList(list);
        this.oldRoleValue = safeGetObjectNameList(list2);
        this.unregisterMBeanList = safeGetObjectNameList(list3);
        this.relationId = str;
        this.relationTypeName = str2;
        this.roleName = str3;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        if (compat) {
            ObjectOutputStream.PutField putFieldPutFields = objectOutputStream.putFields();
            putFieldPutFields.put("myNewRoleValue", this.newRoleValue);
            putFieldPutFields.put("myOldRoleValue", this.oldRoleValue);
            putFieldPutFields.put("myRelId", this.relationId);
            putFieldPutFields.put("myRelObjName", this.relationObjName);
            putFieldPutFields.put("myRelTypeName", this.relationTypeName);
            putFieldPutFields.put("myRoleName", this.roleName);
            putFieldPutFields.put("myUnregMBeanList", this.unregisterMBeanList);
            objectOutputStream.writeFields();
            return;
        }
        objectOutputStream.defaultWriteObject();
    }
}
