package com.sun.xml.internal.ws.api.databinding;

import java.lang.reflect.Method;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/databinding/JavaCallInfo.class */
public class JavaCallInfo implements com.oracle.webservices.internal.api.databinding.JavaCallInfo {
    private Method method;
    private Object[] parameters;
    private Object returnValue;
    private Throwable exception;

    public JavaCallInfo() {
    }

    public JavaCallInfo(Method m2, Object[] args) {
        this.method = m2;
        this.parameters = args;
    }

    @Override // com.oracle.webservices.internal.api.databinding.JavaCallInfo
    public Method getMethod() {
        return this.method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    @Override // com.oracle.webservices.internal.api.databinding.JavaCallInfo
    public Object[] getParameters() {
        return this.parameters;
    }

    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }

    @Override // com.oracle.webservices.internal.api.databinding.JavaCallInfo
    public Object getReturnValue() {
        return this.returnValue;
    }

    @Override // com.oracle.webservices.internal.api.databinding.JavaCallInfo
    public void setReturnValue(Object returnValue) {
        this.returnValue = returnValue;
    }

    @Override // com.oracle.webservices.internal.api.databinding.JavaCallInfo
    public Throwable getException() {
        return this.exception;
    }

    @Override // com.oracle.webservices.internal.api.databinding.JavaCallInfo
    public void setException(Throwable exception) {
        this.exception = exception;
    }
}
