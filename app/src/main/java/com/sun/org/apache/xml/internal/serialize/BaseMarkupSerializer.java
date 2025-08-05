package com.sun.org.apache.xml.internal.serialize;

import com.sun.org.apache.xalan.internal.templates.Constants;
import com.sun.org.apache.xerces.internal.dom.DOMErrorImpl;
import com.sun.org.apache.xerces.internal.dom.DOMLocatorImpl;
import com.sun.org.apache.xerces.internal.dom.DOMMessageFormatter;
import com.sun.org.apache.xerces.internal.util.XMLChar;
import com.sun.org.apache.xml.internal.serializer.SerializerConstants;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Vector;
import jdk.internal.util.xml.impl.XMLStreamWriterImpl;
import org.w3c.dom.DOMError;
import org.w3c.dom.DOMErrorHandler;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.ls.LSException;
import org.w3c.dom.ls.LSSerializerFilter;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.DocumentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.ext.LexicalHandler;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/serialize/BaseMarkupSerializer.class */
public abstract class BaseMarkupSerializer implements ContentHandler, DocumentHandler, LexicalHandler, DTDHandler, DeclHandler, DOMSerializer, Serializer {
    protected DOMErrorHandler fDOMErrorHandler;
    protected LSSerializerFilter fDOMFilter;
    protected EncodingInfo _encodingInfo;
    private int _elementStateCount;
    private Vector _preRoot;
    protected boolean _started;
    private boolean _prepared;
    protected Map<String, String> _prefixes;
    protected String _docTypePublicId;
    protected String _docTypeSystemId;
    protected OutputFormat _format;
    protected Printer _printer;
    protected boolean _indenting;
    private Writer _writer;
    private OutputStream _output;
    protected short features = -1;
    protected final DOMErrorImpl fDOMError = new DOMErrorImpl();
    protected final StringBuffer fStrBuffer = new StringBuffer(40);
    protected Node fCurrentNode = null;
    private ElementState[] _elementStates = new ElementState[10];

    protected abstract String getEntityRef(int i2);

    protected abstract void serializeElement(Element element) throws IOException;

    protected BaseMarkupSerializer(OutputFormat format) {
        for (int i2 = 0; i2 < this._elementStates.length; i2++) {
            this._elementStates[i2] = new ElementState();
        }
        this._format = format;
    }

    @Override // com.sun.org.apache.xml.internal.serialize.Serializer
    public DocumentHandler asDocumentHandler() throws MissingResourceException, IOException {
        prepare();
        return this;
    }

    @Override // com.sun.org.apache.xml.internal.serialize.Serializer
    public ContentHandler asContentHandler() throws MissingResourceException, IOException {
        prepare();
        return this;
    }

    @Override // com.sun.org.apache.xml.internal.serialize.Serializer
    public DOMSerializer asDOMSerializer() throws MissingResourceException, IOException {
        prepare();
        return this;
    }

    @Override // com.sun.org.apache.xml.internal.serialize.Serializer
    public void setOutputByteStream(OutputStream output) throws MissingResourceException {
        if (output == null) {
            String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.SERIALIZER_DOMAIN, "ArgumentIsNull", new Object[]{Constants.ELEMNAME_OUTPUT_STRING});
            throw new NullPointerException(msg);
        }
        this._output = output;
        this._writer = null;
        reset();
    }

    @Override // com.sun.org.apache.xml.internal.serialize.Serializer
    public void setOutputCharStream(Writer writer) throws MissingResourceException {
        if (writer == null) {
            String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.SERIALIZER_DOMAIN, "ArgumentIsNull", new Object[]{"writer"});
            throw new NullPointerException(msg);
        }
        this._writer = writer;
        this._output = null;
        reset();
    }

    @Override // com.sun.org.apache.xml.internal.serialize.Serializer
    public void setOutputFormat(OutputFormat format) throws MissingResourceException {
        if (format == null) {
            String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.SERIALIZER_DOMAIN, "ArgumentIsNull", new Object[]{Constants.ATTRNAME_FORMAT});
            throw new NullPointerException(msg);
        }
        this._format = format;
        reset();
    }

    public boolean reset() throws MissingResourceException {
        if (this._elementStateCount > 1) {
            String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.SERIALIZER_DOMAIN, "ResetInMiddle", null);
            throw new IllegalStateException(msg);
        }
        this._prepared = false;
        this.fCurrentNode = null;
        this.fStrBuffer.setLength(0);
        return true;
    }

    protected void cleanup() {
        this.fCurrentNode = null;
    }

    protected void prepare() throws MissingResourceException, IOException {
        if (this._prepared) {
            return;
        }
        if (this._writer == null && this._output == null) {
            String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.SERIALIZER_DOMAIN, "NoWriterSupplied", null);
            throw new IOException(msg);
        }
        this._encodingInfo = this._format.getEncodingInfo();
        if (this._output != null) {
            this._writer = this._encodingInfo.getWriter(this._output);
        }
        if (this._format.getIndenting()) {
            this._indenting = true;
            this._printer = new IndentPrinter(this._writer, this._format);
        } else {
            this._indenting = false;
            this._printer = new Printer(this._writer, this._format);
        }
        this._elementStateCount = 0;
        ElementState state = this._elementStates[0];
        state.namespaceURI = null;
        state.localName = null;
        state.rawName = null;
        state.preserveSpace = this._format.getPreserveSpace();
        state.empty = true;
        state.afterElement = false;
        state.afterComment = false;
        state.inCData = false;
        state.doCData = false;
        state.prefixes = null;
        this._docTypePublicId = this._format.getDoctypePublic();
        this._docTypeSystemId = this._format.getDoctypeSystem();
        this._started = false;
        this._prepared = true;
    }

    @Override // com.sun.org.apache.xml.internal.serialize.DOMSerializer
    public void serialize(Element elem) throws DOMException, MissingResourceException, IOException {
        reset();
        prepare();
        serializeNode(elem);
        cleanup();
        this._printer.flush();
        if (this._printer.getException() != null) {
            throw this._printer.getException();
        }
    }

    public void serialize(Node node) throws DOMException, MissingResourceException, IOException {
        reset();
        prepare();
        serializeNode(node);
        serializePreRoot();
        this._printer.flush();
        if (this._printer.getException() != null) {
            throw this._printer.getException();
        }
    }

    @Override // com.sun.org.apache.xml.internal.serialize.DOMSerializer
    public void serialize(DocumentFragment frag) throws DOMException, MissingResourceException, IOException {
        reset();
        prepare();
        serializeNode(frag);
        cleanup();
        this._printer.flush();
        if (this._printer.getException() != null) {
            throw this._printer.getException();
        }
    }

    @Override // com.sun.org.apache.xml.internal.serialize.DOMSerializer
    public void serialize(Document doc) throws DOMException, MissingResourceException, IOException {
        reset();
        prepare();
        serializeNode(doc);
        serializePreRoot();
        cleanup();
        this._printer.flush();
        if (this._printer.getException() != null) {
            throw this._printer.getException();
        }
    }

    @Override // org.xml.sax.ContentHandler
    public void startDocument() throws SAXException, MissingResourceException {
        try {
            prepare();
        } catch (IOException except) {
            throw new SAXException(except.toString());
        }
    }

    @Override // org.xml.sax.ContentHandler
    public void characters(char[] chars, int start, int length) throws SAXException {
        try {
            ElementState state = content();
            if (state.inCData || state.doCData) {
                if (!state.inCData) {
                    this._printer.printText("<![CDATA[");
                    state.inCData = true;
                }
                int saveIndent = this._printer.getNextIndent();
                this._printer.setNextIndent(0);
                int end = start + length;
                int index = start;
                while (index < end) {
                    char ch = chars[index];
                    if (ch == ']' && index + 2 < end && chars[index + 1] == ']' && chars[index + 2] == '>') {
                        this._printer.printText(SerializerConstants.CDATA_CONTINUE);
                        index += 2;
                    } else if (!XMLChar.isValid(ch)) {
                        index++;
                        if (index < end) {
                            surrogates(ch, chars[index], true);
                        } else {
                            fatalError("The character '" + ch + "' is an invalid XML character");
                        }
                    } else if ((ch >= ' ' && this._encodingInfo.isPrintable(ch) && ch != 127) || ch == '\n' || ch == '\r' || ch == '\t') {
                        this._printer.printText(ch);
                    } else {
                        this._printer.printText("]]>&#x");
                        this._printer.printText(Integer.toHexString(ch));
                        this._printer.printText(";<![CDATA[");
                    }
                    index++;
                }
                this._printer.setNextIndent(saveIndent);
            } else if (state.preserveSpace) {
                int saveIndent2 = this._printer.getNextIndent();
                this._printer.setNextIndent(0);
                printText(chars, start, length, true, state.unescaped);
                this._printer.setNextIndent(saveIndent2);
            } else {
                printText(chars, start, length, false, state.unescaped);
            }
        } catch (IOException except) {
            throw new SAXException(except);
        }
    }

    @Override // org.xml.sax.ContentHandler
    public void ignorableWhitespace(char[] chars, int start, int length) throws SAXException {
        try {
            content();
            if (this._indenting) {
                this._printer.setThisIndent(0);
                int i2 = start;
                while (true) {
                    int i3 = length;
                    length--;
                    if (i3 <= 0) {
                        break;
                    }
                    this._printer.printText(chars[i2]);
                    i2++;
                }
            }
        } catch (IOException except) {
            throw new SAXException(except);
        }
    }

    @Override // org.xml.sax.ContentHandler
    public final void processingInstruction(String target, String code) throws SAXException {
        try {
            processingInstructionIO(target, code);
        } catch (IOException except) {
            throw new SAXException(except);
        }
    }

    public void processingInstructionIO(String target, String code) throws IOException {
        ElementState state = content();
        int index = target.indexOf("?>");
        if (index >= 0) {
            this.fStrBuffer.append("<?").append(target.substring(0, index));
        } else {
            this.fStrBuffer.append("<?").append(target);
        }
        if (code != null) {
            this.fStrBuffer.append(' ');
            int index2 = code.indexOf("?>");
            if (index2 >= 0) {
                this.fStrBuffer.append(code.substring(0, index2));
            } else {
                this.fStrBuffer.append(code);
            }
        }
        this.fStrBuffer.append("?>");
        if (isDocumentState()) {
            if (this._preRoot == null) {
                this._preRoot = new Vector();
            }
            this._preRoot.addElement(this.fStrBuffer.toString());
        } else {
            this._printer.indent();
            printText(this.fStrBuffer.toString(), true, true);
            this._printer.unindent();
            if (this._indenting) {
                state.afterElement = true;
            }
        }
        this.fStrBuffer.setLength(0);
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void comment(char[] chars, int start, int length) throws SAXException {
        try {
            comment(new String(chars, start, length));
        } catch (IOException except) {
            throw new SAXException(except);
        }
    }

    public void comment(String text) throws IOException {
        if (this._format.getOmitComments()) {
            return;
        }
        ElementState state = content();
        int index = text.indexOf("-->");
        if (index >= 0) {
            this.fStrBuffer.append("<!--").append(text.substring(0, index)).append("-->");
        } else {
            this.fStrBuffer.append("<!--").append(text).append("-->");
        }
        if (isDocumentState()) {
            if (this._preRoot == null) {
                this._preRoot = new Vector();
            }
            this._preRoot.addElement(this.fStrBuffer.toString());
        } else {
            if (this._indenting && !state.preserveSpace) {
                this._printer.breakLine();
            }
            this._printer.indent();
            printText(this.fStrBuffer.toString(), true, true);
            this._printer.unindent();
            if (this._indenting) {
                state.afterElement = true;
            }
        }
        this.fStrBuffer.setLength(0);
        state.afterComment = true;
        state.afterElement = false;
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void startCDATA() {
        ElementState state = getElementState();
        state.doCData = true;
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void endCDATA() {
        ElementState state = getElementState();
        state.doCData = false;
    }

    public void startNonEscaping() {
        ElementState state = getElementState();
        state.unescaped = true;
    }

    public void endNonEscaping() {
        ElementState state = getElementState();
        state.unescaped = false;
    }

    public void startPreserving() {
        ElementState state = getElementState();
        state.preserveSpace = true;
    }

    public void endPreserving() {
        ElementState state = getElementState();
        state.preserveSpace = false;
    }

    @Override // org.xml.sax.ContentHandler
    public void endDocument() throws SAXException {
        try {
            serializePreRoot();
            this._printer.flush();
        } catch (IOException except) {
            throw new SAXException(except);
        }
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void startEntity(String name) {
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void endEntity(String name) {
    }

    @Override // org.xml.sax.ContentHandler
    public void setDocumentLocator(Locator locator) {
    }

    @Override // org.xml.sax.ContentHandler
    public void skippedEntity(String name) throws SAXException {
        try {
            endCDATA();
            content();
            this._printer.printText('&');
            this._printer.printText(name);
            this._printer.printText(';');
        } catch (IOException except) {
            throw new SAXException(except);
        }
    }

    @Override // org.xml.sax.ContentHandler
    public void startPrefixMapping(String prefix, String uri) throws SAXException {
        if (this._prefixes == null) {
            this._prefixes = new HashMap();
        }
        this._prefixes.put(uri, prefix == null ? "" : prefix);
    }

    @Override // org.xml.sax.ContentHandler
    public void endPrefixMapping(String prefix) throws SAXException {
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public final void startDTD(String name, String publicId, String systemId) throws SAXException {
        try {
            this._printer.enterDTD();
            this._docTypePublicId = publicId;
            this._docTypeSystemId = systemId;
        } catch (IOException except) {
            throw new SAXException(except);
        }
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void endDTD() {
    }

    @Override // org.xml.sax.ext.DeclHandler
    public void elementDecl(String name, String model) throws SAXException {
        try {
            this._printer.enterDTD();
            this._printer.printText("<!ELEMENT ");
            this._printer.printText(name);
            this._printer.printText(' ');
            this._printer.printText(model);
            this._printer.printText('>');
            if (this._indenting) {
                this._printer.breakLine();
            }
        } catch (IOException except) {
            throw new SAXException(except);
        }
    }

    @Override // org.xml.sax.ext.DeclHandler
    public void attributeDecl(String eName, String aName, String type, String valueDefault, String value) throws SAXException {
        try {
            this._printer.enterDTD();
            this._printer.printText("<!ATTLIST ");
            this._printer.printText(eName);
            this._printer.printText(' ');
            this._printer.printText(aName);
            this._printer.printText(' ');
            this._printer.printText(type);
            if (valueDefault != null) {
                this._printer.printText(' ');
                this._printer.printText(valueDefault);
            }
            if (value != null) {
                this._printer.printText(" \"");
                printEscaped(value);
                this._printer.printText('\"');
            }
            this._printer.printText('>');
            if (this._indenting) {
                this._printer.breakLine();
            }
        } catch (IOException except) {
            throw new SAXException(except);
        }
    }

    @Override // org.xml.sax.ext.DeclHandler
    public void internalEntityDecl(String name, String value) throws SAXException {
        try {
            this._printer.enterDTD();
            this._printer.printText("<!ENTITY ");
            this._printer.printText(name);
            this._printer.printText(" \"");
            printEscaped(value);
            this._printer.printText("\">");
            if (this._indenting) {
                this._printer.breakLine();
            }
        } catch (IOException except) {
            throw new SAXException(except);
        }
    }

    @Override // org.xml.sax.ext.DeclHandler
    public void externalEntityDecl(String name, String publicId, String systemId) throws SAXException {
        try {
            this._printer.enterDTD();
            unparsedEntityDecl(name, publicId, systemId, null);
        } catch (IOException except) {
            throw new SAXException(except);
        }
    }

    @Override // org.xml.sax.DTDHandler
    public void unparsedEntityDecl(String name, String publicId, String systemId, String notationName) throws SAXException {
        try {
            this._printer.enterDTD();
            if (publicId == null) {
                this._printer.printText("<!ENTITY ");
                this._printer.printText(name);
                this._printer.printText(" SYSTEM ");
                printDoctypeURL(systemId);
            } else {
                this._printer.printText("<!ENTITY ");
                this._printer.printText(name);
                this._printer.printText(" PUBLIC ");
                printDoctypeURL(publicId);
                this._printer.printText(' ');
                printDoctypeURL(systemId);
            }
            if (notationName != null) {
                this._printer.printText(" NDATA ");
                this._printer.printText(notationName);
            }
            this._printer.printText('>');
            if (this._indenting) {
                this._printer.breakLine();
            }
        } catch (IOException except) {
            throw new SAXException(except);
        }
    }

    @Override // org.xml.sax.DTDHandler
    public void notationDecl(String name, String publicId, String systemId) throws SAXException {
        try {
            this._printer.enterDTD();
            if (publicId != null) {
                this._printer.printText("<!NOTATION ");
                this._printer.printText(name);
                this._printer.printText(" PUBLIC ");
                printDoctypeURL(publicId);
                if (systemId != null) {
                    this._printer.printText(' ');
                    printDoctypeURL(systemId);
                }
            } else {
                this._printer.printText("<!NOTATION ");
                this._printer.printText(name);
                this._printer.printText(" SYSTEM ");
                printDoctypeURL(systemId);
            }
            this._printer.printText('>');
            if (this._indenting) {
                this._printer.breakLine();
            }
        } catch (IOException except) {
            throw new SAXException(except);
        }
    }

    protected void serializeNode(Node node) throws DOMException, MissingResourceException, IOException {
        String text;
        this.fCurrentNode = node;
        switch (node.getNodeType()) {
            case 1:
                if (this.fDOMFilter != null && (this.fDOMFilter.getWhatToShow() & 1) != 0) {
                    short code = this.fDOMFilter.acceptNode(node);
                    switch (code) {
                        case 2:
                            break;
                        case 3:
                            Node firstChild = node.getFirstChild();
                            while (true) {
                                Node child = firstChild;
                                if (child == null) {
                                    break;
                                } else {
                                    serializeNode(child);
                                    firstChild = child.getNextSibling();
                                }
                            }
                    }
                    return;
                }
                serializeElement((Element) node);
                return;
            case 2:
            case 6:
            case 10:
            default:
                return;
            case 3:
                String text2 = node.getNodeValue();
                if (text2 != null) {
                    if (this.fDOMFilter != null && (this.fDOMFilter.getWhatToShow() & 4) != 0) {
                        short code2 = this.fDOMFilter.acceptNode(node);
                        switch (code2) {
                            case 2:
                            case 3:
                                break;
                            default:
                                characters(text2);
                                break;
                        }
                        return;
                    }
                    if (!this._indenting || getElementState().preserveSpace || text2.replace('\n', ' ').trim().length() != 0) {
                        characters(text2);
                        return;
                    }
                    return;
                }
                return;
            case 4:
                String text3 = node.getNodeValue();
                if ((this.features & 8) != 0) {
                    if (text3 != null) {
                        if (this.fDOMFilter != null && (this.fDOMFilter.getWhatToShow() & 8) != 0) {
                            short code3 = this.fDOMFilter.acceptNode(node);
                            switch (code3) {
                                case 2:
                                case 3:
                                    break;
                            }
                            return;
                        }
                        startCDATA();
                        characters(text3);
                        endCDATA();
                        return;
                    }
                    return;
                }
                characters(text3);
                return;
            case 5:
                endCDATA();
                content();
                if ((this.features & 4) != 0 || node.getFirstChild() == null) {
                    if (this.fDOMFilter != null && (this.fDOMFilter.getWhatToShow() & 16) != 0) {
                        short code4 = this.fDOMFilter.acceptNode(node);
                        switch (code4) {
                            case 2:
                                break;
                            case 3:
                                Node firstChild2 = node.getFirstChild();
                                while (true) {
                                    Node child2 = firstChild2;
                                    if (child2 == null) {
                                        break;
                                    } else {
                                        serializeNode(child2);
                                        firstChild2 = child2.getNextSibling();
                                    }
                                }
                        }
                        return;
                    }
                    checkUnboundNamespacePrefixedNode(node);
                    this._printer.printText("&");
                    this._printer.printText(node.getNodeName());
                    this._printer.printText(";");
                    return;
                }
                Node firstChild3 = node.getFirstChild();
                while (true) {
                    Node child3 = firstChild3;
                    if (child3 != null) {
                        serializeNode(child3);
                        firstChild3 = child3.getNextSibling();
                    } else {
                        return;
                    }
                }
                break;
            case 7:
                if (this.fDOMFilter != null && (this.fDOMFilter.getWhatToShow() & 64) != 0) {
                    short code5 = this.fDOMFilter.acceptNode(node);
                    switch (code5) {
                        case 2:
                        case 3:
                            break;
                    }
                    return;
                }
                processingInstructionIO(node.getNodeName(), node.getNodeValue());
                return;
            case 8:
                if (!this._format.getOmitComments() && (text = node.getNodeValue()) != null) {
                    if (this.fDOMFilter != null && (this.fDOMFilter.getWhatToShow() & 128) != 0) {
                        short code6 = this.fDOMFilter.acceptNode(node);
                        switch (code6) {
                            case 2:
                            case 3:
                                break;
                        }
                        return;
                    }
                    comment(text);
                    return;
                }
                return;
            case 9:
                serializeDocument();
                DocumentType docType = ((Document) node).getDoctype();
                if (docType != null) {
                    try {
                        this._printer.enterDTD();
                        this._docTypePublicId = docType.getPublicId();
                        this._docTypeSystemId = docType.getSystemId();
                        String internal = docType.getInternalSubset();
                        if (internal != null && internal.length() > 0) {
                            this._printer.printText(internal);
                        }
                        endDTD();
                    } catch (NoSuchMethodError e2) {
                        Class docTypeClass = docType.getClass();
                        String docTypePublicId = null;
                        String docTypeSystemId = null;
                        try {
                            java.lang.reflect.Method getPublicId = docTypeClass.getMethod("getPublicId", (Class[]) null);
                            if (getPublicId.getReturnType().equals(String.class)) {
                                docTypePublicId = (String) getPublicId.invoke(docType, (Object[]) null);
                            }
                        } catch (Exception e3) {
                        }
                        try {
                            java.lang.reflect.Method getSystemId = docTypeClass.getMethod("getSystemId", (Class[]) null);
                            if (getSystemId.getReturnType().equals(String.class)) {
                                docTypeSystemId = (String) getSystemId.invoke(docType, (Object[]) null);
                            }
                        } catch (Exception e4) {
                        }
                        this._printer.enterDTD();
                        this._docTypePublicId = docTypePublicId;
                        this._docTypeSystemId = docTypeSystemId;
                        endDTD();
                    }
                    serializeDTD(docType.getName());
                }
                this._started = true;
                break;
            case 11:
                break;
        }
        Node firstChild4 = node.getFirstChild();
        while (true) {
            Node child4 = firstChild4;
            if (child4 != null) {
                serializeNode(child4);
                firstChild4 = child4.getNextSibling();
            } else {
                return;
            }
        }
    }

    protected void serializeDocument() throws IOException {
        this._printer.leaveDTD();
        if (!this._started && !this._format.getOmitXMLDeclaration()) {
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
        serializePreRoot();
    }

    protected void serializeDTD(String name) throws IOException {
        String dtd = this._printer.leaveDTD();
        if (!this._format.getOmitDocumentType()) {
            if (this._docTypeSystemId != null) {
                this._printer.printText("<!DOCTYPE ");
                this._printer.printText(name);
                if (this._docTypePublicId != null) {
                    this._printer.printText(" PUBLIC ");
                    printDoctypeURL(this._docTypePublicId);
                    if (this._indenting) {
                        this._printer.breakLine();
                        for (int i2 = 0; i2 < 18 + name.length(); i2++) {
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
                return;
            }
            if (dtd != null && dtd.length() > 0) {
                this._printer.printText("<!DOCTYPE ");
                this._printer.printText(name);
                this._printer.printText(" [");
                printText(dtd, true, true);
                this._printer.printText("]>");
                this._printer.breakLine();
            }
        }
    }

    protected ElementState content() throws IOException {
        ElementState state = getElementState();
        if (!isDocumentState()) {
            if (state.inCData && !state.doCData) {
                this._printer.printText("]]>");
                state.inCData = false;
            }
            if (state.empty) {
                this._printer.printText('>');
                state.empty = false;
            }
            state.afterElement = false;
            state.afterComment = false;
        }
        return state;
    }

    protected void characters(String text) throws MissingResourceException, IOException {
        ElementState state = content();
        if (state.inCData || state.doCData) {
            if (!state.inCData) {
                this._printer.printText("<![CDATA[");
                state.inCData = true;
            }
            int saveIndent = this._printer.getNextIndent();
            this._printer.setNextIndent(0);
            printCDATAText(text);
            this._printer.setNextIndent(saveIndent);
            return;
        }
        if (state.preserveSpace) {
            int saveIndent2 = this._printer.getNextIndent();
            this._printer.setNextIndent(0);
            printText(text, true, state.unescaped);
            this._printer.setNextIndent(saveIndent2);
            return;
        }
        printText(text, false, state.unescaped);
    }

    protected void serializePreRoot() throws IOException {
        if (this._preRoot != null) {
            for (int i2 = 0; i2 < this._preRoot.size(); i2++) {
                printText((String) this._preRoot.elementAt(i2), true, true);
                if (this._indenting) {
                    this._printer.breakLine();
                }
            }
            this._preRoot.removeAllElements();
        }
    }

    protected void printCDATAText(String text) throws MissingResourceException, IOException {
        int length = text.length();
        int index = 0;
        while (index < length) {
            char ch = text.charAt(index);
            if (ch == ']' && index + 2 < length && text.charAt(index + 1) == ']' && text.charAt(index + 2) == '>') {
                if (this.fDOMErrorHandler != null) {
                    if ((this.features & 16) == 0) {
                        String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.SERIALIZER_DOMAIN, "EndingCDATA", null);
                        if ((this.features & 2) != 0) {
                            modifyDOMError(msg, (short) 3, "wf-invalid-character", this.fCurrentNode);
                            this.fDOMErrorHandler.handleError(this.fDOMError);
                            throw new LSException((short) 82, msg);
                        }
                        modifyDOMError(msg, (short) 2, "cdata-section-not-splitted", this.fCurrentNode);
                        if (!this.fDOMErrorHandler.handleError(this.fDOMError)) {
                            throw new LSException((short) 82, msg);
                        }
                    } else {
                        modifyDOMError(DOMMessageFormatter.formatMessage(DOMMessageFormatter.SERIALIZER_DOMAIN, "SplittingCDATA", null), (short) 1, null, this.fCurrentNode);
                        this.fDOMErrorHandler.handleError(this.fDOMError);
                    }
                }
                this._printer.printText(SerializerConstants.CDATA_CONTINUE);
                index += 2;
            } else if (!XMLChar.isValid(ch)) {
                index++;
                if (index < length) {
                    surrogates(ch, text.charAt(index), true);
                } else {
                    fatalError("The character '" + ch + "' is an invalid XML character");
                }
            } else if ((ch >= ' ' && this._encodingInfo.isPrintable(ch) && ch != 127) || ch == '\n' || ch == '\r' || ch == '\t') {
                this._printer.printText(ch);
            } else {
                this._printer.printText("]]>&#x");
                this._printer.printText(Integer.toHexString(ch));
                this._printer.printText(";<![CDATA[");
            }
            index++;
        }
    }

    protected void surrogates(int high, int low, boolean inContent) throws IOException {
        if (XMLChar.isHighSurrogate(high)) {
            if (!XMLChar.isLowSurrogate(low)) {
                fatalError("The character '" + ((char) low) + "' is an invalid XML character");
                return;
            }
            int supplemental = XMLChar.supplemental((char) high, (char) low);
            if (!XMLChar.isValid(supplemental)) {
                fatalError("The character '" + ((char) supplemental) + "' is an invalid XML character");
                return;
            }
            if (inContent && content().inCData) {
                this._printer.printText("]]>&#x");
                this._printer.printText(Integer.toHexString(supplemental));
                this._printer.printText(";<![CDATA[");
                return;
            }
            printHex(supplemental);
            return;
        }
        fatalError("The character '" + ((char) high) + "' is an invalid XML character");
    }

    protected void printText(char[] chars, int start, int length, boolean preserveSpace, boolean unescaped) throws IOException {
        if (preserveSpace) {
            while (true) {
                int i2 = length;
                length--;
                if (i2 > 0) {
                    char ch = chars[start];
                    start++;
                    if (ch == '\n' || ch == '\r' || unescaped) {
                        this._printer.printText(ch);
                    } else {
                        printEscaped(ch);
                    }
                } else {
                    return;
                }
            }
        } else {
            while (true) {
                int i3 = length;
                length--;
                if (i3 > 0) {
                    char ch2 = chars[start];
                    start++;
                    if (ch2 == ' ' || ch2 == '\f' || ch2 == '\t' || ch2 == '\n' || ch2 == '\r') {
                        this._printer.printSpace();
                    } else if (unescaped) {
                        this._printer.printText(ch2);
                    } else {
                        printEscaped(ch2);
                    }
                } else {
                    return;
                }
            }
        }
    }

    protected void printText(String text, boolean preserveSpace, boolean unescaped) throws IOException {
        if (preserveSpace) {
            for (int index = 0; index < text.length(); index++) {
                char ch = text.charAt(index);
                if (ch == '\n' || ch == '\r' || unescaped) {
                    this._printer.printText(ch);
                } else {
                    printEscaped(ch);
                }
            }
            return;
        }
        for (int index2 = 0; index2 < text.length(); index2++) {
            char ch2 = text.charAt(index2);
            if (ch2 == ' ' || ch2 == '\f' || ch2 == '\t' || ch2 == '\n' || ch2 == '\r') {
                this._printer.printSpace();
            } else if (unescaped) {
                this._printer.printText(ch2);
            } else {
                printEscaped(ch2);
            }
        }
    }

    protected void printDoctypeURL(String url) throws IOException {
        this._printer.printText('\"');
        for (int i2 = 0; i2 < url.length(); i2++) {
            if (url.charAt(i2) == '\"' || url.charAt(i2) < ' ' || url.charAt(i2) > 127) {
                this._printer.printText('%');
                this._printer.printText(Integer.toHexString(url.charAt(i2)));
            } else {
                this._printer.printText(url.charAt(i2));
            }
        }
        this._printer.printText('\"');
    }

    protected void printEscaped(int ch) throws IOException {
        String charRef = getEntityRef(ch);
        if (charRef != null) {
            this._printer.printText('&');
            this._printer.printText(charRef);
            this._printer.printText(';');
        } else {
            if ((ch >= 32 && this._encodingInfo.isPrintable((char) ch) && ch != 127) || ch == 10 || ch == 13 || ch == 9) {
                if (ch < 65536) {
                    this._printer.printText((char) ch);
                    return;
                } else {
                    this._printer.printText((char) (((ch - 65536) >> 10) + 55296));
                    this._printer.printText((char) (((ch - 65536) & 1023) + 56320));
                    return;
                }
            }
            printHex(ch);
        }
    }

    final void printHex(int ch) throws IOException {
        this._printer.printText(XMLStreamWriterImpl.ENCODING_PREFIX);
        this._printer.printText(Integer.toHexString(ch));
        this._printer.printText(';');
    }

    protected void printEscaped(String source) throws IOException {
        int i2 = 0;
        while (i2 < source.length()) {
            int ch = source.charAt(i2);
            if ((ch & 64512) == 55296 && i2 + 1 < source.length()) {
                int lowch = source.charAt(i2 + 1);
                if ((lowch & 64512) == 56320) {
                    ch = ((65536 + ((ch - 55296) << 10)) + lowch) - 56320;
                    i2++;
                }
            }
            printEscaped(ch);
            i2++;
        }
    }

    protected ElementState getElementState() {
        return this._elementStates[this._elementStateCount];
    }

    protected ElementState enterElementState(String namespaceURI, String localName, String rawName, boolean preserveSpace) {
        if (this._elementStateCount + 1 == this._elementStates.length) {
            ElementState[] newStates = new ElementState[this._elementStates.length + 10];
            for (int i2 = 0; i2 < this._elementStates.length; i2++) {
                newStates[i2] = this._elementStates[i2];
            }
            for (int i3 = this._elementStates.length; i3 < newStates.length; i3++) {
                newStates[i3] = new ElementState();
            }
            this._elementStates = newStates;
        }
        this._elementStateCount++;
        ElementState state = this._elementStates[this._elementStateCount];
        state.namespaceURI = namespaceURI;
        state.localName = localName;
        state.rawName = rawName;
        state.preserveSpace = preserveSpace;
        state.empty = true;
        state.afterElement = false;
        state.afterComment = false;
        state.inCData = false;
        state.doCData = false;
        state.unescaped = false;
        state.prefixes = this._prefixes;
        this._prefixes = null;
        return state;
    }

    protected ElementState leaveElementState() throws MissingResourceException {
        if (this._elementStateCount > 0) {
            this._prefixes = null;
            this._elementStateCount--;
            return this._elementStates[this._elementStateCount];
        }
        String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.SERIALIZER_DOMAIN, "Internal", null);
        throw new IllegalStateException(msg);
    }

    protected boolean isDocumentState() {
        return this._elementStateCount == 0;
    }

    final void clearDocumentState() {
        this._elementStateCount = 0;
    }

    protected String getPrefix(String namespaceURI) {
        String prefix;
        String prefix2;
        if (this._prefixes != null && (prefix2 = this._prefixes.get(namespaceURI)) != null) {
            return prefix2;
        }
        if (this._elementStateCount == 0) {
            return null;
        }
        for (int i2 = this._elementStateCount; i2 > 0; i2--) {
            if (this._elementStates[i2].prefixes != null && (prefix = this._elementStates[i2].prefixes.get(namespaceURI)) != null) {
                return prefix;
            }
        }
        return null;
    }

    protected DOMError modifyDOMError(String message, short severity, String type, Node node) {
        this.fDOMError.reset();
        this.fDOMError.fMessage = message;
        this.fDOMError.fType = type;
        this.fDOMError.fSeverity = severity;
        this.fDOMError.fLocator = new DOMLocatorImpl(-1, -1, -1, node, null);
        return this.fDOMError;
    }

    protected void fatalError(String message) throws IOException {
        if (this.fDOMErrorHandler != null) {
            modifyDOMError(message, (short) 3, null, this.fCurrentNode);
            this.fDOMErrorHandler.handleError(this.fDOMError);
            return;
        }
        throw new IOException(message);
    }

    protected void checkUnboundNamespacePrefixedNode(Node node) throws IOException {
    }
}
