package com.sun.org.apache.xerces.internal.jaxp;

import com.sun.org.apache.xerces.internal.parsers.DOMParser;
import com.sun.org.apache.xerces.internal.util.SAXMessageFormatter;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/jaxp/DocumentBuilderFactoryImpl.class */
public class DocumentBuilderFactoryImpl extends DocumentBuilderFactory {
    private Map<String, Object> attributes;
    private Map<String, Boolean> features;
    private Schema grammar;
    private boolean isXIncludeAware;
    private boolean fSecureProcess = true;

    @Override // javax.xml.parsers.DocumentBuilderFactory
    public DocumentBuilder newDocumentBuilder() throws ParserConfigurationException {
        if (this.grammar != null && this.attributes != null) {
            if (this.attributes.containsKey(JAXPConstants.JAXP_SCHEMA_LANGUAGE)) {
                throw new ParserConfigurationException(SAXMessageFormatter.formatMessage(null, "schema-already-specified", new Object[]{JAXPConstants.JAXP_SCHEMA_LANGUAGE}));
            }
            if (this.attributes.containsKey(JAXPConstants.JAXP_SCHEMA_SOURCE)) {
                throw new ParserConfigurationException(SAXMessageFormatter.formatMessage(null, "schema-already-specified", new Object[]{JAXPConstants.JAXP_SCHEMA_SOURCE}));
            }
        }
        try {
            return new DocumentBuilderImpl(this, this.attributes, this.features, this.fSecureProcess);
        } catch (SAXException se) {
            throw new ParserConfigurationException(se.getMessage());
        }
    }

    @Override // javax.xml.parsers.DocumentBuilderFactory
    public void setAttribute(String name, Object value) throws IllegalArgumentException {
        if (value == null) {
            if (this.attributes != null) {
                this.attributes.remove(name);
                return;
            }
            return;
        }
        if (this.attributes == null) {
            this.attributes = new HashMap();
        }
        this.attributes.put(name, value);
        try {
            new DocumentBuilderImpl(this, this.attributes, this.features);
        } catch (Exception e2) {
            this.attributes.remove(name);
            throw new IllegalArgumentException(e2.getMessage());
        }
    }

    @Override // javax.xml.parsers.DocumentBuilderFactory
    public Object getAttribute(String name) throws IllegalArgumentException {
        Object val;
        if (this.attributes != null && (val = this.attributes.get(name)) != null) {
            return val;
        }
        DOMParser domParser = null;
        try {
            domParser = new DocumentBuilderImpl(this, this.attributes, this.features).getDOMParser();
            return domParser.getProperty(name);
        } catch (SAXException se1) {
            try {
                boolean result = domParser.getFeature(name);
                return result ? Boolean.TRUE : Boolean.FALSE;
            } catch (SAXException e2) {
                throw new IllegalArgumentException(se1.getMessage());
            }
        }
    }

    @Override // javax.xml.parsers.DocumentBuilderFactory
    public Schema getSchema() {
        return this.grammar;
    }

    @Override // javax.xml.parsers.DocumentBuilderFactory
    public void setSchema(Schema grammar) {
        this.grammar = grammar;
    }

    @Override // javax.xml.parsers.DocumentBuilderFactory
    public boolean isXIncludeAware() {
        return this.isXIncludeAware;
    }

    @Override // javax.xml.parsers.DocumentBuilderFactory
    public void setXIncludeAware(boolean state) {
        this.isXIncludeAware = state;
    }

    @Override // javax.xml.parsers.DocumentBuilderFactory
    public boolean getFeature(String name) throws ParserConfigurationException {
        Boolean val;
        if (name.equals("http://javax.xml.XMLConstants/feature/secure-processing")) {
            return this.fSecureProcess;
        }
        if (this.features != null && (val = this.features.get(name)) != null) {
            return val.booleanValue();
        }
        try {
            DOMParser domParser = new DocumentBuilderImpl(this, this.attributes, this.features).getDOMParser();
            return domParser.getFeature(name);
        } catch (SAXException e2) {
            throw new ParserConfigurationException(e2.getMessage());
        }
    }

    @Override // javax.xml.parsers.DocumentBuilderFactory
    public void setFeature(String name, boolean value) throws ParserConfigurationException {
        if (this.features == null) {
            this.features = new HashMap();
        }
        if (name.equals("http://javax.xml.XMLConstants/feature/secure-processing")) {
            if (System.getSecurityManager() != null && !value) {
                throw new ParserConfigurationException(SAXMessageFormatter.formatMessage(null, "jaxp-secureprocessing-feature", null));
            }
            this.fSecureProcess = value;
            this.features.put(name, value ? Boolean.TRUE : Boolean.FALSE);
            return;
        }
        this.features.put(name, value ? Boolean.TRUE : Boolean.FALSE);
        try {
            new DocumentBuilderImpl(this, this.attributes, this.features);
        } catch (SAXNotRecognizedException e2) {
            this.features.remove(name);
            throw new ParserConfigurationException(e2.getMessage());
        } catch (SAXNotSupportedException e3) {
            this.features.remove(name);
            throw new ParserConfigurationException(e3.getMessage());
        }
    }
}
