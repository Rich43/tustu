package com.sun.jna;

import java.lang.reflect.Method;

/* loaded from: JavaFTD2XX.jar:com/sun/jna/CallbackParameterContext.class */
public class CallbackParameterContext extends FromNativeContext {
    private Method method;
    private Object[] args;
    private int index;

    CallbackParameterContext(Class javaType, Method m2, Object[] args, int index) {
        super(javaType);
        this.method = m2;
        this.args = args;
        this.index = index;
    }

    public Method getMethod() {
        return this.method;
    }

    public Object[] getArguments() {
        return this.args;
    }

    public int getIndex() {
        return this.index;
    }
}
