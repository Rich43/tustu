package com.sun.org.apache.xerces.internal.util;

import com.sun.org.apache.xerces.internal.xni.XMLLocator;
import com.sun.org.apache.xerces.internal.xni.XNIException;
import com.sun.org.apache.xerces.internal.xni.parser.XMLErrorHandler;
import com.sun.org.apache.xerces.internal.xni.parser.XMLParseException;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/util/ErrorHandlerWrapper.class */
public class ErrorHandlerWrapper implements XMLErrorHandler {
    protected ErrorHandler fErrorHandler;

    public ErrorHandlerWrapper() {
    }

    public ErrorHandlerWrapper(ErrorHandler errorHandler) {
        setErrorHandler(errorHandler);
    }

    public void setErrorHandler(ErrorHandler errorHandler) {
        this.fErrorHandler = errorHandler;
    }

    public ErrorHandler getErrorHandler() {
        return this.fErrorHandler;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLErrorHandler
    public void warning(String domain, String key, XMLParseException exception) throws XNIException {
        if (this.fErrorHandler != null) {
            SAXParseException saxException = createSAXParseException(exception);
            try {
                this.fErrorHandler.warning(saxException);
            } catch (SAXParseException e2) {
                throw createXMLParseException(e2);
            } catch (SAXException e3) {
                throw createXNIException(e3);
            }
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLErrorHandler
    public void error(String domain, String key, XMLParseException exception) throws XNIException {
        if (this.fErrorHandler != null) {
            SAXParseException saxException = createSAXParseException(exception);
            try {
                this.fErrorHandler.error(saxException);
            } catch (SAXParseException e2) {
                throw createXMLParseException(e2);
            } catch (SAXException e3) {
                throw createXNIException(e3);
            }
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLErrorHandler
    public void fatalError(String domain, String key, XMLParseException exception) throws XNIException {
        if (this.fErrorHandler != null) {
            SAXParseException saxException = createSAXParseException(exception);
            try {
                this.fErrorHandler.fatalError(saxException);
            } catch (SAXParseException e2) {
                throw createXMLParseException(e2);
            } catch (SAXException e3) {
                throw createXNIException(e3);
            }
        }
    }

    protected static SAXParseException createSAXParseException(XMLParseException exception) {
        return new SAXParseException(exception.getMessage(), exception.getPublicId(), exception.getExpandedSystemId(), exception.getLineNumber(), exception.getColumnNumber(), exception.getException());
    }

    protected static XMLParseException createXMLParseException(SAXParseException exception) {
        final String fPublicId = exception.getPublicId();
        final String fExpandedSystemId = exception.getSystemId();
        final int fLineNumber = exception.getLineNumber();
        final int fColumnNumber = exception.getColumnNumber();
        XMLLocator location = new XMLLocator() { // from class: com.sun.org.apache.xerces.internal.util.ErrorHandlerWrapper.1
            @Override // com.sun.org.apache.xerces.internal.xni.XMLLocator
            public String getPublicId() {
                return fPublicId;
            }

            @Override // com.sun.org.apache.xerces.internal.xni.XMLLocator
            public String getExpandedSystemId() {
                return fExpandedSystemId;
            }

            @Override // com.sun.org.apache.xerces.internal.xni.XMLLocator
            public String getBaseSystemId() {
                return null;
            }

            @Override // com.sun.org.apache.xerces.internal.xni.XMLLocator
            public String getLiteralSystemId() {
                return null;
            }

            @Override // com.sun.org.apache.xerces.internal.xni.XMLLocator
            public int getColumnNumber() {
                return fColumnNumber;
            }

            @Override // com.sun.org.apache.xerces.internal.xni.XMLLocator
            public int getLineNumber() {
                return fLineNumber;
            }

            @Override // com.sun.org.apache.xerces.internal.xni.XMLLocator
            public int getCharacterOffset() {
                return -1;
            }

            @Override // com.sun.org.apache.xerces.internal.xni.XMLLocator
            public String getEncoding() {
                return null;
            }

            @Override // com.sun.org.apache.xerces.internal.xni.XMLLocator
            public String getXMLVersion() {
                return null;
            }
        };
        return new XMLParseException(location, exception.getMessage(), exception);
    }

    protected static XNIException createXNIException(SAXException exception) {
        return new XNIException(exception.getMessage(), exception);
    }
}
