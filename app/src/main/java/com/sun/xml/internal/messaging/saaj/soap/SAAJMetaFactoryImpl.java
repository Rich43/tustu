package com.sun.xml.internal.messaging.saaj.soap;

import com.sun.xml.internal.messaging.saaj.soap.dynamic.SOAPFactoryDynamicImpl;
import com.sun.xml.internal.messaging.saaj.soap.dynamic.SOAPMessageFactoryDynamicImpl;
import com.sun.xml.internal.messaging.saaj.soap.ver1_1.SOAPFactory1_1Impl;
import com.sun.xml.internal.messaging.saaj.soap.ver1_1.SOAPMessageFactory1_1Impl;
import com.sun.xml.internal.messaging.saaj.soap.ver1_2.SOAPFactory1_2Impl;
import com.sun.xml.internal.messaging.saaj.soap.ver1_2.SOAPMessageFactory1_2Impl;
import com.sun.xml.internal.messaging.saaj.util.LogDomainConstants;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SAAJMetaFactory;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;

/* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/soap/SAAJMetaFactoryImpl.class */
public class SAAJMetaFactoryImpl extends SAAJMetaFactory {
    protected static final Logger log = Logger.getLogger(LogDomainConstants.SOAP_DOMAIN, "com.sun.xml.internal.messaging.saaj.soap.LocalStrings");

    @Override // javax.xml.soap.SAAJMetaFactory
    protected MessageFactory newMessageFactory(String protocol) throws SOAPException {
        if ("SOAP 1.1 Protocol".equals(protocol)) {
            return new SOAPMessageFactory1_1Impl();
        }
        if (SOAPConstants.SOAP_1_2_PROTOCOL.equals(protocol)) {
            return new SOAPMessageFactory1_2Impl();
        }
        if (SOAPConstants.DYNAMIC_SOAP_PROTOCOL.equals(protocol)) {
            return new SOAPMessageFactoryDynamicImpl();
        }
        log.log(Level.SEVERE, "SAAJ0569.soap.unknown.protocol", new Object[]{protocol, "MessageFactory"});
        throw new SOAPException("Unknown Protocol: " + protocol + "  specified for creating MessageFactory");
    }

    @Override // javax.xml.soap.SAAJMetaFactory
    protected SOAPFactory newSOAPFactory(String protocol) throws SOAPException {
        if ("SOAP 1.1 Protocol".equals(protocol)) {
            return new SOAPFactory1_1Impl();
        }
        if (SOAPConstants.SOAP_1_2_PROTOCOL.equals(protocol)) {
            return new SOAPFactory1_2Impl();
        }
        if (SOAPConstants.DYNAMIC_SOAP_PROTOCOL.equals(protocol)) {
            return new SOAPFactoryDynamicImpl();
        }
        log.log(Level.SEVERE, "SAAJ0569.soap.unknown.protocol", new Object[]{protocol, "SOAPFactory"});
        throw new SOAPException("Unknown Protocol: " + protocol + "  specified for creating SOAPFactory");
    }
}
