package com.sun.xml.internal.messaging.saaj.soap.ver1_1;

import com.sun.xml.internal.messaging.saaj.soap.SOAPDocumentImpl;
import com.sun.xml.internal.messaging.saaj.soap.impl.EnvelopeImpl;
import com.sun.xml.internal.messaging.saaj.soap.name.NameImpl;
import javax.xml.soap.SOAPException;

/* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/soap/ver1_1/Envelope1_1Impl.class */
public class Envelope1_1Impl extends EnvelopeImpl {
    public Envelope1_1Impl(SOAPDocumentImpl ownerDoc, String prefix) {
        super(ownerDoc, NameImpl.createEnvelope1_1Name(prefix));
    }

    Envelope1_1Impl(SOAPDocumentImpl ownerDoc, String prefix, boolean createHeader, boolean createBody) throws SOAPException {
        super(ownerDoc, NameImpl.createEnvelope1_1Name(prefix), createHeader, createBody);
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.EnvelopeImpl
    protected NameImpl getBodyName(String prefix) {
        return NameImpl.createBody1_1Name(prefix);
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.EnvelopeImpl
    protected NameImpl getHeaderName(String prefix) {
        return NameImpl.createHeader1_1Name(prefix);
    }
}
