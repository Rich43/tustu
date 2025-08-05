package com.sun.xml.internal.ws.encoding.fastinfoset;

import com.sun.xml.internal.fastinfoset.stax.StAXDocumentParser;
import com.sun.xml.internal.fastinfoset.stax.StAXDocumentSerializer;
import com.sun.xml.internal.stream.buffer.XMLStreamBuffer;
import com.sun.xml.internal.ws.api.SOAPVersion;
import com.sun.xml.internal.ws.api.message.Packet;
import com.sun.xml.internal.ws.api.pipe.Codec;
import com.sun.xml.internal.ws.api.pipe.ContentType;
import com.sun.xml.internal.ws.api.pipe.StreamSOAPCodec;
import com.sun.xml.internal.ws.encoding.ContentTypeImpl;
import com.sun.xml.internal.ws.message.stream.StreamHeader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.ws.WebServiceException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/encoding/fastinfoset/FastInfosetStreamSOAPCodec.class */
public abstract class FastInfosetStreamSOAPCodec implements Codec {
    private static final FastInfosetStreamReaderFactory READER_FACTORY = FastInfosetStreamReaderFactory.getInstance();
    private StAXDocumentParser _statefulParser;
    private StAXDocumentSerializer _serializer;
    private final StreamSOAPCodec _soapCodec;
    private final boolean _retainState;
    protected final ContentType _defaultContentType;

    protected abstract StreamHeader createHeader(XMLStreamReader xMLStreamReader, XMLStreamBuffer xMLStreamBuffer);

    protected abstract ContentType getContentType(String str);

    FastInfosetStreamSOAPCodec(StreamSOAPCodec soapCodec, SOAPVersion soapVersion, boolean retainState, String mimeType) {
        this._soapCodec = soapCodec;
        this._retainState = retainState;
        this._defaultContentType = new ContentTypeImpl(mimeType);
    }

    FastInfosetStreamSOAPCodec(FastInfosetStreamSOAPCodec that) {
        this._soapCodec = (StreamSOAPCodec) that._soapCodec.copy();
        this._retainState = that._retainState;
        this._defaultContentType = that._defaultContentType;
    }

    @Override // com.sun.xml.internal.ws.api.pipe.Codec
    public String getMimeType() {
        return this._defaultContentType.getContentType();
    }

    @Override // com.sun.xml.internal.ws.api.pipe.Codec
    public ContentType getStaticContentType(Packet packet) {
        return getContentType(packet.soapAction);
    }

    @Override // com.sun.xml.internal.ws.api.pipe.Codec
    public ContentType encode(Packet packet, OutputStream out) {
        if (packet.getMessage() != null) {
            XMLStreamWriter writer = getXMLStreamWriter(out);
            try {
                packet.getMessage().writeTo(writer);
                writer.flush();
            } catch (XMLStreamException e2) {
                throw new WebServiceException(e2);
            }
        }
        return getContentType(packet.soapAction);
    }

    @Override // com.sun.xml.internal.ws.api.pipe.Codec
    public ContentType encode(Packet packet, WritableByteChannel buffer) {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.xml.internal.ws.api.pipe.Codec
    public void decode(InputStream in, String contentType, Packet response) throws IOException {
        response.setMessage(this._soapCodec.decode(getXMLStreamReader(in)));
    }

    @Override // com.sun.xml.internal.ws.api.pipe.Codec
    public void decode(ReadableByteChannel in, String contentType, Packet response) {
        throw new UnsupportedOperationException();
    }

    private XMLStreamWriter getXMLStreamWriter(OutputStream out) {
        if (this._serializer != null) {
            this._serializer.setOutputStream(out);
            return this._serializer;
        }
        StAXDocumentSerializer stAXDocumentSerializerCreateNewStreamWriter = FastInfosetCodec.createNewStreamWriter(out, this._retainState);
        this._serializer = stAXDocumentSerializerCreateNewStreamWriter;
        return stAXDocumentSerializerCreateNewStreamWriter;
    }

    private XMLStreamReader getXMLStreamReader(InputStream in) {
        if (this._retainState) {
            if (this._statefulParser != null) {
                this._statefulParser.setInputStream(in);
                return this._statefulParser;
            }
            StAXDocumentParser stAXDocumentParserCreateNewStreamReader = FastInfosetCodec.createNewStreamReader(in, this._retainState);
            this._statefulParser = stAXDocumentParserCreateNewStreamReader;
            return stAXDocumentParserCreateNewStreamReader;
        }
        return READER_FACTORY.doCreate((String) null, in, false);
    }

    public static FastInfosetStreamSOAPCodec create(StreamSOAPCodec soapCodec, SOAPVersion version) {
        return create(soapCodec, version, false);
    }

    public static FastInfosetStreamSOAPCodec create(StreamSOAPCodec soapCodec, SOAPVersion version, boolean retainState) {
        if (version == null) {
            throw new IllegalArgumentException();
        }
        switch (version) {
            case SOAP_11:
                return new FastInfosetStreamSOAP11Codec(soapCodec, retainState);
            case SOAP_12:
                return new FastInfosetStreamSOAP12Codec(soapCodec, retainState);
            default:
                throw new AssertionError();
        }
    }
}
