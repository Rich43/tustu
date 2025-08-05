package javax.sound.sampled;

import javax.sound.sampled.Control;

/* loaded from: rt.jar:javax/sound/sampled/FloatControl.class */
public abstract class FloatControl extends Control {
    private float minimum;
    private float maximum;
    private float precision;
    private int updatePeriod;
    private final String units;
    private final String minLabel;
    private final String maxLabel;
    private final String midLabel;
    private float value;

    protected FloatControl(Type type, float f2, float f3, float f4, int i2, float f5, String str, String str2, String str3, String str4) {
        super(type);
        if (f2 > f3) {
            throw new IllegalArgumentException("Minimum value " + f2 + " exceeds maximum value " + f3 + ".");
        }
        if (f5 < f2) {
            throw new IllegalArgumentException("Initial value " + f5 + " smaller than allowable minimum value " + f2 + ".");
        }
        if (f5 > f3) {
            throw new IllegalArgumentException("Initial value " + f5 + " exceeds allowable maximum value " + f3 + ".");
        }
        this.minimum = f2;
        this.maximum = f3;
        this.precision = f4;
        this.updatePeriod = i2;
        this.value = f5;
        this.units = str;
        this.minLabel = str2 == null ? "" : str2;
        this.midLabel = str3 == null ? "" : str3;
        this.maxLabel = str4 == null ? "" : str4;
    }

    protected FloatControl(Type type, float f2, float f3, float f4, int i2, float f5, String str) {
        this(type, f2, f3, f4, i2, f5, str, "", "", "");
    }

    public void setValue(float f2) {
        if (f2 > this.maximum) {
            throw new IllegalArgumentException("Requested value " + f2 + " exceeds allowable maximum value " + this.maximum + ".");
        }
        if (f2 < this.minimum) {
            throw new IllegalArgumentException("Requested value " + f2 + " smaller than allowable minimum value " + this.minimum + ".");
        }
        this.value = f2;
    }

    public float getValue() {
        return this.value;
    }

    public float getMaximum() {
        return this.maximum;
    }

    public float getMinimum() {
        return this.minimum;
    }

    public String getUnits() {
        return this.units;
    }

    public String getMinLabel() {
        return this.minLabel;
    }

    public String getMidLabel() {
        return this.midLabel;
    }

    public String getMaxLabel() {
        return this.maxLabel;
    }

    public float getPrecision() {
        return this.precision;
    }

    public int getUpdatePeriod() {
        return this.updatePeriod;
    }

    public void shift(float f2, float f3, int i2) {
        if (f2 < this.minimum) {
            throw new IllegalArgumentException("Requested value " + f2 + " smaller than allowable minimum value " + this.minimum + ".");
        }
        if (f2 > this.maximum) {
            throw new IllegalArgumentException("Requested value " + f2 + " exceeds allowable maximum value " + this.maximum + ".");
        }
        setValue(f3);
    }

    @Override // javax.sound.sampled.Control
    public String toString() {
        return new String(((Object) getType()) + " with current value: " + getValue() + " " + this.units + " (range: " + this.minimum + " - " + this.maximum + ")");
    }

    /* loaded from: rt.jar:javax/sound/sampled/FloatControl$Type.class */
    public static class Type extends Control.Type {
        public static final Type MASTER_GAIN = new Type("Master Gain");
        public static final Type AUX_SEND = new Type("AUX Send");
        public static final Type AUX_RETURN = new Type("AUX Return");
        public static final Type REVERB_SEND = new Type("Reverb Send");
        public static final Type REVERB_RETURN = new Type("Reverb Return");
        public static final Type VOLUME = new Type("Volume");
        public static final Type PAN = new Type("Pan");
        public static final Type BALANCE = new Type("Balance");
        public static final Type SAMPLE_RATE = new Type("Sample Rate");

        protected Type(String str) {
            super(str);
        }
    }
}
