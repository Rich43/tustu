package com.sun.xml.internal.messaging.saaj.soap.ver1_2;

import com.sun.xml.internal.messaging.saaj.SOAPExceptionImpl;
import com.sun.xml.internal.messaging.saaj.soap.SOAPDocument;
import com.sun.xml.internal.messaging.saaj.soap.SOAPDocumentImpl;
import com.sun.xml.internal.messaging.saaj.soap.impl.HeaderImpl;
import com.sun.xml.internal.messaging.saaj.soap.name.NameImpl;
import com.sun.xml.internal.messaging.saaj.util.LogDomainConstants;
import com.sun.xml.internal.ws.encoding.soap.streaming.SOAP12NamespaceConstants;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeaderElement;

/* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/soap/ver1_2/Header1_2Impl.class */
public class Header1_2Impl extends HeaderImpl {
    protected static final Logger log = Logger.getLogger(LogDomainConstants.SOAP_VER1_2_DOMAIN, "com.sun.xml.internal.messaging.saaj.soap.ver1_2.LocalStrings");

    public Header1_2Impl(SOAPDocumentImpl ownerDocument, String prefix) {
        super(ownerDocument, NameImpl.createHeader1_2Name(prefix));
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.HeaderImpl
    protected NameImpl getNotUnderstoodName() {
        return NameImpl.createNotUnderstood1_2Name(null);
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.HeaderImpl
    protected NameImpl getUpgradeName() {
        return NameImpl.createUpgrade1_2Name(null);
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.HeaderImpl
    protected NameImpl getSupportedEnvelopeName() {
        return NameImpl.createSupportedEnvelope1_2Name(null);
    }

    @Override // javax.xml.soap.SOAPHeader
    public SOAPHeaderElement addNotUnderstoodHeaderElement(QName sourceName) throws SOAPException {
        if (sourceName == null) {
            log.severe("SAAJ0410.ver1_2.no.null.to.addNotUnderstoodHeader");
            throw new SOAPException("Cannot pass NULL to addNotUnderstoodHeaderElement");
        }
        if ("".equals(sourceName.getNamespaceURI())) {
            log.severe("SAAJ0417.ver1_2.qname.not.ns.qualified");
            throw new SOAPException("The qname passed to addNotUnderstoodHeaderElement must be namespace-qualified");
        }
        String prefix = sourceName.getPrefix();
        if ("".equals(prefix)) {
            prefix = "ns1";
        }
        Name notunderstoodName = getNotUnderstoodName();
        SOAPHeaderElement notunderstoodHeaderElement = (SOAPHeaderElement) addChildElement(notunderstoodName);
        notunderstoodHeaderElement.addAttribute(NameImpl.createFromUnqualifiedName(SOAP12NamespaceConstants.ATTR_NOT_UNDERSTOOD_QNAME), getQualifiedName(new QName(sourceName.getNamespaceURI(), sourceName.getLocalPart(), prefix)));
        notunderstoodHeaderElement.addNamespaceDeclaration(prefix, sourceName.getNamespaceURI());
        return notunderstoodHeaderElement;
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.ElementImpl, javax.xml.soap.SOAPElement
    public SOAPElement addTextNode(String text) throws SOAPException {
        log.log(Level.SEVERE, "SAAJ0416.ver1_2.adding.text.not.legal", getElementQName());
        throw new SOAPExceptionImpl("Adding text to SOAP 1.2 Header is not legal");
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.HeaderImpl
    protected SOAPHeaderElement createHeaderElement(Name name) throws SOAPException {
        String uri = name.getURI();
        if (uri == null || uri.equals("")) {
            log.severe("SAAJ0413.ver1_2.header.elems.must.be.ns.qualified");
            throw new SOAPExceptionImpl("SOAP 1.2 header elements must be namespace qualified");
        }
        return new HeaderElement1_2Impl(((SOAPDocument) getOwnerDocument()).getDocument(), name);
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.HeaderImpl
    protected SOAPHeaderElement createHeaderElement(QName name) throws SOAPException {
        String uri = name.getNamespaceURI();
        if (uri == null || uri.equals("")) {
            log.severe("SAAJ0413.ver1_2.header.elems.must.be.ns.qualified");
            throw new SOAPExceptionImpl("SOAP 1.2 header elements must be namespace qualified");
        }
        return new HeaderElement1_2Impl(((SOAPDocument) getOwnerDocument()).getDocument(), name);
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.ElementImpl, javax.xml.soap.SOAPElement
    public void setEncodingStyle(String encodingStyle) throws SOAPException {
        log.severe("SAAJ0409.ver1_2.no.encodingstyle.in.header");
        throw new SOAPExceptionImpl("encodingStyle attribute cannot appear on Header");
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
