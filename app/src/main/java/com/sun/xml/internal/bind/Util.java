package com.sun.xml.internal.bind;

import java.util.logging.Logger;

/* loaded from: rt.jar:com/sun/xml/internal/bind/Util.class */
public final class Util {
    private Util() {
    }

    public static Logger getClassLogger() {
        try {
            StackTraceElement[] trace = new Exception().getStackTrace();
            return Logger.getLogger(trace[1].getClassName());
        } catch (SecurityException e2) {
            return Logger.getLogger("com.sun.xml.internal.bind");
        }
    }

    public static String getSystemProperty(String name) {
        try {
            return System.getProperty(name);
        } catch (SecurityException e2) {
            return null;
        }
    }
}
