package com.sun.istack.internal;

import org.xml.sax.Locator;
import org.xml.sax.SAXParseException;

/* loaded from: rt.jar:com/sun/istack/internal/SAXParseException2.class */
public class SAXParseException2 extends SAXParseException {
    public SAXParseException2(String message, Locator locator) {
        super(message, locator);
    }

    public SAXParseException2(String message, Locator locator, Exception e2) {
        super(message, locator, e2);
    }

    public SAXParseException2(String message, String publicId, String systemId, int lineNumber, int columnNumber) {
        super(message, publicId, systemId, lineNumber, columnNumber);
    }

    public SAXParseException2(String message, String publicId, String systemId, int lineNumber, int columnNumber, Exception e2) {
        super(message, publicId, systemId, lineNumber, columnNumber, e2);
    }

    @Override // org.xml.sax.SAXException, java.lang.Throwable
    public Throwable getCause() {
        return getException();
    }
}
