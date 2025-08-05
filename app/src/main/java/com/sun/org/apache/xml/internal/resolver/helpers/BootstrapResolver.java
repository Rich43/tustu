package com.sun.org.apache.xml.internal.resolver.helpers;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javafx.fxml.FXMLLoader;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.sax.SAXSource;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/resolver/helpers/BootstrapResolver.class */
public class BootstrapResolver implements EntityResolver, URIResolver {
    public static final String xmlCatalogXSD = "http://www.oasis-open.org/committees/entity/release/1.0/catalog.xsd";
    public static final String xmlCatalogRNG = "http://www.oasis-open.org/committees/entity/release/1.0/catalog.rng";
    public static final String xmlCatalogPubId = "-//OASIS//DTD XML Catalogs V1.0//EN";
    public static final String xmlCatalogSysId = "http://www.oasis-open.org/committees/entity/release/1.0/catalog.dtd";
    private final Map<String, String> publicMap = new HashMap();
    private final Map<String, String> systemMap = new HashMap();
    private final Map<String, String> uriMap = new HashMap();

    public BootstrapResolver() {
        URL url = getClass().getResource("/com/sun/org/apache/xml/internal/resolver/etc/catalog.dtd");
        if (url != null) {
            this.publicMap.put(xmlCatalogPubId, url.toString());
            this.systemMap.put(xmlCatalogSysId, url.toString());
        }
        URL url2 = getClass().getResource("/com/sun/org/apache/xml/internal/resolver/etc/catalog.rng");
        if (url2 != null) {
            this.uriMap.put(xmlCatalogRNG, url2.toString());
        }
        URL url3 = getClass().getResource("/com/sun/org/apache/xml/internal/resolver/etc/catalog.xsd");
        if (url3 != null) {
            this.uriMap.put(xmlCatalogXSD, url3.toString());
        }
    }

    @Override // org.xml.sax.EntityResolver
    public InputSource resolveEntity(String publicId, String systemId) {
        String resolved = null;
        if (systemId != null && this.systemMap.containsKey(systemId)) {
            resolved = this.systemMap.get(systemId);
        } else if (publicId != null && this.publicMap.containsKey(publicId)) {
            resolved = this.publicMap.get(publicId);
        }
        if (resolved != null) {
            try {
                InputSource iSource = new InputSource(resolved);
                iSource.setPublicId(publicId);
                URL url = new URL(resolved);
                InputStream iStream = url.openStream();
                iSource.setByteStream(iStream);
                return iSource;
            } catch (Exception e2) {
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
        if (href != null && this.uriMap.containsKey(href)) {
            result = this.uriMap.get(href);
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
        SAXSource source = new SAXSource();
        source.setInputSource(new InputSource(result));
        return source;
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
