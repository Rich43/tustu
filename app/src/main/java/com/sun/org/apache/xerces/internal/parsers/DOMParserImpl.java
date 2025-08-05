package com.sun.org.apache.xerces.internal.parsers;

import com.sun.org.apache.xerces.internal.dom.DOMErrorImpl;
import com.sun.org.apache.xerces.internal.dom.DOMMessageFormatter;
import com.sun.org.apache.xerces.internal.dom.DOMStringListImpl;
import com.sun.org.apache.xerces.internal.impl.Constants;
import com.sun.org.apache.xerces.internal.jaxp.JAXPConstants;
import com.sun.org.apache.xerces.internal.parsers.AbstractDOMParser;
import com.sun.org.apache.xerces.internal.util.DOMEntityResolverWrapper;
import com.sun.org.apache.xerces.internal.util.DOMErrorHandlerWrapper;
import com.sun.org.apache.xerces.internal.util.DOMUtil;
import com.sun.org.apache.xerces.internal.util.SymbolTable;
import com.sun.org.apache.xerces.internal.util.XMLSymbols;
import com.sun.org.apache.xerces.internal.utils.ConfigurationError;
import com.sun.org.apache.xerces.internal.xni.Augmentations;
import com.sun.org.apache.xerces.internal.xni.NamespaceContext;
import com.sun.org.apache.xerces.internal.xni.QName;
import com.sun.org.apache.xerces.internal.xni.XMLAttributes;
import com.sun.org.apache.xerces.internal.xni.XMLDTDContentModelHandler;
import com.sun.org.apache.xerces.internal.xni.XMLDTDHandler;
import com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler;
import com.sun.org.apache.xerces.internal.xni.XMLLocator;
import com.sun.org.apache.xerces.internal.xni.XMLResourceIdentifier;
import com.sun.org.apache.xerces.internal.xni.XMLString;
import com.sun.org.apache.xerces.internal.xni.XNIException;
import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool;
import com.sun.org.apache.xerces.internal.xni.parser.XMLConfigurationException;
import com.sun.org.apache.xerces.internal.xni.parser.XMLDTDContentModelSource;
import com.sun.org.apache.xerces.internal.xni.parser.XMLDTDSource;
import com.sun.org.apache.xerces.internal.xni.parser.XMLDocumentSource;
import com.sun.org.apache.xerces.internal.xni.parser.XMLEntityResolver;
import com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;
import com.sun.org.apache.xerces.internal.xni.parser.XMLParseException;
import com.sun.org.apache.xerces.internal.xni.parser.XMLParserConfiguration;
import java.io.StringReader;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.Vector;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMErrorHandler;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMStringList;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.ls.LSException;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSParser;
import org.w3c.dom.ls.LSParserFilter;
import org.w3c.dom.ls.LSResourceResolver;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/parsers/DOMParserImpl.class */
public class DOMParserImpl extends AbstractDOMParser implements LSParser, DOMConfiguration {
    protected static final String NAMESPACES = "http://xml.org/sax/features/namespaces";
    protected static final String VALIDATION_FEATURE = "http://xml.org/sax/features/validation";
    protected static final String XMLSCHEMA = "http://apache.org/xml/features/validation/schema";
    protected static final String XMLSCHEMA_FULL_CHECKING = "http://apache.org/xml/features/validation/schema-full-checking";
    protected static final String DYNAMIC_VALIDATION = "http://apache.org/xml/features/validation/dynamic";
    protected static final String NORMALIZE_DATA = "http://apache.org/xml/features/validation/schema/normalized-value";
    protected static final String DISALLOW_DOCTYPE_DECL_FEATURE = "http://apache.org/xml/features/disallow-doctype-decl";
    protected static final String NAMESPACE_GROWTH = "http://apache.org/xml/features/namespace-growth";
    protected static final String TOLERATE_DUPLICATES = "http://apache.org/xml/features/internal/tolerate-duplicates";
    protected static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
    protected static final String PSVI_AUGMENT = "http://apache.org/xml/features/validation/schema/augment-psvi";
    protected boolean fNamespaceDeclarations;
    protected String fSchemaType;
    protected boolean fBusy;
    private boolean abortNow;
    private Thread currentThread;
    protected static final boolean DEBUG = false;
    private Vector fSchemaLocations;
    private String fSchemaLocation;
    private DOMStringList fRecognizedParameters;
    private AbortHandler abortHandler;

    public DOMParserImpl(XMLParserConfiguration config, String schemaType) throws XMLConfigurationException {
        this(config);
        if (schemaType != null) {
            if (schemaType.equals(Constants.NS_DTD)) {
                this.fConfiguration.setProperty(JAXPConstants.JAXP_SCHEMA_LANGUAGE, Constants.NS_DTD);
                this.fSchemaType = Constants.NS_DTD;
            } else if (schemaType.equals(Constants.NS_XMLSCHEMA)) {
                this.fConfiguration.setProperty(JAXPConstants.JAXP_SCHEMA_LANGUAGE, Constants.NS_XMLSCHEMA);
            }
        }
    }

    public DOMParserImpl(XMLParserConfiguration config) throws XMLConfigurationException {
        super(config);
        this.fNamespaceDeclarations = true;
        this.fSchemaType = null;
        this.fBusy = false;
        this.abortNow = false;
        this.fSchemaLocations = new Vector();
        this.fSchemaLocation = null;
        this.abortHandler = null;
        String[] domRecognizedFeatures = {Constants.DOM_CANONICAL_FORM, Constants.DOM_CDATA_SECTIONS, Constants.DOM_CHARSET_OVERRIDES_XML_ENCODING, Constants.DOM_INFOSET, Constants.DOM_NAMESPACE_DECLARATIONS, Constants.DOM_SPLIT_CDATA, Constants.DOM_SUPPORTED_MEDIATYPES_ONLY, Constants.DOM_CERTIFIED, Constants.DOM_WELLFORMED, Constants.DOM_IGNORE_UNKNOWN_CHARACTER_DENORMALIZATIONS};
        this.fConfiguration.addRecognizedFeatures(domRecognizedFeatures);
        this.fConfiguration.setFeature("http://apache.org/xml/features/dom/defer-node-expansion", false);
        this.fConfiguration.setFeature(Constants.DOM_NAMESPACE_DECLARATIONS, true);
        this.fConfiguration.setFeature(Constants.DOM_WELLFORMED, true);
        this.fConfiguration.setFeature("http://apache.org/xml/features/include-comments", true);
        this.fConfiguration.setFeature("http://apache.org/xml/features/dom/include-ignorable-whitespace", true);
        this.fConfiguration.setFeature("http://xml.org/sax/features/namespaces", true);
        this.fConfiguration.setFeature(DYNAMIC_VALIDATION, false);
        this.fConfiguration.setFeature("http://apache.org/xml/features/dom/create-entity-ref-nodes", false);
        this.fConfiguration.setFeature("http://apache.org/xml/features/create-cdata-nodes", false);
        this.fConfiguration.setFeature(Constants.DOM_CANONICAL_FORM, false);
        this.fConfiguration.setFeature(Constants.DOM_CHARSET_OVERRIDES_XML_ENCODING, true);
        this.fConfiguration.setFeature(Constants.DOM_SPLIT_CDATA, true);
        this.fConfiguration.setFeature(Constants.DOM_SUPPORTED_MEDIATYPES_ONLY, false);
        this.fConfiguration.setFeature(Constants.DOM_IGNORE_UNKNOWN_CHARACTER_DENORMALIZATIONS, true);
        this.fConfiguration.setFeature(Constants.DOM_CERTIFIED, true);
        try {
            this.fConfiguration.setFeature(NORMALIZE_DATA, false);
        } catch (XMLConfigurationException e2) {
        }
    }

    public DOMParserImpl(SymbolTable symbolTable) throws XMLConfigurationException {
        this(new XIncludeAwareParserConfiguration());
        this.fConfiguration.setProperty("http://apache.org/xml/properties/internal/symbol-table", symbolTable);
    }

    public DOMParserImpl(SymbolTable symbolTable, XMLGrammarPool grammarPool) throws XMLConfigurationException {
        this(new XIncludeAwareParserConfiguration());
        this.fConfiguration.setProperty("http://apache.org/xml/properties/internal/symbol-table", symbolTable);
        this.fConfiguration.setProperty("http://apache.org/xml/properties/internal/grammar-pool", grammarPool);
    }

    @Override // com.sun.org.apache.xerces.internal.parsers.AbstractDOMParser, com.sun.org.apache.xerces.internal.parsers.AbstractXMLDocumentParser, com.sun.org.apache.xerces.internal.parsers.XMLParser
    public void reset() throws ConfigurationError, XNIException {
        super.reset();
        this.fNamespaceDeclarations = this.fConfiguration.getFeature(Constants.DOM_NAMESPACE_DECLARATIONS);
        if (this.fSkippedElemStack != null) {
            this.fSkippedElemStack.removeAllElements();
        }
        this.fSchemaLocations.clear();
        this.fRejectedElementDepth = 0;
        this.fFilterReject = false;
        this.fSchemaType = null;
    }

    @Override // org.w3c.dom.ls.LSParser
    public DOMConfiguration getDomConfig() {
        return this;
    }

    @Override // org.w3c.dom.ls.LSParser
    public LSParserFilter getFilter() {
        return this.fDOMFilter;
    }

    @Override // org.w3c.dom.ls.LSParser
    public void setFilter(LSParserFilter filter) {
        this.fDOMFilter = filter;
        if (this.fSkippedElemStack == null) {
            this.fSkippedElemStack = new Stack();
        }
    }

    @Override // org.w3c.dom.DOMConfiguration
    public void setParameter(String name, Object value) throws DOMException, MissingResourceException, XMLConfigurationException {
        String normalizedName;
        if (value instanceof Boolean) {
            boolean state = ((Boolean) value).booleanValue();
            try {
                if (name.equalsIgnoreCase(Constants.DOM_COMMENTS)) {
                    this.fConfiguration.setFeature("http://apache.org/xml/features/include-comments", state);
                } else if (name.equalsIgnoreCase(Constants.DOM_DATATYPE_NORMALIZATION)) {
                    this.fConfiguration.setFeature(NORMALIZE_DATA, state);
                } else if (name.equalsIgnoreCase(Constants.DOM_ENTITIES)) {
                    this.fConfiguration.setFeature("http://apache.org/xml/features/dom/create-entity-ref-nodes", state);
                } else if (name.equalsIgnoreCase(Constants.DOM_DISALLOW_DOCTYPE)) {
                    this.fConfiguration.setFeature(DISALLOW_DOCTYPE_DECL_FEATURE, state);
                } else if (name.equalsIgnoreCase(Constants.DOM_SUPPORTED_MEDIATYPES_ONLY) || name.equalsIgnoreCase(Constants.DOM_NORMALIZE_CHARACTERS) || name.equalsIgnoreCase(Constants.DOM_CHECK_CHAR_NORMALIZATION) || name.equalsIgnoreCase(Constants.DOM_CANONICAL_FORM)) {
                    if (state) {
                        String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "FEATURE_NOT_SUPPORTED", new Object[]{name});
                        throw new DOMException((short) 9, msg);
                    }
                } else if (name.equalsIgnoreCase("namespaces")) {
                    this.fConfiguration.setFeature("http://xml.org/sax/features/namespaces", state);
                } else if (name.equalsIgnoreCase(Constants.DOM_INFOSET)) {
                    if (state) {
                        this.fConfiguration.setFeature("http://xml.org/sax/features/namespaces", true);
                        this.fConfiguration.setFeature(Constants.DOM_NAMESPACE_DECLARATIONS, true);
                        this.fConfiguration.setFeature("http://apache.org/xml/features/include-comments", true);
                        this.fConfiguration.setFeature("http://apache.org/xml/features/dom/include-ignorable-whitespace", true);
                        this.fConfiguration.setFeature(DYNAMIC_VALIDATION, false);
                        this.fConfiguration.setFeature("http://apache.org/xml/features/dom/create-entity-ref-nodes", false);
                        this.fConfiguration.setFeature(NORMALIZE_DATA, false);
                        this.fConfiguration.setFeature("http://apache.org/xml/features/create-cdata-nodes", false);
                    }
                } else if (name.equalsIgnoreCase(Constants.DOM_CDATA_SECTIONS)) {
                    this.fConfiguration.setFeature("http://apache.org/xml/features/create-cdata-nodes", state);
                } else if (name.equalsIgnoreCase(Constants.DOM_NAMESPACE_DECLARATIONS)) {
                    this.fConfiguration.setFeature(Constants.DOM_NAMESPACE_DECLARATIONS, state);
                } else if (name.equalsIgnoreCase(Constants.DOM_WELLFORMED) || name.equalsIgnoreCase(Constants.DOM_IGNORE_UNKNOWN_CHARACTER_DENORMALIZATIONS)) {
                    if (!state) {
                        String msg2 = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "FEATURE_NOT_SUPPORTED", new Object[]{name});
                        throw new DOMException((short) 9, msg2);
                    }
                } else if (name.equalsIgnoreCase(Constants.DOM_VALIDATE)) {
                    this.fConfiguration.setFeature(VALIDATION_FEATURE, state);
                    if (this.fSchemaType != Constants.NS_DTD) {
                        this.fConfiguration.setFeature(XMLSCHEMA, state);
                        this.fConfiguration.setFeature(XMLSCHEMA_FULL_CHECKING, state);
                    }
                    if (state) {
                        this.fConfiguration.setFeature(DYNAMIC_VALIDATION, false);
                    }
                } else if (name.equalsIgnoreCase(Constants.DOM_VALIDATE_IF_SCHEMA)) {
                    this.fConfiguration.setFeature(DYNAMIC_VALIDATION, state);
                    if (state) {
                        this.fConfiguration.setFeature(VALIDATION_FEATURE, false);
                    }
                } else if (name.equalsIgnoreCase(Constants.DOM_ELEMENT_CONTENT_WHITESPACE)) {
                    this.fConfiguration.setFeature("http://apache.org/xml/features/dom/include-ignorable-whitespace", state);
                } else if (name.equalsIgnoreCase(Constants.DOM_PSVI)) {
                    this.fConfiguration.setFeature(PSVI_AUGMENT, true);
                    this.fConfiguration.setProperty("http://apache.org/xml/properties/dom/document-class-name", "com.sun.org.apache.xerces.internal.dom.PSVIDocumentImpl");
                } else {
                    if (name.equals(NAMESPACE_GROWTH)) {
                        normalizedName = NAMESPACE_GROWTH;
                    } else if (name.equals(TOLERATE_DUPLICATES)) {
                        normalizedName = TOLERATE_DUPLICATES;
                    } else {
                        normalizedName = name.toLowerCase(Locale.ENGLISH);
                    }
                    this.fConfiguration.setFeature(normalizedName, state);
                }
                return;
            } catch (XMLConfigurationException e2) {
                String msg3 = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "FEATURE_NOT_FOUND", new Object[]{name});
                throw new DOMException((short) 8, msg3);
            }
        }
        if (name.equalsIgnoreCase(Constants.DOM_ERROR_HANDLER)) {
            if ((value instanceof DOMErrorHandler) || value == null) {
                try {
                    this.fErrorHandler = new DOMErrorHandlerWrapper((DOMErrorHandler) value);
                    this.fConfiguration.setProperty("http://apache.org/xml/properties/internal/error-handler", this.fErrorHandler);
                    return;
                } catch (XMLConfigurationException e3) {
                    return;
                }
            }
            String msg4 = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "TYPE_MISMATCH_ERR", new Object[]{name});
            throw new DOMException((short) 17, msg4);
        }
        if (name.equalsIgnoreCase(Constants.DOM_RESOURCE_RESOLVER)) {
            if ((value instanceof LSResourceResolver) || value == null) {
                try {
                    this.fConfiguration.setProperty("http://apache.org/xml/properties/internal/entity-resolver", new DOMEntityResolverWrapper((LSResourceResolver) value));
                    return;
                } catch (XMLConfigurationException e4) {
                    return;
                }
            } else {
                String msg5 = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "TYPE_MISMATCH_ERR", new Object[]{name});
                throw new DOMException((short) 17, msg5);
            }
        }
        if (name.equalsIgnoreCase(Constants.DOM_SCHEMA_LOCATION)) {
            if ((value instanceof String) || value == null) {
                try {
                    if (value == null) {
                        this.fSchemaLocation = null;
                        this.fConfiguration.setProperty(JAXPConstants.JAXP_SCHEMA_SOURCE, null);
                    } else {
                        this.fSchemaLocation = (String) value;
                        StringTokenizer t2 = new StringTokenizer(this.fSchemaLocation, " \n\t\r");
                        if (t2.hasMoreTokens()) {
                            this.fSchemaLocations.clear();
                            this.fSchemaLocations.add(t2.nextToken());
                            while (t2.hasMoreTokens()) {
                                this.fSchemaLocations.add(t2.nextToken());
                            }
                            this.fConfiguration.setProperty(JAXPConstants.JAXP_SCHEMA_SOURCE, this.fSchemaLocations.toArray());
                        } else {
                            this.fConfiguration.setProperty(JAXPConstants.JAXP_SCHEMA_SOURCE, value);
                        }
                    }
                    return;
                } catch (XMLConfigurationException e5) {
                    return;
                }
            }
            String msg6 = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "TYPE_MISMATCH_ERR", new Object[]{name});
            throw new DOMException((short) 17, msg6);
        }
        if (name.equalsIgnoreCase(Constants.DOM_SCHEMA_TYPE)) {
            if ((value instanceof String) || value == null) {
                try {
                    if (value == null) {
                        this.fConfiguration.setFeature(XMLSCHEMA, false);
                        this.fConfiguration.setFeature(XMLSCHEMA_FULL_CHECKING, false);
                        this.fConfiguration.setProperty(JAXPConstants.JAXP_SCHEMA_LANGUAGE, null);
                        this.fSchemaType = null;
                    } else if (value.equals(Constants.NS_XMLSCHEMA)) {
                        this.fConfiguration.setFeature(XMLSCHEMA, true);
                        this.fConfiguration.setFeature(XMLSCHEMA_FULL_CHECKING, true);
                        this.fConfiguration.setProperty(JAXPConstants.JAXP_SCHEMA_LANGUAGE, Constants.NS_XMLSCHEMA);
                        this.fSchemaType = Constants.NS_XMLSCHEMA;
                    } else if (value.equals(Constants.NS_DTD)) {
                        this.fConfiguration.setFeature(XMLSCHEMA, false);
                        this.fConfiguration.setFeature(XMLSCHEMA_FULL_CHECKING, false);
                        this.fConfiguration.setProperty(JAXPConstants.JAXP_SCHEMA_LANGUAGE, Constants.NS_DTD);
                        this.fSchemaType = Constants.NS_DTD;
                    }
                    return;
                } catch (XMLConfigurationException e6) {
                    return;
                }
            }
            String msg7 = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "TYPE_MISMATCH_ERR", new Object[]{name});
            throw new DOMException((short) 17, msg7);
        }
        if (name.equalsIgnoreCase("http://apache.org/xml/properties/dom/document-class-name")) {
            this.fConfiguration.setProperty("http://apache.org/xml/properties/dom/document-class-name", value);
            return;
        }
        String normalizedName2 = name.toLowerCase(Locale.ENGLISH);
        try {
            this.fConfiguration.setProperty(normalizedName2, value);
        } catch (XMLConfigurationException e7) {
            try {
                if (name.equals(NAMESPACE_GROWTH)) {
                    normalizedName2 = NAMESPACE_GROWTH;
                } else if (name.equals(TOLERATE_DUPLICATES)) {
                    normalizedName2 = TOLERATE_DUPLICATES;
                }
                this.fConfiguration.getFeature(normalizedName2);
                throw newTypeMismatchError(name);
            } catch (XMLConfigurationException e8) {
                throw newFeatureNotFoundError(name);
            }
        }
    }

    @Override // org.w3c.dom.DOMConfiguration
    public Object getParameter(String name) throws DOMException {
        String normalizedName;
        if (name.equalsIgnoreCase(Constants.DOM_COMMENTS)) {
            return this.fConfiguration.getFeature("http://apache.org/xml/features/include-comments") ? Boolean.TRUE : Boolean.FALSE;
        }
        if (name.equalsIgnoreCase(Constants.DOM_DATATYPE_NORMALIZATION)) {
            return this.fConfiguration.getFeature(NORMALIZE_DATA) ? Boolean.TRUE : Boolean.FALSE;
        }
        if (name.equalsIgnoreCase(Constants.DOM_ENTITIES)) {
            return this.fConfiguration.getFeature("http://apache.org/xml/features/dom/create-entity-ref-nodes") ? Boolean.TRUE : Boolean.FALSE;
        }
        if (name.equalsIgnoreCase("namespaces")) {
            return this.fConfiguration.getFeature("http://xml.org/sax/features/namespaces") ? Boolean.TRUE : Boolean.FALSE;
        }
        if (name.equalsIgnoreCase(Constants.DOM_VALIDATE)) {
            return this.fConfiguration.getFeature(VALIDATION_FEATURE) ? Boolean.TRUE : Boolean.FALSE;
        }
        if (name.equalsIgnoreCase(Constants.DOM_VALIDATE_IF_SCHEMA)) {
            return this.fConfiguration.getFeature(DYNAMIC_VALIDATION) ? Boolean.TRUE : Boolean.FALSE;
        }
        if (name.equalsIgnoreCase(Constants.DOM_ELEMENT_CONTENT_WHITESPACE)) {
            return this.fConfiguration.getFeature("http://apache.org/xml/features/dom/include-ignorable-whitespace") ? Boolean.TRUE : Boolean.FALSE;
        }
        if (name.equalsIgnoreCase(Constants.DOM_DISALLOW_DOCTYPE)) {
            return this.fConfiguration.getFeature(DISALLOW_DOCTYPE_DECL_FEATURE) ? Boolean.TRUE : Boolean.FALSE;
        }
        if (name.equalsIgnoreCase(Constants.DOM_INFOSET)) {
            boolean infoset = this.fConfiguration.getFeature("http://xml.org/sax/features/namespaces") && this.fConfiguration.getFeature(Constants.DOM_NAMESPACE_DECLARATIONS) && this.fConfiguration.getFeature("http://apache.org/xml/features/include-comments") && this.fConfiguration.getFeature("http://apache.org/xml/features/dom/include-ignorable-whitespace") && !this.fConfiguration.getFeature(DYNAMIC_VALIDATION) && !this.fConfiguration.getFeature("http://apache.org/xml/features/dom/create-entity-ref-nodes") && !this.fConfiguration.getFeature(NORMALIZE_DATA) && !this.fConfiguration.getFeature("http://apache.org/xml/features/create-cdata-nodes");
            return infoset ? Boolean.TRUE : Boolean.FALSE;
        }
        if (name.equalsIgnoreCase(Constants.DOM_CDATA_SECTIONS)) {
            return this.fConfiguration.getFeature("http://apache.org/xml/features/create-cdata-nodes") ? Boolean.TRUE : Boolean.FALSE;
        }
        if (name.equalsIgnoreCase(Constants.DOM_CHECK_CHAR_NORMALIZATION) || name.equalsIgnoreCase(Constants.DOM_NORMALIZE_CHARACTERS)) {
            return Boolean.FALSE;
        }
        if (name.equalsIgnoreCase(Constants.DOM_NAMESPACE_DECLARATIONS) || name.equalsIgnoreCase(Constants.DOM_WELLFORMED) || name.equalsIgnoreCase(Constants.DOM_IGNORE_UNKNOWN_CHARACTER_DENORMALIZATIONS) || name.equalsIgnoreCase(Constants.DOM_CANONICAL_FORM) || name.equalsIgnoreCase(Constants.DOM_SUPPORTED_MEDIATYPES_ONLY) || name.equalsIgnoreCase(Constants.DOM_SPLIT_CDATA) || name.equalsIgnoreCase(Constants.DOM_CHARSET_OVERRIDES_XML_ENCODING)) {
            return this.fConfiguration.getFeature(name.toLowerCase(Locale.ENGLISH)) ? Boolean.TRUE : Boolean.FALSE;
        }
        if (name.equalsIgnoreCase(Constants.DOM_ERROR_HANDLER)) {
            if (this.fErrorHandler != null) {
                return this.fErrorHandler.getErrorHandler();
            }
            return null;
        }
        if (name.equalsIgnoreCase(Constants.DOM_RESOURCE_RESOLVER)) {
            try {
                XMLEntityResolver entityResolver = (XMLEntityResolver) this.fConfiguration.getProperty("http://apache.org/xml/properties/internal/entity-resolver");
                if (entityResolver != null && (entityResolver instanceof DOMEntityResolverWrapper)) {
                    return ((DOMEntityResolverWrapper) entityResolver).getEntityResolver();
                }
                return null;
            } catch (XMLConfigurationException e2) {
                return null;
            }
        }
        if (name.equalsIgnoreCase(Constants.DOM_SCHEMA_TYPE)) {
            return this.fConfiguration.getProperty(JAXPConstants.JAXP_SCHEMA_LANGUAGE);
        }
        if (name.equalsIgnoreCase(Constants.DOM_SCHEMA_LOCATION)) {
            return this.fSchemaLocation;
        }
        if (name.equalsIgnoreCase("http://apache.org/xml/properties/internal/symbol-table")) {
            return this.fConfiguration.getProperty("http://apache.org/xml/properties/internal/symbol-table");
        }
        if (name.equalsIgnoreCase("http://apache.org/xml/properties/dom/document-class-name")) {
            return this.fConfiguration.getProperty("http://apache.org/xml/properties/dom/document-class-name");
        }
        if (name.equals(NAMESPACE_GROWTH)) {
            normalizedName = NAMESPACE_GROWTH;
        } else if (name.equals(TOLERATE_DUPLICATES)) {
            normalizedName = TOLERATE_DUPLICATES;
        } else {
            normalizedName = name.toLowerCase(Locale.ENGLISH);
        }
        try {
            return this.fConfiguration.getFeature(normalizedName) ? Boolean.TRUE : Boolean.FALSE;
        } catch (XMLConfigurationException e3) {
            try {
                return this.fConfiguration.getProperty(normalizedName);
            } catch (XMLConfigurationException e4) {
                throw newFeatureNotFoundError(name);
            }
        }
    }

    @Override // org.w3c.dom.DOMConfiguration
    public boolean canSetParameter(String name, Object value) {
        String normalizedName;
        if (value == null) {
            return true;
        }
        if (value instanceof Boolean) {
            boolean state = ((Boolean) value).booleanValue();
            if (name.equalsIgnoreCase(Constants.DOM_SUPPORTED_MEDIATYPES_ONLY) || name.equalsIgnoreCase(Constants.DOM_NORMALIZE_CHARACTERS) || name.equalsIgnoreCase(Constants.DOM_CHECK_CHAR_NORMALIZATION) || name.equalsIgnoreCase(Constants.DOM_CANONICAL_FORM)) {
                return !state;
            }
            if (name.equalsIgnoreCase(Constants.DOM_WELLFORMED) || name.equalsIgnoreCase(Constants.DOM_IGNORE_UNKNOWN_CHARACTER_DENORMALIZATIONS)) {
                return state;
            }
            if (name.equalsIgnoreCase(Constants.DOM_CDATA_SECTIONS) || name.equalsIgnoreCase(Constants.DOM_CHARSET_OVERRIDES_XML_ENCODING) || name.equalsIgnoreCase(Constants.DOM_COMMENTS) || name.equalsIgnoreCase(Constants.DOM_DATATYPE_NORMALIZATION) || name.equalsIgnoreCase(Constants.DOM_DISALLOW_DOCTYPE) || name.equalsIgnoreCase(Constants.DOM_ENTITIES) || name.equalsIgnoreCase(Constants.DOM_INFOSET) || name.equalsIgnoreCase("namespaces") || name.equalsIgnoreCase(Constants.DOM_NAMESPACE_DECLARATIONS) || name.equalsIgnoreCase(Constants.DOM_VALIDATE) || name.equalsIgnoreCase(Constants.DOM_VALIDATE_IF_SCHEMA) || name.equalsIgnoreCase(Constants.DOM_ELEMENT_CONTENT_WHITESPACE) || name.equalsIgnoreCase(Constants.DOM_XMLDECL)) {
                return true;
            }
            try {
                if (name.equalsIgnoreCase(NAMESPACE_GROWTH)) {
                    normalizedName = NAMESPACE_GROWTH;
                } else if (name.equalsIgnoreCase(TOLERATE_DUPLICATES)) {
                    normalizedName = TOLERATE_DUPLICATES;
                } else {
                    normalizedName = name.toLowerCase(Locale.ENGLISH);
                }
                this.fConfiguration.getFeature(normalizedName);
                return true;
            } catch (XMLConfigurationException e2) {
                return false;
            }
        }
        if (name.equalsIgnoreCase(Constants.DOM_ERROR_HANDLER)) {
            if ((value instanceof DOMErrorHandler) || value == null) {
                return true;
            }
            return false;
        }
        if (name.equalsIgnoreCase(Constants.DOM_RESOURCE_RESOLVER)) {
            if ((value instanceof LSResourceResolver) || value == null) {
                return true;
            }
            return false;
        }
        if (name.equalsIgnoreCase(Constants.DOM_SCHEMA_TYPE)) {
            if (((value instanceof String) && (value.equals(Constants.NS_XMLSCHEMA) || value.equals(Constants.NS_DTD))) || value == null) {
                return true;
            }
            return false;
        }
        if (name.equalsIgnoreCase(Constants.DOM_SCHEMA_LOCATION)) {
            if ((value instanceof String) || value == null) {
                return true;
            }
            return false;
        }
        if (name.equalsIgnoreCase("http://apache.org/xml/properties/dom/document-class-name")) {
            return true;
        }
        return false;
    }

    @Override // org.w3c.dom.DOMConfiguration
    public DOMStringList getParameterNames() {
        if (this.fRecognizedParameters == null) {
            Vector parameters = new Vector();
            parameters.add("namespaces");
            parameters.add(Constants.DOM_CDATA_SECTIONS);
            parameters.add(Constants.DOM_CANONICAL_FORM);
            parameters.add(Constants.DOM_NAMESPACE_DECLARATIONS);
            parameters.add(Constants.DOM_SPLIT_CDATA);
            parameters.add(Constants.DOM_ENTITIES);
            parameters.add(Constants.DOM_VALIDATE_IF_SCHEMA);
            parameters.add(Constants.DOM_VALIDATE);
            parameters.add(Constants.DOM_DATATYPE_NORMALIZATION);
            parameters.add(Constants.DOM_CHARSET_OVERRIDES_XML_ENCODING);
            parameters.add(Constants.DOM_CHECK_CHAR_NORMALIZATION);
            parameters.add(Constants.DOM_SUPPORTED_MEDIATYPES_ONLY);
            parameters.add(Constants.DOM_IGNORE_UNKNOWN_CHARACTER_DENORMALIZATIONS);
            parameters.add(Constants.DOM_NORMALIZE_CHARACTERS);
            parameters.add(Constants.DOM_WELLFORMED);
            parameters.add(Constants.DOM_INFOSET);
            parameters.add(Constants.DOM_DISALLOW_DOCTYPE);
            parameters.add(Constants.DOM_ELEMENT_CONTENT_WHITESPACE);
            parameters.add(Constants.DOM_COMMENTS);
            parameters.add(Constants.DOM_ERROR_HANDLER);
            parameters.add(Constants.DOM_RESOURCE_RESOLVER);
            parameters.add(Constants.DOM_SCHEMA_LOCATION);
            parameters.add(Constants.DOM_SCHEMA_TYPE);
            this.fRecognizedParameters = new DOMStringListImpl(parameters);
        }
        return this.fRecognizedParameters;
    }

    @Override // org.w3c.dom.ls.LSParser
    public Document parseURI(String uri) throws LSException, MissingResourceException {
        if (this.fBusy) {
            String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "INVALID_STATE_ERR", null);
            throw new DOMException((short) 11, msg);
        }
        XMLInputSource source = new XMLInputSource(null, uri, null);
        try {
            this.currentThread = Thread.currentThread();
            this.fBusy = true;
            parse(source);
            this.fBusy = false;
            if (this.abortNow && this.currentThread.isInterrupted()) {
                this.abortNow = false;
                Thread.interrupted();
            }
        } catch (Exception e2) {
            this.fBusy = false;
            if (this.abortNow && this.currentThread.isInterrupted()) {
                Thread.interrupted();
            }
            if (this.abortNow) {
                this.abortNow = false;
                restoreHandlers();
                return null;
            }
            if (e2 != AbstractDOMParser.Abort.INSTANCE) {
                if (!(e2 instanceof XMLParseException) && this.fErrorHandler != null) {
                    DOMErrorImpl error = new DOMErrorImpl();
                    error.fException = e2;
                    error.fMessage = e2.getMessage();
                    error.fSeverity = (short) 3;
                    this.fErrorHandler.getErrorHandler().handleError(error);
                }
                throw ((LSException) DOMUtil.createLSException((short) 81, e2).fillInStackTrace());
            }
        }
        Document doc = getDocument();
        dropDocumentReferences();
        return doc;
    }

    @Override // org.w3c.dom.ls.LSParser
    public Document parse(LSInput is) throws LSException, MissingResourceException {
        XMLInputSource xmlInputSource = dom2xmlInputSource(is);
        if (this.fBusy) {
            String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "INVALID_STATE_ERR", null);
            throw new DOMException((short) 11, msg);
        }
        try {
            this.currentThread = Thread.currentThread();
            this.fBusy = true;
            parse(xmlInputSource);
            this.fBusy = false;
            if (this.abortNow && this.currentThread.isInterrupted()) {
                this.abortNow = false;
                Thread.interrupted();
            }
        } catch (Exception e2) {
            this.fBusy = false;
            if (this.abortNow && this.currentThread.isInterrupted()) {
                Thread.interrupted();
            }
            if (this.abortNow) {
                this.abortNow = false;
                restoreHandlers();
                return null;
            }
            if (e2 != AbstractDOMParser.Abort.INSTANCE) {
                if (!(e2 instanceof XMLParseException) && this.fErrorHandler != null) {
                    DOMErrorImpl error = new DOMErrorImpl();
                    error.fException = e2;
                    error.fMessage = e2.getMessage();
                    error.fSeverity = (short) 3;
                    this.fErrorHandler.getErrorHandler().handleError(error);
                }
                throw ((LSException) DOMUtil.createLSException((short) 81, e2).fillInStackTrace());
            }
        }
        Document doc = getDocument();
        dropDocumentReferences();
        return doc;
    }

    private void restoreHandlers() {
        this.fConfiguration.setDocumentHandler(this);
        this.fConfiguration.setDTDHandler(this);
        this.fConfiguration.setDTDContentModelHandler(this);
    }

    @Override // org.w3c.dom.ls.LSParser
    public Node parseWithContext(LSInput is, Node cnode, short action) throws DOMException, LSException {
        throw new DOMException((short) 9, "Not supported");
    }

    XMLInputSource dom2xmlInputSource(LSInput is) {
        XMLInputSource xis;
        if (is.getCharacterStream() != null) {
            xis = new XMLInputSource(is.getPublicId(), is.getSystemId(), is.getBaseURI(), is.getCharacterStream(), "UTF-16");
        } else if (is.getByteStream() != null) {
            xis = new XMLInputSource(is.getPublicId(), is.getSystemId(), is.getBaseURI(), is.getByteStream(), is.getEncoding());
        } else if (is.getStringData() != null && is.getStringData().length() > 0) {
            xis = new XMLInputSource(is.getPublicId(), is.getSystemId(), is.getBaseURI(), new StringReader(is.getStringData()), "UTF-16");
        } else if ((is.getSystemId() != null && is.getSystemId().length() > 0) || (is.getPublicId() != null && is.getPublicId().length() > 0)) {
            xis = new XMLInputSource(is.getPublicId(), is.getSystemId(), is.getBaseURI());
        } else {
            if (this.fErrorHandler != null) {
                DOMErrorImpl error = new DOMErrorImpl();
                error.fType = "no-input-specified";
                error.fMessage = "no-input-specified";
                error.fSeverity = (short) 3;
                this.fErrorHandler.getErrorHandler().handleError(error);
            }
            throw new LSException((short) 81, "no-input-specified");
        }
        return xis;
    }

    @Override // org.w3c.dom.ls.LSParser
    public boolean getAsync() {
        return false;
    }

    @Override // org.w3c.dom.ls.LSParser
    public boolean getBusy() {
        return this.fBusy;
    }

    @Override // com.sun.org.apache.xerces.internal.parsers.AbstractDOMParser, org.w3c.dom.ls.LSParser
    public void abort() {
        if (this.fBusy) {
            this.fBusy = false;
            if (this.currentThread != null) {
                this.abortNow = true;
                if (this.abortHandler == null) {
                    this.abortHandler = new AbortHandler();
                }
                this.fConfiguration.setDocumentHandler(this.abortHandler);
                this.fConfiguration.setDTDHandler(this.abortHandler);
                this.fConfiguration.setDTDContentModelHandler(this.abortHandler);
                if (this.currentThread == Thread.currentThread()) {
                    throw AbstractDOMParser.Abort.INSTANCE;
                }
                this.currentThread.interrupt();
            }
        }
    }

    @Override // com.sun.org.apache.xerces.internal.parsers.AbstractDOMParser, com.sun.org.apache.xerces.internal.parsers.AbstractXMLDocumentParser, com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void startElement(QName element, XMLAttributes attributes, Augmentations augs) throws DOMException, MissingResourceException, XNIException {
        if (!this.fNamespaceDeclarations && this.fNamespaceAware) {
            int len = attributes.getLength();
            for (int i2 = len - 1; i2 >= 0; i2--) {
                if (XMLSymbols.PREFIX_XMLNS == attributes.getPrefix(i2) || XMLSymbols.PREFIX_XMLNS == attributes.getQName(i2)) {
                    attributes.removeAttributeAt(i2);
                }
            }
        }
        super.startElement(element, attributes, augs);
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/parsers/DOMParserImpl$AbortHandler.class */
    private class AbortHandler implements XMLDocumentHandler, XMLDTDHandler, XMLDTDContentModelHandler {
        private XMLDocumentSource documentSource;
        private XMLDTDContentModelSource dtdContentSource;
        private XMLDTDSource dtdSource;

        private AbortHandler() {
        }

        @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
        public void startDocument(XMLLocator locator, String encoding, NamespaceContext namespaceContext, Augmentations augs) throws XNIException {
            throw AbstractDOMParser.Abort.INSTANCE;
        }

        @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
        public void xmlDecl(String version, String encoding, String standalone, Augmentations augs) throws XNIException {
            throw AbstractDOMParser.Abort.INSTANCE;
        }

        @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
        public void doctypeDecl(String rootElement, String publicId, String systemId, Augmentations augs) throws XNIException {
            throw AbstractDOMParser.Abort.INSTANCE;
        }

        @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
        public void comment(XMLString text, Augmentations augs) throws XNIException {
            throw AbstractDOMParser.Abort.INSTANCE;
        }

        @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
        public void processingInstruction(String target, XMLString data, Augmentations augs) throws XNIException {
            throw AbstractDOMParser.Abort.INSTANCE;
        }

        @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
        public void startElement(QName element, XMLAttributes attributes, Augmentations augs) throws XNIException {
            throw AbstractDOMParser.Abort.INSTANCE;
        }

        @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
        public void emptyElement(QName element, XMLAttributes attributes, Augmentations augs) throws XNIException {
            throw AbstractDOMParser.Abort.INSTANCE;
        }

        @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
        public void startGeneralEntity(String name, XMLResourceIdentifier identifier, String encoding, Augmentations augs) throws XNIException {
            throw AbstractDOMParser.Abort.INSTANCE;
        }

        @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
        public void textDecl(String version, String encoding, Augmentations augs) throws XNIException {
            throw AbstractDOMParser.Abort.INSTANCE;
        }

        @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
        public void endGeneralEntity(String name, Augmentations augs) throws XNIException {
            throw AbstractDOMParser.Abort.INSTANCE;
        }

        @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
        public void characters(XMLString text, Augmentations augs) throws XNIException {
            throw AbstractDOMParser.Abort.INSTANCE;
        }

        @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
        public void ignorableWhitespace(XMLString text, Augmentations augs) throws XNIException {
            throw AbstractDOMParser.Abort.INSTANCE;
        }

        @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
        public void endElement(QName element, Augmentations augs) throws XNIException {
            throw AbstractDOMParser.Abort.INSTANCE;
        }

        @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
        public void startCDATA(Augmentations augs) throws XNIException {
            throw AbstractDOMParser.Abort.INSTANCE;
        }

        @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
        public void endCDATA(Augmentations augs) throws XNIException {
            throw AbstractDOMParser.Abort.INSTANCE;
        }

        @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
        public void endDocument(Augmentations augs) throws XNIException {
            throw AbstractDOMParser.Abort.INSTANCE;
        }

        @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
        public void setDocumentSource(XMLDocumentSource source) {
            this.documentSource = source;
        }

        @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
        public XMLDocumentSource getDocumentSource() {
            return this.documentSource;
        }

        @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
        public void startDTD(XMLLocator locator, Augmentations augmentations) throws XNIException {
            throw AbstractDOMParser.Abort.INSTANCE;
        }

        @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
        public void startParameterEntity(String name, XMLResourceIdentifier identifier, String encoding, Augmentations augmentations) throws XNIException {
            throw AbstractDOMParser.Abort.INSTANCE;
        }

        @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
        public void endParameterEntity(String name, Augmentations augmentations) throws XNIException {
            throw AbstractDOMParser.Abort.INSTANCE;
        }

        @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
        public void startExternalSubset(XMLResourceIdentifier identifier, Augmentations augmentations) throws XNIException {
            throw AbstractDOMParser.Abort.INSTANCE;
        }

        @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
        public void endExternalSubset(Augmentations augmentations) throws XNIException {
            throw AbstractDOMParser.Abort.INSTANCE;
        }

        @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
        public void elementDecl(String name, String contentModel, Augmentations augmentations) throws XNIException {
            throw AbstractDOMParser.Abort.INSTANCE;
        }

        @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
        public void startAttlist(String elementName, Augmentations augmentations) throws XNIException {
            throw AbstractDOMParser.Abort.INSTANCE;
        }

        @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
        public void attributeDecl(String elementName, String attributeName, String type, String[] enumeration, String defaultType, XMLString defaultValue, XMLString nonNormalizedDefaultValue, Augmentations augmentations) throws XNIException {
            throw AbstractDOMParser.Abort.INSTANCE;
        }

        @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
        public void endAttlist(Augmentations augmentations) throws XNIException {
            throw AbstractDOMParser.Abort.INSTANCE;
        }

        @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
        public void internalEntityDecl(String name, XMLString text, XMLString nonNormalizedText, Augmentations augmentations) throws XNIException {
            throw AbstractDOMParser.Abort.INSTANCE;
        }

        @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
        public void externalEntityDecl(String name, XMLResourceIdentifier identifier, Augmentations augmentations) throws XNIException {
            throw AbstractDOMParser.Abort.INSTANCE;
        }

        @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
        public void unparsedEntityDecl(String name, XMLResourceIdentifier identifier, String notation, Augmentations augmentations) throws XNIException {
            throw AbstractDOMParser.Abort.INSTANCE;
        }

        @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
        public void notationDecl(String name, XMLResourceIdentifier identifier, Augmentations augmentations) throws XNIException {
            throw AbstractDOMParser.Abort.INSTANCE;
        }

        @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
        public void startConditional(short type, Augmentations augmentations) throws XNIException {
            throw AbstractDOMParser.Abort.INSTANCE;
        }

        @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
        public void ignoredCharacters(XMLString text, Augmentations augmentations) throws XNIException {
            throw AbstractDOMParser.Abort.INSTANCE;
        }

        @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
        public void endConditional(Augmentations augmentations) throws XNIException {
            throw AbstractDOMParser.Abort.INSTANCE;
        }

        @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
        public void endDTD(Augmentations augmentations) throws XNIException {
            throw AbstractDOMParser.Abort.INSTANCE;
        }

        @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
        public void setDTDSource(XMLDTDSource source) {
            this.dtdSource = source;
        }

        @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
        public XMLDTDSource getDTDSource() {
            return this.dtdSource;
        }

        @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDContentModelHandler
        public void startContentModel(String elementName, Augmentations augmentations) throws XNIException {
            throw AbstractDOMParser.Abort.INSTANCE;
        }

        @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDContentModelHandler
        public void any(Augmentations augmentations) throws XNIException {
            throw AbstractDOMParser.Abort.INSTANCE;
        }

        @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDContentModelHandler
        public void empty(Augmentations augmentations) throws XNIException {
            throw AbstractDOMParser.Abort.INSTANCE;
        }

        @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDContentModelHandler
        public void startGroup(Augmentations augmentations) throws XNIException {
            throw AbstractDOMParser.Abort.INSTANCE;
        }

        @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDContentModelHandler
        public void pcdata(Augmentations augmentations) throws XNIException {
            throw AbstractDOMParser.Abort.INSTANCE;
        }

        @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDContentModelHandler
        public void element(String elementName, Augmentations augmentations) throws XNIException {
            throw AbstractDOMParser.Abort.INSTANCE;
        }

        @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDContentModelHandler
        public void separator(short separator, Augmentations augmentations) throws XNIException {
            throw AbstractDOMParser.Abort.INSTANCE;
        }

        @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDContentModelHandler
        public void occurrence(short occurrence, Augmentations augmentations) throws XNIException {
            throw AbstractDOMParser.Abort.INSTANCE;
        }

        @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDContentModelHandler
        public void endGroup(Augmentations augmentations) throws XNIException {
            throw AbstractDOMParser.Abort.INSTANCE;
        }

        @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDContentModelHandler
        public void endContentModel(Augmentations augmentations) throws XNIException {
            throw AbstractDOMParser.Abort.INSTANCE;
        }

        @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDContentModelHandler
        public void setDTDContentModelSource(XMLDTDContentModelSource source) {
            this.dtdContentSource = source;
        }

        @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDContentModelHandler
        public XMLDTDContentModelSource getDTDContentModelSource() {
            return this.dtdContentSource;
        }
    }

    private static DOMException newFeatureNotFoundError(String name) throws MissingResourceException {
        String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "FEATURE_NOT_FOUND", new Object[]{name});
        return new DOMException((short) 8, msg);
    }

    private static DOMException newTypeMismatchError(String name) throws MissingResourceException {
        String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "TYPE_MISMATCH_ERR", new Object[]{name});
        return new DOMException((short) 17, msg);
    }
}
