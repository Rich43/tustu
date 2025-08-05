package com.sun.xml.internal.stream.writers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.xml.namespace.NamespaceContext;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.dom.DOMResult;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;
import org.xml.sax.helpers.NamespaceSupport;

/* loaded from: rt.jar:com/sun/xml/internal/stream/writers/XMLDOMWriterImpl.class */
public class XMLDOMWriterImpl implements XMLStreamWriter {
    private Document ownerDoc;
    private Node currentNode;
    private Node node;
    private NamespaceSupport namespaceContext;
    private boolean[] needContextPop;
    private StringBuffer stringBuffer;
    private Method mXmlVersion = null;
    private int resizeValue = 20;
    private int depth = 0;

    public XMLDOMWriterImpl(DOMResult result) {
        this.ownerDoc = null;
        this.currentNode = null;
        this.node = null;
        this.namespaceContext = null;
        this.needContextPop = null;
        this.stringBuffer = null;
        this.node = result.getNode();
        if (this.node.getNodeType() == 9) {
            this.ownerDoc = (Document) this.node;
            this.currentNode = this.ownerDoc;
        } else {
            this.ownerDoc = this.node.getOwnerDocument();
            this.currentNode = this.node;
        }
        getDLThreeMethods();
        this.stringBuffer = new StringBuffer();
        this.needContextPop = new boolean[this.resizeValue];
        this.namespaceContext = new NamespaceSupport();
    }

    private void getDLThreeMethods() {
        try {
            this.mXmlVersion = this.ownerDoc.getClass().getMethod("setXmlVersion", String.class);
        } catch (NoSuchMethodException e2) {
            this.mXmlVersion = null;
        } catch (SecurityException e3) {
            this.mXmlVersion = null;
        }
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void close() throws XMLStreamException {
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void flush() throws XMLStreamException {
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public NamespaceContext getNamespaceContext() {
        return null;
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public String getPrefix(String namespaceURI) throws XMLStreamException {
        String prefix = null;
        if (this.namespaceContext != null) {
            prefix = this.namespaceContext.getPrefix(namespaceURI);
        }
        return prefix;
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public Object getProperty(String str) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void setDefaultNamespace(String uri) throws XMLStreamException {
        this.namespaceContext.declarePrefix("", uri);
        if (!this.needContextPop[this.depth]) {
            this.needContextPop[this.depth] = true;
        }
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void setNamespaceContext(NamespaceContext namespaceContext) throws XMLStreamException {
        throw new UnsupportedOperationException();
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void setPrefix(String prefix, String uri) throws XMLStreamException {
        if (prefix == null) {
            throw new XMLStreamException("Prefix cannot be null");
        }
        this.namespaceContext.declarePrefix(prefix, uri);
        if (!this.needContextPop[this.depth]) {
            this.needContextPop[this.depth] = true;
        }
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeAttribute(String localName, String value) throws DOMException, XMLStreamException {
        if (this.currentNode.getNodeType() == 1) {
            Attr attr = this.ownerDoc.createAttribute(localName);
            attr.setValue(value);
            ((Element) this.currentNode).setAttributeNode(attr);
            return;
        }
        throw new IllegalStateException("Current DOM Node type  is " + ((int) this.currentNode.getNodeType()) + "and does not allow attributes to be set ");
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeAttribute(String namespaceURI, String localName, String value) throws XMLStreamException, DOMException {
        String qualifiedName;
        if (this.currentNode.getNodeType() == 1) {
            String prefix = null;
            if (namespaceURI == null) {
                throw new XMLStreamException("NamespaceURI cannot be null");
            }
            if (localName == null) {
                throw new XMLStreamException("Local name cannot be null");
            }
            if (this.namespaceContext != null) {
                prefix = this.namespaceContext.getPrefix(namespaceURI);
            }
            if (prefix == null) {
                throw new XMLStreamException("Namespace URI " + namespaceURI + "is not bound to any prefix");
            }
            if (prefix.equals("")) {
                qualifiedName = localName;
            } else {
                qualifiedName = getQName(prefix, localName);
            }
            Attr attr = this.ownerDoc.createAttributeNS(namespaceURI, qualifiedName);
            attr.setValue(value);
            ((Element) this.currentNode).setAttributeNode(attr);
            return;
        }
        throw new IllegalStateException("Current DOM Node type  is " + ((int) this.currentNode.getNodeType()) + "and does not allow attributes to be set ");
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeAttribute(String prefix, String namespaceURI, String localName, String value) throws XMLStreamException, DOMException {
        String qualifiedName;
        if (this.currentNode.getNodeType() == 1) {
            if (namespaceURI == null) {
                throw new XMLStreamException("NamespaceURI cannot be null");
            }
            if (localName == null) {
                throw new XMLStreamException("Local name cannot be null");
            }
            if (prefix == null) {
                throw new XMLStreamException("prefix cannot be null");
            }
            if (prefix.equals("")) {
                qualifiedName = localName;
            } else {
                qualifiedName = getQName(prefix, localName);
            }
            Attr attr = this.ownerDoc.createAttributeNS(namespaceURI, qualifiedName);
            attr.setValue(value);
            ((Element) this.currentNode).setAttributeNodeNS(attr);
            return;
        }
        throw new IllegalStateException("Current DOM Node type  is " + ((int) this.currentNode.getNodeType()) + "and does not allow attributes to be set ");
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeCData(String data) throws XMLStreamException, DOMException {
        if (data == null) {
            throw new XMLStreamException("CDATA cannot be null");
        }
        CDATASection cdata = this.ownerDoc.createCDATASection(data);
        getNode().appendChild(cdata);
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeCharacters(String charData) throws DOMException, XMLStreamException {
        Text text = this.ownerDoc.createTextNode(charData);
        this.currentNode.appendChild(text);
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeCharacters(char[] values, int param, int param2) throws DOMException, XMLStreamException {
        Text text = this.ownerDoc.createTextNode(new String(values, param, param2));
        this.currentNode.appendChild(text);
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeComment(String str) throws DOMException, XMLStreamException {
        Comment comment = this.ownerDoc.createComment(str);
        getNode().appendChild(comment);
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeDTD(String str) throws XMLStreamException {
        throw new UnsupportedOperationException();
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeDefaultNamespace(String namespaceURI) throws DOMException, XMLStreamException {
        if (this.currentNode.getNodeType() == 1) {
            ((Element) this.currentNode).setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns", namespaceURI);
            return;
        }
        throw new IllegalStateException("Current DOM Node type  is " + ((int) this.currentNode.getNodeType()) + "and does not allow attributes to be set ");
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeEmptyElement(String localName) throws DOMException, XMLStreamException {
        if (this.ownerDoc != null) {
            Element element = this.ownerDoc.createElement(localName);
            if (this.currentNode != null) {
                this.currentNode.appendChild(element);
            } else {
                this.ownerDoc.appendChild(element);
            }
        }
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeEmptyElement(String namespaceURI, String localName) throws XMLStreamException, DOMException {
        String qualifiedName;
        if (this.ownerDoc != null) {
            String prefix = null;
            if (namespaceURI == null) {
                throw new XMLStreamException("NamespaceURI cannot be null");
            }
            if (localName == null) {
                throw new XMLStreamException("Local name cannot be null");
            }
            if (this.namespaceContext != null) {
                prefix = this.namespaceContext.getPrefix(namespaceURI);
            }
            if (prefix == null) {
                throw new XMLStreamException("Namespace URI " + namespaceURI + "is not bound to any prefix");
            }
            if ("".equals(prefix)) {
                qualifiedName = localName;
            } else {
                qualifiedName = getQName(prefix, localName);
            }
            Element element = this.ownerDoc.createElementNS(namespaceURI, qualifiedName);
            if (this.currentNode != null) {
                this.currentNode.appendChild(element);
            } else {
                this.ownerDoc.appendChild(element);
            }
        }
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeEmptyElement(String prefix, String localName, String namespaceURI) throws XMLStreamException, DOMException {
        String qualifiedName;
        if (this.ownerDoc != null) {
            if (namespaceURI == null) {
                throw new XMLStreamException("NamespaceURI cannot be null");
            }
            if (localName == null) {
                throw new XMLStreamException("Local name cannot be null");
            }
            if (prefix == null) {
                throw new XMLStreamException("Prefix cannot be null");
            }
            if ("".equals(prefix)) {
                qualifiedName = localName;
            } else {
                qualifiedName = getQName(prefix, localName);
            }
            Element el = this.ownerDoc.createElementNS(namespaceURI, qualifiedName);
            if (this.currentNode != null) {
                this.currentNode.appendChild(el);
            } else {
                this.ownerDoc.appendChild(el);
            }
        }
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeEndDocument() throws XMLStreamException {
        this.currentNode = null;
        for (int i2 = 0; i2 < this.depth; i2++) {
            if (this.needContextPop[this.depth]) {
                this.needContextPop[this.depth] = false;
                this.namespaceContext.popContext();
            }
            this.depth--;
        }
        this.depth = 0;
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeEndElement() throws XMLStreamException {
        Node node = this.currentNode.getParentNode();
        if (this.currentNode.getNodeType() == 9) {
            this.currentNode = null;
        } else {
            this.currentNode = node;
        }
        if (this.needContextPop[this.depth]) {
            this.needContextPop[this.depth] = false;
            this.namespaceContext.popContext();
        }
        this.depth--;
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeEntityRef(String name) throws DOMException, XMLStreamException {
        EntityReference er = this.ownerDoc.createEntityReference(name);
        this.currentNode.appendChild(er);
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeNamespace(String prefix, String namespaceURI) throws XMLStreamException, DOMException {
        String qname;
        if (prefix == null) {
            throw new XMLStreamException("prefix cannot be null");
        }
        if (namespaceURI == null) {
            throw new XMLStreamException("NamespaceURI cannot be null");
        }
        if (prefix.equals("")) {
            qname = "xmlns";
        } else {
            qname = getQName("xmlns", prefix);
        }
        ((Element) this.currentNode).setAttributeNS("http://www.w3.org/2000/xmlns/", qname, namespaceURI);
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeProcessingInstruction(String target) throws XMLStreamException, DOMException {
        if (target == null) {
            throw new XMLStreamException("Target cannot be null");
        }
        ProcessingInstruction pi = this.ownerDoc.createProcessingInstruction(target, "");
        this.currentNode.appendChild(pi);
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeProcessingInstruction(String target, String data) throws XMLStreamException, DOMException {
        if (target == null) {
            throw new XMLStreamException("Target cannot be null");
        }
        ProcessingInstruction pi = this.ownerDoc.createProcessingInstruction(target, data);
        this.currentNode.appendChild(pi);
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeStartDocument() throws XMLStreamException, IllegalArgumentException {
        try {
            if (this.mXmlVersion != null) {
                this.mXmlVersion.invoke(this.ownerDoc, "1.0");
            }
        } catch (IllegalAccessException iae) {
            throw new XMLStreamException(iae);
        } catch (InvocationTargetException ite) {
            throw new XMLStreamException(ite);
        }
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeStartDocument(String version) throws XMLStreamException, IllegalArgumentException {
        try {
            if (this.mXmlVersion != null) {
                this.mXmlVersion.invoke(this.ownerDoc, version);
            }
        } catch (IllegalAccessException iae) {
            throw new XMLStreamException(iae);
        } catch (InvocationTargetException ite) {
            throw new XMLStreamException(ite);
        }
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeStartDocument(String encoding, String version) throws XMLStreamException, IllegalArgumentException {
        try {
            if (this.mXmlVersion != null) {
                this.mXmlVersion.invoke(this.ownerDoc, version);
            }
        } catch (IllegalAccessException iae) {
            throw new XMLStreamException(iae);
        } catch (InvocationTargetException ite) {
            throw new XMLStreamException(ite);
        }
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeStartElement(String localName) throws DOMException, XMLStreamException {
        if (this.ownerDoc != null) {
            Element element = this.ownerDoc.createElement(localName);
            if (this.currentNode != null) {
                this.currentNode.appendChild(element);
            } else {
                this.ownerDoc.appendChild(element);
            }
            this.currentNode = element;
        }
        if (this.needContextPop[this.depth]) {
            this.namespaceContext.pushContext();
        }
        incDepth();
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeStartElement(String namespaceURI, String localName) throws XMLStreamException, DOMException {
        String qualifiedName;
        if (this.ownerDoc != null) {
            String prefix = null;
            if (namespaceURI == null) {
                throw new XMLStreamException("NamespaceURI cannot be null");
            }
            if (localName == null) {
                throw new XMLStreamException("Local name cannot be null");
            }
            if (this.namespaceContext != null) {
                prefix = this.namespaceContext.getPrefix(namespaceURI);
            }
            if (prefix == null) {
                throw new XMLStreamException("Namespace URI " + namespaceURI + "is not bound to any prefix");
            }
            if ("".equals(prefix)) {
                qualifiedName = localName;
            } else {
                qualifiedName = getQName(prefix, localName);
            }
            Element element = this.ownerDoc.createElementNS(namespaceURI, qualifiedName);
            if (this.currentNode != null) {
                this.currentNode.appendChild(element);
            } else {
                this.ownerDoc.appendChild(element);
            }
            this.currentNode = element;
        }
        if (this.needContextPop[this.depth]) {
            this.namespaceContext.pushContext();
        }
        incDepth();
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeStartElement(String prefix, String localName, String namespaceURI) throws XMLStreamException, DOMException {
        String qname;
        if (this.ownerDoc != null) {
            if (namespaceURI == null) {
                throw new XMLStreamException("NamespaceURI cannot be null");
            }
            if (localName == null) {
                throw new XMLStreamException("Local name cannot be null");
            }
            if (prefix == null) {
                throw new XMLStreamException("Prefix cannot be null");
            }
            if (prefix.equals("")) {
                qname = localName;
            } else {
                qname = getQName(prefix, localName);
            }
            Element el = this.ownerDoc.createElementNS(namespaceURI, qname);
            if (this.currentNode != null) {
                this.currentNode.appendChild(el);
            } else {
                this.ownerDoc.appendChild(el);
            }
            this.currentNode = el;
            if (this.needContextPop[this.depth]) {
                this.namespaceContext.pushContext();
            }
            incDepth();
        }
    }

    private String getQName(String prefix, String localName) {
        this.stringBuffer.setLength(0);
        this.stringBuffer.append(prefix);
        this.stringBuffer.append(CallSiteDescriptor.TOKEN_DELIMITER);
        this.stringBuffer.append(localName);
        return this.stringBuffer.toString();
    }

    private Node getNode() {
        if (this.currentNode == null) {
            return this.ownerDoc;
        }
        return this.currentNode;
    }

    private void incDepth() {
        this.depth++;
        if (this.depth == this.needContextPop.length) {
            boolean[] array = new boolean[this.depth + this.resizeValue];
            System.arraycopy(this.needContextPop, 0, array, 0, this.depth);
            this.needContextPop = array;
        }
    }
}
