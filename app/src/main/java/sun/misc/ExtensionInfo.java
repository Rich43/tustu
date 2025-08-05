package sun.misc;

import java.text.MessageFormat;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.jar.Attributes;
import sun.util.locale.LanguageTag;

/* loaded from: rt.jar:sun/misc/ExtensionInfo.class */
public class ExtensionInfo {
    public static final int COMPATIBLE = 0;
    public static final int REQUIRE_SPECIFICATION_UPGRADE = 1;
    public static final int REQUIRE_IMPLEMENTATION_UPGRADE = 2;
    public static final int REQUIRE_VENDOR_SWITCH = 3;
    public static final int INCOMPATIBLE = 4;
    public String title;
    public String name;
    public String specVersion;
    public String specVendor;
    public String implementationVersion;
    public String vendor;
    public String vendorId;
    public String url;
    private static final ResourceBundle rb = ResourceBundle.getBundle("sun.misc.resources.Messages");

    public ExtensionInfo() {
    }

    public ExtensionInfo(String str, Attributes attributes) throws NullPointerException {
        String str2;
        if (str != null) {
            str2 = str + LanguageTag.SEP;
        } else {
            str2 = "";
        }
        this.name = attributes.getValue(str2 + Attributes.Name.EXTENSION_NAME.toString());
        if (this.name != null) {
            this.name = this.name.trim();
        }
        this.title = attributes.getValue(str2 + Attributes.Name.SPECIFICATION_TITLE.toString());
        if (this.title != null) {
            this.title = this.title.trim();
        }
        this.specVersion = attributes.getValue(str2 + Attributes.Name.SPECIFICATION_VERSION.toString());
        if (this.specVersion != null) {
            this.specVersion = this.specVersion.trim();
        }
        this.specVendor = attributes.getValue(str2 + Attributes.Name.SPECIFICATION_VENDOR.toString());
        if (this.specVendor != null) {
            this.specVendor = this.specVendor.trim();
        }
        this.implementationVersion = attributes.getValue(str2 + Attributes.Name.IMPLEMENTATION_VERSION.toString());
        if (this.implementationVersion != null) {
            this.implementationVersion = this.implementationVersion.trim();
        }
        this.vendor = attributes.getValue(str2 + Attributes.Name.IMPLEMENTATION_VENDOR.toString());
        if (this.vendor != null) {
            this.vendor = this.vendor.trim();
        }
        this.vendorId = attributes.getValue(str2 + Attributes.Name.IMPLEMENTATION_VENDOR_ID.toString());
        if (this.vendorId != null) {
            this.vendorId = this.vendorId.trim();
        }
        this.url = attributes.getValue(str2 + Attributes.Name.IMPLEMENTATION_URL.toString());
        if (this.url != null) {
            this.url = this.url.trim();
        }
    }

    public int isCompatibleWith(ExtensionInfo extensionInfo) {
        if (this.name != null && extensionInfo.name != null && this.name.compareTo(extensionInfo.name) == 0) {
            if (this.specVersion == null || extensionInfo.specVersion == null) {
                return 0;
            }
            if (compareExtensionVersion(this.specVersion, extensionInfo.specVersion) < 0) {
                if (this.vendorId != null && extensionInfo.vendorId != null && this.vendorId.compareTo(extensionInfo.vendorId) != 0) {
                    return 3;
                }
                return 1;
            }
            if (this.vendorId != null && extensionInfo.vendorId != null) {
                if (this.vendorId.compareTo(extensionInfo.vendorId) != 0) {
                    return 3;
                }
                if (this.implementationVersion != null && extensionInfo.implementationVersion != null && compareExtensionVersion(this.implementationVersion, extensionInfo.implementationVersion) < 0) {
                    return 2;
                }
                return 0;
            }
            return 0;
        }
        return 4;
    }

    public String toString() {
        return "Extension : title(" + this.title + "), name(" + this.name + "), spec vendor(" + this.specVendor + "), spec version(" + this.specVersion + "), impl vendor(" + this.vendor + "), impl vendor id(" + this.vendorId + "), impl version(" + this.implementationVersion + "), impl url(" + this.url + ")";
    }

    private int compareExtensionVersion(String str, String str2) throws NumberFormatException {
        return strictCompareExtensionVersion(str.toLowerCase(), str2.toLowerCase());
    }

    private int strictCompareExtensionVersion(String str, String str2) throws NumberFormatException {
        if (str.equals(str2)) {
            return 0;
        }
        StringTokenizer stringTokenizer = new StringTokenizer(str, ".,");
        StringTokenizer stringTokenizer2 = new StringTokenizer(str2, ".,");
        int iConvertToken = 0;
        int iConvertToken2 = 0;
        if (stringTokenizer.hasMoreTokens()) {
            iConvertToken = convertToken(stringTokenizer.nextToken().toString());
        }
        if (stringTokenizer2.hasMoreTokens()) {
            iConvertToken2 = convertToken(stringTokenizer2.nextToken().toString());
        }
        if (iConvertToken > iConvertToken2) {
            return 1;
        }
        if (iConvertToken2 > iConvertToken) {
            return -1;
        }
        int iIndexOf = str.indexOf(".");
        int iIndexOf2 = str2.indexOf(".");
        if (iIndexOf == -1) {
            iIndexOf = str.length() - 1;
        }
        if (iIndexOf2 == -1) {
            iIndexOf2 = str2.length() - 1;
        }
        return strictCompareExtensionVersion(str.substring(iIndexOf + 1), str2.substring(iIndexOf2 + 1));
    }

    private int convertToken(String str) {
        int i2;
        if (str == null || str.equals("")) {
            return 0;
        }
        int i3 = 0;
        int length = str.length();
        String str2 = new MessageFormat(rb.getString("optpkg.versionerror")).format(new Object[]{this.name});
        int iIndexOf = str.indexOf(LanguageTag.SEP);
        int iIndexOf2 = str.indexOf("_");
        if (iIndexOf == -1 && iIndexOf2 == -1) {
            try {
                return Integer.parseInt(str) * 100;
            } catch (NumberFormatException e2) {
                System.out.println(str2);
                return 0;
            }
        }
        if (iIndexOf2 != -1) {
            try {
                int i4 = Integer.parseInt(str.substring(0, iIndexOf2));
                char cCharAt = str.charAt(length - 1);
                if (Character.isLetter(cCharAt)) {
                    int numericValue = Character.getNumericValue(cCharAt);
                    i2 = Integer.parseInt(str.substring(iIndexOf2 + 1, length - 1));
                    if (numericValue >= Character.getNumericValue('a') && numericValue <= Character.getNumericValue('z')) {
                        i3 = (i2 * 100) + numericValue;
                    } else {
                        i3 = 0;
                        System.out.println(str2);
                    }
                } else {
                    i2 = Integer.parseInt(str.substring(iIndexOf2 + 1, length));
                }
                return (i4 * 100) + i2 + i3;
            } catch (NumberFormatException e3) {
                System.out.println(str2);
                return 0;
            }
        }
        try {
            int i5 = Integer.parseInt(str.substring(0, iIndexOf));
            String strSubstring = str.substring(iIndexOf + 1);
            String strSubstring2 = "";
            int i6 = 0;
            if (strSubstring.indexOf("ea") != -1) {
                strSubstring2 = strSubstring.substring(2);
                i6 = 50;
            } else if (strSubstring.indexOf("alpha") != -1) {
                strSubstring2 = strSubstring.substring(5);
                i6 = 40;
            } else if (strSubstring.indexOf("beta") != -1) {
                strSubstring2 = strSubstring.substring(4);
                i6 = 30;
            } else if (strSubstring.indexOf("rc") != -1) {
                strSubstring2 = strSubstring.substring(2);
                i6 = 20;
            }
            if (strSubstring2 == null || strSubstring2.equals("")) {
                return (i5 * 100) - i6;
            }
            try {
                return ((i5 * 100) - i6) + Integer.parseInt(strSubstring2);
            } catch (NumberFormatException e4) {
                System.out.println(str2);
                return 0;
            }
        } catch (NumberFormatException e5) {
            System.out.println(str2);
            return 0;
        }
    }
}
