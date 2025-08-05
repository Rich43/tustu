package com.sun.xml.internal.messaging.saaj.soap.impl;

import com.sun.xml.internal.messaging.saaj.soap.SOAPDocumentImpl;
import com.sun.xml.internal.messaging.saaj.soap.name.NameImpl;
import com.sun.xml.internal.ws.encoding.soap.streaming.SOAP12NamespaceConstants;
import com.sun.xml.internal.ws.encoding.soap.streaming.SOAPNamespaceConstants;
import javax.xml.namespace.QName;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import org.w3c.dom.DOMException;

/* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/soap/impl/HeaderElementImpl.class */
public abstract class HeaderElementImpl extends ElementImpl implements SOAPHeaderElement {
    protected static Name RELAY_ATTRIBUTE_LOCAL_NAME = NameImpl.createFromTagName("relay");
    protected static Name MUST_UNDERSTAND_ATTRIBUTE_LOCAL_NAME = NameImpl.createFromTagName("mustUnderstand");
    Name actorAttNameWithoutNS;
    Name roleAttNameWithoutNS;

    protected abstract NameImpl getActorAttributeName();

    protected abstract NameImpl getRoleAttributeName();

    protected abstract NameImpl getMustunderstandAttributeName();

    protected abstract boolean getMustunderstandAttributeValue(String str);

    protected abstract String getMustunderstandLiteralValue(boolean z2);

    protected abstract NameImpl getRelayAttributeName();

    protected abstract boolean getRelayAttributeValue(String str);

    protected abstract String getRelayLiteralValue(boolean z2);

    protected abstract String getActorOrRole();

    public HeaderElementImpl(SOAPDocumentImpl ownerDoc, Name qname) {
        super(ownerDoc, qname);
        this.actorAttNameWithoutNS = NameImpl.createFromTagName(SOAPNamespaceConstants.ATTR_ACTOR);
        this.roleAttNameWithoutNS = NameImpl.createFromTagName(SOAP12NamespaceConstants.ATTR_ACTOR);
    }

    public HeaderElementImpl(SOAPDocumentImpl ownerDoc, QName qname) {
        super(ownerDoc, qname);
        this.actorAttNameWithoutNS = NameImpl.createFromTagName(SOAPNamespaceConstants.ATTR_ACTOR);
        this.roleAttNameWithoutNS = NameImpl.createFromTagName(SOAP12NamespaceConstants.ATTR_ACTOR);
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.ElementImpl, javax.xml.soap.Node
    public void setParentElement(SOAPElement element) throws SOAPException, DOMException {
        if (!(element instanceof SOAPHeader)) {
            log.severe("SAAJ0130.impl.header.elem.parent.mustbe.header");
            throw new SOAPException("Parent of a SOAPHeaderElement has to be a SOAPHeader");
        }
        super.setParentElement(element);
    }

    @Override // javax.xml.soap.SOAPHeaderElement
    public void setActor(String actorUri) {
        try {
            removeAttribute(getActorAttributeName());
            addAttribute(getActorAttributeName(), actorUri);
        } catch (SOAPException e2) {
        }
    }

    @Override // javax.xml.soap.SOAPHeaderElement
    public void setRole(String roleUri) throws SOAPException {
        removeAttribute(getRoleAttributeName());
        addAttribute(getRoleAttributeName(), roleUri);
    }

    @Override // javax.xml.soap.SOAPHeaderElement
    public String getActor() {
        String actor = getAttributeValue(getActorAttributeName());
        return actor;
    }

    @Override // javax.xml.soap.SOAPHeaderElement
    public String getRole() {
        String role = getAttributeValue(getRoleAttributeName());
        return role;
    }

    @Override // javax.xml.soap.SOAPHeaderElement
    public void setMustUnderstand(boolean mustUnderstand) {
        try {
            removeAttribute(getMustunderstandAttributeName());
            addAttribute(getMustunderstandAttributeName(), getMustunderstandLiteralValue(mustUnderstand));
        } catch (SOAPException e2) {
        }
    }

    @Override // javax.xml.soap.SOAPHeaderElement
    public boolean getMustUnderstand() {
        String mu = getAttributeValue(getMustunderstandAttributeName());
        if (mu != null) {
            return getMustunderstandAttributeValue(mu);
        }
        return false;
    }

    @Override // javax.xml.soap.SOAPHeaderElement
    public void setRelay(boolean relay) throws SOAPException {
        removeAttribute(getRelayAttributeName());
        addAttribute(getRelayAttributeName(), getRelayLiteralValue(relay));
    }

    @Override // javax.xml.soap.SOAPHeaderElement
    public boolean getRelay() {
        String mu = getAttributeValue(getRelayAttributeName());
        if (mu != null) {
            return getRelayAttributeValue(mu);
        }
        return false;
    }
}
