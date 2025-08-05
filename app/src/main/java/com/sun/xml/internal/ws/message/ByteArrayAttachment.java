package com.sun.xml.internal.ws.message;

import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.ws.api.message.Attachment;
import com.sun.xml.internal.ws.encoding.DataSourceStreamingDataHandler;
import com.sun.xml.internal.ws.util.ByteArrayDataSource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.activation.DataHandler;
import javax.xml.soap.AttachmentPart;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

/* loaded from: rt.jar:com/sun/xml/internal/ws/message/ByteArrayAttachment.class */
public final class ByteArrayAttachment implements Attachment {
    private final String contentId;
    private byte[] data;
    private int start;
    private final int len;
    private final String mimeType;

    public ByteArrayAttachment(@NotNull String contentId, byte[] data, int start, int len, String mimeType) {
        this.contentId = contentId;
        this.data = data;
        this.start = start;
        this.len = len;
        this.mimeType = mimeType;
    }

    public ByteArrayAttachment(@NotNull String contentId, byte[] data, String mimeType) {
        this(contentId, data, 0, data.length, mimeType);
    }

    @Override // com.sun.xml.internal.ws.api.message.Attachment
    public String getContentId() {
        return this.contentId;
    }

    @Override // com.sun.xml.internal.ws.api.message.Attachment
    public String getContentType() {
        return this.mimeType;
    }

    @Override // com.sun.xml.internal.ws.api.message.Attachment
    public byte[] asByteArray() {
        if (this.start != 0 || this.len != this.data.length) {
            byte[] exact = new byte[this.len];
            System.arraycopy(this.data, this.start, exact, 0, this.len);
            this.start = 0;
            this.data = exact;
        }
        return this.data;
    }

    @Override // com.sun.xml.internal.ws.api.message.Attachment
    public DataHandler asDataHandler() {
        return new DataSourceStreamingDataHandler(new ByteArrayDataSource(this.data, this.start, this.len, getContentType()));
    }

    @Override // com.sun.xml.internal.ws.api.message.Attachment
    public Source asSource() {
        return new StreamSource(asInputStream());
    }

    @Override // com.sun.xml.internal.ws.api.message.Attachment
    public InputStream asInputStream() {
        return new ByteArrayInputStream(this.data, this.start, this.len);
    }

    @Override // com.sun.xml.internal.ws.api.message.Attachment
    public void writeTo(OutputStream os) throws IOException {
        os.write(asByteArray());
    }

    @Override // com.sun.xml.internal.ws.api.message.Attachment
    public void writeTo(SOAPMessage saaj) throws SOAPException {
        AttachmentPart part = saaj.createAttachmentPart();
        part.setDataHandler(asDataHandler());
        part.setContentId(this.contentId);
        saaj.addAttachmentPart(part);
    }
}
