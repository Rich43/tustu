package com.sun.javafx.css;

import javafx.scene.text.Font;

/* loaded from: jfxrt.jar:com/sun/javafx/css/Size.class */
public final class Size {
    private final double value;
    private final SizeUnits units;

    public Size(double value, SizeUnits units) {
        this.value = value;
        this.units = units != null ? units : SizeUnits.PX;
    }

    public double getValue() {
        return this.value;
    }

    public SizeUnits getUnits() {
        return this.units;
    }

    public boolean isAbsolute() {
        return this.units.isAbsolute();
    }

    public double points(Font font) {
        return points(1.0d, font);
    }

    public double points(double multiplier, Font font) {
        return this.units.points(this.value, multiplier, font);
    }

    public double pixels(double multiplier, Font font) {
        return this.units.pixels(this.value, multiplier, font);
    }

    public double pixels(Font font) {
        return pixels(1.0d, font);
    }

    public double pixels(double multiplier) {
        return pixels(multiplier, null);
    }

    public double pixels() {
        return pixels(1.0d, null);
    }

    public String toString() {
        return Double.toString(this.value) + this.units.toString();
    }

    public int hashCode() {
        long bits = (37 * ((37 * 17) + Double.doubleToLongBits(this.value))) + this.units.hashCode();
        return (int) (bits ^ (bits >> 32));
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }
        Size other = (Size) obj;
        if (this.units != other.units) {
            return false;
        }
        if (this.value == other.value) {
            return true;
        }
        if (this.value > 0.0d) {
            if (other.value <= 0.0d) {
                return false;
            }
        } else if (other.value >= 0.0d) {
            return false;
        }
        double d2 = this.value > 0.0d ? this.value : -this.value;
        double d3 = other.value > 0.0d ? other.value : -other.value;
        double diff = this.value - other.value;
        if (diff < -1.0E-6d || 1.0E-6d < diff) {
            return false;
        }
        return true;
    }
}
