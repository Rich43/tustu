package com.sun.org.apache.xerces.internal.util;

import com.sun.org.apache.xerces.internal.util.URI;
import com.sun.org.apache.xerces.internal.xni.XMLResourceIdentifier;
import com.sun.org.apache.xerces.internal.xni.XNIException;
import com.sun.org.apache.xerces.internal.xni.parser.XMLEntityResolver;
import com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;
import com.sun.org.apache.xml.internal.resolver.Catalog;
import com.sun.org.apache.xml.internal.resolver.CatalogManager;
import com.sun.org.apache.xml.internal.resolver.readers.OASISXMLCatalogReader;
import com.sun.org.apache.xml.internal.resolver.readers.SAXCatalogReader;
import com.sun.xml.internal.ws.encoding.xml.XMLCodec;
import java.io.IOException;
import javax.xml.parsers.SAXParserFactory;
import jdk.xml.internal.JdkXmlUtils;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.ext.EntityResolver2;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/util/XMLCatalogResolver.class */
public class XMLCatalogResolver implements XMLEntityResolver, EntityResolver2, LSResourceResolver {
    private CatalogManager fResolverCatalogManager;
    private Catalog fCatalog;
    private String[] fCatalogsList;
    private boolean fCatalogsChanged;
    private boolean fPreferPublic;
    private boolean fUseLiteralSystemId;

    public XMLCatalogResolver() {
        this(null, true);
    }

    public XMLCatalogResolver(String[] catalogs) {
        this(catalogs, true);
    }

    public XMLCatalogResolver(String[] catalogs, boolean preferPublic) {
        this.fResolverCatalogManager = null;
        this.fCatalog = null;
        this.fCatalogsList = null;
        this.fCatalogsChanged = true;
        this.fPreferPublic = true;
        this.fUseLiteralSystemId = true;
        init(catalogs, preferPublic);
    }

    public final synchronized String[] getCatalogList() {
        if (this.fCatalogsList != null) {
            return (String[]) this.fCatalogsList.clone();
        }
        return null;
    }

    public final synchronized void setCatalogList(String[] catalogs) {
        this.fCatalogsChanged = true;
        this.fCatalogsList = catalogs != null ? (String[]) catalogs.clone() : null;
    }

    public final synchronized void clear() {
        this.fCatalog = null;
    }

    public final boolean getPreferPublic() {
        return this.fPreferPublic;
    }

    public final void setPreferPublic(boolean preferPublic) {
        this.fPreferPublic = preferPublic;
        this.fResolverCatalogManager.setPreferPublic(preferPublic);
    }

    public final boolean getUseLiteralSystemId() {
        return this.fUseLiteralSystemId;
    }

    public final void setUseLiteralSystemId(boolean useLiteralSystemId) {
        this.fUseLiteralSystemId = useLiteralSystemId;
    }

    @Override // org.xml.sax.EntityResolver
    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
        String resolvedId = null;
        if (publicId != null && systemId != null) {
            resolvedId = resolvePublic(publicId, systemId);
        } else if (systemId != null) {
            resolvedId = resolveSystem(systemId);
        }
        if (resolvedId != null) {
            InputSource source = new InputSource(resolvedId);
            source.setPublicId(publicId);
            return source;
        }
        return null;
    }

    @Override // org.xml.sax.ext.EntityResolver2
    public InputSource resolveEntity(String name, String publicId, String baseURI, String systemId) throws SAXException, IOException {
        String resolvedId = null;
        if (!getUseLiteralSystemId() && baseURI != null) {
            try {
                URI uri = new URI(new URI(baseURI), systemId);
                systemId = uri.toString();
            } catch (URI.MalformedURIException e2) {
            }
        }
        if (publicId != null && systemId != null) {
            resolvedId = resolvePublic(publicId, systemId);
        } else if (systemId != null) {
            resolvedId = resolveSystem(systemId);
        }
        if (resolvedId != null) {
            InputSource source = new InputSource(resolvedId);
            source.setPublicId(publicId);
            return source;
        }
        return null;
    }

    @Override // org.xml.sax.ext.EntityResolver2
    public InputSource getExternalSubset(String name, String baseURI) throws SAXException, IOException {
        return null;
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x003f  */
    @Override // org.w3c.dom.ls.LSResourceResolver
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public org.w3c.dom.ls.LSInput resolveResource(java.lang.String r7, java.lang.String r8, java.lang.String r9, java.lang.String r10, java.lang.String r11) {
        /*
            r6 = this;
            r0 = 0
            r12 = r0
            r0 = r8
            if (r0 == 0) goto Le
            r0 = r6
            r1 = r8
            java.lang.String r0 = r0.resolveURI(r1)     // Catch: java.io.IOException -> L64
            r12 = r0
        Le:
            r0 = r6
            boolean r0 = r0.getUseLiteralSystemId()     // Catch: java.io.IOException -> L64
            if (r0 != 0) goto L3a
            r0 = r11
            if (r0 == 0) goto L3a
            com.sun.org.apache.xerces.internal.util.URI r0 = new com.sun.org.apache.xerces.internal.util.URI     // Catch: com.sun.org.apache.xerces.internal.util.URI.MalformedURIException -> L38 java.io.IOException -> L64
            r1 = r0
            com.sun.org.apache.xerces.internal.util.URI r2 = new com.sun.org.apache.xerces.internal.util.URI     // Catch: com.sun.org.apache.xerces.internal.util.URI.MalformedURIException -> L38 java.io.IOException -> L64
            r3 = r2
            r4 = r11
            r3.<init>(r4)     // Catch: com.sun.org.apache.xerces.internal.util.URI.MalformedURIException -> L38 java.io.IOException -> L64
            r3 = r10
            r1.<init>(r2, r3)     // Catch: com.sun.org.apache.xerces.internal.util.URI.MalformedURIException -> L38 java.io.IOException -> L64
            r13 = r0
            r0 = r13
            java.lang.String r0 = r0.toString()     // Catch: com.sun.org.apache.xerces.internal.util.URI.MalformedURIException -> L38 java.io.IOException -> L64
            r10 = r0
            goto L3a
        L38:
            r13 = move-exception
        L3a:
            r0 = r12
            if (r0 != 0) goto L61
            r0 = r9
            if (r0 == 0) goto L54
            r0 = r10
            if (r0 == 0) goto L54
            r0 = r6
            r1 = r9
            r2 = r10
            java.lang.String r0 = r0.resolvePublic(r1, r2)     // Catch: java.io.IOException -> L64
            r12 = r0
            goto L61
        L54:
            r0 = r10
            if (r0 == 0) goto L61
            r0 = r6
            r1 = r10
            java.lang.String r0 = r0.resolveSystem(r1)     // Catch: java.io.IOException -> L64
            r12 = r0
        L61:
            goto L66
        L64:
            r13 = move-exception
        L66:
            r0 = r12
            if (r0 == 0) goto L78
            com.sun.org.apache.xerces.internal.dom.DOMInputImpl r0 = new com.sun.org.apache.xerces.internal.dom.DOMInputImpl
            r1 = r0
            r2 = r9
            r3 = r12
            r4 = r11
            r1.<init>(r2, r3, r4)
            return r0
        L78:
            r0 = 0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.org.apache.xerces.internal.util.XMLCatalogResolver.resolveResource(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String):org.w3c.dom.ls.LSInput");
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLEntityResolver
    public XMLInputSource resolveEntity(XMLResourceIdentifier resourceIdentifier) throws IOException, XNIException {
        String resolvedId = resolveIdentifier(resourceIdentifier);
        if (resolvedId != null) {
            return new XMLInputSource(resourceIdentifier.getPublicId(), resolvedId, resourceIdentifier.getBaseSystemId());
        }
        return null;
    }

    public String resolveIdentifier(XMLResourceIdentifier resourceIdentifier) throws IOException, XNIException {
        String expandedSystemId;
        String resolvedId = null;
        String namespace = resourceIdentifier.getNamespace();
        if (namespace != null) {
            resolvedId = resolveURI(namespace);
        }
        if (resolvedId == null) {
            String publicId = resourceIdentifier.getPublicId();
            if (getUseLiteralSystemId()) {
                expandedSystemId = resourceIdentifier.getLiteralSystemId();
            } else {
                expandedSystemId = resourceIdentifier.getExpandedSystemId();
            }
            String systemId = expandedSystemId;
            if (publicId != null && systemId != null) {
                resolvedId = resolvePublic(publicId, systemId);
            } else if (systemId != null) {
                resolvedId = resolveSystem(systemId);
            }
        }
        return resolvedId;
    }

    public final synchronized String resolveSystem(String systemId) throws IOException {
        if (this.fCatalogsChanged) {
            parseCatalogs();
            this.fCatalogsChanged = false;
        }
        if (this.fCatalog != null) {
            return this.fCatalog.resolveSystem(systemId);
        }
        return null;
    }

    public final synchronized String resolvePublic(String publicId, String systemId) throws IOException {
        if (this.fCatalogsChanged) {
            parseCatalogs();
            this.fCatalogsChanged = false;
        }
        if (this.fCatalog != null) {
            return this.fCatalog.resolvePublic(publicId, systemId);
        }
        return null;
    }

    public final synchronized String resolveURI(String uri) throws IOException {
        if (this.fCatalogsChanged) {
            parseCatalogs();
            this.fCatalogsChanged = false;
        }
        if (this.fCatalog != null) {
            return this.fCatalog.resolveURI(uri);
        }
        return null;
    }

    private void init(String[] catalogs, boolean preferPublic) {
        this.fCatalogsList = catalogs != null ? (String[]) catalogs.clone() : null;
        this.fPreferPublic = preferPublic;
        this.fResolverCatalogManager = new CatalogManager();
        this.fResolverCatalogManager.setAllowOasisXMLCatalogPI(false);
        this.fResolverCatalogManager.setCatalogClassName("com.sun.org.apache.xml.internal.resolver.Catalog");
        this.fResolverCatalogManager.setCatalogFiles("");
        this.fResolverCatalogManager.setIgnoreMissingProperties(true);
        this.fResolverCatalogManager.setPreferPublic(this.fPreferPublic);
        this.fResolverCatalogManager.setRelativeCatalogs(false);
        this.fResolverCatalogManager.setUseStaticCatalog(false);
        this.fResolverCatalogManager.setVerbosity(0);
    }

    private void parseCatalogs() throws IOException {
        if (this.fCatalogsList != null) {
            this.fCatalog = new Catalog(this.fResolverCatalogManager);
            attachReaderToCatalog(this.fCatalog);
            for (int i2 = 0; i2 < this.fCatalogsList.length; i2++) {
                String catalog = this.fCatalogsList[i2];
                if (catalog != null && catalog.length() > 0) {
                    this.fCatalog.parseCatalog(catalog);
                }
            }
            return;
        }
        this.fCatalog = null;
    }

    private void attachReaderToCatalog(Catalog catalog) {
        SAXParserFactory spf = JdkXmlUtils.getSAXFactory(catalog.getCatalogManager().overrideDefaultParser());
        spf.setValidating(false);
        SAXCatalogReader saxReader = new SAXCatalogReader(spf);
        saxReader.setCatalogParser(OASISXMLCatalogReader.namespaceName, "catalog", "com.sun.org.apache.xml.internal.resolver.readers.OASISXMLCatalogReader");
        catalog.addReader(XMLCodec.XML_APPLICATION_MIME_TYPE, saxReader);
    }
}
