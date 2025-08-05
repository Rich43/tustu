package org.apache.commons.math3.ode.nonstiff;

import org.apache.commons.math3.Field;
import org.apache.commons.math3.RealFieldElement;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.ode.AbstractFieldIntegrator;
import org.apache.commons.math3.ode.FieldEquationsMapper;
import org.apache.commons.math3.ode.FieldODEState;
import org.apache.commons.math3.ode.FieldODEStateAndDerivative;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.MathUtils;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ode/nonstiff/AdaptiveStepsizeFieldIntegrator.class */
public abstract class AdaptiveStepsizeFieldIntegrator<T extends RealFieldElement<T>> extends AbstractFieldIntegrator<T> {
    protected double scalAbsoluteTolerance;
    protected double scalRelativeTolerance;
    protected double[] vecAbsoluteTolerance;
    protected double[] vecRelativeTolerance;
    protected int mainSetDimension;
    private T initialStep;
    private T minStep;
    private T maxStep;

    public AdaptiveStepsizeFieldIntegrator(Field<T> field, String name, double minStep, double maxStep, double scalAbsoluteTolerance, double scalRelativeTolerance) {
        super(field, name);
        setStepSizeControl(minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
        resetInternalState();
    }

    public AdaptiveStepsizeFieldIntegrator(Field<T> field, String name, double minStep, double maxStep, double[] vecAbsoluteTolerance, double[] vecRelativeTolerance) {
        super(field, name);
        setStepSizeControl(minStep, maxStep, vecAbsoluteTolerance, vecRelativeTolerance);
        resetInternalState();
    }

    public void setStepSizeControl(double minimalStep, double maximalStep, double absoluteTolerance, double relativeTolerance) {
        this.minStep = (T) getField().getZero().add(FastMath.abs(minimalStep));
        this.maxStep = (T) getField().getZero().add(FastMath.abs(maximalStep));
        this.initialStep = (T) getField().getOne().negate();
        this.scalAbsoluteTolerance = absoluteTolerance;
        this.scalRelativeTolerance = relativeTolerance;
        this.vecAbsoluteTolerance = null;
        this.vecRelativeTolerance = null;
    }

    public void setStepSizeControl(double minimalStep, double maximalStep, double[] absoluteTolerance, double[] relativeTolerance) {
        this.minStep = (T) getField().getZero().add(FastMath.abs(minimalStep));
        this.maxStep = (T) getField().getZero().add(FastMath.abs(maximalStep));
        this.initialStep = (T) getField().getOne().negate();
        this.scalAbsoluteTolerance = 0.0d;
        this.scalRelativeTolerance = 0.0d;
        this.vecAbsoluteTolerance = (double[]) absoluteTolerance.clone();
        this.vecRelativeTolerance = (double[]) relativeTolerance.clone();
    }

    public void setInitialStepSize(T initialStepSize) {
        if (((RealFieldElement) initialStepSize.subtract(this.minStep)).getReal() < 0.0d || ((RealFieldElement) initialStepSize.subtract(this.maxStep)).getReal() > 0.0d) {
            this.initialStep = (T) getField().getOne().negate();
        } else {
            this.initialStep = initialStepSize;
        }
    }

    @Override // org.apache.commons.math3.ode.AbstractFieldIntegrator
    protected void sanityChecks(FieldODEState<T> eqn, T t2) throws NumberIsTooSmallException, DimensionMismatchException {
        super.sanityChecks(eqn, t2);
        this.mainSetDimension = eqn.getStateDimension();
        if (this.vecAbsoluteTolerance != null && this.vecAbsoluteTolerance.length != this.mainSetDimension) {
            throw new DimensionMismatchException(this.mainSetDimension, this.vecAbsoluteTolerance.length);
        }
        if (this.vecRelativeTolerance != null && this.vecRelativeTolerance.length != this.mainSetDimension) {
            throw new DimensionMismatchException(this.mainSetDimension, this.vecRelativeTolerance.length);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v115, types: [org.apache.commons.math3.RealFieldElement] */
    /* JADX WARN: Type inference failed for: r0v122, types: [org.apache.commons.math3.RealFieldElement] */
    /* JADX WARN: Type inference failed for: r0v94, types: [org.apache.commons.math3.RealFieldElement] */
    public T initializeStep(boolean z2, int i2, T[] tArr, FieldODEStateAndDerivative<T> fieldODEStateAndDerivative, FieldEquationsMapper<T> fieldEquationsMapper) throws MaxCountExceededException, MathIllegalArgumentException {
        if (this.initialStep.getReal() > 0.0d) {
            return z2 ? this.initialStep : (T) this.initialStep.negate();
        }
        RealFieldElement[] realFieldElementArrMapState = fieldEquationsMapper.mapState(fieldODEStateAndDerivative);
        RealFieldElement[] realFieldElementArrMapDerivative = fieldEquationsMapper.mapDerivative(fieldODEStateAndDerivative);
        T zero = getField().getZero();
        T zero2 = getField().getZero();
        for (int i3 = 0; i3 < tArr.length; i3++) {
            RealFieldElement realFieldElement = (RealFieldElement) realFieldElementArrMapState[i3].divide(tArr[i3]);
            zero = (RealFieldElement) zero.add(realFieldElement.multiply(realFieldElement));
            RealFieldElement realFieldElement2 = (RealFieldElement) realFieldElementArrMapDerivative[i3].divide(tArr[i3]);
            zero2 = (RealFieldElement) zero2.add(realFieldElement2.multiply(realFieldElement2));
        }
        RealFieldElement realFieldElement3 = (zero.getReal() < 1.0E-10d || zero2.getReal() < 1.0E-10d) ? (RealFieldElement) getField().getZero().add(1.0E-6d) : (RealFieldElement) ((RealFieldElement) ((RealFieldElement) zero.divide(zero2)).sqrt()).multiply(0.01d);
        if (!z2) {
            realFieldElement3 = (RealFieldElement) realFieldElement3.negate();
        }
        RealFieldElement[] realFieldElementArr = (RealFieldElement[]) MathArrays.buildArray(getField(), realFieldElementArrMapState.length);
        for (int i4 = 0; i4 < realFieldElementArrMapState.length; i4++) {
            realFieldElementArr[i4] = (RealFieldElement) realFieldElementArrMapState[i4].add((RealFieldElement) realFieldElementArrMapDerivative[i4].multiply(realFieldElement3));
        }
        T[] tArrComputeDerivatives = computeDerivatives((RealFieldElement) fieldODEStateAndDerivative.getTime().add(realFieldElement3), realFieldElementArr);
        T zero3 = getField().getZero();
        for (int i5 = 0; i5 < tArr.length; i5++) {
            RealFieldElement realFieldElement4 = (RealFieldElement) ((RealFieldElement) tArrComputeDerivatives[i5].subtract(realFieldElementArrMapDerivative[i5])).divide(tArr[i5]);
            zero3 = (RealFieldElement) zero3.add(realFieldElement4.multiply(realFieldElement4));
        }
        RealFieldElement realFieldElementMax = MathUtils.max((RealFieldElement) zero2.sqrt(), (RealFieldElement) ((RealFieldElement) zero3.sqrt()).divide(realFieldElement3));
        RealFieldElement realFieldElementMax2 = MathUtils.max(this.minStep, MathUtils.min(this.maxStep, MathUtils.max(MathUtils.min((RealFieldElement) ((RealFieldElement) realFieldElement3.abs()).multiply(100), realFieldElementMax.getReal() < 1.0E-15d ? MathUtils.max((RealFieldElement) getField().getZero().add(1.0E-6d), (RealFieldElement) ((RealFieldElement) realFieldElement3.abs()).multiply(0.001d)) : (RealFieldElement) ((RealFieldElement) ((RealFieldElement) realFieldElementMax.multiply(100)).reciprocal()).pow(1.0d / i2)), (RealFieldElement) ((RealFieldElement) fieldODEStateAndDerivative.getTime().abs()).multiply(1.0E-12d))));
        if (!z2) {
            realFieldElementMax2 = (RealFieldElement) realFieldElementMax2.negate();
        }
        return (T) realFieldElementMax2;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v21, types: [org.apache.commons.math3.RealFieldElement] */
    protected T filterStep(T h2, boolean forward, boolean acceptSmall) throws NumberIsTooSmallException {
        T filteredH = h2;
        if (((RealFieldElement) ((RealFieldElement) h2.abs()).subtract(this.minStep)).getReal() < 0.0d) {
            if (acceptSmall) {
                filteredH = forward ? this.minStep : (T) this.minStep.negate();
            } else {
                throw new NumberIsTooSmallException(LocalizedFormats.MINIMAL_STEPSIZE_REACHED_DURING_INTEGRATION, Double.valueOf(((RealFieldElement) h2.abs()).getReal()), Double.valueOf(this.minStep.getReal()), true);
            }
        }
        if (((RealFieldElement) filteredH.subtract(this.maxStep)).getReal() > 0.0d) {
            filteredH = this.maxStep;
        } else if (((RealFieldElement) filteredH.add(this.maxStep)).getReal() < 0.0d) {
            filteredH = (RealFieldElement) this.maxStep.negate();
        }
        return filteredH;
    }

    protected void resetInternalState() {
        setStepStart(null);
        setStepSize((RealFieldElement) ((RealFieldElement) this.minStep.multiply(this.maxStep)).sqrt());
    }

    public T getMinStep() {
        return this.minStep;
    }

    public T getMaxStep() {
        return this.maxStep;
    }
}
