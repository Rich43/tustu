package com.sun.xml.internal.ws.message.stream;

import com.sun.xml.internal.org.jvnet.staxex.Base64Data;
import com.sun.xml.internal.ws.api.message.Attachment;
import com.sun.xml.internal.ws.encoding.DataSourceStreamingDataHandler;
import com.sun.xml.internal.ws.util.ByteArrayBuffer;
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

/* loaded from: rt.jar:com/sun/xml/internal/ws/message/stream/StreamAttachment.class */
public class StreamAttachment implements Attachment {
    private final String contentId;
    private final String contentType;
    private final ByteArrayBuffer byteArrayBuffer;
    private final byte[] data;
    private final int len;

    public StreamAttachment(ByteArrayBuffer buffer, String contentId, String contentType) {
        this.contentId = contentId;
        this.contentType = contentType;
        this.byteArrayBuffer = buffer;
        this.data = this.byteArrayBuffer.getRawData();
        this.len = this.byteArrayBuffer.size();
    }

    @Override // com.sun.xml.internal.ws.api.message.Attachment
    public String getContentId() {
        return this.contentId;
    }

    @Override // com.sun.xml.internal.ws.api.message.Attachment
    public String getContentType() {
        return this.contentType;
    }

    @Override // com.sun.xml.internal.ws.api.message.Attachment
    public byte[] asByteArray() {
        return this.byteArrayBuffer.toByteArray();
    }

    @Override // com.sun.xml.internal.ws.api.message.Attachment
    public DataHandler asDataHandler() {
        return new DataSourceStreamingDataHandler(new ByteArrayDataSource(this.data, 0, this.len, getContentType()));
    }

    @Override // com.sun.xml.internal.ws.api.message.Attachment
    public Source asSource() {
        return new StreamSource(new ByteArrayInputStream(this.data, 0, this.len));
    }

    @Override // com.sun.xml.internal.ws.api.message.Attachment
    public InputStream asInputStream() {
        return this.byteArrayBuffer.newInputStream();
    }

    public Base64Data asBase64Data() {
        Base64Data base64Data = new Base64Data();
        base64Data.set(this.data, this.len, this.contentType);
        return base64Data;
    }

    @Override // com.sun.xml.internal.ws.api.message.Attachment
    public void writeTo(OutputStream os) throws IOException {
        this.byteArrayBuffer.writeTo(os);
    }

    @Override // com.sun.xml.internal.ws.api.message.Attachment
    public void writeTo(SOAPMessage saaj) throws SOAPException {
        AttachmentPart part = saaj.createAttachmentPart();
        part.setRawContentBytes(this.data, 0, this.len, getContentType());
        part.setContentId(this.contentId);
        saaj.addAttachmentPart(part);
    }
}
