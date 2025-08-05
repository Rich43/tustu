package com.sun.xml.internal.ws.util.xml;

import com.sun.xml.internal.org.jvnet.staxex.Base64Data;
import com.sun.xml.internal.org.jvnet.staxex.XMLStreamReaderEx;
import com.sun.xml.internal.org.jvnet.staxex.XMLStreamWriterEx;
import com.sun.xml.internal.ws.streaming.MtomStreamWriter;
import java.io.IOException;
import javax.xml.bind.attachment.AttachmentMarshaller;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

/* loaded from: rt.jar:com/sun/xml/internal/ws/util/xml/XMLStreamReaderToXMLStreamWriter.class */
public class XMLStreamReaderToXMLStreamWriter {
    private static final int BUF_SIZE = 4096;
    protected XMLStreamReader in;
    protected XMLStreamWriter out;
    private char[] buf;
    boolean optimizeBase64Data = false;
    AttachmentMarshaller mtomAttachmentMarshaller;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !XMLStreamReaderToXMLStreamWriter.class.desiredAssertionStatus();
    }

    public void bridge(XMLStreamReader in, XMLStreamWriter out) throws XMLStreamException {
        if (!$assertionsDisabled && (in == null || out == null)) {
            throw new AssertionError();
        }
        this.in = in;
        this.out = out;
        this.optimizeBase64Data = in instanceof XMLStreamReaderEx;
        if ((out instanceof XMLStreamWriterEx) && (out instanceof MtomStreamWriter)) {
            this.mtomAttachmentMarshaller = ((MtomStreamWriter) out).getAttachmentMarshaller();
        }
        int depth = 0;
        this.buf = new char[4096];
        int event = in.getEventType();
        if (event == 7) {
            while (!in.isStartElement()) {
                event = in.next();
                if (event == 5) {
                    handleComment();
                }
            }
        }
        if (event != 1) {
            throw new IllegalStateException("The current event is not START_ELEMENT\n but " + event);
        }
        do {
            switch (event) {
                case 1:
                    depth++;
                    handleStartElement();
                    break;
                case 2:
                    handleEndElement();
                    depth--;
                    if (depth == 0) {
                        return;
                    }
                    break;
                case 3:
                    handlePI();
                    break;
                case 4:
                    handleCharacters();
                    break;
                case 5:
                    handleComment();
                    break;
                case 6:
                    handleSpace();
                    break;
                case 7:
                case 10:
                default:
                    throw new XMLStreamException("Cannot process event: " + event);
                case 8:
                    throw new XMLStreamException("Malformed XML at depth=" + depth + ", Reached EOF. Event=" + event);
                case 9:
                    handleEntityReference();
                    break;
                case 11:
                    handleDTD();
                    break;
                case 12:
                    handleCDATA();
                    break;
            }
            event = in.next();
        } while (depth != 0);
    }

    protected void handlePI() throws XMLStreamException {
        this.out.writeProcessingInstruction(this.in.getPITarget(), this.in.getPIData());
    }

    protected void handleCharacters() throws XMLStreamException {
        CharSequence c2 = null;
        if (this.optimizeBase64Data) {
            c2 = ((XMLStreamReaderEx) this.in).getPCDATA();
        }
        if (c2 != null && (c2 instanceof Base64Data)) {
            if (this.mtomAttachmentMarshaller != null) {
                Base64Data b64d = (Base64Data) c2;
                ((XMLStreamWriterEx) this.out).writeBinary(b64d.getDataHandler());
                return;
            } else {
                try {
                    ((Base64Data) c2).writeTo(this.out);
                    return;
                } catch (IOException e2) {
                    throw new XMLStreamException(e2);
                }
            }
        }
        int start = 0;
        int read = this.buf.length;
        while (read == this.buf.length) {
            read = this.in.getTextCharacters(start, this.buf, 0, this.buf.length);
            this.out.writeCharacters(this.buf, 0, read);
            start += this.buf.length;
        }
    }

    protected void handleEndElement() throws XMLStreamException {
        this.out.writeEndElement();
    }

    protected void handleStartElement() throws XMLStreamException {
        String nsUri = this.in.getNamespaceURI();
        if (nsUri == null) {
            this.out.writeStartElement(this.in.getLocalName());
        } else {
            this.out.writeStartElement(fixNull(this.in.getPrefix()), this.in.getLocalName(), nsUri);
        }
        int nsCount = this.in.getNamespaceCount();
        for (int i2 = 0; i2 < nsCount; i2++) {
            this.out.writeNamespace(this.in.getNamespacePrefix(i2), fixNull(this.in.getNamespaceURI(i2)));
        }
        int attCount = this.in.getAttributeCount();
        for (int i3 = 0; i3 < attCount; i3++) {
            handleAttribute(i3);
        }
    }

    protected void handleAttribute(int i2) throws XMLStreamException {
        String nsUri = this.in.getAttributeNamespace(i2);
        String prefix = this.in.getAttributePrefix(i2);
        if (fixNull(nsUri).equals("http://www.w3.org/2000/xmlns/")) {
            return;
        }
        if (nsUri == null || prefix == null || prefix.equals("")) {
            this.out.writeAttribute(this.in.getAttributeLocalName(i2), this.in.getAttributeValue(i2));
        } else {
            this.out.writeAttribute(prefix, nsUri, this.in.getAttributeLocalName(i2), this.in.getAttributeValue(i2));
        }
    }

    protected void handleDTD() throws XMLStreamException {
        this.out.writeDTD(this.in.getText());
    }

    protected void handleComment() throws XMLStreamException {
        this.out.writeComment(this.in.getText());
    }

    protected void handleEntityReference() throws XMLStreamException {
        this.out.writeEntityRef(this.in.getText());
    }

    protected void handleSpace() throws XMLStreamException {
        handleCharacters();
    }

    protected void handleCDATA() throws XMLStreamException {
        this.out.writeCData(this.in.getText());
    }

    private static String fixNull(String s2) {
        return s2 == null ? "" : s2;
    }
}
