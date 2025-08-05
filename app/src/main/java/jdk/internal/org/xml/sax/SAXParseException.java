package jdk.internal.org.xml.sax;

import org.apache.commons.math3.geometry.VectorFormat;

/* loaded from: rt.jar:jdk/internal/org/xml/sax/SAXParseException.class */
public class SAXParseException extends SAXException {
    private String publicId;
    private String systemId;
    private int lineNumber;
    private int columnNumber;
    static final long serialVersionUID = -5651165872476709336L;

    public SAXParseException(String str, Locator locator) {
        super(str);
        if (locator != null) {
            init(locator.getPublicId(), locator.getSystemId(), locator.getLineNumber(), locator.getColumnNumber());
        } else {
            init(null, null, -1, -1);
        }
    }

    public SAXParseException(String str, Locator locator, Exception exc) {
        super(str, exc);
        if (locator != null) {
            init(locator.getPublicId(), locator.getSystemId(), locator.getLineNumber(), locator.getColumnNumber());
        } else {
            init(null, null, -1, -1);
        }
    }

    public SAXParseException(String str, String str2, String str3, int i2, int i3) {
        super(str);
        init(str2, str3, i2, i3);
    }

    public SAXParseException(String str, String str2, String str3, int i2, int i3, Exception exc) {
        super(str, exc);
        init(str2, str3, i2, i3);
    }

    private void init(String str, String str2, int i2, int i3) {
        this.publicId = str;
        this.systemId = str2;
        this.lineNumber = i2;
        this.columnNumber = i3;
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

    @Override // jdk.internal.org.xml.sax.SAXException, java.lang.Throwable
    public String toString() {
        StringBuilder sb = new StringBuilder(getClass().getName());
        String localizedMessage = getLocalizedMessage();
        if (this.publicId != null) {
            sb.append("publicId: ").append(this.publicId);
        }
        if (this.systemId != null) {
            sb.append("; systemId: ").append(this.systemId);
        }
        if (this.lineNumber != -1) {
            sb.append("; lineNumber: ").append(this.lineNumber);
        }
        if (this.columnNumber != -1) {
            sb.append("; columnNumber: ").append(this.columnNumber);
        }
        if (localizedMessage != null) {
            sb.append(VectorFormat.DEFAULT_SEPARATOR).append(localizedMessage);
        }
        return sb.toString();
    }
}
