package com.sun.xml.internal.ws.api.model;

import com.sun.xml.internal.bind.api.Bridge;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/model/CheckedException.class */
public interface CheckedException {
    SEIModel getOwner();

    JavaMethod getParent();

    Class getExceptionClass();

    Class getDetailBean();

    Bridge getBridge();

    ExceptionType getExceptionType();

    String getMessageName();
}
