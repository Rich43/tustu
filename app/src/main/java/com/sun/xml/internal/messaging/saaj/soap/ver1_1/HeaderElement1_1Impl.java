package com.sun.xml.internal.messaging.saaj.soap.ver1_1;

import com.sun.xml.internal.messaging.saaj.soap.SOAPDocumentImpl;
import com.sun.xml.internal.messaging.saaj.soap.impl.HeaderElementImpl;
import com.sun.xml.internal.messaging.saaj.soap.name.NameImpl;
import com.sun.xml.internal.messaging.saaj.util.LogDomainConstants;
import com.sun.xml.internal.ws.encoding.soap.streaming.SOAPNamespaceConstants;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;

/* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/soap/ver1_1/HeaderElement1_1Impl.class */
public class HeaderElement1_1Impl extends HeaderElementImpl {
    protected static final Logger log = Logger.getLogger(LogDomainConstants.SOAP_VER1_1_DOMAIN, "com.sun.xml.internal.messaging.saaj.soap.ver1_1.LocalStrings");

    public HeaderElement1_1Impl(SOAPDocumentImpl ownerDoc, Name qname) {
        super(ownerDoc, qname);
    }

    public HeaderElement1_1Impl(SOAPDocumentImpl ownerDoc, QName qname) {
        super(ownerDoc, qname);
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.ElementImpl, javax.xml.soap.SOAPElement
    public SOAPElement setElementQName(QName newName) throws SOAPException {
        HeaderElementImpl copy = new HeaderElement1_1Impl((SOAPDocumentImpl) getOwnerDocument(), newName);
        return replaceElementWithSOAPElement(this, copy);
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.HeaderElementImpl
    protected NameImpl getActorAttributeName() {
        return NameImpl.create(SOAPNamespaceConstants.ATTR_ACTOR, null, "http://schemas.xmlsoap.org/soap/envelope/");
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.HeaderElementImpl
    protected NameImpl getRoleAttributeName() {
        log.log(Level.SEVERE, "SAAJ0302.ver1_1.hdr.attr.unsupported.in.SOAP1.1", (Object[]) new String[]{"Role"});
        throw new UnsupportedOperationException("Role not supported by SOAP 1.1");
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.HeaderElementImpl
    protected NameImpl getMustunderstandAttributeName() {
        return NameImpl.create("mustUnderstand", null, "http://schemas.xmlsoap.org/soap/envelope/");
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.HeaderElementImpl
    protected String getMustunderstandLiteralValue(boolean mustUnderstand) {
        return mustUnderstand ? "1" : "0";
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.HeaderElementImpl
    protected boolean getMustunderstandAttributeValue(String mu) {
        if ("1".equals(mu) || "true".equalsIgnoreCase(mu)) {
            return true;
        }
        return false;
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.HeaderElementImpl
    protected NameImpl getRelayAttributeName() {
        log.log(Level.SEVERE, "SAAJ0302.ver1_1.hdr.attr.unsupported.in.SOAP1.1", (Object[]) new String[]{"Relay"});
        throw new UnsupportedOperationException("Relay not supported by SOAP 1.1");
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.HeaderElementImpl
    protected String getRelayLiteralValue(boolean relayAttr) {
        log.log(Level.SEVERE, "SAAJ0302.ver1_1.hdr.attr.unsupported.in.SOAP1.1", (Object[]) new String[]{"Relay"});
        throw new UnsupportedOperationException("Relay not supported by SOAP 1.1");
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.HeaderElementImpl
    protected boolean getRelayAttributeValue(String mu) {
        log.log(Level.SEVERE, "SAAJ0302.ver1_1.hdr.attr.unsupported.in.SOAP1.1", (Object[]) new String[]{"Relay"});
        throw new UnsupportedOperationException("Relay not supported by SOAP 1.1");
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.HeaderElementImpl
    protected String getActorOrRole() {
        return getActor();
    }
}
