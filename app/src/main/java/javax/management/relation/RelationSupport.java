package javax.management.relation;

import com.sun.jmx.defaults.JmxProperties;
import com.sun.jmx.mbeanserver.Util;
import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanRegistration;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.ReflectionException;

/* loaded from: rt.jar:javax/management/relation/RelationSupport.class */
public class RelationSupport implements RelationSupportMBean, MBeanRegistration {
    private String myRelId = null;
    private ObjectName myRelServiceName = null;
    private MBeanServer myRelServiceMBeanServer = null;
    private String myRelTypeName = null;
    private final Map<String, Role> myRoleName2ValueMap = new HashMap();
    private final AtomicBoolean myInRelServFlg = new AtomicBoolean();

    public RelationSupport(String str, ObjectName objectName, String str2, RoleList roleList) throws InvalidRoleValueException, IllegalArgumentException {
        JmxProperties.RELATION_LOGGER.entering(RelationSupport.class.getName(), "RelationSupport");
        initMembers(str, objectName, null, str2, roleList);
        JmxProperties.RELATION_LOGGER.exiting(RelationSupport.class.getName(), "RelationSupport");
    }

    public RelationSupport(String str, ObjectName objectName, MBeanServer mBeanServer, String str2, RoleList roleList) throws InvalidRoleValueException, IllegalArgumentException {
        if (mBeanServer == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        JmxProperties.RELATION_LOGGER.entering(RelationSupport.class.getName(), "RelationSupport");
        initMembers(str, objectName, mBeanServer, str2, roleList);
        JmxProperties.RELATION_LOGGER.exiting(RelationSupport.class.getName(), "RelationSupport");
    }

    @Override // javax.management.relation.Relation
    public List<ObjectName> getRole(String str) throws RoleNotFoundException, IllegalArgumentException, RelationServiceNotRegisteredException {
        if (str == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        JmxProperties.RELATION_LOGGER.entering(RelationSupport.class.getName(), "getRole", str);
        List<ObjectName> list = (List) Util.cast(getRoleInt(str, false, null, false));
        JmxProperties.RELATION_LOGGER.exiting(RelationSupport.class.getName(), "getRole");
        return list;
    }

    @Override // javax.management.relation.Relation
    public RoleResult getRoles(String[] strArr) throws IllegalArgumentException, RelationServiceNotRegisteredException {
        if (strArr == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        JmxProperties.RELATION_LOGGER.entering(RelationSupport.class.getName(), "getRoles");
        RoleResult rolesInt = getRolesInt(strArr, false, null);
        JmxProperties.RELATION_LOGGER.exiting(RelationSupport.class.getName(), "getRoles");
        return rolesInt;
    }

    @Override // javax.management.relation.Relation
    public RoleResult getAllRoles() throws RelationServiceNotRegisteredException {
        JmxProperties.RELATION_LOGGER.entering(RelationSupport.class.getName(), "getAllRoles");
        RoleResult allRolesInt = null;
        try {
            allRolesInt = getAllRolesInt(false, null);
        } catch (IllegalArgumentException e2) {
        }
        JmxProperties.RELATION_LOGGER.exiting(RelationSupport.class.getName(), "getAllRoles");
        return allRolesInt;
    }

    @Override // javax.management.relation.Relation
    public RoleList retrieveAllRoles() {
        RoleList roleList;
        JmxProperties.RELATION_LOGGER.entering(RelationSupport.class.getName(), "retrieveAllRoles");
        synchronized (this.myRoleName2ValueMap) {
            roleList = new RoleList(new ArrayList(this.myRoleName2ValueMap.values()));
        }
        JmxProperties.RELATION_LOGGER.exiting(RelationSupport.class.getName(), "retrieveAllRoles");
        return roleList;
    }

    @Override // javax.management.relation.Relation
    public Integer getRoleCardinality(String str) throws RoleNotFoundException, IllegalArgumentException {
        Role role;
        if (str == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        JmxProperties.RELATION_LOGGER.entering(RelationSupport.class.getName(), "getRoleCardinality", str);
        synchronized (this.myRoleName2ValueMap) {
            role = this.myRoleName2ValueMap.get(str);
        }
        if (role == null) {
            try {
                RelationService.throwRoleProblemException(1, str);
            } catch (InvalidRoleValueException e2) {
            }
        }
        List<ObjectName> roleValue = role.getRoleValue();
        JmxProperties.RELATION_LOGGER.exiting(RelationSupport.class.getName(), "getRoleCardinality");
        return Integer.valueOf(roleValue.size());
    }

    @Override // javax.management.relation.Relation
    public void setRole(Role role) throws RoleNotFoundException, RelationTypeNotFoundException, InvalidRoleValueException, IllegalArgumentException, RelationServiceNotRegisteredException, RelationNotFoundException {
        if (role == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        JmxProperties.RELATION_LOGGER.entering(RelationSupport.class.getName(), "setRole", role);
        setRoleInt(role, false, null, false);
        JmxProperties.RELATION_LOGGER.exiting(RelationSupport.class.getName(), "setRole");
    }

    @Override // javax.management.relation.Relation
    public RoleResult setRoles(RoleList roleList) throws RelationTypeNotFoundException, IllegalArgumentException, RelationServiceNotRegisteredException, RelationNotFoundException {
        if (roleList == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        JmxProperties.RELATION_LOGGER.entering(RelationSupport.class.getName(), "setRoles", roleList);
        RoleResult rolesInt = setRolesInt(roleList, false, null);
        JmxProperties.RELATION_LOGGER.exiting(RelationSupport.class.getName(), "setRoles");
        return rolesInt;
    }

    @Override // javax.management.relation.Relation
    public void handleMBeanUnregistration(ObjectName objectName, String str) throws RoleNotFoundException, RelationTypeNotFoundException, InvalidRoleValueException, IllegalArgumentException, RelationServiceNotRegisteredException, RelationNotFoundException {
        if (objectName == null || str == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        JmxProperties.RELATION_LOGGER.entering(RelationSupport.class.getName(), "handleMBeanUnregistration", new Object[]{objectName, str});
        handleMBeanUnregistrationInt(objectName, str, false, null);
        JmxProperties.RELATION_LOGGER.exiting(RelationSupport.class.getName(), "handleMBeanUnregistration");
    }

    @Override // javax.management.relation.Relation
    public Map<ObjectName, List<String>> getReferencedMBeans() {
        JmxProperties.RELATION_LOGGER.entering(RelationSupport.class.getName(), "getReferencedMBeans");
        HashMap map = new HashMap();
        synchronized (this.myRoleName2ValueMap) {
            for (Role role : this.myRoleName2ValueMap.values()) {
                String roleName = role.getRoleName();
                for (ObjectName objectName : role.getRoleValue()) {
                    List arrayList = (List) map.get(objectName);
                    boolean z2 = false;
                    if (arrayList == null) {
                        z2 = true;
                        arrayList = new ArrayList();
                    }
                    arrayList.add(roleName);
                    if (z2) {
                        map.put(objectName, arrayList);
                    }
                }
            }
        }
        JmxProperties.RELATION_LOGGER.exiting(RelationSupport.class.getName(), "getReferencedMBeans");
        return map;
    }

    @Override // javax.management.relation.Relation
    public String getRelationTypeName() {
        return this.myRelTypeName;
    }

    @Override // javax.management.relation.Relation
    public ObjectName getRelationServiceName() {
        return this.myRelServiceName;
    }

    @Override // javax.management.relation.Relation
    public String getRelationId() {
        return this.myRelId;
    }

    @Override // javax.management.MBeanRegistration
    public ObjectName preRegister(MBeanServer mBeanServer, ObjectName objectName) throws Exception {
        this.myRelServiceMBeanServer = mBeanServer;
        return objectName;
    }

    @Override // javax.management.MBeanRegistration
    public void postRegister(Boolean bool) {
    }

    @Override // javax.management.MBeanRegistration
    public void preDeregister() throws Exception {
    }

    @Override // javax.management.MBeanRegistration
    public void postDeregister() {
    }

    @Override // javax.management.relation.RelationSupportMBean
    public Boolean isInRelationService() {
        return Boolean.valueOf(this.myInRelServFlg.get());
    }

    @Override // javax.management.relation.RelationSupportMBean
    public void setRelationServiceManagementFlag(Boolean bool) throws IllegalArgumentException {
        if (bool == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        this.myInRelServFlg.set(bool.booleanValue());
    }

    Object getRoleInt(String str, boolean z2, RelationService relationService, boolean z3) throws RoleNotFoundException, IllegalArgumentException, RelationServiceNotRegisteredException {
        Role role;
        Integer numCheckRoleReading;
        int iIntValue;
        Object roleUnresolved;
        if (str == null || (z2 && relationService == null)) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        JmxProperties.RELATION_LOGGER.entering(RelationSupport.class.getName(), "getRoleInt", str);
        synchronized (this.myRoleName2ValueMap) {
            role = this.myRoleName2ValueMap.get(str);
        }
        if (role == null) {
            iIntValue = 1;
        } else {
            if (z2) {
                try {
                    numCheckRoleReading = relationService.checkRoleReading(str, this.myRelTypeName);
                } catch (RelationTypeNotFoundException e2) {
                    throw new RuntimeException(e2.getMessage());
                }
            } else {
                try {
                    numCheckRoleReading = (Integer) this.myRelServiceMBeanServer.invoke(this.myRelServiceName, "checkRoleReading", new Object[]{str, this.myRelTypeName}, new String[]{"java.lang.String", "java.lang.String"});
                } catch (InstanceNotFoundException e3) {
                    throw new RelationServiceNotRegisteredException(e3.getMessage());
                } catch (MBeanException e4) {
                    throw new RuntimeException("incorrect relation type");
                } catch (ReflectionException e5) {
                    throw new RuntimeException(e5.getMessage());
                }
            }
            iIntValue = numCheckRoleReading.intValue();
        }
        if (iIntValue == 0) {
            if (!z3) {
                roleUnresolved = new ArrayList(role.getRoleValue());
            } else {
                roleUnresolved = (Role) role.clone();
            }
        } else {
            if (!z3) {
                try {
                    RelationService.throwRoleProblemException(iIntValue, str);
                    return null;
                } catch (InvalidRoleValueException e6) {
                    throw new RuntimeException(e6.getMessage());
                }
            }
            roleUnresolved = new RoleUnresolved(str, null, iIntValue);
        }
        JmxProperties.RELATION_LOGGER.exiting(RelationSupport.class.getName(), "getRoleInt");
        return roleUnresolved;
    }

    RoleResult getRolesInt(String[] strArr, boolean z2, RelationService relationService) throws IllegalArgumentException, RelationServiceNotRegisteredException {
        if (strArr == null || (z2 && relationService == null)) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        JmxProperties.RELATION_LOGGER.entering(RelationSupport.class.getName(), "getRolesInt");
        RoleList roleList = new RoleList();
        RoleUnresolvedList roleUnresolvedList = new RoleUnresolvedList();
        for (String str : strArr) {
            try {
                Object roleInt = getRoleInt(str, z2, relationService, true);
                if (roleInt instanceof Role) {
                    try {
                        roleList.add((Role) roleInt);
                    } catch (IllegalArgumentException e2) {
                        throw new RuntimeException(e2.getMessage());
                    }
                } else if (roleInt instanceof RoleUnresolved) {
                    try {
                        roleUnresolvedList.add((RoleUnresolved) roleInt);
                    } catch (IllegalArgumentException e3) {
                        throw new RuntimeException(e3.getMessage());
                    }
                } else {
                    continue;
                }
            } catch (RoleNotFoundException e4) {
                return null;
            }
        }
        RoleResult roleResult = new RoleResult(roleList, roleUnresolvedList);
        JmxProperties.RELATION_LOGGER.exiting(RelationSupport.class.getName(), "getRolesInt");
        return roleResult;
    }

    RoleResult getAllRolesInt(boolean z2, RelationService relationService) throws IllegalArgumentException, RelationServiceNotRegisteredException {
        ArrayList arrayList;
        if (z2 && relationService == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        JmxProperties.RELATION_LOGGER.entering(RelationSupport.class.getName(), "getAllRolesInt");
        synchronized (this.myRoleName2ValueMap) {
            arrayList = new ArrayList(this.myRoleName2ValueMap.keySet());
        }
        String[] strArr = new String[arrayList.size()];
        arrayList.toArray(strArr);
        RoleResult rolesInt = getRolesInt(strArr, z2, relationService);
        JmxProperties.RELATION_LOGGER.exiting(RelationSupport.class.getName(), "getAllRolesInt");
        return rolesInt;
    }

    Object setRoleInt(Role role, boolean z2, RelationService relationService, boolean z3) throws RoleNotFoundException, RelationTypeNotFoundException, InvalidRoleValueException, IllegalArgumentException, RelationServiceNotRegisteredException, RelationNotFoundException {
        Role role2;
        Boolean bool;
        List<ObjectName> roleValue;
        Integer numCheckRoleWriting;
        if (role == null || (z2 && relationService == null)) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        JmxProperties.RELATION_LOGGER.entering(RelationSupport.class.getName(), "setRoleInt", new Object[]{role, Boolean.valueOf(z2), relationService, Boolean.valueOf(z3)});
        String roleName = role.getRoleName();
        synchronized (this.myRoleName2ValueMap) {
            role2 = this.myRoleName2ValueMap.get(roleName);
        }
        if (role2 == null) {
            bool = true;
            roleValue = new ArrayList();
        } else {
            bool = false;
            roleValue = role2.getRoleValue();
        }
        try {
            if (z2) {
                numCheckRoleWriting = relationService.checkRoleWriting(role, this.myRelTypeName, bool);
            } else {
                numCheckRoleWriting = (Integer) this.myRelServiceMBeanServer.invoke(this.myRelServiceName, "checkRoleWriting", new Object[]{role, this.myRelTypeName, bool}, new String[]{"javax.management.relation.Role", "java.lang.String", Constants.BOOLEAN_CLASS});
            }
            int iIntValue = numCheckRoleWriting.intValue();
            Object roleUnresolved = null;
            if (iIntValue == 0) {
                if (!bool.booleanValue()) {
                    sendRoleUpdateNotification(role, roleValue, z2, relationService);
                    updateRelationServiceMap(role, roleValue, z2, relationService);
                }
                synchronized (this.myRoleName2ValueMap) {
                    this.myRoleName2ValueMap.put(roleName, (Role) role.clone());
                }
                if (z3) {
                    roleUnresolved = role;
                }
            } else {
                if (!z3) {
                    RelationService.throwRoleProblemException(iIntValue, roleName);
                    return null;
                }
                roleUnresolved = new RoleUnresolved(roleName, role.getRoleValue(), iIntValue);
            }
            JmxProperties.RELATION_LOGGER.exiting(RelationSupport.class.getName(), "setRoleInt");
            return roleUnresolved;
        } catch (InstanceNotFoundException e2) {
            throw new RelationServiceNotRegisteredException(e2.getMessage());
        } catch (MBeanException e3) {
            Exception targetException = e3.getTargetException();
            if (targetException instanceof RelationTypeNotFoundException) {
                throw ((RelationTypeNotFoundException) targetException);
            }
            throw new RuntimeException(targetException.getMessage());
        } catch (ReflectionException e4) {
            throw new RuntimeException(e4.getMessage());
        } catch (RelationTypeNotFoundException e5) {
            throw new RuntimeException(e5.getMessage());
        }
    }

    private void sendRoleUpdateNotification(Role role, List<ObjectName> list, boolean z2, RelationService relationService) throws IllegalArgumentException, RelationServiceNotRegisteredException, RelationNotFoundException {
        if (role == null || list == null || (z2 && relationService == null)) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        JmxProperties.RELATION_LOGGER.entering(RelationSupport.class.getName(), "sendRoleUpdateNotification", new Object[]{role, list, Boolean.valueOf(z2), relationService});
        if (z2) {
            try {
                relationService.sendRoleUpdateNotification(this.myRelId, role, list);
            } catch (RelationNotFoundException e2) {
                throw new RuntimeException(e2.getMessage());
            }
        } else {
            try {
                this.myRelServiceMBeanServer.invoke(this.myRelServiceName, "sendRoleUpdateNotification", new Object[]{this.myRelId, role, list}, new String[]{"java.lang.String", "javax.management.relation.Role", "java.util.List"});
            } catch (InstanceNotFoundException e3) {
                throw new RelationServiceNotRegisteredException(e3.getMessage());
            } catch (MBeanException e4) {
                Exception targetException = e4.getTargetException();
                if (targetException instanceof RelationNotFoundException) {
                    throw ((RelationNotFoundException) targetException);
                }
                throw new RuntimeException(targetException.getMessage());
            } catch (ReflectionException e5) {
                throw new RuntimeException(e5.getMessage());
            }
        }
        JmxProperties.RELATION_LOGGER.exiting(RelationSupport.class.getName(), "sendRoleUpdateNotification");
    }

    private void updateRelationServiceMap(Role role, List<ObjectName> list, boolean z2, RelationService relationService) throws IllegalArgumentException, RelationServiceNotRegisteredException, RelationNotFoundException {
        if (role == null || list == null || (z2 && relationService == null)) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        JmxProperties.RELATION_LOGGER.entering(RelationSupport.class.getName(), "updateRelationServiceMap", new Object[]{role, list, Boolean.valueOf(z2), relationService});
        if (z2) {
            try {
                relationService.updateRoleMap(this.myRelId, role, list);
            } catch (RelationNotFoundException e2) {
                throw new RuntimeException(e2.getMessage());
            }
        } else {
            try {
                this.myRelServiceMBeanServer.invoke(this.myRelServiceName, "updateRoleMap", new Object[]{this.myRelId, role, list}, new String[]{"java.lang.String", "javax.management.relation.Role", "java.util.List"});
            } catch (InstanceNotFoundException e3) {
                throw new RelationServiceNotRegisteredException(e3.getMessage());
            } catch (MBeanException e4) {
                Exception targetException = e4.getTargetException();
                if (targetException instanceof RelationNotFoundException) {
                    throw ((RelationNotFoundException) targetException);
                }
                throw new RuntimeException(targetException.getMessage());
            } catch (ReflectionException e5) {
                throw new RuntimeException(e5.getMessage());
            }
        }
        JmxProperties.RELATION_LOGGER.exiting(RelationSupport.class.getName(), "updateRelationServiceMap");
    }

    RoleResult setRolesInt(RoleList roleList, boolean z2, RelationService relationService) throws RelationTypeNotFoundException, IllegalArgumentException, RelationServiceNotRegisteredException, RelationNotFoundException {
        if (roleList == null || (z2 && relationService == null)) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        JmxProperties.RELATION_LOGGER.entering(RelationSupport.class.getName(), "setRolesInt", new Object[]{roleList, Boolean.valueOf(z2), relationService});
        RoleList roleList2 = new RoleList();
        RoleUnresolvedList roleUnresolvedList = new RoleUnresolvedList();
        Iterator<Role> it = roleList.asList().iterator();
        while (it.hasNext()) {
            Object roleInt = null;
            try {
                roleInt = setRoleInt(it.next(), z2, relationService, true);
            } catch (InvalidRoleValueException e2) {
            } catch (RoleNotFoundException e3) {
            }
            if (roleInt instanceof Role) {
                try {
                    roleList2.add((Role) roleInt);
                } catch (IllegalArgumentException e4) {
                    throw new RuntimeException(e4.getMessage());
                }
            } else if (roleInt instanceof RoleUnresolved) {
                try {
                    roleUnresolvedList.add((RoleUnresolved) roleInt);
                } catch (IllegalArgumentException e5) {
                    throw new RuntimeException(e5.getMessage());
                }
            } else {
                continue;
            }
        }
        RoleResult roleResult = new RoleResult(roleList2, roleUnresolvedList);
        JmxProperties.RELATION_LOGGER.exiting(RelationSupport.class.getName(), "setRolesInt");
        return roleResult;
    }

    private void initMembers(String str, ObjectName objectName, MBeanServer mBeanServer, String str2, RoleList roleList) throws InvalidRoleValueException, IllegalArgumentException {
        if (str == null || objectName == null || str2 == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        JmxProperties.RELATION_LOGGER.entering(RelationSupport.class.getName(), "initMembers", new Object[]{str, objectName, mBeanServer, str2, roleList});
        this.myRelId = str;
        this.myRelServiceName = objectName;
        this.myRelServiceMBeanServer = mBeanServer;
        this.myRelTypeName = str2;
        initRoleMap(roleList);
        JmxProperties.RELATION_LOGGER.exiting(RelationSupport.class.getName(), "initMembers");
    }

    private void initRoleMap(RoleList roleList) throws InvalidRoleValueException {
        if (roleList == null) {
            return;
        }
        JmxProperties.RELATION_LOGGER.entering(RelationSupport.class.getName(), "initRoleMap", roleList);
        synchronized (this.myRoleName2ValueMap) {
            for (Role role : roleList.asList()) {
                String roleName = role.getRoleName();
                if (this.myRoleName2ValueMap.containsKey(roleName)) {
                    throw new InvalidRoleValueException("Role name " + roleName + " used for two roles.");
                }
                this.myRoleName2ValueMap.put(roleName, (Role) role.clone());
            }
        }
        JmxProperties.RELATION_LOGGER.exiting(RelationSupport.class.getName(), "initRoleMap");
    }

    void handleMBeanUnregistrationInt(ObjectName objectName, String str, boolean z2, RelationService relationService) throws RoleNotFoundException, RelationTypeNotFoundException, InvalidRoleValueException, IllegalArgumentException, RelationServiceNotRegisteredException, RelationNotFoundException {
        Role role;
        if (objectName == null || str == null || (z2 && relationService == null)) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        JmxProperties.RELATION_LOGGER.entering(RelationSupport.class.getName(), "handleMBeanUnregistrationInt", new Object[]{objectName, str, Boolean.valueOf(z2), relationService});
        synchronized (this.myRoleName2ValueMap) {
            role = this.myRoleName2ValueMap.get(str);
        }
        if (role == null) {
            throw new RoleNotFoundException("No role with name " + str);
        }
        ArrayList arrayList = new ArrayList(role.getRoleValue());
        arrayList.remove(objectName);
        setRoleInt(new Role(str, arrayList), z2, relationService, false);
        JmxProperties.RELATION_LOGGER.exiting(RelationSupport.class.getName(), "handleMBeanUnregistrationInt");
    }
}
