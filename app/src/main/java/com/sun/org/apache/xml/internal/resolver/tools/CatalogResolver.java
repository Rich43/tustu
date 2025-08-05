package com.sun.org.apache.xml.internal.resolver.tools;

import com.sun.org.apache.xml.internal.resolver.Catalog;
import com.sun.org.apache.xml.internal.resolver.CatalogManager;
import com.sun.org.apache.xml.internal.resolver.helpers.FileURL;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.sax.SAXSource;
import jdk.xml.internal.JdkXmlUtils;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/resolver/tools/CatalogResolver.class */
public class CatalogResolver implements EntityResolver, URIResolver {
    public boolean namespaceAware;
    public boolean validating;
    private Catalog catalog;
    private CatalogManager catalogManager;

    public CatalogResolver() {
        this.namespaceAware = true;
        this.validating = false;
        this.catalog = null;
        this.catalogManager = CatalogManager.getStaticManager();
        initializeCatalogs(false);
    }

    public CatalogResolver(boolean privateCatalog) {
        this.namespaceAware = true;
        this.validating = false;
        this.catalog = null;
        this.catalogManager = CatalogManager.getStaticManager();
        initializeCatalogs(privateCatalog);
    }

    public CatalogResolver(CatalogManager manager) {
        this.namespaceAware = true;
        this.validating = false;
        this.catalog = null;
        this.catalogManager = CatalogManager.getStaticManager();
        this.catalogManager = manager;
        initializeCatalogs(!this.catalogManager.getUseStaticCatalog());
    }

    private void initializeCatalogs(boolean privateCatalog) {
        this.catalog = this.catalogManager.getCatalog();
    }

    public Catalog getCatalog() {
        return this.catalog;
    }

    public String getResolvedEntity(String publicId, String systemId) {
        String resolved = null;
        if (this.catalog == null) {
            this.catalogManager.debug.message(1, "Catalog resolution attempted with null catalog; ignored");
            return null;
        }
        if (systemId != null) {
            try {
                resolved = this.catalog.resolveSystem(systemId);
            } catch (MalformedURLException e2) {
                this.catalogManager.debug.message(1, "Malformed URL exception trying to resolve", publicId);
                resolved = null;
            } catch (IOException e3) {
                this.catalogManager.debug.message(1, "I/O exception trying to resolve", publicId);
                resolved = null;
            }
        }
        if (resolved == null) {
            if (publicId != null) {
                try {
                    resolved = this.catalog.resolvePublic(publicId, systemId);
                } catch (MalformedURLException e4) {
                    this.catalogManager.debug.message(1, "Malformed URL exception trying to resolve", publicId);
                } catch (IOException e5) {
                    this.catalogManager.debug.message(1, "I/O exception trying to resolve", publicId);
                }
            }
            if (resolved != null) {
                this.catalogManager.debug.message(2, "Resolved public", publicId, resolved);
            }
        } else {
            this.catalogManager.debug.message(2, "Resolved system", systemId, resolved);
        }
        return resolved;
    }

    @Override // org.xml.sax.EntityResolver
    public InputSource resolveEntity(String publicId, String systemId) {
        String resolved = getResolvedEntity(publicId, systemId);
        if (resolved != null) {
            try {
                InputSource iSource = new InputSource(resolved);
                iSource.setPublicId(publicId);
                URL url = new URL(resolved);
                InputStream iStream = url.openStream();
                iSource.setByteStream(iStream);
                return iSource;
            } catch (Exception e2) {
                this.catalogManager.debug.message(1, "Failed to create InputSource", resolved);
                return null;
            }
        }
        return null;
    }

    @Override // javax.xml.transform.URIResolver
    public Source resolve(String href, String base) throws TransformerException {
        String uri = href;
        int hashPos = href.indexOf(FXMLLoader.CONTROLLER_METHOD_PREFIX);
        if (hashPos >= 0) {
            uri = href.substring(0, hashPos);
            href.substring(hashPos + 1);
        }
        String result = null;
        try {
            result = this.catalog.resolveURI(href);
        } catch (Exception e2) {
        }
        if (result == null) {
            try {
                if (base == null) {
                    URL url = new URL(uri);
                    result = url.toString();
                } else {
                    URL baseURL = new URL(base);
                    URL url2 = href.length() == 0 ? baseURL : new URL(baseURL, uri);
                    result = url2.toString();
                }
            } catch (MalformedURLException mue) {
                String absBase = makeAbsolute(base);
                if (!absBase.equals(base)) {
                    return resolve(href, absBase);
                }
                throw new TransformerException("Malformed URL " + href + "(base " + base + ")", mue);
            }
        }
        this.catalogManager.debug.message(2, "Resolved URI", href, result);
        SAXSource source = new SAXSource();
        source.setInputSource(new InputSource(result));
        setEntityResolver(source);
        return source;
    }

    private void setEntityResolver(SAXSource source) throws TransformerException {
        XMLReader reader = source.getXMLReader();
        if (reader == null) {
            SAXParserFactory spf = JdkXmlUtils.getSAXFactory(this.catalogManager.overrideDefaultParser());
            try {
                reader = spf.newSAXParser().getXMLReader();
            } catch (ParserConfigurationException ex) {
                throw new TransformerException(ex);
            } catch (SAXException ex2) {
                throw new TransformerException(ex2);
            }
        }
        reader.setEntityResolver(this);
        source.setXMLReader(reader);
    }

    private String makeAbsolute(String uri) {
        if (uri == null) {
            uri = "";
        }
        try {
            URL url = new URL(uri);
            return url.toString();
        } catch (MalformedURLException e2) {
            try {
                URL fileURL = FileURL.makeURL(uri);
                return fileURL.toString();
            } catch (MalformedURLException e3) {
                return uri;
            }
        }
    }
}
