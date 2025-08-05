package com.sun.xml.internal.stream.writers;

import com.sun.org.apache.xerces.internal.impl.Constants;
import com.sun.org.apache.xerces.internal.impl.PropertyManager;
import com.sun.org.apache.xerces.internal.util.NamespaceSupport;
import com.sun.org.apache.xerces.internal.util.SymbolTable;
import com.sun.org.apache.xerces.internal.utils.SecuritySupport;
import com.sun.org.apache.xerces.internal.xni.QName;
import com.sun.org.apache.xml.internal.serializer.SerializerConstants;
import com.sun.xml.internal.stream.util.ReadOnlyIterator;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.Vector;
import javax.xml.namespace.NamespaceContext;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.stream.StreamResult;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:com/sun/xml/internal/stream/writers/XMLStreamWriterImpl.class */
public final class XMLStreamWriterImpl extends AbstractMap implements XMLStreamWriter {
    public static final String START_COMMENT = "<!--";
    public static final String END_COMMENT = "-->";
    public static final String DEFAULT_ENCODING = " encoding=\"utf-8\"";
    public static final String DEFAULT_XMLDECL = "<?xml version=\"1.0\" ?>";
    public static final String DEFAULT_XML_VERSION = "1.0";
    public static final char CLOSE_START_TAG = '>';
    public static final char OPEN_START_TAG = '<';
    public static final String OPEN_END_TAG = "</";
    public static final char CLOSE_END_TAG = '>';
    public static final String START_CDATA = "<![CDATA[";
    public static final String END_CDATA = "]]>";
    public static final String CLOSE_EMPTY_ELEMENT = "/>";
    public static final String SPACE = " ";
    public static final String UTF_8 = "UTF-8";
    public static final String OUTPUTSTREAM_PROPERTY = "sjsxp-outputstream";
    boolean fEscapeCharacters;
    private boolean fIsRepairingNamespace;
    private Writer fWriter;
    private OutputStream fOutputStream;
    private ArrayList fAttributeCache;
    private ArrayList fNamespaceDecls;
    private NamespaceContextImpl fNamespaceContext;
    private NamespaceSupport fInternalNamespaceContext;
    private Random fPrefixGen;
    private PropertyManager fPropertyManager;
    private boolean fStartTagOpened;
    private boolean fReuse;
    private SymbolTable fSymbolTable;
    private ElementStack fElementStack;
    private final String DEFAULT_PREFIX;
    private final ReadOnlyIterator fReadOnlyIterator;
    private CharsetEncoder fEncoder;
    HashMap fAttrNamespace;

    public XMLStreamWriterImpl(OutputStream outputStream, PropertyManager props) throws IOException {
        this(new OutputStreamWriter(outputStream), props);
    }

    public XMLStreamWriterImpl(OutputStream outputStream, String encoding, PropertyManager props) throws IOException {
        this(new StreamResult(outputStream), encoding, props);
    }

    public XMLStreamWriterImpl(Writer writer, PropertyManager props) throws IOException {
        this(new StreamResult(writer), (String) null, props);
    }

    public XMLStreamWriterImpl(StreamResult sr, String encoding, PropertyManager props) throws IOException {
        this.fEscapeCharacters = true;
        this.fIsRepairingNamespace = false;
        this.fOutputStream = null;
        this.fNamespaceContext = null;
        this.fInternalNamespaceContext = null;
        this.fPrefixGen = null;
        this.fPropertyManager = null;
        this.fStartTagOpened = false;
        this.fSymbolTable = new SymbolTable();
        this.fElementStack = new ElementStack();
        this.DEFAULT_PREFIX = this.fSymbolTable.addSymbol("");
        this.fReadOnlyIterator = new ReadOnlyIterator();
        this.fEncoder = null;
        this.fAttrNamespace = null;
        setOutput(sr, encoding);
        this.fPropertyManager = props;
        init();
    }

    private void init() {
        this.fReuse = false;
        this.fNamespaceDecls = new ArrayList();
        this.fPrefixGen = new Random();
        this.fAttributeCache = new ArrayList();
        this.fInternalNamespaceContext = new NamespaceSupport();
        this.fInternalNamespaceContext.reset();
        this.fNamespaceContext = new NamespaceContextImpl();
        this.fNamespaceContext.internalContext = this.fInternalNamespaceContext;
        Boolean ob = (Boolean) this.fPropertyManager.getProperty(XMLOutputFactory.IS_REPAIRING_NAMESPACES);
        this.fIsRepairingNamespace = ob.booleanValue();
        Boolean ob2 = (Boolean) this.fPropertyManager.getProperty(Constants.ESCAPE_CHARACTERS);
        setEscapeCharacters(ob2.booleanValue());
    }

    public void reset() {
        reset(false);
    }

    void reset(boolean resetProperties) {
        if (!this.fReuse) {
            throw new IllegalStateException("close() Must be called before calling reset()");
        }
        this.fReuse = false;
        this.fNamespaceDecls.clear();
        this.fAttributeCache.clear();
        this.fElementStack.clear();
        this.fInternalNamespaceContext.reset();
        this.fStartTagOpened = false;
        this.fNamespaceContext.userContext = null;
        if (resetProperties) {
            Boolean ob = (Boolean) this.fPropertyManager.getProperty(XMLOutputFactory.IS_REPAIRING_NAMESPACES);
            this.fIsRepairingNamespace = ob.booleanValue();
            Boolean ob2 = (Boolean) this.fPropertyManager.getProperty(Constants.ESCAPE_CHARACTERS);
            setEscapeCharacters(ob2.booleanValue());
        }
    }

    public void setOutput(StreamResult sr, String encoding) throws IOException {
        if (sr.getOutputStream() != null) {
            setOutputUsingStream(sr.getOutputStream(), encoding);
        } else if (sr.getWriter() != null) {
            setOutputUsingWriter(sr.getWriter());
        } else if (sr.getSystemId() != null) {
            setOutputUsingStream(new FileOutputStream(sr.getSystemId()), encoding);
        }
    }

    private void setOutputUsingWriter(Writer writer) throws IOException {
        String charset;
        this.fWriter = writer;
        if ((writer instanceof OutputStreamWriter) && (charset = ((OutputStreamWriter) writer).getEncoding()) != null && !charset.equalsIgnoreCase("utf-8")) {
            this.fEncoder = Charset.forName(charset).newEncoder();
        }
    }

    private void setOutputUsingStream(OutputStream os, String encoding) throws IOException {
        this.fOutputStream = os;
        if (encoding != null) {
            if (encoding.equalsIgnoreCase("utf-8")) {
                this.fWriter = new UTF8OutputStreamWriter(os);
                return;
            } else {
                this.fWriter = new XMLWriter(new OutputStreamWriter(os, encoding));
                this.fEncoder = Charset.forName(encoding).newEncoder();
                return;
            }
        }
        String encoding2 = SecuritySupport.getSystemProperty("file.encoding");
        if (encoding2 != null && encoding2.equalsIgnoreCase("utf-8")) {
            this.fWriter = new UTF8OutputStreamWriter(os);
        } else {
            this.fWriter = new XMLWriter(new OutputStreamWriter(os));
        }
    }

    public boolean canReuse() {
        return this.fReuse;
    }

    public void setEscapeCharacters(boolean escape) {
        this.fEscapeCharacters = escape;
    }

    public boolean getEscapeCharacters() {
        return this.fEscapeCharacters;
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void close() throws XMLStreamException {
        if (this.fWriter != null) {
            try {
                this.fWriter.flush();
            } catch (IOException e2) {
                throw new XMLStreamException(e2);
            }
        }
        this.fWriter = null;
        this.fOutputStream = null;
        this.fNamespaceDecls.clear();
        this.fAttributeCache.clear();
        this.fElementStack.clear();
        this.fInternalNamespaceContext.reset();
        this.fReuse = true;
        this.fStartTagOpened = false;
        this.fNamespaceContext.userContext = null;
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void flush() throws XMLStreamException {
        try {
            this.fWriter.flush();
        } catch (IOException e2) {
            throw new XMLStreamException(e2);
        }
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public NamespaceContext getNamespaceContext() {
        return this.fNamespaceContext;
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public String getPrefix(String uri) throws XMLStreamException {
        return this.fNamespaceContext.getPrefix(uri);
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public Object getProperty(String str) throws IllegalArgumentException {
        if (str == null) {
            throw new NullPointerException();
        }
        if (!this.fPropertyManager.containsProperty(str)) {
            throw new IllegalArgumentException("Property '" + str + "' is not supported");
        }
        return this.fPropertyManager.getProperty(str);
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void setDefaultNamespace(String uri) throws XMLStreamException {
        if (uri != null) {
            uri = this.fSymbolTable.addSymbol(uri);
        }
        if (this.fIsRepairingNamespace) {
            if (isDefaultNamespace(uri)) {
                return;
            }
            QName qname = new QName();
            qname.setValues(this.DEFAULT_PREFIX, "xmlns", null, uri);
            this.fNamespaceDecls.add(qname);
            return;
        }
        this.fInternalNamespaceContext.declarePrefix(this.DEFAULT_PREFIX, uri);
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void setNamespaceContext(NamespaceContext namespaceContext) throws XMLStreamException {
        this.fNamespaceContext.userContext = namespaceContext;
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void setPrefix(String prefix, String uri) throws XMLStreamException {
        if (prefix == null) {
            throw new XMLStreamException("Prefix cannot be null");
        }
        if (uri == null) {
            throw new XMLStreamException("URI cannot be null");
        }
        String prefix2 = this.fSymbolTable.addSymbol(prefix);
        String uri2 = this.fSymbolTable.addSymbol(uri);
        if (this.fIsRepairingNamespace) {
            String tmpURI = this.fInternalNamespaceContext.getURI(prefix2);
            if ((tmpURI != null && tmpURI == uri2) || checkUserNamespaceContext(prefix2, uri2)) {
                return;
            }
            QName qname = new QName();
            qname.setValues(prefix2, "xmlns", null, uri2);
            this.fNamespaceDecls.add(qname);
            return;
        }
        this.fInternalNamespaceContext.declarePrefix(prefix2, uri2);
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeAttribute(String localName, String value) throws XMLStreamException {
        try {
            if (!this.fStartTagOpened) {
                throw new XMLStreamException("Attribute not associated with any element");
            }
            if (this.fIsRepairingNamespace) {
                Attribute attr = new Attribute(value);
                attr.setValues(null, localName, null, null);
                this.fAttributeCache.add(attr);
            } else {
                this.fWriter.write(" ");
                this.fWriter.write(localName);
                this.fWriter.write("=\"");
                writeXMLContent(value, true, true);
                this.fWriter.write(PdfOps.DOUBLE_QUOTE__TOKEN);
            }
        } catch (IOException e2) {
            throw new XMLStreamException(e2);
        }
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeAttribute(String namespaceURI, String localName, String value) throws XMLStreamException {
        try {
            if (!this.fStartTagOpened) {
                throw new XMLStreamException("Attribute not associated with any element");
            }
            if (namespaceURI == null) {
                throw new XMLStreamException("NamespaceURI cannot be null");
            }
            String namespaceURI2 = this.fSymbolTable.addSymbol(namespaceURI);
            String prefix = this.fInternalNamespaceContext.getPrefix(namespaceURI2);
            if (!this.fIsRepairingNamespace) {
                if (prefix == null) {
                    throw new XMLStreamException("Prefix cannot be null");
                }
                writeAttributeWithPrefix(prefix, localName, value);
            } else {
                Attribute attr = new Attribute(value);
                attr.setValues(null, localName, null, namespaceURI2);
                this.fAttributeCache.add(attr);
            }
        } catch (IOException e2) {
            throw new XMLStreamException(e2);
        }
    }

    private void writeAttributeWithPrefix(String prefix, String localName, String value) throws IOException {
        this.fWriter.write(" ");
        if (prefix != null && prefix != "") {
            this.fWriter.write(prefix);
            this.fWriter.write(CallSiteDescriptor.TOKEN_DELIMITER);
        }
        this.fWriter.write(localName);
        this.fWriter.write("=\"");
        writeXMLContent(value, true, true);
        this.fWriter.write(PdfOps.DOUBLE_QUOTE__TOKEN);
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeAttribute(String prefix, String namespaceURI, String localName, String value) throws XMLStreamException {
        String tmpURI;
        try {
            if (!this.fStartTagOpened) {
                throw new XMLStreamException("Attribute not associated with any element");
            }
            if (namespaceURI == null) {
                throw new XMLStreamException("NamespaceURI cannot be null");
            }
            if (localName == null) {
                throw new XMLStreamException("Local name cannot be null");
            }
            if (!this.fIsRepairingNamespace) {
                if (prefix == null || prefix.equals("")) {
                    if (!namespaceURI.equals("")) {
                        throw new XMLStreamException("prefix cannot be null or empty");
                    }
                    writeAttributeWithPrefix(null, localName, value);
                    return;
                }
                if (!prefix.equals("xml") || !namespaceURI.equals("http://www.w3.org/XML/1998/namespace")) {
                    prefix = this.fSymbolTable.addSymbol(prefix);
                    String namespaceURI2 = this.fSymbolTable.addSymbol(namespaceURI);
                    if (this.fInternalNamespaceContext.containsPrefixInCurrentContext(prefix) && (tmpURI = this.fInternalNamespaceContext.getURI(prefix)) != null && tmpURI != namespaceURI2) {
                        throw new XMLStreamException("Prefix " + prefix + " is already bound to " + tmpURI + ". Trying to rebind it to " + namespaceURI2 + " is an error.");
                    }
                    this.fInternalNamespaceContext.declarePrefix(prefix, namespaceURI2);
                }
                writeAttributeWithPrefix(prefix, localName, value);
            } else {
                if (prefix != null) {
                    prefix = this.fSymbolTable.addSymbol(prefix);
                }
                String namespaceURI3 = this.fSymbolTable.addSymbol(namespaceURI);
                Attribute attr = new Attribute(value);
                attr.setValues(prefix, localName, null, namespaceURI3);
                this.fAttributeCache.add(attr);
            }
        } catch (IOException e2) {
            throw new XMLStreamException(e2);
        }
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeCData(String cdata) throws XMLStreamException {
        try {
            if (cdata == null) {
                throw new XMLStreamException("cdata cannot be null");
            }
            if (this.fStartTagOpened) {
                closeStartTag();
            }
            this.fWriter.write("<![CDATA[");
            this.fWriter.write(cdata);
            this.fWriter.write("]]>");
        } catch (IOException e2) {
            throw new XMLStreamException(e2);
        }
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeCharacters(String data) throws XMLStreamException {
        try {
            if (this.fStartTagOpened) {
                closeStartTag();
            }
            writeXMLContent(data);
        } catch (IOException e2) {
            throw new XMLStreamException(e2);
        }
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeCharacters(char[] data, int start, int len) throws XMLStreamException {
        try {
            if (this.fStartTagOpened) {
                closeStartTag();
            }
            writeXMLContent(data, start, len, this.fEscapeCharacters);
        } catch (IOException e2) {
            throw new XMLStreamException(e2);
        }
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeComment(String comment) throws XMLStreamException {
        try {
            if (this.fStartTagOpened) {
                closeStartTag();
            }
            this.fWriter.write("<!--");
            if (comment != null) {
                this.fWriter.write(comment);
            }
            this.fWriter.write("-->");
        } catch (IOException e2) {
            throw new XMLStreamException(e2);
        }
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeDTD(String dtd) throws XMLStreamException {
        try {
            if (this.fStartTagOpened) {
                closeStartTag();
            }
            this.fWriter.write(dtd);
        } catch (IOException e2) {
            throw new XMLStreamException(e2);
        }
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeDefaultNamespace(String namespaceURI) throws XMLStreamException {
        String namespaceURINormalized;
        String tmp;
        if (namespaceURI == null) {
            namespaceURINormalized = "";
        } else {
            namespaceURINormalized = namespaceURI;
        }
        try {
            if (!this.fStartTagOpened) {
                throw new IllegalStateException("Namespace Attribute not associated with any element");
            }
            if (this.fIsRepairingNamespace) {
                QName qname = new QName();
                qname.setValues("", "xmlns", null, namespaceURINormalized);
                this.fNamespaceDecls.add(qname);
                return;
            }
            String namespaceURINormalized2 = this.fSymbolTable.addSymbol(namespaceURINormalized);
            if (this.fInternalNamespaceContext.containsPrefixInCurrentContext("") && (tmp = this.fInternalNamespaceContext.getURI("")) != null && tmp != namespaceURINormalized2) {
                throw new XMLStreamException("xmlns has been already bound to " + tmp + ". Rebinding it to " + namespaceURINormalized2 + " is an error");
            }
            this.fInternalNamespaceContext.declarePrefix("", namespaceURINormalized2);
            writenamespace(null, namespaceURINormalized2);
        } catch (IOException e2) {
            throw new XMLStreamException(e2);
        }
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeEmptyElement(String localName) throws XMLStreamException {
        try {
            if (this.fStartTagOpened) {
                closeStartTag();
            }
            openStartTag();
            this.fElementStack.push(null, localName, null, null, true);
            this.fInternalNamespaceContext.pushContext();
            if (!this.fIsRepairingNamespace) {
                this.fWriter.write(localName);
            }
        } catch (IOException e2) {
            throw new XMLStreamException(e2);
        }
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeEmptyElement(String namespaceURI, String localName) throws XMLStreamException {
        if (namespaceURI == null) {
            throw new XMLStreamException("NamespaceURI cannot be null");
        }
        String namespaceURI2 = this.fSymbolTable.addSymbol(namespaceURI);
        String prefix = this.fNamespaceContext.getPrefix(namespaceURI2);
        writeEmptyElement(prefix, localName, namespaceURI2);
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeEmptyElement(String prefix, String localName, String namespaceURI) throws XMLStreamException {
        try {
            if (localName == null) {
                throw new XMLStreamException("Local Name cannot be null");
            }
            if (namespaceURI == null) {
                throw new XMLStreamException("NamespaceURI cannot be null");
            }
            if (prefix != null) {
                prefix = this.fSymbolTable.addSymbol(prefix);
            }
            String namespaceURI2 = this.fSymbolTable.addSymbol(namespaceURI);
            if (this.fStartTagOpened) {
                closeStartTag();
            }
            openStartTag();
            this.fElementStack.push(prefix, localName, null, namespaceURI2, true);
            this.fInternalNamespaceContext.pushContext();
            if (!this.fIsRepairingNamespace) {
                if (prefix == null) {
                    throw new XMLStreamException("NamespaceURI " + namespaceURI2 + " has not been bound to any prefix");
                }
                if (prefix != null && prefix != "") {
                    this.fWriter.write(prefix);
                    this.fWriter.write(CallSiteDescriptor.TOKEN_DELIMITER);
                }
                this.fWriter.write(localName);
            }
        } catch (IOException e2) {
            throw new XMLStreamException(e2);
        }
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeEndDocument() throws XMLStreamException {
        try {
            if (this.fStartTagOpened) {
                closeStartTag();
            }
            while (!this.fElementStack.empty()) {
                ElementState elem = this.fElementStack.pop();
                this.fInternalNamespaceContext.popContext();
                if (!elem.isEmpty) {
                    this.fWriter.write("</");
                    if (elem.prefix != null && !elem.prefix.equals("")) {
                        this.fWriter.write(elem.prefix);
                        this.fWriter.write(CallSiteDescriptor.TOKEN_DELIMITER);
                    }
                    this.fWriter.write(elem.localpart);
                    this.fWriter.write(62);
                }
            }
        } catch (IOException e2) {
            throw new XMLStreamException(e2);
        } catch (ArrayIndexOutOfBoundsException e3) {
            throw new XMLStreamException("No more elements to write");
        }
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeEndElement() throws XMLStreamException {
        try {
            if (this.fStartTagOpened) {
                closeStartTag();
            }
            ElementState currentElement = this.fElementStack.pop();
            if (currentElement == null) {
                throw new XMLStreamException("No element was found to write");
            }
            if (currentElement.isEmpty) {
                return;
            }
            this.fWriter.write("</");
            if (currentElement.prefix != null && !currentElement.prefix.equals("")) {
                this.fWriter.write(currentElement.prefix);
                this.fWriter.write(CallSiteDescriptor.TOKEN_DELIMITER);
            }
            this.fWriter.write(currentElement.localpart);
            this.fWriter.write(62);
            this.fInternalNamespaceContext.popContext();
        } catch (IOException e2) {
            throw new XMLStreamException(e2);
        } catch (ArrayIndexOutOfBoundsException e3) {
            throw new XMLStreamException("No element was found to write: " + e3.toString(), e3);
        }
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeEntityRef(String refName) throws XMLStreamException {
        try {
            if (this.fStartTagOpened) {
                closeStartTag();
            }
            this.fWriter.write(38);
            this.fWriter.write(refName);
            this.fWriter.write(59);
        } catch (IOException e2) {
            throw new XMLStreamException(e2);
        }
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeNamespace(String prefix, String namespaceURI) throws XMLStreamException {
        String namespaceURINormalized;
        String tmp;
        if (namespaceURI == null) {
            namespaceURINormalized = "";
        } else {
            namespaceURINormalized = namespaceURI;
        }
        try {
            if (!this.fStartTagOpened) {
                throw new IllegalStateException("Invalid state: start tag is not opened at writeNamespace(" + prefix + ", " + namespaceURINormalized + ")");
            }
            if (prefix == null || prefix.equals("") || prefix.equals("xmlns")) {
                writeDefaultNamespace(namespaceURINormalized);
                return;
            }
            if (prefix.equals("xml") && namespaceURINormalized.equals("http://www.w3.org/XML/1998/namespace")) {
                return;
            }
            String prefix2 = this.fSymbolTable.addSymbol(prefix);
            String namespaceURINormalized2 = this.fSymbolTable.addSymbol(namespaceURINormalized);
            if (this.fIsRepairingNamespace) {
                String tmpURI = this.fInternalNamespaceContext.getURI(prefix2);
                if (tmpURI != null && tmpURI == namespaceURINormalized2) {
                    return;
                }
                QName qname = new QName();
                qname.setValues(prefix2, "xmlns", null, namespaceURINormalized2);
                this.fNamespaceDecls.add(qname);
                return;
            }
            if (this.fInternalNamespaceContext.containsPrefixInCurrentContext(prefix2) && (tmp = this.fInternalNamespaceContext.getURI(prefix2)) != null && tmp != namespaceURINormalized2) {
                throw new XMLStreamException("prefix " + prefix2 + " has been already bound to " + tmp + ". Rebinding it to " + namespaceURINormalized2 + " is an error");
            }
            this.fInternalNamespaceContext.declarePrefix(prefix2, namespaceURINormalized2);
            writenamespace(prefix2, namespaceURINormalized2);
        } catch (IOException e2) {
            throw new XMLStreamException(e2);
        }
    }

    private void writenamespace(String prefix, String namespaceURI) throws IOException {
        this.fWriter.write(" xmlns");
        if (prefix != null && prefix != "") {
            this.fWriter.write(CallSiteDescriptor.TOKEN_DELIMITER);
            this.fWriter.write(prefix);
        }
        this.fWriter.write("=\"");
        writeXMLContent(namespaceURI, true, true);
        this.fWriter.write(PdfOps.DOUBLE_QUOTE__TOKEN);
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeProcessingInstruction(String target) throws XMLStreamException {
        try {
            if (this.fStartTagOpened) {
                closeStartTag();
            }
            if (target != null) {
                this.fWriter.write("<?");
                this.fWriter.write(target);
                this.fWriter.write("?>");
                return;
            }
            throw new XMLStreamException("PI target cannot be null");
        } catch (IOException e2) {
            throw new XMLStreamException(e2);
        }
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeProcessingInstruction(String target, String data) throws XMLStreamException {
        try {
            if (this.fStartTagOpened) {
                closeStartTag();
            }
            if (target == null || data == null) {
                throw new XMLStreamException("PI target cannot be null");
            }
            this.fWriter.write("<?");
            this.fWriter.write(target);
            this.fWriter.write(" ");
            this.fWriter.write(data);
            this.fWriter.write("?>");
        } catch (IOException e2) {
            throw new XMLStreamException(e2);
        }
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeStartDocument() throws XMLStreamException {
        try {
            this.fWriter.write("<?xml version=\"1.0\" ?>");
        } catch (IOException ex) {
            throw new XMLStreamException(ex);
        }
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeStartDocument(String version) throws XMLStreamException {
        if (version != null) {
            try {
                if (!version.equals("")) {
                    this.fWriter.write("<?xml version=\"");
                    this.fWriter.write(version);
                    this.fWriter.write(PdfOps.DOUBLE_QUOTE__TOKEN);
                    this.fWriter.write("?>");
                    return;
                }
            } catch (IOException ex) {
                throw new XMLStreamException(ex);
            }
        }
        writeStartDocument();
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeStartDocument(String encoding, String version) throws XMLStreamException {
        try {
            if (encoding == null && version == null) {
                writeStartDocument();
                return;
            }
            if (encoding == null) {
                writeStartDocument(version);
                return;
            }
            String streamEncoding = null;
            if (this.fWriter instanceof OutputStreamWriter) {
                streamEncoding = ((OutputStreamWriter) this.fWriter).getEncoding();
            } else if (this.fWriter instanceof UTF8OutputStreamWriter) {
                streamEncoding = ((UTF8OutputStreamWriter) this.fWriter).getEncoding();
            } else if (this.fWriter instanceof XMLWriter) {
                streamEncoding = ((OutputStreamWriter) ((XMLWriter) this.fWriter).getWriter()).getEncoding();
            }
            if (streamEncoding != null && !streamEncoding.equalsIgnoreCase(encoding)) {
                boolean foundAlias = false;
                Set aliases = Charset.forName(encoding).aliases();
                Iterator it = aliases.iterator();
                while (!foundAlias && it.hasNext()) {
                    if (streamEncoding.equalsIgnoreCase(it.next())) {
                        foundAlias = true;
                    }
                }
                if (!foundAlias) {
                    throw new XMLStreamException("Underlying stream encoding '" + streamEncoding + "' and input paramter for writeStartDocument() method '" + encoding + "' do not match.");
                }
            }
            this.fWriter.write("<?xml version=\"");
            if (version == null || version.equals("")) {
                this.fWriter.write("1.0");
            } else {
                this.fWriter.write(version);
            }
            if (!encoding.equals("")) {
                this.fWriter.write("\" encoding=\"");
                this.fWriter.write(encoding);
            }
            this.fWriter.write("\"?>");
        } catch (IOException ex) {
            throw new XMLStreamException(ex);
        }
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeStartElement(String localName) throws XMLStreamException {
        try {
            if (localName == null) {
                throw new XMLStreamException("Local Name cannot be null");
            }
            if (this.fStartTagOpened) {
                closeStartTag();
            }
            openStartTag();
            this.fElementStack.push(null, localName, null, null, false);
            this.fInternalNamespaceContext.pushContext();
            if (this.fIsRepairingNamespace) {
                return;
            }
            this.fWriter.write(localName);
        } catch (IOException ex) {
            throw new XMLStreamException(ex);
        }
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeStartElement(String namespaceURI, String localName) throws XMLStreamException {
        if (localName == null) {
            throw new XMLStreamException("Local Name cannot be null");
        }
        if (namespaceURI == null) {
            throw new XMLStreamException("NamespaceURI cannot be null");
        }
        String namespaceURI2 = this.fSymbolTable.addSymbol(namespaceURI);
        String prefix = null;
        if (!this.fIsRepairingNamespace) {
            prefix = this.fNamespaceContext.getPrefix(namespaceURI2);
            if (prefix != null) {
                prefix = this.fSymbolTable.addSymbol(prefix);
            }
        }
        writeStartElement(prefix, localName, namespaceURI2);
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeStartElement(String prefix, String localName, String namespaceURI) throws XMLStreamException {
        try {
            if (localName == null) {
                throw new XMLStreamException("Local Name cannot be null");
            }
            if (namespaceURI == null) {
                throw new XMLStreamException("NamespaceURI cannot be null");
            }
            if (!this.fIsRepairingNamespace && prefix == null) {
                throw new XMLStreamException("Prefix cannot be null");
            }
            if (this.fStartTagOpened) {
                closeStartTag();
            }
            openStartTag();
            String namespaceURI2 = this.fSymbolTable.addSymbol(namespaceURI);
            if (prefix != null) {
                prefix = this.fSymbolTable.addSymbol(prefix);
            }
            this.fElementStack.push(prefix, localName, null, namespaceURI2, false);
            this.fInternalNamespaceContext.pushContext();
            String tmpPrefix = this.fNamespaceContext.getPrefix(namespaceURI2);
            if (prefix != null && (tmpPrefix == null || !prefix.equals(tmpPrefix))) {
                this.fInternalNamespaceContext.declarePrefix(prefix, namespaceURI2);
            }
            if (this.fIsRepairingNamespace) {
                if (prefix != null) {
                    if (tmpPrefix != null && prefix.equals(tmpPrefix)) {
                        return;
                    }
                    QName qname = new QName();
                    qname.setValues(prefix, "xmlns", null, namespaceURI2);
                    this.fNamespaceDecls.add(qname);
                    return;
                }
                return;
            }
            if (prefix != null && prefix != "") {
                this.fWriter.write(prefix);
                this.fWriter.write(CallSiteDescriptor.TOKEN_DELIMITER);
            }
            this.fWriter.write(localName);
        } catch (IOException ex) {
            throw new XMLStreamException(ex);
        }
    }

    private void writeCharRef(int codePoint) throws IOException {
        this.fWriter.write(jdk.internal.util.xml.impl.XMLStreamWriterImpl.ENCODING_PREFIX);
        this.fWriter.write(Integer.toHexString(codePoint));
        this.fWriter.write(59);
    }

    private void writeXMLContent(char[] content, int start, int length, boolean escapeChars) throws IOException {
        if (!escapeChars) {
            this.fWriter.write(content, start, length);
            return;
        }
        int startWritePos = start;
        int end = start + length;
        int index = start;
        while (index < end) {
            char ch = content[index];
            if (this.fEncoder != null && !this.fEncoder.canEncode(ch)) {
                this.fWriter.write(content, startWritePos, index - startWritePos);
                if (index != end - 1 && Character.isSurrogatePair(ch, content[index + 1])) {
                    writeCharRef(Character.toCodePoint(ch, content[index + 1]));
                    index++;
                } else {
                    writeCharRef(ch);
                }
                startWritePos = index + 1;
            } else {
                switch (ch) {
                    case '&':
                        this.fWriter.write(content, startWritePos, index - startWritePos);
                        this.fWriter.write(SerializerConstants.ENTITY_AMP);
                        startWritePos = index + 1;
                        break;
                    case '<':
                        this.fWriter.write(content, startWritePos, index - startWritePos);
                        this.fWriter.write(SerializerConstants.ENTITY_LT);
                        startWritePos = index + 1;
                        break;
                    case '>':
                        this.fWriter.write(content, startWritePos, index - startWritePos);
                        this.fWriter.write(SerializerConstants.ENTITY_GT);
                        startWritePos = index + 1;
                        break;
                }
            }
            index++;
        }
        this.fWriter.write(content, startWritePos, end - startWritePos);
    }

    private void writeXMLContent(String content) throws IOException {
        if (content != null && content.length() > 0) {
            writeXMLContent(content, this.fEscapeCharacters, false);
        }
    }

    private void writeXMLContent(String content, boolean escapeChars, boolean escapeDoubleQuotes) throws IOException {
        if (!escapeChars) {
            this.fWriter.write(content);
            return;
        }
        int startWritePos = 0;
        int end = content.length();
        int index = 0;
        while (index < end) {
            char ch = content.charAt(index);
            if (this.fEncoder != null && !this.fEncoder.canEncode(ch)) {
                this.fWriter.write(content, startWritePos, index - startWritePos);
                if (index != end - 1 && Character.isSurrogatePair(ch, content.charAt(index + 1))) {
                    writeCharRef(Character.toCodePoint(ch, content.charAt(index + 1)));
                    index++;
                } else {
                    writeCharRef(ch);
                }
                startWritePos = index + 1;
            } else {
                switch (ch) {
                    case '\"':
                        this.fWriter.write(content, startWritePos, index - startWritePos);
                        if (escapeDoubleQuotes) {
                            this.fWriter.write(SerializerConstants.ENTITY_QUOT);
                        } else {
                            this.fWriter.write(34);
                        }
                        startWritePos = index + 1;
                        break;
                    case '&':
                        this.fWriter.write(content, startWritePos, index - startWritePos);
                        this.fWriter.write(SerializerConstants.ENTITY_AMP);
                        startWritePos = index + 1;
                        break;
                    case '<':
                        this.fWriter.write(content, startWritePos, index - startWritePos);
                        this.fWriter.write(SerializerConstants.ENTITY_LT);
                        startWritePos = index + 1;
                        break;
                    case '>':
                        this.fWriter.write(content, startWritePos, index - startWritePos);
                        this.fWriter.write(SerializerConstants.ENTITY_GT);
                        startWritePos = index + 1;
                        break;
                }
            }
            index++;
        }
        this.fWriter.write(content, startWritePos, end - startWritePos);
    }

    private void closeStartTag() throws XMLStreamException {
        String tmp;
        try {
            ElementState currentElement = this.fElementStack.peek();
            if (this.fIsRepairingNamespace) {
                repair();
                correctPrefix(currentElement, 1);
                if (currentElement.prefix != null && currentElement.prefix != "") {
                    this.fWriter.write(currentElement.prefix);
                    this.fWriter.write(CallSiteDescriptor.TOKEN_DELIMITER);
                }
                this.fWriter.write(currentElement.localpart);
                int len = this.fNamespaceDecls.size();
                for (int i2 = 0; i2 < len; i2++) {
                    QName qname = (QName) this.fNamespaceDecls.get(i2);
                    if (qname != null && this.fInternalNamespaceContext.declarePrefix(qname.prefix, qname.uri)) {
                        writenamespace(qname.prefix, qname.uri);
                    }
                }
                this.fNamespaceDecls.clear();
                for (int j2 = 0; j2 < this.fAttributeCache.size(); j2++) {
                    Attribute attr = (Attribute) this.fAttributeCache.get(j2);
                    if (attr.prefix != null && attr.uri != null && !attr.prefix.equals("") && !attr.uri.equals("") && ((tmp = this.fInternalNamespaceContext.getPrefix(attr.uri)) == null || tmp != attr.prefix)) {
                        if (getAttrPrefix(attr.uri) == null) {
                            if (this.fInternalNamespaceContext.declarePrefix(attr.prefix, attr.uri)) {
                                writenamespace(attr.prefix, attr.uri);
                            }
                        } else {
                            writenamespace(attr.prefix, attr.uri);
                        }
                    }
                    writeAttributeWithPrefix(attr.prefix, attr.localpart, attr.value);
                }
                this.fAttrNamespace = null;
                this.fAttributeCache.clear();
            }
            if (currentElement.isEmpty) {
                this.fElementStack.pop();
                this.fInternalNamespaceContext.popContext();
                this.fWriter.write("/>");
            } else {
                this.fWriter.write(62);
            }
            this.fStartTagOpened = false;
        } catch (IOException ex) {
            this.fStartTagOpened = false;
            throw new XMLStreamException(ex);
        }
    }

    private void openStartTag() throws IOException {
        this.fStartTagOpened = true;
        this.fWriter.write(60);
    }

    private void correctPrefix(QName attr, int type) {
        String prefix = attr.prefix;
        String uri = attr.uri;
        boolean isSpecialCaseURI = false;
        if (prefix == null || prefix.equals("")) {
            if (uri == null) {
                return;
            }
            if (prefix == "" && uri == "") {
                return;
            }
            String uri2 = this.fSymbolTable.addSymbol(uri);
            for (int i2 = 0; i2 < this.fNamespaceDecls.size(); i2++) {
                QName decl = (QName) this.fNamespaceDecls.get(i2);
                if (decl != null && decl.uri == attr.uri) {
                    attr.prefix = decl.prefix;
                    return;
                }
            }
            String tmpPrefix = this.fNamespaceContext.getPrefix(uri2);
            if (tmpPrefix == "") {
                if (type == 1) {
                    return;
                }
                if (type == 10) {
                    tmpPrefix = getAttrPrefix(uri2);
                    isSpecialCaseURI = true;
                }
            }
            if (tmpPrefix == null) {
                StringBuffer genPrefix = new StringBuffer("zdef");
                for (int i3 = 0; i3 < 1; i3++) {
                    genPrefix.append(this.fPrefixGen.nextInt());
                }
                prefix = this.fSymbolTable.addSymbol(genPrefix.toString());
            } else {
                prefix = this.fSymbolTable.addSymbol(tmpPrefix);
            }
            if (tmpPrefix == null) {
                if (isSpecialCaseURI) {
                    addAttrNamespace(prefix, uri2);
                } else {
                    QName qname = new QName();
                    qname.setValues(prefix, "xmlns", null, uri2);
                    this.fNamespaceDecls.add(qname);
                    this.fInternalNamespaceContext.declarePrefix(this.fSymbolTable.addSymbol(prefix), uri2);
                }
            }
        }
        attr.prefix = prefix;
    }

    private String getAttrPrefix(String uri) {
        if (this.fAttrNamespace != null) {
            return (String) this.fAttrNamespace.get(uri);
        }
        return null;
    }

    private void addAttrNamespace(String prefix, String uri) {
        if (this.fAttrNamespace == null) {
            this.fAttrNamespace = new HashMap();
        }
        this.fAttrNamespace.put(prefix, uri);
    }

    private boolean isDefaultNamespace(String uri) {
        String defaultNamespace = this.fInternalNamespaceContext.getURI(this.DEFAULT_PREFIX);
        if (uri == defaultNamespace) {
            return true;
        }
        return false;
    }

    private boolean checkUserNamespaceContext(String prefix, String uri) {
        String tmpURI;
        if (this.fNamespaceContext.userContext != null && (tmpURI = this.fNamespaceContext.userContext.getNamespaceURI(prefix)) != null && tmpURI.equals(uri)) {
            return true;
        }
        return false;
    }

    protected void repair() {
        ElementState currentElement = this.fElementStack.peek();
        removeDuplicateDecls();
        for (int i2 = 0; i2 < this.fAttributeCache.size(); i2++) {
            Attribute attr = (Attribute) this.fAttributeCache.get(i2);
            if ((attr.prefix != null && !attr.prefix.equals("")) || (attr.uri != null && !attr.uri.equals(""))) {
                correctPrefix(currentElement, attr);
            }
        }
        if (!isDeclared(currentElement) && currentElement.prefix != null && currentElement.uri != null && !currentElement.prefix.equals("") && !currentElement.uri.equals("")) {
            this.fNamespaceDecls.add(currentElement);
        }
        for (int i3 = 0; i3 < this.fAttributeCache.size(); i3++) {
            Attribute attr2 = (Attribute) this.fAttributeCache.get(i3);
            for (int j2 = i3 + 1; j2 < this.fAttributeCache.size(); j2++) {
                Attribute attr22 = (Attribute) this.fAttributeCache.get(j2);
                if (!"".equals(attr2.prefix) && !"".equals(attr22.prefix)) {
                    correctPrefix(attr2, attr22);
                }
            }
        }
        repairNamespaceDecl(currentElement);
        for (int i4 = 0; i4 < this.fAttributeCache.size(); i4++) {
            Attribute attr3 = (Attribute) this.fAttributeCache.get(i4);
            if (attr3.prefix != null && attr3.prefix.equals("") && attr3.uri != null && attr3.uri.equals("")) {
                repairNamespaceDecl(attr3);
            }
        }
        for (int i5 = 0; i5 < this.fNamespaceDecls.size(); i5++) {
            QName qname = (QName) this.fNamespaceDecls.get(i5);
            if (qname != null) {
                this.fInternalNamespaceContext.declarePrefix(qname.prefix, qname.uri);
            }
        }
        for (int i6 = 0; i6 < this.fAttributeCache.size(); i6++) {
            correctPrefix((Attribute) this.fAttributeCache.get(i6), 10);
        }
    }

    void correctPrefix(QName attr1, QName attr2) {
        checkForNull(attr1);
        checkForNull(attr2);
        if (attr1.prefix.equals(attr2.prefix) && !attr1.uri.equals(attr2.uri)) {
            String tmpPrefix = this.fNamespaceContext.getPrefix(attr2.uri);
            if (tmpPrefix != null) {
                attr2.prefix = this.fSymbolTable.addSymbol(tmpPrefix);
                return;
            }
            for (int n2 = 0; n2 < this.fNamespaceDecls.size(); n2++) {
                QName decl = (QName) this.fNamespaceDecls.get(n2);
                if (decl != null && decl.uri == attr2.uri) {
                    attr2.prefix = decl.prefix;
                    return;
                }
            }
            StringBuffer genPrefix = new StringBuffer("zdef");
            for (int k2 = 0; k2 < 1; k2++) {
                genPrefix.append(this.fPrefixGen.nextInt());
            }
            String tmpPrefix2 = this.fSymbolTable.addSymbol(genPrefix.toString());
            attr2.prefix = tmpPrefix2;
            QName qname = new QName();
            qname.setValues(tmpPrefix2, "xmlns", null, attr2.uri);
            this.fNamespaceDecls.add(qname);
        }
    }

    void checkForNull(QName attr) {
        if (attr.prefix == null) {
            attr.prefix = "";
        }
        if (attr.uri == null) {
            attr.uri = "";
        }
    }

    void removeDuplicateDecls() {
        for (int i2 = 0; i2 < this.fNamespaceDecls.size(); i2++) {
            QName decl1 = (QName) this.fNamespaceDecls.get(i2);
            if (decl1 != null) {
                for (int j2 = i2 + 1; j2 < this.fNamespaceDecls.size(); j2++) {
                    QName decl2 = (QName) this.fNamespaceDecls.get(j2);
                    if (decl2 != null && decl1.prefix.equals(decl2.prefix) && decl1.uri.equals(decl2.uri)) {
                        this.fNamespaceDecls.remove(j2);
                    }
                }
            }
        }
    }

    void repairNamespaceDecl(QName attr) {
        String tmpURI;
        for (int j2 = 0; j2 < this.fNamespaceDecls.size(); j2++) {
            QName decl = (QName) this.fNamespaceDecls.get(j2);
            if (decl != null && attr.prefix != null && attr.prefix.equals(decl.prefix) && !attr.uri.equals(decl.uri) && (tmpURI = this.fNamespaceContext.getNamespaceURI(attr.prefix)) != null) {
                if (tmpURI.equals(attr.uri)) {
                    this.fNamespaceDecls.set(j2, null);
                } else {
                    decl.uri = attr.uri;
                }
            }
        }
    }

    boolean isDeclared(QName attr) {
        for (int n2 = 0; n2 < this.fNamespaceDecls.size(); n2++) {
            QName decl = (QName) this.fNamespaceDecls.get(n2);
            if (attr.prefix != null && attr.prefix == decl.prefix && decl.uri == attr.uri) {
                return true;
            }
        }
        if (attr.uri != null && this.fNamespaceContext.getPrefix(attr.uri) != null) {
            return true;
        }
        return false;
    }

    /* loaded from: rt.jar:com/sun/xml/internal/stream/writers/XMLStreamWriterImpl$ElementStack.class */
    protected class ElementStack {
        protected ElementState[] fElements = new ElementState[10];
        protected short fDepth;

        public ElementStack() {
            for (int i2 = 0; i2 < this.fElements.length; i2++) {
                this.fElements[i2] = XMLStreamWriterImpl.this.new ElementState();
            }
        }

        public ElementState push(ElementState element) {
            if (this.fDepth == this.fElements.length) {
                ElementState[] array = new ElementState[this.fElements.length * 2];
                System.arraycopy(this.fElements, 0, array, 0, this.fDepth);
                this.fElements = array;
                for (int i2 = this.fDepth; i2 < this.fElements.length; i2++) {
                    this.fElements[i2] = XMLStreamWriterImpl.this.new ElementState();
                }
            }
            this.fElements[this.fDepth].setValues(element);
            ElementState[] elementStateArr = this.fElements;
            short s2 = this.fDepth;
            this.fDepth = (short) (s2 + 1);
            return elementStateArr[s2];
        }

        public ElementState push(String prefix, String localpart, String rawname, String uri, boolean isEmpty) {
            if (this.fDepth == this.fElements.length) {
                ElementState[] array = new ElementState[this.fElements.length * 2];
                System.arraycopy(this.fElements, 0, array, 0, this.fDepth);
                this.fElements = array;
                for (int i2 = this.fDepth; i2 < this.fElements.length; i2++) {
                    this.fElements[i2] = XMLStreamWriterImpl.this.new ElementState();
                }
            }
            this.fElements[this.fDepth].setValues(prefix, localpart, rawname, uri, isEmpty);
            ElementState[] elementStateArr = this.fElements;
            short s2 = this.fDepth;
            this.fDepth = (short) (s2 + 1);
            return elementStateArr[s2];
        }

        public ElementState pop() {
            ElementState[] elementStateArr = this.fElements;
            short s2 = (short) (this.fDepth - 1);
            this.fDepth = s2;
            return elementStateArr[s2];
        }

        public void clear() {
            this.fDepth = (short) 0;
        }

        public ElementState peek() {
            return this.fElements[this.fDepth - 1];
        }

        public boolean empty() {
            return this.fDepth <= 0;
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/stream/writers/XMLStreamWriterImpl$ElementState.class */
    class ElementState extends QName {
        public boolean isEmpty;

        public ElementState() {
            this.isEmpty = false;
        }

        public ElementState(String prefix, String localpart, String rawname, String uri) {
            super(prefix, localpart, rawname, uri);
            this.isEmpty = false;
        }

        public void setValues(String prefix, String localpart, String rawname, String uri, boolean isEmpty) {
            super.setValues(prefix, localpart, rawname, uri);
            this.isEmpty = isEmpty;
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/stream/writers/XMLStreamWriterImpl$Attribute.class */
    class Attribute extends QName {
        String value;

        Attribute(String value) {
            this.value = value;
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/stream/writers/XMLStreamWriterImpl$NamespaceContextImpl.class */
    class NamespaceContextImpl implements NamespaceContext {
        NamespaceContext userContext = null;
        NamespaceSupport internalContext = null;

        NamespaceContextImpl() {
        }

        @Override // javax.xml.namespace.NamespaceContext
        public String getNamespaceURI(String prefix) {
            String uri;
            if (prefix != null) {
                prefix = XMLStreamWriterImpl.this.fSymbolTable.addSymbol(prefix);
            }
            if (this.internalContext != null && (uri = this.internalContext.getURI(prefix)) != null) {
                return uri;
            }
            if (this.userContext != null) {
                return this.userContext.getNamespaceURI(prefix);
            }
            return null;
        }

        @Override // javax.xml.namespace.NamespaceContext
        public String getPrefix(String uri) {
            String prefix;
            if (uri != null) {
                uri = XMLStreamWriterImpl.this.fSymbolTable.addSymbol(uri);
            }
            if (this.internalContext != null && (prefix = this.internalContext.getPrefix(uri)) != null) {
                return prefix;
            }
            if (this.userContext != null) {
                return this.userContext.getPrefix(uri);
            }
            return null;
        }

        @Override // javax.xml.namespace.NamespaceContext
        public Iterator getPrefixes(String uri) {
            Vector prefixes = null;
            Iterator itr = null;
            if (uri != null) {
                uri = XMLStreamWriterImpl.this.fSymbolTable.addSymbol(uri);
            }
            if (this.userContext != null) {
                itr = this.userContext.getPrefixes(uri);
            }
            if (this.internalContext != null) {
                prefixes = this.internalContext.getPrefixes(uri);
            }
            if (prefixes == null && itr != null) {
                return itr;
            }
            if (prefixes != null && itr == null) {
                return new ReadOnlyIterator(prefixes.iterator());
            }
            if (prefixes == null || itr == null) {
                return XMLStreamWriterImpl.this.fReadOnlyIterator;
            }
            while (itr.hasNext()) {
                String ob = (String) itr.next();
                if (ob != null) {
                    ob = XMLStreamWriterImpl.this.fSymbolTable.addSymbol(ob);
                }
                if (!prefixes.contains(ob)) {
                    prefixes.add(ob);
                }
            }
            return new ReadOnlyIterator(prefixes.iterator());
        }
    }

    @Override // java.util.AbstractMap, java.util.Map
    public int size() {
        return 1;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean isEmpty() {
        return false;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean containsKey(Object key) {
        return key.equals(OUTPUTSTREAM_PROPERTY);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Object get(Object key) {
        if (key.equals(OUTPUTSTREAM_PROPERTY)) {
            return this.fOutputStream;
        }
        return null;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Set entrySet() {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.AbstractMap
    public String toString() {
        return getClass().getName() + "@" + Integer.toHexString(hashCode());
    }

    @Override // java.util.AbstractMap, java.util.Map
    public int hashCode() {
        return this.fElementStack.hashCode();
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean equals(Object obj) {
        return this == obj;
    }
}
