package com.sun.org.apache.xerces.internal.util;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/util/DraconianErrorHandler.class */
public class DraconianErrorHandler implements ErrorHandler {
    public static final ErrorHandler theInstance = new DraconianErrorHandler();

    private DraconianErrorHandler() {
    }

    @Override // org.xml.sax.ErrorHandler
    public void error(SAXParseException e2) throws SAXException {
        throw e2;
    }

    @Override // org.xml.sax.ErrorHandler
    public void fatalError(SAXParseException e2) throws SAXException {
        throw e2;
    }

    @Override // org.xml.sax.ErrorHandler
    public void warning(SAXParseException e2) throws SAXException {
    }
}
