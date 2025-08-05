package com.sun.istack.internal;

import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/istack/internal/SAXException2.class */
public class SAXException2 extends SAXException {
    public SAXException2(String message) {
        super(message);
    }

    public SAXException2(Exception e2) {
        super(e2);
    }

    public SAXException2(String message, Exception e2) {
        super(message, e2);
    }

    @Override // org.xml.sax.SAXException, java.lang.Throwable
    public Throwable getCause() {
        return getException();
    }
}
