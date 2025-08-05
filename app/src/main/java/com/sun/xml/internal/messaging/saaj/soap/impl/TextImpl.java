package com.sun.xml.internal.messaging.saaj.soap.impl;

import com.sun.xml.internal.messaging.saaj.soap.SOAPDocumentImpl;
import com.sun.xml.internal.messaging.saaj.util.LogDomainConstants;
import java.util.logging.Logger;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.Text;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

/* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/soap/impl/TextImpl.class */
public class TextImpl extends com.sun.org.apache.xerces.internal.dom.TextImpl implements Text, org.w3c.dom.Text {
    protected static final Logger log = Logger.getLogger(LogDomainConstants.SOAP_IMPL_DOMAIN, "com.sun.xml.internal.messaging.saaj.soap.impl.LocalStrings");

    public TextImpl(SOAPDocumentImpl ownerDoc, String text) {
        super(ownerDoc, text);
    }

    @Override // javax.xml.soap.Node
    public String getValue() {
        String nodeValue = getNodeValue();
        if (nodeValue.equals("")) {
            return null;
        }
        return nodeValue;
    }

    @Override // javax.xml.soap.Node
    public void setValue(String text) {
        setNodeValue(text);
    }

    @Override // javax.xml.soap.Node
    public void setParentElement(SOAPElement parent) throws SOAPException {
        if (parent == null) {
            log.severe("SAAJ0126.impl.cannot.locate.ns");
            throw new SOAPException("Cannot pass NULL to setParentElement");
        }
        ((ElementImpl) parent).addNode(this);
    }

    @Override // javax.xml.soap.Node
    public SOAPElement getParentElement() {
        return (SOAPElement) getParentNode();
    }

    @Override // javax.xml.soap.Node
    public void detachNode() throws DOMException {
        Node parent = getParentNode();
        if (parent != null) {
            parent.removeChild(this);
        }
    }

    @Override // javax.xml.soap.Node
    public void recycleNode() throws DOMException {
        detachNode();
    }

    @Override // javax.xml.soap.Text
    public boolean isComment() {
        String txt = getNodeValue();
        return txt != null && txt.startsWith("<!--") && txt.endsWith("-->");
    }
}
