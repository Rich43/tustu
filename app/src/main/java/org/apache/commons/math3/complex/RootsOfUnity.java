package org.apache.commons.math3.complex;

import java.io.Serializable;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.ZeroException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/complex/RootsOfUnity.class */
public class RootsOfUnity implements Serializable {
    private static final long serialVersionUID = 20120201;
    private int omegaCount = 0;
    private double[] omegaReal = null;
    private double[] omegaImaginaryCounterClockwise = null;
    private double[] omegaImaginaryClockwise = null;
    private boolean isCounterClockWise = true;

    public synchronized boolean isCounterClockWise() throws MathIllegalStateException {
        if (this.omegaCount == 0) {
            throw new MathIllegalStateException(LocalizedFormats.ROOTS_OF_UNITY_NOT_COMPUTED_YET, new Object[0]);
        }
        return this.isCounterClockWise;
    }

    public synchronized void computeRoots(int n2) throws ZeroException {
        if (n2 == 0) {
            throw new ZeroException(LocalizedFormats.CANNOT_COMPUTE_0TH_ROOT_OF_UNITY, new Object[0]);
        }
        this.isCounterClockWise = n2 > 0;
        int absN = FastMath.abs(n2);
        if (absN == this.omegaCount) {
            return;
        }
        double t2 = 6.283185307179586d / absN;
        double cosT = FastMath.cos(t2);
        double sinT = FastMath.sin(t2);
        this.omegaReal = new double[absN];
        this.omegaImaginaryCounterClockwise = new double[absN];
        this.omegaImaginaryClockwise = new double[absN];
        this.omegaReal[0] = 1.0d;
        this.omegaImaginaryCounterClockwise[0] = 0.0d;
        this.omegaImaginaryClockwise[0] = 0.0d;
        for (int i2 = 1; i2 < absN; i2++) {
            this.omegaReal[i2] = (this.omegaReal[i2 - 1] * cosT) - (this.omegaImaginaryCounterClockwise[i2 - 1] * sinT);
            this.omegaImaginaryCounterClockwise[i2] = (this.omegaReal[i2 - 1] * sinT) + (this.omegaImaginaryCounterClockwise[i2 - 1] * cosT);
            this.omegaImaginaryClockwise[i2] = -this.omegaImaginaryCounterClockwise[i2];
        }
        this.omegaCount = absN;
    }

    public synchronized double getReal(int k2) throws MathIllegalStateException, MathIllegalArgumentException {
        if (this.omegaCount == 0) {
            throw new MathIllegalStateException(LocalizedFormats.ROOTS_OF_UNITY_NOT_COMPUTED_YET, new Object[0]);
        }
        if (k2 < 0 || k2 >= this.omegaCount) {
            throw new OutOfRangeException(LocalizedFormats.OUT_OF_RANGE_ROOT_OF_UNITY_INDEX, Integer.valueOf(k2), 0, Integer.valueOf(this.omegaCount - 1));
        }
        return this.omegaReal[k2];
    }

    public synchronized double getImaginary(int k2) throws OutOfRangeException, MathIllegalStateException {
        if (this.omegaCount == 0) {
            throw new MathIllegalStateException(LocalizedFormats.ROOTS_OF_UNITY_NOT_COMPUTED_YET, new Object[0]);
        }
        if (k2 < 0 || k2 >= this.omegaCount) {
            throw new OutOfRangeException(LocalizedFormats.OUT_OF_RANGE_ROOT_OF_UNITY_INDEX, Integer.valueOf(k2), 0, Integer.valueOf(this.omegaCount - 1));
        }
        return this.isCounterClockWise ? this.omegaImaginaryCounterClockwise[k2] : this.omegaImaginaryClockwise[k2];
    }

    public synchronized int getNumberOfRoots() {
        return this.omegaCount;
    }
}
