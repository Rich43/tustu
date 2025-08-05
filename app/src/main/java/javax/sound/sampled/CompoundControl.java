package javax.sound.sampled;

import javax.sound.sampled.Control;

/* loaded from: rt.jar:javax/sound/sampled/CompoundControl.class */
public abstract class CompoundControl extends Control {
    private Control[] controls;

    protected CompoundControl(Type type, Control[] controlArr) {
        super(type);
        this.controls = controlArr;
    }

    public Control[] getMemberControls() {
        Control[] controlArr = new Control[this.controls.length];
        for (int i2 = 0; i2 < this.controls.length; i2++) {
            controlArr[i2] = this.controls[i2];
        }
        return controlArr;
    }

    @Override // javax.sound.sampled.Control
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i2 = 0; i2 < this.controls.length; i2++) {
            if (i2 != 0) {
                stringBuffer.append(", ");
                if (i2 + 1 == this.controls.length) {
                    stringBuffer.append("and ");
                }
            }
            stringBuffer.append((Object) this.controls[i2].getType());
        }
        return new String(((Object) getType()) + " Control containing " + ((Object) stringBuffer) + " Controls.");
    }

    /* loaded from: rt.jar:javax/sound/sampled/CompoundControl$Type.class */
    public static class Type extends Control.Type {
        protected Type(String str) {
            super(str);
        }
    }
}
