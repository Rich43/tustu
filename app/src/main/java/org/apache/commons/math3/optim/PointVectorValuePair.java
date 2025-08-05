package org.apache.commons.math3.optim;

import java.io.Serializable;
import org.apache.commons.math3.util.Pair;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optim/PointVectorValuePair.class */
public class PointVectorValuePair extends Pair<double[], double[]> implements Serializable {
    private static final long serialVersionUID = 20120513;

    public PointVectorValuePair(double[] point, double[] value) {
        this(point, value, true);
    }

    public PointVectorValuePair(double[] point, double[] value, boolean copyArray) {
        super(copyArray ? point == null ? null : (double[]) point.clone() : point, copyArray ? value == null ? null : (double[]) value.clone() : value);
    }

    public double[] getPoint() {
        double[] p2 = getKey();
        if (p2 == null) {
            return null;
        }
        return (double[]) p2.clone();
    }

    public double[] getPointRef() {
        return getKey();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.util.Pair
    public double[] getValue() {
        double[] v2 = (double[]) super.getValue();
        if (v2 == null) {
            return null;
        }
        return (double[]) v2.clone();
    }

    public double[] getValueRef() {
        return (double[]) super.getValue();
    }

    private Object writeReplace() {
        return new DataTransferObject(getKey(), getValue());
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optim/PointVectorValuePair$DataTransferObject.class */
    private static class DataTransferObject implements Serializable {
        private static final long serialVersionUID = 20120513;
        private final double[] point;
        private final double[] value;

        DataTransferObject(double[] point, double[] value) {
            this.point = (double[]) point.clone();
            this.value = (double[]) value.clone();
        }

        private Object readResolve() {
            return new PointVectorValuePair(this.point, this.value, false);
        }
    }
}
