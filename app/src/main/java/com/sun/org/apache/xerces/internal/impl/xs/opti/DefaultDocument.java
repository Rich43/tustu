package com.sun.org.apache.xerces.internal.impl.xs.opti;

import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/opti/DefaultDocument.class */
public class DefaultDocument extends NodeImpl implements Document {
    private String fDocumentURI = null;

    @Override // org.w3c.dom.Document
    public DocumentType getDoctype() {
        return null;
    }

    @Override // org.w3c.dom.Document
    public DOMImplementation getImplementation() {
        return null;
    }

    @Override // org.w3c.dom.Document
    public Element getDocumentElement() {
        return null;
    }

    @Override // org.w3c.dom.Document
    public NodeList getElementsByTagName(String tagname) {
        return null;
    }

    @Override // org.w3c.dom.Document
    public NodeList getElementsByTagNameNS(String namespaceURI, String localName) {
        return null;
    }

    @Override // org.w3c.dom.Document
    public Element getElementById(String elementId) {
        return null;
    }

    @Override // org.w3c.dom.Document
    public Node importNode(Node importedNode, boolean deep) throws DOMException {
        throw new DOMException((short) 9, "Method not supported");
    }

    @Override // org.w3c.dom.Document
    public Element createElement(String tagName) throws DOMException {
        throw new DOMException((short) 9, "Method not supported");
    }

    @Override // org.w3c.dom.Document
    public DocumentFragment createDocumentFragment() {
        return null;
    }

    @Override // org.w3c.dom.Document
    public Text createTextNode(String data) {
        return null;
    }

    @Override // org.w3c.dom.Document
    public Comment createComment(String data) {
        return null;
    }

    @Override // org.w3c.dom.Document
    public CDATASection createCDATASection(String data) throws DOMException {
        throw new DOMException((short) 9, "Method not supported");
    }

    @Override // org.w3c.dom.Document
    public ProcessingInstruction createProcessingInstruction(String target, String data) throws DOMException {
        throw new DOMException((short) 9, "Method not supported");
    }

    @Override // org.w3c.dom.Document
    public Attr createAttribute(String name) throws DOMException {
        throw new DOMException((short) 9, "Method not supported");
    }

    @Override // org.w3c.dom.Document
    public EntityReference createEntityReference(String name) throws DOMException {
        throw new DOMException((short) 9, "Method not supported");
    }

    @Override // org.w3c.dom.Document
    public Element createElementNS(String namespaceURI, String qualifiedName) throws DOMException {
        throw new DOMException((short) 9, "Method not supported");
    }

    @Override // org.w3c.dom.Document
    public Attr createAttributeNS(String namespaceURI, String qualifiedName) throws DOMException {
        throw new DOMException((short) 9, "Method not supported");
    }

    @Override // org.w3c.dom.Document
    public String getInputEncoding() {
        return null;
    }

    @Override // org.w3c.dom.Document
    public String getXmlEncoding() {
        return null;
    }

    @Override // org.w3c.dom.Document
    public boolean getXmlStandalone() {
        throw new DOMException((short) 9, "Method not supported");
    }

    @Override // org.w3c.dom.Document
    public void setXmlStandalone(boolean standalone) {
        throw new DOMException((short) 9, "Method not supported");
    }

    @Override // org.w3c.dom.Document
    public String getXmlVersion() {
        return null;
    }

    @Override // org.w3c.dom.Document
    public void setXmlVersion(String version) throws DOMException {
        throw new DOMException((short) 9, "Method not supported");
    }

    @Override // org.w3c.dom.Document
    public boolean getStrictErrorChecking() {
        return false;
    }

    @Override // org.w3c.dom.Document
    public void setStrictErrorChecking(boolean strictErrorChecking) {
        throw new DOMException((short) 9, "Method not supported");
    }

    @Override // org.w3c.dom.Document
    public String getDocumentURI() {
        return this.fDocumentURI;
    }

    @Override // org.w3c.dom.Document
    public void setDocumentURI(String documentURI) {
        this.fDocumentURI = documentURI;
    }

    @Override // org.w3c.dom.Document
    public Node adoptNode(Node source) throws DOMException {
        throw new DOMException((short) 9, "Method not supported");
    }

    @Override // org.w3c.dom.Document
    public void normalizeDocument() {
        throw new DOMException((short) 9, "Method not supported");
    }

    @Override // org.w3c.dom.Document
    public DOMConfiguration getDomConfig() {
        throw new DOMException((short) 9, "Method not supported");
    }

    @Override // org.w3c.dom.Document
    public Node renameNode(Node n2, String namespaceURI, String name) throws DOMException {
        throw new DOMException((short) 9, "Method not supported");
    }
}
