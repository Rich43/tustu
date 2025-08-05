package org.apache.commons.math3.analysis.interpolation;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.FieldElement;
import org.apache.commons.math3.analysis.differentiation.DerivativeStructure;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.ZeroException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.MathUtils;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/interpolation/FieldHermiteInterpolator.class */
public class FieldHermiteInterpolator<T extends FieldElement<T>> {
    private final List<T> abscissae = new ArrayList();
    private final List<T[]> topDiagonal = new ArrayList();
    private final List<T[]> bottomDiagonal = new ArrayList();

    public void addSamplePoint(T t2, T[]... tArr) throws NullArgumentException, ZeroException, DimensionMismatchException, MathArithmeticException {
        MathUtils.checkNotNull(t2);
        FieldElement fieldElement = (FieldElement) t2.getField2().getOne();
        for (int i2 = 0; i2 < tArr.length; i2++) {
            FieldElement[] fieldElementArr = (FieldElement[]) tArr[i2].clone();
            if (i2 > 1) {
                fieldElement = (FieldElement) fieldElement.multiply(i2);
                FieldElement fieldElement2 = (FieldElement) fieldElement.reciprocal();
                for (int i3 = 0; i3 < fieldElementArr.length; i3++) {
                    fieldElementArr[i3] = (FieldElement) fieldElementArr[i3].multiply(fieldElement2);
                }
            }
            int size = this.abscissae.size();
            this.bottomDiagonal.add(size - i2, fieldElementArr);
            FieldElement[] fieldElementArr2 = fieldElementArr;
            for (int i4 = i2; i4 < size; i4++) {
                T[] tArr2 = this.bottomDiagonal.get(size - (i4 + 1));
                if (t2.equals(this.abscissae.get(size - (i4 + 1)))) {
                    throw new ZeroException(LocalizedFormats.DUPLICATED_ABSCISSA_DIVISION_BY_ZERO, t2);
                }
                FieldElement fieldElement3 = (FieldElement) ((FieldElement) t2.subtract(this.abscissae.get(size - (i4 + 1)))).reciprocal();
                for (int i5 = 0; i5 < fieldElementArr.length; i5++) {
                    tArr2[i5] = (FieldElement) fieldElement3.multiply((FieldElement) fieldElementArr2[i5].subtract(tArr2[i5]));
                }
                fieldElementArr2 = tArr2;
            }
            this.topDiagonal.add(fieldElementArr2.clone());
            this.abscissae.add(t2);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public T[] value(T t2) throws NullArgumentException, NoDataException {
        MathUtils.checkNotNull(t2);
        if (this.abscissae.isEmpty()) {
            throw new NoDataException(LocalizedFormats.EMPTY_INTERPOLATION_SAMPLE);
        }
        T[] tArr = (T[]) ((FieldElement[]) MathArrays.buildArray(t2.getField2(), this.topDiagonal.get(0).length));
        FieldElement fieldElement = (FieldElement) t2.getField2().getOne();
        for (int i2 = 0; i2 < this.topDiagonal.size(); i2++) {
            T[] tArr2 = this.topDiagonal.get(i2);
            for (int i3 = 0; i3 < tArr.length; i3++) {
                tArr[i3] = (FieldElement) tArr[i3].add((DerivativeStructure) tArr2[i3].multiply(fieldElement));
            }
            fieldElement = (FieldElement) fieldElement.multiply((FieldElement) t2.subtract(this.abscissae.get(i2)));
        }
        return tArr;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public T[][] derivatives(T t2, int i2) throws NullArgumentException, NoDataException {
        MathUtils.checkNotNull(t2);
        if (this.abscissae.isEmpty()) {
            throw new NoDataException(LocalizedFormats.EMPTY_INTERPOLATION_SAMPLE);
        }
        FieldElement fieldElement = (FieldElement) t2.getField2().getZero();
        FieldElement fieldElement2 = (FieldElement) t2.getField2().getOne();
        FieldElement[] fieldElementArr = (FieldElement[]) MathArrays.buildArray(t2.getField2(), i2 + 1);
        fieldElementArr[0] = fieldElement;
        for (int i3 = 0; i3 < i2; i3++) {
            fieldElementArr[i3 + 1] = (FieldElement) fieldElementArr[i3].add(fieldElement2);
        }
        T[][] tArr = (T[][]) ((FieldElement[][]) MathArrays.buildArray(t2.getField2(), i2 + 1, this.topDiagonal.get(0).length));
        FieldElement[] fieldElementArr2 = (FieldElement[]) MathArrays.buildArray(t2.getField2(), i2 + 1);
        fieldElementArr2[0] = (FieldElement) t2.getField2().getOne();
        for (int i4 = 0; i4 < this.topDiagonal.size(); i4++) {
            T[] tArr2 = this.topDiagonal.get(i4);
            FieldElement fieldElement3 = (FieldElement) t2.subtract(this.abscissae.get(i4));
            for (int i5 = i2; i5 >= 0; i5--) {
                for (int i6 = 0; i6 < tArr[i5].length; i6++) {
                    tArr[i5][i6] = (FieldElement) tArr[i5][i6].add((DerivativeStructure) tArr2[i6].multiply(fieldElementArr2[i5]));
                }
                fieldElementArr2[i5] = (FieldElement) fieldElementArr2[i5].multiply(fieldElement3);
                if (i5 > 0) {
                    fieldElementArr2[i5] = (FieldElement) fieldElementArr2[i5].add(fieldElementArr[i5].multiply(fieldElementArr2[i5 - 1]));
                }
            }
        }
        return tArr;
    }
}
