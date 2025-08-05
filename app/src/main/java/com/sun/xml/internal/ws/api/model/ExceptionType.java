package com.sun.xml.internal.ws.api.model;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/model/ExceptionType.class */
public enum ExceptionType {
    WSDLException(0),
    UserDefined(1);

    private final int exceptionType;

    ExceptionType(int exceptionType) {
        this.exceptionType = exceptionType;
    }

    public int value() {
        return this.exceptionType;
    }
}
