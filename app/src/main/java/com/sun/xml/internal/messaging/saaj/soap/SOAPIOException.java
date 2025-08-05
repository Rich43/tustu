package com.sun.xml.internal.messaging.saaj.soap;

import com.sun.xml.internal.messaging.saaj.SOAPExceptionImpl;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;

/* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/soap/SOAPIOException.class */
public class SOAPIOException extends IOException {
    SOAPExceptionImpl soapException;

    public SOAPIOException() {
        this.soapException = new SOAPExceptionImpl();
        this.soapException.fillInStackTrace();
    }

    public SOAPIOException(String s2) {
        this.soapException = new SOAPExceptionImpl(s2);
        this.soapException.fillInStackTrace();
    }

    public SOAPIOException(String reason, Throwable cause) {
        this.soapException = new SOAPExceptionImpl(reason, cause);
        this.soapException.fillInStackTrace();
    }

    public SOAPIOException(Throwable cause) {
        super(cause.toString());
        this.soapException = new SOAPExceptionImpl(cause);
        this.soapException.fillInStackTrace();
    }

    @Override // java.lang.Throwable
    public Throwable fillInStackTrace() {
        if (this.soapException != null) {
            this.soapException.fillInStackTrace();
        }
        return this;
    }

    @Override // java.lang.Throwable
    public String getLocalizedMessage() {
        return this.soapException.getLocalizedMessage();
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        return this.soapException.getMessage();
    }

    @Override // java.lang.Throwable
    public void printStackTrace() {
        this.soapException.printStackTrace();
    }

    @Override // java.lang.Throwable
    public void printStackTrace(PrintStream s2) {
        this.soapException.printStackTrace(s2);
    }

    @Override // java.lang.Throwable
    public void printStackTrace(PrintWriter s2) {
        this.soapException.printStackTrace(s2);
    }

    @Override // java.lang.Throwable
    public String toString() {
        return this.soapException.toString();
    }
}
