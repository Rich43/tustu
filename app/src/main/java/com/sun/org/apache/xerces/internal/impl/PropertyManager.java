package com.sun.org.apache.xerces.internal.impl;

import com.sun.org.apache.xerces.internal.utils.XMLSecurityManager;
import com.sun.org.apache.xerces.internal.utils.XMLSecurityPropertyManager;
import com.sun.xml.internal.stream.StaxEntityResolverWrapper;
import java.util.HashMap;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLResolver;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/PropertyManager.class */
public class PropertyManager {
    public static final String STAX_NOTATIONS = "javax.xml.stream.notations";
    public static final String STAX_ENTITIES = "javax.xml.stream.entities";
    private static final String STRING_INTERNING = "http://xml.org/sax/features/string-interning";
    private static final String SECURITY_MANAGER = "http://apache.org/xml/properties/security-manager";
    private static final String XML_SECURITY_PROPERTY_MANAGER = "http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager";
    HashMap supportedProps = new HashMap();
    private XMLSecurityManager fSecurityManager;
    private XMLSecurityPropertyManager fSecurityPropertyMgr;
    public static final int CONTEXT_READER = 1;
    public static final int CONTEXT_WRITER = 2;

    public PropertyManager(int context) {
        switch (context) {
            case 1:
                initConfigurableReaderProperties();
                break;
            case 2:
                initWriterProps();
                break;
        }
    }

    public PropertyManager(PropertyManager propertyManager) {
        HashMap properties = propertyManager.getProperties();
        this.supportedProps.putAll(properties);
        this.fSecurityManager = (XMLSecurityManager) getProperty("http://apache.org/xml/properties/security-manager");
        this.fSecurityPropertyMgr = (XMLSecurityPropertyManager) getProperty("http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager");
    }

    private HashMap getProperties() {
        return this.supportedProps;
    }

    private void initConfigurableReaderProperties() {
        this.supportedProps.put(XMLInputFactory.IS_NAMESPACE_AWARE, Boolean.TRUE);
        this.supportedProps.put(XMLInputFactory.IS_VALIDATING, Boolean.FALSE);
        this.supportedProps.put(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, Boolean.TRUE);
        this.supportedProps.put(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.TRUE);
        this.supportedProps.put(XMLInputFactory.IS_COALESCING, Boolean.FALSE);
        this.supportedProps.put(XMLInputFactory.SUPPORT_DTD, Boolean.TRUE);
        this.supportedProps.put(XMLInputFactory.REPORTER, null);
        this.supportedProps.put(XMLInputFactory.RESOLVER, null);
        this.supportedProps.put(XMLInputFactory.ALLOCATOR, null);
        this.supportedProps.put(STAX_NOTATIONS, null);
        this.supportedProps.put("http://xml.org/sax/features/string-interning", new Boolean(true));
        this.supportedProps.put("http://apache.org/xml/features/allow-java-encodings", new Boolean(true));
        this.supportedProps.put(Constants.ADD_NAMESPACE_DECL_AS_ATTRIBUTE, Boolean.FALSE);
        this.supportedProps.put(Constants.READER_IN_DEFINED_STATE, new Boolean(true));
        this.supportedProps.put(Constants.REUSE_INSTANCE, new Boolean(true));
        this.supportedProps.put("http://java.sun.com/xml/stream/properties/report-cdata-event", new Boolean(false));
        this.supportedProps.put("http://java.sun.com/xml/stream/properties/ignore-external-dtd", Boolean.FALSE);
        this.supportedProps.put("http://apache.org/xml/features/validation/warn-on-duplicate-attdef", new Boolean(false));
        this.supportedProps.put("http://apache.org/xml/features/warn-on-duplicate-entitydef", new Boolean(false));
        this.supportedProps.put("http://apache.org/xml/features/validation/warn-on-undeclared-elemdef", new Boolean(false));
        this.fSecurityManager = new XMLSecurityManager(true);
        this.supportedProps.put("http://apache.org/xml/properties/security-manager", this.fSecurityManager);
        this.fSecurityPropertyMgr = new XMLSecurityPropertyManager();
        this.supportedProps.put("http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager", this.fSecurityPropertyMgr);
    }

    private void initWriterProps() {
        this.supportedProps.put(XMLOutputFactory.IS_REPAIRING_NAMESPACES, Boolean.FALSE);
        this.supportedProps.put(Constants.ESCAPE_CHARACTERS, Boolean.TRUE);
        this.supportedProps.put(Constants.REUSE_INSTANCE, new Boolean(true));
    }

    public boolean containsProperty(String property) {
        return this.supportedProps.containsKey(property) || (this.fSecurityManager != null && this.fSecurityManager.getIndex(property) > -1) || (this.fSecurityPropertyMgr != null && this.fSecurityPropertyMgr.getIndex(property) > -1);
    }

    public Object getProperty(String property) {
        return this.supportedProps.get(property);
    }

    public void setProperty(String property, Object value) {
        String equivalentProperty = null;
        if (property == XMLInputFactory.IS_NAMESPACE_AWARE || property.equals(XMLInputFactory.IS_NAMESPACE_AWARE)) {
            equivalentProperty = "http://apache.org/xml/features/namespaces";
        } else if (property == XMLInputFactory.IS_VALIDATING || property.equals(XMLInputFactory.IS_VALIDATING)) {
            if ((value instanceof Boolean) && ((Boolean) value).booleanValue()) {
                throw new IllegalArgumentException("true value of isValidating not supported");
            }
        } else if (property == "http://xml.org/sax/features/string-interning" || property.equals("http://xml.org/sax/features/string-interning")) {
            if ((value instanceof Boolean) && !((Boolean) value).booleanValue()) {
                throw new IllegalArgumentException("false value of http://xml.org/sax/features/string-interningfeature is not supported");
            }
        } else if (property == XMLInputFactory.RESOLVER || property.equals(XMLInputFactory.RESOLVER)) {
            this.supportedProps.put("http://apache.org/xml/properties/internal/stax-entity-resolver", new StaxEntityResolverWrapper((XMLResolver) value));
        }
        if (property.equals("http://apache.org/xml/properties/security-manager")) {
            this.fSecurityManager = XMLSecurityManager.convert(value, this.fSecurityManager);
            this.supportedProps.put("http://apache.org/xml/properties/security-manager", this.fSecurityManager);
            return;
        }
        if (property.equals("http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager")) {
            if (value == null) {
                this.fSecurityPropertyMgr = new XMLSecurityPropertyManager();
            } else {
                this.fSecurityPropertyMgr = (XMLSecurityPropertyManager) value;
            }
            this.supportedProps.put("http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager", this.fSecurityPropertyMgr);
            return;
        }
        if ((this.fSecurityManager == null || !this.fSecurityManager.setLimit(property, XMLSecurityManager.State.APIPROPERTY, value)) && (this.fSecurityPropertyMgr == null || !this.fSecurityPropertyMgr.setValue(property, XMLSecurityPropertyManager.State.APIPROPERTY, value))) {
            this.supportedProps.put(property, value);
        }
        if (equivalentProperty != null) {
            this.supportedProps.put(equivalentProperty, value);
        }
    }

    public String toString() {
        return this.supportedProps.toString();
    }
}
