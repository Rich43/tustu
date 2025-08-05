package javax.management;

/* loaded from: rt.jar:javax/management/QualifiedAttributeValueExp.class */
class QualifiedAttributeValueExp extends AttributeValueExp {
    private static final long serialVersionUID = 8832517277410933254L;
    private String className;

    @Deprecated
    public QualifiedAttributeValueExp() {
    }

    public QualifiedAttributeValueExp(String str, String str2) {
        super(str2);
        this.className = str;
    }

    public String getAttrClassName() {
        return this.className;
    }

    @Override // javax.management.AttributeValueExp, javax.management.ValueExp
    public ValueExp apply(ObjectName objectName) throws InvalidApplicationException, BadBinaryOpValueExpException, BadAttributeValueExpException, BadStringOperationException {
        try {
            String className = QueryEval.getMBeanServer().getObjectInstance(objectName).getClassName();
            if (className.equals(this.className)) {
                return super.apply(objectName);
            }
            throw new InvalidApplicationException("Class name is " + className + ", should be " + this.className);
        } catch (Exception e2) {
            throw new InvalidApplicationException("Qualified attribute: " + ((Object) e2));
        }
    }

    @Override // javax.management.AttributeValueExp
    public String toString() {
        if (this.className != null) {
            return this.className + "." + super.toString();
        }
        return super.toString();
    }
}
