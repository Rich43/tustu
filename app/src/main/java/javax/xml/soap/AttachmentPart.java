package javax.xml.soap;

import com.sun.xml.internal.messaging.saaj.soap.MessageImpl;
import java.io.InputStream;
import java.util.Iterator;
import javax.activation.DataHandler;

/* loaded from: rt.jar:javax/xml/soap/AttachmentPart.class */
public abstract class AttachmentPart {
    public abstract int getSize() throws SOAPException;

    public abstract void clearContent();

    public abstract Object getContent() throws SOAPException;

    public abstract InputStream getRawContent() throws SOAPException;

    public abstract byte[] getRawContentBytes() throws SOAPException;

    public abstract InputStream getBase64Content() throws SOAPException;

    public abstract void setContent(Object obj, String str);

    public abstract void setRawContent(InputStream inputStream, String str) throws SOAPException;

    public abstract void setRawContentBytes(byte[] bArr, int i2, int i3, String str) throws SOAPException;

    public abstract void setBase64Content(InputStream inputStream, String str) throws SOAPException;

    public abstract DataHandler getDataHandler() throws SOAPException;

    public abstract void setDataHandler(DataHandler dataHandler);

    public abstract void removeMimeHeader(String str);

    public abstract void removeAllMimeHeaders();

    public abstract String[] getMimeHeader(String str);

    public abstract void setMimeHeader(String str, String str2);

    public abstract void addMimeHeader(String str, String str2);

    public abstract Iterator getAllMimeHeaders();

    public abstract Iterator getMatchingMimeHeaders(String[] strArr);

    public abstract Iterator getNonMatchingMimeHeaders(String[] strArr);

    public String getContentId() {
        String[] values = getMimeHeader(MessageImpl.CONTENT_ID);
        if (values != null && values.length > 0) {
            return values[0];
        }
        return null;
    }

    public String getContentLocation() {
        String[] values = getMimeHeader(MessageImpl.CONTENT_LOCATION);
        if (values != null && values.length > 0) {
            return values[0];
        }
        return null;
    }

    public String getContentType() {
        String[] values = getMimeHeader("Content-Type");
        if (values != null && values.length > 0) {
            return values[0];
        }
        return null;
    }

    public void setContentId(String contentId) {
        setMimeHeader(MessageImpl.CONTENT_ID, contentId);
    }

    public void setContentLocation(String contentLocation) {
        setMimeHeader(MessageImpl.CONTENT_LOCATION, contentLocation);
    }

    public void setContentType(String contentType) {
        setMimeHeader("Content-Type", contentType);
    }
}
