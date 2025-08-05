package com.sun.org.apache.xerces.internal.jaxp.validation;

import com.sun.org.apache.xerces.internal.impl.XMLErrorReporter;
import com.sun.org.apache.xerces.internal.impl.msg.XMLMessageFormatter;
import com.sun.org.apache.xerces.internal.impl.xs.XMLSchemaValidator;
import com.sun.org.apache.xerces.internal.parsers.XML11Configuration;
import com.sun.org.apache.xerces.internal.utils.XMLSecurityManager;
import com.sun.org.apache.xerces.internal.xni.XNIException;
import com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;
import com.sun.org.apache.xerces.internal.xni.parser.XMLParseException;
import com.sun.org.apache.xerces.internal.xni.parser.XMLParserConfiguration;
import java.io.IOException;
import java.lang.ref.SoftReference;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import jdk.xml.internal.JdkXmlUtils;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/jaxp/validation/StreamValidatorHelper.class */
final class StreamValidatorHelper implements ValidatorHelper {
    private static final String PARSER_SETTINGS = "http://apache.org/xml/features/internal/parser-settings";
    private static final String ENTITY_RESOLVER = "http://apache.org/xml/properties/internal/entity-resolver";
    private static final String ERROR_HANDLER = "http://apache.org/xml/properties/internal/error-handler";
    private static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
    private static final String SCHEMA_VALIDATOR = "http://apache.org/xml/properties/internal/validator/schema";
    private static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
    private static final String VALIDATION_MANAGER = "http://apache.org/xml/properties/internal/validation-manager";
    private static final String SECURITY_MANAGER = "http://apache.org/xml/properties/security-manager";
    private XMLSchemaValidator fSchemaValidator;
    private XMLSchemaValidatorComponentManager fComponentManager;
    private SoftReference fConfiguration = new SoftReference(null);
    private ValidatorHandlerImpl handler = null;

    public StreamValidatorHelper(XMLSchemaValidatorComponentManager componentManager) {
        this.fComponentManager = componentManager;
        this.fSchemaValidator = (XMLSchemaValidator) this.fComponentManager.getProperty(SCHEMA_VALIDATOR);
    }

    @Override // com.sun.org.apache.xerces.internal.jaxp.validation.ValidatorHelper
    public void validate(Source source, Result result) throws SAXException, IOException {
        if (result == null || (result instanceof StreamResult)) {
            StreamSource streamSource = (StreamSource) source;
            if (result != null) {
                try {
                    SAXTransformerFactory tf = JdkXmlUtils.getSAXTransformFactory(this.fComponentManager.getFeature(JdkXmlUtils.OVERRIDE_PARSER));
                    TransformerHandler identityTransformerHandler = tf.newTransformerHandler();
                    this.handler = new ValidatorHandlerImpl(this.fComponentManager);
                    this.handler.setContentHandler(identityTransformerHandler);
                    identityTransformerHandler.setResult(result);
                } catch (TransformerConfigurationException e2) {
                    throw new TransformerFactoryConfigurationError(e2);
                }
            }
            XMLInputSource input = new XMLInputSource(streamSource.getPublicId(), streamSource.getSystemId(), null);
            input.setByteStream(streamSource.getInputStream());
            input.setCharacterStream(streamSource.getReader());
            XMLParserConfiguration config = (XMLParserConfiguration) this.fConfiguration.get();
            if (config == null) {
                config = initialize();
            } else if (this.fComponentManager.getFeature(PARSER_SETTINGS)) {
                config.setProperty("http://apache.org/xml/properties/internal/entity-resolver", this.fComponentManager.getProperty("http://apache.org/xml/properties/internal/entity-resolver"));
                config.setProperty(ERROR_HANDLER, this.fComponentManager.getProperty(ERROR_HANDLER));
            }
            this.fComponentManager.reset();
            this.fSchemaValidator.setDocumentHandler(this.handler);
            try {
                config.parse(input);
                return;
            } catch (XMLParseException e3) {
                throw Util.toSAXParseException(e3);
            } catch (XNIException e4) {
                throw Util.toSAXException(e4);
            }
        }
        throw new IllegalArgumentException(JAXPValidationMessageFormatter.formatMessage(this.fComponentManager.getLocale(), "SourceResultMismatch", new Object[]{source.getClass().getName(), result.getClass().getName()}));
    }

    private XMLParserConfiguration initialize() throws XNIException {
        XML11Configuration config = new XML11Configuration();
        if (this.fComponentManager.getFeature("http://javax.xml.XMLConstants/feature/secure-processing")) {
            config.setProperty("http://apache.org/xml/properties/security-manager", new XMLSecurityManager());
        }
        config.setProperty("http://apache.org/xml/properties/internal/entity-resolver", this.fComponentManager.getProperty("http://apache.org/xml/properties/internal/entity-resolver"));
        config.setProperty(ERROR_HANDLER, this.fComponentManager.getProperty(ERROR_HANDLER));
        XMLErrorReporter errorReporter = (XMLErrorReporter) this.fComponentManager.getProperty("http://apache.org/xml/properties/internal/error-reporter");
        config.setProperty("http://apache.org/xml/properties/internal/error-reporter", errorReporter);
        if (errorReporter.getMessageFormatter("http://www.w3.org/TR/1998/REC-xml-19980210") == null) {
            XMLMessageFormatter xmft = new XMLMessageFormatter();
            errorReporter.putMessageFormatter("http://www.w3.org/TR/1998/REC-xml-19980210", xmft);
            errorReporter.putMessageFormatter("http://www.w3.org/TR/1999/REC-xml-names-19990114", xmft);
        }
        config.setProperty("http://apache.org/xml/properties/internal/symbol-table", this.fComponentManager.getProperty("http://apache.org/xml/properties/internal/symbol-table"));
        config.setProperty(VALIDATION_MANAGER, this.fComponentManager.getProperty(VALIDATION_MANAGER));
        config.setDocumentHandler(this.fSchemaValidator);
        config.setDTDHandler(null);
        config.setDTDContentModelHandler(null);
        config.setProperty("http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager", this.fComponentManager.getProperty("http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager"));
        config.setProperty("http://apache.org/xml/properties/security-manager", this.fComponentManager.getProperty("http://apache.org/xml/properties/security-manager"));
        this.fConfiguration = new SoftReference(config);
        return config;
    }
}
