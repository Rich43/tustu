package com.sun.org.apache.xerces.internal.parsers;

import com.sun.org.apache.xerces.internal.util.SymbolTable;
import com.sun.org.apache.xerces.internal.xinclude.XIncludeHandler;
import com.sun.org.apache.xerces.internal.xinclude.XIncludeNamespaceSupport;
import com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler;
import com.sun.org.apache.xerces.internal.xni.XNIException;
import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool;
import com.sun.org.apache.xerces.internal.xni.parser.XMLComponentManager;
import com.sun.org.apache.xerces.internal.xni.parser.XMLDocumentSource;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/parsers/XIncludeParserConfiguration.class */
public class XIncludeParserConfiguration extends XML11Configuration {
    private XIncludeHandler fXIncludeHandler;
    protected static final String ALLOW_UE_AND_NOTATION_EVENTS = "http://xml.org/sax/features/allow-dtd-events-after-endDTD";
    protected static final String XINCLUDE_FIXUP_BASE_URIS = "http://apache.org/xml/features/xinclude/fixup-base-uris";
    protected static final String XINCLUDE_FIXUP_LANGUAGE = "http://apache.org/xml/features/xinclude/fixup-language";
    protected static final String XINCLUDE_HANDLER = "http://apache.org/xml/properties/internal/xinclude-handler";
    protected static final String NAMESPACE_CONTEXT = "http://apache.org/xml/properties/internal/namespace-context";

    public XIncludeParserConfiguration() {
        this(null, null, null);
    }

    public XIncludeParserConfiguration(SymbolTable symbolTable) {
        this(symbolTable, null, null);
    }

    public XIncludeParserConfiguration(SymbolTable symbolTable, XMLGrammarPool grammarPool) {
        this(symbolTable, grammarPool, null);
    }

    public XIncludeParserConfiguration(SymbolTable symbolTable, XMLGrammarPool grammarPool, XMLComponentManager parentSettings) throws XNIException {
        super(symbolTable, grammarPool, parentSettings);
        this.fXIncludeHandler = new XIncludeHandler();
        addCommonComponent(this.fXIncludeHandler);
        String[] recognizedFeatures = {ALLOW_UE_AND_NOTATION_EVENTS, XINCLUDE_FIXUP_BASE_URIS, XINCLUDE_FIXUP_LANGUAGE};
        addRecognizedFeatures(recognizedFeatures);
        String[] recognizedProperties = {XINCLUDE_HANDLER, NAMESPACE_CONTEXT};
        addRecognizedProperties(recognizedProperties);
        setFeature(ALLOW_UE_AND_NOTATION_EVENTS, true);
        setFeature(XINCLUDE_FIXUP_BASE_URIS, true);
        setFeature(XINCLUDE_FIXUP_LANGUAGE, true);
        setProperty(XINCLUDE_HANDLER, this.fXIncludeHandler);
        setProperty(NAMESPACE_CONTEXT, new XIncludeNamespaceSupport());
    }

    @Override // com.sun.org.apache.xerces.internal.parsers.XML11Configuration
    protected void configurePipeline() throws XNIException {
        XMLDocumentSource prev;
        super.configurePipeline();
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
        if (prev != null) {
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
        setDocumentHandler(this.fXIncludeHandler);
    }

    @Override // com.sun.org.apache.xerces.internal.parsers.XML11Configuration
    protected void configureXML11Pipeline() throws XNIException {
        XMLDocumentSource prev;
        super.configureXML11Pipeline();
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
        }
    }

    @Override // com.sun.org.apache.xerces.internal.parsers.XML11Configuration, com.sun.org.apache.xerces.internal.util.ParserConfigurationSettings, com.sun.org.apache.xerces.internal.xni.parser.XMLParserConfiguration
    public void setProperty(String propertyId, Object value) throws XNIException {
        if (propertyId.equals(XINCLUDE_HANDLER)) {
        }
        super.setProperty(propertyId, value);
    }
}
