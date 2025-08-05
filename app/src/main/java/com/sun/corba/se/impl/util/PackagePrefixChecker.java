package com.sun.corba.se.impl.util;

/* loaded from: rt.jar:com/sun/corba/se/impl/util/PackagePrefixChecker.class */
public final class PackagePrefixChecker {
    private static final String PACKAGE_PREFIX = "org.omg.stub.";

    public static String packagePrefix() {
        return PACKAGE_PREFIX;
    }

    public static String correctPackageName(String str) {
        if (str == null) {
            return str;
        }
        if (hasOffendingPrefix(str)) {
            return PACKAGE_PREFIX + str;
        }
        return str;
    }

    public static boolean isOffendingPackage(String str) {
        return str != null && hasOffendingPrefix(str);
    }

    public static boolean hasOffendingPrefix(String str) {
        return str.startsWith("java.") || str.equals("java") || str.startsWith("net.jini.") || str.equals("net.jini") || str.startsWith("jini.") || str.equals("jini") || str.startsWith("javax.") || str.equals("javax");
    }

    public static boolean hasBeenPrefixed(String str) {
        return str.startsWith(packagePrefix());
    }

    public static String withoutPackagePrefix(String str) {
        return hasBeenPrefixed(str) ? str.substring(packagePrefix().length()) : str;
    }
}
