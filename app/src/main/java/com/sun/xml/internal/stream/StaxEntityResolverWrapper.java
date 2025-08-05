package com.sun.xml.internal.stream;

import com.sun.org.apache.xerces.internal.xni.XMLResourceIdentifier;
import com.sun.org.apache.xerces.internal.xni.XNIException;
import com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLResolver;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/* loaded from: rt.jar:com/sun/xml/internal/stream/StaxEntityResolverWrapper.class */
public class StaxEntityResolverWrapper {
    XMLResolver fStaxResolver;

    public StaxEntityResolverWrapper(XMLResolver resolver) {
        this.fStaxResolver = resolver;
    }

    public void setStaxEntityResolver(XMLResolver resolver) {
        this.fStaxResolver = resolver;
    }

    public XMLResolver getStaxEntityResolver() {
        return this.fStaxResolver;
    }

    public StaxXMLInputSource resolveEntity(XMLResourceIdentifier resourceIdentifier) throws IOException, XNIException {
        try {
            Object object = this.fStaxResolver.resolveEntity(resourceIdentifier.getPublicId(), resourceIdentifier.getLiteralSystemId(), resourceIdentifier.getBaseSystemId(), null);
            return getStaxInputSource(object);
        } catch (XMLStreamException streamException) {
            throw new XNIException(streamException);
        }
    }

    StaxXMLInputSource getStaxInputSource(Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof InputStream) {
            return new StaxXMLInputSource(new XMLInputSource((String) null, (String) null, (String) null, (InputStream) object, (String) null), true);
        }
        if (object instanceof XMLStreamReader) {
            return new StaxXMLInputSource((XMLStreamReader) object, true);
        }
        if (object instanceof XMLEventReader) {
            return new StaxXMLInputSource((XMLEventReader) object, true);
        }
        return null;
    }
}
