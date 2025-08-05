package javax.management.relation;

import com.sun.jmx.defaults.JmxProperties;
import com.sun.jmx.mbeanserver.GetPropertyAction;
import com.sun.jmx.mbeanserver.Util;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamField;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/* loaded from: rt.jar:javax/management/relation/RelationTypeSupport.class */
public class RelationTypeSupport implements RelationType {
    private static final long oldSerialVersionUID = -8179019472410837190L;
    private static final long newSerialVersionUID = 4611072955724144607L;
    private static final ObjectStreamField[] oldSerialPersistentFields = {new ObjectStreamField("myTypeName", String.class), new ObjectStreamField("myRoleName2InfoMap", HashMap.class), new ObjectStreamField("myIsInRelServFlg", Boolean.TYPE)};
    private static final ObjectStreamField[] newSerialPersistentFields = {new ObjectStreamField("typeName", String.class), new ObjectStreamField("roleName2InfoMap", Map.class), new ObjectStreamField("isInRelationService", Boolean.TYPE)};
    private static final long serialVersionUID;
    private static final ObjectStreamField[] serialPersistentFields;
    private static boolean compat;
    private String typeName;
    private Map<String, RoleInfo> roleName2InfoMap;
    private boolean isInRelationService;

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

    public RelationTypeSupport(String str, RoleInfo[] roleInfoArr) throws InvalidRelationTypeException, IllegalArgumentException {
        this.typeName = null;
        this.roleName2InfoMap = new HashMap();
        this.isInRelationService = false;
        if (str == null || roleInfoArr == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        JmxProperties.RELATION_LOGGER.entering(RelationTypeSupport.class.getName(), "RelationTypeSupport", str);
        initMembers(str, roleInfoArr);
        JmxProperties.RELATION_LOGGER.exiting(RelationTypeSupport.class.getName(), "RelationTypeSupport");
    }

    protected RelationTypeSupport(String str) {
        this.typeName = null;
        this.roleName2InfoMap = new HashMap();
        this.isInRelationService = false;
        if (str == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        JmxProperties.RELATION_LOGGER.entering(RelationTypeSupport.class.getName(), "RelationTypeSupport", str);
        this.typeName = str;
        JmxProperties.RELATION_LOGGER.exiting(RelationTypeSupport.class.getName(), "RelationTypeSupport");
    }

    @Override // javax.management.relation.RelationType
    public String getRelationTypeName() {
        return this.typeName;
    }

    @Override // javax.management.relation.RelationType
    public List<RoleInfo> getRoleInfos() {
        return new ArrayList(this.roleName2InfoMap.values());
    }

    @Override // javax.management.relation.RelationType
    public RoleInfo getRoleInfo(String str) throws RoleInfoNotFoundException, IllegalArgumentException {
        if (str == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        JmxProperties.RELATION_LOGGER.entering(RelationTypeSupport.class.getName(), "getRoleInfo", str);
        RoleInfo roleInfo = this.roleName2InfoMap.get(str);
        if (roleInfo == null) {
            throw new RoleInfoNotFoundException("No role info for role " + str);
        }
        JmxProperties.RELATION_LOGGER.exiting(RelationTypeSupport.class.getName(), "getRoleInfo");
        return roleInfo;
    }

    protected void addRoleInfo(RoleInfo roleInfo) throws InvalidRelationTypeException, IllegalArgumentException {
        if (roleInfo == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        JmxProperties.RELATION_LOGGER.entering(RelationTypeSupport.class.getName(), "addRoleInfo", roleInfo);
        if (this.isInRelationService) {
            throw new RuntimeException("Relation type cannot be updated as it is declared in the Relation Service.");
        }
        String name = roleInfo.getName();
        if (this.roleName2InfoMap.containsKey(name)) {
            throw new InvalidRelationTypeException("Two role infos provided for role " + name);
        }
        this.roleName2InfoMap.put(name, new RoleInfo(roleInfo));
        JmxProperties.RELATION_LOGGER.exiting(RelationTypeSupport.class.getName(), "addRoleInfo");
    }

    void setRelationServiceFlag(boolean z2) {
        this.isInRelationService = z2;
    }

    private void initMembers(String str, RoleInfo[] roleInfoArr) throws InvalidRelationTypeException, IllegalArgumentException {
        if (str == null || roleInfoArr == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        JmxProperties.RELATION_LOGGER.entering(RelationTypeSupport.class.getName(), "initMembers", str);
        this.typeName = str;
        checkRoleInfos(roleInfoArr);
        for (RoleInfo roleInfo : roleInfoArr) {
            this.roleName2InfoMap.put(roleInfo.getName(), new RoleInfo(roleInfo));
        }
        JmxProperties.RELATION_LOGGER.exiting(RelationTypeSupport.class.getName(), "initMembers");
    }

    static void checkRoleInfos(RoleInfo[] roleInfoArr) throws InvalidRelationTypeException, IllegalArgumentException {
        if (roleInfoArr == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        if (roleInfoArr.length == 0) {
            throw new InvalidRelationTypeException("No role info provided.");
        }
        HashSet hashSet = new HashSet();
        for (RoleInfo roleInfo : roleInfoArr) {
            if (roleInfo == null) {
                throw new InvalidRelationTypeException("Null role info provided.");
            }
            String name = roleInfo.getName();
            if (hashSet.contains(name)) {
                throw new InvalidRelationTypeException("Two role infos provided for role " + name);
            }
            hashSet.add(name);
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        if (compat) {
            ObjectInputStream.GetField fields = objectInputStream.readFields();
            this.typeName = (String) fields.get("myTypeName", (Object) null);
            if (fields.defaulted("myTypeName")) {
                throw new NullPointerException("myTypeName");
            }
            this.roleName2InfoMap = (Map) Util.cast(fields.get("myRoleName2InfoMap", (Object) null));
            if (fields.defaulted("myRoleName2InfoMap")) {
                throw new NullPointerException("myRoleName2InfoMap");
            }
            this.isInRelationService = fields.get("myIsInRelServFlg", false);
            if (fields.defaulted("myIsInRelServFlg")) {
                throw new NullPointerException("myIsInRelServFlg");
            }
            return;
        }
        objectInputStream.defaultReadObject();
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        if (compat) {
            ObjectOutputStream.PutField putFieldPutFields = objectOutputStream.putFields();
            putFieldPutFields.put("myTypeName", this.typeName);
            putFieldPutFields.put("myRoleName2InfoMap", this.roleName2InfoMap);
            putFieldPutFields.put("myIsInRelServFlg", this.isInRelationService);
            objectOutputStream.writeFields();
            return;
        }
        objectOutputStream.defaultWriteObject();
    }
}
