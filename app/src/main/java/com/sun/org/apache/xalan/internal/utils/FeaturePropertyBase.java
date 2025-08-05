package com.sun.org.apache.xalan.internal.utils;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/utils/FeaturePropertyBase.class */
public abstract class FeaturePropertyBase {
    String[] values = null;
    State[] states = {State.DEFAULT, State.DEFAULT};

    /* loaded from: rt.jar:com/sun/org/apache/xalan/internal/utils/FeaturePropertyBase$State.class */
    public enum State {
        DEFAULT,
        FSP,
        JAXPDOTPROPERTIES,
        SYSTEMPROPERTY,
        APIPROPERTY
    }

    public abstract int getIndex(String str);

    public void setValue(Enum property, State state, String value) {
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

    public boolean setValue(String propertyName, State state, Object value) {
        int index = getIndex(propertyName);
        if (index > -1) {
            setValue(index, state, (String) value);
            return true;
        }
        return false;
    }

    public boolean setValue(String propertyName, State state, boolean value) {
        int index = getIndex(propertyName);
        if (index > -1) {
            if (value) {
                setValue(index, state, "true");
                return true;
            }
            setValue(index, state, "false");
            return true;
        }
        return false;
    }

    public String getValue(Enum property) {
        return this.values[property.ordinal()];
    }

    public String getValue(String property) {
        int index = getIndex(property);
        if (index > -1) {
            return getValueByIndex(index);
        }
        return null;
    }

    public String getValueAsString(String propertyName) {
        int index = getIndex(propertyName);
        if (index > -1) {
            return getValueByIndex(index);
        }
        return null;
    }

    public String getValueByIndex(int index) {
        return this.values[index];
    }

    public <E extends Enum<E>> int getIndex(Class<E> property, String propertyName) {
        for (E e2 : property.getEnumConstants()) {
            if (e2.toString().equals(propertyName)) {
                return e2.ordinal();
            }
        }
        return -1;
    }

    void getSystemProperty(Enum property, String systemProperty) {
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
