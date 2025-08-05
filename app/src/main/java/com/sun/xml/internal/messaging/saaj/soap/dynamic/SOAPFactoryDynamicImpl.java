package com.sun.xml.internal.messaging.saaj.soap.dynamic;

import com.sun.xml.internal.messaging.saaj.soap.SOAPDocumentImpl;
import com.sun.xml.internal.messaging.saaj.soap.SOAPFactoryImpl;
import javax.xml.soap.Detail;
import javax.xml.soap.SOAPException;

/* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/soap/dynamic/SOAPFactoryDynamicImpl.class */
public class SOAPFactoryDynamicImpl extends SOAPFactoryImpl {
    @Override // com.sun.xml.internal.messaging.saaj.soap.SOAPFactoryImpl
    protected SOAPDocumentImpl createDocument() {
        return null;
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.SOAPFactoryImpl, javax.xml.soap.SOAPFactory
    public Detail createDetail() throws SOAPException {
        throw new UnsupportedOperationException("createDetail() not supported for Dynamic Protocol");
    }
}
