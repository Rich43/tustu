package org.apache.commons.math3.ode.sampling;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Arrays;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.ode.EquationsMapper;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ode/sampling/NordsieckStepInterpolator.class */
public class NordsieckStepInterpolator extends AbstractStepInterpolator {
    private static final long serialVersionUID = -7179861704951334960L;
    protected double[] stateVariation;
    private double scalingH;
    private double referenceTime;
    private double[] scaled;
    private Array2DRowRealMatrix nordsieck;

    public NordsieckStepInterpolator() {
    }

    public NordsieckStepInterpolator(NordsieckStepInterpolator interpolator) {
        super(interpolator);
        this.scalingH = interpolator.scalingH;
        this.referenceTime = interpolator.referenceTime;
        if (interpolator.scaled != null) {
            this.scaled = (double[]) interpolator.scaled.clone();
        }
        if (interpolator.nordsieck != null) {
            this.nordsieck = new Array2DRowRealMatrix(interpolator.nordsieck.getDataRef(), true);
        }
        if (interpolator.stateVariation != null) {
            this.stateVariation = (double[]) interpolator.stateVariation.clone();
        }
    }

    @Override // org.apache.commons.math3.ode.sampling.AbstractStepInterpolator
    protected StepInterpolator doCopy() {
        return new NordsieckStepInterpolator(this);
    }

    @Override // org.apache.commons.math3.ode.sampling.AbstractStepInterpolator
    public void reinitialize(double[] y2, boolean forward, EquationsMapper primaryMapper, EquationsMapper[] secondaryMappers) {
        super.reinitialize(y2, forward, primaryMapper, secondaryMappers);
        this.stateVariation = new double[y2.length];
    }

    public void reinitialize(double time, double stepSize, double[] scaledDerivative, Array2DRowRealMatrix nordsieckVector) {
        this.referenceTime = time;
        this.scalingH = stepSize;
        this.scaled = scaledDerivative;
        this.nordsieck = nordsieckVector;
        setInterpolatedTime(getInterpolatedTime());
    }

    public void rescale(double stepSize) {
        double ratio = stepSize / this.scalingH;
        for (int i2 = 0; i2 < this.scaled.length; i2++) {
            double[] dArr = this.scaled;
            int i3 = i2;
            dArr[i3] = dArr[i3] * ratio;
        }
        double[][] nData = this.nordsieck.getDataRef();
        double power = ratio;
        for (double[] nDataI : nData) {
            power *= ratio;
            for (int j2 = 0; j2 < nDataI.length; j2++) {
                int i4 = j2;
                nDataI[i4] = nDataI[i4] * power;
            }
        }
        this.scalingH = stepSize;
    }

    public double[] getInterpolatedStateVariation() throws MaxCountExceededException {
        getInterpolatedState();
        return this.stateVariation;
    }

    @Override // org.apache.commons.math3.ode.sampling.AbstractStepInterpolator
    protected void computeInterpolatedStateAndDerivatives(double theta, double oneMinusThetaH) {
        double x2 = this.interpolatedTime - this.referenceTime;
        double normalizedAbscissa = x2 / this.scalingH;
        Arrays.fill(this.stateVariation, 0.0d);
        Arrays.fill(this.interpolatedDerivatives, 0.0d);
        double[][] nData = this.nordsieck.getDataRef();
        for (int i2 = nData.length - 1; i2 >= 0; i2--) {
            int order = i2 + 2;
            double[] nDataI = nData[i2];
            double power = FastMath.pow(normalizedAbscissa, order);
            for (int j2 = 0; j2 < nDataI.length; j2++) {
                double d2 = nDataI[j2] * power;
                double[] dArr = this.stateVariation;
                int i3 = j2;
                dArr[i3] = dArr[i3] + d2;
                double[] dArr2 = this.interpolatedDerivatives;
                int i4 = j2;
                dArr2[i4] = dArr2[i4] + (order * d2);
            }
        }
        for (int j3 = 0; j3 < this.currentState.length; j3++) {
            double[] dArr3 = this.stateVariation;
            int i5 = j3;
            dArr3[i5] = dArr3[i5] + (this.scaled[j3] * normalizedAbscissa);
            this.interpolatedState[j3] = this.currentState[j3] + this.stateVariation[j3];
            this.interpolatedDerivatives[j3] = (this.interpolatedDerivatives[j3] + (this.scaled[j3] * normalizedAbscissa)) / x2;
        }
    }

    @Override // org.apache.commons.math3.ode.sampling.AbstractStepInterpolator, java.io.Externalizable
    public void writeExternal(ObjectOutput out) throws IOException {
        writeBaseExternal(out);
        out.writeDouble(this.scalingH);
        out.writeDouble(this.referenceTime);
        int n2 = this.currentState == null ? -1 : this.currentState.length;
        if (this.scaled == null) {
            out.writeBoolean(false);
        } else {
            out.writeBoolean(true);
            for (int j2 = 0; j2 < n2; j2++) {
                out.writeDouble(this.scaled[j2]);
            }
        }
        if (this.nordsieck == null) {
            out.writeBoolean(false);
        } else {
            out.writeBoolean(true);
            out.writeObject(this.nordsieck);
        }
    }

    @Override // org.apache.commons.math3.ode.sampling.AbstractStepInterpolator, java.io.Externalizable
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        double t2 = readBaseExternal(in);
        this.scalingH = in.readDouble();
        this.referenceTime = in.readDouble();
        int n2 = this.currentState == null ? -1 : this.currentState.length;
        boolean hasScaled = in.readBoolean();
        if (hasScaled) {
            this.scaled = new double[n2];
            for (int j2 = 0; j2 < n2; j2++) {
                this.scaled[j2] = in.readDouble();
            }
        } else {
            this.scaled = null;
        }
        boolean hasNordsieck = in.readBoolean();
        if (hasNordsieck) {
            this.nordsieck = (Array2DRowRealMatrix) in.readObject();
        } else {
            this.nordsieck = null;
        }
        if (hasScaled && hasNordsieck) {
            this.stateVariation = new double[n2];
            setInterpolatedTime(t2);
        } else {
            this.stateVariation = null;
        }
    }
}
