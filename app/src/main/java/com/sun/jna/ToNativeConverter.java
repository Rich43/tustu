package com.sun.jna;

/* loaded from: JavaFTD2XX.jar:com/sun/jna/ToNativeConverter.class */
public interface ToNativeConverter {
    Object toNative(Object obj, ToNativeContext toNativeContext);

    Class nativeType();
}
