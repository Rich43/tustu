package com.sun.xml.internal.ws.api.wsdl.parser;

import com.sun.xml.internal.ws.api.server.SDDocumentSource;
import java.io.IOException;
import java.net.URL;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/wsdl/parser/XMLEntityResolver.class */
public interface XMLEntityResolver {
    Parser resolveEntity(String str, String str2) throws SAXException, XMLStreamException, IOException;

    /* loaded from: rt.jar:com/sun/xml/internal/ws/api/wsdl/parser/XMLEntityResolver$Parser.class */
    public static final class Parser {
        public final URL systemId;
        public final XMLStreamReader parser;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !XMLEntityResolver.class.desiredAssertionStatus();
        }

        public Parser(URL systemId, XMLStreamReader parser) {
            if (!$assertionsDisabled && parser == null) {
                throw new AssertionError();
            }
            this.systemId = systemId;
            this.parser = parser;
        }

        public Parser(SDDocumentSource doc) throws XMLStreamException, IOException {
            this.systemId = doc.getSystemId();
            this.parser = doc.read();
        }
    }
}
