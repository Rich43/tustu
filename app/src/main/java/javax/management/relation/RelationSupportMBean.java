package javax.management.relation;

/* loaded from: rt.jar:javax/management/relation/RelationSupportMBean.class */
public interface RelationSupportMBean extends Relation {
    Boolean isInRelationService();

    void setRelationServiceManagementFlag(Boolean bool) throws IllegalArgumentException;
}
