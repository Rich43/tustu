package com.sun.scenario.animation;

import javafx.animation.Interpolator;

/* loaded from: jfxrt.jar:com/sun/scenario/animation/SplineInterpolator.class */
public class SplineInterpolator extends Interpolator {
    private final double x1;
    private final double y1;
    private final double x2;
    private final double y2;
    private final boolean isCurveLinear;
    private static final int SAMPLE_SIZE = 16;
    private static final double SAMPLE_INCREMENT = 0.0625d;
    private final double[] xSamples = new double[17];

    public SplineInterpolator(double px1, double py1, double px2, double py2) {
        if (px1 < 0.0d || px1 > 1.0d || py1 < 0.0d || py1 > 1.0d || px2 < 0.0d || px2 > 1.0d || py2 < 0.0d || py2 > 1.0d) {
            throw new IllegalArgumentException("Control point coordinates must all be in range [0,1]");
        }
        this.x1 = px1;
        this.y1 = py1;
        this.x2 = px2;
        this.y2 = py2;
        this.isCurveLinear = this.x1 == this.y1 && this.x2 == this.y2;
        if (!this.isCurveLinear) {
            for (int i2 = 0; i2 < 17; i2++) {
                this.xSamples[i2] = eval(i2 * SAMPLE_INCREMENT, this.x1, this.x2);
            }
        }
    }

    public double getX1() {
        return this.x1;
    }

    public double getY1() {
        return this.y1;
    }

    public double getX2() {
        return this.x2;
    }

    public double getY2() {
        return this.y2;
    }

    public int hashCode() {
        int hash = (19 * 7) + ((int) (Double.doubleToLongBits(this.x1) ^ (Double.doubleToLongBits(this.x1) >>> 32)));
        return (19 * ((19 * ((19 * hash) + ((int) (Double.doubleToLongBits(this.y1) ^ (Double.doubleToLongBits(this.y1) >>> 32))))) + ((int) (Double.doubleToLongBits(this.x2) ^ (Double.doubleToLongBits(this.x2) >>> 32))))) + ((int) (Double.doubleToLongBits(this.y2) ^ (Double.doubleToLongBits(this.y2) >>> 32)));
    }

    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        SplineInterpolator other = (SplineInterpolator) obj;
        if (Double.doubleToLongBits(this.x1) != Double.doubleToLongBits(other.x1) || Double.doubleToLongBits(this.y1) != Double.doubleToLongBits(other.y1) || Double.doubleToLongBits(this.x2) != Double.doubleToLongBits(other.x2) || Double.doubleToLongBits(this.y2) != Double.doubleToLongBits(other.y2)) {
            return false;
        }
        return true;
    }

    @Override // javafx.animation.Interpolator
    public double curve(double x2) {
        if (x2 < 0.0d || x2 > 1.0d) {
            throw new IllegalArgumentException("x must be in range [0,1]");
        }
        if (this.isCurveLinear || x2 == 0.0d || x2 == 1.0d) {
            return x2;
        }
        return eval(findTForX(x2), this.y1, this.y2);
    }

    private double eval(double t2, double p1, double p2) {
        double compT = 1.0d - t2;
        return t2 * ((3.0d * compT * ((compT * p1) + (t2 * p2))) + (t2 * t2));
    }

    private double evalDerivative(double t2, double p1, double p2) {
        double compT = 1.0d - t2;
        return 3.0d * ((compT * ((compT * p1) + (2.0d * t2 * (p2 - p1)))) + (t2 * t2 * (1.0d - p2)));
    }

    private double getInitialGuessForT(double x2) {
        for (int i2 = 1; i2 < 17; i2++) {
            if (this.xSamples[i2] >= x2) {
                double xRange = this.xSamples[i2] - this.xSamples[i2 - 1];
                if (xRange == 0.0d) {
                    return (i2 - 1) * SAMPLE_INCREMENT;
                }
                return ((i2 - 1) + ((x2 - this.xSamples[i2 - 1]) / xRange)) * SAMPLE_INCREMENT;
            }
        }
        return 1.0d;
    }

    private double findTForX(double x2) {
        double t2 = getInitialGuessForT(x2);
        for (int i2 = 0; i2 < 4; i2++) {
            double xT = eval(t2, this.x1, this.x2) - x2;
            if (xT == 0.0d) {
                break;
            }
            double dXdT = evalDerivative(t2, this.x1, this.x2);
            if (dXdT == 0.0d) {
                break;
            }
            t2 -= xT / dXdT;
        }
        return t2;
    }

    public String toString() {
        return "SplineInterpolator [x1=" + this.x1 + ", y1=" + this.y1 + ", x2=" + this.x2 + ", y2=" + this.y2 + "]";
    }
}
