package com.oracle.webservices.internal.api.databinding;

import java.lang.reflect.Method;

/* loaded from: rt.jar:com/oracle/webservices/internal/api/databinding/JavaCallInfo.class */
public interface JavaCallInfo {
    Method getMethod();

    Object[] getParameters();

    Object getReturnValue();

    void setReturnValue(Object obj);

    Throwable getException();

    void setException(Throwable th);
}
