package com.sun.org.apache.xml.internal.serializer;

import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/serializer/Version.class */
public final class Version {
    public static String getVersion() {
        return getProduct() + " " + getImplementationLanguage() + " " + getMajorVersionNum() + "." + getReleaseVersionNum() + "." + (getDevelopmentVersionNum() > 0 ? PdfOps.D_TOKEN + getDevelopmentVersionNum() : "" + getMaintenanceVersionNum());
    }

    public static void _main(String[] argv) {
        System.out.println(getVersion());
    }

    public static String getProduct() {
        return "Serializer";
    }

    public static String getImplementationLanguage() {
        return "Java";
    }

    public static int getMajorVersionNum() {
        return 2;
    }

    public static int getReleaseVersionNum() {
        return 7;
    }

    public static int getMaintenanceVersionNum() {
        return 0;
    }

    public static int getDevelopmentVersionNum() {
        try {
            if (new String("").length() == 0) {
                return 0;
            }
            return Integer.parseInt("");
        } catch (NumberFormatException e2) {
            return 0;
        }
    }
}
