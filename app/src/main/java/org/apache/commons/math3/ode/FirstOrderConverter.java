package org.apache.commons.math3.ode;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ode/FirstOrderConverter.class */
public class FirstOrderConverter implements FirstOrderDifferentialEquations {
    private final SecondOrderDifferentialEquations equations;
    private final int dimension;

    /* renamed from: z, reason: collision with root package name */
    private final double[] f13044z;
    private final double[] zDot;
    private final double[] zDDot;

    public FirstOrderConverter(SecondOrderDifferentialEquations equations) {
        this.equations = equations;
        this.dimension = equations.getDimension();
        this.f13044z = new double[this.dimension];
        this.zDot = new double[this.dimension];
        this.zDDot = new double[this.dimension];
    }

    @Override // org.apache.commons.math3.ode.FirstOrderDifferentialEquations
    public int getDimension() {
        return 2 * this.dimension;
    }

    @Override // org.apache.commons.math3.ode.FirstOrderDifferentialEquations
    public void computeDerivatives(double t2, double[] y2, double[] yDot) {
        System.arraycopy(y2, 0, this.f13044z, 0, this.dimension);
        System.arraycopy(y2, this.dimension, this.zDot, 0, this.dimension);
        this.equations.computeSecondDerivatives(t2, this.f13044z, this.zDot, this.zDDot);
        System.arraycopy(this.zDot, 0, yDot, 0, this.dimension);
        System.arraycopy(this.zDDot, 0, yDot, this.dimension, this.dimension);
    }
}
