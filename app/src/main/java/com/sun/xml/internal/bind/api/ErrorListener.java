package com.sun.xml.internal.bind.api;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

/* loaded from: rt.jar:com/sun/xml/internal/bind/api/ErrorListener.class */
public interface ErrorListener extends ErrorHandler {
    @Override // org.xml.sax.ErrorHandler
    void error(SAXParseException sAXParseException);

    @Override // org.xml.sax.ErrorHandler
    void fatalError(SAXParseException sAXParseException);

    @Override // org.xml.sax.ErrorHandler
    void warning(SAXParseException sAXParseException);

    void info(SAXParseException sAXParseException);
}
