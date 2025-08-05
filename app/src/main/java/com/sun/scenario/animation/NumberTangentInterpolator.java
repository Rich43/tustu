package com.sun.scenario.animation;

import com.sun.javafx.animation.TickCalculation;
import javafx.animation.Interpolator;
import javafx.util.Duration;

/* loaded from: jfxrt.jar:com/sun/scenario/animation/NumberTangentInterpolator.class */
public class NumberTangentInterpolator extends Interpolator {
    private final double inValue;
    private final double outValue;
    private final long inTicks;
    private final long outTicks;

    public double getInValue() {
        return this.inValue;
    }

    public double getOutValue() {
        return this.outValue;
    }

    public double getInTicks() {
        return this.inTicks;
    }

    public double getOutTicks() {
        return this.outTicks;
    }

    public NumberTangentInterpolator(Duration inDuration, double inValue, Duration outDuration, double outValue) {
        this.inTicks = TickCalculation.fromDuration(inDuration);
        this.inValue = inValue;
        this.outTicks = TickCalculation.fromDuration(outDuration);
        this.outValue = outValue;
    }

    public NumberTangentInterpolator(Duration duration, double value) {
        long jFromDuration = TickCalculation.fromDuration(duration);
        this.inTicks = jFromDuration;
        this.outTicks = jFromDuration;
        this.outValue = value;
        this.inValue = value;
    }

    public String toString() {
        return "NumberTangentInterpolator [inValue=" + this.inValue + ", inDuration=" + ((Object) TickCalculation.toDuration(this.inTicks)) + ", outValue=" + this.outValue + ", outDuration=" + ((Object) TickCalculation.toDuration(this.outTicks)) + "]";
    }

    public int hashCode() {
        int hash = (59 * 7) + ((int) (Double.doubleToLongBits(this.inValue) ^ (Double.doubleToLongBits(this.inValue) >>> 32)));
        return (59 * ((59 * ((59 * hash) + ((int) (Double.doubleToLongBits(this.outValue) ^ (Double.doubleToLongBits(this.outValue) >>> 32))))) + ((int) (this.inTicks ^ (this.inTicks >>> 32))))) + ((int) (this.outTicks ^ (this.outTicks >>> 32)));
    }

    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        NumberTangentInterpolator other = (NumberTangentInterpolator) obj;
        if (Double.doubleToLongBits(this.inValue) != Double.doubleToLongBits(other.inValue) || Double.doubleToLongBits(this.outValue) != Double.doubleToLongBits(other.outValue) || this.inTicks != other.inTicks || this.outTicks != other.outTicks) {
            return false;
        }
        return true;
    }

    @Override // javafx.animation.Interpolator
    protected double curve(double t2) {
        return t2;
    }
}
