package org.apache.commons.math3.ode.nonstiff;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.ode.AbstractIntegrator;
import org.apache.commons.math3.ode.EquationsMapper;
import org.apache.commons.math3.ode.sampling.StepInterpolator;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ode/nonstiff/DormandPrince853StepInterpolator.class */
class DormandPrince853StepInterpolator extends RungeKuttaStepInterpolator {
    private static final long serialVersionUID = 20111120;
    private static final double B_01 = 0.054293734116568765d;
    private static final double B_06 = 4.450312892752409d;
    private static final double B_07 = 1.8915178993145003d;
    private static final double B_08 = -5.801203960010585d;
    private static final double B_09 = 0.3111643669578199d;
    private static final double B_10 = -0.1521609496625161d;
    private static final double B_11 = 0.20136540080403034d;
    private static final double B_12 = 0.04471061572777259d;
    private static final double C14 = 0.1d;
    private static final double K14_01 = 0.0018737681664791894d;
    private static final double K14_06 = -4.450312892752409d;
    private static final double K14_07 = -1.6380176890978755d;
    private static final double K14_08 = 5.554964922539782d;
    private static final double K14_09 = -0.4353557902216363d;
    private static final double K14_10 = 0.30545274794128174d;
    private static final double K14_11 = -0.19316434850839564d;
    private static final double K14_12 = -0.03714271806722689d;
    private static final double K14_13 = -0.008298d;
    private static final double C15 = 0.2d;
    private static final double K15_01 = -0.022459085953066622d;
    private static final double K15_06 = -4.422011983080043d;
    private static final double K15_07 = -1.8379759110070617d;
    private static final double K15_08 = 5.746280211439194d;
    private static final double K15_09 = -0.3111643669578199d;
    private static final double K15_10 = 0.1521609496625161d;
    private static final double K15_11 = -0.2014737481327276d;
    private static final double K15_12 = -0.04432804463693693d;
    private static final double K15_13 = -3.4046500868740456E-4d;
    private static final double K15_14 = 0.1413124436746325d;
    private static final double C16 = 0.7777777777777778d;
    private static final double K16_01 = -0.4831900357003607d;
    private static final double K16_06 = -9.147934308113573d;
    private static final double K16_07 = 5.791903296748099d;
    private static final double K16_08 = 9.870193778407696d;
    private static final double K16_09 = 0.04556282049746119d;
    private static final double K16_10 = 0.1521609496625161d;
    private static final double K16_11 = -0.20136540080403034d;
    private static final double K16_12 = -0.04471061572777259d;
    private static final double K16_13 = -0.0013990241651590145d;
    private static final double K16_14 = 2.9475147891527724d;
    private static final double K16_15 = -9.15095847217987d;

    /* renamed from: D, reason: collision with root package name */
    private static final double[][] f13050D = {new double[]{-8.428938276109013d, 0.5667149535193777d, -3.0689499459498917d, 2.38466765651207d, 2.1170345824450285d, -0.871391583777973d, 2.2404374302607883d, 0.6315787787694688d, -0.08899033645133331d, 18.148505520854727d, -9.194632392478356d, -4.436036387594894d}, new double[]{10.427508642579134d, 242.28349177525817d, 165.20045171727028d, -374.5467547226902d, -22.113666853125302d, 7.733432668472264d, -30.674084731089398d, -9.332130526430229d, 15.697238121770845d, -31.139403219565178d, -9.35292435884448d, 35.81684148639408d}, new double[]{19.985053242002433d, -387.0373087493518d, -189.17813819516758d, 527.8081592054236d, -11.573902539959631d, 6.8812326946963d, -1.0006050966910838d, 0.7777137798053443d, -2.778205752353508d, -60.19669523126412d, 84.32040550667716d, 11.99229113618279d}, new double[]{-25.69393346270375d, -154.18974869023643d, -231.5293791760455d, 357.6391179106141d, 93.4053241836243d, -37.45832313645163d, 104.0996495089623d, 29.8402934266605d, -43.53345659001114d, 96.32455395918828d, -39.17726167561544d, -149.72683625798564d}};
    private double[][] yDotKLast;

    /* renamed from: v, reason: collision with root package name */
    private double[][] f13051v;
    private boolean vectorsInitialized;

    public DormandPrince853StepInterpolator() {
        this.yDotKLast = (double[][]) null;
        this.f13051v = (double[][]) null;
        this.vectorsInitialized = false;
    }

    /* JADX WARN: Type inference failed for: r1v2, types: [double[], double[][]] */
    /* JADX WARN: Type inference failed for: r1v7, types: [double[], double[][]] */
    DormandPrince853StepInterpolator(DormandPrince853StepInterpolator interpolator) {
        super(interpolator);
        if (interpolator.currentState == null) {
            this.yDotKLast = (double[][]) null;
            this.f13051v = (double[][]) null;
            this.vectorsInitialized = false;
            return;
        }
        int dimension = interpolator.currentState.length;
        this.yDotKLast = new double[3];
        for (int k2 = 0; k2 < this.yDotKLast.length; k2++) {
            this.yDotKLast[k2] = new double[dimension];
            System.arraycopy(interpolator.yDotKLast[k2], 0, this.yDotKLast[k2], 0, dimension);
        }
        this.f13051v = new double[7];
        for (int k3 = 0; k3 < this.f13051v.length; k3++) {
            this.f13051v[k3] = new double[dimension];
            System.arraycopy(interpolator.f13051v[k3], 0, this.f13051v[k3], 0, dimension);
        }
        this.vectorsInitialized = interpolator.vectorsInitialized;
    }

    @Override // org.apache.commons.math3.ode.sampling.AbstractStepInterpolator
    protected StepInterpolator doCopy() {
        return new DormandPrince853StepInterpolator(this);
    }

    /* JADX WARN: Type inference failed for: r1v2, types: [double[], double[][]] */
    /* JADX WARN: Type inference failed for: r1v7, types: [double[], double[][]] */
    @Override // org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator
    public void reinitialize(AbstractIntegrator integrator, double[] y2, double[][] yDotK, boolean forward, EquationsMapper primaryMapper, EquationsMapper[] secondaryMappers) {
        super.reinitialize(integrator, y2, yDotK, forward, primaryMapper, secondaryMappers);
        int dimension = this.currentState.length;
        this.yDotKLast = new double[3];
        for (int k2 = 0; k2 < this.yDotKLast.length; k2++) {
            this.yDotKLast[k2] = new double[dimension];
        }
        this.f13051v = new double[7];
        for (int k3 = 0; k3 < this.f13051v.length; k3++) {
            this.f13051v[k3] = new double[dimension];
        }
        this.vectorsInitialized = false;
    }

    @Override // org.apache.commons.math3.ode.sampling.AbstractStepInterpolator
    public void storeTime(double t2) {
        super.storeTime(t2);
        this.vectorsInitialized = false;
    }

    /* JADX WARN: Type inference failed for: r1v66, types: [double[], double[][]] */
    @Override // org.apache.commons.math3.ode.sampling.AbstractStepInterpolator
    protected void computeInterpolatedStateAndDerivatives(double theta, double oneMinusThetaH) throws MaxCountExceededException {
        if (!this.vectorsInitialized) {
            if (this.f13051v == null) {
                this.f13051v = new double[7];
                for (int k2 = 0; k2 < 7; k2++) {
                    this.f13051v[k2] = new double[this.interpolatedState.length];
                }
            }
            finalizeStep();
            for (int i2 = 0; i2 < this.interpolatedState.length; i2++) {
                double yDot1 = this.yDotK[0][i2];
                double yDot6 = this.yDotK[5][i2];
                double yDot7 = this.yDotK[6][i2];
                double yDot8 = this.yDotK[7][i2];
                double yDot9 = this.yDotK[8][i2];
                double yDot10 = this.yDotK[9][i2];
                double yDot11 = this.yDotK[10][i2];
                double yDot12 = this.yDotK[11][i2];
                double yDot13 = this.yDotK[12][i2];
                double yDot14 = this.yDotKLast[0][i2];
                double yDot15 = this.yDotKLast[1][i2];
                double yDot16 = this.yDotKLast[2][i2];
                this.f13051v[0][i2] = (B_01 * yDot1) + (B_06 * yDot6) + (B_07 * yDot7) + (B_08 * yDot8) + (B_09 * yDot9) + (B_10 * yDot10) + (B_11 * yDot11) + (B_12 * yDot12);
                this.f13051v[1][i2] = yDot1 - this.f13051v[0][i2];
                this.f13051v[2][i2] = (this.f13051v[0][i2] - this.f13051v[1][i2]) - this.yDotK[12][i2];
                for (int k3 = 0; k3 < f13050D.length; k3++) {
                    this.f13051v[k3 + 3][i2] = (f13050D[k3][0] * yDot1) + (f13050D[k3][1] * yDot6) + (f13050D[k3][2] * yDot7) + (f13050D[k3][3] * yDot8) + (f13050D[k3][4] * yDot9) + (f13050D[k3][5] * yDot10) + (f13050D[k3][6] * yDot11) + (f13050D[k3][7] * yDot12) + (f13050D[k3][8] * yDot13) + (f13050D[k3][9] * yDot14) + (f13050D[k3][10] * yDot15) + (f13050D[k3][11] * yDot16);
                }
            }
            this.vectorsInitialized = true;
        }
        double eta = 1.0d - theta;
        double twoTheta = 2.0d * theta;
        double theta2 = theta * theta;
        double dot1 = 1.0d - twoTheta;
        double dot2 = theta * (2.0d - (3.0d * theta));
        double dot3 = twoTheta * (1.0d + (theta * (twoTheta - 3.0d)));
        double dot4 = theta2 * (3.0d + (theta * ((5.0d * theta) - 8.0d)));
        double dot5 = theta2 * (3.0d + (theta * ((-12.0d) + (theta * (15.0d - (6.0d * theta))))));
        double dot6 = theta2 * theta * (4.0d + (theta * ((-15.0d) + (theta * (18.0d - (7.0d * theta))))));
        if (this.previousState != null && theta <= 0.5d) {
            for (int i3 = 0; i3 < this.interpolatedState.length; i3++) {
                this.interpolatedState[i3] = this.previousState[i3] + (theta * this.f13067h * (this.f13051v[0][i3] + (eta * (this.f13051v[1][i3] + (theta * (this.f13051v[2][i3] + (eta * (this.f13051v[3][i3] + (theta * (this.f13051v[4][i3] + (eta * (this.f13051v[5][i3] + (theta * this.f13051v[6][i3])))))))))))));
                this.interpolatedDerivatives[i3] = this.f13051v[0][i3] + (dot1 * this.f13051v[1][i3]) + (dot2 * this.f13051v[2][i3]) + (dot3 * this.f13051v[3][i3]) + (dot4 * this.f13051v[4][i3]) + (dot5 * this.f13051v[5][i3]) + (dot6 * this.f13051v[6][i3]);
            }
            return;
        }
        for (int i4 = 0; i4 < this.interpolatedState.length; i4++) {
            this.interpolatedState[i4] = this.currentState[i4] - (oneMinusThetaH * (this.f13051v[0][i4] - (theta * (this.f13051v[1][i4] + (theta * (this.f13051v[2][i4] + (eta * (this.f13051v[3][i4] + (theta * (this.f13051v[4][i4] + (eta * (this.f13051v[5][i4] + (theta * this.f13051v[6][i4])))))))))))));
            this.interpolatedDerivatives[i4] = this.f13051v[0][i4] + (dot1 * this.f13051v[1][i4]) + (dot2 * this.f13051v[2][i4]) + (dot3 * this.f13051v[3][i4]) + (dot4 * this.f13051v[4][i4]) + (dot5 * this.f13051v[5][i4]) + (dot6 * this.f13051v[6][i4]);
        }
    }

    @Override // org.apache.commons.math3.ode.sampling.AbstractStepInterpolator
    protected void doFinalize() throws MaxCountExceededException, DimensionMismatchException, NullPointerException {
        if (this.currentState == null) {
            return;
        }
        double[] yTmp = new double[this.currentState.length];
        double pT = getGlobalPreviousTime();
        for (int j2 = 0; j2 < this.currentState.length; j2++) {
            double s2 = (K14_01 * this.yDotK[0][j2]) + (K14_06 * this.yDotK[5][j2]) + (K14_07 * this.yDotK[6][j2]) + (K14_08 * this.yDotK[7][j2]) + (K14_09 * this.yDotK[8][j2]) + (K14_10 * this.yDotK[9][j2]) + (K14_11 * this.yDotK[10][j2]) + (K14_12 * this.yDotK[11][j2]) + (K14_13 * this.yDotK[12][j2]);
            yTmp[j2] = this.currentState[j2] + (this.f13067h * s2);
        }
        this.integrator.computeDerivatives(pT + (0.1d * this.f13067h), yTmp, this.yDotKLast[0]);
        for (int j3 = 0; j3 < this.currentState.length; j3++) {
            double s3 = (K15_01 * this.yDotK[0][j3]) + (K15_06 * this.yDotK[5][j3]) + (K15_07 * this.yDotK[6][j3]) + (K15_08 * this.yDotK[7][j3]) + (K15_09 * this.yDotK[8][j3]) + (0.1521609496625161d * this.yDotK[9][j3]) + (K15_11 * this.yDotK[10][j3]) + (K15_12 * this.yDotK[11][j3]) + (K15_13 * this.yDotK[12][j3]) + (K15_14 * this.yDotKLast[0][j3]);
            yTmp[j3] = this.currentState[j3] + (this.f13067h * s3);
        }
        this.integrator.computeDerivatives(pT + (C15 * this.f13067h), yTmp, this.yDotKLast[1]);
        for (int j4 = 0; j4 < this.currentState.length; j4++) {
            double s4 = (K16_01 * this.yDotK[0][j4]) + (K16_06 * this.yDotK[5][j4]) + (K16_07 * this.yDotK[6][j4]) + (K16_08 * this.yDotK[7][j4]) + (K16_09 * this.yDotK[8][j4]) + (0.1521609496625161d * this.yDotK[9][j4]) + (K16_11 * this.yDotK[10][j4]) + (K16_12 * this.yDotK[11][j4]) + (K16_13 * this.yDotK[12][j4]) + (K16_14 * this.yDotKLast[0][j4]) + (K16_15 * this.yDotKLast[1][j4]);
            yTmp[j4] = this.currentState[j4] + (this.f13067h * s4);
        }
        this.integrator.computeDerivatives(pT + (C16 * this.f13067h), yTmp, this.yDotKLast[2]);
    }

    @Override // org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator, org.apache.commons.math3.ode.sampling.AbstractStepInterpolator, java.io.Externalizable
    public void writeExternal(ObjectOutput out) throws IOException {
        try {
            finalizeStep();
            int dimension = this.currentState == null ? -1 : this.currentState.length;
            out.writeInt(dimension);
            for (int i2 = 0; i2 < dimension; i2++) {
                out.writeDouble(this.yDotKLast[0][i2]);
                out.writeDouble(this.yDotKLast[1][i2]);
                out.writeDouble(this.yDotKLast[2][i2]);
            }
            super.writeExternal(out);
        } catch (MaxCountExceededException mcee) {
            IOException ioe = new IOException(mcee.getLocalizedMessage());
            ioe.initCause(mcee);
            throw ioe;
        }
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [double[], double[][]] */
    @Override // org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator, org.apache.commons.math3.ode.sampling.AbstractStepInterpolator, java.io.Externalizable
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.yDotKLast = new double[3];
        int dimension = in.readInt();
        this.yDotKLast[0] = dimension < 0 ? null : new double[dimension];
        this.yDotKLast[1] = dimension < 0 ? null : new double[dimension];
        this.yDotKLast[2] = dimension < 0 ? null : new double[dimension];
        for (int i2 = 0; i2 < dimension; i2++) {
            this.yDotKLast[0][i2] = in.readDouble();
            this.yDotKLast[1][i2] = in.readDouble();
            this.yDotKLast[2][i2] = in.readDouble();
        }
        super.readExternal(in);
    }
}
