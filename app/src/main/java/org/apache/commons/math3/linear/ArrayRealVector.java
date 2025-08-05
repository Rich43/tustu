package org.apache.commons.math3.linear;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathUtils;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/linear/ArrayRealVector.class */
public class ArrayRealVector extends RealVector implements Serializable {
    private static final long serialVersionUID = -1097961340710804027L;
    private static final RealVectorFormat DEFAULT_FORMAT = RealVectorFormat.getInstance();
    private double[] data;

    public ArrayRealVector() {
        this.data = new double[0];
    }

    public ArrayRealVector(int size) {
        this.data = new double[size];
    }

    public ArrayRealVector(int size, double preset) {
        this.data = new double[size];
        Arrays.fill(this.data, preset);
    }

    public ArrayRealVector(double[] d2) {
        this.data = (double[]) d2.clone();
    }

    public ArrayRealVector(double[] d2, boolean copyArray) throws NullArgumentException {
        if (d2 == null) {
            throw new NullArgumentException();
        }
        this.data = copyArray ? (double[]) d2.clone() : d2;
    }

    public ArrayRealVector(double[] d2, int pos, int size) throws NullArgumentException, NumberIsTooLargeException {
        if (d2 == null) {
            throw new NullArgumentException();
        }
        if (d2.length < pos + size) {
            throw new NumberIsTooLargeException(Integer.valueOf(pos + size), Integer.valueOf(d2.length), true);
        }
        this.data = new double[size];
        System.arraycopy(d2, pos, this.data, 0, size);
    }

    public ArrayRealVector(Double[] d2) {
        this.data = new double[d2.length];
        for (int i2 = 0; i2 < d2.length; i2++) {
            this.data[i2] = d2[i2].doubleValue();
        }
    }

    public ArrayRealVector(Double[] d2, int pos, int size) throws NullArgumentException, NumberIsTooLargeException {
        if (d2 == null) {
            throw new NullArgumentException();
        }
        if (d2.length < pos + size) {
            throw new NumberIsTooLargeException(Integer.valueOf(pos + size), Integer.valueOf(d2.length), true);
        }
        this.data = new double[size];
        for (int i2 = pos; i2 < pos + size; i2++) {
            this.data[i2 - pos] = d2[i2].doubleValue();
        }
    }

    public ArrayRealVector(RealVector v2) throws NullArgumentException {
        if (v2 == null) {
            throw new NullArgumentException();
        }
        this.data = new double[v2.getDimension()];
        for (int i2 = 0; i2 < this.data.length; i2++) {
            this.data[i2] = v2.getEntry(i2);
        }
    }

    public ArrayRealVector(ArrayRealVector v2) throws NullArgumentException {
        this(v2, true);
    }

    public ArrayRealVector(ArrayRealVector v2, boolean deep) {
        this.data = deep ? (double[]) v2.data.clone() : v2.data;
    }

    public ArrayRealVector(ArrayRealVector v1, ArrayRealVector v2) {
        this.data = new double[v1.data.length + v2.data.length];
        System.arraycopy(v1.data, 0, this.data, 0, v1.data.length);
        System.arraycopy(v2.data, 0, this.data, v1.data.length, v2.data.length);
    }

    public ArrayRealVector(ArrayRealVector v1, RealVector v2) {
        int l1 = v1.data.length;
        int l2 = v2.getDimension();
        this.data = new double[l1 + l2];
        System.arraycopy(v1.data, 0, this.data, 0, l1);
        for (int i2 = 0; i2 < l2; i2++) {
            this.data[l1 + i2] = v2.getEntry(i2);
        }
    }

    public ArrayRealVector(RealVector v1, ArrayRealVector v2) {
        int l1 = v1.getDimension();
        int l2 = v2.data.length;
        this.data = new double[l1 + l2];
        for (int i2 = 0; i2 < l1; i2++) {
            this.data[i2] = v1.getEntry(i2);
        }
        System.arraycopy(v2.data, 0, this.data, l1, l2);
    }

    public ArrayRealVector(ArrayRealVector v1, double[] v2) {
        int l1 = v1.getDimension();
        int l2 = v2.length;
        this.data = new double[l1 + l2];
        System.arraycopy(v1.data, 0, this.data, 0, l1);
        System.arraycopy(v2, 0, this.data, l1, l2);
    }

    public ArrayRealVector(double[] v1, ArrayRealVector v2) {
        int l1 = v1.length;
        int l2 = v2.getDimension();
        this.data = new double[l1 + l2];
        System.arraycopy(v1, 0, this.data, 0, l1);
        System.arraycopy(v2.data, 0, this.data, l1, l2);
    }

    public ArrayRealVector(double[] v1, double[] v2) {
        int l1 = v1.length;
        int l2 = v2.length;
        this.data = new double[l1 + l2];
        System.arraycopy(v1, 0, this.data, 0, l1);
        System.arraycopy(v2, 0, this.data, l1, l2);
    }

    @Override // org.apache.commons.math3.linear.RealVector
    public ArrayRealVector copy() {
        return new ArrayRealVector(this, true);
    }

    @Override // org.apache.commons.math3.linear.RealVector
    public ArrayRealVector add(RealVector v2) throws DimensionMismatchException {
        if (v2 instanceof ArrayRealVector) {
            double[] vData = ((ArrayRealVector) v2).data;
            int dim = vData.length;
            checkVectorDimensions(dim);
            ArrayRealVector result = new ArrayRealVector(dim);
            double[] resultData = result.data;
            for (int i2 = 0; i2 < dim; i2++) {
                resultData[i2] = this.data[i2] + vData[i2];
            }
            return result;
        }
        checkVectorDimensions(v2);
        double[] out = (double[]) this.data.clone();
        Iterator<RealVector.Entry> it = v2.iterator();
        while (it.hasNext()) {
            RealVector.Entry e2 = it.next();
            int index = e2.getIndex();
            out[index] = out[index] + e2.getValue();
        }
        return new ArrayRealVector(out, false);
    }

    @Override // org.apache.commons.math3.linear.RealVector
    public ArrayRealVector subtract(RealVector v2) throws DimensionMismatchException {
        if (v2 instanceof ArrayRealVector) {
            double[] vData = ((ArrayRealVector) v2).data;
            int dim = vData.length;
            checkVectorDimensions(dim);
            ArrayRealVector result = new ArrayRealVector(dim);
            double[] resultData = result.data;
            for (int i2 = 0; i2 < dim; i2++) {
                resultData[i2] = this.data[i2] - vData[i2];
            }
            return result;
        }
        checkVectorDimensions(v2);
        double[] out = (double[]) this.data.clone();
        Iterator<RealVector.Entry> it = v2.iterator();
        while (it.hasNext()) {
            RealVector.Entry e2 = it.next();
            int index = e2.getIndex();
            out[index] = out[index] - e2.getValue();
        }
        return new ArrayRealVector(out, false);
    }

    @Override // org.apache.commons.math3.linear.RealVector
    public ArrayRealVector map(UnivariateFunction function) {
        return copy().mapToSelf(function);
    }

    @Override // org.apache.commons.math3.linear.RealVector
    public ArrayRealVector mapToSelf(UnivariateFunction function) {
        for (int i2 = 0; i2 < this.data.length; i2++) {
            this.data[i2] = function.value(this.data[i2]);
        }
        return this;
    }

    @Override // org.apache.commons.math3.linear.RealVector
    public RealVector mapAddToSelf(double d2) {
        for (int i2 = 0; i2 < this.data.length; i2++) {
            double[] dArr = this.data;
            int i3 = i2;
            dArr[i3] = dArr[i3] + d2;
        }
        return this;
    }

    @Override // org.apache.commons.math3.linear.RealVector
    public RealVector mapSubtractToSelf(double d2) {
        for (int i2 = 0; i2 < this.data.length; i2++) {
            double[] dArr = this.data;
            int i3 = i2;
            dArr[i3] = dArr[i3] - d2;
        }
        return this;
    }

    @Override // org.apache.commons.math3.linear.RealVector
    public RealVector mapMultiplyToSelf(double d2) {
        for (int i2 = 0; i2 < this.data.length; i2++) {
            double[] dArr = this.data;
            int i3 = i2;
            dArr[i3] = dArr[i3] * d2;
        }
        return this;
    }

    @Override // org.apache.commons.math3.linear.RealVector
    public RealVector mapDivideToSelf(double d2) {
        for (int i2 = 0; i2 < this.data.length; i2++) {
            double[] dArr = this.data;
            int i3 = i2;
            dArr[i3] = dArr[i3] / d2;
        }
        return this;
    }

    @Override // org.apache.commons.math3.linear.RealVector
    public ArrayRealVector ebeMultiply(RealVector v2) throws DimensionMismatchException {
        if (v2 instanceof ArrayRealVector) {
            double[] vData = ((ArrayRealVector) v2).data;
            int dim = vData.length;
            checkVectorDimensions(dim);
            ArrayRealVector result = new ArrayRealVector(dim);
            double[] resultData = result.data;
            for (int i2 = 0; i2 < dim; i2++) {
                resultData[i2] = this.data[i2] * vData[i2];
            }
            return result;
        }
        checkVectorDimensions(v2);
        double[] out = (double[]) this.data.clone();
        for (int i3 = 0; i3 < this.data.length; i3++) {
            int i4 = i3;
            out[i4] = out[i4] * v2.getEntry(i3);
        }
        return new ArrayRealVector(out, false);
    }

    @Override // org.apache.commons.math3.linear.RealVector
    public ArrayRealVector ebeDivide(RealVector v2) throws DimensionMismatchException {
        if (v2 instanceof ArrayRealVector) {
            double[] vData = ((ArrayRealVector) v2).data;
            int dim = vData.length;
            checkVectorDimensions(dim);
            ArrayRealVector result = new ArrayRealVector(dim);
            double[] resultData = result.data;
            for (int i2 = 0; i2 < dim; i2++) {
                resultData[i2] = this.data[i2] / vData[i2];
            }
            return result;
        }
        checkVectorDimensions(v2);
        double[] out = (double[]) this.data.clone();
        for (int i3 = 0; i3 < this.data.length; i3++) {
            int i4 = i3;
            out[i4] = out[i4] / v2.getEntry(i3);
        }
        return new ArrayRealVector(out, false);
    }

    public double[] getDataRef() {
        return this.data;
    }

    @Override // org.apache.commons.math3.linear.RealVector
    public double dotProduct(RealVector v2) throws DimensionMismatchException {
        if (v2 instanceof ArrayRealVector) {
            double[] vData = ((ArrayRealVector) v2).data;
            checkVectorDimensions(vData.length);
            double dot = 0.0d;
            for (int i2 = 0; i2 < this.data.length; i2++) {
                dot += this.data[i2] * vData[i2];
            }
            return dot;
        }
        return super.dotProduct(v2);
    }

    @Override // org.apache.commons.math3.linear.RealVector
    public double getNorm() {
        double sum = 0.0d;
        double[] arr$ = this.data;
        for (double a2 : arr$) {
            sum += a2 * a2;
        }
        return FastMath.sqrt(sum);
    }

    @Override // org.apache.commons.math3.linear.RealVector
    public double getL1Norm() {
        double sum = 0.0d;
        double[] arr$ = this.data;
        for (double a2 : arr$) {
            sum += FastMath.abs(a2);
        }
        return sum;
    }

    @Override // org.apache.commons.math3.linear.RealVector
    public double getLInfNorm() {
        double max = 0.0d;
        double[] arr$ = this.data;
        for (double a2 : arr$) {
            max = FastMath.max(max, FastMath.abs(a2));
        }
        return max;
    }

    @Override // org.apache.commons.math3.linear.RealVector
    public double getDistance(RealVector v2) throws DimensionMismatchException {
        if (v2 instanceof ArrayRealVector) {
            double[] vData = ((ArrayRealVector) v2).data;
            checkVectorDimensions(vData.length);
            double sum = 0.0d;
            for (int i2 = 0; i2 < this.data.length; i2++) {
                double delta = this.data[i2] - vData[i2];
                sum += delta * delta;
            }
            return FastMath.sqrt(sum);
        }
        checkVectorDimensions(v2);
        double sum2 = 0.0d;
        for (int i3 = 0; i3 < this.data.length; i3++) {
            double delta2 = this.data[i3] - v2.getEntry(i3);
            sum2 += delta2 * delta2;
        }
        return FastMath.sqrt(sum2);
    }

    @Override // org.apache.commons.math3.linear.RealVector
    public double getL1Distance(RealVector v2) throws DimensionMismatchException {
        if (v2 instanceof ArrayRealVector) {
            double[] vData = ((ArrayRealVector) v2).data;
            checkVectorDimensions(vData.length);
            double sum = 0.0d;
            for (int i2 = 0; i2 < this.data.length; i2++) {
                double delta = this.data[i2] - vData[i2];
                sum += FastMath.abs(delta);
            }
            return sum;
        }
        checkVectorDimensions(v2);
        double sum2 = 0.0d;
        for (int i3 = 0; i3 < this.data.length; i3++) {
            double delta2 = this.data[i3] - v2.getEntry(i3);
            sum2 += FastMath.abs(delta2);
        }
        return sum2;
    }

    @Override // org.apache.commons.math3.linear.RealVector
    public double getLInfDistance(RealVector v2) throws DimensionMismatchException {
        if (v2 instanceof ArrayRealVector) {
            double[] vData = ((ArrayRealVector) v2).data;
            checkVectorDimensions(vData.length);
            double max = 0.0d;
            for (int i2 = 0; i2 < this.data.length; i2++) {
                double delta = this.data[i2] - vData[i2];
                max = FastMath.max(max, FastMath.abs(delta));
            }
            return max;
        }
        checkVectorDimensions(v2);
        double max2 = 0.0d;
        for (int i3 = 0; i3 < this.data.length; i3++) {
            double delta2 = this.data[i3] - v2.getEntry(i3);
            max2 = FastMath.max(max2, FastMath.abs(delta2));
        }
        return max2;
    }

    @Override // org.apache.commons.math3.linear.RealVector
    public RealMatrix outerProduct(RealVector v2) throws OutOfRangeException {
        if (v2 instanceof ArrayRealVector) {
            double[] vData = ((ArrayRealVector) v2).data;
            int m2 = this.data.length;
            int n2 = vData.length;
            RealMatrix out = MatrixUtils.createRealMatrix(m2, n2);
            for (int i2 = 0; i2 < m2; i2++) {
                for (int j2 = 0; j2 < n2; j2++) {
                    out.setEntry(i2, j2, this.data[i2] * vData[j2]);
                }
            }
            return out;
        }
        int m3 = this.data.length;
        int n3 = v2.getDimension();
        RealMatrix out2 = MatrixUtils.createRealMatrix(m3, n3);
        for (int i3 = 0; i3 < m3; i3++) {
            for (int j3 = 0; j3 < n3; j3++) {
                out2.setEntry(i3, j3, this.data[i3] * v2.getEntry(j3));
            }
        }
        return out2;
    }

    @Override // org.apache.commons.math3.linear.RealVector
    public double getEntry(int index) throws OutOfRangeException {
        try {
            return this.data[index];
        } catch (IndexOutOfBoundsException e2) {
            throw new OutOfRangeException(LocalizedFormats.INDEX, Integer.valueOf(index), 0, Integer.valueOf(getDimension() - 1));
        }
    }

    @Override // org.apache.commons.math3.linear.RealVector
    public int getDimension() {
        return this.data.length;
    }

    @Override // org.apache.commons.math3.linear.RealVector
    public RealVector append(RealVector v2) {
        try {
            return new ArrayRealVector(this, (ArrayRealVector) v2);
        } catch (ClassCastException e2) {
            return new ArrayRealVector(this, v2);
        }
    }

    public ArrayRealVector append(ArrayRealVector v2) {
        return new ArrayRealVector(this, v2);
    }

    @Override // org.apache.commons.math3.linear.RealVector
    public RealVector append(double in) {
        double[] out = new double[this.data.length + 1];
        System.arraycopy(this.data, 0, out, 0, this.data.length);
        out[this.data.length] = in;
        return new ArrayRealVector(out, false);
    }

    @Override // org.apache.commons.math3.linear.RealVector
    public RealVector getSubVector(int index, int n2) throws OutOfRangeException, NotPositiveException {
        if (n2 < 0) {
            throw new NotPositiveException(LocalizedFormats.NUMBER_OF_ELEMENTS_SHOULD_BE_POSITIVE, Integer.valueOf(n2));
        }
        ArrayRealVector out = new ArrayRealVector(n2);
        try {
            System.arraycopy(this.data, index, out.data, 0, n2);
        } catch (IndexOutOfBoundsException e2) {
            checkIndex(index);
            checkIndex((index + n2) - 1);
        }
        return out;
    }

    @Override // org.apache.commons.math3.linear.RealVector
    public void setEntry(int index, double value) throws OutOfRangeException {
        try {
            this.data[index] = value;
        } catch (IndexOutOfBoundsException e2) {
            checkIndex(index);
        }
    }

    @Override // org.apache.commons.math3.linear.RealVector
    public void addToEntry(int index, double increment) throws OutOfRangeException {
        try {
            double[] dArr = this.data;
            dArr[index] = dArr[index] + increment;
        } catch (IndexOutOfBoundsException e2) {
            throw new OutOfRangeException(LocalizedFormats.INDEX, Integer.valueOf(index), 0, Integer.valueOf(this.data.length - 1));
        }
    }

    @Override // org.apache.commons.math3.linear.RealVector
    public void setSubVector(int index, RealVector v2) throws OutOfRangeException {
        if (v2 instanceof ArrayRealVector) {
            setSubVector(index, ((ArrayRealVector) v2).data);
            return;
        }
        for (int i2 = index; i2 < index + v2.getDimension(); i2++) {
            try {
                this.data[i2] = v2.getEntry(i2 - index);
            } catch (IndexOutOfBoundsException e2) {
                checkIndex(index);
                checkIndex((index + v2.getDimension()) - 1);
                return;
            }
        }
    }

    public void setSubVector(int index, double[] v2) throws OutOfRangeException {
        try {
            System.arraycopy(v2, 0, this.data, index, v2.length);
        } catch (IndexOutOfBoundsException e2) {
            checkIndex(index);
            checkIndex((index + v2.length) - 1);
        }
    }

    @Override // org.apache.commons.math3.linear.RealVector
    public void set(double value) {
        Arrays.fill(this.data, value);
    }

    @Override // org.apache.commons.math3.linear.RealVector
    public double[] toArray() {
        return (double[]) this.data.clone();
    }

    public String toString() {
        return DEFAULT_FORMAT.format(this);
    }

    @Override // org.apache.commons.math3.linear.RealVector
    protected void checkVectorDimensions(RealVector v2) throws DimensionMismatchException {
        checkVectorDimensions(v2.getDimension());
    }

    @Override // org.apache.commons.math3.linear.RealVector
    protected void checkVectorDimensions(int n2) throws DimensionMismatchException {
        if (this.data.length != n2) {
            throw new DimensionMismatchException(this.data.length, n2);
        }
    }

    @Override // org.apache.commons.math3.linear.RealVector
    public boolean isNaN() {
        double[] arr$ = this.data;
        for (double v2 : arr$) {
            if (Double.isNaN(v2)) {
                return true;
            }
        }
        return false;
    }

    @Override // org.apache.commons.math3.linear.RealVector
    public boolean isInfinite() {
        if (isNaN()) {
            return false;
        }
        double[] arr$ = this.data;
        for (double v2 : arr$) {
            if (Double.isInfinite(v2)) {
                return true;
            }
        }
        return false;
    }

    @Override // org.apache.commons.math3.linear.RealVector
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof RealVector)) {
            return false;
        }
        RealVector rhs = (RealVector) other;
        if (this.data.length != rhs.getDimension()) {
            return false;
        }
        if (rhs.isNaN()) {
            return isNaN();
        }
        for (int i2 = 0; i2 < this.data.length; i2++) {
            if (this.data[i2] != rhs.getEntry(i2)) {
                return false;
            }
        }
        return true;
    }

    @Override // org.apache.commons.math3.linear.RealVector
    public int hashCode() {
        if (isNaN()) {
            return 9;
        }
        return MathUtils.hash(this.data);
    }

    @Override // org.apache.commons.math3.linear.RealVector
    public ArrayRealVector combine(double a2, double b2, RealVector y2) throws DimensionMismatchException {
        return copy().combineToSelf(a2, b2, y2);
    }

    @Override // org.apache.commons.math3.linear.RealVector
    public ArrayRealVector combineToSelf(double a2, double b2, RealVector y2) throws DimensionMismatchException {
        if (y2 instanceof ArrayRealVector) {
            double[] yData = ((ArrayRealVector) y2).data;
            checkVectorDimensions(yData.length);
            for (int i2 = 0; i2 < this.data.length; i2++) {
                this.data[i2] = (a2 * this.data[i2]) + (b2 * yData[i2]);
            }
        } else {
            checkVectorDimensions(y2);
            for (int i3 = 0; i3 < this.data.length; i3++) {
                this.data[i3] = (a2 * this.data[i3]) + (b2 * y2.getEntry(i3));
            }
        }
        return this;
    }

    @Override // org.apache.commons.math3.linear.RealVector
    public double walkInDefaultOrder(RealVectorPreservingVisitor visitor) {
        visitor.start(this.data.length, 0, this.data.length - 1);
        for (int i2 = 0; i2 < this.data.length; i2++) {
            visitor.visit(i2, this.data[i2]);
        }
        return visitor.end();
    }

    @Override // org.apache.commons.math3.linear.RealVector
    public double walkInDefaultOrder(RealVectorPreservingVisitor visitor, int start, int end) throws NumberIsTooSmallException, OutOfRangeException {
        checkIndices(start, end);
        visitor.start(this.data.length, start, end);
        for (int i2 = start; i2 <= end; i2++) {
            visitor.visit(i2, this.data[i2]);
        }
        return visitor.end();
    }

    @Override // org.apache.commons.math3.linear.RealVector
    public double walkInOptimizedOrder(RealVectorPreservingVisitor visitor) {
        return walkInDefaultOrder(visitor);
    }

    @Override // org.apache.commons.math3.linear.RealVector
    public double walkInOptimizedOrder(RealVectorPreservingVisitor visitor, int start, int end) throws NumberIsTooSmallException, OutOfRangeException {
        return walkInDefaultOrder(visitor, start, end);
    }

    @Override // org.apache.commons.math3.linear.RealVector
    public double walkInDefaultOrder(RealVectorChangingVisitor visitor) {
        visitor.start(this.data.length, 0, this.data.length - 1);
        for (int i2 = 0; i2 < this.data.length; i2++) {
            this.data[i2] = visitor.visit(i2, this.data[i2]);
        }
        return visitor.end();
    }

    @Override // org.apache.commons.math3.linear.RealVector
    public double walkInDefaultOrder(RealVectorChangingVisitor visitor, int start, int end) throws NumberIsTooSmallException, OutOfRangeException {
        checkIndices(start, end);
        visitor.start(this.data.length, start, end);
        for (int i2 = start; i2 <= end; i2++) {
            this.data[i2] = visitor.visit(i2, this.data[i2]);
        }
        return visitor.end();
    }

    @Override // org.apache.commons.math3.linear.RealVector
    public double walkInOptimizedOrder(RealVectorChangingVisitor visitor) {
        return walkInDefaultOrder(visitor);
    }

    @Override // org.apache.commons.math3.linear.RealVector
    public double walkInOptimizedOrder(RealVectorChangingVisitor visitor, int start, int end) throws NumberIsTooSmallException, OutOfRangeException {
        return walkInDefaultOrder(visitor, start, end);
    }
}
