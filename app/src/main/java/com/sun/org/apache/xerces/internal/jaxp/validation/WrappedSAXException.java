package com.sun.org.apache.xerces.internal.jaxp.validation;

import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/jaxp/validation/WrappedSAXException.class */
public class WrappedSAXException extends RuntimeException {
    public final SAXException exception;

    WrappedSAXException(SAXException e2) {
        this.exception = e2;
    }
}
