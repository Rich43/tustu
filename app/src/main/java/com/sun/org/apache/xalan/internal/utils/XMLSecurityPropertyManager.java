package com.sun.org.apache.xalan.internal.utils;

import com.sun.org.apache.xalan.internal.XalanConstants;
import javax.xml.XMLConstants;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/utils/XMLSecurityPropertyManager.class */
public final class XMLSecurityPropertyManager extends FeaturePropertyBase {

    /* loaded from: rt.jar:com/sun/org/apache/xalan/internal/utils/XMLSecurityPropertyManager$Property.class */
    public enum Property {
        ACCESS_EXTERNAL_DTD("http://javax.xml.XMLConstants/property/accessExternalDTD", "all"),
        ACCESS_EXTERNAL_STYLESHEET(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "all");

        final String name;
        final String defaultValue;

        Property(String name, String value) {
            this.name = name;
            this.defaultValue = value;
        }

        public boolean equalsName(String propertyName) {
            if (propertyName == null) {
                return false;
            }
            return this.name.equals(propertyName);
        }

        String defaultValue() {
            return this.defaultValue;
        }
    }

    public XMLSecurityPropertyManager() {
        this.values = new String[Property.values().length];
        for (Property property : Property.values()) {
            this.values[property.ordinal()] = property.defaultValue();
        }
        readSystemProperties();
    }

    @Override // com.sun.org.apache.xalan.internal.utils.FeaturePropertyBase
    public int getIndex(String propertyName) {
        for (Property property : Property.values()) {
            if (property.equalsName(propertyName)) {
                return property.ordinal();
            }
        }
        return -1;
    }

    private void readSystemProperties() {
        getSystemProperty(Property.ACCESS_EXTERNAL_DTD, "javax.xml.accessExternalDTD");
        getSystemProperty(Property.ACCESS_EXTERNAL_STYLESHEET, XalanConstants.SP_ACCESS_EXTERNAL_STYLESHEET);
    }
}
