package com.sun.jna;

/* loaded from: JavaFTD2XX.jar:com/sun/jna/NativeMapped.class */
public interface NativeMapped {
    Object fromNative(Object obj, FromNativeContext fromNativeContext);

    Object toNative();

    Class nativeType();
}
