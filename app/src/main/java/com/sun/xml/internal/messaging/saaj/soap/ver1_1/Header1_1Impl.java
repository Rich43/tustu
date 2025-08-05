package com.sun.xml.internal.messaging.saaj.soap.ver1_1;

import com.sun.xml.internal.messaging.saaj.soap.SOAPDocument;
import com.sun.xml.internal.messaging.saaj.soap.SOAPDocumentImpl;
import com.sun.xml.internal.messaging.saaj.soap.impl.HeaderImpl;
import com.sun.xml.internal.messaging.saaj.soap.name.NameImpl;
import com.sun.xml.internal.messaging.saaj.util.LogDomainConstants;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeaderElement;

/* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/soap/ver1_1/Header1_1Impl.class */
public class Header1_1Impl extends HeaderImpl {
    protected static final Logger log = Logger.getLogger(LogDomainConstants.SOAP_VER1_1_DOMAIN, "com.sun.xml.internal.messaging.saaj.soap.ver1_1.LocalStrings");

    public Header1_1Impl(SOAPDocumentImpl ownerDocument, String prefix) {
        super(ownerDocument, NameImpl.createHeader1_1Name(prefix));
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.HeaderImpl
    protected NameImpl getNotUnderstoodName() {
        log.log(Level.SEVERE, "SAAJ0301.ver1_1.hdr.op.unsupported.in.SOAP1.1", (Object[]) new String[]{"getNotUnderstoodName"});
        throw new UnsupportedOperationException("Not supported by SOAP 1.1");
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.HeaderImpl
    protected NameImpl getUpgradeName() {
        return NameImpl.create("Upgrade", getPrefix(), "http://schemas.xmlsoap.org/soap/envelope/");
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.HeaderImpl
    protected NameImpl getSupportedEnvelopeName() {
        return NameImpl.create("SupportedEnvelope", getPrefix(), "http://schemas.xmlsoap.org/soap/envelope/");
    }

    @Override // javax.xml.soap.SOAPHeader
    public SOAPHeaderElement addNotUnderstoodHeaderElement(QName name) throws SOAPException {
        log.log(Level.SEVERE, "SAAJ0301.ver1_1.hdr.op.unsupported.in.SOAP1.1", (Object[]) new String[]{"addNotUnderstoodHeaderElement"});
        throw new UnsupportedOperationException("Not supported by SOAP 1.1");
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.HeaderImpl
    protected SOAPHeaderElement createHeaderElement(Name name) {
        return new HeaderElement1_1Impl(((SOAPDocument) getOwnerDocument()).getDocument(), name);
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.HeaderImpl
    protected SOAPHeaderElement createHeaderElement(QName name) {
        return new HeaderElement1_1Impl(((SOAPDocument) getOwnerDocument()).getDocument(), name);
    }
}
