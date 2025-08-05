package javax.management;

/* loaded from: rt.jar:javax/management/OrQueryExp.class */
class OrQueryExp extends QueryEval implements QueryExp {
    private static final long serialVersionUID = 2962973084421716523L;
    private QueryExp exp1;
    private QueryExp exp2;

    public OrQueryExp() {
    }

    public OrQueryExp(QueryExp queryExp, QueryExp queryExp2) {
        this.exp1 = queryExp;
        this.exp2 = queryExp2;
    }

    public QueryExp getLeftExp() {
        return this.exp1;
    }

    public QueryExp getRightExp() {
        return this.exp2;
    }

    @Override // javax.management.QueryExp
    public boolean apply(ObjectName objectName) throws InvalidApplicationException, BadBinaryOpValueExpException, BadAttributeValueExpException, BadStringOperationException {
        return this.exp1.apply(objectName) || this.exp2.apply(objectName);
    }

    public String toString() {
        return "(" + ((Object) this.exp1) + ") or (" + ((Object) this.exp2) + ")";
    }
}
