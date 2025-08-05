package com.sun.jna;

/* loaded from: JavaFTD2XX.jar:com/sun/jna/NativeLong.class */
public class NativeLong extends IntegerType {
    public static final int SIZE = Native.LONG_SIZE;

    public NativeLong() {
        this(0L);
    }

    public NativeLong(long value) {
        super(SIZE, value);
    }
}
