package com.sun.jna;

import java.util.Arrays;
import java.util.Collection;

/* loaded from: JavaFTD2XX.jar:com/sun/jna/Callback.class */
public interface Callback {
    public static final String METHOD_NAME = "callback";
    public static final Collection FORBIDDEN_NAMES = Arrays.asList("hashCode", "equals", "toString");

    /* loaded from: JavaFTD2XX.jar:com/sun/jna/Callback$UncaughtExceptionHandler.class */
    public interface UncaughtExceptionHandler {
        void uncaughtException(Callback callback, Throwable th);
    }
}
