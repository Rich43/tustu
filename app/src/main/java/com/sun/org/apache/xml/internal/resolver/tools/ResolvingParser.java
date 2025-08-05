package com.sun.org.apache.xml.internal.resolver.tools;

import com.sun.org.apache.xml.internal.resolver.Catalog;
import com.sun.org.apache.xml.internal.resolver.CatalogManager;
import com.sun.org.apache.xml.internal.resolver.helpers.FileURL;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import jdk.xml.internal.JdkXmlUtils;
import org.xml.sax.AttributeList;
import org.xml.sax.DTDHandler;
import org.xml.sax.DocumentHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.Parser;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/resolver/tools/ResolvingParser.class */
public class ResolvingParser implements Parser, DTDHandler, DocumentHandler, EntityResolver {
    public static boolean namespaceAware = true;
    public static boolean validating = false;
    public static boolean suppressExplanation = false;
    private SAXParser saxParser;
    private Parser parser;
    private DocumentHandler documentHandler;
    private DTDHandler dtdHandler;
    private CatalogManager catalogManager;
    private CatalogResolver catalogResolver;
    private CatalogResolver piCatalogResolver;
    private boolean allowXMLCatalogPI;
    private boolean oasisXMLCatalogPI;
    private URL baseURL;

    public ResolvingParser() {
        this.saxParser = null;
        this.parser = null;
        this.documentHandler = null;
        this.dtdHandler = null;
        this.catalogManager = CatalogManager.getStaticManager();
        this.catalogResolver = null;
        this.piCatalogResolver = null;
        this.allowXMLCatalogPI = false;
        this.oasisXMLCatalogPI = false;
        this.baseURL = null;
        initParser();
    }

    public ResolvingParser(CatalogManager manager) {
        this.saxParser = null;
        this.parser = null;
        this.documentHandler = null;
        this.dtdHandler = null;
        this.catalogManager = CatalogManager.getStaticManager();
        this.catalogResolver = null;
        this.piCatalogResolver = null;
        this.allowXMLCatalogPI = false;
        this.oasisXMLCatalogPI = false;
        this.baseURL = null;
        this.catalogManager = manager;
        initParser();
    }

    private void initParser() {
        this.catalogResolver = new CatalogResolver(this.catalogManager);
        SAXParserFactory spf = JdkXmlUtils.getSAXFactory(this.catalogManager.overrideDefaultParser());
        spf.setValidating(validating);
        try {
            this.saxParser = spf.newSAXParser();
            this.parser = this.saxParser.getParser();
            this.documentHandler = null;
            this.dtdHandler = null;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Catalog getCatalog() {
        return this.catalogResolver.getCatalog();
    }

    @Override // org.xml.sax.Parser, org.xml.sax.XMLReader
    public void parse(InputSource input) throws SAXException, IOException {
        setupParse(input.getSystemId());
        try {
            this.parser.parse(input);
        } catch (InternalError ie) {
            explain(input.getSystemId());
            throw ie;
        }
    }

    @Override // org.xml.sax.Parser, org.xml.sax.XMLReader
    public void parse(String systemId) throws SAXException, IOException {
        setupParse(systemId);
        try {
            this.parser.parse(systemId);
        } catch (InternalError ie) {
            explain(systemId);
            throw ie;
        }
    }

    @Override // org.xml.sax.Parser
    public void setDocumentHandler(DocumentHandler handler) {
        this.documentHandler = handler;
    }

    @Override // org.xml.sax.Parser, org.xml.sax.XMLReader
    public void setDTDHandler(DTDHandler handler) {
        this.dtdHandler = handler;
    }

    @Override // org.xml.sax.Parser, org.xml.sax.XMLReader
    public void setEntityResolver(EntityResolver resolver) {
    }

    @Override // org.xml.sax.Parser, org.xml.sax.XMLReader
    public void setErrorHandler(ErrorHandler handler) {
        this.parser.setErrorHandler(handler);
    }

    @Override // org.xml.sax.Parser
    public void setLocale(Locale locale) throws SAXException {
        this.parser.setLocale(locale);
    }

    @Override // org.xml.sax.DocumentHandler
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (this.documentHandler != null) {
            this.documentHandler.characters(ch, start, length);
        }
    }

    @Override // org.xml.sax.DocumentHandler
    public void endDocument() throws SAXException {
        if (this.documentHandler != null) {
            this.documentHandler.endDocument();
        }
    }

    @Override // org.xml.sax.DocumentHandler
    public void endElement(String name) throws SAXException {
        if (this.documentHandler != null) {
            this.documentHandler.endElement(name);
        }
    }

    @Override // org.xml.sax.DocumentHandler
    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
        if (this.documentHandler != null) {
            this.documentHandler.ignorableWhitespace(ch, start, length);
        }
    }

    @Override // org.xml.sax.DocumentHandler
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
        if (this.documentHandler != null) {
            this.documentHandler.processingInstruction(target, pidata);
        }
    }

    @Override // org.xml.sax.DocumentHandler
    public void setDocumentLocator(Locator locator) {
        if (this.documentHandler != null) {
            this.documentHandler.setDocumentLocator(locator);
        }
    }

    @Override // org.xml.sax.DocumentHandler
    public void startDocument() throws SAXException {
        if (this.documentHandler != null) {
            this.documentHandler.startDocument();
        }
    }

    @Override // org.xml.sax.DocumentHandler
    public void startElement(String name, AttributeList atts) throws SAXException {
        this.allowXMLCatalogPI = false;
        if (this.documentHandler != null) {
            this.documentHandler.startElement(name, atts);
        }
    }

    @Override // org.xml.sax.DTDHandler
    public void notationDecl(String name, String publicId, String systemId) throws SAXException {
        this.allowXMLCatalogPI = false;
        if (this.dtdHandler != null) {
            this.dtdHandler.notationDecl(name, publicId, systemId);
        }
    }

    @Override // org.xml.sax.DTDHandler
    public void unparsedEntityDecl(String name, String publicId, String systemId, String notationName) throws SAXException {
        this.allowXMLCatalogPI = false;
        if (this.dtdHandler != null) {
            this.dtdHandler.unparsedEntityDecl(name, publicId, systemId, notationName);
        }
    }

    @Override // org.xml.sax.EntityResolver
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

    private void setupParse(String systemId) {
        URL cwd;
        this.allowXMLCatalogPI = true;
        this.parser.setEntityResolver(this);
        this.parser.setDocumentHandler(this);
        this.parser.setDTDHandler(this);
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
            System.out.println("Parser probably encountered bad URI in " + systemId);
            System.out.println("For example, replace '/some/uri' with 'file:/some/uri'.");
        }
    }
}
