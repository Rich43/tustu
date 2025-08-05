package com.sun.org.apache.xml.internal.resolver.readers;

import com.sun.org.apache.xml.internal.resolver.Catalog;
import com.sun.org.apache.xml.internal.resolver.CatalogEntry;
import com.sun.org.apache.xml.internal.resolver.CatalogException;
import com.sun.org.apache.xml.internal.resolver.helpers.PublicId;
import java.util.Vector;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/resolver/readers/XCatalogReader.class */
public class XCatalogReader extends SAXCatalogReader implements SAXCatalogParser {
    protected Catalog catalog;

    @Override // com.sun.org.apache.xml.internal.resolver.readers.SAXCatalogParser
    public void setCatalog(Catalog catalog) {
        this.catalog = catalog;
    }

    public Catalog getCatalog() {
        return this.catalog;
    }

    public XCatalogReader(SAXParserFactory parserFactory) {
        super(parserFactory);
        this.catalog = null;
    }

    @Override // com.sun.org.apache.xml.internal.resolver.readers.SAXCatalogReader, org.xml.sax.ContentHandler
    public void setDocumentLocator(Locator locator) {
    }

    @Override // com.sun.org.apache.xml.internal.resolver.readers.SAXCatalogReader, org.xml.sax.ContentHandler
    public void startDocument() throws SAXException {
    }

    @Override // com.sun.org.apache.xml.internal.resolver.readers.SAXCatalogReader, org.xml.sax.ContentHandler
    public void endDocument() throws SAXException {
    }

    @Override // com.sun.org.apache.xml.internal.resolver.readers.SAXCatalogReader, org.xml.sax.ContentHandler
    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
        int entryType = -1;
        Vector entryArgs = new Vector();
        if (localName.equals("Base")) {
            Catalog catalog = this.catalog;
            entryType = Catalog.BASE;
            entryArgs.add(atts.getValue("HRef"));
            this.catalog.getCatalogManager().debug.message(4, "Base", atts.getValue("HRef"));
        } else if (localName.equals("Delegate")) {
            Catalog catalog2 = this.catalog;
            entryType = Catalog.DELEGATE_PUBLIC;
            entryArgs.add(atts.getValue("PublicId"));
            entryArgs.add(atts.getValue("HRef"));
            this.catalog.getCatalogManager().debug.message(4, "Delegate", PublicId.normalize(atts.getValue("PublicId")), atts.getValue("HRef"));
        } else if (localName.equals("Extend")) {
            Catalog catalog3 = this.catalog;
            entryType = Catalog.CATALOG;
            entryArgs.add(atts.getValue("HRef"));
            this.catalog.getCatalogManager().debug.message(4, "Extend", atts.getValue("HRef"));
        } else if (localName.equals("Map")) {
            Catalog catalog4 = this.catalog;
            entryType = Catalog.PUBLIC;
            entryArgs.add(atts.getValue("PublicId"));
            entryArgs.add(atts.getValue("HRef"));
            this.catalog.getCatalogManager().debug.message(4, "Map", PublicId.normalize(atts.getValue("PublicId")), atts.getValue("HRef"));
        } else if (localName.equals("Remap")) {
            Catalog catalog5 = this.catalog;
            entryType = Catalog.SYSTEM;
            entryArgs.add(atts.getValue("SystemId"));
            entryArgs.add(atts.getValue("HRef"));
            this.catalog.getCatalogManager().debug.message(4, "Remap", atts.getValue("SystemId"), atts.getValue("HRef"));
        } else if (!localName.equals("XMLCatalog")) {
            this.catalog.getCatalogManager().debug.message(1, "Invalid catalog entry type", localName);
        }
        if (entryType >= 0) {
            try {
                CatalogEntry ce = new CatalogEntry(entryType, entryArgs);
                this.catalog.addEntry(ce);
            } catch (CatalogException cex) {
                if (cex.getExceptionType() == 3) {
                    this.catalog.getCatalogManager().debug.message(1, "Invalid catalog entry type", localName);
                } else if (cex.getExceptionType() == 2) {
                    this.catalog.getCatalogManager().debug.message(1, "Invalid catalog entry", localName);
                }
            }
        }
    }

    @Override // com.sun.org.apache.xml.internal.resolver.readers.SAXCatalogReader, org.xml.sax.ContentHandler
    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
    }

    @Override // com.sun.org.apache.xml.internal.resolver.readers.SAXCatalogReader, org.xml.sax.ContentHandler
    public void characters(char[] ch, int start, int length) throws SAXException {
    }

    @Override // com.sun.org.apache.xml.internal.resolver.readers.SAXCatalogReader, org.xml.sax.ContentHandler
    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
    }

    @Override // com.sun.org.apache.xml.internal.resolver.readers.SAXCatalogReader, org.xml.sax.ContentHandler
    public void processingInstruction(String target, String data) throws SAXException {
    }
}
