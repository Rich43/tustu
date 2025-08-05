package com.sun.xml.internal.ws.encoding.fastinfoset;

import com.sun.xml.internal.fastinfoset.stax.StAXDocumentParser;
import com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory;
import java.io.InputStream;
import java.io.Reader;
import javax.xml.stream.XMLStreamReader;

/* loaded from: rt.jar:com/sun/xml/internal/ws/encoding/fastinfoset/FastInfosetStreamReaderFactory.class */
public final class FastInfosetStreamReaderFactory extends XMLStreamReaderFactory {
    private static final FastInfosetStreamReaderFactory factory = new FastInfosetStreamReaderFactory();
    private ThreadLocal<StAXDocumentParser> pool = new ThreadLocal<>();

    public static FastInfosetStreamReaderFactory getInstance() {
        return factory;
    }

    @Override // com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory
    public XMLStreamReader doCreate(String systemId, InputStream in, boolean rejectDTDs) {
        StAXDocumentParser parser = fetch();
        if (parser == null) {
            return FastInfosetCodec.createNewStreamReaderRecyclable(in, false);
        }
        parser.setInputStream(in);
        return parser;
    }

    @Override // com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory
    public XMLStreamReader doCreate(String systemId, Reader reader, boolean rejectDTDs) {
        throw new UnsupportedOperationException();
    }

    private StAXDocumentParser fetch() {
        StAXDocumentParser parser = this.pool.get();
        this.pool.set(null);
        return parser;
    }

    @Override // com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory
    public void doRecycle(XMLStreamReader r2) {
        if (r2 instanceof StAXDocumentParser) {
            this.pool.set((StAXDocumentParser) r2);
        }
    }
}
