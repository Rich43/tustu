package com.sun.xml.internal.ws.encoding;

import com.oracle.webservices.internal.impl.encoding.StreamDecoderImpl;
import com.oracle.webservices.internal.impl.internalspi.encoding.StreamDecoder;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.ws.api.SOAPVersion;
import com.sun.xml.internal.ws.api.WSBinding;
import com.sun.xml.internal.ws.api.WSFeatureList;
import com.sun.xml.internal.ws.api.message.AttachmentSet;
import com.sun.xml.internal.ws.api.message.Message;
import com.sun.xml.internal.ws.api.message.Packet;
import com.sun.xml.internal.ws.api.streaming.XMLStreamWriterFactory;
import com.sun.xml.internal.ws.binding.WebServiceFeatureList;
import com.sun.xml.internal.ws.developer.SerializationFeature;
import com.sun.xml.internal.ws.encoding.ContentTypeImpl;
import com.sun.xml.internal.ws.message.AttachmentSetImpl;
import com.sun.xml.internal.ws.message.stream.StreamMessage;
import com.sun.xml.internal.ws.protocol.soap.VersionMismatchException;
import com.sun.xml.internal.ws.server.UnsupportedMediaException;
import com.sun.xml.internal.ws.streaming.XMLStreamReaderUtil;
import com.sun.xml.internal.ws.util.ServiceFinder;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.ws.WebServiceException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/encoding/StreamSOAPCodec.class */
public abstract class StreamSOAPCodec implements com.sun.xml.internal.ws.api.pipe.StreamSOAPCodec, RootOnlyCodec {
    private static final String SOAP_ENVELOPE = "Envelope";
    private static final String SOAP_HEADER = "Header";
    private static final String SOAP_BODY = "Body";
    private final SOAPVersion soapVersion;
    protected final SerializationFeature serializationFeature;
    private final StreamDecoder streamDecoder;
    private static final String DECODED_MESSAGE_CHARSET = "decodedMessageCharset";

    protected abstract com.sun.xml.internal.ws.api.pipe.ContentType getContentType(Packet packet);

    protected abstract String getDefaultContentType();

    protected abstract List<String> getExpectedContentTypes();

    StreamSOAPCodec(SOAPVersion soapVersion) {
        this(soapVersion, null);
    }

    StreamSOAPCodec(WSBinding binding) {
        this(binding.getSOAPVersion(), (SerializationFeature) binding.getFeature(SerializationFeature.class));
    }

    StreamSOAPCodec(WSFeatureList features) {
        this(WebServiceFeatureList.getSoapVersion(features), (SerializationFeature) features.get(SerializationFeature.class));
    }

    private StreamSOAPCodec(SOAPVersion soapVersion, @Nullable SerializationFeature sf) {
        this.soapVersion = soapVersion;
        this.serializationFeature = sf;
        this.streamDecoder = selectStreamDecoder();
    }

    private StreamDecoder selectStreamDecoder() {
        Iterator it = ServiceFinder.find(StreamDecoder.class).iterator();
        if (it.hasNext()) {
            StreamDecoder sd = (StreamDecoder) it.next();
            return sd;
        }
        return new StreamDecoderImpl();
    }

    @Override // com.sun.xml.internal.ws.api.pipe.Codec
    public com.sun.xml.internal.ws.api.pipe.ContentType getStaticContentType(Packet packet) {
        return getContentType(packet);
    }

    @Override // com.sun.xml.internal.ws.api.pipe.Codec
    public com.sun.xml.internal.ws.api.pipe.ContentType encode(Packet packet, OutputStream out) {
        if (packet.getMessage() != null) {
            String encoding = getPacketEncoding(packet);
            packet.invocationProperties.remove(DECODED_MESSAGE_CHARSET);
            XMLStreamWriter writer = XMLStreamWriterFactory.create(out, encoding);
            try {
                packet.getMessage().writeTo(writer);
                writer.flush();
                XMLStreamWriterFactory.recycle(writer);
            } catch (XMLStreamException e2) {
                throw new WebServiceException(e2);
            }
        }
        return getContentType(packet);
    }

    @Override // com.sun.xml.internal.ws.api.pipe.Codec
    public com.sun.xml.internal.ws.api.pipe.ContentType encode(Packet packet, WritableByteChannel buffer) {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.xml.internal.ws.api.pipe.Codec
    public void decode(InputStream in, String contentType, Packet packet) throws IOException {
        decode(in, contentType, packet, new AttachmentSetImpl());
    }

    private static boolean isContentTypeSupported(String ct, List<String> expected) {
        for (String contentType : expected) {
            if (ct.contains(contentType)) {
                return true;
            }
        }
        return false;
    }

    @Override // com.sun.xml.internal.ws.api.pipe.StreamSOAPCodec
    @NotNull
    public final Message decode(@NotNull XMLStreamReader reader) {
        return decode(reader, new AttachmentSetImpl());
    }

    @Override // com.sun.xml.internal.ws.api.pipe.StreamSOAPCodec
    public final Message decode(XMLStreamReader reader, @NotNull AttachmentSet attachmentSet) {
        return decode(this.soapVersion, reader, attachmentSet);
    }

    public static final Message decode(SOAPVersion soapVersion, XMLStreamReader reader, @NotNull AttachmentSet attachmentSet) {
        if (reader.getEventType() != 1) {
            XMLStreamReaderUtil.nextElementContent(reader);
        }
        XMLStreamReaderUtil.verifyReaderState(reader, 1);
        if ("Envelope".equals(reader.getLocalName()) && !soapVersion.nsUri.equals(reader.getNamespaceURI())) {
            throw new VersionMismatchException(soapVersion, soapVersion.nsUri, reader.getNamespaceURI());
        }
        XMLStreamReaderUtil.verifyTag(reader, soapVersion.nsUri, "Envelope");
        return new StreamMessage(soapVersion, reader, attachmentSet);
    }

    @Override // com.sun.xml.internal.ws.api.pipe.Codec
    public void decode(ReadableByteChannel in, String contentType, Packet packet) {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.xml.internal.ws.api.pipe.Codec
    public final StreamSOAPCodec copy() {
        return this;
    }

    public void decode(InputStream in, String contentType, Packet packet, AttachmentSet att) throws IOException {
        List<String> expectedContentTypes = getExpectedContentTypes();
        if (contentType != null && !isContentTypeSupported(contentType, expectedContentTypes)) {
            throw new UnsupportedMediaException(contentType, expectedContentTypes);
        }
        com.oracle.webservices.internal.api.message.ContentType pct = packet.getInternalContentType();
        ContentTypeImpl cti = (pct == null || !(pct instanceof ContentTypeImpl)) ? new ContentTypeImpl(contentType) : (ContentTypeImpl) pct;
        String charset = cti.getCharSet();
        if (charset != null && !Charset.isSupported(charset)) {
            throw new UnsupportedMediaException(charset);
        }
        if (charset != null) {
            packet.invocationProperties.put(DECODED_MESSAGE_CHARSET, charset);
        } else {
            packet.invocationProperties.remove(DECODED_MESSAGE_CHARSET);
        }
        packet.setMessage(this.streamDecoder.decode(in, charset, att, this.soapVersion));
    }

    @Override // com.sun.xml.internal.ws.encoding.RootOnlyCodec
    public void decode(ReadableByteChannel in, String contentType, Packet response, AttachmentSet att) {
        throw new UnsupportedOperationException();
    }

    public static StreamSOAPCodec create(SOAPVersion version) {
        if (version == null) {
            throw new IllegalArgumentException();
        }
        switch (version) {
            case SOAP_11:
                return new StreamSOAP11Codec();
            case SOAP_12:
                return new StreamSOAP12Codec();
            default:
                throw new AssertionError();
        }
    }

    public static StreamSOAPCodec create(WSFeatureList features) {
        SOAPVersion version = WebServiceFeatureList.getSoapVersion(features);
        if (version == null) {
            throw new IllegalArgumentException();
        }
        switch (version) {
            case SOAP_11:
                return new StreamSOAP11Codec(features);
            case SOAP_12:
                return new StreamSOAP12Codec(features);
            default:
                throw new AssertionError();
        }
    }

    public static StreamSOAPCodec create(WSBinding binding) {
        SOAPVersion version = binding.getSOAPVersion();
        if (version == null) {
            throw new IllegalArgumentException();
        }
        switch (version) {
            case SOAP_11:
                return new StreamSOAP11Codec(binding);
            case SOAP_12:
                return new StreamSOAP12Codec(binding);
            default:
                throw new AssertionError();
        }
    }

    private String getPacketEncoding(Packet packet) {
        String charset;
        return (this.serializationFeature == null || this.serializationFeature.getEncoding() == null) ? (packet == null || packet.endpoint == null || (charset = (String) packet.invocationProperties.get(DECODED_MESSAGE_CHARSET)) == null) ? "utf-8" : charset : this.serializationFeature.getEncoding().equals("") ? "utf-8" : this.serializationFeature.getEncoding();
    }

    protected ContentTypeImpl.Builder getContenTypeBuilder(Packet packet) {
        ContentTypeImpl.Builder b2 = new ContentTypeImpl.Builder();
        String encoding = getPacketEncoding(packet);
        if ("utf-8".equalsIgnoreCase(encoding)) {
            b2.contentType = getDefaultContentType();
            b2.charset = "utf-8";
            return b2;
        }
        b2.contentType = getMimeType() + " ;charset=" + encoding;
        b2.charset = encoding;
        return b2;
    }
}
