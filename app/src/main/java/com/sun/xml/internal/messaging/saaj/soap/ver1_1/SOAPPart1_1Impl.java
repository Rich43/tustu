package com.sun.xml.internal.messaging.saaj.soap.ver1_1;

import com.sun.xml.internal.messaging.saaj.soap.Envelope;
import com.sun.xml.internal.messaging.saaj.soap.EnvelopeFactory;
import com.sun.xml.internal.messaging.saaj.soap.MessageImpl;
import com.sun.xml.internal.messaging.saaj.soap.SOAPPartImpl;
import com.sun.xml.internal.messaging.saaj.soap.impl.EnvelopeImpl;
import com.sun.xml.internal.messaging.saaj.util.LogDomainConstants;
import com.sun.xml.internal.messaging.saaj.util.XMLDeclarationParser;
import java.util.logging.Logger;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPException;
import javax.xml.transform.Source;

/* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/soap/ver1_1/SOAPPart1_1Impl.class */
public class SOAPPart1_1Impl extends SOAPPartImpl implements SOAPConstants {
    protected static final Logger log = Logger.getLogger(LogDomainConstants.SOAP_VER1_1_DOMAIN, "com.sun.xml.internal.messaging.saaj.soap.ver1_1.LocalStrings");

    public SOAPPart1_1Impl() {
    }

    public SOAPPart1_1Impl(MessageImpl message) {
        super(message);
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.SOAPPartImpl
    protected String getContentType() {
        return isFastInfoset() ? "application/fastinfoset" : "text/xml";
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.SOAPPartImpl
    protected Envelope createEnvelopeFromSource() throws SOAPException {
        XMLDeclarationParser parser = lookForXmlDecl();
        Source tmp = this.source;
        this.source = null;
        EnvelopeImpl envelope = (EnvelopeImpl) EnvelopeFactory.createEnvelope(tmp, this);
        if (!envelope.getNamespaceURI().equals("http://schemas.xmlsoap.org/soap/envelope/")) {
            log.severe("SAAJ0304.ver1_1.msg.invalid.SOAP1.1");
            throw new SOAPException("InputStream does not represent a valid SOAP 1.1 Message");
        }
        if (parser != null && !this.omitXmlDecl) {
            envelope.setOmitXmlDecl("no");
            envelope.setXmlDecl(parser.getXmlDeclaration());
            envelope.setCharsetEncoding(parser.getEncoding());
        }
        return envelope;
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.SOAPPartImpl
    protected Envelope createEmptyEnvelope(String prefix) throws SOAPException {
        return new Envelope1_1Impl(getDocument(), prefix, true, true);
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.SOAPPartImpl
    protected SOAPPartImpl duplicateType() {
        return new SOAPPart1_1Impl();
    }
}
