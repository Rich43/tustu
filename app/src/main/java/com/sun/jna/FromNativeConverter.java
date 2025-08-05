package com.sun.jna;

/* loaded from: JavaFTD2XX.jar:com/sun/jna/FromNativeConverter.class */
public interface FromNativeConverter {
    Object fromNative(Object obj, FromNativeContext fromNativeContext);

    Class nativeType();
}
