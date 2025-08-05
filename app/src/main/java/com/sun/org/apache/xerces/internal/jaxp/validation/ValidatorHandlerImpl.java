package com.sun.org.apache.xerces.internal.jaxp.validation;

import com.sun.org.apache.xerces.internal.impl.Constants;
import com.sun.org.apache.xerces.internal.impl.XMLEntityManager;
import com.sun.org.apache.xerces.internal.impl.XMLErrorReporter;
import com.sun.org.apache.xerces.internal.impl.dv.XSSimpleType;
import com.sun.org.apache.xerces.internal.impl.validation.EntityState;
import com.sun.org.apache.xerces.internal.impl.validation.ValidationManager;
import com.sun.org.apache.xerces.internal.impl.xs.XMLSchemaValidator;
import com.sun.org.apache.xerces.internal.parsers.SAXParser;
import com.sun.org.apache.xerces.internal.util.AttributesProxy;
import com.sun.org.apache.xerces.internal.util.SAXLocatorWrapper;
import com.sun.org.apache.xerces.internal.util.SAXMessageFormatter;
import com.sun.org.apache.xerces.internal.util.Status;
import com.sun.org.apache.xerces.internal.util.SymbolTable;
import com.sun.org.apache.xerces.internal.util.URI;
import com.sun.org.apache.xerces.internal.util.XMLAttributesImpl;
import com.sun.org.apache.xerces.internal.util.XMLSymbols;
import com.sun.org.apache.xerces.internal.utils.XMLSecurityManager;
import com.sun.org.apache.xerces.internal.utils.XMLSecurityPropertyManager;
import com.sun.org.apache.xerces.internal.xni.Augmentations;
import com.sun.org.apache.xerces.internal.xni.NamespaceContext;
import com.sun.org.apache.xerces.internal.xni.QName;
import com.sun.org.apache.xerces.internal.xni.XMLAttributes;
import com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler;
import com.sun.org.apache.xerces.internal.xni.XMLLocator;
import com.sun.org.apache.xerces.internal.xni.XMLResourceIdentifier;
import com.sun.org.apache.xerces.internal.xni.XMLString;
import com.sun.org.apache.xerces.internal.xni.XNIException;
import com.sun.org.apache.xerces.internal.xni.parser.XMLConfigurationException;
import com.sun.org.apache.xerces.internal.xni.parser.XMLDocumentSource;
import com.sun.org.apache.xerces.internal.xni.parser.XMLParseException;
import com.sun.org.apache.xerces.internal.xs.AttributePSVI;
import com.sun.org.apache.xerces.internal.xs.ElementPSVI;
import com.sun.org.apache.xerces.internal.xs.ItemPSVI;
import com.sun.org.apache.xerces.internal.xs.PSVIProvider;
import com.sun.org.apache.xerces.internal.xs.XSTypeDefinition;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.sax.SAXSource;
import javax.xml.validation.TypeInfoProvider;
import javax.xml.validation.ValidatorHandler;
import jdk.xml.internal.JdkXmlUtils;
import org.w3c.dom.TypeInfo;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.Attributes2;
import org.xml.sax.ext.EntityResolver2;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/jaxp/validation/ValidatorHandlerImpl.class */
final class ValidatorHandlerImpl extends ValidatorHandler implements DTDHandler, EntityState, PSVIProvider, ValidatorHelper, XMLDocumentHandler {
    private static final String NAMESPACE_PREFIXES = "http://xml.org/sax/features/namespace-prefixes";
    protected static final String STRING_INTERNING = "http://xml.org/sax/features/string-interning";
    private static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
    private static final String NAMESPACE_CONTEXT = "http://apache.org/xml/properties/internal/namespace-context";
    private static final String SCHEMA_VALIDATOR = "http://apache.org/xml/properties/internal/validator/schema";
    private static final String SECURITY_MANAGER = "http://apache.org/xml/properties/security-manager";
    private static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
    private static final String VALIDATION_MANAGER = "http://apache.org/xml/properties/internal/validation-manager";
    private static final String XML_SECURITY_PROPERTY_MANAGER = "http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager";
    private XMLErrorReporter fErrorReporter;
    private NamespaceContext fNamespaceContext;
    private XMLSchemaValidator fSchemaValidator;
    private SymbolTable fSymbolTable;
    private ValidationManager fValidationManager;
    private XMLSchemaValidatorComponentManager fComponentManager;
    private final SAXLocatorWrapper fSAXLocatorWrapper;
    private boolean fNeedPushNSContext;
    private HashMap fUnparsedEntities;
    private boolean fStringsInternalized;
    private final QName fElementQName;
    private final QName fAttributeQName;
    private final XMLAttributesImpl fAttributes;
    private final AttributesProxy fAttrAdapter;
    private final XMLString fTempString;
    private ContentHandler fContentHandler;
    private final XMLSchemaTypeInfoProvider fTypeInfoProvider;
    private final ResolutionForwarder fResolutionForwarder;

    public ValidatorHandlerImpl(XSGrammarPoolContainer grammarContainer) throws XMLConfigurationException {
        this(new XMLSchemaValidatorComponentManager(grammarContainer));
        this.fComponentManager.addRecognizedFeatures(new String[]{"http://xml.org/sax/features/namespace-prefixes"});
        this.fComponentManager.setFeature("http://xml.org/sax/features/namespace-prefixes", false);
        setErrorHandler(null);
        setResourceResolver(null);
    }

    public ValidatorHandlerImpl(XMLSchemaValidatorComponentManager componentManager) {
        this.fSAXLocatorWrapper = new SAXLocatorWrapper();
        this.fNeedPushNSContext = true;
        this.fUnparsedEntities = null;
        this.fStringsInternalized = false;
        this.fElementQName = new QName();
        this.fAttributeQName = new QName();
        this.fAttributes = new XMLAttributesImpl();
        this.fAttrAdapter = new AttributesProxy(this.fAttributes);
        this.fTempString = new XMLString();
        this.fContentHandler = null;
        this.fTypeInfoProvider = new XMLSchemaTypeInfoProvider();
        this.fResolutionForwarder = new ResolutionForwarder(null);
        this.fComponentManager = componentManager;
        this.fErrorReporter = (XMLErrorReporter) this.fComponentManager.getProperty("http://apache.org/xml/properties/internal/error-reporter");
        this.fNamespaceContext = (NamespaceContext) this.fComponentManager.getProperty(NAMESPACE_CONTEXT);
        this.fSchemaValidator = (XMLSchemaValidator) this.fComponentManager.getProperty(SCHEMA_VALIDATOR);
        this.fSymbolTable = (SymbolTable) this.fComponentManager.getProperty("http://apache.org/xml/properties/internal/symbol-table");
        this.fValidationManager = (ValidationManager) this.fComponentManager.getProperty(VALIDATION_MANAGER);
    }

    @Override // javax.xml.validation.ValidatorHandler
    public void setContentHandler(ContentHandler receiver) {
        this.fContentHandler = receiver;
    }

    @Override // javax.xml.validation.ValidatorHandler
    public ContentHandler getContentHandler() {
        return this.fContentHandler;
    }

    @Override // javax.xml.validation.ValidatorHandler
    public void setErrorHandler(ErrorHandler errorHandler) {
        this.fComponentManager.setErrorHandler(errorHandler);
    }

    @Override // javax.xml.validation.ValidatorHandler
    public ErrorHandler getErrorHandler() {
        return this.fComponentManager.getErrorHandler();
    }

    @Override // javax.xml.validation.ValidatorHandler
    public void setResourceResolver(LSResourceResolver resourceResolver) {
        this.fComponentManager.setResourceResolver(resourceResolver);
    }

    @Override // javax.xml.validation.ValidatorHandler
    public LSResourceResolver getResourceResolver() {
        return this.fComponentManager.getResourceResolver();
    }

    @Override // javax.xml.validation.ValidatorHandler
    public TypeInfoProvider getTypeInfoProvider() {
        return this.fTypeInfoProvider;
    }

    @Override // javax.xml.validation.ValidatorHandler
    public boolean getFeature(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
        if (name == null) {
            throw new NullPointerException();
        }
        try {
            return this.fComponentManager.getFeature(name);
        } catch (XMLConfigurationException e2) {
            String identifier = e2.getIdentifier();
            String key = e2.getType() == Status.NOT_RECOGNIZED ? "feature-not-recognized" : "feature-not-supported";
            throw new SAXNotRecognizedException(SAXMessageFormatter.formatMessage(this.fComponentManager.getLocale(), key, new Object[]{identifier}));
        }
    }

    @Override // javax.xml.validation.ValidatorHandler
    public void setFeature(String name, boolean value) throws SAXNotRecognizedException, SAXNotSupportedException {
        String key;
        if (name == null) {
            throw new NullPointerException();
        }
        try {
            this.fComponentManager.setFeature(name, value);
        } catch (XMLConfigurationException e2) {
            String identifier = e2.getIdentifier();
            if (e2.getType() == Status.NOT_ALLOWED) {
                throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(this.fComponentManager.getLocale(), "jaxp-secureprocessing-feature", null));
            }
            if (e2.getType() == Status.NOT_RECOGNIZED) {
                key = "feature-not-recognized";
            } else {
                key = "feature-not-supported";
            }
            throw new SAXNotRecognizedException(SAXMessageFormatter.formatMessage(this.fComponentManager.getLocale(), key, new Object[]{identifier}));
        }
    }

    @Override // javax.xml.validation.ValidatorHandler
    public Object getProperty(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
        if (name == null) {
            throw new NullPointerException();
        }
        try {
            return this.fComponentManager.getProperty(name);
        } catch (XMLConfigurationException e2) {
            String identifier = e2.getIdentifier();
            String key = e2.getType() == Status.NOT_RECOGNIZED ? "property-not-recognized" : "property-not-supported";
            throw new SAXNotRecognizedException(SAXMessageFormatter.formatMessage(this.fComponentManager.getLocale(), key, new Object[]{identifier}));
        }
    }

    @Override // javax.xml.validation.ValidatorHandler
    public void setProperty(String name, Object object) throws SAXNotRecognizedException, SAXNotSupportedException {
        if (name == null) {
            throw new NullPointerException();
        }
        try {
            this.fComponentManager.setProperty(name, object);
        } catch (XMLConfigurationException e2) {
            String identifier = e2.getIdentifier();
            String key = e2.getType() == Status.NOT_RECOGNIZED ? "property-not-recognized" : "property-not-supported";
            throw new SAXNotRecognizedException(SAXMessageFormatter.formatMessage(this.fComponentManager.getLocale(), key, new Object[]{identifier}));
        }
    }

    @Override // com.sun.org.apache.xerces.internal.impl.validation.EntityState
    public boolean isEntityDeclared(String name) {
        return false;
    }

    @Override // com.sun.org.apache.xerces.internal.impl.validation.EntityState
    public boolean isEntityUnparsed(String name) {
        if (this.fUnparsedEntities != null) {
            return this.fUnparsedEntities.containsKey(name);
        }
        return false;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void startDocument(XMLLocator locator, String encoding, NamespaceContext namespaceContext, Augmentations augs) throws XNIException {
        if (this.fContentHandler != null) {
            try {
                this.fContentHandler.startDocument();
            } catch (SAXException e2) {
                throw new XNIException(e2);
            }
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void xmlDecl(String version, String encoding, String standalone, Augmentations augs) throws XNIException {
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void doctypeDecl(String rootElement, String publicId, String systemId, Augmentations augs) throws XNIException {
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void comment(XMLString text, Augmentations augs) throws XNIException {
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void processingInstruction(String target, XMLString data, Augmentations augs) throws XNIException {
        if (this.fContentHandler != null) {
            try {
                this.fContentHandler.processingInstruction(target, data.toString());
            } catch (SAXException e2) {
                throw new XNIException(e2);
            }
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void startElement(QName element, XMLAttributes attributes, Augmentations augs) throws XNIException {
        if (this.fContentHandler != null) {
            try {
                try {
                    this.fTypeInfoProvider.beginStartElement(augs, attributes);
                    this.fContentHandler.startElement(element.uri != null ? element.uri : XMLSymbols.EMPTY_STRING, element.localpart, element.rawname, this.fAttrAdapter);
                    this.fTypeInfoProvider.finishStartElement();
                } catch (SAXException e2) {
                    throw new XNIException(e2);
                }
            } catch (Throwable th) {
                this.fTypeInfoProvider.finishStartElement();
                throw th;
            }
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void emptyElement(QName element, XMLAttributes attributes, Augmentations augs) throws XNIException {
        startElement(element, attributes, augs);
        endElement(element, augs);
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void startGeneralEntity(String name, XMLResourceIdentifier identifier, String encoding, Augmentations augs) throws XNIException {
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void textDecl(String version, String encoding, Augmentations augs) throws XNIException {
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void endGeneralEntity(String name, Augmentations augs) throws XNIException {
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void characters(XMLString text, Augmentations augs) throws XNIException {
        if (this.fContentHandler == null || text.length == 0) {
            return;
        }
        try {
            this.fContentHandler.characters(text.ch, text.offset, text.length);
        } catch (SAXException e2) {
            throw new XNIException(e2);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void ignorableWhitespace(XMLString text, Augmentations augs) throws XNIException {
        if (this.fContentHandler != null) {
            try {
                this.fContentHandler.ignorableWhitespace(text.ch, text.offset, text.length);
            } catch (SAXException e2) {
                throw new XNIException(e2);
            }
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void endElement(QName element, Augmentations augs) throws XNIException {
        try {
            if (this.fContentHandler != null) {
                try {
                    this.fTypeInfoProvider.beginEndElement(augs);
                    this.fContentHandler.endElement(element.uri != null ? element.uri : XMLSymbols.EMPTY_STRING, element.localpart, element.rawname);
                    this.fTypeInfoProvider.finishEndElement();
                } catch (SAXException e2) {
                    throw new XNIException(e2);
                }
            }
        } catch (Throwable th) {
            this.fTypeInfoProvider.finishEndElement();
            throw th;
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void startCDATA(Augmentations augs) throws XNIException {
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void endCDATA(Augmentations augs) throws XNIException {
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void endDocument(Augmentations augs) throws XNIException {
        if (this.fContentHandler != null) {
            try {
                this.fContentHandler.endDocument();
            } catch (SAXException e2) {
                throw new XNIException(e2);
            }
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void setDocumentSource(XMLDocumentSource source) {
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public XMLDocumentSource getDocumentSource() {
        return this.fSchemaValidator;
    }

    @Override // org.xml.sax.ContentHandler
    public void setDocumentLocator(Locator locator) {
        this.fSAXLocatorWrapper.setLocator(locator);
        if (this.fContentHandler != null) {
            this.fContentHandler.setDocumentLocator(locator);
        }
    }

    @Override // org.xml.sax.ContentHandler
    public void startDocument() throws SAXException, XNIException {
        this.fComponentManager.reset();
        this.fSchemaValidator.setDocumentHandler(this);
        this.fValidationManager.setEntityState(this);
        this.fTypeInfoProvider.finishStartElement();
        this.fNeedPushNSContext = true;
        if (this.fUnparsedEntities != null && !this.fUnparsedEntities.isEmpty()) {
            this.fUnparsedEntities.clear();
        }
        this.fErrorReporter.setDocumentLocator(this.fSAXLocatorWrapper);
        try {
            this.fSchemaValidator.startDocument(this.fSAXLocatorWrapper, this.fSAXLocatorWrapper.getEncoding(), this.fNamespaceContext, null);
        } catch (XMLParseException e2) {
            throw Util.toSAXParseException(e2);
        } catch (XNIException e3) {
            throw Util.toSAXException(e3);
        }
    }

    @Override // org.xml.sax.ContentHandler
    public void endDocument() throws SAXException {
        this.fSAXLocatorWrapper.setLocator(null);
        try {
            this.fSchemaValidator.endDocument(null);
        } catch (XMLParseException e2) {
            throw Util.toSAXParseException(e2);
        } catch (XNIException e3) {
            throw Util.toSAXException(e3);
        }
    }

    @Override // org.xml.sax.ContentHandler
    public void startPrefixMapping(String prefix, String uri) throws SAXException {
        String prefixSymbol;
        String uriSymbol;
        if (!this.fStringsInternalized) {
            prefixSymbol = prefix != null ? this.fSymbolTable.addSymbol(prefix) : XMLSymbols.EMPTY_STRING;
            uriSymbol = (uri == null || uri.length() <= 0) ? null : this.fSymbolTable.addSymbol(uri);
        } else {
            prefixSymbol = prefix != null ? prefix : XMLSymbols.EMPTY_STRING;
            uriSymbol = (uri == null || uri.length() <= 0) ? null : uri;
        }
        if (this.fNeedPushNSContext) {
            this.fNeedPushNSContext = false;
            this.fNamespaceContext.pushContext();
        }
        this.fNamespaceContext.declarePrefix(prefixSymbol, uriSymbol);
        if (this.fContentHandler != null) {
            this.fContentHandler.startPrefixMapping(prefix, uri);
        }
    }

    @Override // org.xml.sax.ContentHandler
    public void endPrefixMapping(String prefix) throws SAXException {
        if (this.fContentHandler != null) {
            this.fContentHandler.endPrefixMapping(prefix);
        }
    }

    @Override // org.xml.sax.ContentHandler
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        if (this.fNeedPushNSContext) {
            this.fNamespaceContext.pushContext();
        }
        this.fNeedPushNSContext = true;
        fillQName(this.fElementQName, uri, localName, qName);
        if (atts instanceof Attributes2) {
            fillXMLAttributes2((Attributes2) atts);
        } else {
            fillXMLAttributes(atts);
        }
        try {
            this.fSchemaValidator.startElement(this.fElementQName, this.fAttributes, null);
        } catch (XMLParseException e2) {
            throw Util.toSAXParseException(e2);
        } catch (XNIException e3) {
            throw Util.toSAXException(e3);
        }
    }

    @Override // org.xml.sax.ContentHandler
    public void endElement(String uri, String localName, String qName) throws SAXException {
        fillQName(this.fElementQName, uri, localName, qName);
        try {
            try {
                this.fSchemaValidator.endElement(this.fElementQName, null);
                this.fNamespaceContext.popContext();
            } catch (XMLParseException e2) {
                throw Util.toSAXParseException(e2);
            } catch (XNIException e3) {
                throw Util.toSAXException(e3);
            }
        } catch (Throwable th) {
            this.fNamespaceContext.popContext();
            throw th;
        }
    }

    @Override // org.xml.sax.ContentHandler
    public void characters(char[] ch, int start, int length) throws SAXException {
        try {
            this.fTempString.setValues(ch, start, length);
            this.fSchemaValidator.characters(this.fTempString, null);
        } catch (XMLParseException e2) {
            throw Util.toSAXParseException(e2);
        } catch (XNIException e3) {
            throw Util.toSAXException(e3);
        }
    }

    @Override // org.xml.sax.ContentHandler
    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
        try {
            this.fTempString.setValues(ch, start, length);
            this.fSchemaValidator.ignorableWhitespace(this.fTempString, null);
        } catch (XMLParseException e2) {
            throw Util.toSAXParseException(e2);
        } catch (XNIException e3) {
            throw Util.toSAXException(e3);
        }
    }

    @Override // org.xml.sax.ContentHandler
    public void processingInstruction(String target, String data) throws SAXException {
        if (this.fContentHandler != null) {
            this.fContentHandler.processingInstruction(target, data);
        }
    }

    @Override // org.xml.sax.ContentHandler
    public void skippedEntity(String name) throws SAXException {
        if (this.fContentHandler != null) {
            this.fContentHandler.skippedEntity(name);
        }
    }

    @Override // org.xml.sax.DTDHandler
    public void notationDecl(String name, String publicId, String systemId) throws SAXException {
    }

    @Override // org.xml.sax.DTDHandler
    public void unparsedEntityDecl(String name, String publicId, String systemId, String notationName) throws SAXException {
        if (this.fUnparsedEntities == null) {
            this.fUnparsedEntities = new HashMap();
        }
        this.fUnparsedEntities.put(name, name);
    }

    @Override // com.sun.org.apache.xerces.internal.jaxp.validation.ValidatorHelper
    public void validate(Source source, Result result) throws SAXException, IOException {
        if ((result instanceof SAXResult) || result == null) {
            SAXSource saxSource = (SAXSource) source;
            SAXResult saxResult = (SAXResult) result;
            if (result != null) {
                setContentHandler(saxResult.getHandler());
            }
            try {
                XMLReader reader = saxSource.getXMLReader();
                if (reader == null) {
                    reader = JdkXmlUtils.getXMLReader(this.fComponentManager.getFeature(JdkXmlUtils.OVERRIDE_PARSER), this.fComponentManager.getFeature("http://javax.xml.XMLConstants/feature/secure-processing"));
                    try {
                        if (reader instanceof SAXParser) {
                            XMLSecurityManager securityManager = (XMLSecurityManager) this.fComponentManager.getProperty("http://apache.org/xml/properties/security-manager");
                            if (securityManager != null) {
                                try {
                                    reader.setProperty("http://apache.org/xml/properties/security-manager", securityManager);
                                } catch (SAXException e2) {
                                }
                            }
                            try {
                                XMLSecurityPropertyManager spm = (XMLSecurityPropertyManager) this.fComponentManager.getProperty("http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager");
                                reader.setProperty("http://javax.xml.XMLConstants/property/accessExternalDTD", spm.getValue(XMLSecurityPropertyManager.Property.ACCESS_EXTERNAL_DTD));
                            } catch (SAXException exc) {
                                XMLSecurityManager.printWarning(reader.getClass().getName(), "http://javax.xml.XMLConstants/property/accessExternalDTD", exc);
                            }
                        }
                    } catch (Exception e3) {
                        throw new FactoryConfigurationError(e3);
                    }
                }
                try {
                    this.fStringsInternalized = reader.getFeature("http://xml.org/sax/features/string-interning");
                } catch (SAXException e4) {
                    this.fStringsInternalized = false;
                }
                ErrorHandler errorHandler = this.fComponentManager.getErrorHandler();
                reader.setErrorHandler(errorHandler != null ? errorHandler : DraconianErrorHandler.getInstance());
                reader.setEntityResolver(this.fResolutionForwarder);
                this.fResolutionForwarder.setEntityResolver(this.fComponentManager.getResourceResolver());
                reader.setContentHandler(this);
                reader.setDTDHandler(this);
                InputSource is = saxSource.getInputSource();
                reader.parse(is);
                setContentHandler(null);
                return;
            } catch (Throwable th) {
                setContentHandler(null);
                throw th;
            }
        }
        throw new IllegalArgumentException(JAXPValidationMessageFormatter.formatMessage(this.fComponentManager.getLocale(), "SourceResultMismatch", new Object[]{source.getClass().getName(), result.getClass().getName()}));
    }

    @Override // com.sun.org.apache.xerces.internal.xs.PSVIProvider
    public ElementPSVI getElementPSVI() {
        return this.fTypeInfoProvider.getElementPSVI();
    }

    @Override // com.sun.org.apache.xerces.internal.xs.PSVIProvider
    public AttributePSVI getAttributePSVI(int index) {
        return this.fTypeInfoProvider.getAttributePSVI(index);
    }

    @Override // com.sun.org.apache.xerces.internal.xs.PSVIProvider
    public AttributePSVI getAttributePSVIByName(String uri, String localname) {
        return this.fTypeInfoProvider.getAttributePSVIByName(uri, localname);
    }

    private void fillQName(QName toFill, String uri, String localpart, String raw) {
        if (!this.fStringsInternalized) {
            uri = (uri == null || uri.length() <= 0) ? null : this.fSymbolTable.addSymbol(uri);
            localpart = localpart != null ? this.fSymbolTable.addSymbol(localpart) : XMLSymbols.EMPTY_STRING;
            raw = raw != null ? this.fSymbolTable.addSymbol(raw) : XMLSymbols.EMPTY_STRING;
        } else {
            if (uri != null && uri.length() == 0) {
                uri = null;
            }
            if (localpart == null) {
                localpart = XMLSymbols.EMPTY_STRING;
            }
            if (raw == null) {
                raw = XMLSymbols.EMPTY_STRING;
            }
        }
        String prefix = XMLSymbols.EMPTY_STRING;
        int prefixIdx = raw.indexOf(58);
        if (prefixIdx != -1) {
            prefix = this.fSymbolTable.addSymbol(raw.substring(0, prefixIdx));
        }
        toFill.setValues(prefix, localpart, raw, uri);
    }

    private void fillXMLAttributes(Attributes att) {
        this.fAttributes.removeAllAttributes();
        int len = att.getLength();
        for (int i2 = 0; i2 < len; i2++) {
            fillXMLAttribute(att, i2);
            this.fAttributes.setSpecified(i2, true);
        }
    }

    private void fillXMLAttributes2(Attributes2 att) {
        this.fAttributes.removeAllAttributes();
        int len = att.getLength();
        for (int i2 = 0; i2 < len; i2++) {
            fillXMLAttribute(att, i2);
            this.fAttributes.setSpecified(i2, att.isSpecified(i2));
            if (att.isDeclared(i2)) {
                this.fAttributes.getAugmentations(i2).putItem(Constants.ATTRIBUTE_DECLARED, Boolean.TRUE);
            }
        }
    }

    private void fillXMLAttribute(Attributes att, int index) {
        fillQName(this.fAttributeQName, att.getURI(index), att.getLocalName(index), att.getQName(index));
        String type = att.getType(index);
        this.fAttributes.addAttributeNS(this.fAttributeQName, type != null ? type : XMLSymbols.fCDATASymbol, att.getValue(index));
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/jaxp/validation/ValidatorHandlerImpl$XMLSchemaTypeInfoProvider.class */
    private class XMLSchemaTypeInfoProvider extends TypeInfoProvider {
        private Augmentations fElementAugs;
        private XMLAttributes fAttributes;
        private boolean fInStartElement;
        private boolean fInEndElement;

        private XMLSchemaTypeInfoProvider() {
            this.fInStartElement = false;
            this.fInEndElement = false;
        }

        void beginStartElement(Augmentations elementAugs, XMLAttributes attributes) {
            this.fInStartElement = true;
            this.fElementAugs = elementAugs;
            this.fAttributes = attributes;
        }

        void finishStartElement() {
            this.fInStartElement = false;
            this.fElementAugs = null;
            this.fAttributes = null;
        }

        void beginEndElement(Augmentations elementAugs) {
            this.fInEndElement = true;
            this.fElementAugs = elementAugs;
        }

        void finishEndElement() {
            this.fInEndElement = false;
            this.fElementAugs = null;
        }

        private void checkState(boolean forElementInfo) {
            if (this.fInStartElement) {
                return;
            }
            if (!this.fInEndElement || !forElementInfo) {
                throw new IllegalStateException(JAXPValidationMessageFormatter.formatMessage(ValidatorHandlerImpl.this.fComponentManager.getLocale(), "TypeInfoProviderIllegalState", null));
            }
        }

        @Override // javax.xml.validation.TypeInfoProvider
        public TypeInfo getAttributeTypeInfo(int index) {
            checkState(false);
            return getAttributeType(index);
        }

        private TypeInfo getAttributeType(int index) {
            checkState(false);
            if (index < 0 || this.fAttributes.getLength() <= index) {
                throw new IndexOutOfBoundsException(Integer.toString(index));
            }
            Augmentations augs = this.fAttributes.getAugmentations(index);
            if (augs == null) {
                return null;
            }
            AttributePSVI psvi = (AttributePSVI) augs.getItem(Constants.ATTRIBUTE_PSVI);
            return getTypeInfoFromPSVI(psvi);
        }

        public TypeInfo getAttributeTypeInfo(String attributeUri, String attributeLocalName) {
            checkState(false);
            return getAttributeTypeInfo(this.fAttributes.getIndex(attributeUri, attributeLocalName));
        }

        public TypeInfo getAttributeTypeInfo(String attributeQName) {
            checkState(false);
            return getAttributeTypeInfo(this.fAttributes.getIndex(attributeQName));
        }

        @Override // javax.xml.validation.TypeInfoProvider
        public TypeInfo getElementTypeInfo() {
            checkState(true);
            if (this.fElementAugs == null) {
                return null;
            }
            ElementPSVI psvi = (ElementPSVI) this.fElementAugs.getItem(Constants.ELEMENT_PSVI);
            return getTypeInfoFromPSVI(psvi);
        }

        private TypeInfo getTypeInfoFromPSVI(ItemPSVI psvi) {
            XSTypeDefinition t2;
            if (psvi == null) {
                return null;
            }
            if (psvi.getValidity() == 2 && (t2 = psvi.getMemberTypeDefinition()) != null) {
                if (t2 instanceof TypeInfo) {
                    return (TypeInfo) t2;
                }
                return null;
            }
            XSTypeDefinition t3 = psvi.getTypeDefinition();
            if (t3 == null || !(t3 instanceof TypeInfo)) {
                return null;
            }
            return (TypeInfo) t3;
        }

        @Override // javax.xml.validation.TypeInfoProvider
        public boolean isIdAttribute(int index) {
            checkState(false);
            XSSimpleType type = (XSSimpleType) getAttributeType(index);
            if (type == null) {
                return false;
            }
            return type.isIDType();
        }

        @Override // javax.xml.validation.TypeInfoProvider
        public boolean isSpecified(int index) {
            checkState(false);
            return this.fAttributes.isSpecified(index);
        }

        ElementPSVI getElementPSVI() {
            if (this.fElementAugs != null) {
                return (ElementPSVI) this.fElementAugs.getItem(Constants.ELEMENT_PSVI);
            }
            return null;
        }

        AttributePSVI getAttributePSVI(int index) {
            Augmentations augs;
            if (this.fAttributes != null && (augs = this.fAttributes.getAugmentations(index)) != null) {
                return (AttributePSVI) augs.getItem(Constants.ATTRIBUTE_PSVI);
            }
            return null;
        }

        AttributePSVI getAttributePSVIByName(String uri, String localname) {
            Augmentations augs;
            if (this.fAttributes != null && (augs = this.fAttributes.getAugmentations(uri, localname)) != null) {
                return (AttributePSVI) augs.getItem(Constants.ATTRIBUTE_PSVI);
            }
            return null;
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/jaxp/validation/ValidatorHandlerImpl$ResolutionForwarder.class */
    static final class ResolutionForwarder implements EntityResolver2 {
        private static final String XML_TYPE = "http://www.w3.org/TR/REC-xml";
        protected LSResourceResolver fEntityResolver;

        public ResolutionForwarder() {
        }

        public ResolutionForwarder(LSResourceResolver entityResolver) {
            setEntityResolver(entityResolver);
        }

        public void setEntityResolver(LSResourceResolver entityResolver) {
            this.fEntityResolver = entityResolver;
        }

        public LSResourceResolver getEntityResolver() {
            return this.fEntityResolver;
        }

        @Override // org.xml.sax.ext.EntityResolver2
        public InputSource getExternalSubset(String name, String baseURI) throws SAXException, IOException {
            return null;
        }

        @Override // org.xml.sax.ext.EntityResolver2
        public InputSource resolveEntity(String name, String publicId, String baseURI, String systemId) throws SAXException, IOException {
            LSInput lsInput;
            if (this.fEntityResolver != null && (lsInput = this.fEntityResolver.resolveResource("http://www.w3.org/TR/REC-xml", null, publicId, systemId, baseURI)) != null) {
                String pubId = lsInput.getPublicId();
                lsInput.getSystemId();
                String baseSystemId = lsInput.getBaseURI();
                Reader charStream = lsInput.getCharacterStream();
                InputStream byteStream = lsInput.getByteStream();
                String data = lsInput.getStringData();
                String encoding = lsInput.getEncoding();
                InputSource inputSource = new InputSource();
                inputSource.setPublicId(pubId);
                inputSource.setSystemId(baseSystemId != null ? resolveSystemId(systemId, baseSystemId) : systemId);
                if (charStream != null) {
                    inputSource.setCharacterStream(charStream);
                } else if (byteStream != null) {
                    inputSource.setByteStream(byteStream);
                } else if (data != null && data.length() != 0) {
                    inputSource.setCharacterStream(new StringReader(data));
                }
                inputSource.setEncoding(encoding);
                return inputSource;
            }
            return null;
        }

        @Override // org.xml.sax.EntityResolver
        public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
            return resolveEntity(null, publicId, null, systemId);
        }

        private String resolveSystemId(String systemId, String baseURI) {
            try {
                return XMLEntityManager.expandSystemId(systemId, baseURI, false);
            } catch (URI.MalformedURIException e2) {
                return systemId;
            }
        }
    }
}
