package com.sun.xml.internal.ws.protocol.soap;

import com.sun.xml.internal.ws.api.SOAPVersion;
import com.sun.xml.internal.ws.api.message.ExceptionHasMessage;
import com.sun.xml.internal.ws.api.message.Message;
import com.sun.xml.internal.ws.encoding.soap.SOAP12Constants;
import com.sun.xml.internal.ws.encoding.soap.SOAPConstants;
import com.sun.xml.internal.ws.fault.SOAPFaultBuilder;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/ws/protocol/soap/VersionMismatchException.class */
public class VersionMismatchException extends ExceptionHasMessage {
    private final SOAPVersion soapVersion;

    public VersionMismatchException(SOAPVersion soapVersion, Object... args) {
        super("soap.version.mismatch.err", args);
        this.soapVersion = soapVersion;
    }

    @Override // com.sun.xml.internal.ws.util.exception.JAXWSExceptionBase
    public String getDefaultResourceBundleName() {
        return "com.sun.xml.internal.ws.resources.soap";
    }

    @Override // com.sun.xml.internal.ws.api.message.ExceptionHasMessage
    public Message getFaultMessage() {
        QName faultCode = this.soapVersion == SOAPVersion.SOAP_11 ? SOAPConstants.FAULT_CODE_VERSION_MISMATCH : SOAP12Constants.FAULT_CODE_VERSION_MISMATCH;
        return SOAPFaultBuilder.createSOAPFaultMessage(this.soapVersion, getLocalizedMessage(), faultCode);
    }
}
