package org.apache.commons.math3.util;

import java.io.Serializable;
import java.util.Arrays;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.exception.MathInternalError;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.MathArrays;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/util/ResizableDoubleArray.class */
public class ResizableDoubleArray implements DoubleArray, Serializable {

    @Deprecated
    public static final int ADDITIVE_MODE = 1;

    @Deprecated
    public static final int MULTIPLICATIVE_MODE = 0;
    private static final long serialVersionUID = -3485529955529426875L;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final double DEFAULT_EXPANSION_FACTOR = 2.0d;
    private static final double DEFAULT_CONTRACTION_DELTA = 0.5d;
    private double contractionCriterion;
    private double expansionFactor;
    private ExpansionMode expansionMode;
    private double[] internalArray;
    private int numElements;
    private int startIndex;

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/util/ResizableDoubleArray$ExpansionMode.class */
    public enum ExpansionMode {
        MULTIPLICATIVE,
        ADDITIVE
    }

    public ResizableDoubleArray() {
        this(16);
    }

    public ResizableDoubleArray(int initialCapacity) throws MathIllegalArgumentException {
        this(initialCapacity, 2.0d);
    }

    public ResizableDoubleArray(double[] initialArray) {
        this(16, 2.0d, 2.5d, ExpansionMode.MULTIPLICATIVE, initialArray);
    }

    @Deprecated
    public ResizableDoubleArray(int initialCapacity, float expansionFactor) throws MathIllegalArgumentException {
        this(initialCapacity, expansionFactor);
    }

    public ResizableDoubleArray(int initialCapacity, double expansionFactor) throws MathIllegalArgumentException {
        this(initialCapacity, expansionFactor, 0.5d + expansionFactor);
    }

    @Deprecated
    public ResizableDoubleArray(int initialCapacity, float expansionFactor, float contractionCriteria) throws MathIllegalArgumentException {
        this(initialCapacity, expansionFactor, contractionCriteria);
    }

    public ResizableDoubleArray(int initialCapacity, double expansionFactor, double contractionCriterion) throws MathIllegalArgumentException {
        this(initialCapacity, expansionFactor, contractionCriterion, ExpansionMode.MULTIPLICATIVE, null);
    }

    @Deprecated
    public ResizableDoubleArray(int initialCapacity, float expansionFactor, float contractionCriteria, int expansionMode) throws MathIllegalArgumentException {
        this(initialCapacity, expansionFactor, contractionCriteria, expansionMode == 1 ? ExpansionMode.ADDITIVE : ExpansionMode.MULTIPLICATIVE, null);
        setExpansionMode(expansionMode);
    }

    public ResizableDoubleArray(int initialCapacity, double expansionFactor, double contractionCriterion, ExpansionMode expansionMode, double... data) throws MathIllegalArgumentException {
        this.contractionCriterion = 2.5d;
        this.expansionFactor = 2.0d;
        this.expansionMode = ExpansionMode.MULTIPLICATIVE;
        this.numElements = 0;
        this.startIndex = 0;
        if (initialCapacity <= 0) {
            throw new NotStrictlyPositiveException(LocalizedFormats.INITIAL_CAPACITY_NOT_POSITIVE, Integer.valueOf(initialCapacity));
        }
        checkContractExpand(contractionCriterion, expansionFactor);
        this.expansionFactor = expansionFactor;
        this.contractionCriterion = contractionCriterion;
        this.expansionMode = expansionMode;
        this.internalArray = new double[initialCapacity];
        this.numElements = 0;
        this.startIndex = 0;
        if (data != null && data.length > 0) {
            addElements(data);
        }
    }

    public ResizableDoubleArray(ResizableDoubleArray original) throws NullArgumentException {
        this.contractionCriterion = 2.5d;
        this.expansionFactor = 2.0d;
        this.expansionMode = ExpansionMode.MULTIPLICATIVE;
        this.numElements = 0;
        this.startIndex = 0;
        MathUtils.checkNotNull(original);
        copy(original, this);
    }

    @Override // org.apache.commons.math3.util.DoubleArray
    public synchronized void addElement(double value) {
        if (this.internalArray.length <= this.startIndex + this.numElements) {
            expand();
        }
        double[] dArr = this.internalArray;
        int i2 = this.startIndex;
        int i3 = this.numElements;
        this.numElements = i3 + 1;
        dArr[i2 + i3] = value;
    }

    @Override // org.apache.commons.math3.util.DoubleArray
    public synchronized void addElements(double[] values) {
        double[] tempArray = new double[this.numElements + values.length + 1];
        System.arraycopy(this.internalArray, this.startIndex, tempArray, 0, this.numElements);
        System.arraycopy(values, 0, tempArray, this.numElements, values.length);
        this.internalArray = tempArray;
        this.startIndex = 0;
        this.numElements += values.length;
    }

    @Override // org.apache.commons.math3.util.DoubleArray
    public synchronized double addElementRolling(double value) {
        double discarded = this.internalArray[this.startIndex];
        if (this.startIndex + this.numElements + 1 > this.internalArray.length) {
            expand();
        }
        this.startIndex++;
        this.internalArray[this.startIndex + (this.numElements - 1)] = value;
        if (shouldContract()) {
            contract();
        }
        return discarded;
    }

    public synchronized double substituteMostRecentElement(double value) throws MathIllegalStateException {
        if (this.numElements < 1) {
            throw new MathIllegalStateException(LocalizedFormats.CANNOT_SUBSTITUTE_ELEMENT_FROM_EMPTY_ARRAY, new Object[0]);
        }
        int substIndex = this.startIndex + (this.numElements - 1);
        double discarded = this.internalArray[substIndex];
        this.internalArray[substIndex] = value;
        return discarded;
    }

    @Deprecated
    protected void checkContractExpand(float contraction, float expansion) throws MathIllegalArgumentException {
        checkContractExpand(contraction, expansion);
    }

    protected void checkContractExpand(double contraction, double expansion) throws NumberIsTooSmallException {
        if (contraction < expansion) {
            NumberIsTooSmallException e2 = new NumberIsTooSmallException(Double.valueOf(contraction), 1, true);
            e2.getContext().addMessage(LocalizedFormats.CONTRACTION_CRITERIA_SMALLER_THAN_EXPANSION_FACTOR, Double.valueOf(contraction), Double.valueOf(expansion));
            throw e2;
        }
        if (contraction <= 1.0d) {
            NumberIsTooSmallException e3 = new NumberIsTooSmallException(Double.valueOf(contraction), 1, false);
            e3.getContext().addMessage(LocalizedFormats.CONTRACTION_CRITERIA_SMALLER_THAN_ONE, Double.valueOf(contraction));
            throw e3;
        }
        if (expansion <= 1.0d) {
            NumberIsTooSmallException e4 = new NumberIsTooSmallException(Double.valueOf(contraction), 1, false);
            e4.getContext().addMessage(LocalizedFormats.EXPANSION_FACTOR_SMALLER_THAN_ONE, Double.valueOf(expansion));
            throw e4;
        }
    }

    @Override // org.apache.commons.math3.util.DoubleArray
    public synchronized void clear() {
        this.numElements = 0;
        this.startIndex = 0;
    }

    public synchronized void contract() {
        double[] tempArray = new double[this.numElements + 1];
        System.arraycopy(this.internalArray, this.startIndex, tempArray, 0, this.numElements);
        this.internalArray = tempArray;
        this.startIndex = 0;
    }

    public synchronized void discardFrontElements(int i2) throws MathIllegalArgumentException {
        discardExtremeElements(i2, true);
    }

    public synchronized void discardMostRecentElements(int i2) throws MathIllegalArgumentException {
        discardExtremeElements(i2, false);
    }

    private synchronized void discardExtremeElements(int i2, boolean front) throws MathIllegalArgumentException {
        if (i2 > this.numElements) {
            throw new MathIllegalArgumentException(LocalizedFormats.TOO_MANY_ELEMENTS_TO_DISCARD_FROM_ARRAY, Integer.valueOf(i2), Integer.valueOf(this.numElements));
        }
        if (i2 < 0) {
            throw new MathIllegalArgumentException(LocalizedFormats.CANNOT_DISCARD_NEGATIVE_NUMBER_OF_ELEMENTS, Integer.valueOf(i2));
        }
        this.numElements -= i2;
        if (front) {
            this.startIndex += i2;
        }
        if (shouldContract()) {
            contract();
        }
    }

    protected synchronized void expand() {
        int newSize;
        if (this.expansionMode == ExpansionMode.MULTIPLICATIVE) {
            newSize = (int) FastMath.ceil(this.internalArray.length * this.expansionFactor);
        } else {
            newSize = (int) (this.internalArray.length + FastMath.round(this.expansionFactor));
        }
        double[] tempArray = new double[newSize];
        System.arraycopy(this.internalArray, 0, tempArray, 0, this.internalArray.length);
        this.internalArray = tempArray;
    }

    private synchronized void expandTo(int size) {
        double[] tempArray = new double[size];
        System.arraycopy(this.internalArray, 0, tempArray, 0, this.internalArray.length);
        this.internalArray = tempArray;
    }

    @Deprecated
    public float getContractionCriteria() {
        return (float) getContractionCriterion();
    }

    public double getContractionCriterion() {
        return this.contractionCriterion;
    }

    @Override // org.apache.commons.math3.util.DoubleArray
    public synchronized double getElement(int index) {
        if (index >= this.numElements) {
            throw new ArrayIndexOutOfBoundsException(index);
        }
        if (index >= 0) {
            return this.internalArray[this.startIndex + index];
        }
        throw new ArrayIndexOutOfBoundsException(index);
    }

    @Override // org.apache.commons.math3.util.DoubleArray
    public synchronized double[] getElements() {
        double[] elementArray = new double[this.numElements];
        System.arraycopy(this.internalArray, this.startIndex, elementArray, 0, this.numElements);
        return elementArray;
    }

    @Deprecated
    public float getExpansionFactor() {
        return (float) this.expansionFactor;
    }

    @Deprecated
    public int getExpansionMode() {
        synchronized (this) {
            switch (this.expansionMode) {
                case MULTIPLICATIVE:
                    return 0;
                case ADDITIVE:
                    return 1;
                default:
                    throw new MathInternalError();
            }
        }
    }

    @Deprecated
    synchronized int getInternalLength() {
        return this.internalArray.length;
    }

    public int getCapacity() {
        return this.internalArray.length;
    }

    @Override // org.apache.commons.math3.util.DoubleArray
    public synchronized int getNumElements() {
        return this.numElements;
    }

    @Deprecated
    public synchronized double[] getInternalValues() {
        return this.internalArray;
    }

    protected double[] getArrayRef() {
        return this.internalArray;
    }

    protected int getStartIndex() {
        return this.startIndex;
    }

    @Deprecated
    public void setContractionCriteria(float contractionCriteria) throws MathIllegalArgumentException {
        checkContractExpand(contractionCriteria, getExpansionFactor());
        synchronized (this) {
            this.contractionCriterion = contractionCriteria;
        }
    }

    public double compute(MathArrays.Function f2) {
        double[] array;
        int start;
        int num;
        synchronized (this) {
            array = this.internalArray;
            start = this.startIndex;
            num = this.numElements;
        }
        return f2.evaluate(array, start, num);
    }

    @Override // org.apache.commons.math3.util.DoubleArray
    public synchronized void setElement(int index, double value) {
        if (index < 0) {
            throw new ArrayIndexOutOfBoundsException(index);
        }
        if (index + 1 > this.numElements) {
            this.numElements = index + 1;
        }
        if (this.startIndex + index >= this.internalArray.length) {
            expandTo(this.startIndex + index + 1);
        }
        this.internalArray[this.startIndex + index] = value;
    }

    @Deprecated
    public void setExpansionFactor(float expansionFactor) throws MathIllegalArgumentException {
        checkContractExpand(getContractionCriterion(), expansionFactor);
        synchronized (this) {
            this.expansionFactor = expansionFactor;
        }
    }

    @Deprecated
    public void setExpansionMode(int expansionMode) throws MathIllegalArgumentException {
        if (expansionMode != 0 && expansionMode != 1) {
            throw new MathIllegalArgumentException(LocalizedFormats.UNSUPPORTED_EXPANSION_MODE, Integer.valueOf(expansionMode), 0, "MULTIPLICATIVE_MODE", 1, "ADDITIVE_MODE");
        }
        synchronized (this) {
            if (expansionMode == 0) {
                setExpansionMode(ExpansionMode.MULTIPLICATIVE);
            } else if (expansionMode == 1) {
                setExpansionMode(ExpansionMode.ADDITIVE);
            }
        }
    }

    @Deprecated
    public void setExpansionMode(ExpansionMode expansionMode) {
        synchronized (this) {
            this.expansionMode = expansionMode;
        }
    }

    @Deprecated
    protected void setInitialCapacity(int initialCapacity) throws MathIllegalArgumentException {
    }

    public synchronized void setNumElements(int i2) throws MathIllegalArgumentException {
        if (i2 < 0) {
            throw new MathIllegalArgumentException(LocalizedFormats.INDEX_NOT_POSITIVE, Integer.valueOf(i2));
        }
        int newSize = this.startIndex + i2;
        if (newSize > this.internalArray.length) {
            expandTo(newSize);
        }
        this.numElements = i2;
    }

    private synchronized boolean shouldContract() {
        return this.expansionMode == ExpansionMode.MULTIPLICATIVE ? ((double) (((float) this.internalArray.length) / ((float) this.numElements))) > this.contractionCriterion : ((double) (this.internalArray.length - this.numElements)) > this.contractionCriterion;
    }

    @Deprecated
    public synchronized int start() {
        return this.startIndex;
    }

    public static void copy(ResizableDoubleArray source, ResizableDoubleArray dest) throws NullArgumentException {
        MathUtils.checkNotNull(source);
        MathUtils.checkNotNull(dest);
        synchronized (source) {
            synchronized (dest) {
                dest.contractionCriterion = source.contractionCriterion;
                dest.expansionFactor = source.expansionFactor;
                dest.expansionMode = source.expansionMode;
                dest.internalArray = new double[source.internalArray.length];
                System.arraycopy(source.internalArray, 0, dest.internalArray, 0, dest.internalArray.length);
                dest.numElements = source.numElements;
                dest.startIndex = source.startIndex;
            }
        }
    }

    public synchronized ResizableDoubleArray copy() throws NullArgumentException {
        ResizableDoubleArray result = new ResizableDoubleArray();
        copy(this, result);
        return result;
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof ResizableDoubleArray)) {
            return false;
        }
        synchronized (this) {
            synchronized (object) {
                ResizableDoubleArray other = (ResizableDoubleArray) object;
                boolean result = 1 != 0 && other.contractionCriterion == this.contractionCriterion;
                boolean result2 = result && other.expansionFactor == this.expansionFactor;
                boolean result3 = result2 && other.expansionMode == this.expansionMode;
                boolean result4 = result3 && other.numElements == this.numElements;
                boolean result5 = result4 && other.startIndex == this.startIndex;
                if (!result5) {
                    return false;
                }
                return Arrays.equals(this.internalArray, other.internalArray);
            }
        }
    }

    public synchronized int hashCode() {
        int[] hashData = {Double.valueOf(this.expansionFactor).hashCode(), Double.valueOf(this.contractionCriterion).hashCode(), this.expansionMode.hashCode(), Arrays.hashCode(this.internalArray), this.numElements, this.startIndex};
        return Arrays.hashCode(hashData);
    }
}
