package com.sun.org.apache.xerces.internal.util;

import com.sun.org.apache.xerces.internal.xni.XMLResourceIdentifier;
import com.sun.org.apache.xerces.internal.xni.XNIException;
import com.sun.org.apache.xerces.internal.xni.parser.XMLEntityResolver;
import com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/util/EntityResolverWrapper.class */
public class EntityResolverWrapper implements XMLEntityResolver {
    protected EntityResolver fEntityResolver;

    public EntityResolverWrapper() {
    }

    public EntityResolverWrapper(EntityResolver entityResolver) {
        setEntityResolver(entityResolver);
    }

    public void setEntityResolver(EntityResolver entityResolver) {
        this.fEntityResolver = entityResolver;
    }

    public EntityResolver getEntityResolver() {
        return this.fEntityResolver;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLEntityResolver
    public XMLInputSource resolveEntity(XMLResourceIdentifier resourceIdentifier) throws IOException, XNIException {
        String pubId = resourceIdentifier.getPublicId();
        String sysId = resourceIdentifier.getExpandedSystemId();
        if ((pubId != null || sysId != null) && this.fEntityResolver != null && resourceIdentifier != null) {
            try {
                InputSource inputSource = this.fEntityResolver.resolveEntity(pubId, sysId);
                if (inputSource != null) {
                    String publicId = inputSource.getPublicId();
                    String systemId = inputSource.getSystemId();
                    String baseSystemId = resourceIdentifier.getBaseSystemId();
                    InputStream byteStream = inputSource.getByteStream();
                    Reader charStream = inputSource.getCharacterStream();
                    String encoding = inputSource.getEncoding();
                    XMLInputSource xmlInputSource = new XMLInputSource(publicId, systemId, baseSystemId);
                    xmlInputSource.setByteStream(byteStream);
                    xmlInputSource.setCharacterStream(charStream);
                    xmlInputSource.setEncoding(encoding);
                    return xmlInputSource;
                }
                return null;
            } catch (SAXException e2) {
                Exception ex = e2.getException();
                if (ex == null) {
                    ex = e2;
                }
                throw new XNIException(ex);
            }
        }
        return null;
    }
}
