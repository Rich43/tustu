package com.sun.org.apache.xerces.internal.dom;

import com.sun.org.apache.xerces.internal.impl.Constants;
import com.sun.org.apache.xerces.internal.impl.XMLEntityManager;
import com.sun.org.apache.xerces.internal.impl.XMLErrorReporter;
import com.sun.org.apache.xerces.internal.impl.dv.DTDDVFactory;
import com.sun.org.apache.xerces.internal.impl.msg.XMLMessageFormatter;
import com.sun.org.apache.xerces.internal.impl.validation.ValidationManager;
import com.sun.org.apache.xerces.internal.impl.xs.XSMessageFormatter;
import com.sun.org.apache.xerces.internal.util.DOMEntityResolverWrapper;
import com.sun.org.apache.xerces.internal.util.DOMErrorHandlerWrapper;
import com.sun.org.apache.xerces.internal.util.MessageFormatter;
import com.sun.org.apache.xerces.internal.util.ParserConfigurationSettings;
import com.sun.org.apache.xerces.internal.util.PropertyState;
import com.sun.org.apache.xerces.internal.util.SymbolTable;
import com.sun.org.apache.xerces.internal.utils.ObjectFactory;
import com.sun.org.apache.xerces.internal.utils.XMLSecurityManager;
import com.sun.org.apache.xerces.internal.utils.XMLSecurityPropertyManager;
import com.sun.org.apache.xerces.internal.xni.XMLDTDContentModelHandler;
import com.sun.org.apache.xerces.internal.xni.XMLDTDHandler;
import com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler;
import com.sun.org.apache.xerces.internal.xni.XNIException;
import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool;
import com.sun.org.apache.xerces.internal.xni.parser.XMLComponent;
import com.sun.org.apache.xerces.internal.xni.parser.XMLComponentManager;
import com.sun.org.apache.xerces.internal.xni.parser.XMLConfigurationException;
import com.sun.org.apache.xerces.internal.xni.parser.XMLEntityResolver;
import com.sun.org.apache.xerces.internal.xni.parser.XMLErrorHandler;
import com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;
import com.sun.org.apache.xerces.internal.xni.parser.XMLParserConfiguration;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Vector;
import jdk.xml.internal.JdkXmlUtils;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMErrorHandler;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMStringList;
import org.w3c.dom.ls.LSResourceResolver;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/dom/DOMConfigurationImpl.class */
public class DOMConfigurationImpl extends ParserConfigurationSettings implements XMLParserConfiguration, DOMConfiguration {
    protected static final String XERCES_VALIDATION = "http://xml.org/sax/features/validation";
    protected static final String XERCES_NAMESPACES = "http://xml.org/sax/features/namespaces";
    protected static final String SCHEMA = "http://apache.org/xml/features/validation/schema";
    protected static final String SCHEMA_FULL_CHECKING = "http://apache.org/xml/features/validation/schema-full-checking";
    protected static final String DYNAMIC_VALIDATION = "http://apache.org/xml/features/validation/dynamic";
    protected static final String NORMALIZE_DATA = "http://apache.org/xml/features/validation/schema/normalized-value";
    protected static final String SEND_PSVI = "http://apache.org/xml/features/validation/schema/augment-psvi";
    protected static final String DTD_VALIDATOR_FACTORY_PROPERTY = "http://apache.org/xml/properties/internal/datatype-validator-factory";
    protected static final String NAMESPACE_GROWTH = "http://apache.org/xml/features/namespace-growth";
    protected static final String TOLERATE_DUPLICATES = "http://apache.org/xml/features/internal/tolerate-duplicates";
    protected static final String ENTITY_MANAGER = "http://apache.org/xml/properties/internal/entity-manager";
    protected static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
    protected static final String XML_STRING = "http://xml.org/sax/properties/xml-string";
    protected static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
    protected static final String GRAMMAR_POOL = "http://apache.org/xml/properties/internal/grammar-pool";
    protected static final String ERROR_HANDLER = "http://apache.org/xml/properties/internal/error-handler";
    protected static final String ENTITY_RESOLVER = "http://apache.org/xml/properties/internal/entity-resolver";
    protected static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
    protected static final String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";
    protected static final String VALIDATION_MANAGER = "http://apache.org/xml/properties/internal/validation-manager";
    protected static final String SCHEMA_DV_FACTORY = "http://apache.org/xml/properties/internal/validation/schema/dv-factory";
    private static final String SECURITY_MANAGER = "http://apache.org/xml/properties/security-manager";
    private static final String XML_SECURITY_PROPERTY_MANAGER = "http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager";
    XMLDocumentHandler fDocumentHandler;
    protected short features;
    protected static final short NAMESPACES = 1;
    protected static final short DTNORMALIZATION = 2;
    protected static final short ENTITIES = 4;
    protected static final short CDATA = 8;
    protected static final short SPLITCDATA = 16;
    protected static final short COMMENTS = 32;
    protected static final short VALIDATE = 64;
    protected static final short PSVI = 128;
    protected static final short WELLFORMED = 256;
    protected static final short NSDECL = 512;
    protected static final short INFOSET_TRUE_PARAMS = 801;
    protected static final short INFOSET_FALSE_PARAMS = 14;
    protected static final short INFOSET_MASK = 815;
    protected SymbolTable fSymbolTable;
    protected ArrayList fComponents;
    protected ValidationManager fValidationManager;
    protected Locale fLocale;
    protected XMLErrorReporter fErrorReporter;
    protected final DOMErrorHandlerWrapper fErrorHandlerWrapper;
    private DOMStringList fRecognizedParameters;

    protected DOMConfigurationImpl() {
        this(null, null);
    }

    protected DOMConfigurationImpl(SymbolTable symbolTable) {
        this(symbolTable, null);
    }

    protected DOMConfigurationImpl(SymbolTable symbolTable, XMLComponentManager parentSettings) throws XMLConfigurationException {
        super(parentSettings);
        this.features = (short) 0;
        this.fErrorHandlerWrapper = new DOMErrorHandlerWrapper();
        this.fFeatures = new HashMap();
        this.fProperties = new HashMap();
        String[] recognizedFeatures = {XERCES_VALIDATION, "http://xml.org/sax/features/namespaces", SCHEMA, SCHEMA_FULL_CHECKING, DYNAMIC_VALIDATION, NORMALIZE_DATA, SEND_PSVI, NAMESPACE_GROWTH, TOLERATE_DUPLICATES, JdkXmlUtils.OVERRIDE_PARSER};
        addRecognizedFeatures(recognizedFeatures);
        setFeature(XERCES_VALIDATION, false);
        setFeature(SCHEMA, false);
        setFeature(SCHEMA_FULL_CHECKING, false);
        setFeature(DYNAMIC_VALIDATION, false);
        setFeature(NORMALIZE_DATA, false);
        setFeature("http://xml.org/sax/features/namespaces", true);
        setFeature(SEND_PSVI, true);
        setFeature(NAMESPACE_GROWTH, false);
        setFeature(JdkXmlUtils.OVERRIDE_PARSER, JdkXmlUtils.OVERRIDE_PARSER_DEFAULT);
        String[] recognizedProperties = {XML_STRING, "http://apache.org/xml/properties/internal/symbol-table", ERROR_HANDLER, "http://apache.org/xml/properties/internal/entity-resolver", "http://apache.org/xml/properties/internal/error-reporter", ENTITY_MANAGER, VALIDATION_MANAGER, "http://apache.org/xml/properties/internal/grammar-pool", "http://java.sun.com/xml/jaxp/properties/schemaSource", "http://java.sun.com/xml/jaxp/properties/schemaLanguage", DTD_VALIDATOR_FACTORY_PROPERTY, SCHEMA_DV_FACTORY, "http://apache.org/xml/properties/security-manager", "http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager"};
        addRecognizedProperties(recognizedProperties);
        this.features = (short) (this.features | 1);
        this.features = (short) (this.features | 4);
        this.features = (short) (this.features | 32);
        this.features = (short) (this.features | 8);
        this.features = (short) (this.features | 16);
        this.features = (short) (this.features | 256);
        this.features = (short) (this.features | 512);
        this.fSymbolTable = symbolTable == null ? new SymbolTable() : symbolTable;
        this.fComponents = new ArrayList();
        setProperty("http://apache.org/xml/properties/internal/symbol-table", this.fSymbolTable);
        this.fErrorReporter = new XMLErrorReporter();
        setProperty("http://apache.org/xml/properties/internal/error-reporter", this.fErrorReporter);
        addComponent(this.fErrorReporter);
        setProperty(DTD_VALIDATOR_FACTORY_PROPERTY, DTDDVFactory.getInstance());
        XMLEntityManager manager = new XMLEntityManager();
        setProperty(ENTITY_MANAGER, manager);
        addComponent(manager);
        this.fValidationManager = createValidationManager();
        setProperty(VALIDATION_MANAGER, this.fValidationManager);
        setProperty("http://apache.org/xml/properties/security-manager", new XMLSecurityManager(true));
        setProperty("http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager", new XMLSecurityPropertyManager());
        if (this.fErrorReporter.getMessageFormatter("http://www.w3.org/TR/1998/REC-xml-19980210") == null) {
            XMLMessageFormatter xmft = new XMLMessageFormatter();
            this.fErrorReporter.putMessageFormatter("http://www.w3.org/TR/1998/REC-xml-19980210", xmft);
            this.fErrorReporter.putMessageFormatter("http://www.w3.org/TR/1999/REC-xml-names-19990114", xmft);
        }
        if (this.fErrorReporter.getMessageFormatter(XSMessageFormatter.SCHEMA_DOMAIN) == null) {
            MessageFormatter xmft2 = null;
            try {
                xmft2 = (MessageFormatter) ObjectFactory.newInstance("com.sun.org.apache.xerces.internal.impl.xs.XSMessageFormatter", true);
            } catch (Exception e2) {
            }
            if (xmft2 != null) {
                this.fErrorReporter.putMessageFormatter(XSMessageFormatter.SCHEMA_DOMAIN, xmft2);
            }
        }
        try {
            setLocale(Locale.getDefault());
        } catch (XNIException e3) {
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLParserConfiguration
    public void parse(XMLInputSource inputSource) throws IOException, XNIException {
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLParserConfiguration
    public void setDocumentHandler(XMLDocumentHandler documentHandler) {
        this.fDocumentHandler = documentHandler;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLParserConfiguration
    public XMLDocumentHandler getDocumentHandler() {
        return this.fDocumentHandler;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLParserConfiguration
    public void setDTDHandler(XMLDTDHandler dtdHandler) {
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLParserConfiguration
    public XMLDTDHandler getDTDHandler() {
        return null;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLParserConfiguration
    public void setDTDContentModelHandler(XMLDTDContentModelHandler handler) {
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLParserConfiguration
    public XMLDTDContentModelHandler getDTDContentModelHandler() {
        return null;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLParserConfiguration
    public void setEntityResolver(XMLEntityResolver resolver) {
        if (resolver != null) {
            this.fProperties.put("http://apache.org/xml/properties/internal/entity-resolver", resolver);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLParserConfiguration
    public XMLEntityResolver getEntityResolver() {
        return (XMLEntityResolver) this.fProperties.get("http://apache.org/xml/properties/internal/entity-resolver");
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLParserConfiguration
    public void setErrorHandler(XMLErrorHandler errorHandler) {
        if (errorHandler != null) {
            this.fProperties.put(ERROR_HANDLER, errorHandler);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLParserConfiguration
    public XMLErrorHandler getErrorHandler() {
        return (XMLErrorHandler) this.fProperties.get(ERROR_HANDLER);
    }

    @Override // com.sun.org.apache.xerces.internal.util.ParserConfigurationSettings, com.sun.org.apache.xerces.internal.xni.parser.XMLParserConfiguration
    public void setFeature(String featureId, boolean state) throws XMLConfigurationException {
        super.setFeature(featureId, state);
    }

    @Override // com.sun.org.apache.xerces.internal.util.ParserConfigurationSettings, com.sun.org.apache.xerces.internal.xni.parser.XMLParserConfiguration
    public void setProperty(String propertyId, Object value) throws XMLConfigurationException {
        super.setProperty(propertyId, value);
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLParserConfiguration
    public void setLocale(Locale locale) throws XNIException {
        this.fLocale = locale;
        this.fErrorReporter.setLocale(locale);
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLParserConfiguration
    public Locale getLocale() {
        return this.fLocale;
    }

    @Override // org.w3c.dom.DOMConfiguration
    public void setParameter(String name, Object value) throws DOMException, MissingResourceException, XMLConfigurationException {
        boolean found = true;
        if (value instanceof Boolean) {
            boolean state = ((Boolean) value).booleanValue();
            if (name.equalsIgnoreCase(Constants.DOM_COMMENTS)) {
                this.features = (short) (state ? this.features | 32 : this.features & (-33));
            } else if (name.equalsIgnoreCase(Constants.DOM_DATATYPE_NORMALIZATION)) {
                setFeature(NORMALIZE_DATA, state);
                this.features = (short) (state ? this.features | 2 : this.features & (-3));
                if (state) {
                    this.features = (short) (this.features | 64);
                }
            } else if (name.equalsIgnoreCase("namespaces")) {
                this.features = (short) (state ? this.features | 1 : this.features & (-2));
            } else if (name.equalsIgnoreCase(Constants.DOM_CDATA_SECTIONS)) {
                this.features = (short) (state ? this.features | 8 : this.features & (-9));
            } else if (name.equalsIgnoreCase(Constants.DOM_ENTITIES)) {
                this.features = (short) (state ? this.features | 4 : this.features & (-5));
            } else if (name.equalsIgnoreCase(Constants.DOM_SPLIT_CDATA)) {
                this.features = (short) (state ? this.features | 16 : this.features & (-17));
            } else if (name.equalsIgnoreCase(Constants.DOM_VALIDATE)) {
                this.features = (short) (state ? this.features | 64 : this.features & (-65));
            } else if (name.equalsIgnoreCase(Constants.DOM_WELLFORMED)) {
                this.features = (short) (state ? this.features | 256 : this.features & (-257));
            } else if (name.equalsIgnoreCase(Constants.DOM_NAMESPACE_DECLARATIONS)) {
                this.features = (short) (state ? this.features | 512 : this.features & (-513));
            } else if (name.equalsIgnoreCase(Constants.DOM_INFOSET)) {
                if (state) {
                    this.features = (short) (this.features | 801);
                    this.features = (short) (this.features & (-15));
                    setFeature(NORMALIZE_DATA, false);
                }
            } else if (name.equalsIgnoreCase(Constants.DOM_NORMALIZE_CHARACTERS) || name.equalsIgnoreCase(Constants.DOM_CANONICAL_FORM) || name.equalsIgnoreCase(Constants.DOM_VALIDATE_IF_SCHEMA) || name.equalsIgnoreCase(Constants.DOM_CHECK_CHAR_NORMALIZATION)) {
                if (state) {
                    String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "FEATURE_NOT_SUPPORTED", new Object[]{name});
                    throw new DOMException((short) 9, msg);
                }
            } else if (name.equalsIgnoreCase(Constants.DOM_ELEMENT_CONTENT_WHITESPACE)) {
                if (!state) {
                    String msg2 = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "FEATURE_NOT_SUPPORTED", new Object[]{name});
                    throw new DOMException((short) 9, msg2);
                }
            } else if (name.equalsIgnoreCase(SEND_PSVI)) {
                if (!state) {
                    String msg3 = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "FEATURE_NOT_SUPPORTED", new Object[]{name});
                    throw new DOMException((short) 9, msg3);
                }
            } else if (name.equalsIgnoreCase(Constants.DOM_PSVI)) {
                this.features = (short) (state ? this.features | 128 : this.features & (-129));
            } else {
                found = false;
            }
        }
        if (!found || !(value instanceof Boolean)) {
            if (name.equalsIgnoreCase(Constants.DOM_ERROR_HANDLER)) {
                if ((value instanceof DOMErrorHandler) || value == null) {
                    this.fErrorHandlerWrapper.setErrorHandler((DOMErrorHandler) value);
                    setErrorHandler(this.fErrorHandlerWrapper);
                    return;
                } else {
                    String msg4 = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "TYPE_MISMATCH_ERR", new Object[]{name});
                    throw new DOMException((short) 17, msg4);
                }
            }
            if (name.equalsIgnoreCase(Constants.DOM_RESOURCE_RESOLVER)) {
                if ((value instanceof LSResourceResolver) || value == null) {
                    try {
                        setEntityResolver(new DOMEntityResolverWrapper((LSResourceResolver) value));
                        return;
                    } catch (XMLConfigurationException e2) {
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
                        setProperty("http://java.sun.com/xml/jaxp/properties/schemaSource", value);
                        return;
                    } catch (XMLConfigurationException e3) {
                        return;
                    }
                } else {
                    String msg6 = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "TYPE_MISMATCH_ERR", new Object[]{name});
                    throw new DOMException((short) 17, msg6);
                }
            }
            if (name.equalsIgnoreCase(Constants.DOM_SCHEMA_TYPE)) {
                if ((value instanceof String) || value == null) {
                    try {
                        if (value == null) {
                            setProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage", null);
                        } else if (value.equals(Constants.NS_XMLSCHEMA)) {
                            setProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage", Constants.NS_XMLSCHEMA);
                        } else if (value.equals(Constants.NS_DTD)) {
                            setProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage", Constants.NS_DTD);
                        }
                        return;
                    } catch (XMLConfigurationException e4) {
                        return;
                    }
                }
                String msg7 = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "TYPE_MISMATCH_ERR", new Object[]{name});
                throw new DOMException((short) 17, msg7);
            }
            if (name.equalsIgnoreCase("http://apache.org/xml/properties/internal/symbol-table")) {
                if (value instanceof SymbolTable) {
                    setProperty("http://apache.org/xml/properties/internal/symbol-table", value);
                    return;
                } else {
                    String msg8 = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "TYPE_MISMATCH_ERR", new Object[]{name});
                    throw new DOMException((short) 17, msg8);
                }
            }
            if (name.equalsIgnoreCase("http://apache.org/xml/properties/internal/grammar-pool")) {
                if (value instanceof XMLGrammarPool) {
                    setProperty("http://apache.org/xml/properties/internal/grammar-pool", value);
                    return;
                } else {
                    String msg9 = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "TYPE_MISMATCH_ERR", new Object[]{name});
                    throw new DOMException((short) 17, msg9);
                }
            }
            String msg10 = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "FEATURE_NOT_FOUND", new Object[]{name});
            throw new DOMException((short) 8, msg10);
        }
    }

    @Override // org.w3c.dom.DOMConfiguration
    public Object getParameter(String name) throws DOMException, MissingResourceException {
        if (name.equalsIgnoreCase(Constants.DOM_COMMENTS)) {
            return (this.features & 32) != 0 ? Boolean.TRUE : Boolean.FALSE;
        }
        if (name.equalsIgnoreCase("namespaces")) {
            return (this.features & 1) != 0 ? Boolean.TRUE : Boolean.FALSE;
        }
        if (name.equalsIgnoreCase(Constants.DOM_DATATYPE_NORMALIZATION)) {
            return (this.features & 2) != 0 ? Boolean.TRUE : Boolean.FALSE;
        }
        if (name.equalsIgnoreCase(Constants.DOM_CDATA_SECTIONS)) {
            return (this.features & 8) != 0 ? Boolean.TRUE : Boolean.FALSE;
        }
        if (name.equalsIgnoreCase(Constants.DOM_ENTITIES)) {
            return (this.features & 4) != 0 ? Boolean.TRUE : Boolean.FALSE;
        }
        if (name.equalsIgnoreCase(Constants.DOM_SPLIT_CDATA)) {
            return (this.features & 16) != 0 ? Boolean.TRUE : Boolean.FALSE;
        }
        if (name.equalsIgnoreCase(Constants.DOM_VALIDATE)) {
            return (this.features & 64) != 0 ? Boolean.TRUE : Boolean.FALSE;
        }
        if (name.equalsIgnoreCase(Constants.DOM_WELLFORMED)) {
            return (this.features & 256) != 0 ? Boolean.TRUE : Boolean.FALSE;
        }
        if (name.equalsIgnoreCase(Constants.DOM_NAMESPACE_DECLARATIONS)) {
            return (this.features & 512) != 0 ? Boolean.TRUE : Boolean.FALSE;
        }
        if (name.equalsIgnoreCase(Constants.DOM_INFOSET)) {
            return (this.features & INFOSET_MASK) == 801 ? Boolean.TRUE : Boolean.FALSE;
        }
        if (name.equalsIgnoreCase(Constants.DOM_NORMALIZE_CHARACTERS) || name.equalsIgnoreCase(Constants.DOM_CANONICAL_FORM) || name.equalsIgnoreCase(Constants.DOM_VALIDATE_IF_SCHEMA) || name.equalsIgnoreCase(Constants.DOM_CHECK_CHAR_NORMALIZATION)) {
            return Boolean.FALSE;
        }
        if (name.equalsIgnoreCase(SEND_PSVI)) {
            return Boolean.TRUE;
        }
        if (name.equalsIgnoreCase(Constants.DOM_PSVI)) {
            return (this.features & 128) != 0 ? Boolean.TRUE : Boolean.FALSE;
        }
        if (name.equalsIgnoreCase(Constants.DOM_ELEMENT_CONTENT_WHITESPACE)) {
            return Boolean.TRUE;
        }
        if (name.equalsIgnoreCase(Constants.DOM_ERROR_HANDLER)) {
            return this.fErrorHandlerWrapper.getErrorHandler();
        }
        if (name.equalsIgnoreCase(Constants.DOM_RESOURCE_RESOLVER)) {
            XMLEntityResolver entityResolver = getEntityResolver();
            if (entityResolver != null && (entityResolver instanceof DOMEntityResolverWrapper)) {
                return ((DOMEntityResolverWrapper) entityResolver).getEntityResolver();
            }
            return null;
        }
        if (name.equalsIgnoreCase(Constants.DOM_SCHEMA_TYPE)) {
            return getProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage");
        }
        if (name.equalsIgnoreCase(Constants.DOM_SCHEMA_LOCATION)) {
            return getProperty("http://java.sun.com/xml/jaxp/properties/schemaSource");
        }
        if (name.equalsIgnoreCase("http://apache.org/xml/properties/internal/symbol-table")) {
            return getProperty("http://apache.org/xml/properties/internal/symbol-table");
        }
        if (name.equalsIgnoreCase("http://apache.org/xml/properties/internal/grammar-pool")) {
            return getProperty("http://apache.org/xml/properties/internal/grammar-pool");
        }
        String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "FEATURE_NOT_FOUND", new Object[]{name});
        throw new DOMException((short) 8, msg);
    }

    @Override // org.w3c.dom.DOMConfiguration
    public boolean canSetParameter(String name, Object value) {
        if (value == null) {
            return true;
        }
        if (!(value instanceof Boolean)) {
            return name.equalsIgnoreCase(Constants.DOM_ERROR_HANDLER) ? value instanceof DOMErrorHandler : name.equalsIgnoreCase(Constants.DOM_RESOURCE_RESOLVER) ? value instanceof LSResourceResolver : name.equalsIgnoreCase(Constants.DOM_SCHEMA_LOCATION) ? value instanceof String : name.equalsIgnoreCase(Constants.DOM_SCHEMA_TYPE) ? (value instanceof String) && value.equals(Constants.NS_XMLSCHEMA) : name.equalsIgnoreCase("http://apache.org/xml/properties/internal/symbol-table") ? value instanceof SymbolTable : name.equalsIgnoreCase("http://apache.org/xml/properties/internal/grammar-pool") && (value instanceof XMLGrammarPool);
        }
        if (name.equalsIgnoreCase(Constants.DOM_COMMENTS) || name.equalsIgnoreCase(Constants.DOM_DATATYPE_NORMALIZATION) || name.equalsIgnoreCase(Constants.DOM_CDATA_SECTIONS) || name.equalsIgnoreCase(Constants.DOM_ENTITIES) || name.equalsIgnoreCase(Constants.DOM_SPLIT_CDATA) || name.equalsIgnoreCase("namespaces") || name.equalsIgnoreCase(Constants.DOM_VALIDATE) || name.equalsIgnoreCase(Constants.DOM_WELLFORMED) || name.equalsIgnoreCase(Constants.DOM_INFOSET) || name.equalsIgnoreCase(Constants.DOM_NAMESPACE_DECLARATIONS)) {
            return true;
        }
        return (name.equalsIgnoreCase(Constants.DOM_NORMALIZE_CHARACTERS) || name.equalsIgnoreCase(Constants.DOM_CANONICAL_FORM) || name.equalsIgnoreCase(Constants.DOM_VALIDATE_IF_SCHEMA) || name.equalsIgnoreCase(Constants.DOM_CHECK_CHAR_NORMALIZATION)) ? !value.equals(Boolean.TRUE) : (name.equalsIgnoreCase(Constants.DOM_ELEMENT_CONTENT_WHITESPACE) || name.equalsIgnoreCase(SEND_PSVI)) && value.equals(Boolean.TRUE);
    }

    @Override // org.w3c.dom.DOMConfiguration
    public DOMStringList getParameterNames() {
        if (this.fRecognizedParameters == null) {
            Vector parameters = new Vector();
            parameters.add(Constants.DOM_COMMENTS);
            parameters.add(Constants.DOM_DATATYPE_NORMALIZATION);
            parameters.add(Constants.DOM_CDATA_SECTIONS);
            parameters.add(Constants.DOM_ENTITIES);
            parameters.add(Constants.DOM_SPLIT_CDATA);
            parameters.add("namespaces");
            parameters.add(Constants.DOM_VALIDATE);
            parameters.add(Constants.DOM_INFOSET);
            parameters.add(Constants.DOM_NORMALIZE_CHARACTERS);
            parameters.add(Constants.DOM_CANONICAL_FORM);
            parameters.add(Constants.DOM_VALIDATE_IF_SCHEMA);
            parameters.add(Constants.DOM_CHECK_CHAR_NORMALIZATION);
            parameters.add(Constants.DOM_WELLFORMED);
            parameters.add(Constants.DOM_NAMESPACE_DECLARATIONS);
            parameters.add(Constants.DOM_ELEMENT_CONTENT_WHITESPACE);
            parameters.add(Constants.DOM_ERROR_HANDLER);
            parameters.add(Constants.DOM_SCHEMA_TYPE);
            parameters.add(Constants.DOM_SCHEMA_LOCATION);
            parameters.add(Constants.DOM_RESOURCE_RESOLVER);
            parameters.add("http://apache.org/xml/properties/internal/grammar-pool");
            parameters.add("http://apache.org/xml/properties/internal/symbol-table");
            parameters.add(SEND_PSVI);
            this.fRecognizedParameters = new DOMStringListImpl(parameters);
        }
        return this.fRecognizedParameters;
    }

    protected void reset() throws XNIException {
        if (this.fValidationManager != null) {
            this.fValidationManager.reset();
        }
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

    protected void addComponent(XMLComponent component) {
        if (this.fComponents.contains(component)) {
            return;
        }
        this.fComponents.add(component);
        String[] recognizedFeatures = component.getRecognizedFeatures();
        addRecognizedFeatures(recognizedFeatures);
        String[] recognizedProperties = component.getRecognizedProperties();
        addRecognizedProperties(recognizedProperties);
    }

    protected ValidationManager createValidationManager() {
        return new ValidationManager();
    }
}
