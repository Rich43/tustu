package com.sun.org.apache.xerces.internal.impl.dtd;

import com.sun.org.apache.xerces.internal.impl.Constants;
import com.sun.org.apache.xerces.internal.impl.RevalidationHandler;
import com.sun.org.apache.xerces.internal.impl.XMLEntityManager;
import com.sun.org.apache.xerces.internal.impl.XMLErrorReporter;
import com.sun.org.apache.xerces.internal.impl.dtd.models.ContentModelValidator;
import com.sun.org.apache.xerces.internal.impl.dv.DTDDVFactory;
import com.sun.org.apache.xerces.internal.impl.dv.DatatypeValidator;
import com.sun.org.apache.xerces.internal.impl.dv.InvalidDatatypeValueException;
import com.sun.org.apache.xerces.internal.impl.validation.ValidationManager;
import com.sun.org.apache.xerces.internal.impl.validation.ValidationState;
import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import com.sun.org.apache.xerces.internal.jaxp.JAXPConstants;
import com.sun.org.apache.xerces.internal.util.SymbolTable;
import com.sun.org.apache.xerces.internal.util.XMLChar;
import com.sun.org.apache.xerces.internal.util.XMLSymbols;
import com.sun.org.apache.xerces.internal.xni.Augmentations;
import com.sun.org.apache.xerces.internal.xni.NamespaceContext;
import com.sun.org.apache.xerces.internal.xni.QName;
import com.sun.org.apache.xerces.internal.xni.XMLAttributes;
import com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler;
import com.sun.org.apache.xerces.internal.xni.XMLLocator;
import com.sun.org.apache.xerces.internal.xni.XMLResourceIdentifier;
import com.sun.org.apache.xerces.internal.xni.XMLString;
import com.sun.org.apache.xerces.internal.xni.XNIException;
import com.sun.org.apache.xerces.internal.xni.grammars.Grammar;
import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool;
import com.sun.org.apache.xerces.internal.xni.parser.XMLComponent;
import com.sun.org.apache.xerces.internal.xni.parser.XMLComponentManager;
import com.sun.org.apache.xerces.internal.xni.parser.XMLConfigurationException;
import com.sun.org.apache.xerces.internal.xni.parser.XMLDocumentFilter;
import com.sun.org.apache.xerces.internal.xni.parser.XMLDocumentSource;
import java.io.IOException;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/dtd/XMLDTDValidator.class */
public class XMLDTDValidator implements XMLComponent, XMLDocumentFilter, XMLDTDValidatorFilter, RevalidationHandler {
    private static final int TOP_LEVEL_SCOPE = -1;
    protected static final String NAMESPACES = "http://xml.org/sax/features/namespaces";
    protected static final String WARN_ON_DUPLICATE_ATTDEF = "http://apache.org/xml/features/validation/warn-on-duplicate-attdef";
    protected static final String PARSER_SETTINGS = "http://apache.org/xml/features/internal/parser-settings";
    protected static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
    protected static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
    protected static final String GRAMMAR_POOL = "http://apache.org/xml/properties/internal/grammar-pool";
    private static final boolean DEBUG_ATTRIBUTES = false;
    private static final boolean DEBUG_ELEMENT_CHILDREN = false;
    protected boolean fNamespaces;
    protected boolean fValidation;
    protected boolean fDTDValidation;
    protected boolean fDynamicValidation;
    protected boolean fBalanceSyntaxTrees;
    protected boolean fWarnDuplicateAttdef;
    protected SymbolTable fSymbolTable;
    protected XMLErrorReporter fErrorReporter;
    protected XMLGrammarPool fGrammarPool;
    protected DTDGrammarBucket fGrammarBucket;
    protected XMLLocator fDocLocation;
    protected DTDDVFactory fDatatypeValidatorFactory;
    protected XMLDocumentHandler fDocumentHandler;
    protected XMLDocumentSource fDocumentSource;
    protected DTDGrammar fDTDGrammar;
    private boolean fPerformValidation;
    private String fSchemaType;
    protected DatatypeValidator fValID;
    protected DatatypeValidator fValIDRef;
    protected DatatypeValidator fValIDRefs;
    protected DatatypeValidator fValENTITY;
    protected DatatypeValidator fValENTITIES;
    protected DatatypeValidator fValNMTOKEN;
    protected DatatypeValidator fValNMTOKENS;
    protected DatatypeValidator fValNOTATION;
    protected static final String VALIDATION = "http://xml.org/sax/features/validation";
    protected static final String DYNAMIC_VALIDATION = "http://apache.org/xml/features/validation/dynamic";
    protected static final String BALANCE_SYNTAX_TREES = "http://apache.org/xml/features/validation/balance-syntax-trees";
    private static final String[] RECOGNIZED_FEATURES = {"http://xml.org/sax/features/namespaces", VALIDATION, DYNAMIC_VALIDATION, BALANCE_SYNTAX_TREES};
    private static final Boolean[] FEATURE_DEFAULTS = {null, null, Boolean.FALSE, Boolean.FALSE};
    protected static final String DATATYPE_VALIDATOR_FACTORY = "http://apache.org/xml/properties/internal/datatype-validator-factory";
    protected static final String VALIDATION_MANAGER = "http://apache.org/xml/properties/internal/validation-manager";
    private static final String[] RECOGNIZED_PROPERTIES = {"http://apache.org/xml/properties/internal/symbol-table", "http://apache.org/xml/properties/internal/error-reporter", "http://apache.org/xml/properties/internal/grammar-pool", DATATYPE_VALIDATOR_FACTORY, VALIDATION_MANAGER};
    private static final Object[] PROPERTY_DEFAULTS = {null, null, null, null, null};
    protected ValidationManager fValidationManager = null;
    protected final ValidationState fValidationState = new ValidationState();
    protected NamespaceContext fNamespaceContext = null;
    protected boolean fSeenDoctypeDecl = false;
    private final QName fCurrentElement = new QName();
    private int fCurrentElementIndex = -1;
    private int fCurrentContentSpecType = -1;
    private final QName fRootElement = new QName();
    private boolean fInCDATASection = false;
    private int[] fElementIndexStack = new int[8];
    private int[] fContentSpecTypeStack = new int[8];
    private QName[] fElementQNamePartsStack = new QName[8];
    private QName[] fElementChildren = new QName[32];
    private int fElementChildrenLength = 0;
    private int[] fElementChildrenOffsetStack = new int[32];
    private int fElementDepth = -1;
    private boolean fSeenRootElement = false;
    private boolean fInElementContent = false;
    private XMLElementDecl fTempElementDecl = new XMLElementDecl();
    private final XMLAttributeDecl fTempAttDecl = new XMLAttributeDecl();
    private final XMLEntityDecl fEntityDecl = new XMLEntityDecl();
    private final QName fTempQName = new QName();
    private final StringBuffer fBuffer = new StringBuffer();

    public XMLDTDValidator() {
        for (int i2 = 0; i2 < this.fElementQNamePartsStack.length; i2++) {
            this.fElementQNamePartsStack[i2] = new QName();
        }
        this.fGrammarBucket = new DTDGrammarBucket();
    }

    DTDGrammarBucket getGrammarBucket() {
        return this.fGrammarBucket;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLComponent
    public void reset(XMLComponentManager componentManager) throws XMLConfigurationException {
        this.fDTDGrammar = null;
        this.fSeenDoctypeDecl = false;
        this.fInCDATASection = false;
        this.fSeenRootElement = false;
        this.fInElementContent = false;
        this.fCurrentElementIndex = -1;
        this.fCurrentContentSpecType = -1;
        this.fRootElement.clear();
        this.fValidationState.resetIDTables();
        this.fGrammarBucket.clear();
        this.fElementDepth = -1;
        this.fElementChildrenLength = 0;
        boolean parser_settings = componentManager.getFeature(PARSER_SETTINGS, true);
        if (!parser_settings) {
            this.fValidationManager.addValidationState(this.fValidationState);
            return;
        }
        this.fNamespaces = componentManager.getFeature("http://xml.org/sax/features/namespaces", true);
        this.fValidation = componentManager.getFeature(VALIDATION, false);
        this.fDTDValidation = !componentManager.getFeature("http://apache.org/xml/features/validation/schema", false);
        this.fDynamicValidation = componentManager.getFeature(DYNAMIC_VALIDATION, false);
        this.fBalanceSyntaxTrees = componentManager.getFeature(BALANCE_SYNTAX_TREES, false);
        this.fWarnDuplicateAttdef = componentManager.getFeature(WARN_ON_DUPLICATE_ATTDEF, false);
        this.fSchemaType = (String) componentManager.getProperty(JAXPConstants.JAXP_SCHEMA_LANGUAGE, null);
        this.fValidationManager = (ValidationManager) componentManager.getProperty(VALIDATION_MANAGER);
        this.fValidationManager.addValidationState(this.fValidationState);
        this.fValidationState.setUsingNamespaces(this.fNamespaces);
        this.fErrorReporter = (XMLErrorReporter) componentManager.getProperty("http://apache.org/xml/properties/internal/error-reporter");
        this.fSymbolTable = (SymbolTable) componentManager.getProperty("http://apache.org/xml/properties/internal/symbol-table");
        this.fGrammarPool = (XMLGrammarPool) componentManager.getProperty("http://apache.org/xml/properties/internal/grammar-pool", null);
        this.fDatatypeValidatorFactory = (DTDDVFactory) componentManager.getProperty(DATATYPE_VALIDATOR_FACTORY);
        init();
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

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLDocumentSource
    public void setDocumentHandler(XMLDocumentHandler documentHandler) {
        this.fDocumentHandler = documentHandler;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLDocumentSource
    public XMLDocumentHandler getDocumentHandler() {
        return this.fDocumentHandler;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void setDocumentSource(XMLDocumentSource source) {
        this.fDocumentSource = source;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public XMLDocumentSource getDocumentSource() {
        return this.fDocumentSource;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void startDocument(XMLLocator locator, String encoding, NamespaceContext namespaceContext, Augmentations augs) throws XNIException {
        if (this.fGrammarPool != null) {
            Grammar[] grammars = this.fGrammarPool.retrieveInitialGrammarSet("http://www.w3.org/TR/REC-xml");
            int length = grammars != null ? grammars.length : 0;
            for (int i2 = 0; i2 < length; i2++) {
                this.fGrammarBucket.putGrammar((DTDGrammar) grammars[i2]);
            }
        }
        this.fDocLocation = locator;
        this.fNamespaceContext = namespaceContext;
        if (this.fDocumentHandler != null) {
            this.fDocumentHandler.startDocument(locator, encoding, namespaceContext, augs);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void xmlDecl(String version, String encoding, String standalone, Augmentations augs) throws XNIException {
        this.fGrammarBucket.setStandalone(standalone != null && standalone.equals("yes"));
        if (this.fDocumentHandler != null) {
            this.fDocumentHandler.xmlDecl(version, encoding, standalone, augs);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void doctypeDecl(String rootElement, String publicId, String systemId, Augmentations augs) throws XNIException {
        this.fSeenDoctypeDecl = true;
        this.fRootElement.setValues(null, rootElement, rootElement, null);
        String eid = null;
        try {
            eid = XMLEntityManager.expandSystemId(systemId, this.fDocLocation.getExpandedSystemId(), false);
        } catch (IOException e2) {
        }
        XMLDTDDescription grammarDesc = new XMLDTDDescription(publicId, systemId, this.fDocLocation.getExpandedSystemId(), eid, rootElement);
        this.fDTDGrammar = this.fGrammarBucket.getGrammar(grammarDesc);
        if (this.fDTDGrammar == null && this.fGrammarPool != null && (systemId != null || publicId != null)) {
            this.fDTDGrammar = (DTDGrammar) this.fGrammarPool.retrieveGrammar(grammarDesc);
        }
        if (this.fDTDGrammar == null) {
            if (!this.fBalanceSyntaxTrees) {
                this.fDTDGrammar = new DTDGrammar(this.fSymbolTable, grammarDesc);
            } else {
                this.fDTDGrammar = new BalancedDTDGrammar(this.fSymbolTable, grammarDesc);
            }
        } else {
            this.fValidationManager.setCachedDTD(true);
        }
        this.fGrammarBucket.setActiveGrammar(this.fDTDGrammar);
        if (this.fDocumentHandler != null) {
            this.fDocumentHandler.doctypeDecl(rootElement, publicId, systemId, augs);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void startElement(QName element, XMLAttributes attributes, Augmentations augs) throws XNIException {
        handleStartElement(element, attributes, augs);
        if (this.fDocumentHandler != null) {
            this.fDocumentHandler.startElement(element, attributes, augs);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void emptyElement(QName element, XMLAttributes attributes, Augmentations augs) throws XNIException {
        boolean removed = handleStartElement(element, attributes, augs);
        if (this.fDocumentHandler != null) {
            this.fDocumentHandler.emptyElement(element, attributes, augs);
        }
        if (!removed) {
            handleEndElement(element, augs, true);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void characters(XMLString text, Augmentations augs) throws XNIException {
        boolean callNextCharacters = true;
        boolean allWhiteSpace = true;
        int i2 = text.offset;
        while (true) {
            if (i2 >= text.offset + text.length) {
                break;
            }
            if (isSpace(text.ch[i2])) {
                i2++;
            } else {
                allWhiteSpace = false;
                break;
            }
        }
        if (this.fInElementContent && allWhiteSpace && !this.fInCDATASection && this.fDocumentHandler != null) {
            this.fDocumentHandler.ignorableWhitespace(text, augs);
            callNextCharacters = false;
        }
        if (this.fPerformValidation) {
            if (this.fInElementContent) {
                if (this.fGrammarBucket.getStandalone() && this.fDTDGrammar.getElementDeclIsExternal(this.fCurrentElementIndex) && allWhiteSpace) {
                    this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_WHITE_SPACE_IN_ELEMENT_CONTENT_WHEN_STANDALONE", null, (short) 1);
                }
                if (!allWhiteSpace) {
                    charDataInContent();
                }
                if (augs != null && augs.getItem(Constants.CHAR_REF_PROBABLE_WS) == Boolean.TRUE) {
                    this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_CONTENT_INVALID_SPECIFIED", new Object[]{this.fCurrentElement.rawname, this.fDTDGrammar.getContentSpecAsString(this.fElementDepth), "character reference"}, (short) 1);
                }
            }
            if (this.fCurrentContentSpecType == 1) {
                charDataInContent();
            }
        }
        if (callNextCharacters && this.fDocumentHandler != null) {
            this.fDocumentHandler.characters(text, augs);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void ignorableWhitespace(XMLString text, Augmentations augs) throws XNIException {
        if (this.fDocumentHandler != null) {
            this.fDocumentHandler.ignorableWhitespace(text, augs);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void endElement(QName element, Augmentations augs) throws XNIException {
        handleEndElement(element, augs, false);
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void startCDATA(Augmentations augs) throws XNIException {
        if (this.fPerformValidation && this.fInElementContent) {
            charDataInContent();
        }
        this.fInCDATASection = true;
        if (this.fDocumentHandler != null) {
            this.fDocumentHandler.startCDATA(augs);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void endCDATA(Augmentations augs) throws XNIException {
        this.fInCDATASection = false;
        if (this.fDocumentHandler != null) {
            this.fDocumentHandler.endCDATA(augs);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void endDocument(Augmentations augs) throws XNIException {
        if (this.fDocumentHandler != null) {
            this.fDocumentHandler.endDocument(augs);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void comment(XMLString text, Augmentations augs) throws XNIException {
        if (this.fPerformValidation && this.fElementDepth >= 0 && this.fDTDGrammar != null) {
            this.fDTDGrammar.getElementDecl(this.fCurrentElementIndex, this.fTempElementDecl);
            if (this.fTempElementDecl.type == 1) {
                this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_CONTENT_INVALID_SPECIFIED", new Object[]{this.fCurrentElement.rawname, "EMPTY", "comment"}, (short) 1);
            }
        }
        if (this.fDocumentHandler != null) {
            this.fDocumentHandler.comment(text, augs);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void processingInstruction(String target, XMLString data, Augmentations augs) throws XNIException {
        if (this.fPerformValidation && this.fElementDepth >= 0 && this.fDTDGrammar != null) {
            this.fDTDGrammar.getElementDecl(this.fCurrentElementIndex, this.fTempElementDecl);
            if (this.fTempElementDecl.type == 1) {
                this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_CONTENT_INVALID_SPECIFIED", new Object[]{this.fCurrentElement.rawname, "EMPTY", "processing instruction"}, (short) 1);
            }
        }
        if (this.fDocumentHandler != null) {
            this.fDocumentHandler.processingInstruction(target, data, augs);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void startGeneralEntity(String name, XMLResourceIdentifier identifier, String encoding, Augmentations augs) throws XNIException {
        if (this.fPerformValidation && this.fElementDepth >= 0 && this.fDTDGrammar != null) {
            this.fDTDGrammar.getElementDecl(this.fCurrentElementIndex, this.fTempElementDecl);
            if (this.fTempElementDecl.type == 1) {
                this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_CONTENT_INVALID_SPECIFIED", new Object[]{this.fCurrentElement.rawname, "EMPTY", SchemaSymbols.ATTVAL_ENTITY}, (short) 1);
            }
            if (this.fGrammarBucket.getStandalone()) {
                XMLDTDLoader.checkStandaloneEntityRef(name, this.fDTDGrammar, this.fEntityDecl, this.fErrorReporter);
            }
        }
        if (this.fDocumentHandler != null) {
            this.fDocumentHandler.startGeneralEntity(name, identifier, encoding, augs);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void endGeneralEntity(String name, Augmentations augs) throws XNIException {
        if (this.fDocumentHandler != null) {
            this.fDocumentHandler.endGeneralEntity(name, augs);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void textDecl(String version, String encoding, Augmentations augs) throws XNIException {
        if (this.fDocumentHandler != null) {
            this.fDocumentHandler.textDecl(version, encoding, augs);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dtd.XMLDTDValidatorFilter
    public final boolean hasGrammar() {
        return this.fDTDGrammar != null;
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dtd.XMLDTDValidatorFilter
    public final boolean validate() {
        return this.fSchemaType != Constants.NS_XMLSCHEMA && ((!this.fDynamicValidation && this.fValidation) || (this.fDynamicValidation && this.fSeenDoctypeDecl)) && (this.fDTDValidation || this.fSeenDoctypeDecl);
    }

    protected void addDTDDefaultAttrsAndValidate(QName elementName, int elementIndex, XMLAttributes attributes) throws XNIException {
        int position;
        String nonNormalizedValue;
        String entityName;
        int index;
        if (elementIndex == -1 || this.fDTDGrammar == null) {
            return;
        }
        int firstAttributeDeclIndex = this.fDTDGrammar.getFirstAttributeDeclIndex(elementIndex);
        while (true) {
            int attlistIndex = firstAttributeDeclIndex;
            if (attlistIndex == -1) {
                break;
            }
            this.fDTDGrammar.getAttributeDecl(attlistIndex, this.fTempAttDecl);
            String attPrefix = this.fTempAttDecl.name.prefix;
            String attLocalpart = this.fTempAttDecl.name.localpart;
            String attRawName = this.fTempAttDecl.name.rawname;
            String attType = getAttributeTypeName(this.fTempAttDecl);
            int attDefaultType = this.fTempAttDecl.simpleType.defaultType;
            String attValue = null;
            if (this.fTempAttDecl.simpleType.defaultValue != null) {
                attValue = this.fTempAttDecl.simpleType.defaultValue;
            }
            boolean specified = false;
            boolean required = attDefaultType == 2;
            boolean cdata = attType == XMLSymbols.fCDATASymbol;
            if (!cdata || required || attValue != null) {
                int attrCount = attributes.getLength();
                int i2 = 0;
                while (true) {
                    if (i2 >= attrCount) {
                        break;
                    }
                    if (attributes.getQName(i2) != attRawName) {
                        i2++;
                    } else {
                        specified = true;
                        break;
                    }
                }
            }
            if (!specified) {
                if (required) {
                    if (this.fPerformValidation) {
                        Object[] args = {elementName.localpart, attRawName};
                        this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_REQUIRED_ATTRIBUTE_NOT_SPECIFIED", args, (short) 1);
                    }
                } else if (attValue != null) {
                    if (this.fPerformValidation && this.fGrammarBucket.getStandalone() && this.fDTDGrammar.getAttributeDeclIsExternal(attlistIndex)) {
                        Object[] args2 = {elementName.localpart, attRawName};
                        this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_DEFAULTED_ATTRIBUTE_NOT_SPECIFIED", args2, (short) 1);
                    }
                    if (this.fNamespaces && (index = attRawName.indexOf(58)) != -1) {
                        attPrefix = this.fSymbolTable.addSymbol(attRawName.substring(0, index));
                        attLocalpart = this.fSymbolTable.addSymbol(attRawName.substring(index + 1));
                    }
                    this.fTempQName.setValues(attPrefix, attLocalpart, attRawName, this.fTempAttDecl.name.uri);
                    attributes.addAttribute(this.fTempQName, attType, attValue);
                }
            }
            firstAttributeDeclIndex = this.fDTDGrammar.getNextAttributeDeclIndex(attlistIndex);
        }
        int attrCount2 = attributes.getLength();
        for (int i3 = 0; i3 < attrCount2; i3++) {
            String attrRawName = attributes.getQName(i3);
            boolean declared = false;
            if (this.fPerformValidation && this.fGrammarBucket.getStandalone() && (nonNormalizedValue = attributes.getNonNormalizedValue(i3)) != null && (entityName = getExternalEntityRefInAttrValue(nonNormalizedValue)) != null) {
                this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_REFERENCE_TO_EXTERNALLY_DECLARED_ENTITY_WHEN_STANDALONE", new Object[]{entityName}, (short) 1);
            }
            int firstAttributeDeclIndex2 = this.fDTDGrammar.getFirstAttributeDeclIndex(elementIndex);
            while (true) {
                position = firstAttributeDeclIndex2;
                if (position == -1) {
                    break;
                }
                this.fDTDGrammar.getAttributeDecl(position, this.fTempAttDecl);
                if (this.fTempAttDecl.name.rawname == attrRawName) {
                    declared = true;
                    break;
                }
                firstAttributeDeclIndex2 = this.fDTDGrammar.getNextAttributeDeclIndex(position);
            }
            if (!declared) {
                if (this.fPerformValidation) {
                    Object[] args3 = {elementName.rawname, attrRawName};
                    this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_ATTRIBUTE_NOT_DECLARED", args3, (short) 1);
                }
            } else {
                String type = getAttributeTypeName(this.fTempAttDecl);
                attributes.setType(i3, type);
                attributes.getAugmentations(i3).putItem(Constants.ATTRIBUTE_DECLARED, Boolean.TRUE);
                String oldValue = attributes.getValue(i3);
                String attrValue = oldValue;
                if (attributes.isSpecified(i3) && type != XMLSymbols.fCDATASymbol) {
                    boolean changedByNormalization = normalizeAttrValue(attributes, i3);
                    attrValue = attributes.getValue(i3);
                    if (this.fPerformValidation && this.fGrammarBucket.getStandalone() && changedByNormalization && this.fDTDGrammar.getAttributeDeclIsExternal(position)) {
                        this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_ATTVALUE_CHANGED_DURING_NORMALIZATION_WHEN_STANDALONE", new Object[]{attrRawName, oldValue, attrValue}, (short) 1);
                    }
                }
                if (this.fPerformValidation) {
                    if (this.fTempAttDecl.simpleType.defaultType == 1) {
                        String defaultValue = this.fTempAttDecl.simpleType.defaultValue;
                        if (!attrValue.equals(defaultValue)) {
                            Object[] args4 = {elementName.localpart, attrRawName, attrValue, defaultValue};
                            this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_FIXED_ATTVALUE_INVALID", args4, (short) 1);
                        }
                    }
                    if (this.fTempAttDecl.simpleType.type == 1 || this.fTempAttDecl.simpleType.type == 2 || this.fTempAttDecl.simpleType.type == 3 || this.fTempAttDecl.simpleType.type == 4 || this.fTempAttDecl.simpleType.type == 5 || this.fTempAttDecl.simpleType.type == 6) {
                        validateDTDattribute(elementName, attrValue, this.fTempAttDecl);
                    }
                }
            }
        }
    }

    protected String getExternalEntityRefInAttrValue(String nonNormalizedValue) {
        String entityName;
        int valLength = nonNormalizedValue.length();
        int iIndexOf = nonNormalizedValue.indexOf(38);
        while (true) {
            int ampIndex = iIndexOf;
            if (ampIndex != -1) {
                if (ampIndex + 1 < valLength && nonNormalizedValue.charAt(ampIndex + 1) != '#') {
                    int semicolonIndex = nonNormalizedValue.indexOf(59, ampIndex + 1);
                    entityName = this.fSymbolTable.addSymbol(nonNormalizedValue.substring(ampIndex + 1, semicolonIndex));
                    int entIndex = this.fDTDGrammar.getEntityDeclIndex(entityName);
                    if (entIndex > -1) {
                        this.fDTDGrammar.getEntityDecl(entIndex, this.fEntityDecl);
                        if (this.fEntityDecl.inExternal) {
                            break;
                        }
                        String externalEntityRefInAttrValue = getExternalEntityRefInAttrValue(this.fEntityDecl.value);
                        entityName = externalEntityRefInAttrValue;
                        if (externalEntityRefInAttrValue != null) {
                            break;
                        }
                    } else {
                        continue;
                    }
                }
                iIndexOf = nonNormalizedValue.indexOf(38, ampIndex + 1);
            } else {
                return null;
            }
        }
        return entityName;
    }

    protected void validateDTDattribute(QName element, String attValue, XMLAttributeDecl attributeDecl) throws XNIException {
        switch (attributeDecl.simpleType.type) {
            case 1:
                try {
                    if (attributeDecl.simpleType.list) {
                        this.fValENTITIES.validate(attValue, this.fValidationState);
                    } else {
                        this.fValENTITY.validate(attValue, this.fValidationState);
                    }
                    break;
                } catch (InvalidDatatypeValueException ex) {
                    this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", ex.getKey(), ex.getArgs(), (short) 1);
                    return;
                }
            case 2:
            case 6:
                boolean found = false;
                String[] enumVals = attributeDecl.simpleType.enumeration;
                if (enumVals == null) {
                    found = false;
                } else {
                    for (int i2 = 0; i2 < enumVals.length; i2++) {
                        if (attValue == enumVals[i2] || attValue.equals(enumVals[i2])) {
                            found = true;
                        }
                    }
                }
                if (!found) {
                    StringBuffer enumValueString = new StringBuffer();
                    if (enumVals != null) {
                        for (String str : enumVals) {
                            enumValueString.append(str + " ");
                        }
                    }
                    this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_ATTRIBUTE_VALUE_NOT_IN_LIST", new Object[]{attributeDecl.name.rawname, attValue, enumValueString}, (short) 1);
                    break;
                }
                break;
            case 3:
                try {
                    this.fValID.validate(attValue, this.fValidationState);
                    break;
                } catch (InvalidDatatypeValueException ex2) {
                    this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", ex2.getKey(), ex2.getArgs(), (short) 1);
                    return;
                }
            case 4:
                boolean isAlistAttribute = attributeDecl.simpleType.list;
                try {
                    if (isAlistAttribute) {
                        this.fValIDRefs.validate(attValue, this.fValidationState);
                    } else {
                        this.fValIDRef.validate(attValue, this.fValidationState);
                    }
                    break;
                } catch (InvalidDatatypeValueException ex3) {
                    if (isAlistAttribute) {
                        this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "IDREFSInvalid", new Object[]{attValue}, (short) 1);
                        return;
                    } else {
                        this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", ex3.getKey(), ex3.getArgs(), (short) 1);
                        return;
                    }
                }
            case 5:
                boolean isAlistAttribute2 = attributeDecl.simpleType.list;
                try {
                    if (isAlistAttribute2) {
                        this.fValNMTOKENS.validate(attValue, this.fValidationState);
                    } else {
                        this.fValNMTOKEN.validate(attValue, this.fValidationState);
                    }
                    break;
                } catch (InvalidDatatypeValueException e2) {
                    if (isAlistAttribute2) {
                        this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "NMTOKENSInvalid", new Object[]{attValue}, (short) 1);
                        return;
                    } else {
                        this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "NMTOKENInvalid", new Object[]{attValue}, (short) 1);
                        return;
                    }
                }
        }
    }

    protected boolean invalidStandaloneAttDef(QName element, QName attribute) {
        return true;
    }

    private boolean normalizeAttrValue(XMLAttributes attributes, int index) {
        boolean leadingSpace = true;
        boolean spaceStart = false;
        boolean readingNonSpace = false;
        int count = 0;
        int eaten = 0;
        String attrValue = attributes.getValue(index);
        char[] attValue = new char[attrValue.length()];
        this.fBuffer.setLength(0);
        attrValue.getChars(0, attrValue.length(), attValue, 0);
        for (int i2 = 0; i2 < attValue.length; i2++) {
            if (attValue[i2] == ' ') {
                if (readingNonSpace) {
                    spaceStart = true;
                    readingNonSpace = false;
                }
                if (spaceStart && !leadingSpace) {
                    spaceStart = false;
                    this.fBuffer.append(attValue[i2]);
                    count++;
                } else if (leadingSpace || !spaceStart) {
                    eaten++;
                }
            } else {
                readingNonSpace = true;
                spaceStart = false;
                leadingSpace = false;
                this.fBuffer.append(attValue[i2]);
                count++;
            }
        }
        if (count > 0 && this.fBuffer.charAt(count - 1) == ' ') {
            this.fBuffer.setLength(count - 1);
        }
        String newValue = this.fBuffer.toString();
        attributes.setValue(index, newValue);
        return !attrValue.equals(newValue);
    }

    private final void rootElementSpecified(QName rootElement) throws XNIException {
        if (this.fPerformValidation) {
            String root1 = this.fRootElement.rawname;
            String root2 = rootElement.rawname;
            if (root1 == null || !root1.equals(root2)) {
                this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "RootElementTypeMustMatchDoctypedecl", new Object[]{root1, root2}, (short) 1);
            }
        }
    }

    private int checkContent(int elementIndex, QName[] children, int childOffset, int childCount) throws XNIException {
        this.fDTDGrammar.getElementDecl(elementIndex, this.fTempElementDecl);
        String str = this.fCurrentElement.rawname;
        int contentType = this.fCurrentContentSpecType;
        if (contentType == 1) {
            if (childCount != 0) {
                return 0;
            }
            return -1;
        }
        if (contentType != 0) {
            if (contentType == 2 || contentType == 3) {
                ContentModelValidator cmElem = this.fTempElementDecl.contentModelValidator;
                int result = cmElem.validate(children, childOffset, childCount);
                return result;
            }
            if (contentType != -1 && contentType == 4) {
            }
            return -1;
        }
        return -1;
    }

    private int getContentSpecType(int elementIndex) {
        int contentSpecType = -1;
        if (elementIndex > -1 && this.fDTDGrammar.getElementDecl(elementIndex, this.fTempElementDecl)) {
            contentSpecType = this.fTempElementDecl.type;
        }
        return contentSpecType;
    }

    private void charDataInContent() {
        if (this.fElementChildren.length <= this.fElementChildrenLength) {
            QName[] newarray = new QName[this.fElementChildren.length * 2];
            System.arraycopy(this.fElementChildren, 0, newarray, 0, this.fElementChildren.length);
            this.fElementChildren = newarray;
        }
        QName qname = this.fElementChildren[this.fElementChildrenLength];
        if (qname == null) {
            for (int i2 = this.fElementChildrenLength; i2 < this.fElementChildren.length; i2++) {
                this.fElementChildren[i2] = new QName();
            }
            qname = this.fElementChildren[this.fElementChildrenLength];
        }
        qname.clear();
        this.fElementChildrenLength++;
    }

    private String getAttributeTypeName(XMLAttributeDecl attrDecl) {
        switch (attrDecl.simpleType.type) {
            case 1:
                return attrDecl.simpleType.list ? XMLSymbols.fENTITIESSymbol : XMLSymbols.fENTITYSymbol;
            case 2:
                StringBuffer buffer = new StringBuffer();
                buffer.append('(');
                for (int i2 = 0; i2 < attrDecl.simpleType.enumeration.length; i2++) {
                    if (i2 > 0) {
                        buffer.append('|');
                    }
                    buffer.append(attrDecl.simpleType.enumeration[i2]);
                }
                buffer.append(')');
                return this.fSymbolTable.addSymbol(buffer.toString());
            case 3:
                return XMLSymbols.fIDSymbol;
            case 4:
                return attrDecl.simpleType.list ? XMLSymbols.fIDREFSSymbol : XMLSymbols.fIDREFSymbol;
            case 5:
                return attrDecl.simpleType.list ? XMLSymbols.fNMTOKENSSymbol : XMLSymbols.fNMTOKENSymbol;
            case 6:
                return XMLSymbols.fNOTATIONSymbol;
            default:
                return XMLSymbols.fCDATASymbol;
        }
    }

    protected void init() {
        if (this.fValidation || this.fDynamicValidation) {
            try {
                this.fValID = this.fDatatypeValidatorFactory.getBuiltInDV(XMLSymbols.fIDSymbol);
                this.fValIDRef = this.fDatatypeValidatorFactory.getBuiltInDV(XMLSymbols.fIDREFSymbol);
                this.fValIDRefs = this.fDatatypeValidatorFactory.getBuiltInDV(XMLSymbols.fIDREFSSymbol);
                this.fValENTITY = this.fDatatypeValidatorFactory.getBuiltInDV(XMLSymbols.fENTITYSymbol);
                this.fValENTITIES = this.fDatatypeValidatorFactory.getBuiltInDV(XMLSymbols.fENTITIESSymbol);
                this.fValNMTOKEN = this.fDatatypeValidatorFactory.getBuiltInDV(XMLSymbols.fNMTOKENSymbol);
                this.fValNMTOKENS = this.fDatatypeValidatorFactory.getBuiltInDV(XMLSymbols.fNMTOKENSSymbol);
                this.fValNOTATION = this.fDatatypeValidatorFactory.getBuiltInDV(XMLSymbols.fNOTATIONSymbol);
            } catch (Exception e2) {
                e2.printStackTrace(System.err);
            }
        }
    }

    private void ensureStackCapacity(int newElementDepth) {
        if (newElementDepth == this.fElementQNamePartsStack.length) {
            QName[] newStackOfQueue = new QName[newElementDepth * 2];
            System.arraycopy(this.fElementQNamePartsStack, 0, newStackOfQueue, 0, newElementDepth);
            this.fElementQNamePartsStack = newStackOfQueue;
            QName qname = this.fElementQNamePartsStack[newElementDepth];
            if (qname == null) {
                for (int i2 = newElementDepth; i2 < this.fElementQNamePartsStack.length; i2++) {
                    this.fElementQNamePartsStack[i2] = new QName();
                }
            }
            int[] newStack = new int[newElementDepth * 2];
            System.arraycopy(this.fElementIndexStack, 0, newStack, 0, newElementDepth);
            this.fElementIndexStack = newStack;
            int[] newStack2 = new int[newElementDepth * 2];
            System.arraycopy(this.fContentSpecTypeStack, 0, newStack2, 0, newElementDepth);
            this.fContentSpecTypeStack = newStack2;
        }
    }

    protected boolean handleStartElement(QName element, XMLAttributes attributes, Augmentations augs) throws XNIException {
        if (!this.fSeenRootElement) {
            this.fPerformValidation = validate();
            this.fSeenRootElement = true;
            this.fValidationManager.setEntityState(this.fDTDGrammar);
            this.fValidationManager.setGrammarFound(this.fSeenDoctypeDecl);
            rootElementSpecified(element);
        }
        if (this.fDTDGrammar == null) {
            if (!this.fPerformValidation) {
                this.fCurrentElementIndex = -1;
                this.fCurrentContentSpecType = -1;
                this.fInElementContent = false;
            }
            if (this.fPerformValidation) {
                this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_GRAMMAR_NOT_FOUND", new Object[]{element.rawname}, (short) 1);
            }
            if (this.fDocumentSource != null) {
                this.fDocumentSource.setDocumentHandler(this.fDocumentHandler);
                if (this.fDocumentHandler != null) {
                    this.fDocumentHandler.setDocumentSource(this.fDocumentSource);
                    return true;
                }
                return true;
            }
        } else {
            this.fCurrentElementIndex = this.fDTDGrammar.getElementDeclIndex(element);
            this.fCurrentContentSpecType = this.fDTDGrammar.getContentSpecType(this.fCurrentElementIndex);
            if (this.fCurrentContentSpecType == -1 && this.fPerformValidation) {
                this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_ELEMENT_NOT_DECLARED", new Object[]{element.rawname}, (short) 1);
            }
            addDTDDefaultAttrsAndValidate(element, this.fCurrentElementIndex, attributes);
        }
        this.fInElementContent = this.fCurrentContentSpecType == 3;
        this.fElementDepth++;
        if (this.fPerformValidation) {
            if (this.fElementChildrenOffsetStack.length <= this.fElementDepth) {
                int[] newarray = new int[this.fElementChildrenOffsetStack.length * 2];
                System.arraycopy(this.fElementChildrenOffsetStack, 0, newarray, 0, this.fElementChildrenOffsetStack.length);
                this.fElementChildrenOffsetStack = newarray;
            }
            this.fElementChildrenOffsetStack[this.fElementDepth] = this.fElementChildrenLength;
            if (this.fElementChildren.length <= this.fElementChildrenLength) {
                QName[] newarray2 = new QName[this.fElementChildrenLength * 2];
                System.arraycopy(this.fElementChildren, 0, newarray2, 0, this.fElementChildren.length);
                this.fElementChildren = newarray2;
            }
            QName qname = this.fElementChildren[this.fElementChildrenLength];
            if (qname == null) {
                for (int i2 = this.fElementChildrenLength; i2 < this.fElementChildren.length; i2++) {
                    this.fElementChildren[i2] = new QName();
                }
                qname = this.fElementChildren[this.fElementChildrenLength];
            }
            qname.setValues(element);
            this.fElementChildrenLength++;
        }
        this.fCurrentElement.setValues(element);
        ensureStackCapacity(this.fElementDepth);
        this.fElementQNamePartsStack[this.fElementDepth].setValues(this.fCurrentElement);
        this.fElementIndexStack[this.fElementDepth] = this.fCurrentElementIndex;
        this.fContentSpecTypeStack[this.fElementDepth] = this.fCurrentContentSpecType;
        startNamespaceScope(element, attributes, augs);
        return false;
    }

    protected void startNamespaceScope(QName element, XMLAttributes attributes, Augmentations augs) {
    }

    protected void handleEndElement(QName element, Augmentations augs, boolean isEmpty) throws XNIException {
        String value;
        this.fElementDepth--;
        if (this.fPerformValidation) {
            int elementIndex = this.fCurrentElementIndex;
            if (elementIndex != -1 && this.fCurrentContentSpecType != -1) {
                QName[] children = this.fElementChildren;
                int childrenOffset = this.fElementChildrenOffsetStack[this.fElementDepth + 1] + 1;
                int childrenLength = this.fElementChildrenLength - childrenOffset;
                int result = checkContent(elementIndex, children, childrenOffset, childrenLength);
                if (result != -1) {
                    this.fDTDGrammar.getElementDecl(elementIndex, this.fTempElementDecl);
                    if (this.fTempElementDecl.type == 1) {
                        this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_CONTENT_INVALID", new Object[]{element.rawname, "EMPTY"}, (short) 1);
                    } else {
                        String messageKey = result != childrenLength ? "MSG_CONTENT_INVALID" : "MSG_CONTENT_INCOMPLETE";
                        this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", messageKey, new Object[]{element.rawname, this.fDTDGrammar.getContentSpecAsString(elementIndex)}, (short) 1);
                    }
                }
            }
            this.fElementChildrenLength = this.fElementChildrenOffsetStack[this.fElementDepth + 1] + 1;
        }
        endNamespaceScope(this.fCurrentElement, augs, isEmpty);
        if (this.fElementDepth < -1) {
            throw new RuntimeException("FWK008 Element stack underflow");
        }
        if (this.fElementDepth < 0) {
            this.fCurrentElement.clear();
            this.fCurrentElementIndex = -1;
            this.fCurrentContentSpecType = -1;
            this.fInElementContent = false;
            if (this.fPerformValidation && (value = this.fValidationState.checkIDRefID()) != null) {
                this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_ELEMENT_WITH_ID_REQUIRED", new Object[]{value}, (short) 1);
                return;
            }
            return;
        }
        this.fCurrentElement.setValues(this.fElementQNamePartsStack[this.fElementDepth]);
        this.fCurrentElementIndex = this.fElementIndexStack[this.fElementDepth];
        this.fCurrentContentSpecType = this.fContentSpecTypeStack[this.fElementDepth];
        this.fInElementContent = this.fCurrentContentSpecType == 3;
    }

    protected void endNamespaceScope(QName element, Augmentations augs, boolean isEmpty) throws XNIException {
        if (this.fDocumentHandler != null && !isEmpty) {
            this.fDocumentHandler.endElement(this.fCurrentElement, augs);
        }
    }

    protected boolean isSpace(int c2) {
        return XMLChar.isSpace(c2);
    }

    @Override // com.sun.org.apache.xerces.internal.impl.RevalidationHandler
    public boolean characterData(String data, Augmentations augs) {
        characters(new XMLString(data.toCharArray(), 0, data.length()), augs);
        return true;
    }
}
