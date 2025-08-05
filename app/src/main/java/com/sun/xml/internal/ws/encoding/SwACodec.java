package com.sun.xml.internal.ws.encoding;

import com.sun.xml.internal.ws.api.SOAPVersion;
import com.sun.xml.internal.ws.api.WSFeatureList;
import com.sun.xml.internal.ws.api.message.Attachment;
import com.sun.xml.internal.ws.api.message.Packet;
import com.sun.xml.internal.ws.api.pipe.Codec;
import com.sun.xml.internal.ws.message.MimeAttachmentSet;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.Map;

/* loaded from: rt.jar:com/sun/xml/internal/ws/encoding/SwACodec.class */
public final class SwACodec extends MimeCodec {
    @Override // com.sun.xml.internal.ws.encoding.MimeCodec, com.sun.xml.internal.ws.api.pipe.Codec
    public /* bridge */ /* synthetic */ void decode(ReadableByteChannel readableByteChannel, String str, Packet packet) {
        super.decode(readableByteChannel, str, packet);
    }

    @Override // com.sun.xml.internal.ws.encoding.MimeCodec, com.sun.xml.internal.ws.api.pipe.Codec
    public /* bridge */ /* synthetic */ void decode(InputStream inputStream, String str, Packet packet) throws IOException {
        super.decode(inputStream, str, packet);
    }

    @Override // com.sun.xml.internal.ws.encoding.MimeCodec, com.sun.xml.internal.ws.api.pipe.Codec
    public /* bridge */ /* synthetic */ com.sun.xml.internal.ws.api.pipe.ContentType getStaticContentType(Packet packet) {
        return super.getStaticContentType(packet);
    }

    @Override // com.sun.xml.internal.ws.encoding.MimeCodec, com.sun.xml.internal.ws.api.pipe.Codec
    public /* bridge */ /* synthetic */ com.sun.xml.internal.ws.api.pipe.ContentType encode(Packet packet, OutputStream outputStream) throws IOException {
        return super.encode(packet, outputStream);
    }

    @Override // com.sun.xml.internal.ws.encoding.MimeCodec, com.sun.xml.internal.ws.api.pipe.Codec
    public /* bridge */ /* synthetic */ String getMimeType() {
        return super.getMimeType();
    }

    public SwACodec(SOAPVersion version, WSFeatureList f2, Codec rootCodec) {
        super(version, f2);
        this.mimeRootCodec = rootCodec;
    }

    private SwACodec(SwACodec that) {
        super(that);
        this.mimeRootCodec = that.mimeRootCodec.copy();
    }

    @Override // com.sun.xml.internal.ws.encoding.MimeCodec
    protected void decode(MimeMultipartParser mpp, Packet packet) throws IOException {
        Attachment root = mpp.getRootPart();
        Codec rootCodec = getMimeRootCodec(packet);
        if (rootCodec instanceof RootOnlyCodec) {
            ((RootOnlyCodec) rootCodec).decode(root.asInputStream(), root.getContentType(), packet, new MimeAttachmentSet(mpp));
            return;
        }
        rootCodec.decode(root.asInputStream(), root.getContentType(), packet);
        Map<String, Attachment> atts = mpp.getAttachmentParts();
        for (Map.Entry<String, Attachment> att : atts.entrySet()) {
            packet.getMessage().getAttachments().add(att.getValue());
        }
    }

    @Override // com.sun.xml.internal.ws.api.pipe.Codec
    public com.sun.xml.internal.ws.api.pipe.ContentType encode(Packet packet, WritableByteChannel buffer) {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.xml.internal.ws.encoding.MimeCodec, com.sun.xml.internal.ws.api.pipe.Codec
    public SwACodec copy() {
        return new SwACodec(this);
    }
}
