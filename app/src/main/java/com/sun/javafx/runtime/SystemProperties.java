package com.sun.javafx.runtime;

import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import java.io.InputStream;
import java.security.AccessController;
import java.util.Hashtable;

/* loaded from: jfxrt.jar:com/sun/javafx/runtime/SystemProperties.class */
public class SystemProperties {
    private static final String[] sysprop_table = {"application.codebase", "jfx_specific", TransformerFactoryImpl.DEBUG, "javafx.debug"};
    private static final String[] jfxprop_table = {"application.codebase", ""};
    private static final Hashtable sysprop_list = new Hashtable();
    private static final Hashtable jfxprop_list = new Hashtable();
    private static final String versionResourceName = "/com/sun/javafx/runtime/resources/version.properties";
    private static boolean isDebug;
    private static String codebase_value;
    public static final String codebase = "javafx.application.codebase";

    static {
        AccessController.doPrivileged(() -> {
            addProperties(sysprop_table, false);
            addProperties(jfxprop_table, true);
            setVersions();
            isDebug = "true".equalsIgnoreCase(getProperty("javafx.debug"));
            return null;
        });
    }

    private static void setVersions() {
        InputStream is = SystemProperties.class.getResourceAsStream(versionResourceName);
        try {
            int size = is.available();
            byte[] b2 = new byte[size];
            is.read(b2);
            String inStr = new String(b2, "utf-8");
            setFXProperty("javafx.version", getValue(inStr, "release="));
            setFXProperty("javafx.runtime.version", getValue(inStr, "full="));
        } catch (Exception e2) {
        }
    }

    private static String getValue(String toSearch, String name) {
        int index = toSearch.indexOf(name);
        if (index != -1) {
            String s2 = toSearch.substring(index);
            int index2 = s2.indexOf(10);
            if (index2 != -1) {
                return s2.substring(name.length(), index2).trim();
            }
            return s2.substring(name.length(), s2.length()).trim();
        }
        return "unknown";
    }

    public static void addProperties(String[] table, boolean jfx_specific) {
        Hashtable props;
        if (table == null) {
            return;
        }
        if (jfx_specific) {
            props = jfxprop_list;
        } else {
            props = sysprop_list;
        }
        for (int i2 = 0; i2 < table.length; i2 += 2) {
            props.put(table[i2], table[i2 + 1]);
        }
    }

    public static String getProperty(String key) {
        Hashtable props = sysprop_list;
        if (key != null && key.startsWith("javafx.")) {
            String key2 = key.substring("javafx.".length());
            String found = (String) props.get(key2);
            if (found == null || found.equals("")) {
                return null;
            }
            if (found.equals("jfx_specific")) {
                Hashtable props2 = jfxprop_list;
                return (String) props2.get(key2);
            }
            return System.getProperty(found);
        }
        return null;
    }

    public static void clearProperty(String key) {
        if (key == null) {
            return;
        }
        Hashtable props = sysprop_list;
        if (key.startsWith("javafx.".toString())) {
            String key2 = key.substring("javafx.".length());
            String value = (String) props.get(key2);
            if (value == null) {
                return;
            }
            props.remove(key2);
            if (value.equals("jfx_specific")) {
                jfxprop_list.remove(key2);
            }
        }
    }

    public static void setFXProperty(String key, String value) {
        Hashtable props = sysprop_list;
        if (key.startsWith("javafx.")) {
            String key2 = key.substring("javafx.".length());
            String k2 = (String) props.get(key2);
            if (k2 == null) {
                props.put(key2, "jfx_specific");
                jfxprop_list.put(key2, value);
            } else if (k2.equals("jfx_specific")) {
                jfxprop_list.put(key2, value);
                if (codebase.equals("javafx." + key2)) {
                    codebase_value = value;
                }
            }
        }
    }

    public static boolean isDebug() {
        return isDebug;
    }

    public static String getCodebase() {
        return codebase_value;
    }

    public static void setCodebase(String value) {
        if (value == null) {
            value = "";
        }
        codebase_value = value;
        setFXProperty(codebase, value);
    }
}
