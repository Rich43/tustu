package javax.xml.soap;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import javax.activation.DataHandler;

/* loaded from: rt.jar:javax/xml/soap/SOAPMessage.class */
public abstract class SOAPMessage {
    public static final String CHARACTER_SET_ENCODING = "javax.xml.soap.character-set-encoding";
    public static final String WRITE_XML_DECLARATION = "javax.xml.soap.write-xml-declaration";

    public abstract void setContentDescription(String str);

    public abstract String getContentDescription();

    public abstract SOAPPart getSOAPPart();

    public abstract void removeAllAttachments();

    public abstract int countAttachments();

    public abstract Iterator getAttachments();

    public abstract Iterator getAttachments(MimeHeaders mimeHeaders);

    public abstract void removeAttachments(MimeHeaders mimeHeaders);

    public abstract AttachmentPart getAttachment(SOAPElement sOAPElement) throws SOAPException;

    public abstract void addAttachmentPart(AttachmentPart attachmentPart);

    public abstract AttachmentPart createAttachmentPart();

    public abstract MimeHeaders getMimeHeaders();

    public abstract void saveChanges() throws SOAPException;

    public abstract boolean saveRequired();

    public abstract void writeTo(OutputStream outputStream) throws SOAPException, IOException;

    public SOAPBody getSOAPBody() throws SOAPException {
        throw new UnsupportedOperationException("getSOAPBody must be overridden by all subclasses of SOAPMessage");
    }

    public SOAPHeader getSOAPHeader() throws SOAPException {
        throw new UnsupportedOperationException("getSOAPHeader must be overridden by all subclasses of SOAPMessage");
    }

    public AttachmentPart createAttachmentPart(DataHandler dataHandler) {
        AttachmentPart attachment = createAttachmentPart();
        attachment.setDataHandler(dataHandler);
        return attachment;
    }

    public AttachmentPart createAttachmentPart(Object content, String contentType) {
        AttachmentPart attachment = createAttachmentPart();
        attachment.setContent(content, contentType);
        return attachment;
    }

    public void setProperty(String property, Object value) throws SOAPException {
        throw new UnsupportedOperationException("setProperty must be overridden by all subclasses of SOAPMessage");
    }

    public Object getProperty(String property) throws SOAPException {
        throw new UnsupportedOperationException("getProperty must be overridden by all subclasses of SOAPMessage");
    }
}
