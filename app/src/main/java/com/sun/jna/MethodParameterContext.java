package com.sun.jna;

import java.lang.reflect.Method;

/* loaded from: JavaFTD2XX.jar:com/sun/jna/MethodParameterContext.class */
public class MethodParameterContext extends FunctionParameterContext {
    private Method method;

    MethodParameterContext(Function f2, Object[] args, int index, Method m2) {
        super(f2, args, index);
        this.method = m2;
    }

    public Method getMethod() {
        return this.method;
    }
}
