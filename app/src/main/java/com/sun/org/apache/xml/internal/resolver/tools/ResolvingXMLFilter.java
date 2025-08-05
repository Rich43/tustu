package com.sun.org.apache.xml.internal.resolver.tools;

import com.sun.org.apache.xml.internal.resolver.Catalog;
import com.sun.org.apache.xml.internal.resolver.CatalogManager;
import com.sun.org.apache.xml.internal.resolver.helpers.FileURL;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLFilterImpl;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/resolver/tools/ResolvingXMLFilter.class */
public class ResolvingXMLFilter extends XMLFilterImpl {
    public static boolean suppressExplanation = false;
    CatalogManager catalogManager;
    private CatalogResolver catalogResolver;
    private CatalogResolver piCatalogResolver;
    private boolean allowXMLCatalogPI;
    private boolean oasisXMLCatalogPI;
    private URL baseURL;

    public ResolvingXMLFilter() {
        this.catalogManager = CatalogManager.getStaticManager();
        this.catalogResolver = null;
        this.piCatalogResolver = null;
        this.allowXMLCatalogPI = false;
        this.oasisXMLCatalogPI = false;
        this.baseURL = null;
        this.catalogResolver = new CatalogResolver(this.catalogManager);
    }

    public ResolvingXMLFilter(XMLReader parent) {
        super(parent);
        this.catalogManager = CatalogManager.getStaticManager();
        this.catalogResolver = null;
        this.piCatalogResolver = null;
        this.allowXMLCatalogPI = false;
        this.oasisXMLCatalogPI = false;
        this.baseURL = null;
        this.catalogResolver = new CatalogResolver(this.catalogManager);
    }

    public ResolvingXMLFilter(CatalogManager manager) {
        this.catalogManager = CatalogManager.getStaticManager();
        this.catalogResolver = null;
        this.piCatalogResolver = null;
        this.allowXMLCatalogPI = false;
        this.oasisXMLCatalogPI = false;
        this.baseURL = null;
        this.catalogManager = manager;
        this.catalogResolver = new CatalogResolver(this.catalogManager);
    }

    public ResolvingXMLFilter(XMLReader parent, CatalogManager manager) {
        super(parent);
        this.catalogManager = CatalogManager.getStaticManager();
        this.catalogResolver = null;
        this.piCatalogResolver = null;
        this.allowXMLCatalogPI = false;
        this.oasisXMLCatalogPI = false;
        this.baseURL = null;
        this.catalogManager = manager;
        this.catalogResolver = new CatalogResolver(this.catalogManager);
    }

    public Catalog getCatalog() {
        return this.catalogResolver.getCatalog();
    }

    @Override // org.xml.sax.helpers.XMLFilterImpl, org.xml.sax.XMLReader
    public void parse(InputSource input) throws SAXException, IOException {
        this.allowXMLCatalogPI = true;
        setupBaseURI(input.getSystemId());
        try {
            super.parse(input);
        } catch (InternalError ie) {
            explain(input.getSystemId());
            throw ie;
        }
    }

    @Override // org.xml.sax.helpers.XMLFilterImpl, org.xml.sax.XMLReader
    public void parse(String systemId) throws SAXException, IOException {
        this.allowXMLCatalogPI = true;
        setupBaseURI(systemId);
        try {
            super.parse(systemId);
        } catch (InternalError ie) {
            explain(systemId);
            throw ie;
        }
    }

    @Override // org.xml.sax.helpers.XMLFilterImpl, org.xml.sax.EntityResolver
    public InputSource resolveEntity(String publicId, String systemId) {
        this.allowXMLCatalogPI = false;
        String resolved = this.catalogResolver.getResolvedEntity(publicId, systemId);
        if (resolved == null && this.piCatalogResolver != null) {
            resolved = this.piCatalogResolver.getResolvedEntity(publicId, systemId);
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
                this.catalogManager.debug.message(1, "Failed to create InputSource", resolved);
                return null;
            }
        }
        return null;
    }

    @Override // org.xml.sax.helpers.XMLFilterImpl, org.xml.sax.DTDHandler
    public void notationDecl(String name, String publicId, String systemId) throws SAXException {
        this.allowXMLCatalogPI = false;
        super.notationDecl(name, publicId, systemId);
    }

    @Override // org.xml.sax.helpers.XMLFilterImpl, org.xml.sax.DTDHandler
    public void unparsedEntityDecl(String name, String publicId, String systemId, String notationName) throws SAXException {
        this.allowXMLCatalogPI = false;
        super.unparsedEntityDecl(name, publicId, systemId, notationName);
    }

    @Override // org.xml.sax.helpers.XMLFilterImpl, org.xml.sax.ContentHandler
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        this.allowXMLCatalogPI = false;
        super.startElement(uri, localName, qName, atts);
    }

    @Override // org.xml.sax.helpers.XMLFilterImpl, org.xml.sax.ContentHandler
    public void processingInstruction(String target, String pidata) throws SAXException {
        if (target.equals("oasis-xml-catalog")) {
            URL catalog = null;
            int pos = pidata.indexOf("catalog=");
            if (pos >= 0) {
                String data = pidata.substring(pos + 8);
                if (data.length() > 1) {
                    String quote = data.substring(0, 1);
                    String data2 = data.substring(1);
                    int pos2 = data2.indexOf(quote);
                    if (pos2 >= 0) {
                        String data3 = data2.substring(0, pos2);
                        try {
                            if (this.baseURL != null) {
                                catalog = new URL(this.baseURL, data3);
                            } else {
                                catalog = new URL(data3);
                            }
                        } catch (MalformedURLException e2) {
                        }
                    }
                }
            }
            if (this.allowXMLCatalogPI) {
                if (this.catalogManager.getAllowOasisXMLCatalogPI()) {
                    this.catalogManager.debug.message(4, "oasis-xml-catalog PI", pidata);
                    if (catalog != null) {
                        try {
                            this.catalogManager.debug.message(4, "oasis-xml-catalog", catalog.toString());
                            this.oasisXMLCatalogPI = true;
                            if (this.piCatalogResolver == null) {
                                this.piCatalogResolver = new CatalogResolver(true);
                            }
                            this.piCatalogResolver.getCatalog().parseCatalog(catalog.toString());
                            return;
                        } catch (Exception e3) {
                            this.catalogManager.debug.message(3, "Exception parsing oasis-xml-catalog: " + catalog.toString());
                            return;
                        }
                    }
                    this.catalogManager.debug.message(3, "PI oasis-xml-catalog unparseable: " + pidata);
                    return;
                }
                this.catalogManager.debug.message(4, "PI oasis-xml-catalog ignored: " + pidata);
                return;
            }
            this.catalogManager.debug.message(3, "PI oasis-xml-catalog occurred in an invalid place: " + pidata);
            return;
        }
        super.processingInstruction(target, pidata);
    }

    private void setupBaseURI(String systemId) {
        URL cwd;
        try {
            cwd = FileURL.makeURL("basename");
        } catch (MalformedURLException e2) {
            cwd = null;
        }
        try {
            this.baseURL = new URL(systemId);
        } catch (MalformedURLException e3) {
            if (cwd != null) {
                try {
                    this.baseURL = new URL(cwd, systemId);
                    return;
                } catch (MalformedURLException e4) {
                    this.baseURL = null;
                    return;
                }
            }
            this.baseURL = null;
        }
    }

    private void explain(String systemId) {
        if (!suppressExplanation) {
            System.out.println("XMLReader probably encountered bad URI in " + systemId);
            System.out.println("For example, replace '/some/uri' with 'file:/some/uri'.");
        }
        suppressExplanation = true;
    }
}
