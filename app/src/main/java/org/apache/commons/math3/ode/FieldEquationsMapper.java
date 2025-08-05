package org.apache.commons.math3.ode;

import java.io.Serializable;
import org.apache.commons.math3.RealFieldElement;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.MathArrays;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ode/FieldEquationsMapper.class */
public class FieldEquationsMapper<T extends RealFieldElement<T>> implements Serializable {
    private static final long serialVersionUID = 20151114;
    private final int[] start;

    FieldEquationsMapper(FieldEquationsMapper<T> mapper, int dimension) {
        int index = mapper == null ? 0 : mapper.getNumberOfEquations();
        this.start = new int[index + 2];
        if (mapper == null) {
            this.start[0] = 0;
        } else {
            System.arraycopy(mapper.start, 0, this.start, 0, index + 1);
        }
        this.start[index + 1] = this.start[index] + dimension;
    }

    public int getNumberOfEquations() {
        return this.start.length - 1;
    }

    public int getTotalDimension() {
        return this.start[this.start.length - 1];
    }

    public T[] mapState(FieldODEState<T> fieldODEState) throws MathIllegalArgumentException {
        T[] tArr = (T[]) ((RealFieldElement[]) MathArrays.buildArray(fieldODEState.getTime().getField(), getTotalDimension()));
        int i2 = 0;
        insertEquationData(0, fieldODEState.getState(), tArr);
        while (true) {
            i2++;
            if (i2 < getNumberOfEquations()) {
                insertEquationData(i2, fieldODEState.getSecondaryState(i2), tArr);
            } else {
                return tArr;
            }
        }
    }

    public T[] mapDerivative(FieldODEStateAndDerivative<T> fieldODEStateAndDerivative) throws MathIllegalArgumentException {
        T[] tArr = (T[]) ((RealFieldElement[]) MathArrays.buildArray(fieldODEStateAndDerivative.getTime().getField(), getTotalDimension()));
        int i2 = 0;
        insertEquationData(0, fieldODEStateAndDerivative.getDerivative(), tArr);
        while (true) {
            i2++;
            if (i2 < getNumberOfEquations()) {
                insertEquationData(i2, fieldODEStateAndDerivative.getSecondaryDerivative(i2), tArr);
            } else {
                return tArr;
            }
        }
    }

    public FieldODEStateAndDerivative<T> mapStateAndDerivative(T t2, T[] y2, T[] yDot) throws MathIllegalArgumentException {
        if (y2.length != getTotalDimension()) {
            throw new DimensionMismatchException(y2.length, getTotalDimension());
        }
        if (yDot.length != getTotalDimension()) {
            throw new DimensionMismatchException(yDot.length, getTotalDimension());
        }
        int n2 = getNumberOfEquations();
        int index = 0;
        RealFieldElement[] realFieldElementArrExtractEquationData = extractEquationData(0, y2);
        RealFieldElement[] realFieldElementArrExtractEquationData2 = extractEquationData(0, yDot);
        if (n2 < 2) {
            return new FieldODEStateAndDerivative<>(t2, realFieldElementArrExtractEquationData, realFieldElementArrExtractEquationData2);
        }
        RealFieldElement[][] realFieldElementArr = (RealFieldElement[][]) MathArrays.buildArray(t2.getField(), n2 - 1, -1);
        RealFieldElement[][] realFieldElementArr2 = (RealFieldElement[][]) MathArrays.buildArray(t2.getField(), n2 - 1, -1);
        while (true) {
            index++;
            if (index < getNumberOfEquations()) {
                realFieldElementArr[index - 1] = extractEquationData(index, y2);
                realFieldElementArr2[index - 1] = extractEquationData(index, yDot);
            } else {
                return new FieldODEStateAndDerivative<>(t2, realFieldElementArrExtractEquationData, realFieldElementArrExtractEquationData2, realFieldElementArr, realFieldElementArr2);
            }
        }
    }

    public T[] extractEquationData(int i2, T[] tArr) throws MathIllegalArgumentException {
        checkIndex(i2);
        int i3 = this.start[i2];
        int i4 = this.start[i2 + 1];
        if (tArr.length < i4) {
            throw new DimensionMismatchException(tArr.length, i4);
        }
        int i5 = i4 - i3;
        T[] tArr2 = (T[]) ((RealFieldElement[]) MathArrays.buildArray(tArr[0].getField(), i5));
        System.arraycopy(tArr, i3, tArr2, 0, i5);
        return tArr2;
    }

    public void insertEquationData(int index, T[] equationData, T[] complete) throws MathIllegalArgumentException {
        checkIndex(index);
        int begin = this.start[index];
        int end = this.start[index + 1];
        int dimension = end - begin;
        if (complete.length < end) {
            throw new DimensionMismatchException(complete.length, end);
        }
        if (equationData.length != dimension) {
            throw new DimensionMismatchException(equationData.length, dimension);
        }
        System.arraycopy(equationData, 0, complete, begin, dimension);
    }

    private void checkIndex(int index) throws MathIllegalArgumentException {
        if (index < 0 || index > this.start.length - 2) {
            throw new MathIllegalArgumentException(LocalizedFormats.ARGUMENT_OUTSIDE_DOMAIN, Integer.valueOf(index), 0, Integer.valueOf(this.start.length - 2));
        }
    }
}
