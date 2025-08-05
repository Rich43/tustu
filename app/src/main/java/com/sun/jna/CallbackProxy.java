package com.sun.jna;

/* loaded from: JavaFTD2XX.jar:com/sun/jna/CallbackProxy.class */
public interface CallbackProxy extends Callback {
    Object callback(Object[] objArr);

    Class[] getParameterTypes();

    Class getReturnType();
}
