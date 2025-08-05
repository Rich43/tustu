package javax.management;

/* loaded from: rt.jar:javax/management/InQueryExp.class */
class InQueryExp extends QueryEval implements QueryExp {
    private static final long serialVersionUID = -5801329450358952434L;
    private ValueExp val;
    private ValueExp[] valueList;

    public InQueryExp() {
    }

    public InQueryExp(ValueExp valueExp, ValueExp[] valueExpArr) {
        this.val = valueExp;
        this.valueList = valueExpArr;
    }

    public ValueExp getCheckedValue() {
        return this.val;
    }

    public ValueExp[] getExplicitValues() {
        return this.valueList;
    }

    @Override // javax.management.QueryExp
    public boolean apply(ObjectName objectName) throws InvalidApplicationException, BadBinaryOpValueExpException, BadAttributeValueExpException, BadStringOperationException {
        if (this.valueList != null) {
            ValueExp valueExpApply = this.val.apply(objectName);
            boolean z2 = valueExpApply instanceof NumericValueExp;
            for (ValueExp valueExp : this.valueList) {
                ValueExp valueExpApply2 = valueExp.apply(objectName);
                if (z2) {
                    if (((NumericValueExp) valueExpApply2).doubleValue() == ((NumericValueExp) valueExpApply).doubleValue()) {
                        return true;
                    }
                } else if (((StringValueExp) valueExpApply2).getValue().equals(((StringValueExp) valueExpApply).getValue())) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public String toString() {
        return ((Object) this.val) + " in (" + generateValueList() + ")";
    }

    private String generateValueList() {
        if (this.valueList == null || this.valueList.length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder(this.valueList[0].toString());
        for (int i2 = 1; i2 < this.valueList.length; i2++) {
            sb.append(", ");
            sb.append((Object) this.valueList[i2]);
        }
        return sb.toString();
    }
}
