package com.sun.xml.internal.ws.encoding;

import com.sun.xml.internal.ws.api.SOAPVersion;
import com.sun.xml.internal.ws.api.WSBinding;
import com.sun.xml.internal.ws.api.WSFeatureList;
import com.sun.xml.internal.ws.api.message.Packet;
import com.sun.xml.internal.ws.encoding.ContentTypeImpl;
import java.util.Collections;
import java.util.List;

/* loaded from: rt.jar:com/sun/xml/internal/ws/encoding/StreamSOAP11Codec.class */
final class StreamSOAP11Codec extends StreamSOAPCodec {
    public static final String SOAP11_MIME_TYPE = "text/xml";
    public static final String DEFAULT_SOAP11_CONTENT_TYPE = "text/xml; charset=utf-8";
    private static final List<String> EXPECTED_CONTENT_TYPES = Collections.singletonList("text/xml");

    StreamSOAP11Codec() {
        super(SOAPVersion.SOAP_11);
    }

    StreamSOAP11Codec(WSBinding binding) {
        super(binding);
    }

    StreamSOAP11Codec(WSFeatureList features) {
        super(features);
    }

    @Override // com.sun.xml.internal.ws.api.pipe.Codec
    public String getMimeType() {
        return "text/xml";
    }

    @Override // com.sun.xml.internal.ws.encoding.StreamSOAPCodec
    protected com.sun.xml.internal.ws.api.pipe.ContentType getContentType(Packet packet) {
        ContentTypeImpl.Builder b2 = getContenTypeBuilder(packet);
        b2.soapAction = packet.soapAction;
        return b2.build();
    }

    @Override // com.sun.xml.internal.ws.encoding.StreamSOAPCodec
    protected String getDefaultContentType() {
        return DEFAULT_SOAP11_CONTENT_TYPE;
    }

    @Override // com.sun.xml.internal.ws.encoding.StreamSOAPCodec
    protected List<String> getExpectedContentTypes() {
        return EXPECTED_CONTENT_TYPES;
    }
}
