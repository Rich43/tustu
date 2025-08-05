package com.sun.org.apache.xerces.internal.jaxp.validation;

import com.sun.org.apache.xerces.internal.impl.Constants;
import com.sun.org.apache.xerces.internal.impl.XMLEntityManager;
import com.sun.org.apache.xerces.internal.impl.XMLErrorReporter;
import com.sun.org.apache.xerces.internal.impl.validation.ValidationManager;
import com.sun.org.apache.xerces.internal.impl.xs.XMLSchemaValidator;
import com.sun.org.apache.xerces.internal.impl.xs.XSMessageFormatter;
import com.sun.org.apache.xerces.internal.util.DOMEntityResolverWrapper;
import com.sun.org.apache.xerces.internal.util.ErrorHandlerWrapper;
import com.sun.org.apache.xerces.internal.util.FeatureState;
import com.sun.org.apache.xerces.internal.util.NamespaceSupport;
import com.sun.org.apache.xerces.internal.util.ParserConfigurationSettings;
import com.sun.org.apache.xerces.internal.util.PropertyState;
import com.sun.org.apache.xerces.internal.util.Status;
import com.sun.org.apache.xerces.internal.util.SymbolTable;
import com.sun.org.apache.xerces.internal.utils.XMLSecurityManager;
import com.sun.org.apache.xerces.internal.utils.XMLSecurityPropertyManager;
import com.sun.org.apache.xerces.internal.xni.NamespaceContext;
import com.sun.org.apache.xerces.internal.xni.XNIException;
import com.sun.org.apache.xerces.internal.xni.parser.XMLComponent;
import com.sun.org.apache.xerces.internal.xni.parser.XMLComponentManager;
import com.sun.org.apache.xerces.internal.xni.parser.XMLConfigurationException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.ErrorHandler;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/jaxp/validation/XMLSchemaValidatorComponentManager.class */
final class XMLSchemaValidatorComponentManager extends ParserConfigurationSettings implements XMLComponentManager {
    private static final String SCHEMA_VALIDATION = "http://apache.org/xml/features/validation/schema";
    private static final String VALIDATION = "http://xml.org/sax/features/validation";
    private static final String SCHEMA_ELEMENT_DEFAULT = "http://apache.org/xml/features/validation/schema/element-default";
    private static final String USE_GRAMMAR_POOL_ONLY = "http://apache.org/xml/features/internal/validation/schema/use-grammar-pool-only";
    private static final String ENTITY_MANAGER = "http://apache.org/xml/properties/internal/entity-manager";
    private static final String ENTITY_RESOLVER = "http://apache.org/xml/properties/internal/entity-resolver";
    private static final String ERROR_HANDLER = "http://apache.org/xml/properties/internal/error-handler";
    private static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
    private static final String NAMESPACE_CONTEXT = "http://apache.org/xml/properties/internal/namespace-context";
    private static final String SCHEMA_VALIDATOR = "http://apache.org/xml/properties/internal/validator/schema";
    private static final String SECURITY_MANAGER = "http://apache.org/xml/properties/security-manager";
    private static final String XML_SECURITY_PROPERTY_MANAGER = "http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager";
    private static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
    private static final String VALIDATION_MANAGER = "http://apache.org/xml/properties/internal/validation-manager";
    private static final String XMLGRAMMAR_POOL = "http://apache.org/xml/properties/internal/grammar-pool";
    private static final String LOCALE = "http://apache.org/xml/properties/locale";
    private boolean _isSecureMode;
    private boolean fUseGrammarPoolOnly;
    private XMLErrorReporter fErrorReporter;
    private NamespaceContext fNamespaceContext;
    private XMLSchemaValidator fSchemaValidator;
    private ValidationManager fValidationManager;
    private XMLSecurityManager fInitSecurityManager;
    private final XMLSecurityPropertyManager fSecurityPropertyMgr;
    private boolean fConfigUpdated = true;
    private final HashMap fComponents = new HashMap();
    private final HashMap fInitFeatures = new HashMap();
    private final HashMap fInitProperties = new HashMap();
    private ErrorHandler fErrorHandler = null;
    private LSResourceResolver fResourceResolver = null;
    private Locale fLocale = null;
    private XMLEntityManager fEntityManager = new XMLEntityManager();

    public XMLSchemaValidatorComponentManager(XSGrammarPoolContainer grammarContainer) throws XMLConfigurationException {
        this._isSecureMode = false;
        this.fComponents.put(ENTITY_MANAGER, this.fEntityManager);
        this.fErrorReporter = new XMLErrorReporter();
        this.fComponents.put("http://apache.org/xml/properties/internal/error-reporter", this.fErrorReporter);
        this.fNamespaceContext = new NamespaceSupport();
        this.fComponents.put(NAMESPACE_CONTEXT, this.fNamespaceContext);
        this.fSchemaValidator = new XMLSchemaValidator();
        this.fComponents.put(SCHEMA_VALIDATOR, this.fSchemaValidator);
        this.fValidationManager = new ValidationManager();
        this.fComponents.put(VALIDATION_MANAGER, this.fValidationManager);
        this.fComponents.put("http://apache.org/xml/properties/internal/entity-resolver", null);
        this.fComponents.put(ERROR_HANDLER, null);
        this.fComponents.put("http://apache.org/xml/properties/internal/symbol-table", new SymbolTable());
        this.fComponents.put("http://apache.org/xml/properties/internal/grammar-pool", grammarContainer.getGrammarPool());
        this.fUseGrammarPoolOnly = grammarContainer.isFullyComposed();
        this.fErrorReporter.putMessageFormatter(XSMessageFormatter.SCHEMA_DOMAIN, new XSMessageFormatter());
        addRecognizedParamsAndSetDefaults(this.fEntityManager, grammarContainer);
        addRecognizedParamsAndSetDefaults(this.fErrorReporter, grammarContainer);
        addRecognizedParamsAndSetDefaults(this.fSchemaValidator, grammarContainer);
        boolean secureProcessing = grammarContainer.getFeature("http://javax.xml.XMLConstants/feature/secure-processing").booleanValue();
        if (System.getSecurityManager() != null) {
            this._isSecureMode = true;
            secureProcessing = true;
        }
        this.fInitSecurityManager = (XMLSecurityManager) grammarContainer.getProperty("http://apache.org/xml/properties/security-manager");
        if (this.fInitSecurityManager != null) {
            this.fInitSecurityManager.setSecureProcessing(secureProcessing);
        } else {
            this.fInitSecurityManager = new XMLSecurityManager(secureProcessing);
        }
        setProperty("http://apache.org/xml/properties/security-manager", this.fInitSecurityManager);
        this.fSecurityPropertyMgr = (XMLSecurityPropertyManager) grammarContainer.getProperty("http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager");
        setProperty("http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager", this.fSecurityPropertyMgr);
    }

    @Override // com.sun.org.apache.xerces.internal.util.ParserConfigurationSettings, com.sun.org.apache.xerces.internal.xni.parser.XMLComponentManager
    public FeatureState getFeatureState(String featureId) throws XMLConfigurationException {
        if ("http://apache.org/xml/features/internal/parser-settings".equals(featureId)) {
            return FeatureState.is(this.fConfigUpdated);
        }
        if (VALIDATION.equals(featureId) || SCHEMA_VALIDATION.equals(featureId)) {
            return FeatureState.is(true);
        }
        if (USE_GRAMMAR_POOL_ONLY.equals(featureId)) {
            return FeatureState.is(this.fUseGrammarPoolOnly);
        }
        if ("http://javax.xml.XMLConstants/feature/secure-processing".equals(featureId)) {
            return FeatureState.is(this.fInitSecurityManager.isSecureProcessing());
        }
        if (SCHEMA_ELEMENT_DEFAULT.equals(featureId)) {
            return FeatureState.is(true);
        }
        return super.getFeatureState(featureId);
    }

    @Override // com.sun.org.apache.xerces.internal.util.ParserConfigurationSettings, com.sun.org.apache.xerces.internal.xni.parser.XMLParserConfiguration
    public void setFeature(String featureId, boolean value) throws XMLConfigurationException {
        if ("http://apache.org/xml/features/internal/parser-settings".equals(featureId)) {
            throw new XMLConfigurationException(Status.NOT_SUPPORTED, featureId);
        }
        if (!value && (VALIDATION.equals(featureId) || SCHEMA_VALIDATION.equals(featureId))) {
            throw new XMLConfigurationException(Status.NOT_SUPPORTED, featureId);
        }
        if (USE_GRAMMAR_POOL_ONLY.equals(featureId) && value != this.fUseGrammarPoolOnly) {
            throw new XMLConfigurationException(Status.NOT_SUPPORTED, featureId);
        }
        if ("http://javax.xml.XMLConstants/feature/secure-processing".equals(featureId)) {
            if (this._isSecureMode && !value) {
                throw new XMLConfigurationException(Status.NOT_ALLOWED, "http://javax.xml.XMLConstants/feature/secure-processing");
            }
            this.fInitSecurityManager.setSecureProcessing(value);
            setProperty("http://apache.org/xml/properties/security-manager", this.fInitSecurityManager);
            if (value && Constants.IS_JDK8_OR_ABOVE) {
                this.fSecurityPropertyMgr.setValue(XMLSecurityPropertyManager.Property.ACCESS_EXTERNAL_DTD, XMLSecurityPropertyManager.State.FSP, "");
                this.fSecurityPropertyMgr.setValue(XMLSecurityPropertyManager.Property.ACCESS_EXTERNAL_SCHEMA, XMLSecurityPropertyManager.State.FSP, "");
                setProperty("http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager", this.fSecurityPropertyMgr);
                return;
            }
            return;
        }
        this.fConfigUpdated = true;
        this.fEntityManager.setFeature(featureId, value);
        this.fErrorReporter.setFeature(featureId, value);
        this.fSchemaValidator.setFeature(featureId, value);
        if (!this.fInitFeatures.containsKey(featureId)) {
            boolean current = super.getFeature(featureId);
            this.fInitFeatures.put(featureId, current ? Boolean.TRUE : Boolean.FALSE);
        }
        super.setFeature(featureId, value);
    }

    @Override // com.sun.org.apache.xerces.internal.util.ParserConfigurationSettings, com.sun.org.apache.xerces.internal.xni.parser.XMLComponentManager
    public PropertyState getPropertyState(String propertyId) throws XMLConfigurationException {
        if ("http://apache.org/xml/properties/locale".equals(propertyId)) {
            return PropertyState.is(getLocale());
        }
        Object component = this.fComponents.get(propertyId);
        if (component != null) {
            return PropertyState.is(component);
        }
        if (this.fComponents.containsKey(propertyId)) {
            return PropertyState.is(null);
        }
        return super.getPropertyState(propertyId);
    }

    @Override // com.sun.org.apache.xerces.internal.util.ParserConfigurationSettings, com.sun.org.apache.xerces.internal.xni.parser.XMLParserConfiguration
    public void setProperty(String propertyId, Object value) throws XMLConfigurationException {
        if (ENTITY_MANAGER.equals(propertyId) || "http://apache.org/xml/properties/internal/error-reporter".equals(propertyId) || NAMESPACE_CONTEXT.equals(propertyId) || SCHEMA_VALIDATOR.equals(propertyId) || "http://apache.org/xml/properties/internal/symbol-table".equals(propertyId) || VALIDATION_MANAGER.equals(propertyId) || "http://apache.org/xml/properties/internal/grammar-pool".equals(propertyId)) {
            throw new XMLConfigurationException(Status.NOT_SUPPORTED, propertyId);
        }
        this.fConfigUpdated = true;
        this.fEntityManager.setProperty(propertyId, value);
        this.fErrorReporter.setProperty(propertyId, value);
        this.fSchemaValidator.setProperty(propertyId, value);
        if ("http://apache.org/xml/properties/internal/entity-resolver".equals(propertyId) || ERROR_HANDLER.equals(propertyId) || "http://apache.org/xml/properties/security-manager".equals(propertyId)) {
            this.fComponents.put(propertyId, value);
            return;
        }
        if ("http://apache.org/xml/properties/locale".equals(propertyId)) {
            setLocale((Locale) value);
            this.fComponents.put(propertyId, value);
        } else if (this.fInitSecurityManager == null || !this.fInitSecurityManager.setLimit(propertyId, XMLSecurityManager.State.APIPROPERTY, value)) {
            if (this.fSecurityPropertyMgr == null || !this.fSecurityPropertyMgr.setValue(propertyId, XMLSecurityPropertyManager.State.APIPROPERTY, value)) {
                if (!this.fInitProperties.containsKey(propertyId)) {
                    this.fInitProperties.put(propertyId, super.getProperty(propertyId));
                }
                super.setProperty(propertyId, value);
            }
        }
    }

    public void addRecognizedParamsAndSetDefaults(XMLComponent component, XSGrammarPoolContainer grammarContainer) {
        String[] recognizedFeatures = component.getRecognizedFeatures();
        addRecognizedFeatures(recognizedFeatures);
        String[] recognizedProperties = component.getRecognizedProperties();
        addRecognizedProperties(recognizedProperties);
        setFeatureDefaults(component, recognizedFeatures, grammarContainer);
        setPropertyDefaults(component, recognizedProperties);
    }

    public void reset() throws XNIException {
        this.fNamespaceContext.reset();
        this.fValidationManager.reset();
        this.fEntityManager.reset(this);
        this.fErrorReporter.reset(this);
        this.fSchemaValidator.reset(this);
        this.fConfigUpdated = false;
    }

    void setErrorHandler(ErrorHandler errorHandler) {
        this.fErrorHandler = errorHandler;
        setProperty(ERROR_HANDLER, errorHandler != null ? new ErrorHandlerWrapper(errorHandler) : new ErrorHandlerWrapper(DraconianErrorHandler.getInstance()));
    }

    ErrorHandler getErrorHandler() {
        return this.fErrorHandler;
    }

    void setResourceResolver(LSResourceResolver resourceResolver) {
        this.fResourceResolver = resourceResolver;
        setProperty("http://apache.org/xml/properties/internal/entity-resolver", new DOMEntityResolverWrapper(resourceResolver));
    }

    LSResourceResolver getResourceResolver() {
        return this.fResourceResolver;
    }

    void setLocale(Locale locale) {
        this.fLocale = locale;
        this.fErrorReporter.setLocale(locale);
    }

    Locale getLocale() {
        return this.fLocale;
    }

    void restoreInitialState() {
        this.fConfigUpdated = true;
        this.fComponents.put("http://apache.org/xml/properties/internal/entity-resolver", null);
        this.fComponents.put(ERROR_HANDLER, null);
        setLocale(null);
        this.fComponents.put("http://apache.org/xml/properties/locale", null);
        this.fComponents.put("http://apache.org/xml/properties/security-manager", this.fInitSecurityManager);
        setLocale(null);
        this.fComponents.put("http://apache.org/xml/properties/locale", null);
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

    private void setFeatureDefaults(XMLComponent component, String[] recognizedFeatures, XSGrammarPoolContainer grammarContainer) {
        if (recognizedFeatures != null) {
            for (String featureId : recognizedFeatures) {
                Boolean state = grammarContainer.getFeature(featureId);
                if (state == null) {
                    state = component.getFeatureDefault(featureId);
                }
                if (state != null && !this.fFeatures.containsKey(featureId)) {
                    this.fFeatures.put(featureId, state);
                    this.fConfigUpdated = true;
                }
            }
        }
    }

    private void setPropertyDefaults(XMLComponent component, String[] recognizedProperties) {
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
}
