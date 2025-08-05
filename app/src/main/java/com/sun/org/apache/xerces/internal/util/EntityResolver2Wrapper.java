package com.sun.org.apache.xerces.internal.util;

import com.sun.org.apache.xerces.internal.impl.ExternalSubsetResolver;
import com.sun.org.apache.xerces.internal.impl.XMLEntityDescription;
import com.sun.org.apache.xerces.internal.xni.XMLResourceIdentifier;
import com.sun.org.apache.xerces.internal.xni.XNIException;
import com.sun.org.apache.xerces.internal.xni.grammars.XMLDTDDescription;
import com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.ext.EntityResolver2;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/util/EntityResolver2Wrapper.class */
public class EntityResolver2Wrapper implements ExternalSubsetResolver {
    protected EntityResolver2 fEntityResolver;

    public EntityResolver2Wrapper() {
    }

    public EntityResolver2Wrapper(EntityResolver2 entityResolver) {
        setEntityResolver(entityResolver);
    }

    public void setEntityResolver(EntityResolver2 entityResolver) {
        this.fEntityResolver = entityResolver;
    }

    public EntityResolver2 getEntityResolver() {
        return this.fEntityResolver;
    }

    @Override // com.sun.org.apache.xerces.internal.impl.ExternalSubsetResolver
    public XMLInputSource getExternalSubset(XMLDTDDescription grammarDescription) throws IOException, XNIException {
        if (this.fEntityResolver != null) {
            String name = grammarDescription.getRootName();
            String baseURI = grammarDescription.getBaseSystemId();
            try {
                InputSource inputSource = this.fEntityResolver.getExternalSubset(name, baseURI);
                if (inputSource != null) {
                    return createXMLInputSource(inputSource, baseURI);
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

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLEntityResolver
    public XMLInputSource resolveEntity(XMLResourceIdentifier resourceIdentifier) throws IOException, XNIException {
        if (this.fEntityResolver != null) {
            String pubId = resourceIdentifier.getPublicId();
            String sysId = resourceIdentifier.getLiteralSystemId();
            String baseURI = resourceIdentifier.getBaseSystemId();
            String name = null;
            if (resourceIdentifier instanceof XMLDTDDescription) {
                name = "[dtd]";
            } else if (resourceIdentifier instanceof XMLEntityDescription) {
                name = ((XMLEntityDescription) resourceIdentifier).getEntityName();
            }
            if (pubId == null && sysId == null) {
                return null;
            }
            try {
                InputSource inputSource = this.fEntityResolver.resolveEntity(name, pubId, baseURI, sysId);
                if (inputSource != null) {
                    return createXMLInputSource(inputSource, baseURI);
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

    private XMLInputSource createXMLInputSource(InputSource source, String baseURI) {
        String publicId = source.getPublicId();
        String systemId = source.getSystemId();
        InputStream byteStream = source.getByteStream();
        Reader charStream = source.getCharacterStream();
        String encoding = source.getEncoding();
        XMLInputSource xmlInputSource = new XMLInputSource(publicId, systemId, baseURI);
        xmlInputSource.setByteStream(byteStream);
        xmlInputSource.setCharacterStream(charStream);
        xmlInputSource.setEncoding(encoding);
        return xmlInputSource;
    }
}
