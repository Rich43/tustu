package com.sun.xml.internal.messaging.saaj.soap;

import com.sun.xml.internal.messaging.saaj.soap.impl.ElementFactory;
import com.sun.xml.internal.messaging.saaj.soap.name.NameImpl;
import com.sun.xml.internal.messaging.saaj.util.LogDomainConstants;
import com.sun.xml.internal.ws.encoding.soap.streaming.SOAP12NamespaceConstants;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.soap.Detail;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPFault;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/soap/SOAPFactoryImpl.class */
public abstract class SOAPFactoryImpl extends SOAPFactory {
    protected static final Logger log = Logger.getLogger(LogDomainConstants.SOAP_DOMAIN, "com.sun.xml.internal.messaging.saaj.soap.LocalStrings");

    protected abstract SOAPDocumentImpl createDocument();

    @Override // javax.xml.soap.SOAPFactory
    public SOAPElement createElement(String tagName) throws SOAPException {
        if (tagName == null) {
            log.log(Level.SEVERE, "SAAJ0567.soap.null.input", new Object[]{"tagName", "SOAPFactory.createElement"});
            throw new SOAPException("Null tagName argument passed to createElement");
        }
        return ElementFactory.createElement(createDocument(), NameImpl.createFromTagName(tagName));
    }

    @Override // javax.xml.soap.SOAPFactory
    public SOAPElement createElement(Name name) throws SOAPException {
        if (name == null) {
            log.log(Level.SEVERE, "SAAJ0567.soap.null.input", new Object[]{"name", "SOAPFactory.createElement"});
            throw new SOAPException("Null name argument passed to createElement");
        }
        return ElementFactory.createElement(createDocument(), name);
    }

    @Override // javax.xml.soap.SOAPFactory
    public SOAPElement createElement(QName qname) throws SOAPException {
        if (qname == null) {
            log.log(Level.SEVERE, "SAAJ0567.soap.null.input", new Object[]{SOAP12NamespaceConstants.ATTR_NOT_UNDERSTOOD_QNAME, "SOAPFactory.createElement"});
            throw new SOAPException("Null qname argument passed to createElement");
        }
        return ElementFactory.createElement(createDocument(), qname);
    }

    @Override // javax.xml.soap.SOAPFactory
    public SOAPElement createElement(String localName, String prefix, String uri) throws SOAPException {
        if (localName == null) {
            log.log(Level.SEVERE, "SAAJ0567.soap.null.input", new Object[]{"localName", "SOAPFactory.createElement"});
            throw new SOAPException("Null localName argument passed to createElement");
        }
        return ElementFactory.createElement(createDocument(), localName, prefix, uri);
    }

    @Override // javax.xml.soap.SOAPFactory
    public Name createName(String localName, String prefix, String uri) throws SOAPException {
        if (localName == null) {
            log.log(Level.SEVERE, "SAAJ0567.soap.null.input", new Object[]{"localName", "SOAPFactory.createName"});
            throw new SOAPException("Null localName argument passed to createName");
        }
        return NameImpl.create(localName, prefix, uri);
    }

    @Override // javax.xml.soap.SOAPFactory
    public Name createName(String localName) throws SOAPException {
        if (localName == null) {
            log.log(Level.SEVERE, "SAAJ0567.soap.null.input", new Object[]{"localName", "SOAPFactory.createName"});
            throw new SOAPException("Null localName argument passed to createName");
        }
        return NameImpl.createFromUnqualifiedName(localName);
    }

    @Override // javax.xml.soap.SOAPFactory
    public SOAPElement createElement(Element domElement) throws SOAPException {
        if (domElement == null) {
            return null;
        }
        return convertToSoapElement(domElement);
    }

    private SOAPElement convertToSoapElement(Element element) throws SOAPException, DOMException {
        if (element instanceof SOAPElement) {
            return (SOAPElement) element;
        }
        SOAPElement copy = createElement(element.getLocalName(), element.getPrefix(), element.getNamespaceURI());
        Document ownerDoc = copy.getOwnerDocument();
        NamedNodeMap attrMap = element.getAttributes();
        for (int i2 = 0; i2 < attrMap.getLength(); i2++) {
            Attr nextAttr = (Attr) attrMap.item(i2);
            Attr importedAttr = (Attr) ownerDoc.importNode(nextAttr, true);
            copy.setAttributeNodeNS(importedAttr);
        }
        NodeList nl = element.getChildNodes();
        for (int i3 = 0; i3 < nl.getLength(); i3++) {
            Node next = nl.item(i3);
            Node imported = ownerDoc.importNode(next, true);
            copy.appendChild(imported);
        }
        return copy;
    }

    @Override // javax.xml.soap.SOAPFactory
    public Detail createDetail() throws SOAPException {
        throw new UnsupportedOperationException();
    }

    @Override // javax.xml.soap.SOAPFactory
    public SOAPFault createFault(String reasonText, QName faultCode) throws SOAPException {
        throw new UnsupportedOperationException();
    }

    @Override // javax.xml.soap.SOAPFactory
    public SOAPFault createFault() throws SOAPException {
        throw new UnsupportedOperationException();
    }
}
