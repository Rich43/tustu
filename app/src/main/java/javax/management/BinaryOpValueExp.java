package javax.management;

import org.slf4j.Marker;
import sun.util.locale.LanguageTag;

/* loaded from: rt.jar:javax/management/BinaryOpValueExp.class */
class BinaryOpValueExp extends QueryEval implements ValueExp {
    private static final long serialVersionUID = 1216286847881456786L;
    private int op;
    private ValueExp exp1;
    private ValueExp exp2;

    public BinaryOpValueExp() {
    }

    public BinaryOpValueExp(int i2, ValueExp valueExp, ValueExp valueExp2) {
        this.op = i2;
        this.exp1 = valueExp;
        this.exp2 = valueExp2;
    }

    public int getOperator() {
        return this.op;
    }

    public ValueExp getLeftValue() {
        return this.exp1;
    }

    public ValueExp getRightValue() {
        return this.exp2;
    }

    @Override // javax.management.ValueExp
    public ValueExp apply(ObjectName objectName) throws InvalidApplicationException, BadBinaryOpValueExpException, BadAttributeValueExpException, BadStringOperationException {
        ValueExp valueExpApply = this.exp1.apply(objectName);
        ValueExp valueExpApply2 = this.exp2.apply(objectName);
        if (valueExpApply instanceof NumericValueExp) {
            if (((NumericValueExp) valueExpApply).isLong()) {
                long jLongValue = ((NumericValueExp) valueExpApply).longValue();
                long jLongValue2 = ((NumericValueExp) valueExpApply2).longValue();
                switch (this.op) {
                    case 0:
                        return Query.value(jLongValue + jLongValue2);
                    case 1:
                        return Query.value(jLongValue - jLongValue2);
                    case 2:
                        return Query.value(jLongValue * jLongValue2);
                    case 3:
                        return Query.value(jLongValue / jLongValue2);
                }
            }
            double dDoubleValue = ((NumericValueExp) valueExpApply).doubleValue();
            double dDoubleValue2 = ((NumericValueExp) valueExpApply2).doubleValue();
            switch (this.op) {
                case 0:
                    return Query.value(dDoubleValue + dDoubleValue2);
                case 1:
                    return Query.value(dDoubleValue - dDoubleValue2);
                case 2:
                    return Query.value(dDoubleValue * dDoubleValue2);
                case 3:
                    return Query.value(dDoubleValue / dDoubleValue2);
            }
            throw new BadBinaryOpValueExpException(this);
        }
        String value = ((StringValueExp) valueExpApply).getValue();
        String value2 = ((StringValueExp) valueExpApply2).getValue();
        switch (this.op) {
            case 0:
                return new StringValueExp(value + value2);
            default:
                throw new BadStringOperationException(opString());
        }
    }

    public String toString() {
        try {
            return parens(this.exp1, true) + " " + opString() + " " + parens(this.exp2, false);
        } catch (BadBinaryOpValueExpException e2) {
            return "invalid expression";
        }
    }

    private String parens(ValueExp valueExp, boolean z2) throws BadBinaryOpValueExpException {
        boolean z3;
        if (valueExp instanceof BinaryOpValueExp) {
            int i2 = ((BinaryOpValueExp) valueExp).op;
            if (z2) {
                z3 = precedence(i2) >= precedence(this.op);
            } else {
                z3 = precedence(i2) > precedence(this.op);
            }
        } else {
            z3 = true;
        }
        if (z3) {
            return valueExp.toString();
        }
        return "(" + ((Object) valueExp) + ")";
    }

    private int precedence(int i2) throws BadBinaryOpValueExpException {
        switch (i2) {
            case 0:
            case 1:
                return 0;
            case 2:
            case 3:
                return 1;
            default:
                throw new BadBinaryOpValueExpException(this);
        }
    }

    private String opString() throws BadBinaryOpValueExpException {
        switch (this.op) {
            case 0:
                return Marker.ANY_NON_NULL_MARKER;
            case 1:
                return LanguageTag.SEP;
            case 2:
                return "*";
            case 3:
                return "/";
            default:
                throw new BadBinaryOpValueExpException(this);
        }
    }

    @Override // javax.management.QueryEval, javax.management.ValueExp
    @Deprecated
    public void setMBeanServer(MBeanServer mBeanServer) {
        super.setMBeanServer(mBeanServer);
    }
}
