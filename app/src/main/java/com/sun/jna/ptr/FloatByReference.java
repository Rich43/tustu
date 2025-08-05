package com.sun.jna.ptr;

/* loaded from: JavaFTD2XX.jar:com/sun/jna/ptr/FloatByReference.class */
public class FloatByReference extends ByReference {
    public FloatByReference() {
        this(0.0f);
    }

    public FloatByReference(float value) {
        super(4);
        setValue(value);
    }

    public void setValue(float value) {
        getPointer().setFloat(0L, value);
    }

    public float getValue() {
        return getPointer().getFloat(0L);
    }
}
