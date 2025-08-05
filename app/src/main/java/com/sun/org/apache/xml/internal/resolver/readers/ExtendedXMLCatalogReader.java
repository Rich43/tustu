package com.sun.org.apache.xml.internal.resolver.readers;

import com.sun.org.apache.xalan.internal.templates.Constants;
import com.sun.org.apache.xml.internal.resolver.Catalog;
import com.sun.org.apache.xml.internal.resolver.CatalogEntry;
import com.sun.org.apache.xml.internal.resolver.CatalogException;
import com.sun.org.apache.xml.internal.resolver.Resolver;
import java.util.Vector;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/resolver/readers/ExtendedXMLCatalogReader.class */
public class ExtendedXMLCatalogReader extends OASISXMLCatalogReader {
    public static final String extendedNamespaceName = "http://nwalsh.com/xcatalog/1.0";

    @Override // com.sun.org.apache.xml.internal.resolver.readers.OASISXMLCatalogReader, com.sun.org.apache.xml.internal.resolver.readers.SAXCatalogReader, org.xml.sax.ContentHandler
    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
        boolean inExtension = inExtensionNamespace();
        super.startElement(namespaceURI, localName, qName, atts);
        int entryType = -1;
        Vector entryArgs = new Vector();
        if (namespaceURI != null && extendedNamespaceName.equals(namespaceURI) && !inExtension) {
            if (atts.getValue("xml:base") != null) {
                String baseURI = atts.getValue("xml:base");
                int entryType2 = Catalog.BASE;
                entryArgs.add(baseURI);
                this.baseURIStack.push(baseURI);
                this.debug.message(4, "xml:base", baseURI);
                try {
                    CatalogEntry ce = new CatalogEntry(entryType2, entryArgs);
                    this.catalog.addEntry(ce);
                } catch (CatalogException cex) {
                    if (cex.getExceptionType() == 3) {
                        this.debug.message(1, "Invalid catalog entry type", localName);
                    } else if (cex.getExceptionType() == 2) {
                        this.debug.message(1, "Invalid catalog entry (base)", localName);
                    }
                }
                entryType = -1;
                entryArgs = new Vector();
            } else {
                this.baseURIStack.push(this.baseURIStack.peek());
            }
            if (localName.equals("uriSuffix")) {
                if (checkAttributes(atts, "suffix", Constants.ELEMNAME_URL_STRING)) {
                    entryType = Resolver.URISUFFIX;
                    entryArgs.add(atts.getValue("suffix"));
                    entryArgs.add(atts.getValue(Constants.ELEMNAME_URL_STRING));
                    this.debug.message(4, "uriSuffix", atts.getValue("suffix"), atts.getValue(Constants.ELEMNAME_URL_STRING));
                }
            } else if (localName.equals("systemSuffix")) {
                if (checkAttributes(atts, "suffix", Constants.ELEMNAME_URL_STRING)) {
                    entryType = Resolver.SYSTEMSUFFIX;
                    entryArgs.add(atts.getValue("suffix"));
                    entryArgs.add(atts.getValue(Constants.ELEMNAME_URL_STRING));
                    this.debug.message(4, "systemSuffix", atts.getValue("suffix"), atts.getValue(Constants.ELEMNAME_URL_STRING));
                }
            } else {
                this.debug.message(1, "Invalid catalog entry type", localName);
            }
            if (entryType >= 0) {
                try {
                    CatalogEntry ce2 = new CatalogEntry(entryType, entryArgs);
                    this.catalog.addEntry(ce2);
                } catch (CatalogException cex2) {
                    if (cex2.getExceptionType() == 3) {
                        this.debug.message(1, "Invalid catalog entry type", localName);
                    } else if (cex2.getExceptionType() == 2) {
                        this.debug.message(1, "Invalid catalog entry", localName);
                    }
                }
            }
        }
    }

    @Override // com.sun.org.apache.xml.internal.resolver.readers.OASISXMLCatalogReader, com.sun.org.apache.xml.internal.resolver.readers.SAXCatalogReader, org.xml.sax.ContentHandler
    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
        super.endElement(namespaceURI, localName, qName);
        boolean inExtension = inExtensionNamespace();
        Vector entryArgs = new Vector();
        if (namespaceURI != null && extendedNamespaceName.equals(namespaceURI) && !inExtension) {
            String popURI = (String) this.baseURIStack.pop();
            String baseURI = (String) this.baseURIStack.peek();
            if (!baseURI.equals(popURI)) {
                Catalog catalog = this.catalog;
                int entryType = Catalog.BASE;
                entryArgs.add(baseURI);
                this.debug.message(4, "(reset) xml:base", baseURI);
                try {
                    CatalogEntry ce = new CatalogEntry(entryType, entryArgs);
                    this.catalog.addEntry(ce);
                } catch (CatalogException cex) {
                    if (cex.getExceptionType() == 3) {
                        this.debug.message(1, "Invalid catalog entry type", localName);
                    } else if (cex.getExceptionType() == 2) {
                        this.debug.message(1, "Invalid catalog entry (rbase)", localName);
                    }
                }
            }
        }
    }
}
