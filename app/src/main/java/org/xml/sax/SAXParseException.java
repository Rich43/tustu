package org.xml.sax;

import org.apache.commons.math3.geometry.VectorFormat;

/* loaded from: rt.jar:org/xml/sax/SAXParseException.class */
public class SAXParseException extends SAXException {
    private String publicId;
    private String systemId;
    private int lineNumber;
    private int columnNumber;
    static final long serialVersionUID = -5651165872476709336L;

    public SAXParseException(String message, Locator locator) {
        super(message);
        if (locator != null) {
            init(locator.getPublicId(), locator.getSystemId(), locator.getLineNumber(), locator.getColumnNumber());
        } else {
            init(null, null, -1, -1);
        }
    }

    public SAXParseException(String message, Locator locator, Exception e2) {
        super(message, e2);
        if (locator != null) {
            init(locator.getPublicId(), locator.getSystemId(), locator.getLineNumber(), locator.getColumnNumber());
        } else {
            init(null, null, -1, -1);
        }
    }

    public SAXParseException(String message, String publicId, String systemId, int lineNumber, int columnNumber) {
        super(message);
        init(publicId, systemId, lineNumber, columnNumber);
    }

    public SAXParseException(String message, String publicId, String systemId, int lineNumber, int columnNumber, Exception e2) {
        super(message, e2);
        init(publicId, systemId, lineNumber, columnNumber);
    }

    private void init(String publicId, String systemId, int lineNumber, int columnNumber) {
        this.publicId = publicId;
        this.systemId = systemId;
        this.lineNumber = lineNumber;
        this.columnNumber = columnNumber;
    }

    public String getPublicId() {
        return this.publicId;
    }

    public String getSystemId() {
        return this.systemId;
    }

    public int getLineNumber() {
        return this.lineNumber;
    }

    public int getColumnNumber() {
        return this.columnNumber;
    }

    @Override // org.xml.sax.SAXException, java.lang.Throwable
    public String toString() {
        StringBuilder buf = new StringBuilder(getClass().getName());
        String message = getLocalizedMessage();
        if (this.publicId != null) {
            buf.append("publicId: ").append(this.publicId);
        }
        if (this.systemId != null) {
            buf.append("; systemId: ").append(this.systemId);
        }
        if (this.lineNumber != -1) {
            buf.append("; lineNumber: ").append(this.lineNumber);
        }
        if (this.columnNumber != -1) {
            buf.append("; columnNumber: ").append(this.columnNumber);
        }
        if (message != null) {
            buf.append(VectorFormat.DEFAULT_SEPARATOR).append(message);
        }
        return buf.toString();
    }
}
