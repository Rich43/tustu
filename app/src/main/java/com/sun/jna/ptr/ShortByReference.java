package com.sun.jna.ptr;

/* loaded from: JavaFTD2XX.jar:com/sun/jna/ptr/ShortByReference.class */
public class ShortByReference extends ByReference {
    public ShortByReference() {
        this((short) 0);
    }

    public ShortByReference(short value) {
        super(2);
        setValue(value);
    }

    public void setValue(short value) {
        getPointer().setShort(0L, value);
    }

    public short getValue() {
        return getPointer().getShort(0L);
    }
}
