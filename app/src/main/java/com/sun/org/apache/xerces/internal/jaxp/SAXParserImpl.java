package com.sun.org.apache.xerces.internal.jaxp;

import com.sun.org.apache.xerces.internal.impl.Constants;
import com.sun.org.apache.xerces.internal.impl.validation.ValidationManager;
import com.sun.org.apache.xerces.internal.impl.xs.XMLSchemaValidator;
import com.sun.org.apache.xerces.internal.jaxp.validation.XSGrammarPoolContainer;
import com.sun.org.apache.xerces.internal.util.SAXMessageFormatter;
import com.sun.org.apache.xerces.internal.util.Status;
import com.sun.org.apache.xerces.internal.utils.XMLSecurityManager;
import com.sun.org.apache.xerces.internal.utils.XMLSecurityPropertyManager;
import com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler;
import com.sun.org.apache.xerces.internal.xni.parser.XMLComponent;
import com.sun.org.apache.xerces.internal.xni.parser.XMLComponentManager;
import com.sun.org.apache.xerces.internal.xni.parser.XMLConfigurationException;
import com.sun.org.apache.xerces.internal.xni.parser.XMLDocumentSource;
import com.sun.org.apache.xerces.internal.xni.parser.XMLParserConfiguration;
import com.sun.org.apache.xerces.internal.xs.AttributePSVI;
import com.sun.org.apache.xerces.internal.xs.ElementPSVI;
import com.sun.org.apache.xerces.internal.xs.PSVIProvider;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.xml.parsers.SAXParser;
import javax.xml.validation.Schema;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.HandlerBase;
import org.xml.sax.InputSource;
import org.xml.sax.Parser;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/jaxp/SAXParserImpl.class */
public class SAXParserImpl extends SAXParser implements JAXPConstants, PSVIProvider {
    private static final String NAMESPACES_FEATURE = "http://xml.org/sax/features/namespaces";
    private static final String NAMESPACE_PREFIXES_FEATURE = "http://xml.org/sax/features/namespace-prefixes";
    private static final String VALIDATION_FEATURE = "http://xml.org/sax/features/validation";
    private static final String XMLSCHEMA_VALIDATION_FEATURE = "http://apache.org/xml/features/validation/schema";
    private static final String XINCLUDE_FEATURE = "http://apache.org/xml/features/xinclude";
    private static final String SECURITY_MANAGER = "http://apache.org/xml/properties/security-manager";
    private static final String XML_SECURITY_PROPERTY_MANAGER = "http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager";
    private final JAXPSAXParser xmlReader;
    private String schemaLanguage;
    private final Schema grammar;
    private final XMLComponent fSchemaValidator;
    private final XMLComponentManager fSchemaValidatorComponentManager;
    private final ValidationManager fSchemaValidationManager;
    private final UnparsedEntityHandler fUnparsedEntityHandler;
    private final ErrorHandler fInitErrorHandler;
    private final EntityResolver fInitEntityResolver;
    private final XMLSecurityManager fSecurityManager;
    private final XMLSecurityPropertyManager fSecurityPropertyMgr;

    SAXParserImpl(SAXParserFactoryImpl spf, Map<String, Boolean> features) throws SAXException {
        this(spf, features, false);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v26, types: [com.sun.org.apache.xerces.internal.xni.parser.XMLComponent] */
    /* JADX WARN: Type inference failed for: r1v28, types: [com.sun.org.apache.xerces.internal.xni.parser.XMLComponent] */
    /* JADX WARN: Type inference failed for: r1v36, types: [com.sun.org.apache.xerces.internal.xni.parser.XMLComponent] */
    SAXParserImpl(SAXParserFactoryImpl spf, Map<String, Boolean> features, boolean secureProcessing) throws SAXException {
        XMLDocumentHandler jAXPValidatorComponent;
        Boolean temp;
        this.schemaLanguage = null;
        this.fSecurityManager = new XMLSecurityManager(secureProcessing);
        this.fSecurityPropertyMgr = new XMLSecurityPropertyManager();
        this.xmlReader = new JAXPSAXParser(this, this.fSecurityPropertyMgr, this.fSecurityManager);
        this.xmlReader.setFeature0("http://xml.org/sax/features/namespaces", spf.isNamespaceAware());
        this.xmlReader.setFeature0("http://xml.org/sax/features/namespace-prefixes", !spf.isNamespaceAware());
        if (spf.isXIncludeAware()) {
            this.xmlReader.setFeature0(XINCLUDE_FEATURE, true);
        }
        this.xmlReader.setProperty0("http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager", this.fSecurityPropertyMgr);
        this.xmlReader.setProperty0("http://apache.org/xml/properties/security-manager", this.fSecurityManager);
        if (secureProcessing && features != null && (temp = features.get("http://javax.xml.XMLConstants/feature/secure-processing")) != null && temp.booleanValue() && Constants.IS_JDK8_OR_ABOVE) {
            this.fSecurityPropertyMgr.setValue(XMLSecurityPropertyManager.Property.ACCESS_EXTERNAL_DTD, XMLSecurityPropertyManager.State.FSP, "");
            this.fSecurityPropertyMgr.setValue(XMLSecurityPropertyManager.Property.ACCESS_EXTERNAL_SCHEMA, XMLSecurityPropertyManager.State.FSP, "");
        }
        setFeatures(features);
        if (spf.isValidating()) {
            this.fInitErrorHandler = new DefaultValidationErrorHandler(this.xmlReader.getLocale());
            this.xmlReader.setErrorHandler(this.fInitErrorHandler);
        } else {
            this.fInitErrorHandler = this.xmlReader.getErrorHandler();
        }
        this.xmlReader.setFeature0(VALIDATION_FEATURE, spf.isValidating());
        this.grammar = spf.getSchema();
        if (this.grammar != null) {
            XMLParserConfiguration config = this.xmlReader.getXMLParserConfiguration();
            if (this.grammar instanceof XSGrammarPoolContainer) {
                jAXPValidatorComponent = new XMLSchemaValidator();
                this.fSchemaValidationManager = new ValidationManager();
                this.fUnparsedEntityHandler = new UnparsedEntityHandler(this.fSchemaValidationManager);
                config.setDTDHandler(this.fUnparsedEntityHandler);
                this.fUnparsedEntityHandler.setDTDHandler(this.xmlReader);
                this.xmlReader.setDTDSource(this.fUnparsedEntityHandler);
                this.fSchemaValidatorComponentManager = new SchemaValidatorConfiguration(config, (XSGrammarPoolContainer) this.grammar, this.fSchemaValidationManager);
            } else {
                jAXPValidatorComponent = new JAXPValidatorComponent(this.grammar.newValidatorHandler());
                this.fSchemaValidationManager = null;
                this.fUnparsedEntityHandler = null;
                this.fSchemaValidatorComponentManager = config;
            }
            config.addRecognizedFeatures(jAXPValidatorComponent.getRecognizedFeatures());
            config.addRecognizedProperties(jAXPValidatorComponent.getRecognizedProperties());
            config.setDocumentHandler(jAXPValidatorComponent);
            ((XMLDocumentSource) jAXPValidatorComponent).setDocumentHandler(this.xmlReader);
            this.xmlReader.setDocumentSource((XMLDocumentSource) jAXPValidatorComponent);
            this.fSchemaValidator = jAXPValidatorComponent;
        } else {
            this.fSchemaValidationManager = null;
            this.fUnparsedEntityHandler = null;
            this.fSchemaValidatorComponentManager = null;
            this.fSchemaValidator = null;
        }
        this.fInitEntityResolver = this.xmlReader.getEntityResolver();
    }

    private void setFeatures(Map<String, Boolean> features) throws SAXNotRecognizedException, SAXNotSupportedException {
        if (features != null) {
            for (Map.Entry<String, Boolean> entry : features.entrySet()) {
                this.xmlReader.setFeature0(entry.getKey(), entry.getValue().booleanValue());
            }
        }
    }

    @Override // javax.xml.parsers.SAXParser
    public Parser getParser() throws SAXException {
        return this.xmlReader;
    }

    @Override // javax.xml.parsers.SAXParser
    public XMLReader getXMLReader() {
        return this.xmlReader;
    }

    @Override // javax.xml.parsers.SAXParser
    public boolean isNamespaceAware() {
        try {
            return this.xmlReader.getFeature("http://xml.org/sax/features/namespaces");
        } catch (SAXException x2) {
            throw new IllegalStateException(x2.getMessage());
        }
    }

    @Override // javax.xml.parsers.SAXParser
    public boolean isValidating() {
        try {
            return this.xmlReader.getFeature(VALIDATION_FEATURE);
        } catch (SAXException x2) {
            throw new IllegalStateException(x2.getMessage());
        }
    }

    @Override // javax.xml.parsers.SAXParser
    public boolean isXIncludeAware() {
        try {
            return this.xmlReader.getFeature(XINCLUDE_FEATURE);
        } catch (SAXException e2) {
            return false;
        }
    }

    @Override // javax.xml.parsers.SAXParser
    public void setProperty(String name, Object value) throws SAXNotRecognizedException, SAXNotSupportedException {
        this.xmlReader.setProperty(name, value);
    }

    @Override // javax.xml.parsers.SAXParser
    public Object getProperty(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
        return this.xmlReader.getProperty(name);
    }

    @Override // javax.xml.parsers.SAXParser
    public void parse(InputSource is, DefaultHandler dh) throws SAXException, IOException {
        if (is == null) {
            throw new IllegalArgumentException();
        }
        if (dh != null) {
            this.xmlReader.setContentHandler(dh);
            this.xmlReader.setEntityResolver(dh);
            this.xmlReader.setErrorHandler(dh);
            this.xmlReader.setDTDHandler(dh);
            this.xmlReader.setDocumentHandler(null);
        }
        this.xmlReader.parse(is);
    }

    @Override // javax.xml.parsers.SAXParser
    public void parse(InputSource is, HandlerBase hb) throws SAXException, IOException {
        if (is == null) {
            throw new IllegalArgumentException();
        }
        if (hb != null) {
            this.xmlReader.setDocumentHandler(hb);
            this.xmlReader.setEntityResolver(hb);
            this.xmlReader.setErrorHandler(hb);
            this.xmlReader.setDTDHandler(hb);
            this.xmlReader.setContentHandler(null);
        }
        this.xmlReader.parse(is);
    }

    @Override // javax.xml.parsers.SAXParser
    public Schema getSchema() {
        return this.grammar;
    }

    @Override // javax.xml.parsers.SAXParser
    public void reset() {
        try {
            this.xmlReader.restoreInitState();
        } catch (SAXException e2) {
        }
        this.xmlReader.setContentHandler(null);
        this.xmlReader.setDTDHandler(null);
        if (this.xmlReader.getErrorHandler() != this.fInitErrorHandler) {
            this.xmlReader.setErrorHandler(this.fInitErrorHandler);
        }
        if (this.xmlReader.getEntityResolver() != this.fInitEntityResolver) {
            this.xmlReader.setEntityResolver(this.fInitEntityResolver);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xs.PSVIProvider
    public ElementPSVI getElementPSVI() {
        return this.xmlReader.getElementPSVI();
    }

    @Override // com.sun.org.apache.xerces.internal.xs.PSVIProvider
    public AttributePSVI getAttributePSVI(int index) {
        return this.xmlReader.getAttributePSVI(index);
    }

    @Override // com.sun.org.apache.xerces.internal.xs.PSVIProvider
    public AttributePSVI getAttributePSVIByName(String uri, String localname) {
        return this.xmlReader.getAttributePSVIByName(uri, localname);
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/jaxp/SAXParserImpl$JAXPSAXParser.class */
    public static class JAXPSAXParser extends com.sun.org.apache.xerces.internal.parsers.SAXParser {
        private final HashMap fInitFeatures;
        private final HashMap fInitProperties;
        private final SAXParserImpl fSAXParser;
        private XMLSecurityManager fSecurityManager;
        private XMLSecurityPropertyManager fSecurityPropertyMgr;

        public JAXPSAXParser() {
            this(null, null, null);
        }

        JAXPSAXParser(SAXParserImpl saxParser, XMLSecurityPropertyManager securityPropertyMgr, XMLSecurityManager securityManager) {
            this.fInitFeatures = new HashMap();
            this.fInitProperties = new HashMap();
            this.fSAXParser = saxParser;
            this.fSecurityManager = securityManager;
            this.fSecurityPropertyMgr = securityPropertyMgr;
            if (this.fSecurityManager == null) {
                this.fSecurityManager = new XMLSecurityManager(true);
                try {
                    super.setProperty("http://apache.org/xml/properties/security-manager", this.fSecurityManager);
                } catch (SAXException e2) {
                    throw new UnsupportedOperationException(SAXMessageFormatter.formatMessage(this.fConfiguration.getLocale(), "property-not-recognized", new Object[]{"http://apache.org/xml/properties/security-manager"}), e2);
                }
            }
            if (this.fSecurityPropertyMgr == null) {
                this.fSecurityPropertyMgr = new XMLSecurityPropertyManager();
                try {
                    super.setProperty("http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager", this.fSecurityPropertyMgr);
                } catch (SAXException e3) {
                    throw new UnsupportedOperationException(SAXMessageFormatter.formatMessage(this.fConfiguration.getLocale(), "property-not-recognized", new Object[]{"http://apache.org/xml/properties/security-manager"}), e3);
                }
            }
        }

        @Override // com.sun.org.apache.xerces.internal.parsers.AbstractSAXParser, org.xml.sax.XMLReader
        public synchronized void setFeature(String name, boolean value) throws SAXNotRecognizedException, SAXNotSupportedException {
            if (name == null) {
                throw new NullPointerException();
            }
            if (name.equals("http://javax.xml.XMLConstants/feature/secure-processing")) {
                try {
                    this.fSecurityManager.setSecureProcessing(value);
                    setProperty("http://apache.org/xml/properties/security-manager", this.fSecurityManager);
                    return;
                } catch (SAXNotRecognizedException exc) {
                    if (value) {
                        throw exc;
                    }
                    return;
                } catch (SAXNotSupportedException exc2) {
                    if (value) {
                        throw exc2;
                    }
                    return;
                }
            }
            if (!this.fInitFeatures.containsKey(name)) {
                boolean current = super.getFeature(name);
                this.fInitFeatures.put(name, current ? Boolean.TRUE : Boolean.FALSE);
            }
            if (this.fSAXParser != null && this.fSAXParser.fSchemaValidator != null) {
                setSchemaValidatorFeature(name, value);
            }
            super.setFeature(name, value);
        }

        @Override // com.sun.org.apache.xerces.internal.parsers.AbstractSAXParser, com.sun.org.apache.xerces.internal.parsers.XMLParser, org.xml.sax.XMLReader
        public synchronized boolean getFeature(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
            if (name == null) {
                throw new NullPointerException();
            }
            if (name.equals("http://javax.xml.XMLConstants/feature/secure-processing")) {
                return this.fSecurityManager.isSecureProcessing();
            }
            return super.getFeature(name);
        }

        @Override // com.sun.org.apache.xerces.internal.parsers.SAXParser, com.sun.org.apache.xerces.internal.parsers.AbstractSAXParser, org.xml.sax.XMLReader
        public synchronized void setProperty(String name, Object value) throws SAXNotRecognizedException, SAXNotSupportedException {
            if (name == null) {
                throw new NullPointerException();
            }
            if (this.fSAXParser != null) {
                if (JAXPConstants.JAXP_SCHEMA_LANGUAGE.equals(name)) {
                    if (this.fSAXParser.grammar != null) {
                        throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(this.fConfiguration.getLocale(), "schema-already-specified", new Object[]{name}));
                    }
                    if ("http://www.w3.org/2001/XMLSchema".equals(value)) {
                        if (this.fSAXParser.isValidating()) {
                            this.fSAXParser.schemaLanguage = "http://www.w3.org/2001/XMLSchema";
                            setFeature(SAXParserImpl.XMLSCHEMA_VALIDATION_FEATURE, true);
                            if (!this.fInitProperties.containsKey(JAXPConstants.JAXP_SCHEMA_LANGUAGE)) {
                                this.fInitProperties.put(JAXPConstants.JAXP_SCHEMA_LANGUAGE, super.getProperty(JAXPConstants.JAXP_SCHEMA_LANGUAGE));
                            }
                            super.setProperty(JAXPConstants.JAXP_SCHEMA_LANGUAGE, "http://www.w3.org/2001/XMLSchema");
                            return;
                        }
                        return;
                    }
                    if (value == null) {
                        this.fSAXParser.schemaLanguage = null;
                        setFeature(SAXParserImpl.XMLSCHEMA_VALIDATION_FEATURE, false);
                        return;
                    }
                    throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(this.fConfiguration.getLocale(), "schema-not-supported", null));
                }
                if (JAXPConstants.JAXP_SCHEMA_SOURCE.equals(name)) {
                    if (this.fSAXParser.grammar != null) {
                        throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(this.fConfiguration.getLocale(), "schema-already-specified", new Object[]{name}));
                    }
                    String val = (String) getProperty(JAXPConstants.JAXP_SCHEMA_LANGUAGE);
                    if (val != null && "http://www.w3.org/2001/XMLSchema".equals(val)) {
                        if (!this.fInitProperties.containsKey(JAXPConstants.JAXP_SCHEMA_SOURCE)) {
                            this.fInitProperties.put(JAXPConstants.JAXP_SCHEMA_SOURCE, super.getProperty(JAXPConstants.JAXP_SCHEMA_SOURCE));
                        }
                        super.setProperty(name, value);
                        return;
                    }
                    throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(this.fConfiguration.getLocale(), "jaxp-order-not-supported", new Object[]{JAXPConstants.JAXP_SCHEMA_LANGUAGE, JAXPConstants.JAXP_SCHEMA_SOURCE}));
                }
            }
            if (this.fSAXParser != null && this.fSAXParser.fSchemaValidator != null) {
                setSchemaValidatorProperty(name, value);
            }
            if (this.fSecurityManager == null || !this.fSecurityManager.setLimit(name, XMLSecurityManager.State.APIPROPERTY, value)) {
                if (this.fSecurityPropertyMgr == null || !this.fSecurityPropertyMgr.setValue(name, XMLSecurityPropertyManager.State.APIPROPERTY, value)) {
                    if (!this.fInitProperties.containsKey(name)) {
                        this.fInitProperties.put(name, super.getProperty(name));
                    }
                    super.setProperty(name, value);
                }
            }
        }

        @Override // com.sun.org.apache.xerces.internal.parsers.AbstractSAXParser, org.xml.sax.XMLReader
        public synchronized Object getProperty(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
            if (name == null) {
                throw new NullPointerException();
            }
            if (this.fSAXParser != null && JAXPConstants.JAXP_SCHEMA_LANGUAGE.equals(name)) {
                return this.fSAXParser.schemaLanguage;
            }
            String propertyValue = this.fSecurityManager != null ? this.fSecurityManager.getLimitAsString(name) : null;
            if (propertyValue != null) {
                return propertyValue;
            }
            String propertyValue2 = this.fSecurityPropertyMgr != null ? this.fSecurityPropertyMgr.getValue(name) : null;
            if (propertyValue2 != null) {
                return propertyValue2;
            }
            return super.getProperty(name);
        }

        synchronized void restoreInitState() throws SAXNotRecognizedException, SAXNotSupportedException {
            if (!this.fInitFeatures.isEmpty()) {
                for (Map.Entry entry : this.fInitFeatures.entrySet()) {
                    String name = (String) entry.getKey();
                    boolean value = ((Boolean) entry.getValue()).booleanValue();
                    super.setFeature(name, value);
                }
                this.fInitFeatures.clear();
            }
            if (!this.fInitProperties.isEmpty()) {
                for (Map.Entry entry2 : this.fInitProperties.entrySet()) {
                    String name2 = (String) entry2.getKey();
                    Object value2 = entry2.getValue();
                    super.setProperty(name2, value2);
                }
                this.fInitProperties.clear();
            }
        }

        @Override // com.sun.org.apache.xerces.internal.parsers.AbstractSAXParser, org.xml.sax.Parser, org.xml.sax.XMLReader
        public void parse(InputSource inputSource) throws SAXException, IOException {
            if (this.fSAXParser != null && this.fSAXParser.fSchemaValidator != null) {
                if (this.fSAXParser.fSchemaValidationManager != null) {
                    this.fSAXParser.fSchemaValidationManager.reset();
                    this.fSAXParser.fUnparsedEntityHandler.reset();
                }
                resetSchemaValidator();
            }
            super.parse(inputSource);
        }

        @Override // com.sun.org.apache.xerces.internal.parsers.AbstractSAXParser, org.xml.sax.Parser, org.xml.sax.XMLReader
        public void parse(String systemId) throws SAXException, IOException {
            if (this.fSAXParser != null && this.fSAXParser.fSchemaValidator != null) {
                if (this.fSAXParser.fSchemaValidationManager != null) {
                    this.fSAXParser.fSchemaValidationManager.reset();
                    this.fSAXParser.fUnparsedEntityHandler.reset();
                }
                resetSchemaValidator();
            }
            super.parse(systemId);
        }

        XMLParserConfiguration getXMLParserConfiguration() {
            return this.fConfiguration;
        }

        void setFeature0(String name, boolean value) throws SAXNotRecognizedException, SAXNotSupportedException {
            super.setFeature(name, value);
        }

        boolean getFeature0(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
            return super.getFeature(name);
        }

        void setProperty0(String name, Object value) throws SAXNotRecognizedException, SAXNotSupportedException {
            super.setProperty(name, value);
        }

        Object getProperty0(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
            return super.getProperty(name);
        }

        Locale getLocale() {
            return this.fConfiguration.getLocale();
        }

        private void setSchemaValidatorFeature(String name, boolean value) throws SAXNotRecognizedException, SAXNotSupportedException {
            try {
                this.fSAXParser.fSchemaValidator.setFeature(name, value);
            } catch (XMLConfigurationException e2) {
                String identifier = e2.getIdentifier();
                if (e2.getType() == Status.NOT_RECOGNIZED) {
                    throw new SAXNotRecognizedException(SAXMessageFormatter.formatMessage(this.fConfiguration.getLocale(), "feature-not-recognized", new Object[]{identifier}));
                }
                throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(this.fConfiguration.getLocale(), "feature-not-supported", new Object[]{identifier}));
            }
        }

        private void setSchemaValidatorProperty(String name, Object value) throws SAXNotRecognizedException, SAXNotSupportedException {
            try {
                this.fSAXParser.fSchemaValidator.setProperty(name, value);
            } catch (XMLConfigurationException e2) {
                String identifier = e2.getIdentifier();
                if (e2.getType() == Status.NOT_RECOGNIZED) {
                    throw new SAXNotRecognizedException(SAXMessageFormatter.formatMessage(this.fConfiguration.getLocale(), "property-not-recognized", new Object[]{identifier}));
                }
                throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(this.fConfiguration.getLocale(), "property-not-supported", new Object[]{identifier}));
            }
        }

        private void resetSchemaValidator() throws SAXException {
            try {
                this.fSAXParser.fSchemaValidator.reset(this.fSAXParser.fSchemaValidatorComponentManager);
            } catch (XMLConfigurationException e2) {
                throw new SAXException(e2);
            }
        }
    }
}
