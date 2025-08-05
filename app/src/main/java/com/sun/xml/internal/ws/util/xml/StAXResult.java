package com.sun.xml.internal.ws.util.xml;

import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.sax.SAXResult;

/* loaded from: rt.jar:com/sun/xml/internal/ws/util/xml/StAXResult.class */
public class StAXResult extends SAXResult {
    public StAXResult(XMLStreamWriter writer) {
        if (writer == null) {
            throw new IllegalArgumentException();
        }
        super.setHandler(new ContentHandlerToXMLStreamWriter(writer));
    }
}
