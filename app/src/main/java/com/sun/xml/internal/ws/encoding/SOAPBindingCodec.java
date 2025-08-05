package com.sun.xml.internal.ws.encoding;

import com.sun.xml.internal.ws.api.SOAPVersion;
import com.sun.xml.internal.ws.api.WSFeatureList;
import com.sun.xml.internal.ws.api.client.SelectOptimalEncodingFeature;
import com.sun.xml.internal.ws.api.fastinfoset.FastInfosetFeature;
import com.sun.xml.internal.ws.api.message.ExceptionHasMessage;
import com.sun.xml.internal.ws.api.message.Message;
import com.sun.xml.internal.ws.api.message.Packet;
import com.sun.xml.internal.ws.api.pipe.Codec;
import com.sun.xml.internal.ws.api.pipe.Codecs;
import com.sun.xml.internal.ws.binding.WebServiceFeatureList;
import com.sun.xml.internal.ws.client.ContentNegotiation;
import com.sun.xml.internal.ws.protocol.soap.MessageCreationException;
import com.sun.xml.internal.ws.resources.StreamingMessages;
import com.sun.xml.internal.ws.server.UnsupportedMediaException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.soap.MTOMFeature;

/* loaded from: rt.jar:com/sun/xml/internal/ws/encoding/SOAPBindingCodec.class */
public class SOAPBindingCodec extends MimeCodec implements com.sun.xml.internal.ws.api.pipe.SOAPBindingCodec {
    public static final String UTF8_ENCODING = "utf-8";
    public static final String DEFAULT_ENCODING = "utf-8";
    private boolean isFastInfosetDisabled;
    private boolean useFastInfosetForEncoding;
    private boolean ignoreContentNegotiationProperty;
    private final com.sun.xml.internal.ws.api.pipe.StreamSOAPCodec xmlSoapCodec;
    private final Codec fiSoapCodec;
    private final MimeCodec xmlMtomCodec;
    private final MimeCodec xmlSwaCodec;
    private final MimeCodec fiSwaCodec;
    private final String xmlMimeType;
    private final String fiMimeType;
    private final String xmlAccept;
    private final String connegXmlAccept;

    @Override // com.sun.xml.internal.ws.api.pipe.SOAPBindingCodec
    public com.sun.xml.internal.ws.api.pipe.StreamSOAPCodec getXMLCodec() {
        return this.xmlSoapCodec;
    }

    private ContentTypeImpl setAcceptHeader(Packet p2, ContentTypeImpl c2) {
        String _accept;
        if (!this.ignoreContentNegotiationProperty && p2.contentNegotiation != ContentNegotiation.none) {
            _accept = this.connegXmlAccept;
        } else {
            _accept = this.xmlAccept;
        }
        c2.setAcceptHeader(_accept);
        return c2;
    }

    public SOAPBindingCodec(WSFeatureList features) {
        this(features, Codecs.createSOAPEnvelopeXmlCodec(features));
    }

    public SOAPBindingCodec(WSFeatureList features, com.sun.xml.internal.ws.api.pipe.StreamSOAPCodec xmlSoapCodec) {
        super(WebServiceFeatureList.getSoapVersion(features), features);
        this.xmlSoapCodec = xmlSoapCodec;
        this.xmlMimeType = xmlSoapCodec.getMimeType();
        this.xmlMtomCodec = new MtomCodec(this.version, xmlSoapCodec, features);
        this.xmlSwaCodec = new SwACodec(this.version, features, xmlSoapCodec);
        String clientAcceptedContentTypes = xmlSoapCodec.getMimeType() + ", " + this.xmlMtomCodec.getMimeType();
        WebServiceFeature fi = features.get(FastInfosetFeature.class);
        this.isFastInfosetDisabled = (fi == null || fi.isEnabled()) ? false : true;
        if (!this.isFastInfosetDisabled) {
            this.fiSoapCodec = getFICodec(xmlSoapCodec, this.version);
            if (this.fiSoapCodec != null) {
                this.fiMimeType = this.fiSoapCodec.getMimeType();
                this.fiSwaCodec = new SwACodec(this.version, features, this.fiSoapCodec);
                this.connegXmlAccept = this.fiMimeType + ", " + clientAcceptedContentTypes;
                WebServiceFeature select = features.get(SelectOptimalEncodingFeature.class);
                if (select != null) {
                    this.ignoreContentNegotiationProperty = true;
                    if (select.isEnabled()) {
                        if (fi != null) {
                            this.useFastInfosetForEncoding = true;
                        }
                        clientAcceptedContentTypes = this.connegXmlAccept;
                    } else {
                        this.isFastInfosetDisabled = true;
                    }
                }
            } else {
                this.isFastInfosetDisabled = true;
                this.fiSwaCodec = null;
                this.fiMimeType = "";
                this.connegXmlAccept = clientAcceptedContentTypes;
                this.ignoreContentNegotiationProperty = true;
            }
        } else {
            this.fiSwaCodec = null;
            this.fiSoapCodec = null;
            this.fiMimeType = "";
            this.connegXmlAccept = clientAcceptedContentTypes;
            this.ignoreContentNegotiationProperty = true;
        }
        this.xmlAccept = clientAcceptedContentTypes;
        if (WebServiceFeatureList.getSoapVersion(features) == null) {
            throw new WebServiceException("Expecting a SOAP binding but found ");
        }
    }

    @Override // com.sun.xml.internal.ws.encoding.MimeCodec, com.sun.xml.internal.ws.api.pipe.Codec
    public String getMimeType() {
        return null;
    }

    @Override // com.sun.xml.internal.ws.encoding.MimeCodec, com.sun.xml.internal.ws.api.pipe.Codec
    public com.sun.xml.internal.ws.api.pipe.ContentType getStaticContentType(Packet packet) {
        com.sun.xml.internal.ws.api.pipe.ContentType toAdapt = getEncoder(packet).getStaticContentType(packet);
        return setAcceptHeader(packet, (ContentTypeImpl) toAdapt);
    }

    @Override // com.sun.xml.internal.ws.encoding.MimeCodec, com.sun.xml.internal.ws.api.pipe.Codec
    public com.sun.xml.internal.ws.api.pipe.ContentType encode(Packet packet, OutputStream out) throws IOException {
        preEncode(packet);
        com.sun.xml.internal.ws.api.pipe.ContentType ct = getEncoder(packet).encode(packet, out);
        com.sun.xml.internal.ws.api.pipe.ContentType ct2 = setAcceptHeader(packet, (ContentTypeImpl) ct);
        postEncode();
        return ct2;
    }

    @Override // com.sun.xml.internal.ws.api.pipe.Codec
    public com.sun.xml.internal.ws.api.pipe.ContentType encode(Packet packet, WritableByteChannel buffer) {
        preEncode(packet);
        com.sun.xml.internal.ws.api.pipe.ContentType ct = getEncoder(packet).encode(packet, buffer);
        com.sun.xml.internal.ws.api.pipe.ContentType ct2 = setAcceptHeader(packet, (ContentTypeImpl) ct);
        postEncode();
        return ct2;
    }

    private void preEncode(Packet p2) {
    }

    private void postEncode() {
    }

    private void preDecode(Packet p2) {
        if (p2.contentNegotiation == null) {
            this.useFastInfosetForEncoding = false;
        }
    }

    private void postDecode(Packet p2) {
        p2.setFastInfosetDisabled(this.isFastInfosetDisabled);
        if (this.features.isEnabled(MTOMFeature.class)) {
            p2.checkMtomAcceptable();
        }
        MTOMFeature mtomFeature = (MTOMFeature) this.features.get(MTOMFeature.class);
        if (mtomFeature != null) {
            p2.setMtomFeature(mtomFeature);
        }
        if (!this.useFastInfosetForEncoding) {
            this.useFastInfosetForEncoding = p2.getFastInfosetAcceptable(this.fiMimeType).booleanValue();
        }
    }

    @Override // com.sun.xml.internal.ws.encoding.MimeCodec, com.sun.xml.internal.ws.api.pipe.Codec
    public void decode(InputStream in, String contentType, Packet packet) throws IOException {
        if (contentType == null) {
            contentType = this.xmlMimeType;
        }
        packet.setContentType(new ContentTypeImpl(contentType));
        preDecode(packet);
        try {
            if (isMultipartRelated(contentType)) {
                super.decode(in, contentType, packet);
            } else if (isFastInfoset(contentType)) {
                if (!this.ignoreContentNegotiationProperty && packet.contentNegotiation == ContentNegotiation.none) {
                    throw noFastInfosetForDecoding();
                }
                this.useFastInfosetForEncoding = true;
                this.fiSoapCodec.decode(in, contentType, packet);
            } else {
                this.xmlSoapCodec.decode(in, contentType, packet);
            }
            postDecode(packet);
        } catch (RuntimeException we) {
            if ((we instanceof ExceptionHasMessage) || (we instanceof UnsupportedMediaException)) {
                throw we;
            }
            throw new MessageCreationException(this.version, we);
        }
    }

    @Override // com.sun.xml.internal.ws.encoding.MimeCodec, com.sun.xml.internal.ws.api.pipe.Codec
    public void decode(ReadableByteChannel in, String contentType, Packet packet) {
        if (contentType == null) {
            throw new UnsupportedMediaException();
        }
        preDecode(packet);
        try {
            if (isMultipartRelated(contentType)) {
                super.decode(in, contentType, packet);
            } else if (isFastInfoset(contentType)) {
                if (packet.contentNegotiation == ContentNegotiation.none) {
                    throw noFastInfosetForDecoding();
                }
                this.useFastInfosetForEncoding = true;
                this.fiSoapCodec.decode(in, contentType, packet);
            } else {
                this.xmlSoapCodec.decode(in, contentType, packet);
            }
            postDecode(packet);
        } catch (RuntimeException we) {
            if ((we instanceof ExceptionHasMessage) || (we instanceof UnsupportedMediaException)) {
                throw we;
            }
            throw new MessageCreationException(this.version, we);
        }
    }

    @Override // com.sun.xml.internal.ws.encoding.MimeCodec, com.sun.xml.internal.ws.api.pipe.Codec
    public SOAPBindingCodec copy() {
        return new SOAPBindingCodec(this.features, (com.sun.xml.internal.ws.api.pipe.StreamSOAPCodec) this.xmlSoapCodec.copy());
    }

    @Override // com.sun.xml.internal.ws.encoding.MimeCodec
    protected void decode(MimeMultipartParser mpp, Packet packet) throws IOException {
        String rootContentType = mpp.getRootPart().getContentType();
        boolean isMTOM = isApplicationXopXml(rootContentType);
        packet.setMtomRequest(Boolean.valueOf(isMTOM));
        if (isMTOM) {
            this.xmlMtomCodec.decode(mpp, packet);
            return;
        }
        if (isFastInfoset(rootContentType)) {
            if (packet.contentNegotiation == ContentNegotiation.none) {
                throw noFastInfosetForDecoding();
            }
            this.useFastInfosetForEncoding = true;
            this.fiSwaCodec.decode(mpp, packet);
            return;
        }
        if (isXml(rootContentType)) {
            this.xmlSwaCodec.decode(mpp, packet);
            return;
        }
        throw new IOException("");
    }

    private boolean isMultipartRelated(String contentType) {
        return compareStrings(contentType, MimeCodec.MULTIPART_RELATED_MIME_TYPE);
    }

    private boolean isApplicationXopXml(String contentType) {
        return compareStrings(contentType, MtomCodec.XOP_XML_MIME_TYPE);
    }

    private boolean isXml(String contentType) {
        return compareStrings(contentType, this.xmlMimeType);
    }

    private boolean isFastInfoset(String contentType) {
        if (this.isFastInfosetDisabled) {
            return false;
        }
        return compareStrings(contentType, this.fiMimeType);
    }

    private boolean compareStrings(String a2, String b2) {
        return a2.length() >= b2.length() && b2.equalsIgnoreCase(a2.substring(0, b2.length()));
    }

    private Codec getEncoder(Packet p2) {
        if (!this.ignoreContentNegotiationProperty) {
            if (p2.contentNegotiation == ContentNegotiation.none) {
                this.useFastInfosetForEncoding = false;
            } else if (p2.contentNegotiation == ContentNegotiation.optimistic) {
                this.useFastInfosetForEncoding = true;
            }
        }
        if (this.useFastInfosetForEncoding) {
            Message m2 = p2.getMessage();
            if (m2 == null || m2.getAttachments().isEmpty() || this.features.isEnabled(MTOMFeature.class)) {
                return this.fiSoapCodec;
            }
            return this.fiSwaCodec;
        }
        if (p2.getBinding() == null && this.features != null) {
            p2.setMtomFeature((MTOMFeature) this.features.get(MTOMFeature.class));
        }
        if (p2.shouldUseMtom()) {
            return this.xmlMtomCodec;
        }
        Message m3 = p2.getMessage();
        if (m3 == null || m3.getAttachments().isEmpty()) {
            return this.xmlSoapCodec;
        }
        return this.xmlSwaCodec;
    }

    private RuntimeException noFastInfosetForDecoding() {
        return new RuntimeException(StreamingMessages.FASTINFOSET_DECODING_NOT_ACCEPTED());
    }

    private static Codec getFICodec(com.sun.xml.internal.ws.api.pipe.StreamSOAPCodec soapCodec, SOAPVersion version) {
        try {
            Class c2 = Class.forName("com.sun.xml.internal.ws.encoding.fastinfoset.FastInfosetStreamSOAPCodec");
            Method m2 = c2.getMethod("create", com.sun.xml.internal.ws.api.pipe.StreamSOAPCodec.class, SOAPVersion.class);
            return (Codec) m2.invoke(null, soapCodec, version);
        } catch (Exception e2) {
            return null;
        }
    }
}
