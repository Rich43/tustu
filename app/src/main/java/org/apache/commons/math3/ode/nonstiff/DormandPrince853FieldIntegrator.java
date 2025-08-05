package org.apache.commons.math3.ode.nonstiff;

import org.apache.commons.math3.Field;
import org.apache.commons.math3.RealFieldElement;
import org.apache.commons.math3.ode.FieldEquationsMapper;
import org.apache.commons.math3.ode.FieldODEStateAndDerivative;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.MathUtils;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ode/nonstiff/DormandPrince853FieldIntegrator.class */
public class DormandPrince853FieldIntegrator<T extends RealFieldElement<T>> extends EmbeddedRungeKuttaFieldIntegrator<T> {
    private static final String METHOD_NAME = "Dormand-Prince 8 (5, 3)";
    private final T e1_01;
    private final T e1_06;
    private final T e1_07;
    private final T e1_08;
    private final T e1_09;
    private final T e1_10;
    private final T e1_11;
    private final T e1_12;
    private final T e2_01;
    private final T e2_06;
    private final T e2_07;
    private final T e2_08;
    private final T e2_09;
    private final T e2_10;
    private final T e2_11;
    private final T e2_12;

    public DormandPrince853FieldIntegrator(Field<T> field, double minStep, double maxStep, double scalAbsoluteTolerance, double scalRelativeTolerance) {
        super(field, METHOD_NAME, 12, minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
        this.e1_01 = fraction(1.16092271E8d, 8.84846592E9d);
        this.e1_06 = fraction(-1871647.0d, 1527680.0d);
        this.e1_07 = fraction(-6.9799717E7d, 1.4079366E8d);
        this.e1_08 = fraction(1.230164450203E12d, 7.39113984E11d);
        this.e1_09 = fraction(-1.980813971228885E15d, 5.654156025964544E15d);
        this.e1_10 = fraction(4.64500805E8d, 1.389975552E9d);
        this.e1_11 = fraction(1.606764981773E12d, 1.9613062656E13d);
        this.e1_12 = fraction(-137909.0d, 6168960.0d);
        this.e2_01 = fraction(-364463.0d, 1920240.0d);
        this.e2_06 = fraction(3399327.0d, 763840.0d);
        this.e2_07 = fraction(6.6578432E7d, 3.5198415E7d);
        this.e2_08 = fraction(-1.674902723E9d, 2.887164E8d);
        this.e2_09 = fraction(-7.4684743568175E13d, 1.76692375811392E14d);
        this.e2_10 = fraction(-734375.0d, 4826304.0d);
        this.e2_11 = fraction(1.71414593E8d, 8.512614E8d);
        this.e2_12 = fraction(69869.0d, 3084480.0d);
    }

    public DormandPrince853FieldIntegrator(Field<T> field, double minStep, double maxStep, double[] vecAbsoluteTolerance, double[] vecRelativeTolerance) {
        super(field, METHOD_NAME, 12, minStep, maxStep, vecAbsoluteTolerance, vecRelativeTolerance);
        this.e1_01 = fraction(1.16092271E8d, 8.84846592E9d);
        this.e1_06 = fraction(-1871647.0d, 1527680.0d);
        this.e1_07 = fraction(-6.9799717E7d, 1.4079366E8d);
        this.e1_08 = fraction(1.230164450203E12d, 7.39113984E11d);
        this.e1_09 = fraction(-1.980813971228885E15d, 5.654156025964544E15d);
        this.e1_10 = fraction(4.64500805E8d, 1.389975552E9d);
        this.e1_11 = fraction(1.606764981773E12d, 1.9613062656E13d);
        this.e1_12 = fraction(-137909.0d, 6168960.0d);
        this.e2_01 = fraction(-364463.0d, 1920240.0d);
        this.e2_06 = fraction(3399327.0d, 763840.0d);
        this.e2_07 = fraction(6.6578432E7d, 3.5198415E7d);
        this.e2_08 = fraction(-1.674902723E9d, 2.887164E8d);
        this.e2_09 = fraction(-7.4684743568175E13d, 1.76692375811392E14d);
        this.e2_10 = fraction(-734375.0d, 4826304.0d);
        this.e2_11 = fraction(1.71414593E8d, 8.512614E8d);
        this.e2_12 = fraction(69869.0d, 3084480.0d);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.apache.commons.math3.ode.nonstiff.FieldButcherArrayProvider
    public T[] getC() {
        RealFieldElement realFieldElement = (RealFieldElement) ((RealFieldElement) getField().getOne().multiply(6)).sqrt();
        T[] tArr = (T[]) ((RealFieldElement[]) MathArrays.buildArray(getField(), 15));
        tArr[0] = (RealFieldElement) ((RealFieldElement) realFieldElement.add(-6.0d)).divide(-67.5d);
        tArr[1] = (RealFieldElement) ((RealFieldElement) realFieldElement.add(-6.0d)).divide(-45.0d);
        tArr[2] = (RealFieldElement) ((RealFieldElement) realFieldElement.add(-6.0d)).divide(-30.0d);
        tArr[3] = (RealFieldElement) ((RealFieldElement) realFieldElement.add(6.0d)).divide(30.0d);
        tArr[4] = fraction(1, 3);
        tArr[5] = fraction(1, 4);
        tArr[6] = fraction(4, 13);
        tArr[7] = fraction(127, 195);
        tArr[8] = fraction(3, 5);
        tArr[9] = fraction(6, 7);
        tArr[10] = getField().getOne();
        tArr[11] = getField().getOne();
        tArr[12] = fraction(1.0d, 10.0d);
        tArr[13] = fraction(1.0d, 5.0d);
        tArr[14] = fraction(7.0d, 9.0d);
        return tArr;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.apache.commons.math3.ode.nonstiff.FieldButcherArrayProvider
    public T[][] getA() {
        RealFieldElement realFieldElement = (RealFieldElement) ((RealFieldElement) getField().getOne().multiply(6)).sqrt();
        T[][] tArr = (T[][]) ((RealFieldElement[][]) MathArrays.buildArray(getField(), 15, -1));
        for (int i2 = 0; i2 < tArr.length; i2++) {
            tArr[i2] = (RealFieldElement[]) MathArrays.buildArray(getField(), i2 + 1);
        }
        tArr[0][0] = (RealFieldElement) ((RealFieldElement) realFieldElement.add(-6.0d)).divide(-67.5d);
        tArr[1][0] = (RealFieldElement) ((RealFieldElement) realFieldElement.add(-6.0d)).divide(-180.0d);
        tArr[1][1] = (RealFieldElement) ((RealFieldElement) realFieldElement.add(-6.0d)).divide(-60.0d);
        tArr[2][0] = (RealFieldElement) ((RealFieldElement) realFieldElement.add(-6.0d)).divide(-120.0d);
        tArr[2][1] = getField().getZero();
        tArr[2][2] = (RealFieldElement) ((RealFieldElement) realFieldElement.add(-6.0d)).divide(-40.0d);
        tArr[3][0] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) realFieldElement.multiply(107)).add(462.0d)).divide(3000.0d);
        tArr[3][1] = getField().getZero();
        tArr[3][2] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) realFieldElement.multiply(197)).add(402.0d)).divide(-1000.0d);
        tArr[3][3] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) realFieldElement.multiply(73)).add(168.0d)).divide(375.0d);
        tArr[4][0] = fraction(1, 27);
        tArr[4][1] = getField().getZero();
        tArr[4][2] = getField().getZero();
        tArr[4][3] = (RealFieldElement) ((RealFieldElement) realFieldElement.add(16.0d)).divide(108.0d);
        tArr[4][4] = (RealFieldElement) ((RealFieldElement) realFieldElement.add(-16.0d)).divide(-108.0d);
        tArr[5][0] = fraction(19, 512);
        tArr[5][1] = getField().getZero();
        tArr[5][2] = getField().getZero();
        tArr[5][3] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) realFieldElement.multiply(23)).add(118.0d)).divide(1024.0d);
        tArr[5][4] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) realFieldElement.multiply(-23)).add(118.0d)).divide(1024.0d);
        tArr[5][5] = fraction(-9, 512);
        tArr[6][0] = fraction(13772, 371293);
        tArr[6][1] = getField().getZero();
        tArr[6][2] = getField().getZero();
        tArr[6][3] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) realFieldElement.multiply(4784)).add(51544.0d)).divide(371293.0d);
        tArr[6][4] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) realFieldElement.multiply(-4784)).add(51544.0d)).divide(371293.0d);
        tArr[6][5] = fraction(-5688, 371293);
        tArr[6][6] = fraction(3072, 371293);
        tArr[7][0] = fraction(5.8656157643E10d, 9.3983540625E10d);
        tArr[7][1] = getField().getZero();
        tArr[7][2] = getField().getZero();
        tArr[7][3] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) realFieldElement.multiply(-3.18801444819E11d)).add(-1.324889724104E12d)).divide(6.265569375E11d);
        tArr[7][4] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) realFieldElement.multiply(3.18801444819E11d)).add(-1.324889724104E12d)).divide(6.265569375E11d);
        tArr[7][5] = fraction(9.6044563816E10d, 3.480871875E9d);
        tArr[7][6] = fraction(5.682451879168E12d, 2.81950621875E11d);
        tArr[7][7] = fraction(-1.65125654E8d, 3796875.0d);
        tArr[8][0] = fraction(8909899.0d, 1.8653125E7d);
        tArr[8][1] = getField().getZero();
        tArr[8][2] = getField().getZero();
        tArr[8][3] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) realFieldElement.multiply(-1137963.0d)).add(-4521408.0d)).divide(2937500.0d);
        tArr[8][4] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) realFieldElement.multiply(1137963.0d)).add(-4521408.0d)).divide(2937500.0d);
        tArr[8][5] = fraction(9.6663078E7d, 4553125.0d);
        tArr[8][6] = fraction(2.107245056E9d, 1.37915625E8d);
        tArr[8][7] = fraction(-4.913652016E9d, 1.47609375E8d);
        tArr[8][8] = fraction(-7.889427E7d, 3.880452869E9d);
        tArr[9][0] = fraction(-2.0401265806E10d, 2.1769653311E10d);
        tArr[9][1] = getField().getZero();
        tArr[9][2] = getField().getZero();
        tArr[9][3] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) realFieldElement.multiply(94326.0d)).add(354216.0d)).divide(112847.0d);
        tArr[9][4] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) realFieldElement.multiply(-94326.0d)).add(354216.0d)).divide(112847.0d);
        tArr[9][5] = fraction(-4.3306765128E10d, 5.313852383E9d);
        tArr[9][6] = fraction(-2.0866708358144E13d, 1.126708119789E12d);
        tArr[9][7] = fraction(1.488600343802E13d, 6.54632330667E11d);
        tArr[9][8] = fraction(3.5290686222309376E16d, 1.4152473387134412E16d);
        tArr[9][9] = fraction(-1.477884375E9d, 4.85066827E8d);
        tArr[10][0] = fraction(3.9815761E7d, 1.7514443E7d);
        tArr[10][1] = getField().getZero();
        tArr[10][2] = getField().getZero();
        tArr[10][3] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) realFieldElement.multiply(-960905.0d)).add(-3457480.0d)).divide(551636.0d);
        tArr[10][4] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) realFieldElement.multiply(960905.0d)).add(-3457480.0d)).divide(551636.0d);
        tArr[10][5] = fraction(-8.44554132E8d, 4.7026969E7d);
        tArr[10][6] = fraction(8.444996352E9d, 3.02158619E8d);
        tArr[10][7] = fraction(-2.509602342E9d, 8.77790785E8d);
        tArr[10][8] = fraction(-2.838879529799625E16d, 3.199510091356783E15d);
        tArr[10][9] = fraction(2.2671625E8d, 1.8341897E7d);
        tArr[10][10] = fraction(1.371316744E9d, 2.131383595E9d);
        tArr[11][0] = fraction(104257.0d, 1920240.0d);
        tArr[11][1] = getField().getZero();
        tArr[11][2] = getField().getZero();
        tArr[11][3] = getField().getZero();
        tArr[11][4] = getField().getZero();
        tArr[11][5] = fraction(3399327.0d, 763840.0d);
        tArr[11][6] = fraction(6.6578432E7d, 3.5198415E7d);
        tArr[11][7] = fraction(-1.674902723E9d, 2.887164E8d);
        tArr[11][8] = fraction(5.4980371265625E13d, 1.76692375811392E14d);
        tArr[11][9] = fraction(-734375.0d, 4826304.0d);
        tArr[11][10] = fraction(1.71414593E8d, 8.512614E8d);
        tArr[11][11] = fraction(137909.0d, 3084480.0d);
        tArr[12][0] = fraction(1.3481885573E10d, 2.4003E11d);
        tArr[12][1] = getField().getZero();
        tArr[12][2] = getField().getZero();
        tArr[12][3] = getField().getZero();
        tArr[12][4] = getField().getZero();
        tArr[12][5] = getField().getZero();
        tArr[12][6] = fraction(1.39418837528E11d, 5.49975234375E11d);
        tArr[12][7] = fraction(-1.1108320068443E13d, 4.51119375E13d);
        tArr[12][8] = fraction(-1.769651421925959E15d, 1.424938514608E16d);
        tArr[12][9] = fraction(5.7799439E7d, 3.77055E8d);
        tArr[12][10] = fraction(7.93322643029E11d, 9.673425E13d);
        tArr[12][11] = fraction(1.458939311E9d, 1.9278E11d);
        tArr[12][12] = fraction(-4149.0d, 500000.0d);
        tArr[13][0] = fraction(1.595561272731E12d, 5.01202735E13d);
        tArr[13][1] = getField().getZero();
        tArr[13][2] = getField().getZero();
        tArr[13][3] = getField().getZero();
        tArr[13][4] = getField().getZero();
        tArr[13][5] = fraction(9.75183916491E11d, 3.445768803125E13d);
        tArr[13][6] = fraction(3.8492013932672E13d, 7.18912673015625E14d);
        tArr[13][7] = fraction(-1.114881286517557E15d, 2.02987107675E16d);
        tArr[13][8] = getField().getZero();
        tArr[13][9] = getField().getZero();
        tArr[13][10] = fraction(-2.538710946863E12d, 2.343122786125E16d);
        tArr[13][11] = fraction(8.824659001E9d, 2.306671678125E13d);
        tArr[13][12] = fraction(-1.1518334563E10d, 3.38311846125E13d);
        tArr[13][13] = fraction(1.912306948E9d, 1.3532473845E10d);
        tArr[14][0] = fraction(-1.3613986967E10d, 3.1741908048E10d);
        tArr[14][1] = getField().getZero();
        tArr[14][2] = getField().getZero();
        tArr[14][3] = getField().getZero();
        tArr[14][4] = getField().getZero();
        tArr[14][5] = fraction(-4.755612631E9d, 1.012344804E9d);
        tArr[14][6] = fraction(4.2939257944576E13d, 5.588559685701E12d);
        tArr[14][7] = fraction(7.7881972900277E13d, 1.9140370552944E13d);
        tArr[14][8] = fraction(2.2719829234375E13d, 6.3689648654052E13d);
        tArr[14][9] = getField().getZero();
        tArr[14][10] = getField().getZero();
        tArr[14][11] = getField().getZero();
        tArr[14][12] = fraction(-1.199007803E9d, 8.57031517296E11d);
        tArr[14][13] = fraction(1.57882067E11d, 5.3564469831E10d);
        tArr[14][14] = fraction(-2.90468882375E11d, 3.1741908048E10d);
        return tArr;
    }

    @Override // org.apache.commons.math3.ode.nonstiff.FieldButcherArrayProvider
    public T[] getB() {
        T[] tArr = (T[]) ((RealFieldElement[]) MathArrays.buildArray(getField(), 16));
        tArr[0] = fraction(104257, 1920240);
        tArr[1] = getField().getZero();
        tArr[2] = getField().getZero();
        tArr[3] = getField().getZero();
        tArr[4] = getField().getZero();
        tArr[5] = fraction(3399327.0d, 763840.0d);
        tArr[6] = fraction(6.6578432E7d, 3.5198415E7d);
        tArr[7] = fraction(-1.674902723E9d, 2.887164E8d);
        tArr[8] = fraction(5.4980371265625E13d, 1.76692375811392E14d);
        tArr[9] = fraction(-734375.0d, 4826304.0d);
        tArr[10] = fraction(1.71414593E8d, 8.512614E8d);
        tArr[11] = fraction(137909.0d, 3084480.0d);
        tArr[12] = getField().getZero();
        tArr[13] = getField().getZero();
        tArr[14] = getField().getZero();
        tArr[15] = getField().getZero();
        return tArr;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaFieldIntegrator
    public DormandPrince853FieldStepInterpolator<T> createInterpolator(boolean forward, T[][] yDotK, FieldODEStateAndDerivative<T> globalPreviousState, FieldODEStateAndDerivative<T> globalCurrentState, FieldEquationsMapper<T> mapper) {
        return new DormandPrince853FieldStepInterpolator<>(getField(), forward, yDotK, globalPreviousState, globalCurrentState, globalPreviousState, globalCurrentState, mapper);
    }

    @Override // org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaFieldIntegrator
    public int getOrder() {
        return 8;
    }

    @Override // org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaFieldIntegrator
    protected T estimateError(T[][] yDotK, T[] y0, T[] y1, T h2) {
        RealFieldElement realFieldElement = (RealFieldElement) h2.getField2().getZero();
        RealFieldElement realFieldElement2 = (RealFieldElement) h2.getField2().getZero();
        for (int j2 = 0; j2 < this.mainSetDimension; j2++) {
            RealFieldElement realFieldElement3 = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) yDotK[0][j2].multiply(this.e1_01)).add((RealFieldElement) yDotK[5][j2].multiply(this.e1_06))).add((RealFieldElement) yDotK[6][j2].multiply(this.e1_07))).add((RealFieldElement) yDotK[7][j2].multiply(this.e1_08))).add((RealFieldElement) yDotK[8][j2].multiply(this.e1_09))).add((RealFieldElement) yDotK[9][j2].multiply(this.e1_10))).add((RealFieldElement) yDotK[10][j2].multiply(this.e1_11))).add((RealFieldElement) yDotK[11][j2].multiply(this.e1_12));
            RealFieldElement realFieldElement4 = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) yDotK[0][j2].multiply(this.e2_01)).add((RealFieldElement) yDotK[5][j2].multiply(this.e2_06))).add((RealFieldElement) yDotK[6][j2].multiply(this.e2_07))).add((RealFieldElement) yDotK[7][j2].multiply(this.e2_08))).add((RealFieldElement) yDotK[8][j2].multiply(this.e2_09))).add((RealFieldElement) yDotK[9][j2].multiply(this.e2_10))).add((RealFieldElement) yDotK[10][j2].multiply(this.e2_11))).add((RealFieldElement) yDotK[11][j2].multiply(this.e2_12));
            RealFieldElement realFieldElementMax = MathUtils.max((RealFieldElement) y0[j2].abs(), (RealFieldElement) y1[j2].abs());
            RealFieldElement realFieldElement5 = (RealFieldElement) (this.vecAbsoluteTolerance == null ? ((RealFieldElement) realFieldElementMax.multiply(this.scalRelativeTolerance)).add(this.scalAbsoluteTolerance) : ((RealFieldElement) realFieldElementMax.multiply(this.vecRelativeTolerance[j2])).add(this.vecAbsoluteTolerance[j2]));
            RealFieldElement realFieldElement6 = (RealFieldElement) realFieldElement3.divide(realFieldElement5);
            realFieldElement = (RealFieldElement) realFieldElement.add((RealFieldElement) realFieldElement6.multiply(realFieldElement6));
            RealFieldElement realFieldElement7 = (RealFieldElement) realFieldElement4.divide(realFieldElement5);
            realFieldElement2 = (RealFieldElement) realFieldElement2.add((RealFieldElement) realFieldElement7.multiply(realFieldElement7));
        }
        RealFieldElement realFieldElement8 = (RealFieldElement) realFieldElement.add((RealFieldElement) realFieldElement2.multiply(0.01d));
        if (realFieldElement8.getReal() <= 0.0d) {
            realFieldElement8 = (RealFieldElement) h2.getField2().getOne();
        }
        return (T) ((RealFieldElement) ((RealFieldElement) h2.abs()).multiply(realFieldElement)).divide((RealFieldElement) ((RealFieldElement) realFieldElement8.multiply(this.mainSetDimension)).sqrt());
    }
}
