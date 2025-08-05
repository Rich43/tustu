package org.apache.commons.math3.ode.nonstiff;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import org.apache.commons.math3.ode.AbstractIntegrator;
import org.apache.commons.math3.ode.EquationsMapper;
import org.apache.commons.math3.ode.sampling.AbstractStepInterpolator;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ode/nonstiff/RungeKuttaStepInterpolator.class */
abstract class RungeKuttaStepInterpolator extends AbstractStepInterpolator {
    protected double[] previousState;
    protected double[][] yDotK;
    protected AbstractIntegrator integrator;

    protected RungeKuttaStepInterpolator() {
        this.previousState = null;
        this.yDotK = (double[][]) null;
        this.integrator = null;
    }

    /* JADX WARN: Type inference failed for: r1v12, types: [double[], double[][]] */
    RungeKuttaStepInterpolator(RungeKuttaStepInterpolator interpolator) {
        super(interpolator);
        if (interpolator.currentState != null) {
            this.previousState = (double[]) interpolator.previousState.clone();
            this.yDotK = new double[interpolator.yDotK.length];
            for (int k2 = 0; k2 < interpolator.yDotK.length; k2++) {
                this.yDotK[k2] = (double[]) interpolator.yDotK[k2].clone();
            }
        } else {
            this.previousState = null;
            this.yDotK = (double[][]) null;
        }
        this.integrator = null;
    }

    public void reinitialize(AbstractIntegrator rkIntegrator, double[] y2, double[][] yDotArray, boolean forward, EquationsMapper primaryMapper, EquationsMapper[] secondaryMappers) {
        reinitialize(y2, forward, primaryMapper, secondaryMappers);
        this.previousState = null;
        this.yDotK = yDotArray;
        this.integrator = rkIntegrator;
    }

    @Override // org.apache.commons.math3.ode.sampling.AbstractStepInterpolator
    public void shift() {
        this.previousState = (double[]) this.currentState.clone();
        super.shift();
    }

    @Override // org.apache.commons.math3.ode.sampling.AbstractStepInterpolator, java.io.Externalizable
    public void writeExternal(ObjectOutput out) throws IOException {
        writeBaseExternal(out);
        int n2 = this.currentState == null ? -1 : this.currentState.length;
        for (int i2 = 0; i2 < n2; i2++) {
            out.writeDouble(this.previousState[i2]);
        }
        int kMax = this.yDotK == null ? -1 : this.yDotK.length;
        out.writeInt(kMax);
        for (int k2 = 0; k2 < kMax; k2++) {
            for (int i3 = 0; i3 < n2; i3++) {
                out.writeDouble(this.yDotK[k2][i3]);
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.apache.commons.math3.ode.sampling.AbstractStepInterpolator, java.io.Externalizable
    public void readExternal(ObjectInput objectInput) throws IOException, ClassNotFoundException {
        double baseExternal = readBaseExternal(objectInput);
        int length = this.currentState == null ? -1 : this.currentState.length;
        if (length < 0) {
            this.previousState = null;
        } else {
            this.previousState = new double[length];
            for (int i2 = 0; i2 < length; i2++) {
                this.previousState[i2] = objectInput.readDouble();
            }
        }
        int i3 = objectInput.readInt();
        this.yDotK = i3 < 0 ? (double[][]) null : new double[i3];
        for (int i4 = 0; i4 < i3; i4++) {
            this.yDotK[i4] = length < 0 ? null : new double[length];
            for (int i5 = 0; i5 < length; i5++) {
                this.yDotK[i4][i5] = objectInput.readDouble();
            }
        }
        this.integrator = null;
        if (this.currentState != null) {
            setInterpolatedTime(baseExternal);
        } else {
            this.interpolatedTime = baseExternal;
        }
    }
}
