package com.sun.xml.internal.messaging.saaj.soap.ver1_2;

import com.sun.xml.internal.messaging.saaj.soap.SOAPDocumentImpl;
import com.sun.xml.internal.messaging.saaj.soap.impl.HeaderElementImpl;
import com.sun.xml.internal.messaging.saaj.soap.name.NameImpl;
import com.sun.xml.internal.ws.encoding.soap.streaming.SOAP12NamespaceConstants;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;

/* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/soap/ver1_2/HeaderElement1_2Impl.class */
public class HeaderElement1_2Impl extends HeaderElementImpl {
    private static final Logger log = Logger.getLogger(HeaderElement1_2Impl.class.getName(), "com.sun.xml.internal.messaging.saaj.soap.ver1_2.LocalStrings");

    public HeaderElement1_2Impl(SOAPDocumentImpl ownerDoc, Name qname) {
        super(ownerDoc, qname);
    }

    public HeaderElement1_2Impl(SOAPDocumentImpl ownerDoc, QName qname) {
        super(ownerDoc, qname);
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.ElementImpl, javax.xml.soap.SOAPElement
    public SOAPElement setElementQName(QName newName) throws SOAPException {
        HeaderElementImpl copy = new HeaderElement1_2Impl((SOAPDocumentImpl) getOwnerDocument(), newName);
        return replaceElementWithSOAPElement(this, copy);
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.HeaderElementImpl
    protected NameImpl getRoleAttributeName() {
        return NameImpl.create(SOAP12NamespaceConstants.ATTR_ACTOR, null, "http://www.w3.org/2003/05/soap-envelope");
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.HeaderElementImpl
    protected NameImpl getActorAttributeName() {
        return getRoleAttributeName();
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.HeaderElementImpl
    protected NameImpl getMustunderstandAttributeName() {
        return NameImpl.create("mustUnderstand", null, "http://www.w3.org/2003/05/soap-envelope");
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.HeaderElementImpl
    protected String getMustunderstandLiteralValue(boolean mustUnderstand) {
        return mustUnderstand ? "true" : "false";
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.HeaderElementImpl
    protected boolean getMustunderstandAttributeValue(String mu) {
        if (mu.equals("true") || mu.equals("1")) {
            return true;
        }
        return false;
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.HeaderElementImpl
    protected NameImpl getRelayAttributeName() {
        return NameImpl.create("relay", null, "http://www.w3.org/2003/05/soap-envelope");
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.HeaderElementImpl
    protected String getRelayLiteralValue(boolean relay) {
        return relay ? "true" : "false";
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.HeaderElementImpl
    protected boolean getRelayAttributeValue(String relay) {
        if (relay.equals("true") || relay.equals("1")) {
            return true;
        }
        return false;
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.HeaderElementImpl
    protected String getActorOrRole() {
        return getRole();
    }
}
