package org.apache.commons.math3.ode.nonstiff;

import org.apache.commons.math3.Field;
import org.apache.commons.math3.RealFieldElement;
import org.apache.commons.math3.ode.FieldEquationsMapper;
import org.apache.commons.math3.ode.FieldODEStateAndDerivative;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.MathUtils;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ode/nonstiff/HighamHall54FieldIntegrator.class */
public class HighamHall54FieldIntegrator<T extends RealFieldElement<T>> extends EmbeddedRungeKuttaFieldIntegrator<T> {
    private static final String METHOD_NAME = "Higham-Hall 5(4)";

    /* renamed from: e, reason: collision with root package name */
    private final T[] f13058e;

    public HighamHall54FieldIntegrator(Field<T> field, double d2, double d3, double d4, double d5) {
        super(field, METHOD_NAME, -1, d2, d3, d4, d5);
        this.f13058e = (T[]) ((RealFieldElement[]) MathArrays.buildArray(field, 7));
        this.f13058e[0] = fraction(-1, 20);
        this.f13058e[1] = field.getZero();
        this.f13058e[2] = fraction(81, 160);
        this.f13058e[3] = fraction(-6, 5);
        this.f13058e[4] = fraction(25, 32);
        this.f13058e[5] = fraction(1, 16);
        this.f13058e[6] = fraction(-1, 10);
    }

    public HighamHall54FieldIntegrator(Field<T> field, double d2, double d3, double[] dArr, double[] dArr2) {
        super(field, METHOD_NAME, -1, d2, d3, dArr, dArr2);
        this.f13058e = (T[]) ((RealFieldElement[]) MathArrays.buildArray(field, 7));
        this.f13058e[0] = fraction(-1, 20);
        this.f13058e[1] = field.getZero();
        this.f13058e[2] = fraction(81, 160);
        this.f13058e[3] = fraction(-6, 5);
        this.f13058e[4] = fraction(25, 32);
        this.f13058e[5] = fraction(1, 16);
        this.f13058e[6] = fraction(-1, 10);
    }

    @Override // org.apache.commons.math3.ode.nonstiff.FieldButcherArrayProvider
    public T[] getC() {
        T[] tArr = (T[]) ((RealFieldElement[]) MathArrays.buildArray(getField(), 6));
        tArr[0] = fraction(2, 9);
        tArr[1] = fraction(1, 3);
        tArr[2] = fraction(1, 2);
        tArr[3] = fraction(3, 5);
        tArr[4] = getField().getOne();
        tArr[5] = getField().getOne();
        return tArr;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.apache.commons.math3.ode.nonstiff.FieldButcherArrayProvider
    public T[][] getA() {
        T[][] tArr = (T[][]) ((RealFieldElement[][]) MathArrays.buildArray(getField(), 6, -1));
        for (int i2 = 0; i2 < tArr.length; i2++) {
            tArr[i2] = (RealFieldElement[]) MathArrays.buildArray(getField(), i2 + 1);
        }
        tArr[0][0] = fraction(2, 9);
        tArr[1][0] = fraction(1, 12);
        tArr[1][1] = fraction(1, 4);
        tArr[2][0] = fraction(1, 8);
        tArr[2][1] = getField().getZero();
        tArr[2][2] = fraction(3, 8);
        tArr[3][0] = fraction(91, 500);
        tArr[3][1] = fraction(-27, 100);
        tArr[3][2] = fraction(78, 125);
        tArr[3][3] = fraction(8, 125);
        tArr[4][0] = fraction(-11, 20);
        tArr[4][1] = fraction(27, 20);
        tArr[4][2] = fraction(12, 5);
        tArr[4][3] = fraction(-36, 5);
        tArr[4][4] = fraction(5, 1);
        tArr[5][0] = fraction(1, 12);
        tArr[5][1] = getField().getZero();
        tArr[5][2] = fraction(27, 32);
        tArr[5][3] = fraction(-4, 3);
        tArr[5][4] = fraction(125, 96);
        tArr[5][5] = fraction(5, 48);
        return tArr;
    }

    @Override // org.apache.commons.math3.ode.nonstiff.FieldButcherArrayProvider
    public T[] getB() {
        T[] tArr = (T[]) ((RealFieldElement[]) MathArrays.buildArray(getField(), 7));
        tArr[0] = fraction(1, 12);
        tArr[1] = getField().getZero();
        tArr[2] = fraction(27, 32);
        tArr[3] = fraction(-4, 3);
        tArr[4] = fraction(125, 96);
        tArr[5] = fraction(5, 48);
        tArr[6] = getField().getZero();
        return tArr;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaFieldIntegrator
    public HighamHall54FieldStepInterpolator<T> createInterpolator(boolean forward, T[][] yDotK, FieldODEStateAndDerivative<T> globalPreviousState, FieldODEStateAndDerivative<T> globalCurrentState, FieldEquationsMapper<T> mapper) {
        return new HighamHall54FieldStepInterpolator<>(getField(), forward, yDotK, globalPreviousState, globalCurrentState, globalPreviousState, globalCurrentState, mapper);
    }

    @Override // org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaFieldIntegrator
    public int getOrder() {
        return 5;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v38, types: [org.apache.commons.math3.RealFieldElement] */
    @Override // org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaFieldIntegrator
    protected T estimateError(T[][] yDotK, T[] y0, T[] y1, T h2) {
        T error = getField().getZero();
        for (int j2 = 0; j2 < this.mainSetDimension; j2++) {
            RealFieldElement realFieldElement = (RealFieldElement) yDotK[0][j2].multiply(this.f13058e[0]);
            for (int l2 = 1; l2 < this.f13058e.length; l2++) {
                realFieldElement = (RealFieldElement) realFieldElement.add((RealFieldElement) yDotK[l2][j2].multiply(this.f13058e[l2]));
            }
            RealFieldElement realFieldElementMax = MathUtils.max((RealFieldElement) y0[j2].abs(), (RealFieldElement) y1[j2].abs());
            RealFieldElement realFieldElement2 = (RealFieldElement) ((RealFieldElement) h2.multiply(realFieldElement)).divide((RealFieldElement) (this.vecAbsoluteTolerance == null ? ((RealFieldElement) realFieldElementMax.multiply(this.scalRelativeTolerance)).add(this.scalAbsoluteTolerance) : ((RealFieldElement) realFieldElementMax.multiply(this.vecRelativeTolerance[j2])).add(this.vecAbsoluteTolerance[j2])));
            error = (RealFieldElement) error.add(realFieldElement2.multiply(realFieldElement2));
        }
        return (T) ((RealFieldElement) error.divide(this.mainSetDimension)).sqrt();
    }
}
