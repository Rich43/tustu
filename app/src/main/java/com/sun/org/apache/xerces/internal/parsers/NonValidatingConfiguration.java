package com.sun.org.apache.xerces.internal.parsers;

import com.sun.org.apache.xerces.internal.impl.Constants;
import com.sun.org.apache.xerces.internal.impl.XMLDTDScannerImpl;
import com.sun.org.apache.xerces.internal.impl.XMLDocumentScannerImpl;
import com.sun.org.apache.xerces.internal.impl.XMLEntityManager;
import com.sun.org.apache.xerces.internal.impl.XMLErrorReporter;
import com.sun.org.apache.xerces.internal.impl.XMLNSDocumentScannerImpl;
import com.sun.org.apache.xerces.internal.impl.dv.DTDDVFactory;
import com.sun.org.apache.xerces.internal.impl.msg.XMLMessageFormatter;
import com.sun.org.apache.xerces.internal.impl.validation.ValidationManager;
import com.sun.org.apache.xerces.internal.util.FeatureState;
import com.sun.org.apache.xerces.internal.util.PropertyState;
import com.sun.org.apache.xerces.internal.util.SymbolTable;
import com.sun.org.apache.xerces.internal.utils.XMLSecurityPropertyManager;
import com.sun.org.apache.xerces.internal.xni.XMLLocator;
import com.sun.org.apache.xerces.internal.xni.XNIException;
import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool;
import com.sun.org.apache.xerces.internal.xni.parser.XMLComponent;
import com.sun.org.apache.xerces.internal.xni.parser.XMLComponentManager;
import com.sun.org.apache.xerces.internal.xni.parser.XMLConfigurationException;
import com.sun.org.apache.xerces.internal.xni.parser.XMLDTDScanner;
import com.sun.org.apache.xerces.internal.xni.parser.XMLDocumentScanner;
import com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;
import com.sun.org.apache.xerces.internal.xni.parser.XMLPullParserConfiguration;
import java.io.IOException;
import java.util.Locale;
import jdk.xml.internal.JdkXmlUtils;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/parsers/NonValidatingConfiguration.class */
public class NonValidatingConfiguration extends BasicParserConfiguration implements XMLPullParserConfiguration {
    protected static final String WARN_ON_DUPLICATE_ATTDEF = "http://apache.org/xml/features/validation/warn-on-duplicate-attdef";
    protected static final String WARN_ON_DUPLICATE_ENTITYDEF = "http://apache.org/xml/features/warn-on-duplicate-entitydef";
    protected static final String WARN_ON_UNDECLARED_ELEMDEF = "http://apache.org/xml/features/validation/warn-on-undeclared-elemdef";
    protected static final String ALLOW_JAVA_ENCODINGS = "http://apache.org/xml/features/allow-java-encodings";
    protected static final String CONTINUE_AFTER_FATAL_ERROR = "http://apache.org/xml/features/continue-after-fatal-error";
    protected static final String LOAD_EXTERNAL_DTD = "http://apache.org/xml/features/nonvalidating/load-external-dtd";
    protected static final String NOTIFY_BUILTIN_REFS = "http://apache.org/xml/features/scanner/notify-builtin-refs";
    protected static final String NOTIFY_CHAR_REFS = "http://apache.org/xml/features/scanner/notify-char-refs";
    protected static final String NORMALIZE_DATA = "http://apache.org/xml/features/validation/schema/normalized-value";
    protected static final String SCHEMA_ELEMENT_DEFAULT = "http://apache.org/xml/features/validation/schema/element-default";
    protected static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
    protected static final String ENTITY_MANAGER = "http://apache.org/xml/properties/internal/entity-manager";
    protected static final String DOCUMENT_SCANNER = "http://apache.org/xml/properties/internal/document-scanner";
    protected static final String DTD_SCANNER = "http://apache.org/xml/properties/internal/dtd-scanner";
    protected static final String XMLGRAMMAR_POOL = "http://apache.org/xml/properties/internal/grammar-pool";
    protected static final String DTD_VALIDATOR = "http://apache.org/xml/properties/internal/validator/dtd";
    protected static final String NAMESPACE_BINDER = "http://apache.org/xml/properties/internal/namespace-binder";
    protected static final String DATATYPE_VALIDATOR_FACTORY = "http://apache.org/xml/properties/internal/datatype-validator-factory";
    protected static final String VALIDATION_MANAGER = "http://apache.org/xml/properties/internal/validation-manager";
    protected static final String SCHEMA_VALIDATOR = "http://apache.org/xml/properties/internal/validator/schema";
    protected static final String LOCALE = "http://apache.org/xml/properties/locale";
    protected static final String XML_SECURITY_PROPERTY_MANAGER = "http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager";
    private static final String SECURITY_MANAGER = "http://apache.org/xml/properties/security-manager";
    private static final boolean PRINT_EXCEPTION_STACK_TRACE = false;
    protected XMLGrammarPool fGrammarPool;
    protected DTDDVFactory fDatatypeValidatorFactory;
    protected XMLErrorReporter fErrorReporter;
    protected XMLEntityManager fEntityManager;
    protected XMLDocumentScanner fScanner;
    protected XMLInputSource fInputSource;
    protected XMLDTDScanner fDTDScanner;
    protected ValidationManager fValidationManager;
    private XMLNSDocumentScannerImpl fNamespaceScanner;
    private XMLDocumentScannerImpl fNonNSScanner;
    protected boolean fConfigUpdated;
    protected XMLLocator fLocator;
    protected boolean fParseInProgress;

    public NonValidatingConfiguration() {
        this(null, null, null);
    }

    public NonValidatingConfiguration(SymbolTable symbolTable) {
        this(symbolTable, null, null);
    }

    public NonValidatingConfiguration(SymbolTable symbolTable, XMLGrammarPool grammarPool) {
        this(symbolTable, grammarPool, null);
    }

    public NonValidatingConfiguration(SymbolTable symbolTable, XMLGrammarPool grammarPool, XMLComponentManager parentSettings) throws XNIException {
        super(symbolTable, parentSettings);
        this.fConfigUpdated = false;
        this.fParseInProgress = false;
        String[] recognizedFeatures = {"http://apache.org/xml/features/internal/parser-settings", "http://xml.org/sax/features/namespaces", CONTINUE_AFTER_FATAL_ERROR, JdkXmlUtils.OVERRIDE_PARSER};
        addRecognizedFeatures(recognizedFeatures);
        this.fFeatures.put(CONTINUE_AFTER_FATAL_ERROR, Boolean.FALSE);
        this.fFeatures.put("http://apache.org/xml/features/internal/parser-settings", Boolean.TRUE);
        this.fFeatures.put("http://xml.org/sax/features/namespaces", Boolean.TRUE);
        this.fFeatures.put(JdkXmlUtils.OVERRIDE_PARSER, Boolean.valueOf(JdkXmlUtils.OVERRIDE_PARSER_DEFAULT));
        String[] recognizedProperties = {"http://apache.org/xml/properties/internal/error-reporter", ENTITY_MANAGER, DOCUMENT_SCANNER, DTD_SCANNER, DTD_VALIDATOR, NAMESPACE_BINDER, "http://apache.org/xml/properties/internal/grammar-pool", DATATYPE_VALIDATOR_FACTORY, VALIDATION_MANAGER, "http://apache.org/xml/properties/locale", "http://apache.org/xml/properties/security-manager", "http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager"};
        addRecognizedProperties(recognizedProperties);
        this.fGrammarPool = grammarPool;
        if (this.fGrammarPool != null) {
            this.fProperties.put("http://apache.org/xml/properties/internal/grammar-pool", this.fGrammarPool);
        }
        this.fEntityManager = createEntityManager();
        this.fProperties.put(ENTITY_MANAGER, this.fEntityManager);
        addComponent(this.fEntityManager);
        this.fErrorReporter = createErrorReporter();
        this.fErrorReporter.setDocumentLocator(this.fEntityManager.getEntityScanner());
        this.fProperties.put("http://apache.org/xml/properties/internal/error-reporter", this.fErrorReporter);
        addComponent(this.fErrorReporter);
        this.fDTDScanner = createDTDScanner();
        if (this.fDTDScanner != null) {
            this.fProperties.put(DTD_SCANNER, this.fDTDScanner);
            if (this.fDTDScanner instanceof XMLComponent) {
                addComponent((XMLComponent) this.fDTDScanner);
            }
        }
        this.fDatatypeValidatorFactory = createDatatypeValidatorFactory();
        if (this.fDatatypeValidatorFactory != null) {
            this.fProperties.put(DATATYPE_VALIDATOR_FACTORY, this.fDatatypeValidatorFactory);
        }
        this.fValidationManager = createValidationManager();
        if (this.fValidationManager != null) {
            this.fProperties.put(VALIDATION_MANAGER, this.fValidationManager);
        }
        if (this.fErrorReporter.getMessageFormatter("http://www.w3.org/TR/1998/REC-xml-19980210") == null) {
            XMLMessageFormatter xmft = new XMLMessageFormatter();
            this.fErrorReporter.putMessageFormatter("http://www.w3.org/TR/1998/REC-xml-19980210", xmft);
            this.fErrorReporter.putMessageFormatter("http://www.w3.org/TR/1999/REC-xml-names-19990114", xmft);
        }
        this.fConfigUpdated = false;
        try {
            setLocale(Locale.getDefault());
        } catch (XNIException e2) {
        }
        setProperty("http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager", new XMLSecurityPropertyManager());
    }

    @Override // com.sun.org.apache.xerces.internal.parsers.BasicParserConfiguration, com.sun.org.apache.xerces.internal.util.ParserConfigurationSettings, com.sun.org.apache.xerces.internal.xni.parser.XMLParserConfiguration
    public void setFeature(String featureId, boolean state) throws XMLConfigurationException {
        this.fConfigUpdated = true;
        super.setFeature(featureId, state);
    }

    @Override // com.sun.org.apache.xerces.internal.util.ParserConfigurationSettings, com.sun.org.apache.xerces.internal.xni.parser.XMLComponentManager
    public PropertyState getPropertyState(String propertyId) throws XMLConfigurationException {
        if ("http://apache.org/xml/properties/locale".equals(propertyId)) {
            return PropertyState.is(getLocale());
        }
        return super.getPropertyState(propertyId);
    }

    @Override // com.sun.org.apache.xerces.internal.parsers.BasicParserConfiguration, com.sun.org.apache.xerces.internal.util.ParserConfigurationSettings, com.sun.org.apache.xerces.internal.xni.parser.XMLParserConfiguration
    public void setProperty(String propertyId, Object value) throws XNIException {
        this.fConfigUpdated = true;
        if ("http://apache.org/xml/properties/locale".equals(propertyId)) {
            setLocale((Locale) value);
        }
        super.setProperty(propertyId, value);
    }

    @Override // com.sun.org.apache.xerces.internal.parsers.BasicParserConfiguration, com.sun.org.apache.xerces.internal.xni.parser.XMLParserConfiguration
    public void setLocale(Locale locale) throws XNIException {
        super.setLocale(locale);
        this.fErrorReporter.setLocale(locale);
    }

    @Override // com.sun.org.apache.xerces.internal.util.ParserConfigurationSettings, com.sun.org.apache.xerces.internal.xni.parser.XMLComponentManager
    public FeatureState getFeatureState(String featureId) throws XMLConfigurationException {
        if (featureId.equals("http://apache.org/xml/features/internal/parser-settings")) {
            return FeatureState.is(this.fConfigUpdated);
        }
        return super.getFeatureState(featureId);
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLPullParserConfiguration
    public void setInputSource(XMLInputSource inputSource) throws IOException, XMLConfigurationException {
        this.fInputSource = inputSource;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLPullParserConfiguration
    public boolean parse(boolean complete) throws IOException, XNIException {
        if (this.fInputSource != null) {
            try {
                reset();
                this.fScanner.setInputSource(this.fInputSource);
                this.fInputSource = null;
            } catch (XNIException ex) {
                throw ex;
            } catch (IOException ex2) {
                throw ex2;
            } catch (RuntimeException ex3) {
                throw ex3;
            } catch (Exception ex4) {
                throw new XNIException(ex4);
            }
        }
        try {
            return this.fScanner.scanDocument(complete);
        } catch (XNIException ex5) {
            throw ex5;
        } catch (IOException ex6) {
            throw ex6;
        } catch (RuntimeException ex7) {
            throw ex7;
        } catch (Exception ex8) {
            throw new XNIException(ex8);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLPullParserConfiguration
    public void cleanup() {
        this.fEntityManager.closeReaders();
    }

    @Override // com.sun.org.apache.xerces.internal.parsers.BasicParserConfiguration, com.sun.org.apache.xerces.internal.xni.parser.XMLParserConfiguration
    public void parse(XMLInputSource source) throws IOException, XNIException {
        if (this.fParseInProgress) {
            throw new XNIException("FWK005 parse may not be called while parsing.");
        }
        this.fParseInProgress = true;
        try {
            try {
                try {
                    try {
                        try {
                            setInputSource(source);
                            parse(true);
                            this.fParseInProgress = false;
                            cleanup();
                        } catch (IOException ex) {
                            throw ex;
                        }
                    } catch (Exception ex2) {
                        throw new XNIException(ex2);
                    }
                } catch (RuntimeException ex3) {
                    throw ex3;
                }
            } catch (XNIException ex4) {
                throw ex4;
            }
        } catch (Throwable th) {
            this.fParseInProgress = false;
            cleanup();
            throw th;
        }
    }

    @Override // com.sun.org.apache.xerces.internal.parsers.BasicParserConfiguration
    protected void reset() throws XNIException {
        if (this.fValidationManager != null) {
            this.fValidationManager.reset();
        }
        configurePipeline();
        super.reset();
    }

    protected void configurePipeline() {
        if (this.fFeatures.get("http://xml.org/sax/features/namespaces") == Boolean.TRUE) {
            if (this.fNamespaceScanner == null) {
                this.fNamespaceScanner = new XMLNSDocumentScannerImpl();
                addComponent(this.fNamespaceScanner);
            }
            this.fProperties.put(DOCUMENT_SCANNER, this.fNamespaceScanner);
            this.fNamespaceScanner.setDTDValidator(null);
            this.fScanner = this.fNamespaceScanner;
        } else {
            if (this.fNonNSScanner == null) {
                this.fNonNSScanner = new XMLDocumentScannerImpl();
                addComponent(this.fNonNSScanner);
            }
            this.fProperties.put(DOCUMENT_SCANNER, this.fNonNSScanner);
            this.fScanner = this.fNonNSScanner;
        }
        this.fScanner.setDocumentHandler(this.fDocumentHandler);
        this.fLastComponent = this.fScanner;
        if (this.fDTDScanner != null) {
            this.fDTDScanner.setDTDHandler(this.fDTDHandler);
            this.fDTDScanner.setDTDContentModelHandler(this.fDTDContentModelHandler);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.parsers.BasicParserConfiguration, com.sun.org.apache.xerces.internal.util.ParserConfigurationSettings
    protected FeatureState checkFeature(String featureId) throws XMLConfigurationException {
        if (featureId.startsWith(Constants.XERCES_FEATURE_PREFIX)) {
            int suffixLength = featureId.length() - Constants.XERCES_FEATURE_PREFIX.length();
            if (suffixLength == Constants.DYNAMIC_VALIDATION_FEATURE.length() && featureId.endsWith(Constants.DYNAMIC_VALIDATION_FEATURE)) {
                return FeatureState.RECOGNIZED;
            }
            if (suffixLength == Constants.DEFAULT_ATTRIBUTE_VALUES_FEATURE.length() && featureId.endsWith(Constants.DEFAULT_ATTRIBUTE_VALUES_FEATURE)) {
                return FeatureState.NOT_SUPPORTED;
            }
            if (suffixLength == Constants.VALIDATE_CONTENT_MODELS_FEATURE.length() && featureId.endsWith(Constants.VALIDATE_CONTENT_MODELS_FEATURE)) {
                return FeatureState.NOT_SUPPORTED;
            }
            if (suffixLength == Constants.LOAD_DTD_GRAMMAR_FEATURE.length() && featureId.endsWith(Constants.LOAD_DTD_GRAMMAR_FEATURE)) {
                return FeatureState.RECOGNIZED;
            }
            if (suffixLength == Constants.LOAD_EXTERNAL_DTD_FEATURE.length() && featureId.endsWith(Constants.LOAD_EXTERNAL_DTD_FEATURE)) {
                return FeatureState.RECOGNIZED;
            }
            if (suffixLength == Constants.VALIDATE_DATATYPES_FEATURE.length() && featureId.endsWith(Constants.VALIDATE_DATATYPES_FEATURE)) {
                return FeatureState.NOT_SUPPORTED;
            }
        }
        return super.checkFeature(featureId);
    }

    @Override // com.sun.org.apache.xerces.internal.parsers.BasicParserConfiguration, com.sun.org.apache.xerces.internal.util.ParserConfigurationSettings
    protected PropertyState checkProperty(String propertyId) throws XMLConfigurationException {
        if (propertyId.startsWith(Constants.XERCES_PROPERTY_PREFIX)) {
            int suffixLength = propertyId.length() - Constants.XERCES_PROPERTY_PREFIX.length();
            if (suffixLength == Constants.DTD_SCANNER_PROPERTY.length() && propertyId.endsWith(Constants.DTD_SCANNER_PROPERTY)) {
                return PropertyState.RECOGNIZED;
            }
        }
        if (propertyId.startsWith(Constants.JAXP_PROPERTY_PREFIX)) {
            int suffixLength2 = propertyId.length() - Constants.JAXP_PROPERTY_PREFIX.length();
            if (suffixLength2 == Constants.SCHEMA_SOURCE.length() && propertyId.endsWith(Constants.SCHEMA_SOURCE)) {
                return PropertyState.RECOGNIZED;
            }
        }
        return super.checkProperty(propertyId);
    }

    protected XMLEntityManager createEntityManager() {
        return new XMLEntityManager();
    }

    protected XMLErrorReporter createErrorReporter() {
        return new XMLErrorReporter();
    }

    protected XMLDocumentScanner createDocumentScanner() {
        return null;
    }

    protected XMLDTDScanner createDTDScanner() {
        return new XMLDTDScannerImpl();
    }

    protected DTDDVFactory createDatatypeValidatorFactory() {
        return DTDDVFactory.getInstance();
    }

    protected ValidationManager createValidationManager() {
        return new ValidationManager();
    }
}
