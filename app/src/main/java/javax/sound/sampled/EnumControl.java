package javax.sound.sampled;

import javax.sound.sampled.Control;

/* loaded from: rt.jar:javax/sound/sampled/EnumControl.class */
public abstract class EnumControl extends Control {
    private Object[] values;
    private Object value;

    protected EnumControl(Type type, Object[] objArr, Object obj) {
        super(type);
        this.values = objArr;
        this.value = obj;
    }

    public void setValue(Object obj) {
        if (!isValueSupported(obj)) {
            throw new IllegalArgumentException("Requested value " + obj + " is not supported.");
        }
        this.value = obj;
    }

    public Object getValue() {
        return this.value;
    }

    public Object[] getValues() {
        Object[] objArr = new Object[this.values.length];
        for (int i2 = 0; i2 < this.values.length; i2++) {
            objArr[i2] = this.values[i2];
        }
        return objArr;
    }

    private boolean isValueSupported(Object obj) {
        for (int i2 = 0; i2 < this.values.length; i2++) {
            if (obj.equals(this.values[i2])) {
                return true;
            }
        }
        return false;
    }

    @Override // javax.sound.sampled.Control
    public String toString() {
        return new String(((Object) getType()) + " with current value: " + getValue());
    }

    /* loaded from: rt.jar:javax/sound/sampled/EnumControl$Type.class */
    public static class Type extends Control.Type {
        public static final Type REVERB = new Type("Reverb");

        protected Type(String str) {
            super(str);
        }
    }
}
