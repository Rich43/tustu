package com.sun.xml.internal.ws.wsdl.parser;

import com.sun.xml.internal.stream.buffer.XMLStreamBufferResult;
import com.sun.xml.internal.ws.api.server.SDDocumentSource;
import com.sun.xml.internal.ws.api.wsdl.parser.XMLEntityResolver;
import com.sun.xml.internal.ws.util.JAXWSUtils;
import com.sun.xml.internal.ws.util.xml.XmlUtil;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.ws.WebServiceException;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/wsdl/parser/MexEntityResolver.class */
public final class MexEntityResolver implements XMLEntityResolver {
    private final Map<String, SDDocumentSource> wsdls = new HashMap();

    public MexEntityResolver(List<? extends Source> wsdls) throws IOException {
        Transformer transformer = XmlUtil.newTransformer();
        for (Source source : wsdls) {
            XMLStreamBufferResult xsbr = new XMLStreamBufferResult();
            try {
                transformer.transform(source, xsbr);
                String systemId = source.getSystemId();
                if (systemId != null) {
                    SDDocumentSource doc = SDDocumentSource.create(JAXWSUtils.getFileOrURL(systemId), xsbr.getXMLStreamBuffer());
                    this.wsdls.put(systemId, doc);
                }
            } catch (TransformerException e2) {
                throw new WebServiceException(e2);
            }
        }
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.XMLEntityResolver
    public XMLEntityResolver.Parser resolveEntity(String publicId, String systemId) throws SAXException, XMLStreamException, IOException {
        SDDocumentSource src;
        if (systemId != null && (src = this.wsdls.get(systemId)) != null) {
            return new XMLEntityResolver.Parser(src);
        }
        return null;
    }
}
