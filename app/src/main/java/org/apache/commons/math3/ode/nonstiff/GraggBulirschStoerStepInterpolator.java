package org.apache.commons.math3.ode.nonstiff;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import org.apache.commons.math3.ode.EquationsMapper;
import org.apache.commons.math3.ode.sampling.AbstractStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ode/nonstiff/GraggBulirschStoerStepInterpolator.class */
class GraggBulirschStoerStepInterpolator extends AbstractStepInterpolator {
    private static final long serialVersionUID = 20110928;
    private double[] y0Dot;
    private double[] y1;
    private double[] y1Dot;
    private double[][] yMidDots;
    private double[][] polynomials;
    private double[] errfac;
    private int currentDegree;

    public GraggBulirschStoerStepInterpolator() {
        this.y0Dot = null;
        this.y1 = null;
        this.y1Dot = null;
        this.yMidDots = (double[][]) null;
        resetTables(-1);
    }

    GraggBulirschStoerStepInterpolator(double[] y2, double[] y0Dot, double[] y1, double[] y1Dot, double[][] yMidDots, boolean forward, EquationsMapper primaryMapper, EquationsMapper[] secondaryMappers) {
        super(y2, forward, primaryMapper, secondaryMappers);
        this.y0Dot = y0Dot;
        this.y1 = y1;
        this.y1Dot = y1Dot;
        this.yMidDots = yMidDots;
        resetTables(yMidDots.length + 4);
    }

    GraggBulirschStoerStepInterpolator(GraggBulirschStoerStepInterpolator interpolator) {
        super(interpolator);
        int dimension = this.currentState.length;
        this.y0Dot = null;
        this.y1 = null;
        this.y1Dot = null;
        this.yMidDots = (double[][]) null;
        if (interpolator.polynomials == null) {
            this.polynomials = (double[][]) null;
            this.currentDegree = -1;
            return;
        }
        resetTables(interpolator.currentDegree);
        for (int i2 = 0; i2 < this.polynomials.length; i2++) {
            this.polynomials[i2] = new double[dimension];
            System.arraycopy(interpolator.polynomials[i2], 0, this.polynomials[i2], 0, dimension);
        }
        this.currentDegree = interpolator.currentDegree;
    }

    /* JADX WARN: Type inference failed for: r0v3, types: [double[], double[][], java.lang.Object] */
    private void resetTables(int maxDegree) {
        if (maxDegree < 0) {
            this.polynomials = (double[][]) null;
            this.errfac = null;
            this.currentDegree = -1;
            return;
        }
        ?? r0 = new double[maxDegree + 1];
        if (this.polynomials != null) {
            System.arraycopy(this.polynomials, 0, r0, 0, this.polynomials.length);
            for (int i2 = this.polynomials.length; i2 < r0.length; i2++) {
                r0[i2] = new double[this.currentState.length];
            }
        } else {
            for (int i3 = 0; i3 < r0.length; i3++) {
                r0[i3] = new double[this.currentState.length];
            }
        }
        this.polynomials = r0;
        if (maxDegree <= 4) {
            this.errfac = null;
        } else {
            this.errfac = new double[maxDegree - 4];
            for (int i4 = 0; i4 < this.errfac.length; i4++) {
                int ip5 = i4 + 5;
                this.errfac[i4] = 1.0d / (ip5 * ip5);
                double e2 = 0.5d * FastMath.sqrt((i4 + 1) / ip5);
                for (int j2 = 0; j2 <= i4; j2++) {
                    double[] dArr = this.errfac;
                    int i5 = i4;
                    dArr[i5] = dArr[i5] * (e2 / (j2 + 1));
                }
            }
        }
        this.currentDegree = 0;
    }

    @Override // org.apache.commons.math3.ode.sampling.AbstractStepInterpolator
    protected StepInterpolator doCopy() {
        return new GraggBulirschStoerStepInterpolator(this);
    }

    public void computeCoefficients(int mu, double h2) {
        if (this.polynomials == null || this.polynomials.length <= mu + 4) {
            resetTables(mu + 4);
        }
        this.currentDegree = mu + 4;
        for (int i2 = 0; i2 < this.currentState.length; i2++) {
            double yp0 = h2 * this.y0Dot[i2];
            double yp1 = h2 * this.y1Dot[i2];
            double ydiff = this.y1[i2] - this.currentState[i2];
            double aspl = ydiff - yp1;
            double bspl = yp0 - ydiff;
            this.polynomials[0][i2] = this.currentState[i2];
            this.polynomials[1][i2] = ydiff;
            this.polynomials[2][i2] = aspl;
            this.polynomials[3][i2] = bspl;
            if (mu < 0) {
                return;
            }
            double ph0 = (0.5d * (this.currentState[i2] + this.y1[i2])) + (0.125d * (aspl + bspl));
            this.polynomials[4][i2] = 16.0d * (this.yMidDots[0][i2] - ph0);
            if (mu > 0) {
                double ph1 = ydiff + (0.25d * (aspl - bspl));
                this.polynomials[5][i2] = 16.0d * (this.yMidDots[1][i2] - ph1);
                if (mu > 1) {
                    double ph2 = yp1 - yp0;
                    this.polynomials[6][i2] = 16.0d * ((this.yMidDots[2][i2] - ph2) + this.polynomials[4][i2]);
                    if (mu > 2) {
                        double ph3 = 6.0d * (bspl - aspl);
                        this.polynomials[7][i2] = 16.0d * ((this.yMidDots[3][i2] - ph3) + (3.0d * this.polynomials[5][i2]));
                        for (int j2 = 4; j2 <= mu; j2++) {
                            double fac1 = 0.5d * j2 * (j2 - 1);
                            double fac2 = 2.0d * fac1 * (j2 - 2) * (j2 - 3);
                            this.polynomials[j2 + 4][i2] = 16.0d * ((this.yMidDots[j2][i2] + (fac1 * this.polynomials[j2 + 2][i2])) - (fac2 * this.polynomials[j2][i2]));
                        }
                    }
                }
            }
        }
    }

    public double estimateError(double[] scale) {
        double error = 0.0d;
        if (this.currentDegree >= 5) {
            for (int i2 = 0; i2 < scale.length; i2++) {
                double e2 = this.polynomials[this.currentDegree][i2] / scale[i2];
                error += e2 * e2;
            }
            error = FastMath.sqrt(error / scale.length) * this.errfac[this.currentDegree - 5];
        }
        return error;
    }

    @Override // org.apache.commons.math3.ode.sampling.AbstractStepInterpolator
    protected void computeInterpolatedStateAndDerivatives(double theta, double oneMinusThetaH) {
        int dimension = this.currentState.length;
        double oneMinusTheta = 1.0d - theta;
        double theta05 = theta - 0.5d;
        double tOmT = theta * oneMinusTheta;
        double t4 = tOmT * tOmT;
        double t4Dot = 2.0d * tOmT * (1.0d - (2.0d * theta));
        double dot1 = 1.0d / this.f13067h;
        double dot2 = (theta * (2.0d - (3.0d * theta))) / this.f13067h;
        double dot3 = ((((3.0d * theta) - 4.0d) * theta) + 1.0d) / this.f13067h;
        for (int i2 = 0; i2 < dimension; i2++) {
            double p0 = this.polynomials[0][i2];
            double p1 = this.polynomials[1][i2];
            double p2 = this.polynomials[2][i2];
            double p3 = this.polynomials[3][i2];
            this.interpolatedState[i2] = p0 + (theta * (p1 + (oneMinusTheta * ((p2 * theta) + (p3 * oneMinusTheta)))));
            this.interpolatedDerivatives[i2] = (dot1 * p1) + (dot2 * p2) + (dot3 * p3);
            if (this.currentDegree > 3) {
                double cDot = 0.0d;
                double c2 = this.polynomials[this.currentDegree][i2];
                for (int j2 = this.currentDegree - 1; j2 > 3; j2--) {
                    double d2 = 1.0d / (j2 - 3);
                    cDot = d2 * ((theta05 * cDot) + c2);
                    c2 = this.polynomials[j2][i2] + (c2 * d2 * theta05);
                }
                double[] dArr = this.interpolatedState;
                int i3 = i2;
                dArr[i3] = dArr[i3] + (t4 * c2);
                double[] dArr2 = this.interpolatedDerivatives;
                int i4 = i2;
                dArr2[i4] = dArr2[i4] + (((t4 * cDot) + (t4Dot * c2)) / this.f13067h);
            }
        }
        if (this.f13067h == 0.0d) {
            System.arraycopy(this.yMidDots[1], 0, this.interpolatedDerivatives, 0, dimension);
        }
    }

    @Override // org.apache.commons.math3.ode.sampling.AbstractStepInterpolator, java.io.Externalizable
    public void writeExternal(ObjectOutput out) throws IOException {
        int dimension = this.currentState == null ? -1 : this.currentState.length;
        writeBaseExternal(out);
        out.writeInt(this.currentDegree);
        for (int k2 = 0; k2 <= this.currentDegree; k2++) {
            for (int l2 = 0; l2 < dimension; l2++) {
                out.writeDouble(this.polynomials[k2][l2]);
            }
        }
    }

    @Override // org.apache.commons.math3.ode.sampling.AbstractStepInterpolator, java.io.Externalizable
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        double t2 = readBaseExternal(in);
        int dimension = this.currentState == null ? -1 : this.currentState.length;
        int degree = in.readInt();
        resetTables(degree);
        this.currentDegree = degree;
        for (int k2 = 0; k2 <= this.currentDegree; k2++) {
            for (int l2 = 0; l2 < dimension; l2++) {
                this.polynomials[k2][l2] = in.readDouble();
            }
        }
        setInterpolatedTime(t2);
    }
}
