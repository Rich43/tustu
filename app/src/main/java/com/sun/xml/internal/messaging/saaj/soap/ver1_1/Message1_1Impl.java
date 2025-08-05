package com.sun.xml.internal.messaging.saaj.soap.ver1_1;

import com.sun.xml.internal.messaging.saaj.SOAPExceptionImpl;
import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.ContentType;
import com.sun.xml.internal.messaging.saaj.soap.MessageImpl;
import com.sun.xml.internal.messaging.saaj.util.LogDomainConstants;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

/* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/soap/ver1_1/Message1_1Impl.class */
public class Message1_1Impl extends MessageImpl implements SOAPConstants {
    protected static final Logger log = Logger.getLogger(LogDomainConstants.SOAP_VER1_1_DOMAIN, "com.sun.xml.internal.messaging.saaj.soap.ver1_1.LocalStrings");

    public Message1_1Impl() {
    }

    public Message1_1Impl(boolean isFastInfoset, boolean acceptFastInfoset) {
        super(isFastInfoset, acceptFastInfoset);
    }

    public Message1_1Impl(SOAPMessage msg) {
        super(msg);
    }

    public Message1_1Impl(MimeHeaders headers, InputStream in) throws SOAPExceptionImpl, IOException {
        super(headers, in);
    }

    public Message1_1Impl(MimeHeaders headers, ContentType ct, int stat, InputStream in) throws SOAPExceptionImpl {
        super(headers, ct, stat, in);
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.MessageImpl, javax.xml.soap.SOAPMessage
    public SOAPPart getSOAPPart() {
        if (this.soapPartImpl == null) {
            this.soapPartImpl = new SOAPPart1_1Impl(this);
        }
        return this.soapPartImpl;
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.MessageImpl
    protected boolean isCorrectSoapVersion(int contentTypeId) {
        return (contentTypeId & 4) != 0;
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.MessageImpl
    public String getAction() {
        log.log(Level.SEVERE, "SAAJ0303.ver1_1.msg.op.unsupported.in.SOAP1.1", (Object[]) new String[]{"Action"});
        throw new UnsupportedOperationException("Operation not supported by SOAP 1.1");
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.MessageImpl
    public void setAction(String type) {
        log.log(Level.SEVERE, "SAAJ0303.ver1_1.msg.op.unsupported.in.SOAP1.1", (Object[]) new String[]{"Action"});
        throw new UnsupportedOperationException("Operation not supported by SOAP 1.1");
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.MessageImpl
    public String getCharset() {
        log.log(Level.SEVERE, "SAAJ0303.ver1_1.msg.op.unsupported.in.SOAP1.1", (Object[]) new String[]{"Charset"});
        throw new UnsupportedOperationException("Operation not supported by SOAP 1.1");
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.MessageImpl
    public void setCharset(String charset) {
        log.log(Level.SEVERE, "SAAJ0303.ver1_1.msg.op.unsupported.in.SOAP1.1", (Object[]) new String[]{"Charset"});
        throw new UnsupportedOperationException("Operation not supported by SOAP 1.1");
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.MessageImpl
    protected String getExpectedContentType() {
        return this.isFastInfoset ? "application/fastinfoset" : "text/xml";
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.MessageImpl
    protected String getExpectedAcceptHeader() {
        return this.acceptFastInfoset ? "application/fastinfoset, text/xml, text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2" : "text/xml, text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2";
    }
}
