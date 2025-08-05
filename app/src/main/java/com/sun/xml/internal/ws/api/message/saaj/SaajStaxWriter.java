package com.sun.xml.internal.ws.api.message.saaj;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.w3c.dom.Comment;
import org.w3c.dom.Node;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/message/saaj/SaajStaxWriter.class */
public class SaajStaxWriter implements XMLStreamWriter {
    protected SOAPMessage soap;
    protected String envURI;
    protected SOAPElement currentElement;
    protected DeferredElement deferredElement = new DeferredElement();
    protected static final String Envelope = "Envelope";
    protected static final String Header = "Header";
    protected static final String Body = "Body";
    protected static final String xmlns = "xmlns";

    public SaajStaxWriter(SOAPMessage msg) throws SOAPException {
        this.soap = msg;
        this.currentElement = this.soap.getSOAPPart().getEnvelope();
        this.envURI = this.currentElement.getNamespaceURI();
    }

    public SOAPMessage getSOAPMessage() {
        return this.soap;
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeStartElement(String localName) throws XMLStreamException {
        this.currentElement = this.deferredElement.flushTo(this.currentElement);
        this.deferredElement.setLocalName(localName);
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeStartElement(String ns, String ln) throws XMLStreamException {
        writeStartElement(null, ln, ns);
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeStartElement(String prefix, String ln, String ns) throws XMLStreamException {
        this.currentElement = this.deferredElement.flushTo(this.currentElement);
        if (this.envURI.equals(ns)) {
            try {
                if ("Envelope".equals(ln)) {
                    this.currentElement = this.soap.getSOAPPart().getEnvelope();
                    fixPrefix(prefix);
                    return;
                } else if ("Header".equals(ln)) {
                    this.currentElement = this.soap.getSOAPHeader();
                    fixPrefix(prefix);
                    return;
                } else if ("Body".equals(ln)) {
                    this.currentElement = this.soap.getSOAPBody();
                    fixPrefix(prefix);
                    return;
                }
            } catch (SOAPException e2) {
                throw new XMLStreamException(e2);
            }
        }
        this.deferredElement.setLocalName(ln);
        this.deferredElement.setNamespaceUri(ns);
        this.deferredElement.setPrefix(prefix);
    }

    private void fixPrefix(String prfx) throws XMLStreamException {
        String oldPrfx = this.currentElement.getPrefix();
        if (prfx != null && !prfx.equals(oldPrfx)) {
            this.currentElement.setPrefix(prfx);
        }
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeEmptyElement(String uri, String ln) throws XMLStreamException {
        writeStartElement(null, ln, uri);
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeEmptyElement(String prefix, String ln, String uri) throws XMLStreamException {
        writeStartElement(prefix, ln, uri);
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeEmptyElement(String ln) throws XMLStreamException {
        writeStartElement(null, ln, null);
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeEndElement() throws XMLStreamException {
        this.currentElement = this.deferredElement.flushTo(this.currentElement);
        if (this.currentElement != null) {
            this.currentElement = this.currentElement.getParentElement();
        }
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeEndDocument() throws XMLStreamException {
        this.currentElement = this.deferredElement.flushTo(this.currentElement);
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void close() throws XMLStreamException {
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void flush() throws XMLStreamException {
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeAttribute(String ln, String val) throws XMLStreamException {
        writeAttribute(null, null, ln, val);
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeAttribute(String prefix, String ns, String ln, String value) throws XMLStreamException {
        if (ns == null && prefix == null && "xmlns".equals(ln)) {
            writeNamespace("", value);
        } else if (this.deferredElement.isInitialized()) {
            this.deferredElement.addAttribute(prefix, ns, ln, value);
        } else {
            addAttibuteToElement(this.currentElement, prefix, ns, ln, value);
        }
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeAttribute(String ns, String ln, String val) throws XMLStreamException {
        writeAttribute(null, ns, ln, val);
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeNamespace(String prefix, String uri) throws XMLStreamException {
        String thePrefix = (prefix == null || "xmlns".equals(prefix)) ? "" : prefix;
        if (this.deferredElement.isInitialized()) {
            this.deferredElement.addNamespaceDeclaration(thePrefix, uri);
            return;
        }
        try {
            this.currentElement.addNamespaceDeclaration(thePrefix, uri);
        } catch (SOAPException e2) {
            throw new XMLStreamException(e2);
        }
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeDefaultNamespace(String uri) throws XMLStreamException {
        writeNamespace("", uri);
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeComment(String data) throws XMLStreamException {
        this.currentElement = this.deferredElement.flushTo(this.currentElement);
        Comment c2 = this.soap.getSOAPPart().createComment(data);
        this.currentElement.appendChild(c2);
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeProcessingInstruction(String target) throws XMLStreamException {
        this.currentElement = this.deferredElement.flushTo(this.currentElement);
        Node n2 = this.soap.getSOAPPart().createProcessingInstruction(target, "");
        this.currentElement.appendChild(n2);
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeProcessingInstruction(String target, String data) throws XMLStreamException {
        this.currentElement = this.deferredElement.flushTo(this.currentElement);
        Node n2 = this.soap.getSOAPPart().createProcessingInstruction(target, data);
        this.currentElement.appendChild(n2);
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeCData(String data) throws XMLStreamException {
        this.currentElement = this.deferredElement.flushTo(this.currentElement);
        Node n2 = this.soap.getSOAPPart().createCDATASection(data);
        this.currentElement.appendChild(n2);
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeDTD(String dtd) throws XMLStreamException {
        this.currentElement = this.deferredElement.flushTo(this.currentElement);
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeEntityRef(String name) throws XMLStreamException {
        this.currentElement = this.deferredElement.flushTo(this.currentElement);
        Node n2 = this.soap.getSOAPPart().createEntityReference(name);
        this.currentElement.appendChild(n2);
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeStartDocument() throws XMLStreamException {
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeStartDocument(String version) throws XMLStreamException {
        if (version != null) {
            this.soap.getSOAPPart().setXmlVersion(version);
        }
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeStartDocument(String encoding, String version) throws XMLStreamException {
        if (version != null) {
            this.soap.getSOAPPart().setXmlVersion(version);
        }
        if (encoding != null) {
            try {
                this.soap.setProperty(SOAPMessage.CHARACTER_SET_ENCODING, encoding);
            } catch (SOAPException e2) {
                throw new XMLStreamException(e2);
            }
        }
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeCharacters(String text) throws XMLStreamException {
        this.currentElement = this.deferredElement.flushTo(this.currentElement);
        try {
            this.currentElement.addTextNode(text);
        } catch (SOAPException e2) {
            throw new XMLStreamException(e2);
        }
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeCharacters(char[] text, int start, int len) throws XMLStreamException {
        this.currentElement = this.deferredElement.flushTo(this.currentElement);
        char[] chr = (start == 0 && len == text.length) ? text : Arrays.copyOfRange(text, start, start + len);
        try {
            this.currentElement.addTextNode(new String(chr));
        } catch (SOAPException e2) {
            throw new XMLStreamException(e2);
        }
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public String getPrefix(String uri) throws XMLStreamException {
        return this.currentElement.lookupPrefix(uri);
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void setPrefix(String prefix, String uri) throws XMLStreamException {
        if (this.deferredElement.isInitialized()) {
            this.deferredElement.addNamespaceDeclaration(prefix, uri);
            return;
        }
        throw new XMLStreamException("Namespace not associated with any element");
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void setDefaultNamespace(String uri) throws XMLStreamException {
        setPrefix("", uri);
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void setNamespaceContext(NamespaceContext context) throws XMLStreamException {
        throw new UnsupportedOperationException();
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public Object getProperty(String name) throws IllegalArgumentException {
        if (XMLOutputFactory.IS_REPAIRING_NAMESPACES.equals(name)) {
            return Boolean.FALSE;
        }
        return null;
    }

    /* renamed from: com.sun.xml.internal.ws.api.message.saaj.SaajStaxWriter$1, reason: invalid class name */
    /* loaded from: rt.jar:com/sun/xml/internal/ws/api/message/saaj/SaajStaxWriter$1.class */
    class AnonymousClass1 implements NamespaceContext {
        AnonymousClass1() {
        }

        @Override // javax.xml.namespace.NamespaceContext
        public String getNamespaceURI(String prefix) {
            return SaajStaxWriter.this.currentElement.getNamespaceURI(prefix);
        }

        @Override // javax.xml.namespace.NamespaceContext
        public String getPrefix(String namespaceURI) {
            return SaajStaxWriter.this.currentElement.lookupPrefix(namespaceURI);
        }

        @Override // javax.xml.namespace.NamespaceContext
        public Iterator getPrefixes(final String namespaceURI) {
            return new Iterator<String>() { // from class: com.sun.xml.internal.ws.api.message.saaj.SaajStaxWriter.1.1
                String prefix;

                {
                    this.prefix = AnonymousClass1.this.getPrefix(namespaceURI);
                }

                @Override // java.util.Iterator
                public boolean hasNext() {
                    return this.prefix != null;
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.util.Iterator
                public String next() {
                    if (!hasNext()) {
                        throw new NoSuchElementException();
                    }
                    String next = this.prefix;
                    this.prefix = null;
                    return next;
                }

                @Override // java.util.Iterator
                public void remove() {
                }
            };
        }
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public NamespaceContext getNamespaceContext() {
        return new AnonymousClass1();
    }

    static void addAttibuteToElement(SOAPElement element, String prefix, String ns, String ln, String value) throws XMLStreamException {
        try {
            if (ns == null) {
                element.setAttributeNS("", ln, value);
            } else {
                QName name = prefix == null ? new QName(ns, ln) : new QName(ns, ln, prefix);
                element.addAttribute(name, value);
            }
        } catch (SOAPException e2) {
            throw new XMLStreamException(e2);
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/api/message/saaj/SaajStaxWriter$DeferredElement.class */
    static class DeferredElement {
        private String prefix;
        private String localName;
        private String namespaceUri;
        private final List<NamespaceDeclaration> namespaceDeclarations = new LinkedList();
        private final List<AttributeDeclaration> attributeDeclarations = new LinkedList();

        DeferredElement() {
            reset();
        }

        public void setPrefix(String prefix) {
            this.prefix = prefix;
        }

        public void setLocalName(String localName) {
            if (localName == null) {
                throw new IllegalArgumentException("localName can not be null");
            }
            this.localName = localName;
        }

        public void setNamespaceUri(String namespaceUri) {
            this.namespaceUri = namespaceUri;
        }

        public void addNamespaceDeclaration(String prefix, String namespaceUri) {
            if (null == this.namespaceUri && null != namespaceUri && prefix.equals(emptyIfNull(this.prefix))) {
                this.namespaceUri = namespaceUri;
            }
            this.namespaceDeclarations.add(new NamespaceDeclaration(prefix, namespaceUri));
        }

        public void addAttribute(String prefix, String ns, String ln, String value) {
            if (ns == null && prefix == null && "xmlns".equals(ln)) {
                addNamespaceDeclaration(prefix, value);
            } else {
                this.attributeDeclarations.add(new AttributeDeclaration(prefix, ns, ln, value));
            }
        }

        public SOAPElement flushTo(SOAPElement target) throws XMLStreamException {
            SOAPElement newElement;
            try {
                if (this.localName != null) {
                    if (this.namespaceUri == null) {
                        newElement = target.addChildElement(this.localName);
                    } else if (this.prefix == null) {
                        newElement = target.addChildElement(new QName(this.namespaceUri, this.localName));
                    } else {
                        newElement = target.addChildElement(this.localName, this.prefix, this.namespaceUri);
                    }
                    for (NamespaceDeclaration namespace : this.namespaceDeclarations) {
                        newElement.addNamespaceDeclaration(namespace.prefix, namespace.namespaceUri);
                    }
                    for (AttributeDeclaration attribute : this.attributeDeclarations) {
                        SaajStaxWriter.addAttibuteToElement(newElement, attribute.prefix, attribute.namespaceUri, attribute.localName, attribute.value);
                    }
                    reset();
                    return newElement;
                }
                return target;
            } catch (SOAPException e2) {
                throw new XMLStreamException(e2);
            }
        }

        public boolean isInitialized() {
            return this.localName != null;
        }

        private void reset() {
            this.localName = null;
            this.prefix = null;
            this.namespaceUri = null;
            this.namespaceDeclarations.clear();
            this.attributeDeclarations.clear();
        }

        private static String emptyIfNull(String s2) {
            return s2 == null ? "" : s2;
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/api/message/saaj/SaajStaxWriter$NamespaceDeclaration.class */
    static class NamespaceDeclaration {
        final String prefix;
        final String namespaceUri;

        NamespaceDeclaration(String prefix, String namespaceUri) {
            this.prefix = prefix;
            this.namespaceUri = namespaceUri;
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/api/message/saaj/SaajStaxWriter$AttributeDeclaration.class */
    static class AttributeDeclaration {
        final String prefix;
        final String namespaceUri;
        final String localName;
        final String value;

        AttributeDeclaration(String prefix, String namespaceUri, String localName, String value) {
            this.prefix = prefix;
            this.namespaceUri = namespaceUri;
            this.localName = localName;
            this.value = value;
        }
    }
}
