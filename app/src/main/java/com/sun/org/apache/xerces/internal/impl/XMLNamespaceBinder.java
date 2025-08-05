package com.sun.org.apache.xerces.internal.impl;

import com.sun.org.apache.xerces.internal.util.SymbolTable;
import com.sun.org.apache.xerces.internal.util.XMLSymbols;
import com.sun.org.apache.xerces.internal.xni.Augmentations;
import com.sun.org.apache.xerces.internal.xni.NamespaceContext;
import com.sun.org.apache.xerces.internal.xni.QName;
import com.sun.org.apache.xerces.internal.xni.XMLAttributes;
import com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler;
import com.sun.org.apache.xerces.internal.xni.XMLLocator;
import com.sun.org.apache.xerces.internal.xni.XMLResourceIdentifier;
import com.sun.org.apache.xerces.internal.xni.XMLString;
import com.sun.org.apache.xerces.internal.xni.XNIException;
import com.sun.org.apache.xerces.internal.xni.parser.XMLComponent;
import com.sun.org.apache.xerces.internal.xni.parser.XMLComponentManager;
import com.sun.org.apache.xerces.internal.xni.parser.XMLConfigurationException;
import com.sun.org.apache.xerces.internal.xni.parser.XMLDocumentFilter;
import com.sun.org.apache.xerces.internal.xni.parser.XMLDocumentSource;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/XMLNamespaceBinder.class */
public class XMLNamespaceBinder implements XMLComponent, XMLDocumentFilter {
    protected static final String NAMESPACES = "http://xml.org/sax/features/namespaces";
    protected static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
    protected static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
    private static final String[] RECOGNIZED_FEATURES = {"http://xml.org/sax/features/namespaces"};
    private static final Boolean[] FEATURE_DEFAULTS = {null};
    private static final String[] RECOGNIZED_PROPERTIES = {"http://apache.org/xml/properties/internal/symbol-table", "http://apache.org/xml/properties/internal/error-reporter"};
    private static final Object[] PROPERTY_DEFAULTS = {null, null};
    protected boolean fNamespaces;
    protected SymbolTable fSymbolTable;
    protected XMLErrorReporter fErrorReporter;
    protected XMLDocumentHandler fDocumentHandler;
    protected XMLDocumentSource fDocumentSource;
    protected boolean fOnlyPassPrefixMappingEvents;
    private NamespaceContext fNamespaceContext;
    private QName fAttributeQName = new QName();

    public void setOnlyPassPrefixMappingEvents(boolean onlyPassPrefixMappingEvents) {
        this.fOnlyPassPrefixMappingEvents = onlyPassPrefixMappingEvents;
    }

    public boolean getOnlyPassPrefixMappingEvents() {
        return this.fOnlyPassPrefixMappingEvents;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLComponent
    public void reset(XMLComponentManager componentManager) throws XNIException {
        this.fNamespaces = componentManager.getFeature("http://xml.org/sax/features/namespaces", true);
        this.fSymbolTable = (SymbolTable) componentManager.getProperty("http://apache.org/xml/properties/internal/symbol-table");
        this.fErrorReporter = (XMLErrorReporter) componentManager.getProperty("http://apache.org/xml/properties/internal/error-reporter");
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLComponent
    public String[] getRecognizedFeatures() {
        return (String[]) RECOGNIZED_FEATURES.clone();
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLComponent
    public void setFeature(String featureId, boolean state) throws XMLConfigurationException {
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLComponent
    public String[] getRecognizedProperties() {
        return (String[]) RECOGNIZED_PROPERTIES.clone();
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLComponent
    public void setProperty(String propertyId, Object value) throws XMLConfigurationException {
        if (propertyId.startsWith(Constants.XERCES_PROPERTY_PREFIX)) {
            int suffixLength = propertyId.length() - Constants.XERCES_PROPERTY_PREFIX.length();
            if (suffixLength == Constants.SYMBOL_TABLE_PROPERTY.length() && propertyId.endsWith(Constants.SYMBOL_TABLE_PROPERTY)) {
                this.fSymbolTable = (SymbolTable) value;
            } else if (suffixLength == Constants.ERROR_REPORTER_PROPERTY.length() && propertyId.endsWith(Constants.ERROR_REPORTER_PROPERTY)) {
                this.fErrorReporter = (XMLErrorReporter) value;
            }
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLComponent
    public Boolean getFeatureDefault(String featureId) {
        for (int i2 = 0; i2 < RECOGNIZED_FEATURES.length; i2++) {
            if (RECOGNIZED_FEATURES[i2].equals(featureId)) {
                return FEATURE_DEFAULTS[i2];
            }
        }
        return null;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLComponent
    public Object getPropertyDefault(String propertyId) {
        for (int i2 = 0; i2 < RECOGNIZED_PROPERTIES.length; i2++) {
            if (RECOGNIZED_PROPERTIES[i2].equals(propertyId)) {
                return PROPERTY_DEFAULTS[i2];
            }
        }
        return null;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLDocumentSource
    public void setDocumentHandler(XMLDocumentHandler documentHandler) {
        this.fDocumentHandler = documentHandler;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLDocumentSource
    public XMLDocumentHandler getDocumentHandler() {
        return this.fDocumentHandler;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void setDocumentSource(XMLDocumentSource source) {
        this.fDocumentSource = source;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public XMLDocumentSource getDocumentSource() {
        return this.fDocumentSource;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void startGeneralEntity(String name, XMLResourceIdentifier identifier, String encoding, Augmentations augs) throws XNIException {
        if (this.fDocumentHandler != null && !this.fOnlyPassPrefixMappingEvents) {
            this.fDocumentHandler.startGeneralEntity(name, identifier, encoding, augs);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void textDecl(String version, String encoding, Augmentations augs) throws XNIException {
        if (this.fDocumentHandler != null && !this.fOnlyPassPrefixMappingEvents) {
            this.fDocumentHandler.textDecl(version, encoding, augs);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void startDocument(XMLLocator locator, String encoding, NamespaceContext namespaceContext, Augmentations augs) throws XNIException {
        this.fNamespaceContext = namespaceContext;
        if (this.fDocumentHandler != null && !this.fOnlyPassPrefixMappingEvents) {
            this.fDocumentHandler.startDocument(locator, encoding, namespaceContext, augs);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void xmlDecl(String version, String encoding, String standalone, Augmentations augs) throws XNIException {
        if (this.fDocumentHandler != null && !this.fOnlyPassPrefixMappingEvents) {
            this.fDocumentHandler.xmlDecl(version, encoding, standalone, augs);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void doctypeDecl(String rootElement, String publicId, String systemId, Augmentations augs) throws XNIException {
        if (this.fDocumentHandler != null && !this.fOnlyPassPrefixMappingEvents) {
            this.fDocumentHandler.doctypeDecl(rootElement, publicId, systemId, augs);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void comment(XMLString text, Augmentations augs) throws XNIException {
        if (this.fDocumentHandler != null && !this.fOnlyPassPrefixMappingEvents) {
            this.fDocumentHandler.comment(text, augs);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void processingInstruction(String target, XMLString data, Augmentations augs) throws XNIException {
        if (this.fDocumentHandler != null && !this.fOnlyPassPrefixMappingEvents) {
            this.fDocumentHandler.processingInstruction(target, data, augs);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void startElement(QName element, XMLAttributes attributes, Augmentations augs) throws XNIException {
        if (this.fNamespaces) {
            handleStartElement(element, attributes, augs, false);
        } else if (this.fDocumentHandler != null) {
            this.fDocumentHandler.startElement(element, attributes, augs);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void emptyElement(QName element, XMLAttributes attributes, Augmentations augs) throws XNIException {
        if (this.fNamespaces) {
            handleStartElement(element, attributes, augs, true);
            handleEndElement(element, augs, true);
        } else if (this.fDocumentHandler != null) {
            this.fDocumentHandler.emptyElement(element, attributes, augs);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void characters(XMLString text, Augmentations augs) throws XNIException {
        if (this.fDocumentHandler != null && !this.fOnlyPassPrefixMappingEvents) {
            this.fDocumentHandler.characters(text, augs);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void ignorableWhitespace(XMLString text, Augmentations augs) throws XNIException {
        if (this.fDocumentHandler != null && !this.fOnlyPassPrefixMappingEvents) {
            this.fDocumentHandler.ignorableWhitespace(text, augs);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void endElement(QName element, Augmentations augs) throws XNIException {
        if (this.fNamespaces) {
            handleEndElement(element, augs, false);
        } else if (this.fDocumentHandler != null) {
            this.fDocumentHandler.endElement(element, augs);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void startCDATA(Augmentations augs) throws XNIException {
        if (this.fDocumentHandler != null && !this.fOnlyPassPrefixMappingEvents) {
            this.fDocumentHandler.startCDATA(augs);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void endCDATA(Augmentations augs) throws XNIException {
        if (this.fDocumentHandler != null && !this.fOnlyPassPrefixMappingEvents) {
            this.fDocumentHandler.endCDATA(augs);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void endDocument(Augmentations augs) throws XNIException {
        if (this.fDocumentHandler != null && !this.fOnlyPassPrefixMappingEvents) {
            this.fDocumentHandler.endDocument(augs);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void endGeneralEntity(String name, Augmentations augs) throws XNIException {
        if (this.fDocumentHandler != null && !this.fOnlyPassPrefixMappingEvents) {
            this.fDocumentHandler.endGeneralEntity(name, augs);
        }
    }

    protected void handleStartElement(QName element, XMLAttributes attributes, Augmentations augs, boolean isEmpty) throws XNIException {
        this.fNamespaceContext.pushContext();
        if (element.prefix == XMLSymbols.PREFIX_XMLNS) {
            this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "ElementXMLNSPrefix", new Object[]{element.rawname}, (short) 2);
        }
        int length = attributes.getLength();
        for (int i2 = 0; i2 < length; i2++) {
            String localpart = attributes.getLocalName(i2);
            String prefix = attributes.getPrefix(i2);
            if (prefix == XMLSymbols.PREFIX_XMLNS || (prefix == XMLSymbols.EMPTY_STRING && localpart == XMLSymbols.PREFIX_XMLNS)) {
                String uri = this.fSymbolTable.addSymbol(attributes.getValue(i2));
                if (prefix == XMLSymbols.PREFIX_XMLNS && localpart == XMLSymbols.PREFIX_XMLNS) {
                    this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "CantBindXMLNS", new Object[]{attributes.getQName(i2)}, (short) 2);
                }
                if (uri == NamespaceContext.XMLNS_URI) {
                    this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "CantBindXMLNS", new Object[]{attributes.getQName(i2)}, (short) 2);
                }
                if (localpart == XMLSymbols.PREFIX_XML) {
                    if (uri != NamespaceContext.XML_URI) {
                        this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "CantBindXML", new Object[]{attributes.getQName(i2)}, (short) 2);
                    }
                } else if (uri == NamespaceContext.XML_URI) {
                    this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "CantBindXML", new Object[]{attributes.getQName(i2)}, (short) 2);
                }
                String prefix2 = localpart != XMLSymbols.PREFIX_XMLNS ? localpart : XMLSymbols.EMPTY_STRING;
                if (prefixBoundToNullURI(uri, localpart)) {
                    this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "EmptyPrefixedAttName", new Object[]{attributes.getQName(i2)}, (short) 2);
                } else {
                    this.fNamespaceContext.declarePrefix(prefix2, uri.length() != 0 ? uri : null);
                }
            }
        }
        element.uri = this.fNamespaceContext.getURI(element.prefix != null ? element.prefix : XMLSymbols.EMPTY_STRING);
        if (element.prefix == null && element.uri != null) {
            element.prefix = XMLSymbols.EMPTY_STRING;
        }
        if (element.prefix != null && element.uri == null) {
            this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "ElementPrefixUnbound", new Object[]{element.prefix, element.rawname}, (short) 2);
        }
        for (int i3 = 0; i3 < length; i3++) {
            attributes.getName(i3, this.fAttributeQName);
            String aprefix = this.fAttributeQName.prefix != null ? this.fAttributeQName.prefix : XMLSymbols.EMPTY_STRING;
            String arawname = this.fAttributeQName.rawname;
            if (arawname == XMLSymbols.PREFIX_XMLNS) {
                this.fAttributeQName.uri = this.fNamespaceContext.getURI(XMLSymbols.PREFIX_XMLNS);
                attributes.setName(i3, this.fAttributeQName);
            } else if (aprefix != XMLSymbols.EMPTY_STRING) {
                this.fAttributeQName.uri = this.fNamespaceContext.getURI(aprefix);
                if (this.fAttributeQName.uri == null) {
                    this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "AttributePrefixUnbound", new Object[]{element.rawname, arawname, aprefix}, (short) 2);
                }
                attributes.setName(i3, this.fAttributeQName);
            }
        }
        int attrCount = attributes.getLength();
        for (int i4 = 0; i4 < attrCount - 1; i4++) {
            String auri = attributes.getURI(i4);
            if (auri != null && auri != NamespaceContext.XMLNS_URI) {
                String alocalpart = attributes.getLocalName(i4);
                for (int j2 = i4 + 1; j2 < attrCount; j2++) {
                    String blocalpart = attributes.getLocalName(j2);
                    String buri = attributes.getURI(j2);
                    if (alocalpart == blocalpart && auri == buri) {
                        this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "AttributeNSNotUnique", new Object[]{element.rawname, alocalpart, auri}, (short) 2);
                    }
                }
            }
        }
        if (this.fDocumentHandler != null && !this.fOnlyPassPrefixMappingEvents) {
            if (isEmpty) {
                this.fDocumentHandler.emptyElement(element, attributes, augs);
            } else {
                this.fDocumentHandler.startElement(element, attributes, augs);
            }
        }
    }

    protected void handleEndElement(QName element, Augmentations augs, boolean isEmpty) throws XNIException {
        String eprefix = element.prefix != null ? element.prefix : XMLSymbols.EMPTY_STRING;
        element.uri = this.fNamespaceContext.getURI(eprefix);
        if (element.uri != null) {
            element.prefix = eprefix;
        }
        if (this.fDocumentHandler != null && !this.fOnlyPassPrefixMappingEvents && !isEmpty) {
            this.fDocumentHandler.endElement(element, augs);
        }
        this.fNamespaceContext.popContext();
    }

    protected boolean prefixBoundToNullURI(String uri, String localpart) {
        return uri == XMLSymbols.EMPTY_STRING && localpart != XMLSymbols.PREFIX_XMLNS;
    }
}
