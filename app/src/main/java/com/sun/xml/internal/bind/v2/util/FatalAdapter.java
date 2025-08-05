package com.sun.xml.internal.bind.v2.util;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/util/FatalAdapter.class */
public class FatalAdapter implements ErrorHandler {
    private final ErrorHandler core;

    public FatalAdapter(ErrorHandler handler) {
        this.core = handler;
    }

    @Override // org.xml.sax.ErrorHandler
    public void warning(SAXParseException exception) throws SAXException {
        this.core.warning(exception);
    }

    @Override // org.xml.sax.ErrorHandler
    public void error(SAXParseException exception) throws SAXException {
        this.core.fatalError(exception);
    }

    @Override // org.xml.sax.ErrorHandler
    public void fatalError(SAXParseException exception) throws SAXException {
        this.core.fatalError(exception);
    }
}
