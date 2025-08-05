package com.oracle.webservices.internal.impl.encoding;

import com.oracle.webservices.internal.impl.internalspi.encoding.StreamDecoder;
import com.sun.xml.internal.ws.api.SOAPVersion;
import com.sun.xml.internal.ws.api.message.AttachmentSet;
import com.sun.xml.internal.ws.api.message.Message;
import com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory;
import com.sun.xml.internal.ws.encoding.StreamSOAPCodec;
import com.sun.xml.internal.ws.streaming.TidyXMLStreamReader;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.stream.XMLStreamReader;

/* loaded from: rt.jar:com/oracle/webservices/internal/impl/encoding/StreamDecoderImpl.class */
public class StreamDecoderImpl implements StreamDecoder {
    @Override // com.oracle.webservices.internal.impl.internalspi.encoding.StreamDecoder
    public Message decode(InputStream in, String charset, AttachmentSet att, SOAPVersion soapVersion) throws IOException {
        XMLStreamReader reader = XMLStreamReaderFactory.create(null, in, charset, true);
        return StreamSOAPCodec.decode(soapVersion, new TidyXMLStreamReader(reader, in), att);
    }
}
