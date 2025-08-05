package com.sun.xml.internal.ws.encoding.fastinfoset;

import com.sun.xml.internal.stream.buffer.XMLStreamBuffer;
import com.sun.xml.internal.ws.api.SOAPVersion;
import com.sun.xml.internal.ws.api.pipe.Codec;
import com.sun.xml.internal.ws.api.pipe.ContentType;
import com.sun.xml.internal.ws.api.pipe.StreamSOAPCodec;
import com.sun.xml.internal.ws.encoding.ContentTypeImpl;
import com.sun.xml.internal.ws.message.stream.StreamHeader;
import com.sun.xml.internal.ws.message.stream.StreamHeader11;
import javax.xml.stream.XMLStreamReader;

/* loaded from: rt.jar:com/sun/xml/internal/ws/encoding/fastinfoset/FastInfosetStreamSOAP11Codec.class */
final class FastInfosetStreamSOAP11Codec extends FastInfosetStreamSOAPCodec {
    FastInfosetStreamSOAP11Codec(StreamSOAPCodec soapCodec, boolean retainState) {
        super(soapCodec, SOAPVersion.SOAP_11, retainState, retainState ? "application/vnd.sun.stateful.fastinfoset" : "application/fastinfoset");
    }

    private FastInfosetStreamSOAP11Codec(FastInfosetStreamSOAP11Codec that) {
        super(that);
    }

    @Override // com.sun.xml.internal.ws.api.pipe.Codec
    public Codec copy() {
        return new FastInfosetStreamSOAP11Codec(this);
    }

    @Override // com.sun.xml.internal.ws.encoding.fastinfoset.FastInfosetStreamSOAPCodec
    protected final StreamHeader createHeader(XMLStreamReader reader, XMLStreamBuffer mark) {
        return new StreamHeader11(reader, mark);
    }

    @Override // com.sun.xml.internal.ws.encoding.fastinfoset.FastInfosetStreamSOAPCodec
    protected ContentType getContentType(String soapAction) {
        if (soapAction == null || soapAction.length() == 0) {
            return this._defaultContentType;
        }
        return new ContentTypeImpl(this._defaultContentType.getContentType(), soapAction);
    }
}
