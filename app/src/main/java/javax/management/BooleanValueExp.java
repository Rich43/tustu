package javax.management;

/* loaded from: rt.jar:javax/management/BooleanValueExp.class */
class BooleanValueExp extends QueryEval implements ValueExp {
    private static final long serialVersionUID = 7754922052666594581L;
    private boolean val;

    BooleanValueExp(boolean z2) {
        this.val = false;
        this.val = z2;
    }

    BooleanValueExp(Boolean bool) {
        this.val = false;
        this.val = bool.booleanValue();
    }

    public Boolean getValue() {
        return Boolean.valueOf(this.val);
    }

    public String toString() {
        return String.valueOf(this.val);
    }

    @Override // javax.management.ValueExp
    public ValueExp apply(ObjectName objectName) throws InvalidApplicationException, BadBinaryOpValueExpException, BadAttributeValueExpException, BadStringOperationException {
        return this;
    }

    @Override // javax.management.QueryEval, javax.management.ValueExp
    @Deprecated
    public void setMBeanServer(MBeanServer mBeanServer) {
        super.setMBeanServer(mBeanServer);
    }
}
