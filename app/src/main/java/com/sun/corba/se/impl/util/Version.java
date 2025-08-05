package com.sun.corba.se.impl.util;

/* loaded from: rt.jar:com/sun/corba/se/impl/util/Version.class */
public class Version {
    public static final String PROJECT_NAME = "RMI-IIOP";
    public static final String VERSION = "1.0";
    public static final String BUILD = "0.0";
    public static final String BUILD_TIME = "unknown";
    public static final String FULL = "RMI-IIOP 1.0 (unknown)";

    public static String asString() {
        return FULL;
    }

    public static void main(String[] strArr) {
        System.out.println(FULL);
    }
}
