package org.icepdf.core.application;

/* loaded from: icepdf-core.jar:org/icepdf/core/application/ProductInfo.class */
public class ProductInfo {
    public static String COMPANY = "ICEsoft Technologies, Inc.";
    public static String PRODUCT = "ICEpdf";
    public static String PRIMARY = "5";
    public static String SECONDARY = "1";
    public static String TERTIARY = "0";
    public static String RELEASE_TYPE = "";
    public static String BUILD_NO = "27";
    public static String REVISION = "42594";

    public String toString() {
        return "\n" + COMPANY + "\n" + PRODUCT + " " + PRIMARY + "." + SECONDARY + "." + TERTIARY + " " + RELEASE_TYPE + "\nBuild number: " + BUILD_NO + "\nRevision: " + REVISION + "\n";
    }

    public String getVersion() {
        return PRIMARY + "." + SECONDARY + "." + TERTIARY + " " + RELEASE_TYPE;
    }

    public static void main(String[] args) {
        ProductInfo app = new ProductInfo();
        System.out.println(app.toString());
    }
}
