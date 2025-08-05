package com.sun.xml.internal.ws.streaming;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.ws.util.xml.XMLStreamReaderFilter;
import java.io.Closeable;
import java.io.IOException;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.ws.WebServiceException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/streaming/TidyXMLStreamReader.class */
public class TidyXMLStreamReader extends XMLStreamReaderFilter {
    private final Closeable closeableSource;

    public TidyXMLStreamReader(@NotNull XMLStreamReader reader, @Nullable Closeable closeableSource) {
        super(reader);
        this.closeableSource = closeableSource;
    }

    @Override // com.sun.xml.internal.ws.util.xml.XMLStreamReaderFilter, javax.xml.stream.XMLStreamReader
    public void close() throws XMLStreamException {
        super.close();
        try {
            if (this.closeableSource != null) {
                this.closeableSource.close();
            }
        } catch (IOException e2) {
            throw new WebServiceException(e2);
        }
    }
}
