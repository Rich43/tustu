package com.sun.xml.internal.ws.encoding;

import com.sun.xml.internal.ws.api.SOAPVersion;
import com.sun.xml.internal.ws.api.WSBinding;
import com.sun.xml.internal.ws.api.WSFeatureList;
import com.sun.xml.internal.ws.api.message.AttachmentSet;
import com.sun.xml.internal.ws.api.message.Packet;
import com.sun.xml.internal.ws.encoding.ContentTypeImpl;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:com/sun/xml/internal/ws/encoding/StreamSOAP12Codec.class */
final class StreamSOAP12Codec extends StreamSOAPCodec {
    public static final String SOAP12_MIME_TYPE = "application/soap+xml";
    public static final String DEFAULT_SOAP12_CONTENT_TYPE = "application/soap+xml; charset=utf-8";
    private static final List<String> EXPECTED_CONTENT_TYPES = Collections.singletonList("application/soap+xml");

    StreamSOAP12Codec() {
        super(SOAPVersion.SOAP_12);
    }

    StreamSOAP12Codec(WSBinding binding) {
        super(binding);
    }

    StreamSOAP12Codec(WSFeatureList features) {
        super(features);
    }

    @Override // com.sun.xml.internal.ws.api.pipe.Codec
    public String getMimeType() {
        return "application/soap+xml";
    }

    @Override // com.sun.xml.internal.ws.encoding.StreamSOAPCodec
    protected com.sun.xml.internal.ws.api.pipe.ContentType getContentType(Packet packet) {
        ContentTypeImpl.Builder b2 = getContenTypeBuilder(packet);
        if (packet.soapAction == null) {
            return b2.build();
        }
        b2.contentType += ";action=" + fixQuotesAroundSoapAction(packet.soapAction);
        return b2.build();
    }

    @Override // com.sun.xml.internal.ws.encoding.StreamSOAPCodec, com.sun.xml.internal.ws.encoding.RootOnlyCodec
    public void decode(InputStream in, String contentType, Packet packet, AttachmentSet att) throws IOException {
        ContentType ct = new ContentType(contentType);
        packet.soapAction = fixQuotesAroundSoapAction(ct.getParameter("action"));
        super.decode(in, contentType, packet, att);
    }

    private String fixQuotesAroundSoapAction(String soapAction) {
        if (soapAction != null && (!soapAction.startsWith(PdfOps.DOUBLE_QUOTE__TOKEN) || !soapAction.endsWith(PdfOps.DOUBLE_QUOTE__TOKEN))) {
            String fixedSoapAction = soapAction;
            if (!soapAction.startsWith(PdfOps.DOUBLE_QUOTE__TOKEN)) {
                fixedSoapAction = PdfOps.DOUBLE_QUOTE__TOKEN + fixedSoapAction;
            }
            if (!soapAction.endsWith(PdfOps.DOUBLE_QUOTE__TOKEN)) {
                fixedSoapAction = fixedSoapAction + PdfOps.DOUBLE_QUOTE__TOKEN;
            }
            return fixedSoapAction;
        }
        return soapAction;
    }

    @Override // com.sun.xml.internal.ws.encoding.StreamSOAPCodec
    protected List<String> getExpectedContentTypes() {
        return EXPECTED_CONTENT_TYPES;
    }

    @Override // com.sun.xml.internal.ws.encoding.StreamSOAPCodec
    protected String getDefaultContentType() {
        return DEFAULT_SOAP12_CONTENT_TYPE;
    }
}
