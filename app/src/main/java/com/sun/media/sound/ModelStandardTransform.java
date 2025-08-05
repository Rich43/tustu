package com.sun.media.sound;

/* loaded from: rt.jar:com/sun/media/sound/ModelStandardTransform.class */
public final class ModelStandardTransform implements ModelTransform {
    public static final boolean DIRECTION_MIN2MAX = false;
    public static final boolean DIRECTION_MAX2MIN = true;
    public static final boolean POLARITY_UNIPOLAR = false;
    public static final boolean POLARITY_BIPOLAR = true;
    public static final int TRANSFORM_LINEAR = 0;
    public static final int TRANSFORM_CONCAVE = 1;
    public static final int TRANSFORM_CONVEX = 2;
    public static final int TRANSFORM_SWITCH = 3;
    public static final int TRANSFORM_ABSOLUTE = 4;
    private boolean direction;
    private boolean polarity;
    private int transform;

    public ModelStandardTransform() {
        this.direction = false;
        this.polarity = false;
        this.transform = 0;
    }

    public ModelStandardTransform(boolean z2) {
        this.direction = false;
        this.polarity = false;
        this.transform = 0;
        this.direction = z2;
    }

    public ModelStandardTransform(boolean z2, boolean z3) {
        this.direction = false;
        this.polarity = false;
        this.transform = 0;
        this.direction = z2;
        this.polarity = z3;
    }

    public ModelStandardTransform(boolean z2, boolean z3, int i2) {
        this.direction = false;
        this.polarity = false;
        this.transform = 0;
        this.direction = z2;
        this.polarity = z3;
        this.transform = i2;
    }

    @Override // com.sun.media.sound.ModelTransform
    public double transform(double d2) {
        if (this.direction) {
            d2 = 1.0d - d2;
        }
        if (this.polarity) {
            d2 = (d2 * 2.0d) - 1.0d;
        }
        switch (this.transform) {
            case 1:
                double dSignum = Math.signum(d2);
                double dLog = (-(0.4166666666666667d / Math.log(10.0d))) * Math.log(1.0d - Math.abs(d2));
                if (dLog < 0.0d) {
                    dLog = 0.0d;
                } else if (dLog > 1.0d) {
                    dLog = 1.0d;
                }
                return dSignum * dLog;
            case 2:
                double dSignum2 = Math.signum(d2);
                double dLog2 = 1.0d + ((0.4166666666666667d / Math.log(10.0d)) * Math.log(Math.abs(d2)));
                if (dLog2 < 0.0d) {
                    dLog2 = 0.0d;
                } else if (dLog2 > 1.0d) {
                    dLog2 = 1.0d;
                }
                return dSignum2 * dLog2;
            case 3:
                return this.polarity ? d2 > 0.0d ? 1.0d : -1.0d : d2 > 0.5d ? 1.0d : 0.0d;
            case 4:
                return Math.abs(d2);
            default:
                return d2;
        }
    }

    public boolean getDirection() {
        return this.direction;
    }

    public void setDirection(boolean z2) {
        this.direction = z2;
    }

    public boolean getPolarity() {
        return this.polarity;
    }

    public void setPolarity(boolean z2) {
        this.polarity = z2;
    }

    public int getTransform() {
        return this.transform;
    }

    public void setTransform(int i2) {
        this.transform = i2;
    }
}
