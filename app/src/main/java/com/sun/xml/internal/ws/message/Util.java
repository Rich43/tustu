package com.sun.xml.internal.ws.message;

/* loaded from: rt.jar:com/sun/xml/internal/ws/message/Util.class */
public abstract class Util {
    public static boolean parseBool(String value) {
        if (value.length() == 0) {
            return false;
        }
        char ch = value.charAt(0);
        return ch == 't' || ch == '1';
    }
}
