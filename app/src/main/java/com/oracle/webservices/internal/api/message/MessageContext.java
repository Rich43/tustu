package com.oracle.webservices.internal.api.message;

import java.io.IOException;
import java.io.OutputStream;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

/* loaded from: rt.jar:com/oracle/webservices/internal/api/message/MessageContext.class */
public interface MessageContext extends DistributedPropertySet {
    SOAPMessage getAsSOAPMessage() throws SOAPException;

    SOAPMessage getSOAPMessage() throws SOAPException;

    ContentType writeTo(OutputStream outputStream) throws IOException;

    ContentType getContentType();
}
