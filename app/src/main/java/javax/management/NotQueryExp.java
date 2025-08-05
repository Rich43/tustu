package javax.management;

/* loaded from: rt.jar:javax/management/NotQueryExp.class */
class NotQueryExp extends QueryEval implements QueryExp {
    private static final long serialVersionUID = 5269643775896723397L;
    private QueryExp exp;

    public NotQueryExp() {
    }

    public NotQueryExp(QueryExp queryExp) {
        this.exp = queryExp;
    }

    public QueryExp getNegatedExp() {
        return this.exp;
    }

    @Override // javax.management.QueryExp
    public boolean apply(ObjectName objectName) throws InvalidApplicationException, BadBinaryOpValueExpException, BadAttributeValueExpException, BadStringOperationException {
        return !this.exp.apply(objectName);
    }

    public String toString() {
        return "not (" + ((Object) this.exp) + ")";
    }
}
