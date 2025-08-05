package com.sun.xml.internal.ws.api.server;

import com.sun.xml.internal.stream.buffer.XMLStreamBuffer;
import com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory;
import com.sun.xml.internal.ws.streaming.TidyXMLStreamReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/server/SDDocumentSource.class */
public abstract class SDDocumentSource {
    public abstract XMLStreamReader read(XMLInputFactory xMLInputFactory) throws XMLStreamException, IOException;

    public abstract XMLStreamReader read() throws XMLStreamException, IOException;

    public abstract URL getSystemId();

    public static SDDocumentSource create(final URL url) {
        return new SDDocumentSource() { // from class: com.sun.xml.internal.ws.api.server.SDDocumentSource.1
            private final URL systemId;

            {
                this.systemId = url;
            }

            @Override // com.sun.xml.internal.ws.api.server.SDDocumentSource
            public XMLStreamReader read(XMLInputFactory xif) throws XMLStreamException, IOException {
                InputStream is = url.openStream();
                return new TidyXMLStreamReader(xif.createXMLStreamReader(this.systemId.toExternalForm(), is), is);
            }

            @Override // com.sun.xml.internal.ws.api.server.SDDocumentSource
            public XMLStreamReader read() throws XMLStreamException, IOException {
                InputStream is = url.openStream();
                return new TidyXMLStreamReader(XMLStreamReaderFactory.create(this.systemId.toExternalForm(), is, false), is);
            }

            @Override // com.sun.xml.internal.ws.api.server.SDDocumentSource
            public URL getSystemId() {
                return this.systemId;
            }
        };
    }

    public static SDDocumentSource create(final URL systemId, final XMLStreamBuffer xsb) {
        return new SDDocumentSource() { // from class: com.sun.xml.internal.ws.api.server.SDDocumentSource.2
            @Override // com.sun.xml.internal.ws.api.server.SDDocumentSource
            public XMLStreamReader read(XMLInputFactory xif) throws XMLStreamException {
                return xsb.readAsXMLStreamReader();
            }

            @Override // com.sun.xml.internal.ws.api.server.SDDocumentSource
            public XMLStreamReader read() throws XMLStreamException {
                return xsb.readAsXMLStreamReader();
            }

            @Override // com.sun.xml.internal.ws.api.server.SDDocumentSource
            public URL getSystemId() {
                return systemId;
            }
        };
    }
}
