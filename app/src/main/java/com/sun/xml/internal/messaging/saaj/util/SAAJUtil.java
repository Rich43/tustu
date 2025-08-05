package com.sun.xml.internal.messaging.saaj.util;

import java.security.AccessControlException;

/* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/util/SAAJUtil.class */
public final class SAAJUtil {
    public static boolean getSystemBoolean(String arg) {
        try {
            return Boolean.getBoolean(arg);
        } catch (AccessControlException e2) {
            return false;
        }
    }

    public static String getSystemProperty(String arg) {
        try {
            return System.getProperty(arg);
        } catch (SecurityException e2) {
            return null;
        }
    }
}
