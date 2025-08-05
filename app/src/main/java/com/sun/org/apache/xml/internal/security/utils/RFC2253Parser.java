package com.sun.org.apache.xml.internal.security.utils;

import java.io.IOException;
import java.io.StringReader;
import javafx.fxml.FXMLLoader;
import org.icepdf.core.util.PdfOps;
import org.slf4j.Marker;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/utils/RFC2253Parser.class */
public class RFC2253Parser {
    public static String rfc2253toXMLdsig(String str) {
        return rfctoXML(normalize(str, true));
    }

    public static String xmldsigtoRFC2253(String str) {
        return xmltoRFC(normalize(str, false));
    }

    public static String normalize(String str) {
        return normalize(str, true);
    }

    public static String normalize(String str, boolean z2) {
        if (str == null || str.equals("")) {
            return "";
        }
        try {
            String strSemicolonToComma = semicolonToComma(str);
            StringBuilder sb = new StringBuilder();
            int i2 = 0;
            int iCountQuotes = 0;
            int i3 = 0;
            while (true) {
                int iIndexOf = strSemicolonToComma.indexOf(44, i3);
                if (iIndexOf >= 0) {
                    iCountQuotes += countQuotes(strSemicolonToComma, i3, iIndexOf);
                    if (iIndexOf > 0 && strSemicolonToComma.charAt(iIndexOf - 1) != '\\' && iCountQuotes % 2 == 0) {
                        sb.append(parseRDN(strSemicolonToComma.substring(i2, iIndexOf).trim(), z2)).append(",");
                        i2 = iIndexOf + 1;
                        iCountQuotes = 0;
                    }
                    i3 = iIndexOf + 1;
                } else {
                    sb.append(parseRDN(trim(strSemicolonToComma.substring(i2)), z2));
                    return sb.toString();
                }
            }
        } catch (IOException e2) {
            return str;
        }
    }

    static String parseRDN(String str, boolean z2) throws IOException {
        StringBuilder sb = new StringBuilder();
        int i2 = 0;
        int iCountQuotes = 0;
        int i3 = 0;
        while (true) {
            int i4 = i3;
            int iIndexOf = str.indexOf(43, i4);
            if (iIndexOf >= 0) {
                iCountQuotes += countQuotes(str, i4, iIndexOf);
                if (iIndexOf > 0 && str.charAt(iIndexOf - 1) != '\\' && iCountQuotes % 2 == 0) {
                    sb.append(parseATAV(trim(str.substring(i2, iIndexOf)), z2)).append(Marker.ANY_NON_NULL_MARKER);
                    i2 = iIndexOf + 1;
                    iCountQuotes = 0;
                }
                i3 = iIndexOf + 1;
            } else {
                sb.append(parseATAV(trim(str.substring(i2)), z2));
                return sb.toString();
            }
        }
    }

    static String parseATAV(String str, boolean z2) throws IOException {
        String strNormalizeV;
        int iIndexOf = str.indexOf(61);
        if (iIndexOf == -1 || (iIndexOf > 0 && str.charAt(iIndexOf - 1) == '\\')) {
            return str;
        }
        String strNormalizeAT = normalizeAT(str.substring(0, iIndexOf));
        if (strNormalizeAT.charAt(0) >= '0' && strNormalizeAT.charAt(0) <= '9') {
            strNormalizeV = str.substring(iIndexOf + 1);
        } else {
            strNormalizeV = normalizeV(str.substring(iIndexOf + 1), z2);
        }
        return strNormalizeAT + "=" + strNormalizeV;
    }

    static String normalizeAT(String str) {
        String strTrim = str.toUpperCase().trim();
        if (strTrim.startsWith("OID")) {
            strTrim = strTrim.substring(3);
        }
        return strTrim;
    }

    static String normalizeV(String str, boolean z2) throws IOException {
        String strTrim = trim(str);
        if (strTrim.startsWith(PdfOps.DOUBLE_QUOTE__TOKEN)) {
            StringBuilder sb = new StringBuilder();
            StringReader stringReader = new StringReader(strTrim.substring(1, strTrim.length() - 1));
            while (true) {
                int i2 = stringReader.read();
                if (i2 <= -1) {
                    break;
                }
                char c2 = (char) i2;
                if (c2 == ',' || c2 == '=' || c2 == '+' || c2 == '<' || c2 == '>' || c2 == '#' || c2 == ';') {
                    sb.append('\\');
                }
                sb.append(c2);
            }
            strTrim = trim(sb.toString());
        }
        if (z2) {
            if (strTrim.startsWith(FXMLLoader.CONTROLLER_METHOD_PREFIX)) {
                strTrim = '\\' + strTrim;
            }
        } else if (strTrim.startsWith("\\#")) {
            strTrim = strTrim.substring(1);
        }
        return strTrim;
    }

    static String rfctoXML(String str) {
        try {
            return changeWStoXML(changeLess32toXML(str));
        } catch (Exception e2) {
            return str;
        }
    }

    static String xmltoRFC(String str) {
        try {
            return changeWStoRFC(changeLess32toRFC(str));
        } catch (Exception e2) {
            return str;
        }
    }

    static String changeLess32toRFC(String str) throws IOException {
        StringBuilder sb = new StringBuilder();
        StringReader stringReader = new StringReader(str);
        while (true) {
            int i2 = stringReader.read();
            if (i2 > -1) {
                char c2 = (char) i2;
                if (c2 == '\\') {
                    sb.append(c2);
                    char c3 = (char) stringReader.read();
                    char c4 = (char) stringReader.read();
                    if (((c3 >= '0' && c3 <= '9') || ((c3 >= 'A' && c3 <= 'F') || (c3 >= 'a' && c3 <= 'f'))) && ((c4 >= '0' && c4 <= '9') || ((c4 >= 'A' && c4 <= 'F') || (c4 >= 'a' && c4 <= 'f')))) {
                        sb.append((char) Byte.parseByte("" + c3 + c4, 16));
                    } else {
                        sb.append(c3);
                        sb.append(c4);
                    }
                } else {
                    sb.append(c2);
                }
            } else {
                return sb.toString();
            }
        }
    }

    static String changeLess32toXML(String str) throws IOException {
        StringBuilder sb = new StringBuilder();
        StringReader stringReader = new StringReader(str);
        while (true) {
            int i2 = stringReader.read();
            if (i2 > -1) {
                if (i2 < 32) {
                    sb.append('\\');
                    sb.append(Integer.toHexString(i2));
                } else {
                    sb.append((char) i2);
                }
            } else {
                return sb.toString();
            }
        }
    }

    static String changeWStoXML(String str) throws IOException {
        StringBuilder sb = new StringBuilder();
        StringReader stringReader = new StringReader(str);
        while (true) {
            int i2 = stringReader.read();
            if (i2 > -1) {
                char c2 = (char) i2;
                if (c2 == '\\') {
                    char c3 = (char) stringReader.read();
                    if (c3 == ' ') {
                        sb.append('\\');
                        sb.append("20");
                    } else {
                        sb.append('\\');
                        sb.append(c3);
                    }
                } else {
                    sb.append(c2);
                }
            } else {
                return sb.toString();
            }
        }
    }

    static String changeWStoRFC(String str) {
        StringBuilder sb = new StringBuilder();
        int i2 = 0;
        int i3 = 0;
        while (true) {
            int iIndexOf = str.indexOf("\\20", i3);
            if (iIndexOf >= 0) {
                sb.append(trim(str.substring(i2, iIndexOf)) + "\\ ");
                i2 = iIndexOf + 3;
                i3 = iIndexOf + 3;
            } else {
                sb.append(str.substring(i2));
                return sb.toString();
            }
        }
    }

    static String semicolonToComma(String str) {
        return removeWSandReplace(str, ";", ",");
    }

    static String removeWhiteSpace(String str, String str2) {
        return removeWSandReplace(str, str2, str2);
    }

    static String removeWSandReplace(String str, String str2, String str3) {
        StringBuilder sb = new StringBuilder();
        int i2 = 0;
        int iCountQuotes = 0;
        int i3 = 0;
        while (true) {
            int i4 = i3;
            int iIndexOf = str.indexOf(str2, i4);
            if (iIndexOf >= 0) {
                iCountQuotes += countQuotes(str, i4, iIndexOf);
                if (iIndexOf > 0 && str.charAt(iIndexOf - 1) != '\\' && iCountQuotes % 2 == 0) {
                    sb.append(trim(str.substring(i2, iIndexOf)) + str3);
                    i2 = iIndexOf + 1;
                    iCountQuotes = 0;
                }
                i3 = iIndexOf + 1;
            } else {
                sb.append(trim(str.substring(i2)));
                return sb.toString();
            }
        }
    }

    private static int countQuotes(String str, int i2, int i3) {
        int i4 = 0;
        for (int i5 = i2; i5 < i3; i5++) {
            if (str.charAt(i5) == '\"') {
                i4++;
            }
        }
        return i4;
    }

    static String trim(String str) {
        String strTrim = str.trim();
        int iIndexOf = str.indexOf(strTrim) + strTrim.length();
        if (str.length() > iIndexOf && strTrim.endsWith(FXMLLoader.ESCAPE_PREFIX) && !strTrim.endsWith("\\\\") && str.charAt(iIndexOf) == ' ') {
            strTrim = strTrim + " ";
        }
        return strTrim;
    }
}
