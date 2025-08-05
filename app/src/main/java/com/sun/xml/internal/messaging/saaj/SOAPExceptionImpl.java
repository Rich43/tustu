package com.sun.xml.internal.messaging.saaj;

import java.io.PrintStream;
import java.io.PrintWriter;
import javax.xml.soap.SOAPException;

/* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/SOAPExceptionImpl.class */
public class SOAPExceptionImpl extends SOAPException {
    private Throwable cause;

    public SOAPExceptionImpl() {
        this.cause = null;
    }

    public SOAPExceptionImpl(String reason) {
        super(reason);
        this.cause = null;
    }

    public SOAPExceptionImpl(String reason, Throwable cause) {
        super(reason);
        initCause(cause);
    }

    public SOAPExceptionImpl(Throwable cause) {
        super(cause.toString());
        initCause(cause);
    }

    @Override // javax.xml.soap.SOAPException, java.lang.Throwable
    public String getMessage() {
        String message = super.getMessage();
        if (message == null && this.cause != null) {
            return this.cause.getMessage();
        }
        return message;
    }

    @Override // javax.xml.soap.SOAPException, java.lang.Throwable
    public Throwable getCause() {
        return this.cause;
    }

    @Override // javax.xml.soap.SOAPException, java.lang.Throwable
    public synchronized Throwable initCause(Throwable cause) {
        if (this.cause != null) {
            throw new IllegalStateException("Can't override cause");
        }
        if (cause == this) {
            throw new IllegalArgumentException("Self-causation not permitted");
        }
        this.cause = cause;
        return this;
    }

    @Override // java.lang.Throwable
    public void printStackTrace() {
        super.printStackTrace();
        if (this.cause != null) {
            System.err.println("\nCAUSE:\n");
            this.cause.printStackTrace();
        }
    }

    @Override // java.lang.Throwable
    public void printStackTrace(PrintStream s2) {
        super.printStackTrace(s2);
        if (this.cause != null) {
            s2.println("\nCAUSE:\n");
            this.cause.printStackTrace(s2);
        }
    }

    @Override // java.lang.Throwable
    public void printStackTrace(PrintWriter s2) {
        super.printStackTrace(s2);
        if (this.cause != null) {
            s2.println("\nCAUSE:\n");
            this.cause.printStackTrace(s2);
        }
    }
}
