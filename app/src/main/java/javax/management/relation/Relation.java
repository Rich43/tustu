package javax.management.relation;

import java.util.List;
import java.util.Map;
import javax.management.ObjectName;

/* loaded from: rt.jar:javax/management/relation/Relation.class */
public interface Relation {
    List<ObjectName> getRole(String str) throws RoleNotFoundException, IllegalArgumentException, RelationServiceNotRegisteredException;

    RoleResult getRoles(String[] strArr) throws IllegalArgumentException, RelationServiceNotRegisteredException;

    Integer getRoleCardinality(String str) throws RoleNotFoundException, IllegalArgumentException;

    RoleResult getAllRoles() throws RelationServiceNotRegisteredException;

    RoleList retrieveAllRoles();

    void setRole(Role role) throws RoleNotFoundException, RelationTypeNotFoundException, InvalidRoleValueException, IllegalArgumentException, RelationServiceNotRegisteredException, RelationNotFoundException;

    RoleResult setRoles(RoleList roleList) throws RelationTypeNotFoundException, IllegalArgumentException, RelationServiceNotRegisteredException, RelationNotFoundException;

    void handleMBeanUnregistration(ObjectName objectName, String str) throws RoleNotFoundException, RelationTypeNotFoundException, InvalidRoleValueException, IllegalArgumentException, RelationServiceNotRegisteredException, RelationNotFoundException;

    Map<ObjectName, List<String>> getReferencedMBeans();

    String getRelationTypeName();

    ObjectName getRelationServiceName();

    String getRelationId();
}
