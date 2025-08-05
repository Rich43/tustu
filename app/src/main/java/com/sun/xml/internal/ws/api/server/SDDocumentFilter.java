package com.sun.xml.internal.ws.api.server;

import java.io.IOException;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/server/SDDocumentFilter.class */
public interface SDDocumentFilter {
    XMLStreamWriter filter(SDDocument sDDocument, XMLStreamWriter xMLStreamWriter) throws XMLStreamException, IOException;
}
