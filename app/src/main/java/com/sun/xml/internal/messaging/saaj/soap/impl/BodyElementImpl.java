package com.sun.xml.internal.messaging.saaj.soap.impl;

import com.sun.xml.internal.messaging.saaj.soap.SOAPDocumentImpl;
import javax.xml.namespace.QName;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import org.w3c.dom.DOMException;

/* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/soap/impl/BodyElementImpl.class */
public abstract class BodyElementImpl extends ElementImpl implements SOAPBodyElement {
    public BodyElementImpl(SOAPDocumentImpl ownerDoc, Name qname) {
        super(ownerDoc, qname);
    }

    public BodyElementImpl(SOAPDocumentImpl ownerDoc, QName qname) {
        super(ownerDoc, qname);
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.ElementImpl, javax.xml.soap.Node
    public void setParentElement(SOAPElement element) throws SOAPException, DOMException {
        if (!(element instanceof SOAPBody)) {
            log.severe("SAAJ0101.impl.parent.of.body.elem.mustbe.body");
            throw new SOAPException("Parent of a SOAPBodyElement has to be a SOAPBody");
        }
        super.setParentElement(element);
    }
}
