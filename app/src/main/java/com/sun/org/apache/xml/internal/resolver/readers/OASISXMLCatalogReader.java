package com.sun.org.apache.xml.internal.resolver.readers;

import com.sun.org.apache.xalan.internal.templates.Constants;
import com.sun.org.apache.xml.internal.resolver.Catalog;
import com.sun.org.apache.xml.internal.resolver.CatalogEntry;
import com.sun.org.apache.xml.internal.resolver.CatalogException;
import com.sun.org.apache.xml.internal.resolver.helpers.PublicId;
import java.util.Enumeration;
import java.util.Stack;
import java.util.Vector;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/resolver/readers/OASISXMLCatalogReader.class */
public class OASISXMLCatalogReader extends SAXCatalogReader implements SAXCatalogParser {
    public static final String namespaceName = "urn:oasis:names:tc:entity:xmlns:xml:catalog";
    public static final String tr9401NamespaceName = "urn:oasis:names:tc:entity:xmlns:tr9401:catalog";
    protected Catalog catalog = null;
    protected Stack baseURIStack = new Stack();
    protected Stack overrideStack = new Stack();
    protected Stack namespaceStack = new Stack();

    @Override // com.sun.org.apache.xml.internal.resolver.readers.SAXCatalogParser
    public void setCatalog(Catalog catalog) {
        this.catalog = catalog;
        this.debug = catalog.getCatalogManager().debug;
    }

    public Catalog getCatalog() {
        return this.catalog;
    }

    protected boolean inExtensionNamespace() {
        boolean inExtension = false;
        Enumeration elements = this.namespaceStack.elements();
        while (!inExtension && elements.hasMoreElements()) {
            String ns = (String) elements.nextElement2();
            if (ns == null) {
                inExtension = true;
            } else {
                inExtension = (ns.equals(tr9401NamespaceName) || ns.equals(namespaceName)) ? false : true;
            }
        }
        return inExtension;
    }

    @Override // com.sun.org.apache.xml.internal.resolver.readers.SAXCatalogReader, org.xml.sax.ContentHandler
    public void setDocumentLocator(Locator locator) {
    }

    @Override // com.sun.org.apache.xml.internal.resolver.readers.SAXCatalogReader, org.xml.sax.ContentHandler
    public void startDocument() throws SAXException {
        this.baseURIStack.push(this.catalog.getCurrentBase());
        this.overrideStack.push(this.catalog.getDefaultOverride());
    }

    @Override // com.sun.org.apache.xml.internal.resolver.readers.SAXCatalogReader, org.xml.sax.ContentHandler
    public void endDocument() throws SAXException {
    }

    @Override // com.sun.org.apache.xml.internal.resolver.readers.SAXCatalogReader, org.xml.sax.ContentHandler
    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
        String override;
        int entryType = -1;
        Vector entryArgs = new Vector();
        this.namespaceStack.push(namespaceURI);
        boolean inExtension = inExtensionNamespace();
        if (namespaceURI != null && namespaceName.equals(namespaceURI) && !inExtension) {
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
            if ((localName.equals("catalog") || localName.equals("group")) && atts.getValue("prefer") != null) {
                String override2 = atts.getValue("prefer");
                if (override2.equals("public")) {
                    override = "yes";
                } else if (override2.equals("system")) {
                    override = "no";
                } else {
                    this.debug.message(1, "Invalid prefer: must be 'system' or 'public'", localName);
                    override = this.catalog.getDefaultOverride();
                }
                int entryType3 = Catalog.OVERRIDE;
                entryArgs.add(override);
                this.overrideStack.push(override);
                this.debug.message(4, "override", override);
                try {
                    CatalogEntry ce2 = new CatalogEntry(entryType3, entryArgs);
                    this.catalog.addEntry(ce2);
                } catch (CatalogException cex2) {
                    if (cex2.getExceptionType() == 3) {
                        this.debug.message(1, "Invalid catalog entry type", localName);
                    } else if (cex2.getExceptionType() == 2) {
                        this.debug.message(1, "Invalid catalog entry (override)", localName);
                    }
                }
                entryType = -1;
                entryArgs = new Vector();
            } else {
                this.overrideStack.push(this.overrideStack.peek());
            }
            if (localName.equals("delegatePublic")) {
                if (checkAttributes(atts, "publicIdStartString", "catalog")) {
                    entryType = Catalog.DELEGATE_PUBLIC;
                    entryArgs.add(atts.getValue("publicIdStartString"));
                    entryArgs.add(atts.getValue("catalog"));
                    this.debug.message(4, "delegatePublic", PublicId.normalize(atts.getValue("publicIdStartString")), atts.getValue("catalog"));
                }
            } else if (localName.equals("delegateSystem")) {
                if (checkAttributes(atts, "systemIdStartString", "catalog")) {
                    entryType = Catalog.DELEGATE_SYSTEM;
                    entryArgs.add(atts.getValue("systemIdStartString"));
                    entryArgs.add(atts.getValue("catalog"));
                    this.debug.message(4, "delegateSystem", atts.getValue("systemIdStartString"), atts.getValue("catalog"));
                }
            } else if (localName.equals("delegateURI")) {
                if (checkAttributes(atts, "uriStartString", "catalog")) {
                    entryType = Catalog.DELEGATE_URI;
                    entryArgs.add(atts.getValue("uriStartString"));
                    entryArgs.add(atts.getValue("catalog"));
                    this.debug.message(4, "delegateURI", atts.getValue("uriStartString"), atts.getValue("catalog"));
                }
            } else if (localName.equals("rewriteSystem")) {
                if (checkAttributes(atts, "systemIdStartString", "rewritePrefix")) {
                    entryType = Catalog.REWRITE_SYSTEM;
                    entryArgs.add(atts.getValue("systemIdStartString"));
                    entryArgs.add(atts.getValue("rewritePrefix"));
                    this.debug.message(4, "rewriteSystem", atts.getValue("systemIdStartString"), atts.getValue("rewritePrefix"));
                }
            } else if (localName.equals("systemSuffix")) {
                if (checkAttributes(atts, "systemIdSuffix", Constants.ELEMNAME_URL_STRING)) {
                    entryType = Catalog.SYSTEM_SUFFIX;
                    entryArgs.add(atts.getValue("systemIdSuffix"));
                    entryArgs.add(atts.getValue(Constants.ELEMNAME_URL_STRING));
                    this.debug.message(4, "systemSuffix", atts.getValue("systemIdSuffix"), atts.getValue(Constants.ELEMNAME_URL_STRING));
                }
            } else if (localName.equals("rewriteURI")) {
                if (checkAttributes(atts, "uriStartString", "rewritePrefix")) {
                    entryType = Catalog.REWRITE_URI;
                    entryArgs.add(atts.getValue("uriStartString"));
                    entryArgs.add(atts.getValue("rewritePrefix"));
                    this.debug.message(4, "rewriteURI", atts.getValue("uriStartString"), atts.getValue("rewritePrefix"));
                }
            } else if (localName.equals("uriSuffix")) {
                if (checkAttributes(atts, "uriSuffix", Constants.ELEMNAME_URL_STRING)) {
                    entryType = Catalog.URI_SUFFIX;
                    entryArgs.add(atts.getValue("uriSuffix"));
                    entryArgs.add(atts.getValue(Constants.ELEMNAME_URL_STRING));
                    this.debug.message(4, "uriSuffix", atts.getValue("uriSuffix"), atts.getValue(Constants.ELEMNAME_URL_STRING));
                }
            } else if (localName.equals("nextCatalog")) {
                if (checkAttributes(atts, "catalog")) {
                    entryType = Catalog.CATALOG;
                    entryArgs.add(atts.getValue("catalog"));
                    this.debug.message(4, "nextCatalog", atts.getValue("catalog"));
                }
            } else if (localName.equals("public")) {
                if (checkAttributes(atts, "publicId", Constants.ELEMNAME_URL_STRING)) {
                    entryType = Catalog.PUBLIC;
                    entryArgs.add(atts.getValue("publicId"));
                    entryArgs.add(atts.getValue(Constants.ELEMNAME_URL_STRING));
                    this.debug.message(4, "public", PublicId.normalize(atts.getValue("publicId")), atts.getValue(Constants.ELEMNAME_URL_STRING));
                }
            } else if (localName.equals("system")) {
                if (checkAttributes(atts, "systemId", Constants.ELEMNAME_URL_STRING)) {
                    entryType = Catalog.SYSTEM;
                    entryArgs.add(atts.getValue("systemId"));
                    entryArgs.add(atts.getValue(Constants.ELEMNAME_URL_STRING));
                    this.debug.message(4, "system", atts.getValue("systemId"), atts.getValue(Constants.ELEMNAME_URL_STRING));
                }
            } else if (localName.equals(Constants.ELEMNAME_URL_STRING)) {
                if (checkAttributes(atts, "name", Constants.ELEMNAME_URL_STRING)) {
                    entryType = Catalog.URI;
                    entryArgs.add(atts.getValue("name"));
                    entryArgs.add(atts.getValue(Constants.ELEMNAME_URL_STRING));
                    this.debug.message(4, Constants.ELEMNAME_URL_STRING, atts.getValue("name"), atts.getValue(Constants.ELEMNAME_URL_STRING));
                }
            } else if (!localName.equals("catalog") && !localName.equals("group")) {
                this.debug.message(1, "Invalid catalog entry type", localName);
            }
            if (entryType >= 0) {
                try {
                    CatalogEntry ce3 = new CatalogEntry(entryType, entryArgs);
                    this.catalog.addEntry(ce3);
                } catch (CatalogException cex3) {
                    if (cex3.getExceptionType() == 3) {
                        this.debug.message(1, "Invalid catalog entry type", localName);
                    } else if (cex3.getExceptionType() == 2) {
                        this.debug.message(1, "Invalid catalog entry", localName);
                    }
                }
            }
        }
        if (namespaceURI != null && tr9401NamespaceName.equals(namespaceURI) && !inExtension) {
            if (atts.getValue("xml:base") != null) {
                String baseURI2 = atts.getValue("xml:base");
                int entryType4 = Catalog.BASE;
                entryArgs.add(baseURI2);
                this.baseURIStack.push(baseURI2);
                this.debug.message(4, "xml:base", baseURI2);
                try {
                    CatalogEntry ce4 = new CatalogEntry(entryType4, entryArgs);
                    this.catalog.addEntry(ce4);
                } catch (CatalogException cex4) {
                    if (cex4.getExceptionType() == 3) {
                        this.debug.message(1, "Invalid catalog entry type", localName);
                    } else if (cex4.getExceptionType() == 2) {
                        this.debug.message(1, "Invalid catalog entry (base)", localName);
                    }
                }
                entryType = -1;
                entryArgs = new Vector();
            } else {
                this.baseURIStack.push(this.baseURIStack.peek());
            }
            if (localName.equals("doctype")) {
                Catalog catalog = this.catalog;
                entryType = Catalog.DOCTYPE;
                entryArgs.add(atts.getValue("name"));
                entryArgs.add(atts.getValue(Constants.ELEMNAME_URL_STRING));
            } else if (localName.equals(com.sun.org.apache.xalan.internal.xsltc.compiler.Constants.DOCUMENT_PNAME)) {
                Catalog catalog2 = this.catalog;
                entryType = Catalog.DOCUMENT;
                entryArgs.add(atts.getValue(Constants.ELEMNAME_URL_STRING));
            } else if (localName.equals("dtddecl")) {
                Catalog catalog3 = this.catalog;
                entryType = Catalog.DTDDECL;
                entryArgs.add(atts.getValue("publicId"));
                entryArgs.add(atts.getValue(Constants.ELEMNAME_URL_STRING));
            } else if (localName.equals("entity")) {
                entryType = Catalog.ENTITY;
                entryArgs.add(atts.getValue("name"));
                entryArgs.add(atts.getValue(Constants.ELEMNAME_URL_STRING));
            } else if (localName.equals("linktype")) {
                entryType = Catalog.LINKTYPE;
                entryArgs.add(atts.getValue("name"));
                entryArgs.add(atts.getValue(Constants.ELEMNAME_URL_STRING));
            } else if (localName.equals("notation")) {
                entryType = Catalog.NOTATION;
                entryArgs.add(atts.getValue("name"));
                entryArgs.add(atts.getValue(Constants.ELEMNAME_URL_STRING));
            } else if (localName.equals("sgmldecl")) {
                entryType = Catalog.SGMLDECL;
                entryArgs.add(atts.getValue(Constants.ELEMNAME_URL_STRING));
            } else {
                this.debug.message(1, "Invalid catalog entry type", localName);
            }
            if (entryType >= 0) {
                try {
                    CatalogEntry ce5 = new CatalogEntry(entryType, entryArgs);
                    this.catalog.addEntry(ce5);
                } catch (CatalogException cex5) {
                    if (cex5.getExceptionType() == 3) {
                        this.debug.message(1, "Invalid catalog entry type", localName);
                    } else if (cex5.getExceptionType() == 2) {
                        this.debug.message(1, "Invalid catalog entry", localName);
                    }
                }
            }
        }
    }

    public boolean checkAttributes(Attributes atts, String attName) {
        if (atts.getValue(attName) == null) {
            this.debug.message(1, "Error: required attribute " + attName + " missing.");
            return false;
        }
        return true;
    }

    public boolean checkAttributes(Attributes atts, String attName1, String attName2) {
        return checkAttributes(atts, attName1) && checkAttributes(atts, attName2);
    }

    @Override // com.sun.org.apache.xml.internal.resolver.readers.SAXCatalogReader, org.xml.sax.ContentHandler
    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
        Vector entryArgs = new Vector();
        boolean inExtension = inExtensionNamespace();
        if (namespaceURI != null && !inExtension && (namespaceName.equals(namespaceURI) || tr9401NamespaceName.equals(namespaceURI))) {
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
        if (namespaceURI != null && namespaceName.equals(namespaceURI) && !inExtension && (localName.equals("catalog") || localName.equals("group"))) {
            String popOverride = (String) this.overrideStack.pop();
            String override = (String) this.overrideStack.peek();
            if (!override.equals(popOverride)) {
                Catalog catalog2 = this.catalog;
                int entryType2 = Catalog.OVERRIDE;
                entryArgs.add(override);
                this.overrideStack.push(override);
                this.debug.message(4, "(reset) override", override);
                try {
                    CatalogEntry ce2 = new CatalogEntry(entryType2, entryArgs);
                    this.catalog.addEntry(ce2);
                } catch (CatalogException cex2) {
                    if (cex2.getExceptionType() == 3) {
                        this.debug.message(1, "Invalid catalog entry type", localName);
                    } else if (cex2.getExceptionType() == 2) {
                        this.debug.message(1, "Invalid catalog entry (roverride)", localName);
                    }
                }
            }
        }
        this.namespaceStack.pop();
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

    @Override // com.sun.org.apache.xml.internal.resolver.readers.SAXCatalogReader, org.xml.sax.ContentHandler
    public void skippedEntity(String name) throws SAXException {
    }

    @Override // com.sun.org.apache.xml.internal.resolver.readers.SAXCatalogReader, org.xml.sax.ContentHandler
    public void startPrefixMapping(String prefix, String uri) throws SAXException {
    }

    @Override // com.sun.org.apache.xml.internal.resolver.readers.SAXCatalogReader, org.xml.sax.ContentHandler
    public void endPrefixMapping(String prefix) throws SAXException {
    }
}
