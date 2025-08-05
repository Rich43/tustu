package com.sun.jna;

/* loaded from: JavaFTD2XX.jar:com/sun/jna/FunctionResultContext.class */
public class FunctionResultContext extends FromNativeContext {
    private Function function;
    private Object[] args;

    FunctionResultContext(Class resultClass, Function function, Object[] args) {
        super(resultClass);
        this.function = function;
        this.args = args;
    }

    public Function getFunction() {
        return this.function;
    }

    public Object[] getArguments() {
        return this.args;
    }
}
