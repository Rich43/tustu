package com.sun.org.apache.xml.internal.serialize;

import com.sun.org.apache.xalan.internal.templates.Constants;
import com.sun.org.apache.xerces.internal.dom.DOMMessageFormatter;
import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import com.sun.org.apache.xerces.internal.util.NamespaceSupport;
import com.sun.org.apache.xerces.internal.util.SymbolTable;
import com.sun.org.apache.xerces.internal.util.XMLChar;
import com.sun.org.apache.xerces.internal.util.XMLSymbols;
import com.sun.org.apache.xerces.internal.xni.NamespaceContext;
import com.sun.org.apache.xml.internal.serializer.SerializerConstants;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Map;
import java.util.MissingResourceException;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.AttributeList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/serialize/XMLSerializer.class */
public class XMLSerializer extends BaseMarkupSerializer {
    protected static final boolean DEBUG = false;
    protected NamespaceSupport fNSBinder;
    protected NamespaceSupport fLocalNSBinder;
    protected SymbolTable fSymbolTable;
    protected static final String PREFIX = "NS";
    protected boolean fNamespaces;
    protected boolean fNamespacePrefixes;
    private boolean fPreserveSpace;

    public XMLSerializer() {
        super(new OutputFormat("xml", (String) null, false));
        this.fNamespaces = false;
        this.fNamespacePrefixes = true;
    }

    public XMLSerializer(OutputFormat format) {
        super(format != null ? format : new OutputFormat("xml", (String) null, false));
        this.fNamespaces = false;
        this.fNamespacePrefixes = true;
        this._format.setMethod("xml");
    }

    public XMLSerializer(Writer writer, OutputFormat format) {
        super(format != null ? format : new OutputFormat("xml", (String) null, false));
        this.fNamespaces = false;
        this.fNamespacePrefixes = true;
        this._format.setMethod("xml");
        setOutputCharStream(writer);
    }

    public XMLSerializer(OutputStream output, OutputFormat format) {
        super(format != null ? format : new OutputFormat("xml", (String) null, false));
        this.fNamespaces = false;
        this.fNamespacePrefixes = true;
        this._format.setMethod("xml");
        setOutputByteStream(output);
    }

    @Override // com.sun.org.apache.xml.internal.serialize.BaseMarkupSerializer, com.sun.org.apache.xml.internal.serialize.Serializer
    public void setOutputFormat(OutputFormat format) throws MissingResourceException {
        super.setOutputFormat(format != null ? format : new OutputFormat("xml", (String) null, false));
    }

    public void setNamespaces(boolean namespaces) {
        this.fNamespaces = namespaces;
        if (this.fNSBinder == null) {
            this.fNSBinder = new NamespaceSupport();
            this.fLocalNSBinder = new NamespaceSupport();
            this.fSymbolTable = new SymbolTable();
        }
    }

    @Override // org.xml.sax.ContentHandler
    public void startElement(String namespaceURI, String localName, String rawName, Attributes attrs) throws SAXException, MissingResourceException, ArrayIndexOutOfBoundsException {
        String prefix;
        try {
            if (this._printer == null) {
                String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.SERIALIZER_DOMAIN, "NoWriterSupplied", null);
                throw new IllegalStateException(msg);
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
                if (state.inCData) {
                    this._printer.printText("]]>");
                    state.inCData = false;
                }
                if (this._indenting && !state.preserveSpace && (state.empty || state.afterElement || state.afterComment)) {
                    this._printer.breakLine();
                }
            }
            boolean preserveSpace = state.preserveSpace;
            Attributes attrs2 = extractNamespaces(attrs);
            if (rawName == null || rawName.length() == 0) {
                if (localName == null) {
                    String msg2 = DOMMessageFormatter.formatMessage(DOMMessageFormatter.SERIALIZER_DOMAIN, "NoName", null);
                    throw new SAXException(msg2);
                }
                if (namespaceURI != null && !namespaceURI.equals("")) {
                    String prefix2 = getPrefix(namespaceURI);
                    if (prefix2 != null && prefix2.length() > 0) {
                        rawName = prefix2 + CallSiteDescriptor.TOKEN_DELIMITER + localName;
                    } else {
                        rawName = localName;
                    }
                } else {
                    rawName = localName;
                }
            }
            this._printer.printText('<');
            this._printer.printText(rawName);
            this._printer.indent();
            if (attrs2 != null) {
                for (int i2 = 0; i2 < attrs2.getLength(); i2++) {
                    this._printer.printSpace();
                    String name = attrs2.getQName(i2);
                    if (name != null && name.length() == 0) {
                        name = attrs2.getLocalName(i2);
                        String attrURI = attrs2.getURI(i2);
                        if (attrURI != null && attrURI.length() != 0 && ((namespaceURI == null || namespaceURI.length() == 0 || !attrURI.equals(namespaceURI)) && (prefix = getPrefix(attrURI)) != null && prefix.length() > 0)) {
                            name = prefix + CallSiteDescriptor.TOKEN_DELIMITER + name;
                        }
                    }
                    String value = attrs2.getValue(i2);
                    if (value == null) {
                        value = "";
                    }
                    this._printer.printText(name);
                    this._printer.printText("=\"");
                    printEscaped(value);
                    this._printer.printText('\"');
                    if (name.equals(Constants.ATTRNAME_XMLSPACE)) {
                        if (value.equals(SchemaSymbols.ATTVAL_PRESERVE)) {
                            preserveSpace = true;
                        } else {
                            preserveSpace = this._format.getPreserveSpace();
                        }
                    }
                }
            }
            if (this._prefixes != null) {
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
            String name3 = (localName == null || localName.length() == 0) ? rawName : namespaceURI + "^" + localName;
            state2.doCData = this._format.isCDataElement(name3);
            state2.unescaped = this._format.isNonEscapingElement(name3);
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
        this._printer.unindent();
        ElementState state = getElementState();
        if (state.empty) {
            this._printer.printText("/>");
        } else {
            if (state.inCData) {
                this._printer.printText("]]>");
            }
            if (this._indenting && !state.preserveSpace && (state.afterElement || state.afterComment)) {
                this._printer.breakLine();
            }
            this._printer.printText("</");
            this._printer.printText(state.rawName);
            this._printer.printText('>');
        }
        ElementState state2 = leaveElementState();
        state2.afterElement = true;
        state2.afterComment = false;
        state2.empty = false;
        if (isDocumentState()) {
            this._printer.flush();
        }
    }

    @Override // org.xml.sax.DocumentHandler
    public void startElement(String tagName, AttributeList attrs) throws SAXException, MissingResourceException {
        try {
            if (this._printer == null) {
                String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.SERIALIZER_DOMAIN, "NoWriterSupplied", null);
                throw new IllegalStateException(msg);
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
                if (state.inCData) {
                    this._printer.printText("]]>");
                    state.inCData = false;
                }
                if (this._indenting && !state.preserveSpace && (state.empty || state.afterElement || state.afterComment)) {
                    this._printer.breakLine();
                }
            }
            boolean preserveSpace = state.preserveSpace;
            this._printer.printText('<');
            this._printer.printText(tagName);
            this._printer.indent();
            if (attrs != null) {
                for (int i2 = 0; i2 < attrs.getLength(); i2++) {
                    this._printer.printSpace();
                    String name = attrs.getName(i2);
                    String value = attrs.getValue(i2);
                    if (value != null) {
                        this._printer.printText(name);
                        this._printer.printText("=\"");
                        printEscaped(value);
                        this._printer.printText('\"');
                    }
                    if (name.equals(Constants.ATTRNAME_XMLSPACE)) {
                        if (value.equals(SchemaSymbols.ATTVAL_PRESERVE)) {
                            preserveSpace = true;
                        } else {
                            preserveSpace = this._format.getPreserveSpace();
                        }
                    }
                }
            }
            ElementState state2 = enterElementState(null, null, tagName, preserveSpace);
            state2.doCData = this._format.isCDataElement(tagName);
            state2.unescaped = this._format.isNonEscapingElement(tagName);
        } catch (IOException except) {
            throw new SAXException(except);
        }
    }

    @Override // org.xml.sax.DocumentHandler
    public void endElement(String tagName) throws SAXException {
        endElement(null, null, tagName);
    }

    protected void startDocument(String rootTagName) throws IOException {
        String dtd = this._printer.leaveDTD();
        if (!this._started) {
            if (!this._format.getOmitXMLDeclaration()) {
                StringBuffer buffer = new StringBuffer("<?xml version=\"");
                if (this._format.getVersion() != null) {
                    buffer.append(this._format.getVersion());
                } else {
                    buffer.append("1.0");
                }
                buffer.append('\"');
                String format_encoding = this._format.getEncoding();
                if (format_encoding != null) {
                    buffer.append(" encoding=\"");
                    buffer.append(format_encoding);
                    buffer.append('\"');
                }
                if (this._format.getStandalone() && this._docTypeSystemId == null && this._docTypePublicId == null) {
                    buffer.append(" standalone=\"yes\"");
                }
                buffer.append("?>");
                this._printer.printText(buffer);
                this._printer.breakLine();
            }
            if (!this._format.getOmitDocumentType()) {
                if (this._docTypeSystemId != null) {
                    this._printer.printText("<!DOCTYPE ");
                    this._printer.printText(rootTagName);
                    if (this._docTypePublicId != null) {
                        this._printer.printText(" PUBLIC ");
                        printDoctypeURL(this._docTypePublicId);
                        if (this._indenting) {
                            this._printer.breakLine();
                            for (int i2 = 0; i2 < 18 + rootTagName.length(); i2++) {
                                this._printer.printText(" ");
                            }
                        } else {
                            this._printer.printText(" ");
                        }
                        printDoctypeURL(this._docTypeSystemId);
                    } else {
                        this._printer.printText(" SYSTEM ");
                        printDoctypeURL(this._docTypeSystemId);
                    }
                    if (dtd != null && dtd.length() > 0) {
                        this._printer.printText(" [");
                        printText(dtd, true, true);
                        this._printer.printText(']');
                    }
                    this._printer.printText(">");
                    this._printer.breakLine();
                } else if (dtd != null && dtd.length() > 0) {
                    this._printer.printText("<!DOCTYPE ");
                    this._printer.printText(rootTagName);
                    this._printer.printText(" [");
                    printText(dtd, true, true);
                    this._printer.printText("]>");
                    this._printer.breakLine();
                }
            }
        }
        this._started = true;
        serializePreRoot();
    }

    @Override // com.sun.org.apache.xml.internal.serialize.BaseMarkupSerializer
    protected void serializeElement(Element elem) throws MissingResourceException, IOException {
        if (this.fNamespaces) {
            this.fLocalNSBinder.reset();
            this.fNSBinder.pushContext();
        }
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
            if (state.inCData) {
                this._printer.printText("]]>");
                state.inCData = false;
            }
            if (this._indenting && !state.preserveSpace && (state.empty || state.afterElement || state.afterComment)) {
                this._printer.breakLine();
            }
        }
        this.fPreserveSpace = state.preserveSpace;
        int length = 0;
        NamedNodeMap attrMap = null;
        if (elem.hasAttributes()) {
            attrMap = elem.getAttributes();
            length = attrMap.getLength();
        }
        if (!this.fNamespaces) {
            this._printer.printText('<');
            this._printer.printText(tagName);
            this._printer.indent();
            for (int i2 = 0; i2 < length; i2++) {
                Attr attr = (Attr) attrMap.item(i2);
                String name = attr.getName();
                String value = attr.getValue();
                if (value == null) {
                    value = "";
                }
                printAttribute(name, value, attr.getSpecified(), attr);
            }
        } else {
            for (int i3 = 0; i3 < length; i3++) {
                Attr attr2 = (Attr) attrMap.item(i3);
                String uri = attr2.getNamespaceURI();
                if (uri != null && uri.equals(NamespaceContext.XMLNS_URI)) {
                    String value2 = attr2.getNodeValue();
                    if (value2 == null) {
                        value2 = XMLSymbols.EMPTY_STRING;
                    }
                    if (value2.equals(NamespaceContext.XMLNS_URI)) {
                        if (this.fDOMErrorHandler != null) {
                            String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/TR/1998/REC-xml-19980210", "CantBindXMLNS", null);
                            modifyDOMError(msg, (short) 2, null, attr2);
                            boolean continueProcess = this.fDOMErrorHandler.handleError(this.fDOMError);
                            if (!continueProcess) {
                                throw new RuntimeException(DOMMessageFormatter.formatMessage(DOMMessageFormatter.SERIALIZER_DOMAIN, "SerializationStopped", null));
                            }
                        } else {
                            continue;
                        }
                    } else {
                        String prefix = attr2.getPrefix();
                        String prefix2 = (prefix == null || prefix.length() == 0) ? XMLSymbols.EMPTY_STRING : this.fSymbolTable.addSymbol(prefix);
                        String localpart = this.fSymbolTable.addSymbol(attr2.getLocalName());
                        if (prefix2 == XMLSymbols.PREFIX_XMLNS) {
                            String value3 = this.fSymbolTable.addSymbol(value2);
                            if (value3.length() != 0) {
                                this.fNSBinder.declarePrefix(localpart, value3);
                            }
                        } else {
                            this.fNSBinder.declarePrefix(XMLSymbols.EMPTY_STRING, this.fSymbolTable.addSymbol(value2));
                        }
                    }
                }
            }
            String uri2 = elem.getNamespaceURI();
            String prefix3 = elem.getPrefix();
            if (uri2 != null && prefix3 != null && uri2.length() == 0 && prefix3.length() != 0) {
                prefix3 = null;
                this._printer.printText('<');
                this._printer.printText(elem.getLocalName());
                this._printer.indent();
            } else {
                this._printer.printText('<');
                this._printer.printText(tagName);
                this._printer.indent();
            }
            if (uri2 != null) {
                String uri3 = this.fSymbolTable.addSymbol(uri2);
                String prefix4 = (prefix3 == null || prefix3.length() == 0) ? XMLSymbols.EMPTY_STRING : this.fSymbolTable.addSymbol(prefix3);
                if (this.fNSBinder.getURI(prefix4) != uri3) {
                    if (this.fNamespacePrefixes) {
                        printNamespaceAttr(prefix4, uri3);
                    }
                    this.fLocalNSBinder.declarePrefix(prefix4, uri3);
                    this.fNSBinder.declarePrefix(prefix4, uri3);
                }
            } else if (elem.getLocalName() == null) {
                if (this.fDOMErrorHandler != null) {
                    String msg2 = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NullLocalElementName", new Object[]{elem.getNodeName()});
                    modifyDOMError(msg2, (short) 2, null, elem);
                    boolean continueProcess2 = this.fDOMErrorHandler.handleError(this.fDOMError);
                    if (!continueProcess2) {
                        throw new RuntimeException(DOMMessageFormatter.formatMessage(DOMMessageFormatter.SERIALIZER_DOMAIN, "SerializationStopped", null));
                    }
                }
            } else {
                String uri4 = this.fNSBinder.getURI(XMLSymbols.EMPTY_STRING);
                if (uri4 != null && uri4.length() > 0) {
                    if (this.fNamespacePrefixes) {
                        printNamespaceAttr(XMLSymbols.EMPTY_STRING, XMLSymbols.EMPTY_STRING);
                    }
                    this.fLocalNSBinder.declarePrefix(XMLSymbols.EMPTY_STRING, XMLSymbols.EMPTY_STRING);
                    this.fNSBinder.declarePrefix(XMLSymbols.EMPTY_STRING, XMLSymbols.EMPTY_STRING);
                }
            }
            for (int i4 = 0; i4 < length; i4++) {
                Attr attr3 = (Attr) attrMap.item(i4);
                String value4 = attr3.getValue();
                String name2 = attr3.getNodeName();
                String uri5 = attr3.getNamespaceURI();
                if (uri5 != null && uri5.length() == 0) {
                    uri5 = null;
                    name2 = attr3.getLocalName();
                }
                if (value4 == null) {
                    value4 = XMLSymbols.EMPTY_STRING;
                }
                if (uri5 != null) {
                    String prefix5 = attr3.getPrefix();
                    String prefix6 = prefix5 == null ? XMLSymbols.EMPTY_STRING : this.fSymbolTable.addSymbol(prefix5);
                    String localpart2 = this.fSymbolTable.addSymbol(attr3.getLocalName());
                    if (uri5 != null && uri5.equals(NamespaceContext.XMLNS_URI)) {
                        String prefix7 = attr3.getPrefix();
                        String prefix8 = (prefix7 == null || prefix7.length() == 0) ? XMLSymbols.EMPTY_STRING : this.fSymbolTable.addSymbol(prefix7);
                        String localpart3 = this.fSymbolTable.addSymbol(attr3.getLocalName());
                        if (prefix8 == XMLSymbols.PREFIX_XMLNS) {
                            String localUri = this.fLocalNSBinder.getURI(localpart3);
                            String value5 = this.fSymbolTable.addSymbol(value4);
                            if (value5.length() != 0 && localUri == null) {
                                if (this.fNamespacePrefixes) {
                                    printNamespaceAttr(localpart3, value5);
                                }
                                this.fLocalNSBinder.declarePrefix(localpart3, value5);
                            }
                        } else {
                            this.fNSBinder.getURI(XMLSymbols.EMPTY_STRING);
                            String localUri2 = this.fLocalNSBinder.getURI(XMLSymbols.EMPTY_STRING);
                            String value6 = this.fSymbolTable.addSymbol(value4);
                            if (localUri2 == null && this.fNamespacePrefixes) {
                                printNamespaceAttr(XMLSymbols.EMPTY_STRING, value6);
                            }
                        }
                    } else {
                        String uri6 = this.fSymbolTable.addSymbol(uri5);
                        String declaredURI = this.fNSBinder.getURI(prefix6);
                        if (prefix6 == XMLSymbols.EMPTY_STRING || declaredURI != uri6) {
                            name2 = attr3.getNodeName();
                            String declaredPrefix = this.fNSBinder.getPrefix(uri6);
                            if (declaredPrefix != null && declaredPrefix != XMLSymbols.EMPTY_STRING) {
                                name2 = declaredPrefix + CallSiteDescriptor.TOKEN_DELIMITER + localpart2;
                            } else {
                                if (prefix6 == XMLSymbols.EMPTY_STRING || this.fLocalNSBinder.getURI(prefix6) != null) {
                                    int counter = 1 + 1;
                                    String strAddSymbol = this.fSymbolTable.addSymbol(PREFIX + 1);
                                    while (true) {
                                        prefix6 = strAddSymbol;
                                        if (this.fLocalNSBinder.getURI(prefix6) == null) {
                                            break;
                                        }
                                        int i5 = counter;
                                        counter++;
                                        strAddSymbol = this.fSymbolTable.addSymbol(PREFIX + i5);
                                    }
                                    name2 = prefix6 + CallSiteDescriptor.TOKEN_DELIMITER + localpart2;
                                }
                                if (this.fNamespacePrefixes) {
                                    printNamespaceAttr(prefix6, uri6);
                                }
                                value4 = this.fSymbolTable.addSymbol(value4);
                                this.fLocalNSBinder.declarePrefix(prefix6, value4);
                                this.fNSBinder.declarePrefix(prefix6, uri6);
                            }
                        }
                        printAttribute(name2, value4 == null ? XMLSymbols.EMPTY_STRING : value4, attr3.getSpecified(), attr3);
                    }
                } else if (attr3.getLocalName() == null) {
                    if (this.fDOMErrorHandler != null) {
                        String msg3 = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NullLocalAttrName", new Object[]{attr3.getNodeName()});
                        modifyDOMError(msg3, (short) 2, null, attr3);
                        boolean continueProcess3 = this.fDOMErrorHandler.handleError(this.fDOMError);
                        if (!continueProcess3) {
                            throw new RuntimeException(DOMMessageFormatter.formatMessage(DOMMessageFormatter.SERIALIZER_DOMAIN, "SerializationStopped", null));
                        }
                    }
                    printAttribute(name2, value4, attr3.getSpecified(), attr3);
                } else {
                    printAttribute(name2, value4, attr3.getSpecified(), attr3);
                }
            }
        }
        if (elem.hasChildNodes()) {
            ElementState state2 = enterElementState(null, null, tagName, this.fPreserveSpace);
            state2.doCData = this._format.isCDataElement(tagName);
            state2.unescaped = this._format.isNonEscapingElement(tagName);
            Node firstChild = elem.getFirstChild();
            while (true) {
                Node child = firstChild;
                if (child == null) {
                    break;
                }
                serializeNode(child);
                firstChild = child.getNextSibling();
            }
            if (this.fNamespaces) {
                this.fNSBinder.popContext();
            }
            endElementIO(null, null, tagName);
            return;
        }
        if (this.fNamespaces) {
            this.fNSBinder.popContext();
        }
        this._printer.unindent();
        this._printer.printText("/>");
        state.afterElement = true;
        state.afterComment = false;
        state.empty = false;
        if (isDocumentState()) {
            this._printer.flush();
        }
    }

    private void printNamespaceAttr(String prefix, String uri) throws IOException {
        this._printer.printSpace();
        if (prefix == XMLSymbols.EMPTY_STRING) {
            this._printer.printText(XMLSymbols.PREFIX_XMLNS);
        } else {
            this._printer.printText("xmlns:" + prefix);
        }
        this._printer.printText("=\"");
        printEscaped(uri);
        this._printer.printText('\"');
    }

    private void printAttribute(String name, String value, boolean isSpecified, Attr attr) throws IOException {
        if (isSpecified || (this.features & 64) == 0) {
            if (this.fDOMFilter != null && (this.fDOMFilter.getWhatToShow() & 2) != 0) {
                short code = this.fDOMFilter.acceptNode(attr);
                switch (code) {
                    case 2:
                    case 3:
                        return;
                }
            }
            this._printer.printSpace();
            this._printer.printText(name);
            this._printer.printText("=\"");
            printEscaped(value);
            this._printer.printText('\"');
        }
        if (name.equals(Constants.ATTRNAME_XMLSPACE)) {
            if (value.equals(SchemaSymbols.ATTVAL_PRESERVE)) {
                this.fPreserveSpace = true;
            } else {
                this.fPreserveSpace = this._format.getPreserveSpace();
            }
        }
    }

    @Override // com.sun.org.apache.xml.internal.serialize.BaseMarkupSerializer
    protected String getEntityRef(int ch) {
        switch (ch) {
            case 34:
                return "quot";
            case 38:
                return "amp";
            case 39:
                return "apos";
            case 60:
                return "lt";
            case 62:
                return "gt";
            default:
                return null;
        }
    }

    private Attributes extractNamespaces(Attributes attrs) throws SAXException, ArrayIndexOutOfBoundsException {
        if (attrs == null) {
            return null;
        }
        int length = attrs.getLength();
        AttributesImpl attrsOnly = new AttributesImpl(attrs);
        for (int i2 = length - 1; i2 >= 0; i2--) {
            String rawName = attrsOnly.getQName(i2);
            if (rawName.startsWith("xmlns")) {
                if (rawName.length() == 5) {
                    startPrefixMapping("", attrs.getValue(i2));
                    attrsOnly.removeAttribute(i2);
                } else if (rawName.charAt(5) == ':') {
                    startPrefixMapping(rawName.substring(6), attrs.getValue(i2));
                    attrsOnly.removeAttribute(i2);
                }
            }
        }
        return attrsOnly;
    }

    @Override // com.sun.org.apache.xml.internal.serialize.BaseMarkupSerializer
    protected void printEscaped(String source) throws IOException {
        int length = source.length();
        int i2 = 0;
        while (i2 < length) {
            int ch = source.charAt(i2);
            if (!XMLChar.isValid(ch)) {
                i2++;
                if (i2 < length) {
                    surrogates(ch, source.charAt(i2), false);
                } else {
                    fatalError("The character '" + ((char) ch) + "' is an invalid XML character");
                }
            } else if (ch == 10 || ch == 13 || ch == 9) {
                printHex(ch);
            } else if (ch == 60) {
                this._printer.printText(SerializerConstants.ENTITY_LT);
            } else if (ch == 38) {
                this._printer.printText(SerializerConstants.ENTITY_AMP);
            } else if (ch == 34) {
                this._printer.printText(SerializerConstants.ENTITY_QUOT);
            } else if (ch >= 32 && this._encodingInfo.isPrintable((char) ch)) {
                this._printer.printText((char) ch);
            } else {
                printHex(ch);
            }
            i2++;
        }
    }

    protected void printXMLChar(int ch) throws IOException {
        if (ch == 13) {
            printHex(ch);
            return;
        }
        if (ch == 60) {
            this._printer.printText(SerializerConstants.ENTITY_LT);
            return;
        }
        if (ch == 38) {
            this._printer.printText(SerializerConstants.ENTITY_AMP);
            return;
        }
        if (ch == 62) {
            this._printer.printText(SerializerConstants.ENTITY_GT);
            return;
        }
        if (ch == 10 || ch == 9 || (ch >= 32 && this._encodingInfo.isPrintable((char) ch))) {
            this._printer.printText((char) ch);
        } else {
            printHex(ch);
        }
    }

    @Override // com.sun.org.apache.xml.internal.serialize.BaseMarkupSerializer
    protected void printText(String text, boolean preserveSpace, boolean unescaped) throws IOException {
        int length = text.length();
        if (preserveSpace) {
            int index = 0;
            while (index < length) {
                char ch = text.charAt(index);
                if (!XMLChar.isValid(ch)) {
                    index++;
                    if (index < length) {
                        surrogates(ch, text.charAt(index), true);
                    } else {
                        fatalError("The character '" + ch + "' is an invalid XML character");
                    }
                } else if (unescaped) {
                    this._printer.printText(ch);
                } else {
                    printXMLChar(ch);
                }
                index++;
            }
            return;
        }
        int index2 = 0;
        while (index2 < length) {
            char ch2 = text.charAt(index2);
            if (!XMLChar.isValid(ch2)) {
                index2++;
                if (index2 < length) {
                    surrogates(ch2, text.charAt(index2), true);
                } else {
                    fatalError("The character '" + ch2 + "' is an invalid XML character");
                }
            } else if (unescaped) {
                this._printer.printText(ch2);
            } else {
                printXMLChar(ch2);
            }
            index2++;
        }
    }

    @Override // com.sun.org.apache.xml.internal.serialize.BaseMarkupSerializer
    protected void printText(char[] chars, int start, int length, boolean preserveSpace, boolean unescaped) throws IOException {
        if (preserveSpace) {
            while (true) {
                int i2 = length;
                length--;
                if (i2 > 0) {
                    int i3 = start;
                    start++;
                    char ch = chars[i3];
                    if (!XMLChar.isValid(ch)) {
                        length--;
                        if (length > 0) {
                            start++;
                            surrogates(ch, chars[start], true);
                        } else {
                            fatalError("The character '" + ch + "' is an invalid XML character");
                        }
                    } else if (unescaped) {
                        this._printer.printText(ch);
                    } else {
                        printXMLChar(ch);
                    }
                } else {
                    return;
                }
            }
        } else {
            while (true) {
                int i4 = length;
                length--;
                if (i4 > 0) {
                    int i5 = start;
                    start++;
                    char ch2 = chars[i5];
                    if (!XMLChar.isValid(ch2)) {
                        length--;
                        if (length > 0) {
                            start++;
                            surrogates(ch2, chars[start], true);
                        } else {
                            fatalError("The character '" + ch2 + "' is an invalid XML character");
                        }
                    } else if (unescaped) {
                        this._printer.printText(ch2);
                    } else {
                        printXMLChar(ch2);
                    }
                } else {
                    return;
                }
            }
        }
    }

    @Override // com.sun.org.apache.xml.internal.serialize.BaseMarkupSerializer
    protected void checkUnboundNamespacePrefixedNode(Node node) throws IOException {
        if (this.fNamespaces) {
            Node firstChild = node.getFirstChild();
            while (true) {
                Node child = firstChild;
                if (child != null) {
                    Node next = child.getNextSibling();
                    String prefix = child.getPrefix();
                    String prefix2 = (prefix == null || prefix.length() == 0) ? XMLSymbols.EMPTY_STRING : this.fSymbolTable.addSymbol(prefix);
                    if (this.fNSBinder.getURI(prefix2) == null && prefix2 != null) {
                        fatalError("The replacement text of the entity node '" + node.getNodeName() + "' contains an element node '" + child.getNodeName() + "' with an undeclared prefix '" + prefix2 + "'.");
                    }
                    if (child.getNodeType() == 1) {
                        NamedNodeMap attrs = child.getAttributes();
                        for (int i2 = 0; i2 < attrs.getLength(); i2++) {
                            String attrPrefix = attrs.item(i2).getPrefix();
                            String attrPrefix2 = (attrPrefix == null || attrPrefix.length() == 0) ? XMLSymbols.EMPTY_STRING : this.fSymbolTable.addSymbol(attrPrefix);
                            if (this.fNSBinder.getURI(attrPrefix2) == null && attrPrefix2 != null) {
                                fatalError("The replacement text of the entity node '" + node.getNodeName() + "' contains an element node '" + child.getNodeName() + "' with an attribute '" + attrs.item(i2).getNodeName() + "' an undeclared prefix '" + attrPrefix2 + "'.");
                            }
                        }
                    }
                    if (child.hasChildNodes()) {
                        checkUnboundNamespacePrefixedNode(child);
                    }
                    firstChild = next;
                } else {
                    return;
                }
            }
        }
    }

    @Override // com.sun.org.apache.xml.internal.serialize.BaseMarkupSerializer
    public boolean reset() throws MissingResourceException {
        super.reset();
        if (this.fNSBinder != null) {
            this.fNSBinder.reset();
            this.fNSBinder.declarePrefix(XMLSymbols.EMPTY_STRING, XMLSymbols.EMPTY_STRING);
            return true;
        }
        return true;
    }
}
