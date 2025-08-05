package com.sun.org.apache.xerces.internal.jaxp;

import com.sun.org.apache.xerces.internal.impl.XMLErrorReporter;
import com.sun.org.apache.xerces.internal.impl.validation.ValidationManager;
import com.sun.org.apache.xerces.internal.impl.xs.XSMessageFormatter;
import com.sun.org.apache.xerces.internal.jaxp.validation.XSGrammarPoolContainer;
import com.sun.org.apache.xerces.internal.util.FeatureState;
import com.sun.org.apache.xerces.internal.util.PropertyState;
import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool;
import com.sun.org.apache.xerces.internal.xni.parser.XMLComponentManager;
import com.sun.org.apache.xerces.internal.xni.parser.XMLConfigurationException;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/jaxp/SchemaValidatorConfiguration.class */
final class SchemaValidatorConfiguration implements XMLComponentManager {
    private static final String SCHEMA_VALIDATION = "http://apache.org/xml/features/validation/schema";
    private static final String VALIDATION = "http://xml.org/sax/features/validation";
    private static final String USE_GRAMMAR_POOL_ONLY = "http://apache.org/xml/features/internal/validation/schema/use-grammar-pool-only";
    private static final String PARSER_SETTINGS = "http://apache.org/xml/features/internal/parser-settings";
    private static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
    private static final String VALIDATION_MANAGER = "http://apache.org/xml/properties/internal/validation-manager";
    private static final String XMLGRAMMAR_POOL = "http://apache.org/xml/properties/internal/grammar-pool";
    private final XMLComponentManager fParentComponentManager;
    private final XMLGrammarPool fGrammarPool;
    private final boolean fUseGrammarPoolOnly;
    private final ValidationManager fValidationManager;

    public SchemaValidatorConfiguration(XMLComponentManager parentManager, XSGrammarPoolContainer grammarContainer, ValidationManager validationManager) {
        this.fParentComponentManager = parentManager;
        this.fGrammarPool = grammarContainer.getGrammarPool();
        this.fUseGrammarPoolOnly = grammarContainer.isFullyComposed();
        this.fValidationManager = validationManager;
        try {
            XMLErrorReporter errorReporter = (XMLErrorReporter) this.fParentComponentManager.getProperty("http://apache.org/xml/properties/internal/error-reporter");
            if (errorReporter != null) {
                errorReporter.putMessageFormatter(XSMessageFormatter.SCHEMA_DOMAIN, new XSMessageFormatter());
            }
        } catch (XMLConfigurationException e2) {
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLComponentManager
    public boolean getFeature(String featureId) throws XMLConfigurationException {
        FeatureState state = getFeatureState(featureId);
        if (state.isExceptional()) {
            throw new XMLConfigurationException(state.status, featureId);
        }
        return state.state;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLComponentManager
    public FeatureState getFeatureState(String featureId) {
        if (PARSER_SETTINGS.equals(featureId)) {
            return this.fParentComponentManager.getFeatureState(featureId);
        }
        if (VALIDATION.equals(featureId) || SCHEMA_VALIDATION.equals(featureId)) {
            return FeatureState.is(true);
        }
        if (USE_GRAMMAR_POOL_ONLY.equals(featureId)) {
            return FeatureState.is(this.fUseGrammarPoolOnly);
        }
        return this.fParentComponentManager.getFeatureState(featureId);
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLComponentManager
    public PropertyState getPropertyState(String propertyId) {
        if ("http://apache.org/xml/properties/internal/grammar-pool".equals(propertyId)) {
            return PropertyState.is(this.fGrammarPool);
        }
        if (VALIDATION_MANAGER.equals(propertyId)) {
            return PropertyState.is(this.fValidationManager);
        }
        return this.fParentComponentManager.getPropertyState(propertyId);
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLComponentManager
    public Object getProperty(String propertyId) throws XMLConfigurationException {
        PropertyState state = getPropertyState(propertyId);
        if (state.isExceptional()) {
            throw new XMLConfigurationException(state.status, propertyId);
        }
        return state.state;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLComponentManager
    public boolean getFeature(String featureId, boolean defaultValue) {
        FeatureState state = getFeatureState(featureId);
        if (state.isExceptional()) {
            return defaultValue;
        }
        return state.state;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLComponentManager
    public Object getProperty(String propertyId, Object defaultValue) {
        PropertyState state = getPropertyState(propertyId);
        if (state.isExceptional()) {
            return defaultValue;
        }
        return state.state;
    }
}
