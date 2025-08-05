package com.sun.org.apache.xerces.internal.util;

import com.sun.org.apache.xerces.internal.xni.XNIException;
import com.sun.org.apache.xerces.internal.xni.parser.XMLErrorHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/util/ErrorHandlerProxy.class */
public abstract class ErrorHandlerProxy implements ErrorHandler {
    protected abstract XMLErrorHandler getErrorHandler();

    @Override // org.xml.sax.ErrorHandler
    public void error(SAXParseException e2) throws SAXException, XNIException {
        XMLErrorHandler eh = getErrorHandler();
        if (eh instanceof ErrorHandlerWrapper) {
            ((ErrorHandlerWrapper) eh).fErrorHandler.error(e2);
        } else {
            eh.error("", "", ErrorHandlerWrapper.createXMLParseException(e2));
        }
    }

    @Override // org.xml.sax.ErrorHandler
    public void fatalError(SAXParseException e2) throws SAXException, XNIException {
        XMLErrorHandler eh = getErrorHandler();
        if (eh instanceof ErrorHandlerWrapper) {
            ((ErrorHandlerWrapper) eh).fErrorHandler.fatalError(e2);
        } else {
            eh.fatalError("", "", ErrorHandlerWrapper.createXMLParseException(e2));
        }
    }

    @Override // org.xml.sax.ErrorHandler
    public void warning(SAXParseException e2) throws SAXException, XNIException {
        XMLErrorHandler eh = getErrorHandler();
        if (eh instanceof ErrorHandlerWrapper) {
            ((ErrorHandlerWrapper) eh).fErrorHandler.warning(e2);
        } else {
            eh.warning("", "", ErrorHandlerWrapper.createXMLParseException(e2));
        }
    }
}
