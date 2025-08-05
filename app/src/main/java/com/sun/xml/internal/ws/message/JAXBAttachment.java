package com.sun.xml.internal.ws.message;

import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.ws.api.message.Attachment;
import com.sun.xml.internal.ws.encoding.DataSourceStreamingDataHandler;
import com.sun.xml.internal.ws.spi.db.XMLBridge;
import com.sun.xml.internal.ws.util.ByteArrayBuffer;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.xml.bind.JAXBException;
import javax.xml.soap.AttachmentPart;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.WebServiceException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/message/JAXBAttachment.class */
public final class JAXBAttachment implements Attachment, DataSource {
    private final String contentId;
    private final String mimeType;
    private final Object jaxbObject;
    private final XMLBridge bridge;

    public JAXBAttachment(@NotNull String contentId, Object jaxbObject, XMLBridge bridge, String mimeType) {
        this.contentId = contentId;
        this.jaxbObject = jaxbObject;
        this.bridge = bridge;
        this.mimeType = mimeType;
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
        ByteArrayBuffer bab = new ByteArrayBuffer();
        try {
            writeTo(bab);
            return bab.getRawData();
        } catch (IOException e2) {
            throw new WebServiceException(e2);
        }
    }

    @Override // com.sun.xml.internal.ws.api.message.Attachment
    public DataHandler asDataHandler() {
        return new DataSourceStreamingDataHandler(this);
    }

    @Override // com.sun.xml.internal.ws.api.message.Attachment
    public Source asSource() {
        return new StreamSource(asInputStream());
    }

    @Override // com.sun.xml.internal.ws.api.message.Attachment
    public InputStream asInputStream() {
        ByteArrayBuffer bab = new ByteArrayBuffer();
        try {
            writeTo(bab);
            return bab.newInputStream();
        } catch (IOException e2) {
            throw new WebServiceException(e2);
        }
    }

    @Override // com.sun.xml.internal.ws.api.message.Attachment
    public void writeTo(OutputStream os) throws IOException {
        try {
            this.bridge.marshal(this.jaxbObject, os, null, null);
        } catch (JAXBException e2) {
            throw new WebServiceException(e2);
        }
    }

    @Override // com.sun.xml.internal.ws.api.message.Attachment
    public void writeTo(SOAPMessage saaj) throws SOAPException {
        AttachmentPart part = saaj.createAttachmentPart();
        part.setDataHandler(asDataHandler());
        part.setContentId(this.contentId);
        saaj.addAttachmentPart(part);
    }

    @Override // javax.activation.DataSource
    public InputStream getInputStream() throws IOException {
        return asInputStream();
    }

    @Override // javax.activation.DataSource
    public OutputStream getOutputStream() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override // javax.activation.DataSource
    public String getName() {
        return null;
    }
}
