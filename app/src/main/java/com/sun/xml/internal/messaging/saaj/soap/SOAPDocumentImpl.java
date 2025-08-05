package com.sun.xml.internal.messaging.saaj.soap;

import com.sun.org.apache.xerces.internal.dom.CoreDocumentImpl;
import com.sun.org.apache.xerces.internal.dom.DocumentImpl;
import com.sun.xml.internal.messaging.saaj.soap.impl.CDATAImpl;
import com.sun.xml.internal.messaging.saaj.soap.impl.CommentImpl;
import com.sun.xml.internal.messaging.saaj.soap.impl.ElementFactory;
import com.sun.xml.internal.messaging.saaj.soap.impl.ElementImpl;
import com.sun.xml.internal.messaging.saaj.soap.impl.TextImpl;
import com.sun.xml.internal.messaging.saaj.soap.name.NameImpl;
import com.sun.xml.internal.messaging.saaj.util.LogDomainConstants;
import java.util.logging.Logger;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;

/* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/soap/SOAPDocumentImpl.class */
public class SOAPDocumentImpl extends DocumentImpl implements SOAPDocument {
    private static final String XMLNS = "xmlns".intern();
    protected static final Logger log = Logger.getLogger(LogDomainConstants.SOAP_DOMAIN, "com.sun.xml.internal.messaging.saaj.soap.LocalStrings");
    SOAPPartImpl enclosingSOAPPart;

    public SOAPDocumentImpl(SOAPPartImpl enclosingDocument) {
        this.enclosingSOAPPart = enclosingDocument;
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.SOAPDocument
    public SOAPPartImpl getSOAPPart() {
        if (this.enclosingSOAPPart == null) {
            log.severe("SAAJ0541.soap.fragment.not.bound.to.part");
            throw new RuntimeException("Could not complete operation. Fragment not bound to SOAP part.");
        }
        return this.enclosingSOAPPart;
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.SOAPDocument
    public SOAPDocumentImpl getDocument() {
        return this;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.CoreDocumentImpl, org.w3c.dom.Document
    public DocumentType getDoctype() {
        return null;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.DocumentImpl, com.sun.org.apache.xerces.internal.dom.CoreDocumentImpl, org.w3c.dom.Document
    public DOMImplementation getImplementation() {
        return super.getImplementation();
    }

    @Override // com.sun.org.apache.xerces.internal.dom.CoreDocumentImpl, org.w3c.dom.Document
    public Element getDocumentElement() {
        getSOAPPart().doGetDocumentElement();
        return doGetDocumentElement();
    }

    protected Element doGetDocumentElement() {
        return super.getDocumentElement();
    }

    @Override // com.sun.org.apache.xerces.internal.dom.CoreDocumentImpl, org.w3c.dom.Document
    public Element createElement(String tagName) throws DOMException {
        return ElementFactory.createElement(this, NameImpl.getLocalNameFromTagName(tagName), NameImpl.getPrefixFromTagName(tagName), null);
    }

    @Override // com.sun.org.apache.xerces.internal.dom.CoreDocumentImpl, org.w3c.dom.Document
    public DocumentFragment createDocumentFragment() {
        return new SOAPDocumentFragment(this);
    }

    @Override // com.sun.org.apache.xerces.internal.dom.CoreDocumentImpl, org.w3c.dom.Document
    public Text createTextNode(String data) {
        return new TextImpl(this, data);
    }

    @Override // com.sun.org.apache.xerces.internal.dom.CoreDocumentImpl, org.w3c.dom.Document
    public Comment createComment(String data) {
        return new CommentImpl(this, data);
    }

    @Override // com.sun.org.apache.xerces.internal.dom.CoreDocumentImpl, org.w3c.dom.Document
    public CDATASection createCDATASection(String data) throws DOMException {
        return new CDATAImpl(this, data);
    }

    @Override // com.sun.org.apache.xerces.internal.dom.CoreDocumentImpl, org.w3c.dom.Document
    public ProcessingInstruction createProcessingInstruction(String target, String data) throws DOMException {
        log.severe("SAAJ0542.soap.proc.instructions.not.allowed.in.docs");
        throw new UnsupportedOperationException("Processing Instructions are not allowed in SOAP documents");
    }

    @Override // com.sun.org.apache.xerces.internal.dom.CoreDocumentImpl, org.w3c.dom.Document
    public Attr createAttribute(String name) throws DOMException {
        boolean isQualifiedName = name.indexOf(CallSiteDescriptor.TOKEN_DELIMITER) > 0;
        if (isQualifiedName) {
            String prefix = name.substring(0, name.indexOf(CallSiteDescriptor.TOKEN_DELIMITER));
            if (XMLNS.equals(prefix)) {
                String nsUri = ElementImpl.XMLNS_URI;
                return createAttributeNS(nsUri, name);
            }
        }
        return super.createAttribute(name);
    }

    @Override // com.sun.org.apache.xerces.internal.dom.CoreDocumentImpl, org.w3c.dom.Document
    public EntityReference createEntityReference(String name) throws DOMException {
        log.severe("SAAJ0543.soap.entity.refs.not.allowed.in.docs");
        throw new UnsupportedOperationException("Entity References are not allowed in SOAP documents");
    }

    @Override // com.sun.org.apache.xerces.internal.dom.CoreDocumentImpl, org.w3c.dom.Document
    public NodeList getElementsByTagName(String tagname) {
        return super.getElementsByTagName(tagname);
    }

    @Override // com.sun.org.apache.xerces.internal.dom.CoreDocumentImpl, org.w3c.dom.Document
    public Node importNode(Node importedNode, boolean deep) throws DOMException {
        return super.importNode(importedNode, deep);
    }

    @Override // com.sun.org.apache.xerces.internal.dom.CoreDocumentImpl, org.w3c.dom.Document
    public Element createElementNS(String namespaceURI, String qualifiedName) throws DOMException {
        return ElementFactory.createElement(this, NameImpl.getLocalNameFromTagName(qualifiedName), NameImpl.getPrefixFromTagName(qualifiedName), namespaceURI);
    }

    @Override // com.sun.org.apache.xerces.internal.dom.CoreDocumentImpl, org.w3c.dom.Document
    public Attr createAttributeNS(String namespaceURI, String qualifiedName) throws DOMException {
        return super.createAttributeNS(namespaceURI, qualifiedName);
    }

    @Override // com.sun.org.apache.xerces.internal.dom.CoreDocumentImpl, org.w3c.dom.Document
    public NodeList getElementsByTagNameNS(String namespaceURI, String localName) {
        return super.getElementsByTagNameNS(namespaceURI, localName);
    }

    @Override // com.sun.org.apache.xerces.internal.dom.CoreDocumentImpl, org.w3c.dom.Document
    public Element getElementById(String elementId) {
        return super.getElementById(elementId);
    }

    @Override // com.sun.org.apache.xerces.internal.dom.DocumentImpl, com.sun.org.apache.xerces.internal.dom.CoreDocumentImpl, com.sun.org.apache.xerces.internal.dom.ParentNode, com.sun.org.apache.xerces.internal.dom.ChildNode, com.sun.org.apache.xerces.internal.dom.NodeImpl, org.w3c.dom.Node
    public Node cloneNode(boolean deep) {
        SOAPPartImpl newSoapPart = getSOAPPart().doCloneNode();
        super.cloneNode((CoreDocumentImpl) newSoapPart.getDocument(), deep);
        return newSoapPart;
    }

    public void cloneNode(SOAPDocumentImpl newdoc, boolean deep) {
        super.cloneNode((CoreDocumentImpl) newdoc, deep);
    }
}
