package com.sun.xml.internal.ws.message;

import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.ws.api.message.Attachment;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.activation.DataHandler;
import javax.xml.soap.AttachmentPart;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.WebServiceException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/message/DataHandlerAttachment.class */
public final class DataHandlerAttachment implements Attachment {
    private final DataHandler dh;
    private final String contentId;
    String contentIdNoAngleBracket;

    public DataHandlerAttachment(@NotNull String contentId, @NotNull DataHandler dh) {
        this.dh = dh;
        this.contentId = contentId;
    }

    @Override // com.sun.xml.internal.ws.api.message.Attachment
    public String getContentId() {
        if (this.contentIdNoAngleBracket == null) {
            this.contentIdNoAngleBracket = this.contentId;
            if (this.contentIdNoAngleBracket != null && this.contentIdNoAngleBracket.charAt(0) == '<') {
                this.contentIdNoAngleBracket = this.contentIdNoAngleBracket.substring(1, this.contentIdNoAngleBracket.length() - 1);
            }
        }
        return this.contentIdNoAngleBracket;
    }

    @Override // com.sun.xml.internal.ws.api.message.Attachment
    public String getContentType() {
        return this.dh.getContentType();
    }

    @Override // com.sun.xml.internal.ws.api.message.Attachment
    public byte[] asByteArray() {
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            this.dh.writeTo(os);
            return os.toByteArray();
        } catch (IOException e2) {
            throw new WebServiceException(e2);
        }
    }

    @Override // com.sun.xml.internal.ws.api.message.Attachment
    public DataHandler asDataHandler() {
        return this.dh;
    }

    @Override // com.sun.xml.internal.ws.api.message.Attachment
    public Source asSource() {
        try {
            return new StreamSource(this.dh.getInputStream());
        } catch (IOException e2) {
            throw new WebServiceException(e2);
        }
    }

    @Override // com.sun.xml.internal.ws.api.message.Attachment
    public InputStream asInputStream() {
        try {
            return this.dh.getInputStream();
        } catch (IOException e2) {
            throw new WebServiceException(e2);
        }
    }

    @Override // com.sun.xml.internal.ws.api.message.Attachment
    public void writeTo(OutputStream os) throws IOException {
        this.dh.writeTo(os);
    }

    @Override // com.sun.xml.internal.ws.api.message.Attachment
    public void writeTo(SOAPMessage saaj) throws SOAPException {
        AttachmentPart part = saaj.createAttachmentPart();
        part.setDataHandler(this.dh);
        part.setContentId(this.contentId);
        saaj.addAttachmentPart(part);
    }
}
