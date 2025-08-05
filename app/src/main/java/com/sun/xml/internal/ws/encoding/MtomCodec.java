package com.sun.xml.internal.ws.encoding;

import com.sun.istack.internal.NotNull;
import com.sun.org.apache.xalan.internal.templates.Constants;
import com.sun.xml.internal.bind.DatatypeConverterImpl;
import com.sun.xml.internal.org.jvnet.staxex.Base64Data;
import com.sun.xml.internal.org.jvnet.staxex.NamespaceContextEx;
import com.sun.xml.internal.org.jvnet.staxex.XMLStreamReaderEx;
import com.sun.xml.internal.org.jvnet.staxex.XMLStreamWriterEx;
import com.sun.xml.internal.stream.writers.XMLStreamWriterImpl;
import com.sun.xml.internal.ws.api.SOAPVersion;
import com.sun.xml.internal.ws.api.WSFeatureList;
import com.sun.xml.internal.ws.api.message.Attachment;
import com.sun.xml.internal.ws.api.message.AttachmentSet;
import com.sun.xml.internal.ws.api.message.Packet;
import com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory;
import com.sun.xml.internal.ws.api.streaming.XMLStreamWriterFactory;
import com.sun.xml.internal.ws.developer.SerializationFeature;
import com.sun.xml.internal.ws.developer.StreamingDataHandler;
import com.sun.xml.internal.ws.message.MimeAttachmentSet;
import com.sun.xml.internal.ws.server.UnsupportedMediaException;
import com.sun.xml.internal.ws.streaming.MtomStreamWriter;
import com.sun.xml.internal.ws.streaming.XMLStreamReaderUtil;
import com.sun.xml.internal.ws.streaming.XMLStreamWriterUtil;
import com.sun.xml.internal.ws.util.ByteArrayDataSource;
import com.sun.xml.internal.ws.util.xml.NamespaceContextExAdaper;
import com.sun.xml.internal.ws.util.xml.XMLStreamReaderFilter;
import com.sun.xml.internal.ws.util.xml.XMLStreamWriterFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.activation.DataHandler;
import javax.xml.bind.attachment.AttachmentMarshaller;
import javax.xml.namespace.NamespaceContext;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.soap.MTOMFeature;
import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:com/sun/xml/internal/ws/encoding/MtomCodec.class */
public class MtomCodec extends MimeCodec {
    public static final String XOP_XML_MIME_TYPE = "application/xop+xml";
    public static final String XOP_LOCALNAME = "Include";
    public static final String XOP_NAMESPACEURI = "http://www.w3.org/2004/08/xop/include";
    private final com.sun.xml.internal.ws.api.pipe.StreamSOAPCodec codec;
    private final MTOMFeature mtomFeature;
    private final SerializationFeature sf;
    private static final String DECODED_MESSAGE_CHARSET = "decodedMessageCharset";

    @Override // com.sun.xml.internal.ws.encoding.MimeCodec, com.sun.xml.internal.ws.api.pipe.Codec
    public /* bridge */ /* synthetic */ void decode(ReadableByteChannel readableByteChannel, String str, Packet packet) {
        super.decode(readableByteChannel, str, packet);
    }

    @Override // com.sun.xml.internal.ws.encoding.MimeCodec, com.sun.xml.internal.ws.api.pipe.Codec
    public /* bridge */ /* synthetic */ void decode(InputStream inputStream, String str, Packet packet) throws IOException {
        super.decode(inputStream, str, packet);
    }

    @Override // com.sun.xml.internal.ws.encoding.MimeCodec, com.sun.xml.internal.ws.api.pipe.Codec
    public /* bridge */ /* synthetic */ String getMimeType() {
        return super.getMimeType();
    }

    MtomCodec(SOAPVersion version, com.sun.xml.internal.ws.api.pipe.StreamSOAPCodec codec, WSFeatureList features) {
        super(version, features);
        this.codec = codec;
        this.sf = (SerializationFeature) features.get(SerializationFeature.class);
        MTOMFeature mtom = (MTOMFeature) features.get(MTOMFeature.class);
        if (mtom == null) {
            this.mtomFeature = new MTOMFeature();
        } else {
            this.mtomFeature = mtom;
        }
    }

    @Override // com.sun.xml.internal.ws.encoding.MimeCodec, com.sun.xml.internal.ws.api.pipe.Codec
    public com.sun.xml.internal.ws.api.pipe.ContentType getStaticContentType(Packet packet) {
        return getStaticContentTypeStatic(packet, this.version);
    }

    public static com.sun.xml.internal.ws.api.pipe.ContentType getStaticContentTypeStatic(Packet packet, SOAPVersion version) {
        ContentTypeImpl contentTypeImpl;
        com.sun.xml.internal.ws.api.pipe.ContentType ct = (com.sun.xml.internal.ws.api.pipe.ContentType) packet.getInternalContentType();
        if (ct != null) {
            return ct;
        }
        String uuid = UUID.randomUUID().toString();
        String boundary = "uuid:" + uuid;
        String rootId = "<rootpart*" + uuid + "@example.jaxws.sun.com>";
        String soapActionParameter = SOAPVersion.SOAP_11.equals(version) ? null : createActionParameter(packet);
        String boundaryParameter = "boundary=\"" + boundary + PdfOps.DOUBLE_QUOTE__TOKEN;
        String messageContentType = "multipart/related;start=\"" + rootId + "\";type=\"" + XOP_XML_MIME_TYPE + "\";" + boundaryParameter + ";start-info=\"" + version.contentType + (soapActionParameter == null ? "" : soapActionParameter) + PdfOps.DOUBLE_QUOTE__TOKEN;
        if (SOAPVersion.SOAP_11.equals(version)) {
            contentTypeImpl = new ContentTypeImpl(messageContentType, packet.soapAction == null ? "" : packet.soapAction, null);
        } else {
            contentTypeImpl = new ContentTypeImpl(messageContentType, null, null);
        }
        ContentTypeImpl ctImpl = contentTypeImpl;
        ctImpl.setBoundary(boundary);
        ctImpl.setRootId(rootId);
        packet.setContentType(ctImpl);
        return ctImpl;
    }

    private static String createActionParameter(Packet packet) {
        return packet.soapAction != null ? ";action=\\\"" + packet.soapAction + "\\\"" : "";
    }

    @Override // com.sun.xml.internal.ws.encoding.MimeCodec, com.sun.xml.internal.ws.api.pipe.Codec
    public com.sun.xml.internal.ws.api.pipe.ContentType encode(Packet packet, OutputStream out) throws IOException {
        ContentTypeImpl ctImpl = (ContentTypeImpl) getStaticContentType(packet);
        String boundary = ctImpl.getBoundary();
        String rootId = ctImpl.getRootId();
        if (packet.getMessage() != null) {
            try {
                String encoding = getPacketEncoding(packet);
                packet.invocationProperties.remove(DECODED_MESSAGE_CHARSET);
                String actionParameter = getActionParameter(packet, this.version);
                String soapXopContentType = getSOAPXopContentType(encoding, this.version, actionParameter);
                writeln("--" + boundary, out);
                writeMimeHeaders(soapXopContentType, rootId, out);
                List<ByteArrayBuffer> mtomAttachments = new ArrayList<>();
                MtomStreamWriterImpl writer = new MtomStreamWriterImpl(XMLStreamWriterFactory.create(out, encoding), mtomAttachments, boundary, this.mtomFeature);
                packet.getMessage().writeTo(writer);
                XMLStreamWriterFactory.recycle(writer);
                writeln(out);
                for (ByteArrayBuffer bos : mtomAttachments) {
                    bos.write(out);
                }
                writeNonMtomAttachments(packet.getMessage().getAttachments(), out, boundary);
                writeAsAscii("--" + boundary, out);
                writeAsAscii("--", out);
            } catch (XMLStreamException e2) {
                throw new WebServiceException(e2);
            }
        }
        return ctImpl;
    }

    public static String getSOAPXopContentType(String encoding, SOAPVersion version, String actionParameter) {
        return "application/xop+xml;charset=" + encoding + ";type=\"" + version.contentType + actionParameter + PdfOps.DOUBLE_QUOTE__TOKEN;
    }

    public static String getActionParameter(Packet packet, SOAPVersion version) {
        return version == SOAPVersion.SOAP_11 ? "" : createActionParameter(packet);
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/encoding/MtomCodec$ByteArrayBuffer.class */
    public static class ByteArrayBuffer {
        final String contentId;
        private final DataHandler dh;
        private final String boundary;

        ByteArrayBuffer(@NotNull DataHandler dh, String b2) {
            this.dh = dh;
            String cid = null;
            if (dh instanceof StreamingDataHandler) {
                StreamingDataHandler sdh = (StreamingDataHandler) dh;
                if (sdh.getHrefCid() != null) {
                    cid = sdh.getHrefCid();
                }
            }
            this.contentId = cid != null ? cid : MtomCodec.encodeCid();
            this.boundary = b2;
        }

        public void write(OutputStream os) throws IOException {
            MimeCodec.writeln("--" + this.boundary, os);
            MtomCodec.writeMimeHeaders(this.dh.getContentType(), this.contentId, os);
            this.dh.writeTo(os);
            MimeCodec.writeln(os);
        }
    }

    public static void writeMimeHeaders(String contentType, String contentId, OutputStream out) throws IOException {
        String cid = contentId;
        if (cid != null && cid.length() > 0 && cid.charAt(0) != '<') {
            cid = '<' + cid + '>';
        }
        writeln("Content-Id: " + cid, out);
        writeln("Content-Type: " + contentType, out);
        writeln("Content-Transfer-Encoding: binary", out);
        writeln(out);
    }

    private void writeNonMtomAttachments(AttachmentSet attachments, OutputStream out, String boundary) throws IOException {
        for (Attachment att : attachments) {
            DataHandler dh = att.asDataHandler();
            if (dh instanceof StreamingDataHandler) {
                StreamingDataHandler sdh = (StreamingDataHandler) dh;
                if (sdh.getHrefCid() != null) {
                }
            }
            writeln("--" + boundary, out);
            writeMimeHeaders(att.getContentType(), att.getContentId(), out);
            att.writeTo(out);
            writeln(out);
        }
    }

    @Override // com.sun.xml.internal.ws.api.pipe.Codec
    public com.sun.xml.internal.ws.api.pipe.ContentType encode(Packet packet, WritableByteChannel buffer) {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.xml.internal.ws.encoding.MimeCodec, com.sun.xml.internal.ws.api.pipe.Codec
    public MtomCodec copy() {
        return new MtomCodec(this.version, (com.sun.xml.internal.ws.api.pipe.StreamSOAPCodec) this.codec.copy(), this.features);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String encodeCid() {
        String name = ((Object) UUID.randomUUID()) + "@";
        return name + "example.jaxws.sun.com";
    }

    @Override // com.sun.xml.internal.ws.encoding.MimeCodec
    protected void decode(MimeMultipartParser mpp, Packet packet) throws IOException {
        String charset = null;
        String ct = mpp.getRootPart().getContentType();
        if (ct != null) {
            charset = new ContentTypeImpl(ct).getCharSet();
        }
        if (charset != null && !Charset.isSupported(charset)) {
            throw new UnsupportedMediaException(charset);
        }
        if (charset != null) {
            packet.invocationProperties.put(DECODED_MESSAGE_CHARSET, charset);
        } else {
            packet.invocationProperties.remove(DECODED_MESSAGE_CHARSET);
        }
        XMLStreamReader mtomReader = new MtomXMLStreamReaderEx(mpp, XMLStreamReaderFactory.create(null, mpp.getRootPart().asInputStream(), charset, true));
        packet.setMessage(this.codec.decode(mtomReader, new MimeAttachmentSet(mpp)));
        packet.setMtomFeature(this.mtomFeature);
        packet.setContentType(mpp.getContentType());
    }

    private String getPacketEncoding(Packet packet) {
        if (this.sf == null || this.sf.getEncoding() == null) {
            return determinePacketEncoding(packet);
        }
        return this.sf.getEncoding().equals("") ? "utf-8" : this.sf.getEncoding();
    }

    public static String determinePacketEncoding(Packet packet) {
        String charset;
        return (packet == null || packet.endpoint == null || (charset = (String) packet.invocationProperties.get(DECODED_MESSAGE_CHARSET)) == null) ? "utf-8" : charset;
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/encoding/MtomCodec$MtomStreamWriterImpl.class */
    public static class MtomStreamWriterImpl extends XMLStreamWriterFilter implements XMLStreamWriterEx, MtomStreamWriter, HasEncoding {
        private final List<ByteArrayBuffer> mtomAttachments;
        private final String boundary;
        private final MTOMFeature myMtomFeature;

        public MtomStreamWriterImpl(XMLStreamWriter w2, List<ByteArrayBuffer> mtomAttachments, String b2, MTOMFeature myMtomFeature) {
            super(w2);
            this.mtomAttachments = mtomAttachments;
            this.boundary = b2;
            this.myMtomFeature = myMtomFeature;
        }

        @Override // com.sun.xml.internal.org.jvnet.staxex.XMLStreamWriterEx
        public void writeBinary(byte[] data, int start, int len, String contentType) throws XMLStreamException {
            if (this.myMtomFeature.getThreshold() > len) {
                writeCharacters(DatatypeConverterImpl._printBase64Binary(data, start, len));
            } else {
                ByteArrayBuffer bab = new ByteArrayBuffer(new DataHandler(new ByteArrayDataSource(data, start, len, contentType)), this.boundary);
                writeBinary(bab);
            }
        }

        @Override // com.sun.xml.internal.org.jvnet.staxex.XMLStreamWriterEx
        public void writeBinary(DataHandler dataHandler) throws XMLStreamException {
            writeBinary(new ByteArrayBuffer(dataHandler, this.boundary));
        }

        @Override // com.sun.xml.internal.org.jvnet.staxex.XMLStreamWriterEx
        public OutputStream writeBinary(String contentType) throws XMLStreamException {
            throw new UnsupportedOperationException();
        }

        @Override // com.sun.xml.internal.org.jvnet.staxex.XMLStreamWriterEx
        public void writePCDATA(CharSequence data) throws XMLStreamException {
            if (data == null) {
                return;
            }
            if (data instanceof Base64Data) {
                Base64Data binaryData = (Base64Data) data;
                writeBinary(binaryData.getDataHandler());
            } else {
                writeCharacters(data.toString());
            }
        }

        private void writeBinary(ByteArrayBuffer bab) {
            try {
                this.mtomAttachments.add(bab);
                String prefix = this.writer.getPrefix("http://www.w3.org/2004/08/xop/include");
                if (prefix == null || !prefix.equals("xop")) {
                    this.writer.setPrefix("xop", "http://www.w3.org/2004/08/xop/include");
                    this.writer.writeNamespace("xop", "http://www.w3.org/2004/08/xop/include");
                }
                this.writer.writeStartElement("http://www.w3.org/2004/08/xop/include", MtomCodec.XOP_LOCALNAME);
                this.writer.writeAttribute(Constants.ATTRNAME_HREF, "cid:" + bab.contentId);
                this.writer.writeEndElement();
                this.writer.flush();
            } catch (XMLStreamException e2) {
                throw new WebServiceException(e2);
            }
        }

        @Override // com.sun.xml.internal.ws.util.xml.XMLStreamWriterFilter, javax.xml.stream.XMLStreamWriter
        public Object getProperty(String name) throws IllegalArgumentException {
            Object obj;
            if (name.equals(XMLStreamWriterImpl.OUTPUTSTREAM_PROPERTY) && (this.writer instanceof Map) && (obj = ((Map) this.writer).get(XMLStreamWriterImpl.OUTPUTSTREAM_PROPERTY)) != null) {
                return obj;
            }
            return super.getProperty(name);
        }

        @Override // com.sun.xml.internal.ws.streaming.MtomStreamWriter
        public AttachmentMarshaller getAttachmentMarshaller() {
            return new AttachmentMarshaller() { // from class: com.sun.xml.internal.ws.encoding.MtomCodec.MtomStreamWriterImpl.1
                @Override // javax.xml.bind.attachment.AttachmentMarshaller
                public String addMtomAttachment(DataHandler data, String elementNamespace, String elementLocalName) {
                    ByteArrayBuffer bab = new ByteArrayBuffer(data, MtomStreamWriterImpl.this.boundary);
                    MtomStreamWriterImpl.this.mtomAttachments.add(bab);
                    return "cid:" + bab.contentId;
                }

                @Override // javax.xml.bind.attachment.AttachmentMarshaller
                public String addMtomAttachment(byte[] data, int offset, int length, String mimeType, String elementNamespace, String elementLocalName) {
                    if (MtomStreamWriterImpl.this.myMtomFeature.getThreshold() > length) {
                        return null;
                    }
                    ByteArrayBuffer bab = new ByteArrayBuffer(new DataHandler(new ByteArrayDataSource(data, offset, length, mimeType)), MtomStreamWriterImpl.this.boundary);
                    MtomStreamWriterImpl.this.mtomAttachments.add(bab);
                    return "cid:" + bab.contentId;
                }

                @Override // javax.xml.bind.attachment.AttachmentMarshaller
                public String addSwaRefAttachment(DataHandler data) {
                    ByteArrayBuffer bab = new ByteArrayBuffer(data, MtomStreamWriterImpl.this.boundary);
                    MtomStreamWriterImpl.this.mtomAttachments.add(bab);
                    return "cid:" + bab.contentId;
                }

                @Override // javax.xml.bind.attachment.AttachmentMarshaller
                public boolean isXOPPackage() {
                    return true;
                }
            };
        }

        public List<ByteArrayBuffer> getMtomAttachments() {
            return this.mtomAttachments;
        }

        @Override // com.sun.xml.internal.ws.encoding.HasEncoding
        public String getEncoding() {
            return XMLStreamWriterUtil.getEncoding(this.writer);
        }

        /* loaded from: rt.jar:com/sun/xml/internal/ws/encoding/MtomCodec$MtomStreamWriterImpl$MtomNamespaceContextEx.class */
        private static class MtomNamespaceContextEx implements NamespaceContextEx {
            private final NamespaceContext nsContext;

            public MtomNamespaceContextEx(NamespaceContext nsContext) {
                this.nsContext = nsContext;
            }

            @Override // com.sun.xml.internal.org.jvnet.staxex.NamespaceContextEx, java.lang.Iterable, java.util.List
            public Iterator<NamespaceContextEx.Binding> iterator() {
                throw new UnsupportedOperationException();
            }

            @Override // javax.xml.namespace.NamespaceContext
            public String getNamespaceURI(String prefix) {
                return this.nsContext.getNamespaceURI(prefix);
            }

            @Override // javax.xml.namespace.NamespaceContext
            public String getPrefix(String namespaceURI) {
                return this.nsContext.getPrefix(namespaceURI);
            }

            @Override // javax.xml.namespace.NamespaceContext
            public Iterator getPrefixes(String namespaceURI) {
                return this.nsContext.getPrefixes(namespaceURI);
            }
        }

        @Override // com.sun.xml.internal.ws.util.xml.XMLStreamWriterFilter, javax.xml.stream.XMLStreamWriter
        public NamespaceContextEx getNamespaceContext() {
            NamespaceContext nsContext = this.writer.getNamespaceContext();
            return new MtomNamespaceContextEx(nsContext);
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/encoding/MtomCodec$MtomXMLStreamReaderEx.class */
    public static class MtomXMLStreamReaderEx extends XMLStreamReaderFilter implements XMLStreamReaderEx {
        private final MimeMultipartParser mimeMP;
        private boolean xopReferencePresent;
        private Base64Data base64AttData;
        private char[] base64EncodedText;
        private String xopHref;

        public MtomXMLStreamReaderEx(MimeMultipartParser mimeMP, XMLStreamReader reader) {
            super(reader);
            this.xopReferencePresent = false;
            this.mimeMP = mimeMP;
        }

        @Override // com.sun.xml.internal.org.jvnet.staxex.XMLStreamReaderEx
        public CharSequence getPCDATA() throws XMLStreamException {
            if (this.xopReferencePresent) {
                return this.base64AttData;
            }
            return this.reader.getText();
        }

        @Override // com.sun.xml.internal.ws.util.xml.XMLStreamReaderFilter, javax.xml.stream.XMLStreamReader
        public NamespaceContextEx getNamespaceContext() {
            return new NamespaceContextExAdaper(this.reader.getNamespaceContext());
        }

        @Override // com.sun.xml.internal.org.jvnet.staxex.XMLStreamReaderEx
        public String getElementTextTrim() throws XMLStreamException {
            throw new UnsupportedOperationException();
        }

        @Override // com.sun.xml.internal.ws.util.xml.XMLStreamReaderFilter, javax.xml.stream.XMLStreamReader
        public int getTextLength() {
            if (this.xopReferencePresent) {
                return this.base64AttData.length();
            }
            return this.reader.getTextLength();
        }

        @Override // com.sun.xml.internal.ws.util.xml.XMLStreamReaderFilter, javax.xml.stream.XMLStreamReader
        public int getTextStart() {
            if (this.xopReferencePresent) {
                return 0;
            }
            return this.reader.getTextStart();
        }

        @Override // com.sun.xml.internal.ws.util.xml.XMLStreamReaderFilter, javax.xml.stream.XMLStreamReader
        public int getEventType() {
            if (this.xopReferencePresent) {
                return 4;
            }
            return super.getEventType();
        }

        @Override // com.sun.xml.internal.ws.util.xml.XMLStreamReaderFilter, javax.xml.stream.XMLStreamReader
        public int next() throws XMLStreamException {
            int event = this.reader.next();
            if (event == 1 && this.reader.getLocalName().equals(MtomCodec.XOP_LOCALNAME) && this.reader.getNamespaceURI().equals("http://www.w3.org/2004/08/xop/include")) {
                String href = this.reader.getAttributeValue(null, Constants.ATTRNAME_HREF);
                try {
                    this.xopHref = href;
                    Attachment att = getAttachment(href);
                    if (att != null) {
                        DataHandler dh = att.asDataHandler();
                        if (dh instanceof StreamingDataHandler) {
                            ((StreamingDataHandler) dh).setHrefCid(att.getContentId());
                        }
                        this.base64AttData = new Base64Data();
                        this.base64AttData.set(dh);
                    }
                    this.xopReferencePresent = true;
                    XMLStreamReaderUtil.nextElementContent(this.reader);
                    return 4;
                } catch (IOException e2) {
                    throw new WebServiceException(e2);
                }
            }
            if (this.xopReferencePresent) {
                this.xopReferencePresent = false;
                this.base64EncodedText = null;
                this.xopHref = null;
            }
            return event;
        }

        private String decodeCid(String cid) {
            try {
                cid = URLDecoder.decode(cid, "utf-8");
            } catch (UnsupportedEncodingException e2) {
            }
            return cid;
        }

        private Attachment getAttachment(String cid) throws IOException {
            if (cid.startsWith("cid:")) {
                cid = cid.substring(4, cid.length());
            }
            if (cid.indexOf(37) != -1) {
                return this.mimeMP.getAttachmentPart(decodeCid(cid));
            }
            return this.mimeMP.getAttachmentPart(cid);
        }

        @Override // com.sun.xml.internal.ws.util.xml.XMLStreamReaderFilter, javax.xml.stream.XMLStreamReader
        public char[] getTextCharacters() {
            if (this.xopReferencePresent) {
                char[] chars = new char[this.base64AttData.length()];
                this.base64AttData.writeTo(chars, 0);
                return chars;
            }
            return this.reader.getTextCharacters();
        }

        @Override // com.sun.xml.internal.ws.util.xml.XMLStreamReaderFilter, javax.xml.stream.XMLStreamReader
        public int getTextCharacters(int sourceStart, char[] target, int targetStart, int length) throws XMLStreamException {
            if (this.xopReferencePresent) {
                if (target == null) {
                    throw new NullPointerException("target char array can't be null");
                }
                if (targetStart < 0 || length < 0 || sourceStart < 0 || targetStart >= target.length || targetStart + length > target.length) {
                    throw new IndexOutOfBoundsException();
                }
                int textLength = this.base64AttData.length();
                if (sourceStart > textLength) {
                    throw new IndexOutOfBoundsException();
                }
                if (this.base64EncodedText == null) {
                    this.base64EncodedText = new char[this.base64AttData.length()];
                    this.base64AttData.writeTo(this.base64EncodedText, 0);
                }
                int copiedLength = Math.min(textLength - sourceStart, length);
                System.arraycopy(this.base64EncodedText, sourceStart, target, targetStart, copiedLength);
                return copiedLength;
            }
            return this.reader.getTextCharacters(sourceStart, target, targetStart, length);
        }

        @Override // com.sun.xml.internal.ws.util.xml.XMLStreamReaderFilter, javax.xml.stream.XMLStreamReader
        public String getText() {
            if (this.xopReferencePresent) {
                return this.base64AttData.toString();
            }
            return this.reader.getText();
        }

        protected boolean isXopReference() throws XMLStreamException {
            return this.xopReferencePresent;
        }

        protected String getXopHref() {
            return this.xopHref;
        }

        public MimeMultipartParser getMimeMultipartParser() {
            return this.mimeMP;
        }
    }
}
