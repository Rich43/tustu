package com.sun.org.apache.xml.internal.resolver.helpers;

import javafx.fxml.FXMLLoader;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.icepdf.core.util.PdfOps;
import org.slf4j.Marker;
import sun.security.pkcs11.wrapper.Constants;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/resolver/helpers/PublicId.class */
public abstract class PublicId {
    protected PublicId() {
    }

    public static String normalize(String publicId) {
        String strTrim = publicId.replace('\t', ' ').replace('\r', ' ').replace('\n', ' ').trim();
        while (true) {
            String normal = strTrim;
            int pos = normal.indexOf(Constants.INDENT);
            if (pos >= 0) {
                strTrim = normal.substring(0, pos) + normal.substring(pos + 1);
            } else {
                return normal;
            }
        }
    }

    public static String encodeURN(String publicId) {
        String urn = normalize(publicId);
        return "urn:publicid:" + stringReplace(stringReplace(stringReplace(stringReplace(stringReplace(stringReplace(stringReplace(stringReplace(stringReplace(stringReplace(stringReplace(urn, FXMLLoader.RESOURCE_KEY_PREFIX, "%25"), ";", "%3B"), PdfOps.SINGLE_QUOTE_TOKEN, "%27"), "?", "%3F"), FXMLLoader.CONTROLLER_METHOD_PREFIX, "%23"), Marker.ANY_NON_NULL_MARKER, "%2B"), " ", Marker.ANY_NON_NULL_MARKER), "::", ";"), CallSiteDescriptor.TOKEN_DELIMITER, "%3A"), "//", CallSiteDescriptor.TOKEN_DELIMITER), "/", "%2F");
    }

    public static String decodeURN(String urn) {
        if (urn.startsWith("urn:publicid:")) {
            String publicId = urn.substring(13);
            return stringReplace(stringReplace(stringReplace(stringReplace(stringReplace(stringReplace(stringReplace(stringReplace(stringReplace(stringReplace(stringReplace(publicId, "%2F", "/"), CallSiteDescriptor.TOKEN_DELIMITER, "//"), "%3A", CallSiteDescriptor.TOKEN_DELIMITER), ";", "::"), Marker.ANY_NON_NULL_MARKER, " "), "%2B", Marker.ANY_NON_NULL_MARKER), "%23", FXMLLoader.CONTROLLER_METHOD_PREFIX), "%3F", "?"), "%27", PdfOps.SINGLE_QUOTE_TOKEN), "%3B", ";"), "%25", FXMLLoader.RESOURCE_KEY_PREFIX);
        }
        return urn;
    }

    private static String stringReplace(String str, String oldStr, String newStr) {
        String result = "";
        int iIndexOf = str.indexOf(oldStr);
        while (true) {
            int pos = iIndexOf;
            if (pos >= 0) {
                result = (result + str.substring(0, pos)) + newStr;
                str = str.substring(pos + 1);
                iIndexOf = str.indexOf(oldStr);
            } else {
                return result + str;
            }
        }
    }
}
