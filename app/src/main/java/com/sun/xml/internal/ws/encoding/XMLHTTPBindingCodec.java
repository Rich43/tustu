package com.sun.xml.internal.ws.encoding;

import com.sun.xml.internal.ws.api.SOAPVersion;
import com.sun.xml.internal.ws.api.WSFeatureList;
import com.sun.xml.internal.ws.api.message.Packet;
import com.sun.xml.internal.ws.api.pipe.Codec;
import com.sun.xml.internal.ws.client.ContentNegotiation;
import com.sun.xml.internal.ws.encoding.xml.XMLCodec;
import com.sun.xml.internal.ws.encoding.xml.XMLMessage;
import com.sun.xml.internal.ws.resources.StreamingMessages;
import com.sun.xml.internal.ws.util.ByteArrayBuffer;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.StringTokenizer;
import javax.activation.DataSource;
import javax.xml.ws.WebServiceException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/encoding/XMLHTTPBindingCodec.class */
public final class XMLHTTPBindingCodec extends MimeCodec {
    private static final String BASE_ACCEPT_VALUE = "*";
    private static final String APPLICATION_FAST_INFOSET_MIME_TYPE = "application/fastinfoset";
    private boolean useFastInfosetForEncoding;
    private final Codec xmlCodec;
    private final Codec fiCodec;
    private static final String xmlAccept = null;
    private static final String fiXmlAccept = "application/fastinfoset, *";

    @Override // com.sun.xml.internal.ws.encoding.MimeCodec, com.sun.xml.internal.ws.api.pipe.Codec
    public /* bridge */ /* synthetic */ void decode(ReadableByteChannel readableByteChannel, String str, Packet packet) {
        super.decode(readableByteChannel, str, packet);
    }

    private ContentTypeImpl setAcceptHeader(Packet p2, com.sun.xml.internal.ws.api.pipe.ContentType c2) {
        ContentTypeImpl ctImpl = (ContentTypeImpl) c2;
        if (p2.contentNegotiation == ContentNegotiation.optimistic || p2.contentNegotiation == ContentNegotiation.pessimistic) {
            ctImpl.setAcceptHeader(fiXmlAccept);
        } else {
            ctImpl.setAcceptHeader(xmlAccept);
        }
        p2.setContentType(ctImpl);
        return ctImpl;
    }

    public XMLHTTPBindingCodec(WSFeatureList f2) {
        super(SOAPVersion.SOAP_11, f2);
        this.xmlCodec = new XMLCodec(f2);
        this.fiCodec = getFICodec();
    }

    @Override // com.sun.xml.internal.ws.encoding.MimeCodec, com.sun.xml.internal.ws.api.pipe.Codec
    public String getMimeType() {
        return null;
    }

    @Override // com.sun.xml.internal.ws.encoding.MimeCodec, com.sun.xml.internal.ws.api.pipe.Codec
    public com.sun.xml.internal.ws.api.pipe.ContentType getStaticContentType(Packet packet) {
        if (packet.getInternalMessage() instanceof XMLMessage.MessageDataSource) {
            XMLMessage.MessageDataSource mds = (XMLMessage.MessageDataSource) packet.getInternalMessage();
            if (mds.hasUnconsumedDataSource()) {
                com.sun.xml.internal.ws.api.pipe.ContentType ct = getStaticContentType(mds);
                if (ct != null) {
                    return setAcceptHeader(packet, ct);
                }
                return null;
            }
        }
        com.sun.xml.internal.ws.api.pipe.ContentType ct2 = super.getStaticContentType(packet);
        if (ct2 != null) {
            return setAcceptHeader(packet, ct2);
        }
        return null;
    }

    @Override // com.sun.xml.internal.ws.encoding.MimeCodec, com.sun.xml.internal.ws.api.pipe.Codec
    public com.sun.xml.internal.ws.api.pipe.ContentType encode(Packet packet, OutputStream out) throws IOException {
        if (packet.getInternalMessage() instanceof XMLMessage.MessageDataSource) {
            XMLMessage.MessageDataSource mds = (XMLMessage.MessageDataSource) packet.getInternalMessage();
            if (mds.hasUnconsumedDataSource()) {
                return setAcceptHeader(packet, encode(mds, out));
            }
        }
        return setAcceptHeader(packet, super.encode(packet, out));
    }

    @Override // com.sun.xml.internal.ws.api.pipe.Codec
    public com.sun.xml.internal.ws.api.pipe.ContentType encode(Packet packet, WritableByteChannel buffer) {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.xml.internal.ws.encoding.MimeCodec, com.sun.xml.internal.ws.api.pipe.Codec
    public void decode(InputStream in, String contentType, Packet packet) throws IOException {
        if (packet.contentNegotiation == null) {
            this.useFastInfosetForEncoding = false;
        }
        if (contentType == null) {
            this.xmlCodec.decode(in, contentType, packet);
        } else if (isMultipartRelated(contentType)) {
            packet.setMessage(new XMLMessage.XMLMultiPart(contentType, in, this.features));
        } else if (isFastInfoset(contentType)) {
            if (this.fiCodec == null) {
                throw new RuntimeException(StreamingMessages.FASTINFOSET_NO_IMPLEMENTATION());
            }
            this.useFastInfosetForEncoding = true;
            this.fiCodec.decode(in, contentType, packet);
        } else if (isXml(contentType)) {
            this.xmlCodec.decode(in, contentType, packet);
        } else {
            packet.setMessage(new XMLMessage.UnknownContent(contentType, in));
        }
        if (!this.useFastInfosetForEncoding) {
            this.useFastInfosetForEncoding = isFastInfosetAcceptable(packet.acceptableMimeTypes);
        }
    }

    @Override // com.sun.xml.internal.ws.encoding.MimeCodec
    protected void decode(MimeMultipartParser mpp, Packet packet) throws IOException {
    }

    @Override // com.sun.xml.internal.ws.encoding.MimeCodec, com.sun.xml.internal.ws.api.pipe.Codec
    public MimeCodec copy() {
        return new XMLHTTPBindingCodec(this.features);
    }

    private boolean isMultipartRelated(String contentType) {
        return compareStrings(contentType, MimeCodec.MULTIPART_RELATED_MIME_TYPE);
    }

    private boolean isXml(String contentType) {
        return compareStrings(contentType, XMLCodec.XML_APPLICATION_MIME_TYPE) || compareStrings(contentType, "text/xml") || (compareStrings(contentType, "application/") && contentType.toLowerCase().indexOf("+xml") != -1);
    }

    private boolean isFastInfoset(String contentType) {
        return compareStrings(contentType, "application/fastinfoset");
    }

    private boolean compareStrings(String a2, String b2) {
        return a2.length() >= b2.length() && b2.equalsIgnoreCase(a2.substring(0, b2.length()));
    }

    private boolean isFastInfosetAcceptable(String accept) {
        if (accept == null) {
            return false;
        }
        StringTokenizer st = new StringTokenizer(accept, ",");
        while (st.hasMoreTokens()) {
            String token = st.nextToken().trim();
            if (token.equalsIgnoreCase("application/fastinfoset")) {
                return true;
            }
        }
        return false;
    }

    private com.sun.xml.internal.ws.api.pipe.ContentType getStaticContentType(XMLMessage.MessageDataSource mds) {
        String contentType = mds.getDataSource().getContentType();
        boolean isFastInfoset = XMLMessage.isFastInfoset(contentType);
        if (!requiresTransformationOfDataSource(isFastInfoset, this.useFastInfosetForEncoding)) {
            return new ContentTypeImpl(contentType);
        }
        return null;
    }

    private com.sun.xml.internal.ws.api.pipe.ContentType encode(XMLMessage.MessageDataSource mds, OutputStream out) {
        try {
            boolean isFastInfoset = XMLMessage.isFastInfoset(mds.getDataSource().getContentType());
            DataSource ds = transformDataSource(mds.getDataSource(), isFastInfoset, this.useFastInfosetForEncoding, this.features);
            InputStream is = ds.getInputStream();
            byte[] buf = new byte[1024];
            while (true) {
                int count = is.read(buf);
                if (count != -1) {
                    out.write(buf, 0, count);
                } else {
                    return new ContentTypeImpl(ds.getContentType());
                }
            }
        } catch (IOException ioe) {
            throw new WebServiceException(ioe);
        }
    }

    @Override // com.sun.xml.internal.ws.encoding.MimeCodec
    protected Codec getMimeRootCodec(Packet p2) {
        if (p2.contentNegotiation == ContentNegotiation.none) {
            this.useFastInfosetForEncoding = false;
        } else if (p2.contentNegotiation == ContentNegotiation.optimistic) {
            this.useFastInfosetForEncoding = true;
        }
        return (!this.useFastInfosetForEncoding || this.fiCodec == null) ? this.xmlCodec : this.fiCodec;
    }

    public static boolean requiresTransformationOfDataSource(boolean isFastInfoset, boolean useFastInfoset) {
        return (isFastInfoset && !useFastInfoset) || (!isFastInfoset && useFastInfoset);
    }

    public static DataSource transformDataSource(DataSource in, boolean isFastInfoset, boolean useFastInfoset, WSFeatureList f2) {
        try {
            if (isFastInfoset && !useFastInfoset) {
                Codec codec = new XMLHTTPBindingCodec(f2);
                Packet p2 = new Packet();
                codec.decode(in.getInputStream(), in.getContentType(), p2);
                p2.getMessage().getAttachments();
                codec.getStaticContentType(p2);
                ByteArrayBuffer bos = new ByteArrayBuffer();
                com.sun.xml.internal.ws.api.pipe.ContentType ct = codec.encode(p2, bos);
                return XMLMessage.createDataSource(ct.getContentType(), bos.newInputStream());
            }
            if (!isFastInfoset && useFastInfoset) {
                Codec codec2 = new XMLHTTPBindingCodec(f2);
                Packet p3 = new Packet();
                codec2.decode(in.getInputStream(), in.getContentType(), p3);
                p3.contentNegotiation = ContentNegotiation.optimistic;
                p3.getMessage().getAttachments();
                codec2.getStaticContentType(p3);
                ByteArrayBuffer bos2 = new ByteArrayBuffer();
                com.sun.xml.internal.ws.api.pipe.ContentType ct2 = codec2.encode(p3, bos2);
                return XMLMessage.createDataSource(ct2.getContentType(), bos2.newInputStream());
            }
            return in;
        } catch (Exception ex) {
            throw new WebServiceException(ex);
        }
    }

    private static Codec getFICodec() {
        try {
            Class c2 = Class.forName("com.sun.xml.internal.ws.encoding.fastinfoset.FastInfosetCodec");
            Method m2 = c2.getMethod("create", new Class[0]);
            return (Codec) m2.invoke(null, new Object[0]);
        } catch (Exception e2) {
            return null;
        }
    }
}
