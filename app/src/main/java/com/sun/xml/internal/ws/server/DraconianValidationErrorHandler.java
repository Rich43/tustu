package com.sun.xml.internal.ws.server;

import com.sun.xml.internal.ws.developer.ValidationErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/server/DraconianValidationErrorHandler.class */
public class DraconianValidationErrorHandler extends ValidationErrorHandler {
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
