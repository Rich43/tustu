package com.sun.org.apache.xerces.internal.impl.dtd;

import com.sun.org.apache.xerces.internal.impl.XMLErrorReporter;
import com.sun.org.apache.xerces.internal.util.SymbolTable;
import com.sun.org.apache.xerces.internal.util.XMLChar;
import com.sun.org.apache.xerces.internal.xni.Augmentations;
import com.sun.org.apache.xerces.internal.xni.XMLDTDContentModelHandler;
import com.sun.org.apache.xerces.internal.xni.XMLDTDHandler;
import com.sun.org.apache.xerces.internal.xni.XMLLocator;
import com.sun.org.apache.xerces.internal.xni.XMLResourceIdentifier;
import com.sun.org.apache.xerces.internal.xni.XMLString;
import com.sun.org.apache.xerces.internal.xni.XNIException;
import com.sun.org.apache.xerces.internal.xni.grammars.Grammar;
import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool;
import com.sun.org.apache.xerces.internal.xni.parser.XMLComponent;
import com.sun.org.apache.xerces.internal.xni.parser.XMLComponentManager;
import com.sun.org.apache.xerces.internal.xni.parser.XMLConfigurationException;
import com.sun.org.apache.xerces.internal.xni.parser.XMLDTDContentModelFilter;
import com.sun.org.apache.xerces.internal.xni.parser.XMLDTDContentModelSource;
import com.sun.org.apache.xerces.internal.xni.parser.XMLDTDFilter;
import com.sun.org.apache.xerces.internal.xni.parser.XMLDTDSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/dtd/XMLDTDProcessor.class */
public class XMLDTDProcessor implements XMLComponent, XMLDTDFilter, XMLDTDContentModelFilter {
    private static final int TOP_LEVEL_SCOPE = -1;
    protected static final String PARSER_SETTINGS = "http://apache.org/xml/features/internal/parser-settings";
    protected static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
    protected static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
    protected static final String GRAMMAR_POOL = "http://apache.org/xml/properties/internal/grammar-pool";
    protected boolean fValidation;
    protected boolean fDTDValidation;
    protected boolean fWarnDuplicateAttdef;
    protected boolean fWarnOnUndeclaredElemdef;
    protected SymbolTable fSymbolTable;
    protected XMLErrorReporter fErrorReporter;
    protected DTDGrammarBucket fGrammarBucket;
    protected XMLDTDValidator fValidator;
    protected XMLGrammarPool fGrammarPool;
    protected Locale fLocale;
    protected XMLDTDHandler fDTDHandler;
    protected XMLDTDSource fDTDSource;
    protected XMLDTDContentModelHandler fDTDContentModelHandler;
    protected XMLDTDContentModelSource fDTDContentModelSource;
    protected DTDGrammar fDTDGrammar;
    private boolean fPerformValidation;
    protected boolean fInDTDIgnore;
    private boolean fMixed;
    private final XMLEntityDecl fEntityDecl = new XMLEntityDecl();
    private final HashMap fNDataDeclNotations = new HashMap();
    private String fDTDElementDeclName = null;
    private final ArrayList fMixedElementTypes = new ArrayList();
    private final ArrayList fDTDElementDecls = new ArrayList();
    private HashMap fTableOfIDAttributeNames;
    private HashMap fTableOfNOTATIONAttributeNames;
    private HashMap fNotationEnumVals;
    protected static final String VALIDATION = "http://xml.org/sax/features/validation";
    protected static final String WARN_ON_DUPLICATE_ATTDEF = "http://apache.org/xml/features/validation/warn-on-duplicate-attdef";
    protected static final String WARN_ON_UNDECLARED_ELEMDEF = "http://apache.org/xml/features/validation/warn-on-undeclared-elemdef";
    protected static final String NOTIFY_CHAR_REFS = "http://apache.org/xml/features/scanner/notify-char-refs";
    private static final String[] RECOGNIZED_FEATURES = {VALIDATION, WARN_ON_DUPLICATE_ATTDEF, WARN_ON_UNDECLARED_ELEMDEF, NOTIFY_CHAR_REFS};
    private static final Boolean[] FEATURE_DEFAULTS = {null, Boolean.FALSE, Boolean.FALSE, null};
    protected static final String DTD_VALIDATOR = "http://apache.org/xml/properties/internal/validator/dtd";
    private static final String[] RECOGNIZED_PROPERTIES = {"http://apache.org/xml/properties/internal/symbol-table", "http://apache.org/xml/properties/internal/error-reporter", "http://apache.org/xml/properties/internal/grammar-pool", DTD_VALIDATOR};
    private static final Object[] PROPERTY_DEFAULTS = {null, null, null, null};

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLComponent
    public void reset(XMLComponentManager componentManager) throws XMLConfigurationException {
        boolean parser_settings = componentManager.getFeature(PARSER_SETTINGS, true);
        if (!parser_settings) {
            reset();
            return;
        }
        this.fValidation = componentManager.getFeature(VALIDATION, false);
        this.fDTDValidation = !componentManager.getFeature("http://apache.org/xml/features/validation/schema", false);
        this.fWarnDuplicateAttdef = componentManager.getFeature(WARN_ON_DUPLICATE_ATTDEF, false);
        this.fWarnOnUndeclaredElemdef = componentManager.getFeature(WARN_ON_UNDECLARED_ELEMDEF, false);
        this.fErrorReporter = (XMLErrorReporter) componentManager.getProperty("http://apache.org/xml/properties/internal/error-reporter");
        this.fSymbolTable = (SymbolTable) componentManager.getProperty("http://apache.org/xml/properties/internal/symbol-table");
        this.fGrammarPool = (XMLGrammarPool) componentManager.getProperty("http://apache.org/xml/properties/internal/grammar-pool", null);
        try {
            this.fValidator = (XMLDTDValidator) componentManager.getProperty(DTD_VALIDATOR, null);
        } catch (ClassCastException e2) {
            this.fValidator = null;
        }
        if (this.fValidator != null) {
            this.fGrammarBucket = this.fValidator.getGrammarBucket();
        } else {
            this.fGrammarBucket = null;
        }
        reset();
    }

    protected void reset() {
        this.fDTDGrammar = null;
        this.fInDTDIgnore = false;
        this.fNDataDeclNotations.clear();
        if (this.fValidation) {
            if (this.fNotationEnumVals == null) {
                this.fNotationEnumVals = new HashMap();
            }
            this.fNotationEnumVals.clear();
            this.fTableOfIDAttributeNames = new HashMap();
            this.fTableOfNOTATIONAttributeNames = new HashMap();
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLComponent
    public String[] getRecognizedFeatures() {
        return (String[]) RECOGNIZED_FEATURES.clone();
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLComponent
    public void setFeature(String featureId, boolean state) throws XMLConfigurationException {
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLComponent
    public String[] getRecognizedProperties() {
        return (String[]) RECOGNIZED_PROPERTIES.clone();
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLComponent
    public void setProperty(String propertyId, Object value) throws XMLConfigurationException {
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLComponent
    public Boolean getFeatureDefault(String featureId) {
        for (int i2 = 0; i2 < RECOGNIZED_FEATURES.length; i2++) {
            if (RECOGNIZED_FEATURES[i2].equals(featureId)) {
                return FEATURE_DEFAULTS[i2];
            }
        }
        return null;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLComponent
    public Object getPropertyDefault(String propertyId) {
        for (int i2 = 0; i2 < RECOGNIZED_PROPERTIES.length; i2++) {
            if (RECOGNIZED_PROPERTIES[i2].equals(propertyId)) {
                return PROPERTY_DEFAULTS[i2];
            }
        }
        return null;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLDTDSource
    public void setDTDHandler(XMLDTDHandler dtdHandler) {
        this.fDTDHandler = dtdHandler;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLDTDSource
    public XMLDTDHandler getDTDHandler() {
        return this.fDTDHandler;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLDTDContentModelSource
    public void setDTDContentModelHandler(XMLDTDContentModelHandler dtdContentModelHandler) {
        this.fDTDContentModelHandler = dtdContentModelHandler;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLDTDContentModelSource
    public XMLDTDContentModelHandler getDTDContentModelHandler() {
        return this.fDTDContentModelHandler;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
    public void startExternalSubset(XMLResourceIdentifier identifier, Augmentations augs) throws XNIException {
        if (this.fDTDGrammar != null) {
            this.fDTDGrammar.startExternalSubset(identifier, augs);
        }
        if (this.fDTDHandler != null) {
            this.fDTDHandler.startExternalSubset(identifier, augs);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
    public void endExternalSubset(Augmentations augs) throws XNIException {
        if (this.fDTDGrammar != null) {
            this.fDTDGrammar.endExternalSubset(augs);
        }
        if (this.fDTDHandler != null) {
            this.fDTDHandler.endExternalSubset(augs);
        }
    }

    protected static void checkStandaloneEntityRef(String name, DTDGrammar grammar, XMLEntityDecl tempEntityDecl, XMLErrorReporter errorReporter) throws XNIException {
        int entIndex = grammar.getEntityDeclIndex(name);
        if (entIndex > -1) {
            grammar.getEntityDecl(entIndex, tempEntityDecl);
            if (tempEntityDecl.inExternal) {
                errorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_REFERENCE_TO_EXTERNALLY_DECLARED_ENTITY_WHEN_STANDALONE", new Object[]{name}, (short) 1);
            }
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
    public void comment(XMLString text, Augmentations augs) throws XNIException {
        if (this.fDTDGrammar != null) {
            this.fDTDGrammar.comment(text, augs);
        }
        if (this.fDTDHandler != null) {
            this.fDTDHandler.comment(text, augs);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
    public void processingInstruction(String target, XMLString data, Augmentations augs) throws XNIException {
        if (this.fDTDGrammar != null) {
            this.fDTDGrammar.processingInstruction(target, data, augs);
        }
        if (this.fDTDHandler != null) {
            this.fDTDHandler.processingInstruction(target, data, augs);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
    public void startDTD(XMLLocator locator, Augmentations augs) throws XNIException {
        this.fNDataDeclNotations.clear();
        this.fDTDElementDecls.clear();
        if (!this.fGrammarBucket.getActiveGrammar().isImmutable()) {
            this.fDTDGrammar = this.fGrammarBucket.getActiveGrammar();
        }
        if (this.fDTDGrammar != null) {
            this.fDTDGrammar.startDTD(locator, augs);
        }
        if (this.fDTDHandler != null) {
            this.fDTDHandler.startDTD(locator, augs);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
    public void ignoredCharacters(XMLString text, Augmentations augs) throws XNIException {
        if (this.fDTDGrammar != null) {
            this.fDTDGrammar.ignoredCharacters(text, augs);
        }
        if (this.fDTDHandler != null) {
            this.fDTDHandler.ignoredCharacters(text, augs);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
    public void textDecl(String version, String encoding, Augmentations augs) throws XNIException {
        if (this.fDTDGrammar != null) {
            this.fDTDGrammar.textDecl(version, encoding, augs);
        }
        if (this.fDTDHandler != null) {
            this.fDTDHandler.textDecl(version, encoding, augs);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
    public void startParameterEntity(String name, XMLResourceIdentifier identifier, String encoding, Augmentations augs) throws XNIException {
        if (this.fPerformValidation && this.fDTDGrammar != null && this.fGrammarBucket.getStandalone()) {
            checkStandaloneEntityRef(name, this.fDTDGrammar, this.fEntityDecl, this.fErrorReporter);
        }
        if (this.fDTDGrammar != null) {
            this.fDTDGrammar.startParameterEntity(name, identifier, encoding, augs);
        }
        if (this.fDTDHandler != null) {
            this.fDTDHandler.startParameterEntity(name, identifier, encoding, augs);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
    public void endParameterEntity(String name, Augmentations augs) throws XNIException {
        if (this.fDTDGrammar != null) {
            this.fDTDGrammar.endParameterEntity(name, augs);
        }
        if (this.fDTDHandler != null) {
            this.fDTDHandler.endParameterEntity(name, augs);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
    public void elementDecl(String name, String contentModel, Augmentations augs) throws XNIException {
        if (this.fValidation) {
            if (this.fDTDElementDecls.contains(name)) {
                this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_ELEMENT_ALREADY_DECLARED", new Object[]{name}, (short) 1);
            } else {
                this.fDTDElementDecls.add(name);
            }
        }
        if (this.fDTDGrammar != null) {
            this.fDTDGrammar.elementDecl(name, contentModel, augs);
        }
        if (this.fDTDHandler != null) {
            this.fDTDHandler.elementDecl(name, contentModel, augs);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
    public void startAttlist(String elementName, Augmentations augs) throws XNIException {
        if (this.fDTDGrammar != null) {
            this.fDTDGrammar.startAttlist(elementName, augs);
        }
        if (this.fDTDHandler != null) {
            this.fDTDHandler.startAttlist(elementName, augs);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:96:0x0257  */
    @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void attributeDecl(java.lang.String r11, java.lang.String r12, java.lang.String r13, java.lang.String[] r14, java.lang.String r15, com.sun.org.apache.xerces.internal.xni.XMLString r16, com.sun.org.apache.xerces.internal.xni.XMLString r17, com.sun.org.apache.xerces.internal.xni.Augmentations r18) throws com.sun.org.apache.xerces.internal.xni.XNIException {
        /*
            Method dump skipped, instructions count: 823
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.org.apache.xerces.internal.impl.dtd.XMLDTDProcessor.attributeDecl(java.lang.String, java.lang.String, java.lang.String, java.lang.String[], java.lang.String, com.sun.org.apache.xerces.internal.xni.XMLString, com.sun.org.apache.xerces.internal.xni.XMLString, com.sun.org.apache.xerces.internal.xni.Augmentations):void");
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
    public void endAttlist(Augmentations augs) throws XNIException {
        if (this.fDTDGrammar != null) {
            this.fDTDGrammar.endAttlist(augs);
        }
        if (this.fDTDHandler != null) {
            this.fDTDHandler.endAttlist(augs);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
    public void internalEntityDecl(String name, XMLString text, XMLString nonNormalizedText, Augmentations augs) throws XNIException {
        DTDGrammar grammar = this.fDTDGrammar != null ? this.fDTDGrammar : this.fGrammarBucket.getActiveGrammar();
        int index = grammar.getEntityDeclIndex(name);
        if (index == -1) {
            if (this.fDTDGrammar != null) {
                this.fDTDGrammar.internalEntityDecl(name, text, nonNormalizedText, augs);
            }
            if (this.fDTDHandler != null) {
                this.fDTDHandler.internalEntityDecl(name, text, nonNormalizedText, augs);
            }
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
    public void externalEntityDecl(String name, XMLResourceIdentifier identifier, Augmentations augs) throws XNIException {
        DTDGrammar grammar = this.fDTDGrammar != null ? this.fDTDGrammar : this.fGrammarBucket.getActiveGrammar();
        int index = grammar.getEntityDeclIndex(name);
        if (index == -1) {
            if (this.fDTDGrammar != null) {
                this.fDTDGrammar.externalEntityDecl(name, identifier, augs);
            }
            if (this.fDTDHandler != null) {
                this.fDTDHandler.externalEntityDecl(name, identifier, augs);
            }
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
    public void unparsedEntityDecl(String name, XMLResourceIdentifier identifier, String notation, Augmentations augs) throws XNIException {
        if (this.fValidation) {
            this.fNDataDeclNotations.put(name, notation);
        }
        if (this.fDTDGrammar != null) {
            this.fDTDGrammar.unparsedEntityDecl(name, identifier, notation, augs);
        }
        if (this.fDTDHandler != null) {
            this.fDTDHandler.unparsedEntityDecl(name, identifier, notation, augs);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
    public void notationDecl(String name, XMLResourceIdentifier identifier, Augmentations augs) throws XNIException {
        if (this.fValidation) {
            DTDGrammar grammar = this.fDTDGrammar != null ? this.fDTDGrammar : this.fGrammarBucket.getActiveGrammar();
            if (grammar.getNotationDeclIndex(name) != -1) {
                this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "UniqueNotationName", new Object[]{name}, (short) 1);
            }
        }
        if (this.fDTDGrammar != null) {
            this.fDTDGrammar.notationDecl(name, identifier, augs);
        }
        if (this.fDTDHandler != null) {
            this.fDTDHandler.notationDecl(name, identifier, augs);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
    public void startConditional(short type, Augmentations augs) throws XNIException {
        this.fInDTDIgnore = type == 1;
        if (this.fDTDGrammar != null) {
            this.fDTDGrammar.startConditional(type, augs);
        }
        if (this.fDTDHandler != null) {
            this.fDTDHandler.startConditional(type, augs);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
    public void endConditional(Augmentations augs) throws XNIException {
        this.fInDTDIgnore = false;
        if (this.fDTDGrammar != null) {
            this.fDTDGrammar.endConditional(augs);
        }
        if (this.fDTDHandler != null) {
            this.fDTDHandler.endConditional(augs);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
    public void endDTD(Augmentations augs) throws XNIException {
        if (this.fDTDGrammar != null) {
            this.fDTDGrammar.endDTD(augs);
            if (this.fGrammarPool != null) {
                this.fGrammarPool.cacheGrammars("http://www.w3.org/TR/REC-xml", new Grammar[]{this.fDTDGrammar});
            }
        }
        if (this.fValidation) {
            DTDGrammar grammar = this.fDTDGrammar != null ? this.fDTDGrammar : this.fGrammarBucket.getActiveGrammar();
            for (Map.Entry entry : this.fNDataDeclNotations.entrySet()) {
                String notation = (String) entry.getValue();
                if (grammar.getNotationDeclIndex(notation) == -1) {
                    String entity = (String) entry.getKey();
                    this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_NOTATION_NOT_DECLARED_FOR_UNPARSED_ENTITYDECL", new Object[]{entity, notation}, (short) 1);
                }
            }
            for (Map.Entry entry2 : this.fNotationEnumVals.entrySet()) {
                String notation2 = (String) entry2.getKey();
                if (grammar.getNotationDeclIndex(notation2) == -1) {
                    String attributeName = (String) entry2.getValue();
                    this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_NOTATION_NOT_DECLARED_FOR_NOTATIONTYPE_ATTRIBUTE", new Object[]{attributeName, notation2}, (short) 1);
                }
            }
            for (Map.Entry entry3 : this.fTableOfNOTATIONAttributeNames.entrySet()) {
                String elementName = (String) entry3.getKey();
                int elementIndex = grammar.getElementDeclIndex(elementName);
                if (grammar.getContentSpecType(elementIndex) == 1) {
                    String attributeName2 = (String) entry3.getValue();
                    this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "NoNotationOnEmptyElement", new Object[]{elementName, attributeName2}, (short) 1);
                }
            }
            this.fTableOfIDAttributeNames = null;
            this.fTableOfNOTATIONAttributeNames = null;
            if (this.fWarnOnUndeclaredElemdef) {
                checkDeclaredElements(grammar);
            }
        }
        if (this.fDTDHandler != null) {
            this.fDTDHandler.endDTD(augs);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
    public void setDTDSource(XMLDTDSource source) {
        this.fDTDSource = source;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
    public XMLDTDSource getDTDSource() {
        return this.fDTDSource;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDContentModelHandler
    public void setDTDContentModelSource(XMLDTDContentModelSource source) {
        this.fDTDContentModelSource = source;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDContentModelHandler
    public XMLDTDContentModelSource getDTDContentModelSource() {
        return this.fDTDContentModelSource;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDContentModelHandler
    public void startContentModel(String elementName, Augmentations augs) throws XNIException {
        if (this.fValidation) {
            this.fDTDElementDeclName = elementName;
            this.fMixedElementTypes.clear();
        }
        if (this.fDTDGrammar != null) {
            this.fDTDGrammar.startContentModel(elementName, augs);
        }
        if (this.fDTDContentModelHandler != null) {
            this.fDTDContentModelHandler.startContentModel(elementName, augs);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDContentModelHandler
    public void any(Augmentations augs) throws XNIException {
        if (this.fDTDGrammar != null) {
            this.fDTDGrammar.any(augs);
        }
        if (this.fDTDContentModelHandler != null) {
            this.fDTDContentModelHandler.any(augs);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDContentModelHandler
    public void empty(Augmentations augs) throws XNIException {
        if (this.fDTDGrammar != null) {
            this.fDTDGrammar.empty(augs);
        }
        if (this.fDTDContentModelHandler != null) {
            this.fDTDContentModelHandler.empty(augs);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDContentModelHandler
    public void startGroup(Augmentations augs) throws XNIException {
        this.fMixed = false;
        if (this.fDTDGrammar != null) {
            this.fDTDGrammar.startGroup(augs);
        }
        if (this.fDTDContentModelHandler != null) {
            this.fDTDContentModelHandler.startGroup(augs);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDContentModelHandler
    public void pcdata(Augmentations augs) throws XNIException {
        this.fMixed = true;
        if (this.fDTDGrammar != null) {
            this.fDTDGrammar.pcdata(augs);
        }
        if (this.fDTDContentModelHandler != null) {
            this.fDTDContentModelHandler.pcdata(augs);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDContentModelHandler
    public void element(String elementName, Augmentations augs) throws XNIException {
        if (this.fMixed && this.fValidation) {
            if (this.fMixedElementTypes.contains(elementName)) {
                this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "DuplicateTypeInMixedContent", new Object[]{this.fDTDElementDeclName, elementName}, (short) 1);
            } else {
                this.fMixedElementTypes.add(elementName);
            }
        }
        if (this.fDTDGrammar != null) {
            this.fDTDGrammar.element(elementName, augs);
        }
        if (this.fDTDContentModelHandler != null) {
            this.fDTDContentModelHandler.element(elementName, augs);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDContentModelHandler
    public void separator(short separator, Augmentations augs) throws XNIException {
        if (this.fDTDGrammar != null) {
            this.fDTDGrammar.separator(separator, augs);
        }
        if (this.fDTDContentModelHandler != null) {
            this.fDTDContentModelHandler.separator(separator, augs);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDContentModelHandler
    public void occurrence(short occurrence, Augmentations augs) throws XNIException {
        if (this.fDTDGrammar != null) {
            this.fDTDGrammar.occurrence(occurrence, augs);
        }
        if (this.fDTDContentModelHandler != null) {
            this.fDTDContentModelHandler.occurrence(occurrence, augs);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDContentModelHandler
    public void endGroup(Augmentations augs) throws XNIException {
        if (this.fDTDGrammar != null) {
            this.fDTDGrammar.endGroup(augs);
        }
        if (this.fDTDContentModelHandler != null) {
            this.fDTDContentModelHandler.endGroup(augs);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDContentModelHandler
    public void endContentModel(Augmentations augs) throws XNIException {
        if (this.fDTDGrammar != null) {
            this.fDTDGrammar.endContentModel(augs);
        }
        if (this.fDTDContentModelHandler != null) {
            this.fDTDContentModelHandler.endContentModel(augs);
        }
    }

    private boolean normalizeDefaultAttrValue(XMLString value) {
        boolean skipSpace = true;
        int current = value.offset;
        int end = value.offset + value.length;
        for (int i2 = value.offset; i2 < end; i2++) {
            if (value.ch[i2] == ' ') {
                if (!skipSpace) {
                    int i3 = current;
                    current++;
                    value.ch[i3] = ' ';
                    skipSpace = true;
                }
            } else {
                if (current != i2) {
                    value.ch[current] = value.ch[i2];
                }
                current++;
                skipSpace = false;
            }
        }
        if (current != end) {
            if (skipSpace) {
                current--;
            }
            value.length = current - value.offset;
            return true;
        }
        return false;
    }

    protected boolean isValidNmtoken(String nmtoken) {
        return XMLChar.isValidNmtoken(nmtoken);
    }

    protected boolean isValidName(String name) {
        return XMLChar.isValidName(name);
    }

    private void checkDeclaredElements(DTDGrammar grammar) throws XNIException {
        int elementIndex = grammar.getFirstElementDeclIndex();
        XMLContentSpec contentSpec = new XMLContentSpec();
        while (elementIndex >= 0) {
            int type = grammar.getContentSpecType(elementIndex);
            if (type == 3 || type == 2) {
                checkDeclaredElements(grammar, elementIndex, grammar.getContentSpecIndex(elementIndex), contentSpec);
            }
            elementIndex = grammar.getNextElementDeclIndex(elementIndex);
        }
    }

    private void checkDeclaredElements(DTDGrammar grammar, int elementIndex, int contentSpecIndex, XMLContentSpec contentSpec) throws XNIException {
        grammar.getContentSpec(contentSpecIndex, contentSpec);
        if (contentSpec.type == 0) {
            String value = (String) contentSpec.value;
            if (value != null && grammar.getElementDeclIndex(value) == -1) {
                this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "UndeclaredElementInContentSpec", new Object[]{grammar.getElementDeclName(elementIndex).rawname, value}, (short) 0);
                return;
            }
            return;
        }
        if (contentSpec.type == 4 || contentSpec.type == 5) {
            int leftNode = ((int[]) contentSpec.value)[0];
            int rightNode = ((int[]) contentSpec.otherValue)[0];
            checkDeclaredElements(grammar, elementIndex, leftNode, contentSpec);
            checkDeclaredElements(grammar, elementIndex, rightNode, contentSpec);
            return;
        }
        if (contentSpec.type == 2 || contentSpec.type == 1 || contentSpec.type == 3) {
            int leftNode2 = ((int[]) contentSpec.value)[0];
            checkDeclaredElements(grammar, elementIndex, leftNode2, contentSpec);
        }
    }
}
