package com.sun.org.apache.xerces.internal.impl.xs.traversers;

import com.sun.org.apache.xerces.internal.impl.XMLEntityManager;
import com.sun.org.apache.xerces.internal.impl.XMLErrorReporter;
import com.sun.org.apache.xerces.internal.impl.dv.SchemaDVFactory;
import com.sun.org.apache.xerces.internal.impl.dv.xs.XSSimpleTypeDecl;
import com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar;
import com.sun.org.apache.xerces.internal.impl.xs.SchemaNamespaceSupport;
import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import com.sun.org.apache.xerces.internal.impl.xs.XMLSchemaException;
import com.sun.org.apache.xerces.internal.impl.xs.XMLSchemaLoader;
import com.sun.org.apache.xerces.internal.impl.xs.XSAttributeDecl;
import com.sun.org.apache.xerces.internal.impl.xs.XSAttributeGroupDecl;
import com.sun.org.apache.xerces.internal.impl.xs.XSComplexTypeDecl;
import com.sun.org.apache.xerces.internal.impl.xs.XSDDescription;
import com.sun.org.apache.xerces.internal.impl.xs.XSDeclarationPool;
import com.sun.org.apache.xerces.internal.impl.xs.XSElementDecl;
import com.sun.org.apache.xerces.internal.impl.xs.XSGrammarBucket;
import com.sun.org.apache.xerces.internal.impl.xs.XSGroupDecl;
import com.sun.org.apache.xerces.internal.impl.xs.XSMessageFormatter;
import com.sun.org.apache.xerces.internal.impl.xs.XSModelGroupImpl;
import com.sun.org.apache.xerces.internal.impl.xs.XSNotationDecl;
import com.sun.org.apache.xerces.internal.impl.xs.XSParticleDecl;
import com.sun.org.apache.xerces.internal.impl.xs.identity.IdentityConstraint;
import com.sun.org.apache.xerces.internal.impl.xs.opti.ElementImpl;
import com.sun.org.apache.xerces.internal.impl.xs.opti.SchemaDOM;
import com.sun.org.apache.xerces.internal.impl.xs.opti.SchemaDOMParser;
import com.sun.org.apache.xerces.internal.impl.xs.opti.SchemaParsingConfig;
import com.sun.org.apache.xerces.internal.impl.xs.util.SimpleLocator;
import com.sun.org.apache.xerces.internal.impl.xs.util.XSInputSource;
import com.sun.org.apache.xerces.internal.parsers.SAXParser;
import com.sun.org.apache.xerces.internal.parsers.XML11Configuration;
import com.sun.org.apache.xerces.internal.util.DOMInputSource;
import com.sun.org.apache.xerces.internal.util.DOMUtil;
import com.sun.org.apache.xerces.internal.util.DefaultErrorHandler;
import com.sun.org.apache.xerces.internal.util.ErrorHandlerWrapper;
import com.sun.org.apache.xerces.internal.util.SAXInputSource;
import com.sun.org.apache.xerces.internal.util.StAXInputSource;
import com.sun.org.apache.xerces.internal.util.StAXLocationWrapper;
import com.sun.org.apache.xerces.internal.util.SymbolHash;
import com.sun.org.apache.xerces.internal.util.SymbolTable;
import com.sun.org.apache.xerces.internal.util.URI;
import com.sun.org.apache.xerces.internal.util.XMLSymbols;
import com.sun.org.apache.xerces.internal.utils.SecuritySupport;
import com.sun.org.apache.xerces.internal.utils.XMLSecurityManager;
import com.sun.org.apache.xerces.internal.utils.XMLSecurityPropertyManager;
import com.sun.org.apache.xerces.internal.xni.QName;
import com.sun.org.apache.xerces.internal.xni.XNIException;
import com.sun.org.apache.xerces.internal.xni.grammars.Grammar;
import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarDescription;
import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool;
import com.sun.org.apache.xerces.internal.xni.grammars.XMLSchemaDescription;
import com.sun.org.apache.xerces.internal.xni.parser.XMLComponentManager;
import com.sun.org.apache.xerces.internal.xni.parser.XMLConfigurationException;
import com.sun.org.apache.xerces.internal.xni.parser.XMLEntityResolver;
import com.sun.org.apache.xerces.internal.xni.parser.XMLErrorHandler;
import com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;
import com.sun.org.apache.xerces.internal.xni.parser.XMLParseException;
import com.sun.org.apache.xerces.internal.xs.StringList;
import com.sun.org.apache.xerces.internal.xs.XSAttributeDeclaration;
import com.sun.org.apache.xerces.internal.xs.XSAttributeGroupDefinition;
import com.sun.org.apache.xerces.internal.xs.XSAttributeUse;
import com.sun.org.apache.xerces.internal.xs.XSElementDeclaration;
import com.sun.org.apache.xerces.internal.xs.XSModelGroup;
import com.sun.org.apache.xerces.internal.xs.XSModelGroupDefinition;
import com.sun.org.apache.xerces.internal.xs.XSNamedMap;
import com.sun.org.apache.xerces.internal.xs.XSObject;
import com.sun.org.apache.xerces.internal.xs.XSObjectList;
import com.sun.org.apache.xerces.internal.xs.XSParticle;
import com.sun.org.apache.xerces.internal.xs.XSSimpleTypeDefinition;
import com.sun.org.apache.xerces.internal.xs.XSTerm;
import com.sun.org.apache.xerces.internal.xs.XSTypeDefinition;
import com.sun.org.apache.xerces.internal.xs.datatypes.ObjectList;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Stack;
import java.util.Vector;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import jdk.internal.dynalink.CallSiteDescriptor;
import jdk.xml.internal.JdkXmlUtils;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/traversers/XSDHandler.class */
public class XSDHandler {
    protected static final String VALIDATION = "http://xml.org/sax/features/validation";
    protected static final String XMLSCHEMA_VALIDATION = "http://apache.org/xml/features/validation/schema";
    protected static final String ALLOW_JAVA_ENCODINGS = "http://apache.org/xml/features/allow-java-encodings";
    protected static final String CONTINUE_AFTER_FATAL_ERROR = "http://apache.org/xml/features/continue-after-fatal-error";
    protected static final String STANDARD_URI_CONFORMANT_FEATURE = "http://apache.org/xml/features/standard-uri-conformant";
    protected static final String DISALLOW_DOCTYPE = "http://apache.org/xml/features/disallow-doctype-decl";
    protected static final String GENERATE_SYNTHETIC_ANNOTATIONS = "http://apache.org/xml/features/generate-synthetic-annotations";
    protected static final String VALIDATE_ANNOTATIONS = "http://apache.org/xml/features/validate-annotations";
    protected static final String HONOUR_ALL_SCHEMALOCATIONS = "http://apache.org/xml/features/honour-all-schemaLocations";
    protected static final String NAMESPACE_GROWTH = "http://apache.org/xml/features/namespace-growth";
    protected static final String TOLERATE_DUPLICATES = "http://apache.org/xml/features/internal/tolerate-duplicates";
    private static final String NAMESPACE_PREFIXES = "http://xml.org/sax/features/namespace-prefixes";
    protected static final String STRING_INTERNING = "http://xml.org/sax/features/string-interning";
    protected static final String ERROR_HANDLER = "http://apache.org/xml/properties/internal/error-handler";
    protected static final String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";
    public static final String ENTITY_RESOLVER = "http://apache.org/xml/properties/internal/entity-resolver";
    protected static final String ENTITY_MANAGER = "http://apache.org/xml/properties/internal/entity-manager";
    public static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
    public static final String XMLGRAMMAR_POOL = "http://apache.org/xml/properties/internal/grammar-pool";
    public static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
    protected static final String SECURITY_MANAGER = "http://apache.org/xml/properties/security-manager";
    protected static final String LOCALE = "http://apache.org/xml/properties/locale";
    private static final String XML_SECURITY_PROPERTY_MANAGER = "http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager";
    protected static final boolean DEBUG_NODE_POOL = false;
    static final int ATTRIBUTE_TYPE = 1;
    static final int ATTRIBUTEGROUP_TYPE = 2;
    static final int ELEMENT_TYPE = 3;
    static final int GROUP_TYPE = 4;
    static final int IDENTITYCONSTRAINT_TYPE = 5;
    static final int NOTATION_TYPE = 6;
    static final int TYPEDECL_TYPE = 7;
    public static final String REDEF_IDENTIFIER = "_fn3dktizrknc9pi";
    protected XSDeclarationPool fDeclPool;
    protected XMLSecurityManager fSecurityManager;
    private String fAccessExternalSchema;
    private String fAccessExternalDTD;
    private boolean registryEmpty;
    private Map<String, Element> fUnparsedAttributeRegistry;
    private Map<String, Element> fUnparsedAttributeGroupRegistry;
    private Map<String, Element> fUnparsedElementRegistry;
    private Map<String, Element> fUnparsedGroupRegistry;
    private Map<String, Element> fUnparsedIdentityConstraintRegistry;
    private Map<String, Element> fUnparsedNotationRegistry;
    private Map<String, Element> fUnparsedTypeRegistry;
    private Map<String, XSDocumentInfo> fUnparsedAttributeRegistrySub;
    private Map<String, XSDocumentInfo> fUnparsedAttributeGroupRegistrySub;
    private Map<String, XSDocumentInfo> fUnparsedElementRegistrySub;
    private Map<String, XSDocumentInfo> fUnparsedGroupRegistrySub;
    private Map<String, XSDocumentInfo> fUnparsedIdentityConstraintRegistrySub;
    private Map<String, XSDocumentInfo> fUnparsedNotationRegistrySub;
    private Map<String, XSDocumentInfo> fUnparsedTypeRegistrySub;
    private Map<String, XSDocumentInfo>[] fUnparsedRegistriesExt;
    private Map<XSDocumentInfo, Vector<XSDocumentInfo>> fDependencyMap;
    private Map<String, Vector> fImportMap;
    private Vector<String> fAllTNSs;
    private Map<String, XMLSchemaLoader.LocationArray> fLocationPairs;
    Map<Node, String> fHiddenNodes;
    private Map<XSDKey, Element> fTraversed;
    private Map<Element, String> fDoc2SystemId;
    private XSDocumentInfo fRoot;
    private Map fDoc2XSDocumentMap;
    private Map fRedefine2XSDMap;
    private Map fRedefine2NSSupport;
    private Map fRedefinedRestrictedAttributeGroupRegistry;
    private Map fRedefinedRestrictedGroupRegistry;
    private boolean fLastSchemaWasDuplicate;
    private boolean fValidateAnnotations;
    private boolean fHonourAllSchemaLocations;
    boolean fNamespaceGrowth;
    boolean fTolerateDuplicates;
    private XMLErrorReporter fErrorReporter;
    private XMLErrorHandler fErrorHandler;
    private Locale fLocale;
    private XMLEntityResolver fEntityManager;
    private XSAttributeChecker fAttributeChecker;
    private SymbolTable fSymbolTable;
    private XSGrammarBucket fGrammarBucket;
    private XSDDescription fSchemaGrammarDescription;
    private XMLGrammarPool fGrammarPool;
    private XMLSecurityPropertyManager fSecurityPropertyMgr;
    private boolean fOverrideDefaultParser;
    XSDAttributeGroupTraverser fAttributeGroupTraverser;
    XSDAttributeTraverser fAttributeTraverser;
    XSDComplexTypeTraverser fComplexTypeTraverser;
    XSDElementTraverser fElementTraverser;
    XSDGroupTraverser fGroupTraverser;
    XSDKeyrefTraverser fKeyrefTraverser;
    XSDNotationTraverser fNotationTraverser;
    XSDSimpleTypeTraverser fSimpleTypeTraverser;
    XSDUniqueOrKeyTraverser fUniqueOrKeyTraverser;
    XSDWildcardTraverser fWildCardTraverser;
    SchemaDVFactory fDVFactory;
    SchemaDOMParser fSchemaParser;
    SchemaContentHandler fXSContentHandler;
    StAXSchemaParser fStAXSchemaParser;
    XML11Configuration fAnnotationValidator;
    XSAnnotationGrammarPool fGrammarBucketAdapter;
    private static final int INIT_STACK_SIZE = 30;
    private static final int INC_STACK_SIZE = 10;
    private int fLocalElemStackPos;
    private XSParticleDecl[] fParticle;
    private Element[] fLocalElementDecl;
    private XSDocumentInfo[] fLocalElementDecl_schema;
    private int[] fAllContext;
    private XSObject[] fParent;
    private String[][] fLocalElemNamespaceContext;
    private static final int INIT_KEYREF_STACK = 2;
    private static final int INC_KEYREF_STACK_AMOUNT = 2;
    private int fKeyrefStackPos;
    private Element[] fKeyrefs;
    private XSDocumentInfo[] fKeyrefsMapXSDocumentInfo;
    private XSElementDecl[] fKeyrefElems;
    private String[][] fKeyrefNamespaceContext;
    SymbolHash fGlobalAttrDecls;
    SymbolHash fGlobalAttrGrpDecls;
    SymbolHash fGlobalElemDecls;
    SymbolHash fGlobalGroupDecls;
    SymbolHash fGlobalNotationDecls;
    SymbolHash fGlobalIDConstraintDecls;
    SymbolHash fGlobalTypeDecls;
    private Vector fReportedTNS;
    private SimpleLocator xl;
    private static final String[][] NS_ERROR_CODES = {new String[]{"src-include.2.1", "src-include.2.1"}, new String[]{"src-redefine.3.1", "src-redefine.3.1"}, new String[]{"src-import.3.1", "src-import.3.2"}, 0, new String[]{"TargetNamespace.1", "TargetNamespace.2"}, new String[]{"TargetNamespace.1", "TargetNamespace.2"}, new String[]{"TargetNamespace.1", "TargetNamespace.2"}, new String[]{"TargetNamespace.1", "TargetNamespace.2"}};
    private static final String[] ELE_ERROR_CODES = {"src-include.1", "src-redefine.2", "src-import.2", "schema_reference.4", "schema_reference.4", "schema_reference.4", "schema_reference.4", "schema_reference.4"};
    private static final String[] COMP_TYPE = {null, "attribute declaration", "attribute group", "element declaration", "group", "identity constraint", "notation", "type definition"};
    private static final String[] CIRCULAR_CODES = {"Internal-Error", "Internal-Error", "src-attribute_group.3", "e-props-correct.6", "mg-props-correct.2", "Internal-Error", "Internal-Error", "st-props-correct.2"};

    private String null2EmptyString(String ns) {
        return ns == null ? XMLSymbols.EMPTY_STRING : ns;
    }

    private String emptyString2Null(String ns) {
        if (ns == XMLSymbols.EMPTY_STRING) {
            return null;
        }
        return ns;
    }

    private String doc2SystemId(Element ele) {
        String documentURI = null;
        if (ele.getOwnerDocument() instanceof SchemaDOM) {
            documentURI = ((SchemaDOM) ele.getOwnerDocument()).getDocumentURI();
        }
        return documentURI != null ? documentURI : this.fDoc2SystemId.get(ele);
    }

    public XSDHandler() {
        this.fDeclPool = null;
        this.fSecurityManager = null;
        this.registryEmpty = true;
        this.fUnparsedAttributeRegistry = new HashMap();
        this.fUnparsedAttributeGroupRegistry = new HashMap();
        this.fUnparsedElementRegistry = new HashMap();
        this.fUnparsedGroupRegistry = new HashMap();
        this.fUnparsedIdentityConstraintRegistry = new HashMap();
        this.fUnparsedNotationRegistry = new HashMap();
        this.fUnparsedTypeRegistry = new HashMap();
        this.fUnparsedAttributeRegistrySub = new HashMap();
        this.fUnparsedAttributeGroupRegistrySub = new HashMap();
        this.fUnparsedElementRegistrySub = new HashMap();
        this.fUnparsedGroupRegistrySub = new HashMap();
        this.fUnparsedIdentityConstraintRegistrySub = new HashMap();
        this.fUnparsedNotationRegistrySub = new HashMap();
        this.fUnparsedTypeRegistrySub = new HashMap();
        this.fUnparsedRegistriesExt = new HashMap[]{null, null, null, null, null, null, null, null};
        this.fDependencyMap = new HashMap();
        this.fImportMap = new HashMap();
        this.fAllTNSs = new Vector<>();
        this.fLocationPairs = null;
        this.fHiddenNodes = null;
        this.fTraversed = new HashMap();
        this.fDoc2SystemId = new HashMap();
        this.fRoot = null;
        this.fDoc2XSDocumentMap = new HashMap();
        this.fRedefine2XSDMap = null;
        this.fRedefine2NSSupport = null;
        this.fRedefinedRestrictedAttributeGroupRegistry = new HashMap();
        this.fRedefinedRestrictedGroupRegistry = new HashMap();
        this.fValidateAnnotations = false;
        this.fHonourAllSchemaLocations = false;
        this.fNamespaceGrowth = false;
        this.fTolerateDuplicates = false;
        this.fSecurityPropertyMgr = null;
        this.fLocalElemStackPos = 0;
        this.fParticle = new XSParticleDecl[30];
        this.fLocalElementDecl = new Element[30];
        this.fLocalElementDecl_schema = new XSDocumentInfo[30];
        this.fAllContext = new int[30];
        this.fParent = new XSObject[30];
        this.fLocalElemNamespaceContext = new String[30][1];
        this.fKeyrefStackPos = 0;
        this.fKeyrefs = new Element[2];
        this.fKeyrefsMapXSDocumentInfo = new XSDocumentInfo[2];
        this.fKeyrefElems = new XSElementDecl[2];
        this.fKeyrefNamespaceContext = new String[2][1];
        this.fGlobalAttrDecls = new SymbolHash(12);
        this.fGlobalAttrGrpDecls = new SymbolHash(5);
        this.fGlobalElemDecls = new SymbolHash(25);
        this.fGlobalGroupDecls = new SymbolHash(5);
        this.fGlobalNotationDecls = new SymbolHash(1);
        this.fGlobalIDConstraintDecls = new SymbolHash(3);
        this.fGlobalTypeDecls = new SymbolHash(25);
        this.fReportedTNS = null;
        this.xl = new SimpleLocator();
        this.fHiddenNodes = new HashMap();
        this.fSchemaParser = new SchemaDOMParser(new SchemaParsingConfig());
    }

    public XSDHandler(XSGrammarBucket gBucket) {
        this();
        this.fGrammarBucket = gBucket;
        this.fSchemaGrammarDescription = new XSDDescription();
    }

    public SchemaGrammar parseSchema(XMLInputSource is, XSDDescription desc, Map<String, XMLSchemaLoader.LocationArray> locationPairs) throws DOMException, MissingResourceException, IOException, XNIException {
        Element schemaRoot;
        String schemaNamespace;
        this.fLocationPairs = locationPairs;
        this.fSchemaParser.resetNodePool();
        SchemaGrammar grammar = null;
        String schemaNamespace2 = null;
        short referType = desc.getContextType();
        if (referType != 3) {
            if (this.fHonourAllSchemaLocations && referType == 2 && isExistingGrammar(desc, this.fNamespaceGrowth)) {
                grammar = this.fGrammarBucket.getGrammar(desc.getTargetNamespace());
            } else {
                grammar = findGrammar(desc, this.fNamespaceGrowth);
            }
            if (grammar != null) {
                if (!this.fNamespaceGrowth) {
                    return grammar;
                }
                try {
                    if (grammar.getDocumentLocations().contains(XMLEntityManager.expandSystemId(is.getSystemId(), is.getBaseSystemId(), false))) {
                        return grammar;
                    }
                } catch (URI.MalformedURIException e2) {
                }
            }
            schemaNamespace2 = desc.getTargetNamespace();
            if (schemaNamespace2 != null) {
                schemaNamespace2 = this.fSymbolTable.addSymbol(schemaNamespace2);
            }
        }
        prepareForParse();
        if (is instanceof DOMInputSource) {
            schemaRoot = getSchemaDocument(schemaNamespace2, (DOMInputSource) is, referType == 3, referType, (Element) null);
        } else if (is instanceof SAXInputSource) {
            schemaRoot = getSchemaDocument(schemaNamespace2, (SAXInputSource) is, referType == 3, referType, (Element) null);
        } else if (is instanceof StAXInputSource) {
            schemaRoot = getSchemaDocument(schemaNamespace2, (StAXInputSource) is, referType == 3, referType, (Element) null);
        } else if (is instanceof XSInputSource) {
            schemaRoot = getSchemaDocument((XSInputSource) is, desc);
        } else {
            schemaRoot = getSchemaDocument(schemaNamespace2, is, referType == 3, referType, (Element) null);
        }
        if (schemaRoot == null) {
            if (is instanceof XSInputSource) {
                return this.fGrammarBucket.getGrammar(desc.getTargetNamespace());
            }
            return grammar;
        }
        if (referType == 3) {
            Element schemaElem = schemaRoot;
            String schemaNamespace3 = DOMUtil.getAttrValue(schemaElem, SchemaSymbols.ATT_TARGETNAMESPACE);
            if (schemaNamespace3 != null && schemaNamespace3.length() > 0) {
                schemaNamespace = this.fSymbolTable.addSymbol(schemaNamespace3);
                desc.setTargetNamespace(schemaNamespace);
            } else {
                schemaNamespace = null;
            }
            grammar = findGrammar(desc, this.fNamespaceGrowth);
            String schemaId = XMLEntityManager.expandSystemId(is.getSystemId(), is.getBaseSystemId(), false);
            if (grammar != null && (!this.fNamespaceGrowth || (schemaId != null && grammar.getDocumentLocations().contains(schemaId)))) {
                return grammar;
            }
            XSDKey key = new XSDKey(schemaId, referType, schemaNamespace);
            this.fTraversed.put(key, schemaRoot);
            if (schemaId != null) {
                this.fDoc2SystemId.put(schemaRoot, schemaId);
            }
        }
        prepareForTraverse();
        this.fRoot = constructTrees(schemaRoot, is.getSystemId(), desc, grammar != null);
        if (this.fRoot == null) {
            return null;
        }
        buildGlobalNameRegistries();
        ArrayList annotationInfo = this.fValidateAnnotations ? new ArrayList() : null;
        traverseSchemas(annotationInfo);
        traverseLocalElements();
        resolveKeyRefs();
        for (int i2 = this.fAllTNSs.size() - 1; i2 >= 0; i2--) {
            String tns = this.fAllTNSs.elementAt(i2);
            Vector ins = this.fImportMap.get(tns);
            SchemaGrammar sg = this.fGrammarBucket.getGrammar(emptyString2Null(tns));
            if (sg != null) {
                int count = 0;
                for (int j2 = 0; j2 < ins.size(); j2++) {
                    SchemaGrammar isg = this.fGrammarBucket.getGrammar((String) ins.elementAt(j2));
                    if (isg != null) {
                        int i3 = count;
                        count++;
                        ins.setElementAt(isg, i3);
                    }
                }
                ins.setSize(count);
                sg.setImportedGrammars(ins);
            }
        }
        if (this.fValidateAnnotations && annotationInfo.size() > 0) {
            validateAnnotations(annotationInfo);
        }
        return this.fGrammarBucket.getGrammar(this.fRoot.fTargetNamespace);
    }

    private void validateAnnotations(ArrayList annotationInfo) throws XNIException {
        if (this.fAnnotationValidator == null) {
            createAnnotationValidator();
        }
        int size = annotationInfo.size();
        XMLInputSource src = new XMLInputSource(null, null, null);
        this.fGrammarBucketAdapter.refreshGrammars(this.fGrammarBucket);
        for (int i2 = 0; i2 < size; i2 += 2) {
            src.setSystemId((String) annotationInfo.get(i2));
            XSAnnotationInfo xSAnnotationInfo = (XSAnnotationInfo) annotationInfo.get(i2 + 1);
            while (true) {
                XSAnnotationInfo annotation = xSAnnotationInfo;
                if (annotation != null) {
                    src.setCharacterStream(new StringReader(annotation.fAnnotation));
                    try {
                        this.fAnnotationValidator.parse(src);
                    } catch (IOException e2) {
                    }
                    xSAnnotationInfo = annotation.next;
                }
            }
        }
    }

    private void createAnnotationValidator() throws XNIException {
        this.fAnnotationValidator = new XML11Configuration();
        this.fGrammarBucketAdapter = new XSAnnotationGrammarPool();
        this.fAnnotationValidator.setFeature(VALIDATION, true);
        this.fAnnotationValidator.setFeature(XMLSCHEMA_VALIDATION, true);
        this.fAnnotationValidator.setProperty("http://apache.org/xml/properties/internal/grammar-pool", this.fGrammarBucketAdapter);
        this.fAnnotationValidator.setProperty("http://apache.org/xml/properties/security-manager", this.fSecurityManager != null ? this.fSecurityManager : new XMLSecurityManager(true));
        this.fAnnotationValidator.setProperty("http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager", this.fSecurityPropertyMgr);
        this.fAnnotationValidator.setProperty(ERROR_HANDLER, this.fErrorHandler != null ? this.fErrorHandler : new DefaultErrorHandler());
        this.fAnnotationValidator.setProperty("http://apache.org/xml/properties/locale", this.fLocale);
    }

    SchemaGrammar getGrammar(String tns) {
        return this.fGrammarBucket.getGrammar(tns);
    }

    protected SchemaGrammar findGrammar(XSDDescription desc, boolean ignoreConflict) throws MissingResourceException, XNIException {
        SchemaGrammar sg = this.fGrammarBucket.getGrammar(desc.getTargetNamespace());
        if (sg == null && this.fGrammarPool != null) {
            sg = (SchemaGrammar) this.fGrammarPool.retrieveGrammar(desc);
            if (sg != null && !this.fGrammarBucket.putGrammar(sg, true, ignoreConflict)) {
                reportSchemaWarning("GrammarConflict", null, null);
                sg = null;
            }
        }
        return sg;
    }

    protected XSDocumentInfo constructTrees(Element schemaRoot, String locationHint, XSDDescription desc, boolean nsCollision) throws MissingResourceException, XNIException {
        SchemaGrammar sg;
        String schemaHint;
        XSDocumentInfo newSchemaInfo;
        if (schemaRoot == null) {
            return null;
        }
        String callerTNS = desc.getTargetNamespace();
        short referType = desc.getContextType();
        try {
            XSDocumentInfo currSchemaInfo = new XSDocumentInfo(schemaRoot, this.fAttributeChecker, this.fSymbolTable);
            if (currSchemaInfo.fTargetNamespace != null && currSchemaInfo.fTargetNamespace.length() == 0) {
                reportSchemaWarning("EmptyTargetNamespace", new Object[]{locationHint}, schemaRoot);
                currSchemaInfo.fTargetNamespace = null;
            }
            if (callerTNS != null) {
                if (referType == 0 || referType == 1) {
                    if (currSchemaInfo.fTargetNamespace == null) {
                        currSchemaInfo.fTargetNamespace = callerTNS;
                        currSchemaInfo.fIsChameleonSchema = true;
                    } else if (callerTNS != currSchemaInfo.fTargetNamespace) {
                        reportSchemaError(NS_ERROR_CODES[referType][0], new Object[]{callerTNS, currSchemaInfo.fTargetNamespace}, schemaRoot);
                        return null;
                    }
                } else if (referType != 3 && callerTNS != currSchemaInfo.fTargetNamespace) {
                    reportSchemaError(NS_ERROR_CODES[referType][0], new Object[]{callerTNS, currSchemaInfo.fTargetNamespace}, schemaRoot);
                    return null;
                }
            } else if (currSchemaInfo.fTargetNamespace != null) {
                if (referType == 3) {
                    desc.setTargetNamespace(currSchemaInfo.fTargetNamespace);
                    callerTNS = currSchemaInfo.fTargetNamespace;
                } else {
                    reportSchemaError(NS_ERROR_CODES[referType][1], new Object[]{callerTNS, currSchemaInfo.fTargetNamespace}, schemaRoot);
                    return null;
                }
            }
            currSchemaInfo.addAllowedNS(currSchemaInfo.fTargetNamespace);
            if (nsCollision) {
                SchemaGrammar sg2 = this.fGrammarBucket.getGrammar(currSchemaInfo.fTargetNamespace);
                if (sg2.isImmutable()) {
                    sg = new SchemaGrammar(sg2);
                    this.fGrammarBucket.putGrammar(sg);
                    updateImportListWith(sg);
                } else {
                    sg = sg2;
                }
                updateImportListFor(sg);
            } else if (referType == 0 || referType == 1) {
                sg = this.fGrammarBucket.getGrammar(currSchemaInfo.fTargetNamespace);
            } else if (this.fHonourAllSchemaLocations && referType == 2) {
                sg = findGrammar(desc, false);
                if (sg == null) {
                    sg = new SchemaGrammar(currSchemaInfo.fTargetNamespace, desc.makeClone(), this.fSymbolTable);
                    this.fGrammarBucket.putGrammar(sg);
                }
            } else {
                sg = new SchemaGrammar(currSchemaInfo.fTargetNamespace, desc.makeClone(), this.fSymbolTable);
                this.fGrammarBucket.putGrammar(sg);
            }
            sg.addDocument(null, this.fDoc2SystemId.get(currSchemaInfo.fSchemaElement));
            this.fDoc2XSDocumentMap.put(schemaRoot, currSchemaInfo);
            Vector<XSDocumentInfo> dependencies = new Vector<>();
            Element newSchemaRoot = null;
            Element firstChildElement = DOMUtil.getFirstChildElement(schemaRoot);
            while (true) {
                Element child = firstChildElement;
                if (child == null) {
                    break;
                }
                String localName = DOMUtil.getLocalName(child);
                boolean importCollision = false;
                if (!localName.equals(SchemaSymbols.ELT_ANNOTATION)) {
                    if (localName.equals(SchemaSymbols.ELT_IMPORT)) {
                        Object[] importAttrs = this.fAttributeChecker.checkAttributes(child, true, currSchemaInfo);
                        schemaHint = (String) importAttrs[XSAttributeChecker.ATTIDX_SCHEMALOCATION];
                        String schemaNamespace = (String) importAttrs[XSAttributeChecker.ATTIDX_NAMESPACE];
                        if (schemaNamespace != null) {
                            schemaNamespace = this.fSymbolTable.addSymbol(schemaNamespace);
                        }
                        Element importChild = DOMUtil.getFirstChildElement(child);
                        if (importChild != null) {
                            String importComponentType = DOMUtil.getLocalName(importChild);
                            if (importComponentType.equals(SchemaSymbols.ELT_ANNOTATION)) {
                                sg.addAnnotation(this.fElementTraverser.traverseAnnotationDecl(importChild, importAttrs, true, currSchemaInfo));
                            } else {
                                reportSchemaError("s4s-elt-must-match.1", new Object[]{localName, "annotation?", importComponentType}, child);
                            }
                            if (DOMUtil.getNextSiblingElement(importChild) != null) {
                                reportSchemaError("s4s-elt-must-match.1", new Object[]{localName, "annotation?", DOMUtil.getLocalName(DOMUtil.getNextSiblingElement(importChild))}, child);
                            }
                        } else {
                            String text = DOMUtil.getSyntheticAnnotation(child);
                            if (text != null) {
                                sg.addAnnotation(this.fElementTraverser.traverseSyntheticAnnotation(child, text, importAttrs, true, currSchemaInfo));
                            }
                        }
                        this.fAttributeChecker.returnAttrArray(importAttrs, currSchemaInfo);
                        if (schemaNamespace == currSchemaInfo.fTargetNamespace) {
                            reportSchemaError(schemaNamespace != null ? "src-import.1.1" : "src-import.1.2", new Object[]{schemaNamespace}, child);
                        } else {
                            if (currSchemaInfo.isAllowedNS(schemaNamespace)) {
                                if (this.fHonourAllSchemaLocations || this.fNamespaceGrowth) {
                                }
                            } else {
                                currSchemaInfo.addAllowedNS(schemaNamespace);
                            }
                            String tns = null2EmptyString(currSchemaInfo.fTargetNamespace);
                            Vector ins = this.fImportMap.get(tns);
                            if (ins == null) {
                                this.fAllTNSs.addElement(tns);
                                Vector ins2 = new Vector();
                                this.fImportMap.put(tns, ins2);
                                ins2.addElement(schemaNamespace);
                            } else if (!ins.contains(schemaNamespace)) {
                                ins.addElement(schemaNamespace);
                            }
                            this.fSchemaGrammarDescription.reset();
                            this.fSchemaGrammarDescription.setContextType((short) 2);
                            this.fSchemaGrammarDescription.setBaseSystemId(doc2SystemId(schemaRoot));
                            this.fSchemaGrammarDescription.setLiteralSystemId(schemaHint);
                            this.fSchemaGrammarDescription.setLocationHints(new String[]{schemaHint});
                            this.fSchemaGrammarDescription.setTargetNamespace(schemaNamespace);
                            SchemaGrammar isg = findGrammar(this.fSchemaGrammarDescription, this.fNamespaceGrowth);
                            if (isg != null) {
                                if (this.fNamespaceGrowth) {
                                    try {
                                        if (!isg.getDocumentLocations().contains(XMLEntityManager.expandSystemId(schemaHint, this.fSchemaGrammarDescription.getBaseSystemId(), false))) {
                                            importCollision = true;
                                        }
                                    } catch (URI.MalformedURIException e2) {
                                    }
                                } else if (!this.fHonourAllSchemaLocations || isExistingGrammar(this.fSchemaGrammarDescription, false)) {
                                }
                            }
                            newSchemaRoot = resolveSchema(this.fSchemaGrammarDescription, false, child, isg == null);
                        }
                    } else {
                        if (!localName.equals(SchemaSymbols.ELT_INCLUDE) && !localName.equals(SchemaSymbols.ELT_REDEFINE)) {
                            break;
                        }
                        Object[] includeAttrs = this.fAttributeChecker.checkAttributes(child, true, currSchemaInfo);
                        schemaHint = (String) includeAttrs[XSAttributeChecker.ATTIDX_SCHEMALOCATION];
                        if (localName.equals(SchemaSymbols.ELT_REDEFINE)) {
                            if (this.fRedefine2NSSupport == null) {
                                this.fRedefine2NSSupport = new HashMap();
                            }
                            this.fRedefine2NSSupport.put(child, new SchemaNamespaceSupport(currSchemaInfo.fNamespaceSupport));
                        }
                        if (localName.equals(SchemaSymbols.ELT_INCLUDE)) {
                            Element includeChild = DOMUtil.getFirstChildElement(child);
                            if (includeChild != null) {
                                String includeComponentType = DOMUtil.getLocalName(includeChild);
                                if (includeComponentType.equals(SchemaSymbols.ELT_ANNOTATION)) {
                                    sg.addAnnotation(this.fElementTraverser.traverseAnnotationDecl(includeChild, includeAttrs, true, currSchemaInfo));
                                } else {
                                    reportSchemaError("s4s-elt-must-match.1", new Object[]{localName, "annotation?", includeComponentType}, child);
                                }
                                if (DOMUtil.getNextSiblingElement(includeChild) != null) {
                                    reportSchemaError("s4s-elt-must-match.1", new Object[]{localName, "annotation?", DOMUtil.getLocalName(DOMUtil.getNextSiblingElement(includeChild))}, child);
                                }
                            } else {
                                String text2 = DOMUtil.getSyntheticAnnotation(child);
                                if (text2 != null) {
                                    sg.addAnnotation(this.fElementTraverser.traverseSyntheticAnnotation(child, text2, includeAttrs, true, currSchemaInfo));
                                }
                            }
                        } else {
                            Element firstChildElement2 = DOMUtil.getFirstChildElement(child);
                            while (true) {
                                Element redefinedChild = firstChildElement2;
                                if (redefinedChild == null) {
                                    break;
                                }
                                String redefinedComponentType = DOMUtil.getLocalName(redefinedChild);
                                if (redefinedComponentType.equals(SchemaSymbols.ELT_ANNOTATION)) {
                                    sg.addAnnotation(this.fElementTraverser.traverseAnnotationDecl(redefinedChild, includeAttrs, true, currSchemaInfo));
                                    DOMUtil.setHidden(redefinedChild, this.fHiddenNodes);
                                } else {
                                    String text3 = DOMUtil.getSyntheticAnnotation(child);
                                    if (text3 != null) {
                                        sg.addAnnotation(this.fElementTraverser.traverseSyntheticAnnotation(child, text3, includeAttrs, true, currSchemaInfo));
                                    }
                                }
                                firstChildElement2 = DOMUtil.getNextSiblingElement(redefinedChild);
                            }
                        }
                        this.fAttributeChecker.returnAttrArray(includeAttrs, currSchemaInfo);
                        if (schemaHint == null) {
                            reportSchemaError("s4s-att-must-appear", new Object[]{"<include> or <redefine>", "schemaLocation"}, child);
                        }
                        boolean mustResolve = false;
                        short refType = 0;
                        if (localName.equals(SchemaSymbols.ELT_REDEFINE)) {
                            mustResolve = nonAnnotationContent(child);
                            refType = 1;
                        }
                        this.fSchemaGrammarDescription.reset();
                        this.fSchemaGrammarDescription.setContextType(refType);
                        this.fSchemaGrammarDescription.setBaseSystemId(doc2SystemId(schemaRoot));
                        this.fSchemaGrammarDescription.setLocationHints(new String[]{schemaHint});
                        this.fSchemaGrammarDescription.setTargetNamespace(callerTNS);
                        boolean alreadyTraversed = false;
                        XMLInputSource schemaSource = resolveSchemaSource(this.fSchemaGrammarDescription, mustResolve, child, true);
                        if (this.fNamespaceGrowth && refType == 0) {
                            try {
                                String schemaId = XMLEntityManager.expandSystemId(schemaSource.getSystemId(), schemaSource.getBaseSystemId(), false);
                                alreadyTraversed = sg.getDocumentLocations().contains(schemaId);
                            } catch (URI.MalformedURIException e3) {
                            }
                        }
                        if (!alreadyTraversed) {
                            newSchemaRoot = resolveSchema(schemaSource, this.fSchemaGrammarDescription, mustResolve, child);
                            String str = currSchemaInfo.fTargetNamespace;
                        } else {
                            this.fLastSchemaWasDuplicate = true;
                        }
                    }
                    if (this.fLastSchemaWasDuplicate) {
                        newSchemaInfo = newSchemaRoot == null ? null : (XSDocumentInfo) this.fDoc2XSDocumentMap.get(newSchemaRoot);
                    } else {
                        newSchemaInfo = constructTrees(newSchemaRoot, schemaHint, this.fSchemaGrammarDescription, importCollision);
                    }
                    if (localName.equals(SchemaSymbols.ELT_REDEFINE) && newSchemaInfo != null) {
                        if (this.fRedefine2XSDMap == null) {
                            this.fRedefine2XSDMap = new HashMap();
                        }
                        this.fRedefine2XSDMap.put(child, newSchemaInfo);
                    }
                    if (newSchemaRoot != null) {
                        if (newSchemaInfo != null) {
                            dependencies.addElement(newSchemaInfo);
                        }
                        newSchemaRoot = null;
                    }
                }
                firstChildElement = DOMUtil.getNextSiblingElement(child);
            }
            this.fDependencyMap.put(currSchemaInfo, dependencies);
            return currSchemaInfo;
        } catch (XMLSchemaException e4) {
            reportSchemaError(ELE_ERROR_CODES[referType], new Object[]{locationHint}, schemaRoot);
            return null;
        }
    }

    private boolean isExistingGrammar(XSDDescription desc, boolean ignoreConflict) {
        SchemaGrammar sg = this.fGrammarBucket.getGrammar(desc.getTargetNamespace());
        if (sg == null) {
            return findGrammar(desc, ignoreConflict) != null;
        }
        if (sg.isImmutable()) {
            return true;
        }
        try {
            return sg.getDocumentLocations().contains(XMLEntityManager.expandSystemId(desc.getLiteralSystemId(), desc.getBaseSystemId(), false));
        } catch (URI.MalformedURIException e2) {
            return false;
        }
    }

    private void updateImportListFor(SchemaGrammar grammar) {
        Vector importedGrammars = grammar.getImportedGrammars();
        if (importedGrammars != null) {
            for (int i2 = 0; i2 < importedGrammars.size(); i2++) {
                SchemaGrammar isg1 = (SchemaGrammar) importedGrammars.elementAt(i2);
                SchemaGrammar isg2 = this.fGrammarBucket.getGrammar(isg1.getTargetNamespace());
                if (isg2 != null && isg1 != isg2) {
                    importedGrammars.set(i2, isg2);
                }
            }
        }
    }

    private void updateImportListWith(SchemaGrammar newGrammar) {
        Vector importedGrammars;
        SchemaGrammar[] schemaGrammars = this.fGrammarBucket.getGrammars();
        for (SchemaGrammar sg : schemaGrammars) {
            if (sg != newGrammar && (importedGrammars = sg.getImportedGrammars()) != null) {
                int j2 = 0;
                while (true) {
                    if (j2 < importedGrammars.size()) {
                        SchemaGrammar isg = (SchemaGrammar) importedGrammars.elementAt(j2);
                        if (!null2EmptyString(isg.getTargetNamespace()).equals(null2EmptyString(newGrammar.getTargetNamespace()))) {
                            j2++;
                        } else if (isg != newGrammar) {
                            importedGrammars.set(j2, newGrammar);
                        }
                    }
                }
            }
        }
    }

    protected void buildGlobalNameRegistries() throws DOMException, MissingResourceException, XNIException {
        this.registryEmpty = false;
        Stack schemasToProcess = new Stack();
        schemasToProcess.push(this.fRoot);
        while (!schemasToProcess.empty()) {
            XSDocumentInfo currSchemaDoc = (XSDocumentInfo) schemasToProcess.pop();
            Element currDoc = currSchemaDoc.fSchemaElement;
            if (!DOMUtil.isHidden(currDoc, this.fHiddenNodes)) {
                boolean dependenciesCanOccur = true;
                Element firstChildElement = DOMUtil.getFirstChildElement(currDoc);
                while (true) {
                    Element globalComp = firstChildElement;
                    if (globalComp == null) {
                        break;
                    }
                    if (!DOMUtil.getLocalName(globalComp).equals(SchemaSymbols.ELT_ANNOTATION)) {
                        if (DOMUtil.getLocalName(globalComp).equals(SchemaSymbols.ELT_INCLUDE) || DOMUtil.getLocalName(globalComp).equals(SchemaSymbols.ELT_IMPORT)) {
                            if (!dependenciesCanOccur) {
                                reportSchemaError("s4s-elt-invalid-content.3", new Object[]{DOMUtil.getLocalName(globalComp)}, globalComp);
                            }
                            DOMUtil.setHidden(globalComp, this.fHiddenNodes);
                        } else if (DOMUtil.getLocalName(globalComp).equals(SchemaSymbols.ELT_REDEFINE)) {
                            if (!dependenciesCanOccur) {
                                reportSchemaError("s4s-elt-invalid-content.3", new Object[]{DOMUtil.getLocalName(globalComp)}, globalComp);
                            }
                            Element firstChildElement2 = DOMUtil.getFirstChildElement(globalComp);
                            while (true) {
                                Element redefineComp = firstChildElement2;
                                if (redefineComp != null) {
                                    String lName = DOMUtil.getAttrValue(redefineComp, SchemaSymbols.ATT_NAME);
                                    if (lName.length() != 0) {
                                        String qName = currSchemaDoc.fTargetNamespace == null ? "," + lName : currSchemaDoc.fTargetNamespace + "," + lName;
                                        String componentType = DOMUtil.getLocalName(redefineComp);
                                        if (componentType.equals(SchemaSymbols.ELT_ATTRIBUTEGROUP)) {
                                            checkForDuplicateNames(qName, 2, this.fUnparsedAttributeGroupRegistry, this.fUnparsedAttributeGroupRegistrySub, redefineComp, currSchemaDoc);
                                            renameRedefiningComponents(currSchemaDoc, redefineComp, SchemaSymbols.ELT_ATTRIBUTEGROUP, lName, DOMUtil.getAttrValue(redefineComp, SchemaSymbols.ATT_NAME) + REDEF_IDENTIFIER);
                                        } else if (componentType.equals(SchemaSymbols.ELT_COMPLEXTYPE) || componentType.equals(SchemaSymbols.ELT_SIMPLETYPE)) {
                                            checkForDuplicateNames(qName, 7, this.fUnparsedTypeRegistry, this.fUnparsedTypeRegistrySub, redefineComp, currSchemaDoc);
                                            String targetLName = DOMUtil.getAttrValue(redefineComp, SchemaSymbols.ATT_NAME) + REDEF_IDENTIFIER;
                                            if (componentType.equals(SchemaSymbols.ELT_COMPLEXTYPE)) {
                                                renameRedefiningComponents(currSchemaDoc, redefineComp, SchemaSymbols.ELT_COMPLEXTYPE, lName, targetLName);
                                            } else {
                                                renameRedefiningComponents(currSchemaDoc, redefineComp, SchemaSymbols.ELT_SIMPLETYPE, lName, targetLName);
                                            }
                                        } else if (componentType.equals(SchemaSymbols.ELT_GROUP)) {
                                            checkForDuplicateNames(qName, 4, this.fUnparsedGroupRegistry, this.fUnparsedGroupRegistrySub, redefineComp, currSchemaDoc);
                                            renameRedefiningComponents(currSchemaDoc, redefineComp, SchemaSymbols.ELT_GROUP, lName, DOMUtil.getAttrValue(redefineComp, SchemaSymbols.ATT_NAME) + REDEF_IDENTIFIER);
                                        }
                                    }
                                    firstChildElement2 = DOMUtil.getNextSiblingElement(redefineComp);
                                }
                            }
                        } else {
                            dependenciesCanOccur = false;
                            String lName2 = DOMUtil.getAttrValue(globalComp, SchemaSymbols.ATT_NAME);
                            if (lName2.length() != 0) {
                                String qName2 = currSchemaDoc.fTargetNamespace == null ? "," + lName2 : currSchemaDoc.fTargetNamespace + "," + lName2;
                                String componentType2 = DOMUtil.getLocalName(globalComp);
                                if (componentType2.equals(SchemaSymbols.ELT_ATTRIBUTE)) {
                                    checkForDuplicateNames(qName2, 1, this.fUnparsedAttributeRegistry, this.fUnparsedAttributeRegistrySub, globalComp, currSchemaDoc);
                                } else if (componentType2.equals(SchemaSymbols.ELT_ATTRIBUTEGROUP)) {
                                    checkForDuplicateNames(qName2, 2, this.fUnparsedAttributeGroupRegistry, this.fUnparsedAttributeGroupRegistrySub, globalComp, currSchemaDoc);
                                } else if (componentType2.equals(SchemaSymbols.ELT_COMPLEXTYPE) || componentType2.equals(SchemaSymbols.ELT_SIMPLETYPE)) {
                                    checkForDuplicateNames(qName2, 7, this.fUnparsedTypeRegistry, this.fUnparsedTypeRegistrySub, globalComp, currSchemaDoc);
                                } else if (componentType2.equals(SchemaSymbols.ELT_ELEMENT)) {
                                    checkForDuplicateNames(qName2, 3, this.fUnparsedElementRegistry, this.fUnparsedElementRegistrySub, globalComp, currSchemaDoc);
                                } else if (componentType2.equals(SchemaSymbols.ELT_GROUP)) {
                                    checkForDuplicateNames(qName2, 4, this.fUnparsedGroupRegistry, this.fUnparsedGroupRegistrySub, globalComp, currSchemaDoc);
                                } else if (componentType2.equals(SchemaSymbols.ELT_NOTATION)) {
                                    checkForDuplicateNames(qName2, 6, this.fUnparsedNotationRegistry, this.fUnparsedNotationRegistrySub, globalComp, currSchemaDoc);
                                }
                            }
                        }
                    }
                    firstChildElement = DOMUtil.getNextSiblingElement(globalComp);
                }
                DOMUtil.setHidden(currDoc, this.fHiddenNodes);
                Vector<XSDocumentInfo> currSchemaDepends = this.fDependencyMap.get(currSchemaDoc);
                for (int i2 = 0; i2 < currSchemaDepends.size(); i2++) {
                    schemasToProcess.push(currSchemaDepends.elementAt(i2));
                }
            }
        }
    }

    protected void traverseSchemas(ArrayList annotationInfo) throws MissingResourceException, XNIException {
        XSAnnotationInfo info;
        String text;
        setSchemasVisible(this.fRoot);
        Stack schemasToProcess = new Stack();
        schemasToProcess.push(this.fRoot);
        while (!schemasToProcess.empty()) {
            XSDocumentInfo currSchemaDoc = (XSDocumentInfo) schemasToProcess.pop();
            Element currDoc = currSchemaDoc.fSchemaElement;
            SchemaGrammar currSG = this.fGrammarBucket.getGrammar(currSchemaDoc.fTargetNamespace);
            if (!DOMUtil.isHidden(currDoc, this.fHiddenNodes)) {
                boolean sawAnnotation = false;
                Element firstVisibleChildElement = DOMUtil.getFirstVisibleChildElement(currDoc, this.fHiddenNodes);
                while (true) {
                    Element globalComp = firstVisibleChildElement;
                    if (globalComp == null) {
                        break;
                    }
                    DOMUtil.setHidden(globalComp, this.fHiddenNodes);
                    String componentType = DOMUtil.getLocalName(globalComp);
                    if (DOMUtil.getLocalName(globalComp).equals(SchemaSymbols.ELT_REDEFINE)) {
                        currSchemaDoc.backupNSSupport(this.fRedefine2NSSupport != null ? (SchemaNamespaceSupport) this.fRedefine2NSSupport.get(globalComp) : null);
                        Element firstVisibleChildElement2 = DOMUtil.getFirstVisibleChildElement(globalComp, this.fHiddenNodes);
                        while (true) {
                            Element redefinedComp = firstVisibleChildElement2;
                            if (redefinedComp == null) {
                                break;
                            }
                            String redefinedComponentType = DOMUtil.getLocalName(redefinedComp);
                            DOMUtil.setHidden(redefinedComp, this.fHiddenNodes);
                            if (redefinedComponentType.equals(SchemaSymbols.ELT_ATTRIBUTEGROUP)) {
                                this.fAttributeGroupTraverser.traverseGlobal(redefinedComp, currSchemaDoc, currSG);
                            } else if (redefinedComponentType.equals(SchemaSymbols.ELT_COMPLEXTYPE)) {
                                this.fComplexTypeTraverser.traverseGlobal(redefinedComp, currSchemaDoc, currSG);
                            } else if (redefinedComponentType.equals(SchemaSymbols.ELT_GROUP)) {
                                this.fGroupTraverser.traverseGlobal(redefinedComp, currSchemaDoc, currSG);
                            } else if (redefinedComponentType.equals(SchemaSymbols.ELT_SIMPLETYPE)) {
                                this.fSimpleTypeTraverser.traverseGlobal(redefinedComp, currSchemaDoc, currSG);
                            } else {
                                reportSchemaError("s4s-elt-must-match.1", new Object[]{DOMUtil.getLocalName(globalComp), "(annotation | (simpleType | complexType | group | attributeGroup))*", redefinedComponentType}, redefinedComp);
                            }
                            firstVisibleChildElement2 = DOMUtil.getNextVisibleSiblingElement(redefinedComp, this.fHiddenNodes);
                        }
                        currSchemaDoc.restoreNSSupport();
                    } else if (componentType.equals(SchemaSymbols.ELT_ATTRIBUTE)) {
                        this.fAttributeTraverser.traverseGlobal(globalComp, currSchemaDoc, currSG);
                    } else if (componentType.equals(SchemaSymbols.ELT_ATTRIBUTEGROUP)) {
                        this.fAttributeGroupTraverser.traverseGlobal(globalComp, currSchemaDoc, currSG);
                    } else if (componentType.equals(SchemaSymbols.ELT_COMPLEXTYPE)) {
                        this.fComplexTypeTraverser.traverseGlobal(globalComp, currSchemaDoc, currSG);
                    } else if (componentType.equals(SchemaSymbols.ELT_ELEMENT)) {
                        this.fElementTraverser.traverseGlobal(globalComp, currSchemaDoc, currSG);
                    } else if (componentType.equals(SchemaSymbols.ELT_GROUP)) {
                        this.fGroupTraverser.traverseGlobal(globalComp, currSchemaDoc, currSG);
                    } else if (componentType.equals(SchemaSymbols.ELT_NOTATION)) {
                        this.fNotationTraverser.traverse(globalComp, currSchemaDoc, currSG);
                    } else if (componentType.equals(SchemaSymbols.ELT_SIMPLETYPE)) {
                        this.fSimpleTypeTraverser.traverseGlobal(globalComp, currSchemaDoc, currSG);
                    } else if (componentType.equals(SchemaSymbols.ELT_ANNOTATION)) {
                        currSG.addAnnotation(this.fElementTraverser.traverseAnnotationDecl(globalComp, currSchemaDoc.getSchemaAttrs(), true, currSchemaDoc));
                        sawAnnotation = true;
                    } else {
                        reportSchemaError("s4s-elt-invalid-content.1", new Object[]{SchemaSymbols.ELT_SCHEMA, DOMUtil.getLocalName(globalComp)}, globalComp);
                    }
                    firstVisibleChildElement = DOMUtil.getNextVisibleSiblingElement(globalComp, this.fHiddenNodes);
                }
                if (!sawAnnotation && (text = DOMUtil.getSyntheticAnnotation(currDoc)) != null) {
                    currSG.addAnnotation(this.fElementTraverser.traverseSyntheticAnnotation(currDoc, text, currSchemaDoc.getSchemaAttrs(), true, currSchemaDoc));
                }
                if (annotationInfo != null && (info = currSchemaDoc.getAnnotations()) != null) {
                    annotationInfo.add(doc2SystemId(currDoc));
                    annotationInfo.add(info);
                }
                currSchemaDoc.returnSchemaAttrs();
                DOMUtil.setHidden(currDoc, this.fHiddenNodes);
                Vector<XSDocumentInfo> currSchemaDepends = this.fDependencyMap.get(currSchemaDoc);
                for (int i2 = 0; i2 < currSchemaDepends.size(); i2++) {
                    schemasToProcess.push(currSchemaDepends.elementAt(i2));
                }
            }
        }
    }

    private final boolean needReportTNSError(String uri) {
        if (this.fReportedTNS == null) {
            this.fReportedTNS = new Vector();
        } else if (this.fReportedTNS.contains(uri)) {
            return false;
        }
        this.fReportedTNS.addElement(uri);
        return true;
    }

    void addGlobalAttributeDecl(XSAttributeDecl decl) {
        String namespace = decl.getNamespace();
        String declKey = (namespace == null || namespace.length() == 0) ? "," + decl.getName() : namespace + "," + decl.getName();
        if (this.fGlobalAttrDecls.get(declKey) == null) {
            this.fGlobalAttrDecls.put(declKey, decl);
        }
    }

    void addGlobalAttributeGroupDecl(XSAttributeGroupDecl decl) {
        String namespace = decl.getNamespace();
        String declKey = (namespace == null || namespace.length() == 0) ? "," + decl.getName() : namespace + "," + decl.getName();
        if (this.fGlobalAttrGrpDecls.get(declKey) == null) {
            this.fGlobalAttrGrpDecls.put(declKey, decl);
        }
    }

    void addGlobalElementDecl(XSElementDecl decl) {
        String namespace = decl.getNamespace();
        String declKey = (namespace == null || namespace.length() == 0) ? "," + decl.getName() : namespace + "," + decl.getName();
        if (this.fGlobalElemDecls.get(declKey) == null) {
            this.fGlobalElemDecls.put(declKey, decl);
        }
    }

    void addGlobalGroupDecl(XSGroupDecl decl) {
        String namespace = decl.getNamespace();
        String declKey = (namespace == null || namespace.length() == 0) ? "," + decl.getName() : namespace + "," + decl.getName();
        if (this.fGlobalGroupDecls.get(declKey) == null) {
            this.fGlobalGroupDecls.put(declKey, decl);
        }
    }

    void addGlobalNotationDecl(XSNotationDecl decl) {
        String namespace = decl.getNamespace();
        String declKey = (namespace == null || namespace.length() == 0) ? "," + decl.getName() : namespace + "," + decl.getName();
        if (this.fGlobalNotationDecls.get(declKey) == null) {
            this.fGlobalNotationDecls.put(declKey, decl);
        }
    }

    void addGlobalTypeDecl(XSTypeDefinition decl) {
        String namespace = decl.getNamespace();
        String declKey = (namespace == null || namespace.length() == 0) ? "," + decl.getName() : namespace + "," + decl.getName();
        if (this.fGlobalTypeDecls.get(declKey) == null) {
            this.fGlobalTypeDecls.put(declKey, decl);
        }
    }

    void addIDConstraintDecl(IdentityConstraint decl) {
        String namespace = decl.getNamespace();
        String declKey = (namespace == null || namespace.length() == 0) ? "," + decl.getIdentityConstraintName() : namespace + "," + decl.getIdentityConstraintName();
        if (this.fGlobalIDConstraintDecls.get(declKey) == null) {
            this.fGlobalIDConstraintDecls.put(declKey, decl);
        }
    }

    private XSAttributeDecl getGlobalAttributeDecl(String declKey) {
        return (XSAttributeDecl) this.fGlobalAttrDecls.get(declKey);
    }

    private XSAttributeGroupDecl getGlobalAttributeGroupDecl(String declKey) {
        return (XSAttributeGroupDecl) this.fGlobalAttrGrpDecls.get(declKey);
    }

    private XSElementDecl getGlobalElementDecl(String declKey) {
        return (XSElementDecl) this.fGlobalElemDecls.get(declKey);
    }

    private XSGroupDecl getGlobalGroupDecl(String declKey) {
        return (XSGroupDecl) this.fGlobalGroupDecls.get(declKey);
    }

    private XSNotationDecl getGlobalNotationDecl(String declKey) {
        return (XSNotationDecl) this.fGlobalNotationDecls.get(declKey);
    }

    private XSTypeDefinition getGlobalTypeDecl(String declKey) {
        return (XSTypeDefinition) this.fGlobalTypeDecls.get(declKey);
    }

    private IdentityConstraint getIDConstraintDecl(String declKey) {
        return (IdentityConstraint) this.fGlobalIDConstraintDecls.get(declKey);
    }

    protected Object getGlobalDecl(XSDocumentInfo currSchema, int declType, QName declToTraverse, Element elmNode) throws MissingResourceException, XNIException {
        Object retObj;
        if (declToTraverse.uri != null && declToTraverse.uri == SchemaSymbols.URI_SCHEMAFORSCHEMA && declType == 7 && (retObj = SchemaGrammar.SG_SchemaNS.getGlobalTypeDecl(declToTraverse.localpart)) != null) {
            return retObj;
        }
        if (!currSchema.isAllowedNS(declToTraverse.uri) && currSchema.needReportTNSError(declToTraverse.uri)) {
            reportSchemaError(declToTraverse.uri == null ? "src-resolve.4.1" : "src-resolve.4.2", new Object[]{this.fDoc2SystemId.get(currSchema.fSchemaElement), declToTraverse.uri, declToTraverse.rawname}, elmNode);
        }
        SchemaGrammar sGrammar = this.fGrammarBucket.getGrammar(declToTraverse.uri);
        if (sGrammar == null) {
            if (needReportTNSError(declToTraverse.uri)) {
                reportSchemaError("src-resolve", new Object[]{declToTraverse.rawname, COMP_TYPE[declType]}, elmNode);
                return null;
            }
            return null;
        }
        Object retObj2 = getGlobalDeclFromGrammar(sGrammar, declType, declToTraverse.localpart);
        String declKey = declToTraverse.uri == null ? "," + declToTraverse.localpart : declToTraverse.uri + "," + declToTraverse.localpart;
        if (!this.fTolerateDuplicates) {
            if (retObj2 != null) {
                return retObj2;
            }
        } else {
            Object retObj22 = getGlobalDecl(declKey, declType);
            if (retObj22 != null) {
                return retObj22;
            }
        }
        Element decl = null;
        XSDocumentInfo declDoc = null;
        switch (declType) {
            case 1:
                decl = getElementFromMap(this.fUnparsedAttributeRegistry, declKey);
                declDoc = getDocInfoFromMap(this.fUnparsedAttributeRegistrySub, declKey);
                break;
            case 2:
                decl = getElementFromMap(this.fUnparsedAttributeGroupRegistry, declKey);
                declDoc = getDocInfoFromMap(this.fUnparsedAttributeGroupRegistrySub, declKey);
                break;
            case 3:
                decl = getElementFromMap(this.fUnparsedElementRegistry, declKey);
                declDoc = getDocInfoFromMap(this.fUnparsedElementRegistrySub, declKey);
                break;
            case 4:
                decl = getElementFromMap(this.fUnparsedGroupRegistry, declKey);
                declDoc = getDocInfoFromMap(this.fUnparsedGroupRegistrySub, declKey);
                break;
            case 5:
                decl = getElementFromMap(this.fUnparsedIdentityConstraintRegistry, declKey);
                declDoc = getDocInfoFromMap(this.fUnparsedIdentityConstraintRegistrySub, declKey);
                break;
            case 6:
                decl = getElementFromMap(this.fUnparsedNotationRegistry, declKey);
                declDoc = getDocInfoFromMap(this.fUnparsedNotationRegistrySub, declKey);
                break;
            case 7:
                decl = getElementFromMap(this.fUnparsedTypeRegistry, declKey);
                declDoc = getDocInfoFromMap(this.fUnparsedTypeRegistrySub, declKey);
                break;
            default:
                reportSchemaError("Internal-Error", new Object[]{"XSDHandler asked to locate component of type " + declType + "; it does not recognize this type!"}, elmNode);
                break;
        }
        if (decl == null) {
            if (retObj2 == null) {
                reportSchemaError("src-resolve", new Object[]{declToTraverse.rawname, COMP_TYPE[declType]}, elmNode);
            }
            return retObj2;
        }
        XSDocumentInfo schemaWithDecl = findXSDocumentForDecl(currSchema, decl, declDoc);
        if (schemaWithDecl == null) {
            if (retObj2 == null) {
                reportSchemaError(declToTraverse.uri == null ? "src-resolve.4.1" : "src-resolve.4.2", new Object[]{this.fDoc2SystemId.get(currSchema.fSchemaElement), declToTraverse.uri, declToTraverse.rawname}, elmNode);
            }
            return retObj2;
        }
        if (DOMUtil.isHidden(decl, this.fHiddenNodes)) {
            if (retObj2 == null) {
                String code = CIRCULAR_CODES[declType];
                if (declType == 7 && SchemaSymbols.ELT_COMPLEXTYPE.equals(DOMUtil.getLocalName(decl))) {
                    code = "ct-props-correct.3";
                }
                reportSchemaError(code, new Object[]{declToTraverse.prefix + CallSiteDescriptor.TOKEN_DELIMITER + declToTraverse.localpart}, elmNode);
            }
            return retObj2;
        }
        return traverseGlobalDecl(declType, decl, schemaWithDecl, sGrammar);
    }

    protected Object getGlobalDecl(String declKey, int declType) {
        Object retObj = null;
        switch (declType) {
            case 1:
                retObj = getGlobalAttributeDecl(declKey);
                break;
            case 2:
                retObj = getGlobalAttributeGroupDecl(declKey);
                break;
            case 3:
                retObj = getGlobalElementDecl(declKey);
                break;
            case 4:
                retObj = getGlobalGroupDecl(declKey);
                break;
            case 5:
                retObj = getIDConstraintDecl(declKey);
                break;
            case 6:
                retObj = getGlobalNotationDecl(declKey);
                break;
            case 7:
                retObj = getGlobalTypeDecl(declKey);
                break;
        }
        return retObj;
    }

    protected Object getGlobalDeclFromGrammar(SchemaGrammar sGrammar, int declType, String localpart) {
        Object retObj = null;
        switch (declType) {
            case 1:
                retObj = sGrammar.getGlobalAttributeDecl(localpart);
                break;
            case 2:
                retObj = sGrammar.getGlobalAttributeGroupDecl(localpart);
                break;
            case 3:
                retObj = sGrammar.getGlobalElementDecl(localpart);
                break;
            case 4:
                retObj = sGrammar.getGlobalGroupDecl(localpart);
                break;
            case 5:
                retObj = sGrammar.getIDConstraintDecl(localpart);
                break;
            case 6:
                retObj = sGrammar.getGlobalNotationDecl(localpart);
                break;
            case 7:
                retObj = sGrammar.getGlobalTypeDecl(localpart);
                break;
        }
        return retObj;
    }

    protected Object getGlobalDeclFromGrammar(SchemaGrammar sGrammar, int declType, String localpart, String schemaLoc) {
        Object retObj = null;
        switch (declType) {
            case 1:
                retObj = sGrammar.getGlobalAttributeDecl(localpart, schemaLoc);
                break;
            case 2:
                retObj = sGrammar.getGlobalAttributeGroupDecl(localpart, schemaLoc);
                break;
            case 3:
                retObj = sGrammar.getGlobalElementDecl(localpart, schemaLoc);
                break;
            case 4:
                retObj = sGrammar.getGlobalGroupDecl(localpart, schemaLoc);
                break;
            case 5:
                retObj = sGrammar.getIDConstraintDecl(localpart, schemaLoc);
                break;
            case 6:
                retObj = sGrammar.getGlobalNotationDecl(localpart, schemaLoc);
                break;
            case 7:
                retObj = sGrammar.getGlobalTypeDecl(localpart, schemaLoc);
                break;
        }
        return retObj;
    }

    protected Object traverseGlobalDecl(int declType, Element decl, XSDocumentInfo schemaDoc, SchemaGrammar grammar) {
        Object retObj = null;
        DOMUtil.setHidden(decl, this.fHiddenNodes);
        SchemaNamespaceSupport nsSupport = null;
        Element parent = DOMUtil.getParent(decl);
        if (DOMUtil.getLocalName(parent).equals(SchemaSymbols.ELT_REDEFINE)) {
            nsSupport = this.fRedefine2NSSupport != null ? (SchemaNamespaceSupport) this.fRedefine2NSSupport.get(parent) : null;
        }
        schemaDoc.backupNSSupport(nsSupport);
        switch (declType) {
            case 1:
                retObj = this.fAttributeTraverser.traverseGlobal(decl, schemaDoc, grammar);
                break;
            case 2:
                retObj = this.fAttributeGroupTraverser.traverseGlobal(decl, schemaDoc, grammar);
                break;
            case 3:
                retObj = this.fElementTraverser.traverseGlobal(decl, schemaDoc, grammar);
                break;
            case 4:
                retObj = this.fGroupTraverser.traverseGlobal(decl, schemaDoc, grammar);
                break;
            case 6:
                retObj = this.fNotationTraverser.traverse(decl, schemaDoc, grammar);
                break;
            case 7:
                if (DOMUtil.getLocalName(decl).equals(SchemaSymbols.ELT_COMPLEXTYPE)) {
                    retObj = this.fComplexTypeTraverser.traverseGlobal(decl, schemaDoc, grammar);
                    break;
                } else {
                    retObj = this.fSimpleTypeTraverser.traverseGlobal(decl, schemaDoc, grammar);
                    break;
                }
        }
        schemaDoc.restoreNSSupport();
        return retObj;
    }

    public String schemaDocument2SystemId(XSDocumentInfo schemaDoc) {
        return this.fDoc2SystemId.get(schemaDoc.fSchemaElement);
    }

    Object getGrpOrAttrGrpRedefinedByRestriction(int type, QName name, XSDocumentInfo currSchema, Element elmNode) throws MissingResourceException, XNIException {
        String nameToFind;
        String realName = name.uri != null ? name.uri + "," + name.localpart : "," + name.localpart;
        switch (type) {
            case 2:
                nameToFind = (String) this.fRedefinedRestrictedAttributeGroupRegistry.get(realName);
                break;
            case 4:
                nameToFind = (String) this.fRedefinedRestrictedGroupRegistry.get(realName);
                break;
            default:
                return null;
        }
        if (nameToFind == null) {
            return null;
        }
        int commaPos = nameToFind.indexOf(",");
        QName qNameToFind = new QName(XMLSymbols.EMPTY_STRING, nameToFind.substring(commaPos + 1), nameToFind.substring(commaPos), commaPos == 0 ? null : nameToFind.substring(0, commaPos));
        Object retObj = getGlobalDecl(currSchema, type, qNameToFind, elmNode);
        if (retObj == null) {
            switch (type) {
                case 2:
                    reportSchemaError("src-redefine.7.2.1", new Object[]{name.localpart}, elmNode);
                    break;
                case 4:
                    reportSchemaError("src-redefine.6.2.1", new Object[]{name.localpart}, elmNode);
                    break;
            }
            return null;
        }
        return retObj;
    }

    protected void resolveKeyRefs() {
        for (int i2 = 0; i2 < this.fKeyrefStackPos; i2++) {
            XSDocumentInfo keyrefSchemaDoc = this.fKeyrefsMapXSDocumentInfo[i2];
            keyrefSchemaDoc.fNamespaceSupport.makeGlobal();
            keyrefSchemaDoc.fNamespaceSupport.setEffectiveContext(this.fKeyrefNamespaceContext[i2]);
            SchemaGrammar keyrefGrammar = this.fGrammarBucket.getGrammar(keyrefSchemaDoc.fTargetNamespace);
            DOMUtil.setHidden(this.fKeyrefs[i2], this.fHiddenNodes);
            this.fKeyrefTraverser.traverse(this.fKeyrefs[i2], this.fKeyrefElems[i2], keyrefSchemaDoc, keyrefGrammar);
        }
    }

    protected Map getIDRegistry() {
        return this.fUnparsedIdentityConstraintRegistry;
    }

    protected Map getIDRegistry_sub() {
        return this.fUnparsedIdentityConstraintRegistrySub;
    }

    /* JADX WARN: Type inference failed for: r0v31, types: [java.lang.Object, java.lang.String[], java.lang.String[][]] */
    protected void storeKeyRef(Element keyrefToStore, XSDocumentInfo schemaDoc, XSElementDecl currElemDecl) throws DOMException, MissingResourceException, XNIException {
        String keyrefName = DOMUtil.getAttrValue(keyrefToStore, SchemaSymbols.ATT_NAME);
        if (keyrefName.length() != 0) {
            String keyrefQName = schemaDoc.fTargetNamespace == null ? "," + keyrefName : schemaDoc.fTargetNamespace + "," + keyrefName;
            checkForDuplicateNames(keyrefQName, 5, this.fUnparsedIdentityConstraintRegistry, this.fUnparsedIdentityConstraintRegistrySub, keyrefToStore, schemaDoc);
        }
        if (this.fKeyrefStackPos == this.fKeyrefs.length) {
            Element[] elemArray = new Element[this.fKeyrefStackPos + 2];
            System.arraycopy(this.fKeyrefs, 0, elemArray, 0, this.fKeyrefStackPos);
            this.fKeyrefs = elemArray;
            XSElementDecl[] declArray = new XSElementDecl[this.fKeyrefStackPos + 2];
            System.arraycopy(this.fKeyrefElems, 0, declArray, 0, this.fKeyrefStackPos);
            this.fKeyrefElems = declArray;
            ?? r0 = new String[this.fKeyrefStackPos + 2];
            System.arraycopy(this.fKeyrefNamespaceContext, 0, r0, 0, this.fKeyrefStackPos);
            this.fKeyrefNamespaceContext = r0;
            XSDocumentInfo[] xsDocumentInfo = new XSDocumentInfo[this.fKeyrefStackPos + 2];
            System.arraycopy(this.fKeyrefsMapXSDocumentInfo, 0, xsDocumentInfo, 0, this.fKeyrefStackPos);
            this.fKeyrefsMapXSDocumentInfo = xsDocumentInfo;
        }
        this.fKeyrefs[this.fKeyrefStackPos] = keyrefToStore;
        this.fKeyrefElems[this.fKeyrefStackPos] = currElemDecl;
        this.fKeyrefNamespaceContext[this.fKeyrefStackPos] = schemaDoc.fNamespaceSupport.getEffectiveLocalContext();
        XSDocumentInfo[] xSDocumentInfoArr = this.fKeyrefsMapXSDocumentInfo;
        int i2 = this.fKeyrefStackPos;
        this.fKeyrefStackPos = i2 + 1;
        xSDocumentInfoArr[i2] = schemaDoc;
    }

    private Element resolveSchema(XSDDescription desc, boolean mustResolve, Element referElement, boolean usePairs) throws MissingResourceException, XNIException {
        XMLInputSource schemaSource = null;
        try {
            Map<String, XMLSchemaLoader.LocationArray> pairs = usePairs ? this.fLocationPairs : Collections.emptyMap();
            schemaSource = XMLSchemaLoader.resolveDocument(desc, pairs, this.fEntityManager);
        } catch (IOException e2) {
            if (mustResolve) {
                reportSchemaError("schema_reference.4", new Object[]{desc.getLocationHints()[0]}, referElement);
            } else {
                reportSchemaWarning("schema_reference.4", new Object[]{desc.getLocationHints()[0]}, referElement);
            }
        }
        if (schemaSource instanceof DOMInputSource) {
            return getSchemaDocument(desc.getTargetNamespace(), (DOMInputSource) schemaSource, mustResolve, desc.getContextType(), referElement);
        }
        if (schemaSource instanceof SAXInputSource) {
            return getSchemaDocument(desc.getTargetNamespace(), (SAXInputSource) schemaSource, mustResolve, desc.getContextType(), referElement);
        }
        if (schemaSource instanceof StAXInputSource) {
            return getSchemaDocument(desc.getTargetNamespace(), (StAXInputSource) schemaSource, mustResolve, desc.getContextType(), referElement);
        }
        if (schemaSource instanceof XSInputSource) {
            return getSchemaDocument((XSInputSource) schemaSource, desc);
        }
        return getSchemaDocument(desc.getTargetNamespace(), schemaSource, mustResolve, desc.getContextType(), referElement);
    }

    private Element resolveSchema(XMLInputSource schemaSource, XSDDescription desc, boolean mustResolve, Element referElement) {
        if (schemaSource instanceof DOMInputSource) {
            return getSchemaDocument(desc.getTargetNamespace(), (DOMInputSource) schemaSource, mustResolve, desc.getContextType(), referElement);
        }
        if (schemaSource instanceof SAXInputSource) {
            return getSchemaDocument(desc.getTargetNamespace(), (SAXInputSource) schemaSource, mustResolve, desc.getContextType(), referElement);
        }
        if (schemaSource instanceof StAXInputSource) {
            return getSchemaDocument(desc.getTargetNamespace(), (StAXInputSource) schemaSource, mustResolve, desc.getContextType(), referElement);
        }
        if (schemaSource instanceof XSInputSource) {
            return getSchemaDocument((XSInputSource) schemaSource, desc);
        }
        return getSchemaDocument(desc.getTargetNamespace(), schemaSource, mustResolve, desc.getContextType(), referElement);
    }

    private XMLInputSource resolveSchemaSource(XSDDescription desc, boolean mustResolve, Element referElement, boolean usePairs) throws MissingResourceException, XNIException {
        XMLInputSource schemaSource = null;
        try {
            Map<String, XMLSchemaLoader.LocationArray> pairs = usePairs ? this.fLocationPairs : Collections.emptyMap();
            schemaSource = XMLSchemaLoader.resolveDocument(desc, pairs, this.fEntityManager);
        } catch (IOException e2) {
            if (mustResolve) {
                reportSchemaError("schema_reference.4", new Object[]{desc.getLocationHints()[0]}, referElement);
            } else {
                reportSchemaWarning("schema_reference.4", new Object[]{desc.getLocationHints()[0]}, referElement);
            }
        }
        return schemaSource;
    }

    private Element getSchemaDocument(String schemaNamespace, XMLInputSource schemaSource, boolean mustResolve, short referType, Element referElement) throws MissingResourceException, XNIException {
        String accessError;
        boolean hasInput = true;
        IOException exception = null;
        if (schemaSource != null) {
            try {
                if (schemaSource.getSystemId() != null || schemaSource.getByteStream() != null || schemaSource.getCharacterStream() != null) {
                    XSDKey key = null;
                    String schemaId = null;
                    if (referType != 3) {
                        schemaId = XMLEntityManager.expandSystemId(schemaSource.getSystemId(), schemaSource.getBaseSystemId(), false);
                        key = new XSDKey(schemaId, referType, schemaNamespace);
                        Element schemaElement = this.fTraversed.get(key);
                        if (schemaElement != null) {
                            this.fLastSchemaWasDuplicate = true;
                            return schemaElement;
                        }
                        if ((referType == 2 || referType == 0 || referType == 1) && (accessError = SecuritySupport.checkAccess(schemaId, this.fAccessExternalSchema, "all")) != null) {
                            reportSchemaFatalError("schema_reference.access", new Object[]{SecuritySupport.sanitizePath(schemaId), accessError}, referElement);
                        }
                    }
                    this.fSchemaParser.parse(schemaSource);
                    Document schemaDocument = this.fSchemaParser.getDocument();
                    return getSchemaDocument0(key, schemaId, schemaDocument != null ? DOMUtil.getRoot(schemaDocument) : null);
                }
                hasInput = false;
            } catch (IOException ex) {
                exception = ex;
            }
        } else {
            hasInput = false;
        }
        return getSchemaDocument1(mustResolve, hasInput, schemaSource, referElement, exception);
    }

    private Element getSchemaDocument(String schemaNamespace, SAXInputSource schemaSource, boolean mustResolve, short referType, Element referElement) {
        XMLReader parser = schemaSource.getXMLReader();
        InputSource inputSource = schemaSource.getInputSource();
        boolean hasInput = true;
        IOException exception = null;
        if (inputSource != null) {
            try {
                if (inputSource.getSystemId() != null || inputSource.getByteStream() != null || inputSource.getCharacterStream() != null) {
                    XSDKey key = null;
                    String schemaId = null;
                    if (referType != 3) {
                        schemaId = XMLEntityManager.expandSystemId(inputSource.getSystemId(), schemaSource.getBaseSystemId(), false);
                        key = new XSDKey(schemaId, referType, schemaNamespace);
                        Element schemaElement = this.fTraversed.get(key);
                        if (schemaElement != null) {
                            this.fLastSchemaWasDuplicate = true;
                            return schemaElement;
                        }
                    }
                    boolean namespacePrefixes = false;
                    if (parser != null) {
                        try {
                            namespacePrefixes = parser.getFeature("http://xml.org/sax/features/namespace-prefixes");
                        } catch (SAXException e2) {
                        }
                    } else {
                        parser = JdkXmlUtils.getXMLReader(this.fOverrideDefaultParser, this.fSecurityManager.isSecureProcessing());
                        try {
                            parser.setFeature("http://xml.org/sax/features/namespace-prefixes", true);
                            namespacePrefixes = true;
                            if ((parser instanceof SAXParser) && this.fSecurityManager != null) {
                                parser.setProperty("http://apache.org/xml/properties/security-manager", this.fSecurityManager);
                            }
                        } catch (SAXException e3) {
                        }
                        try {
                            parser.setProperty("http://javax.xml.XMLConstants/property/accessExternalDTD", this.fAccessExternalDTD);
                        } catch (SAXNotRecognizedException exc) {
                            XMLSecurityManager.printWarning(parser.getClass().getName(), "http://javax.xml.XMLConstants/property/accessExternalDTD", exc);
                        }
                    }
                    boolean stringsInternalized = false;
                    try {
                        stringsInternalized = parser.getFeature("http://xml.org/sax/features/string-interning");
                    } catch (SAXException e4) {
                    }
                    if (this.fXSContentHandler == null) {
                        this.fXSContentHandler = new SchemaContentHandler();
                    }
                    this.fXSContentHandler.reset(this.fSchemaParser, this.fSymbolTable, namespacePrefixes, stringsInternalized);
                    parser.setContentHandler(this.fXSContentHandler);
                    parser.setErrorHandler(this.fErrorReporter.getSAXErrorHandler());
                    parser.parse(inputSource);
                    try {
                        parser.setContentHandler(null);
                        parser.setErrorHandler(null);
                    } catch (Exception e5) {
                    }
                    Document schemaDocument = this.fXSContentHandler.getDocument();
                    return getSchemaDocument0(key, schemaId, schemaDocument != null ? DOMUtil.getRoot(schemaDocument) : null);
                }
                hasInput = false;
            } catch (IOException ioe) {
                exception = ioe;
            } catch (SAXParseException spe) {
                throw SAX2XNIUtil.createXMLParseException0(spe);
            } catch (SAXException se) {
                throw SAX2XNIUtil.createXNIException0(se);
            }
        } else {
            hasInput = false;
        }
        return getSchemaDocument1(mustResolve, hasInput, schemaSource, referElement, exception);
    }

    private Element getSchemaDocument(String schemaNamespace, DOMInputSource schemaSource, boolean mustResolve, short referType, Element referElement) {
        Node parent;
        boolean hasInput = true;
        IOException exception = null;
        Element schemaRootElement = null;
        Node node = schemaSource.getNode();
        short nodeType = -1;
        if (node != null) {
            nodeType = node.getNodeType();
            if (nodeType == 9) {
                schemaRootElement = DOMUtil.getRoot((Document) node);
            } else if (nodeType == 1) {
                schemaRootElement = (Element) node;
            }
        }
        if (schemaRootElement != null) {
            XSDKey key = null;
            String schemaId = null;
            if (referType != 3) {
                try {
                    schemaId = XMLEntityManager.expandSystemId(schemaSource.getSystemId(), schemaSource.getBaseSystemId(), false);
                    boolean isDocument = nodeType == 9;
                    if (!isDocument && (parent = schemaRootElement.getParentNode()) != null) {
                        isDocument = parent.getNodeType() == 9;
                    }
                    if (isDocument) {
                        key = new XSDKey(schemaId, referType, schemaNamespace);
                        Element schemaElement = this.fTraversed.get(key);
                        if (schemaElement != null) {
                            this.fLastSchemaWasDuplicate = true;
                            return schemaElement;
                        }
                    }
                } catch (IOException ioe) {
                    exception = ioe;
                }
            }
            return getSchemaDocument0(key, schemaId, schemaRootElement);
        }
        hasInput = false;
        return getSchemaDocument1(mustResolve, hasInput, schemaSource, referElement, exception);
    }

    private Element getSchemaDocument(String schemaNamespace, StAXInputSource schemaSource, boolean mustResolve, short referType, Element referElement) throws XNIException {
        try {
            boolean consumeRemainingContent = schemaSource.shouldConsumeRemainingContent();
            XMLStreamReader streamReader = schemaSource.getXMLStreamReader();
            XMLEventReader eventReader = schemaSource.getXMLEventReader();
            XSDKey key = null;
            String schemaId = null;
            if (referType != 3) {
                schemaId = XMLEntityManager.expandSystemId(schemaSource.getSystemId(), schemaSource.getBaseSystemId(), false);
                boolean isDocument = consumeRemainingContent;
                if (!isDocument) {
                    if (streamReader != null) {
                        isDocument = streamReader.getEventType() == 7;
                    } else {
                        isDocument = eventReader.peek().isStartDocument();
                    }
                }
                if (isDocument) {
                    key = new XSDKey(schemaId, referType, schemaNamespace);
                    Element schemaElement = this.fTraversed.get(key);
                    if (schemaElement != null) {
                        this.fLastSchemaWasDuplicate = true;
                        return schemaElement;
                    }
                }
            }
            if (this.fStAXSchemaParser == null) {
                this.fStAXSchemaParser = new StAXSchemaParser();
            }
            this.fStAXSchemaParser.reset(this.fSchemaParser, this.fSymbolTable);
            if (streamReader != null) {
                this.fStAXSchemaParser.parse(streamReader);
                if (consumeRemainingContent) {
                    while (streamReader.hasNext()) {
                        streamReader.next();
                    }
                }
            } else {
                this.fStAXSchemaParser.parse(eventReader);
                if (consumeRemainingContent) {
                    while (eventReader.hasNext()) {
                        eventReader.nextEvent();
                    }
                }
            }
            Document schemaDocument = this.fStAXSchemaParser.getDocument();
            return getSchemaDocument0(key, schemaId, schemaDocument != null ? DOMUtil.getRoot(schemaDocument) : null);
        } catch (IOException e2) {
            return getSchemaDocument1(mustResolve, true, schemaSource, referElement, e2);
        } catch (XMLStreamException e3) {
            StAXLocationWrapper slw = new StAXLocationWrapper();
            slw.setLocation(e3.getLocation());
            throw new XMLParseException(slw, e3.getMessage(), e3);
        }
    }

    private Element getSchemaDocument0(XSDKey key, String schemaId, Element schemaElement) {
        if (key != null) {
            this.fTraversed.put(key, schemaElement);
        }
        if (schemaId != null) {
            this.fDoc2SystemId.put(schemaElement, schemaId);
        }
        this.fLastSchemaWasDuplicate = false;
        return schemaElement;
    }

    private Element getSchemaDocument1(boolean mustResolve, boolean hasInput, XMLInputSource schemaSource, Element referElement, IOException ioe) throws MissingResourceException, XNIException {
        if (mustResolve) {
            if (hasInput) {
                reportSchemaError("schema_reference.4", new Object[]{schemaSource.getSystemId()}, referElement, ioe);
            } else {
                Object[] objArr = new Object[1];
                objArr[0] = schemaSource == null ? "" : schemaSource.getSystemId();
                reportSchemaError("schema_reference.4", objArr, referElement, ioe);
            }
        } else if (hasInput) {
            reportSchemaWarning("schema_reference.4", new Object[]{schemaSource.getSystemId()}, referElement, ioe);
        }
        this.fLastSchemaWasDuplicate = false;
        return null;
    }

    private Element getSchemaDocument(XSInputSource schemaSource, XSDDescription desc) throws MissingResourceException, XNIException {
        SchemaGrammar[] grammars = schemaSource.getGrammars();
        short referType = desc.getContextType();
        if (grammars != null && grammars.length > 0) {
            Vector expandedGrammars = expandGrammars(grammars);
            if (this.fNamespaceGrowth || !existingGrammars(expandedGrammars)) {
                addGrammars(expandedGrammars);
                if (referType == 3) {
                    desc.setTargetNamespace(grammars[0].getTargetNamespace());
                    return null;
                }
                return null;
            }
            return null;
        }
        XSObject[] components = schemaSource.getComponents();
        if (components != null && components.length > 0) {
            Map<String, Vector> importDependencies = new HashMap<>();
            Vector expandedComponents = expandComponents(components, importDependencies);
            if (this.fNamespaceGrowth || canAddComponents(expandedComponents)) {
                addGlobalComponents(expandedComponents, importDependencies);
                if (referType == 3) {
                    desc.setTargetNamespace(components[0].getNamespace());
                    return null;
                }
                return null;
            }
            return null;
        }
        return null;
    }

    private Vector expandGrammars(SchemaGrammar[] grammars) {
        Vector currGrammars = new Vector();
        for (int i2 = 0; i2 < grammars.length; i2++) {
            if (!currGrammars.contains(grammars[i2])) {
                currGrammars.add(grammars[i2]);
            }
        }
        for (int i3 = 0; i3 < currGrammars.size(); i3++) {
            SchemaGrammar sg1 = (SchemaGrammar) currGrammars.elementAt(i3);
            Vector gs = sg1.getImportedGrammars();
            if (gs != null) {
                for (int j2 = gs.size() - 1; j2 >= 0; j2--) {
                    SchemaGrammar sg2 = (SchemaGrammar) gs.elementAt(j2);
                    if (!currGrammars.contains(sg2)) {
                        currGrammars.addElement(sg2);
                    }
                }
            }
        }
        return currGrammars;
    }

    private boolean existingGrammars(Vector grammars) throws MissingResourceException, XNIException {
        int length = grammars.size();
        XSDDescription desc = new XSDDescription();
        for (int i2 = 0; i2 < length; i2++) {
            SchemaGrammar sg1 = (SchemaGrammar) grammars.elementAt(i2);
            desc.setNamespace(sg1.getTargetNamespace());
            SchemaGrammar sg2 = findGrammar(desc, false);
            if (sg2 != null) {
                return true;
            }
        }
        return false;
    }

    private boolean canAddComponents(Vector components) {
        int size = components.size();
        XSDDescription desc = new XSDDescription();
        for (int i2 = 0; i2 < size; i2++) {
            XSObject component = (XSObject) components.elementAt(i2);
            if (!canAddComponent(component, desc)) {
                return false;
            }
        }
        return true;
    }

    private boolean canAddComponent(XSObject component, XSDDescription desc) throws MissingResourceException, XNIException {
        desc.setNamespace(component.getNamespace());
        SchemaGrammar sg = findGrammar(desc, false);
        if (sg == null) {
            return true;
        }
        if (sg.isImmutable()) {
            return false;
        }
        short componentType = component.getType();
        String name = component.getName();
        switch (componentType) {
            case 1:
                if (sg.getGlobalAttributeDecl(name) == component) {
                    return true;
                }
                return false;
            case 2:
                if (sg.getGlobalElementDecl(name) == component) {
                    return true;
                }
                return false;
            case 3:
                if (sg.getGlobalTypeDecl(name) == component) {
                    return true;
                }
                return false;
            case 4:
            case 7:
            case 8:
            case 9:
            case 10:
            default:
                return true;
            case 5:
                if (sg.getGlobalAttributeDecl(name) == component) {
                    return true;
                }
                return false;
            case 6:
                if (sg.getGlobalGroupDecl(name) == component) {
                    return true;
                }
                return false;
            case 11:
                if (sg.getGlobalNotationDecl(name) == component) {
                    return true;
                }
                return false;
        }
    }

    private void addGrammars(Vector grammars) throws MissingResourceException, XNIException {
        int length = grammars.size();
        XSDDescription desc = new XSDDescription();
        for (int i2 = 0; i2 < length; i2++) {
            SchemaGrammar sg1 = (SchemaGrammar) grammars.elementAt(i2);
            desc.setNamespace(sg1.getTargetNamespace());
            SchemaGrammar sg2 = findGrammar(desc, this.fNamespaceGrowth);
            if (sg1 != sg2) {
                addGrammarComponents(sg1, sg2);
            }
        }
    }

    private void addGrammarComponents(SchemaGrammar srcGrammar, SchemaGrammar dstGrammar) throws MissingResourceException, XNIException {
        if (dstGrammar == null) {
            createGrammarFrom(srcGrammar);
            return;
        }
        SchemaGrammar tmpGrammar = dstGrammar;
        if (tmpGrammar.isImmutable()) {
            tmpGrammar = createGrammarFrom(dstGrammar);
        }
        addNewGrammarLocations(srcGrammar, tmpGrammar);
        addNewImportedGrammars(srcGrammar, tmpGrammar);
        addNewGrammarComponents(srcGrammar, tmpGrammar);
    }

    private SchemaGrammar createGrammarFrom(SchemaGrammar grammar) {
        SchemaGrammar newGrammar = new SchemaGrammar(grammar);
        this.fGrammarBucket.putGrammar(newGrammar);
        updateImportListWith(newGrammar);
        updateImportListFor(newGrammar);
        return newGrammar;
    }

    private void addNewGrammarLocations(SchemaGrammar srcGrammar, SchemaGrammar dstGrammar) {
        StringList locations = srcGrammar.getDocumentLocations();
        int locSize = locations.size();
        StringList locations2 = dstGrammar.getDocumentLocations();
        for (int i2 = 0; i2 < locSize; i2++) {
            String loc = locations.item(i2);
            if (!locations2.contains(loc)) {
                dstGrammar.addDocument(null, loc);
            }
        }
    }

    private void addNewImportedGrammars(SchemaGrammar srcGrammar, SchemaGrammar dstGrammar) {
        Vector igs1 = srcGrammar.getImportedGrammars();
        if (igs1 != null) {
            Vector igs2 = dstGrammar.getImportedGrammars();
            if (igs2 == null) {
                dstGrammar.setImportedGrammars((Vector) igs1.clone());
            } else {
                updateImportList(igs1, igs2);
            }
        }
    }

    private void updateImportList(Vector importedSrc, Vector importedDst) {
        int size = importedSrc.size();
        for (int i2 = 0; i2 < size; i2++) {
            SchemaGrammar sg = (SchemaGrammar) importedSrc.elementAt(i2);
            if (!containedImportedGrammar(importedDst, sg)) {
                importedDst.add(sg);
            }
        }
    }

    private void addNewGrammarComponents(SchemaGrammar srcGrammar, SchemaGrammar dstGrammar) throws MissingResourceException, XNIException {
        dstGrammar.resetComponents();
        addGlobalElementDecls(srcGrammar, dstGrammar);
        addGlobalAttributeDecls(srcGrammar, dstGrammar);
        addGlobalAttributeGroupDecls(srcGrammar, dstGrammar);
        addGlobalGroupDecls(srcGrammar, dstGrammar);
        addGlobalTypeDecls(srcGrammar, dstGrammar);
        addGlobalNotationDecls(srcGrammar, dstGrammar);
    }

    private void addGlobalElementDecls(SchemaGrammar srcGrammar, SchemaGrammar dstGrammar) {
        XSNamedMap components = srcGrammar.getComponents((short) 2);
        int len = components.getLength();
        for (int i2 = 0; i2 < len; i2++) {
            XSElementDecl srcDecl = (XSElementDecl) components.item(i2);
            XSElementDecl dstDecl = dstGrammar.getGlobalElementDecl(srcDecl.getName());
            if (dstDecl == null) {
                dstGrammar.addGlobalElementDecl(srcDecl);
            } else if (dstDecl != srcDecl) {
            }
        }
        ObjectList componentsExt = srcGrammar.getComponentsExt((short) 2);
        int len2 = componentsExt.getLength();
        for (int i3 = 0; i3 < len2; i3 += 2) {
            String key = (String) componentsExt.item(i3);
            int index = key.indexOf(44);
            String location = key.substring(0, index);
            String name = key.substring(index + 1, key.length());
            XSElementDecl srcDecl2 = (XSElementDecl) componentsExt.item(i3 + 1);
            XSElementDecl dstDecl2 = dstGrammar.getGlobalElementDecl(name, location);
            if (dstDecl2 == null) {
                dstGrammar.addGlobalElementDecl(srcDecl2, location);
            } else if (dstDecl2 != srcDecl2) {
            }
        }
    }

    private void addGlobalAttributeDecls(SchemaGrammar srcGrammar, SchemaGrammar dstGrammar) throws MissingResourceException, XNIException {
        XSNamedMap components = srcGrammar.getComponents((short) 1);
        int len = components.getLength();
        for (int i2 = 0; i2 < len; i2++) {
            XSAttributeDecl srcDecl = (XSAttributeDecl) components.item(i2);
            XSAttributeDecl dstDecl = dstGrammar.getGlobalAttributeDecl(srcDecl.getName());
            if (dstDecl == null) {
                dstGrammar.addGlobalAttributeDecl(srcDecl);
            } else if (dstDecl != srcDecl && !this.fTolerateDuplicates) {
                reportSharingError(srcDecl.getNamespace(), srcDecl.getName());
            }
        }
        ObjectList componentsExt = srcGrammar.getComponentsExt((short) 1);
        int len2 = componentsExt.getLength();
        for (int i3 = 0; i3 < len2; i3 += 2) {
            String key = (String) componentsExt.item(i3);
            int index = key.indexOf(44);
            String location = key.substring(0, index);
            String name = key.substring(index + 1, key.length());
            XSAttributeDecl srcDecl2 = (XSAttributeDecl) componentsExt.item(i3 + 1);
            XSAttributeDecl dstDecl2 = dstGrammar.getGlobalAttributeDecl(name, location);
            if (dstDecl2 == null) {
                dstGrammar.addGlobalAttributeDecl(srcDecl2, location);
            } else if (dstDecl2 != srcDecl2) {
            }
        }
    }

    private void addGlobalAttributeGroupDecls(SchemaGrammar srcGrammar, SchemaGrammar dstGrammar) throws MissingResourceException, XNIException {
        XSNamedMap components = srcGrammar.getComponents((short) 5);
        int len = components.getLength();
        for (int i2 = 0; i2 < len; i2++) {
            XSAttributeGroupDecl srcDecl = (XSAttributeGroupDecl) components.item(i2);
            XSAttributeGroupDecl dstDecl = dstGrammar.getGlobalAttributeGroupDecl(srcDecl.getName());
            if (dstDecl == null) {
                dstGrammar.addGlobalAttributeGroupDecl(srcDecl);
            } else if (dstDecl != srcDecl && !this.fTolerateDuplicates) {
                reportSharingError(srcDecl.getNamespace(), srcDecl.getName());
            }
        }
        ObjectList componentsExt = srcGrammar.getComponentsExt((short) 5);
        int len2 = componentsExt.getLength();
        for (int i3 = 0; i3 < len2; i3 += 2) {
            String key = (String) componentsExt.item(i3);
            int index = key.indexOf(44);
            String location = key.substring(0, index);
            String name = key.substring(index + 1, key.length());
            XSAttributeGroupDecl srcDecl2 = (XSAttributeGroupDecl) componentsExt.item(i3 + 1);
            XSAttributeGroupDecl dstDecl2 = dstGrammar.getGlobalAttributeGroupDecl(name, location);
            if (dstDecl2 == null) {
                dstGrammar.addGlobalAttributeGroupDecl(srcDecl2, location);
            } else if (dstDecl2 != srcDecl2) {
            }
        }
    }

    private void addGlobalNotationDecls(SchemaGrammar srcGrammar, SchemaGrammar dstGrammar) throws MissingResourceException, XNIException {
        XSNamedMap components = srcGrammar.getComponents((short) 11);
        int len = components.getLength();
        for (int i2 = 0; i2 < len; i2++) {
            XSNotationDecl srcDecl = (XSNotationDecl) components.item(i2);
            XSNotationDecl dstDecl = dstGrammar.getGlobalNotationDecl(srcDecl.getName());
            if (dstDecl == null) {
                dstGrammar.addGlobalNotationDecl(srcDecl);
            } else if (dstDecl != srcDecl && !this.fTolerateDuplicates) {
                reportSharingError(srcDecl.getNamespace(), srcDecl.getName());
            }
        }
        ObjectList componentsExt = srcGrammar.getComponentsExt((short) 11);
        int len2 = componentsExt.getLength();
        for (int i3 = 0; i3 < len2; i3 += 2) {
            String key = (String) componentsExt.item(i3);
            int index = key.indexOf(44);
            String location = key.substring(0, index);
            String name = key.substring(index + 1, key.length());
            XSNotationDecl srcDecl2 = (XSNotationDecl) componentsExt.item(i3 + 1);
            XSNotationDecl dstDecl2 = dstGrammar.getGlobalNotationDecl(name, location);
            if (dstDecl2 == null) {
                dstGrammar.addGlobalNotationDecl(srcDecl2, location);
            } else if (dstDecl2 != srcDecl2) {
            }
        }
    }

    private void addGlobalGroupDecls(SchemaGrammar srcGrammar, SchemaGrammar dstGrammar) throws MissingResourceException, XNIException {
        XSNamedMap components = srcGrammar.getComponents((short) 6);
        int len = components.getLength();
        for (int i2 = 0; i2 < len; i2++) {
            XSGroupDecl srcDecl = (XSGroupDecl) components.item(i2);
            XSGroupDecl dstDecl = dstGrammar.getGlobalGroupDecl(srcDecl.getName());
            if (dstDecl == null) {
                dstGrammar.addGlobalGroupDecl(srcDecl);
            } else if (srcDecl != dstDecl && !this.fTolerateDuplicates) {
                reportSharingError(srcDecl.getNamespace(), srcDecl.getName());
            }
        }
        ObjectList componentsExt = srcGrammar.getComponentsExt((short) 6);
        int len2 = componentsExt.getLength();
        for (int i3 = 0; i3 < len2; i3 += 2) {
            String key = (String) componentsExt.item(i3);
            int index = key.indexOf(44);
            String location = key.substring(0, index);
            String name = key.substring(index + 1, key.length());
            XSGroupDecl srcDecl2 = (XSGroupDecl) componentsExt.item(i3 + 1);
            XSGroupDecl dstDecl2 = dstGrammar.getGlobalGroupDecl(name, location);
            if (dstDecl2 == null) {
                dstGrammar.addGlobalGroupDecl(srcDecl2, location);
            } else if (dstDecl2 != srcDecl2) {
            }
        }
    }

    private void addGlobalTypeDecls(SchemaGrammar srcGrammar, SchemaGrammar dstGrammar) throws MissingResourceException, XNIException {
        XSNamedMap components = srcGrammar.getComponents((short) 3);
        int len = components.getLength();
        for (int i2 = 0; i2 < len; i2++) {
            XSTypeDefinition srcDecl = (XSTypeDefinition) components.item(i2);
            XSTypeDefinition dstDecl = dstGrammar.getGlobalTypeDecl(srcDecl.getName());
            if (dstDecl == null) {
                dstGrammar.addGlobalTypeDecl(srcDecl);
            } else if (dstDecl != srcDecl && !this.fTolerateDuplicates) {
                reportSharingError(srcDecl.getNamespace(), srcDecl.getName());
            }
        }
        ObjectList componentsExt = srcGrammar.getComponentsExt((short) 3);
        int len2 = componentsExt.getLength();
        for (int i3 = 0; i3 < len2; i3 += 2) {
            String key = (String) componentsExt.item(i3);
            int index = key.indexOf(44);
            String location = key.substring(0, index);
            String name = key.substring(index + 1, key.length());
            XSTypeDefinition srcDecl2 = (XSTypeDefinition) componentsExt.item(i3 + 1);
            XSTypeDefinition dstDecl2 = dstGrammar.getGlobalTypeDecl(name, location);
            if (dstDecl2 == null) {
                dstGrammar.addGlobalTypeDecl(srcDecl2, location);
            } else if (dstDecl2 != srcDecl2) {
            }
        }
    }

    private Vector expandComponents(XSObject[] components, Map<String, Vector> dependencies) {
        Vector newComponents = new Vector();
        for (int i2 = 0; i2 < components.length; i2++) {
            if (!newComponents.contains(components[i2])) {
                newComponents.add(components[i2]);
            }
        }
        for (int i3 = 0; i3 < newComponents.size(); i3++) {
            XSObject component = (XSObject) newComponents.elementAt(i3);
            expandRelatedComponents(component, newComponents, dependencies);
        }
        return newComponents;
    }

    private void expandRelatedComponents(XSObject component, Vector componentList, Map<String, Vector> dependencies) {
        short componentType = component.getType();
        switch (componentType) {
            case 1:
                expandRelatedAttributeComponents((XSAttributeDeclaration) component, componentList, component.getNamespace(), dependencies);
                return;
            case 2:
                break;
            case 3:
                expandRelatedTypeComponents((XSTypeDefinition) component, componentList, component.getNamespace(), dependencies);
                return;
            case 4:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            default:
                return;
            case 5:
                expandRelatedAttributeGroupComponents((XSAttributeGroupDefinition) component, componentList, component.getNamespace(), dependencies);
                break;
            case 6:
                expandRelatedModelGroupDefinitionComponents((XSModelGroupDefinition) component, componentList, component.getNamespace(), dependencies);
                return;
        }
        expandRelatedElementComponents((XSElementDeclaration) component, componentList, component.getNamespace(), dependencies);
    }

    private void expandRelatedAttributeComponents(XSAttributeDeclaration decl, Vector componentList, String namespace, Map<String, Vector> dependencies) {
        addRelatedType(decl.getTypeDefinition(), componentList, namespace, dependencies);
    }

    private void expandRelatedElementComponents(XSElementDeclaration decl, Vector componentList, String namespace, Map<String, Vector> dependencies) {
        addRelatedType(decl.getTypeDefinition(), componentList, namespace, dependencies);
        XSElementDeclaration subElemDecl = decl.getSubstitutionGroupAffiliation();
        if (subElemDecl != null) {
            addRelatedElement(subElemDecl, componentList, namespace, dependencies);
        }
    }

    private void expandRelatedTypeComponents(XSTypeDefinition type, Vector componentList, String namespace, Map<String, Vector> dependencies) {
        if (type instanceof XSComplexTypeDecl) {
            expandRelatedComplexTypeComponents((XSComplexTypeDecl) type, componentList, namespace, dependencies);
        } else if (type instanceof XSSimpleTypeDecl) {
            expandRelatedSimpleTypeComponents((XSSimpleTypeDefinition) type, componentList, namespace, dependencies);
        }
    }

    private void expandRelatedModelGroupDefinitionComponents(XSModelGroupDefinition modelGroupDef, Vector componentList, String namespace, Map<String, Vector> dependencies) {
        expandRelatedModelGroupComponents(modelGroupDef.getModelGroup(), componentList, namespace, dependencies);
    }

    private void expandRelatedAttributeGroupComponents(XSAttributeGroupDefinition attrGroup, Vector componentList, String namespace, Map<String, Vector> dependencies) {
        expandRelatedAttributeUsesComponents(attrGroup.getAttributeUses(), componentList, namespace, dependencies);
    }

    private void expandRelatedComplexTypeComponents(XSComplexTypeDecl type, Vector componentList, String namespace, Map<String, Vector> dependencies) {
        addRelatedType(type.getBaseType(), componentList, namespace, dependencies);
        expandRelatedAttributeUsesComponents(type.getAttributeUses(), componentList, namespace, dependencies);
        XSParticle particle = type.getParticle();
        if (particle != null) {
            expandRelatedParticleComponents(particle, componentList, namespace, dependencies);
        }
    }

    private void expandRelatedSimpleTypeComponents(XSSimpleTypeDefinition type, Vector componentList, String namespace, Map<String, Vector> dependencies) {
        XSTypeDefinition baseType = type.getBaseType();
        if (baseType != null) {
            addRelatedType(baseType, componentList, namespace, dependencies);
        }
        XSTypeDefinition itemType = type.getItemType();
        if (itemType != null) {
            addRelatedType(itemType, componentList, namespace, dependencies);
        }
        XSTypeDefinition primitiveType = type.getPrimitiveType();
        if (primitiveType != null) {
            addRelatedType(primitiveType, componentList, namespace, dependencies);
        }
        XSObjectList memberTypes = type.getMemberTypes();
        if (memberTypes.size() > 0) {
            for (int i2 = 0; i2 < memberTypes.size(); i2++) {
                addRelatedType((XSTypeDefinition) memberTypes.item(i2), componentList, namespace, dependencies);
            }
        }
    }

    private void expandRelatedAttributeUsesComponents(XSObjectList attrUses, Vector componentList, String namespace, Map<String, Vector> dependencies) {
        int attrUseSize = attrUses == null ? 0 : attrUses.size();
        for (int i2 = 0; i2 < attrUseSize; i2++) {
            expandRelatedAttributeUseComponents((XSAttributeUse) attrUses.item(i2), componentList, namespace, dependencies);
        }
    }

    private void expandRelatedAttributeUseComponents(XSAttributeUse component, Vector componentList, String namespace, Map<String, Vector> dependencies) {
        addRelatedAttribute(component.getAttrDeclaration(), componentList, namespace, dependencies);
    }

    private void expandRelatedParticleComponents(XSParticle component, Vector componentList, String namespace, Map<String, Vector> dependencies) {
        XSTerm term = component.getTerm();
        switch (term.getType()) {
            case 2:
                addRelatedElement((XSElementDeclaration) term, componentList, namespace, dependencies);
                break;
            case 7:
                expandRelatedModelGroupComponents((XSModelGroup) term, componentList, namespace, dependencies);
                break;
        }
    }

    private void expandRelatedModelGroupComponents(XSModelGroup modelGroup, Vector componentList, String namespace, Map<String, Vector> dependencies) {
        XSObjectList particles = modelGroup.getParticles();
        int length = particles == null ? 0 : particles.getLength();
        for (int i2 = 0; i2 < length; i2++) {
            expandRelatedParticleComponents((XSParticle) particles.item(i2), componentList, namespace, dependencies);
        }
    }

    private void addRelatedType(XSTypeDefinition type, Vector componentList, String namespace, Map<String, Vector> dependencies) {
        if (!type.getAnonymous()) {
            if (!type.getNamespace().equals(SchemaSymbols.URI_SCHEMAFORSCHEMA) && !componentList.contains(type)) {
                Vector importedNamespaces = findDependentNamespaces(namespace, dependencies);
                addNamespaceDependency(namespace, type.getNamespace(), importedNamespaces);
                componentList.add(type);
                return;
            }
            return;
        }
        expandRelatedTypeComponents(type, componentList, namespace, dependencies);
    }

    private void addRelatedElement(XSElementDeclaration decl, Vector componentList, String namespace, Map<String, Vector> dependencies) {
        if (decl.getScope() == 1) {
            if (!componentList.contains(decl)) {
                Vector importedNamespaces = findDependentNamespaces(namespace, dependencies);
                addNamespaceDependency(namespace, decl.getNamespace(), importedNamespaces);
                componentList.add(decl);
                return;
            }
            return;
        }
        expandRelatedElementComponents(decl, componentList, namespace, dependencies);
    }

    private void addRelatedAttribute(XSAttributeDeclaration decl, Vector componentList, String namespace, Map<String, Vector> dependencies) {
        if (decl.getScope() == 1) {
            if (!componentList.contains(decl)) {
                Vector importedNamespaces = findDependentNamespaces(namespace, dependencies);
                addNamespaceDependency(namespace, decl.getNamespace(), importedNamespaces);
                componentList.add(decl);
                return;
            }
            return;
        }
        expandRelatedAttributeComponents(decl, componentList, namespace, dependencies);
    }

    private void addGlobalComponents(Vector components, Map<String, Vector> importDependencies) throws MissingResourceException, XNIException {
        XSDDescription desc = new XSDDescription();
        int size = components.size();
        for (int i2 = 0; i2 < size; i2++) {
            addGlobalComponent((XSObject) components.elementAt(i2), desc);
        }
        updateImportDependencies(importDependencies);
    }

    private void addGlobalComponent(XSObject component, XSDDescription desc) throws MissingResourceException, XNIException {
        String namespace = component.getNamespace();
        desc.setNamespace(namespace);
        SchemaGrammar sg = getSchemaGrammar(desc);
        short componentType = component.getType();
        String name = component.getName();
        switch (componentType) {
            case 1:
                if (((XSAttributeDecl) component).getScope() == 1) {
                    if (sg.getGlobalAttributeDecl(name) == null) {
                        sg.addGlobalAttributeDecl((XSAttributeDecl) component);
                    }
                    if (sg.getGlobalAttributeDecl(name, "") == null) {
                        sg.addGlobalAttributeDecl((XSAttributeDecl) component, "");
                        break;
                    }
                }
                break;
            case 2:
                if (((XSElementDecl) component).getScope() == 1) {
                    sg.addGlobalElementDeclAll((XSElementDecl) component);
                    if (sg.getGlobalElementDecl(name) == null) {
                        sg.addGlobalElementDecl((XSElementDecl) component);
                    }
                    if (sg.getGlobalElementDecl(name, "") == null) {
                        sg.addGlobalElementDecl((XSElementDecl) component, "");
                        break;
                    }
                }
                break;
            case 3:
                if (!((XSTypeDefinition) component).getAnonymous()) {
                    if (sg.getGlobalTypeDecl(name) == null) {
                        sg.addGlobalTypeDecl((XSTypeDefinition) component);
                    }
                    if (sg.getGlobalTypeDecl(name, "") == null) {
                        sg.addGlobalTypeDecl((XSTypeDefinition) component, "");
                        break;
                    }
                }
                break;
            case 5:
                if (sg.getGlobalAttributeDecl(name) == null) {
                    sg.addGlobalAttributeGroupDecl((XSAttributeGroupDecl) component);
                }
                if (sg.getGlobalAttributeDecl(name, "") == null) {
                    sg.addGlobalAttributeGroupDecl((XSAttributeGroupDecl) component, "");
                    break;
                }
                break;
            case 6:
                if (sg.getGlobalGroupDecl(name) == null) {
                    sg.addGlobalGroupDecl((XSGroupDecl) component);
                }
                if (sg.getGlobalGroupDecl(name, "") == null) {
                    sg.addGlobalGroupDecl((XSGroupDecl) component, "");
                    break;
                }
                break;
            case 11:
                if (sg.getGlobalNotationDecl(name) == null) {
                    sg.addGlobalNotationDecl((XSNotationDecl) component);
                }
                if (sg.getGlobalNotationDecl(name, "") == null) {
                    sg.addGlobalNotationDecl((XSNotationDecl) component, "");
                    break;
                }
                break;
        }
    }

    private void updateImportDependencies(Map<String, Vector> table) {
        if (table == null) {
            return;
        }
        for (Map.Entry<String, Vector> entry : table.entrySet()) {
            String namespace = entry.getKey();
            Vector importList = entry.getValue();
            if (importList.size() > 0) {
                expandImportList(namespace, importList);
            }
        }
    }

    private void expandImportList(String namespace, Vector namespaceList) {
        SchemaGrammar sg = this.fGrammarBucket.getGrammar(namespace);
        if (sg != null) {
            Vector isgs = sg.getImportedGrammars();
            if (isgs == null) {
                Vector isgs2 = new Vector();
                addImportList(sg, isgs2, namespaceList);
                sg.setImportedGrammars(isgs2);
                return;
            }
            updateImportList(sg, isgs, namespaceList);
        }
    }

    private void addImportList(SchemaGrammar sg, Vector importedGrammars, Vector namespaceList) {
        int size = namespaceList.size();
        for (int i2 = 0; i2 < size; i2++) {
            SchemaGrammar isg = this.fGrammarBucket.getGrammar((String) namespaceList.elementAt(i2));
            if (isg != null) {
                importedGrammars.add(isg);
            }
        }
    }

    private void updateImportList(SchemaGrammar sg, Vector importedGrammars, Vector namespaceList) {
        int size = namespaceList.size();
        for (int i2 = 0; i2 < size; i2++) {
            SchemaGrammar isg = this.fGrammarBucket.getGrammar((String) namespaceList.elementAt(i2));
            if (isg != null && !containedImportedGrammar(importedGrammars, isg)) {
                importedGrammars.add(isg);
            }
        }
    }

    private boolean containedImportedGrammar(Vector importedGrammar, SchemaGrammar grammar) {
        int size = importedGrammar.size();
        for (int i2 = 0; i2 < size; i2++) {
            SchemaGrammar sg = (SchemaGrammar) importedGrammar.elementAt(i2);
            if (null2EmptyString(sg.getTargetNamespace()).equals(null2EmptyString(grammar.getTargetNamespace()))) {
                return true;
            }
        }
        return false;
    }

    private SchemaGrammar getSchemaGrammar(XSDDescription desc) throws MissingResourceException, XNIException {
        SchemaGrammar sg = findGrammar(desc, this.fNamespaceGrowth);
        if (sg == null) {
            sg = new SchemaGrammar(desc.getNamespace(), desc.makeClone(), this.fSymbolTable);
            this.fGrammarBucket.putGrammar(sg);
        } else if (sg.isImmutable()) {
            sg = createGrammarFrom(sg);
        }
        return sg;
    }

    private Vector findDependentNamespaces(String namespace, Map table) {
        String ns = null2EmptyString(namespace);
        Vector namespaceList = (Vector) getFromMap(table, ns);
        if (namespaceList == null) {
            namespaceList = new Vector();
            table.put(ns, namespaceList);
        }
        return namespaceList;
    }

    private void addNamespaceDependency(String namespace1, String namespace2, Vector list) {
        String ns1 = null2EmptyString(namespace1);
        String ns2 = null2EmptyString(namespace2);
        if (!ns1.equals(ns2) && !list.contains(ns2)) {
            list.add(ns2);
        }
    }

    private void reportSharingError(String namespace, String name) throws MissingResourceException, XNIException {
        String qName = namespace == null ? "," + name : namespace + "," + name;
        reportSchemaError("sch-props-correct.2", new Object[]{qName}, null);
    }

    private void createTraversers() {
        this.fAttributeChecker = new XSAttributeChecker(this);
        this.fAttributeGroupTraverser = new XSDAttributeGroupTraverser(this, this.fAttributeChecker);
        this.fAttributeTraverser = new XSDAttributeTraverser(this, this.fAttributeChecker);
        this.fComplexTypeTraverser = new XSDComplexTypeTraverser(this, this.fAttributeChecker);
        this.fElementTraverser = new XSDElementTraverser(this, this.fAttributeChecker);
        this.fGroupTraverser = new XSDGroupTraverser(this, this.fAttributeChecker);
        this.fKeyrefTraverser = new XSDKeyrefTraverser(this, this.fAttributeChecker);
        this.fNotationTraverser = new XSDNotationTraverser(this, this.fAttributeChecker);
        this.fSimpleTypeTraverser = new XSDSimpleTypeTraverser(this, this.fAttributeChecker);
        this.fUniqueOrKeyTraverser = new XSDUniqueOrKeyTraverser(this, this.fAttributeChecker);
        this.fWildCardTraverser = new XSDWildcardTraverser(this, this.fAttributeChecker);
    }

    void prepareForParse() {
        this.fTraversed.clear();
        this.fDoc2SystemId.clear();
        this.fHiddenNodes.clear();
        this.fLastSchemaWasDuplicate = false;
    }

    void prepareForTraverse() {
        if (!this.registryEmpty) {
            this.fUnparsedAttributeRegistry.clear();
            this.fUnparsedAttributeGroupRegistry.clear();
            this.fUnparsedElementRegistry.clear();
            this.fUnparsedGroupRegistry.clear();
            this.fUnparsedIdentityConstraintRegistry.clear();
            this.fUnparsedNotationRegistry.clear();
            this.fUnparsedTypeRegistry.clear();
            this.fUnparsedAttributeRegistrySub.clear();
            this.fUnparsedAttributeGroupRegistrySub.clear();
            this.fUnparsedElementRegistrySub.clear();
            this.fUnparsedGroupRegistrySub.clear();
            this.fUnparsedIdentityConstraintRegistrySub.clear();
            this.fUnparsedNotationRegistrySub.clear();
            this.fUnparsedTypeRegistrySub.clear();
        }
        for (int i2 = 1; i2 <= 7; i2++) {
            if (this.fUnparsedRegistriesExt[i2] != null) {
                this.fUnparsedRegistriesExt[i2].clear();
            }
        }
        this.fDependencyMap.clear();
        this.fDoc2XSDocumentMap.clear();
        if (this.fRedefine2XSDMap != null) {
            this.fRedefine2XSDMap.clear();
        }
        if (this.fRedefine2NSSupport != null) {
            this.fRedefine2NSSupport.clear();
        }
        this.fAllTNSs.removeAllElements();
        this.fImportMap.clear();
        this.fRoot = null;
        for (int i3 = 0; i3 < this.fLocalElemStackPos; i3++) {
            this.fParticle[i3] = null;
            this.fLocalElementDecl[i3] = null;
            this.fLocalElementDecl_schema[i3] = null;
            this.fLocalElemNamespaceContext[i3] = null;
        }
        this.fLocalElemStackPos = 0;
        for (int i4 = 0; i4 < this.fKeyrefStackPos; i4++) {
            this.fKeyrefs[i4] = null;
            this.fKeyrefElems[i4] = null;
            this.fKeyrefNamespaceContext[i4] = null;
            this.fKeyrefsMapXSDocumentInfo[i4] = null;
        }
        this.fKeyrefStackPos = 0;
        if (this.fAttributeChecker == null) {
            createTraversers();
        }
        Locale locale = this.fErrorReporter.getLocale();
        this.fAttributeChecker.reset(this.fSymbolTable);
        this.fAttributeGroupTraverser.reset(this.fSymbolTable, this.fValidateAnnotations, locale);
        this.fAttributeTraverser.reset(this.fSymbolTable, this.fValidateAnnotations, locale);
        this.fComplexTypeTraverser.reset(this.fSymbolTable, this.fValidateAnnotations, locale);
        this.fElementTraverser.reset(this.fSymbolTable, this.fValidateAnnotations, locale);
        this.fGroupTraverser.reset(this.fSymbolTable, this.fValidateAnnotations, locale);
        this.fKeyrefTraverser.reset(this.fSymbolTable, this.fValidateAnnotations, locale);
        this.fNotationTraverser.reset(this.fSymbolTable, this.fValidateAnnotations, locale);
        this.fSimpleTypeTraverser.reset(this.fSymbolTable, this.fValidateAnnotations, locale);
        this.fUniqueOrKeyTraverser.reset(this.fSymbolTable, this.fValidateAnnotations, locale);
        this.fWildCardTraverser.reset(this.fSymbolTable, this.fValidateAnnotations, locale);
        this.fRedefinedRestrictedAttributeGroupRegistry.clear();
        this.fRedefinedRestrictedGroupRegistry.clear();
        this.fGlobalAttrDecls.clear();
        this.fGlobalAttrGrpDecls.clear();
        this.fGlobalElemDecls.clear();
        this.fGlobalGroupDecls.clear();
        this.fGlobalNotationDecls.clear();
        this.fGlobalIDConstraintDecls.clear();
        this.fGlobalTypeDecls.clear();
    }

    public void setDeclPool(XSDeclarationPool declPool) {
        this.fDeclPool = declPool;
    }

    public void setDVFactory(SchemaDVFactory dvFactory) {
        this.fDVFactory = dvFactory;
    }

    public SchemaDVFactory getDVFactory() {
        return this.fDVFactory;
    }

    public void reset(XMLComponentManager componentManager) throws XNIException {
        this.fSymbolTable = (SymbolTable) componentManager.getProperty("http://apache.org/xml/properties/internal/symbol-table");
        this.fSecurityManager = (XMLSecurityManager) componentManager.getProperty("http://apache.org/xml/properties/security-manager", null);
        this.fEntityManager = (XMLEntityResolver) componentManager.getProperty(ENTITY_MANAGER);
        XMLEntityResolver er = (XMLEntityResolver) componentManager.getProperty("http://apache.org/xml/properties/internal/entity-resolver");
        if (er != null) {
            this.fSchemaParser.setEntityResolver(er);
        }
        this.fErrorReporter = (XMLErrorReporter) componentManager.getProperty("http://apache.org/xml/properties/internal/error-reporter");
        this.fErrorHandler = this.fErrorReporter.getErrorHandler();
        this.fLocale = this.fErrorReporter.getLocale();
        this.fValidateAnnotations = componentManager.getFeature(VALIDATE_ANNOTATIONS, false);
        this.fHonourAllSchemaLocations = componentManager.getFeature(HONOUR_ALL_SCHEMALOCATIONS, false);
        this.fNamespaceGrowth = componentManager.getFeature(NAMESPACE_GROWTH, false);
        this.fTolerateDuplicates = componentManager.getFeature(TOLERATE_DUPLICATES, false);
        try {
            if (this.fErrorHandler != this.fSchemaParser.getProperty(ERROR_HANDLER)) {
                this.fSchemaParser.setProperty(ERROR_HANDLER, this.fErrorHandler != null ? this.fErrorHandler : new DefaultErrorHandler());
                if (this.fAnnotationValidator != null) {
                    this.fAnnotationValidator.setProperty(ERROR_HANDLER, this.fErrorHandler != null ? this.fErrorHandler : new DefaultErrorHandler());
                }
            }
            if (this.fLocale != this.fSchemaParser.getProperty("http://apache.org/xml/properties/locale")) {
                this.fSchemaParser.setProperty("http://apache.org/xml/properties/locale", this.fLocale);
                if (this.fAnnotationValidator != null) {
                    this.fAnnotationValidator.setProperty("http://apache.org/xml/properties/locale", this.fLocale);
                }
            }
        } catch (XMLConfigurationException e2) {
        }
        try {
            this.fSchemaParser.setFeature(CONTINUE_AFTER_FATAL_ERROR, this.fErrorReporter.getFeature(CONTINUE_AFTER_FATAL_ERROR));
        } catch (XMLConfigurationException e3) {
        }
        try {
            if (componentManager.getFeature(ALLOW_JAVA_ENCODINGS, false)) {
                this.fSchemaParser.setFeature(ALLOW_JAVA_ENCODINGS, true);
            }
        } catch (XMLConfigurationException e4) {
        }
        try {
            if (componentManager.getFeature(STANDARD_URI_CONFORMANT_FEATURE, false)) {
                this.fSchemaParser.setFeature(STANDARD_URI_CONFORMANT_FEATURE, true);
            }
        } catch (XMLConfigurationException e5) {
        }
        try {
            this.fGrammarPool = (XMLGrammarPool) componentManager.getProperty("http://apache.org/xml/properties/internal/grammar-pool");
        } catch (XMLConfigurationException e6) {
            this.fGrammarPool = null;
        }
        try {
            if (componentManager.getFeature(DISALLOW_DOCTYPE, false)) {
                this.fSchemaParser.setFeature(DISALLOW_DOCTYPE, true);
            }
        } catch (XMLConfigurationException e7) {
        }
        try {
            if (this.fSecurityManager != null) {
                this.fSchemaParser.setProperty("http://apache.org/xml/properties/security-manager", this.fSecurityManager);
            }
        } catch (XMLConfigurationException e8) {
        }
        this.fSecurityPropertyMgr = (XMLSecurityPropertyManager) componentManager.getProperty("http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager");
        this.fSchemaParser.setProperty("http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager", this.fSecurityPropertyMgr);
        this.fAccessExternalDTD = this.fSecurityPropertyMgr.getValue(XMLSecurityPropertyManager.Property.ACCESS_EXTERNAL_DTD);
        this.fAccessExternalSchema = this.fSecurityPropertyMgr.getValue(XMLSecurityPropertyManager.Property.ACCESS_EXTERNAL_SCHEMA);
        this.fOverrideDefaultParser = componentManager.getFeature(JdkXmlUtils.OVERRIDE_PARSER);
        this.fSchemaParser.setFeature(JdkXmlUtils.OVERRIDE_PARSER, this.fOverrideDefaultParser);
    }

    void traverseLocalElements() {
        this.fElementTraverser.fDeferTraversingLocalElements = false;
        for (int i2 = 0; i2 < this.fLocalElemStackPos; i2++) {
            Element currElem = this.fLocalElementDecl[i2];
            XSDocumentInfo currSchema = this.fLocalElementDecl_schema[i2];
            SchemaGrammar currGrammar = this.fGrammarBucket.getGrammar(currSchema.fTargetNamespace);
            this.fElementTraverser.traverseLocal(this.fParticle[i2], currElem, currSchema, currGrammar, this.fAllContext[i2], this.fParent[i2], this.fLocalElemNamespaceContext[i2]);
            if (this.fParticle[i2].fType == 0) {
                XSModelGroupImpl group = null;
                if (this.fParent[i2] instanceof XSComplexTypeDecl) {
                    XSParticle p2 = ((XSComplexTypeDecl) this.fParent[i2]).getParticle();
                    if (p2 != null) {
                        group = (XSModelGroupImpl) p2.getTerm();
                    }
                } else {
                    group = ((XSGroupDecl) this.fParent[i2]).fModelGroup;
                }
                if (group != null) {
                    removeParticle(group, this.fParticle[i2]);
                }
            }
        }
    }

    private boolean removeParticle(XSModelGroupImpl group, XSParticleDecl particle) {
        for (int i2 = 0; i2 < group.fParticleCount; i2++) {
            XSParticleDecl member = group.fParticles[i2];
            if (member == particle) {
                for (int j2 = i2; j2 < group.fParticleCount - 1; j2++) {
                    group.fParticles[j2] = group.fParticles[j2 + 1];
                }
                group.fParticleCount--;
                return true;
            }
            if (member.fType == 3 && removeParticle((XSModelGroupImpl) member.fValue, particle)) {
                return true;
            }
        }
        return false;
    }

    /* JADX WARN: Type inference failed for: r0v53, types: [java.lang.Object, java.lang.String[], java.lang.String[][]] */
    void fillInLocalElemInfo(Element elmDecl, XSDocumentInfo schemaDoc, int allContextFlags, XSObject parent, XSParticleDecl particle) {
        if (this.fParticle.length == this.fLocalElemStackPos) {
            XSParticleDecl[] newStackP = new XSParticleDecl[this.fLocalElemStackPos + 10];
            System.arraycopy(this.fParticle, 0, newStackP, 0, this.fLocalElemStackPos);
            this.fParticle = newStackP;
            Element[] newStackE = new Element[this.fLocalElemStackPos + 10];
            System.arraycopy(this.fLocalElementDecl, 0, newStackE, 0, this.fLocalElemStackPos);
            this.fLocalElementDecl = newStackE;
            XSDocumentInfo[] newStackE_schema = new XSDocumentInfo[this.fLocalElemStackPos + 10];
            System.arraycopy(this.fLocalElementDecl_schema, 0, newStackE_schema, 0, this.fLocalElemStackPos);
            this.fLocalElementDecl_schema = newStackE_schema;
            int[] newStackI = new int[this.fLocalElemStackPos + 10];
            System.arraycopy(this.fAllContext, 0, newStackI, 0, this.fLocalElemStackPos);
            this.fAllContext = newStackI;
            XSObject[] newStackC = new XSObject[this.fLocalElemStackPos + 10];
            System.arraycopy(this.fParent, 0, newStackC, 0, this.fLocalElemStackPos);
            this.fParent = newStackC;
            ?? r0 = new String[this.fLocalElemStackPos + 10];
            System.arraycopy(this.fLocalElemNamespaceContext, 0, r0, 0, this.fLocalElemStackPos);
            this.fLocalElemNamespaceContext = r0;
        }
        this.fParticle[this.fLocalElemStackPos] = particle;
        this.fLocalElementDecl[this.fLocalElemStackPos] = elmDecl;
        this.fLocalElementDecl_schema[this.fLocalElemStackPos] = schemaDoc;
        this.fAllContext[this.fLocalElemStackPos] = allContextFlags;
        this.fParent[this.fLocalElemStackPos] = parent;
        String[][] strArr = this.fLocalElemNamespaceContext;
        int i2 = this.fLocalElemStackPos;
        this.fLocalElemStackPos = i2 + 1;
        strArr[i2] = schemaDoc.fNamespaceSupport.getEffectiveLocalContext();
    }

    void checkForDuplicateNames(String qName, int declType, Map<String, Element> registry, Map<String, XSDocumentInfo> registry_sub, Element currComp, XSDocumentInfo currSchema) throws DOMException, MissingResourceException, XNIException {
        Object objElem = registry.get(qName);
        if (objElem == null) {
            if (this.fNamespaceGrowth && !this.fTolerateDuplicates) {
                checkForDuplicateNames(qName, declType, currComp);
            }
            registry.put(qName, currComp);
            registry_sub.put(qName, currSchema);
        } else {
            Element collidingElem = (Element) objElem;
            XSDocumentInfo collidingElemSchema = registry_sub.get(qName);
            if (collidingElem == currComp) {
                return;
            }
            XSDocumentInfo redefinedSchema = null;
            boolean collidedWithRedefine = true;
            Element elemParent = DOMUtil.getParent(collidingElem);
            if (DOMUtil.getLocalName(elemParent).equals(SchemaSymbols.ELT_REDEFINE)) {
                redefinedSchema = this.fRedefine2XSDMap != null ? (XSDocumentInfo) this.fRedefine2XSDMap.get(elemParent) : null;
            } else if (DOMUtil.getLocalName(DOMUtil.getParent(currComp)).equals(SchemaSymbols.ELT_REDEFINE)) {
                redefinedSchema = collidingElemSchema;
                collidedWithRedefine = false;
            }
            if (redefinedSchema != null) {
                if (collidingElemSchema == currSchema) {
                    reportSchemaError("sch-props-correct.2", new Object[]{qName}, currComp);
                    return;
                }
                String newName = qName.substring(qName.lastIndexOf(44) + 1) + REDEF_IDENTIFIER;
                if (redefinedSchema == currSchema) {
                    currComp.setAttribute(SchemaSymbols.ATT_NAME, newName);
                    if (currSchema.fTargetNamespace == null) {
                        registry.put("," + newName, currComp);
                        registry_sub.put("," + newName, currSchema);
                    } else {
                        registry.put(currSchema.fTargetNamespace + "," + newName, currComp);
                        registry_sub.put(currSchema.fTargetNamespace + "," + newName, currSchema);
                    }
                    if (currSchema.fTargetNamespace == null) {
                        checkForDuplicateNames("," + newName, declType, registry, registry_sub, currComp, currSchema);
                    } else {
                        checkForDuplicateNames(currSchema.fTargetNamespace + "," + newName, declType, registry, registry_sub, currComp, currSchema);
                    }
                } else if (collidedWithRedefine) {
                    if (currSchema.fTargetNamespace == null) {
                        checkForDuplicateNames("," + newName, declType, registry, registry_sub, currComp, currSchema);
                    } else {
                        checkForDuplicateNames(currSchema.fTargetNamespace + "," + newName, declType, registry, registry_sub, currComp, currSchema);
                    }
                } else {
                    reportSchemaError("sch-props-correct.2", new Object[]{qName}, currComp);
                }
            } else if (!this.fTolerateDuplicates) {
                reportSchemaError("sch-props-correct.2", new Object[]{qName}, currComp);
            } else if (this.fUnparsedRegistriesExt[declType] != null && this.fUnparsedRegistriesExt[declType].get(qName) == currSchema) {
                reportSchemaError("sch-props-correct.2", new Object[]{qName}, currComp);
            }
        }
        if (this.fTolerateDuplicates) {
            if (this.fUnparsedRegistriesExt[declType] == null) {
                this.fUnparsedRegistriesExt[declType] = new HashMap();
            }
            this.fUnparsedRegistriesExt[declType].put(qName, currSchema);
        }
    }

    void checkForDuplicateNames(String qName, int declType, Element currComp) throws MissingResourceException, XNIException {
        int namespaceEnd = qName.indexOf(44);
        String namespace = qName.substring(0, namespaceEnd);
        SchemaGrammar grammar = this.fGrammarBucket.getGrammar(emptyString2Null(namespace));
        if (grammar != null) {
            Object obj = getGlobalDeclFromGrammar(grammar, declType, qName.substring(namespaceEnd + 1));
            if (obj != null) {
                reportSchemaError("sch-props-correct.2", new Object[]{qName}, currComp);
            }
        }
    }

    private void renameRedefiningComponents(XSDocumentInfo currSchema, Element child, String componentType, String oldName, String newName) throws DOMException, MissingResourceException, XNIException {
        if (componentType.equals(SchemaSymbols.ELT_SIMPLETYPE)) {
            Element grandKid = DOMUtil.getFirstChildElement(child);
            if (grandKid == null) {
                reportSchemaError("src-redefine.5.a.a", null, child);
                return;
            }
            if (DOMUtil.getLocalName(grandKid).equals(SchemaSymbols.ELT_ANNOTATION)) {
                grandKid = DOMUtil.getNextSiblingElement(grandKid);
            }
            if (grandKid == null) {
                reportSchemaError("src-redefine.5.a.a", null, child);
                return;
            }
            String grandKidName = DOMUtil.getLocalName(grandKid);
            if (!grandKidName.equals(SchemaSymbols.ELT_RESTRICTION)) {
                reportSchemaError("src-redefine.5.a.b", new Object[]{grandKidName}, child);
                return;
            }
            Object[] attrs = this.fAttributeChecker.checkAttributes(grandKid, false, currSchema);
            QName derivedBase = (QName) attrs[XSAttributeChecker.ATTIDX_BASE];
            if (derivedBase == null || derivedBase.uri != currSchema.fTargetNamespace || !derivedBase.localpart.equals(oldName)) {
                Object[] objArr = new Object[2];
                objArr[0] = grandKidName;
                objArr[1] = (currSchema.fTargetNamespace == null ? "" : currSchema.fTargetNamespace) + "," + oldName;
                reportSchemaError("src-redefine.5.a.c", objArr, child);
            } else if (derivedBase.prefix != null && derivedBase.prefix.length() > 0) {
                grandKid.setAttribute(SchemaSymbols.ATT_BASE, derivedBase.prefix + CallSiteDescriptor.TOKEN_DELIMITER + newName);
            } else {
                grandKid.setAttribute(SchemaSymbols.ATT_BASE, newName);
            }
            this.fAttributeChecker.returnAttrArray(attrs, currSchema);
            return;
        }
        if (componentType.equals(SchemaSymbols.ELT_COMPLEXTYPE)) {
            Element grandKid2 = DOMUtil.getFirstChildElement(child);
            if (grandKid2 == null) {
                reportSchemaError("src-redefine.5.b.a", null, child);
                return;
            }
            if (DOMUtil.getLocalName(grandKid2).equals(SchemaSymbols.ELT_ANNOTATION)) {
                grandKid2 = DOMUtil.getNextSiblingElement(grandKid2);
            }
            if (grandKid2 == null) {
                reportSchemaError("src-redefine.5.b.a", null, child);
                return;
            }
            Element greatGrandKid = DOMUtil.getFirstChildElement(grandKid2);
            if (greatGrandKid == null) {
                reportSchemaError("src-redefine.5.b.b", null, grandKid2);
                return;
            }
            if (DOMUtil.getLocalName(greatGrandKid).equals(SchemaSymbols.ELT_ANNOTATION)) {
                greatGrandKid = DOMUtil.getNextSiblingElement(greatGrandKid);
            }
            if (greatGrandKid == null) {
                reportSchemaError("src-redefine.5.b.b", null, grandKid2);
                return;
            }
            String greatGrandKidName = DOMUtil.getLocalName(greatGrandKid);
            if (!greatGrandKidName.equals(SchemaSymbols.ELT_RESTRICTION) && !greatGrandKidName.equals(SchemaSymbols.ELT_EXTENSION)) {
                reportSchemaError("src-redefine.5.b.c", new Object[]{greatGrandKidName}, greatGrandKid);
                return;
            }
            QName derivedBase2 = (QName) this.fAttributeChecker.checkAttributes(greatGrandKid, false, currSchema)[XSAttributeChecker.ATTIDX_BASE];
            if (derivedBase2 == null || derivedBase2.uri != currSchema.fTargetNamespace || !derivedBase2.localpart.equals(oldName)) {
                Object[] objArr2 = new Object[2];
                objArr2[0] = greatGrandKidName;
                objArr2[1] = (currSchema.fTargetNamespace == null ? "" : currSchema.fTargetNamespace) + "," + oldName;
                reportSchemaError("src-redefine.5.b.d", objArr2, greatGrandKid);
                return;
            }
            if (derivedBase2.prefix != null && derivedBase2.prefix.length() > 0) {
                greatGrandKid.setAttribute(SchemaSymbols.ATT_BASE, derivedBase2.prefix + CallSiteDescriptor.TOKEN_DELIMITER + newName);
                return;
            } else {
                greatGrandKid.setAttribute(SchemaSymbols.ATT_BASE, newName);
                return;
            }
        }
        if (componentType.equals(SchemaSymbols.ELT_ATTRIBUTEGROUP)) {
            String processedBaseName = currSchema.fTargetNamespace == null ? "," + oldName : currSchema.fTargetNamespace + "," + oldName;
            int attGroupRefsCount = changeRedefineGroup(processedBaseName, componentType, newName, child, currSchema);
            if (attGroupRefsCount > 1) {
                reportSchemaError("src-redefine.7.1", new Object[]{new Integer(attGroupRefsCount)}, child);
                return;
            } else {
                if (attGroupRefsCount != 1) {
                    if (currSchema.fTargetNamespace == null) {
                        this.fRedefinedRestrictedAttributeGroupRegistry.put(processedBaseName, "," + newName);
                        return;
                    } else {
                        this.fRedefinedRestrictedAttributeGroupRegistry.put(processedBaseName, currSchema.fTargetNamespace + "," + newName);
                        return;
                    }
                }
                return;
            }
        }
        if (componentType.equals(SchemaSymbols.ELT_GROUP)) {
            String processedBaseName2 = currSchema.fTargetNamespace == null ? "," + oldName : currSchema.fTargetNamespace + "," + oldName;
            int groupRefsCount = changeRedefineGroup(processedBaseName2, componentType, newName, child, currSchema);
            if (groupRefsCount > 1) {
                reportSchemaError("src-redefine.6.1.1", new Object[]{new Integer(groupRefsCount)}, child);
                return;
            } else {
                if (groupRefsCount != 1) {
                    if (currSchema.fTargetNamespace == null) {
                        this.fRedefinedRestrictedGroupRegistry.put(processedBaseName2, "," + newName);
                        return;
                    } else {
                        this.fRedefinedRestrictedGroupRegistry.put(processedBaseName2, currSchema.fTargetNamespace + "," + newName);
                        return;
                    }
                }
                return;
            }
        }
        reportSchemaError("Internal-Error", new Object[]{"could not handle this particular <redefine>; please submit your schemas and instance document in a bug report!"}, child);
    }

    private String findQName(String name, XSDocumentInfo schemaDoc) {
        SchemaNamespaceSupport currNSMap = schemaDoc.fNamespaceSupport;
        int colonPtr = name.indexOf(58);
        String prefix = XMLSymbols.EMPTY_STRING;
        if (colonPtr > 0) {
            prefix = name.substring(0, colonPtr);
        }
        String uri = currNSMap.getURI(this.fSymbolTable.addSymbol(prefix));
        String localpart = colonPtr == 0 ? name : name.substring(colonPtr + 1);
        if (prefix == XMLSymbols.EMPTY_STRING && uri == null && schemaDoc.fIsChameleonSchema) {
            uri = schemaDoc.fTargetNamespace;
        }
        if (uri == null) {
            return "," + localpart;
        }
        return uri + "," + localpart;
    }

    private int changeRedefineGroup(String originalQName, String elementSought, String newName, Element curr, XSDocumentInfo schemaDoc) throws DOMException, MissingResourceException, XNIException {
        int result = 0;
        Element firstChildElement = DOMUtil.getFirstChildElement(curr);
        while (true) {
            Element child = firstChildElement;
            if (child != null) {
                String name = DOMUtil.getLocalName(child);
                if (!name.equals(elementSought)) {
                    result += changeRedefineGroup(originalQName, elementSought, newName, child, schemaDoc);
                } else {
                    String ref = child.getAttribute(SchemaSymbols.ATT_REF);
                    if (ref.length() != 0) {
                        String processedRef = findQName(ref, schemaDoc);
                        if (originalQName.equals(processedRef)) {
                            String str = XMLSymbols.EMPTY_STRING;
                            int colonptr = ref.indexOf(CallSiteDescriptor.TOKEN_DELIMITER);
                            if (colonptr > 0) {
                                String prefix = ref.substring(0, colonptr);
                                child.setAttribute(SchemaSymbols.ATT_REF, prefix + CallSiteDescriptor.TOKEN_DELIMITER + newName);
                            } else {
                                child.setAttribute(SchemaSymbols.ATT_REF, newName);
                            }
                            result++;
                            if (elementSought.equals(SchemaSymbols.ELT_GROUP)) {
                                String minOccurs = child.getAttribute(SchemaSymbols.ATT_MINOCCURS);
                                String maxOccurs = child.getAttribute(SchemaSymbols.ATT_MAXOCCURS);
                                if ((maxOccurs.length() != 0 && !maxOccurs.equals("1")) || (minOccurs.length() != 0 && !minOccurs.equals("1"))) {
                                    reportSchemaError("src-redefine.6.1.2", new Object[]{ref}, child);
                                }
                            }
                        }
                    }
                }
                firstChildElement = DOMUtil.getNextSiblingElement(child);
            } else {
                return result;
            }
        }
    }

    private XSDocumentInfo findXSDocumentForDecl(XSDocumentInfo currSchema, Element decl, XSDocumentInfo decl_Doc) {
        if (decl_Doc == null) {
            return null;
        }
        XSDocumentInfo declDocInfo = decl_Doc;
        return declDocInfo;
    }

    private boolean nonAnnotationContent(Element elem) {
        Element firstChildElement = DOMUtil.getFirstChildElement(elem);
        while (true) {
            Element child = firstChildElement;
            if (child != null) {
                if (!DOMUtil.getLocalName(child).equals(SchemaSymbols.ELT_ANNOTATION)) {
                    return true;
                }
                firstChildElement = DOMUtil.getNextSiblingElement(child);
            } else {
                return false;
            }
        }
    }

    private void setSchemasVisible(XSDocumentInfo startSchema) {
        if (DOMUtil.isHidden(startSchema.fSchemaElement, this.fHiddenNodes)) {
            DOMUtil.setVisible(startSchema.fSchemaElement, this.fHiddenNodes);
            Vector<XSDocumentInfo> dependingSchemas = this.fDependencyMap.get(startSchema);
            for (int i2 = 0; i2 < dependingSchemas.size(); i2++) {
                setSchemasVisible(dependingSchemas.elementAt(i2));
            }
        }
    }

    public SimpleLocator element2Locator(Element e2) {
        if (!(e2 instanceof ElementImpl)) {
            return null;
        }
        SimpleLocator l2 = new SimpleLocator();
        if (element2Locator(e2, l2)) {
            return l2;
        }
        return null;
    }

    public boolean element2Locator(Element e2, SimpleLocator l2) {
        if (l2 != null && (e2 instanceof ElementImpl)) {
            ElementImpl ele = (ElementImpl) e2;
            Document doc = ele.getOwnerDocument();
            String sid = this.fDoc2SystemId.get(DOMUtil.getRoot(doc));
            int line = ele.getLineNumber();
            int column = ele.getColumnNumber();
            l2.setValues(sid, sid, line, column, ele.getCharacterOffset());
            return true;
        }
        return false;
    }

    private Element getElementFromMap(Map<String, Element> registry, String declKey) {
        if (registry == null) {
            return null;
        }
        return registry.get(declKey);
    }

    private XSDocumentInfo getDocInfoFromMap(Map<String, XSDocumentInfo> registry, String declKey) {
        if (registry == null) {
            return null;
        }
        return registry.get(declKey);
    }

    private Object getFromMap(Map registry, String key) {
        if (registry == null) {
            return null;
        }
        return registry.get(key);
    }

    void reportSchemaFatalError(String key, Object[] args, Element ele) throws MissingResourceException, XNIException {
        reportSchemaErr(key, args, ele, (short) 2, null);
    }

    void reportSchemaError(String key, Object[] args, Element ele) throws MissingResourceException, XNIException {
        reportSchemaErr(key, args, ele, (short) 1, null);
    }

    void reportSchemaError(String key, Object[] args, Element ele, Exception exception) throws MissingResourceException, XNIException {
        reportSchemaErr(key, args, ele, (short) 1, exception);
    }

    void reportSchemaWarning(String key, Object[] args, Element ele) throws MissingResourceException, XNIException {
        reportSchemaErr(key, args, ele, (short) 0, null);
    }

    void reportSchemaWarning(String key, Object[] args, Element ele, Exception exception) throws MissingResourceException, XNIException {
        reportSchemaErr(key, args, ele, (short) 0, exception);
    }

    void reportSchemaErr(String key, Object[] args, Element ele, short type, Exception exception) throws MissingResourceException, XNIException {
        if (element2Locator(ele, this.xl)) {
            this.fErrorReporter.reportError(this.xl, XSMessageFormatter.SCHEMA_DOMAIN, key, args, type, exception);
        } else {
            this.fErrorReporter.reportError(XSMessageFormatter.SCHEMA_DOMAIN, key, args, type, exception);
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/traversers/XSDHandler$XSAnnotationGrammarPool.class */
    private static class XSAnnotationGrammarPool implements XMLGrammarPool {
        private XSGrammarBucket fGrammarBucket;
        private Grammar[] fInitialGrammarSet;

        private XSAnnotationGrammarPool() {
        }

        @Override // com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool
        public Grammar[] retrieveInitialGrammarSet(String grammarType) {
            if (grammarType == "http://www.w3.org/2001/XMLSchema") {
                if (this.fInitialGrammarSet == null) {
                    if (this.fGrammarBucket == null) {
                        this.fInitialGrammarSet = new Grammar[]{SchemaGrammar.Schema4Annotations.INSTANCE};
                    } else {
                        SchemaGrammar[] schemaGrammars = this.fGrammarBucket.getGrammars();
                        for (SchemaGrammar schemaGrammar : schemaGrammars) {
                            if (SchemaSymbols.URI_SCHEMAFORSCHEMA.equals(schemaGrammar.getTargetNamespace())) {
                                this.fInitialGrammarSet = schemaGrammars;
                                return this.fInitialGrammarSet;
                            }
                        }
                        Grammar[] grammars = new Grammar[schemaGrammars.length + 1];
                        System.arraycopy(schemaGrammars, 0, grammars, 0, schemaGrammars.length);
                        grammars[grammars.length - 1] = SchemaGrammar.Schema4Annotations.INSTANCE;
                        this.fInitialGrammarSet = grammars;
                    }
                }
                return this.fInitialGrammarSet;
            }
            return new Grammar[0];
        }

        @Override // com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool
        public void cacheGrammars(String grammarType, Grammar[] grammars) {
        }

        @Override // com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool
        public Grammar retrieveGrammar(XMLGrammarDescription desc) {
            Grammar grammar;
            if (desc.getGrammarType() == "http://www.w3.org/2001/XMLSchema") {
                String tns = ((XMLSchemaDescription) desc).getTargetNamespace();
                if (this.fGrammarBucket != null && (grammar = this.fGrammarBucket.getGrammar(tns)) != null) {
                    return grammar;
                }
                if (SchemaSymbols.URI_SCHEMAFORSCHEMA.equals(tns)) {
                    return SchemaGrammar.Schema4Annotations.INSTANCE;
                }
                return null;
            }
            return null;
        }

        public void refreshGrammars(XSGrammarBucket gBucket) {
            this.fGrammarBucket = gBucket;
            this.fInitialGrammarSet = null;
        }

        @Override // com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool
        public void lockPool() {
        }

        @Override // com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool
        public void unlockPool() {
        }

        @Override // com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool
        public void clear() {
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/traversers/XSDHandler$XSDKey.class */
    private static class XSDKey {
        String systemId;
        short referType;
        String referNS;

        XSDKey(String systemId, short referType, String referNS) {
            this.systemId = systemId;
            this.referType = referType;
            this.referNS = referNS;
        }

        public int hashCode() {
            if (this.referNS == null) {
                return 0;
            }
            return this.referNS.hashCode();
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof XSDKey)) {
                return false;
            }
            XSDKey key = (XSDKey) obj;
            if (this.referNS != key.referNS || this.systemId == null || !this.systemId.equals(key.systemId)) {
                return false;
            }
            return true;
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/traversers/XSDHandler$SAX2XNIUtil.class */
    private static final class SAX2XNIUtil extends ErrorHandlerWrapper {
        private SAX2XNIUtil() {
        }

        public static XMLParseException createXMLParseException0(SAXParseException exception) {
            return createXMLParseException(exception);
        }

        public static XNIException createXNIException0(SAXException exception) {
            return createXNIException(exception);
        }
    }

    public void setGenerateSyntheticAnnotations(boolean state) {
        this.fSchemaParser.setFeature("http://apache.org/xml/features/generate-synthetic-annotations", state);
    }
}
