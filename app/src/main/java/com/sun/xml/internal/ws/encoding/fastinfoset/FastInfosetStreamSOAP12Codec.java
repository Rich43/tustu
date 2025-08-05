package com.sun.xml.internal.ws.encoding.fastinfoset;

import com.sun.xml.internal.stream.buffer.XMLStreamBuffer;
import com.sun.xml.internal.ws.api.SOAPVersion;
import com.sun.xml.internal.ws.api.pipe.Codec;
import com.sun.xml.internal.ws.api.pipe.ContentType;
import com.sun.xml.internal.ws.api.pipe.StreamSOAPCodec;
import com.sun.xml.internal.ws.encoding.ContentTypeImpl;
import com.sun.xml.internal.ws.message.stream.StreamHeader;
import com.sun.xml.internal.ws.message.stream.StreamHeader12;
import javax.xml.stream.XMLStreamReader;
import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:com/sun/xml/internal/ws/encoding/fastinfoset/FastInfosetStreamSOAP12Codec.class */
final class FastInfosetStreamSOAP12Codec extends FastInfosetStreamSOAPCodec {
    FastInfosetStreamSOAP12Codec(StreamSOAPCodec soapCodec, boolean retainState) {
        super(soapCodec, SOAPVersion.SOAP_12, retainState, retainState ? FastInfosetMIMETypes.STATEFUL_SOAP_12 : FastInfosetMIMETypes.SOAP_12);
    }

    private FastInfosetStreamSOAP12Codec(FastInfosetStreamSOAPCodec that) {
        super(that);
    }

    @Override // com.sun.xml.internal.ws.api.pipe.Codec
    public Codec copy() {
        return new FastInfosetStreamSOAP12Codec(this);
    }

    @Override // com.sun.xml.internal.ws.encoding.fastinfoset.FastInfosetStreamSOAPCodec
    protected final StreamHeader createHeader(XMLStreamReader reader, XMLStreamBuffer mark) {
        return new StreamHeader12(reader, mark);
    }

    @Override // com.sun.xml.internal.ws.encoding.fastinfoset.FastInfosetStreamSOAPCodec
    protected ContentType getContentType(String soapAction) {
        if (soapAction == null) {
            return this._defaultContentType;
        }
        return new ContentTypeImpl(this._defaultContentType.getContentType() + ";action=\"" + soapAction + PdfOps.DOUBLE_QUOTE__TOKEN);
    }
}
