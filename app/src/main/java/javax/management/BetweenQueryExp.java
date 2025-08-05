package javax.management;

/* loaded from: rt.jar:javax/management/BetweenQueryExp.class */
class BetweenQueryExp extends QueryEval implements QueryExp {
    private static final long serialVersionUID = -2933597532866307444L;
    private ValueExp exp1;
    private ValueExp exp2;
    private ValueExp exp3;

    public BetweenQueryExp() {
    }

    public BetweenQueryExp(ValueExp valueExp, ValueExp valueExp2, ValueExp valueExp3) {
        this.exp1 = valueExp;
        this.exp2 = valueExp2;
        this.exp3 = valueExp3;
    }

    public ValueExp getCheckedValue() {
        return this.exp1;
    }

    public ValueExp getLowerBound() {
        return this.exp2;
    }

    public ValueExp getUpperBound() {
        return this.exp3;
    }

    @Override // javax.management.QueryExp
    public boolean apply(ObjectName objectName) throws InvalidApplicationException, BadBinaryOpValueExpException, BadAttributeValueExpException, BadStringOperationException {
        ValueExp valueExpApply = this.exp1.apply(objectName);
        ValueExp valueExpApply2 = this.exp2.apply(objectName);
        ValueExp valueExpApply3 = this.exp3.apply(objectName);
        if (valueExpApply instanceof NumericValueExp) {
            if (((NumericValueExp) valueExpApply).isLong()) {
                long jLongValue = ((NumericValueExp) valueExpApply).longValue();
                return ((NumericValueExp) valueExpApply2).longValue() <= jLongValue && jLongValue <= ((NumericValueExp) valueExpApply3).longValue();
            }
            double dDoubleValue = ((NumericValueExp) valueExpApply).doubleValue();
            return ((NumericValueExp) valueExpApply2).doubleValue() <= dDoubleValue && dDoubleValue <= ((NumericValueExp) valueExpApply3).doubleValue();
        }
        String value = ((StringValueExp) valueExpApply).getValue();
        return ((StringValueExp) valueExpApply2).getValue().compareTo(value) <= 0 && value.compareTo(((StringValueExp) valueExpApply3).getValue()) <= 0;
    }

    public String toString() {
        return "(" + ((Object) this.exp1) + ") between (" + ((Object) this.exp2) + ") and (" + ((Object) this.exp3) + ")";
    }
}
