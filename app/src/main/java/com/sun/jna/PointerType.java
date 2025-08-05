package com.sun.jna;

/* loaded from: JavaFTD2XX.jar:com/sun/jna/PointerType.class */
public abstract class PointerType implements NativeMapped {
    private Pointer pointer;
    static Class class$com$sun$jna$Pointer;

    protected PointerType() {
        this.pointer = Pointer.NULL;
    }

    protected PointerType(Pointer p2) {
        this.pointer = p2;
    }

    @Override // com.sun.jna.NativeMapped
    public Class nativeType() throws Throwable {
        if (class$com$sun$jna$Pointer != null) {
            return class$com$sun$jna$Pointer;
        }
        Class clsClass$ = class$("com.sun.jna.Pointer");
        class$com$sun$jna$Pointer = clsClass$;
        return clsClass$;
    }

    static Class class$(String x0) throws Throwable {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError().initCause(x1);
        }
    }

    @Override // com.sun.jna.NativeMapped
    public Object toNative() {
        return getPointer();
    }

    public Pointer getPointer() {
        return this.pointer;
    }

    public void setPointer(Pointer p2) {
        this.pointer = p2;
    }

    @Override // com.sun.jna.NativeMapped
    public Object fromNative(Object nativeValue, FromNativeContext context) {
        if (nativeValue == null) {
            return null;
        }
        try {
            PointerType pt = (PointerType) getClass().newInstance();
            pt.pointer = (Pointer) nativeValue;
            return pt;
        } catch (IllegalAccessException e2) {
            throw new IllegalArgumentException(new StringBuffer().append("Not allowed to instantiate ").append((Object) getClass()).toString());
        } catch (InstantiationException e3) {
            throw new IllegalArgumentException(new StringBuffer().append("Can't instantiate ").append((Object) getClass()).toString());
        }
    }

    public int hashCode() {
        if (this.pointer != null) {
            return this.pointer.hashCode();
        }
        return 0;
    }

    public boolean equals(Object o2) {
        if (o2 == this) {
            return true;
        }
        if (o2 instanceof PointerType) {
            Pointer p2 = ((PointerType) o2).getPointer();
            if (this.pointer == null) {
                return p2 == null;
            }
            return this.pointer.equals(p2);
        }
        return false;
    }

    public String toString() {
        return this.pointer == null ? "NULL" : this.pointer.toString();
    }
}
