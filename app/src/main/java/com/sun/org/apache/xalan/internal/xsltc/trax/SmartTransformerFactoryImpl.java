package com.sun.org.apache.xalan.internal.xsltc.trax;

import com.sun.org.apache.xalan.internal.utils.ConfigurationError;
import com.sun.org.apache.xalan.internal.utils.ObjectFactory;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TemplatesHandler;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.xml.sax.XMLFilter;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/trax/SmartTransformerFactoryImpl.class */
public class SmartTransformerFactoryImpl extends SAXTransformerFactory {
    private static final String CLASS_NAME = "SmartTransformerFactoryImpl";
    private SAXTransformerFactory _xsltcFactory = null;
    private SAXTransformerFactory _xalanFactory = null;
    private SAXTransformerFactory _currFactory = null;
    private ErrorListener _errorlistener = null;
    private URIResolver _uriresolver = null;
    private boolean featureSecureProcessing = false;

    private void createXSLTCTransformerFactory() {
        this._xsltcFactory = new TransformerFactoryImpl();
        this._currFactory = this._xsltcFactory;
    }

    private void createXalanTransformerFactory() throws ConfigurationError {
        try {
            Class xalanFactClass = ObjectFactory.findProviderClass("com.sun.org.apache.xalan.internal.processor.TransformerFactoryImpl", true);
            this._xalanFactory = (SAXTransformerFactory) xalanFactClass.newInstance();
        } catch (ClassNotFoundException e2) {
            System.err.println("com.sun.org.apache.xalan.internal.xsltc.trax.SmartTransformerFactoryImpl could not create an com.sun.org.apache.xalan.internal.processor.TransformerFactoryImpl.");
        } catch (IllegalAccessException e3) {
            System.err.println("com.sun.org.apache.xalan.internal.xsltc.trax.SmartTransformerFactoryImpl could not create an com.sun.org.apache.xalan.internal.processor.TransformerFactoryImpl.");
        } catch (InstantiationException e4) {
            System.err.println("com.sun.org.apache.xalan.internal.xsltc.trax.SmartTransformerFactoryImpl could not create an com.sun.org.apache.xalan.internal.processor.TransformerFactoryImpl.");
        }
        this._currFactory = this._xalanFactory;
    }

    @Override // javax.xml.transform.TransformerFactory
    public void setErrorListener(ErrorListener listener) throws IllegalArgumentException {
        this._errorlistener = listener;
    }

    @Override // javax.xml.transform.TransformerFactory
    public ErrorListener getErrorListener() {
        return this._errorlistener;
    }

    @Override // javax.xml.transform.TransformerFactory
    public Object getAttribute(String name) throws IllegalArgumentException, ConfigurationError {
        if (name.equals(TransformerFactoryImpl.TRANSLET_NAME) || name.equals(TransformerFactoryImpl.DEBUG)) {
            if (this._xsltcFactory == null) {
                createXSLTCTransformerFactory();
            }
            return this._xsltcFactory.getAttribute(name);
        }
        if (this._xalanFactory == null) {
            createXalanTransformerFactory();
        }
        return this._xalanFactory.getAttribute(name);
    }

    @Override // javax.xml.transform.TransformerFactory
    public void setAttribute(String name, Object value) throws IllegalArgumentException, ConfigurationError {
        if (name.equals(TransformerFactoryImpl.TRANSLET_NAME) || name.equals(TransformerFactoryImpl.DEBUG)) {
            if (this._xsltcFactory == null) {
                createXSLTCTransformerFactory();
            }
            this._xsltcFactory.setAttribute(name, value);
        } else {
            if (this._xalanFactory == null) {
                createXalanTransformerFactory();
            }
            this._xalanFactory.setAttribute(name, value);
        }
    }

    @Override // javax.xml.transform.TransformerFactory
    public void setFeature(String name, boolean value) throws TransformerConfigurationException {
        if (name == null) {
            ErrorMsg err = new ErrorMsg(ErrorMsg.JAXP_SET_FEATURE_NULL_NAME);
            throw new NullPointerException(err.toString());
        }
        if (name.equals("http://javax.xml.XMLConstants/feature/secure-processing")) {
            this.featureSecureProcessing = value;
        } else {
            ErrorMsg err2 = new ErrorMsg(ErrorMsg.JAXP_UNSUPPORTED_FEATURE, name);
            throw new TransformerConfigurationException(err2.toString());
        }
    }

    @Override // javax.xml.transform.TransformerFactory
    public boolean getFeature(String name) {
        String[] features = {DOMSource.FEATURE, DOMResult.FEATURE, SAXSource.FEATURE, SAXResult.FEATURE, StreamSource.FEATURE, StreamResult.FEATURE};
        if (name == null) {
            ErrorMsg err = new ErrorMsg(ErrorMsg.JAXP_GET_FEATURE_NULL_NAME);
            throw new NullPointerException(err.toString());
        }
        for (String str : features) {
            if (name.equals(str)) {
                return true;
            }
        }
        if (name.equals("http://javax.xml.XMLConstants/feature/secure-processing")) {
            return this.featureSecureProcessing;
        }
        return false;
    }

    @Override // javax.xml.transform.TransformerFactory
    public URIResolver getURIResolver() {
        return this._uriresolver;
    }

    @Override // javax.xml.transform.TransformerFactory
    public void setURIResolver(URIResolver resolver) {
        this._uriresolver = resolver;
    }

    @Override // javax.xml.transform.TransformerFactory
    public Source getAssociatedStylesheet(Source source, String media, String title, String charset) throws TransformerConfigurationException {
        if (this._currFactory == null) {
            createXSLTCTransformerFactory();
        }
        return this._currFactory.getAssociatedStylesheet(source, media, title, charset);
    }

    @Override // javax.xml.transform.TransformerFactory
    public Transformer newTransformer() throws TransformerConfigurationException, ConfigurationError {
        if (this._xalanFactory == null) {
            createXalanTransformerFactory();
        }
        if (this._errorlistener != null) {
            this._xalanFactory.setErrorListener(this._errorlistener);
        }
        if (this._uriresolver != null) {
            this._xalanFactory.setURIResolver(this._uriresolver);
        }
        this._currFactory = this._xalanFactory;
        return this._currFactory.newTransformer();
    }

    @Override // javax.xml.transform.TransformerFactory
    public Transformer newTransformer(Source source) throws TransformerConfigurationException, ConfigurationError {
        if (this._xalanFactory == null) {
            createXalanTransformerFactory();
        }
        if (this._errorlistener != null) {
            this._xalanFactory.setErrorListener(this._errorlistener);
        }
        if (this._uriresolver != null) {
            this._xalanFactory.setURIResolver(this._uriresolver);
        }
        this._currFactory = this._xalanFactory;
        return this._currFactory.newTransformer(source);
    }

    @Override // javax.xml.transform.TransformerFactory
    public Templates newTemplates(Source source) throws TransformerConfigurationException {
        if (this._xsltcFactory == null) {
            createXSLTCTransformerFactory();
        }
        if (this._errorlistener != null) {
            this._xsltcFactory.setErrorListener(this._errorlistener);
        }
        if (this._uriresolver != null) {
            this._xsltcFactory.setURIResolver(this._uriresolver);
        }
        this._currFactory = this._xsltcFactory;
        return this._currFactory.newTemplates(source);
    }

    @Override // javax.xml.transform.sax.SAXTransformerFactory
    public TemplatesHandler newTemplatesHandler() throws TransformerConfigurationException {
        if (this._xsltcFactory == null) {
            createXSLTCTransformerFactory();
        }
        if (this._errorlistener != null) {
            this._xsltcFactory.setErrorListener(this._errorlistener);
        }
        if (this._uriresolver != null) {
            this._xsltcFactory.setURIResolver(this._uriresolver);
        }
        return this._xsltcFactory.newTemplatesHandler();
    }

    @Override // javax.xml.transform.sax.SAXTransformerFactory
    public TransformerHandler newTransformerHandler() throws TransformerConfigurationException, ConfigurationError {
        if (this._xalanFactory == null) {
            createXalanTransformerFactory();
        }
        if (this._errorlistener != null) {
            this._xalanFactory.setErrorListener(this._errorlistener);
        }
        if (this._uriresolver != null) {
            this._xalanFactory.setURIResolver(this._uriresolver);
        }
        return this._xalanFactory.newTransformerHandler();
    }

    @Override // javax.xml.transform.sax.SAXTransformerFactory
    public TransformerHandler newTransformerHandler(Source src) throws TransformerConfigurationException, ConfigurationError {
        if (this._xalanFactory == null) {
            createXalanTransformerFactory();
        }
        if (this._errorlistener != null) {
            this._xalanFactory.setErrorListener(this._errorlistener);
        }
        if (this._uriresolver != null) {
            this._xalanFactory.setURIResolver(this._uriresolver);
        }
        return this._xalanFactory.newTransformerHandler(src);
    }

    @Override // javax.xml.transform.sax.SAXTransformerFactory
    public TransformerHandler newTransformerHandler(Templates templates) throws TransformerConfigurationException {
        if (this._xsltcFactory == null) {
            createXSLTCTransformerFactory();
        }
        if (this._errorlistener != null) {
            this._xsltcFactory.setErrorListener(this._errorlistener);
        }
        if (this._uriresolver != null) {
            this._xsltcFactory.setURIResolver(this._uriresolver);
        }
        return this._xsltcFactory.newTransformerHandler(templates);
    }

    @Override // javax.xml.transform.sax.SAXTransformerFactory
    public XMLFilter newXMLFilter(Source src) throws TransformerConfigurationException {
        if (this._xsltcFactory == null) {
            createXSLTCTransformerFactory();
        }
        if (this._errorlistener != null) {
            this._xsltcFactory.setErrorListener(this._errorlistener);
        }
        if (this._uriresolver != null) {
            this._xsltcFactory.setURIResolver(this._uriresolver);
        }
        Templates templates = this._xsltcFactory.newTemplates(src);
        if (templates == null) {
            return null;
        }
        return newXMLFilter(templates);
    }

    @Override // javax.xml.transform.sax.SAXTransformerFactory
    public XMLFilter newXMLFilter(Templates templates) throws TransformerConfigurationException {
        try {
            return new TrAXFilter(templates);
        } catch (TransformerConfigurationException e1) {
            if (this._xsltcFactory == null) {
                createXSLTCTransformerFactory();
            }
            ErrorListener errorListener = this._xsltcFactory.getErrorListener();
            if (errorListener != null) {
                try {
                    errorListener.fatalError(e1);
                    return null;
                } catch (TransformerException e2) {
                    new TransformerConfigurationException(e2);
                    throw e1;
                }
            }
            throw e1;
        }
    }
}
