package com.sun.xml.internal.ws.wsdl;

import com.sun.xml.internal.ws.api.message.Message;

/* loaded from: rt.jar:com/sun/xml/internal/ws/wsdl/DispatchException.class */
public final class DispatchException extends Exception {
    public final Message fault;

    public DispatchException(Message fault) {
        this.fault = fault;
    }
}
