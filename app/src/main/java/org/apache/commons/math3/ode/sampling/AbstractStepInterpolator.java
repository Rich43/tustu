package org.apache.commons.math3.ode.sampling;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.ode.EquationsMapper;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ode/sampling/AbstractStepInterpolator.class */
public abstract class AbstractStepInterpolator implements StepInterpolator {

    /* renamed from: h, reason: collision with root package name */
    protected double f13067h;
    protected double[] currentState;
    protected double interpolatedTime;
    protected double[] interpolatedState;
    protected double[] interpolatedDerivatives;
    protected double[] interpolatedPrimaryState;
    protected double[] interpolatedPrimaryDerivatives;
    protected double[][] interpolatedSecondaryState;
    protected double[][] interpolatedSecondaryDerivatives;
    private double globalPreviousTime;
    private double globalCurrentTime;
    private double softPreviousTime;
    private double softCurrentTime;
    private boolean finalized;
    private boolean forward;
    private boolean dirtyState;
    private EquationsMapper primaryMapper;
    private EquationsMapper[] secondaryMappers;

    protected abstract StepInterpolator doCopy();

    protected abstract void computeInterpolatedStateAndDerivatives(double d2, double d3) throws MaxCountExceededException;

    @Override // java.io.Externalizable
    public abstract void writeExternal(ObjectOutput objectOutput) throws IOException;

    @Override // java.io.Externalizable
    public abstract void readExternal(ObjectInput objectInput) throws IOException, ClassNotFoundException;

    protected AbstractStepInterpolator() {
        this.globalPreviousTime = Double.NaN;
        this.globalCurrentTime = Double.NaN;
        this.softPreviousTime = Double.NaN;
        this.softCurrentTime = Double.NaN;
        this.f13067h = Double.NaN;
        this.interpolatedTime = Double.NaN;
        this.currentState = null;
        this.finalized = false;
        this.forward = true;
        this.dirtyState = true;
        this.primaryMapper = null;
        this.secondaryMappers = null;
        allocateInterpolatedArrays(-1);
    }

    protected AbstractStepInterpolator(double[] y2, boolean forward, EquationsMapper primaryMapper, EquationsMapper[] secondaryMappers) {
        this.globalPreviousTime = Double.NaN;
        this.globalCurrentTime = Double.NaN;
        this.softPreviousTime = Double.NaN;
        this.softCurrentTime = Double.NaN;
        this.f13067h = Double.NaN;
        this.interpolatedTime = Double.NaN;
        this.currentState = y2;
        this.finalized = false;
        this.forward = forward;
        this.dirtyState = true;
        this.primaryMapper = primaryMapper;
        this.secondaryMappers = secondaryMappers == null ? null : (EquationsMapper[]) secondaryMappers.clone();
        allocateInterpolatedArrays(y2.length);
    }

    /* JADX WARN: Type inference failed for: r1v35, types: [double[], double[][]] */
    /* JADX WARN: Type inference failed for: r1v39, types: [double[], double[][]] */
    protected AbstractStepInterpolator(AbstractStepInterpolator interpolator) {
        this.globalPreviousTime = interpolator.globalPreviousTime;
        this.globalCurrentTime = interpolator.globalCurrentTime;
        this.softPreviousTime = interpolator.softPreviousTime;
        this.softCurrentTime = interpolator.softCurrentTime;
        this.f13067h = interpolator.f13067h;
        this.interpolatedTime = interpolator.interpolatedTime;
        if (interpolator.currentState == null) {
            this.currentState = null;
            this.primaryMapper = null;
            this.secondaryMappers = null;
            allocateInterpolatedArrays(-1);
        } else {
            this.currentState = (double[]) interpolator.currentState.clone();
            this.interpolatedState = (double[]) interpolator.interpolatedState.clone();
            this.interpolatedDerivatives = (double[]) interpolator.interpolatedDerivatives.clone();
            this.interpolatedPrimaryState = (double[]) interpolator.interpolatedPrimaryState.clone();
            this.interpolatedPrimaryDerivatives = (double[]) interpolator.interpolatedPrimaryDerivatives.clone();
            this.interpolatedSecondaryState = new double[interpolator.interpolatedSecondaryState.length];
            this.interpolatedSecondaryDerivatives = new double[interpolator.interpolatedSecondaryDerivatives.length];
            for (int i2 = 0; i2 < this.interpolatedSecondaryState.length; i2++) {
                this.interpolatedSecondaryState[i2] = (double[]) interpolator.interpolatedSecondaryState[i2].clone();
                this.interpolatedSecondaryDerivatives[i2] = (double[]) interpolator.interpolatedSecondaryDerivatives[i2].clone();
            }
        }
        this.finalized = interpolator.finalized;
        this.forward = interpolator.forward;
        this.dirtyState = interpolator.dirtyState;
        this.primaryMapper = interpolator.primaryMapper;
        this.secondaryMappers = interpolator.secondaryMappers == null ? null : (EquationsMapper[]) interpolator.secondaryMappers.clone();
    }

    /* JADX WARN: Type inference failed for: r1v15, types: [double[], double[][]] */
    /* JADX WARN: Type inference failed for: r1v19, types: [double[], double[][]] */
    private void allocateInterpolatedArrays(int dimension) {
        if (dimension < 0) {
            this.interpolatedState = null;
            this.interpolatedDerivatives = null;
            this.interpolatedPrimaryState = null;
            this.interpolatedPrimaryDerivatives = null;
            this.interpolatedSecondaryState = (double[][]) null;
            this.interpolatedSecondaryDerivatives = (double[][]) null;
            return;
        }
        this.interpolatedState = new double[dimension];
        this.interpolatedDerivatives = new double[dimension];
        this.interpolatedPrimaryState = new double[this.primaryMapper.getDimension()];
        this.interpolatedPrimaryDerivatives = new double[this.primaryMapper.getDimension()];
        if (this.secondaryMappers == null) {
            this.interpolatedSecondaryState = (double[][]) null;
            this.interpolatedSecondaryDerivatives = (double[][]) null;
            return;
        }
        this.interpolatedSecondaryState = new double[this.secondaryMappers.length];
        this.interpolatedSecondaryDerivatives = new double[this.secondaryMappers.length];
        for (int i2 = 0; i2 < this.secondaryMappers.length; i2++) {
            this.interpolatedSecondaryState[i2] = new double[this.secondaryMappers[i2].getDimension()];
            this.interpolatedSecondaryDerivatives[i2] = new double[this.secondaryMappers[i2].getDimension()];
        }
    }

    protected void reinitialize(double[] y2, boolean isForward, EquationsMapper primary, EquationsMapper[] secondary) {
        this.globalPreviousTime = Double.NaN;
        this.globalCurrentTime = Double.NaN;
        this.softPreviousTime = Double.NaN;
        this.softCurrentTime = Double.NaN;
        this.f13067h = Double.NaN;
        this.interpolatedTime = Double.NaN;
        this.currentState = y2;
        this.finalized = false;
        this.forward = isForward;
        this.dirtyState = true;
        this.primaryMapper = primary;
        this.secondaryMappers = (EquationsMapper[]) secondary.clone();
        allocateInterpolatedArrays(y2.length);
    }

    @Override // org.apache.commons.math3.ode.sampling.StepInterpolator
    public StepInterpolator copy() throws MaxCountExceededException {
        finalizeStep();
        return doCopy();
    }

    public void shift() {
        this.globalPreviousTime = this.globalCurrentTime;
        this.softPreviousTime = this.globalPreviousTime;
        this.softCurrentTime = this.globalCurrentTime;
    }

    public void storeTime(double t2) {
        this.globalCurrentTime = t2;
        this.softCurrentTime = this.globalCurrentTime;
        this.f13067h = this.globalCurrentTime - this.globalPreviousTime;
        setInterpolatedTime(t2);
        this.finalized = false;
    }

    public void setSoftPreviousTime(double softPreviousTime) {
        this.softPreviousTime = softPreviousTime;
    }

    public void setSoftCurrentTime(double softCurrentTime) {
        this.softCurrentTime = softCurrentTime;
    }

    public double getGlobalPreviousTime() {
        return this.globalPreviousTime;
    }

    public double getGlobalCurrentTime() {
        return this.globalCurrentTime;
    }

    @Override // org.apache.commons.math3.ode.sampling.StepInterpolator
    public double getPreviousTime() {
        return this.softPreviousTime;
    }

    @Override // org.apache.commons.math3.ode.sampling.StepInterpolator
    public double getCurrentTime() {
        return this.softCurrentTime;
    }

    @Override // org.apache.commons.math3.ode.sampling.StepInterpolator
    public double getInterpolatedTime() {
        return this.interpolatedTime;
    }

    @Override // org.apache.commons.math3.ode.sampling.StepInterpolator
    public void setInterpolatedTime(double time) {
        this.interpolatedTime = time;
        this.dirtyState = true;
    }

    @Override // org.apache.commons.math3.ode.sampling.StepInterpolator
    public boolean isForward() {
        return this.forward;
    }

    private void evaluateCompleteInterpolatedState() throws MaxCountExceededException {
        if (this.dirtyState) {
            double oneMinusThetaH = this.globalCurrentTime - this.interpolatedTime;
            double theta = this.f13067h == 0.0d ? 0.0d : (this.f13067h - oneMinusThetaH) / this.f13067h;
            computeInterpolatedStateAndDerivatives(theta, oneMinusThetaH);
            this.dirtyState = false;
        }
    }

    @Override // org.apache.commons.math3.ode.sampling.StepInterpolator
    public double[] getInterpolatedState() throws MaxCountExceededException, DimensionMismatchException {
        evaluateCompleteInterpolatedState();
        this.primaryMapper.extractEquationData(this.interpolatedState, this.interpolatedPrimaryState);
        return this.interpolatedPrimaryState;
    }

    @Override // org.apache.commons.math3.ode.sampling.StepInterpolator
    public double[] getInterpolatedDerivatives() throws MaxCountExceededException, DimensionMismatchException {
        evaluateCompleteInterpolatedState();
        this.primaryMapper.extractEquationData(this.interpolatedDerivatives, this.interpolatedPrimaryDerivatives);
        return this.interpolatedPrimaryDerivatives;
    }

    @Override // org.apache.commons.math3.ode.sampling.StepInterpolator
    public double[] getInterpolatedSecondaryState(int index) throws MaxCountExceededException, DimensionMismatchException {
        evaluateCompleteInterpolatedState();
        this.secondaryMappers[index].extractEquationData(this.interpolatedState, this.interpolatedSecondaryState[index]);
        return this.interpolatedSecondaryState[index];
    }

    @Override // org.apache.commons.math3.ode.sampling.StepInterpolator
    public double[] getInterpolatedSecondaryDerivatives(int index) throws MaxCountExceededException, DimensionMismatchException {
        evaluateCompleteInterpolatedState();
        this.secondaryMappers[index].extractEquationData(this.interpolatedDerivatives, this.interpolatedSecondaryDerivatives[index]);
        return this.interpolatedSecondaryDerivatives[index];
    }

    public final void finalizeStep() throws MaxCountExceededException {
        if (!this.finalized) {
            doFinalize();
            this.finalized = true;
        }
    }

    protected void doFinalize() throws MaxCountExceededException {
    }

    protected void writeBaseExternal(ObjectOutput out) throws IOException {
        if (this.currentState == null) {
            out.writeInt(-1);
        } else {
            out.writeInt(this.currentState.length);
        }
        out.writeDouble(this.globalPreviousTime);
        out.writeDouble(this.globalCurrentTime);
        out.writeDouble(this.softPreviousTime);
        out.writeDouble(this.softCurrentTime);
        out.writeDouble(this.f13067h);
        out.writeBoolean(this.forward);
        out.writeObject(this.primaryMapper);
        out.write(this.secondaryMappers.length);
        EquationsMapper[] arr$ = this.secondaryMappers;
        for (EquationsMapper mapper : arr$) {
            out.writeObject(mapper);
        }
        if (this.currentState != null) {
            for (int i2 = 0; i2 < this.currentState.length; i2++) {
                out.writeDouble(this.currentState[i2]);
            }
        }
        out.writeDouble(this.interpolatedTime);
        try {
            finalizeStep();
        } catch (MaxCountExceededException mcee) {
            IOException ioe = new IOException(mcee.getLocalizedMessage());
            ioe.initCause(mcee);
            throw ioe;
        }
    }

    protected double readBaseExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        int dimension = in.readInt();
        this.globalPreviousTime = in.readDouble();
        this.globalCurrentTime = in.readDouble();
        this.softPreviousTime = in.readDouble();
        this.softCurrentTime = in.readDouble();
        this.f13067h = in.readDouble();
        this.forward = in.readBoolean();
        this.primaryMapper = (EquationsMapper) in.readObject();
        this.secondaryMappers = new EquationsMapper[in.read()];
        for (int i2 = 0; i2 < this.secondaryMappers.length; i2++) {
            this.secondaryMappers[i2] = (EquationsMapper) in.readObject();
        }
        this.dirtyState = true;
        if (dimension < 0) {
            this.currentState = null;
        } else {
            this.currentState = new double[dimension];
            for (int i3 = 0; i3 < this.currentState.length; i3++) {
                this.currentState[i3] = in.readDouble();
            }
        }
        this.interpolatedTime = Double.NaN;
        allocateInterpolatedArrays(dimension);
        this.finalized = true;
        return in.readDouble();
    }
}
