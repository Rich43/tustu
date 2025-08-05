package org.apache.commons.math3.linear;

import java.io.Serializable;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.OpenIntToDoubleHashMap;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/linear/OpenMapRealVector.class */
public class OpenMapRealVector extends SparseRealVector implements Serializable {
    public static final double DEFAULT_ZERO_TOLERANCE = 1.0E-12d;
    private static final long serialVersionUID = 8772222695580707260L;
    private final OpenIntToDoubleHashMap entries;
    private final int virtualSize;
    private final double epsilon;

    public OpenMapRealVector() {
        this(0, 1.0E-12d);
    }

    public OpenMapRealVector(int dimension) {
        this(dimension, 1.0E-12d);
    }

    public OpenMapRealVector(int dimension, double epsilon) {
        this.virtualSize = dimension;
        this.entries = new OpenIntToDoubleHashMap(0.0d);
        this.epsilon = epsilon;
    }

    protected OpenMapRealVector(OpenMapRealVector v2, int resize) {
        this.virtualSize = v2.getDimension() + resize;
        this.entries = new OpenIntToDoubleHashMap(v2.entries);
        this.epsilon = v2.epsilon;
    }

    public OpenMapRealVector(int dimension, int expectedSize) {
        this(dimension, expectedSize, 1.0E-12d);
    }

    public OpenMapRealVector(int dimension, int expectedSize, double epsilon) {
        this.virtualSize = dimension;
        this.entries = new OpenIntToDoubleHashMap(expectedSize, 0.0d);
        this.epsilon = epsilon;
    }

    public OpenMapRealVector(double[] values) {
        this(values, 1.0E-12d);
    }

    public OpenMapRealVector(double[] values, double epsilon) {
        this.virtualSize = values.length;
        this.entries = new OpenIntToDoubleHashMap(0.0d);
        this.epsilon = epsilon;
        for (int key = 0; key < values.length; key++) {
            double value = values[key];
            if (!isDefaultValue(value)) {
                this.entries.put(key, value);
            }
        }
    }

    public OpenMapRealVector(Double[] values) {
        this(values, 1.0E-12d);
    }

    public OpenMapRealVector(Double[] values, double epsilon) {
        this.virtualSize = values.length;
        this.entries = new OpenIntToDoubleHashMap(0.0d);
        this.epsilon = epsilon;
        for (int key = 0; key < values.length; key++) {
            double value = values[key].doubleValue();
            if (!isDefaultValue(value)) {
                this.entries.put(key, value);
            }
        }
    }

    public OpenMapRealVector(OpenMapRealVector v2) {
        this.virtualSize = v2.getDimension();
        this.entries = new OpenIntToDoubleHashMap(v2.getEntries());
        this.epsilon = v2.epsilon;
    }

    public OpenMapRealVector(RealVector v2) throws OutOfRangeException {
        this.virtualSize = v2.getDimension();
        this.entries = new OpenIntToDoubleHashMap(0.0d);
        this.epsilon = 1.0E-12d;
        for (int key = 0; key < this.virtualSize; key++) {
            double value = v2.getEntry(key);
            if (!isDefaultValue(value)) {
                this.entries.put(key, value);
            }
        }
    }

    private OpenIntToDoubleHashMap getEntries() {
        return this.entries;
    }

    protected boolean isDefaultValue(double value) {
        return FastMath.abs(value) < this.epsilon;
    }

    @Override // org.apache.commons.math3.linear.RealVector
    public RealVector add(RealVector v2) throws DimensionMismatchException {
        checkVectorDimensions(v2.getDimension());
        if (v2 instanceof OpenMapRealVector) {
            return add((OpenMapRealVector) v2);
        }
        return super.add(v2);
    }

    public OpenMapRealVector add(OpenMapRealVector v2) throws OutOfRangeException, DimensionMismatchException, NoSuchElementException, ConcurrentModificationException {
        checkVectorDimensions(v2.getDimension());
        boolean copyThis = this.entries.size() > v2.entries.size();
        OpenMapRealVector res = copyThis ? copy() : v2.copy();
        OpenIntToDoubleHashMap.Iterator iter = copyThis ? v2.entries.iterator() : this.entries.iterator();
        OpenIntToDoubleHashMap randomAccess = copyThis ? this.entries : v2.entries;
        while (iter.hasNext()) {
            iter.advance();
            int key = iter.key();
            if (randomAccess.containsKey(key)) {
                res.setEntry(key, randomAccess.get(key) + iter.value());
            } else {
                res.setEntry(key, iter.value());
            }
        }
        return res;
    }

    public OpenMapRealVector append(OpenMapRealVector v2) throws OutOfRangeException, NoSuchElementException, ConcurrentModificationException {
        OpenMapRealVector res = new OpenMapRealVector(this, v2.getDimension());
        OpenIntToDoubleHashMap.Iterator iter = v2.entries.iterator();
        while (iter.hasNext()) {
            iter.advance();
            res.setEntry(iter.key() + this.virtualSize, iter.value());
        }
        return res;
    }

    @Override // org.apache.commons.math3.linear.RealVector
    public OpenMapRealVector append(RealVector v2) throws OutOfRangeException {
        if (v2 instanceof OpenMapRealVector) {
            return append((OpenMapRealVector) v2);
        }
        OpenMapRealVector res = new OpenMapRealVector(this, v2.getDimension());
        for (int i2 = 0; i2 < v2.getDimension(); i2++) {
            res.setEntry(i2 + this.virtualSize, v2.getEntry(i2));
        }
        return res;
    }

    @Override // org.apache.commons.math3.linear.RealVector
    public OpenMapRealVector append(double d2) throws OutOfRangeException {
        OpenMapRealVector res = new OpenMapRealVector(this, 1);
        res.setEntry(this.virtualSize, d2);
        return res;
    }

    @Override // org.apache.commons.math3.linear.RealVector
    public OpenMapRealVector copy() {
        return new OpenMapRealVector(this);
    }

    @Deprecated
    public double dotProduct(OpenMapRealVector v2) throws DimensionMismatchException {
        return dotProduct((RealVector) v2);
    }

    @Override // org.apache.commons.math3.linear.RealVector
    public OpenMapRealVector ebeDivide(RealVector v2) throws OutOfRangeException, DimensionMismatchException {
        checkVectorDimensions(v2.getDimension());
        OpenMapRealVector res = new OpenMapRealVector(this);
        int n2 = getDimension();
        for (int i2 = 0; i2 < n2; i2++) {
            res.setEntry(i2, getEntry(i2) / v2.getEntry(i2));
        }
        return res;
    }

    @Override // org.apache.commons.math3.linear.RealVector
    public OpenMapRealVector ebeMultiply(RealVector v2) throws OutOfRangeException, DimensionMismatchException, NoSuchElementException, ConcurrentModificationException {
        checkVectorDimensions(v2.getDimension());
        OpenMapRealVector res = new OpenMapRealVector(this);
        OpenIntToDoubleHashMap.Iterator iter = this.entries.iterator();
        while (iter.hasNext()) {
            iter.advance();
            res.setEntry(iter.key(), iter.value() * v2.getEntry(iter.key()));
        }
        return res;
    }

    @Override // org.apache.commons.math3.linear.RealVector
    public OpenMapRealVector getSubVector(int index, int n2) throws OutOfRangeException, NotPositiveException, NoSuchElementException, ConcurrentModificationException {
        checkIndex(index);
        if (n2 < 0) {
            throw new NotPositiveException(LocalizedFormats.NUMBER_OF_ELEMENTS_SHOULD_BE_POSITIVE, Integer.valueOf(n2));
        }
        checkIndex((index + n2) - 1);
        OpenMapRealVector res = new OpenMapRealVector(n2);
        int end = index + n2;
        OpenIntToDoubleHashMap.Iterator iter = this.entries.iterator();
        while (iter.hasNext()) {
            iter.advance();
            int key = iter.key();
            if (key >= index && key < end) {
                res.setEntry(key - index, iter.value());
            }
        }
        return res;
    }

    @Override // org.apache.commons.math3.linear.RealVector
    public int getDimension() {
        return this.virtualSize;
    }

    public double getDistance(OpenMapRealVector v2) throws DimensionMismatchException, NoSuchElementException, ConcurrentModificationException {
        double res;
        checkVectorDimensions(v2.getDimension());
        OpenIntToDoubleHashMap.Iterator iter = this.entries.iterator();
        double d2 = 0.0d;
        while (true) {
            res = d2;
            if (!iter.hasNext()) {
                break;
            }
            iter.advance();
            int key = iter.key();
            double delta = iter.value() - v2.getEntry(key);
            d2 = res + (delta * delta);
        }
        OpenIntToDoubleHashMap.Iterator iter2 = v2.getEntries().iterator();
        while (iter2.hasNext()) {
            iter2.advance();
            int key2 = iter2.key();
            if (!this.entries.containsKey(key2)) {
                double value = iter2.value();
                res += value * value;
            }
        }
        return FastMath.sqrt(res);
    }

    @Override // org.apache.commons.math3.linear.RealVector
    public double getDistance(RealVector v2) throws DimensionMismatchException {
        checkVectorDimensions(v2.getDimension());
        if (v2 instanceof OpenMapRealVector) {
            return getDistance((OpenMapRealVector) v2);
        }
        return super.getDistance(v2);
    }

    @Override // org.apache.commons.math3.linear.RealVector
    public double getEntry(int index) throws OutOfRangeException {
        checkIndex(index);
        return this.entries.get(index);
    }

    public double getL1Distance(OpenMapRealVector v2) throws DimensionMismatchException, NoSuchElementException, ConcurrentModificationException {
        checkVectorDimensions(v2.getDimension());
        double max = 0.0d;
        OpenIntToDoubleHashMap.Iterator iter = this.entries.iterator();
        while (iter.hasNext()) {
            iter.advance();
            double delta = FastMath.abs(iter.value() - v2.getEntry(iter.key()));
            max += delta;
        }
        OpenIntToDoubleHashMap.Iterator iter2 = v2.getEntries().iterator();
        while (iter2.hasNext()) {
            iter2.advance();
            int key = iter2.key();
            if (!this.entries.containsKey(key)) {
                double delta2 = FastMath.abs(iter2.value());
                max += FastMath.abs(delta2);
            }
        }
        return max;
    }

    @Override // org.apache.commons.math3.linear.RealVector
    public double getL1Distance(RealVector v2) throws DimensionMismatchException {
        checkVectorDimensions(v2.getDimension());
        if (v2 instanceof OpenMapRealVector) {
            return getL1Distance((OpenMapRealVector) v2);
        }
        return super.getL1Distance(v2);
    }

    private double getLInfDistance(OpenMapRealVector v2) throws DimensionMismatchException, NoSuchElementException, ConcurrentModificationException {
        checkVectorDimensions(v2.getDimension());
        double max = 0.0d;
        OpenIntToDoubleHashMap.Iterator iter = this.entries.iterator();
        while (iter.hasNext()) {
            iter.advance();
            double delta = FastMath.abs(iter.value() - v2.getEntry(iter.key()));
            if (delta > max) {
                max = delta;
            }
        }
        OpenIntToDoubleHashMap.Iterator iter2 = v2.getEntries().iterator();
        while (iter2.hasNext()) {
            iter2.advance();
            int key = iter2.key();
            if (!this.entries.containsKey(key) && iter2.value() > max) {
                max = iter2.value();
            }
        }
        return max;
    }

    @Override // org.apache.commons.math3.linear.RealVector
    public double getLInfDistance(RealVector v2) throws DimensionMismatchException {
        checkVectorDimensions(v2.getDimension());
        if (v2 instanceof OpenMapRealVector) {
            return getLInfDistance((OpenMapRealVector) v2);
        }
        return super.getLInfDistance(v2);
    }

    @Override // org.apache.commons.math3.linear.RealVector
    public boolean isInfinite() throws NoSuchElementException, ConcurrentModificationException {
        boolean infiniteFound = false;
        OpenIntToDoubleHashMap.Iterator iter = this.entries.iterator();
        while (iter.hasNext()) {
            iter.advance();
            double value = iter.value();
            if (Double.isNaN(value)) {
                return false;
            }
            if (Double.isInfinite(value)) {
                infiniteFound = true;
            }
        }
        return infiniteFound;
    }

    @Override // org.apache.commons.math3.linear.RealVector
    public boolean isNaN() throws NoSuchElementException, ConcurrentModificationException {
        OpenIntToDoubleHashMap.Iterator iter = this.entries.iterator();
        while (iter.hasNext()) {
            iter.advance();
            if (Double.isNaN(iter.value())) {
                return true;
            }
        }
        return false;
    }

    @Override // org.apache.commons.math3.linear.RealVector
    public OpenMapRealVector mapAdd(double d2) {
        return copy().mapAddToSelf(d2);
    }

    @Override // org.apache.commons.math3.linear.RealVector
    public OpenMapRealVector mapAddToSelf(double d2) throws OutOfRangeException {
        for (int i2 = 0; i2 < this.virtualSize; i2++) {
            setEntry(i2, getEntry(i2) + d2);
        }
        return this;
    }

    @Override // org.apache.commons.math3.linear.RealVector
    public void setEntry(int index, double value) throws OutOfRangeException {
        checkIndex(index);
        if (!isDefaultValue(value)) {
            this.entries.put(index, value);
        } else if (this.entries.containsKey(index)) {
            this.entries.remove(index);
        }
    }

    @Override // org.apache.commons.math3.linear.RealVector
    public void setSubVector(int index, RealVector v2) throws OutOfRangeException {
        checkIndex(index);
        checkIndex((index + v2.getDimension()) - 1);
        for (int i2 = 0; i2 < v2.getDimension(); i2++) {
            setEntry(i2 + index, v2.getEntry(i2));
        }
    }

    @Override // org.apache.commons.math3.linear.RealVector
    public void set(double value) throws OutOfRangeException {
        for (int i2 = 0; i2 < this.virtualSize; i2++) {
            setEntry(i2, value);
        }
    }

    public OpenMapRealVector subtract(OpenMapRealVector v2) throws OutOfRangeException, DimensionMismatchException, NoSuchElementException, ConcurrentModificationException {
        checkVectorDimensions(v2.getDimension());
        OpenMapRealVector res = copy();
        OpenIntToDoubleHashMap.Iterator iter = v2.getEntries().iterator();
        while (iter.hasNext()) {
            iter.advance();
            int key = iter.key();
            if (this.entries.containsKey(key)) {
                res.setEntry(key, this.entries.get(key) - iter.value());
            } else {
                res.setEntry(key, -iter.value());
            }
        }
        return res;
    }

    @Override // org.apache.commons.math3.linear.RealVector
    public RealVector subtract(RealVector v2) throws DimensionMismatchException {
        checkVectorDimensions(v2.getDimension());
        if (v2 instanceof OpenMapRealVector) {
            return subtract((OpenMapRealVector) v2);
        }
        return super.subtract(v2);
    }

    @Override // org.apache.commons.math3.linear.RealVector
    public OpenMapRealVector unitVector() throws NoSuchElementException, ConcurrentModificationException, MathArithmeticException {
        OpenMapRealVector res = copy();
        res.unitize();
        return res;
    }

    @Override // org.apache.commons.math3.linear.RealVector
    public void unitize() throws NoSuchElementException, ConcurrentModificationException, MathArithmeticException {
        double norm = getNorm();
        if (isDefaultValue(norm)) {
            throw new MathArithmeticException(LocalizedFormats.ZERO_NORM, new Object[0]);
        }
        OpenIntToDoubleHashMap.Iterator iter = this.entries.iterator();
        while (iter.hasNext()) {
            iter.advance();
            this.entries.put(iter.key(), iter.value() / norm);
        }
    }

    @Override // org.apache.commons.math3.linear.RealVector
    public double[] toArray() throws NoSuchElementException, ConcurrentModificationException {
        double[] res = new double[this.virtualSize];
        OpenIntToDoubleHashMap.Iterator iter = this.entries.iterator();
        while (iter.hasNext()) {
            iter.advance();
            res[iter.key()] = iter.value();
        }
        return res;
    }

    @Override // org.apache.commons.math3.linear.RealVector
    public int hashCode() throws NoSuchElementException, ConcurrentModificationException {
        long temp = Double.doubleToLongBits(this.epsilon);
        int result = (31 * 1) + ((int) (temp ^ (temp >>> 32)));
        int result2 = (31 * result) + this.virtualSize;
        OpenIntToDoubleHashMap.Iterator iter = this.entries.iterator();
        while (iter.hasNext()) {
            iter.advance();
            long temp2 = Double.doubleToLongBits(iter.value());
            result2 = (31 * result2) + ((int) (temp2 ^ (temp2 >> 32)));
        }
        return result2;
    }

    @Override // org.apache.commons.math3.linear.RealVector
    public boolean equals(Object obj) throws OutOfRangeException, NoSuchElementException, ConcurrentModificationException {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof OpenMapRealVector)) {
            return false;
        }
        OpenMapRealVector other = (OpenMapRealVector) obj;
        if (this.virtualSize != other.virtualSize || Double.doubleToLongBits(this.epsilon) != Double.doubleToLongBits(other.epsilon)) {
            return false;
        }
        OpenIntToDoubleHashMap.Iterator iter = this.entries.iterator();
        while (iter.hasNext()) {
            iter.advance();
            double test = other.getEntry(iter.key());
            if (Double.doubleToLongBits(test) != Double.doubleToLongBits(iter.value())) {
                return false;
            }
        }
        OpenIntToDoubleHashMap.Iterator iter2 = other.getEntries().iterator();
        while (iter2.hasNext()) {
            iter2.advance();
            double test2 = iter2.value();
            if (Double.doubleToLongBits(test2) != Double.doubleToLongBits(getEntry(iter2.key()))) {
                return false;
            }
        }
        return true;
    }

    public double getSparsity() {
        return this.entries.size() / getDimension();
    }

    @Override // org.apache.commons.math3.linear.RealVector
    public Iterator<RealVector.Entry> sparseIterator() {
        return new OpenMapSparseIterator();
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/linear/OpenMapRealVector$OpenMapEntry.class */
    protected class OpenMapEntry extends RealVector.Entry {
        private final OpenIntToDoubleHashMap.Iterator iter;

        protected OpenMapEntry(OpenIntToDoubleHashMap.Iterator iter) {
            super();
            this.iter = iter;
        }

        @Override // org.apache.commons.math3.linear.RealVector.Entry
        public double getValue() {
            return this.iter.value();
        }

        @Override // org.apache.commons.math3.linear.RealVector.Entry
        public void setValue(double value) {
            OpenMapRealVector.this.entries.put(this.iter.key(), value);
        }

        @Override // org.apache.commons.math3.linear.RealVector.Entry
        public int getIndex() {
            return this.iter.key();
        }
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/linear/OpenMapRealVector$OpenMapSparseIterator.class */
    protected class OpenMapSparseIterator implements Iterator<RealVector.Entry> {
        private final OpenIntToDoubleHashMap.Iterator iter;
        private final RealVector.Entry current;

        protected OpenMapSparseIterator() {
            this.iter = OpenMapRealVector.this.entries.iterator();
            this.current = OpenMapRealVector.this.new OpenMapEntry(this.iter);
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.iter.hasNext();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Iterator
        public RealVector.Entry next() throws NoSuchElementException, ConcurrentModificationException {
            this.iter.advance();
            return this.current;
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException("Not supported");
        }
    }
}
