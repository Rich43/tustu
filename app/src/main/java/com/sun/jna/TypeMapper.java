package com.sun.jna;

/* loaded from: JavaFTD2XX.jar:com/sun/jna/TypeMapper.class */
public interface TypeMapper {
    FromNativeConverter getFromNativeConverter(Class cls);

    ToNativeConverter getToNativeConverter(Class cls);
}
