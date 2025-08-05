package com.sun.org.apache.xerces.internal.dom;

import com.sun.org.apache.xerces.internal.xni.parser.XMLParseException;
import org.w3c.dom.DOMError;
import org.w3c.dom.DOMLocator;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/dom/DOMErrorImpl.class */
public class DOMErrorImpl implements DOMError {
    public short fSeverity;
    public String fMessage;
    public DOMLocatorImpl fLocator;
    public Exception fException;
    public String fType;
    public Object fRelatedData;

    public DOMErrorImpl() {
        this.fSeverity = (short) 1;
        this.fMessage = null;
        this.fLocator = new DOMLocatorImpl();
        this.fException = null;
    }

    public DOMErrorImpl(short severity, XMLParseException exception) {
        this.fSeverity = (short) 1;
        this.fMessage = null;
        this.fLocator = new DOMLocatorImpl();
        this.fException = null;
        this.fSeverity = severity;
        this.fException = exception;
        this.fLocator = createDOMLocator(exception);
    }

    @Override // org.w3c.dom.DOMError
    public short getSeverity() {
        return this.fSeverity;
    }

    @Override // org.w3c.dom.DOMError
    public String getMessage() {
        return this.fMessage;
    }

    @Override // org.w3c.dom.DOMError
    public DOMLocator getLocation() {
        return this.fLocator;
    }

    private DOMLocatorImpl createDOMLocator(XMLParseException exception) {
        return new DOMLocatorImpl(exception.getLineNumber(), exception.getColumnNumber(), exception.getCharacterOffset(), exception.getExpandedSystemId());
    }

    @Override // org.w3c.dom.DOMError
    public Object getRelatedException() {
        return this.fException;
    }

    public void reset() {
        this.fSeverity = (short) 1;
        this.fException = null;
    }

    @Override // org.w3c.dom.DOMError
    public String getType() {
        return this.fType;
    }

    @Override // org.w3c.dom.DOMError
    public Object getRelatedData() {
        return this.fRelatedData;
    }
}
