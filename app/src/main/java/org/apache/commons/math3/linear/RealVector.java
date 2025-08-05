package org.apache.commons.math3.linear;

import java.util.Iterator;
import java.util.NoSuchElementException;
import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.function.Add;
import org.apache.commons.math3.analysis.function.Divide;
import org.apache.commons.math3.analysis.function.Multiply;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathUnsupportedOperationException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/linear/RealVector.class */
public abstract class RealVector {
    public abstract int getDimension();

    public abstract double getEntry(int i2) throws OutOfRangeException;

    public abstract void setEntry(int i2, double d2) throws OutOfRangeException;

    public abstract RealVector append(RealVector realVector);

    public abstract RealVector append(double d2);

    public abstract RealVector getSubVector(int i2, int i3) throws OutOfRangeException, NotPositiveException;

    public abstract void setSubVector(int i2, RealVector realVector) throws OutOfRangeException;

    public abstract boolean isNaN();

    public abstract boolean isInfinite();

    public abstract RealVector copy();

    public abstract RealVector ebeDivide(RealVector realVector) throws DimensionMismatchException;

    public abstract RealVector ebeMultiply(RealVector realVector) throws DimensionMismatchException;

    public void addToEntry(int index, double increment) throws OutOfRangeException {
        setEntry(index, getEntry(index) + increment);
    }

    protected void checkVectorDimensions(RealVector v2) throws DimensionMismatchException {
        checkVectorDimensions(v2.getDimension());
    }

    protected void checkVectorDimensions(int n2) throws DimensionMismatchException {
        int d2 = getDimension();
        if (d2 != n2) {
            throw new DimensionMismatchException(d2, n2);
        }
    }

    protected void checkIndex(int index) throws OutOfRangeException {
        if (index < 0 || index >= getDimension()) {
            throw new OutOfRangeException(LocalizedFormats.INDEX, Integer.valueOf(index), 0, Integer.valueOf(getDimension() - 1));
        }
    }

    protected void checkIndices(int start, int end) throws NumberIsTooSmallException, OutOfRangeException {
        int dim = getDimension();
        if (start < 0 || start >= dim) {
            throw new OutOfRangeException(LocalizedFormats.INDEX, Integer.valueOf(start), 0, Integer.valueOf(dim - 1));
        }
        if (end < 0 || end >= dim) {
            throw new OutOfRangeException(LocalizedFormats.INDEX, Integer.valueOf(end), 0, Integer.valueOf(dim - 1));
        }
        if (end < start) {
            throw new NumberIsTooSmallException(LocalizedFormats.INITIAL_ROW_AFTER_FINAL_ROW, Integer.valueOf(end), Integer.valueOf(start), false);
        }
    }

    public RealVector add(RealVector v2) throws OutOfRangeException, DimensionMismatchException {
        checkVectorDimensions(v2);
        RealVector result = v2.copy();
        Iterator<Entry> it = iterator();
        while (it.hasNext()) {
            Entry e2 = it.next();
            int index = e2.getIndex();
            result.setEntry(index, e2.getValue() + result.getEntry(index));
        }
        return result;
    }

    public RealVector subtract(RealVector v2) throws OutOfRangeException, DimensionMismatchException {
        checkVectorDimensions(v2);
        RealVector result = v2.mapMultiply(-1.0d);
        Iterator<Entry> it = iterator();
        while (it.hasNext()) {
            Entry e2 = it.next();
            int index = e2.getIndex();
            result.setEntry(index, e2.getValue() + result.getEntry(index));
        }
        return result;
    }

    public RealVector mapAdd(double d2) {
        return copy().mapAddToSelf(d2);
    }

    public RealVector mapAddToSelf(double d2) {
        if (d2 != 0.0d) {
            return mapToSelf(FunctionUtils.fix2ndArgument(new Add(), d2));
        }
        return this;
    }

    public double dotProduct(RealVector v2) throws DimensionMismatchException {
        checkVectorDimensions(v2);
        double d2 = 0.0d;
        int n2 = getDimension();
        for (int i2 = 0; i2 < n2; i2++) {
            d2 += getEntry(i2) * v2.getEntry(i2);
        }
        return d2;
    }

    public double cosine(RealVector v2) throws DimensionMismatchException, MathArithmeticException {
        double norm = getNorm();
        double vNorm = v2.getNorm();
        if (norm == 0.0d || vNorm == 0.0d) {
            throw new MathArithmeticException(LocalizedFormats.ZERO_NORM, new Object[0]);
        }
        return dotProduct(v2) / (norm * vNorm);
    }

    public double getDistance(RealVector v2) throws DimensionMismatchException {
        checkVectorDimensions(v2);
        double d2 = 0.0d;
        Iterator<Entry> it = iterator();
        while (it.hasNext()) {
            Entry e2 = it.next();
            double diff = e2.getValue() - v2.getEntry(e2.getIndex());
            d2 += diff * diff;
        }
        return FastMath.sqrt(d2);
    }

    public double getNorm() {
        double sum = 0.0d;
        Iterator<Entry> it = iterator();
        while (it.hasNext()) {
            Entry e2 = it.next();
            double value = e2.getValue();
            sum += value * value;
        }
        return FastMath.sqrt(sum);
    }

    public double getL1Norm() {
        double norm = 0.0d;
        Iterator<Entry> it = iterator();
        while (it.hasNext()) {
            Entry e2 = it.next();
            norm += FastMath.abs(e2.getValue());
        }
        return norm;
    }

    public double getLInfNorm() {
        double norm = 0.0d;
        Iterator<Entry> it = iterator();
        while (it.hasNext()) {
            Entry e2 = it.next();
            norm = FastMath.max(norm, FastMath.abs(e2.getValue()));
        }
        return norm;
    }

    public double getL1Distance(RealVector v2) throws DimensionMismatchException {
        checkVectorDimensions(v2);
        double d2 = 0.0d;
        Iterator<Entry> it = iterator();
        while (it.hasNext()) {
            Entry e2 = it.next();
            d2 += FastMath.abs(e2.getValue() - v2.getEntry(e2.getIndex()));
        }
        return d2;
    }

    public double getLInfDistance(RealVector v2) throws DimensionMismatchException {
        checkVectorDimensions(v2);
        double d2 = 0.0d;
        Iterator<Entry> it = iterator();
        while (it.hasNext()) {
            Entry e2 = it.next();
            d2 = FastMath.max(FastMath.abs(e2.getValue() - v2.getEntry(e2.getIndex())), d2);
        }
        return d2;
    }

    public int getMinIndex() {
        int minIndex = -1;
        double minValue = Double.POSITIVE_INFINITY;
        Iterator<Entry> iterator = iterator();
        while (iterator.hasNext()) {
            Entry entry = iterator.next();
            if (entry.getValue() <= minValue) {
                minIndex = entry.getIndex();
                minValue = entry.getValue();
            }
        }
        return minIndex;
    }

    public double getMinValue() {
        int minIndex = getMinIndex();
        if (minIndex < 0) {
            return Double.NaN;
        }
        return getEntry(minIndex);
    }

    public int getMaxIndex() {
        int maxIndex = -1;
        double maxValue = Double.NEGATIVE_INFINITY;
        Iterator<Entry> iterator = iterator();
        while (iterator.hasNext()) {
            Entry entry = iterator.next();
            if (entry.getValue() >= maxValue) {
                maxIndex = entry.getIndex();
                maxValue = entry.getValue();
            }
        }
        return maxIndex;
    }

    public double getMaxValue() {
        int maxIndex = getMaxIndex();
        if (maxIndex < 0) {
            return Double.NaN;
        }
        return getEntry(maxIndex);
    }

    public RealVector mapMultiply(double d2) {
        return copy().mapMultiplyToSelf(d2);
    }

    public RealVector mapMultiplyToSelf(double d2) {
        return mapToSelf(FunctionUtils.fix2ndArgument(new Multiply(), d2));
    }

    public RealVector mapSubtract(double d2) {
        return copy().mapSubtractToSelf(d2);
    }

    public RealVector mapSubtractToSelf(double d2) {
        return mapAddToSelf(-d2);
    }

    public RealVector mapDivide(double d2) {
        return copy().mapDivideToSelf(d2);
    }

    public RealVector mapDivideToSelf(double d2) {
        return mapToSelf(FunctionUtils.fix2ndArgument(new Divide(), d2));
    }

    public RealMatrix outerProduct(RealVector v2) throws OutOfRangeException {
        RealMatrix product;
        int m2 = getDimension();
        int n2 = v2.getDimension();
        if ((v2 instanceof SparseRealVector) || (this instanceof SparseRealVector)) {
            product = new OpenMapRealMatrix(m2, n2);
        } else {
            product = new Array2DRowRealMatrix(m2, n2);
        }
        for (int i2 = 0; i2 < m2; i2++) {
            for (int j2 = 0; j2 < n2; j2++) {
                product.setEntry(i2, j2, getEntry(i2) * v2.getEntry(j2));
            }
        }
        return product;
    }

    public RealVector projection(RealVector v2) throws DimensionMismatchException, MathArithmeticException {
        double norm2 = v2.dotProduct(v2);
        if (norm2 == 0.0d) {
            throw new MathArithmeticException(LocalizedFormats.ZERO_NORM, new Object[0]);
        }
        return v2.mapMultiply(dotProduct(v2) / v2.dotProduct(v2));
    }

    public void set(double value) throws OutOfRangeException {
        Iterator<Entry> it = iterator();
        while (it.hasNext()) {
            Entry e2 = it.next();
            e2.setValue(value);
        }
    }

    public double[] toArray() {
        int dim = getDimension();
        double[] values = new double[dim];
        for (int i2 = 0; i2 < dim; i2++) {
            values[i2] = getEntry(i2);
        }
        return values;
    }

    public RealVector unitVector() throws MathArithmeticException {
        double norm = getNorm();
        if (norm == 0.0d) {
            throw new MathArithmeticException(LocalizedFormats.ZERO_NORM, new Object[0]);
        }
        return mapDivide(norm);
    }

    public void unitize() throws MathArithmeticException {
        double norm = getNorm();
        if (norm == 0.0d) {
            throw new MathArithmeticException(LocalizedFormats.ZERO_NORM, new Object[0]);
        }
        mapDivideToSelf(getNorm());
    }

    public Iterator<Entry> sparseIterator() {
        return new SparseEntryIterator();
    }

    public Iterator<Entry> iterator() {
        final int dim = getDimension();
        return new Iterator<Entry>() { // from class: org.apache.commons.math3.linear.RealVector.1

            /* renamed from: i, reason: collision with root package name */
            private int f13027i = 0;

            /* renamed from: e, reason: collision with root package name */
            private Entry f13028e;

            {
                this.f13028e = RealVector.this.new Entry();
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                return this.f13027i < dim;
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.Iterator
            public Entry next() {
                if (this.f13027i < dim) {
                    Entry entry = this.f13028e;
                    int i2 = this.f13027i;
                    this.f13027i = i2 + 1;
                    entry.setIndex(i2);
                    return this.f13028e;
                }
                throw new NoSuchElementException();
            }

            @Override // java.util.Iterator
            public void remove() throws MathUnsupportedOperationException {
                throw new MathUnsupportedOperationException();
            }
        };
    }

    public RealVector map(UnivariateFunction function) {
        return copy().mapToSelf(function);
    }

    public RealVector mapToSelf(UnivariateFunction function) throws OutOfRangeException {
        Iterator<Entry> it = iterator();
        while (it.hasNext()) {
            Entry e2 = it.next();
            e2.setValue(function.value(e2.getValue()));
        }
        return this;
    }

    public RealVector combine(double a2, double b2, RealVector y2) throws DimensionMismatchException {
        return copy().combineToSelf(a2, b2, y2);
    }

    public RealVector combineToSelf(double a2, double b2, RealVector y2) throws OutOfRangeException, DimensionMismatchException {
        checkVectorDimensions(y2);
        for (int i2 = 0; i2 < getDimension(); i2++) {
            double xi = getEntry(i2);
            double yi = y2.getEntry(i2);
            setEntry(i2, (a2 * xi) + (b2 * yi));
        }
        return this;
    }

    public double walkInDefaultOrder(RealVectorPreservingVisitor visitor) {
        int dim = getDimension();
        visitor.start(dim, 0, dim - 1);
        for (int i2 = 0; i2 < dim; i2++) {
            visitor.visit(i2, getEntry(i2));
        }
        return visitor.end();
    }

    public double walkInDefaultOrder(RealVectorPreservingVisitor visitor, int start, int end) throws NumberIsTooSmallException, OutOfRangeException {
        checkIndices(start, end);
        visitor.start(getDimension(), start, end);
        for (int i2 = start; i2 <= end; i2++) {
            visitor.visit(i2, getEntry(i2));
        }
        return visitor.end();
    }

    public double walkInOptimizedOrder(RealVectorPreservingVisitor visitor) {
        return walkInDefaultOrder(visitor);
    }

    public double walkInOptimizedOrder(RealVectorPreservingVisitor visitor, int start, int end) throws NumberIsTooSmallException, OutOfRangeException {
        return walkInDefaultOrder(visitor, start, end);
    }

    public double walkInDefaultOrder(RealVectorChangingVisitor visitor) throws OutOfRangeException {
        int dim = getDimension();
        visitor.start(dim, 0, dim - 1);
        for (int i2 = 0; i2 < dim; i2++) {
            setEntry(i2, visitor.visit(i2, getEntry(i2)));
        }
        return visitor.end();
    }

    public double walkInDefaultOrder(RealVectorChangingVisitor visitor, int start, int end) throws NumberIsTooSmallException, OutOfRangeException {
        checkIndices(start, end);
        visitor.start(getDimension(), start, end);
        for (int i2 = start; i2 <= end; i2++) {
            setEntry(i2, visitor.visit(i2, getEntry(i2)));
        }
        return visitor.end();
    }

    public double walkInOptimizedOrder(RealVectorChangingVisitor visitor) {
        return walkInDefaultOrder(visitor);
    }

    public double walkInOptimizedOrder(RealVectorChangingVisitor visitor, int start, int end) throws NumberIsTooSmallException, OutOfRangeException {
        return walkInDefaultOrder(visitor, start, end);
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/linear/RealVector$Entry.class */
    protected class Entry {
        private int index;

        public Entry() {
            setIndex(0);
        }

        public double getValue() {
            return RealVector.this.getEntry(getIndex());
        }

        public void setValue(double value) throws OutOfRangeException {
            RealVector.this.setEntry(getIndex(), value);
        }

        public int getIndex() {
            return this.index;
        }

        public void setIndex(int index) {
            this.index = index;
        }
    }

    public boolean equals(Object other) throws MathUnsupportedOperationException {
        throw new MathUnsupportedOperationException();
    }

    public int hashCode() throws MathUnsupportedOperationException {
        throw new MathUnsupportedOperationException();
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/linear/RealVector$SparseEntryIterator.class */
    protected class SparseEntryIterator implements Iterator<Entry> {
        private final int dim;
        private Entry current;
        private Entry next;

        protected SparseEntryIterator() {
            this.dim = RealVector.this.getDimension();
            this.current = RealVector.this.new Entry();
            this.next = RealVector.this.new Entry();
            if (this.next.getValue() == 0.0d) {
                advance(this.next);
            }
        }

        protected void advance(Entry e2) {
            if (e2 == null) {
                return;
            }
            do {
                e2.setIndex(e2.getIndex() + 1);
                if (e2.getIndex() >= this.dim) {
                    break;
                }
            } while (e2.getValue() == 0.0d);
            if (e2.getIndex() >= this.dim) {
                e2.setIndex(-1);
            }
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.next.getIndex() >= 0;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Iterator
        public Entry next() {
            int index = this.next.getIndex();
            if (index < 0) {
                throw new NoSuchElementException();
            }
            this.current.setIndex(index);
            advance(this.next);
            return this.current;
        }

        @Override // java.util.Iterator
        public void remove() throws MathUnsupportedOperationException {
            throw new MathUnsupportedOperationException();
        }
    }

    public static RealVector unmodifiableRealVector(RealVector v2) {
        return new RealVector() { // from class: org.apache.commons.math3.linear.RealVector.2
            @Override // org.apache.commons.math3.linear.RealVector
            public RealVector mapToSelf(UnivariateFunction function) throws MathUnsupportedOperationException {
                throw new MathUnsupportedOperationException();
            }

            @Override // org.apache.commons.math3.linear.RealVector
            public RealVector map(UnivariateFunction function) {
                return RealVector.this.map(function);
            }

            @Override // org.apache.commons.math3.linear.RealVector
            public Iterator<Entry> iterator() {
                final Iterator<Entry> i2 = RealVector.this.iterator();
                return new Iterator<Entry>() { // from class: org.apache.commons.math3.linear.RealVector.2.1

                    /* renamed from: e, reason: collision with root package name */
                    private final UnmodifiableEntry f13029e;

                    {
                        this.f13029e = AnonymousClass2.this.new UnmodifiableEntry();
                    }

                    @Override // java.util.Iterator
                    public boolean hasNext() {
                        return i2.hasNext();
                    }

                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.util.Iterator
                    public Entry next() {
                        this.f13029e.setIndex(((Entry) i2.next()).getIndex());
                        return this.f13029e;
                    }

                    @Override // java.util.Iterator
                    public void remove() throws MathUnsupportedOperationException {
                        throw new MathUnsupportedOperationException();
                    }
                };
            }

            @Override // org.apache.commons.math3.linear.RealVector
            public Iterator<Entry> sparseIterator() {
                final Iterator<Entry> i2 = RealVector.this.sparseIterator();
                return new Iterator<Entry>() { // from class: org.apache.commons.math3.linear.RealVector.2.2

                    /* renamed from: e, reason: collision with root package name */
                    private final UnmodifiableEntry f13030e;

                    {
                        this.f13030e = AnonymousClass2.this.new UnmodifiableEntry();
                    }

                    @Override // java.util.Iterator
                    public boolean hasNext() {
                        return i2.hasNext();
                    }

                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.util.Iterator
                    public Entry next() {
                        this.f13030e.setIndex(((Entry) i2.next()).getIndex());
                        return this.f13030e;
                    }

                    @Override // java.util.Iterator
                    public void remove() throws MathUnsupportedOperationException {
                        throw new MathUnsupportedOperationException();
                    }
                };
            }

            @Override // org.apache.commons.math3.linear.RealVector
            public RealVector copy() {
                return RealVector.this.copy();
            }

            @Override // org.apache.commons.math3.linear.RealVector
            public RealVector add(RealVector w2) throws DimensionMismatchException {
                return RealVector.this.add(w2);
            }

            @Override // org.apache.commons.math3.linear.RealVector
            public RealVector subtract(RealVector w2) throws DimensionMismatchException {
                return RealVector.this.subtract(w2);
            }

            @Override // org.apache.commons.math3.linear.RealVector
            public RealVector mapAdd(double d2) {
                return RealVector.this.mapAdd(d2);
            }

            @Override // org.apache.commons.math3.linear.RealVector
            public RealVector mapAddToSelf(double d2) throws MathUnsupportedOperationException {
                throw new MathUnsupportedOperationException();
            }

            @Override // org.apache.commons.math3.linear.RealVector
            public RealVector mapSubtract(double d2) {
                return RealVector.this.mapSubtract(d2);
            }

            @Override // org.apache.commons.math3.linear.RealVector
            public RealVector mapSubtractToSelf(double d2) throws MathUnsupportedOperationException {
                throw new MathUnsupportedOperationException();
            }

            @Override // org.apache.commons.math3.linear.RealVector
            public RealVector mapMultiply(double d2) {
                return RealVector.this.mapMultiply(d2);
            }

            @Override // org.apache.commons.math3.linear.RealVector
            public RealVector mapMultiplyToSelf(double d2) throws MathUnsupportedOperationException {
                throw new MathUnsupportedOperationException();
            }

            @Override // org.apache.commons.math3.linear.RealVector
            public RealVector mapDivide(double d2) {
                return RealVector.this.mapDivide(d2);
            }

            @Override // org.apache.commons.math3.linear.RealVector
            public RealVector mapDivideToSelf(double d2) throws MathUnsupportedOperationException {
                throw new MathUnsupportedOperationException();
            }

            @Override // org.apache.commons.math3.linear.RealVector
            public RealVector ebeMultiply(RealVector w2) throws DimensionMismatchException {
                return RealVector.this.ebeMultiply(w2);
            }

            @Override // org.apache.commons.math3.linear.RealVector
            public RealVector ebeDivide(RealVector w2) throws DimensionMismatchException {
                return RealVector.this.ebeDivide(w2);
            }

            @Override // org.apache.commons.math3.linear.RealVector
            public double dotProduct(RealVector w2) throws DimensionMismatchException {
                return RealVector.this.dotProduct(w2);
            }

            @Override // org.apache.commons.math3.linear.RealVector
            public double cosine(RealVector w2) throws DimensionMismatchException, MathArithmeticException {
                return RealVector.this.cosine(w2);
            }

            @Override // org.apache.commons.math3.linear.RealVector
            public double getNorm() {
                return RealVector.this.getNorm();
            }

            @Override // org.apache.commons.math3.linear.RealVector
            public double getL1Norm() {
                return RealVector.this.getL1Norm();
            }

            @Override // org.apache.commons.math3.linear.RealVector
            public double getLInfNorm() {
                return RealVector.this.getLInfNorm();
            }

            @Override // org.apache.commons.math3.linear.RealVector
            public double getDistance(RealVector w2) throws DimensionMismatchException {
                return RealVector.this.getDistance(w2);
            }

            @Override // org.apache.commons.math3.linear.RealVector
            public double getL1Distance(RealVector w2) throws DimensionMismatchException {
                return RealVector.this.getL1Distance(w2);
            }

            @Override // org.apache.commons.math3.linear.RealVector
            public double getLInfDistance(RealVector w2) throws DimensionMismatchException {
                return RealVector.this.getLInfDistance(w2);
            }

            @Override // org.apache.commons.math3.linear.RealVector
            public RealVector unitVector() throws MathArithmeticException {
                return RealVector.this.unitVector();
            }

            @Override // org.apache.commons.math3.linear.RealVector
            public void unitize() throws MathUnsupportedOperationException {
                throw new MathUnsupportedOperationException();
            }

            @Override // org.apache.commons.math3.linear.RealVector
            public RealMatrix outerProduct(RealVector w2) {
                return RealVector.this.outerProduct(w2);
            }

            @Override // org.apache.commons.math3.linear.RealVector
            public double getEntry(int index) throws OutOfRangeException {
                return RealVector.this.getEntry(index);
            }

            @Override // org.apache.commons.math3.linear.RealVector
            public void setEntry(int index, double value) throws MathUnsupportedOperationException {
                throw new MathUnsupportedOperationException();
            }

            @Override // org.apache.commons.math3.linear.RealVector
            public void addToEntry(int index, double value) throws MathUnsupportedOperationException {
                throw new MathUnsupportedOperationException();
            }

            @Override // org.apache.commons.math3.linear.RealVector
            public int getDimension() {
                return RealVector.this.getDimension();
            }

            @Override // org.apache.commons.math3.linear.RealVector
            public RealVector append(RealVector w2) {
                return RealVector.this.append(w2);
            }

            @Override // org.apache.commons.math3.linear.RealVector
            public RealVector append(double d2) {
                return RealVector.this.append(d2);
            }

            @Override // org.apache.commons.math3.linear.RealVector
            public RealVector getSubVector(int index, int n2) throws OutOfRangeException, NotPositiveException {
                return RealVector.this.getSubVector(index, n2);
            }

            @Override // org.apache.commons.math3.linear.RealVector
            public void setSubVector(int index, RealVector w2) throws MathUnsupportedOperationException {
                throw new MathUnsupportedOperationException();
            }

            @Override // org.apache.commons.math3.linear.RealVector
            public void set(double value) throws MathUnsupportedOperationException {
                throw new MathUnsupportedOperationException();
            }

            @Override // org.apache.commons.math3.linear.RealVector
            public double[] toArray() {
                return RealVector.this.toArray();
            }

            @Override // org.apache.commons.math3.linear.RealVector
            public boolean isNaN() {
                return RealVector.this.isNaN();
            }

            @Override // org.apache.commons.math3.linear.RealVector
            public boolean isInfinite() {
                return RealVector.this.isInfinite();
            }

            @Override // org.apache.commons.math3.linear.RealVector
            public RealVector combine(double a2, double b2, RealVector y2) throws DimensionMismatchException {
                return RealVector.this.combine(a2, b2, y2);
            }

            @Override // org.apache.commons.math3.linear.RealVector
            public RealVector combineToSelf(double a2, double b2, RealVector y2) throws MathUnsupportedOperationException {
                throw new MathUnsupportedOperationException();
            }

            /* renamed from: org.apache.commons.math3.linear.RealVector$2$UnmodifiableEntry */
            /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/linear/RealVector$2$UnmodifiableEntry.class */
            class UnmodifiableEntry extends Entry {
                UnmodifiableEntry() {
                    super();
                }

                @Override // org.apache.commons.math3.linear.RealVector.Entry
                public double getValue() {
                    return RealVector.this.getEntry(getIndex());
                }

                @Override // org.apache.commons.math3.linear.RealVector.Entry
                public void setValue(double value) throws MathUnsupportedOperationException {
                    throw new MathUnsupportedOperationException();
                }
            }
        };
    }
}
