package javax.management.relation;

import java.io.Serializable;
import java.util.List;

/* loaded from: rt.jar:javax/management/relation/RelationType.class */
public interface RelationType extends Serializable {
    String getRelationTypeName();

    List<RoleInfo> getRoleInfos();

    RoleInfo getRoleInfo(String str) throws RoleInfoNotFoundException, IllegalArgumentException;
}
