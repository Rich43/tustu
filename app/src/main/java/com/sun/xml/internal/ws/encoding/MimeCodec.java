package com.sun.xml.internal.ws.encoding;

import com.sun.xml.internal.ws.api.SOAPVersion;
import com.sun.xml.internal.ws.api.WSFeatureList;
import com.sun.xml.internal.ws.api.message.Attachment;
import com.sun.xml.internal.ws.api.message.AttachmentEx;
import com.sun.xml.internal.ws.api.message.Message;
import com.sun.xml.internal.ws.api.message.Packet;
import com.sun.xml.internal.ws.api.pipe.Codec;
import com.sun.xml.internal.ws.developer.StreamingAttachmentFeature;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.ReadableByteChannel;
import java.util.Iterator;
import java.util.UUID;
import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:com/sun/xml/internal/ws/encoding/MimeCodec.class */
abstract class MimeCodec implements Codec {
    public static final String MULTIPART_RELATED_MIME_TYPE = "multipart/related";
    protected Codec mimeRootCodec;
    protected final SOAPVersion version;
    protected final WSFeatureList features;

    protected abstract void decode(MimeMultipartParser mimeMultipartParser, Packet packet) throws IOException;

    @Override // com.sun.xml.internal.ws.api.pipe.Codec
    public abstract MimeCodec copy();

    protected MimeCodec(SOAPVersion version, WSFeatureList f2) {
        this.version = version;
        this.features = f2;
    }

    @Override // com.sun.xml.internal.ws.api.pipe.Codec
    public String getMimeType() {
        return MULTIPART_RELATED_MIME_TYPE;
    }

    protected Codec getMimeRootCodec(Packet packet) {
        return this.mimeRootCodec;
    }

    @Override // com.sun.xml.internal.ws.api.pipe.Codec
    public com.sun.xml.internal.ws.api.pipe.ContentType encode(Packet packet, OutputStream out) throws IOException {
        Message msg = packet.getMessage();
        if (msg == null) {
            return null;
        }
        ContentTypeImpl ctImpl = (ContentTypeImpl) getStaticContentType(packet);
        String boundary = ctImpl.getBoundary();
        boolean hasAttachments = boundary != null;
        Codec rootCodec = getMimeRootCodec(packet);
        if (hasAttachments) {
            writeln("--" + boundary, out);
            com.sun.xml.internal.ws.api.pipe.ContentType ct = rootCodec.getStaticContentType(packet);
            String ctStr = ct != null ? ct.getContentType() : rootCodec.getMimeType();
            writeln("Content-Type: " + ctStr, out);
            writeln(out);
        }
        com.sun.xml.internal.ws.api.pipe.ContentType primaryCt = rootCodec.encode(packet, out);
        if (hasAttachments) {
            writeln(out);
            for (Attachment att : msg.getAttachments()) {
                writeln("--" + boundary, out);
                String cid = att.getContentId();
                if (cid != null && cid.length() > 0 && cid.charAt(0) != '<') {
                    cid = '<' + cid + '>';
                }
                writeln("Content-Id:" + cid, out);
                writeln("Content-Type: " + att.getContentType(), out);
                writeCustomMimeHeaders(att, out);
                writeln("Content-Transfer-Encoding: binary", out);
                writeln(out);
                att.writeTo(out);
                writeln(out);
            }
            writeAsAscii("--" + boundary, out);
            writeAsAscii("--", out);
        }
        return hasAttachments ? ctImpl : primaryCt;
    }

    private void writeCustomMimeHeaders(Attachment att, OutputStream out) throws IOException {
        if (att instanceof AttachmentEx) {
            Iterator<AttachmentEx.MimeHeader> allMimeHeaders = ((AttachmentEx) att).getMimeHeaders();
            while (allMimeHeaders.hasNext()) {
                AttachmentEx.MimeHeader mh = allMimeHeaders.next();
                String name = mh.getName();
                if (!"Content-Type".equalsIgnoreCase(name) && !"Content-Id".equalsIgnoreCase(name)) {
                    writeln(name + ": " + mh.getValue(), out);
                }
            }
        }
    }

    @Override // com.sun.xml.internal.ws.api.pipe.Codec
    public com.sun.xml.internal.ws.api.pipe.ContentType getStaticContentType(Packet packet) {
        com.sun.xml.internal.ws.api.pipe.ContentType ct = (com.sun.xml.internal.ws.api.pipe.ContentType) packet.getInternalContentType();
        if (ct != null) {
            return ct;
        }
        Message msg = packet.getMessage();
        boolean hasAttachments = !msg.getAttachments().isEmpty();
        Codec rootCodec = getMimeRootCodec(packet);
        if (hasAttachments) {
            String boundary = "uuid:" + UUID.randomUUID().toString();
            String boundaryParameter = "boundary=\"" + boundary + PdfOps.DOUBLE_QUOTE__TOKEN;
            String messageContentType = "multipart/related; type=\"" + rootCodec.getMimeType() + "\"; " + boundaryParameter;
            ContentTypeImpl impl = new ContentTypeImpl(messageContentType, packet.soapAction, null);
            impl.setBoundary(boundary);
            impl.setBoundaryParameter(boundaryParameter);
            packet.setContentType(impl);
            return impl;
        }
        com.sun.xml.internal.ws.api.pipe.ContentType ct2 = rootCodec.getStaticContentType(packet);
        packet.setContentType(ct2);
        return ct2;
    }

    protected MimeCodec(MimeCodec that) {
        this.version = that.version;
        this.features = that.features;
    }

    @Override // com.sun.xml.internal.ws.api.pipe.Codec
    public void decode(InputStream in, String contentType, Packet packet) throws IOException {
        MimeMultipartParser parser = new MimeMultipartParser(in, contentType, (StreamingAttachmentFeature) this.features.get(StreamingAttachmentFeature.class));
        decode(parser, packet);
    }

    @Override // com.sun.xml.internal.ws.api.pipe.Codec
    public void decode(ReadableByteChannel in, String contentType, Packet packet) {
        throw new UnsupportedOperationException();
    }

    public static void writeln(String s2, OutputStream out) throws IOException {
        writeAsAscii(s2, out);
        writeln(out);
    }

    public static void writeAsAscii(String s2, OutputStream out) throws IOException {
        int len = s2.length();
        for (int i2 = 0; i2 < len; i2++) {
            out.write((byte) s2.charAt(i2));
        }
    }

    public static void writeln(OutputStream out) throws IOException {
        out.write(13);
        out.write(10);
    }
}
