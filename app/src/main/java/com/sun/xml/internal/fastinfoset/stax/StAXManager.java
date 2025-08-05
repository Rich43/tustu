package com.sun.xml.internal.fastinfoset.stax;

import com.sun.xml.internal.fastinfoset.CommonResourceBundle;
import java.util.HashMap;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;

/* loaded from: rt.jar:com/sun/xml/internal/fastinfoset/stax/StAXManager.class */
public class StAXManager {
    protected static final String STAX_NOTATIONS = "javax.xml.stream.notations";
    protected static final String STAX_ENTITIES = "javax.xml.stream.entities";
    HashMap features = new HashMap();
    public static final int CONTEXT_READER = 1;
    public static final int CONTEXT_WRITER = 2;

    public StAXManager() {
    }

    public StAXManager(int context) {
        switch (context) {
            case 1:
                initConfigurableReaderProperties();
                break;
            case 2:
                initWriterProps();
                break;
        }
    }

    public StAXManager(StAXManager manager) {
        HashMap properties = manager.getProperties();
        this.features.putAll(properties);
    }

    private HashMap getProperties() {
        return this.features;
    }

    private void initConfigurableReaderProperties() {
        this.features.put(XMLInputFactory.IS_NAMESPACE_AWARE, Boolean.TRUE);
        this.features.put(XMLInputFactory.IS_VALIDATING, Boolean.FALSE);
        this.features.put(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, Boolean.TRUE);
        this.features.put(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.TRUE);
        this.features.put(XMLInputFactory.IS_COALESCING, Boolean.FALSE);
        this.features.put(XMLInputFactory.SUPPORT_DTD, Boolean.FALSE);
        this.features.put(XMLInputFactory.REPORTER, null);
        this.features.put(XMLInputFactory.RESOLVER, null);
        this.features.put(XMLInputFactory.ALLOCATOR, null);
        this.features.put("javax.xml.stream.notations", null);
    }

    private void initWriterProps() {
        this.features.put(XMLOutputFactory.IS_REPAIRING_NAMESPACES, Boolean.FALSE);
    }

    public boolean containsProperty(String property) {
        return this.features.containsKey(property);
    }

    public Object getProperty(String name) {
        checkProperty(name);
        return this.features.get(name);
    }

    public void setProperty(String name, Object value) {
        checkProperty(name);
        if (name.equals(XMLInputFactory.IS_VALIDATING) && Boolean.TRUE.equals(value)) {
            throw new IllegalArgumentException(CommonResourceBundle.getInstance().getString("message.validationNotSupported") + CommonResourceBundle.getInstance().getString("support_validation"));
        }
        if (name.equals(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES) && Boolean.TRUE.equals(value)) {
            throw new IllegalArgumentException(CommonResourceBundle.getInstance().getString("message.externalEntities") + CommonResourceBundle.getInstance().getString("resolve_external_entities_"));
        }
        this.features.put(name, value);
    }

    public void checkProperty(String name) {
        if (!this.features.containsKey(name)) {
            throw new IllegalArgumentException(CommonResourceBundle.getInstance().getString("message.propertyNotSupported", new Object[]{name}));
        }
    }

    public String toString() {
        return this.features.toString();
    }
}
