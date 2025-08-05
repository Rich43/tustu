package com.sun.org.apache.xerces.internal.jaxp.validation;

import com.sun.org.apache.xerces.internal.util.SAXMessageFormatter;
import com.sun.org.apache.xerces.internal.util.Status;
import com.sun.org.apache.xerces.internal.xni.parser.XMLConfigurationException;
import com.sun.org.apache.xerces.internal.xs.AttributePSVI;
import com.sun.org.apache.xerces.internal.xs.ElementPSVI;
import com.sun.org.apache.xerces.internal.xs.PSVIProvider;
import java.io.IOException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stax.StAXSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Validator;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/jaxp/validation/ValidatorImpl.class */
final class ValidatorImpl extends Validator implements PSVIProvider {
    private XMLSchemaValidatorComponentManager fComponentManager;
    private ValidatorHandlerImpl fSAXValidatorHelper;
    private DOMValidatorHelper fDOMValidatorHelper;
    private StreamValidatorHelper fStreamValidatorHelper;
    private StAXValidatorHelper fStaxValidatorHelper;
    private boolean fConfigurationChanged = false;
    private boolean fErrorHandlerChanged = false;
    private boolean fResourceResolverChanged = false;
    private static final String CURRENT_ELEMENT_NODE = "http://apache.org/xml/properties/dom/current-element-node";

    public ValidatorImpl(XSGrammarPoolContainer grammarContainer) {
        this.fComponentManager = new XMLSchemaValidatorComponentManager(grammarContainer);
        setErrorHandler(null);
        setResourceResolver(null);
    }

    @Override // javax.xml.validation.Validator
    public void validate(Source source, Result result) throws SAXException, IOException {
        if (source instanceof SAXSource) {
            if (this.fSAXValidatorHelper == null) {
                this.fSAXValidatorHelper = new ValidatorHandlerImpl(this.fComponentManager);
            }
            this.fSAXValidatorHelper.validate(source, result);
            return;
        }
        if (source instanceof DOMSource) {
            if (this.fDOMValidatorHelper == null) {
                this.fDOMValidatorHelper = new DOMValidatorHelper(this.fComponentManager);
            }
            this.fDOMValidatorHelper.validate(source, result);
        } else if (source instanceof StreamSource) {
            if (this.fStreamValidatorHelper == null) {
                this.fStreamValidatorHelper = new StreamValidatorHelper(this.fComponentManager);
            }
            this.fStreamValidatorHelper.validate(source, result);
        } else if (source instanceof StAXSource) {
            if (this.fStaxValidatorHelper == null) {
                this.fStaxValidatorHelper = new StAXValidatorHelper(this.fComponentManager);
            }
            this.fStaxValidatorHelper.validate(source, result);
        } else {
            if (source == null) {
                throw new NullPointerException(JAXPValidationMessageFormatter.formatMessage(this.fComponentManager.getLocale(), "SourceParameterNull", null));
            }
            throw new IllegalArgumentException(JAXPValidationMessageFormatter.formatMessage(this.fComponentManager.getLocale(), "SourceNotAccepted", new Object[]{source.getClass().getName()}));
        }
    }

    @Override // javax.xml.validation.Validator
    public void setErrorHandler(ErrorHandler errorHandler) {
        this.fErrorHandlerChanged = errorHandler != null;
        this.fComponentManager.setErrorHandler(errorHandler);
    }

    @Override // javax.xml.validation.Validator
    public ErrorHandler getErrorHandler() {
        return this.fComponentManager.getErrorHandler();
    }

    @Override // javax.xml.validation.Validator
    public void setResourceResolver(LSResourceResolver resourceResolver) {
        this.fResourceResolverChanged = resourceResolver != null;
        this.fComponentManager.setResourceResolver(resourceResolver);
    }

    @Override // javax.xml.validation.Validator
    public LSResourceResolver getResourceResolver() {
        return this.fComponentManager.getResourceResolver();
    }

    @Override // javax.xml.validation.Validator
    public boolean getFeature(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
        if (name == null) {
            throw new NullPointerException();
        }
        try {
            return this.fComponentManager.getFeature(name);
        } catch (XMLConfigurationException e2) {
            String identifier = e2.getIdentifier();
            String key = e2.getType() == Status.NOT_RECOGNIZED ? "feature-not-recognized" : "feature-not-supported";
            throw new SAXNotRecognizedException(SAXMessageFormatter.formatMessage(this.fComponentManager.getLocale(), key, new Object[]{identifier}));
        }
    }

    @Override // javax.xml.validation.Validator
    public void setFeature(String name, boolean value) throws SAXNotRecognizedException, SAXNotSupportedException {
        String key;
        if (name == null) {
            throw new NullPointerException();
        }
        try {
            this.fComponentManager.setFeature(name, value);
            this.fConfigurationChanged = true;
        } catch (XMLConfigurationException e2) {
            String identifier = e2.getIdentifier();
            if (e2.getType() == Status.NOT_ALLOWED) {
                throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(this.fComponentManager.getLocale(), "jaxp-secureprocessing-feature", null));
            }
            if (e2.getType() == Status.NOT_RECOGNIZED) {
                key = "feature-not-recognized";
            } else {
                key = "feature-not-supported";
            }
            throw new SAXNotRecognizedException(SAXMessageFormatter.formatMessage(this.fComponentManager.getLocale(), key, new Object[]{identifier}));
        }
    }

    @Override // javax.xml.validation.Validator
    public Object getProperty(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
        if (name == null) {
            throw new NullPointerException();
        }
        if (CURRENT_ELEMENT_NODE.equals(name)) {
            if (this.fDOMValidatorHelper != null) {
                return this.fDOMValidatorHelper.getCurrentElement();
            }
            return null;
        }
        try {
            return this.fComponentManager.getProperty(name);
        } catch (XMLConfigurationException e2) {
            String identifier = e2.getIdentifier();
            String key = e2.getType() == Status.NOT_RECOGNIZED ? "property-not-recognized" : "property-not-supported";
            throw new SAXNotRecognizedException(SAXMessageFormatter.formatMessage(this.fComponentManager.getLocale(), key, new Object[]{identifier}));
        }
    }

    @Override // javax.xml.validation.Validator
    public void setProperty(String name, Object object) throws SAXNotRecognizedException, SAXNotSupportedException {
        if (name == null) {
            throw new NullPointerException();
        }
        try {
            this.fComponentManager.setProperty(name, object);
            this.fConfigurationChanged = true;
        } catch (XMLConfigurationException e2) {
            String identifier = e2.getIdentifier();
            String key = e2.getType() == Status.NOT_RECOGNIZED ? "property-not-recognized" : "property-not-supported";
            throw new SAXNotRecognizedException(SAXMessageFormatter.formatMessage(this.fComponentManager.getLocale(), key, new Object[]{identifier}));
        }
    }

    @Override // javax.xml.validation.Validator
    public void reset() {
        if (this.fConfigurationChanged) {
            this.fComponentManager.restoreInitialState();
            setErrorHandler(null);
            setResourceResolver(null);
            this.fConfigurationChanged = false;
            this.fErrorHandlerChanged = false;
            this.fResourceResolverChanged = false;
            return;
        }
        if (this.fErrorHandlerChanged) {
            setErrorHandler(null);
            this.fErrorHandlerChanged = false;
        }
        if (this.fResourceResolverChanged) {
            setResourceResolver(null);
            this.fResourceResolverChanged = false;
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xs.PSVIProvider
    public ElementPSVI getElementPSVI() {
        if (this.fSAXValidatorHelper != null) {
            return this.fSAXValidatorHelper.getElementPSVI();
        }
        return null;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.PSVIProvider
    public AttributePSVI getAttributePSVI(int index) {
        if (this.fSAXValidatorHelper != null) {
            return this.fSAXValidatorHelper.getAttributePSVI(index);
        }
        return null;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.PSVIProvider
    public AttributePSVI getAttributePSVIByName(String uri, String localname) {
        if (this.fSAXValidatorHelper != null) {
            return this.fSAXValidatorHelper.getAttributePSVIByName(uri, localname);
        }
        return null;
    }
}
