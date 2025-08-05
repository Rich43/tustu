package com.sun.org.apache.xerces.internal.dom;

import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/dom/PSVIDocumentImpl.class */
public class PSVIDocumentImpl extends DocumentImpl {
    static final long serialVersionUID = -8822220250676434522L;

    public PSVIDocumentImpl() {
    }

    public PSVIDocumentImpl(DocumentType doctype) {
        super(doctype);
    }

    @Override // com.sun.org.apache.xerces.internal.dom.DocumentImpl, com.sun.org.apache.xerces.internal.dom.CoreDocumentImpl, com.sun.org.apache.xerces.internal.dom.ParentNode, com.sun.org.apache.xerces.internal.dom.ChildNode, com.sun.org.apache.xerces.internal.dom.NodeImpl, org.w3c.dom.Node
    public Node cloneNode(boolean deep) {
        PSVIDocumentImpl newdoc = new PSVIDocumentImpl();
        callUserDataHandlers(this, newdoc, (short) 1);
        cloneNode(newdoc, deep);
        newdoc.mutationEvents = this.mutationEvents;
        return newdoc;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.DocumentImpl, com.sun.org.apache.xerces.internal.dom.CoreDocumentImpl, org.w3c.dom.Document
    public DOMImplementation getImplementation() {
        return PSVIDOMImplementationImpl.getDOMImplementation();
    }

    @Override // com.sun.org.apache.xerces.internal.dom.CoreDocumentImpl, org.w3c.dom.Document
    public Element createElementNS(String namespaceURI, String qualifiedName) throws DOMException {
        return new PSVIElementNSImpl(this, namespaceURI, qualifiedName);
    }

    @Override // com.sun.org.apache.xerces.internal.dom.CoreDocumentImpl
    public Element createElementNS(String namespaceURI, String qualifiedName, String localpart) throws DOMException {
        return new PSVIElementNSImpl(this, namespaceURI, qualifiedName, localpart);
    }

    @Override // com.sun.org.apache.xerces.internal.dom.CoreDocumentImpl, org.w3c.dom.Document
    public Attr createAttributeNS(String namespaceURI, String qualifiedName) throws DOMException {
        return new PSVIAttrNSImpl(this, namespaceURI, qualifiedName);
    }

    @Override // com.sun.org.apache.xerces.internal.dom.CoreDocumentImpl
    public Attr createAttributeNS(String namespaceURI, String qualifiedName, String localName) throws DOMException {
        return new PSVIAttrNSImpl(this, namespaceURI, qualifiedName, localName);
    }

    @Override // com.sun.org.apache.xerces.internal.dom.CoreDocumentImpl, org.w3c.dom.Document
    public DOMConfiguration getDomConfig() {
        super.getDomConfig();
        return this.fConfiguration;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        throw new NotSerializableException(getClass().getName());
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        throw new NotSerializableException(getClass().getName());
    }
}
