package com.sun.xml.internal.ws.protocol.soap;

import com.sun.xml.internal.ws.api.SOAPVersion;
import com.sun.xml.internal.ws.api.message.ExceptionHasMessage;
import com.sun.xml.internal.ws.api.message.Message;
import com.sun.xml.internal.ws.fault.SOAPFaultBuilder;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/ws/protocol/soap/MessageCreationException.class */
public class MessageCreationException extends ExceptionHasMessage {
    private final SOAPVersion soapVersion;

    public MessageCreationException(SOAPVersion soapVersion, Object... args) {
        super("soap.msg.create.err", args);
        this.soapVersion = soapVersion;
    }

    @Override // com.sun.xml.internal.ws.util.exception.JAXWSExceptionBase
    public String getDefaultResourceBundleName() {
        return "com.sun.xml.internal.ws.resources.soap";
    }

    @Override // com.sun.xml.internal.ws.api.message.ExceptionHasMessage
    public Message getFaultMessage() {
        QName faultCode = this.soapVersion.faultCodeClient;
        return SOAPFaultBuilder.createSOAPFaultMessage(this.soapVersion, getLocalizedMessage(), faultCode);
    }
}
