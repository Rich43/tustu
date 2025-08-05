package com.sun.xml.internal.messaging.saaj.soap.ver1_1;

import com.sun.xml.internal.messaging.saaj.soap.SOAPDocumentImpl;
import com.sun.xml.internal.messaging.saaj.soap.impl.BodyElementImpl;
import javax.xml.namespace.QName;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;

/* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/soap/ver1_1/BodyElement1_1Impl.class */
public class BodyElement1_1Impl extends BodyElementImpl {
    public BodyElement1_1Impl(SOAPDocumentImpl ownerDoc, Name qname) {
        super(ownerDoc, qname);
    }

    public BodyElement1_1Impl(SOAPDocumentImpl ownerDoc, QName qname) {
        super(ownerDoc, qname);
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.ElementImpl, javax.xml.soap.SOAPElement
    public SOAPElement setElementQName(QName newName) throws SOAPException {
        BodyElementImpl copy = new BodyElement1_1Impl((SOAPDocumentImpl) getOwnerDocument(), newName);
        return replaceElementWithSOAPElement(this, copy);
    }
}
