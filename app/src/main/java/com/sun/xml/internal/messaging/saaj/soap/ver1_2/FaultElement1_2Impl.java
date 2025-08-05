package com.sun.xml.internal.messaging.saaj.soap.ver1_2;

import com.sun.xml.internal.messaging.saaj.SOAPExceptionImpl;
import com.sun.xml.internal.messaging.saaj.soap.SOAPDocumentImpl;
import com.sun.xml.internal.messaging.saaj.soap.impl.FaultElementImpl;
import com.sun.xml.internal.messaging.saaj.soap.name.NameImpl;
import com.sun.xml.internal.ws.encoding.soap.streaming.SOAP12NamespaceConstants;
import javax.xml.namespace.QName;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import sun.security.x509.CRLReasonCodeExtension;

/* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/soap/ver1_2/FaultElement1_2Impl.class */
public class FaultElement1_2Impl extends FaultElementImpl {
    public FaultElement1_2Impl(SOAPDocumentImpl ownerDoc, NameImpl qname) {
        super(ownerDoc, qname);
    }

    public FaultElement1_2Impl(SOAPDocumentImpl ownerDoc, QName qname) {
        super(ownerDoc, qname);
    }

    public FaultElement1_2Impl(SOAPDocumentImpl ownerDoc, String localName) {
        super(ownerDoc, NameImpl.createSOAP12Name(localName));
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.FaultElementImpl
    protected boolean isStandardFaultElement() {
        String localName = this.elementQName.getLocalPart();
        if (localName.equalsIgnoreCase("code") || localName.equalsIgnoreCase(CRLReasonCodeExtension.REASON) || localName.equalsIgnoreCase("node") || localName.equalsIgnoreCase(SOAP12NamespaceConstants.ATTR_ACTOR)) {
            return true;
        }
        return false;
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.FaultElementImpl, com.sun.xml.internal.messaging.saaj.soap.impl.ElementImpl, javax.xml.soap.SOAPElement
    public SOAPElement setElementQName(QName newName) throws SOAPException {
        if (!isStandardFaultElement()) {
            FaultElement1_2Impl copy = new FaultElement1_2Impl((SOAPDocumentImpl) getOwnerDocument(), newName);
            return replaceElementWithSOAPElement(this, copy);
        }
        return super.setElementQName(newName);
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.ElementImpl, javax.xml.soap.SOAPElement
    public void setEncodingStyle(String encodingStyle) throws SOAPException {
        log.severe("SAAJ0408.ver1_2.no.encodingStyle.in.fault.child");
        throw new SOAPExceptionImpl("encodingStyle attribute cannot appear on a Fault child element");
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.ElementImpl, javax.xml.soap.SOAPElement
    public SOAPElement addAttribute(Name name, String value) throws SOAPException {
        if (name.getLocalName().equals("encodingStyle") && name.getURI().equals("http://www.w3.org/2003/05/soap-envelope")) {
            setEncodingStyle(value);
        }
        return super.addAttribute(name, value);
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.ElementImpl, javax.xml.soap.SOAPElement
    public SOAPElement addAttribute(QName name, String value) throws SOAPException {
        if (name.getLocalPart().equals("encodingStyle") && name.getNamespaceURI().equals("http://www.w3.org/2003/05/soap-envelope")) {
            setEncodingStyle(value);
        }
        return super.addAttribute(name, value);
    }
}
