package com.sun.xml.internal.messaging.saaj.client.p2p;

import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPException;

/* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/client/p2p/HttpSOAPConnectionFactory.class */
public class HttpSOAPConnectionFactory extends SOAPConnectionFactory {
    @Override // javax.xml.soap.SOAPConnectionFactory
    public SOAPConnection createConnection() throws SOAPException {
        return new HttpSOAPConnection();
    }
}
