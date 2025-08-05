package javax.sound.sampled;

import javax.sound.sampled.Control;

/* loaded from: rt.jar:javax/sound/sampled/BooleanControl.class */
public abstract class BooleanControl extends Control {
    private final String trueStateLabel;
    private final String falseStateLabel;
    private boolean value;

    protected BooleanControl(Type type, boolean z2, String str, String str2) {
        super(type);
        this.value = z2;
        this.trueStateLabel = str;
        this.falseStateLabel = str2;
    }

    protected BooleanControl(Type type, boolean z2) {
        this(type, z2, "true", "false");
    }

    public void setValue(boolean z2) {
        this.value = z2;
    }

    public boolean getValue() {
        return this.value;
    }

    public String getStateLabel(boolean z2) {
        return z2 ? this.trueStateLabel : this.falseStateLabel;
    }

    @Override // javax.sound.sampled.Control
    public String toString() {
        return new String(super.toString() + " with current value: " + getStateLabel(getValue()));
    }

    /* loaded from: rt.jar:javax/sound/sampled/BooleanControl$Type.class */
    public static class Type extends Control.Type {
        public static final Type MUTE = new Type("Mute");
        public static final Type APPLY_REVERB = new Type("Apply Reverb");

        protected Type(String str) {
            super(str);
        }
    }
}
