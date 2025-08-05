package com.sun.xml.internal.ws.api.message;

import com.sun.xml.internal.ws.util.exception.JAXWSExceptionBase;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/message/ExceptionHasMessage.class */
public abstract class ExceptionHasMessage extends JAXWSExceptionBase {
    public abstract Message getFaultMessage();

    public ExceptionHasMessage(String key, Object... args) {
        super(key, args);
    }
}
