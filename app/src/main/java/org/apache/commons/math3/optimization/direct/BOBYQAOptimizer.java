package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.MultivariateOptimizer;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.util.FastMath;

@Deprecated
/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optimization/direct/BOBYQAOptimizer.class */
public class BOBYQAOptimizer extends BaseAbstractMultivariateSimpleBoundsOptimizer<MultivariateFunction> implements MultivariateOptimizer {
    public static final int MINIMUM_PROBLEM_DIMENSION = 2;
    public static final double DEFAULT_INITIAL_RADIUS = 10.0d;
    public static final double DEFAULT_STOPPING_RADIUS = 1.0E-8d;
    private static final double ZERO = 0.0d;
    private static final double ONE = 1.0d;
    private static final double TWO = 2.0d;
    private static final double TEN = 10.0d;
    private static final double SIXTEEN = 16.0d;
    private static final double TWO_HUNDRED_FIFTY = 250.0d;
    private static final double MINUS_ONE = -1.0d;
    private static final double HALF = 0.5d;
    private static final double ONE_OVER_FOUR = 0.25d;
    private static final double ONE_OVER_EIGHT = 0.125d;
    private static final double ONE_OVER_TEN = 0.1d;
    private static final double ONE_OVER_A_THOUSAND = 0.001d;
    private final int numberOfInterpolationPoints;
    private double initialTrustRegionRadius;
    private final double stoppingTrustRegionRadius;
    private boolean isMinimize;
    private ArrayRealVector currentBest;
    private double[] boundDifference;
    private int trustRegionCenterInterpolationPointIndex;
    private Array2DRowRealMatrix bMatrix;
    private Array2DRowRealMatrix zMatrix;
    private Array2DRowRealMatrix interpolationPoints;
    private ArrayRealVector originShift;
    private ArrayRealVector fAtInterpolationPoints;
    private ArrayRealVector trustRegionCenterOffset;
    private ArrayRealVector gradientAtTrustRegionCenter;
    private ArrayRealVector lowerDifference;
    private ArrayRealVector upperDifference;
    private ArrayRealVector modelSecondDerivativesParameters;
    private ArrayRealVector newPoint;
    private ArrayRealVector alternativeNewPoint;
    private ArrayRealVector trialStepPoint;
    private ArrayRealVector lagrangeValuesAtNewPoint;
    private ArrayRealVector modelSecondDerivativesValues;

    public BOBYQAOptimizer(int numberOfInterpolationPoints) {
        this(numberOfInterpolationPoints, 10.0d, 1.0E-8d);
    }

    public BOBYQAOptimizer(int numberOfInterpolationPoints, double initialTrustRegionRadius, double stoppingTrustRegionRadius) {
        super(null);
        this.numberOfInterpolationPoints = numberOfInterpolationPoints;
        this.initialTrustRegionRadius = initialTrustRegionRadius;
        this.stoppingTrustRegionRadius = stoppingTrustRegionRadius;
    }

    @Override // org.apache.commons.math3.optimization.direct.BaseAbstractMultivariateOptimizer
    protected PointValuePair doOptimize() throws OutOfRangeException {
        double[] lowerBound = getLowerBound();
        double[] upperBound = getUpperBound();
        setup(lowerBound, upperBound);
        this.isMinimize = getGoalType() == GoalType.MINIMIZE;
        this.currentBest = new ArrayRealVector(getStartPoint());
        double value = bobyqa(lowerBound, upperBound);
        return new PointValuePair(this.currentBest.getDataRef(), this.isMinimize ? value : -value);
    }

    private double bobyqa(double[] lowerBound, double[] upperBound) throws OutOfRangeException {
        printMethod();
        int n2 = this.currentBest.getDimension();
        for (int j2 = 0; j2 < n2; j2++) {
            double boundDiff = this.boundDifference[j2];
            this.lowerDifference.setEntry(j2, lowerBound[j2] - this.currentBest.getEntry(j2));
            this.upperDifference.setEntry(j2, upperBound[j2] - this.currentBest.getEntry(j2));
            if (this.lowerDifference.getEntry(j2) >= (-this.initialTrustRegionRadius)) {
                if (this.lowerDifference.getEntry(j2) >= 0.0d) {
                    this.currentBest.setEntry(j2, lowerBound[j2]);
                    this.lowerDifference.setEntry(j2, 0.0d);
                    this.upperDifference.setEntry(j2, boundDiff);
                } else {
                    this.currentBest.setEntry(j2, lowerBound[j2] + this.initialTrustRegionRadius);
                    this.lowerDifference.setEntry(j2, -this.initialTrustRegionRadius);
                    double deltaOne = upperBound[j2] - this.currentBest.getEntry(j2);
                    this.upperDifference.setEntry(j2, FastMath.max(deltaOne, this.initialTrustRegionRadius));
                }
            } else if (this.upperDifference.getEntry(j2) <= this.initialTrustRegionRadius) {
                if (this.upperDifference.getEntry(j2) <= 0.0d) {
                    this.currentBest.setEntry(j2, upperBound[j2]);
                    this.lowerDifference.setEntry(j2, -boundDiff);
                    this.upperDifference.setEntry(j2, 0.0d);
                } else {
                    this.currentBest.setEntry(j2, upperBound[j2] - this.initialTrustRegionRadius);
                    double deltaOne2 = lowerBound[j2] - this.currentBest.getEntry(j2);
                    double deltaTwo = -this.initialTrustRegionRadius;
                    this.lowerDifference.setEntry(j2, FastMath.min(deltaOne2, deltaTwo));
                    this.upperDifference.setEntry(j2, this.initialTrustRegionRadius);
                }
            }
        }
        return bobyqb(lowerBound, upperBound);
    }

    /* JADX WARN: Removed duplicated region for block: B:141:0x0818  */
    /* JADX WARN: Removed duplicated region for block: B:149:0x08b8  */
    /* JADX WARN: Removed duplicated region for block: B:161:0x0936  */
    /* JADX WARN: Removed duplicated region for block: B:172:0x0a2e  */
    /* JADX WARN: Removed duplicated region for block: B:204:0x0b96  */
    /* JADX WARN: Removed duplicated region for block: B:213:0x0c30  */
    /* JADX WARN: Removed duplicated region for block: B:405:0x1474  */
    /* JADX WARN: Removed duplicated region for block: B:469:0x03e7 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:475:0x02c0 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:476:0x07b9 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:477:0x07b1 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:480:0x0aa0 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:484:0x0c47 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:485:0x0c3b A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:491:0x1517 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:492:0x14c2 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:499:0x15b3 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:500:0x1557 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:69:0x03fb  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private double bobyqb(double[] r12, double[] r13) throws org.apache.commons.math3.exception.OutOfRangeException, org.apache.commons.math3.exception.DimensionMismatchException {
        /*
            Method dump skipped, instructions count: 5771
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.math3.optimization.direct.BOBYQAOptimizer.bobyqb(double[], double[]):double");
    }

    private double[] altmov(int knew, double adelt) throws OutOfRangeException {
        double cauchy;
        double vlag;
        printMethod();
        int n2 = this.currentBest.getDimension();
        int npt = this.numberOfInterpolationPoints;
        ArrayRealVector glag = new ArrayRealVector(n2);
        ArrayRealVector hcol = new ArrayRealVector(npt);
        ArrayRealVector work1 = new ArrayRealVector(n2);
        ArrayRealVector work2 = new ArrayRealVector(n2);
        for (int k2 = 0; k2 < npt; k2++) {
            hcol.setEntry(k2, 0.0d);
        }
        int max = (npt - n2) - 1;
        for (int j2 = 0; j2 < max; j2++) {
            double tmp = this.zMatrix.getEntry(knew, j2);
            for (int k3 = 0; k3 < npt; k3++) {
                hcol.setEntry(k3, hcol.getEntry(k3) + (tmp * this.zMatrix.getEntry(k3, j2)));
            }
        }
        double alpha = hcol.getEntry(knew);
        double ha = 0.5d * alpha;
        for (int i2 = 0; i2 < n2; i2++) {
            glag.setEntry(i2, this.bMatrix.getEntry(knew, i2));
        }
        for (int k4 = 0; k4 < npt; k4++) {
            double tmp2 = 0.0d;
            for (int j3 = 0; j3 < n2; j3++) {
                tmp2 += this.interpolationPoints.getEntry(k4, j3) * this.trustRegionCenterOffset.getEntry(j3);
            }
            double tmp3 = tmp2 * hcol.getEntry(k4);
            for (int i3 = 0; i3 < n2; i3++) {
                glag.setEntry(i3, glag.getEntry(i3) + (tmp3 * this.interpolationPoints.getEntry(k4, i3)));
            }
        }
        double presav = 0.0d;
        double step = Double.NaN;
        int ksav = 0;
        int ibdsav = 0;
        double stpsav = 0.0d;
        for (int k5 = 0; k5 < npt; k5++) {
            if (k5 != this.trustRegionCenterInterpolationPointIndex) {
                double dderiv = 0.0d;
                double distsq = 0.0d;
                for (int i4 = 0; i4 < n2; i4++) {
                    double tmp4 = this.interpolationPoints.getEntry(k5, i4) - this.trustRegionCenterOffset.getEntry(i4);
                    dderiv += glag.getEntry(i4) * tmp4;
                    distsq += tmp4 * tmp4;
                }
                double subd = adelt / FastMath.sqrt(distsq);
                double slbd = -subd;
                int ilbd = 0;
                int iubd = 0;
                double sumin = FastMath.min(1.0d, subd);
                for (int i5 = 0; i5 < n2; i5++) {
                    double tmp5 = this.interpolationPoints.getEntry(k5, i5) - this.trustRegionCenterOffset.getEntry(i5);
                    if (tmp5 > 0.0d) {
                        if (slbd * tmp5 < this.lowerDifference.getEntry(i5) - this.trustRegionCenterOffset.getEntry(i5)) {
                            slbd = (this.lowerDifference.getEntry(i5) - this.trustRegionCenterOffset.getEntry(i5)) / tmp5;
                            ilbd = (-i5) - 1;
                        }
                        if (subd * tmp5 > this.upperDifference.getEntry(i5) - this.trustRegionCenterOffset.getEntry(i5)) {
                            subd = FastMath.max(sumin, (this.upperDifference.getEntry(i5) - this.trustRegionCenterOffset.getEntry(i5)) / tmp5);
                            iubd = i5 + 1;
                        }
                    } else if (tmp5 < 0.0d) {
                        if (slbd * tmp5 > this.upperDifference.getEntry(i5) - this.trustRegionCenterOffset.getEntry(i5)) {
                            slbd = (this.upperDifference.getEntry(i5) - this.trustRegionCenterOffset.getEntry(i5)) / tmp5;
                            ilbd = i5 + 1;
                        }
                        if (subd * tmp5 < this.lowerDifference.getEntry(i5) - this.trustRegionCenterOffset.getEntry(i5)) {
                            subd = FastMath.max(sumin, (this.lowerDifference.getEntry(i5) - this.trustRegionCenterOffset.getEntry(i5)) / tmp5);
                            iubd = (-i5) - 1;
                        }
                    }
                }
                step = slbd;
                int isbd = ilbd;
                if (k5 == knew) {
                    double diff = dderiv - 1.0d;
                    vlag = slbd * (dderiv - (slbd * diff));
                    double d1 = subd * (dderiv - (subd * diff));
                    if (FastMath.abs(d1) > FastMath.abs(vlag)) {
                        step = subd;
                        vlag = d1;
                        isbd = iubd;
                    }
                    double d2 = 0.5d * dderiv;
                    double d3 = d2 - (diff * slbd);
                    double d4 = d2 - (diff * subd);
                    if (d3 * d4 < 0.0d) {
                        double d5 = (d2 * d2) / diff;
                        if (FastMath.abs(d5) > FastMath.abs(vlag)) {
                            step = d2 / diff;
                            vlag = d5;
                            isbd = 0;
                        }
                    }
                } else {
                    double vlag2 = slbd * (1.0d - slbd);
                    double tmp6 = subd * (1.0d - subd);
                    if (FastMath.abs(tmp6) > FastMath.abs(vlag2)) {
                        step = subd;
                        vlag2 = tmp6;
                        isbd = iubd;
                    }
                    if (subd > 0.5d && FastMath.abs(vlag2) < ONE_OVER_FOUR) {
                        step = 0.5d;
                        vlag2 = 0.25d;
                        isbd = 0;
                    }
                    vlag = vlag2 * dderiv;
                }
                double tmp7 = step * (1.0d - step) * distsq;
                double predsq = vlag * vlag * ((vlag * vlag) + (ha * tmp7 * tmp7));
                if (predsq > presav) {
                    presav = predsq;
                    ksav = k5;
                    stpsav = step;
                    ibdsav = isbd;
                }
            }
        }
        for (int i6 = 0; i6 < n2; i6++) {
            double tmp8 = this.trustRegionCenterOffset.getEntry(i6) + (stpsav * (this.interpolationPoints.getEntry(ksav, i6) - this.trustRegionCenterOffset.getEntry(i6)));
            this.newPoint.setEntry(i6, FastMath.max(this.lowerDifference.getEntry(i6), FastMath.min(this.upperDifference.getEntry(i6), tmp8)));
        }
        if (ibdsav < 0) {
            this.newPoint.setEntry((-ibdsav) - 1, this.lowerDifference.getEntry((-ibdsav) - 1));
        }
        if (ibdsav > 0) {
            this.newPoint.setEntry(ibdsav - 1, this.upperDifference.getEntry(ibdsav - 1));
        }
        double bigstp = adelt + adelt;
        int iflag = 0;
        double csave = 0.0d;
        while (true) {
            double wfixsq = 0.0d;
            double ggfree = 0.0d;
            for (int i7 = 0; i7 < n2; i7++) {
                double glagValue = glag.getEntry(i7);
                work1.setEntry(i7, 0.0d);
                if (FastMath.min(this.trustRegionCenterOffset.getEntry(i7) - this.lowerDifference.getEntry(i7), glagValue) > 0.0d || FastMath.max(this.trustRegionCenterOffset.getEntry(i7) - this.upperDifference.getEntry(i7), glagValue) < 0.0d) {
                    work1.setEntry(i7, bigstp);
                    ggfree += glagValue * glagValue;
                }
            }
            if (ggfree != 0.0d) {
                double tmp1 = (adelt * adelt) - 0.0d;
                if (tmp1 > 0.0d) {
                    step = FastMath.sqrt(tmp1 / ggfree);
                    double ggfree2 = 0.0d;
                    for (int i8 = 0; i8 < n2; i8++) {
                        if (work1.getEntry(i8) == bigstp) {
                            double tmp22 = this.trustRegionCenterOffset.getEntry(i8) - (step * glag.getEntry(i8));
                            if (tmp22 <= this.lowerDifference.getEntry(i8)) {
                                work1.setEntry(i8, this.lowerDifference.getEntry(i8) - this.trustRegionCenterOffset.getEntry(i8));
                                double d12 = work1.getEntry(i8);
                                wfixsq += d12 * d12;
                            } else if (tmp22 >= this.upperDifference.getEntry(i8)) {
                                work1.setEntry(i8, this.upperDifference.getEntry(i8) - this.trustRegionCenterOffset.getEntry(i8));
                                double d13 = work1.getEntry(i8);
                                wfixsq += d13 * d13;
                            } else {
                                double d14 = glag.getEntry(i8);
                                ggfree2 += d14 * d14;
                            }
                        }
                    }
                }
                double gw = 0.0d;
                for (int i9 = 0; i9 < n2; i9++) {
                    double glagValue2 = glag.getEntry(i9);
                    if (work1.getEntry(i9) == bigstp) {
                        work1.setEntry(i9, (-step) * glagValue2);
                        double min = FastMath.min(this.upperDifference.getEntry(i9), this.trustRegionCenterOffset.getEntry(i9) + work1.getEntry(i9));
                        this.alternativeNewPoint.setEntry(i9, FastMath.max(this.lowerDifference.getEntry(i9), min));
                    } else if (work1.getEntry(i9) == 0.0d) {
                        this.alternativeNewPoint.setEntry(i9, this.trustRegionCenterOffset.getEntry(i9));
                    } else if (glagValue2 > 0.0d) {
                        this.alternativeNewPoint.setEntry(i9, this.lowerDifference.getEntry(i9));
                    } else {
                        this.alternativeNewPoint.setEntry(i9, this.upperDifference.getEntry(i9));
                    }
                    gw += glagValue2 * work1.getEntry(i9);
                }
                double curv = 0.0d;
                for (int k6 = 0; k6 < npt; k6++) {
                    double tmp9 = 0.0d;
                    for (int j4 = 0; j4 < n2; j4++) {
                        tmp9 += this.interpolationPoints.getEntry(k6, j4) * work1.getEntry(j4);
                    }
                    curv += hcol.getEntry(k6) * tmp9 * tmp9;
                }
                if (iflag == 1) {
                    curv = -curv;
                }
                if (curv > (-gw) && curv < (-gw) * (1.0d + FastMath.sqrt(2.0d))) {
                    double scale = (-gw) / curv;
                    for (int i10 = 0; i10 < n2; i10++) {
                        double tmp10 = this.trustRegionCenterOffset.getEntry(i10) + (scale * work1.getEntry(i10));
                        this.alternativeNewPoint.setEntry(i10, FastMath.max(this.lowerDifference.getEntry(i10), FastMath.min(this.upperDifference.getEntry(i10), tmp10)));
                    }
                    double d15 = 0.5d * gw * scale;
                    cauchy = d15 * d15;
                } else {
                    double d16 = gw + (0.5d * curv);
                    cauchy = d16 * d16;
                }
                if (iflag == 0) {
                    for (int i11 = 0; i11 < n2; i11++) {
                        glag.setEntry(i11, -glag.getEntry(i11));
                        work2.setEntry(i11, this.alternativeNewPoint.getEntry(i11));
                    }
                    csave = cauchy;
                    iflag = 1;
                } else {
                    if (csave > cauchy) {
                        for (int i12 = 0; i12 < n2; i12++) {
                            this.alternativeNewPoint.setEntry(i12, work2.getEntry(i12));
                        }
                        cauchy = csave;
                    }
                    return new double[]{alpha, cauchy};
                }
            } else {
                return new double[]{alpha, 0.0d};
            }
        }
    }

    private void prelim(double[] lowerBound, double[] upperBound) throws OutOfRangeException {
        printMethod();
        int n2 = this.currentBest.getDimension();
        int npt = this.numberOfInterpolationPoints;
        int ndim = this.bMatrix.getRowDimension();
        double rhosq = this.initialTrustRegionRadius * this.initialTrustRegionRadius;
        double recip = 1.0d / rhosq;
        int np = n2 + 1;
        for (int j2 = 0; j2 < n2; j2++) {
            this.originShift.setEntry(j2, this.currentBest.getEntry(j2));
            for (int k2 = 0; k2 < npt; k2++) {
                this.interpolationPoints.setEntry(k2, j2, 0.0d);
            }
            for (int i2 = 0; i2 < ndim; i2++) {
                this.bMatrix.setEntry(i2, j2, 0.0d);
            }
        }
        int max = (n2 * np) / 2;
        for (int i3 = 0; i3 < max; i3++) {
            this.modelSecondDerivativesValues.setEntry(i3, 0.0d);
        }
        for (int k3 = 0; k3 < npt; k3++) {
            this.modelSecondDerivativesParameters.setEntry(k3, 0.0d);
            int max2 = npt - np;
            for (int j3 = 0; j3 < max2; j3++) {
                this.zMatrix.setEntry(k3, j3, 0.0d);
            }
        }
        int ipt = 0;
        int jpt = 0;
        double fbeg = Double.NaN;
        do {
            int nfm = getEvaluations();
            int nfx = nfm - n2;
            int nfmm = nfm - 1;
            int nfxm = nfx - 1;
            double stepa = 0.0d;
            double stepb = 0.0d;
            if (nfm <= 2 * n2) {
                if (nfm >= 1 && nfm <= n2) {
                    stepa = this.initialTrustRegionRadius;
                    if (this.upperDifference.getEntry(nfmm) == 0.0d) {
                        stepa = -stepa;
                    }
                    this.interpolationPoints.setEntry(nfm, nfmm, stepa);
                } else if (nfm > n2) {
                    stepa = this.interpolationPoints.getEntry(nfx, nfxm);
                    stepb = -this.initialTrustRegionRadius;
                    if (this.lowerDifference.getEntry(nfxm) == 0.0d) {
                        stepb = FastMath.min(2.0d * this.initialTrustRegionRadius, this.upperDifference.getEntry(nfxm));
                    }
                    if (this.upperDifference.getEntry(nfxm) == 0.0d) {
                        stepb = FastMath.max((-2.0d) * this.initialTrustRegionRadius, this.lowerDifference.getEntry(nfxm));
                    }
                    this.interpolationPoints.setEntry(nfm, nfxm, stepb);
                }
            } else {
                int tmp1 = (nfm - np) / n2;
                jpt = (nfm - (tmp1 * n2)) - n2;
                ipt = jpt + tmp1;
                if (ipt > n2) {
                    jpt = ipt - n2;
                    ipt = jpt;
                }
                int iptMinus1 = ipt - 1;
                int jptMinus1 = jpt - 1;
                this.interpolationPoints.setEntry(nfm, iptMinus1, this.interpolationPoints.getEntry(ipt, iptMinus1));
                this.interpolationPoints.setEntry(nfm, jptMinus1, this.interpolationPoints.getEntry(jpt, jptMinus1));
            }
            for (int j4 = 0; j4 < n2; j4++) {
                this.currentBest.setEntry(j4, FastMath.min(FastMath.max(lowerBound[j4], this.originShift.getEntry(j4) + this.interpolationPoints.getEntry(nfm, j4)), upperBound[j4]));
                if (this.interpolationPoints.getEntry(nfm, j4) == this.lowerDifference.getEntry(j4)) {
                    this.currentBest.setEntry(j4, lowerBound[j4]);
                }
                if (this.interpolationPoints.getEntry(nfm, j4) == this.upperDifference.getEntry(j4)) {
                    this.currentBest.setEntry(j4, upperBound[j4]);
                }
            }
            double objectiveValue = computeObjectiveValue(this.currentBest.toArray());
            double f2 = this.isMinimize ? objectiveValue : -objectiveValue;
            int numEval = getEvaluations();
            this.fAtInterpolationPoints.setEntry(nfm, f2);
            if (numEval == 1) {
                fbeg = f2;
                this.trustRegionCenterInterpolationPointIndex = 0;
            } else if (f2 < this.fAtInterpolationPoints.getEntry(this.trustRegionCenterInterpolationPointIndex)) {
                this.trustRegionCenterInterpolationPointIndex = nfm;
            }
            if (numEval <= (2 * n2) + 1) {
                if (numEval >= 2 && numEval <= n2 + 1) {
                    this.gradientAtTrustRegionCenter.setEntry(nfmm, (f2 - fbeg) / stepa);
                    if (npt < numEval + n2) {
                        double oneOverStepA = 1.0d / stepa;
                        this.bMatrix.setEntry(0, nfmm, -oneOverStepA);
                        this.bMatrix.setEntry(nfm, nfmm, oneOverStepA);
                        this.bMatrix.setEntry(npt + nfmm, nfmm, (-0.5d) * rhosq);
                    }
                } else if (numEval >= n2 + 2) {
                    int ih = ((nfx * (nfx + 1)) / 2) - 1;
                    double tmp = (f2 - fbeg) / stepb;
                    double diff = stepb - stepa;
                    this.modelSecondDerivativesValues.setEntry(ih, (2.0d * (tmp - this.gradientAtTrustRegionCenter.getEntry(nfxm))) / diff);
                    this.gradientAtTrustRegionCenter.setEntry(nfxm, ((this.gradientAtTrustRegionCenter.getEntry(nfxm) * stepb) - (tmp * stepa)) / diff);
                    if (stepa * stepb < 0.0d && f2 < this.fAtInterpolationPoints.getEntry(nfm - n2)) {
                        this.fAtInterpolationPoints.setEntry(nfm, this.fAtInterpolationPoints.getEntry(nfm - n2));
                        this.fAtInterpolationPoints.setEntry(nfm - n2, f2);
                        if (this.trustRegionCenterInterpolationPointIndex == nfm) {
                            this.trustRegionCenterInterpolationPointIndex = nfm - n2;
                        }
                        this.interpolationPoints.setEntry(nfm - n2, nfxm, stepb);
                        this.interpolationPoints.setEntry(nfm, nfxm, stepa);
                    }
                    this.bMatrix.setEntry(0, nfxm, (-(stepa + stepb)) / (stepa * stepb));
                    this.bMatrix.setEntry(nfm, nfxm, (-0.5d) / this.interpolationPoints.getEntry(nfm - n2, nfxm));
                    this.bMatrix.setEntry(nfm - n2, nfxm, (-this.bMatrix.getEntry(0, nfxm)) - this.bMatrix.getEntry(nfm, nfxm));
                    this.zMatrix.setEntry(0, nfxm, FastMath.sqrt(2.0d) / (stepa * stepb));
                    this.zMatrix.setEntry(nfm, nfxm, FastMath.sqrt(0.5d) / rhosq);
                    this.zMatrix.setEntry(nfm - n2, nfxm, (-this.zMatrix.getEntry(0, nfxm)) - this.zMatrix.getEntry(nfm, nfxm));
                }
            } else {
                this.zMatrix.setEntry(0, nfxm, recip);
                this.zMatrix.setEntry(nfm, nfxm, recip);
                this.zMatrix.setEntry(ipt, nfxm, -recip);
                this.zMatrix.setEntry(jpt, nfxm, -recip);
                int ih2 = (((ipt * (ipt - 1)) / 2) + jpt) - 1;
                this.modelSecondDerivativesValues.setEntry(ih2, (((fbeg - this.fAtInterpolationPoints.getEntry(ipt)) - this.fAtInterpolationPoints.getEntry(jpt)) + f2) / (this.interpolationPoints.getEntry(nfm, ipt - 1) * this.interpolationPoints.getEntry(nfm, jpt - 1)));
            }
        } while (getEvaluations() < npt);
    }

    /* JADX WARN: Removed duplicated region for block: B:246:0x01fd A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:255:0x01f5 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:26:0x018e  */
    /* JADX WARN: Removed duplicated region for block: B:272:0x04d8 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:273:0x04d0 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private double[] trsbox(double r11, org.apache.commons.math3.linear.ArrayRealVector r13, org.apache.commons.math3.linear.ArrayRealVector r14, org.apache.commons.math3.linear.ArrayRealVector r15, org.apache.commons.math3.linear.ArrayRealVector r16, org.apache.commons.math3.linear.ArrayRealVector r17) throws org.apache.commons.math3.exception.OutOfRangeException, org.apache.commons.math3.exception.DimensionMismatchException {
        /*
            Method dump skipped, instructions count: 2990
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.math3.optimization.direct.BOBYQAOptimizer.trsbox(double, org.apache.commons.math3.linear.ArrayRealVector, org.apache.commons.math3.linear.ArrayRealVector, org.apache.commons.math3.linear.ArrayRealVector, org.apache.commons.math3.linear.ArrayRealVector, org.apache.commons.math3.linear.ArrayRealVector):double[]");
    }

    private void update(double beta, double denom, int knew) throws OutOfRangeException {
        printMethod();
        int n2 = this.currentBest.getDimension();
        int npt = this.numberOfInterpolationPoints;
        int nptm = (npt - n2) - 1;
        ArrayRealVector work = new ArrayRealVector(npt + n2);
        double ztest = 0.0d;
        for (int k2 = 0; k2 < npt; k2++) {
            for (int j2 = 0; j2 < nptm; j2++) {
                ztest = FastMath.max(ztest, FastMath.abs(this.zMatrix.getEntry(k2, j2)));
            }
        }
        double ztest2 = ztest * 1.0E-20d;
        for (int j3 = 1; j3 < nptm; j3++) {
            double d1 = this.zMatrix.getEntry(knew, j3);
            if (FastMath.abs(d1) > ztest2) {
                double d2 = this.zMatrix.getEntry(knew, 0);
                double d3 = this.zMatrix.getEntry(knew, j3);
                double d4 = FastMath.sqrt((d2 * d2) + (d3 * d3));
                double d5 = this.zMatrix.getEntry(knew, 0) / d4;
                double d6 = this.zMatrix.getEntry(knew, j3) / d4;
                for (int i2 = 0; i2 < npt; i2++) {
                    double d7 = (d5 * this.zMatrix.getEntry(i2, 0)) + (d6 * this.zMatrix.getEntry(i2, j3));
                    this.zMatrix.setEntry(i2, j3, (d5 * this.zMatrix.getEntry(i2, j3)) - (d6 * this.zMatrix.getEntry(i2, 0)));
                    this.zMatrix.setEntry(i2, 0, d7);
                }
            }
            this.zMatrix.setEntry(knew, j3, 0.0d);
        }
        for (int i3 = 0; i3 < npt; i3++) {
            work.setEntry(i3, this.zMatrix.getEntry(knew, 0) * this.zMatrix.getEntry(i3, 0));
        }
        double alpha = work.getEntry(knew);
        double tau = this.lagrangeValuesAtNewPoint.getEntry(knew);
        this.lagrangeValuesAtNewPoint.setEntry(knew, this.lagrangeValuesAtNewPoint.getEntry(knew) - 1.0d);
        double sqrtDenom = FastMath.sqrt(denom);
        double d12 = tau / sqrtDenom;
        double d22 = this.zMatrix.getEntry(knew, 0) / sqrtDenom;
        for (int i4 = 0; i4 < npt; i4++) {
            this.zMatrix.setEntry(i4, 0, (d12 * this.zMatrix.getEntry(i4, 0)) - (d22 * this.lagrangeValuesAtNewPoint.getEntry(i4)));
        }
        for (int j4 = 0; j4 < n2; j4++) {
            int jp = npt + j4;
            work.setEntry(jp, this.bMatrix.getEntry(knew, j4));
            double d32 = ((alpha * this.lagrangeValuesAtNewPoint.getEntry(jp)) - (tau * work.getEntry(jp))) / denom;
            double d42 = (((-beta) * work.getEntry(jp)) - (tau * this.lagrangeValuesAtNewPoint.getEntry(jp))) / denom;
            for (int i5 = 0; i5 <= jp; i5++) {
                this.bMatrix.setEntry(i5, j4, this.bMatrix.getEntry(i5, j4) + (d32 * this.lagrangeValuesAtNewPoint.getEntry(i5)) + (d42 * work.getEntry(i5)));
                if (i5 >= npt) {
                    this.bMatrix.setEntry(jp, i5 - npt, this.bMatrix.getEntry(i5, j4));
                }
            }
        }
    }

    private void setup(double[] lowerBound, double[] upperBound) {
        printMethod();
        double[] init = getStartPoint();
        int dimension = init.length;
        if (dimension < 2) {
            throw new NumberIsTooSmallException(Integer.valueOf(dimension), 2, true);
        }
        int[] nPointsInterval = {dimension + 2, ((dimension + 2) * (dimension + 1)) / 2};
        if (this.numberOfInterpolationPoints < nPointsInterval[0] || this.numberOfInterpolationPoints > nPointsInterval[1]) {
            throw new OutOfRangeException(LocalizedFormats.NUMBER_OF_INTERPOLATION_POINTS, Integer.valueOf(this.numberOfInterpolationPoints), Integer.valueOf(nPointsInterval[0]), Integer.valueOf(nPointsInterval[1]));
        }
        this.boundDifference = new double[dimension];
        double requiredMinDiff = 2.0d * this.initialTrustRegionRadius;
        double minDiff = Double.POSITIVE_INFINITY;
        for (int i2 = 0; i2 < dimension; i2++) {
            this.boundDifference[i2] = upperBound[i2] - lowerBound[i2];
            minDiff = FastMath.min(minDiff, this.boundDifference[i2]);
        }
        if (minDiff < requiredMinDiff) {
            this.initialTrustRegionRadius = minDiff / 3.0d;
        }
        this.bMatrix = new Array2DRowRealMatrix(dimension + this.numberOfInterpolationPoints, dimension);
        this.zMatrix = new Array2DRowRealMatrix(this.numberOfInterpolationPoints, (this.numberOfInterpolationPoints - dimension) - 1);
        this.interpolationPoints = new Array2DRowRealMatrix(this.numberOfInterpolationPoints, dimension);
        this.originShift = new ArrayRealVector(dimension);
        this.fAtInterpolationPoints = new ArrayRealVector(this.numberOfInterpolationPoints);
        this.trustRegionCenterOffset = new ArrayRealVector(dimension);
        this.gradientAtTrustRegionCenter = new ArrayRealVector(dimension);
        this.lowerDifference = new ArrayRealVector(dimension);
        this.upperDifference = new ArrayRealVector(dimension);
        this.modelSecondDerivativesParameters = new ArrayRealVector(this.numberOfInterpolationPoints);
        this.newPoint = new ArrayRealVector(dimension);
        this.alternativeNewPoint = new ArrayRealVector(dimension);
        this.trialStepPoint = new ArrayRealVector(dimension);
        this.lagrangeValuesAtNewPoint = new ArrayRealVector(dimension + this.numberOfInterpolationPoints);
        this.modelSecondDerivativesValues = new ArrayRealVector((dimension * (dimension + 1)) / 2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String caller(int n2) {
        Throwable t2 = new Throwable();
        StackTraceElement[] elements = t2.getStackTrace();
        StackTraceElement e2 = elements[n2];
        return e2.getMethodName() + " (at line " + e2.getLineNumber() + ")";
    }

    private static void printState(int s2) {
    }

    private static void printMethod() {
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optimization/direct/BOBYQAOptimizer$PathIsExploredException.class */
    private static class PathIsExploredException extends RuntimeException {
        private static final long serialVersionUID = 745350979634801853L;
        private static final String PATH_IS_EXPLORED = "If this exception is thrown, just remove it from the code";

        PathIsExploredException() {
            super("If this exception is thrown, just remove it from the code " + BOBYQAOptimizer.caller(3));
        }
    }
}
