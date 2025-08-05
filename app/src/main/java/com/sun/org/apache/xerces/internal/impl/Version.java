package com.sun.org.apache.xerces.internal.impl;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/Version.class */
public class Version {
    public static final String fVersion = getVersion();
    private static final String fImmutableVersion = "Xerces-J 2.7.1";

    public static String getVersion() {
        return fImmutableVersion;
    }

    public static void main(String[] argv) {
        System.out.println(fVersion);
    }
}
