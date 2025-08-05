package com.sun.jna.ptr;

import com.sun.jna.Pointer;

/* loaded from: JavaFTD2XX.jar:com/sun/jna/ptr/PointerByReference.class */
public class PointerByReference extends ByReference {
    public PointerByReference() {
        this(null);
    }

    public PointerByReference(Pointer value) {
        super(Pointer.SIZE);
        setValue(value);
    }

    public void setValue(Pointer value) {
        getPointer().setPointer(0L, value);
    }

    public Pointer getValue() {
        return getPointer().getPointer(0L);
    }
}
