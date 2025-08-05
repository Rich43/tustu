package com.sun.org.apache.xerces.internal.impl.xs;

import com.sun.org.apache.xerces.internal.impl.Constants;
import com.sun.org.apache.xerces.internal.impl.RevalidationHandler;
import com.sun.org.apache.xerces.internal.impl.XMLEntityManager;
import com.sun.org.apache.xerces.internal.impl.XMLErrorReporter;
import com.sun.org.apache.xerces.internal.impl.dv.InvalidDatatypeValueException;
import com.sun.org.apache.xerces.internal.impl.dv.ValidatedInfo;
import com.sun.org.apache.xerces.internal.impl.dv.ValidationContext;
import com.sun.org.apache.xerces.internal.impl.dv.XSSimpleType;
import com.sun.org.apache.xerces.internal.impl.validation.ValidationManager;
import com.sun.org.apache.xerces.internal.impl.validation.ValidationState;
import com.sun.org.apache.xerces.internal.impl.xs.XMLSchemaLoader;
import com.sun.org.apache.xerces.internal.impl.xs.identity.Field;
import com.sun.org.apache.xerces.internal.impl.xs.identity.FieldActivator;
import com.sun.org.apache.xerces.internal.impl.xs.identity.IdentityConstraint;
import com.sun.org.apache.xerces.internal.impl.xs.identity.KeyRef;
import com.sun.org.apache.xerces.internal.impl.xs.identity.Selector;
import com.sun.org.apache.xerces.internal.impl.xs.identity.UniqueOrKey;
import com.sun.org.apache.xerces.internal.impl.xs.identity.ValueStore;
import com.sun.org.apache.xerces.internal.impl.xs.identity.XPathMatcher;
import com.sun.org.apache.xerces.internal.impl.xs.models.CMBuilder;
import com.sun.org.apache.xerces.internal.impl.xs.models.CMNodeFactory;
import com.sun.org.apache.xerces.internal.impl.xs.models.XSCMValidator;
import com.sun.org.apache.xerces.internal.parsers.XMLParser;
import com.sun.org.apache.xerces.internal.util.AugmentationsImpl;
import com.sun.org.apache.xerces.internal.util.IntStack;
import com.sun.org.apache.xerces.internal.util.SymbolTable;
import com.sun.org.apache.xerces.internal.util.URI;
import com.sun.org.apache.xerces.internal.util.XMLAttributesImpl;
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
import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool;
import com.sun.org.apache.xerces.internal.xni.parser.XMLComponent;
import com.sun.org.apache.xerces.internal.xni.parser.XMLComponentManager;
import com.sun.org.apache.xerces.internal.xni.parser.XMLConfigurationException;
import com.sun.org.apache.xerces.internal.xni.parser.XMLDocumentFilter;
import com.sun.org.apache.xerces.internal.xni.parser.XMLDocumentSource;
import com.sun.org.apache.xerces.internal.xni.parser.XMLEntityResolver;
import com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;
import com.sun.org.apache.xerces.internal.xs.ShortList;
import com.sun.org.apache.xerces.internal.xs.StringList;
import com.sun.org.apache.xerces.internal.xs.XSObjectList;
import com.sun.org.apache.xerces.internal.xs.XSTypeDefinition;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.Vector;
import jdk.xml.internal.JdkXmlUtils;
import org.apache.commons.math3.geometry.VectorFormat;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/XMLSchemaValidator.class */
public class XMLSchemaValidator implements XMLComponent, XMLDocumentFilter, FieldActivator, RevalidationHandler {
    private static final boolean DEBUG = false;
    protected static final String NORMALIZE_DATA = "http://apache.org/xml/features/validation/schema/normalized-value";
    protected static final String SCHEMA_ELEMENT_DEFAULT = "http://apache.org/xml/features/validation/schema/element-default";
    protected static final String SCHEMA_AUGMENT_PSVI = "http://apache.org/xml/features/validation/schema/augment-psvi";
    protected static final String GENERATE_SYNTHETIC_ANNOTATIONS = "http://apache.org/xml/features/generate-synthetic-annotations";
    protected static final String PARSER_SETTINGS = "http://apache.org/xml/features/internal/parser-settings";
    protected static final String REPORT_WHITESPACE = "http://java.sun.com/xml/schema/features/report-ignored-element-content-whitespace";
    public static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
    public static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
    public static final String ENTITY_RESOLVER = "http://apache.org/xml/properties/internal/entity-resolver";
    public static final String XMLGRAMMAR_POOL = "http://apache.org/xml/properties/internal/grammar-pool";
    protected static final String ENTITY_MANAGER = "http://apache.org/xml/properties/internal/entity-manager";
    protected static final String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";
    protected static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
    private static final String XML_SECURITY_PROPERTY_MANAGER = "http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager";
    protected static final String OVERRIDE_PARSER = "jdk.xml.overrideDefaultParser";
    protected static final int ID_CONSTRAINT_NUM = 1;
    protected XMLString fDefaultValue;
    protected SymbolTable fSymbolTable;
    private XMLLocator fLocator;
    protected XMLEntityResolver fEntityResolver;
    protected XMLGrammarPool fGrammarPool;
    protected XMLDocumentHandler fDocumentHandler;
    protected XMLDocumentSource fDocumentSource;
    static final int INITIAL_STACK_SIZE = 8;
    static final int INC_STACK_SIZE = 8;
    private static final boolean DEBUG_NORMALIZATION = false;
    private static final int BUFFER_SIZE = 20;
    private String fValidationRoot;
    private int fSkipValidationDepth;
    private int fNFullValidationDepth;
    private int fNNoneValidationDepth;
    private int fElementDepth;
    private boolean fSubElement;
    private XSElementDecl fCurrentElemDecl;
    private boolean fNil;
    private XSNotationDecl fNotation;
    private XSTypeDefinition fCurrentType;
    private XSCMValidator fCurrentCM;
    private int[] fCurrCMState;
    protected static final String VALIDATION = "http://xml.org/sax/features/validation";
    protected static final String SCHEMA_VALIDATION = "http://apache.org/xml/features/validation/schema";
    protected static final String DYNAMIC_VALIDATION = "http://apache.org/xml/features/validation/dynamic";
    protected static final String SCHEMA_FULL_CHECKING = "http://apache.org/xml/features/validation/schema-full-checking";
    protected static final String ALLOW_JAVA_ENCODINGS = "http://apache.org/xml/features/allow-java-encodings";
    protected static final String CONTINUE_AFTER_FATAL_ERROR = "http://apache.org/xml/features/continue-after-fatal-error";
    protected static final String STANDARD_URI_CONFORMANT_FEATURE = "http://apache.org/xml/features/standard-uri-conformant";
    protected static final String VALIDATE_ANNOTATIONS = "http://apache.org/xml/features/validate-annotations";
    protected static final String HONOUR_ALL_SCHEMALOCATIONS = "http://apache.org/xml/features/honour-all-schemaLocations";
    protected static final String USE_GRAMMAR_POOL_ONLY = "http://apache.org/xml/features/internal/validation/schema/use-grammar-pool-only";
    protected static final String NAMESPACE_GROWTH = "http://apache.org/xml/features/namespace-growth";
    protected static final String TOLERATE_DUPLICATES = "http://apache.org/xml/features/internal/tolerate-duplicates";
    private static final String[] RECOGNIZED_FEATURES = {VALIDATION, SCHEMA_VALIDATION, DYNAMIC_VALIDATION, SCHEMA_FULL_CHECKING, ALLOW_JAVA_ENCODINGS, CONTINUE_AFTER_FATAL_ERROR, STANDARD_URI_CONFORMANT_FEATURE, "http://apache.org/xml/features/generate-synthetic-annotations", VALIDATE_ANNOTATIONS, HONOUR_ALL_SCHEMALOCATIONS, USE_GRAMMAR_POOL_ONLY, NAMESPACE_GROWTH, TOLERATE_DUPLICATES, "jdk.xml.overrideDefaultParser"};
    private static final Boolean[] FEATURE_DEFAULTS = {null, null, null, null, null, null, null, null, null, null, null, null, null, Boolean.valueOf(JdkXmlUtils.OVERRIDE_PARSER_DEFAULT)};
    protected static final String VALIDATION_MANAGER = "http://apache.org/xml/properties/internal/validation-manager";
    protected static final String SCHEMA_LOCATION = "http://apache.org/xml/properties/schema/external-schemaLocation";
    protected static final String SCHEMA_NONS_LOCATION = "http://apache.org/xml/properties/schema/external-noNamespaceSchemaLocation";
    protected static final String SCHEMA_DV_FACTORY = "http://apache.org/xml/properties/internal/validation/schema/dv-factory";
    private static final String[] RECOGNIZED_PROPERTIES = {"http://apache.org/xml/properties/internal/symbol-table", "http://apache.org/xml/properties/internal/error-reporter", "http://apache.org/xml/properties/internal/entity-resolver", VALIDATION_MANAGER, SCHEMA_LOCATION, SCHEMA_NONS_LOCATION, "http://java.sun.com/xml/jaxp/properties/schemaSource", "http://java.sun.com/xml/jaxp/properties/schemaLanguage", SCHEMA_DV_FACTORY, "http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager"};
    private static final Object[] PROPERTY_DEFAULTS = {null, null, null, null, null, null, null, null, null, null, null, null, null};
    protected ElementPSVImpl fCurrentPSVI = new ElementPSVImpl();
    protected final AugmentationsImpl fAugmentations = new AugmentationsImpl();
    protected final HashMap fMayMatchFieldMap = new HashMap();
    protected boolean fDynamicValidation = false;
    protected boolean fSchemaDynamicValidation = false;
    protected boolean fDoValidation = false;
    protected boolean fFullChecking = false;
    protected boolean fNormalizeData = true;
    protected boolean fSchemaElementDefault = true;
    protected boolean fAugPSVI = true;
    protected boolean fIdConstraint = false;
    protected boolean fUseGrammarPoolOnly = false;
    protected boolean fNamespaceGrowth = false;
    private String fSchemaType = null;
    protected boolean fEntityRef = false;
    protected boolean fInCDATA = false;
    protected boolean fSawOnlyWhitespaceInElementContent = false;
    protected final XSIErrorReporter fXSIErrorReporter = new XSIErrorReporter();
    protected ValidationManager fValidationManager = null;
    protected ValidationState fValidationState = new ValidationState();
    protected String fExternalSchemas = null;
    protected String fExternalNoNamespaceSchema = null;
    protected Object fJaxpSchemaSource = null;
    protected final XSDDescription fXSDDescription = new XSDDescription();
    protected final Map<String, XMLSchemaLoader.LocationArray> fLocationPairs = new HashMap();
    boolean reportWhitespace = false;
    private final XMLString fEmptyXMLStr = new XMLString(null, 0, -1);
    private final XMLString fNormalizedStr = new XMLString();
    private boolean fFirstChunk = true;
    private boolean fTrailing = false;
    private short fWhiteSpace = -1;
    private boolean fUnionType = false;
    private final XSGrammarBucket fGrammarBucket = new XSGrammarBucket();
    private final SubstitutionGroupHandler fSubGroupHandler = new SubstitutionGroupHandler(this.fGrammarBucket);
    private final XSSimpleType fQNameDV = (XSSimpleType) SchemaGrammar.SG_SchemaNS.getGlobalTypeDecl(SchemaSymbols.ATTVAL_QNAME);
    private final CMNodeFactory nodeFactory = new CMNodeFactory();
    private final CMBuilder fCMBuilder = new CMBuilder(this.nodeFactory);
    private final XMLSchemaLoader fSchemaLoader = new XMLSchemaLoader(this.fXSIErrorReporter.fErrorReporter, this.fGrammarBucket, this.fSubGroupHandler, this.fCMBuilder);
    private boolean[] fSubElementStack = new boolean[8];
    private XSElementDecl[] fElemDeclStack = new XSElementDecl[8];
    private boolean[] fNilStack = new boolean[8];
    private XSNotationDecl[] fNotationStack = new XSNotationDecl[8];
    private XSTypeDefinition[] fTypeStack = new XSTypeDefinition[8];
    private XSCMValidator[] fCMStack = new XSCMValidator[8];
    private int[][] fCMStateStack = new int[8];
    private boolean fStrictAssess = true;
    private boolean[] fStrictAssessStack = new boolean[8];
    private final StringBuffer fBuffer = new StringBuffer();
    private boolean fAppendBuffer = true;
    private boolean fSawText = false;
    private boolean[] fSawTextStack = new boolean[8];
    private boolean fSawCharacters = false;
    private boolean[] fStringContent = new boolean[8];
    private final QName fTempQName = new QName();
    private ValidatedInfo fValidatedInfo = new ValidatedInfo();
    private ValidationState fState4XsiType = new ValidationState();
    private ValidationState fState4ApplyDefault = new ValidationState();
    protected XPathMatcherStack fMatcherStack = new XPathMatcherStack();
    protected ValueStoreCache fValueStoreCache = new ValueStoreCache();

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/XMLSchemaValidator$XSIErrorReporter.class */
    protected final class XSIErrorReporter {
        XMLErrorReporter fErrorReporter;
        Vector fErrors = new Vector();
        int[] fContext = new int[8];
        int fContextCount;

        protected XSIErrorReporter() {
        }

        public void reset(XMLErrorReporter errorReporter) {
            this.fErrorReporter = errorReporter;
            this.fErrors.removeAllElements();
            this.fContextCount = 0;
        }

        public void pushContext() {
            if (!XMLSchemaValidator.this.fAugPSVI) {
                return;
            }
            if (this.fContextCount == this.fContext.length) {
                int newSize = this.fContextCount + 8;
                int[] newArray = new int[newSize];
                System.arraycopy(this.fContext, 0, newArray, 0, this.fContextCount);
                this.fContext = newArray;
            }
            int[] iArr = this.fContext;
            int i2 = this.fContextCount;
            this.fContextCount = i2 + 1;
            iArr[i2] = this.fErrors.size();
        }

        public String[] popContext() {
            if (!XMLSchemaValidator.this.fAugPSVI) {
                return null;
            }
            int[] iArr = this.fContext;
            int i2 = this.fContextCount - 1;
            this.fContextCount = i2;
            int contextPos = iArr[i2];
            int size = this.fErrors.size() - contextPos;
            if (size == 0) {
                return null;
            }
            String[] errors = new String[size];
            for (int i3 = 0; i3 < size; i3++) {
                errors[i3] = (String) this.fErrors.elementAt(contextPos + i3);
            }
            this.fErrors.setSize(contextPos);
            return errors;
        }

        public String[] mergeContext() {
            if (!XMLSchemaValidator.this.fAugPSVI) {
                return null;
            }
            int[] iArr = this.fContext;
            int i2 = this.fContextCount - 1;
            this.fContextCount = i2;
            int contextPos = iArr[i2];
            int size = this.fErrors.size() - contextPos;
            if (size == 0) {
                return null;
            }
            String[] errors = new String[size];
            for (int i3 = 0; i3 < size; i3++) {
                errors[i3] = (String) this.fErrors.elementAt(contextPos + i3);
            }
            return errors;
        }

        public void reportError(String domain, String key, Object[] arguments, short severity) throws XNIException {
            this.fErrorReporter.reportError(domain, key, arguments, severity);
            if (XMLSchemaValidator.this.fAugPSVI) {
                this.fErrors.addElement(key);
            }
        }

        public void reportError(XMLLocator location, String domain, String key, Object[] arguments, short severity) throws XNIException {
            this.fErrorReporter.reportError(location, domain, key, arguments, severity);
            if (XMLSchemaValidator.this.fAugPSVI) {
                this.fErrors.addElement(key);
            }
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

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLDocumentSource
    public void setDocumentHandler(XMLDocumentHandler xMLDocumentHandler) {
        this.fDocumentHandler = xMLDocumentHandler;
        if (xMLDocumentHandler instanceof XMLParser) {
            try {
                this.reportWhitespace = ((XMLParser) xMLDocumentHandler).getFeature(REPORT_WHITESPACE);
            } catch (Exception e2) {
                this.reportWhitespace = false;
            }
        }
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
        this.fValidationState.setNamespaceSupport(namespaceContext);
        this.fState4XsiType.setNamespaceSupport(namespaceContext);
        this.fState4ApplyDefault.setNamespaceSupport(namespaceContext);
        this.fLocator = locator;
        handleStartDocument(locator, encoding);
        if (this.fDocumentHandler != null) {
            this.fDocumentHandler.startDocument(locator, encoding, namespaceContext, augs);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void xmlDecl(String version, String encoding, String standalone, Augmentations augs) throws XNIException {
        if (this.fDocumentHandler != null) {
            this.fDocumentHandler.xmlDecl(version, encoding, standalone, augs);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void doctypeDecl(String rootElement, String publicId, String systemId, Augmentations augs) throws XNIException {
        if (this.fDocumentHandler != null) {
            this.fDocumentHandler.doctypeDecl(rootElement, publicId, systemId, augs);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void startElement(QName element, XMLAttributes attributes, Augmentations augs) throws XNIException {
        Augmentations modifiedAugs = handleStartElement(element, attributes, augs);
        if (this.fDocumentHandler != null) {
            this.fDocumentHandler.startElement(element, attributes, modifiedAugs);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void emptyElement(QName element, XMLAttributes attributes, Augmentations augs) throws XNIException {
        Augmentations modifiedAugs = handleStartElement(element, attributes, augs);
        this.fDefaultValue = null;
        if (this.fElementDepth != -2) {
            modifiedAugs = handleEndElement(element, modifiedAugs);
        }
        if (this.fDocumentHandler != null) {
            if (!this.fSchemaElementDefault || this.fDefaultValue == null) {
                this.fDocumentHandler.emptyElement(element, attributes, modifiedAugs);
                return;
            }
            this.fDocumentHandler.startElement(element, attributes, modifiedAugs);
            this.fDocumentHandler.characters(this.fDefaultValue, null);
            this.fDocumentHandler.endElement(element, modifiedAugs);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void characters(XMLString text, Augmentations augs) throws XNIException {
        XMLString text2 = handleCharacters(text);
        if (this.fSawOnlyWhitespaceInElementContent) {
            this.fSawOnlyWhitespaceInElementContent = false;
            if (!this.reportWhitespace) {
                ignorableWhitespace(text2, augs);
                return;
            }
        }
        if (this.fDocumentHandler != null) {
            if (this.fNormalizeData && this.fUnionType) {
                if (augs != null) {
                    this.fDocumentHandler.characters(this.fEmptyXMLStr, augs);
                    return;
                }
                return;
            }
            this.fDocumentHandler.characters(text2, augs);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void ignorableWhitespace(XMLString text, Augmentations augs) throws XNIException {
        handleIgnorableWhitespace(text);
        if (this.fDocumentHandler != null) {
            this.fDocumentHandler.ignorableWhitespace(text, augs);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void endElement(QName element, Augmentations augs) throws XNIException {
        this.fDefaultValue = null;
        Augmentations modifiedAugs = handleEndElement(element, augs);
        if (this.fDocumentHandler != null) {
            if (!this.fSchemaElementDefault || this.fDefaultValue == null) {
                this.fDocumentHandler.endElement(element, modifiedAugs);
            } else {
                this.fDocumentHandler.characters(this.fDefaultValue, null);
                this.fDocumentHandler.endElement(element, modifiedAugs);
            }
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void startCDATA(Augmentations augs) throws XNIException {
        this.fInCDATA = true;
        if (this.fDocumentHandler != null) {
            this.fDocumentHandler.startCDATA(augs);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void endCDATA(Augmentations augs) throws XNIException {
        this.fInCDATA = false;
        if (this.fDocumentHandler != null) {
            this.fDocumentHandler.endCDATA(augs);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void endDocument(Augmentations augs) throws XNIException {
        handleEndDocument();
        if (this.fDocumentHandler != null) {
            this.fDocumentHandler.endDocument(augs);
        }
        this.fLocator = null;
    }

    @Override // com.sun.org.apache.xerces.internal.impl.RevalidationHandler
    public boolean characterData(String data, Augmentations augs) {
        this.fSawText = this.fSawText || data.length() > 0;
        if (this.fNormalizeData && this.fWhiteSpace != -1 && this.fWhiteSpace != 0) {
            normalizeWhitespace(data, this.fWhiteSpace == 2);
            this.fBuffer.append(this.fNormalizedStr.ch, this.fNormalizedStr.offset, this.fNormalizedStr.length);
        } else if (this.fAppendBuffer) {
            this.fBuffer.append(data);
        }
        boolean allWhiteSpace = true;
        if (this.fCurrentType != null && this.fCurrentType.getTypeCategory() == 15) {
            XSComplexTypeDecl ctype = (XSComplexTypeDecl) this.fCurrentType;
            if (ctype.fContentType == 2) {
                int i2 = 0;
                while (true) {
                    if (i2 >= data.length()) {
                        break;
                    }
                    if (XMLChar.isSpace(data.charAt(i2))) {
                        i2++;
                    } else {
                        allWhiteSpace = false;
                        this.fSawCharacters = true;
                        break;
                    }
                }
            }
        }
        return allWhiteSpace;
    }

    public void elementDefault(String data) {
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void startGeneralEntity(String name, XMLResourceIdentifier identifier, String encoding, Augmentations augs) throws XNIException {
        this.fEntityRef = true;
        if (this.fDocumentHandler != null) {
            this.fDocumentHandler.startGeneralEntity(name, identifier, encoding, augs);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void textDecl(String version, String encoding, Augmentations augs) throws XNIException {
        if (this.fDocumentHandler != null) {
            this.fDocumentHandler.textDecl(version, encoding, augs);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void comment(XMLString text, Augmentations augs) throws XNIException {
        if (this.fDocumentHandler != null) {
            this.fDocumentHandler.comment(text, augs);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void processingInstruction(String target, XMLString data, Augmentations augs) throws XNIException {
        if (this.fDocumentHandler != null) {
            this.fDocumentHandler.processingInstruction(target, data, augs);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void endGeneralEntity(String name, Augmentations augs) throws XNIException {
        this.fEntityRef = false;
        if (this.fDocumentHandler != null) {
            this.fDocumentHandler.endGeneralEntity(name, augs);
        }
    }

    /* JADX WARN: Type inference failed for: r1v53, types: [int[], int[][]] */
    public XMLSchemaValidator() {
        this.fState4XsiType.setExtraChecking(false);
        this.fState4ApplyDefault.setFacetChecking(false);
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLComponent
    public void reset(XMLComponentManager componentManager) throws XMLConfigurationException {
        this.fIdConstraint = false;
        this.fLocationPairs.clear();
        this.fValidationState.resetIDTables();
        this.nodeFactory.reset(componentManager);
        this.fSchemaLoader.reset(componentManager);
        this.fCurrentElemDecl = null;
        this.fCurrentCM = null;
        this.fCurrCMState = null;
        this.fSkipValidationDepth = -1;
        this.fNFullValidationDepth = -1;
        this.fNNoneValidationDepth = -1;
        this.fElementDepth = -1;
        this.fSubElement = false;
        this.fSchemaDynamicValidation = false;
        this.fEntityRef = false;
        this.fInCDATA = false;
        this.fMatcherStack.clear();
        if (!this.fMayMatchFieldMap.isEmpty()) {
            this.fMayMatchFieldMap.clear();
        }
        this.fXSIErrorReporter.reset((XMLErrorReporter) componentManager.getProperty("http://apache.org/xml/properties/internal/error-reporter"));
        boolean parser_settings = componentManager.getFeature(PARSER_SETTINGS, true);
        if (!parser_settings) {
            this.fValidationManager.addValidationState(this.fValidationState);
            XMLSchemaLoader.processExternalHints(this.fExternalSchemas, this.fExternalNoNamespaceSchema, this.fLocationPairs, this.fXSIErrorReporter.fErrorReporter);
            return;
        }
        SymbolTable symbolTable = (SymbolTable) componentManager.getProperty("http://apache.org/xml/properties/internal/symbol-table");
        if (symbolTable != this.fSymbolTable) {
            this.fSymbolTable = symbolTable;
        }
        this.fNamespaceGrowth = componentManager.getFeature(NAMESPACE_GROWTH, false);
        this.fDynamicValidation = componentManager.getFeature(DYNAMIC_VALIDATION, false);
        if (this.fDynamicValidation) {
            this.fDoValidation = true;
        } else {
            this.fDoValidation = componentManager.getFeature(VALIDATION, false);
        }
        if (this.fDoValidation) {
            this.fDoValidation |= componentManager.getFeature(SCHEMA_VALIDATION, false);
        }
        this.fFullChecking = componentManager.getFeature(SCHEMA_FULL_CHECKING, false);
        this.fNormalizeData = componentManager.getFeature(NORMALIZE_DATA, false);
        this.fSchemaElementDefault = componentManager.getFeature(SCHEMA_ELEMENT_DEFAULT, false);
        this.fAugPSVI = componentManager.getFeature(SCHEMA_AUGMENT_PSVI, true);
        this.fSchemaType = (String) componentManager.getProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage", null);
        this.fUseGrammarPoolOnly = componentManager.getFeature(USE_GRAMMAR_POOL_ONLY, false);
        this.fEntityResolver = (XMLEntityResolver) componentManager.getProperty(ENTITY_MANAGER);
        this.fValidationManager = (ValidationManager) componentManager.getProperty(VALIDATION_MANAGER);
        this.fValidationManager.addValidationState(this.fValidationState);
        this.fValidationState.setSymbolTable(this.fSymbolTable);
        try {
            this.fExternalSchemas = (String) componentManager.getProperty(SCHEMA_LOCATION);
            this.fExternalNoNamespaceSchema = (String) componentManager.getProperty(SCHEMA_NONS_LOCATION);
        } catch (XMLConfigurationException e2) {
            this.fExternalSchemas = null;
            this.fExternalNoNamespaceSchema = null;
        }
        XMLSchemaLoader.processExternalHints(this.fExternalSchemas, this.fExternalNoNamespaceSchema, this.fLocationPairs, this.fXSIErrorReporter.fErrorReporter);
        this.fJaxpSchemaSource = componentManager.getProperty("http://java.sun.com/xml/jaxp/properties/schemaSource", null);
        this.fGrammarPool = (XMLGrammarPool) componentManager.getProperty("http://apache.org/xml/properties/internal/grammar-pool", null);
        this.fState4XsiType.setSymbolTable(symbolTable);
        this.fState4ApplyDefault.setSymbolTable(symbolTable);
    }

    @Override // com.sun.org.apache.xerces.internal.impl.xs.identity.FieldActivator
    public void startValueScopeFor(IdentityConstraint identityConstraint, int initialDepth) {
        ValueStoreBase valueStore = this.fValueStoreCache.getValueStoreFor(identityConstraint, initialDepth);
        valueStore.startValueScope();
    }

    @Override // com.sun.org.apache.xerces.internal.impl.xs.identity.FieldActivator
    public XPathMatcher activateField(Field field, int initialDepth) {
        ValueStore valueStore = this.fValueStoreCache.getValueStoreFor(field.getIdentityConstraint(), initialDepth);
        setMayMatch(field, Boolean.TRUE);
        XPathMatcher matcher = field.createMatcher(this, valueStore);
        this.fMatcherStack.addMatcher(matcher);
        matcher.startDocumentFragment();
        return matcher;
    }

    @Override // com.sun.org.apache.xerces.internal.impl.xs.identity.FieldActivator
    public void endValueScopeFor(IdentityConstraint identityConstraint, int initialDepth) throws XNIException {
        ValueStoreBase valueStore = this.fValueStoreCache.getValueStoreFor(identityConstraint, initialDepth);
        valueStore.endValueScope();
    }

    @Override // com.sun.org.apache.xerces.internal.impl.xs.identity.FieldActivator
    public void setMayMatch(Field field, Boolean state) {
        this.fMayMatchFieldMap.put(field, state);
    }

    @Override // com.sun.org.apache.xerces.internal.impl.xs.identity.FieldActivator
    public Boolean mayMatch(Field field) {
        return (Boolean) this.fMayMatchFieldMap.get(field);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void activateSelectorFor(IdentityConstraint ic) {
        Selector selector = ic.getSelector();
        if (selector == null) {
            return;
        }
        XPathMatcher matcher = selector.createMatcher(this, this.fElementDepth);
        this.fMatcherStack.addMatcher(matcher);
        matcher.startDocumentFragment();
    }

    /* JADX WARN: Type inference failed for: r0v51, types: [int[], int[][], java.lang.Object] */
    void ensureStackCapacity() {
        if (this.fElementDepth == this.fElemDeclStack.length) {
            int newSize = this.fElementDepth + 8;
            boolean[] newArrayB = new boolean[newSize];
            System.arraycopy(this.fSubElementStack, 0, newArrayB, 0, this.fElementDepth);
            this.fSubElementStack = newArrayB;
            XSElementDecl[] newArrayE = new XSElementDecl[newSize];
            System.arraycopy(this.fElemDeclStack, 0, newArrayE, 0, this.fElementDepth);
            this.fElemDeclStack = newArrayE;
            boolean[] newArrayB2 = new boolean[newSize];
            System.arraycopy(this.fNilStack, 0, newArrayB2, 0, this.fElementDepth);
            this.fNilStack = newArrayB2;
            XSNotationDecl[] newArrayN = new XSNotationDecl[newSize];
            System.arraycopy(this.fNotationStack, 0, newArrayN, 0, this.fElementDepth);
            this.fNotationStack = newArrayN;
            XSTypeDefinition[] newArrayT = new XSTypeDefinition[newSize];
            System.arraycopy(this.fTypeStack, 0, newArrayT, 0, this.fElementDepth);
            this.fTypeStack = newArrayT;
            XSCMValidator[] newArrayC = new XSCMValidator[newSize];
            System.arraycopy(this.fCMStack, 0, newArrayC, 0, this.fElementDepth);
            this.fCMStack = newArrayC;
            boolean[] newArrayB3 = new boolean[newSize];
            System.arraycopy(this.fSawTextStack, 0, newArrayB3, 0, this.fElementDepth);
            this.fSawTextStack = newArrayB3;
            boolean[] newArrayB4 = new boolean[newSize];
            System.arraycopy(this.fStringContent, 0, newArrayB4, 0, this.fElementDepth);
            this.fStringContent = newArrayB4;
            boolean[] newArrayB5 = new boolean[newSize];
            System.arraycopy(this.fStrictAssessStack, 0, newArrayB5, 0, this.fElementDepth);
            this.fStrictAssessStack = newArrayB5;
            ?? r0 = new int[newSize];
            System.arraycopy(this.fCMStateStack, 0, r0, 0, this.fElementDepth);
            this.fCMStateStack = r0;
        }
    }

    void handleStartDocument(XMLLocator locator, String encoding) {
        this.fValueStoreCache.startDocument();
        if (this.fAugPSVI) {
            this.fCurrentPSVI.fGrammars = null;
            this.fCurrentPSVI.fSchemaInformation = null;
        }
    }

    void handleEndDocument() {
        this.fValueStoreCache.endDocument();
    }

    XMLString handleCharacters(XMLString text) {
        if (this.fSkipValidationDepth >= 0) {
            return text;
        }
        this.fSawText = this.fSawText || text.length > 0;
        if (this.fNormalizeData && this.fWhiteSpace != -1 && this.fWhiteSpace != 0) {
            normalizeWhitespace(text, this.fWhiteSpace == 2);
            text = this.fNormalizedStr;
        }
        if (this.fAppendBuffer) {
            this.fBuffer.append(text.ch, text.offset, text.length);
        }
        this.fSawOnlyWhitespaceInElementContent = false;
        if (this.fCurrentType != null && this.fCurrentType.getTypeCategory() == 15) {
            XSComplexTypeDecl ctype = (XSComplexTypeDecl) this.fCurrentType;
            if (ctype.fContentType == 2) {
                int i2 = text.offset;
                while (true) {
                    if (i2 >= text.offset + text.length) {
                        break;
                    }
                    if (!XMLChar.isSpace(text.ch[i2])) {
                        this.fSawCharacters = true;
                        break;
                    }
                    this.fSawOnlyWhitespaceInElementContent = !this.fSawCharacters;
                    i2++;
                }
            }
        }
        return text;
    }

    private void normalizeWhitespace(XMLString value, boolean collapse) {
        boolean skipSpace = collapse;
        boolean sawNonWS = false;
        boolean leading = false;
        boolean trailing = false;
        int size = value.offset + value.length;
        if (this.fNormalizedStr.ch == null || this.fNormalizedStr.ch.length < value.length + 1) {
            this.fNormalizedStr.ch = new char[value.length + 1];
        }
        this.fNormalizedStr.offset = 1;
        this.fNormalizedStr.length = 1;
        for (int i2 = value.offset; i2 < size; i2++) {
            char c2 = value.ch[i2];
            if (XMLChar.isSpace(c2)) {
                if (!skipSpace) {
                    char[] cArr = this.fNormalizedStr.ch;
                    XMLString xMLString = this.fNormalizedStr;
                    int i3 = xMLString.length;
                    xMLString.length = i3 + 1;
                    cArr[i3] = ' ';
                    skipSpace = collapse;
                }
                if (!sawNonWS) {
                    leading = true;
                }
            } else {
                char[] cArr2 = this.fNormalizedStr.ch;
                XMLString xMLString2 = this.fNormalizedStr;
                int i4 = xMLString2.length;
                xMLString2.length = i4 + 1;
                cArr2[i4] = c2;
                skipSpace = false;
                sawNonWS = true;
            }
        }
        if (skipSpace) {
            if (this.fNormalizedStr.length > 1) {
                this.fNormalizedStr.length--;
                trailing = true;
            } else if (leading && !this.fFirstChunk) {
                trailing = true;
            }
        }
        if (this.fNormalizedStr.length > 1 && !this.fFirstChunk && this.fWhiteSpace == 2 && (this.fTrailing || leading)) {
            this.fNormalizedStr.offset = 0;
            this.fNormalizedStr.ch[0] = ' ';
        }
        this.fNormalizedStr.length -= this.fNormalizedStr.offset;
        this.fTrailing = trailing;
        if (trailing || sawNonWS) {
            this.fFirstChunk = false;
        }
    }

    private void normalizeWhitespace(String value, boolean collapse) {
        boolean skipSpace = collapse;
        int size = value.length();
        if (this.fNormalizedStr.ch == null || this.fNormalizedStr.ch.length < size) {
            this.fNormalizedStr.ch = new char[size];
        }
        this.fNormalizedStr.offset = 0;
        this.fNormalizedStr.length = 0;
        for (int i2 = 0; i2 < size; i2++) {
            char c2 = value.charAt(i2);
            if (XMLChar.isSpace(c2)) {
                if (!skipSpace) {
                    char[] cArr = this.fNormalizedStr.ch;
                    XMLString xMLString = this.fNormalizedStr;
                    int i3 = xMLString.length;
                    xMLString.length = i3 + 1;
                    cArr[i3] = ' ';
                    skipSpace = collapse;
                }
            } else {
                char[] cArr2 = this.fNormalizedStr.ch;
                XMLString xMLString2 = this.fNormalizedStr;
                int i4 = xMLString2.length;
                xMLString2.length = i4 + 1;
                cArr2[i4] = c2;
                skipSpace = false;
            }
        }
        if (skipSpace && this.fNormalizedStr.length != 0) {
            this.fNormalizedStr.length--;
        }
    }

    void handleIgnorableWhitespace(XMLString text) {
        if (this.fSkipValidationDepth >= 0) {
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:27:0x00e7  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    com.sun.org.apache.xerces.internal.xni.Augmentations handleStartElement(com.sun.org.apache.xerces.internal.xni.QName r9, com.sun.org.apache.xerces.internal.xni.XMLAttributes r10, com.sun.org.apache.xerces.internal.xni.Augmentations r11) throws com.sun.org.apache.xerces.internal.xni.XNIException {
        /*
            Method dump skipped, instructions count: 1458
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.org.apache.xerces.internal.impl.xs.XMLSchemaValidator.handleStartElement(com.sun.org.apache.xerces.internal.xni.QName, com.sun.org.apache.xerces.internal.xni.XMLAttributes, com.sun.org.apache.xerces.internal.xni.Augmentations):com.sun.org.apache.xerces.internal.xni.Augmentations");
    }

    Augmentations handleEndElement(QName element, Augmentations augs) throws XNIException {
        Augmentations augs2;
        Selector.Matcher selMatcher;
        IdentityConstraint id;
        ValueStoreBase values;
        Selector.Matcher selMatcher2;
        IdentityConstraint id2;
        if (this.fSkipValidationDepth >= 0) {
            if (this.fSkipValidationDepth == this.fElementDepth && this.fSkipValidationDepth > 0) {
                this.fNFullValidationDepth = this.fSkipValidationDepth - 1;
                this.fSkipValidationDepth = -1;
                this.fElementDepth--;
                this.fSubElement = this.fSubElementStack[this.fElementDepth];
                this.fCurrentElemDecl = this.fElemDeclStack[this.fElementDepth];
                this.fNil = this.fNilStack[this.fElementDepth];
                this.fNotation = this.fNotationStack[this.fElementDepth];
                this.fCurrentType = this.fTypeStack[this.fElementDepth];
                this.fCurrentCM = this.fCMStack[this.fElementDepth];
                this.fStrictAssess = this.fStrictAssessStack[this.fElementDepth];
                this.fCurrCMState = this.fCMStateStack[this.fElementDepth];
                this.fSawText = this.fSawTextStack[this.fElementDepth];
                this.fSawCharacters = this.fStringContent[this.fElementDepth];
            } else {
                this.fElementDepth--;
            }
            if (this.fElementDepth == -1 && this.fFullChecking) {
                XSConstraints.fullSchemaChecking(this.fGrammarBucket, this.fSubGroupHandler, this.fCMBuilder, this.fXSIErrorReporter.fErrorReporter);
            }
            if (this.fAugPSVI) {
                augs = getEmptyAugs(augs);
            }
            return augs;
        }
        processElementContent(element);
        int oldCount = this.fMatcherStack.getMatcherCount();
        for (int i2 = oldCount - 1; i2 >= 0; i2--) {
            XPathMatcher matcher = this.fMatcherStack.getMatcherAt(i2);
            if (this.fCurrentElemDecl == null) {
                matcher.endElement(element, null, false, this.fValidatedInfo.actualValue, this.fValidatedInfo.actualValueType, this.fValidatedInfo.itemValueTypes);
            } else {
                matcher.endElement(element, this.fCurrentType, this.fCurrentElemDecl.getNillable(), this.fDefaultValue == null ? this.fValidatedInfo.actualValue : this.fCurrentElemDecl.fDefault.actualValue, this.fDefaultValue == null ? this.fValidatedInfo.actualValueType : this.fCurrentElemDecl.fDefault.actualValueType, this.fDefaultValue == null ? this.fValidatedInfo.itemValueTypes : this.fCurrentElemDecl.fDefault.itemValueTypes);
            }
        }
        if (this.fMatcherStack.size() > 0) {
            this.fMatcherStack.popContext();
        }
        int newCount = this.fMatcherStack.getMatcherCount();
        for (int i3 = oldCount - 1; i3 >= newCount; i3--) {
            XPathMatcher matcher2 = this.fMatcherStack.getMatcherAt(i3);
            if ((matcher2 instanceof Selector.Matcher) && (id2 = (selMatcher2 = (Selector.Matcher) matcher2).getIdentityConstraint()) != null && id2.getCategory() != 2) {
                this.fValueStoreCache.transplant(id2, selMatcher2.getInitialDepth());
            }
        }
        for (int i4 = oldCount - 1; i4 >= newCount; i4--) {
            XPathMatcher matcher3 = this.fMatcherStack.getMatcherAt(i4);
            if ((matcher3 instanceof Selector.Matcher) && (id = (selMatcher = (Selector.Matcher) matcher3).getIdentityConstraint()) != null && id.getCategory() == 2 && (values = this.fValueStoreCache.getValueStoreFor(id, selMatcher.getInitialDepth())) != null) {
                values.endDocumentFragment();
            }
        }
        this.fValueStoreCache.endElement();
        if (this.fElementDepth == 0) {
            String invIdRef = this.fValidationState.checkIDRefID();
            this.fValidationState.resetIDTables();
            if (invIdRef != null) {
                reportSchemaError("cvc-id.1", new Object[]{invIdRef});
            }
            if (this.fFullChecking) {
                XSConstraints.fullSchemaChecking(this.fGrammarBucket, this.fSubGroupHandler, this.fCMBuilder, this.fXSIErrorReporter.fErrorReporter);
            }
            SchemaGrammar[] grammars = this.fGrammarBucket.getGrammars();
            if (this.fGrammarPool != null) {
                for (SchemaGrammar schemaGrammar : grammars) {
                    schemaGrammar.setImmutable(true);
                }
                this.fGrammarPool.cacheGrammars("http://www.w3.org/2001/XMLSchema", grammars);
            }
            augs2 = endElementPSVI(true, grammars, augs);
        } else {
            augs2 = endElementPSVI(false, null, augs);
            this.fElementDepth--;
            this.fSubElement = this.fSubElementStack[this.fElementDepth];
            this.fCurrentElemDecl = this.fElemDeclStack[this.fElementDepth];
            this.fNil = this.fNilStack[this.fElementDepth];
            this.fNotation = this.fNotationStack[this.fElementDepth];
            this.fCurrentType = this.fTypeStack[this.fElementDepth];
            this.fCurrentCM = this.fCMStack[this.fElementDepth];
            this.fStrictAssess = this.fStrictAssessStack[this.fElementDepth];
            this.fCurrCMState = this.fCMStateStack[this.fElementDepth];
            this.fSawText = this.fSawTextStack[this.fElementDepth];
            this.fSawCharacters = this.fStringContent[this.fElementDepth];
            this.fWhiteSpace = (short) -1;
            this.fAppendBuffer = false;
            this.fUnionType = false;
        }
        return augs2;
    }

    final Augmentations endElementPSVI(boolean root, SchemaGrammar[] grammars, Augmentations augs) {
        if (this.fAugPSVI) {
            augs = getEmptyAugs(augs);
            this.fCurrentPSVI.fDeclaration = this.fCurrentElemDecl;
            this.fCurrentPSVI.fTypeDecl = this.fCurrentType;
            this.fCurrentPSVI.fNotation = this.fNotation;
            this.fCurrentPSVI.fValidationContext = this.fValidationRoot;
            if (this.fElementDepth > this.fNFullValidationDepth) {
                this.fCurrentPSVI.fValidationAttempted = (short) 2;
            } else if (this.fElementDepth > this.fNNoneValidationDepth) {
                this.fCurrentPSVI.fValidationAttempted = (short) 0;
            } else {
                this.fCurrentPSVI.fValidationAttempted = (short) 1;
                int i2 = this.fElementDepth - 1;
                this.fNNoneValidationDepth = i2;
                this.fNFullValidationDepth = i2;
            }
            if (this.fDefaultValue != null) {
                this.fCurrentPSVI.fSpecified = true;
            }
            this.fCurrentPSVI.fNil = this.fNil;
            this.fCurrentPSVI.fMemberType = this.fValidatedInfo.memberType;
            this.fCurrentPSVI.fNormalizedValue = this.fValidatedInfo.normalizedValue;
            this.fCurrentPSVI.fActualValue = this.fValidatedInfo.actualValue;
            this.fCurrentPSVI.fActualValueType = this.fValidatedInfo.actualValueType;
            this.fCurrentPSVI.fItemValueTypes = this.fValidatedInfo.itemValueTypes;
            if (this.fStrictAssess) {
                String[] errors = this.fXSIErrorReporter.mergeContext();
                this.fCurrentPSVI.fErrorCodes = errors;
                this.fCurrentPSVI.fValidity = errors == null ? (short) 2 : (short) 1;
            } else {
                this.fCurrentPSVI.fValidity = (short) 0;
                this.fXSIErrorReporter.popContext();
            }
            if (root) {
                this.fCurrentPSVI.fGrammars = grammars;
                this.fCurrentPSVI.fSchemaInformation = null;
            }
        }
        return augs;
    }

    Augmentations getEmptyAugs(Augmentations augs) {
        if (augs == null) {
            augs = this.fAugmentations;
            augs.removeAllItems();
        }
        augs.putItem(Constants.ELEMENT_PSVI, this.fCurrentPSVI);
        this.fCurrentPSVI.reset();
        return augs;
    }

    void storeLocations(String sLocation, String nsLocation) throws XNIException {
        if (sLocation != null && !XMLSchemaLoader.tokenizeSchemaLocationStr(sLocation, this.fLocationPairs)) {
            this.fXSIErrorReporter.reportError(XSMessageFormatter.SCHEMA_DOMAIN, "SchemaLocation", new Object[]{sLocation}, (short) 0);
        }
        if (nsLocation != null) {
            XMLSchemaLoader.LocationArray la = this.fLocationPairs.get(XMLSymbols.EMPTY_STRING);
            if (la == null) {
                la = new XMLSchemaLoader.LocationArray();
                this.fLocationPairs.put(XMLSymbols.EMPTY_STRING, la);
            }
            la.addLocation(nsLocation);
        }
    }

    SchemaGrammar findSchemaGrammar(short contextType, String namespace, QName enclosingElement, QName triggeringComponet, XMLAttributes attributes) throws XNIException {
        SchemaGrammar grammar = this.fGrammarBucket.getGrammar(namespace);
        if (grammar == null) {
            this.fXSDDescription.setNamespace(namespace);
            if (this.fGrammarPool != null) {
                grammar = (SchemaGrammar) this.fGrammarPool.retrieveGrammar(this.fXSDDescription);
                if (grammar != null && !this.fGrammarBucket.putGrammar(grammar, true, this.fNamespaceGrowth)) {
                    this.fXSIErrorReporter.fErrorReporter.reportError(XSMessageFormatter.SCHEMA_DOMAIN, "GrammarConflict", null, (short) 0);
                    grammar = null;
                }
            }
        }
        if ((grammar == null && !this.fUseGrammarPoolOnly) || this.fNamespaceGrowth) {
            this.fXSDDescription.reset();
            this.fXSDDescription.fContextType = contextType;
            this.fXSDDescription.setNamespace(namespace);
            this.fXSDDescription.fEnclosedElementName = enclosingElement;
            this.fXSDDescription.fTriggeringComponent = triggeringComponet;
            this.fXSDDescription.fAttributes = attributes;
            if (this.fLocator != null) {
                this.fXSDDescription.setBaseSystemId(this.fLocator.getExpandedSystemId());
            }
            Map<String, XMLSchemaLoader.LocationArray> locationPairs = this.fLocationPairs;
            XMLSchemaLoader.LocationArray locationArray = locationPairs.get(namespace == null ? XMLSymbols.EMPTY_STRING : namespace);
            if (locationArray != null) {
                String[] temp = locationArray.getLocationArray();
                if (temp.length != 0) {
                    setLocationHints(this.fXSDDescription, temp, grammar);
                }
            }
            if (grammar == null || this.fXSDDescription.fLocationHints != null) {
                boolean toParseSchema = true;
                if (grammar != null) {
                    locationPairs = Collections.emptyMap();
                }
                try {
                    XMLInputSource xis = XMLSchemaLoader.resolveDocument(this.fXSDDescription, locationPairs, this.fEntityResolver);
                    if (grammar != null && this.fNamespaceGrowth) {
                        try {
                            if (grammar.getDocumentLocations().contains(XMLEntityManager.expandSystemId(xis.getSystemId(), xis.getBaseSystemId(), false))) {
                                toParseSchema = false;
                            }
                        } catch (URI.MalformedURIException e2) {
                        }
                    }
                    if (toParseSchema) {
                        grammar = this.fSchemaLoader.loadSchema(this.fXSDDescription, xis, this.fLocationPairs);
                    }
                } catch (IOException e3) {
                    String[] locationHints = this.fXSDDescription.getLocationHints();
                    XMLErrorReporter xMLErrorReporter = this.fXSIErrorReporter.fErrorReporter;
                    Object[] objArr = new Object[1];
                    objArr[0] = locationHints != null ? locationHints[0] : XMLSymbols.EMPTY_STRING;
                    xMLErrorReporter.reportError(XSMessageFormatter.SCHEMA_DOMAIN, "schema_reference.4", objArr, (short) 0);
                }
            }
        }
        return grammar;
    }

    private void setLocationHints(XSDDescription desc, String[] locations, SchemaGrammar grammar) {
        int length = locations.length;
        if (grammar == null) {
            this.fXSDDescription.fLocationHints = new String[length];
            System.arraycopy(locations, 0, this.fXSDDescription.fLocationHints, 0, length);
            return;
        }
        setLocationHints(desc, locations, grammar.getDocumentLocations());
    }

    private void setLocationHints(XSDDescription desc, String[] locations, StringList docLocations) {
        int length = locations.length;
        String[] hints = new String[length];
        int counter = 0;
        for (int i2 = 0; i2 < length; i2++) {
            try {
                String id = XMLEntityManager.expandSystemId(locations[i2], desc.getBaseSystemId(), false);
                if (!docLocations.contains(id)) {
                    int i3 = counter;
                    counter++;
                    hints[i3] = locations[i2];
                }
            } catch (URI.MalformedURIException e2) {
            }
        }
        if (counter > 0) {
            if (counter == length) {
                this.fXSDDescription.fLocationHints = hints;
                return;
            }
            this.fXSDDescription.fLocationHints = new String[counter];
            System.arraycopy(hints, 0, this.fXSDDescription.fLocationHints, 0, counter);
        }
    }

    XSTypeDefinition getAndCheckXsiType(QName element, String xsiType, XMLAttributes attributes) throws XNIException {
        SchemaGrammar grammar;
        try {
            QName typeName = (QName) this.fQNameDV.validate(xsiType, (ValidationContext) this.fValidationState, (ValidatedInfo) null);
            XSTypeDefinition type = null;
            if (typeName.uri == SchemaSymbols.URI_SCHEMAFORSCHEMA) {
                type = SchemaGrammar.SG_SchemaNS.getGlobalTypeDecl(typeName.localpart);
            }
            if (type == null && (grammar = findSchemaGrammar((short) 7, typeName.uri, element, typeName, attributes)) != null) {
                type = grammar.getGlobalTypeDecl(typeName.localpart);
            }
            if (type == null) {
                reportSchemaError("cvc-elt.4.2", new Object[]{element.rawname, xsiType});
                return null;
            }
            if (this.fCurrentType != null) {
                short block = this.fCurrentElemDecl.fBlock;
                if (this.fCurrentType.getTypeCategory() == 15) {
                    block = (short) (block | ((XSComplexTypeDecl) this.fCurrentType).fBlock);
                }
                if (!XSConstraints.checkTypeDerivationOk(type, this.fCurrentType, block)) {
                    reportSchemaError("cvc-elt.4.3", new Object[]{element.rawname, xsiType, this.fCurrentType.getName()});
                }
            }
            return type;
        } catch (InvalidDatatypeValueException e2) {
            reportSchemaError(e2.getKey(), e2.getArgs());
            reportSchemaError("cvc-elt.4.1", new Object[]{element.rawname, SchemaSymbols.URI_XSI + "," + SchemaSymbols.XSI_TYPE, xsiType});
            return null;
        }
    }

    boolean getXsiNil(QName element, String xsiNil) throws XNIException {
        if (this.fCurrentElemDecl != null && !this.fCurrentElemDecl.getNillable()) {
            reportSchemaError("cvc-elt.3.1", new Object[]{element.rawname, SchemaSymbols.URI_XSI + "," + SchemaSymbols.XSI_NIL});
            return false;
        }
        String value = XMLChar.trim(xsiNil);
        if (value.equals("true") || value.equals("1")) {
            if (this.fCurrentElemDecl != null && this.fCurrentElemDecl.getConstraintType() == 2) {
                reportSchemaError("cvc-elt.3.2.2", new Object[]{element.rawname, SchemaSymbols.URI_XSI + "," + SchemaSymbols.XSI_NIL});
                return true;
            }
            return true;
        }
        return false;
    }

    /* JADX WARN: Removed duplicated region for block: B:40:0x0147  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    void processAttributes(com.sun.org.apache.xerces.internal.xni.QName r9, com.sun.org.apache.xerces.internal.xni.XMLAttributes r10, com.sun.org.apache.xerces.internal.impl.xs.XSAttributeGroupDecl r11) throws com.sun.org.apache.xerces.internal.xni.XNIException {
        /*
            Method dump skipped, instructions count: 784
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.org.apache.xerces.internal.impl.xs.XMLSchemaValidator.processAttributes(com.sun.org.apache.xerces.internal.xni.QName, com.sun.org.apache.xerces.internal.xni.XMLAttributes, com.sun.org.apache.xerces.internal.impl.xs.XSAttributeGroupDecl):void");
    }

    void processOneAttribute(QName element, XMLAttributes attributes, int index, XSAttributeDecl currDecl, XSAttributeUseImpl currUse, AttributePSVImpl attrPSVI) throws XNIException {
        boolean zIsIDType;
        String attrValue = attributes.getValue(index);
        this.fXSIErrorReporter.pushContext();
        XSSimpleType attDV = currDecl.fType;
        Object actualValue = null;
        try {
            actualValue = attDV.validate(attrValue, (ValidationContext) this.fValidationState, this.fValidatedInfo);
            if (this.fNormalizeData) {
                attributes.setValue(index, this.fValidatedInfo.normalizedValue);
            }
            if (attributes instanceof XMLAttributesImpl) {
                XMLAttributesImpl attrs = (XMLAttributesImpl) attributes;
                if (this.fValidatedInfo.memberType != null) {
                    zIsIDType = this.fValidatedInfo.memberType.isIDType();
                } else {
                    zIsIDType = attDV.isIDType();
                }
                boolean schemaId = zIsIDType;
                attrs.setSchemaId(index, schemaId);
            }
            if (attDV.getVariety() == 1 && attDV.getPrimitiveKind() == 20) {
                QName qName = (QName) actualValue;
                SchemaGrammar grammar = this.fGrammarBucket.getGrammar(qName.uri);
                if (grammar != null) {
                    this.fNotation = grammar.getGlobalNotationDecl(qName.localpart);
                }
            }
        } catch (InvalidDatatypeValueException idve) {
            reportSchemaError(idve.getKey(), idve.getArgs());
            reportSchemaError("cvc-attribute.3", new Object[]{element.rawname, this.fTempQName.rawname, attrValue, attDV.getName()});
        }
        if (actualValue != null && currDecl.getConstraintType() == 2 && (!isComparable(this.fValidatedInfo, currDecl.fDefault) || !actualValue.equals(currDecl.fDefault.actualValue))) {
            reportSchemaError("cvc-attribute.4", new Object[]{element.rawname, this.fTempQName.rawname, attrValue, currDecl.fDefault.stringValue()});
        }
        if (actualValue != null && currUse != null && currUse.fConstraintType == 2 && (!isComparable(this.fValidatedInfo, currUse.fDefault) || !actualValue.equals(currUse.fDefault.actualValue))) {
            reportSchemaError("cvc-complex-type.3.1", new Object[]{element.rawname, this.fTempQName.rawname, attrValue, currUse.fDefault.stringValue()});
        }
        if (this.fIdConstraint) {
            attrPSVI.fActualValue = actualValue;
        }
        if (this.fAugPSVI) {
            attrPSVI.fDeclaration = currDecl;
            attrPSVI.fTypeDecl = attDV;
            attrPSVI.fMemberType = this.fValidatedInfo.memberType;
            attrPSVI.fNormalizedValue = this.fValidatedInfo.normalizedValue;
            attrPSVI.fActualValue = this.fValidatedInfo.actualValue;
            attrPSVI.fActualValueType = this.fValidatedInfo.actualValueType;
            attrPSVI.fItemValueTypes = this.fValidatedInfo.itemValueTypes;
            attrPSVI.fValidationAttempted = (short) 2;
            String[] errors = this.fXSIErrorReporter.mergeContext();
            attrPSVI.fErrorCodes = errors;
            attrPSVI.fValidity = errors == null ? (short) 2 : (short) 1;
        }
    }

    void addDefaultAttributes(QName element, XMLAttributes attributes, XSAttributeGroupDecl attrGrp) throws XNIException {
        boolean zIsIDType;
        XSObjectList attrUses = attrGrp.getAttributeUses();
        int useCount = attrUses.getLength();
        for (int i2 = 0; i2 < useCount; i2++) {
            XSAttributeUseImpl currUse = (XSAttributeUseImpl) attrUses.item(i2);
            XSAttributeDecl currDecl = currUse.fAttrDecl;
            short constType = currUse.fConstraintType;
            ValidatedInfo defaultValue = currUse.fDefault;
            if (constType == 0) {
                constType = currDecl.getConstraintType();
                defaultValue = currDecl.fDefault;
            }
            boolean isSpecified = attributes.getValue(currDecl.fTargetNamespace, currDecl.fName) != null;
            if (currUse.fUse == 1 && !isSpecified) {
                reportSchemaError("cvc-complex-type.4", new Object[]{element.rawname, currDecl.fName});
            }
            if (!isSpecified && constType != 0) {
                QName attName = new QName(null, currDecl.fName, currDecl.fName, currDecl.fTargetNamespace);
                String normalized = defaultValue != null ? defaultValue.stringValue() : "";
                int attrIndex = attributes.addAttribute(attName, "CDATA", normalized);
                if (attributes instanceof XMLAttributesImpl) {
                    XMLAttributesImpl attrs = (XMLAttributesImpl) attributes;
                    if (defaultValue != null && defaultValue.memberType != null) {
                        zIsIDType = defaultValue.memberType.isIDType();
                    } else {
                        zIsIDType = currDecl.fType.isIDType();
                    }
                    boolean schemaId = zIsIDType;
                    attrs.setSchemaId(attrIndex, schemaId);
                }
                if (this.fAugPSVI) {
                    Augmentations augs = attributes.getAugmentations(attrIndex);
                    AttributePSVImpl attrPSVI = new AttributePSVImpl();
                    augs.putItem(Constants.ATTRIBUTE_PSVI, attrPSVI);
                    attrPSVI.fDeclaration = currDecl;
                    attrPSVI.fTypeDecl = currDecl.fType;
                    attrPSVI.fMemberType = defaultValue.memberType;
                    attrPSVI.fNormalizedValue = normalized;
                    attrPSVI.fActualValue = defaultValue.actualValue;
                    attrPSVI.fActualValueType = defaultValue.actualValueType;
                    attrPSVI.fItemValueTypes = defaultValue.itemValueTypes;
                    attrPSVI.fValidationContext = this.fValidationRoot;
                    attrPSVI.fValidity = (short) 2;
                    attrPSVI.fValidationAttempted = (short) 2;
                    attrPSVI.fSpecified = true;
                }
            }
        }
    }

    void processElementContent(QName element) throws XNIException {
        if (this.fCurrentElemDecl != null && this.fCurrentElemDecl.fDefault != null && !this.fSawText && !this.fSubElement && !this.fNil) {
            String strv = this.fCurrentElemDecl.fDefault.stringValue();
            int bufLen = strv.length();
            if (this.fNormalizedStr.ch == null || this.fNormalizedStr.ch.length < bufLen) {
                this.fNormalizedStr.ch = new char[bufLen];
            }
            strv.getChars(0, bufLen, this.fNormalizedStr.ch, 0);
            this.fNormalizedStr.offset = 0;
            this.fNormalizedStr.length = bufLen;
            this.fDefaultValue = this.fNormalizedStr;
        }
        this.fValidatedInfo.normalizedValue = null;
        if (this.fNil && (this.fSubElement || this.fSawText)) {
            reportSchemaError("cvc-elt.3.2.1", new Object[]{element.rawname, SchemaSymbols.URI_XSI + "," + SchemaSymbols.XSI_NIL});
        }
        this.fValidatedInfo.reset();
        if (this.fCurrentElemDecl != null && this.fCurrentElemDecl.getConstraintType() != 0 && !this.fSubElement && !this.fSawText && !this.fNil) {
            if (this.fCurrentType != this.fCurrentElemDecl.fType && XSConstraints.ElementDefaultValidImmediate(this.fCurrentType, this.fCurrentElemDecl.fDefault.stringValue(), this.fState4XsiType, null) == null) {
                reportSchemaError("cvc-elt.5.1.1", new Object[]{element.rawname, this.fCurrentType.getName(), this.fCurrentElemDecl.fDefault.stringValue()});
            }
            elementLocallyValidType(element, this.fCurrentElemDecl.fDefault.stringValue());
        } else {
            Object actualValue = elementLocallyValidType(element, this.fBuffer);
            if (this.fCurrentElemDecl != null && this.fCurrentElemDecl.getConstraintType() == 2 && !this.fNil) {
                String content = this.fBuffer.toString();
                if (this.fSubElement) {
                    reportSchemaError("cvc-elt.5.2.2.1", new Object[]{element.rawname});
                }
                if (this.fCurrentType.getTypeCategory() == 15) {
                    XSComplexTypeDecl ctype = (XSComplexTypeDecl) this.fCurrentType;
                    if (ctype.fContentType == 3) {
                        if (!this.fCurrentElemDecl.fDefault.normalizedValue.equals(content)) {
                            reportSchemaError("cvc-elt.5.2.2.2.1", new Object[]{element.rawname, content, this.fCurrentElemDecl.fDefault.normalizedValue});
                        }
                    } else if (ctype.fContentType == 1 && actualValue != null && (!isComparable(this.fValidatedInfo, this.fCurrentElemDecl.fDefault) || !actualValue.equals(this.fCurrentElemDecl.fDefault.actualValue))) {
                        reportSchemaError("cvc-elt.5.2.2.2.2", new Object[]{element.rawname, content, this.fCurrentElemDecl.fDefault.stringValue()});
                    }
                } else if (this.fCurrentType.getTypeCategory() == 16 && actualValue != null && (!isComparable(this.fValidatedInfo, this.fCurrentElemDecl.fDefault) || !actualValue.equals(this.fCurrentElemDecl.fDefault.actualValue))) {
                    reportSchemaError("cvc-elt.5.2.2.2.2", new Object[]{element.rawname, content, this.fCurrentElemDecl.fDefault.stringValue()});
                }
            }
        }
        if (this.fDefaultValue == null && this.fNormalizeData && this.fDocumentHandler != null && this.fUnionType) {
            String content2 = this.fValidatedInfo.normalizedValue;
            if (content2 == null) {
                content2 = this.fBuffer.toString();
            }
            int bufLen2 = content2.length();
            if (this.fNormalizedStr.ch == null || this.fNormalizedStr.ch.length < bufLen2) {
                this.fNormalizedStr.ch = new char[bufLen2];
            }
            content2.getChars(0, bufLen2, this.fNormalizedStr.ch, 0);
            this.fNormalizedStr.offset = 0;
            this.fNormalizedStr.length = bufLen2;
            this.fDocumentHandler.characters(this.fNormalizedStr, null);
        }
    }

    Object elementLocallyValidType(QName element, Object textContent) throws XNIException {
        if (this.fCurrentType == null) {
            return null;
        }
        Object retValue = null;
        if (this.fCurrentType.getTypeCategory() == 16) {
            if (this.fSubElement) {
                reportSchemaError("cvc-type.3.1.2", new Object[]{element.rawname});
            }
            if (!this.fNil) {
                XSSimpleType dv = (XSSimpleType) this.fCurrentType;
                try {
                    if (!this.fNormalizeData || this.fUnionType) {
                        this.fValidationState.setNormalizationRequired(true);
                    }
                    retValue = dv.validate(textContent, this.fValidationState, this.fValidatedInfo);
                } catch (InvalidDatatypeValueException e2) {
                    reportSchemaError(e2.getKey(), e2.getArgs());
                    reportSchemaError("cvc-type.3.1.3", new Object[]{element.rawname, textContent});
                }
            }
        } else {
            retValue = elementLocallyValidComplexType(element, textContent);
        }
        return retValue;
    }

    Object elementLocallyValidComplexType(QName element, Object textContent) throws XNIException {
        Object actualValue = null;
        XSComplexTypeDecl ctype = (XSComplexTypeDecl) this.fCurrentType;
        if (!this.fNil) {
            if (ctype.fContentType == 0 && (this.fSubElement || this.fSawText)) {
                reportSchemaError("cvc-complex-type.2.1", new Object[]{element.rawname});
            } else if (ctype.fContentType == 1) {
                if (this.fSubElement) {
                    reportSchemaError("cvc-complex-type.2.2", new Object[]{element.rawname});
                }
                XSSimpleType dv = ctype.fXSSimpleType;
                try {
                    if (!this.fNormalizeData || this.fUnionType) {
                        this.fValidationState.setNormalizationRequired(true);
                    }
                    actualValue = dv.validate(textContent, this.fValidationState, this.fValidatedInfo);
                } catch (InvalidDatatypeValueException e2) {
                    reportSchemaError(e2.getKey(), e2.getArgs());
                    reportSchemaError("cvc-complex-type.2.2", new Object[]{element.rawname});
                }
            } else if (ctype.fContentType == 2 && this.fSawCharacters) {
                reportSchemaError("cvc-complex-type.2.3", new Object[]{element.rawname});
            }
            if (ctype.fContentType == 2 || ctype.fContentType == 3) {
                if (this.fCurrCMState[0] >= 0 && !this.fCurrentCM.endContentModel(this.fCurrCMState)) {
                    String expected = expectedStr(this.fCurrentCM.whatCanGoHere(this.fCurrCMState));
                    reportSchemaError("cvc-complex-type.2.4.b", new Object[]{element.rawname, expected});
                } else {
                    ArrayList errors = this.fCurrentCM.checkMinMaxBounds();
                    if (errors != null) {
                        for (int i2 = 0; i2 < errors.size(); i2 += 2) {
                            reportSchemaError((String) errors.get(i2), new Object[]{element.rawname, errors.get(i2 + 1)});
                        }
                    }
                }
            }
        }
        return actualValue;
    }

    void reportSchemaError(String key, Object[] arguments) throws XNIException {
        if (this.fDoValidation) {
            this.fXSIErrorReporter.reportError(XSMessageFormatter.SCHEMA_DOMAIN, key, arguments, (short) 1);
        }
    }

    private boolean isComparable(ValidatedInfo info1, ValidatedInfo info2) {
        short primitiveType1 = convertToPrimitiveKind(info1.actualValueType);
        short primitiveType2 = convertToPrimitiveKind(info2.actualValueType);
        if (primitiveType1 != primitiveType2) {
            return (primitiveType1 == 1 && primitiveType2 == 2) || (primitiveType1 == 2 && primitiveType2 == 1);
        }
        if (primitiveType1 == 44 || primitiveType1 == 43) {
            ShortList typeList1 = info1.itemValueTypes;
            ShortList typeList2 = info2.itemValueTypes;
            int typeList1Length = typeList1 != null ? typeList1.getLength() : 0;
            int typeList2Length = typeList2 != null ? typeList2.getLength() : 0;
            if (typeList1Length != typeList2Length) {
                return false;
            }
            for (int i2 = 0; i2 < typeList1Length; i2++) {
                short primitiveItem1 = convertToPrimitiveKind(typeList1.item(i2));
                short primitiveItem2 = convertToPrimitiveKind(typeList2.item(i2));
                if (primitiveItem1 != primitiveItem2 && ((primitiveItem1 != 1 || primitiveItem2 != 2) && (primitiveItem1 != 2 || primitiveItem2 != 1))) {
                    return false;
                }
            }
            return true;
        }
        return true;
    }

    private short convertToPrimitiveKind(short valueType) {
        if (valueType <= 20) {
            return valueType;
        }
        if (valueType <= 29) {
            return (short) 2;
        }
        if (valueType <= 42) {
            return (short) 4;
        }
        return valueType;
    }

    private String expectedStr(Vector expected) {
        StringBuffer ret = new StringBuffer(VectorFormat.DEFAULT_PREFIX);
        int size = expected.size();
        for (int i2 = 0; i2 < size; i2++) {
            if (i2 > 0) {
                ret.append(", ");
            }
            ret.append(expected.elementAt(i2).toString());
        }
        ret.append('}');
        return ret.toString();
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/XMLSchemaValidator$XPathMatcherStack.class */
    protected static class XPathMatcherStack {
        protected int fMatchersCount;
        protected XPathMatcher[] fMatchers = new XPathMatcher[4];
        protected IntStack fContextStack = new IntStack();

        public void clear() {
            for (int i2 = 0; i2 < this.fMatchersCount; i2++) {
                this.fMatchers[i2] = null;
            }
            this.fMatchersCount = 0;
            this.fContextStack.clear();
        }

        public int size() {
            return this.fContextStack.size();
        }

        public int getMatcherCount() {
            return this.fMatchersCount;
        }

        public void addMatcher(XPathMatcher matcher) {
            ensureMatcherCapacity();
            XPathMatcher[] xPathMatcherArr = this.fMatchers;
            int i2 = this.fMatchersCount;
            this.fMatchersCount = i2 + 1;
            xPathMatcherArr[i2] = matcher;
        }

        public XPathMatcher getMatcherAt(int index) {
            return this.fMatchers[index];
        }

        public void pushContext() {
            this.fContextStack.push(this.fMatchersCount);
        }

        public void popContext() {
            this.fMatchersCount = this.fContextStack.pop();
        }

        private void ensureMatcherCapacity() {
            if (this.fMatchersCount == this.fMatchers.length) {
                XPathMatcher[] array = new XPathMatcher[this.fMatchers.length * 2];
                System.arraycopy(this.fMatchers, 0, array, 0, this.fMatchers.length);
                this.fMatchers = array;
            }
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/XMLSchemaValidator$ValueStoreBase.class */
    protected abstract class ValueStoreBase implements ValueStore {
        protected IdentityConstraint fIdentityConstraint;
        protected int fFieldCount;
        protected Field[] fFields;
        protected Object[] fLocalValues;
        protected short[] fLocalValueTypes;
        protected ShortList[] fLocalItemValueTypes;
        protected int fValuesCount;
        public final Vector fValues = new Vector();
        public ShortVector fValueTypes = null;
        public Vector fItemValueTypes = null;
        private boolean fUseValueTypeVector = false;
        private int fValueTypesLength = 0;
        private short fValueType = 0;
        private boolean fUseItemValueTypeVector = false;
        private int fItemValueTypesLength = 0;
        private ShortList fItemValueType = null;
        final StringBuffer fTempBuffer = new StringBuffer();

        protected ValueStoreBase(IdentityConstraint identityConstraint) {
            this.fFieldCount = 0;
            this.fFields = null;
            this.fLocalValues = null;
            this.fLocalValueTypes = null;
            this.fLocalItemValueTypes = null;
            this.fIdentityConstraint = identityConstraint;
            this.fFieldCount = this.fIdentityConstraint.getFieldCount();
            this.fFields = new Field[this.fFieldCount];
            this.fLocalValues = new Object[this.fFieldCount];
            this.fLocalValueTypes = new short[this.fFieldCount];
            this.fLocalItemValueTypes = new ShortList[this.fFieldCount];
            for (int i2 = 0; i2 < this.fFieldCount; i2++) {
                this.fFields[i2] = this.fIdentityConstraint.getFieldAt(i2);
            }
        }

        public void clear() {
            this.fValuesCount = 0;
            this.fUseValueTypeVector = false;
            this.fValueTypesLength = 0;
            this.fValueType = (short) 0;
            this.fUseItemValueTypeVector = false;
            this.fItemValueTypesLength = 0;
            this.fItemValueType = null;
            this.fValues.setSize(0);
            if (this.fValueTypes != null) {
                this.fValueTypes.clear();
            }
            if (this.fItemValueTypes != null) {
                this.fItemValueTypes.setSize(0);
            }
        }

        public void append(ValueStoreBase newVal) {
            for (int i2 = 0; i2 < newVal.fValues.size(); i2++) {
                this.fValues.addElement(newVal.fValues.elementAt(i2));
            }
        }

        public void startValueScope() {
            this.fValuesCount = 0;
            for (int i2 = 0; i2 < this.fFieldCount; i2++) {
                this.fLocalValues[i2] = null;
                this.fLocalValueTypes[i2] = 0;
                this.fLocalItemValueTypes[i2] = null;
            }
        }

        public void endValueScope() throws XNIException {
            if (this.fValuesCount == 0) {
                if (this.fIdentityConstraint.getCategory() == 1) {
                    String eName = this.fIdentityConstraint.getElementName();
                    String cName = this.fIdentityConstraint.getIdentityConstraintName();
                    XMLSchemaValidator.this.reportSchemaError("AbsentKeyValue", new Object[]{eName, cName});
                    return;
                }
                return;
            }
            if (this.fValuesCount != this.fFieldCount && this.fIdentityConstraint.getCategory() == 1) {
                UniqueOrKey key = (UniqueOrKey) this.fIdentityConstraint;
                String eName2 = this.fIdentityConstraint.getElementName();
                String cName2 = key.getIdentityConstraintName();
                XMLSchemaValidator.this.reportSchemaError("KeyNotEnoughValues", new Object[]{eName2, cName2});
            }
        }

        public void endDocumentFragment() {
        }

        public void endDocument() {
        }

        @Override // com.sun.org.apache.xerces.internal.impl.xs.identity.ValueStore
        public void reportError(String key, Object[] args) throws XNIException {
            XMLSchemaValidator.this.reportSchemaError(key, args);
        }

        @Override // com.sun.org.apache.xerces.internal.impl.xs.identity.ValueStore
        public void addValue(Field field, Object actualValue, short valueType, ShortList itemValueType) throws XNIException {
            int i2 = this.fFieldCount - 1;
            while (i2 > -1 && this.fFields[i2] != field) {
                i2--;
            }
            if (i2 == -1) {
                String eName = this.fIdentityConstraint.getElementName();
                String cName = this.fIdentityConstraint.getIdentityConstraintName();
                XMLSchemaValidator.this.reportSchemaError("UnknownField", new Object[]{field.toString(), eName, cName});
                return;
            }
            if (Boolean.TRUE != XMLSchemaValidator.this.mayMatch(field)) {
                String cName2 = this.fIdentityConstraint.getIdentityConstraintName();
                XMLSchemaValidator.this.reportSchemaError("FieldMultipleMatch", new Object[]{field.toString(), cName2});
            } else {
                this.fValuesCount++;
            }
            this.fLocalValues[i2] = actualValue;
            this.fLocalValueTypes[i2] = valueType;
            this.fLocalItemValueTypes[i2] = itemValueType;
            if (this.fValuesCount == this.fFieldCount) {
                checkDuplicateValues();
                for (int i3 = 0; i3 < this.fFieldCount; i3++) {
                    this.fValues.addElement(this.fLocalValues[i3]);
                    addValueType(this.fLocalValueTypes[i3]);
                    addItemValueType(this.fLocalItemValueTypes[i3]);
                }
            }
        }

        public boolean contains() {
            int size = this.fValues.size();
            int i2 = 0;
            while (true) {
                int i3 = i2;
                if (i3 < size) {
                    int next = i3 + this.fFieldCount;
                    for (int j2 = 0; j2 < this.fFieldCount; j2++) {
                        Object value1 = this.fLocalValues[j2];
                        Object value2 = this.fValues.elementAt(i3);
                        short valueType1 = this.fLocalValueTypes[j2];
                        short valueType2 = getValueTypeAt(i3);
                        if (value1 == null || value2 == null || valueType1 != valueType2 || !value1.equals(value2)) {
                            break;
                        }
                        if (valueType1 == 44 || valueType1 == 43) {
                            ShortList list1 = this.fLocalItemValueTypes[j2];
                            ShortList list2 = getItemValueTypeAt(i3);
                            if (list1 == null || list2 == null || !list1.equals(list2)) {
                                break;
                            }
                        }
                        i3++;
                        i2 = next;
                    }
                    return true;
                }
                return false;
            }
        }

        public int contains(ValueStoreBase vsb) {
            Vector values = vsb.fValues;
            int size1 = values.size();
            if (this.fFieldCount <= 1) {
                for (int i2 = 0; i2 < size1; i2++) {
                    short val = vsb.getValueTypeAt(i2);
                    if (!valueTypeContains(val) || !this.fValues.contains(values.elementAt(i2))) {
                        return i2;
                    }
                    if ((val == 44 || val == 43) && !itemValueTypeContains(vsb.getItemValueTypeAt(i2))) {
                        return i2;
                    }
                }
                return -1;
            }
            int size2 = this.fValues.size();
            int i3 = 0;
            while (true) {
                int i4 = i3;
                if (i4 < size1) {
                    int i5 = 0;
                    while (true) {
                        int j2 = i5;
                        if (j2 < size2) {
                            for (int k2 = 0; k2 < this.fFieldCount; k2++) {
                                Object value1 = values.elementAt(i4 + k2);
                                Object value2 = this.fValues.elementAt(j2 + k2);
                                short valueType1 = vsb.getValueTypeAt(i4 + k2);
                                short valueType2 = getValueTypeAt(j2 + k2);
                                if (value1 == value2 || (valueType1 == valueType2 && value1 != null && value1.equals(value2))) {
                                    if (valueType1 == 44 || valueType1 == 43) {
                                        ShortList list1 = vsb.getItemValueTypeAt(i4 + k2);
                                        ShortList list2 = getItemValueTypeAt(j2 + k2);
                                        if (list1 == null || list2 == null || !list1.equals(list2)) {
                                            break;
                                        }
                                    }
                                }
                                i5 = j2 + this.fFieldCount;
                            }
                        } else {
                            return i4;
                        }
                    }
                } else {
                    return -1;
                }
                i3 = i4 + this.fFieldCount;
            }
        }

        protected void checkDuplicateValues() {
        }

        protected String toString(Object[] values) {
            int size = values.length;
            if (size == 0) {
                return "";
            }
            this.fTempBuffer.setLength(0);
            for (int i2 = 0; i2 < size; i2++) {
                if (i2 > 0) {
                    this.fTempBuffer.append(',');
                }
                this.fTempBuffer.append(values[i2]);
            }
            return this.fTempBuffer.toString();
        }

        protected String toString(Vector values, int start, int length) {
            if (length == 0) {
                return "";
            }
            if (length == 1) {
                return String.valueOf(values.elementAt(start));
            }
            StringBuffer str = new StringBuffer();
            for (int i2 = 0; i2 < length; i2++) {
                if (i2 > 0) {
                    str.append(',');
                }
                str.append(values.elementAt(start + i2));
            }
            return str.toString();
        }

        public String toString() {
            String s2 = super.toString();
            int index1 = s2.lastIndexOf(36);
            if (index1 != -1) {
                s2 = s2.substring(index1 + 1);
            }
            int index2 = s2.lastIndexOf(46);
            if (index2 != -1) {
                s2 = s2.substring(index2 + 1);
            }
            return s2 + '[' + ((Object) this.fIdentityConstraint) + ']';
        }

        private void addValueType(short type) {
            if (this.fUseValueTypeVector) {
                this.fValueTypes.add(type);
                return;
            }
            int i2 = this.fValueTypesLength;
            this.fValueTypesLength = i2 + 1;
            if (i2 == 0) {
                this.fValueType = type;
                return;
            }
            if (this.fValueType != type) {
                this.fUseValueTypeVector = true;
                if (this.fValueTypes == null) {
                    this.fValueTypes = new ShortVector(this.fValueTypesLength * 2);
                }
                for (int i3 = 1; i3 < this.fValueTypesLength; i3++) {
                    this.fValueTypes.add(this.fValueType);
                }
                this.fValueTypes.add(type);
            }
        }

        private short getValueTypeAt(int index) {
            if (this.fUseValueTypeVector) {
                return this.fValueTypes.valueAt(index);
            }
            return this.fValueType;
        }

        private boolean valueTypeContains(short value) {
            if (this.fUseValueTypeVector) {
                return this.fValueTypes.contains(value);
            }
            return this.fValueType == value;
        }

        private void addItemValueType(ShortList itemValueType) {
            if (this.fUseItemValueTypeVector) {
                this.fItemValueTypes.add(itemValueType);
                return;
            }
            int i2 = this.fItemValueTypesLength;
            this.fItemValueTypesLength = i2 + 1;
            if (i2 == 0) {
                this.fItemValueType = itemValueType;
                return;
            }
            if (this.fItemValueType != itemValueType) {
                if (this.fItemValueType == null || !this.fItemValueType.equals(itemValueType)) {
                    this.fUseItemValueTypeVector = true;
                    if (this.fItemValueTypes == null) {
                        this.fItemValueTypes = new Vector(this.fItemValueTypesLength * 2);
                    }
                    for (int i3 = 1; i3 < this.fItemValueTypesLength; i3++) {
                        this.fItemValueTypes.add(this.fItemValueType);
                    }
                    this.fItemValueTypes.add(itemValueType);
                }
            }
        }

        private ShortList getItemValueTypeAt(int index) {
            if (this.fUseItemValueTypeVector) {
                return (ShortList) this.fItemValueTypes.elementAt(index);
            }
            return this.fItemValueType;
        }

        private boolean itemValueTypeContains(ShortList value) {
            if (this.fUseItemValueTypeVector) {
                return this.fItemValueTypes.contains(value);
            }
            return this.fItemValueType == value || (this.fItemValueType != null && this.fItemValueType.equals(value));
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/XMLSchemaValidator$UniqueValueStore.class */
    protected class UniqueValueStore extends ValueStoreBase {
        public UniqueValueStore(UniqueOrKey unique) {
            super(unique);
        }

        @Override // com.sun.org.apache.xerces.internal.impl.xs.XMLSchemaValidator.ValueStoreBase
        protected void checkDuplicateValues() throws XNIException {
            if (contains()) {
                String value = toString(this.fLocalValues);
                String eName = this.fIdentityConstraint.getElementName();
                String cName = this.fIdentityConstraint.getIdentityConstraintName();
                XMLSchemaValidator.this.reportSchemaError("DuplicateUnique", new Object[]{value, eName, cName});
            }
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/XMLSchemaValidator$KeyValueStore.class */
    protected class KeyValueStore extends ValueStoreBase {
        public KeyValueStore(UniqueOrKey key) {
            super(key);
        }

        @Override // com.sun.org.apache.xerces.internal.impl.xs.XMLSchemaValidator.ValueStoreBase
        protected void checkDuplicateValues() throws XNIException {
            if (contains()) {
                String value = toString(this.fLocalValues);
                String eName = this.fIdentityConstraint.getElementName();
                String cName = this.fIdentityConstraint.getIdentityConstraintName();
                XMLSchemaValidator.this.reportSchemaError("DuplicateKey", new Object[]{value, eName, cName});
            }
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/XMLSchemaValidator$KeyRefValueStore.class */
    protected class KeyRefValueStore extends ValueStoreBase {
        protected ValueStoreBase fKeyValueStore;

        public KeyRefValueStore(KeyRef keyRef, KeyValueStore keyValueStore) {
            super(keyRef);
            this.fKeyValueStore = keyValueStore;
        }

        @Override // com.sun.org.apache.xerces.internal.impl.xs.XMLSchemaValidator.ValueStoreBase
        public void endDocumentFragment() throws XNIException {
            super.endDocumentFragment();
            this.fKeyValueStore = XMLSchemaValidator.this.fValueStoreCache.fGlobalIDConstraintMap.get(((KeyRef) this.fIdentityConstraint).getKey());
            if (this.fKeyValueStore == null) {
                String value = this.fIdentityConstraint.toString();
                XMLSchemaValidator.this.reportSchemaError("KeyRefOutOfScope", new Object[]{value});
                return;
            }
            int errorIndex = this.fKeyValueStore.contains(this);
            if (errorIndex != -1) {
                String values = toString(this.fValues, errorIndex, this.fFieldCount);
                String element = this.fIdentityConstraint.getElementName();
                String name = this.fIdentityConstraint.getName();
                XMLSchemaValidator.this.reportSchemaError("KeyNotFound", new Object[]{name, values, element});
            }
        }

        @Override // com.sun.org.apache.xerces.internal.impl.xs.XMLSchemaValidator.ValueStoreBase
        public void endDocument() {
            super.endDocument();
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/XMLSchemaValidator$ValueStoreCache.class */
    protected class ValueStoreCache {
        final LocalIDKey fLocalId;
        protected final Vector fValueStores = new Vector();
        protected final Map<LocalIDKey, ValueStoreBase> fIdentityConstraint2ValueStoreMap = new HashMap();
        protected final Stack<Map<IdentityConstraint, ValueStoreBase>> fGlobalMapStack = new Stack<>();
        protected final Map<IdentityConstraint, ValueStoreBase> fGlobalIDConstraintMap = new HashMap();

        public ValueStoreCache() {
            this.fLocalId = XMLSchemaValidator.this.new LocalIDKey();
        }

        public void startDocument() {
            this.fValueStores.removeAllElements();
            this.fIdentityConstraint2ValueStoreMap.clear();
            this.fGlobalIDConstraintMap.clear();
            this.fGlobalMapStack.removeAllElements();
        }

        public void startElement() {
            if (this.fGlobalIDConstraintMap.size() > 0) {
                this.fGlobalMapStack.push((Map) ((HashMap) this.fGlobalIDConstraintMap).clone());
            } else {
                this.fGlobalMapStack.push(null);
            }
            this.fGlobalIDConstraintMap.clear();
        }

        public void endElement() {
            Map<IdentityConstraint, ValueStoreBase> oldMap;
            if (this.fGlobalMapStack.isEmpty() || (oldMap = this.fGlobalMapStack.pop()) == null) {
                return;
            }
            for (Map.Entry<IdentityConstraint, ValueStoreBase> entry : oldMap.entrySet()) {
                IdentityConstraint id = entry.getKey();
                ValueStoreBase oldVal = entry.getValue();
                if (oldVal != null) {
                    ValueStoreBase currVal = this.fGlobalIDConstraintMap.get(id);
                    if (currVal == null) {
                        this.fGlobalIDConstraintMap.put(id, oldVal);
                    } else if (currVal != oldVal) {
                        currVal.append(oldVal);
                    }
                }
            }
        }

        public void initValueStoresFor(XSElementDecl eDecl, FieldActivator activator) {
            IdentityConstraint[] icArray = eDecl.fIDConstraints;
            int icCount = eDecl.fIDCPos;
            for (int i2 = 0; i2 < icCount; i2++) {
                switch (icArray[i2].getCategory()) {
                    case 1:
                        UniqueOrKey key = (UniqueOrKey) icArray[i2];
                        LocalIDKey toHash = XMLSchemaValidator.this.new LocalIDKey(key, XMLSchemaValidator.this.fElementDepth);
                        KeyValueStore keyValueStore = (KeyValueStore) this.fIdentityConstraint2ValueStoreMap.get(toHash);
                        if (keyValueStore == null) {
                            keyValueStore = XMLSchemaValidator.this.new KeyValueStore(key);
                            this.fIdentityConstraint2ValueStoreMap.put(toHash, keyValueStore);
                        } else {
                            keyValueStore.clear();
                        }
                        this.fValueStores.addElement(keyValueStore);
                        XMLSchemaValidator.this.activateSelectorFor(icArray[i2]);
                        break;
                    case 2:
                        KeyRef keyRef = (KeyRef) icArray[i2];
                        LocalIDKey toHash2 = XMLSchemaValidator.this.new LocalIDKey(keyRef, XMLSchemaValidator.this.fElementDepth);
                        KeyRefValueStore keyRefValueStore = (KeyRefValueStore) this.fIdentityConstraint2ValueStoreMap.get(toHash2);
                        if (keyRefValueStore == null) {
                            keyRefValueStore = XMLSchemaValidator.this.new KeyRefValueStore(keyRef, null);
                            this.fIdentityConstraint2ValueStoreMap.put(toHash2, keyRefValueStore);
                        } else {
                            keyRefValueStore.clear();
                        }
                        this.fValueStores.addElement(keyRefValueStore);
                        XMLSchemaValidator.this.activateSelectorFor(icArray[i2]);
                        break;
                    case 3:
                        UniqueOrKey unique = (UniqueOrKey) icArray[i2];
                        LocalIDKey toHash3 = XMLSchemaValidator.this.new LocalIDKey(unique, XMLSchemaValidator.this.fElementDepth);
                        UniqueValueStore uniqueValueStore = (UniqueValueStore) this.fIdentityConstraint2ValueStoreMap.get(toHash3);
                        if (uniqueValueStore == null) {
                            uniqueValueStore = XMLSchemaValidator.this.new UniqueValueStore(unique);
                            this.fIdentityConstraint2ValueStoreMap.put(toHash3, uniqueValueStore);
                        } else {
                            uniqueValueStore.clear();
                        }
                        this.fValueStores.addElement(uniqueValueStore);
                        XMLSchemaValidator.this.activateSelectorFor(icArray[i2]);
                        break;
                }
            }
        }

        public ValueStoreBase getValueStoreFor(IdentityConstraint id, int initialDepth) {
            this.fLocalId.fDepth = initialDepth;
            this.fLocalId.fId = id;
            return this.fIdentityConstraint2ValueStoreMap.get(this.fLocalId);
        }

        public ValueStoreBase getGlobalValueStoreFor(IdentityConstraint id) {
            return this.fGlobalIDConstraintMap.get(id);
        }

        public void transplant(IdentityConstraint id, int initialDepth) {
            this.fLocalId.fDepth = initialDepth;
            this.fLocalId.fId = id;
            ValueStoreBase newVals = this.fIdentityConstraint2ValueStoreMap.get(this.fLocalId);
            if (id.getCategory() == 2) {
                return;
            }
            ValueStoreBase currVals = this.fGlobalIDConstraintMap.get(id);
            if (currVals != null) {
                currVals.append(newVals);
                this.fGlobalIDConstraintMap.put(id, currVals);
            } else {
                this.fGlobalIDConstraintMap.put(id, newVals);
            }
        }

        public void endDocument() {
            int count = this.fValueStores.size();
            for (int i2 = 0; i2 < count; i2++) {
                ValueStoreBase valueStore = (ValueStoreBase) this.fValueStores.elementAt(i2);
                valueStore.endDocument();
            }
        }

        public String toString() {
            String s2 = super.toString();
            int index1 = s2.lastIndexOf(36);
            if (index1 != -1) {
                return s2.substring(index1 + 1);
            }
            int index2 = s2.lastIndexOf(46);
            if (index2 != -1) {
                return s2.substring(index2 + 1);
            }
            return s2;
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/XMLSchemaValidator$LocalIDKey.class */
    protected class LocalIDKey {
        public IdentityConstraint fId;
        public int fDepth;

        public LocalIDKey() {
        }

        public LocalIDKey(IdentityConstraint id, int depth) {
            this.fId = id;
            this.fDepth = depth;
        }

        public int hashCode() {
            return this.fId.hashCode() + this.fDepth;
        }

        public boolean equals(Object localIDKey) {
            if (localIDKey instanceof LocalIDKey) {
                LocalIDKey lIDKey = (LocalIDKey) localIDKey;
                return lIDKey.fId == this.fId && lIDKey.fDepth == this.fDepth;
            }
            return false;
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/XMLSchemaValidator$ShortVector.class */
    protected static final class ShortVector {
        private int fLength;
        private short[] fData;

        public ShortVector() {
        }

        public ShortVector(int initialCapacity) {
            this.fData = new short[initialCapacity];
        }

        public int length() {
            return this.fLength;
        }

        public void add(short value) {
            ensureCapacity(this.fLength + 1);
            short[] sArr = this.fData;
            int i2 = this.fLength;
            this.fLength = i2 + 1;
            sArr[i2] = value;
        }

        public short valueAt(int position) {
            return this.fData[position];
        }

        public void clear() {
            this.fLength = 0;
        }

        public boolean contains(short value) {
            for (int i2 = 0; i2 < this.fLength; i2++) {
                if (this.fData[i2] == value) {
                    return true;
                }
            }
            return false;
        }

        private void ensureCapacity(int size) {
            if (this.fData == null) {
                this.fData = new short[8];
            } else if (this.fData.length <= size) {
                short[] newdata = new short[this.fData.length * 2];
                System.arraycopy(this.fData, 0, newdata, 0, this.fData.length);
                this.fData = newdata;
            }
        }
    }
}
