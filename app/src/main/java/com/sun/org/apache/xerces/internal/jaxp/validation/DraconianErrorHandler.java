package com.sun.org.apache.xerces.internal.jaxp.validation;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/jaxp/validation/DraconianErrorHandler.class */
final class DraconianErrorHandler implements ErrorHandler {
    private static final DraconianErrorHandler ERROR_HANDLER_INSTANCE = new DraconianErrorHandler();

    private DraconianErrorHandler() {
    }

    public static DraconianErrorHandler getInstance() {
        return ERROR_HANDLER_INSTANCE;
    }

    @Override // org.xml.sax.ErrorHandler
    public void warning(SAXParseException e2) throws SAXException {
    }

    @Override // org.xml.sax.ErrorHandler
    public void error(SAXParseException e2) throws SAXException {
        throw e2;
    }

    @Override // org.xml.sax.ErrorHandler
    public void fatalError(SAXParseException e2) throws SAXException {
        throw e2;
    }
}
