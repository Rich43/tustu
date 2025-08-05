package com.sun.org.apache.xerces.internal.xni.parser;

import com.sun.org.apache.xerces.internal.xni.XMLLocator;
import com.sun.org.apache.xerces.internal.xni.XNIException;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/xni/parser/XMLParseException.class */
public class XMLParseException extends XNIException {
    static final long serialVersionUID = 1732959359448549967L;
    protected String fPublicId;
    protected String fLiteralSystemId;
    protected String fExpandedSystemId;
    protected String fBaseSystemId;
    protected int fLineNumber;
    protected int fColumnNumber;
    protected int fCharacterOffset;

    public XMLParseException(XMLLocator locator, String message) {
        super(message);
        this.fLineNumber = -1;
        this.fColumnNumber = -1;
        this.fCharacterOffset = -1;
        if (locator != null) {
            this.fPublicId = locator.getPublicId();
            this.fLiteralSystemId = locator.getLiteralSystemId();
            this.fExpandedSystemId = locator.getExpandedSystemId();
            this.fBaseSystemId = locator.getBaseSystemId();
            this.fLineNumber = locator.getLineNumber();
            this.fColumnNumber = locator.getColumnNumber();
            this.fCharacterOffset = locator.getCharacterOffset();
        }
    }

    public XMLParseException(XMLLocator locator, String message, Exception exception) {
        super(message, exception);
        this.fLineNumber = -1;
        this.fColumnNumber = -1;
        this.fCharacterOffset = -1;
        if (locator != null) {
            this.fPublicId = locator.getPublicId();
            this.fLiteralSystemId = locator.getLiteralSystemId();
            this.fExpandedSystemId = locator.getExpandedSystemId();
            this.fBaseSystemId = locator.getBaseSystemId();
            this.fLineNumber = locator.getLineNumber();
            this.fColumnNumber = locator.getColumnNumber();
            this.fCharacterOffset = locator.getCharacterOffset();
        }
    }

    public String getPublicId() {
        return this.fPublicId;
    }

    public String getExpandedSystemId() {
        return this.fExpandedSystemId;
    }

    public String getLiteralSystemId() {
        return this.fLiteralSystemId;
    }

    public String getBaseSystemId() {
        return this.fBaseSystemId;
    }

    public int getLineNumber() {
        return this.fLineNumber;
    }

    public int getColumnNumber() {
        return this.fColumnNumber;
    }

    public int getCharacterOffset() {
        return this.fCharacterOffset;
    }

    @Override // java.lang.Throwable
    public String toString() {
        Exception exception;
        StringBuffer str = new StringBuffer();
        if (this.fPublicId != null) {
            str.append(this.fPublicId);
        }
        str.append(':');
        if (this.fLiteralSystemId != null) {
            str.append(this.fLiteralSystemId);
        }
        str.append(':');
        if (this.fExpandedSystemId != null) {
            str.append(this.fExpandedSystemId);
        }
        str.append(':');
        if (this.fBaseSystemId != null) {
            str.append(this.fBaseSystemId);
        }
        str.append(':');
        str.append(this.fLineNumber);
        str.append(':');
        str.append(this.fColumnNumber);
        str.append(':');
        str.append(this.fCharacterOffset);
        str.append(':');
        String message = getMessage();
        if (message == null && (exception = getException()) != null) {
            message = exception.getMessage();
        }
        if (message != null) {
            str.append(message);
        }
        return str.toString();
    }
}
