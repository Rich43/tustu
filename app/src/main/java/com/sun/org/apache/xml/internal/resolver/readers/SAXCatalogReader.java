package com.sun.org.apache.xml.internal.resolver.readers;

import com.sun.org.apache.xml.internal.resolver.Catalog;
import com.sun.org.apache.xml.internal.resolver.CatalogException;
import com.sun.org.apache.xml.internal.resolver.CatalogManager;
import com.sun.org.apache.xml.internal.resolver.helpers.Debug;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.apache.commons.math3.geometry.VectorFormat;
import org.xml.sax.AttributeList;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DocumentHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.Parser;
import org.xml.sax.SAXException;
import sun.reflect.misc.ReflectUtil;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/resolver/readers/SAXCatalogReader.class */
public class SAXCatalogReader implements CatalogReader, ContentHandler, DocumentHandler {
    protected SAXParserFactory parserFactory;
    protected String parserClass;
    protected Map<String, String> namespaceMap;
    private SAXCatalogParser saxParser;
    private boolean abandonHope;
    private Catalog catalog;
    protected Debug debug;

    public void setParserFactory(SAXParserFactory parserFactory) {
        this.parserFactory = parserFactory;
    }

    public void setParserClass(String parserClass) {
        this.parserClass = parserClass;
    }

    public SAXParserFactory getParserFactory() {
        return this.parserFactory;
    }

    public String getParserClass() {
        return this.parserClass;
    }

    public SAXCatalogReader() {
        this.parserFactory = null;
        this.parserClass = null;
        this.namespaceMap = new HashMap();
        this.saxParser = null;
        this.abandonHope = false;
        this.debug = CatalogManager.getStaticManager().debug;
        this.parserFactory = null;
        this.parserClass = null;
    }

    public SAXCatalogReader(SAXParserFactory parserFactory) {
        this.parserFactory = null;
        this.parserClass = null;
        this.namespaceMap = new HashMap();
        this.saxParser = null;
        this.abandonHope = false;
        this.debug = CatalogManager.getStaticManager().debug;
        this.parserFactory = parserFactory;
    }

    public SAXCatalogReader(String parserClass) {
        this.parserFactory = null;
        this.parserClass = null;
        this.namespaceMap = new HashMap();
        this.saxParser = null;
        this.abandonHope = false;
        this.debug = CatalogManager.getStaticManager().debug;
        this.parserClass = parserClass;
    }

    public void setCatalogParser(String namespaceURI, String rootElement, String parserClass) {
        if (namespaceURI == null) {
            this.namespaceMap.put(rootElement, parserClass);
        } else {
            this.namespaceMap.put(VectorFormat.DEFAULT_PREFIX + namespaceURI + "}" + rootElement, parserClass);
        }
    }

    public String getCatalogParser(String namespaceURI, String rootElement) {
        if (namespaceURI == null) {
            return this.namespaceMap.get(rootElement);
        }
        return this.namespaceMap.get(VectorFormat.DEFAULT_PREFIX + namespaceURI + "}" + rootElement);
    }

    @Override // com.sun.org.apache.xml.internal.resolver.readers.CatalogReader
    public void readCatalog(Catalog catalog, String fileUrl) throws CatalogException, IOException {
        URL url;
        try {
            url = new URL(fileUrl);
        } catch (MalformedURLException e2) {
            url = new URL("file:///" + fileUrl);
        }
        this.debug = catalog.getCatalogManager().debug;
        try {
            URLConnection urlCon = url.openConnection();
            readCatalog(catalog, urlCon.getInputStream());
        } catch (FileNotFoundException e3) {
            catalog.getCatalogManager().debug.message(1, "Failed to load catalog, file not found", url.toString());
        }
    }

    @Override // com.sun.org.apache.xml.internal.resolver.readers.CatalogReader
    public void readCatalog(Catalog catalog, InputStream is) throws CatalogException, IOException {
        if (this.parserFactory == null && this.parserClass == null) {
            this.debug.message(1, "Cannot read SAX catalog without a parser");
            throw new CatalogException(6);
        }
        this.debug = catalog.getCatalogManager().debug;
        EntityResolver bResolver = catalog.getCatalogManager().getBootstrapResolver();
        this.catalog = catalog;
        try {
            if (this.parserFactory != null) {
                SAXParser parser = this.parserFactory.newSAXParser();
                SAXParserHandler spHandler = new SAXParserHandler();
                spHandler.setContentHandler(this);
                if (bResolver != null) {
                    spHandler.setEntityResolver(bResolver);
                }
                parser.parse(new InputSource(is), spHandler);
            } else {
                Parser parser2 = (Parser) ReflectUtil.forName(this.parserClass).newInstance();
                parser2.setDocumentHandler(this);
                if (bResolver != null) {
                    parser2.setEntityResolver(bResolver);
                }
                parser2.parse(new InputSource(is));
            }
        } catch (ClassNotFoundException e2) {
            throw new CatalogException(6);
        } catch (IllegalAccessException e3) {
            throw new CatalogException(6);
        } catch (InstantiationException e4) {
            throw new CatalogException(6);
        } catch (ParserConfigurationException e5) {
            throw new CatalogException(5);
        } catch (SAXException se) {
            Exception e6 = se.getException();
            UnknownHostException uhe = new UnknownHostException();
            FileNotFoundException fnfe = new FileNotFoundException();
            if (e6 != null) {
                if (e6.getClass() == uhe.getClass()) {
                    throw new CatalogException(7, e6.toString());
                }
                if (e6.getClass() == fnfe.getClass()) {
                    throw new CatalogException(7, e6.toString());
                }
            }
            throw new CatalogException(se);
        }
    }

    public void setDocumentLocator(Locator locator) {
        if (this.saxParser != null) {
            this.saxParser.setDocumentLocator(locator);
        }
    }

    public void startDocument() throws SAXException {
        this.saxParser = null;
        this.abandonHope = false;
    }

    public void endDocument() throws SAXException {
        if (this.saxParser != null) {
            this.saxParser.endDocument();
        }
    }

    @Override // org.xml.sax.DocumentHandler
    public void startElement(String name, AttributeList atts) throws SAXException {
        if (this.abandonHope) {
            return;
        }
        if (this.saxParser != null) {
            this.saxParser.startElement(name, atts);
            return;
        }
        String prefix = name.indexOf(58) > 0 ? name.substring(0, name.indexOf(58)) : "";
        String localName = name;
        if (localName.indexOf(58) > 0) {
            localName = localName.substring(localName.indexOf(58) + 1);
        }
        String namespaceURI = prefix.equals("") ? atts.getValue("xmlns") : atts.getValue("xmlns:" + prefix);
        String saxParserClass = getCatalogParser(namespaceURI, localName);
        if (saxParserClass == null) {
            this.abandonHope = true;
            if (namespaceURI == null) {
                this.debug.message(2, "No Catalog parser for " + name);
                return;
            } else {
                this.debug.message(2, "No Catalog parser for {" + namespaceURI + "}" + name);
                return;
            }
        }
        try {
            this.saxParser = (SAXCatalogParser) ReflectUtil.forName(saxParserClass).newInstance();
            this.saxParser.setCatalog(this.catalog);
            this.saxParser.startDocument();
            this.saxParser.startElement(name, atts);
        } catch (ClassCastException cce) {
            this.saxParser = null;
            this.abandonHope = true;
            this.debug.message(2, cce.toString());
        } catch (ClassNotFoundException cnfe) {
            this.saxParser = null;
            this.abandonHope = true;
            this.debug.message(2, cnfe.toString());
        } catch (IllegalAccessException iae) {
            this.saxParser = null;
            this.abandonHope = true;
            this.debug.message(2, iae.toString());
        } catch (InstantiationException ie) {
            this.saxParser = null;
            this.abandonHope = true;
            this.debug.message(2, ie.toString());
        }
    }

    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
        if (this.abandonHope) {
            return;
        }
        if (this.saxParser != null) {
            this.saxParser.startElement(namespaceURI, localName, qName, atts);
            return;
        }
        String saxParserClass = getCatalogParser(namespaceURI, localName);
        if (saxParserClass == null) {
            this.abandonHope = true;
            if (namespaceURI == null) {
                this.debug.message(2, "No Catalog parser for " + localName);
                return;
            } else {
                this.debug.message(2, "No Catalog parser for {" + namespaceURI + "}" + localName);
                return;
            }
        }
        try {
            this.saxParser = (SAXCatalogParser) ReflectUtil.forName(saxParserClass).newInstance();
            this.saxParser.setCatalog(this.catalog);
            this.saxParser.startDocument();
            this.saxParser.startElement(namespaceURI, localName, qName, atts);
        } catch (ClassCastException cce) {
            this.saxParser = null;
            this.abandonHope = true;
            this.debug.message(2, cce.toString());
        } catch (ClassNotFoundException cnfe) {
            this.saxParser = null;
            this.abandonHope = true;
            this.debug.message(2, cnfe.toString());
        } catch (IllegalAccessException iae) {
            this.saxParser = null;
            this.abandonHope = true;
            this.debug.message(2, iae.toString());
        } catch (InstantiationException ie) {
            this.saxParser = null;
            this.abandonHope = true;
            this.debug.message(2, ie.toString());
        }
    }

    @Override // org.xml.sax.DocumentHandler
    public void endElement(String name) throws SAXException {
        if (this.saxParser != null) {
            this.saxParser.endElement(name);
        }
    }

    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
        if (this.saxParser != null) {
            this.saxParser.endElement(namespaceURI, localName, qName);
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        if (this.saxParser != null) {
            this.saxParser.characters(ch, start, length);
        }
    }

    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
        if (this.saxParser != null) {
            this.saxParser.ignorableWhitespace(ch, start, length);
        }
    }

    public void processingInstruction(String target, String data) throws SAXException {
        if (this.saxParser != null) {
            this.saxParser.processingInstruction(target, data);
        }
    }

    public void startPrefixMapping(String prefix, String uri) throws SAXException {
        if (this.saxParser != null) {
            this.saxParser.startPrefixMapping(prefix, uri);
        }
    }

    public void endPrefixMapping(String prefix) throws SAXException {
        if (this.saxParser != null) {
            this.saxParser.endPrefixMapping(prefix);
        }
    }

    public void skippedEntity(String name) throws SAXException {
        if (this.saxParser != null) {
            this.saxParser.skippedEntity(name);
        }
    }
}
