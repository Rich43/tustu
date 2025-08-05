package com.sun.org.apache.xml.internal.serialize;

import com.sun.org.apache.xerces.internal.dom.DOMMessageFormatter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.apache.commons.net.ftp.FTP;
import org.icepdf.core.util.PdfOps;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.AttributeList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/serialize/HTMLSerializer.class */
public class HTMLSerializer extends BaseMarkupSerializer {
    private boolean _xhtml;
    public static final String XHTMLNamespace = "http://www.w3.org/1999/xhtml";
    private String fUserXHTMLNamespace;

    protected HTMLSerializer(boolean xhtml, OutputFormat format) {
        super(format);
        this.fUserXHTMLNamespace = null;
        this._xhtml = xhtml;
    }

    public HTMLSerializer() {
        this(false, new OutputFormat("html", FTP.DEFAULT_CONTROL_ENCODING, false));
    }

    public HTMLSerializer(OutputFormat format) {
        this(false, format != null ? format : new OutputFormat("html", FTP.DEFAULT_CONTROL_ENCODING, false));
    }

    public HTMLSerializer(Writer writer, OutputFormat format) {
        this(false, format != null ? format : new OutputFormat("html", FTP.DEFAULT_CONTROL_ENCODING, false));
        setOutputCharStream(writer);
    }

    public HTMLSerializer(OutputStream output, OutputFormat format) {
        this(false, format != null ? format : new OutputFormat("html", FTP.DEFAULT_CONTROL_ENCODING, false));
        setOutputByteStream(output);
    }

    @Override // com.sun.org.apache.xml.internal.serialize.BaseMarkupSerializer, com.sun.org.apache.xml.internal.serialize.Serializer
    public void setOutputFormat(OutputFormat format) throws MissingResourceException {
        super.setOutputFormat(format != null ? format : new OutputFormat("html", FTP.DEFAULT_CONTROL_ENCODING, false));
    }

    public void setXHTMLNamespace(String newNamespace) {
        this.fUserXHTMLNamespace = newNamespace;
    }

    @Override // org.xml.sax.ContentHandler
    public void startElement(String namespaceURI, String localName, String rawName, Attributes attrs) throws SAXException {
        String prefix;
        String htmlName;
        boolean addNSAttr = false;
        try {
            if (this._printer == null) {
                throw new IllegalStateException(DOMMessageFormatter.formatMessage(DOMMessageFormatter.SERIALIZER_DOMAIN, "NoWriterSupplied", null));
            }
            ElementState state = getElementState();
            if (isDocumentState()) {
                if (!this._started) {
                    startDocument((localName == null || localName.length() == 0) ? rawName : localName);
                }
            } else {
                if (state.empty) {
                    this._printer.printText('>');
                }
                if (this._indenting && !state.preserveSpace && (state.empty || state.afterElement)) {
                    this._printer.breakLine();
                }
            }
            boolean preserveSpace = state.preserveSpace;
            boolean hasNamespaceURI = (namespaceURI == null || namespaceURI.length() == 0) ? false : true;
            if (rawName == null || rawName.length() == 0) {
                rawName = localName;
                if (hasNamespaceURI && (prefix = getPrefix(namespaceURI)) != null && prefix.length() != 0) {
                    rawName = prefix + CallSiteDescriptor.TOKEN_DELIMITER + localName;
                }
                addNSAttr = true;
            }
            if (!hasNamespaceURI) {
                htmlName = rawName;
            } else if (namespaceURI.equals("http://www.w3.org/1999/xhtml") || (this.fUserXHTMLNamespace != null && this.fUserXHTMLNamespace.equals(namespaceURI))) {
                htmlName = localName;
            } else {
                htmlName = null;
            }
            this._printer.printText('<');
            if (this._xhtml) {
                this._printer.printText(rawName.toLowerCase(Locale.ENGLISH));
            } else {
                this._printer.printText(rawName);
            }
            this._printer.indent();
            if (attrs != null) {
                for (int i2 = 0; i2 < attrs.getLength(); i2++) {
                    this._printer.printSpace();
                    String name = attrs.getQName(i2).toLowerCase(Locale.ENGLISH);
                    String value = attrs.getValue(i2);
                    if (this._xhtml || hasNamespaceURI) {
                        if (value == null) {
                            this._printer.printText(name);
                            this._printer.printText("=\"\"");
                        } else {
                            this._printer.printText(name);
                            this._printer.printText("=\"");
                            printEscaped(value);
                            this._printer.printText('\"');
                        }
                    } else {
                        if (value == null) {
                            value = "";
                        }
                        if (!this._format.getPreserveEmptyAttributes() && value.length() == 0) {
                            this._printer.printText(name);
                        } else if (HTMLdtd.isURI(rawName, name)) {
                            this._printer.printText(name);
                            this._printer.printText("=\"");
                            this._printer.printText(escapeURI(value));
                            this._printer.printText('\"');
                        } else if (HTMLdtd.isBoolean(rawName, name)) {
                            this._printer.printText(name);
                        } else {
                            this._printer.printText(name);
                            this._printer.printText("=\"");
                            printEscaped(value);
                            this._printer.printText('\"');
                        }
                    }
                }
            }
            if (htmlName != null && HTMLdtd.isPreserveSpace(htmlName)) {
                preserveSpace = true;
            }
            if (addNSAttr) {
                for (Map.Entry<String, String> entry : this._prefixes.entrySet()) {
                    this._printer.printSpace();
                    String value2 = entry.getKey();
                    String name2 = entry.getValue();
                    if (name2.length() == 0) {
                        this._printer.printText("xmlns=\"");
                        printEscaped(value2);
                        this._printer.printText('\"');
                    } else {
                        this._printer.printText("xmlns:");
                        this._printer.printText(name2);
                        this._printer.printText("=\"");
                        printEscaped(value2);
                        this._printer.printText('\"');
                    }
                }
            }
            ElementState state2 = enterElementState(namespaceURI, localName, rawName, preserveSpace);
            if (htmlName != null && (htmlName.equalsIgnoreCase("A") || htmlName.equalsIgnoreCase(PdfOps.TD_TOKEN))) {
                state2.empty = false;
                this._printer.printText('>');
            }
            if (htmlName != null && (rawName.equalsIgnoreCase("SCRIPT") || rawName.equalsIgnoreCase("STYLE"))) {
                if (this._xhtml) {
                    state2.doCData = true;
                } else {
                    state2.unescaped = true;
                }
            }
        } catch (IOException except) {
            throw new SAXException(except);
        }
    }

    @Override // org.xml.sax.ContentHandler
    public void endElement(String namespaceURI, String localName, String rawName) throws SAXException {
        try {
            endElementIO(namespaceURI, localName, rawName);
        } catch (IOException except) {
            throw new SAXException(except);
        }
    }

    public void endElementIO(String namespaceURI, String localName, String rawName) throws IOException {
        String htmlName;
        this._printer.unindent();
        ElementState state = getElementState();
        if (state.namespaceURI == null || state.namespaceURI.length() == 0) {
            htmlName = state.rawName;
        } else if (state.namespaceURI.equals("http://www.w3.org/1999/xhtml") || (this.fUserXHTMLNamespace != null && this.fUserXHTMLNamespace.equals(state.namespaceURI))) {
            htmlName = state.localName;
        } else {
            htmlName = null;
        }
        if (this._xhtml) {
            if (state.empty) {
                this._printer.printText(" />");
            } else {
                if (state.inCData) {
                    this._printer.printText("]]>");
                }
                this._printer.printText("</");
                this._printer.printText(state.rawName.toLowerCase(Locale.ENGLISH));
                this._printer.printText('>');
            }
        } else {
            if (state.empty) {
                this._printer.printText('>');
            }
            if (htmlName == null || !HTMLdtd.isOnlyOpening(htmlName)) {
                if (this._indenting && !state.preserveSpace && state.afterElement) {
                    this._printer.breakLine();
                }
                if (state.inCData) {
                    this._printer.printText("]]>");
                }
                this._printer.printText("</");
                this._printer.printText(state.rawName);
                this._printer.printText('>');
            }
        }
        ElementState state2 = leaveElementState();
        if (htmlName == null || (!htmlName.equalsIgnoreCase("A") && !htmlName.equalsIgnoreCase(PdfOps.TD_TOKEN))) {
            state2.afterElement = true;
        }
        state2.empty = false;
        if (isDocumentState()) {
            this._printer.flush();
        }
    }

    @Override // com.sun.org.apache.xml.internal.serialize.BaseMarkupSerializer, org.xml.sax.ContentHandler
    public void characters(char[] chars, int start, int length) throws SAXException {
        try {
            ElementState state = content();
            state.doCData = false;
            super.characters(chars, start, length);
        } catch (IOException except) {
            throw new SAXException(except);
        }
    }

    @Override // org.xml.sax.DocumentHandler
    public void startElement(String tagName, AttributeList attrs) throws SAXException {
        try {
            if (this._printer == null) {
                throw new IllegalStateException(DOMMessageFormatter.formatMessage(DOMMessageFormatter.SERIALIZER_DOMAIN, "NoWriterSupplied", null));
            }
            ElementState state = getElementState();
            if (isDocumentState()) {
                if (!this._started) {
                    startDocument(tagName);
                }
            } else {
                if (state.empty) {
                    this._printer.printText('>');
                }
                if (this._indenting && !state.preserveSpace && (state.empty || state.afterElement)) {
                    this._printer.breakLine();
                }
            }
            boolean preserveSpace = state.preserveSpace;
            this._printer.printText('<');
            if (this._xhtml) {
                this._printer.printText(tagName.toLowerCase(Locale.ENGLISH));
            } else {
                this._printer.printText(tagName);
            }
            this._printer.indent();
            if (attrs != null) {
                for (int i2 = 0; i2 < attrs.getLength(); i2++) {
                    this._printer.printSpace();
                    String name = attrs.getName(i2).toLowerCase(Locale.ENGLISH);
                    String value = attrs.getValue(i2);
                    if (this._xhtml) {
                        if (value == null) {
                            this._printer.printText(name);
                            this._printer.printText("=\"\"");
                        } else {
                            this._printer.printText(name);
                            this._printer.printText("=\"");
                            printEscaped(value);
                            this._printer.printText('\"');
                        }
                    } else {
                        if (value == null) {
                            value = "";
                        }
                        if (!this._format.getPreserveEmptyAttributes() && value.length() == 0) {
                            this._printer.printText(name);
                        } else if (HTMLdtd.isURI(tagName, name)) {
                            this._printer.printText(name);
                            this._printer.printText("=\"");
                            this._printer.printText(escapeURI(value));
                            this._printer.printText('\"');
                        } else if (HTMLdtd.isBoolean(tagName, name)) {
                            this._printer.printText(name);
                        } else {
                            this._printer.printText(name);
                            this._printer.printText("=\"");
                            printEscaped(value);
                            this._printer.printText('\"');
                        }
                    }
                }
            }
            if (HTMLdtd.isPreserveSpace(tagName)) {
                preserveSpace = true;
            }
            ElementState state2 = enterElementState(null, null, tagName, preserveSpace);
            if (tagName.equalsIgnoreCase("A") || tagName.equalsIgnoreCase(PdfOps.TD_TOKEN)) {
                state2.empty = false;
                this._printer.printText('>');
            }
            if (tagName.equalsIgnoreCase("SCRIPT") || tagName.equalsIgnoreCase("STYLE")) {
                if (this._xhtml) {
                    state2.doCData = true;
                } else {
                    state2.unescaped = true;
                }
            }
        } catch (IOException except) {
            throw new SAXException(except);
        }
    }

    @Override // org.xml.sax.DocumentHandler
    public void endElement(String tagName) throws SAXException {
        endElement(null, null, tagName);
    }

    protected void startDocument(String rootTagName) throws IOException {
        this._printer.leaveDTD();
        if (!this._started) {
            if (this._docTypePublicId == null && this._docTypeSystemId == null) {
                if (this._xhtml) {
                    this._docTypePublicId = "-//W3C//DTD XHTML 1.0 Strict//EN";
                    this._docTypeSystemId = "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd";
                } else {
                    this._docTypePublicId = "-//W3C//DTD HTML 4.01//EN";
                    this._docTypeSystemId = "http://www.w3.org/TR/html4/strict.dtd";
                }
            }
            if (!this._format.getOmitDocumentType()) {
                if (this._docTypePublicId != null && (!this._xhtml || this._docTypeSystemId != null)) {
                    if (this._xhtml) {
                        this._printer.printText("<!DOCTYPE html PUBLIC ");
                    } else {
                        this._printer.printText("<!DOCTYPE HTML PUBLIC ");
                    }
                    printDoctypeURL(this._docTypePublicId);
                    if (this._docTypeSystemId != null) {
                        if (this._indenting) {
                            this._printer.breakLine();
                            this._printer.printText("                      ");
                        } else {
                            this._printer.printText(' ');
                        }
                        printDoctypeURL(this._docTypeSystemId);
                    }
                    this._printer.printText('>');
                    this._printer.breakLine();
                } else if (this._docTypeSystemId != null) {
                    if (this._xhtml) {
                        this._printer.printText("<!DOCTYPE html SYSTEM ");
                    } else {
                        this._printer.printText("<!DOCTYPE HTML SYSTEM ");
                    }
                    printDoctypeURL(this._docTypeSystemId);
                    this._printer.printText('>');
                    this._printer.breakLine();
                }
            }
        }
        this._started = true;
        serializePreRoot();
    }

    @Override // com.sun.org.apache.xml.internal.serialize.BaseMarkupSerializer
    protected void serializeElement(Element elem) throws IOException {
        String tagName = elem.getTagName();
        ElementState state = getElementState();
        if (isDocumentState()) {
            if (!this._started) {
                startDocument(tagName);
            }
        } else {
            if (state.empty) {
                this._printer.printText('>');
            }
            if (this._indenting && !state.preserveSpace && (state.empty || state.afterElement)) {
                this._printer.breakLine();
            }
        }
        boolean preserveSpace = state.preserveSpace;
        this._printer.printText('<');
        if (this._xhtml) {
            this._printer.printText(tagName.toLowerCase(Locale.ENGLISH));
        } else {
            this._printer.printText(tagName);
        }
        this._printer.indent();
        NamedNodeMap attrMap = elem.getAttributes();
        if (attrMap != null) {
            for (int i2 = 0; i2 < attrMap.getLength(); i2++) {
                Attr attr = (Attr) attrMap.item(i2);
                String name = attr.getName().toLowerCase(Locale.ENGLISH);
                String value = attr.getValue();
                if (attr.getSpecified()) {
                    this._printer.printSpace();
                    if (this._xhtml) {
                        if (value == null) {
                            this._printer.printText(name);
                            this._printer.printText("=\"\"");
                        } else {
                            this._printer.printText(name);
                            this._printer.printText("=\"");
                            printEscaped(value);
                            this._printer.printText('\"');
                        }
                    } else {
                        if (value == null) {
                            value = "";
                        }
                        if (!this._format.getPreserveEmptyAttributes() && value.length() == 0) {
                            this._printer.printText(name);
                        } else if (HTMLdtd.isURI(tagName, name)) {
                            this._printer.printText(name);
                            this._printer.printText("=\"");
                            this._printer.printText(escapeURI(value));
                            this._printer.printText('\"');
                        } else if (HTMLdtd.isBoolean(tagName, name)) {
                            this._printer.printText(name);
                        } else {
                            this._printer.printText(name);
                            this._printer.printText("=\"");
                            printEscaped(value);
                            this._printer.printText('\"');
                        }
                    }
                }
            }
        }
        if (HTMLdtd.isPreserveSpace(tagName)) {
            preserveSpace = true;
        }
        if (elem.hasChildNodes() || !HTMLdtd.isEmptyTag(tagName)) {
            ElementState state2 = enterElementState(null, null, tagName, preserveSpace);
            if (tagName.equalsIgnoreCase("A") || tagName.equalsIgnoreCase(PdfOps.TD_TOKEN)) {
                state2.empty = false;
                this._printer.printText('>');
            }
            if (tagName.equalsIgnoreCase("SCRIPT") || tagName.equalsIgnoreCase("STYLE")) {
                if (this._xhtml) {
                    state2.doCData = true;
                } else {
                    state2.unescaped = true;
                }
            }
            Node firstChild = elem.getFirstChild();
            while (true) {
                Node child = firstChild;
                if (child != null) {
                    serializeNode(child);
                    firstChild = child.getNextSibling();
                } else {
                    endElementIO(null, null, tagName);
                    return;
                }
            }
        } else {
            this._printer.unindent();
            if (this._xhtml) {
                this._printer.printText(" />");
            } else {
                this._printer.printText('>');
            }
            state.afterElement = true;
            state.empty = false;
            if (isDocumentState()) {
                this._printer.flush();
            }
        }
    }

    @Override // com.sun.org.apache.xml.internal.serialize.BaseMarkupSerializer
    protected void characters(String text) throws MissingResourceException, IOException {
        content();
        super.characters(text);
    }

    @Override // com.sun.org.apache.xml.internal.serialize.BaseMarkupSerializer
    protected String getEntityRef(int ch) {
        return HTMLdtd.fromChar(ch);
    }

    protected String escapeURI(String uri) {
        int index = uri.indexOf(PdfOps.DOUBLE_QUOTE__TOKEN);
        if (index >= 0) {
            return uri.substring(0, index);
        }
        return uri;
    }
}
