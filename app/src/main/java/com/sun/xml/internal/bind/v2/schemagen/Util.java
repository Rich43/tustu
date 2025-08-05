package com.sun.xml.internal.bind.v2.schemagen;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/schemagen/Util.class */
public final class Util {
    private Util() {
    }

    public static String escapeURI(String s2) {
        StringBuilder sb = new StringBuilder();
        for (int i2 = 0; i2 < s2.length(); i2++) {
            char c2 = s2.charAt(i2);
            if (Character.isSpaceChar(c2)) {
                sb.append("%20");
            } else {
                sb.append(c2);
            }
        }
        return sb.toString();
    }

    public static String getParentUriPath(String uriPath) {
        int idx = uriPath.lastIndexOf(47);
        if (uriPath.endsWith("/")) {
            uriPath = uriPath.substring(0, idx);
            idx = uriPath.lastIndexOf(47);
        }
        return uriPath.substring(0, idx) + "/";
    }

    public static String normalizeUriPath(String uriPath) {
        if (uriPath.endsWith("/")) {
            return uriPath;
        }
        int idx = uriPath.lastIndexOf(47);
        return uriPath.substring(0, idx + 1);
    }

    public static boolean equalsIgnoreCase(String s2, String t2) {
        if (s2 == t2) {
            return true;
        }
        if (s2 != null && t2 != null) {
            return s2.equalsIgnoreCase(t2);
        }
        return false;
    }

    public static boolean equal(String s2, String t2) {
        if (s2 == t2) {
            return true;
        }
        if (s2 != null && t2 != null) {
            return s2.equals(t2);
        }
        return false;
    }
}
