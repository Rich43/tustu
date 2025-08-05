package org.apache.commons.math3.ode.nonstiff;

import jssc.SerialPort;
import org.apache.commons.math3.Field;
import org.apache.commons.math3.RealFieldElement;
import org.apache.commons.math3.ode.FieldEquationsMapper;
import org.apache.commons.math3.ode.FieldODEStateAndDerivative;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.MathUtils;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ode/nonstiff/DormandPrince54FieldIntegrator.class */
public class DormandPrince54FieldIntegrator<T extends RealFieldElement<T>> extends EmbeddedRungeKuttaFieldIntegrator<T> {
    private static final String METHOD_NAME = "Dormand-Prince 5(4)";
    private final T e1;
    private final T e3;
    private final T e4;
    private final T e5;
    private final T e6;
    private final T e7;

    public DormandPrince54FieldIntegrator(Field<T> field, double minStep, double maxStep, double scalAbsoluteTolerance, double scalRelativeTolerance) {
        super(field, METHOD_NAME, 6, minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
        this.e1 = fraction(71, SerialPort.BAUDRATE_57600);
        this.e3 = fraction(-71, 16695);
        this.e4 = fraction(71, 1920);
        this.e5 = fraction(-17253, 339200);
        this.e6 = fraction(22, 525);
        this.e7 = fraction(-1, 40);
    }

    public DormandPrince54FieldIntegrator(Field<T> field, double minStep, double maxStep, double[] vecAbsoluteTolerance, double[] vecRelativeTolerance) {
        super(field, METHOD_NAME, 6, minStep, maxStep, vecAbsoluteTolerance, vecRelativeTolerance);
        this.e1 = fraction(71, SerialPort.BAUDRATE_57600);
        this.e3 = fraction(-71, 16695);
        this.e4 = fraction(71, 1920);
        this.e5 = fraction(-17253, 339200);
        this.e6 = fraction(22, 525);
        this.e7 = fraction(-1, 40);
    }

    @Override // org.apache.commons.math3.ode.nonstiff.FieldButcherArrayProvider
    public T[] getC() {
        T[] tArr = (T[]) ((RealFieldElement[]) MathArrays.buildArray(getField(), 6));
        tArr[0] = fraction(1, 5);
        tArr[1] = fraction(3, 10);
        tArr[2] = fraction(4, 5);
        tArr[3] = fraction(8, 9);
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
        tArr[0][0] = fraction(1, 5);
        tArr[1][0] = fraction(3, 40);
        tArr[1][1] = fraction(9, 40);
        tArr[2][0] = fraction(44, 45);
        tArr[2][1] = fraction(-56, 15);
        tArr[2][2] = fraction(32, 9);
        tArr[3][0] = fraction(19372, 6561);
        tArr[3][1] = fraction(-25360, 2187);
        tArr[3][2] = fraction(64448, 6561);
        tArr[3][3] = fraction(-212, 729);
        tArr[4][0] = fraction(9017, 3168);
        tArr[4][1] = fraction(-355, 33);
        tArr[4][2] = fraction(46732, 5247);
        tArr[4][3] = fraction(49, 176);
        tArr[4][4] = fraction(-5103, 18656);
        tArr[5][0] = fraction(35, 384);
        tArr[5][1] = getField().getZero();
        tArr[5][2] = fraction(500, 1113);
        tArr[5][3] = fraction(125, 192);
        tArr[5][4] = fraction(-2187, 6784);
        tArr[5][5] = fraction(11, 84);
        return tArr;
    }

    @Override // org.apache.commons.math3.ode.nonstiff.FieldButcherArrayProvider
    public T[] getB() {
        T[] tArr = (T[]) ((RealFieldElement[]) MathArrays.buildArray(getField(), 7));
        tArr[0] = fraction(35, 384);
        tArr[1] = getField().getZero();
        tArr[2] = fraction(500, 1113);
        tArr[3] = fraction(125, 192);
        tArr[4] = fraction(-2187, 6784);
        tArr[5] = fraction(11, 84);
        tArr[6] = getField().getZero();
        return tArr;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaFieldIntegrator
    public DormandPrince54FieldStepInterpolator<T> createInterpolator(boolean forward, T[][] yDotK, FieldODEStateAndDerivative<T> globalPreviousState, FieldODEStateAndDerivative<T> globalCurrentState, FieldEquationsMapper<T> mapper) {
        return new DormandPrince54FieldStepInterpolator<>(getField(), forward, yDotK, globalPreviousState, globalCurrentState, globalPreviousState, globalCurrentState, mapper);
    }

    @Override // org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaFieldIntegrator
    public int getOrder() {
        return 5;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v46, types: [org.apache.commons.math3.RealFieldElement] */
    @Override // org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaFieldIntegrator
    protected T estimateError(T[][] yDotK, T[] y0, T[] y1, T h2) {
        T error = getField().getZero();
        for (int j2 = 0; j2 < this.mainSetDimension; j2++) {
            RealFieldElement realFieldElement = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) yDotK[0][j2].multiply(this.e1)).add((RealFieldElement) yDotK[2][j2].multiply(this.e3))).add((RealFieldElement) yDotK[3][j2].multiply(this.e4))).add((RealFieldElement) yDotK[4][j2].multiply(this.e5))).add((RealFieldElement) yDotK[5][j2].multiply(this.e6))).add((RealFieldElement) yDotK[6][j2].multiply(this.e7));
            RealFieldElement realFieldElementMax = MathUtils.max((RealFieldElement) y0[j2].abs(), (RealFieldElement) y1[j2].abs());
            RealFieldElement realFieldElement2 = (RealFieldElement) ((RealFieldElement) h2.multiply(realFieldElement)).divide((RealFieldElement) (this.vecAbsoluteTolerance == null ? ((RealFieldElement) realFieldElementMax.multiply(this.scalRelativeTolerance)).add(this.scalAbsoluteTolerance) : ((RealFieldElement) realFieldElementMax.multiply(this.vecRelativeTolerance[j2])).add(this.vecAbsoluteTolerance[j2])));
            error = (RealFieldElement) error.add(realFieldElement2.multiply(realFieldElement2));
        }
        return (T) ((RealFieldElement) error.divide(this.mainSetDimension)).sqrt();
    }
}
