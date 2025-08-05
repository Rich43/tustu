package com.sun.org.apache.xml.internal.serializer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Properties;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.Transformer;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/serializer/ToUnknownStream.class */
public final class ToUnknownStream extends SerializerBase {
    private static final String EMPTYSTRING = "";
    private String m_firstElementPrefix;
    private String m_firstElementName;
    private String m_firstElementURI;
    private boolean m_wrapped_handler_not_initialized = false;
    private String m_firstElementLocalName = null;
    private boolean m_firstTagNotEmitted = true;
    private ArrayList<String> m_namespaceURI = null;
    private ArrayList<String> m_namespacePrefix = null;
    private boolean m_needToCallStartDocument = false;
    private boolean m_setVersion_called = false;
    private boolean m_setDoctypeSystem_called = false;
    private boolean m_setDoctypePublic_called = false;
    private boolean m_setMediaType_called = false;
    private SerializationHandler m_handler = new ToXMLStream();

    @Override // com.sun.org.apache.xml.internal.serializer.SerializerBase, com.sun.org.apache.xml.internal.serializer.Serializer
    public ContentHandler asContentHandler() throws IOException {
        return this;
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializerBase, com.sun.org.apache.xml.internal.serializer.SerializationHandler
    public void close() {
        this.m_handler.close();
    }

    @Override // com.sun.org.apache.xml.internal.serializer.Serializer
    public Properties getOutputFormat() {
        return this.m_handler.getOutputFormat();
    }

    @Override // com.sun.org.apache.xml.internal.serializer.Serializer
    public OutputStream getOutputStream() {
        return this.m_handler.getOutputStream();
    }

    @Override // com.sun.org.apache.xml.internal.serializer.Serializer
    public Writer getWriter() {
        return this.m_handler.getWriter();
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializerBase, com.sun.org.apache.xml.internal.serializer.Serializer
    public boolean reset() {
        return this.m_handler.reset();
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializationHandler, com.sun.org.apache.xml.internal.serializer.DOMSerializer
    public void serialize(Node node) throws Exception {
        if (this.m_firstTagNotEmitted) {
            flush();
        }
        this.m_handler.serialize(node);
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializationHandler
    public boolean setEscaping(boolean escape) throws SAXException {
        return this.m_handler.setEscaping(escape);
    }

    @Override // com.sun.org.apache.xml.internal.serializer.Serializer
    public void setOutputFormat(Properties format) {
        this.m_handler.setOutputFormat(format);
    }

    @Override // com.sun.org.apache.xml.internal.serializer.Serializer
    public void setOutputStream(OutputStream output) {
        this.m_handler.setOutputStream(output);
    }

    @Override // com.sun.org.apache.xml.internal.serializer.Serializer
    public void setWriter(Writer writer) {
        this.m_handler.setWriter(writer);
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializerBase, com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public void addAttribute(String uri, String localName, String rawName, String type, String value) throws Exception {
        addAttribute(uri, localName, rawName, type, value, false);
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializerBase, com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public void addAttribute(String uri, String localName, String rawName, String type, String value, boolean XSLAttribute) throws Exception {
        if (this.m_firstTagNotEmitted) {
            flush();
        }
        this.m_handler.addAttribute(uri, localName, rawName, type, value, XSLAttribute);
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializerBase, com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public void addAttribute(String rawName, String value) throws Exception {
        if (this.m_firstTagNotEmitted) {
            flush();
        }
        this.m_handler.addAttribute(rawName, value);
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public void addUniqueAttribute(String rawName, String value, int flags) throws Exception {
        if (this.m_firstTagNotEmitted) {
            flush();
        }
        this.m_handler.addUniqueAttribute(rawName, value, flags);
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public void characters(String chars) throws Exception {
        int length = chars.length();
        if (length > this.m_charsBuff.length) {
            this.m_charsBuff = new char[(length * 2) + 1];
        }
        chars.getChars(0, length, this.m_charsBuff, 0);
        characters(this.m_charsBuff, 0, length);
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public void endElement(String elementName) throws Exception {
        if (this.m_firstTagNotEmitted) {
            flush();
        }
        this.m_handler.endElement(elementName);
    }

    @Override // org.xml.sax.ContentHandler
    public void startPrefixMapping(String prefix, String uri) throws Exception {
        startPrefixMapping(prefix, uri, true);
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializerBase, com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public void namespaceAfterStartElement(String prefix, String uri) throws Exception {
        if (this.m_firstTagNotEmitted && this.m_firstElementURI == null && this.m_firstElementName != null) {
            String prefix1 = getPrefixPart(this.m_firstElementName);
            if (prefix1 == null && "".equals(prefix)) {
                this.m_firstElementURI = uri;
            }
        }
        startPrefixMapping(prefix, uri, false);
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public boolean startPrefixMapping(String prefix, String uri, boolean shouldFlush) throws Exception {
        boolean pushed = false;
        if (this.m_firstTagNotEmitted) {
            if (this.m_firstElementName != null && shouldFlush) {
                flush();
                pushed = this.m_handler.startPrefixMapping(prefix, uri, shouldFlush);
            } else {
                if (this.m_namespacePrefix == null) {
                    this.m_namespacePrefix = new ArrayList<>();
                    this.m_namespaceURI = new ArrayList<>();
                }
                this.m_namespacePrefix.add(prefix);
                this.m_namespaceURI.add(uri);
                if (this.m_firstElementURI == null && prefix.equals(this.m_firstElementPrefix)) {
                    this.m_firstElementURI = uri;
                }
            }
        } else {
            pushed = this.m_handler.startPrefixMapping(prefix, uri, shouldFlush);
        }
        return pushed;
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializerBase, com.sun.org.apache.xml.internal.serializer.XSLOutputAttributes
    public void setVersion(String version) {
        this.m_handler.setVersion(version);
        this.m_setVersion_called = true;
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializerBase, org.xml.sax.ContentHandler
    public void startDocument() throws SAXException {
        this.m_needToCallStartDocument = true;
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public void startElement(String qName) throws Exception {
        startElement(null, null, qName, null);
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public void startElement(String namespaceURI, String localName, String qName) throws Exception {
        startElement(namespaceURI, localName, qName, null);
    }

    @Override // org.xml.sax.ContentHandler
    public void startElement(String namespaceURI, String localName, String elementName, Attributes atts) throws Exception {
        if (this.m_needToCallSetDocumentInfo) {
            super.setDocumentInfo();
            this.m_needToCallSetDocumentInfo = false;
        }
        if (this.m_firstTagNotEmitted) {
            if (this.m_firstElementName != null) {
                flush();
                this.m_handler.startElement(namespaceURI, localName, elementName, atts);
                return;
            }
            this.m_wrapped_handler_not_initialized = true;
            this.m_firstElementName = elementName;
            this.m_firstElementPrefix = getPrefixPartUnknown(elementName);
            this.m_firstElementURI = namespaceURI;
            this.m_firstElementLocalName = localName;
            if (this.m_tracer != null) {
                firePseudoElement(elementName);
            }
            if (atts != null) {
                super.addAttributes(atts);
            }
            if (atts != null) {
                flush();
                return;
            }
            return;
        }
        this.m_handler.startElement(namespaceURI, localName, elementName, atts);
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializerBase, com.sun.org.apache.xml.internal.serializer.ExtendedLexicalHandler
    public void comment(String comment) throws Exception {
        if (this.m_firstTagNotEmitted && this.m_firstElementName != null) {
            emitFirstTag();
        } else if (this.m_needToCallStartDocument) {
            this.m_handler.startDocument();
            this.m_needToCallStartDocument = false;
        }
        this.m_handler.comment(comment);
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializerBase, com.sun.org.apache.xml.internal.serializer.XSLOutputAttributes
    public String getDoctypePublic() {
        return this.m_handler.getDoctypePublic();
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializerBase, com.sun.org.apache.xml.internal.serializer.XSLOutputAttributes
    public String getDoctypeSystem() {
        return this.m_handler.getDoctypeSystem();
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializerBase, com.sun.org.apache.xml.internal.serializer.XSLOutputAttributes
    public String getEncoding() {
        return this.m_handler.getEncoding();
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializerBase, com.sun.org.apache.xml.internal.serializer.XSLOutputAttributes
    public boolean getIndent() {
        return this.m_handler.getIndent();
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializerBase, com.sun.org.apache.xml.internal.serializer.XSLOutputAttributes
    public int getIndentAmount() {
        return this.m_handler.getIndentAmount();
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializerBase, com.sun.org.apache.xml.internal.serializer.XSLOutputAttributes
    public String getMediaType() {
        return this.m_handler.getMediaType();
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializerBase, com.sun.org.apache.xml.internal.serializer.XSLOutputAttributes
    public boolean getOmitXMLDeclaration() {
        return this.m_handler.getOmitXMLDeclaration();
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializerBase, com.sun.org.apache.xml.internal.serializer.XSLOutputAttributes
    public String getStandalone() {
        return this.m_handler.getStandalone();
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializerBase, com.sun.org.apache.xml.internal.serializer.XSLOutputAttributes
    public String getVersion() {
        return this.m_handler.getVersion();
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializerBase, com.sun.org.apache.xml.internal.serializer.XSLOutputAttributes
    public void setDoctype(String system, String pub) {
        this.m_handler.setDoctypePublic(pub);
        this.m_handler.setDoctypeSystem(system);
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializerBase, com.sun.org.apache.xml.internal.serializer.XSLOutputAttributes
    public void setDoctypePublic(String doctype) {
        this.m_handler.setDoctypePublic(doctype);
        this.m_setDoctypePublic_called = true;
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializerBase, com.sun.org.apache.xml.internal.serializer.XSLOutputAttributes
    public void setDoctypeSystem(String doctype) {
        this.m_handler.setDoctypeSystem(doctype);
        this.m_setDoctypeSystem_called = true;
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializerBase, com.sun.org.apache.xml.internal.serializer.XSLOutputAttributes
    public void setEncoding(String encoding) {
        this.m_handler.setEncoding(encoding);
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializerBase, com.sun.org.apache.xml.internal.serializer.XSLOutputAttributes
    public void setIndent(boolean indent) {
        this.m_handler.setIndent(indent);
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializerBase, com.sun.org.apache.xml.internal.serializer.SerializationHandler
    public void setIndentAmount(int value) {
        this.m_handler.setIndentAmount(value);
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializerBase, com.sun.org.apache.xml.internal.serializer.XSLOutputAttributes
    public void setMediaType(String mediaType) {
        this.m_handler.setMediaType(mediaType);
        this.m_setMediaType_called = true;
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializerBase, com.sun.org.apache.xml.internal.serializer.XSLOutputAttributes
    public void setOmitXMLDeclaration(boolean b2) {
        this.m_handler.setOmitXMLDeclaration(b2);
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializerBase, com.sun.org.apache.xml.internal.serializer.XSLOutputAttributes
    public void setStandalone(String standalone) {
        this.m_handler.setStandalone(standalone);
    }

    @Override // org.xml.sax.ext.DeclHandler
    public void attributeDecl(String arg0, String arg1, String arg2, String arg3, String arg4) throws SAXException {
        this.m_handler.attributeDecl(arg0, arg1, arg2, arg3, arg4);
    }

    @Override // org.xml.sax.ext.DeclHandler
    public void elementDecl(String arg0, String arg1) throws Exception {
        if (this.m_firstTagNotEmitted) {
            emitFirstTag();
        }
        this.m_handler.elementDecl(arg0, arg1);
    }

    @Override // org.xml.sax.ext.DeclHandler
    public void externalEntityDecl(String name, String publicId, String systemId) throws Exception {
        if (this.m_firstTagNotEmitted) {
            flush();
        }
        this.m_handler.externalEntityDecl(name, publicId, systemId);
    }

    @Override // org.xml.sax.ext.DeclHandler
    public void internalEntityDecl(String arg0, String arg1) throws Exception {
        if (this.m_firstTagNotEmitted) {
            flush();
        }
        this.m_handler.internalEntityDecl(arg0, arg1);
    }

    @Override // org.xml.sax.ContentHandler
    public void characters(char[] characters, int offset, int length) throws Exception {
        if (this.m_firstTagNotEmitted) {
            flush();
        }
        this.m_handler.characters(characters, offset, length);
    }

    @Override // org.xml.sax.ContentHandler
    public void endDocument() throws Exception {
        if (this.m_firstTagNotEmitted) {
            flush();
        }
        this.m_handler.endDocument();
    }

    @Override // org.xml.sax.ContentHandler
    public void endElement(String namespaceURI, String localName, String qName) throws Exception {
        if (this.m_firstTagNotEmitted) {
            flush();
            if (namespaceURI == null && this.m_firstElementURI != null) {
                namespaceURI = this.m_firstElementURI;
            }
            if (localName == null && this.m_firstElementLocalName != null) {
                localName = this.m_firstElementLocalName;
            }
        }
        this.m_handler.endElement(namespaceURI, localName, qName);
    }

    @Override // org.xml.sax.ContentHandler
    public void endPrefixMapping(String prefix) throws SAXException {
        this.m_handler.endPrefixMapping(prefix);
    }

    @Override // org.xml.sax.ContentHandler
    public void ignorableWhitespace(char[] ch, int start, int length) throws Exception {
        if (this.m_firstTagNotEmitted) {
            flush();
        }
        this.m_handler.ignorableWhitespace(ch, start, length);
    }

    @Override // org.xml.sax.ContentHandler
    public void processingInstruction(String target, String data) throws Exception {
        if (this.m_firstTagNotEmitted) {
            flush();
        }
        this.m_handler.processingInstruction(target, data);
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializerBase, org.xml.sax.ContentHandler
    public void setDocumentLocator(Locator locator) {
        super.setDocumentLocator(locator);
        this.m_handler.setDocumentLocator(locator);
    }

    @Override // org.xml.sax.ContentHandler
    public void skippedEntity(String name) throws SAXException {
        this.m_handler.skippedEntity(name);
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void comment(char[] ch, int start, int length) throws Exception {
        if (this.m_firstTagNotEmitted) {
            flush();
        }
        this.m_handler.comment(ch, start, length);
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void endCDATA() throws SAXException {
        this.m_handler.endCDATA();
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void endDTD() throws SAXException {
        this.m_handler.endDTD();
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializerBase, org.xml.sax.ext.LexicalHandler
    public void endEntity(String name) throws Exception {
        if (this.m_firstTagNotEmitted) {
            emitFirstTag();
        }
        this.m_handler.endEntity(name);
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void startCDATA() throws SAXException {
        this.m_handler.startCDATA();
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void startDTD(String name, String publicId, String systemId) throws SAXException {
        this.m_handler.startDTD(name, publicId, systemId);
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void startEntity(String name) throws SAXException {
        this.m_handler.startEntity(name);
    }

    private void initStreamOutput() throws Exception {
        boolean firstElementIsHTML = isFirstElemHTML();
        if (firstElementIsHTML) {
            SerializationHandler oldHandler = this.m_handler;
            Properties htmlProperties = OutputPropertiesFactory.getDefaultMethodProperties("html");
            Serializer serializer = SerializerFactory.getSerializer(htmlProperties);
            this.m_handler = (SerializationHandler) serializer;
            Writer writer = oldHandler.getWriter();
            if (null != writer) {
                this.m_handler.setWriter(writer);
            } else {
                OutputStream os = oldHandler.getOutputStream();
                if (null != os) {
                    this.m_handler.setOutputStream(os);
                }
            }
            this.m_handler.setVersion(oldHandler.getVersion());
            this.m_handler.setDoctypeSystem(oldHandler.getDoctypeSystem());
            this.m_handler.setDoctypePublic(oldHandler.getDoctypePublic());
            this.m_handler.setMediaType(oldHandler.getMediaType());
            this.m_handler.setTransformer(oldHandler.getTransformer());
        }
        if (this.m_needToCallStartDocument) {
            this.m_handler.startDocument();
            this.m_needToCallStartDocument = false;
        }
        this.m_wrapped_handler_not_initialized = false;
    }

    private void emitFirstTag() throws Exception {
        if (this.m_firstElementName != null) {
            if (this.m_wrapped_handler_not_initialized) {
                initStreamOutput();
                this.m_wrapped_handler_not_initialized = false;
            }
            this.m_handler.startElement(this.m_firstElementURI, null, this.m_firstElementName, this.m_attributes);
            this.m_attributes = null;
            if (this.m_namespacePrefix != null) {
                int n2 = this.m_namespacePrefix.size();
                for (int i2 = 0; i2 < n2; i2++) {
                    String prefix = this.m_namespacePrefix.get(i2);
                    String uri = this.m_namespaceURI.get(i2);
                    this.m_handler.startPrefixMapping(prefix, uri, false);
                }
                this.m_namespacePrefix = null;
                this.m_namespaceURI = null;
            }
            this.m_firstTagNotEmitted = false;
        }
    }

    private String getLocalNameUnknown(String value) {
        int idx = value.lastIndexOf(58);
        if (idx >= 0) {
            value = value.substring(idx + 1);
        }
        int idx2 = value.lastIndexOf(64);
        if (idx2 >= 0) {
            value = value.substring(idx2 + 1);
        }
        return value;
    }

    private String getPrefixPartUnknown(String qname) {
        int index = qname.indexOf(58);
        return index > 0 ? qname.substring(0, index) : "";
    }

    private boolean isFirstElemHTML() {
        boolean isHTML = getLocalNameUnknown(this.m_firstElementName).equalsIgnoreCase("html");
        if (isHTML && this.m_firstElementURI != null && !"".equals(this.m_firstElementURI)) {
            isHTML = false;
        }
        if (isHTML && this.m_namespacePrefix != null) {
            int max = this.m_namespacePrefix.size();
            int i2 = 0;
            while (true) {
                if (i2 >= max) {
                    break;
                }
                String prefix = this.m_namespacePrefix.get(i2);
                String uri = this.m_namespaceURI.get(i2);
                if (this.m_firstElementPrefix == null || !this.m_firstElementPrefix.equals(prefix) || "".equals(uri)) {
                    i2++;
                } else {
                    isHTML = false;
                    break;
                }
            }
        }
        return isHTML;
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializerBase, com.sun.org.apache.xml.internal.serializer.Serializer
    public DOMSerializer asDOMSerializer() throws IOException {
        return this.m_handler.asDOMSerializer();
    }

    @Override // com.sun.org.apache.xml.internal.serializer.XSLOutputAttributes
    public void setCdataSectionElements(ArrayList<String> URI_and_localNames) {
        this.m_handler.setCdataSectionElements(URI_and_localNames);
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializerBase, com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public void addAttributes(Attributes atts) throws SAXException {
        this.m_handler.addAttributes(atts);
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializerBase, com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public NamespaceMappings getNamespaceMappings() {
        NamespaceMappings mappings = null;
        if (this.m_handler != null) {
            mappings = this.m_handler.getNamespaceMappings();
        }
        return mappings;
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializationHandler
    public void flushPending() throws Exception {
        flush();
        this.m_handler.flushPending();
    }

    private void flush() throws Exception {
        try {
            if (this.m_firstTagNotEmitted) {
                emitFirstTag();
            }
            if (this.m_needToCallStartDocument) {
                this.m_handler.startDocument();
                this.m_needToCallStartDocument = false;
            }
        } catch (SAXException e2) {
            throw new RuntimeException(e2.toString());
        }
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializerBase, com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public String getPrefix(String namespaceURI) {
        return this.m_handler.getPrefix(namespaceURI);
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializerBase, com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public void entityReference(String entityName) throws SAXException {
        this.m_handler.entityReference(entityName);
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializerBase, com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public String getNamespaceURI(String qname, boolean isElement) {
        return this.m_handler.getNamespaceURI(qname, isElement);
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializerBase, com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public String getNamespaceURIFromPrefix(String prefix) {
        return this.m_handler.getNamespaceURIFromPrefix(prefix);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.org.apache.xml.internal.serializer.SerializerBase, com.sun.org.apache.xml.internal.serializer.SerializationHandler
    public void setTransformer(Transformer transformer) {
        this.m_handler.setTransformer(transformer);
        if ((transformer instanceof SerializerTrace) && ((SerializerTrace) transformer).hasTraceListeners()) {
            this.m_tracer = (SerializerTrace) transformer;
        } else {
            this.m_tracer = null;
        }
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializerBase, com.sun.org.apache.xml.internal.serializer.SerializationHandler
    public Transformer getTransformer() {
        return this.m_handler.getTransformer();
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializationHandler
    public void setContentHandler(ContentHandler ch) {
        this.m_handler.setContentHandler(ch);
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializerBase, com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public void setSourceLocator(SourceLocator locator) {
        this.m_handler.setSourceLocator(locator);
    }

    protected void firePseudoElement(String elementName) {
        if (this.m_tracer != null) {
            StringBuffer sb = new StringBuffer();
            sb.append('<');
            sb.append(elementName);
            char[] ch = sb.toString().toCharArray();
            this.m_tracer.fireGenerateEvent(11, ch, 0, ch.length);
        }
    }
}
