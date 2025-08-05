package org.apache.commons.math3.ode.nonstiff;

import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ode/nonstiff/GraggBulirschStoerIntegrator.class */
public class GraggBulirschStoerIntegrator extends AdaptiveStepsizeIntegrator {
    private static final String METHOD_NAME = "Gragg-Bulirsch-Stoer";
    private int maxOrder;
    private int[] sequence;
    private int[] costPerStep;
    private double[] costPerTimeUnit;
    private double[] optimalStep;
    private double[][] coeff;
    private boolean performTest;
    private int maxChecks;
    private int maxIter;
    private double stabilityReduction;
    private double stepControl1;
    private double stepControl2;
    private double stepControl3;
    private double stepControl4;
    private double orderControl1;
    private double orderControl2;
    private boolean useInterpolationError;
    private int mudif;

    public GraggBulirschStoerIntegrator(double minStep, double maxStep, double scalAbsoluteTolerance, double scalRelativeTolerance) {
        super(METHOD_NAME, minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
        setStabilityCheck(true, -1, -1, -1.0d);
        setControlFactors(-1.0d, -1.0d, -1.0d, -1.0d);
        setOrderControl(-1, -1.0d, -1.0d);
        setInterpolationControl(true, -1);
    }

    public GraggBulirschStoerIntegrator(double minStep, double maxStep, double[] vecAbsoluteTolerance, double[] vecRelativeTolerance) {
        super(METHOD_NAME, minStep, maxStep, vecAbsoluteTolerance, vecRelativeTolerance);
        setStabilityCheck(true, -1, -1, -1.0d);
        setControlFactors(-1.0d, -1.0d, -1.0d, -1.0d);
        setOrderControl(-1, -1.0d, -1.0d);
        setInterpolationControl(true, -1);
    }

    public void setStabilityCheck(boolean performStabilityCheck, int maxNumIter, int maxNumChecks, double stepsizeReductionFactor) {
        this.performTest = performStabilityCheck;
        this.maxIter = maxNumIter <= 0 ? 2 : maxNumIter;
        this.maxChecks = maxNumChecks <= 0 ? 1 : maxNumChecks;
        if (stepsizeReductionFactor < 1.0E-4d || stepsizeReductionFactor > 0.9999d) {
            this.stabilityReduction = 0.5d;
        } else {
            this.stabilityReduction = stepsizeReductionFactor;
        }
    }

    public void setControlFactors(double control1, double control2, double control3, double control4) {
        if (control1 < 1.0E-4d || control1 > 0.9999d) {
            this.stepControl1 = 0.65d;
        } else {
            this.stepControl1 = control1;
        }
        if (control2 < 1.0E-4d || control2 > 0.9999d) {
            this.stepControl2 = 0.94d;
        } else {
            this.stepControl2 = control2;
        }
        if (control3 < 1.0E-4d || control3 > 0.9999d) {
            this.stepControl3 = 0.02d;
        } else {
            this.stepControl3 = control3;
        }
        if (control4 < 1.0001d || control4 > 999.9d) {
            this.stepControl4 = 4.0d;
        } else {
            this.stepControl4 = control4;
        }
    }

    public void setOrderControl(int maximalOrder, double control1, double control2) {
        if (maximalOrder <= 6 || maximalOrder % 2 != 0) {
            this.maxOrder = 18;
        }
        if (control1 < 1.0E-4d || control1 > 0.9999d) {
            this.orderControl1 = 0.8d;
        } else {
            this.orderControl1 = control1;
        }
        if (control2 < 1.0E-4d || control2 > 0.9999d) {
            this.orderControl2 = 0.9d;
        } else {
            this.orderControl2 = control2;
        }
        initializeArrays();
    }

    @Override // org.apache.commons.math3.ode.AbstractIntegrator, org.apache.commons.math3.ode.ODEIntegrator
    public void addStepHandler(StepHandler handler) {
        super.addStepHandler(handler);
        initializeArrays();
    }

    @Override // org.apache.commons.math3.ode.AbstractIntegrator, org.apache.commons.math3.ode.ODEIntegrator
    public void addEventHandler(EventHandler function, double maxCheckInterval, double convergence, int maxIterationCount, UnivariateSolver solver) {
        super.addEventHandler(function, maxCheckInterval, convergence, maxIterationCount, solver);
        initializeArrays();
    }

    /* JADX WARN: Type inference failed for: r1v6, types: [double[], double[][]] */
    private void initializeArrays() {
        int size = this.maxOrder / 2;
        if (this.sequence == null || this.sequence.length != size) {
            this.sequence = new int[size];
            this.costPerStep = new int[size];
            this.coeff = new double[size];
            this.costPerTimeUnit = new double[size];
            this.optimalStep = new double[size];
        }
        for (int k2 = 0; k2 < size; k2++) {
            this.sequence[k2] = (4 * k2) + 2;
        }
        this.costPerStep[0] = this.sequence[0] + 1;
        for (int k3 = 1; k3 < size; k3++) {
            this.costPerStep[k3] = this.costPerStep[k3 - 1] + this.sequence[k3];
        }
        int k4 = 0;
        while (k4 < size) {
            this.coeff[k4] = k4 > 0 ? new double[k4] : null;
            for (int l2 = 0; l2 < k4; l2++) {
                double ratio = this.sequence[k4] / this.sequence[(k4 - l2) - 1];
                this.coeff[k4][l2] = 1.0d / ((ratio * ratio) - 1.0d);
            }
            k4++;
        }
    }

    public void setInterpolationControl(boolean useInterpolationErrorForControl, int mudifControlParameter) {
        this.useInterpolationError = useInterpolationErrorForControl;
        if (mudifControlParameter <= 0 || mudifControlParameter >= 7) {
            this.mudif = 4;
        } else {
            this.mudif = mudifControlParameter;
        }
    }

    private void rescale(double[] y1, double[] y2, double[] scale) {
        if (this.vecAbsoluteTolerance == null) {
            for (int i2 = 0; i2 < scale.length; i2++) {
                double yi = FastMath.max(FastMath.abs(y1[i2]), FastMath.abs(y2[i2]));
                scale[i2] = this.scalAbsoluteTolerance + (this.scalRelativeTolerance * yi);
            }
            return;
        }
        for (int i3 = 0; i3 < scale.length; i3++) {
            double yi2 = FastMath.max(FastMath.abs(y1[i3]), FastMath.abs(y2[i3]));
            scale[i3] = this.vecAbsoluteTolerance[i3] + (this.vecRelativeTolerance[i3] * yi2);
        }
    }

    private boolean tryStep(double t0, double[] y0, double step, int k2, double[] scale, double[][] f2, double[] yMiddle, double[] yEnd, double[] yTmp) throws MaxCountExceededException, DimensionMismatchException {
        int n2 = this.sequence[k2];
        double subStep = step / n2;
        double subStep2 = 2.0d * subStep;
        double t2 = t0 + subStep;
        for (int i2 = 0; i2 < y0.length; i2++) {
            yTmp[i2] = y0[i2];
            yEnd[i2] = y0[i2] + (subStep * f2[0][i2]);
        }
        computeDerivatives(t2, yEnd, f2[1]);
        for (int j2 = 1; j2 < n2; j2++) {
            if (2 * j2 == n2) {
                System.arraycopy(yEnd, 0, yMiddle, 0, y0.length);
            }
            t2 += subStep;
            for (int i3 = 0; i3 < y0.length; i3++) {
                double middle = yEnd[i3];
                yEnd[i3] = yTmp[i3] + (subStep2 * f2[j2][i3]);
                yTmp[i3] = middle;
            }
            computeDerivatives(t2, yEnd, f2[j2 + 1]);
            if (this.performTest && j2 <= this.maxChecks && k2 < this.maxIter) {
                double initialNorm = 0.0d;
                for (int l2 = 0; l2 < scale.length; l2++) {
                    double ratio = f2[0][l2] / scale[l2];
                    initialNorm += ratio * ratio;
                }
                double deltaNorm = 0.0d;
                for (int l3 = 0; l3 < scale.length; l3++) {
                    double ratio2 = (f2[j2 + 1][l3] - f2[0][l3]) / scale[l3];
                    deltaNorm += ratio2 * ratio2;
                }
                if (deltaNorm > 4.0d * FastMath.max(1.0E-15d, initialNorm)) {
                    return false;
                }
            }
        }
        for (int i4 = 0; i4 < y0.length; i4++) {
            yEnd[i4] = 0.5d * (yTmp[i4] + yEnd[i4] + (subStep * f2[n2][i4]));
        }
        return true;
    }

    private void extrapolate(int offset, int k2, double[][] diag, double[] last) {
        for (int j2 = 1; j2 < k2; j2++) {
            for (int i2 = 0; i2 < last.length; i2++) {
                diag[(k2 - j2) - 1][i2] = diag[k2 - j2][i2] + (this.coeff[k2 + offset][j2 - 1] * (diag[k2 - j2][i2] - diag[(k2 - j2) - 1][i2]));
            }
        }
        for (int i3 = 0; i3 < last.length; i3++) {
            last[i3] = diag[0][i3] + (this.coeff[k2 + offset][k2 - 1] * (diag[0][i3] - last[i3]));
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v204 */
    /* JADX WARN: Type inference failed for: r0v218 */
    /* JADX WARN: Type inference failed for: r0v225 */
    /* JADX WARN: Type inference failed for: r0v226 */
    /* JADX WARN: Type inference failed for: r0v233 */
    /* JADX WARN: Type inference failed for: r0v240 */
    /* JADX WARN: Type inference failed for: r0v27, types: [double[], double[][]] */
    /* JADX WARN: Type inference failed for: r0v32, types: [double[], double[][]] */
    /* JADX WARN: Type inference failed for: r0v377 */
    /* JADX WARN: Type inference failed for: r0v38, types: [double[][]] */
    /* JADX WARN: Type inference failed for: r0v381 */
    /* JADX WARN: Type inference failed for: r14v0, types: [org.apache.commons.math3.ode.nonstiff.GraggBulirschStoerIntegrator] */
    /* JADX WARN: Type inference failed for: r1v261 */
    /* JADX WARN: Type inference failed for: r1v262, types: [double] */
    /* JADX WARN: Type inference failed for: r2v70, types: [double] */
    /* JADX WARN: Type inference failed for: r3v29 */
    /* JADX WARN: Type inference failed for: r3v30 */
    /* JADX WARN: Type inference failed for: r3v31, types: [double] */
    /* JADX WARN: Type inference failed for: r3v38 */
    /* JADX WARN: Type inference failed for: r3v39 */
    /* JADX WARN: Type inference failed for: r3v40, types: [double] */
    /* JADX WARN: Type inference failed for: r3v42 */
    /* JADX WARN: Type inference failed for: r3v43 */
    /* JADX WARN: Type inference failed for: r3v44, types: [double] */
    /* JADX WARN: Type inference failed for: r6v3, types: [double[][]] */
    /* JADX WARN: Type inference failed for: r7v4 */
    /* JADX WARN: Type inference failed for: r8v6 */
    @Override // org.apache.commons.math3.ode.nonstiff.AdaptiveStepsizeIntegrator, org.apache.commons.math3.ode.AbstractIntegrator
    public void integrate(ExpandableStatefulODE expandableStatefulODE, double d2) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
        int iMin;
        sanityChecks(expandableStatefulODE, d2);
        setEquations(expandableStatefulODE);
        boolean z2 = d2 > expandableStatefulODE.getTime();
        double[] completeState = expandableStatefulODE.getCompleteState();
        double[] dArr = (double[]) completeState.clone();
        double[] dArr2 = new double[dArr.length];
        double[] dArr3 = new double[dArr.length];
        double[] dArr4 = new double[dArr.length];
        double[] dArr5 = new double[dArr.length];
        ?? r0 = new double[this.sequence.length - 1];
        ?? r02 = new double[this.sequence.length - 1];
        for (int i2 = 0; i2 < this.sequence.length - 1; i2++) {
            r0[i2] = new double[dArr.length];
            r02[i2] = new double[dArr.length];
        }
        ?? r03 = new double[this.sequence.length][];
        for (int i3 = 0; i3 < this.sequence.length; i3++) {
            r03[i3] = new double[this.sequence[i3] + 1];
            r03[i3][0] = dArr2;
            for (int i4 = 0; i4 < this.sequence[i3]; i4++) {
                r03[i3][i4 + 1] = new double[completeState.length];
            }
        }
        if (dArr != completeState) {
            System.arraycopy(completeState, 0, dArr, 0, completeState.length);
        }
        double[] dArr6 = new double[completeState.length];
        double[][] dArr7 = new double[1 + (2 * this.sequence.length)][completeState.length];
        double[] dArr8 = new double[this.mainSetDimension];
        rescale(dArr, dArr, dArr8);
        int iMax = FastMath.max(1, FastMath.min(this.sequence.length - 2, (int) FastMath.floor(0.5d - (0.6d * FastMath.log10(FastMath.max(1.0E-10d, this.vecRelativeTolerance == null ? this.scalRelativeTolerance : this.vecRelativeTolerance[0]))))));
        GraggBulirschStoerStepInterpolator graggBulirschStoerStepInterpolator = new GraggBulirschStoerStepInterpolator(dArr, dArr2, dArr3, dArr6, dArr7, z2, expandableStatefulODE.getPrimaryMapper(), expandableStatefulODE.getSecondaryMappers());
        graggBulirschStoerStepInterpolator.storeTime(expandableStatefulODE.getTime());
        this.stepStart = expandableStatefulODE.getTime();
        double dMin = 0.0d;
        double dMax = Double.MAX_VALUE;
        boolean z3 = false;
        boolean z4 = true;
        boolean z5 = true;
        boolean z6 = false;
        initIntegration(expandableStatefulODE.getTime(), completeState, d2);
        this.costPerTimeUnit[0] = 0.0d;
        this.isLastStep = false;
        do {
            boolean z7 = false;
            if (z5) {
                graggBulirschStoerStepInterpolator.shift();
                if (!z6) {
                    computeDerivatives(this.stepStart, dArr, dArr2);
                }
                if (z4) {
                    dMin = initializeStep(z2, (2 * iMax) + 1, dArr8, this.stepStart, dArr, dArr2, dArr4, dArr5);
                }
                z5 = false;
            }
            this.stepSize = dMin;
            if ((z2 && this.stepStart + this.stepSize > d2) || (!z2 && this.stepStart + this.stepSize < d2)) {
                this.stepSize = d2 - this.stepStart;
            }
            double d3 = this.stepStart + this.stepSize;
            this.isLastStep = z2 ? d3 >= d2 : d3 <= d2;
            int i5 = -1;
            boolean z8 = true;
            while (z8) {
                i5++;
                if (!tryStep(this.stepStart, dArr, this.stepSize, i5, dArr8, r03[i5], i5 == 0 ? dArr7[0] : r0[i5 - 1], i5 == 0 ? dArr3 : r02[i5 - 1], dArr4)) {
                    dMin = FastMath.abs(filterStep(this.stepSize * this.stabilityReduction, z2, false));
                    z7 = true;
                    z8 = false;
                } else if (i5 > 0) {
                    extrapolate(0, i5, r02, dArr3);
                    rescale(dArr, dArr3, dArr8);
                    double d4 = 0.0d;
                    for (int i6 = 0; i6 < this.mainSetDimension; i6++) {
                        double dAbs = FastMath.abs(dArr3[i6] - r02[0][i6]) / dArr8[i6];
                        d4 += dAbs * dAbs;
                    }
                    double dSqrt = FastMath.sqrt(d4 / this.mainSetDimension);
                    if (dSqrt > 1.0E15d || (i5 > 1 && dSqrt > dMax)) {
                        dMin = FastMath.abs(filterStep(this.stepSize * this.stabilityReduction, z2, false));
                        z7 = true;
                        z8 = false;
                    } else {
                        dMax = FastMath.max(4.0d * dSqrt, 1.0d);
                        double d5 = 1.0d / ((2 * i5) + 1);
                        double dPow = this.stepControl2 / FastMath.pow(dSqrt / this.stepControl1, d5);
                        double dPow2 = FastMath.pow(this.stepControl3, d5);
                        this.optimalStep[i5] = FastMath.abs(filterStep(this.stepSize * FastMath.max(dPow2 / this.stepControl4, FastMath.min(1.0d / dPow2, dPow)), z2, true));
                        this.costPerTimeUnit[i5] = this.costPerStep[i5] / this.optimalStep[i5];
                        switch (i5 - iMax) {
                            case -1:
                                if (iMax <= 1 || z3) {
                                    break;
                                } else if (dSqrt <= 1.0d) {
                                    z8 = false;
                                    break;
                                } else {
                                    double d6 = (this.sequence[iMax] * this.sequence[iMax + 1]) / (this.sequence[0] * this.sequence[0]);
                                    if (dSqrt > d6 * d6) {
                                        z7 = true;
                                        z8 = false;
                                        iMax = i5;
                                        if (iMax > 1 && this.costPerTimeUnit[iMax - 1] < this.orderControl1 * this.costPerTimeUnit[iMax]) {
                                            iMax--;
                                        }
                                        dMin = this.optimalStep[iMax];
                                        break;
                                    } else {
                                        break;
                                    }
                                }
                            case 0:
                                if (dSqrt <= 1.0d) {
                                    z8 = false;
                                    break;
                                } else {
                                    double d7 = this.sequence[i5 + 1] / this.sequence[0];
                                    if (dSqrt > d7 * d7) {
                                        z7 = true;
                                        z8 = false;
                                        if (iMax > 1 && this.costPerTimeUnit[iMax - 1] < this.orderControl1 * this.costPerTimeUnit[iMax]) {
                                            iMax--;
                                        }
                                        dMin = this.optimalStep[iMax];
                                        break;
                                    } else {
                                        break;
                                    }
                                }
                                break;
                            case 1:
                                if (dSqrt > 1.0d) {
                                    z7 = true;
                                    if (iMax > 1 && this.costPerTimeUnit[iMax - 1] < this.orderControl1 * this.costPerTimeUnit[iMax]) {
                                        iMax--;
                                    }
                                    dMin = this.optimalStep[iMax];
                                }
                                z8 = false;
                                break;
                            default:
                                if (!z4 && !this.isLastStep) {
                                    break;
                                } else if (dSqrt <= 1.0d) {
                                    z8 = false;
                                    break;
                                } else {
                                    break;
                                }
                        }
                    }
                }
            }
            if (!z7) {
                computeDerivatives(this.stepStart + this.stepSize, dArr3, dArr6);
            }
            double maxStep = getMaxStep();
            if (!z7) {
                for (int i7 = 1; i7 <= i5; i7++) {
                    extrapolate(0, i7, r0, dArr7[0]);
                }
                int i8 = ((2 * i5) - this.mudif) + 3;
                for (int i9 = 0; i9 < i8; i9++) {
                    int i10 = i9 / 2;
                    double dPow3 = FastMath.pow(0.5d * this.sequence[i10], i9);
                    int length = r03[i10].length / 2;
                    for (int i11 = 0; i11 < completeState.length; i11++) {
                        dArr7[i9 + 1][i11] = dPow3 * r03[i10][length + i9][i11];
                    }
                    for (int i12 = 1; i12 <= i5 - i10; i12++) {
                        double dPow4 = FastMath.pow(0.5d * this.sequence[i12 + i10], i9);
                        int length2 = r03[i10 + i12].length / 2;
                        for (int i13 = 0; i13 < completeState.length; i13++) {
                            r0[i12 - 1][i13] = dPow4 * r03[i10 + i12][length2 + i9][i13];
                        }
                        extrapolate(i10, i12, r0, dArr7[i9 + 1]);
                    }
                    for (int i14 = 0; i14 < completeState.length; i14++) {
                        double[] dArr9 = dArr7[i9 + 1];
                        int i15 = i14;
                        dArr9[i15] = dArr9[i15] * this.stepSize;
                    }
                    for (int i16 = (i9 + 1) / 2; i16 <= i5; i16++) {
                        for (int length3 = r03[i16].length - 1; length3 >= 2 * (i9 + 1); length3--) {
                            for (int i17 = 0; i17 < completeState.length; i17++) {
                                ?? r04 = r03[i16][length3];
                                int i18 = i17;
                                r04[i18] = r04[i18] - r03[i16][length3 - 2][i17];
                            }
                        }
                    }
                }
                if (i8 >= 0) {
                    GraggBulirschStoerStepInterpolator graggBulirschStoerStepInterpolator2 = graggBulirschStoerStepInterpolator;
                    graggBulirschStoerStepInterpolator2.computeCoefficients(i8, this.stepSize);
                    if (this.useInterpolationError) {
                        double dEstimateError = graggBulirschStoerStepInterpolator2.estimateError(dArr8);
                        maxStep = FastMath.abs(this.stepSize / FastMath.max(FastMath.pow(dEstimateError, 1.0d / (i8 + 4)), 0.01d));
                        if (dEstimateError > 10.0d) {
                            dMin = maxStep;
                            z7 = true;
                        }
                    }
                }
            }
            if (!z7) {
                graggBulirschStoerStepInterpolator.storeTime(this.stepStart + this.stepSize);
                this.stepStart = acceptStep(graggBulirschStoerStepInterpolator, dArr3, dArr6, d2);
                graggBulirschStoerStepInterpolator.storeTime(this.stepStart);
                System.arraycopy(dArr3, 0, dArr, 0, completeState.length);
                System.arraycopy(dArr6, 0, dArr2, 0, completeState.length);
                z6 = true;
                if (i5 == 1) {
                    iMin = 2;
                    if (z3) {
                        iMin = 1;
                    }
                } else if (i5 <= iMax) {
                    iMin = i5;
                    if (this.costPerTimeUnit[i5 - 1] < this.orderControl1 * this.costPerTimeUnit[i5]) {
                        iMin = i5 - 1;
                    } else if (this.costPerTimeUnit[i5] < this.orderControl2 * this.costPerTimeUnit[i5 - 1]) {
                        iMin = FastMath.min(i5 + 1, this.sequence.length - 2);
                    }
                } else {
                    iMin = i5 - 1;
                    if (i5 > 2 && this.costPerTimeUnit[i5 - 2] < this.orderControl1 * this.costPerTimeUnit[i5 - 1]) {
                        iMin = i5 - 2;
                    }
                    if (this.costPerTimeUnit[i5] < this.orderControl2 * this.costPerTimeUnit[iMin]) {
                        iMin = FastMath.min(i5, this.sequence.length - 2);
                    }
                }
                if (z3) {
                    iMax = FastMath.min(iMin, i5);
                    dMin = FastMath.min(FastMath.abs(this.stepSize), this.optimalStep[iMax]);
                } else {
                    if (iMin <= i5) {
                        dMin = this.optimalStep[iMin];
                    } else if (i5 < iMax && this.costPerTimeUnit[i5] < this.orderControl2 * this.costPerTimeUnit[i5 - 1]) {
                        dMin = filterStep((this.optimalStep[i5] * this.costPerStep[iMin + 1]) / this.costPerStep[i5], z2, false);
                    } else {
                        dMin = filterStep((this.optimalStep[i5] * this.costPerStep[iMin]) / this.costPerStep[i5], z2, false);
                    }
                    iMax = iMin;
                }
                z5 = true;
            }
            dMin = FastMath.min(dMin, maxStep);
            if (!z2) {
                dMin = -dMin;
            }
            z4 = false;
            if (z7) {
                this.isLastStep = false;
                z3 = true;
            } else {
                z3 = false;
            }
        } while (!this.isLastStep);
        expandableStatefulODE.setTime(this.stepStart);
        expandableStatefulODE.setCompleteState(dArr);
        resetInternalState();
    }
}
