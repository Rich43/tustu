package com.sun.org.apache.xerces.internal.parsers;

import com.sun.org.apache.xerces.internal.util.FeatureState;
import com.sun.org.apache.xerces.internal.util.NamespaceSupport;
import com.sun.org.apache.xerces.internal.util.SymbolTable;
import com.sun.org.apache.xerces.internal.xinclude.XIncludeHandler;
import com.sun.org.apache.xerces.internal.xinclude.XIncludeNamespaceSupport;
import com.sun.org.apache.xerces.internal.xni.NamespaceContext;
import com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler;
import com.sun.org.apache.xerces.internal.xni.XNIException;
import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool;
import com.sun.org.apache.xerces.internal.xni.parser.XMLComponentManager;
import com.sun.org.apache.xerces.internal.xni.parser.XMLConfigurationException;
import com.sun.org.apache.xerces.internal.xni.parser.XMLDocumentSource;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/parsers/XIncludeAwareParserConfiguration.class */
public class XIncludeAwareParserConfiguration extends XML11Configuration {
    protected static final String ALLOW_UE_AND_NOTATION_EVENTS = "http://xml.org/sax/features/allow-dtd-events-after-endDTD";
    protected static final String XINCLUDE_FIXUP_BASE_URIS = "http://apache.org/xml/features/xinclude/fixup-base-uris";
    protected static final String XINCLUDE_FIXUP_LANGUAGE = "http://apache.org/xml/features/xinclude/fixup-language";
    protected static final String XINCLUDE_FEATURE = "http://apache.org/xml/features/xinclude";
    protected static final String XINCLUDE_HANDLER = "http://apache.org/xml/properties/internal/xinclude-handler";
    protected static final String NAMESPACE_CONTEXT = "http://apache.org/xml/properties/internal/namespace-context";
    protected XIncludeHandler fXIncludeHandler;
    protected NamespaceSupport fNonXIncludeNSContext;
    protected XIncludeNamespaceSupport fXIncludeNSContext;
    protected NamespaceContext fCurrentNSContext;
    protected boolean fXIncludeEnabled;

    public XIncludeAwareParserConfiguration() {
        this(null, null, null);
    }

    public XIncludeAwareParserConfiguration(SymbolTable symbolTable) {
        this(symbolTable, null, null);
    }

    public XIncludeAwareParserConfiguration(SymbolTable symbolTable, XMLGrammarPool grammarPool) {
        this(symbolTable, grammarPool, null);
    }

    public XIncludeAwareParserConfiguration(SymbolTable symbolTable, XMLGrammarPool grammarPool, XMLComponentManager parentSettings) throws XMLConfigurationException {
        super(symbolTable, grammarPool, parentSettings);
        this.fXIncludeEnabled = false;
        String[] recognizedFeatures = {ALLOW_UE_AND_NOTATION_EVENTS, XINCLUDE_FIXUP_BASE_URIS, XINCLUDE_FIXUP_LANGUAGE};
        addRecognizedFeatures(recognizedFeatures);
        String[] recognizedProperties = {XINCLUDE_HANDLER, NAMESPACE_CONTEXT};
        addRecognizedProperties(recognizedProperties);
        setFeature(ALLOW_UE_AND_NOTATION_EVENTS, true);
        setFeature(XINCLUDE_FIXUP_BASE_URIS, true);
        setFeature(XINCLUDE_FIXUP_LANGUAGE, true);
        this.fNonXIncludeNSContext = new NamespaceSupport();
        this.fCurrentNSContext = this.fNonXIncludeNSContext;
        setProperty(NAMESPACE_CONTEXT, this.fNonXIncludeNSContext);
    }

    @Override // com.sun.org.apache.xerces.internal.parsers.XML11Configuration
    protected void configurePipeline() throws XNIException {
        XMLDocumentSource prev;
        super.configurePipeline();
        if (this.fXIncludeEnabled) {
            if (this.fXIncludeHandler == null) {
                this.fXIncludeHandler = new XIncludeHandler();
                setProperty(XINCLUDE_HANDLER, this.fXIncludeHandler);
                addCommonComponent(this.fXIncludeHandler);
                this.fXIncludeHandler.reset(this);
            }
            if (this.fCurrentNSContext != this.fXIncludeNSContext) {
                if (this.fXIncludeNSContext == null) {
                    this.fXIncludeNSContext = new XIncludeNamespaceSupport();
                }
                this.fCurrentNSContext = this.fXIncludeNSContext;
                setProperty(NAMESPACE_CONTEXT, this.fXIncludeNSContext);
            }
            this.fDTDScanner.setDTDHandler(this.fDTDProcessor);
            this.fDTDProcessor.setDTDSource(this.fDTDScanner);
            this.fDTDProcessor.setDTDHandler(this.fXIncludeHandler);
            this.fXIncludeHandler.setDTDSource(this.fDTDProcessor);
            this.fXIncludeHandler.setDTDHandler(this.fDTDHandler);
            if (this.fDTDHandler != null) {
                this.fDTDHandler.setDTDSource(this.fXIncludeHandler);
            }
            if (this.fFeatures.get("http://apache.org/xml/features/validation/schema") == Boolean.TRUE) {
                prev = this.fSchemaValidator.getDocumentSource();
            } else {
                prev = this.fLastComponent;
                this.fLastComponent = this.fXIncludeHandler;
            }
            XMLDocumentHandler next = prev.getDocumentHandler();
            prev.setDocumentHandler(this.fXIncludeHandler);
            this.fXIncludeHandler.setDocumentSource(prev);
            if (next != null) {
                this.fXIncludeHandler.setDocumentHandler(next);
                next.setDocumentSource(this.fXIncludeHandler);
                return;
            }
            return;
        }
        if (this.fCurrentNSContext != this.fNonXIncludeNSContext) {
            this.fCurrentNSContext = this.fNonXIncludeNSContext;
            setProperty(NAMESPACE_CONTEXT, this.fNonXIncludeNSContext);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.parsers.XML11Configuration
    protected void configureXML11Pipeline() throws XNIException {
        XMLDocumentSource prev;
        super.configureXML11Pipeline();
        if (this.fXIncludeEnabled) {
            if (this.fXIncludeHandler == null) {
                this.fXIncludeHandler = new XIncludeHandler();
                setProperty(XINCLUDE_HANDLER, this.fXIncludeHandler);
                addCommonComponent(this.fXIncludeHandler);
                this.fXIncludeHandler.reset(this);
            }
            if (this.fCurrentNSContext != this.fXIncludeNSContext) {
                if (this.fXIncludeNSContext == null) {
                    this.fXIncludeNSContext = new XIncludeNamespaceSupport();
                }
                this.fCurrentNSContext = this.fXIncludeNSContext;
                setProperty(NAMESPACE_CONTEXT, this.fXIncludeNSContext);
            }
            this.fXML11DTDScanner.setDTDHandler(this.fXML11DTDProcessor);
            this.fXML11DTDProcessor.setDTDSource(this.fXML11DTDScanner);
            this.fXML11DTDProcessor.setDTDHandler(this.fXIncludeHandler);
            this.fXIncludeHandler.setDTDSource(this.fXML11DTDProcessor);
            this.fXIncludeHandler.setDTDHandler(this.fDTDHandler);
            if (this.fDTDHandler != null) {
                this.fDTDHandler.setDTDSource(this.fXIncludeHandler);
            }
            if (this.fFeatures.get("http://apache.org/xml/features/validation/schema") == Boolean.TRUE) {
                prev = this.fSchemaValidator.getDocumentSource();
            } else {
                prev = this.fLastComponent;
                this.fLastComponent = this.fXIncludeHandler;
            }
            XMLDocumentHandler next = prev.getDocumentHandler();
            prev.setDocumentHandler(this.fXIncludeHandler);
            this.fXIncludeHandler.setDocumentSource(prev);
            if (next != null) {
                this.fXIncludeHandler.setDocumentHandler(next);
                next.setDocumentSource(this.fXIncludeHandler);
                return;
            }
            return;
        }
        if (this.fCurrentNSContext != this.fNonXIncludeNSContext) {
            this.fCurrentNSContext = this.fNonXIncludeNSContext;
            setProperty(NAMESPACE_CONTEXT, this.fNonXIncludeNSContext);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.parsers.XML11Configuration, com.sun.org.apache.xerces.internal.util.ParserConfigurationSettings, com.sun.org.apache.xerces.internal.xni.parser.XMLComponentManager
    public FeatureState getFeatureState(String featureId) throws XMLConfigurationException {
        if (featureId.equals("http://apache.org/xml/features/internal/parser-settings")) {
            return FeatureState.is(this.fConfigUpdated);
        }
        if (featureId.equals(XINCLUDE_FEATURE)) {
            return FeatureState.is(this.fXIncludeEnabled);
        }
        return super.getFeatureState0(featureId);
    }

    @Override // com.sun.org.apache.xerces.internal.parsers.XML11Configuration, com.sun.org.apache.xerces.internal.util.ParserConfigurationSettings, com.sun.org.apache.xerces.internal.xni.parser.XMLParserConfiguration
    public void setFeature(String featureId, boolean state) throws XMLConfigurationException {
        if (featureId.equals(XINCLUDE_FEATURE)) {
            this.fXIncludeEnabled = state;
            this.fConfigUpdated = true;
        } else {
            super.setFeature(featureId, state);
        }
    }
}
