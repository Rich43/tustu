package javax.management;

/* loaded from: rt.jar:javax/management/BinaryRelQueryExp.class */
class BinaryRelQueryExp extends QueryEval implements QueryExp {
    private static final long serialVersionUID = -5690656271650491000L;
    private int relOp;
    private ValueExp exp1;
    private ValueExp exp2;

    public BinaryRelQueryExp() {
    }

    public BinaryRelQueryExp(int i2, ValueExp valueExp, ValueExp valueExp2) {
        this.relOp = i2;
        this.exp1 = valueExp;
        this.exp2 = valueExp2;
    }

    public int getOperator() {
        return this.relOp;
    }

    public ValueExp getLeftValue() {
        return this.exp1;
    }

    public ValueExp getRightValue() {
        return this.exp2;
    }

    @Override // javax.management.QueryExp
    public boolean apply(ObjectName objectName) throws InvalidApplicationException, BadBinaryOpValueExpException, BadAttributeValueExpException, BadStringOperationException {
        ValueExp valueExpApply = this.exp1.apply(objectName);
        ValueExp valueExpApply2 = this.exp2.apply(objectName);
        boolean z2 = valueExpApply instanceof NumericValueExp;
        boolean z3 = valueExpApply instanceof BooleanValueExp;
        if (z2) {
            if (((NumericValueExp) valueExpApply).isLong()) {
                long jLongValue = ((NumericValueExp) valueExpApply).longValue();
                long jLongValue2 = ((NumericValueExp) valueExpApply2).longValue();
                switch (this.relOp) {
                    case 0:
                        if (jLongValue > jLongValue2) {
                        }
                        break;
                    case 1:
                        if (jLongValue < jLongValue2) {
                        }
                        break;
                    case 2:
                        if (jLongValue >= jLongValue2) {
                        }
                        break;
                    case 3:
                        if (jLongValue <= jLongValue2) {
                        }
                        break;
                    case 4:
                        if (jLongValue == jLongValue2) {
                        }
                        break;
                }
                return false;
            }
            double dDoubleValue = ((NumericValueExp) valueExpApply).doubleValue();
            double dDoubleValue2 = ((NumericValueExp) valueExpApply2).doubleValue();
            switch (this.relOp) {
                case 0:
                    if (dDoubleValue > dDoubleValue2) {
                    }
                    break;
                case 1:
                    if (dDoubleValue < dDoubleValue2) {
                    }
                    break;
                case 2:
                    if (dDoubleValue >= dDoubleValue2) {
                    }
                    break;
                case 3:
                    if (dDoubleValue <= dDoubleValue2) {
                    }
                    break;
                case 4:
                    if (dDoubleValue == dDoubleValue2) {
                    }
                    break;
            }
            return false;
        }
        if (z3) {
            boolean zBooleanValue = ((BooleanValueExp) valueExpApply).getValue().booleanValue();
            boolean zBooleanValue2 = ((BooleanValueExp) valueExpApply2).getValue().booleanValue();
            switch (this.relOp) {
                case 0:
                    if (!zBooleanValue || zBooleanValue2) {
                    }
                    break;
                case 1:
                    if (zBooleanValue || !zBooleanValue2) {
                    }
                    break;
                case 2:
                    if (zBooleanValue || !zBooleanValue2) {
                    }
                    break;
                case 3:
                    if (!zBooleanValue || zBooleanValue2) {
                    }
                    break;
                case 4:
                    if (zBooleanValue == zBooleanValue2) {
                    }
                    break;
            }
            return false;
        }
        String value = ((StringValueExp) valueExpApply).getValue();
        String value2 = ((StringValueExp) valueExpApply2).getValue();
        switch (this.relOp) {
            case 0:
                if (value.compareTo(value2) > 0) {
                }
                break;
            case 1:
                if (value.compareTo(value2) < 0) {
                }
                break;
            case 2:
                if (value.compareTo(value2) >= 0) {
                }
                break;
            case 3:
                if (value.compareTo(value2) <= 0) {
                }
                break;
            case 4:
                if (value.compareTo(value2) == 0) {
                }
                break;
        }
        return false;
    }

    public String toString() {
        return "(" + ((Object) this.exp1) + ") " + relOpString() + " (" + ((Object) this.exp2) + ")";
    }

    private String relOpString() {
        switch (this.relOp) {
            case 0:
                return ">";
            case 1:
                return "<";
            case 2:
                return ">=";
            case 3:
                return "<=";
            case 4:
                return "=";
            default:
                return "=";
        }
    }
}
