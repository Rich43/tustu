package com.sun.org.apache.xerces.internal.impl.xs.opti;

import com.sun.org.apache.xerces.internal.impl.Constants;
import com.sun.org.apache.xerces.internal.impl.XML11DTDScannerImpl;
import com.sun.org.apache.xerces.internal.impl.XML11NSDocumentScannerImpl;
import com.sun.org.apache.xerces.internal.impl.XMLDTDScannerImpl;
import com.sun.org.apache.xerces.internal.impl.XMLEntityHandler;
import com.sun.org.apache.xerces.internal.impl.XMLEntityManager;
import com.sun.org.apache.xerces.internal.impl.XMLErrorReporter;
import com.sun.org.apache.xerces.internal.impl.XMLNSDocumentScannerImpl;
import com.sun.org.apache.xerces.internal.impl.XMLVersionDetector;
import com.sun.org.apache.xerces.internal.impl.dv.DTDDVFactory;
import com.sun.org.apache.xerces.internal.impl.msg.XMLMessageFormatter;
import com.sun.org.apache.xerces.internal.impl.validation.ValidationManager;
import com.sun.org.apache.xerces.internal.impl.xs.XSMessageFormatter;
import com.sun.org.apache.xerces.internal.parsers.BasicParserConfiguration;
import com.sun.org.apache.xerces.internal.util.FeatureState;
import com.sun.org.apache.xerces.internal.util.PropertyState;
import com.sun.org.apache.xerces.internal.util.SymbolTable;
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

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/opti/SchemaParsingConfig.class */
public class SchemaParsingConfig extends BasicParserConfiguration implements XMLPullParserConfiguration {
    protected static final String XML11_DATATYPE_VALIDATOR_FACTORY = "com.sun.org.apache.xerces.internal.impl.dv.dtd.XML11DTDDVFactoryImpl";
    protected static final String WARN_ON_DUPLICATE_ATTDEF = "http://apache.org/xml/features/validation/warn-on-duplicate-attdef";
    protected static final String WARN_ON_UNDECLARED_ELEMDEF = "http://apache.org/xml/features/validation/warn-on-undeclared-elemdef";
    protected static final String ALLOW_JAVA_ENCODINGS = "http://apache.org/xml/features/allow-java-encodings";
    protected static final String CONTINUE_AFTER_FATAL_ERROR = "http://apache.org/xml/features/continue-after-fatal-error";
    protected static final String LOAD_EXTERNAL_DTD = "http://apache.org/xml/features/nonvalidating/load-external-dtd";
    protected static final String NOTIFY_BUILTIN_REFS = "http://apache.org/xml/features/scanner/notify-builtin-refs";
    protected static final String NOTIFY_CHAR_REFS = "http://apache.org/xml/features/scanner/notify-char-refs";
    protected static final String NORMALIZE_DATA = "http://apache.org/xml/features/validation/schema/normalized-value";
    protected static final String SCHEMA_ELEMENT_DEFAULT = "http://apache.org/xml/features/validation/schema/element-default";
    protected static final String GENERATE_SYNTHETIC_ANNOTATIONS = "http://apache.org/xml/features/generate-synthetic-annotations";
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
    private static final boolean PRINT_EXCEPTION_STACK_TRACE = false;
    protected final DTDDVFactory fDatatypeValidatorFactory;
    protected final XMLNSDocumentScannerImpl fNamespaceScanner;
    protected final XMLDTDScannerImpl fDTDScanner;
    protected DTDDVFactory fXML11DatatypeFactory;
    protected XML11NSDocumentScannerImpl fXML11NSDocScanner;
    protected XML11DTDScannerImpl fXML11DTDScanner;
    protected DTDDVFactory fCurrentDVFactory;
    protected XMLDocumentScanner fCurrentScanner;
    protected XMLDTDScanner fCurrentDTDScanner;
    protected XMLGrammarPool fGrammarPool;
    protected final XMLVersionDetector fVersionDetector;
    protected final XMLErrorReporter fErrorReporter;
    protected final XMLEntityManager fEntityManager;
    protected XMLInputSource fInputSource;
    protected final ValidationManager fValidationManager;
    protected XMLLocator fLocator;
    protected boolean fParseInProgress;
    protected boolean fConfigUpdated;
    private boolean f11Initialized;

    public SchemaParsingConfig() {
        this(null, null, null);
    }

    public SchemaParsingConfig(SymbolTable symbolTable) {
        this(symbolTable, null, null);
    }

    public SchemaParsingConfig(SymbolTable symbolTable, XMLGrammarPool grammarPool) {
        this(symbolTable, grammarPool, null);
    }

    public SchemaParsingConfig(SymbolTable symbolTable, XMLGrammarPool grammarPool, XMLComponentManager parentSettings) throws XNIException {
        super(symbolTable, parentSettings);
        this.fXML11DatatypeFactory = null;
        this.fXML11NSDocScanner = null;
        this.fXML11DTDScanner = null;
        this.fParseInProgress = false;
        this.fConfigUpdated = false;
        this.f11Initialized = false;
        String[] recognizedFeatures = {"http://apache.org/xml/features/internal/parser-settings", WARN_ON_DUPLICATE_ATTDEF, WARN_ON_UNDECLARED_ELEMDEF, ALLOW_JAVA_ENCODINGS, CONTINUE_AFTER_FATAL_ERROR, LOAD_EXTERNAL_DTD, NOTIFY_BUILTIN_REFS, NOTIFY_CHAR_REFS, "http://apache.org/xml/features/generate-synthetic-annotations", JdkXmlUtils.OVERRIDE_PARSER};
        addRecognizedFeatures(recognizedFeatures);
        this.fFeatures.put("http://apache.org/xml/features/internal/parser-settings", Boolean.TRUE);
        this.fFeatures.put(WARN_ON_DUPLICATE_ATTDEF, Boolean.FALSE);
        this.fFeatures.put(WARN_ON_UNDECLARED_ELEMDEF, Boolean.FALSE);
        this.fFeatures.put(ALLOW_JAVA_ENCODINGS, Boolean.FALSE);
        this.fFeatures.put(CONTINUE_AFTER_FATAL_ERROR, Boolean.FALSE);
        this.fFeatures.put(LOAD_EXTERNAL_DTD, Boolean.TRUE);
        this.fFeatures.put(NOTIFY_BUILTIN_REFS, Boolean.FALSE);
        this.fFeatures.put(NOTIFY_CHAR_REFS, Boolean.FALSE);
        this.fFeatures.put("http://apache.org/xml/features/generate-synthetic-annotations", Boolean.FALSE);
        this.fFeatures.put(JdkXmlUtils.OVERRIDE_PARSER, Boolean.valueOf(JdkXmlUtils.OVERRIDE_PARSER_DEFAULT));
        String[] recognizedProperties = {"http://apache.org/xml/properties/internal/error-reporter", ENTITY_MANAGER, DOCUMENT_SCANNER, DTD_SCANNER, DTD_VALIDATOR, NAMESPACE_BINDER, "http://apache.org/xml/properties/internal/grammar-pool", DATATYPE_VALIDATOR_FACTORY, VALIDATION_MANAGER, "http://apache.org/xml/features/generate-synthetic-annotations", "http://apache.org/xml/properties/locale"};
        addRecognizedProperties(recognizedProperties);
        this.fGrammarPool = grammarPool;
        if (this.fGrammarPool != null) {
            setProperty("http://apache.org/xml/properties/internal/grammar-pool", this.fGrammarPool);
        }
        this.fEntityManager = new XMLEntityManager();
        this.fProperties.put(ENTITY_MANAGER, this.fEntityManager);
        addComponent(this.fEntityManager);
        this.fErrorReporter = new XMLErrorReporter();
        this.fErrorReporter.setDocumentLocator(this.fEntityManager.getEntityScanner());
        this.fProperties.put("http://apache.org/xml/properties/internal/error-reporter", this.fErrorReporter);
        addComponent(this.fErrorReporter);
        this.fNamespaceScanner = new XMLNSDocumentScannerImpl();
        this.fProperties.put(DOCUMENT_SCANNER, this.fNamespaceScanner);
        addRecognizedParamsAndSetDefaults(this.fNamespaceScanner);
        this.fDTDScanner = new XMLDTDScannerImpl();
        this.fProperties.put(DTD_SCANNER, this.fDTDScanner);
        addRecognizedParamsAndSetDefaults(this.fDTDScanner);
        this.fDatatypeValidatorFactory = DTDDVFactory.getInstance();
        this.fProperties.put(DATATYPE_VALIDATOR_FACTORY, this.fDatatypeValidatorFactory);
        this.fValidationManager = new ValidationManager();
        this.fProperties.put(VALIDATION_MANAGER, this.fValidationManager);
        this.fVersionDetector = new XMLVersionDetector();
        if (this.fErrorReporter.getMessageFormatter("http://www.w3.org/TR/1998/REC-xml-19980210") == null) {
            XMLMessageFormatter xmft = new XMLMessageFormatter();
            this.fErrorReporter.putMessageFormatter("http://www.w3.org/TR/1998/REC-xml-19980210", xmft);
            this.fErrorReporter.putMessageFormatter("http://www.w3.org/TR/1999/REC-xml-names-19990114", xmft);
        }
        if (this.fErrorReporter.getMessageFormatter(XSMessageFormatter.SCHEMA_DOMAIN) == null) {
            this.fErrorReporter.putMessageFormatter(XSMessageFormatter.SCHEMA_DOMAIN, new XSMessageFormatter());
        }
        try {
            setLocale(Locale.getDefault());
        } catch (XNIException e2) {
        }
    }

    @Override // com.sun.org.apache.xerces.internal.util.ParserConfigurationSettings, com.sun.org.apache.xerces.internal.xni.parser.XMLComponentManager
    public FeatureState getFeatureState(String featureId) throws XMLConfigurationException {
        if (featureId.equals("http://apache.org/xml/features/internal/parser-settings")) {
            return FeatureState.is(this.fConfigUpdated);
        }
        return super.getFeatureState(featureId);
    }

    @Override // com.sun.org.apache.xerces.internal.parsers.BasicParserConfiguration, com.sun.org.apache.xerces.internal.util.ParserConfigurationSettings, com.sun.org.apache.xerces.internal.xni.parser.XMLParserConfiguration
    public void setFeature(String featureId, boolean state) throws XMLConfigurationException {
        this.fConfigUpdated = true;
        this.fNamespaceScanner.setFeature(featureId, state);
        this.fDTDScanner.setFeature(featureId, state);
        if (this.f11Initialized) {
            try {
                this.fXML11DTDScanner.setFeature(featureId, state);
            } catch (Exception e2) {
            }
            try {
                this.fXML11NSDocScanner.setFeature(featureId, state);
            } catch (Exception e3) {
            }
        }
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
        this.fNamespaceScanner.setProperty(propertyId, value);
        this.fDTDScanner.setProperty(propertyId, value);
        if (this.f11Initialized) {
            try {
                this.fXML11DTDScanner.setProperty(propertyId, value);
            } catch (Exception e2) {
            }
            try {
                this.fXML11NSDocScanner.setProperty(propertyId, value);
            } catch (Exception e3) {
            }
        }
        super.setProperty(propertyId, value);
    }

    @Override // com.sun.org.apache.xerces.internal.parsers.BasicParserConfiguration, com.sun.org.apache.xerces.internal.xni.parser.XMLParserConfiguration
    public void setLocale(Locale locale) throws XNIException {
        super.setLocale(locale);
        this.fErrorReporter.setLocale(locale);
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLPullParserConfiguration
    public void setInputSource(XMLInputSource inputSource) throws IOException, XMLConfigurationException {
        this.fInputSource = inputSource;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLPullParserConfiguration
    public boolean parse(boolean complete) throws IOException, XNIException {
        if (this.fInputSource != null) {
            try {
                this.fValidationManager.reset();
                this.fVersionDetector.reset(this);
                reset();
                short version = this.fVersionDetector.determineDocVersion(this.fInputSource);
                if (version == 1) {
                    configurePipeline();
                    resetXML10();
                } else if (version == 2) {
                    initXML11Components();
                    configureXML11Pipeline();
                    resetXML11();
                } else {
                    return false;
                }
                this.fConfigUpdated = false;
                this.fVersionDetector.startDocumentParsing((XMLEntityHandler) this.fCurrentScanner, version);
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
            return this.fCurrentScanner.scanDocument(complete);
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
    public void reset() throws XNIException {
        super.reset();
    }

    protected void configurePipeline() throws XNIException {
        if (this.fCurrentDVFactory != this.fDatatypeValidatorFactory) {
            this.fCurrentDVFactory = this.fDatatypeValidatorFactory;
            setProperty(DATATYPE_VALIDATOR_FACTORY, this.fCurrentDVFactory);
        }
        if (this.fCurrentScanner != this.fNamespaceScanner) {
            this.fCurrentScanner = this.fNamespaceScanner;
            setProperty(DOCUMENT_SCANNER, this.fCurrentScanner);
        }
        this.fNamespaceScanner.setDocumentHandler(this.fDocumentHandler);
        if (this.fDocumentHandler != null) {
            this.fDocumentHandler.setDocumentSource(this.fNamespaceScanner);
        }
        this.fLastComponent = this.fNamespaceScanner;
        if (this.fCurrentDTDScanner != this.fDTDScanner) {
            this.fCurrentDTDScanner = this.fDTDScanner;
            setProperty(DTD_SCANNER, this.fCurrentDTDScanner);
        }
        this.fDTDScanner.setDTDHandler(this.fDTDHandler);
        if (this.fDTDHandler != null) {
            this.fDTDHandler.setDTDSource(this.fDTDScanner);
        }
        this.fDTDScanner.setDTDContentModelHandler(this.fDTDContentModelHandler);
        if (this.fDTDContentModelHandler != null) {
            this.fDTDContentModelHandler.setDTDContentModelSource(this.fDTDScanner);
        }
    }

    protected void configureXML11Pipeline() throws XNIException {
        if (this.fCurrentDVFactory != this.fXML11DatatypeFactory) {
            this.fCurrentDVFactory = this.fXML11DatatypeFactory;
            setProperty(DATATYPE_VALIDATOR_FACTORY, this.fCurrentDVFactory);
        }
        if (this.fCurrentScanner != this.fXML11NSDocScanner) {
            this.fCurrentScanner = this.fXML11NSDocScanner;
            setProperty(DOCUMENT_SCANNER, this.fCurrentScanner);
        }
        this.fXML11NSDocScanner.setDocumentHandler(this.fDocumentHandler);
        if (this.fDocumentHandler != null) {
            this.fDocumentHandler.setDocumentSource(this.fXML11NSDocScanner);
        }
        this.fLastComponent = this.fXML11NSDocScanner;
        if (this.fCurrentDTDScanner != this.fXML11DTDScanner) {
            this.fCurrentDTDScanner = this.fXML11DTDScanner;
            setProperty(DTD_SCANNER, this.fCurrentDTDScanner);
        }
        this.fXML11DTDScanner.setDTDHandler(this.fDTDHandler);
        if (this.fDTDHandler != null) {
            this.fDTDHandler.setDTDSource(this.fXML11DTDScanner);
        }
        this.fXML11DTDScanner.setDTDContentModelHandler(this.fDTDContentModelHandler);
        if (this.fDTDContentModelHandler != null) {
            this.fDTDContentModelHandler.setDTDContentModelSource(this.fXML11DTDScanner);
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

    private void addRecognizedParamsAndSetDefaults(XMLComponent component) {
        String[] recognizedFeatures = component.getRecognizedFeatures();
        addRecognizedFeatures(recognizedFeatures);
        String[] recognizedProperties = component.getRecognizedProperties();
        addRecognizedProperties(recognizedProperties);
        if (recognizedFeatures != null) {
            for (String featureId : recognizedFeatures) {
                Boolean state = component.getFeatureDefault(featureId);
                if (state != null && !this.fFeatures.containsKey(featureId)) {
                    this.fFeatures.put(featureId, state);
                    this.fConfigUpdated = true;
                }
            }
        }
        if (recognizedProperties != null) {
            for (String propertyId : recognizedProperties) {
                Object value = component.getPropertyDefault(propertyId);
                if (value != null && !this.fProperties.containsKey(propertyId)) {
                    this.fProperties.put(propertyId, value);
                    this.fConfigUpdated = true;
                }
            }
        }
    }

    protected final void resetXML10() throws XNIException {
        this.fNamespaceScanner.reset(this);
        this.fDTDScanner.reset(this);
    }

    protected final void resetXML11() throws XNIException {
        this.fXML11NSDocScanner.reset(this);
        this.fXML11DTDScanner.reset(this);
    }

    public void resetNodePool() {
    }

    private void initXML11Components() {
        if (!this.f11Initialized) {
            this.fXML11DatatypeFactory = DTDDVFactory.getInstance(XML11_DATATYPE_VALIDATOR_FACTORY);
            this.fXML11DTDScanner = new XML11DTDScannerImpl();
            addRecognizedParamsAndSetDefaults(this.fXML11DTDScanner);
            this.fXML11NSDocScanner = new XML11NSDocumentScannerImpl();
            addRecognizedParamsAndSetDefaults(this.fXML11NSDocScanner);
            this.f11Initialized = true;
        }
    }
}
