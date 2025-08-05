package jdk.xml.internal;

/* loaded from: rt.jar:jdk/xml/internal/JdkXmlFeatures.class */
public class JdkXmlFeatures {
    public static final String ORACLE_JAXP_PROPERTY_PREFIX = "http://www.oracle.com/xml/jaxp/properties/";
    public static final String XML_FEATURE_MANAGER = "http://www.oracle.com/xml/jaxp/properties/XmlFeatureManager";
    public static final String ORACLE_FEATURE_SERVICE_MECHANISM = "http://www.oracle.com/feature/use-service-mechanism";
    public static final String ORACLE_ENABLE_EXTENSION_FUNCTION = "http://www.oracle.com/xml/jaxp/properties/enableExtensionFunctions";
    public static final String SP_ENABLE_EXTENSION_FUNCTION = "javax.xml.enableExtensionFunctions";
    public static final String SP_ENABLE_EXTENSION_FUNCTION_SPEC = "jdk.xml.enableExtensionFunctions";
    private final boolean[] featureValues = new boolean[XmlFeature.values().length];
    private final State[] states = new State[XmlFeature.values().length];
    boolean secureProcessing;

    /* loaded from: rt.jar:jdk/xml/internal/JdkXmlFeatures$XmlFeature.class */
    public enum XmlFeature {
        ENABLE_EXTENSION_FUNCTION("http://www.oracle.com/xml/jaxp/properties/enableExtensionFunctions", JdkXmlFeatures.SP_ENABLE_EXTENSION_FUNCTION_SPEC, "http://www.oracle.com/xml/jaxp/properties/enableExtensionFunctions", "javax.xml.enableExtensionFunctions", true, false, true, true),
        JDK_OVERRIDE_PARSER(JdkXmlUtils.OVERRIDE_PARSER, JdkXmlUtils.OVERRIDE_PARSER, "http://www.oracle.com/feature/use-service-mechanism", "http://www.oracle.com/feature/use-service-mechanism", false, false, true, false);

        private final String name;
        private final String nameSP;
        private final String nameOld;
        private final String nameOldSP;
        private final boolean valueDefault;
        private final boolean valueEnforced;
        private final boolean hasSystem;
        private final boolean enforced;

        XmlFeature(String name, String nameSP, String nameOld, String nameOldSP, boolean value, boolean valueEnforced, boolean hasSystem, boolean enforced) {
            this.name = name;
            this.nameSP = nameSP;
            this.nameOld = nameOld;
            this.nameOldSP = nameOldSP;
            this.valueDefault = value;
            this.valueEnforced = valueEnforced;
            this.hasSystem = hasSystem;
            this.enforced = enforced;
        }

        boolean equalsPropertyName(String propertyName) {
            return this.name.equals(propertyName) || (this.nameOld != null && this.nameOld.equals(propertyName));
        }

        public String apiProperty() {
            return this.name;
        }

        String systemProperty() {
            return this.nameSP;
        }

        String systemPropertyOld() {
            return this.nameOldSP;
        }

        public boolean defaultValue() {
            return this.valueDefault;
        }

        public boolean enforcedValue() {
            return this.valueEnforced;
        }

        boolean hasSystemProperty() {
            return this.hasSystem;
        }

        boolean enforced() {
            return this.enforced;
        }
    }

    /* loaded from: rt.jar:jdk/xml/internal/JdkXmlFeatures$State.class */
    public enum State {
        DEFAULT("default"),
        FSP("FEATURE_SECURE_PROCESSING"),
        JAXPDOTPROPERTIES("jaxp.properties"),
        SYSTEMPROPERTY("system property"),
        APIPROPERTY("property");

        final String literal;

        State(String literal) {
            this.literal = literal;
        }

        String literal() {
            return this.literal;
        }
    }

    public JdkXmlFeatures(boolean secureProcessing) {
        this.secureProcessing = secureProcessing;
        for (XmlFeature f2 : XmlFeature.values()) {
            if (secureProcessing && f2.enforced()) {
                this.featureValues[f2.ordinal()] = f2.enforcedValue();
                this.states[f2.ordinal()] = State.FSP;
            } else {
                this.featureValues[f2.ordinal()] = f2.defaultValue();
                this.states[f2.ordinal()] = State.DEFAULT;
            }
        }
        readSystemProperties();
    }

    public void update() {
        readSystemProperties();
    }

    public boolean setFeature(String propertyName, State state, Object value) {
        int index = getIndex(propertyName);
        if (index > -1) {
            setFeature(index, state, value);
            return true;
        }
        return false;
    }

    public void setFeature(XmlFeature feature, State state, boolean value) {
        setFeature(feature.ordinal(), state, value);
    }

    public boolean getFeature(XmlFeature feature) {
        return this.featureValues[feature.ordinal()];
    }

    public boolean getFeature(int index) {
        return this.featureValues[index];
    }

    public void setFeature(int index, State state, Object value) {
        boolean temp;
        if (Boolean.class.isAssignableFrom(value.getClass())) {
            temp = ((Boolean) value).booleanValue();
        } else {
            temp = Boolean.parseBoolean((String) value);
        }
        setFeature(index, state, temp);
    }

    public void setFeature(int index, State state, boolean value) {
        if (state.compareTo(this.states[index]) >= 0) {
            this.featureValues[index] = value;
            this.states[index] = state;
        }
    }

    public int getIndex(String propertyName) {
        for (XmlFeature feature : XmlFeature.values()) {
            if (feature.equalsPropertyName(propertyName)) {
                return feature.ordinal();
            }
        }
        return -1;
    }

    private void readSystemProperties() {
        String oldName;
        for (XmlFeature feature : XmlFeature.values()) {
            if (!getSystemProperty(feature, feature.systemProperty()) && (oldName = feature.systemPropertyOld()) != null) {
                getSystemProperty(feature, oldName);
            }
        }
    }

    private boolean getSystemProperty(XmlFeature feature, String sysPropertyName) {
        try {
            String value = SecuritySupport.getSystemProperty(sysPropertyName);
            if (value != null && !value.equals("")) {
                setFeature(feature, State.SYSTEMPROPERTY, Boolean.parseBoolean(value));
                return true;
            }
            String value2 = SecuritySupport.readJAXPProperty(sysPropertyName);
            if (value2 != null && !value2.equals("")) {
                setFeature(feature, State.JAXPDOTPROPERTIES, Boolean.parseBoolean(value2));
                return true;
            }
            return false;
        } catch (NumberFormatException e2) {
            throw new NumberFormatException("Invalid setting for system property: " + feature.systemProperty());
        }
    }
}
