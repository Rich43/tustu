package com.sun.xml.internal.ws.encoding;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.org.jvnet.mimepull.Header;
import com.sun.xml.internal.org.jvnet.mimepull.MIMEMessage;
import com.sun.xml.internal.org.jvnet.mimepull.MIMEPart;
import com.sun.xml.internal.ws.api.message.Attachment;
import com.sun.xml.internal.ws.api.message.AttachmentEx;
import com.sun.xml.internal.ws.developer.StreamingAttachmentFeature;
import com.sun.xml.internal.ws.developer.StreamingDataHandler;
import com.sun.xml.internal.ws.util.ByteArrayBuffer;
import com.sun.xml.internal.ws.util.ByteArrayDataSource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.WebServiceException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/encoding/MimeMultipartParser.class */
public final class MimeMultipartParser {
    private final String start;
    private final MIMEMessage message;
    private Attachment root;
    private ContentTypeImpl contentType;
    private final Map<String, Attachment> attachments = new HashMap();
    private boolean gotAll;

    public MimeMultipartParser(InputStream in, String cType, StreamingAttachmentFeature feature) {
        this.contentType = new ContentTypeImpl(cType);
        String boundary = this.contentType.getBoundary();
        if (boundary == null || boundary.equals("")) {
            throw new WebServiceException("MIME boundary parameter not found" + ((Object) this.contentType));
        }
        this.message = feature != null ? new MIMEMessage(in, boundary, feature.getConfig()) : new MIMEMessage(in, boundary);
        String st = this.contentType.getRootId();
        if (st != null && st.length() > 2 && st.charAt(0) == '<' && st.charAt(st.length() - 1) == '>') {
            st = st.substring(1, st.length() - 1);
        }
        this.start = st;
    }

    @Nullable
    public Attachment getRootPart() {
        if (this.root == null) {
            this.root = new PartAttachment(this.start != null ? this.message.getPart(this.start) : this.message.getPart(0));
        }
        return this.root;
    }

    @NotNull
    public Map<String, Attachment> getAttachmentParts() {
        if (!this.gotAll) {
            MIMEPart rootPart = this.start != null ? this.message.getPart(this.start) : this.message.getPart(0);
            List<MIMEPart> parts = this.message.getAttachments();
            for (MIMEPart part : parts) {
                if (part != rootPart) {
                    String cid = part.getContentId();
                    if (!this.attachments.containsKey(cid)) {
                        PartAttachment attach = new PartAttachment(part);
                        this.attachments.put(attach.getContentId(), attach);
                    }
                }
            }
            this.gotAll = true;
        }
        return this.attachments;
    }

    @Nullable
    public Attachment getAttachmentPart(String contentId) throws IOException {
        Attachment attach = this.attachments.get(contentId);
        if (attach == null) {
            MIMEPart part = this.message.getPart(contentId);
            attach = new PartAttachment(part);
            this.attachments.put(contentId, attach);
        }
        return attach;
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/encoding/MimeMultipartParser$PartAttachment.class */
    static class PartAttachment implements AttachmentEx {
        final MIMEPart part;
        byte[] buf;
        private StreamingDataHandler streamingDataHandler;

        PartAttachment(MIMEPart part) {
            this.part = part;
        }

        @Override // com.sun.xml.internal.ws.api.message.Attachment
        @NotNull
        public String getContentId() {
            return this.part.getContentId();
        }

        @Override // com.sun.xml.internal.ws.api.message.Attachment
        @NotNull
        public String getContentType() {
            return this.part.getContentType();
        }

        @Override // com.sun.xml.internal.ws.api.message.Attachment
        public byte[] asByteArray() {
            if (this.buf == null) {
                ByteArrayBuffer baf = new ByteArrayBuffer();
                try {
                    try {
                        baf.write(this.part.readOnce());
                        if (baf != null) {
                            try {
                                baf.close();
                            } catch (IOException ex) {
                                Logger.getLogger(MimeMultipartParser.class.getName()).log(Level.FINE, (String) null, (Throwable) ex);
                            }
                        }
                        this.buf = baf.toByteArray();
                    } catch (IOException ioe) {
                        throw new WebServiceException(ioe);
                    }
                } catch (Throwable th) {
                    if (baf != null) {
                        try {
                            baf.close();
                        } catch (IOException ex2) {
                            Logger.getLogger(MimeMultipartParser.class.getName()).log(Level.FINE, (String) null, (Throwable) ex2);
                        }
                    }
                    throw th;
                }
            }
            return this.buf;
        }

        @Override // com.sun.xml.internal.ws.api.message.Attachment
        public DataHandler asDataHandler() {
            if (this.streamingDataHandler == null) {
                this.streamingDataHandler = this.buf != null ? new DataSourceStreamingDataHandler(new ByteArrayDataSource(this.buf, getContentType())) : new MIMEPartStreamingDataHandler(this.part);
            }
            return this.streamingDataHandler;
        }

        @Override // com.sun.xml.internal.ws.api.message.Attachment
        public Source asSource() {
            return this.buf != null ? new StreamSource(new ByteArrayInputStream(this.buf)) : new StreamSource(this.part.read());
        }

        @Override // com.sun.xml.internal.ws.api.message.Attachment
        public InputStream asInputStream() {
            return this.buf != null ? new ByteArrayInputStream(this.buf) : this.part.read();
        }

        @Override // com.sun.xml.internal.ws.api.message.Attachment
        public void writeTo(OutputStream os) throws IOException {
            if (this.buf != null) {
                os.write(this.buf);
                return;
            }
            InputStream in = this.part.read();
            byte[] temp = new byte[8192];
            while (true) {
                int len = in.read(temp);
                if (len != -1) {
                    os.write(temp, 0, len);
                } else {
                    in.close();
                    return;
                }
            }
        }

        @Override // com.sun.xml.internal.ws.api.message.Attachment
        public void writeTo(SOAPMessage saaj) throws SOAPException {
            saaj.createAttachmentPart().setDataHandler(asDataHandler());
        }

        @Override // com.sun.xml.internal.ws.api.message.AttachmentEx
        public Iterator<AttachmentEx.MimeHeader> getMimeHeaders() {
            final Iterator<? extends Header> ih = this.part.getAllHeaders().iterator();
            return new Iterator<AttachmentEx.MimeHeader>() { // from class: com.sun.xml.internal.ws.encoding.MimeMultipartParser.PartAttachment.1
                @Override // java.util.Iterator
                public boolean hasNext() {
                    return ih.hasNext();
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.util.Iterator
                public AttachmentEx.MimeHeader next() {
                    final Header hdr = (Header) ih.next();
                    return new AttachmentEx.MimeHeader() { // from class: com.sun.xml.internal.ws.encoding.MimeMultipartParser.PartAttachment.1.1
                        @Override // com.sun.xml.internal.ws.api.message.AttachmentEx.MimeHeader
                        public String getValue() {
                            return hdr.getValue();
                        }

                        @Override // com.sun.xml.internal.ws.api.message.AttachmentEx.MimeHeader
                        public String getName() {
                            return hdr.getName();
                        }
                    };
                }

                @Override // java.util.Iterator
                public void remove() {
                    throw new UnsupportedOperationException();
                }
            };
        }
    }

    public ContentTypeImpl getContentType() {
        return this.contentType;
    }
}
