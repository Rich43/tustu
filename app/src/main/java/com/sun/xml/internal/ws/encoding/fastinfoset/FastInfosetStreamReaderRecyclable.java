package com.sun.xml.internal.ws.encoding.fastinfoset;

import com.sun.xml.internal.fastinfoset.stax.StAXDocumentParser;
import com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory;
import java.io.InputStream;

/* loaded from: rt.jar:com/sun/xml/internal/ws/encoding/fastinfoset/FastInfosetStreamReaderRecyclable.class */
public final class FastInfosetStreamReaderRecyclable extends StAXDocumentParser implements XMLStreamReaderFactory.RecycleAware {
    private static final FastInfosetStreamReaderFactory READER_FACTORY = FastInfosetStreamReaderFactory.getInstance();

    public FastInfosetStreamReaderRecyclable() {
    }

    public FastInfosetStreamReaderRecyclable(InputStream in) {
        super(in);
    }

    @Override // com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory.RecycleAware
    public void onRecycled() {
        READER_FACTORY.doRecycle(this);
    }
}
