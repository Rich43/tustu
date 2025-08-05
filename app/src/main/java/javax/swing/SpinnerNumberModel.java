package javax.swing;

import java.io.Serializable;

/* loaded from: rt.jar:javax/swing/SpinnerNumberModel.class */
public class SpinnerNumberModel extends AbstractSpinnerModel implements Serializable {
    private Number stepSize;
    private Number value;
    private Comparable minimum;
    private Comparable maximum;

    public SpinnerNumberModel(Number number, Comparable comparable, Comparable comparable2, Number number2) {
        if (number == null || number2 == null) {
            throw new IllegalArgumentException("value and stepSize must be non-null");
        }
        if ((comparable != null && comparable.compareTo(number) > 0) || (comparable2 != null && comparable2.compareTo(number) < 0)) {
            throw new IllegalArgumentException("(minimum <= value <= maximum) is false");
        }
        this.value = number;
        this.minimum = comparable;
        this.maximum = comparable2;
        this.stepSize = number2;
    }

    public SpinnerNumberModel(int i2, int i3, int i4, int i5) {
        this(Integer.valueOf(i2), Integer.valueOf(i3), Integer.valueOf(i4), Integer.valueOf(i5));
    }

    public SpinnerNumberModel(double d2, double d3, double d4, double d5) {
        this(new Double(d2), new Double(d3), new Double(d4), new Double(d5));
    }

    public SpinnerNumberModel() {
        this((Number) 0, (Comparable) null, (Comparable) null, (Number) 1);
    }

    public void setMinimum(Comparable comparable) {
        if (comparable == null) {
            if (this.minimum == null) {
                return;
            }
        } else if (comparable.equals(this.minimum)) {
            return;
        }
        this.minimum = comparable;
        fireStateChanged();
    }

    public Comparable getMinimum() {
        return this.minimum;
    }

    public void setMaximum(Comparable comparable) {
        if (comparable == null) {
            if (this.maximum == null) {
                return;
            }
        } else if (comparable.equals(this.maximum)) {
            return;
        }
        this.maximum = comparable;
        fireStateChanged();
    }

    public Comparable getMaximum() {
        return this.maximum;
    }

    public void setStepSize(Number number) {
        if (number == null) {
            throw new IllegalArgumentException("null stepSize");
        }
        if (!number.equals(this.stepSize)) {
            this.stepSize = number;
            fireStateChanged();
        }
    }

    public Number getStepSize() {
        return this.stepSize;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v11, types: [java.lang.Double] */
    /* JADX WARN: Type inference failed for: r0v44, types: [java.lang.Byte] */
    /* JADX WARN: Type inference failed for: r0v48, types: [java.lang.Short] */
    /* JADX WARN: Type inference failed for: r0v51, types: [java.lang.Integer] */
    /* JADX WARN: Type inference failed for: r0v53, types: [java.lang.Long] */
    private Number incrValue(int i2) {
        Float f2;
        if ((this.value instanceof Float) || (this.value instanceof Double)) {
            double dDoubleValue = this.value.doubleValue() + (this.stepSize.doubleValue() * i2);
            if (this.value instanceof Double) {
                f2 = new Double(dDoubleValue);
            } else {
                f2 = new Float(dDoubleValue);
            }
        } else {
            long jLongValue = this.value.longValue() + (this.stepSize.longValue() * i2);
            if (this.value instanceof Long) {
                f2 = Long.valueOf(jLongValue);
            } else if (this.value instanceof Integer) {
                f2 = Integer.valueOf((int) jLongValue);
            } else if (this.value instanceof Short) {
                f2 = Short.valueOf((short) jLongValue);
            } else {
                f2 = Byte.valueOf((byte) jLongValue);
            }
        }
        if (this.maximum != null && this.maximum.compareTo(f2) < 0) {
            return null;
        }
        if (this.minimum != null && this.minimum.compareTo(f2) > 0) {
            return null;
        }
        return f2;
    }

    @Override // javax.swing.SpinnerModel
    public Object getNextValue() {
        return incrValue(1);
    }

    @Override // javax.swing.SpinnerModel
    public Object getPreviousValue() {
        return incrValue(-1);
    }

    public Number getNumber() {
        return this.value;
    }

    @Override // javax.swing.SpinnerModel
    public Object getValue() {
        return this.value;
    }

    @Override // javax.swing.SpinnerModel
    public void setValue(Object obj) {
        if (obj == null || !(obj instanceof Number)) {
            throw new IllegalArgumentException("illegal value");
        }
        if (!obj.equals(this.value)) {
            this.value = (Number) obj;
            fireStateChanged();
        }
    }
}
