package com.sun.xml.internal.messaging.saaj.soap;

import com.sun.xml.internal.messaging.saaj.SOAPExceptionImpl;

/* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/soap/SOAPVersionMismatchException.class */
public class SOAPVersionMismatchException extends SOAPExceptionImpl {
    public SOAPVersionMismatchException() {
    }

    public SOAPVersionMismatchException(String reason) {
        super(reason);
    }

    public SOAPVersionMismatchException(String reason, Throwable cause) {
        super(reason, cause);
    }

    public SOAPVersionMismatchException(Throwable cause) {
        super(cause);
    }
}
