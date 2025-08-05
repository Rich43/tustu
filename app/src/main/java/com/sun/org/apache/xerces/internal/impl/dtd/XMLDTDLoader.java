package com.sun.org.apache.xerces.internal.impl.dtd;

import com.sun.org.apache.xerces.internal.impl.XMLDTDScannerImpl;
import com.sun.org.apache.xerces.internal.impl.XMLEntityManager;
import com.sun.org.apache.xerces.internal.impl.XMLErrorReporter;
import com.sun.org.apache.xerces.internal.impl.msg.XMLMessageFormatter;
import com.sun.org.apache.xerces.internal.util.DefaultErrorHandler;
import com.sun.org.apache.xerces.internal.util.Status;
import com.sun.org.apache.xerces.internal.util.SymbolTable;
import com.sun.org.apache.xerces.internal.xni.XNIException;
import com.sun.org.apache.xerces.internal.xni.grammars.Grammar;
import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarLoader;
import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool;
import com.sun.org.apache.xerces.internal.xni.parser.XMLConfigurationException;
import com.sun.org.apache.xerces.internal.xni.parser.XMLEntityResolver;
import com.sun.org.apache.xerces.internal.xni.parser.XMLErrorHandler;
import com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;
import java.io.EOFException;
import java.io.IOException;
import java.io.StringReader;
import java.util.Locale;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/dtd/XMLDTDLoader.class */
public class XMLDTDLoader extends XMLDTDProcessor implements XMLGrammarLoader {
    public static final String ENTITY_RESOLVER = "http://apache.org/xml/properties/internal/entity-resolver";
    private boolean fStrictURI;
    private boolean fBalanceSyntaxTrees;
    protected XMLEntityResolver fEntityResolver;
    protected XMLDTDScannerImpl fDTDScanner;
    protected XMLEntityManager fEntityManager;
    protected Locale fLocale;
    protected static final String STANDARD_URI_CONFORMANT_FEATURE = "http://apache.org/xml/features/standard-uri-conformant";
    protected static final String BALANCE_SYNTAX_TREES = "http://apache.org/xml/features/validation/balance-syntax-trees";
    private static final String[] LOADER_RECOGNIZED_FEATURES = {"http://xml.org/sax/features/validation", "http://apache.org/xml/features/validation/warn-on-duplicate-attdef", "http://apache.org/xml/features/validation/warn-on-undeclared-elemdef", "http://apache.org/xml/features/scanner/notify-char-refs", STANDARD_URI_CONFORMANT_FEATURE, BALANCE_SYNTAX_TREES};
    protected static final String ERROR_HANDLER = "http://apache.org/xml/properties/internal/error-handler";
    public static final String LOCALE = "http://apache.org/xml/properties/locale";
    private static final String[] LOADER_RECOGNIZED_PROPERTIES = {"http://apache.org/xml/properties/internal/symbol-table", "http://apache.org/xml/properties/internal/error-reporter", ERROR_HANDLER, "http://apache.org/xml/properties/internal/entity-resolver", "http://apache.org/xml/properties/internal/grammar-pool", "http://apache.org/xml/properties/internal/validator/dtd", LOCALE};

    public XMLDTDLoader() {
        this(new SymbolTable());
    }

    public XMLDTDLoader(SymbolTable symbolTable) {
        this(symbolTable, null);
    }

    public XMLDTDLoader(SymbolTable symbolTable, XMLGrammarPool grammarPool) {
        this(symbolTable, grammarPool, null, new XMLEntityManager());
    }

    XMLDTDLoader(SymbolTable symbolTable, XMLGrammarPool grammarPool, XMLErrorReporter errorReporter, XMLEntityResolver entityResolver) throws XMLConfigurationException {
        this.fStrictURI = false;
        this.fBalanceSyntaxTrees = false;
        this.fSymbolTable = symbolTable;
        this.fGrammarPool = grammarPool;
        if (errorReporter == null) {
            errorReporter = new XMLErrorReporter();
            errorReporter.setProperty(ERROR_HANDLER, new DefaultErrorHandler());
        }
        this.fErrorReporter = errorReporter;
        if (this.fErrorReporter.getMessageFormatter("http://www.w3.org/TR/1998/REC-xml-19980210") == null) {
            XMLMessageFormatter xmft = new XMLMessageFormatter();
            this.fErrorReporter.putMessageFormatter("http://www.w3.org/TR/1998/REC-xml-19980210", xmft);
            this.fErrorReporter.putMessageFormatter("http://www.w3.org/TR/1999/REC-xml-names-19990114", xmft);
        }
        this.fEntityResolver = entityResolver;
        if (this.fEntityResolver instanceof XMLEntityManager) {
            this.fEntityManager = (XMLEntityManager) this.fEntityResolver;
        } else {
            this.fEntityManager = new XMLEntityManager();
        }
        this.fEntityManager.setProperty("http://apache.org/xml/properties/internal/error-reporter", errorReporter);
        this.fDTDScanner = createDTDScanner(this.fSymbolTable, this.fErrorReporter, this.fEntityManager);
        this.fDTDScanner.setDTDHandler(this);
        this.fDTDScanner.setDTDContentModelHandler(this);
        reset();
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dtd.XMLDTDProcessor, com.sun.org.apache.xerces.internal.xni.parser.XMLComponent
    public String[] getRecognizedFeatures() {
        return (String[]) LOADER_RECOGNIZED_FEATURES.clone();
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dtd.XMLDTDProcessor, com.sun.org.apache.xerces.internal.xni.parser.XMLComponent
    public void setFeature(String featureId, boolean state) throws XMLConfigurationException {
        if (featureId.equals("http://xml.org/sax/features/validation")) {
            this.fValidation = state;
            return;
        }
        if (featureId.equals("http://apache.org/xml/features/validation/warn-on-duplicate-attdef")) {
            this.fWarnDuplicateAttdef = state;
            return;
        }
        if (featureId.equals("http://apache.org/xml/features/validation/warn-on-undeclared-elemdef")) {
            this.fWarnOnUndeclaredElemdef = state;
            return;
        }
        if (featureId.equals("http://apache.org/xml/features/scanner/notify-char-refs")) {
            this.fDTDScanner.setFeature(featureId, state);
        } else if (featureId.equals(STANDARD_URI_CONFORMANT_FEATURE)) {
            this.fStrictURI = state;
        } else {
            if (featureId.equals(BALANCE_SYNTAX_TREES)) {
                this.fBalanceSyntaxTrees = state;
                return;
            }
            throw new XMLConfigurationException(Status.NOT_RECOGNIZED, featureId);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dtd.XMLDTDProcessor, com.sun.org.apache.xerces.internal.xni.parser.XMLComponent
    public String[] getRecognizedProperties() {
        return (String[]) LOADER_RECOGNIZED_PROPERTIES.clone();
    }

    @Override // com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarLoader
    public Object getProperty(String propertyId) throws XMLConfigurationException {
        if (propertyId.equals("http://apache.org/xml/properties/internal/symbol-table")) {
            return this.fSymbolTable;
        }
        if (propertyId.equals("http://apache.org/xml/properties/internal/error-reporter")) {
            return this.fErrorReporter;
        }
        if (propertyId.equals(ERROR_HANDLER)) {
            return this.fErrorReporter.getErrorHandler();
        }
        if (propertyId.equals("http://apache.org/xml/properties/internal/entity-resolver")) {
            return this.fEntityResolver;
        }
        if (propertyId.equals(LOCALE)) {
            return getLocale();
        }
        if (propertyId.equals("http://apache.org/xml/properties/internal/grammar-pool")) {
            return this.fGrammarPool;
        }
        if (propertyId.equals("http://apache.org/xml/properties/internal/validator/dtd")) {
            return this.fValidator;
        }
        throw new XMLConfigurationException(Status.NOT_RECOGNIZED, propertyId);
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dtd.XMLDTDProcessor, com.sun.org.apache.xerces.internal.xni.parser.XMLComponent
    public void setProperty(String propertyId, Object value) throws XMLConfigurationException {
        if (propertyId.equals("http://apache.org/xml/properties/internal/symbol-table")) {
            this.fSymbolTable = (SymbolTable) value;
            this.fDTDScanner.setProperty(propertyId, value);
            this.fEntityManager.setProperty(propertyId, value);
            return;
        }
        if (propertyId.equals("http://apache.org/xml/properties/internal/error-reporter")) {
            this.fErrorReporter = (XMLErrorReporter) value;
            if (this.fErrorReporter.getMessageFormatter("http://www.w3.org/TR/1998/REC-xml-19980210") == null) {
                XMLMessageFormatter xmft = new XMLMessageFormatter();
                this.fErrorReporter.putMessageFormatter("http://www.w3.org/TR/1998/REC-xml-19980210", xmft);
                this.fErrorReporter.putMessageFormatter("http://www.w3.org/TR/1999/REC-xml-names-19990114", xmft);
            }
            this.fDTDScanner.setProperty(propertyId, value);
            this.fEntityManager.setProperty(propertyId, value);
            return;
        }
        if (propertyId.equals(ERROR_HANDLER)) {
            this.fErrorReporter.setProperty(propertyId, value);
            return;
        }
        if (propertyId.equals("http://apache.org/xml/properties/internal/entity-resolver")) {
            this.fEntityResolver = (XMLEntityResolver) value;
            this.fEntityManager.setProperty(propertyId, value);
        } else if (propertyId.equals(LOCALE)) {
            setLocale((Locale) value);
        } else {
            if (propertyId.equals("http://apache.org/xml/properties/internal/grammar-pool")) {
                this.fGrammarPool = (XMLGrammarPool) value;
                return;
            }
            throw new XMLConfigurationException(Status.NOT_RECOGNIZED, propertyId);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarLoader
    public boolean getFeature(String featureId) throws XMLConfigurationException {
        if (featureId.equals("http://xml.org/sax/features/validation")) {
            return this.fValidation;
        }
        if (featureId.equals("http://apache.org/xml/features/validation/warn-on-duplicate-attdef")) {
            return this.fWarnDuplicateAttdef;
        }
        if (featureId.equals("http://apache.org/xml/features/validation/warn-on-undeclared-elemdef")) {
            return this.fWarnOnUndeclaredElemdef;
        }
        if (featureId.equals("http://apache.org/xml/features/scanner/notify-char-refs")) {
            return this.fDTDScanner.getFeature(featureId);
        }
        if (featureId.equals(STANDARD_URI_CONFORMANT_FEATURE)) {
            return this.fStrictURI;
        }
        if (featureId.equals(BALANCE_SYNTAX_TREES)) {
            return this.fBalanceSyntaxTrees;
        }
        throw new XMLConfigurationException(Status.NOT_RECOGNIZED, featureId);
    }

    @Override // com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarLoader
    public void setLocale(Locale locale) {
        this.fLocale = locale;
        this.fErrorReporter.setLocale(locale);
    }

    @Override // com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarLoader
    public Locale getLocale() {
        return this.fLocale;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarLoader
    public void setErrorHandler(XMLErrorHandler errorHandler) throws XMLConfigurationException {
        this.fErrorReporter.setProperty(ERROR_HANDLER, errorHandler);
    }

    @Override // com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarLoader
    public XMLErrorHandler getErrorHandler() {
        return this.fErrorReporter.getErrorHandler();
    }

    @Override // com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarLoader
    public void setEntityResolver(XMLEntityResolver entityResolver) {
        this.fEntityResolver = entityResolver;
        this.fEntityManager.setProperty("http://apache.org/xml/properties/internal/entity-resolver", entityResolver);
    }

    @Override // com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarLoader
    public XMLEntityResolver getEntityResolver() {
        return this.fEntityResolver;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarLoader
    public Grammar loadGrammar(XMLInputSource source) throws IOException, XNIException {
        reset();
        String eid = XMLEntityManager.expandSystemId(source.getSystemId(), source.getBaseSystemId(), this.fStrictURI);
        XMLDTDDescription desc = new XMLDTDDescription(source.getPublicId(), source.getSystemId(), source.getBaseSystemId(), eid, null);
        if (!this.fBalanceSyntaxTrees) {
            this.fDTDGrammar = new DTDGrammar(this.fSymbolTable, desc);
        } else {
            this.fDTDGrammar = new BalancedDTDGrammar(this.fSymbolTable, desc);
        }
        this.fGrammarBucket = new DTDGrammarBucket();
        this.fGrammarBucket.setStandalone(false);
        this.fGrammarBucket.setActiveGrammar(this.fDTDGrammar);
        try {
            this.fDTDScanner.setInputSource(source);
            this.fDTDScanner.scanDTDExternalSubset(true);
            this.fEntityManager.closeReaders();
        } catch (EOFException e2) {
            this.fEntityManager.closeReaders();
        } catch (Throwable th) {
            this.fEntityManager.closeReaders();
            throw th;
        }
        if (this.fDTDGrammar != null && this.fGrammarPool != null) {
            this.fGrammarPool.cacheGrammars("http://www.w3.org/TR/REC-xml", new Grammar[]{this.fDTDGrammar});
        }
        return this.fDTDGrammar;
    }

    public void loadGrammarWithContext(XMLDTDValidator validator, String rootName, String publicId, String systemId, String baseSystemId, String internalSubset) throws IOException, XNIException {
        DTDGrammarBucket grammarBucket = validator.getGrammarBucket();
        DTDGrammar activeGrammar = grammarBucket.getActiveGrammar();
        if (activeGrammar != null && !activeGrammar.isImmutable()) {
            this.fGrammarBucket = grammarBucket;
            this.fEntityManager.setScannerVersion(getScannerVersion());
            reset();
            if (internalSubset != null) {
                try {
                    StringBuffer buffer = new StringBuffer(internalSubset.length() + 2);
                    buffer.append(internalSubset).append("]>");
                    XMLInputSource is = new XMLInputSource((String) null, baseSystemId, (String) null, new StringReader(buffer.toString()), (String) null);
                    this.fEntityManager.startDocumentEntity(is);
                    this.fDTDScanner.scanDTDInternalSubset(true, false, systemId != null);
                } catch (EOFException e2) {
                    this.fEntityManager.closeReaders();
                    return;
                } catch (Throwable th) {
                    this.fEntityManager.closeReaders();
                    throw th;
                }
            }
            if (systemId != null) {
                XMLDTDDescription desc = new XMLDTDDescription(publicId, systemId, baseSystemId, null, rootName);
                XMLInputSource source = this.fEntityManager.resolveEntity(desc);
                this.fDTDScanner.setInputSource(source);
                this.fDTDScanner.scanDTDExternalSubset(true);
            }
            this.fEntityManager.closeReaders();
        }
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dtd.XMLDTDProcessor
    protected void reset() {
        super.reset();
        this.fDTDScanner.reset();
        this.fEntityManager.reset();
        this.fErrorReporter.setDocumentLocator(this.fEntityManager.getEntityScanner());
    }

    protected XMLDTDScannerImpl createDTDScanner(SymbolTable symbolTable, XMLErrorReporter errorReporter, XMLEntityManager entityManager) {
        return new XMLDTDScannerImpl(symbolTable, errorReporter, entityManager);
    }

    protected short getScannerVersion() {
        return (short) 1;
    }
}
