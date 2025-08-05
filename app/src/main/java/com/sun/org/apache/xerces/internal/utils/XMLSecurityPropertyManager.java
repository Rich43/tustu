package com.sun.org.apache.xerces.internal.utils;

import com.sun.org.apache.xerces.internal.impl.Constants;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/utils/XMLSecurityPropertyManager.class */
public final class XMLSecurityPropertyManager {
    private State[] states = {State.DEFAULT, State.DEFAULT};
    private final String[] values = new String[Property.values().length];

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/utils/XMLSecurityPropertyManager$State.class */
    public enum State {
        DEFAULT,
        FSP,
        JAXPDOTPROPERTIES,
        SYSTEMPROPERTY,
        APIPROPERTY
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/utils/XMLSecurityPropertyManager$Property.class */
    public enum Property {
        ACCESS_EXTERNAL_DTD("http://javax.xml.XMLConstants/property/accessExternalDTD", "all"),
        ACCESS_EXTERNAL_SCHEMA("http://javax.xml.XMLConstants/property/accessExternalSchema", "all");

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
        for (Property property : Property.values()) {
            this.values[property.ordinal()] = property.defaultValue();
        }
        readSystemProperties();
    }

    public boolean setValue(String propertyName, State state, Object value) {
        int index = getIndex(propertyName);
        if (index > -1) {
            setValue(index, state, (String) value);
            return true;
        }
        return false;
    }

    public void setValue(Property property, State state, String value) {
        if (state.compareTo(this.states[property.ordinal()]) >= 0) {
            this.values[property.ordinal()] = value;
            this.states[property.ordinal()] = state;
        }
    }

    public void setValue(int index, State state, String value) {
        if (state.compareTo(this.states[index]) >= 0) {
            this.values[index] = value;
            this.states[index] = state;
        }
    }

    public String getValue(String propertyName) {
        int index = getIndex(propertyName);
        if (index > -1) {
            return getValueByIndex(index);
        }
        return null;
    }

    public String getValue(Property property) {
        return this.values[property.ordinal()];
    }

    public String getValueByIndex(int index) {
        return this.values[index];
    }

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
        getSystemProperty(Property.ACCESS_EXTERNAL_SCHEMA, Constants.SP_ACCESS_EXTERNAL_SCHEMA);
    }

    private void getSystemProperty(Property property, String systemProperty) {
        try {
            String value = SecuritySupport.getSystemProperty(systemProperty);
            if (value != null) {
                this.values[property.ordinal()] = value;
                this.states[property.ordinal()] = State.SYSTEMPROPERTY;
            } else {
                String value2 = SecuritySupport.readJAXPProperty(systemProperty);
                if (value2 != null) {
                    this.values[property.ordinal()] = value2;
                    this.states[property.ordinal()] = State.JAXPDOTPROPERTIES;
                }
            }
        } catch (NumberFormatException e2) {
        }
    }
}
