package org.apache.commons.math3.ode.nonstiff;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.math3.Field;
import org.apache.commons.math3.FieldElement;
import org.apache.commons.math3.RealFieldElement;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.linear.Array2DRowFieldMatrix;
import org.apache.commons.math3.linear.ArrayFieldVector;
import org.apache.commons.math3.linear.FieldDecompositionSolver;
import org.apache.commons.math3.linear.FieldLUDecomposition;
import org.apache.commons.math3.linear.FieldMatrix;
import org.apache.commons.math3.util.MathArrays;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ode/nonstiff/AdamsNordsieckFieldTransformer.class */
public class AdamsNordsieckFieldTransformer<T extends RealFieldElement<T>> {
    private static final Map<Integer, Map<Field<? extends RealFieldElement<?>>, AdamsNordsieckFieldTransformer<? extends RealFieldElement<?>>>> CACHE = new HashMap();
    private final Field<T> field;
    private final Array2DRowFieldMatrix<T> update;
    private final T[] c1;

    private AdamsNordsieckFieldTransformer(Field<T> field, int i2) {
        this.field = field;
        int i3 = i2 - 1;
        FieldMatrix<T> fieldMatrixBuildP = buildP(i3);
        FieldDecompositionSolver solver = new FieldLUDecomposition(fieldMatrixBuildP).getSolver();
        RealFieldElement[] realFieldElementArr = (RealFieldElement[]) MathArrays.buildArray(field, i3);
        Arrays.fill(realFieldElementArr, field.getOne());
        this.c1 = (T[]) ((RealFieldElement[]) solver.solve(new ArrayFieldVector((FieldElement[]) realFieldElementArr, false)).toArray());
        RealFieldElement[][] realFieldElementArr2 = (RealFieldElement[][]) fieldMatrixBuildP.getData();
        for (int length = realFieldElementArr2.length - 1; length > 0; length--) {
            realFieldElementArr2[length] = realFieldElementArr2[length - 1];
        }
        realFieldElementArr2[0] = (RealFieldElement[]) MathArrays.buildArray(field, i3);
        Arrays.fill(realFieldElementArr2[0], field.getZero());
        this.update = new Array2DRowFieldMatrix<>(solver.solve(new Array2DRowFieldMatrix((FieldElement[][]) realFieldElementArr2, false)).getData());
    }

    public static <T extends RealFieldElement<T>> AdamsNordsieckFieldTransformer<T> getInstance(Field<T> field, int nSteps) {
        AdamsNordsieckFieldTransformer adamsNordsieckFieldTransformer;
        synchronized (CACHE) {
            Map<Field<? extends RealFieldElement<?>>, AdamsNordsieckFieldTransformer<? extends RealFieldElement<?>>> map = CACHE.get(Integer.valueOf(nSteps));
            if (map == null) {
                map = new HashMap();
                CACHE.put(Integer.valueOf(nSteps), map);
            }
            AdamsNordsieckFieldTransformer t2 = map.get(field);
            if (t2 == null) {
                t2 = new AdamsNordsieckFieldTransformer(field, nSteps);
                map.put(field, t2);
            }
            adamsNordsieckFieldTransformer = t2;
        }
        return adamsNordsieckFieldTransformer;
    }

    private FieldMatrix<T> buildP(int rows) {
        RealFieldElement[][] realFieldElementArr = (RealFieldElement[][]) MathArrays.buildArray(this.field, rows, rows);
        for (int i2 = 1; i2 <= realFieldElementArr.length; i2++) {
            RealFieldElement[] realFieldElementArr2 = realFieldElementArr[i2 - 1];
            int factor = -i2;
            RealFieldElement realFieldElement = (RealFieldElement) this.field.getZero().add(factor);
            for (int j2 = 1; j2 <= realFieldElementArr2.length; j2++) {
                realFieldElementArr2[j2 - 1] = (RealFieldElement) realFieldElement.multiply(j2 + 1);
                realFieldElement = (RealFieldElement) realFieldElement.multiply(factor);
            }
        }
        return new Array2DRowFieldMatrix((FieldElement[][]) realFieldElementArr, false);
    }

    public Array2DRowFieldMatrix<T> initializeHighOrderDerivatives(T h2, T[] t2, T[][] y2, T[][] yDot) throws OutOfRangeException {
        RealFieldElement[][] realFieldElementArr = (RealFieldElement[][]) MathArrays.buildArray(this.field, this.c1.length + 1, this.c1.length + 1);
        RealFieldElement[][] realFieldElementArr2 = (RealFieldElement[][]) MathArrays.buildArray(this.field, this.c1.length + 1, y2[0].length);
        T[] y0 = y2[0];
        T[] yDot0 = yDot[0];
        for (int i2 = 1; i2 < y2.length; i2++) {
            RealFieldElement realFieldElement = (RealFieldElement) t2[i2].subtract(t2[0]);
            RealFieldElement realFieldElement2 = (RealFieldElement) realFieldElement.divide(h2);
            RealFieldElement realFieldElement3 = (RealFieldElement) h2.reciprocal();
            RealFieldElement[] realFieldElementArr3 = realFieldElementArr[(2 * i2) - 2];
            RealFieldElement[] realFieldElementArr4 = (2 * i2) - 1 < realFieldElementArr.length ? realFieldElementArr[(2 * i2) - 1] : null;
            for (int j2 = 0; j2 < realFieldElementArr3.length; j2++) {
                realFieldElement3 = (RealFieldElement) realFieldElement3.multiply(realFieldElement2);
                realFieldElementArr3[j2] = (RealFieldElement) realFieldElement.multiply(realFieldElement3);
                if (realFieldElementArr4 != null) {
                    realFieldElementArr4[j2] = (RealFieldElement) realFieldElement3.multiply(j2 + 2);
                }
            }
            T[] yI = y2[i2];
            T[] yDotI = yDot[i2];
            RealFieldElement[] realFieldElementArr5 = realFieldElementArr2[(2 * i2) - 2];
            RealFieldElement[] realFieldElementArr6 = (2 * i2) - 1 < realFieldElementArr2.length ? realFieldElementArr2[(2 * i2) - 1] : null;
            for (int j3 = 0; j3 < yI.length; j3++) {
                realFieldElementArr5[j3] = (RealFieldElement) ((RealFieldElement) yI[j3].subtract(y0[j3])).subtract((RealFieldElement) realFieldElement.multiply(yDot0[j3]));
                if (realFieldElementArr6 != null) {
                    realFieldElementArr6[j3] = (RealFieldElement) yDotI[j3].subtract(yDot0[j3]);
                }
            }
        }
        FieldLUDecomposition<T> decomposition = new FieldLUDecomposition<>(new Array2DRowFieldMatrix((FieldElement[][]) realFieldElementArr, false));
        FieldMatrix<T> x2 = decomposition.getSolver().solve(new Array2DRowFieldMatrix((FieldElement[][]) realFieldElementArr2, false));
        Array2DRowFieldMatrix<T> truncatedX = new Array2DRowFieldMatrix<>(this.field, x2.getRowDimension() - 1, x2.getColumnDimension());
        for (int i3 = 0; i3 < truncatedX.getRowDimension(); i3++) {
            for (int j4 = 0; j4 < truncatedX.getColumnDimension(); j4++) {
                truncatedX.setEntry(i3, j4, x2.getEntry(i3, j4));
            }
        }
        return truncatedX;
    }

    public Array2DRowFieldMatrix<T> updateHighOrderDerivativesPhase1(Array2DRowFieldMatrix<T> array2DRowFieldMatrix) {
        return (Array2DRowFieldMatrix<T>) this.update.multiply((Array2DRowFieldMatrix) array2DRowFieldMatrix);
    }

    public void updateHighOrderDerivativesPhase2(T[] start, T[] end, Array2DRowFieldMatrix<T> highOrder) {
        RealFieldElement[][] realFieldElementArr = (RealFieldElement[][]) highOrder.getDataRef();
        for (int i2 = 0; i2 < realFieldElementArr.length; i2++) {
            RealFieldElement[] realFieldElementArr2 = realFieldElementArr[i2];
            T c1I = this.c1[i2];
            for (int j2 = 0; j2 < realFieldElementArr2.length; j2++) {
                realFieldElementArr2[j2] = (RealFieldElement) realFieldElementArr2[j2].add((RealFieldElement) c1I.multiply(start[j2].subtract(end[j2])));
            }
        }
    }
}
