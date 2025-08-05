package javax.management;

/* loaded from: rt.jar:javax/management/InstanceOfQueryExp.class */
class InstanceOfQueryExp extends QueryEval implements QueryExp {
    private static final long serialVersionUID = -1081892073854801359L;
    private StringValueExp classNameValue;

    public InstanceOfQueryExp(StringValueExp stringValueExp) {
        if (stringValueExp == null) {
            throw new IllegalArgumentException("Null class name.");
        }
        this.classNameValue = stringValueExp;
    }

    public StringValueExp getClassNameValue() {
        return this.classNameValue;
    }

    @Override // javax.management.QueryExp
    public boolean apply(ObjectName objectName) throws InvalidApplicationException, BadBinaryOpValueExpException, BadAttributeValueExpException, BadStringOperationException {
        try {
            try {
                return getMBeanServer().isInstanceOf(objectName, ((StringValueExp) this.classNameValue.apply(objectName)).getValue());
            } catch (InstanceNotFoundException e2) {
                return false;
            }
        } catch (ClassCastException e3) {
            BadStringOperationException badStringOperationException = new BadStringOperationException(e3.toString());
            badStringOperationException.initCause(e3);
            throw badStringOperationException;
        }
    }

    public String toString() {
        return "InstanceOf " + this.classNameValue.toString();
    }
}
