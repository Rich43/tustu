package com.sun.xml.internal.messaging.saaj.soap.dynamic;

import com.sun.xml.internal.messaging.saaj.soap.MessageFactoryImpl;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

/* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/soap/dynamic/SOAPMessageFactoryDynamicImpl.class */
public class SOAPMessageFactoryDynamicImpl extends MessageFactoryImpl {
    @Override // com.sun.xml.internal.messaging.saaj.soap.MessageFactoryImpl, javax.xml.soap.MessageFactory
    public SOAPMessage createMessage() throws SOAPException {
        throw new UnsupportedOperationException("createMessage() not supported for Dynamic Protocol");
    }
}
