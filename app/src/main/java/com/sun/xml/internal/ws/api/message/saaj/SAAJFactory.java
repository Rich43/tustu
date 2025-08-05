package com.sun.xml.internal.ws.api.message.saaj;

import com.sun.xml.internal.bind.marshaller.SAX2DOMEx;
import com.sun.xml.internal.messaging.saaj.soap.MessageImpl;
import com.sun.xml.internal.ws.api.SOAPVersion;
import com.sun.xml.internal.ws.api.message.Attachment;
import com.sun.xml.internal.ws.api.message.AttachmentEx;
import com.sun.xml.internal.ws.api.message.Message;
import com.sun.xml.internal.ws.api.message.Packet;
import com.sun.xml.internal.ws.message.saaj.SAAJMessage;
import com.sun.xml.internal.ws.util.ServiceFinder;
import com.sun.xml.internal.ws.util.xml.XmlUtil;
import java.util.Iterator;
import javax.xml.soap.AttachmentPart;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPMessage;
import javax.xml.stream.XMLStreamException;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/message/saaj/SAAJFactory.class */
public class SAAJFactory {
    private static final SAAJFactory instance = new SAAJFactory();

    public static MessageFactory getMessageFactory(String protocol) throws SOAPException {
        Iterator it = ServiceFinder.find(SAAJFactory.class).iterator();
        while (it.hasNext()) {
            SAAJFactory s2 = (SAAJFactory) it.next();
            MessageFactory mf = s2.createMessageFactory(protocol);
            if (mf != null) {
                return mf;
            }
        }
        return instance.createMessageFactory(protocol);
    }

    public static SOAPFactory getSOAPFactory(String protocol) throws SOAPException {
        Iterator it = ServiceFinder.find(SAAJFactory.class).iterator();
        while (it.hasNext()) {
            SAAJFactory s2 = (SAAJFactory) it.next();
            SOAPFactory sf = s2.createSOAPFactory(protocol);
            if (sf != null) {
                return sf;
            }
        }
        return instance.createSOAPFactory(protocol);
    }

    public static Message create(SOAPMessage saaj) {
        Iterator it = ServiceFinder.find(SAAJFactory.class).iterator();
        while (it.hasNext()) {
            SAAJFactory s2 = (SAAJFactory) it.next();
            Message m2 = s2.createMessage(saaj);
            if (m2 != null) {
                return m2;
            }
        }
        return instance.createMessage(saaj);
    }

    public static SOAPMessage read(SOAPVersion soapVersion, Message message) throws SOAPException {
        Iterator it = ServiceFinder.find(SAAJFactory.class).iterator();
        while (it.hasNext()) {
            SAAJFactory s2 = (SAAJFactory) it.next();
            SOAPMessage msg = s2.readAsSOAPMessage(soapVersion, message);
            if (msg != null) {
                return msg;
            }
        }
        return instance.readAsSOAPMessage(soapVersion, message);
    }

    public static SOAPMessage read(SOAPVersion soapVersion, Message message, Packet packet) throws SOAPException {
        Iterator it = ServiceFinder.find(SAAJFactory.class).iterator();
        while (it.hasNext()) {
            SAAJFactory s2 = (SAAJFactory) it.next();
            SOAPMessage msg = s2.readAsSOAPMessage(soapVersion, message, packet);
            if (msg != null) {
                return msg;
            }
        }
        return instance.readAsSOAPMessage(soapVersion, message, packet);
    }

    public static SAAJMessage read(Packet packet) throws SOAPException {
        ServiceFinder<SAAJFactory> serviceFinderFind;
        if (packet.component != null) {
            serviceFinderFind = ServiceFinder.find(SAAJFactory.class, packet.component);
        } else {
            serviceFinderFind = ServiceFinder.find(SAAJFactory.class);
        }
        ServiceFinder<SAAJFactory> factories = serviceFinderFind;
        Iterator<SAAJFactory> it = factories.iterator();
        while (it.hasNext()) {
            SAAJFactory s2 = it.next();
            SAAJMessage msg = s2.readAsSAAJ(packet);
            if (msg != null) {
                return msg;
            }
        }
        return instance.readAsSAAJ(packet);
    }

    public SAAJMessage readAsSAAJ(Packet packet) throws SOAPException {
        SOAPVersion v2 = packet.getMessage().getSOAPVersion();
        SOAPMessage msg = readAsSOAPMessage(v2, packet.getMessage());
        return new SAAJMessage(msg);
    }

    public MessageFactory createMessageFactory(String protocol) throws SOAPException {
        return MessageFactory.newInstance(protocol);
    }

    public SOAPFactory createSOAPFactory(String protocol) throws SOAPException {
        return SOAPFactory.newInstance(protocol);
    }

    public Message createMessage(SOAPMessage saaj) {
        return new SAAJMessage(saaj);
    }

    public SOAPMessage readAsSOAPMessage(SOAPVersion soapVersion, Message message) throws SOAPException {
        SaajStaxWriter writer = new SaajStaxWriter(soapVersion.getMessageFactory().createMessage());
        try {
            message.writeTo(writer);
            SOAPMessage msg = writer.getSOAPMessage();
            addAttachmentsToSOAPMessage(msg, message);
            if (msg.saveRequired()) {
                msg.saveChanges();
            }
            return msg;
        } catch (XMLStreamException e2) {
            if (e2.getCause() instanceof SOAPException) {
                throw ((SOAPException) e2.getCause());
            }
            throw new SOAPException(e2);
        }
    }

    public SOAPMessage readAsSOAPMessageSax2Dom(SOAPVersion soapVersion, Message message) throws SOAPException {
        SOAPMessage msg = soapVersion.getMessageFactory().createMessage();
        SAX2DOMEx s2d = new SAX2DOMEx(msg.getSOAPPart());
        try {
            message.writeTo(s2d, XmlUtil.DRACONIAN_ERROR_HANDLER);
            addAttachmentsToSOAPMessage(msg, message);
            if (msg.saveRequired()) {
                msg.saveChanges();
            }
            return msg;
        } catch (SAXException e2) {
            throw new SOAPException(e2);
        }
    }

    protected static void addAttachmentsToSOAPMessage(SOAPMessage msg, Message message) {
        for (Attachment att : message.getAttachments()) {
            AttachmentPart part = msg.createAttachmentPart();
            part.setDataHandler(att.asDataHandler());
            String cid = att.getContentId();
            if (cid != null) {
                if (cid.startsWith("<") && cid.endsWith(">")) {
                    part.setContentId(cid);
                } else {
                    part.setContentId('<' + cid + '>');
                }
            }
            if (att instanceof AttachmentEx) {
                AttachmentEx ax2 = (AttachmentEx) att;
                Iterator<AttachmentEx.MimeHeader> imh = ax2.getMimeHeaders();
                while (imh.hasNext()) {
                    AttachmentEx.MimeHeader ame = imh.next();
                    if (!MessageImpl.CONTENT_ID.equals(ame.getName()) && !"Content-Type".equals(ame.getName())) {
                        part.addMimeHeader(ame.getName(), ame.getValue());
                    }
                }
            }
            msg.addAttachmentPart(part);
        }
    }

    public SOAPMessage readAsSOAPMessage(SOAPVersion soapVersion, Message message, Packet packet) throws SOAPException {
        return readAsSOAPMessage(soapVersion, message);
    }
}
