package com.sun.xml.internal.messaging.saaj.soap.ver1_2;

import com.sun.xml.internal.messaging.saaj.SOAPExceptionImpl;
import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.ContentType;
import com.sun.xml.internal.messaging.saaj.soap.MessageImpl;
import com.sun.xml.internal.ws.encoding.fastinfoset.FastInfosetMIMETypes;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

/* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/soap/ver1_2/Message1_2Impl.class */
public class Message1_2Impl extends MessageImpl implements SOAPConstants {
    public Message1_2Impl() {
    }

    public Message1_2Impl(SOAPMessage msg) {
        super(msg);
    }

    public Message1_2Impl(boolean isFastInfoset, boolean acceptFastInfoset) {
        super(isFastInfoset, acceptFastInfoset);
    }

    public Message1_2Impl(MimeHeaders headers, InputStream in) throws SOAPExceptionImpl, IOException {
        super(headers, in);
    }

    public Message1_2Impl(MimeHeaders headers, ContentType ct, int stat, InputStream in) throws SOAPExceptionImpl {
        super(headers, ct, stat, in);
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.MessageImpl, javax.xml.soap.SOAPMessage
    public SOAPPart getSOAPPart() {
        if (this.soapPartImpl == null) {
            this.soapPartImpl = new SOAPPart1_2Impl(this);
        }
        return this.soapPartImpl;
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.MessageImpl
    protected boolean isCorrectSoapVersion(int contentTypeId) {
        return (contentTypeId & 8) != 0;
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.MessageImpl
    protected String getExpectedContentType() {
        return this.isFastInfoset ? FastInfosetMIMETypes.SOAP_12 : "application/soap+xml";
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.MessageImpl
    protected String getExpectedAcceptHeader() {
        return this.acceptFastInfoset ? "application/soap+fastinfoset, application/soap+xml, text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2" : "application/soap+xml, text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2";
    }
}
