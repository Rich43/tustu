package org.apache.commons.math3.analysis.interpolation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.random.UnitSphereRandomVectorGenerator;
import org.apache.commons.math3.util.FastMath;

@Deprecated
/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/interpolation/MicrosphereInterpolatingFunction.class */
public class MicrosphereInterpolatingFunction implements MultivariateFunction {
    private final int dimension;
    private final List<MicrosphereSurfaceElement> microsphere;
    private final double brightnessExponent;
    private final Map<RealVector, Double> samples;

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/interpolation/MicrosphereInterpolatingFunction$MicrosphereSurfaceElement.class */
    private static class MicrosphereSurfaceElement {
        private final RealVector normal;
        private double brightestIllumination;
        private Map.Entry<RealVector, Double> brightestSample;

        MicrosphereSurfaceElement(double[] n2) {
            this.normal = new ArrayRealVector(n2);
        }

        RealVector normal() {
            return this.normal;
        }

        void reset() {
            this.brightestIllumination = 0.0d;
            this.brightestSample = null;
        }

        void store(double illuminationFromSample, Map.Entry<RealVector, Double> sample) {
            if (illuminationFromSample > this.brightestIllumination) {
                this.brightestIllumination = illuminationFromSample;
                this.brightestSample = sample;
            }
        }

        double illumination() {
            return this.brightestIllumination;
        }

        Map.Entry<RealVector, Double> sample() {
            return this.brightestSample;
        }
    }

    public MicrosphereInterpolatingFunction(double[][] xval, double[] yval, int brightnessExponent, int microsphereElements, UnitSphereRandomVectorGenerator rand) throws NullArgumentException, NoDataException, DimensionMismatchException {
        if (xval == null || yval == null) {
            throw new NullArgumentException();
        }
        if (xval.length == 0) {
            throw new NoDataException();
        }
        if (xval.length != yval.length) {
            throw new DimensionMismatchException(xval.length, yval.length);
        }
        if (xval[0] == null) {
            throw new NullArgumentException();
        }
        this.dimension = xval[0].length;
        this.brightnessExponent = brightnessExponent;
        this.samples = new HashMap(yval.length);
        for (int i2 = 0; i2 < xval.length; i2++) {
            double[] xvalI = xval[i2];
            if (xvalI == null) {
                throw new NullArgumentException();
            }
            if (xvalI.length != this.dimension) {
                throw new DimensionMismatchException(xvalI.length, this.dimension);
            }
            this.samples.put(new ArrayRealVector(xvalI), Double.valueOf(yval[i2]));
        }
        this.microsphere = new ArrayList(microsphereElements);
        for (int i3 = 0; i3 < microsphereElements; i3++) {
            this.microsphere.add(new MicrosphereSurfaceElement(rand.nextVector()));
        }
    }

    @Override // org.apache.commons.math3.analysis.MultivariateFunction
    public double value(double[] point) throws OutOfRangeException, DimensionMismatchException {
        RealVector p2 = new ArrayRealVector(point);
        Iterator i$ = this.microsphere.iterator();
        while (i$.hasNext()) {
            i$.next().reset();
        }
        for (Map.Entry<RealVector, Double> sd : this.samples.entrySet()) {
            RealVector diff = sd.getKey().subtract(p2);
            double diffNorm = diff.getNorm();
            if (FastMath.abs(diffNorm) < FastMath.ulp(1.0d)) {
                return sd.getValue().doubleValue();
            }
            for (MicrosphereSurfaceElement md : this.microsphere) {
                double w2 = FastMath.pow(diffNorm, -this.brightnessExponent);
                md.store(cosAngle(diff, md.normal()) * w2, sd);
            }
        }
        double value = 0.0d;
        double totalWeight = 0.0d;
        for (MicrosphereSurfaceElement md2 : this.microsphere) {
            double iV = md2.illumination();
            Map.Entry<RealVector, Double> sd2 = md2.sample();
            if (sd2 != null) {
                value += iV * sd2.getValue().doubleValue();
                totalWeight += iV;
            }
        }
        return value / totalWeight;
    }

    private double cosAngle(RealVector v2, RealVector w2) {
        return v2.dotProduct(w2) / (v2.getNorm() * w2.getNorm());
    }
}
