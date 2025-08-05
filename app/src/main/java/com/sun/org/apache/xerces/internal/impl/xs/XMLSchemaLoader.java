package com.sun.org.apache.xerces.internal.impl.xs;

import com.sun.org.apache.xerces.internal.dom.DOMErrorImpl;
import com.sun.org.apache.xerces.internal.dom.DOMMessageFormatter;
import com.sun.org.apache.xerces.internal.dom.DOMStringListImpl;
import com.sun.org.apache.xerces.internal.impl.Constants;
import com.sun.org.apache.xerces.internal.impl.XMLEntityManager;
import com.sun.org.apache.xerces.internal.impl.XMLErrorReporter;
import com.sun.org.apache.xerces.internal.impl.dv.InvalidDatatypeValueException;
import com.sun.org.apache.xerces.internal.impl.dv.SchemaDVFactory;
import com.sun.org.apache.xerces.internal.impl.dv.ValidatedInfo;
import com.sun.org.apache.xerces.internal.impl.dv.ValidationContext;
import com.sun.org.apache.xerces.internal.impl.dv.xs.SchemaDVFactoryImpl;
import com.sun.org.apache.xerces.internal.impl.xs.models.CMBuilder;
import com.sun.org.apache.xerces.internal.impl.xs.models.CMNodeFactory;
import com.sun.org.apache.xerces.internal.impl.xs.traversers.XSDHandler;
import com.sun.org.apache.xerces.internal.util.DOMEntityResolverWrapper;
import com.sun.org.apache.xerces.internal.util.DOMErrorHandlerWrapper;
import com.sun.org.apache.xerces.internal.util.DefaultErrorHandler;
import com.sun.org.apache.xerces.internal.util.ParserConfigurationSettings;
import com.sun.org.apache.xerces.internal.util.Status;
import com.sun.org.apache.xerces.internal.util.SymbolTable;
import com.sun.org.apache.xerces.internal.util.XMLSymbols;
import com.sun.org.apache.xerces.internal.utils.SecuritySupport;
import com.sun.org.apache.xerces.internal.utils.XMLSecurityManager;
import com.sun.org.apache.xerces.internal.utils.XMLSecurityPropertyManager;
import com.sun.org.apache.xerces.internal.xni.XNIException;
import com.sun.org.apache.xerces.internal.xni.grammars.Grammar;
import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarLoader;
import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool;
import com.sun.org.apache.xerces.internal.xni.grammars.XSGrammar;
import com.sun.org.apache.xerces.internal.xni.parser.XMLComponent;
import com.sun.org.apache.xerces.internal.xni.parser.XMLComponentManager;
import com.sun.org.apache.xerces.internal.xni.parser.XMLConfigurationException;
import com.sun.org.apache.xerces.internal.xni.parser.XMLEntityResolver;
import com.sun.org.apache.xerces.internal.xni.parser.XMLErrorHandler;
import com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;
import com.sun.org.apache.xerces.internal.xs.LSInputList;
import com.sun.org.apache.xerces.internal.xs.StringList;
import com.sun.org.apache.xerces.internal.xs.XSLoader;
import com.sun.org.apache.xerces.internal.xs.XSModel;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.StringTokenizer;
import java.util.Vector;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMErrorHandler;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMStringList;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.InputSource;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/XMLSchemaLoader.class */
public class XMLSchemaLoader implements XMLGrammarLoader, XMLComponent, XSLoader, DOMConfiguration {
    protected static final String GENERATE_SYNTHETIC_ANNOTATIONS = "http://apache.org/xml/features/generate-synthetic-annotations";
    protected static final String PARSER_SETTINGS = "http://apache.org/xml/features/internal/parser-settings";
    protected static final String OVERRIDE_PARSER = "jdk.xml.overrideDefaultParser";
    public static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
    public static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
    public static final String ENTITY_RESOLVER = "http://apache.org/xml/properties/internal/entity-resolver";
    public static final String XMLGRAMMAR_POOL = "http://apache.org/xml/properties/internal/grammar-pool";
    protected static final String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";
    protected static final String SECURITY_MANAGER = "http://apache.org/xml/properties/security-manager";
    protected static final String LOCALE = "http://apache.org/xml/properties/locale";
    private static final String XML_SECURITY_PROPERTY_MANAGER = "http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager";
    public static final String ACCESS_EXTERNAL_DTD = "http://javax.xml.XMLConstants/property/accessExternalDTD";
    public static final String ACCESS_EXTERNAL_SCHEMA = "http://javax.xml.XMLConstants/property/accessExternalSchema";
    private ParserConfigurationSettings fLoaderConfig;
    private SymbolTable fSymbolTable;
    private XMLErrorReporter fErrorReporter;
    private XMLEntityManager fEntityManager;
    private XMLEntityResolver fUserEntityResolver;
    private XMLGrammarPool fGrammarPool;
    private String fExternalSchemas;
    private String fExternalNoNSSchema;
    private Object fJAXPSource;
    private boolean fIsCheckedFully;
    private boolean fJAXPProcessed;
    private boolean fSettingsChanged;
    private XSDHandler fSchemaHandler;
    private XSGrammarBucket fGrammarBucket;
    private XSDeclarationPool fDeclPool;
    private SubstitutionGroupHandler fSubGroupHandler;
    private final CMNodeFactory fNodeFactory;
    private CMBuilder fCMBuilder;
    private XSDDescription fXSDDescription;
    private String faccessExternalSchema;
    private Map fJAXPCache;
    private Locale fLocale;
    private DOMStringList fRecognizedParameters;
    private DOMErrorHandlerWrapper fErrorHandler;
    private DOMEntityResolverWrapper fResourceResolver;
    protected static final String SCHEMA_FULL_CHECKING = "http://apache.org/xml/features/validation/schema-full-checking";
    protected static final String AUGMENT_PSVI = "http://apache.org/xml/features/validation/schema/augment-psvi";
    protected static final String CONTINUE_AFTER_FATAL_ERROR = "http://apache.org/xml/features/continue-after-fatal-error";
    protected static final String ALLOW_JAVA_ENCODINGS = "http://apache.org/xml/features/allow-java-encodings";
    protected static final String STANDARD_URI_CONFORMANT_FEATURE = "http://apache.org/xml/features/standard-uri-conformant";
    protected static final String DISALLOW_DOCTYPE = "http://apache.org/xml/features/disallow-doctype-decl";
    protected static final String VALIDATE_ANNOTATIONS = "http://apache.org/xml/features/validate-annotations";
    protected static final String HONOUR_ALL_SCHEMALOCATIONS = "http://apache.org/xml/features/honour-all-schemaLocations";
    protected static final String NAMESPACE_GROWTH = "http://apache.org/xml/features/namespace-growth";
    protected static final String TOLERATE_DUPLICATES = "http://apache.org/xml/features/internal/tolerate-duplicates";
    private static final String[] RECOGNIZED_FEATURES = {SCHEMA_FULL_CHECKING, AUGMENT_PSVI, CONTINUE_AFTER_FATAL_ERROR, ALLOW_JAVA_ENCODINGS, STANDARD_URI_CONFORMANT_FEATURE, DISALLOW_DOCTYPE, "http://apache.org/xml/features/generate-synthetic-annotations", VALIDATE_ANNOTATIONS, HONOUR_ALL_SCHEMALOCATIONS, NAMESPACE_GROWTH, TOLERATE_DUPLICATES, "jdk.xml.overrideDefaultParser"};
    protected static final String ENTITY_MANAGER = "http://apache.org/xml/properties/internal/entity-manager";
    protected static final String ERROR_HANDLER = "http://apache.org/xml/properties/internal/error-handler";
    protected static final String SCHEMA_LOCATION = "http://apache.org/xml/properties/schema/external-schemaLocation";
    protected static final String SCHEMA_NONS_LOCATION = "http://apache.org/xml/properties/schema/external-noNamespaceSchemaLocation";
    protected static final String SCHEMA_DV_FACTORY = "http://apache.org/xml/properties/internal/validation/schema/dv-factory";
    private static final String[] RECOGNIZED_PROPERTIES = {ENTITY_MANAGER, "http://apache.org/xml/properties/internal/symbol-table", "http://apache.org/xml/properties/internal/error-reporter", ERROR_HANDLER, "http://apache.org/xml/properties/internal/entity-resolver", "http://apache.org/xml/properties/internal/grammar-pool", SCHEMA_LOCATION, SCHEMA_NONS_LOCATION, "http://java.sun.com/xml/jaxp/properties/schemaSource", "http://apache.org/xml/properties/security-manager", "http://apache.org/xml/properties/locale", SCHEMA_DV_FACTORY, "http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager"};

    public XMLSchemaLoader() {
        this(new SymbolTable(), null, new XMLEntityManager(), null, null, null);
    }

    public XMLSchemaLoader(SymbolTable symbolTable) {
        this(symbolTable, null, new XMLEntityManager(), null, null, null);
    }

    XMLSchemaLoader(XMLErrorReporter errorReporter, XSGrammarBucket grammarBucket, SubstitutionGroupHandler sHandler, CMBuilder builder) {
        this(null, errorReporter, null, grammarBucket, sHandler, builder);
    }

    XMLSchemaLoader(SymbolTable symbolTable, XMLErrorReporter errorReporter, XMLEntityManager entityResolver, XSGrammarBucket grammarBucket, SubstitutionGroupHandler sHandler, CMBuilder builder) throws XMLConfigurationException {
        this.fLoaderConfig = new ParserConfigurationSettings();
        this.fSymbolTable = null;
        this.fErrorReporter = new XMLErrorReporter();
        this.fEntityManager = null;
        this.fUserEntityResolver = null;
        this.fGrammarPool = null;
        this.fExternalSchemas = null;
        this.fExternalNoNSSchema = null;
        this.fJAXPSource = null;
        this.fIsCheckedFully = false;
        this.fJAXPProcessed = false;
        this.fSettingsChanged = true;
        this.fDeclPool = null;
        this.fNodeFactory = new CMNodeFactory();
        this.fXSDDescription = new XSDDescription();
        this.faccessExternalSchema = "all";
        this.fLocale = Locale.getDefault();
        this.fRecognizedParameters = null;
        this.fErrorHandler = null;
        this.fResourceResolver = null;
        this.fLoaderConfig.addRecognizedFeatures(RECOGNIZED_FEATURES);
        this.fLoaderConfig.addRecognizedProperties(RECOGNIZED_PROPERTIES);
        if (symbolTable != null) {
            this.fLoaderConfig.setProperty("http://apache.org/xml/properties/internal/symbol-table", symbolTable);
        }
        if (errorReporter == null) {
            errorReporter = new XMLErrorReporter();
            errorReporter.setLocale(this.fLocale);
            errorReporter.setProperty(ERROR_HANDLER, new DefaultErrorHandler());
        }
        this.fErrorReporter = errorReporter;
        if (this.fErrorReporter.getMessageFormatter(XSMessageFormatter.SCHEMA_DOMAIN) == null) {
            this.fErrorReporter.putMessageFormatter(XSMessageFormatter.SCHEMA_DOMAIN, new XSMessageFormatter());
        }
        this.fLoaderConfig.setProperty("http://apache.org/xml/properties/internal/error-reporter", this.fErrorReporter);
        this.fEntityManager = entityResolver;
        if (this.fEntityManager != null) {
            this.fLoaderConfig.setProperty(ENTITY_MANAGER, this.fEntityManager);
        }
        this.fLoaderConfig.setFeature(AUGMENT_PSVI, true);
        this.fGrammarBucket = grammarBucket == null ? new XSGrammarBucket() : grammarBucket;
        this.fSubGroupHandler = sHandler == null ? new SubstitutionGroupHandler(this.fGrammarBucket) : sHandler;
        this.fCMBuilder = builder == null ? new CMBuilder(this.fNodeFactory) : builder;
        this.fSchemaHandler = new XSDHandler(this.fGrammarBucket);
        if (this.fDeclPool != null) {
            this.fDeclPool.reset();
        }
        this.fJAXPCache = new HashMap();
        this.fSettingsChanged = true;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarLoader, com.sun.org.apache.xerces.internal.xni.parser.XMLComponent
    public String[] getRecognizedFeatures() {
        return (String[]) RECOGNIZED_FEATURES.clone();
    }

    @Override // com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarLoader
    public boolean getFeature(String featureId) throws XMLConfigurationException {
        return this.fLoaderConfig.getFeature(featureId);
    }

    @Override // com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarLoader, com.sun.org.apache.xerces.internal.xni.parser.XMLComponent
    public void setFeature(String featureId, boolean state) throws XMLConfigurationException {
        this.fSettingsChanged = true;
        if (featureId.equals(CONTINUE_AFTER_FATAL_ERROR)) {
            this.fErrorReporter.setFeature(CONTINUE_AFTER_FATAL_ERROR, state);
        } else if (featureId.equals("http://apache.org/xml/features/generate-synthetic-annotations")) {
            this.fSchemaHandler.setGenerateSyntheticAnnotations(state);
        }
        this.fLoaderConfig.setFeature(featureId, state);
    }

    @Override // com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarLoader, com.sun.org.apache.xerces.internal.xni.parser.XMLComponent
    public String[] getRecognizedProperties() {
        return (String[]) RECOGNIZED_PROPERTIES.clone();
    }

    @Override // com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarLoader
    public Object getProperty(String propertyId) throws XMLConfigurationException {
        return this.fLoaderConfig.getProperty(propertyId);
    }

    @Override // com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarLoader, com.sun.org.apache.xerces.internal.xni.parser.XMLComponent
    public void setProperty(String propertyId, Object state) throws XMLConfigurationException {
        this.fSettingsChanged = true;
        this.fLoaderConfig.setProperty(propertyId, state);
        if (propertyId.equals("http://java.sun.com/xml/jaxp/properties/schemaSource")) {
            this.fJAXPSource = state;
            this.fJAXPProcessed = false;
            return;
        }
        if (propertyId.equals("http://apache.org/xml/properties/internal/grammar-pool")) {
            this.fGrammarPool = (XMLGrammarPool) state;
            return;
        }
        if (propertyId.equals(SCHEMA_LOCATION)) {
            this.fExternalSchemas = (String) state;
            return;
        }
        if (propertyId.equals(SCHEMA_NONS_LOCATION)) {
            this.fExternalNoNSSchema = (String) state;
            return;
        }
        if (propertyId.equals("http://apache.org/xml/properties/locale")) {
            setLocale((Locale) state);
            return;
        }
        if (propertyId.equals("http://apache.org/xml/properties/internal/entity-resolver")) {
            this.fEntityManager.setProperty("http://apache.org/xml/properties/internal/entity-resolver", state);
            return;
        }
        if (propertyId.equals("http://apache.org/xml/properties/internal/error-reporter")) {
            this.fErrorReporter = (XMLErrorReporter) state;
            if (this.fErrorReporter.getMessageFormatter(XSMessageFormatter.SCHEMA_DOMAIN) == null) {
                this.fErrorReporter.putMessageFormatter(XSMessageFormatter.SCHEMA_DOMAIN, new XSMessageFormatter());
                return;
            }
            return;
        }
        if (propertyId.equals("http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager")) {
            XMLSecurityPropertyManager spm = (XMLSecurityPropertyManager) state;
            this.faccessExternalSchema = spm.getValue(XMLSecurityPropertyManager.Property.ACCESS_EXTERNAL_SCHEMA);
        }
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
    public void setEntityResolver(XMLEntityResolver entityResolver) throws XMLConfigurationException {
        this.fUserEntityResolver = entityResolver;
        this.fLoaderConfig.setProperty("http://apache.org/xml/properties/internal/entity-resolver", entityResolver);
        this.fEntityManager.setProperty("http://apache.org/xml/properties/internal/entity-resolver", entityResolver);
    }

    @Override // com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarLoader
    public XMLEntityResolver getEntityResolver() {
        return this.fUserEntityResolver;
    }

    public void loadGrammar(XMLInputSource[] source) throws IOException, XNIException {
        for (XMLInputSource xMLInputSource : source) {
            loadGrammar(xMLInputSource);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarLoader
    public Grammar loadGrammar(XMLInputSource source) throws IOException, XNIException {
        reset(this.fLoaderConfig);
        this.fSettingsChanged = false;
        XSDDescription desc = new XSDDescription();
        desc.fContextType = (short) 3;
        desc.setBaseSystemId(source.getBaseSystemId());
        desc.setLiteralSystemId(source.getSystemId());
        Map locationPairs = new HashMap();
        processExternalHints(this.fExternalSchemas, this.fExternalNoNSSchema, locationPairs, this.fErrorReporter);
        SchemaGrammar grammar = loadSchema(desc, source, locationPairs);
        if (grammar != null && this.fGrammarPool != null) {
            this.fGrammarPool.cacheGrammars("http://www.w3.org/2001/XMLSchema", this.fGrammarBucket.getGrammars());
            if (this.fIsCheckedFully && this.fJAXPCache.get(grammar) != grammar) {
                XSConstraints.fullSchemaChecking(this.fGrammarBucket, this.fSubGroupHandler, this.fCMBuilder, this.fErrorReporter);
            }
        }
        return grammar;
    }

    SchemaGrammar loadSchema(XSDDescription desc, XMLInputSource source, Map<String, LocationArray> locationPairs) throws IOException, XNIException {
        String accessError;
        if (!this.fJAXPProcessed) {
            processJAXPSchemaSource(locationPairs);
        }
        if (desc.isExternal() && (accessError = SecuritySupport.checkAccess(desc.getExpandedSystemId(), this.faccessExternalSchema, "all")) != null) {
            throw new XNIException(this.fErrorReporter.reportError(XSMessageFormatter.SCHEMA_DOMAIN, "schema_reference.access", new Object[]{SecuritySupport.sanitizePath(desc.getExpandedSystemId()), accessError}, (short) 1));
        }
        SchemaGrammar grammar = this.fSchemaHandler.parseSchema(source, desc, locationPairs);
        return grammar;
    }

    public static XMLInputSource resolveDocument(XSDDescription desc, Map<String, LocationArray> locationPairs, XMLEntityResolver entityResolver) throws IOException {
        String[] hints;
        String loc = null;
        if (desc.getContextType() == 2 || desc.fromInstance()) {
            String namespace = desc.getTargetNamespace();
            String ns = namespace == null ? XMLSymbols.EMPTY_STRING : namespace;
            LocationArray tempLA = locationPairs.get(ns);
            if (tempLA != null) {
                loc = tempLA.getFirstLocation();
            }
        }
        if (loc == null && (hints = desc.getLocationHints()) != null && hints.length > 0) {
            loc = hints[0];
        }
        String expandedLoc = XMLEntityManager.expandSystemId(loc, desc.getBaseSystemId(), false);
        desc.setLiteralSystemId(loc);
        desc.setExpandedSystemId(expandedLoc);
        return entityResolver.resolveEntity(desc);
    }

    public static void processExternalHints(String sl, String nsl, Map<String, LocationArray> locations, XMLErrorReporter er) {
        if (sl != null) {
            try {
                XSAttributeDecl attrDecl = SchemaGrammar.SG_XSI.getGlobalAttributeDecl(SchemaSymbols.XSI_SCHEMALOCATION);
                attrDecl.fType.validate(sl, (ValidationContext) null, (ValidatedInfo) null);
                if (!tokenizeSchemaLocationStr(sl, locations)) {
                    er.reportError(XSMessageFormatter.SCHEMA_DOMAIN, "SchemaLocation", new Object[]{sl}, (short) 0);
                }
            } catch (InvalidDatatypeValueException ex) {
                er.reportError(XSMessageFormatter.SCHEMA_DOMAIN, ex.getKey(), ex.getArgs(), (short) 0);
            }
        }
        if (nsl != null) {
            try {
                XSAttributeDecl attrDecl2 = SchemaGrammar.SG_XSI.getGlobalAttributeDecl(SchemaSymbols.XSI_NONAMESPACESCHEMALOCATION);
                attrDecl2.fType.validate(nsl, (ValidationContext) null, (ValidatedInfo) null);
                LocationArray la = locations.get(XMLSymbols.EMPTY_STRING);
                if (la == null) {
                    la = new LocationArray();
                    locations.put(XMLSymbols.EMPTY_STRING, la);
                }
                la.addLocation(nsl);
            } catch (InvalidDatatypeValueException ex2) {
                er.reportError(XSMessageFormatter.SCHEMA_DOMAIN, ex2.getKey(), ex2.getArgs(), (short) 0);
            }
        }
    }

    public static boolean tokenizeSchemaLocationStr(String schemaStr, Map<String, LocationArray> locations) {
        if (schemaStr != null) {
            StringTokenizer t2 = new StringTokenizer(schemaStr, " \n\t\r");
            while (t2.hasMoreTokens()) {
                String namespace = t2.nextToken();
                if (!t2.hasMoreTokens()) {
                    return false;
                }
                String location = t2.nextToken();
                LocationArray la = locations.get(namespace);
                if (la == null) {
                    la = new LocationArray();
                    locations.put(namespace, la);
                }
                la.addLocation(location);
            }
            return true;
        }
        return true;
    }

    private void processJAXPSchemaSource(Map<String, LocationArray> locationPairs) throws DOMException, MissingResourceException, IOException, XNIException {
        SchemaGrammar g2;
        SchemaGrammar g3;
        this.fJAXPProcessed = true;
        if (this.fJAXPSource == null) {
            return;
        }
        Class componentType = this.fJAXPSource.getClass().getComponentType();
        if (componentType == null) {
            if (((this.fJAXPSource instanceof InputStream) || (this.fJAXPSource instanceof InputSource)) && (g3 = (SchemaGrammar) this.fJAXPCache.get(this.fJAXPSource)) != null) {
                this.fGrammarBucket.putGrammar(g3);
                return;
            }
            this.fXSDDescription.reset();
            XMLInputSource xis = xsdToXMLInputSource(this.fJAXPSource);
            String sid = xis.getSystemId();
            this.fXSDDescription.fContextType = (short) 3;
            if (sid != null) {
                this.fXSDDescription.setBaseSystemId(xis.getBaseSystemId());
                this.fXSDDescription.setLiteralSystemId(sid);
                this.fXSDDescription.setExpandedSystemId(sid);
                this.fXSDDescription.fLocationHints = new String[]{sid};
            }
            SchemaGrammar g4 = loadSchema(this.fXSDDescription, xis, locationPairs);
            if (g4 != null) {
                if ((this.fJAXPSource instanceof InputStream) || (this.fJAXPSource instanceof InputSource)) {
                    this.fJAXPCache.put(this.fJAXPSource, g4);
                    if (this.fIsCheckedFully) {
                        XSConstraints.fullSchemaChecking(this.fGrammarBucket, this.fSubGroupHandler, this.fCMBuilder, this.fErrorReporter);
                    }
                }
                this.fGrammarBucket.putGrammar(g4);
                return;
            }
            return;
        }
        if (componentType != Object.class && componentType != String.class && componentType != File.class && componentType != InputStream.class && componentType != InputSource.class) {
            throw new XMLConfigurationException(Status.NOT_SUPPORTED, "\"http://java.sun.com/xml/jaxp/properties/schemaSource\" property cannot have an array of type {" + componentType.getName() + "}. Possible types of the array supported are Object, String, File, InputStream, InputSource.");
        }
        Object[] objArr = (Object[]) this.fJAXPSource;
        Vector jaxpSchemaSourceNamespaces = new Vector();
        for (int i2 = 0; i2 < objArr.length; i2++) {
            if (((objArr[i2] instanceof InputStream) || (objArr[i2] instanceof InputSource)) && (g2 = (SchemaGrammar) this.fJAXPCache.get(objArr[i2])) != null) {
                this.fGrammarBucket.putGrammar(g2);
            } else {
                this.fXSDDescription.reset();
                XMLInputSource xis2 = xsdToXMLInputSource(objArr[i2]);
                String sid2 = xis2.getSystemId();
                this.fXSDDescription.fContextType = (short) 3;
                if (sid2 != null) {
                    this.fXSDDescription.setBaseSystemId(xis2.getBaseSystemId());
                    this.fXSDDescription.setLiteralSystemId(sid2);
                    this.fXSDDescription.setExpandedSystemId(sid2);
                    this.fXSDDescription.fLocationHints = new String[]{sid2};
                }
                SchemaGrammar grammar = this.fSchemaHandler.parseSchema(xis2, this.fXSDDescription, locationPairs);
                if (this.fIsCheckedFully) {
                    XSConstraints.fullSchemaChecking(this.fGrammarBucket, this.fSubGroupHandler, this.fCMBuilder, this.fErrorReporter);
                }
                if (grammar != null) {
                    String targetNamespace = grammar.getTargetNamespace();
                    if (jaxpSchemaSourceNamespaces.contains(targetNamespace)) {
                        throw new IllegalArgumentException(" When using array of Objects as the value of SCHEMA_SOURCE property , no two Schemas should share the same targetNamespace. ");
                    }
                    jaxpSchemaSourceNamespaces.add(targetNamespace);
                    if ((objArr[i2] instanceof InputStream) || (objArr[i2] instanceof InputSource)) {
                        this.fJAXPCache.put(objArr[i2], grammar);
                    }
                    this.fGrammarBucket.putGrammar(grammar);
                } else {
                    continue;
                }
            }
        }
    }

    private XMLInputSource xsdToXMLInputSource(Object val) throws XNIException {
        if (val instanceof String) {
            String loc = (String) val;
            this.fXSDDescription.reset();
            this.fXSDDescription.setValues(null, loc, null, null);
            XMLInputSource xis = null;
            try {
                xis = this.fEntityManager.resolveEntity(this.fXSDDescription);
            } catch (IOException e2) {
                this.fErrorReporter.reportError(XSMessageFormatter.SCHEMA_DOMAIN, "schema_reference.4", new Object[]{loc}, (short) 1);
            }
            if (xis == null) {
                return new XMLInputSource(null, loc, null);
            }
            return xis;
        }
        if (val instanceof InputSource) {
            return saxToXMLInputSource((InputSource) val);
        }
        if (val instanceof InputStream) {
            return new XMLInputSource((String) null, (String) null, (String) null, (InputStream) val, (String) null);
        }
        if (val instanceof File) {
            File file = (File) val;
            InputStream is = null;
            try {
                is = new BufferedInputStream(new FileInputStream(file));
            } catch (FileNotFoundException e3) {
                this.fErrorReporter.reportError(XSMessageFormatter.SCHEMA_DOMAIN, "schema_reference.4", new Object[]{file.toString()}, (short) 1);
            }
            return new XMLInputSource((String) null, (String) null, (String) null, is, (String) null);
        }
        throw new XMLConfigurationException(Status.NOT_SUPPORTED, "\"http://java.sun.com/xml/jaxp/properties/schemaSource\" property cannot have a value of type {" + val.getClass().getName() + "}. Possible types of the value supported are String, File, InputStream, InputSource OR an array of these types.");
    }

    private static XMLInputSource saxToXMLInputSource(InputSource sis) {
        String publicId = sis.getPublicId();
        String systemId = sis.getSystemId();
        Reader charStream = sis.getCharacterStream();
        if (charStream != null) {
            return new XMLInputSource(publicId, systemId, (String) null, charStream, (String) null);
        }
        InputStream byteStream = sis.getByteStream();
        if (byteStream != null) {
            return new XMLInputSource(publicId, systemId, (String) null, byteStream, sis.getEncoding());
        }
        return new XMLInputSource(publicId, systemId, null);
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/XMLSchemaLoader$LocationArray.class */
    public static class LocationArray {
        int length;
        String[] locations = new String[2];

        public void resize(int oldLength, int newLength) {
            String[] temp = new String[newLength];
            System.arraycopy(this.locations, 0, temp, 0, Math.min(oldLength, newLength));
            this.locations = temp;
            this.length = Math.min(oldLength, newLength);
        }

        public void addLocation(String location) {
            if (this.length >= this.locations.length) {
                resize(this.length, Math.max(1, this.length * 2));
            }
            String[] strArr = this.locations;
            int i2 = this.length;
            this.length = i2 + 1;
            strArr[i2] = location;
        }

        public String[] getLocationArray() {
            if (this.length < this.locations.length) {
                resize(this.locations.length, this.length);
            }
            return this.locations;
        }

        public String getFirstLocation() {
            if (this.length > 0) {
                return this.locations[0];
            }
            return null;
        }

        public int getLength() {
            return this.length;
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLComponent
    public Boolean getFeatureDefault(String featureId) {
        if (featureId.equals(AUGMENT_PSVI)) {
            return Boolean.TRUE;
        }
        return null;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLComponent
    public Object getPropertyDefault(String propertyId) {
        return null;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLComponent
    public void reset(XMLComponentManager componentManager) throws XMLConfigurationException {
        XMLSecurityPropertyManager spm = (XMLSecurityPropertyManager) componentManager.getProperty("http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager");
        if (spm == null) {
            spm = new XMLSecurityPropertyManager();
            setProperty("http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager", spm);
        }
        XMLSecurityManager sm = (XMLSecurityManager) componentManager.getProperty("http://apache.org/xml/properties/security-manager");
        if (sm == null) {
            setProperty("http://apache.org/xml/properties/security-manager", new XMLSecurityManager(true));
        }
        this.faccessExternalSchema = spm.getValue(XMLSecurityPropertyManager.Property.ACCESS_EXTERNAL_SCHEMA);
        this.fGrammarBucket.reset();
        this.fSubGroupHandler.reset();
        boolean parser_settings = componentManager.getFeature(PARSER_SETTINGS, true);
        if (!parser_settings || !this.fSettingsChanged) {
            this.fJAXPProcessed = false;
            initGrammarBucket();
            return;
        }
        this.fNodeFactory.reset(componentManager);
        this.fEntityManager = (XMLEntityManager) componentManager.getProperty(ENTITY_MANAGER);
        this.fErrorReporter = (XMLErrorReporter) componentManager.getProperty("http://apache.org/xml/properties/internal/error-reporter");
        SchemaDVFactory dvFactory = this.fSchemaHandler.getDVFactory();
        if (dvFactory == null) {
            dvFactory = SchemaDVFactory.getInstance();
            this.fSchemaHandler.setDVFactory(dvFactory);
        }
        boolean psvi = componentManager.getFeature(AUGMENT_PSVI, false);
        if (!psvi) {
            if (this.fDeclPool != null) {
                this.fDeclPool.reset();
            } else {
                this.fDeclPool = new XSDeclarationPool();
            }
            this.fCMBuilder.setDeclPool(this.fDeclPool);
            this.fSchemaHandler.setDeclPool(this.fDeclPool);
            if (dvFactory instanceof SchemaDVFactoryImpl) {
                this.fDeclPool.setDVFactory((SchemaDVFactoryImpl) dvFactory);
                ((SchemaDVFactoryImpl) dvFactory).setDeclPool(this.fDeclPool);
            }
        } else {
            this.fCMBuilder.setDeclPool(null);
            this.fSchemaHandler.setDeclPool(null);
        }
        try {
            this.fExternalSchemas = (String) componentManager.getProperty(SCHEMA_LOCATION);
            this.fExternalNoNSSchema = (String) componentManager.getProperty(SCHEMA_NONS_LOCATION);
        } catch (XMLConfigurationException e2) {
            this.fExternalSchemas = null;
            this.fExternalNoNSSchema = null;
        }
        this.fJAXPSource = componentManager.getProperty("http://java.sun.com/xml/jaxp/properties/schemaSource", null);
        this.fJAXPProcessed = false;
        this.fGrammarPool = (XMLGrammarPool) componentManager.getProperty("http://apache.org/xml/properties/internal/grammar-pool", null);
        initGrammarBucket();
        try {
            boolean fatalError = componentManager.getFeature(CONTINUE_AFTER_FATAL_ERROR, false);
            if (!fatalError) {
                this.fErrorReporter.setFeature(CONTINUE_AFTER_FATAL_ERROR, fatalError);
            }
        } catch (XMLConfigurationException e3) {
        }
        this.fIsCheckedFully = componentManager.getFeature(SCHEMA_FULL_CHECKING, false);
        this.fSchemaHandler.setGenerateSyntheticAnnotations(componentManager.getFeature("http://apache.org/xml/features/generate-synthetic-annotations", false));
        this.fSchemaHandler.reset(componentManager);
    }

    private void initGrammarBucket() throws XNIException {
        if (this.fGrammarPool != null) {
            Grammar[] initialGrammars = this.fGrammarPool.retrieveInitialGrammarSet("http://www.w3.org/2001/XMLSchema");
            for (Grammar grammar : initialGrammars) {
                if (!this.fGrammarBucket.putGrammar((SchemaGrammar) grammar, true)) {
                    this.fErrorReporter.reportError(XSMessageFormatter.SCHEMA_DOMAIN, "GrammarConflict", null, (short) 0);
                }
            }
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSLoader
    public DOMConfiguration getConfig() {
        return this;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSLoader
    public XSModel load(LSInput is) {
        try {
            Grammar g2 = loadGrammar(dom2xmlInputSource(is));
            return ((XSGrammar) g2).toXSModel();
        } catch (Exception e2) {
            reportDOMFatalError(e2);
            return null;
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSLoader
    public XSModel loadInputList(LSInputList is) {
        int length = is.getLength();
        SchemaGrammar[] gs = new SchemaGrammar[length];
        for (int i2 = 0; i2 < length; i2++) {
            try {
                gs[i2] = (SchemaGrammar) loadGrammar(dom2xmlInputSource(is.item(i2)));
            } catch (Exception e2) {
                reportDOMFatalError(e2);
                return null;
            }
        }
        return new XSModelImpl(gs);
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSLoader
    public XSModel loadURI(String uri) {
        try {
            Grammar g2 = loadGrammar(new XMLInputSource(null, uri, null));
            return ((XSGrammar) g2).toXSModel();
        } catch (Exception e2) {
            reportDOMFatalError(e2);
            return null;
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSLoader
    public XSModel loadURIList(StringList uriList) {
        int length = uriList.getLength();
        SchemaGrammar[] gs = new SchemaGrammar[length];
        for (int i2 = 0; i2 < length; i2++) {
            try {
                gs[i2] = (SchemaGrammar) loadGrammar(new XMLInputSource(null, uriList.item(i2), null));
            } catch (Exception e2) {
                reportDOMFatalError(e2);
                return null;
            }
        }
        return new XSModelImpl(gs);
    }

    void reportDOMFatalError(Exception e2) {
        if (this.fErrorHandler != null) {
            DOMErrorImpl error = new DOMErrorImpl();
            error.fException = e2;
            error.fMessage = e2.getMessage();
            error.fSeverity = (short) 3;
            this.fErrorHandler.getErrorHandler().handleError(error);
        }
    }

    @Override // org.w3c.dom.DOMConfiguration
    public boolean canSetParameter(String name, Object value) {
        if (value instanceof Boolean) {
            if (name.equals(Constants.DOM_VALIDATE) || name.equals(SCHEMA_FULL_CHECKING) || name.equals(VALIDATE_ANNOTATIONS) || name.equals(CONTINUE_AFTER_FATAL_ERROR) || name.equals(ALLOW_JAVA_ENCODINGS) || name.equals(STANDARD_URI_CONFORMANT_FEATURE) || name.equals("http://apache.org/xml/features/generate-synthetic-annotations") || name.equals(HONOUR_ALL_SCHEMALOCATIONS) || name.equals(NAMESPACE_GROWTH) || name.equals(TOLERATE_DUPLICATES) || name.equals("jdk.xml.overrideDefaultParser")) {
                return true;
            }
            return false;
        }
        if (name.equals(Constants.DOM_ERROR_HANDLER) || name.equals(Constants.DOM_RESOURCE_RESOLVER) || name.equals("http://apache.org/xml/properties/internal/symbol-table") || name.equals("http://apache.org/xml/properties/internal/error-reporter") || name.equals(ERROR_HANDLER) || name.equals("http://apache.org/xml/properties/internal/entity-resolver") || name.equals("http://apache.org/xml/properties/internal/grammar-pool") || name.equals(SCHEMA_LOCATION) || name.equals(SCHEMA_NONS_LOCATION) || name.equals("http://java.sun.com/xml/jaxp/properties/schemaSource") || name.equals(SCHEMA_DV_FACTORY)) {
            return true;
        }
        return false;
    }

    @Override // org.w3c.dom.DOMConfiguration
    public Object getParameter(String name) throws DOMException, MissingResourceException {
        if (name.equals(Constants.DOM_ERROR_HANDLER)) {
            if (this.fErrorHandler != null) {
                return this.fErrorHandler.getErrorHandler();
            }
            return null;
        }
        if (name.equals(Constants.DOM_RESOURCE_RESOLVER)) {
            if (this.fResourceResolver != null) {
                return this.fResourceResolver.getEntityResolver();
            }
            return null;
        }
        try {
            boolean feature = getFeature(name);
            return feature ? Boolean.TRUE : Boolean.FALSE;
        } catch (Exception e2) {
            try {
                Object property = getProperty(name);
                return property;
            } catch (Exception e3) {
                String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "FEATURE_NOT_SUPPORTED", new Object[]{name});
                throw new DOMException((short) 9, msg);
            }
        }
    }

    @Override // org.w3c.dom.DOMConfiguration
    public DOMStringList getParameterNames() {
        if (this.fRecognizedParameters == null) {
            Vector v2 = new Vector();
            v2.add(Constants.DOM_VALIDATE);
            v2.add(Constants.DOM_ERROR_HANDLER);
            v2.add(Constants.DOM_RESOURCE_RESOLVER);
            v2.add("http://apache.org/xml/properties/internal/symbol-table");
            v2.add("http://apache.org/xml/properties/internal/error-reporter");
            v2.add(ERROR_HANDLER);
            v2.add("http://apache.org/xml/properties/internal/entity-resolver");
            v2.add("http://apache.org/xml/properties/internal/grammar-pool");
            v2.add(SCHEMA_LOCATION);
            v2.add(SCHEMA_NONS_LOCATION);
            v2.add("http://java.sun.com/xml/jaxp/properties/schemaSource");
            v2.add(SCHEMA_FULL_CHECKING);
            v2.add(CONTINUE_AFTER_FATAL_ERROR);
            v2.add(ALLOW_JAVA_ENCODINGS);
            v2.add(STANDARD_URI_CONFORMANT_FEATURE);
            v2.add(VALIDATE_ANNOTATIONS);
            v2.add("http://apache.org/xml/features/generate-synthetic-annotations");
            v2.add(HONOUR_ALL_SCHEMALOCATIONS);
            v2.add(NAMESPACE_GROWTH);
            v2.add(TOLERATE_DUPLICATES);
            v2.add("jdk.xml.overrideDefaultParser");
            this.fRecognizedParameters = new DOMStringListImpl(v2);
        }
        return this.fRecognizedParameters;
    }

    @Override // org.w3c.dom.DOMConfiguration
    public void setParameter(String name, Object value) throws DOMException {
        if (value instanceof Boolean) {
            boolean state = ((Boolean) value).booleanValue();
            if (name.equals(Constants.DOM_VALIDATE) && state) {
                return;
            }
            try {
                setFeature(name, state);
                return;
            } catch (Exception e2) {
                String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "FEATURE_NOT_SUPPORTED", new Object[]{name});
                throw new DOMException((short) 9, msg);
            }
        }
        if (name.equals(Constants.DOM_ERROR_HANDLER)) {
            if (value instanceof DOMErrorHandler) {
                try {
                    this.fErrorHandler = new DOMErrorHandlerWrapper((DOMErrorHandler) value);
                    setErrorHandler(this.fErrorHandler);
                    return;
                } catch (XMLConfigurationException e3) {
                    return;
                }
            }
            String msg2 = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "FEATURE_NOT_SUPPORTED", new Object[]{name});
            throw new DOMException((short) 9, msg2);
        }
        if (name.equals(Constants.DOM_RESOURCE_RESOLVER)) {
            if (value instanceof LSResourceResolver) {
                try {
                    this.fResourceResolver = new DOMEntityResolverWrapper((LSResourceResolver) value);
                    setEntityResolver(this.fResourceResolver);
                    return;
                } catch (XMLConfigurationException e4) {
                    return;
                }
            }
            String msg3 = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "FEATURE_NOT_SUPPORTED", new Object[]{name});
            throw new DOMException((short) 9, msg3);
        }
        try {
            setProperty(name, value);
        } catch (Exception e5) {
            String msg4 = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "FEATURE_NOT_SUPPORTED", new Object[]{name});
            throw new DOMException((short) 9, msg4);
        }
    }

    XMLInputSource dom2xmlInputSource(LSInput is) {
        XMLInputSource xis;
        if (is.getCharacterStream() != null) {
            xis = new XMLInputSource(is.getPublicId(), is.getSystemId(), is.getBaseURI(), is.getCharacterStream(), "UTF-16");
        } else if (is.getByteStream() != null) {
            xis = new XMLInputSource(is.getPublicId(), is.getSystemId(), is.getBaseURI(), is.getByteStream(), is.getEncoding());
        } else if (is.getStringData() != null && is.getStringData().length() != 0) {
            xis = new XMLInputSource(is.getPublicId(), is.getSystemId(), is.getBaseURI(), new StringReader(is.getStringData()), "UTF-16");
        } else {
            xis = new XMLInputSource(is.getPublicId(), is.getSystemId(), is.getBaseURI());
        }
        return xis;
    }
}
