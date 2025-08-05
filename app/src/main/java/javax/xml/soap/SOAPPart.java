package javax.xml.soap;

import com.sun.xml.internal.messaging.saaj.soap.MessageImpl;
import java.util.Iterator;
import javax.xml.transform.Source;
import org.w3c.dom.Document;

/* loaded from: rt.jar:javax/xml/soap/SOAPPart.class */
public abstract class SOAPPart implements Document, Node {
    public abstract SOAPEnvelope getEnvelope() throws SOAPException;

    public abstract void removeMimeHeader(String str);

    public abstract void removeAllMimeHeaders();

    public abstract String[] getMimeHeader(String str);

    public abstract void setMimeHeader(String str, String str2);

    public abstract void addMimeHeader(String str, String str2);

    public abstract Iterator getAllMimeHeaders();

    public abstract Iterator getMatchingMimeHeaders(String[] strArr);

    public abstract Iterator getNonMatchingMimeHeaders(String[] strArr);

    public abstract void setContent(Source source) throws SOAPException;

    public abstract Source getContent() throws SOAPException;

    public String getContentId() {
        String[] values = getMimeHeader("Content-Id");
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

    public void setContentId(String contentId) {
        setMimeHeader("Content-Id", contentId);
    }

    public void setContentLocation(String contentLocation) {
        setMimeHeader(MessageImpl.CONTENT_LOCATION, contentLocation);
    }
}
