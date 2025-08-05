package com.sun.org.apache.xerces.internal.parsers;

import com.sun.org.apache.xerces.internal.impl.Constants;
import com.sun.org.apache.xerces.internal.util.FeatureState;
import com.sun.org.apache.xerces.internal.util.ParserConfigurationSettings;
import com.sun.org.apache.xerces.internal.util.PropertyState;
import com.sun.org.apache.xerces.internal.util.SymbolTable;
import com.sun.org.apache.xerces.internal.xni.XMLDTDContentModelHandler;
import com.sun.org.apache.xerces.internal.xni.XMLDTDHandler;
import com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler;
import com.sun.org.apache.xerces.internal.xni.XNIException;
import com.sun.org.apache.xerces.internal.xni.parser.XMLComponent;
import com.sun.org.apache.xerces.internal.xni.parser.XMLComponentManager;
import com.sun.org.apache.xerces.internal.xni.parser.XMLConfigurationException;
import com.sun.org.apache.xerces.internal.xni.parser.XMLDocumentSource;
import com.sun.org.apache.xerces.internal.xni.parser.XMLEntityResolver;
import com.sun.org.apache.xerces.internal.xni.parser.XMLErrorHandler;
import com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;
import com.sun.org.apache.xerces.internal.xni.parser.XMLParserConfiguration;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/parsers/BasicParserConfiguration.class */
public abstract class BasicParserConfiguration extends ParserConfigurationSettings implements XMLParserConfiguration {
    protected static final String VALIDATION = "http://xml.org/sax/features/validation";
    protected static final String NAMESPACES = "http://xml.org/sax/features/namespaces";
    protected static final String EXTERNAL_GENERAL_ENTITIES = "http://xml.org/sax/features/external-general-entities";
    protected static final String EXTERNAL_PARAMETER_ENTITIES = "http://xml.org/sax/features/external-parameter-entities";
    protected static final String XML_STRING = "http://xml.org/sax/properties/xml-string";
    protected static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
    protected static final String ERROR_HANDLER = "http://apache.org/xml/properties/internal/error-handler";
    protected static final String ENTITY_RESOLVER = "http://apache.org/xml/properties/internal/entity-resolver";
    protected SymbolTable fSymbolTable;
    protected Locale fLocale;
    protected ArrayList fComponents;
    protected XMLDocumentHandler fDocumentHandler;
    protected XMLDTDHandler fDTDHandler;
    protected XMLDTDContentModelHandler fDTDContentModelHandler;
    protected XMLDocumentSource fLastComponent;

    public abstract void parse(XMLInputSource xMLInputSource) throws IOException, XNIException;

    protected BasicParserConfiguration() {
        this(null, null);
    }

    protected BasicParserConfiguration(SymbolTable symbolTable) {
        this(symbolTable, null);
    }

    protected BasicParserConfiguration(SymbolTable symbolTable, XMLComponentManager parentSettings) {
        super(parentSettings);
        this.fComponents = new ArrayList();
        this.fFeatures = new HashMap();
        this.fProperties = new HashMap();
        String[] recognizedFeatures = {"http://apache.org/xml/features/internal/parser-settings", VALIDATION, "http://xml.org/sax/features/namespaces", "http://xml.org/sax/features/external-general-entities", "http://xml.org/sax/features/external-parameter-entities"};
        addRecognizedFeatures(recognizedFeatures);
        this.fFeatures.put("http://apache.org/xml/features/internal/parser-settings", Boolean.TRUE);
        this.fFeatures.put(VALIDATION, Boolean.FALSE);
        this.fFeatures.put("http://xml.org/sax/features/namespaces", Boolean.TRUE);
        this.fFeatures.put("http://xml.org/sax/features/external-general-entities", Boolean.TRUE);
        this.fFeatures.put("http://xml.org/sax/features/external-parameter-entities", Boolean.TRUE);
        String[] recognizedProperties = {XML_STRING, "http://apache.org/xml/properties/internal/symbol-table", ERROR_HANDLER, "http://apache.org/xml/properties/internal/entity-resolver"};
        addRecognizedProperties(recognizedProperties);
        this.fSymbolTable = symbolTable == null ? new SymbolTable() : symbolTable;
        this.fProperties.put("http://apache.org/xml/properties/internal/symbol-table", this.fSymbolTable);
    }

    protected void addComponent(XMLComponent component) throws XMLConfigurationException {
        if (this.fComponents.contains(component)) {
            return;
        }
        this.fComponents.add(component);
        String[] recognizedFeatures = component.getRecognizedFeatures();
        addRecognizedFeatures(recognizedFeatures);
        String[] recognizedProperties = component.getRecognizedProperties();
        addRecognizedProperties(recognizedProperties);
        if (recognizedFeatures != null) {
            for (String featureId : recognizedFeatures) {
                Boolean state = component.getFeatureDefault(featureId);
                if (state != null) {
                    super.setFeature(featureId, state.booleanValue());
                }
            }
        }
        if (recognizedProperties != null) {
            for (String propertyId : recognizedProperties) {
                Object value = component.getPropertyDefault(propertyId);
                if (value != null) {
                    super.setProperty(propertyId, value);
                }
            }
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLParserConfiguration
    public void setDocumentHandler(XMLDocumentHandler documentHandler) {
        this.fDocumentHandler = documentHandler;
        if (this.fLastComponent != null) {
            this.fLastComponent.setDocumentHandler(this.fDocumentHandler);
            if (this.fDocumentHandler != null) {
                this.fDocumentHandler.setDocumentSource(this.fLastComponent);
            }
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLParserConfiguration
    public XMLDocumentHandler getDocumentHandler() {
        return this.fDocumentHandler;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLParserConfiguration
    public void setDTDHandler(XMLDTDHandler dtdHandler) {
        this.fDTDHandler = dtdHandler;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLParserConfiguration
    public XMLDTDHandler getDTDHandler() {
        return this.fDTDHandler;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLParserConfiguration
    public void setDTDContentModelHandler(XMLDTDContentModelHandler handler) {
        this.fDTDContentModelHandler = handler;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLParserConfiguration
    public XMLDTDContentModelHandler getDTDContentModelHandler() {
        return this.fDTDContentModelHandler;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLParserConfiguration
    public void setEntityResolver(XMLEntityResolver resolver) {
        this.fProperties.put("http://apache.org/xml/properties/internal/entity-resolver", resolver);
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLParserConfiguration
    public XMLEntityResolver getEntityResolver() {
        return (XMLEntityResolver) this.fProperties.get("http://apache.org/xml/properties/internal/entity-resolver");
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLParserConfiguration
    public void setErrorHandler(XMLErrorHandler errorHandler) {
        this.fProperties.put(ERROR_HANDLER, errorHandler);
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLParserConfiguration
    public XMLErrorHandler getErrorHandler() {
        return (XMLErrorHandler) this.fProperties.get(ERROR_HANDLER);
    }

    @Override // com.sun.org.apache.xerces.internal.util.ParserConfigurationSettings, com.sun.org.apache.xerces.internal.xni.parser.XMLParserConfiguration
    public void setFeature(String featureId, boolean state) throws XMLConfigurationException {
        int count = this.fComponents.size();
        for (int i2 = 0; i2 < count; i2++) {
            XMLComponent c2 = (XMLComponent) this.fComponents.get(i2);
            c2.setFeature(featureId, state);
        }
        super.setFeature(featureId, state);
    }

    @Override // com.sun.org.apache.xerces.internal.util.ParserConfigurationSettings, com.sun.org.apache.xerces.internal.xni.parser.XMLParserConfiguration
    public void setProperty(String propertyId, Object value) throws XMLConfigurationException {
        int count = this.fComponents.size();
        for (int i2 = 0; i2 < count; i2++) {
            XMLComponent c2 = (XMLComponent) this.fComponents.get(i2);
            c2.setProperty(propertyId, value);
        }
        super.setProperty(propertyId, value);
    }

    public void setLocale(Locale locale) throws XNIException {
        this.fLocale = locale;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLParserConfiguration
    public Locale getLocale() {
        return this.fLocale;
    }

    protected void reset() throws XNIException {
        int count = this.fComponents.size();
        for (int i2 = 0; i2 < count; i2++) {
            XMLComponent c2 = (XMLComponent) this.fComponents.get(i2);
            c2.reset(this);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.util.ParserConfigurationSettings
    protected PropertyState checkProperty(String propertyId) throws XMLConfigurationException {
        if (propertyId.startsWith(Constants.SAX_PROPERTY_PREFIX)) {
            int suffixLength = propertyId.length() - Constants.SAX_PROPERTY_PREFIX.length();
            if (suffixLength == Constants.XML_STRING_PROPERTY.length() && propertyId.endsWith(Constants.XML_STRING_PROPERTY)) {
                return PropertyState.NOT_SUPPORTED;
            }
        }
        return super.checkProperty(propertyId);
    }

    @Override // com.sun.org.apache.xerces.internal.util.ParserConfigurationSettings
    protected FeatureState checkFeature(String featureId) throws XMLConfigurationException {
        if (featureId.startsWith(Constants.XERCES_FEATURE_PREFIX)) {
            int suffixLength = featureId.length() - Constants.XERCES_FEATURE_PREFIX.length();
            if (suffixLength == Constants.PARSER_SETTINGS.length() && featureId.endsWith(Constants.PARSER_SETTINGS)) {
                return FeatureState.NOT_SUPPORTED;
            }
        }
        return super.checkFeature(featureId);
    }
}
