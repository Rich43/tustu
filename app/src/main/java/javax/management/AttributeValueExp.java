package javax.management;

/* loaded from: rt.jar:javax/management/AttributeValueExp.class */
public class AttributeValueExp implements ValueExp {
    private static final long serialVersionUID = -7768025046539163385L;
    private String attr;

    @Deprecated
    public AttributeValueExp() {
    }

    public AttributeValueExp(String str) {
        this.attr = str;
    }

    public String getAttributeName() {
        return this.attr;
    }

    @Override // javax.management.ValueExp
    public ValueExp apply(ObjectName objectName) throws InvalidApplicationException, BadBinaryOpValueExpException, BadAttributeValueExpException, BadStringOperationException {
        Object attribute = getAttribute(objectName);
        if (attribute instanceof Number) {
            return new NumericValueExp((Number) attribute);
        }
        if (attribute instanceof String) {
            return new StringValueExp((String) attribute);
        }
        if (attribute instanceof Boolean) {
            return new BooleanValueExp((Boolean) attribute);
        }
        throw new BadAttributeValueExpException(attribute);
    }

    public String toString() {
        return this.attr;
    }

    @Override // javax.management.ValueExp
    @Deprecated
    public void setMBeanServer(MBeanServer mBeanServer) {
    }

    protected Object getAttribute(ObjectName objectName) {
        try {
            return QueryEval.getMBeanServer().getAttribute(objectName, this.attr);
        } catch (Exception e2) {
            return null;
        }
    }
}
