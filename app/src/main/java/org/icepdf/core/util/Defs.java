package org.icepdf.core.util;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: icepdf-core.jar:org/icepdf/core/util/Defs.class */
public class Defs {
    private static final Logger logger = Logger.getLogger(Defs.class.toString());

    public static String property(String name) {
        return property(name, null);
    }

    public static String property(String name, String defaultValue) {
        try {
            return System.getProperty(name, defaultValue);
        } catch (SecurityException ex) {
            logger.log(Level.FINE, "Security exception, property could not be set.", (Throwable) ex);
            return defaultValue;
        }
    }

    public static int intProperty(String name, int defaultValue) {
        String value = property(name);
        if (value != null) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException ex) {
                logger.log(Level.FINE, "Failed to parse property.", (Throwable) ex);
            }
        }
        return defaultValue;
    }

    public static double doubleProperty(String name, double defaultValue) {
        String value = property(name);
        if (value != null) {
            try {
                return Double.parseDouble(value);
            } catch (NumberFormatException ex) {
                logger.log(Level.FINE, "Failed to parse property.", (Throwable) ex);
            }
        }
        return defaultValue;
    }

    public static boolean booleanProperty(String name) {
        return booleanProperty(name, false);
    }

    public static boolean booleanProperty(String name, boolean defaultValue) {
        String value = property(name);
        if (value != null) {
            switch (value.length()) {
                case 2:
                    if ("no".equals(value.toLowerCase())) {
                        return false;
                    }
                    break;
                case 3:
                    if ("yes".equals(value.toLowerCase())) {
                        return true;
                    }
                    break;
                case 4:
                    if ("true".equals(value.toLowerCase())) {
                        return true;
                    }
                    break;
                case 5:
                    if ("false".equals(value.toLowerCase())) {
                        return false;
                    }
                    break;
            }
        }
        return defaultValue;
    }

    public static String sysProperty(String name) {
        return property(name);
    }

    public static String sysProperty(String name, String defaultValue) {
        return property(name, defaultValue);
    }

    public static int sysPropertyInt(String name, int defaultValue) {
        return intProperty(name, defaultValue);
    }

    public static double sysPropertyDouble(String name, double defaultValue) {
        return doubleProperty(name, defaultValue);
    }

    public static boolean sysPropertyBoolean(String name) {
        return booleanProperty(name);
    }

    public static boolean sysPropertyBoolean(String name, boolean defaultValue) {
        return booleanProperty(name, defaultValue);
    }

    public static void setProperty(String property, Object value) {
        try {
            Properties prop = System.getProperties();
            if (value != null) {
                prop.put(property, value);
            }
        } catch (SecurityException ex) {
            logger.log(Level.FINE, "Security exception, property could not be set.", (Throwable) ex);
        }
    }

    public static void setSystemProperty(String name, String value) {
        setProperty(name, value);
    }
}
