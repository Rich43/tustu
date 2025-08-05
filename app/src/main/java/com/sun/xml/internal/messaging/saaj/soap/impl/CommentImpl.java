package com.sun.xml.internal.messaging.saaj.soap.impl;

import com.sun.xml.internal.messaging.saaj.soap.SOAPDocumentImpl;
import com.sun.xml.internal.messaging.saaj.util.LogDomainConstants;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.Text;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

/* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/soap/impl/CommentImpl.class */
public class CommentImpl extends com.sun.org.apache.xerces.internal.dom.CommentImpl implements Text, Comment {
    protected static final Logger log = Logger.getLogger(LogDomainConstants.SOAP_IMPL_DOMAIN, "com.sun.xml.internal.messaging.saaj.soap.impl.LocalStrings");
    protected static ResourceBundle rb = log.getResourceBundle();

    public CommentImpl(SOAPDocumentImpl ownerDoc, String text) {
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
    public void setParentElement(SOAPElement element) throws SOAPException {
        if (element == null) {
            log.severe("SAAJ0112.impl.no.null.to.parent.elem");
            throw new SOAPException("Cannot pass NULL to setParentElement");
        }
        ((ElementImpl) element).addNode(this);
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
        return true;
    }

    @Override // org.w3c.dom.Text
    public org.w3c.dom.Text splitText(int offset) throws DOMException {
        log.severe("SAAJ0113.impl.cannot.split.text.from.comment");
        throw new UnsupportedOperationException("Cannot split text from a Comment Node.");
    }

    @Override // org.w3c.dom.Text
    public org.w3c.dom.Text replaceWholeText(String content) throws DOMException {
        log.severe("SAAJ0114.impl.cannot.replace.wholetext.from.comment");
        throw new UnsupportedOperationException("Cannot replace Whole Text from a Comment Node.");
    }

    @Override // org.w3c.dom.Text
    public String getWholeText() {
        throw new UnsupportedOperationException("Not Supported");
    }

    @Override // org.w3c.dom.Text
    public boolean isElementContentWhitespace() {
        throw new UnsupportedOperationException("Not Supported");
    }
}
