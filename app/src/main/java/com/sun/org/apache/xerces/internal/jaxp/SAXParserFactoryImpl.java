package com.sun.org.apache.xerces.internal.jaxp;

import com.sun.org.apache.xerces.internal.util.SAXMessageFormatter;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.validation.Schema;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/jaxp/SAXParserFactoryImpl.class */
public class SAXParserFactoryImpl extends SAXParserFactory {
    private static final String VALIDATION_FEATURE = "http://xml.org/sax/features/validation";
    private static final String NAMESPACES_FEATURE = "http://xml.org/sax/features/namespaces";
    private static final String XINCLUDE_FEATURE = "http://apache.org/xml/features/xinclude";
    private Map<String, Boolean> features;
    private Schema grammar;
    private boolean isXIncludeAware;
    private boolean fSecureProcess = true;

    @Override // javax.xml.parsers.SAXParserFactory
    public SAXParser newSAXParser() throws ParserConfigurationException {
        try {
            SAXParser saxParserImpl = new SAXParserImpl(this, this.features, this.fSecureProcess);
            return saxParserImpl;
        } catch (SAXException se) {
            throw new ParserConfigurationException(se.getMessage());
        }
    }

    private SAXParserImpl newSAXParserImpl() throws SAXNotRecognizedException, SAXNotSupportedException, ParserConfigurationException {
        try {
            SAXParserImpl saxParserImpl = new SAXParserImpl(this, this.features);
            return saxParserImpl;
        } catch (SAXNotRecognizedException e2) {
            throw e2;
        } catch (SAXNotSupportedException e3) {
            throw e3;
        } catch (SAXException se) {
            throw new ParserConfigurationException(se.getMessage());
        }
    }

    @Override // javax.xml.parsers.SAXParserFactory
    public void setFeature(String name, boolean value) throws SAXNotRecognizedException, SAXNotSupportedException, ParserConfigurationException {
        if (name == null) {
            throw new NullPointerException();
        }
        if (name.equals("http://javax.xml.XMLConstants/feature/secure-processing")) {
            if (System.getSecurityManager() != null && !value) {
                throw new ParserConfigurationException(SAXMessageFormatter.formatMessage(null, "jaxp-secureprocessing-feature", null));
            }
            this.fSecureProcess = value;
            putInFeatures(name, value);
            return;
        }
        putInFeatures(name, value);
        try {
            newSAXParserImpl();
        } catch (SAXNotRecognizedException e2) {
            this.features.remove(name);
            throw e2;
        } catch (SAXNotSupportedException e3) {
            this.features.remove(name);
            throw e3;
        }
    }

    @Override // javax.xml.parsers.SAXParserFactory
    public boolean getFeature(String name) throws SAXNotRecognizedException, SAXNotSupportedException, ParserConfigurationException {
        if (name == null) {
            throw new NullPointerException();
        }
        if (name.equals("http://javax.xml.XMLConstants/feature/secure-processing")) {
            return this.fSecureProcess;
        }
        return newSAXParserImpl().getXMLReader().getFeature(name);
    }

    @Override // javax.xml.parsers.SAXParserFactory
    public Schema getSchema() {
        return this.grammar;
    }

    @Override // javax.xml.parsers.SAXParserFactory
    public void setSchema(Schema grammar) {
        this.grammar = grammar;
    }

    @Override // javax.xml.parsers.SAXParserFactory
    public boolean isXIncludeAware() {
        return getFromFeatures(XINCLUDE_FEATURE);
    }

    @Override // javax.xml.parsers.SAXParserFactory
    public void setXIncludeAware(boolean state) {
        putInFeatures(XINCLUDE_FEATURE, state);
    }

    @Override // javax.xml.parsers.SAXParserFactory
    public void setValidating(boolean validating) {
        putInFeatures(VALIDATION_FEATURE, validating);
    }

    @Override // javax.xml.parsers.SAXParserFactory
    public boolean isValidating() {
        return getFromFeatures(VALIDATION_FEATURE);
    }

    private void putInFeatures(String name, boolean value) {
        if (this.features == null) {
            this.features = new HashMap();
        }
        this.features.put(name, value ? Boolean.TRUE : Boolean.FALSE);
    }

    private boolean getFromFeatures(String name) {
        Boolean value;
        if (this.features == null || (value = this.features.get(name)) == null) {
            return false;
        }
        return value.booleanValue();
    }

    @Override // javax.xml.parsers.SAXParserFactory
    public boolean isNamespaceAware() {
        return getFromFeatures("http://xml.org/sax/features/namespaces");
    }

    @Override // javax.xml.parsers.SAXParserFactory
    public void setNamespaceAware(boolean awareness) {
        putInFeatures("http://xml.org/sax/features/namespaces", awareness);
    }
}
