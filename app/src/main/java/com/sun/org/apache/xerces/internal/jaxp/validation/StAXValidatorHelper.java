package com.sun.org.apache.xerces.internal.jaxp.validation;

import com.sun.org.apache.xerces.internal.utils.XMLSecurityManager;
import java.io.IOException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stax.StAXResult;
import jdk.xml.internal.JdkXmlUtils;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/jaxp/validation/StAXValidatorHelper.class */
public final class StAXValidatorHelper implements ValidatorHelper {
    private XMLSchemaValidatorComponentManager fComponentManager;
    private Transformer identityTransformer1 = null;
    private TransformerHandler identityTransformer2 = null;
    private ValidatorHandlerImpl handler = null;

    public StAXValidatorHelper(XMLSchemaValidatorComponentManager componentManager) {
        this.fComponentManager = componentManager;
    }

    @Override // com.sun.org.apache.xerces.internal.jaxp.validation.ValidatorHelper
    public void validate(Source source, Result result) throws SAXException, IOException {
        if (result == null || (result instanceof StAXResult)) {
            if (this.identityTransformer1 == null) {
                try {
                    SAXTransformerFactory tf = JdkXmlUtils.getSAXTransformFactory(this.fComponentManager.getFeature(JdkXmlUtils.OVERRIDE_PARSER));
                    XMLSecurityManager securityManager = (XMLSecurityManager) this.fComponentManager.getProperty("http://apache.org/xml/properties/security-manager");
                    if (securityManager != null) {
                        for (XMLSecurityManager.Limit limit : XMLSecurityManager.Limit.values()) {
                            if (securityManager.isSet(limit.ordinal())) {
                                tf.setAttribute(limit.apiProperty(), securityManager.getLimitValueAsString(limit));
                            }
                        }
                        if (securityManager.printEntityCountInfo()) {
                            tf.setAttribute("http://www.oracle.com/xml/jaxp/properties/getEntityCountInfo", "yes");
                        }
                    }
                    this.identityTransformer1 = tf.newTransformer();
                    this.identityTransformer2 = tf.newTransformerHandler();
                } catch (TransformerConfigurationException e2) {
                    throw new TransformerFactoryConfigurationError(e2);
                }
            }
            this.handler = new ValidatorHandlerImpl(this.fComponentManager);
            if (result != null) {
                this.handler.setContentHandler(this.identityTransformer2);
                this.identityTransformer2.setResult(result);
            }
            try {
                try {
                    this.identityTransformer1.transform(source, new SAXResult(this.handler));
                    this.handler.setContentHandler(null);
                    return;
                } catch (Throwable th) {
                    this.handler.setContentHandler(null);
                    throw th;
                }
            } catch (TransformerException e3) {
                if (e3.getException() instanceof SAXException) {
                    throw ((SAXException) e3.getException());
                }
                throw new SAXException(e3);
            }
        }
        throw new IllegalArgumentException(JAXPValidationMessageFormatter.formatMessage(this.fComponentManager.getLocale(), "SourceResultMismatch", new Object[]{source.getClass().getName(), result.getClass().getName()}));
    }
}
