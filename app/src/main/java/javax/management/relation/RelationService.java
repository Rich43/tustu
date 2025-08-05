package javax.management.relation;

import com.sun.jmx.defaults.JmxProperties;
import com.sun.jmx.mbeanserver.Util;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import javax.management.Attribute;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanException;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanRegistration;
import javax.management.MBeanServer;
import javax.management.MBeanServerDelegate;
import javax.management.MBeanServerNotification;
import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import javax.management.ReflectionException;

/* loaded from: rt.jar:javax/management/relation/RelationService.class */
public class RelationService extends NotificationBroadcasterSupport implements RelationServiceMBean, MBeanRegistration, NotificationListener {
    private Map<String, Object> myRelId2ObjMap = new HashMap();
    private Map<String, String> myRelId2RelTypeMap = new HashMap();
    private Map<ObjectName, String> myRelMBeanObjName2RelIdMap = new HashMap();
    private Map<String, RelationType> myRelType2ObjMap = new HashMap();
    private Map<String, List<String>> myRelType2RelIdsMap = new HashMap();
    private final Map<ObjectName, Map<String, List<String>>> myRefedMBeanObjName2RelIdsMap = new HashMap();
    private boolean myPurgeFlag = true;
    private final AtomicLong atomicSeqNo = new AtomicLong();
    private ObjectName myObjName = null;
    private MBeanServer myMBeanServer = null;
    private MBeanServerNotificationFilter myUnregNtfFilter = null;
    private List<MBeanServerNotification> myUnregNtfList = new ArrayList();

    public RelationService(boolean z2) {
        JmxProperties.RELATION_LOGGER.entering(RelationService.class.getName(), "RelationService");
        setPurgeFlag(z2);
        JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "RelationService");
    }

    @Override // javax.management.relation.RelationServiceMBean
    public void isActive() throws RelationServiceNotRegisteredException {
        if (this.myMBeanServer == null) {
            throw new RelationServiceNotRegisteredException("Relation Service not registered in the MBean Server.");
        }
    }

    @Override // javax.management.MBeanRegistration
    public ObjectName preRegister(MBeanServer mBeanServer, ObjectName objectName) throws Exception {
        this.myMBeanServer = mBeanServer;
        this.myObjName = objectName;
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

    @Override // javax.management.relation.RelationServiceMBean
    public boolean getPurgeFlag() {
        return this.myPurgeFlag;
    }

    @Override // javax.management.relation.RelationServiceMBean
    public void setPurgeFlag(boolean z2) {
        this.myPurgeFlag = z2;
    }

    @Override // javax.management.relation.RelationServiceMBean
    public void createRelationType(String str, RoleInfo[] roleInfoArr) throws InvalidRelationTypeException, IllegalArgumentException {
        if (str == null || roleInfoArr == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        JmxProperties.RELATION_LOGGER.entering(RelationService.class.getName(), "createRelationType", str);
        addRelationTypeInt(new RelationTypeSupport(str, roleInfoArr));
        JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "createRelationType");
    }

    @Override // javax.management.relation.RelationServiceMBean
    public void addRelationType(RelationType relationType) throws InvalidRelationTypeException, IllegalArgumentException {
        if (relationType == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        JmxProperties.RELATION_LOGGER.entering(RelationService.class.getName(), "addRelationType");
        List<RoleInfo> roleInfos = relationType.getRoleInfos();
        if (roleInfos == null) {
            throw new InvalidRelationTypeException("No role info provided.");
        }
        RoleInfo[] roleInfoArr = new RoleInfo[roleInfos.size()];
        int i2 = 0;
        Iterator<RoleInfo> it = roleInfos.iterator();
        while (it.hasNext()) {
            roleInfoArr[i2] = it.next();
            i2++;
        }
        RelationTypeSupport.checkRoleInfos(roleInfoArr);
        addRelationTypeInt(relationType);
        JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "addRelationType");
    }

    @Override // javax.management.relation.RelationServiceMBean
    public List<String> getAllRelationTypeNames() {
        ArrayList arrayList;
        synchronized (this.myRelType2ObjMap) {
            arrayList = new ArrayList(this.myRelType2ObjMap.keySet());
        }
        return arrayList;
    }

    @Override // javax.management.relation.RelationServiceMBean
    public List<RoleInfo> getRoleInfos(String str) throws RelationTypeNotFoundException, IllegalArgumentException {
        if (str == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        JmxProperties.RELATION_LOGGER.entering(RelationService.class.getName(), "getRoleInfos", str);
        RelationType relationType = getRelationType(str);
        JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "getRoleInfos");
        return relationType.getRoleInfos();
    }

    @Override // javax.management.relation.RelationServiceMBean
    public RoleInfo getRoleInfo(String str, String str2) throws RoleInfoNotFoundException, RelationTypeNotFoundException, IllegalArgumentException {
        if (str == null || str2 == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        JmxProperties.RELATION_LOGGER.entering(RelationService.class.getName(), "getRoleInfo", new Object[]{str, str2});
        RoleInfo roleInfo = getRelationType(str).getRoleInfo(str2);
        JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "getRoleInfo");
        return roleInfo;
    }

    @Override // javax.management.relation.RelationServiceMBean
    public void removeRelationType(String str) throws RelationTypeNotFoundException, IllegalArgumentException, RelationServiceNotRegisteredException {
        isActive();
        if (str == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        JmxProperties.RELATION_LOGGER.entering(RelationService.class.getName(), "removeRelationType", str);
        getRelationType(str);
        ArrayList arrayList = null;
        synchronized (this.myRelType2RelIdsMap) {
            List<String> list = this.myRelType2RelIdsMap.get(str);
            if (list != null) {
                arrayList = new ArrayList(list);
            }
        }
        synchronized (this.myRelType2ObjMap) {
            this.myRelType2ObjMap.remove(str);
        }
        synchronized (this.myRelType2RelIdsMap) {
            this.myRelType2RelIdsMap.remove(str);
        }
        if (arrayList != null) {
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                try {
                    removeRelation((String) it.next());
                } catch (RelationNotFoundException e2) {
                    throw new RuntimeException(e2.getMessage());
                }
            }
        }
        JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "removeRelationType");
    }

    @Override // javax.management.relation.RelationServiceMBean
    public void createRelation(String str, String str2, RoleList roleList) throws RoleNotFoundException, RelationTypeNotFoundException, InvalidRelationIdException, InvalidRoleValueException, IllegalArgumentException, RelationServiceNotRegisteredException {
        isActive();
        if (str == null || str2 == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        JmxProperties.RELATION_LOGGER.entering(RelationService.class.getName(), "createRelation", new Object[]{str, str2, roleList});
        addRelationInt(true, new RelationSupport(str, this.myObjName, str2, roleList), null, str, str2, roleList);
        JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "createRelation");
    }

    @Override // javax.management.relation.RelationServiceMBean
    public void addRelation(ObjectName objectName) throws RoleNotFoundException, InvalidRelationServiceException, NoSuchMethodException, RelationTypeNotFoundException, InvalidRelationIdException, InvalidRoleValueException, IllegalArgumentException, RelationServiceNotRegisteredException, InstanceNotFoundException {
        if (objectName == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        JmxProperties.RELATION_LOGGER.entering(RelationService.class.getName(), "addRelation", objectName);
        isActive();
        if (!this.myMBeanServer.isInstanceOf(objectName, "javax.management.relation.Relation")) {
            throw new NoSuchMethodException("This MBean does not implement the Relation interface.");
        }
        try {
            String str = (String) this.myMBeanServer.getAttribute(objectName, "RelationId");
            if (str == null) {
                throw new InvalidRelationIdException("This MBean does not provide a relation id.");
            }
            try {
                ObjectName objectName2 = (ObjectName) this.myMBeanServer.getAttribute(objectName, "RelationServiceName");
                boolean z2 = false;
                if (objectName2 == null || !objectName2.equals(this.myObjName)) {
                    z2 = true;
                }
                if (z2) {
                    throw new InvalidRelationServiceException("The Relation Service referenced in the MBean is not the current one.");
                }
                try {
                    String str2 = (String) this.myMBeanServer.getAttribute(objectName, "RelationTypeName");
                    if (str2 == null) {
                        throw new RelationTypeNotFoundException("No relation type provided.");
                    }
                    try {
                        addRelationInt(false, null, objectName, str, str2, (RoleList) this.myMBeanServer.invoke(objectName, "retrieveAllRoles", null, null));
                        synchronized (this.myRelMBeanObjName2RelIdMap) {
                            this.myRelMBeanObjName2RelIdMap.put(objectName, str);
                        }
                        try {
                            this.myMBeanServer.setAttribute(objectName, new Attribute("RelationServiceManagementFlag", Boolean.TRUE));
                        } catch (Exception e2) {
                        }
                        ArrayList arrayList = new ArrayList();
                        arrayList.add(objectName);
                        updateUnregistrationListener(arrayList, null);
                        JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "addRelation");
                    } catch (MBeanException e3) {
                        throw new RuntimeException(e3.getTargetException().getMessage());
                    } catch (ReflectionException e4) {
                        throw new RuntimeException(e4.getMessage());
                    }
                } catch (AttributeNotFoundException e5) {
                    throw new RuntimeException(e5.getMessage());
                } catch (MBeanException e6) {
                    throw new RuntimeException(e6.getTargetException().getMessage());
                } catch (ReflectionException e7) {
                    throw new RuntimeException(e7.getMessage());
                }
            } catch (AttributeNotFoundException e8) {
                throw new RuntimeException(e8.getMessage());
            } catch (MBeanException e9) {
                throw new RuntimeException(e9.getTargetException().getMessage());
            } catch (ReflectionException e10) {
                throw new RuntimeException(e10.getMessage());
            }
        } catch (AttributeNotFoundException e11) {
            throw new RuntimeException(e11.getMessage());
        } catch (MBeanException e12) {
            throw new RuntimeException(e12.getTargetException().getMessage());
        } catch (ReflectionException e13) {
            throw new RuntimeException(e13.getMessage());
        }
    }

    @Override // javax.management.relation.RelationServiceMBean
    public ObjectName isRelationMBean(String str) throws IllegalArgumentException, RelationNotFoundException {
        if (str == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        JmxProperties.RELATION_LOGGER.entering(RelationService.class.getName(), "isRelationMBean", str);
        Object relation = getRelation(str);
        if (relation instanceof ObjectName) {
            return (ObjectName) relation;
        }
        return null;
    }

    @Override // javax.management.relation.RelationServiceMBean
    public String isRelation(ObjectName objectName) throws IllegalArgumentException {
        if (objectName == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        JmxProperties.RELATION_LOGGER.entering(RelationService.class.getName(), "isRelation", objectName);
        String str = null;
        synchronized (this.myRelMBeanObjName2RelIdMap) {
            String str2 = this.myRelMBeanObjName2RelIdMap.get(objectName);
            if (str2 != null) {
                str = str2;
            }
        }
        return str;
    }

    @Override // javax.management.relation.RelationServiceMBean
    public Boolean hasRelation(String str) throws IllegalArgumentException {
        if (str == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        JmxProperties.RELATION_LOGGER.entering(RelationService.class.getName(), "hasRelation", str);
        try {
            getRelation(str);
            return true;
        } catch (RelationNotFoundException e2) {
            return false;
        }
    }

    @Override // javax.management.relation.RelationServiceMBean
    public List<String> getAllRelationIds() {
        ArrayList arrayList;
        synchronized (this.myRelId2ObjMap) {
            arrayList = new ArrayList(this.myRelId2ObjMap.keySet());
        }
        return arrayList;
    }

    @Override // javax.management.relation.RelationServiceMBean
    public Integer checkRoleReading(String str, String str2) throws RelationTypeNotFoundException, IllegalArgumentException {
        Integer numCheckRoleInt;
        if (str == null || str2 == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        JmxProperties.RELATION_LOGGER.entering(RelationService.class.getName(), "checkRoleReading", new Object[]{str, str2});
        try {
            numCheckRoleInt = checkRoleInt(1, str, null, getRelationType(str2).getRoleInfo(str), false);
        } catch (RoleInfoNotFoundException e2) {
            numCheckRoleInt = 1;
        }
        JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "checkRoleReading");
        return numCheckRoleInt;
    }

    @Override // javax.management.relation.RelationServiceMBean
    public Integer checkRoleWriting(Role role, String str, Boolean bool) throws RelationTypeNotFoundException, IllegalArgumentException {
        if (role == null || str == null || bool == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        JmxProperties.RELATION_LOGGER.entering(RelationService.class.getName(), "checkRoleWriting", new Object[]{role, str, bool});
        RelationType relationType = getRelationType(str);
        String roleName = role.getRoleName();
        List<ObjectName> roleValue = role.getRoleValue();
        boolean z2 = true;
        if (bool.booleanValue()) {
            z2 = false;
        }
        try {
            Integer numCheckRoleInt = checkRoleInt(2, roleName, roleValue, relationType.getRoleInfo(roleName), z2);
            JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "checkRoleWriting");
            return numCheckRoleInt;
        } catch (RoleInfoNotFoundException e2) {
            JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "checkRoleWriting");
            return 1;
        }
    }

    @Override // javax.management.relation.RelationServiceMBean
    public void sendRelationCreationNotification(String str) throws IllegalArgumentException, RelationNotFoundException {
        if (str == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        JmxProperties.RELATION_LOGGER.entering(RelationService.class.getName(), "sendRelationCreationNotification", str);
        sendNotificationInt(1, "Creation of relation " + str, str, null, null, null, null);
        JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "sendRelationCreationNotification");
    }

    @Override // javax.management.relation.RelationServiceMBean
    public void sendRoleUpdateNotification(String str, Role role, List<ObjectName> list) throws IllegalArgumentException, RelationNotFoundException {
        if (str == null || role == null || list == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        if (!(list instanceof ArrayList)) {
            list = new ArrayList(list);
        }
        JmxProperties.RELATION_LOGGER.entering(RelationService.class.getName(), "sendRoleUpdateNotification", new Object[]{str, role, list});
        String roleName = role.getRoleName();
        List<ObjectName> roleValue = role.getRoleValue();
        String strRoleValueToString = Role.roleValueToString(roleValue);
        sendNotificationInt(2, "Value of role " + roleName + " has changed\nOld value:\n" + Role.roleValueToString(list) + "\nNew value:\n" + strRoleValueToString, str, null, roleName, roleValue, list);
        JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "sendRoleUpdateNotification");
    }

    @Override // javax.management.relation.RelationServiceMBean
    public void sendRelationRemovalNotification(String str, List<ObjectName> list) throws IllegalArgumentException, RelationNotFoundException {
        if (str == null) {
            throw new IllegalArgumentException("Invalid parameter");
        }
        JmxProperties.RELATION_LOGGER.entering(RelationService.class.getName(), "sendRelationRemovalNotification", new Object[]{str, list});
        sendNotificationInt(3, "Removal of relation " + str, str, list, null, null, null);
        JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "sendRelationRemovalNotification");
    }

    @Override // javax.management.relation.RelationServiceMBean
    public void updateRoleMap(String str, Role role, List<ObjectName> list) throws IllegalArgumentException, RelationServiceNotRegisteredException, RelationNotFoundException {
        if (str == null || role == null || list == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        JmxProperties.RELATION_LOGGER.entering(RelationService.class.getName(), "updateRoleMap", new Object[]{str, role, list});
        isActive();
        getRelation(str);
        String roleName = role.getRoleName();
        List<ObjectName> roleValue = role.getRoleValue();
        ArrayList<ObjectName> arrayList = new ArrayList(list);
        ArrayList arrayList2 = new ArrayList();
        for (ObjectName objectName : roleValue) {
            int iIndexOf = arrayList.indexOf(objectName);
            if (iIndexOf == -1) {
                if (addNewMBeanReference(objectName, str, roleName)) {
                    arrayList2.add(objectName);
                }
            } else {
                arrayList.remove(iIndexOf);
            }
        }
        ArrayList arrayList3 = new ArrayList();
        for (ObjectName objectName2 : arrayList) {
            if (removeMBeanReference(objectName2, str, roleName, false)) {
                arrayList3.add(objectName2);
            }
        }
        updateUnregistrationListener(arrayList2, arrayList3);
        JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "updateRoleMap");
    }

    @Override // javax.management.relation.RelationServiceMBean
    public void removeRelation(String str) throws IllegalArgumentException, RelationServiceNotRegisteredException, RelationNotFoundException {
        String str2;
        isActive();
        if (str == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        JmxProperties.RELATION_LOGGER.entering(RelationService.class.getName(), "removeRelation", str);
        Object relation = getRelation(str);
        if (relation instanceof ObjectName) {
            ArrayList arrayList = new ArrayList();
            arrayList.add((ObjectName) relation);
            updateUnregistrationListener(null, arrayList);
        }
        sendRelationRemovalNotification(str, null);
        ArrayList arrayList2 = new ArrayList();
        ArrayList arrayList3 = new ArrayList();
        synchronized (this.myRefedMBeanObjName2RelIdsMap) {
            for (ObjectName objectName : this.myRefedMBeanObjName2RelIdsMap.keySet()) {
                Map<String, List<String>> map = this.myRefedMBeanObjName2RelIdsMap.get(objectName);
                if (map.containsKey(str)) {
                    map.remove(str);
                    arrayList2.add(objectName);
                }
                if (map.isEmpty()) {
                    arrayList3.add(objectName);
                }
            }
            Iterator<E> it = arrayList3.iterator();
            while (it.hasNext()) {
                this.myRefedMBeanObjName2RelIdsMap.remove((ObjectName) it.next());
            }
        }
        synchronized (this.myRelId2ObjMap) {
            this.myRelId2ObjMap.remove(str);
        }
        if (relation instanceof ObjectName) {
            synchronized (this.myRelMBeanObjName2RelIdMap) {
                this.myRelMBeanObjName2RelIdMap.remove((ObjectName) relation);
            }
        }
        synchronized (this.myRelId2RelTypeMap) {
            str2 = this.myRelId2RelTypeMap.get(str);
            this.myRelId2RelTypeMap.remove(str);
        }
        synchronized (this.myRelType2RelIdsMap) {
            List<String> list = this.myRelType2RelIdsMap.get(str2);
            if (list != null) {
                list.remove(str);
                if (list.isEmpty()) {
                    this.myRelType2RelIdsMap.remove(str2);
                }
            }
        }
        JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "removeRelation");
    }

    @Override // javax.management.relation.RelationServiceMBean
    public void purgeRelations() throws IllegalArgumentException, RelationServiceNotRegisteredException {
        ArrayList arrayList;
        JmxProperties.RELATION_LOGGER.entering(RelationService.class.getName(), "purgeRelations");
        isActive();
        synchronized (this.myRefedMBeanObjName2RelIdsMap) {
            arrayList = new ArrayList(this.myUnregNtfList);
            this.myUnregNtfList = new ArrayList();
        }
        ArrayList arrayList2 = new ArrayList();
        HashMap map = new HashMap();
        synchronized (this.myRefedMBeanObjName2RelIdsMap) {
            Iterator<E> it = arrayList.iterator();
            while (it.hasNext()) {
                ObjectName mBeanName = ((MBeanServerNotification) it.next()).getMBeanName();
                arrayList2.add(mBeanName);
                map.put(mBeanName, this.myRefedMBeanObjName2RelIdsMap.get(mBeanName));
                this.myRefedMBeanObjName2RelIdsMap.remove(mBeanName);
            }
        }
        updateUnregistrationListener(null, arrayList2);
        Iterator<E> it2 = arrayList.iterator();
        while (it2.hasNext()) {
            ObjectName mBeanName2 = ((MBeanServerNotification) it2.next()).getMBeanName();
            for (Map.Entry entry : ((Map) map.get(mBeanName2)).entrySet()) {
                try {
                    handleReferenceUnregistration((String) entry.getKey(), mBeanName2, (List) entry.getValue());
                } catch (RelationNotFoundException e2) {
                    throw new RuntimeException(e2.getMessage());
                } catch (RoleNotFoundException e3) {
                    throw new RuntimeException(e3.getMessage());
                }
            }
        }
        JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "purgeRelations");
    }

    @Override // javax.management.relation.RelationServiceMBean
    public Map<String, List<String>> findReferencingRelations(ObjectName objectName, String str, String str2) throws IllegalArgumentException {
        ArrayList<String> arrayList;
        String str3;
        if (objectName == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        JmxProperties.RELATION_LOGGER.entering(RelationService.class.getName(), "findReferencingRelations", new Object[]{objectName, str, str2});
        HashMap map = new HashMap();
        synchronized (this.myRefedMBeanObjName2RelIdsMap) {
            Map<String, List<String>> map2 = this.myRefedMBeanObjName2RelIdsMap.get(objectName);
            if (map2 != null) {
                Set<String> setKeySet = map2.keySet();
                if (str == null) {
                    arrayList = new ArrayList(setKeySet);
                } else {
                    arrayList = new ArrayList();
                    for (String str4 : setKeySet) {
                        synchronized (this.myRelId2RelTypeMap) {
                            str3 = this.myRelId2RelTypeMap.get(str4);
                        }
                        if (str3.equals(str)) {
                            arrayList.add(str4);
                        }
                    }
                }
                for (String str5 : arrayList) {
                    List<String> list = map2.get(str5);
                    if (str2 == null) {
                        map.put(str5, new ArrayList(list));
                    } else if (list.contains(str2)) {
                        ArrayList arrayList2 = new ArrayList();
                        arrayList2.add(str2);
                        map.put(str5, arrayList2);
                    }
                }
            }
        }
        JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "findReferencingRelations");
        return map;
    }

    @Override // javax.management.relation.RelationServiceMBean
    public Map<ObjectName, List<String>> findAssociatedMBeans(ObjectName objectName, String str, String str2) throws IllegalArgumentException {
        if (objectName == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        JmxProperties.RELATION_LOGGER.entering(RelationService.class.getName(), "findAssociatedMBeans", new Object[]{objectName, str, str2});
        Map<String, List<String>> mapFindReferencingRelations = findReferencingRelations(objectName, str, str2);
        HashMap map = new HashMap();
        for (String str3 : mapFindReferencingRelations.keySet()) {
            try {
                for (ObjectName objectName2 : getReferencedMBeans(str3).keySet()) {
                    if (!objectName2.equals(objectName)) {
                        List list = (List) map.get(objectName2);
                        if (list == null) {
                            ArrayList arrayList = new ArrayList();
                            arrayList.add(str3);
                            map.put(objectName2, arrayList);
                        } else {
                            list.add(str3);
                        }
                    }
                }
            } catch (RelationNotFoundException e2) {
                throw new RuntimeException(e2.getMessage());
            }
        }
        JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "findAssociatedMBeans");
        return map;
    }

    @Override // javax.management.relation.RelationServiceMBean
    public List<String> findRelationsOfType(String str) throws RelationTypeNotFoundException, IllegalArgumentException {
        ArrayList arrayList;
        if (str == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        JmxProperties.RELATION_LOGGER.entering(RelationService.class.getName(), "findRelationsOfType");
        getRelationType(str);
        synchronized (this.myRelType2RelIdsMap) {
            List<String> list = this.myRelType2RelIdsMap.get(str);
            if (list == null) {
                arrayList = new ArrayList();
            } else {
                arrayList = new ArrayList(list);
            }
        }
        JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "findRelationsOfType");
        return arrayList;
    }

    @Override // javax.management.relation.RelationServiceMBean
    public List<ObjectName> getRole(String str, String str2) throws RoleNotFoundException, IllegalArgumentException, RelationServiceNotRegisteredException, RelationNotFoundException {
        List<ObjectName> arrayList;
        if (str == null || str2 == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        JmxProperties.RELATION_LOGGER.entering(RelationService.class.getName(), "getRole", new Object[]{str, str2});
        isActive();
        Object relation = getRelation(str);
        if (relation instanceof RelationSupport) {
            arrayList = (List) Util.cast(((RelationSupport) relation).getRoleInt(str2, true, this, false));
        } else {
            try {
                List<ObjectName> list = (List) Util.cast(this.myMBeanServer.invoke((ObjectName) relation, "getRole", new Object[]{str2}, new String[]{"java.lang.String"}));
                if (list == null || (list instanceof ArrayList)) {
                    arrayList = list;
                } else {
                    arrayList = new ArrayList(list);
                }
            } catch (InstanceNotFoundException e2) {
                throw new RuntimeException(e2.getMessage());
            } catch (MBeanException e3) {
                Exception targetException = e3.getTargetException();
                if (targetException instanceof RoleNotFoundException) {
                    throw ((RoleNotFoundException) targetException);
                }
                throw new RuntimeException(targetException.getMessage());
            } catch (ReflectionException e4) {
                throw new RuntimeException(e4.getMessage());
            }
        }
        JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "getRole");
        return arrayList;
    }

    @Override // javax.management.relation.RelationServiceMBean
    public RoleResult getRoles(String str, String[] strArr) throws IllegalArgumentException, RelationServiceNotRegisteredException, RelationNotFoundException {
        RoleResult rolesInt;
        if (str == null || strArr == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        JmxProperties.RELATION_LOGGER.entering(RelationService.class.getName(), "getRoles", str);
        isActive();
        Object relation = getRelation(str);
        if (relation instanceof RelationSupport) {
            rolesInt = ((RelationSupport) relation).getRolesInt(strArr, true, this);
        } else {
            Object[] objArr = {strArr};
            String[] strArr2 = new String[1];
            try {
                strArr2[0] = strArr.getClass().getName();
            } catch (Exception e2) {
            }
            try {
                rolesInt = (RoleResult) this.myMBeanServer.invoke((ObjectName) relation, "getRoles", objArr, strArr2);
            } catch (InstanceNotFoundException e3) {
                throw new RuntimeException(e3.getMessage());
            } catch (MBeanException e4) {
                throw new RuntimeException(e4.getTargetException().getMessage());
            } catch (ReflectionException e5) {
                throw new RuntimeException(e5.getMessage());
            }
        }
        JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "getRoles");
        return rolesInt;
    }

    @Override // javax.management.relation.RelationServiceMBean
    public RoleResult getAllRoles(String str) throws IllegalArgumentException, RelationNotFoundException, RelationServiceNotRegisteredException {
        RoleResult allRolesInt;
        if (str == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        JmxProperties.RELATION_LOGGER.entering(RelationService.class.getName(), "getRoles", str);
        Object relation = getRelation(str);
        if (relation instanceof RelationSupport) {
            allRolesInt = ((RelationSupport) relation).getAllRolesInt(true, this);
        } else {
            try {
                allRolesInt = (RoleResult) this.myMBeanServer.getAttribute((ObjectName) relation, "AllRoles");
            } catch (Exception e2) {
                throw new RuntimeException(e2.getMessage());
            }
        }
        JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "getRoles");
        return allRolesInt;
    }

    @Override // javax.management.relation.RelationServiceMBean
    public Integer getRoleCardinality(String str, String str2) throws RoleNotFoundException, IllegalArgumentException, RelationNotFoundException {
        Integer roleCardinality;
        if (str == null || str2 == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        JmxProperties.RELATION_LOGGER.entering(RelationService.class.getName(), "getRoleCardinality", new Object[]{str, str2});
        Object relation = getRelation(str);
        if (relation instanceof RelationSupport) {
            roleCardinality = ((RelationSupport) relation).getRoleCardinality(str2);
        } else {
            try {
                roleCardinality = (Integer) this.myMBeanServer.invoke((ObjectName) relation, "getRoleCardinality", new Object[]{str2}, new String[]{"java.lang.String"});
            } catch (InstanceNotFoundException e2) {
                throw new RuntimeException(e2.getMessage());
            } catch (MBeanException e3) {
                Exception targetException = e3.getTargetException();
                if (targetException instanceof RoleNotFoundException) {
                    throw ((RoleNotFoundException) targetException);
                }
                throw new RuntimeException(targetException.getMessage());
            } catch (ReflectionException e4) {
                throw new RuntimeException(e4.getMessage());
            }
        }
        JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "getRoleCardinality");
        return roleCardinality;
    }

    @Override // javax.management.relation.RelationServiceMBean
    public void setRole(String str, Role role) throws RoleNotFoundException, InvalidRoleValueException, IllegalArgumentException, RelationServiceNotRegisteredException, RelationNotFoundException {
        if (str == null || role == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        JmxProperties.RELATION_LOGGER.entering(RelationService.class.getName(), "setRole", new Object[]{str, role});
        isActive();
        Object relation = getRelation(str);
        if (relation instanceof RelationSupport) {
            try {
                ((RelationSupport) relation).setRoleInt(role, true, this, false);
            } catch (RelationTypeNotFoundException e2) {
                throw new RuntimeException(e2.getMessage());
            }
        } else {
            new Object[1][0] = role;
            new String[1][0] = "javax.management.relation.Role";
            try {
                this.myMBeanServer.setAttribute((ObjectName) relation, new Attribute("Role", role));
            } catch (AttributeNotFoundException e3) {
                throw new RuntimeException(e3.getMessage());
            } catch (InstanceNotFoundException e4) {
                throw new RuntimeException(e4.getMessage());
            } catch (InvalidAttributeValueException e5) {
                throw new RuntimeException(e5.getMessage());
            } catch (MBeanException e6) {
                Exception targetException = e6.getTargetException();
                if (targetException instanceof RoleNotFoundException) {
                    throw ((RoleNotFoundException) targetException);
                }
                if (targetException instanceof InvalidRoleValueException) {
                    throw ((InvalidRoleValueException) targetException);
                }
                throw new RuntimeException(targetException.getMessage());
            } catch (ReflectionException e7) {
                throw new RuntimeException(e7.getMessage());
            }
        }
        JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "setRole");
    }

    @Override // javax.management.relation.RelationServiceMBean
    public RoleResult setRoles(String str, RoleList roleList) throws IllegalArgumentException, RelationServiceNotRegisteredException, RelationNotFoundException {
        RoleResult rolesInt;
        if (str == null || roleList == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        JmxProperties.RELATION_LOGGER.entering(RelationService.class.getName(), "setRoles", new Object[]{str, roleList});
        isActive();
        Object relation = getRelation(str);
        if (relation instanceof RelationSupport) {
            try {
                rolesInt = ((RelationSupport) relation).setRolesInt(roleList, true, this);
            } catch (RelationTypeNotFoundException e2) {
                throw new RuntimeException(e2.getMessage());
            }
        } else {
            try {
                rolesInt = (RoleResult) this.myMBeanServer.invoke((ObjectName) relation, "setRoles", new Object[]{roleList}, new String[]{"javax.management.relation.RoleList"});
            } catch (InstanceNotFoundException e3) {
                throw new RuntimeException(e3.getMessage());
            } catch (MBeanException e4) {
                throw new RuntimeException(e4.getTargetException().getMessage());
            } catch (ReflectionException e5) {
                throw new RuntimeException(e5.getMessage());
            }
        }
        JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "setRoles");
        return rolesInt;
    }

    @Override // javax.management.relation.RelationServiceMBean
    public Map<ObjectName, List<String>> getReferencedMBeans(String str) throws IllegalArgumentException, RelationNotFoundException {
        Map<ObjectName, List<String>> referencedMBeans;
        if (str == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        JmxProperties.RELATION_LOGGER.entering(RelationService.class.getName(), "getReferencedMBeans", str);
        Object relation = getRelation(str);
        if (relation instanceof RelationSupport) {
            referencedMBeans = ((RelationSupport) relation).getReferencedMBeans();
        } else {
            try {
                referencedMBeans = (Map) Util.cast(this.myMBeanServer.getAttribute((ObjectName) relation, "ReferencedMBeans"));
            } catch (Exception e2) {
                throw new RuntimeException(e2.getMessage());
            }
        }
        JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "getReferencedMBeans");
        return referencedMBeans;
    }

    @Override // javax.management.relation.RelationServiceMBean
    public String getRelationTypeName(String str) throws IllegalArgumentException, RelationNotFoundException {
        String relationTypeName;
        if (str == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        JmxProperties.RELATION_LOGGER.entering(RelationService.class.getName(), "getRelationTypeName", str);
        Object relation = getRelation(str);
        if (relation instanceof RelationSupport) {
            relationTypeName = ((RelationSupport) relation).getRelationTypeName();
        } else {
            try {
                relationTypeName = (String) this.myMBeanServer.getAttribute((ObjectName) relation, "RelationTypeName");
            } catch (Exception e2) {
                throw new RuntimeException(e2.getMessage());
            }
        }
        JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "getRelationTypeName");
        return relationTypeName;
    }

    @Override // javax.management.NotificationListener
    public void handleNotification(Notification notification, Object obj) {
        String str;
        if (notification == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        JmxProperties.RELATION_LOGGER.entering(RelationService.class.getName(), "handleNotification", notification);
        if (notification instanceof MBeanServerNotification) {
            MBeanServerNotification mBeanServerNotification = (MBeanServerNotification) notification;
            if (notification.getType().equals(MBeanServerNotification.UNREGISTRATION_NOTIFICATION)) {
                ObjectName mBeanName = ((MBeanServerNotification) notification).getMBeanName();
                boolean z2 = false;
                synchronized (this.myRefedMBeanObjName2RelIdsMap) {
                    if (this.myRefedMBeanObjName2RelIdsMap.containsKey(mBeanName)) {
                        synchronized (this.myUnregNtfList) {
                            this.myUnregNtfList.add(mBeanServerNotification);
                        }
                        z2 = true;
                    }
                    if (z2 && this.myPurgeFlag) {
                        try {
                            purgeRelations();
                        } catch (Exception e2) {
                            throw new RuntimeException(e2.getMessage());
                        }
                    }
                }
                synchronized (this.myRelMBeanObjName2RelIdMap) {
                    str = this.myRelMBeanObjName2RelIdMap.get(mBeanName);
                }
                if (str != null) {
                    try {
                        removeRelation(str);
                    } catch (Exception e3) {
                        throw new RuntimeException(e3.getMessage());
                    }
                }
            }
        }
        JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "handleNotification");
    }

    @Override // javax.management.NotificationBroadcasterSupport, javax.management.NotificationBroadcaster
    public MBeanNotificationInfo[] getNotificationInfo() {
        JmxProperties.RELATION_LOGGER.entering(RelationService.class.getName(), "getNotificationInfo");
        MBeanNotificationInfo mBeanNotificationInfo = new MBeanNotificationInfo(new String[]{RelationNotification.RELATION_BASIC_CREATION, RelationNotification.RELATION_MBEAN_CREATION, RelationNotification.RELATION_BASIC_UPDATE, RelationNotification.RELATION_MBEAN_UPDATE, RelationNotification.RELATION_BASIC_REMOVAL, RelationNotification.RELATION_MBEAN_REMOVAL}, "javax.management.relation.RelationNotification", "Sent when a relation is created, updated or deleted.");
        JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "getNotificationInfo");
        return new MBeanNotificationInfo[]{mBeanNotificationInfo};
    }

    private void addRelationTypeInt(RelationType relationType) throws InvalidRelationTypeException, IllegalArgumentException {
        if (relationType == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        JmxProperties.RELATION_LOGGER.entering(RelationService.class.getName(), "addRelationTypeInt");
        String relationTypeName = relationType.getRelationTypeName();
        if (getRelationType(relationTypeName) != null) {
            throw new InvalidRelationTypeException("There is already a relation type in the Relation Service with name " + relationTypeName);
        }
        synchronized (this.myRelType2ObjMap) {
            this.myRelType2ObjMap.put(relationTypeName, relationType);
        }
        if (relationType instanceof RelationTypeSupport) {
            ((RelationTypeSupport) relationType).setRelationServiceFlag(true);
        }
        JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "addRelationTypeInt");
    }

    RelationType getRelationType(String str) throws RelationTypeNotFoundException, IllegalArgumentException {
        RelationType relationType;
        if (str == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        JmxProperties.RELATION_LOGGER.entering(RelationService.class.getName(), "getRelationType", str);
        synchronized (this.myRelType2ObjMap) {
            relationType = this.myRelType2ObjMap.get(str);
        }
        if (relationType == null) {
            throw new RelationTypeNotFoundException("No relation type created in the Relation Service with the name " + str);
        }
        JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "getRelationType");
        return relationType;
    }

    Object getRelation(String str) throws IllegalArgumentException, RelationNotFoundException {
        Object obj;
        if (str == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        JmxProperties.RELATION_LOGGER.entering(RelationService.class.getName(), "getRelation", str);
        synchronized (this.myRelId2ObjMap) {
            obj = this.myRelId2ObjMap.get(str);
        }
        if (obj == null) {
            throw new RelationNotFoundException("No relation associated to relation id " + str);
        }
        JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "getRelation");
        return obj;
    }

    private boolean addNewMBeanReference(ObjectName objectName, String str, String str2) throws IllegalArgumentException {
        if (objectName == null || str == null || str2 == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        JmxProperties.RELATION_LOGGER.entering(RelationService.class.getName(), "addNewMBeanReference", new Object[]{objectName, str, str2});
        boolean z2 = false;
        synchronized (this.myRefedMBeanObjName2RelIdsMap) {
            Map<String, List<String>> map = this.myRefedMBeanObjName2RelIdsMap.get(objectName);
            if (map == null) {
                z2 = true;
                ArrayList arrayList = new ArrayList();
                arrayList.add(str2);
                HashMap map2 = new HashMap();
                map2.put(str, arrayList);
                this.myRefedMBeanObjName2RelIdsMap.put(objectName, map2);
            } else {
                List<String> list = map.get(str);
                if (list == null) {
                    ArrayList arrayList2 = new ArrayList();
                    arrayList2.add(str2);
                    map.put(str, arrayList2);
                } else {
                    list.add(str2);
                }
            }
        }
        JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "addNewMBeanReference");
        return z2;
    }

    private boolean removeMBeanReference(ObjectName objectName, String str, String str2, boolean z2) throws IllegalArgumentException {
        if (objectName == null || str == null || str2 == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        JmxProperties.RELATION_LOGGER.entering(RelationService.class.getName(), "removeMBeanReference", new Object[]{objectName, str, str2, Boolean.valueOf(z2)});
        boolean z3 = false;
        synchronized (this.myRefedMBeanObjName2RelIdsMap) {
            Map<String, List<String>> map = this.myRefedMBeanObjName2RelIdsMap.get(objectName);
            if (map == null) {
                JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "removeMBeanReference");
                return true;
            }
            List<String> list = null;
            if (!z2) {
                list = map.get(str);
                int iIndexOf = list.indexOf(str2);
                if (iIndexOf != -1) {
                    list.remove(iIndexOf);
                }
            }
            if (list.isEmpty() || z2) {
                map.remove(str);
            }
            if (map.isEmpty()) {
                this.myRefedMBeanObjName2RelIdsMap.remove(objectName);
                z3 = true;
            }
            JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "removeMBeanReference");
            return z3;
        }
    }

    private void updateUnregistrationListener(List<ObjectName> list, List<ObjectName> list2) throws RelationServiceNotRegisteredException {
        if (list != null && list2 != null && list.isEmpty() && list2.isEmpty()) {
            return;
        }
        JmxProperties.RELATION_LOGGER.entering(RelationService.class.getName(), "updateUnregistrationListener", new Object[]{list, list2});
        isActive();
        if (list != null || list2 != null) {
            boolean z2 = false;
            if (this.myUnregNtfFilter == null) {
                this.myUnregNtfFilter = new MBeanServerNotificationFilter();
                z2 = true;
            }
            synchronized (this.myUnregNtfFilter) {
                if (list != null) {
                    Iterator<ObjectName> it = list.iterator();
                    while (it.hasNext()) {
                        this.myUnregNtfFilter.enableObjectName(it.next());
                    }
                }
                if (list2 != null) {
                    Iterator<ObjectName> it2 = list2.iterator();
                    while (it2.hasNext()) {
                        this.myUnregNtfFilter.disableObjectName(it2.next());
                    }
                }
                if (z2) {
                    try {
                        this.myMBeanServer.addNotificationListener(MBeanServerDelegate.DELEGATE_NAME, this, this.myUnregNtfFilter, (Object) null);
                    } catch (InstanceNotFoundException e2) {
                        throw new RelationServiceNotRegisteredException(e2.getMessage());
                    }
                }
            }
        }
        JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "updateUnregistrationListener");
    }

    private void addRelationInt(boolean z2, RelationSupport relationSupport, ObjectName objectName, String str, String str2, RoleList roleList) throws RoleNotFoundException, RelationTypeNotFoundException, InvalidRelationIdException, InvalidRoleValueException, IllegalArgumentException, RelationServiceNotRegisteredException {
        if (str == null || str2 == null || ((z2 && (relationSupport == null || objectName != null)) || (!z2 && (objectName == null || relationSupport != null)))) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        JmxProperties.RELATION_LOGGER.entering(RelationService.class.getName(), "addRelationInt", new Object[]{Boolean.valueOf(z2), relationSupport, objectName, str, str2, roleList});
        isActive();
        if (getRelation(str) != null) {
            throw new InvalidRelationIdException("There is already a relation with id " + str);
        }
        RelationType relationType = getRelationType(str2);
        ArrayList arrayList = new ArrayList(relationType.getRoleInfos());
        if (roleList != null) {
            for (Role role : roleList.asList()) {
                String roleName = role.getRoleName();
                List<ObjectName> roleValue = role.getRoleValue();
                try {
                    RoleInfo roleInfo = relationType.getRoleInfo(roleName);
                    int iIntValue = checkRoleInt(2, roleName, roleValue, roleInfo, false).intValue();
                    if (iIntValue != 0) {
                        throwRoleProblemException(iIntValue, roleName);
                    }
                    arrayList.remove(arrayList.indexOf(roleInfo));
                } catch (RoleInfoNotFoundException e2) {
                    throw new RoleNotFoundException(e2.getMessage());
                }
            }
        }
        initializeMissingRoles(z2, relationSupport, objectName, str, str2, arrayList);
        synchronized (this.myRelId2ObjMap) {
            if (z2) {
                this.myRelId2ObjMap.put(str, relationSupport);
            } else {
                this.myRelId2ObjMap.put(str, objectName);
            }
        }
        synchronized (this.myRelId2RelTypeMap) {
            this.myRelId2RelTypeMap.put(str, str2);
        }
        synchronized (this.myRelType2RelIdsMap) {
            List<String> arrayList2 = this.myRelType2RelIdsMap.get(str2);
            boolean z3 = false;
            if (arrayList2 == null) {
                z3 = true;
                arrayList2 = new ArrayList();
            }
            arrayList2.add(str);
            if (z3) {
                this.myRelType2RelIdsMap.put(str2, arrayList2);
            }
        }
        Iterator<Role> it = roleList.asList().iterator();
        while (it.hasNext()) {
            try {
                updateRoleMap(str, it.next(), new ArrayList());
            } catch (RelationNotFoundException e3) {
            }
        }
        try {
            sendRelationCreationNotification(str);
        } catch (RelationNotFoundException e4) {
        }
        JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "addRelationInt");
    }

    private Integer checkRoleInt(int i2, String str, List<ObjectName> list, RoleInfo roleInfo, boolean z2) throws IllegalArgumentException {
        if (str == null || roleInfo == null || (i2 == 2 && list == null)) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        JmxProperties.RELATION_LOGGER.entering(RelationService.class.getName(), "checkRoleInt", new Object[]{Integer.valueOf(i2), str, list, roleInfo, Boolean.valueOf(z2)});
        if (!str.equals(roleInfo.getName())) {
            JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "checkRoleInt");
            return 1;
        }
        if (i2 == 1) {
            if (!roleInfo.isReadable()) {
                JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "checkRoleInt");
                return 2;
            }
            JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "checkRoleInt");
            return new Integer(0);
        }
        if (z2 && !roleInfo.isWritable()) {
            JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "checkRoleInt");
            return new Integer(3);
        }
        int size = list.size();
        if (!roleInfo.checkMinDegree(size)) {
            JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "checkRoleInt");
            return new Integer(4);
        }
        if (!roleInfo.checkMaxDegree(size)) {
            JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "checkRoleInt");
            return new Integer(5);
        }
        String refMBeanClassName = roleInfo.getRefMBeanClassName();
        for (ObjectName objectName : list) {
            if (objectName == null) {
                JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "checkRoleInt");
                return new Integer(7);
            }
            try {
                if (!this.myMBeanServer.isInstanceOf(objectName, refMBeanClassName)) {
                    JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "checkRoleInt");
                    return new Integer(6);
                }
            } catch (InstanceNotFoundException e2) {
                JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "checkRoleInt");
                return new Integer(7);
            }
        }
        JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "checkRoleInt");
        return new Integer(0);
    }

    private void initializeMissingRoles(boolean z2, RelationSupport relationSupport, ObjectName objectName, String str, String str2, List<RoleInfo> list) throws InvalidRoleValueException, IllegalArgumentException, RelationServiceNotRegisteredException {
        if ((z2 && (relationSupport == null || objectName != null)) || ((!z2 && (objectName == null || relationSupport != null)) || str == null || str2 == null || list == null)) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        JmxProperties.RELATION_LOGGER.entering(RelationService.class.getName(), "initializeMissingRoles", new Object[]{Boolean.valueOf(z2), relationSupport, objectName, str, str2, list});
        isActive();
        Iterator<RoleInfo> it = list.iterator();
        while (it.hasNext()) {
            Role role = new Role(it.next().getName(), new ArrayList());
            if (z2) {
                try {
                    relationSupport.setRoleInt(role, true, this, false);
                } catch (RelationNotFoundException e2) {
                    throw new RuntimeException(e2.getMessage());
                } catch (RelationTypeNotFoundException e3) {
                    throw new RuntimeException(e3.getMessage());
                } catch (RoleNotFoundException e4) {
                    throw new RuntimeException(e4.getMessage());
                }
            } else {
                new Object[1][0] = role;
                new String[1][0] = "javax.management.relation.Role";
                try {
                    this.myMBeanServer.setAttribute(objectName, new Attribute("Role", role));
                } catch (AttributeNotFoundException e5) {
                    throw new RuntimeException(e5.getMessage());
                } catch (InstanceNotFoundException e6) {
                    throw new RuntimeException(e6.getMessage());
                } catch (InvalidAttributeValueException e7) {
                    throw new RuntimeException(e7.getMessage());
                } catch (MBeanException e8) {
                    Exception targetException = e8.getTargetException();
                    if (targetException instanceof InvalidRoleValueException) {
                        throw ((InvalidRoleValueException) targetException);
                    }
                    throw new RuntimeException(targetException.getMessage());
                } catch (ReflectionException e9) {
                    throw new RuntimeException(e9.getMessage());
                }
            }
        }
        JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "initializeMissingRoles");
    }

    /* JADX WARN: Multi-variable type inference failed */
    static void throwRoleProblemException(int i2, String str) throws RoleNotFoundException, InvalidRoleValueException, IllegalArgumentException {
        if (str == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        boolean z2 = false;
        String str2 = null;
        switch (i2) {
            case 1:
                str2 = " does not exist in relation.";
                z2 = true;
                break;
            case 2:
                str2 = " is not readable.";
                z2 = true;
                break;
            case 3:
                str2 = " is not writable.";
                z2 = true;
                break;
            case 4:
                str2 = " has a number of MBean references less than the expected minimum degree.";
                z2 = 2;
                break;
            case 5:
                str2 = " has a number of MBean references greater than the expected maximum degree.";
                z2 = 2;
                break;
            case 6:
                str2 = " has an MBean reference to an MBean not of the expected class of references for that role.";
                z2 = 2;
                break;
            case 7:
                str2 = " has a reference to null or to an MBean not registered.";
                z2 = 2;
                break;
        }
        String str3 = str + str2;
        if (z2) {
            throw new RoleNotFoundException(str3);
        }
        if (z2 == 2) {
            throw new InvalidRoleValueException(str3);
        }
    }

    private void sendNotificationInt(int i2, String str, String str2, List<ObjectName> list, String str3, List<ObjectName> list2, List<ObjectName> list3) throws IllegalArgumentException, RelationNotFoundException {
        String str4;
        if (str == null || str2 == null || ((i2 != 3 && list != null) || (i2 == 2 && (str3 == null || list2 == null || list3 == null)))) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        JmxProperties.RELATION_LOGGER.entering(RelationService.class.getName(), "sendNotificationInt", new Object[]{Integer.valueOf(i2), str, str2, list, str3, list2, list3});
        synchronized (this.myRelId2RelTypeMap) {
            str4 = this.myRelId2RelTypeMap.get(str2);
        }
        ObjectName objectNameIsRelationMBean = isRelationMBean(str2);
        String str5 = null;
        if (objectNameIsRelationMBean != null) {
            switch (i2) {
                case 1:
                    str5 = RelationNotification.RELATION_MBEAN_CREATION;
                    break;
                case 2:
                    str5 = RelationNotification.RELATION_MBEAN_UPDATE;
                    break;
                case 3:
                    str5 = RelationNotification.RELATION_MBEAN_REMOVAL;
                    break;
            }
        } else {
            switch (i2) {
                case 1:
                    str5 = RelationNotification.RELATION_BASIC_CREATION;
                    break;
                case 2:
                    str5 = RelationNotification.RELATION_BASIC_UPDATE;
                    break;
                case 3:
                    str5 = RelationNotification.RELATION_BASIC_REMOVAL;
                    break;
            }
        }
        Long lValueOf = Long.valueOf(this.atomicSeqNo.incrementAndGet());
        long time = new Date().getTime();
        RelationNotification relationNotification = null;
        if (str5.equals(RelationNotification.RELATION_BASIC_CREATION) || str5.equals(RelationNotification.RELATION_MBEAN_CREATION) || str5.equals(RelationNotification.RELATION_BASIC_REMOVAL) || str5.equals(RelationNotification.RELATION_MBEAN_REMOVAL)) {
            relationNotification = new RelationNotification(str5, this, lValueOf.longValue(), time, str, str2, str4, objectNameIsRelationMBean, list);
        } else if (str5.equals(RelationNotification.RELATION_BASIC_UPDATE) || str5.equals(RelationNotification.RELATION_MBEAN_UPDATE)) {
            relationNotification = new RelationNotification(str5, this, lValueOf.longValue(), time, str, str2, str4, objectNameIsRelationMBean, str3, list2, list3);
        }
        sendNotification(relationNotification);
        JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "sendNotificationInt");
    }

    private void handleReferenceUnregistration(String str, ObjectName objectName, List<String> list) throws RoleNotFoundException, IllegalArgumentException, RelationServiceNotRegisteredException, RelationNotFoundException {
        if (str == null || list == null || objectName == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        JmxProperties.RELATION_LOGGER.entering(RelationService.class.getName(), "handleReferenceUnregistration", new Object[]{str, objectName, list});
        isActive();
        String relationTypeName = getRelationTypeName(str);
        Object relation = getRelation(str);
        boolean z2 = false;
        for (String str2 : list) {
            if (z2) {
                break;
            }
            try {
                if (!getRoleInfo(relationTypeName, str2).checkMinDegree(getRoleCardinality(str, str2).intValue() - 1)) {
                    z2 = true;
                }
            } catch (RelationTypeNotFoundException e2) {
                throw new RuntimeException(e2.getMessage());
            } catch (RoleInfoNotFoundException e3) {
                throw new RuntimeException(e3.getMessage());
            }
        }
        if (z2) {
            removeRelation(str);
        } else {
            for (String str3 : list) {
                if (relation instanceof RelationSupport) {
                    try {
                        ((RelationSupport) relation).handleMBeanUnregistrationInt(objectName, str3, true, this);
                    } catch (InvalidRoleValueException e4) {
                        throw new RuntimeException(e4.getMessage());
                    } catch (RelationTypeNotFoundException e5) {
                        throw new RuntimeException(e5.getMessage());
                    }
                } else {
                    try {
                        this.myMBeanServer.invoke((ObjectName) relation, "handleMBeanUnregistration", new Object[]{objectName, str3}, new String[]{"javax.management.ObjectName", "java.lang.String"});
                    } catch (InstanceNotFoundException e6) {
                        throw new RuntimeException(e6.getMessage());
                    } catch (MBeanException e7) {
                        throw new RuntimeException(e7.getTargetException().getMessage());
                    } catch (ReflectionException e8) {
                        throw new RuntimeException(e8.getMessage());
                    }
                }
            }
        }
        JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "handleReferenceUnregistration");
    }
}
