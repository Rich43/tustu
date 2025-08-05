package com.sun.xml.internal.ws.encoding.xml;

import com.sun.xml.internal.ws.api.WSFeatureList;
import com.sun.xml.internal.ws.api.message.Message;
import com.sun.xml.internal.ws.api.message.Packet;
import com.sun.xml.internal.ws.api.pipe.Codec;
import com.sun.xml.internal.ws.api.pipe.ContentType;
import com.sun.xml.internal.ws.api.streaming.XMLStreamWriterFactory;
import com.sun.xml.internal.ws.encoding.ContentTypeImpl;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.ws.WebServiceException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/encoding/xml/XMLCodec.class */
public final class XMLCodec implements Codec {
    public static final String XML_APPLICATION_MIME_TYPE = "application/xml";
    public static final String XML_TEXT_MIME_TYPE = "text/xml";
    private static final ContentType contentType = new ContentTypeImpl("text/xml");
    private WSFeatureList features;

    public XMLCodec(WSFeatureList f2) {
        this.features = f2;
    }

    @Override // com.sun.xml.internal.ws.api.pipe.Codec
    public String getMimeType() {
        return XML_APPLICATION_MIME_TYPE;
    }

    @Override // com.sun.xml.internal.ws.api.pipe.Codec
    public ContentType getStaticContentType(Packet packet) {
        return contentType;
    }

    @Override // com.sun.xml.internal.ws.api.pipe.Codec
    public ContentType encode(Packet packet, OutputStream out) {
        XMLStreamWriter writer;
        String encoding = (String) packet.invocationProperties.get(XMLConstants.OUTPUT_XML_CHARACTER_ENCODING);
        if (encoding != null && encoding.length() > 0) {
            writer = XMLStreamWriterFactory.create(out, encoding);
        } else {
            writer = XMLStreamWriterFactory.create(out);
        }
        try {
            if (packet.getMessage().hasPayload()) {
                writer.writeStartDocument();
                packet.getMessage().writePayloadTo(writer);
                writer.flush();
            }
            return contentType;
        } catch (XMLStreamException e2) {
            throw new WebServiceException(e2);
        }
    }

    @Override // com.sun.xml.internal.ws.api.pipe.Codec
    public ContentType encode(Packet packet, WritableByteChannel buffer) {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.xml.internal.ws.api.pipe.Codec
    public Codec copy() {
        return this;
    }

    @Override // com.sun.xml.internal.ws.api.pipe.Codec
    public void decode(InputStream in, String contentType2, Packet packet) throws IOException {
        Message message = XMLMessage.create(contentType2, in, this.features);
        packet.setMessage(message);
    }

    @Override // com.sun.xml.internal.ws.api.pipe.Codec
    public void decode(ReadableByteChannel in, String contentType2, Packet packet) {
        throw new UnsupportedOperationException();
    }
}
