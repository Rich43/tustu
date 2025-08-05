package com.sun.xml.internal.messaging.saaj.soap.ver1_2;

import com.sun.xml.internal.messaging.saaj.soap.SOAPDocumentImpl;
import com.sun.xml.internal.messaging.saaj.soap.SOAPFactoryImpl;
import javax.xml.namespace.QName;
import javax.xml.soap.Detail;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;

/* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/soap/ver1_2/SOAPFactory1_2Impl.class */
public class SOAPFactory1_2Impl extends SOAPFactoryImpl {
    @Override // com.sun.xml.internal.messaging.saaj.soap.SOAPFactoryImpl
    protected SOAPDocumentImpl createDocument() {
        return new SOAPPart1_2Impl().getDocument();
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.SOAPFactoryImpl, javax.xml.soap.SOAPFactory
    public Detail createDetail() throws SOAPException {
        return new Detail1_2Impl(createDocument());
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.SOAPFactoryImpl, javax.xml.soap.SOAPFactory
    public SOAPFault createFault(String reasonText, QName faultCode) throws SOAPException {
        if (faultCode == null) {
            throw new IllegalArgumentException("faultCode argument for createFault was passed NULL");
        }
        if (reasonText == null) {
            throw new IllegalArgumentException("reasonText argument for createFault was passed NULL");
        }
        Fault1_2Impl fault = new Fault1_2Impl(createDocument(), null);
        fault.setFaultCode(faultCode);
        fault.setFaultString(reasonText);
        return fault;
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.SOAPFactoryImpl, javax.xml.soap.SOAPFactory
    public SOAPFault createFault() throws SOAPException {
        Fault1_2Impl fault = new Fault1_2Impl(createDocument(), null);
        fault.setFaultCode(fault.getDefaultFaultCode());
        fault.setFaultString("Fault string, and possibly fault code, not set");
        return fault;
    }
}
