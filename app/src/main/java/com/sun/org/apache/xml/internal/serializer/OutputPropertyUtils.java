package com.sun.org.apache.xml.internal.serializer;

import java.util.Properties;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/serializer/OutputPropertyUtils.class */
public final class OutputPropertyUtils {
    public static boolean getBooleanProperty(String key, Properties props) {
        String s2 = props.getProperty(key);
        if (null == s2 || !s2.equals("yes")) {
            return false;
        }
        return true;
    }

    public static int getIntProperty(String key, Properties props) {
        String s2 = props.getProperty(key);
        if (null == s2) {
            return 0;
        }
        return Integer.parseInt(s2);
    }
}
