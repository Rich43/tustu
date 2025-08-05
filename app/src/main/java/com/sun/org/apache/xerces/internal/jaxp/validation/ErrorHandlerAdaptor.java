package com.sun.org.apache.xerces.internal.jaxp.validation;

import com.sun.org.apache.xerces.internal.xni.parser.XMLErrorHandler;
import com.sun.org.apache.xerces.internal.xni.parser.XMLParseException;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/jaxp/validation/ErrorHandlerAdaptor.class */
public abstract class ErrorHandlerAdaptor implements XMLErrorHandler {
    private boolean hadError = false;

    protected abstract ErrorHandler getErrorHandler();

    public boolean hadError() {
        return this.hadError;
    }

    public void reset() {
        this.hadError = false;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLErrorHandler
    public void fatalError(String domain, String key, XMLParseException e2) {
        try {
            this.hadError = true;
            getErrorHandler().fatalError(Util.toSAXParseException(e2));
        } catch (SAXException se) {
            throw new WrappedSAXException(se);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLErrorHandler
    public void error(String domain, String key, XMLParseException e2) {
        try {
            this.hadError = true;
            getErrorHandler().error(Util.toSAXParseException(e2));
        } catch (SAXException se) {
            throw new WrappedSAXException(se);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLErrorHandler
    public void warning(String domain, String key, XMLParseException e2) {
        try {
            getErrorHandler().warning(Util.toSAXParseException(e2));
        } catch (SAXException se) {
            throw new WrappedSAXException(se);
        }
    }
}
