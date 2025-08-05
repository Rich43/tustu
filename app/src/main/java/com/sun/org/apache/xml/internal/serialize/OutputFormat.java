package com.sun.org.apache.xml.internal.serialize;

import com.sun.glass.ui.Clipboard;
import java.io.UnsupportedEncodingException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Node;
import org.w3c.dom.html.HTMLDocument;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/serialize/OutputFormat.class */
public class OutputFormat {
    private String _method;
    private String _version;
    private int _indent;
    private String _encoding;
    private EncodingInfo _encodingInfo;
    private boolean _allowJavaNames;
    private String _mediaType;
    private String _doctypeSystem;
    private String _doctypePublic;
    private boolean _omitXmlDeclaration;
    private boolean _omitDoctype;
    private boolean _omitComments;
    private boolean _stripComments;
    private boolean _standalone;
    private String[] _cdataElements;
    private String[] _nonEscapingElements;
    private String _lineSeparator;
    private int _lineWidth;
    private boolean _preserve;
    private boolean _preserveEmptyAttributes;

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/serialize/OutputFormat$DTD.class */
    public static class DTD {
        public static final String HTMLPublicId = "-//W3C//DTD HTML 4.01//EN";
        public static final String HTMLSystemId = "http://www.w3.org/TR/html4/strict.dtd";
        public static final String XHTMLPublicId = "-//W3C//DTD XHTML 1.0 Strict//EN";
        public static final String XHTMLSystemId = "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd";
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/serialize/OutputFormat$Defaults.class */
    public static class Defaults {
        public static final int Indent = 4;
        public static final String Encoding = "UTF-8";
        public static final int LineWidth = 72;
    }

    public OutputFormat() {
        this._indent = 0;
        this._encoding = "UTF-8";
        this._encodingInfo = null;
        this._allowJavaNames = false;
        this._omitXmlDeclaration = false;
        this._omitDoctype = false;
        this._omitComments = false;
        this._stripComments = false;
        this._standalone = false;
        this._lineSeparator = "\n";
        this._lineWidth = 72;
        this._preserve = false;
        this._preserveEmptyAttributes = false;
    }

    public OutputFormat(String method, String encoding, boolean indenting) {
        this._indent = 0;
        this._encoding = "UTF-8";
        this._encodingInfo = null;
        this._allowJavaNames = false;
        this._omitXmlDeclaration = false;
        this._omitDoctype = false;
        this._omitComments = false;
        this._stripComments = false;
        this._standalone = false;
        this._lineSeparator = "\n";
        this._lineWidth = 72;
        this._preserve = false;
        this._preserveEmptyAttributes = false;
        setMethod(method);
        setEncoding(encoding);
        setIndenting(indenting);
    }

    public OutputFormat(Document doc) {
        this._indent = 0;
        this._encoding = "UTF-8";
        this._encodingInfo = null;
        this._allowJavaNames = false;
        this._omitXmlDeclaration = false;
        this._omitDoctype = false;
        this._omitComments = false;
        this._stripComments = false;
        this._standalone = false;
        this._lineSeparator = "\n";
        this._lineWidth = 72;
        this._preserve = false;
        this._preserveEmptyAttributes = false;
        setMethod(whichMethod(doc));
        setDoctype(whichDoctypePublic(doc), whichDoctypeSystem(doc));
        setMediaType(whichMediaType(getMethod()));
    }

    public OutputFormat(Document doc, String encoding, boolean indenting) {
        this(doc);
        setEncoding(encoding);
        setIndenting(indenting);
    }

    public String getMethod() {
        return this._method;
    }

    public void setMethod(String method) {
        this._method = method;
    }

    public String getVersion() {
        return this._version;
    }

    public void setVersion(String version) {
        this._version = version;
    }

    public int getIndent() {
        return this._indent;
    }

    public boolean getIndenting() {
        return this._indent > 0;
    }

    public void setIndent(int indent) {
        if (indent < 0) {
            this._indent = 0;
        } else {
            this._indent = indent;
        }
    }

    public void setIndenting(boolean on) {
        if (on) {
            this._indent = 4;
            this._lineWidth = 72;
        } else {
            this._indent = 0;
            this._lineWidth = 0;
        }
    }

    public String getEncoding() {
        return this._encoding;
    }

    public void setEncoding(String encoding) {
        this._encoding = encoding;
        this._encodingInfo = null;
    }

    public void setEncoding(EncodingInfo encInfo) {
        this._encoding = encInfo.getIANAName();
        this._encodingInfo = encInfo;
    }

    public EncodingInfo getEncodingInfo() throws UnsupportedEncodingException {
        if (this._encodingInfo == null) {
            this._encodingInfo = Encodings.getEncodingInfo(this._encoding, this._allowJavaNames);
        }
        return this._encodingInfo;
    }

    public void setAllowJavaNames(boolean allow) {
        this._allowJavaNames = allow;
    }

    public boolean setAllowJavaNames() {
        return this._allowJavaNames;
    }

    public String getMediaType() {
        return this._mediaType;
    }

    public void setMediaType(String mediaType) {
        this._mediaType = mediaType;
    }

    public void setDoctype(String publicId, String systemId) {
        this._doctypePublic = publicId;
        this._doctypeSystem = systemId;
    }

    public String getDoctypePublic() {
        return this._doctypePublic;
    }

    public String getDoctypeSystem() {
        return this._doctypeSystem;
    }

    public boolean getOmitComments() {
        return this._omitComments;
    }

    public void setOmitComments(boolean omit) {
        this._omitComments = omit;
    }

    public boolean getOmitDocumentType() {
        return this._omitDoctype;
    }

    public void setOmitDocumentType(boolean omit) {
        this._omitDoctype = omit;
    }

    public boolean getOmitXMLDeclaration() {
        return this._omitXmlDeclaration;
    }

    public void setOmitXMLDeclaration(boolean omit) {
        this._omitXmlDeclaration = omit;
    }

    public boolean getStandalone() {
        return this._standalone;
    }

    public void setStandalone(boolean standalone) {
        this._standalone = standalone;
    }

    public String[] getCDataElements() {
        return this._cdataElements;
    }

    public boolean isCDataElement(String tagName) {
        if (this._cdataElements == null) {
            return false;
        }
        for (int i2 = 0; i2 < this._cdataElements.length; i2++) {
            if (this._cdataElements[i2].equals(tagName)) {
                return true;
            }
        }
        return false;
    }

    public void setCDataElements(String[] cdataElements) {
        this._cdataElements = cdataElements;
    }

    public String[] getNonEscapingElements() {
        return this._nonEscapingElements;
    }

    public boolean isNonEscapingElement(String tagName) {
        if (this._nonEscapingElements == null) {
            return false;
        }
        for (int i2 = 0; i2 < this._nonEscapingElements.length; i2++) {
            if (this._nonEscapingElements[i2].equals(tagName)) {
                return true;
            }
        }
        return false;
    }

    public void setNonEscapingElements(String[] nonEscapingElements) {
        this._nonEscapingElements = nonEscapingElements;
    }

    public String getLineSeparator() {
        return this._lineSeparator;
    }

    public void setLineSeparator(String lineSeparator) {
        if (lineSeparator == null) {
            this._lineSeparator = "\n";
        } else {
            this._lineSeparator = lineSeparator;
        }
    }

    public boolean getPreserveSpace() {
        return this._preserve;
    }

    public void setPreserveSpace(boolean preserve) {
        this._preserve = preserve;
    }

    public int getLineWidth() {
        return this._lineWidth;
    }

    public void setLineWidth(int lineWidth) {
        if (lineWidth <= 0) {
            this._lineWidth = 0;
        } else {
            this._lineWidth = lineWidth;
        }
    }

    public boolean getPreserveEmptyAttributes() {
        return this._preserveEmptyAttributes;
    }

    public void setPreserveEmptyAttributes(boolean preserve) {
        this._preserveEmptyAttributes = preserve;
    }

    public char getLastPrintable() {
        if (getEncoding() != null && getEncoding().equalsIgnoreCase("ASCII")) {
            return (char) 255;
        }
        return (char) 65535;
    }

    public static String whichMethod(Document doc) throws DOMException {
        if (doc instanceof HTMLDocument) {
            return "html";
        }
        Node firstChild = doc.getFirstChild();
        while (true) {
            Node node = firstChild;
            if (node != null) {
                if (node.getNodeType() == 1) {
                    if (node.getNodeName().equalsIgnoreCase("html")) {
                        return "html";
                    }
                    if (node.getNodeName().equalsIgnoreCase("root")) {
                        return Method.FOP;
                    }
                    return "xml";
                }
                if (node.getNodeType() == 3) {
                    String value = node.getNodeValue();
                    for (int i2 = 0; i2 < value.length(); i2++) {
                        if (value.charAt(i2) != ' ' && value.charAt(i2) != '\n' && value.charAt(i2) != '\t' && value.charAt(i2) != '\r') {
                            return "xml";
                        }
                    }
                }
                firstChild = node.getNextSibling();
            } else {
                return "xml";
            }
        }
    }

    public static String whichDoctypePublic(Document doc) {
        DocumentType doctype = doc.getDoctype();
        if (doctype != null) {
            try {
                return doctype.getPublicId();
            } catch (Error e2) {
            }
        }
        if (doc instanceof HTMLDocument) {
            return "-//W3C//DTD XHTML 1.0 Strict//EN";
        }
        return null;
    }

    public static String whichDoctypeSystem(Document doc) {
        DocumentType doctype = doc.getDoctype();
        if (doctype != null) {
            try {
                return doctype.getSystemId();
            } catch (Error e2) {
            }
        }
        if (doc instanceof HTMLDocument) {
            return "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd";
        }
        return null;
    }

    public static String whichMediaType(String method) {
        if (method.equalsIgnoreCase("xml")) {
            return "text/xml";
        }
        if (method.equalsIgnoreCase("html") || method.equalsIgnoreCase("xhtml")) {
            return Clipboard.HTML_TYPE;
        }
        if (method.equalsIgnoreCase("text")) {
            return Clipboard.TEXT_TYPE;
        }
        if (method.equalsIgnoreCase(Method.FOP)) {
            return "application/pdf";
        }
        return null;
    }
}
