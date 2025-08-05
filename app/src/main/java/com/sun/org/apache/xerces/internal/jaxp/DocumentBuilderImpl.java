package com.sun.org.apache.xerces.internal.jaxp;

import com.sun.org.apache.xerces.internal.dom.DOMImplementationImpl;
import com.sun.org.apache.xerces.internal.dom.DOMMessageFormatter;
import com.sun.org.apache.xerces.internal.dom.DocumentImpl;
import com.sun.org.apache.xerces.internal.impl.Constants;
import com.sun.org.apache.xerces.internal.impl.validation.ValidationManager;
import com.sun.org.apache.xerces.internal.impl.xs.XMLSchemaValidator;
import com.sun.org.apache.xerces.internal.jaxp.validation.XSGrammarPoolContainer;
import com.sun.org.apache.xerces.internal.parsers.DOMParser;
import com.sun.org.apache.xerces.internal.utils.XMLSecurityManager;
import com.sun.org.apache.xerces.internal.utils.XMLSecurityPropertyManager;
import com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler;
import com.sun.org.apache.xerces.internal.xni.parser.XMLComponent;
import com.sun.org.apache.xerces.internal.xni.parser.XMLComponentManager;
import com.sun.org.apache.xerces.internal.xni.parser.XMLConfigurationException;
import com.sun.org.apache.xerces.internal.xni.parser.XMLDocumentSource;
import com.sun.org.apache.xerces.internal.xni.parser.XMLParserConfiguration;
import java.io.IOException;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.validation.Schema;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/jaxp/DocumentBuilderImpl.class */
public class DocumentBuilderImpl extends DocumentBuilder implements JAXPConstants {
    private static final String NAMESPACES_FEATURE = "http://xml.org/sax/features/namespaces";
    private static final String INCLUDE_IGNORABLE_WHITESPACE = "http://apache.org/xml/features/dom/include-ignorable-whitespace";
    private static final String CREATE_ENTITY_REF_NODES_FEATURE = "http://apache.org/xml/features/dom/create-entity-ref-nodes";
    private static final String INCLUDE_COMMENTS_FEATURE = "http://apache.org/xml/features/include-comments";
    private static final String CREATE_CDATA_NODES_FEATURE = "http://apache.org/xml/features/create-cdata-nodes";
    private static final String XINCLUDE_FEATURE = "http://apache.org/xml/features/xinclude";
    private static final String XMLSCHEMA_VALIDATION_FEATURE = "http://apache.org/xml/features/validation/schema";
    private static final String VALIDATION_FEATURE = "http://xml.org/sax/features/validation";
    private static final String SECURITY_MANAGER = "http://apache.org/xml/properties/security-manager";
    private static final String XML_SECURITY_PROPERTY_MANAGER = "http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager";
    public static final String ACCESS_EXTERNAL_DTD = "http://javax.xml.XMLConstants/property/accessExternalDTD";
    public static final String ACCESS_EXTERNAL_SCHEMA = "http://javax.xml.XMLConstants/property/accessExternalSchema";
    private final DOMParser domParser;
    private final Schema grammar;
    private final XMLComponent fSchemaValidator;
    private final XMLComponentManager fSchemaValidatorComponentManager;
    private final ValidationManager fSchemaValidationManager;
    private final UnparsedEntityHandler fUnparsedEntityHandler;
    private final ErrorHandler fInitErrorHandler;
    private final EntityResolver fInitEntityResolver;
    private XMLSecurityManager fSecurityManager;
    private XMLSecurityPropertyManager fSecurityPropertyMgr;

    DocumentBuilderImpl(DocumentBuilderFactoryImpl dbf, Map<String, Object> dbfAttrs, Map<String, Boolean> features) throws SAXNotRecognizedException, SAXNotSupportedException {
        this(dbf, dbfAttrs, features, false);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v29, types: [com.sun.org.apache.xerces.internal.xni.parser.XMLComponent] */
    /* JADX WARN: Type inference failed for: r1v31, types: [com.sun.org.apache.xerces.internal.xni.parser.XMLComponent] */
    /* JADX WARN: Type inference failed for: r1v40, types: [com.sun.org.apache.xerces.internal.xni.parser.XMLComponent] */
    DocumentBuilderImpl(DocumentBuilderFactoryImpl dbf, Map<String, Object> dbfAttrs, Map<String, Boolean> features, boolean secureProcessing) throws SAXNotRecognizedException, SAXNotSupportedException {
        XMLDocumentHandler jAXPValidatorComponent;
        Boolean temp;
        this.domParser = new DOMParser();
        if (dbf.isValidating()) {
            this.fInitErrorHandler = new DefaultValidationErrorHandler(this.domParser.getXMLParserConfiguration().getLocale());
            setErrorHandler(this.fInitErrorHandler);
        } else {
            this.fInitErrorHandler = this.domParser.getErrorHandler();
        }
        this.domParser.setFeature(VALIDATION_FEATURE, dbf.isValidating());
        this.domParser.setFeature("http://xml.org/sax/features/namespaces", dbf.isNamespaceAware());
        this.domParser.setFeature(INCLUDE_IGNORABLE_WHITESPACE, !dbf.isIgnoringElementContentWhitespace());
        this.domParser.setFeature(CREATE_ENTITY_REF_NODES_FEATURE, !dbf.isExpandEntityReferences());
        this.domParser.setFeature(INCLUDE_COMMENTS_FEATURE, !dbf.isIgnoringComments());
        this.domParser.setFeature(CREATE_CDATA_NODES_FEATURE, !dbf.isCoalescing());
        if (dbf.isXIncludeAware()) {
            this.domParser.setFeature(XINCLUDE_FEATURE, true);
        }
        this.fSecurityPropertyMgr = new XMLSecurityPropertyManager();
        this.domParser.setProperty("http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager", this.fSecurityPropertyMgr);
        this.fSecurityManager = new XMLSecurityManager(secureProcessing);
        this.domParser.setProperty("http://apache.org/xml/properties/security-manager", this.fSecurityManager);
        if (secureProcessing && features != null && (temp = features.get("http://javax.xml.XMLConstants/feature/secure-processing")) != null && temp.booleanValue() && Constants.IS_JDK8_OR_ABOVE) {
            this.fSecurityPropertyMgr.setValue(XMLSecurityPropertyManager.Property.ACCESS_EXTERNAL_DTD, XMLSecurityPropertyManager.State.FSP, "");
            this.fSecurityPropertyMgr.setValue(XMLSecurityPropertyManager.Property.ACCESS_EXTERNAL_SCHEMA, XMLSecurityPropertyManager.State.FSP, "");
        }
        this.grammar = dbf.getSchema();
        if (this.grammar != null) {
            XMLParserConfiguration config = this.domParser.getXMLParserConfiguration();
            if (this.grammar instanceof XSGrammarPoolContainer) {
                jAXPValidatorComponent = new XMLSchemaValidator();
                this.fSchemaValidationManager = new ValidationManager();
                this.fUnparsedEntityHandler = new UnparsedEntityHandler(this.fSchemaValidationManager);
                config.setDTDHandler(this.fUnparsedEntityHandler);
                this.fUnparsedEntityHandler.setDTDHandler(this.domParser);
                this.domParser.setDTDSource(this.fUnparsedEntityHandler);
                this.fSchemaValidatorComponentManager = new SchemaValidatorConfiguration(config, (XSGrammarPoolContainer) this.grammar, this.fSchemaValidationManager);
            } else {
                jAXPValidatorComponent = new JAXPValidatorComponent(this.grammar.newValidatorHandler());
                this.fSchemaValidationManager = null;
                this.fUnparsedEntityHandler = null;
                this.fSchemaValidatorComponentManager = config;
            }
            config.addRecognizedFeatures(jAXPValidatorComponent.getRecognizedFeatures());
            config.addRecognizedProperties(jAXPValidatorComponent.getRecognizedProperties());
            setFeatures(features);
            config.setDocumentHandler(jAXPValidatorComponent);
            ((XMLDocumentSource) jAXPValidatorComponent).setDocumentHandler(this.domParser);
            this.domParser.setDocumentSource((XMLDocumentSource) jAXPValidatorComponent);
            this.fSchemaValidator = jAXPValidatorComponent;
        } else {
            this.fSchemaValidationManager = null;
            this.fUnparsedEntityHandler = null;
            this.fSchemaValidatorComponentManager = null;
            this.fSchemaValidator = null;
            setFeatures(features);
        }
        setDocumentBuilderFactoryAttributes(dbfAttrs);
        this.fInitEntityResolver = this.domParser.getEntityResolver();
    }

    private void setFeatures(Map<String, Boolean> features) throws SAXNotRecognizedException, SAXNotSupportedException {
        if (features != null) {
            for (Map.Entry<String, Boolean> entry : features.entrySet()) {
                this.domParser.setFeature(entry.getKey(), entry.getValue().booleanValue());
            }
        }
    }

    private void setDocumentBuilderFactoryAttributes(Map<String, Object> dbfAttrs) throws SAXNotRecognizedException, SAXNotSupportedException {
        if (dbfAttrs == null) {
            return;
        }
        for (Map.Entry<String, Object> entry : dbfAttrs.entrySet()) {
            String name = entry.getKey();
            Object val = entry.getValue();
            if (val instanceof Boolean) {
                this.domParser.setFeature(name, ((Boolean) val).booleanValue());
            } else if (JAXPConstants.JAXP_SCHEMA_LANGUAGE.equals(name)) {
                if ("http://www.w3.org/2001/XMLSchema".equals(val) && isValidating()) {
                    this.domParser.setFeature(XMLSCHEMA_VALIDATION_FEATURE, true);
                    this.domParser.setProperty(JAXPConstants.JAXP_SCHEMA_LANGUAGE, "http://www.w3.org/2001/XMLSchema");
                }
            } else if (JAXPConstants.JAXP_SCHEMA_SOURCE.equals(name)) {
                if (isValidating()) {
                    String value = (String) dbfAttrs.get(JAXPConstants.JAXP_SCHEMA_LANGUAGE);
                    if (value != null && "http://www.w3.org/2001/XMLSchema".equals(value)) {
                        this.domParser.setProperty(name, val);
                    } else {
                        throw new IllegalArgumentException(DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "jaxp-order-not-supported", new Object[]{JAXPConstants.JAXP_SCHEMA_LANGUAGE, JAXPConstants.JAXP_SCHEMA_SOURCE}));
                    }
                } else {
                    continue;
                }
            } else if (this.fSecurityManager == null || !this.fSecurityManager.setLimit(name, XMLSecurityManager.State.APIPROPERTY, val)) {
                if (this.fSecurityPropertyMgr == null || !this.fSecurityPropertyMgr.setValue(name, XMLSecurityPropertyManager.State.APIPROPERTY, val)) {
                    this.domParser.setProperty(name, val);
                }
            }
        }
    }

    @Override // javax.xml.parsers.DocumentBuilder
    public Document newDocument() {
        return new DocumentImpl();
    }

    @Override // javax.xml.parsers.DocumentBuilder
    public DOMImplementation getDOMImplementation() {
        return DOMImplementationImpl.getDOMImplementation();
    }

    @Override // javax.xml.parsers.DocumentBuilder
    public Document parse(InputSource is) throws SAXException, IOException {
        if (is == null) {
            throw new IllegalArgumentException(DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "jaxp-null-input-source", null));
        }
        if (this.fSchemaValidator != null) {
            if (this.fSchemaValidationManager != null) {
                this.fSchemaValidationManager.reset();
                this.fUnparsedEntityHandler.reset();
            }
            resetSchemaValidator();
        }
        this.domParser.parse(is);
        Document doc = this.domParser.getDocument();
        this.domParser.dropDocumentReferences();
        return doc;
    }

    @Override // javax.xml.parsers.DocumentBuilder
    public boolean isNamespaceAware() {
        try {
            return this.domParser.getFeature("http://xml.org/sax/features/namespaces");
        } catch (SAXException x2) {
            throw new IllegalStateException(x2.getMessage());
        }
    }

    @Override // javax.xml.parsers.DocumentBuilder
    public boolean isValidating() {
        try {
            return this.domParser.getFeature(VALIDATION_FEATURE);
        } catch (SAXException x2) {
            throw new IllegalStateException(x2.getMessage());
        }
    }

    @Override // javax.xml.parsers.DocumentBuilder
    public boolean isXIncludeAware() {
        try {
            return this.domParser.getFeature(XINCLUDE_FEATURE);
        } catch (SAXException e2) {
            return false;
        }
    }

    @Override // javax.xml.parsers.DocumentBuilder
    public void setEntityResolver(EntityResolver er) {
        this.domParser.setEntityResolver(er);
    }

    @Override // javax.xml.parsers.DocumentBuilder
    public void setErrorHandler(ErrorHandler eh) {
        this.domParser.setErrorHandler(eh);
    }

    @Override // javax.xml.parsers.DocumentBuilder
    public Schema getSchema() {
        return this.grammar;
    }

    @Override // javax.xml.parsers.DocumentBuilder
    public void reset() {
        if (this.domParser.getErrorHandler() != this.fInitErrorHandler) {
            this.domParser.setErrorHandler(this.fInitErrorHandler);
        }
        if (this.domParser.getEntityResolver() != this.fInitEntityResolver) {
            this.domParser.setEntityResolver(this.fInitEntityResolver);
        }
    }

    DOMParser getDOMParser() {
        return this.domParser;
    }

    private void resetSchemaValidator() throws SAXException {
        try {
            this.fSchemaValidator.reset(this.fSchemaValidatorComponentManager);
        } catch (XMLConfigurationException e2) {
            throw new SAXException(e2);
        }
    }
}
