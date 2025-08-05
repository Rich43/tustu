package com.sun.org.apache.xerces.internal.jaxp.validation;

import com.sun.org.apache.xerces.internal.impl.Constants;
import com.sun.org.apache.xerces.internal.impl.xs.XMLSchemaLoader;
import com.sun.org.apache.xerces.internal.util.DOMEntityResolverWrapper;
import com.sun.org.apache.xerces.internal.util.DOMInputSource;
import com.sun.org.apache.xerces.internal.util.ErrorHandlerWrapper;
import com.sun.org.apache.xerces.internal.util.SAXInputSource;
import com.sun.org.apache.xerces.internal.util.SAXMessageFormatter;
import com.sun.org.apache.xerces.internal.util.StAXInputSource;
import com.sun.org.apache.xerces.internal.util.Status;
import com.sun.org.apache.xerces.internal.util.XMLGrammarPoolImpl;
import com.sun.org.apache.xerces.internal.utils.XMLSecurityManager;
import com.sun.org.apache.xerces.internal.utils.XMLSecurityPropertyManager;
import com.sun.org.apache.xerces.internal.xni.XNIException;
import com.sun.org.apache.xerces.internal.xni.grammars.Grammar;
import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarDescription;
import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool;
import com.sun.org.apache.xerces.internal.xni.parser.XMLConfigurationException;
import com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import javax.xml.stream.XMLEventReader;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stax.StAXSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import jdk.xml.internal.JdkXmlFeatures;
import jdk.xml.internal.JdkXmlUtils;
import org.w3c.dom.Node;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXParseException;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/jaxp/validation/XMLSchemaFactory.class */
public final class XMLSchemaFactory extends SchemaFactory {
    private static final String SCHEMA_FULL_CHECKING = "http://apache.org/xml/features/validation/schema-full-checking";
    private static final String XMLGRAMMAR_POOL = "http://apache.org/xml/properties/internal/grammar-pool";
    private static final String SECURITY_MANAGER = "http://apache.org/xml/properties/security-manager";
    private static final String XML_SECURITY_PROPERTY_MANAGER = "http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager";
    private ErrorHandler fErrorHandler;
    private LSResourceResolver fLSResourceResolver;
    private XMLSecurityManager fSecurityManager;
    private XMLSecurityPropertyManager fSecurityPropertyMgr;
    private final JdkXmlFeatures fXmlFeatures;
    private final boolean fOverrideDefaultParser;
    private final XMLSchemaLoader fXMLSchemaLoader = new XMLSchemaLoader();
    private ErrorHandlerWrapper fErrorHandlerWrapper = new ErrorHandlerWrapper(DraconianErrorHandler.getInstance());
    private final DOMEntityResolverWrapper fDOMEntityResolverWrapper = new DOMEntityResolverWrapper();
    private XMLGrammarPoolWrapper fXMLGrammarPoolWrapper = new XMLGrammarPoolWrapper();

    public XMLSchemaFactory() throws XMLConfigurationException {
        this.fXMLSchemaLoader.setFeature(SCHEMA_FULL_CHECKING, true);
        this.fXMLSchemaLoader.setProperty("http://apache.org/xml/properties/internal/grammar-pool", this.fXMLGrammarPoolWrapper);
        this.fXMLSchemaLoader.setEntityResolver(this.fDOMEntityResolverWrapper);
        this.fXMLSchemaLoader.setErrorHandler(this.fErrorHandlerWrapper);
        this.fSecurityManager = new XMLSecurityManager(true);
        this.fXMLSchemaLoader.setProperty("http://apache.org/xml/properties/security-manager", this.fSecurityManager);
        this.fSecurityPropertyMgr = new XMLSecurityPropertyManager();
        this.fXMLSchemaLoader.setProperty("http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager", this.fSecurityPropertyMgr);
        this.fXmlFeatures = new JdkXmlFeatures(this.fSecurityManager.isSecureProcessing());
        this.fOverrideDefaultParser = this.fXmlFeatures.getFeature(JdkXmlFeatures.XmlFeature.JDK_OVERRIDE_PARSER);
        this.fXMLSchemaLoader.setFeature(JdkXmlUtils.OVERRIDE_PARSER, this.fOverrideDefaultParser);
    }

    @Override // javax.xml.validation.SchemaFactory
    public boolean isSchemaLanguageSupported(String schemaLanguage) {
        if (schemaLanguage == null) {
            throw new NullPointerException(JAXPValidationMessageFormatter.formatMessage(this.fXMLSchemaLoader.getLocale(), "SchemaLanguageNull", null));
        }
        if (schemaLanguage.length() == 0) {
            throw new IllegalArgumentException(JAXPValidationMessageFormatter.formatMessage(this.fXMLSchemaLoader.getLocale(), "SchemaLanguageLengthZero", null));
        }
        return schemaLanguage.equals("http://www.w3.org/2001/XMLSchema");
    }

    @Override // javax.xml.validation.SchemaFactory
    public LSResourceResolver getResourceResolver() {
        return this.fLSResourceResolver;
    }

    @Override // javax.xml.validation.SchemaFactory
    public void setResourceResolver(LSResourceResolver resourceResolver) throws XMLConfigurationException {
        this.fLSResourceResolver = resourceResolver;
        this.fDOMEntityResolverWrapper.setEntityResolver(resourceResolver);
        this.fXMLSchemaLoader.setEntityResolver(this.fDOMEntityResolverWrapper);
    }

    @Override // javax.xml.validation.SchemaFactory
    public ErrorHandler getErrorHandler() {
        return this.fErrorHandler;
    }

    @Override // javax.xml.validation.SchemaFactory
    public void setErrorHandler(ErrorHandler errorHandler) throws XMLConfigurationException {
        this.fErrorHandler = errorHandler;
        this.fErrorHandlerWrapper.setErrorHandler(errorHandler != null ? errorHandler : DraconianErrorHandler.getInstance());
        this.fXMLSchemaLoader.setErrorHandler(this.fErrorHandlerWrapper);
    }

    @Override // javax.xml.validation.SchemaFactory
    public Schema newSchema(Source[] schemas) throws SAXException, XMLConfigurationException {
        AbstractXMLSchema schema;
        XMLGrammarPoolImplExtension pool = new XMLGrammarPoolImplExtension();
        this.fXMLGrammarPoolWrapper.setGrammarPool(pool);
        XMLInputSource[] xmlInputSources = new XMLInputSource[schemas.length];
        for (int i2 = 0; i2 < schemas.length; i2++) {
            Source source = schemas[i2];
            if (source instanceof StreamSource) {
                StreamSource streamSource = (StreamSource) source;
                String publicId = streamSource.getPublicId();
                String systemId = streamSource.getSystemId();
                InputStream inputStream = streamSource.getInputStream();
                Reader reader = streamSource.getReader();
                xmlInputSources[i2] = new XMLInputSource(publicId, systemId, null);
                xmlInputSources[i2].setByteStream(inputStream);
                xmlInputSources[i2].setCharacterStream(reader);
            } else if (source instanceof SAXSource) {
                SAXSource saxSource = (SAXSource) source;
                InputSource inputSource = saxSource.getInputSource();
                if (inputSource == null) {
                    throw new SAXException(JAXPValidationMessageFormatter.formatMessage(this.fXMLSchemaLoader.getLocale(), "SAXSourceNullInputSource", null));
                }
                xmlInputSources[i2] = new SAXInputSource(saxSource.getXMLReader(), inputSource);
            } else if (source instanceof DOMSource) {
                DOMSource domSource = (DOMSource) source;
                Node node = domSource.getNode();
                String systemID = domSource.getSystemId();
                xmlInputSources[i2] = new DOMInputSource(node, systemID);
            } else if (source instanceof StAXSource) {
                StAXSource staxSource = (StAXSource) source;
                XMLEventReader eventReader = staxSource.getXMLEventReader();
                if (eventReader != null) {
                    xmlInputSources[i2] = new StAXInputSource(eventReader);
                } else {
                    xmlInputSources[i2] = new StAXInputSource(staxSource.getXMLStreamReader());
                }
            } else {
                if (source == null) {
                    throw new NullPointerException(JAXPValidationMessageFormatter.formatMessage(this.fXMLSchemaLoader.getLocale(), "SchemaSourceArrayMemberNull", null));
                }
                throw new IllegalArgumentException(JAXPValidationMessageFormatter.formatMessage(this.fXMLSchemaLoader.getLocale(), "SchemaFactorySourceUnrecognized", new Object[]{source.getClass().getName()}));
            }
        }
        try {
            this.fXMLSchemaLoader.loadGrammar(xmlInputSources);
            this.fXMLGrammarPoolWrapper.setGrammarPool(null);
            int grammarCount = pool.getGrammarCount();
            if (grammarCount > 1) {
                schema = new XMLSchema(new ReadOnlyGrammarPool(pool));
            } else if (grammarCount == 1) {
                Grammar[] grammars = pool.retrieveInitialGrammarSet("http://www.w3.org/2001/XMLSchema");
                schema = new SimpleXMLSchema(grammars[0]);
            } else {
                schema = new EmptyXMLSchema();
            }
            propagateFeatures(schema);
            propagateProperties(schema);
            return schema;
        } catch (XNIException e2) {
            throw Util.toSAXException(e2);
        } catch (IOException e3) {
            SAXParseException se = new SAXParseException(e3.getMessage(), null, e3);
            this.fErrorHandler.error(se);
            throw se;
        }
    }

    @Override // javax.xml.validation.SchemaFactory
    public Schema newSchema() throws SAXException, XMLConfigurationException {
        AbstractXMLSchema schema = new WeakReferenceXMLSchema();
        propagateFeatures(schema);
        propagateProperties(schema);
        return schema;
    }

    @Override // javax.xml.validation.SchemaFactory
    public boolean getFeature(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
        if (name == null) {
            throw new NullPointerException(JAXPValidationMessageFormatter.formatMessage(this.fXMLSchemaLoader.getLocale(), "FeatureNameNull", null));
        }
        if (name.equals("http://javax.xml.XMLConstants/feature/secure-processing")) {
            return this.fSecurityManager != null && this.fSecurityManager.isSecureProcessing();
        }
        try {
            return this.fXMLSchemaLoader.getFeature(name);
        } catch (XMLConfigurationException e2) {
            String identifier = e2.getIdentifier();
            if (e2.getType() == Status.NOT_RECOGNIZED) {
                throw new SAXNotRecognizedException(SAXMessageFormatter.formatMessage(this.fXMLSchemaLoader.getLocale(), "feature-not-recognized", new Object[]{identifier}));
            }
            throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(this.fXMLSchemaLoader.getLocale(), "feature-not-supported", new Object[]{identifier}));
        }
    }

    @Override // javax.xml.validation.SchemaFactory
    public Object getProperty(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
        if (name == null) {
            throw new NullPointerException(JAXPValidationMessageFormatter.formatMessage(this.fXMLSchemaLoader.getLocale(), "ProperyNameNull", null));
        }
        if (name.equals("http://apache.org/xml/properties/security-manager")) {
            return this.fSecurityManager;
        }
        if (name.equals("http://apache.org/xml/properties/internal/grammar-pool")) {
            throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(this.fXMLSchemaLoader.getLocale(), "property-not-supported", new Object[]{name}));
        }
        int index = this.fXmlFeatures.getIndex(name);
        if (index > -1) {
            return Boolean.valueOf(this.fXmlFeatures.getFeature(index));
        }
        try {
            return this.fXMLSchemaLoader.getProperty(name);
        } catch (XMLConfigurationException e2) {
            String identifier = e2.getIdentifier();
            if (e2.getType() == Status.NOT_RECOGNIZED) {
                throw new SAXNotRecognizedException(SAXMessageFormatter.formatMessage(this.fXMLSchemaLoader.getLocale(), "property-not-recognized", new Object[]{identifier}));
            }
            throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(this.fXMLSchemaLoader.getLocale(), "property-not-supported", new Object[]{identifier}));
        }
    }

    @Override // javax.xml.validation.SchemaFactory
    public void setFeature(String name, boolean value) throws SAXNotRecognizedException, SAXNotSupportedException, XMLConfigurationException {
        if (name == null) {
            throw new NullPointerException(JAXPValidationMessageFormatter.formatMessage(this.fXMLSchemaLoader.getLocale(), "FeatureNameNull", null));
        }
        if (name.equals("http://javax.xml.XMLConstants/feature/secure-processing")) {
            if (System.getSecurityManager() != null && !value) {
                throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(null, "jaxp-secureprocessing-feature", null));
            }
            this.fSecurityManager.setSecureProcessing(value);
            if (value && Constants.IS_JDK8_OR_ABOVE) {
                this.fSecurityPropertyMgr.setValue(XMLSecurityPropertyManager.Property.ACCESS_EXTERNAL_DTD, XMLSecurityPropertyManager.State.FSP, "");
                this.fSecurityPropertyMgr.setValue(XMLSecurityPropertyManager.Property.ACCESS_EXTERNAL_SCHEMA, XMLSecurityPropertyManager.State.FSP, "");
            }
            this.fXMLSchemaLoader.setProperty("http://apache.org/xml/properties/security-manager", this.fSecurityManager);
            return;
        }
        if (name.equals("http://www.oracle.com/feature/use-service-mechanism") && System.getSecurityManager() != null) {
            return;
        }
        if (this.fXmlFeatures != null && this.fXmlFeatures.setFeature(name, JdkXmlFeatures.State.APIPROPERTY, Boolean.valueOf(value))) {
            if (name.equals(JdkXmlUtils.OVERRIDE_PARSER) || name.equals("http://www.oracle.com/feature/use-service-mechanism")) {
                this.fXMLSchemaLoader.setFeature(name, value);
                return;
            }
            return;
        }
        try {
            this.fXMLSchemaLoader.setFeature(name, value);
        } catch (XMLConfigurationException e2) {
            String identifier = e2.getIdentifier();
            if (e2.getType() == Status.NOT_RECOGNIZED) {
                throw new SAXNotRecognizedException(SAXMessageFormatter.formatMessage(this.fXMLSchemaLoader.getLocale(), "feature-not-recognized", new Object[]{identifier}));
            }
            throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(this.fXMLSchemaLoader.getLocale(), "feature-not-supported", new Object[]{identifier}));
        }
    }

    @Override // javax.xml.validation.SchemaFactory
    public void setProperty(String name, Object object) throws SAXNotRecognizedException, SAXNotSupportedException, XMLConfigurationException {
        if (name == null) {
            throw new NullPointerException(JAXPValidationMessageFormatter.formatMessage(this.fXMLSchemaLoader.getLocale(), "ProperyNameNull", null));
        }
        if (name.equals("http://apache.org/xml/properties/security-manager")) {
            this.fSecurityManager = XMLSecurityManager.convert(object, this.fSecurityManager);
            this.fXMLSchemaLoader.setProperty("http://apache.org/xml/properties/security-manager", this.fSecurityManager);
            return;
        }
        if (name.equals("http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager")) {
            if (object == null) {
                this.fSecurityPropertyMgr = new XMLSecurityPropertyManager();
            } else {
                this.fSecurityPropertyMgr = (XMLSecurityPropertyManager) object;
            }
            this.fXMLSchemaLoader.setProperty("http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager", this.fSecurityPropertyMgr);
            return;
        }
        if (name.equals("http://apache.org/xml/properties/internal/grammar-pool")) {
            throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(this.fXMLSchemaLoader.getLocale(), "property-not-supported", new Object[]{name}));
        }
        try {
            if ((this.fSecurityManager == null || !this.fSecurityManager.setLimit(name, XMLSecurityManager.State.APIPROPERTY, object)) && (this.fSecurityPropertyMgr == null || !this.fSecurityPropertyMgr.setValue(name, XMLSecurityPropertyManager.State.APIPROPERTY, object))) {
                this.fXMLSchemaLoader.setProperty(name, object);
            }
        } catch (XMLConfigurationException e2) {
            String identifier = e2.getIdentifier();
            if (e2.getType() == Status.NOT_RECOGNIZED) {
                throw new SAXNotRecognizedException(SAXMessageFormatter.formatMessage(this.fXMLSchemaLoader.getLocale(), "property-not-recognized", new Object[]{identifier}));
            }
            throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(this.fXMLSchemaLoader.getLocale(), "property-not-supported", new Object[]{identifier}));
        }
    }

    private void propagateFeatures(AbstractXMLSchema schema) throws XMLConfigurationException {
        schema.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", this.fSecurityManager != null && this.fSecurityManager.isSecureProcessing());
        schema.setFeature(JdkXmlUtils.OVERRIDE_PARSER, this.fOverrideDefaultParser);
        String[] features = this.fXMLSchemaLoader.getRecognizedFeatures();
        for (int i2 = 0; i2 < features.length; i2++) {
            boolean state = this.fXMLSchemaLoader.getFeature(features[i2]);
            schema.setFeature(features[i2], state);
        }
    }

    private void propagateProperties(AbstractXMLSchema schema) throws XMLConfigurationException {
        String[] properties = this.fXMLSchemaLoader.getRecognizedProperties();
        for (int i2 = 0; i2 < properties.length; i2++) {
            Object state = this.fXMLSchemaLoader.getProperty(properties[i2]);
            schema.setProperty(properties[i2], state);
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/jaxp/validation/XMLSchemaFactory$XMLGrammarPoolImplExtension.class */
    static class XMLGrammarPoolImplExtension extends XMLGrammarPoolImpl {
        public XMLGrammarPoolImplExtension() {
        }

        public XMLGrammarPoolImplExtension(int initialCapacity) {
            super(initialCapacity);
        }

        int getGrammarCount() {
            return this.fGrammarCount;
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/jaxp/validation/XMLSchemaFactory$XMLGrammarPoolWrapper.class */
    static class XMLGrammarPoolWrapper implements XMLGrammarPool {
        private XMLGrammarPool fGrammarPool;

        XMLGrammarPoolWrapper() {
        }

        @Override // com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool
        public Grammar[] retrieveInitialGrammarSet(String grammarType) {
            return this.fGrammarPool.retrieveInitialGrammarSet(grammarType);
        }

        @Override // com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool
        public void cacheGrammars(String grammarType, Grammar[] grammars) {
            this.fGrammarPool.cacheGrammars(grammarType, grammars);
        }

        @Override // com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool
        public Grammar retrieveGrammar(XMLGrammarDescription desc) {
            return this.fGrammarPool.retrieveGrammar(desc);
        }

        @Override // com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool
        public void lockPool() {
            this.fGrammarPool.lockPool();
        }

        @Override // com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool
        public void unlockPool() {
            this.fGrammarPool.unlockPool();
        }

        @Override // com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool
        public void clear() {
            this.fGrammarPool.clear();
        }

        void setGrammarPool(XMLGrammarPool grammarPool) {
            this.fGrammarPool = grammarPool;
        }

        XMLGrammarPool getGrammarPool() {
            return this.fGrammarPool;
        }
    }
}
