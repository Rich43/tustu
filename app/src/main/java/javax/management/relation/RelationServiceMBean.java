package javax.management.relation;

import java.util.List;
import java.util.Map;
import javax.management.InstanceNotFoundException;
import javax.management.ObjectName;

/* loaded from: rt.jar:javax/management/relation/RelationServiceMBean.class */
public interface RelationServiceMBean {
    void isActive() throws RelationServiceNotRegisteredException;

    boolean getPurgeFlag();

    void setPurgeFlag(boolean z2);

    void createRelationType(String str, RoleInfo[] roleInfoArr) throws InvalidRelationTypeException, IllegalArgumentException;

    void addRelationType(RelationType relationType) throws InvalidRelationTypeException, IllegalArgumentException;

    List<String> getAllRelationTypeNames();

    List<RoleInfo> getRoleInfos(String str) throws RelationTypeNotFoundException, IllegalArgumentException;

    RoleInfo getRoleInfo(String str, String str2) throws RelationTypeNotFoundException, RoleInfoNotFoundException, IllegalArgumentException;

    void removeRelationType(String str) throws RelationTypeNotFoundException, IllegalArgumentException, RelationServiceNotRegisteredException;

    void createRelation(String str, String str2, RoleList roleList) throws RoleNotFoundException, RelationTypeNotFoundException, InvalidRelationIdException, InvalidRoleValueException, IllegalArgumentException, RelationServiceNotRegisteredException;

    void addRelation(ObjectName objectName) throws RoleNotFoundException, InvalidRelationServiceException, NoSuchMethodException, RelationTypeNotFoundException, InvalidRelationIdException, InvalidRoleValueException, IllegalArgumentException, RelationServiceNotRegisteredException, InstanceNotFoundException;

    ObjectName isRelationMBean(String str) throws IllegalArgumentException, RelationNotFoundException;

    String isRelation(ObjectName objectName) throws IllegalArgumentException;

    Boolean hasRelation(String str) throws IllegalArgumentException;

    List<String> getAllRelationIds();

    Integer checkRoleReading(String str, String str2) throws RelationTypeNotFoundException, IllegalArgumentException;

    Integer checkRoleWriting(Role role, String str, Boolean bool) throws RelationTypeNotFoundException, IllegalArgumentException;

    void sendRelationCreationNotification(String str) throws IllegalArgumentException, RelationNotFoundException;

    void sendRoleUpdateNotification(String str, Role role, List<ObjectName> list) throws IllegalArgumentException, RelationNotFoundException;

    void sendRelationRemovalNotification(String str, List<ObjectName> list) throws IllegalArgumentException, RelationNotFoundException;

    void updateRoleMap(String str, Role role, List<ObjectName> list) throws IllegalArgumentException, RelationServiceNotRegisteredException, RelationNotFoundException;

    void removeRelation(String str) throws IllegalArgumentException, RelationServiceNotRegisteredException, RelationNotFoundException;

    void purgeRelations() throws RelationServiceNotRegisteredException;

    Map<String, List<String>> findReferencingRelations(ObjectName objectName, String str, String str2) throws IllegalArgumentException;

    Map<ObjectName, List<String>> findAssociatedMBeans(ObjectName objectName, String str, String str2) throws IllegalArgumentException;

    List<String> findRelationsOfType(String str) throws RelationTypeNotFoundException, IllegalArgumentException;

    List<ObjectName> getRole(String str, String str2) throws RoleNotFoundException, IllegalArgumentException, RelationServiceNotRegisteredException, RelationNotFoundException;

    RoleResult getRoles(String str, String[] strArr) throws IllegalArgumentException, RelationServiceNotRegisteredException, RelationNotFoundException;

    RoleResult getAllRoles(String str) throws IllegalArgumentException, RelationNotFoundException, RelationServiceNotRegisteredException;

    Integer getRoleCardinality(String str, String str2) throws RoleNotFoundException, IllegalArgumentException, RelationNotFoundException;

    void setRole(String str, Role role) throws RoleNotFoundException, RelationTypeNotFoundException, InvalidRoleValueException, IllegalArgumentException, RelationServiceNotRegisteredException, RelationNotFoundException;

    RoleResult setRoles(String str, RoleList roleList) throws IllegalArgumentException, RelationServiceNotRegisteredException, RelationNotFoundException;

    Map<ObjectName, List<String>> getReferencedMBeans(String str) throws IllegalArgumentException, RelationNotFoundException;

    String getRelationTypeName(String str) throws IllegalArgumentException, RelationNotFoundException;
}
