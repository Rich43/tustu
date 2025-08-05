package org.apache.commons.math3.stat.correlation;

import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.util.LocalizedFormats;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/stat/correlation/StorelessBivariateCovariance.class */
class StorelessBivariateCovariance {
    private double meanX;
    private double meanY;

    /* renamed from: n, reason: collision with root package name */
    private double f13094n;
    private double covarianceNumerator;
    private boolean biasCorrected;

    StorelessBivariateCovariance() {
        this(true);
    }

    StorelessBivariateCovariance(boolean biasCorrection) {
        this.meanY = 0.0d;
        this.meanX = 0.0d;
        this.f13094n = 0.0d;
        this.covarianceNumerator = 0.0d;
        this.biasCorrected = biasCorrection;
    }

    public void increment(double x2, double y2) {
        this.f13094n += 1.0d;
        double deltaX = x2 - this.meanX;
        double deltaY = y2 - this.meanY;
        this.meanX += deltaX / this.f13094n;
        this.meanY += deltaY / this.f13094n;
        this.covarianceNumerator += ((this.f13094n - 1.0d) / this.f13094n) * deltaX * deltaY;
    }

    public void append(StorelessBivariateCovariance cov) {
        double oldN = this.f13094n;
        this.f13094n += cov.f13094n;
        double deltaX = cov.meanX - this.meanX;
        double deltaY = cov.meanY - this.meanY;
        this.meanX += (deltaX * cov.f13094n) / this.f13094n;
        this.meanY += (deltaY * cov.f13094n) / this.f13094n;
        this.covarianceNumerator += cov.covarianceNumerator + (((oldN * cov.f13094n) / this.f13094n) * deltaX * deltaY);
    }

    public double getN() {
        return this.f13094n;
    }

    public double getResult() throws NumberIsTooSmallException {
        if (this.f13094n < 2.0d) {
            throw new NumberIsTooSmallException(LocalizedFormats.INSUFFICIENT_DIMENSION, Double.valueOf(this.f13094n), 2, true);
        }
        if (this.biasCorrected) {
            return this.covarianceNumerator / (this.f13094n - 1.0d);
        }
        return this.covarianceNumerator / this.f13094n;
    }
}
