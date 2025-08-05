package com.sun.xml.internal.org.jvnet.mimepull;

import java.util.Properties;

/* loaded from: rt.jar:com/sun/xml/internal/org/jvnet/mimepull/PropUtil.class */
final class PropUtil {
    private PropUtil() {
    }

    public static boolean getBooleanSystemProperty(String name, boolean def) {
        try {
            return getBoolean(getProp(System.getProperties(), name), def);
        } catch (SecurityException e2) {
            try {
                String value = System.getProperty(name);
                if (value == null) {
                    return def;
                }
                if (def) {
                    return !value.equalsIgnoreCase("false");
                }
                return value.equalsIgnoreCase("true");
            } catch (SecurityException e3) {
                return def;
            }
        }
    }

    private static Object getProp(Properties props, String name) {
        Object val = props.get(name);
        if (val != null) {
            return val;
        }
        return props.getProperty(name);
    }

    private static boolean getBoolean(Object value, boolean def) {
        if (value == null) {
            return def;
        }
        if (value instanceof String) {
            if (def) {
                return !((String) value).equalsIgnoreCase("false");
            }
            return ((String) value).equalsIgnoreCase("true");
        }
        if (value instanceof Boolean) {
            return ((Boolean) value).booleanValue();
        }
        return def;
    }
}
