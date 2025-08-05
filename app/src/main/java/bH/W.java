package bH;

import com.sun.org.apache.xml.internal.serializer.SerializerConstants;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;
import javafx.fxml.FXMLLoader;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.apache.commons.math3.geometry.VectorFormat;
import org.icepdf.core.pobjects.annotations.FreeTextAnnotation;
import org.icepdf.core.util.PdfOps;
import org.icepdf.ri.common.utility.annotation.GoToActionDialog;
import org.slf4j.Marker;
import sun.security.pkcs11.wrapper.Constants;
import sun.util.locale.LanguageTag;

/* loaded from: TunerStudioMS.jar:bH/W.class */
public class W {

    /* renamed from: a, reason: collision with root package name */
    private static String f7029a = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static boolean a(String str, String str2) {
        if ((str == null) ^ (str2 == null)) {
            return false;
        }
        if (str == null) {
            return true;
        }
        return str.equals(str2);
    }

    public static String a(String str) {
        String str2 = "";
        char[] charArray = str.toCharArray();
        for (int i2 = 0; i2 < charArray.length; i2++) {
            if (f7029a.indexOf(charArray[i2]) != -1) {
                str2 = str2 + charArray[i2];
            }
        }
        return str2;
    }

    public static int b(String str, String str2) {
        int iIndexOf = str.indexOf(str2);
        int length = iIndexOf + str2.length();
        int i2 = 0;
        while (iIndexOf >= 0) {
            i2++;
            if (length > str.length()) {
                break;
            }
            iIndexOf = str.indexOf(str2, length);
            length = iIndexOf + str2.length();
        }
        return i2;
    }

    public static int a(String str, char c2) {
        char[] charArray = str.toCharArray();
        boolean z2 = false;
        for (int i2 = 0; i2 < charArray.length; i2++) {
            if (charArray[i2] == '\"') {
                z2 = !z2;
            }
            if (!z2 && charArray[i2] == c2) {
                return i2;
            }
        }
        return -1;
    }

    public static String b(String str) {
        return b(b(b(b(str, SerializerConstants.ENTITY_GT, ">"), SerializerConstants.ENTITY_LT, "<"), SerializerConstants.ENTITY_QUOT, PdfOps.DOUBLE_QUOTE__TOKEN), SerializerConstants.ENTITY_AMP, "&");
    }

    public static boolean c(String str) {
        return str == null || str.trim().isEmpty();
    }

    public static String d(String str) {
        if (str.contains("<br>")) {
            str = b(str, "\n", "");
        }
        if (str.contains(Constants.INDENT)) {
            str = b(str, Constants.INDENT, "&nbsp;");
        }
        if (str.contains("<body>\n")) {
            str = str.substring(str.indexOf("<body>") + 7);
            if (str.contains(FreeTextAnnotation.BODY_END)) {
                str = str.substring(0, str.indexOf(FreeTextAnnotation.BODY_END));
            }
        } else if (str.contains("<body>")) {
            str = str.substring(str.indexOf("<body>") + 6);
            if (str.contains(FreeTextAnnotation.BODY_END)) {
                str = str.substring(0, str.indexOf(FreeTextAnnotation.BODY_END));
            }
        } else if (str.contains("</head>")) {
            str = str.substring(str.indexOf("</head>") + 7);
        }
        if (str.endsWith("\n")) {
            str = str.substring(0, str.length() - 1);
        }
        return b(str, "<br>", "\n").trim();
    }

    public static Object[] a(Object[] objArr) {
        for (int i2 = 0; i2 < objArr.length; i2++) {
            for (int i3 = i2 + 1; i3 < objArr.length; i3++) {
                String str = (String) objArr[i2];
                String str2 = (String) objArr[i3];
                if (str.toLowerCase().compareTo(str2.toLowerCase()) > 0) {
                    objArr[i2] = str2;
                    objArr[i3] = str;
                }
            }
        }
        return objArr;
    }

    public static String[] a(String[] strArr) {
        for (int i2 = 0; i2 < strArr.length; i2++) {
            for (int i3 = i2 + 1; i3 < strArr.length; i3++) {
                String str = strArr[i2];
                String str2 = strArr[i3];
                if (str.toLowerCase().compareTo(str2.toLowerCase()) > 0) {
                    strArr[i2] = str2;
                    strArr[i3] = str;
                }
            }
        }
        return strArr;
    }

    public static StringBuilder a(StringBuilder sb, String str, String str2) {
        int iIndexOf = sb.indexOf(str);
        while (true) {
            int i2 = iIndexOf;
            if (i2 < 0) {
                return sb;
            }
            sb = sb.replace(i2, i2 + str.length(), str2);
            iIndexOf = sb.indexOf(str, i2 + str2.length());
        }
    }

    public static String a(String str, String str2, String str3) {
        int iIndexOf = str.toLowerCase().indexOf(str2.toLowerCase());
        while (true) {
            int i2 = iIndexOf;
            if (i2 < 0) {
                return str;
            }
            str = str.substring(0, i2) + str3 + str.substring(i2 + str2.length());
            iIndexOf = str.toLowerCase().indexOf(str2.toLowerCase(), i2 + str3.length());
        }
    }

    public static String b(String str, String str2, String str3) {
        int iIndexOf = str.indexOf(str2);
        while (true) {
            int i2 = iIndexOf;
            if (i2 < 0) {
                return str;
            }
            StringBuilder sb = new StringBuilder();
            sb.append(str.substring(0, i2));
            if (!str3.isEmpty()) {
                sb.append(str3);
            }
            sb.append(str.substring(i2 + str2.length()));
            str = sb.toString();
            iIndexOf = str.indexOf(str2, i2 + str3.length());
        }
    }

    public static String c(String str, String str2, String str3) {
        int iIndexOf = str.indexOf(str2);
        return iIndexOf == -1 ? str : str.substring(0, iIndexOf) + str3 + str.substring(iIndexOf + str2.length());
    }

    public static String e(String str) {
        if (str != null) {
            str = b(b(b(b(b(b(b(b(b(b(b(b(b(str, "/", ""), LanguageTag.SEP, "_"), Marker.ANY_NON_NULL_MARKER, "_"), "*", LanguageTag.PRIVATEUSE), FXMLLoader.RESOURCE_KEY_PREFIX, ""), "^", ""), CallSiteDescriptor.OPERATOR_DELIMITER, ""), "(", "_"), ")", "_"), VectorFormat.DEFAULT_PREFIX, ""), "}", ""), FXMLLoader.EXPRESSION_PREFIX, ""), "&", "");
        }
        return str;
    }

    public static boolean f(String str) {
        if (str != null) {
            return str.contains("/") || str.contains(LanguageTag.SEP) || str.contains(Marker.ANY_NON_NULL_MARKER) || str.contains("*") || str.contains(FXMLLoader.RESOURCE_KEY_PREFIX) || str.contains("^") || str.contains(CallSiteDescriptor.OPERATOR_DELIMITER) || str.contains("(") || str.contains(")") || str.contains(VectorFormat.DEFAULT_PREFIX) || str.contains("}") || str.contains(FXMLLoader.EXPRESSION_PREFIX) || str.contains("&");
        }
        return false;
    }

    public static String[] c(String str, String str2) {
        StringTokenizer stringTokenizer = new StringTokenizer(str + str2, str2);
        ArrayList arrayList = new ArrayList();
        while (stringTokenizer.hasMoreTokens()) {
            String strNextToken = stringTokenizer.nextToken();
            if (strNextToken.trim().startsWith(PdfOps.DOUBLE_QUOTE__TOKEN)) {
                while (strNextToken.length() > 1 && strNextToken.trim().indexOf(34, 1) == -1 && stringTokenizer.hasMoreTokens()) {
                    strNextToken = strNextToken + str2 + stringTokenizer.nextToken();
                }
            }
            if (strNextToken.trim().indexOf("table(") != -1) {
                while (strNextToken.length() > 1 && strNextToken.trim().indexOf(41, 1) == -1 && stringTokenizer.hasMoreTokens()) {
                    strNextToken = strNextToken + str2 + stringTokenizer.nextToken();
                }
            }
            arrayList.add(strNextToken.trim());
        }
        String[] strArr = new String[arrayList.size()];
        for (int i2 = 0; i2 < strArr.length; i2++) {
            strArr[i2] = (String) arrayList.get(i2);
        }
        return strArr;
    }

    public static int[] d(String str, String str2) {
        int[] iArr = new int[new StringTokenizer(str, str2).countTokens()];
        int i2 = 0;
        for (int i3 = 0; i3 < iArr.length; i3++) {
            int iIndexOf = str.indexOf(str2, i2);
            if (iIndexOf == -1) {
                iIndexOf = str.length();
            }
            iArr[i3] = g(str.substring(i2, iIndexOf).trim());
            i2 = iIndexOf + 1;
        }
        return iArr;
    }

    public static int g(String str) {
        int i2;
        String strTrim = str.trim();
        if (strTrim.startsWith("0x")) {
            strTrim = strTrim.substring(2);
            i2 = 16;
        } else if (strTrim.startsWith(LanguageTag.PRIVATEUSE)) {
            strTrim = strTrim.substring(1);
            i2 = 16;
        } else {
            i2 = 10;
        }
        return Integer.parseInt(strTrim, i2);
    }

    public static double[][] a(double[][] dArr, String str) {
        return a(dArr, str, false);
    }

    public static String h(String str) {
        StringBuilder sb = new StringBuilder();
        for (int i2 = 0; i2 < str.length(); i2++) {
            if (str.charAt(i2) > '~' || str.charAt(i2) < ' ') {
                sb.append('?');
            } else {
                sb.append(str.charAt(i2));
            }
        }
        return sb.toString();
    }

    public static double[][] a(double[][] dArr, String str, boolean z2) {
        if (str.indexOf("\n") == -1) {
            dArr[0][0] = Double.parseDouble(str);
            return dArr;
        }
        StringTokenizer stringTokenizer = new StringTokenizer(str, "\n");
        for (int i2 = 0; i2 < dArr.length; i2++) {
            StringTokenizer stringTokenizer2 = new StringTokenizer(stringTokenizer.nextToken().replace(' ', '|'), CallSiteDescriptor.OPERATOR_DELIMITER);
            int length = (dArr.length - 1) - i2;
            for (int i3 = 0; i3 < dArr[i2].length && stringTokenizer2.hasMoreTokens(); i3++) {
                String strNextToken = stringTokenizer2.nextToken();
                if (z2) {
                    dArr[length][i3] = Double.parseDouble(strNextToken);
                } else {
                    dArr[i2][i3] = Double.parseDouble(strNextToken);
                }
            }
        }
        return dArr;
    }

    public static String a(String[][] strArr) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        for (int i2 = 0; i2 < strArr.length; i2++) {
            sb.append("         ");
            for (int i3 = 0; i3 < strArr[i2].length; i3++) {
                sb.append(strArr[i2][i3]).append(" ");
            }
            sb.append("\n");
        }
        sb.append(GoToActionDialog.EMPTY_DESTINATION);
        return sb.toString();
    }

    public static String a(double d2, int i2) {
        int i3 = 0;
        while (Math.pow(10.0d, i2 - i3) > d2 && i3 != i2) {
            i3++;
        }
        return b(d2, i3);
    }

    public static String a(double d2) {
        return b(d2, 1);
    }

    public static String b(double d2, int i2) {
        DecimalFormat decimalFormat = (DecimalFormat) DecimalFormat.getInstance(Locale.US);
        decimalFormat.setGroupingUsed(false);
        if (i2 < 0) {
            i2 = d2 < 10000.0d ? 3 - ((int) Math.log10(Math.abs(d2))) : 0;
        } else {
            d2 = Math.round(d2 * r0) / Math.pow(10.0d, i2);
        }
        decimalFormat.setMaximumFractionDigits(i2);
        decimalFormat.setMinimumFractionDigits(i2);
        return decimalFormat.format(d2);
    }

    public static String a(String str, int i2) {
        if (i2 <= 0) {
            return str.indexOf(".") == -1 ? str : str.substring(0, str.indexOf("."));
        }
        if (str.indexOf(".") == -1 && i2 > 0) {
            str = str + ".0";
        }
        if (str.length() - str.indexOf(".") > i2) {
            str = str.substring(0, str.indexOf(".") + i2 + 1);
        } else {
            while (str.length() - str.indexOf(".") < i2 + 1) {
                str = str + "0";
            }
        }
        return str;
    }

    public static String b(double d2) {
        return c(d2, 1);
    }

    public static String c(double d2, int i2) {
        double dRound = Math.round(d2 * r0) / Math.pow(10.0d, i2);
        String string = Double.toString(dRound);
        return string.contains("E") ? b(dRound, i2) : a(string, i2);
    }

    public static String a(float f2, int i2) {
        return b(Float.toString((float) (Math.round(f2 * ((float) r0)) / Math.pow(10.0d, i2))), i2);
    }

    public static String b(String str, int i2) {
        if (i2 <= 0) {
            return str.indexOf(".") == -1 ? str : str.substring(0, str.indexOf("."));
        }
        if (str.indexOf(".") == -1 && i2 > 0) {
            str = str + ".0";
        }
        if (str.length() - str.indexOf(".") > i2) {
            str = str.substring(0, str.indexOf(".") + i2 + 1);
        } else {
            while (str.length() - str.indexOf(".") < i2 + 1) {
                str = str + "0";
            }
        }
        return str;
    }

    public static String a(String str, char c2, int i2) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        while (sb.length() < i2) {
            sb.insert(0, c2);
        }
        return sb.toString();
    }

    public static String b(String str, char c2, int i2) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        while (sb.length() < i2) {
            sb.append(c2);
        }
        return sb.toString();
    }

    public static String a() {
        return a(new Date());
    }

    public static String a(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(1) + LanguageTag.SEP + a((calendar.get(2) + 1) + "", '0', 2) + LanguageTag.SEP + a(calendar.get(5) + "", '0', 2) + "_" + a(calendar.get(11) + "", '0', 2) + "." + a(calendar.get(12) + "", '0', 2) + "." + a(calendar.get(13) + "", '0', 2);
    }

    public static String a(long j2) {
        String str;
        double d2 = j2;
        DecimalFormat decimalFormat = (DecimalFormat) DecimalFormat.getInstance(Locale.US);
        decimalFormat.setGroupingUsed(true);
        if (d2 < 1024.0d) {
            decimalFormat.setMaximumFractionDigits(0);
            decimalFormat.setMinimumFractionDigits(0);
            str = decimalFormat.format(d2) + " bytes";
        } else if (d2 < 1048576.0d) {
            decimalFormat.setMaximumFractionDigits(2);
            decimalFormat.setMinimumFractionDigits(1);
            str = decimalFormat.format(d2 / 1024.0d) + " KB";
        } else if (d2 < 1.073741824E9d) {
            decimalFormat.setMaximumFractionDigits(3);
            decimalFormat.setMinimumFractionDigits(2);
            str = decimalFormat.format(d2 / 1048576.0d) + " MB";
        } else {
            decimalFormat.setMaximumFractionDigits(3);
            decimalFormat.setMinimumFractionDigits(2);
            str = decimalFormat.format(d2 / 1.073741824E9d) + " GB";
        }
        return str;
    }

    public static int e(String str, String str2) {
        int i2 = 0;
        int i3 = 0;
        while (true) {
            int iIndexOf = str.indexOf(str2, i3);
            if (iIndexOf == -1) {
                return i2;
            }
            i2++;
            i3 = iIndexOf + 1;
        }
    }

    public static String a(String str, int i2, String str2) {
        int i3 = 0;
        int i4 = 0;
        StringBuilder sb = new StringBuilder();
        for (int i5 = 0; i5 < str.length(); i5++) {
            if (i3 <= i2 || str.charAt(i5) != ' ') {
                i3 = str.charAt(i5) == '\n' ? 0 : i3 + 1;
            } else {
                sb.append(str.subSequence(i4, i5)).append(str2);
                i4 = i5;
                i3 = 0;
            }
        }
        sb.append(str.subSequence(i4, str.length()));
        return sb.toString();
    }

    public static StringBuilder a(StringBuilder sb, int i2, String str) {
        int i3 = 0;
        int length = 0;
        while (length < sb.length()) {
            if (i3 <= i2 || sb.charAt(length) != ' ') {
                i3 = sb.charAt(length) == '\n' ? 0 : i3 + 1;
            } else {
                sb.replace(length, length + 1, str);
                length += str.length();
                i3 = 0;
            }
            length++;
        }
        return sb;
    }

    public static String i(String str) {
        if (str.length() > 0) {
            if (str.charAt(0) == '\"') {
                str = str.substring(1);
            }
            if (str.endsWith(PdfOps.DOUBLE_QUOTE__TOKEN)) {
                str = str.substring(0, str.length() - 1);
            }
        }
        return str;
    }

    public static String[] j(String str) {
        boolean z2 = false;
        int i2 = 0;
        ArrayList arrayList = new ArrayList();
        int i3 = 0;
        while (i3 < str.length()) {
            if (str.charAt(i3) == '\"' || str.charAt(i3) == '{' || str.charAt(i3) == '}' || str.charAt(i3) == '[' || str.charAt(i3) == ']') {
                z2 = !z2;
            } else if (!z2 && str.charAt(i3) == '=') {
                arrayList.add(str.substring(i2, i3));
                while (true) {
                    if (i3 >= str.length()) {
                        break;
                    }
                    if (str.charAt(i3) != '=') {
                        i3--;
                        break;
                    }
                    i3++;
                }
                i2 = i3 + 1;
            }
            i3++;
        }
        if (i2 < str.length()) {
            arrayList.add(str.substring(i2, str.length()));
        }
        return (String[]) arrayList.toArray(new String[arrayList.size()]);
    }

    public static String k(String str) {
        int i2 = 0;
        while (true) {
            if (i2 >= str.length()) {
                break;
            }
            if (0 == ((byte) str.charAt(i2))) {
                str = str.substring(0, i2);
                break;
            }
            i2++;
        }
        return str;
    }

    public static String a(byte[] bArr, int i2, int i3) {
        for (int i4 = 0; i4 < i3; i4++) {
            if (0 == bArr[i4 + i2] || i4 == i3 - 1) {
                byte[] bArr2 = new byte[i4];
                System.arraycopy(bArr, i2, bArr2, 0, bArr2.length);
                try {
                    return new String(bArr2, "UTF-8");
                } catch (UnsupportedEncodingException e2) {
                    C.c("getNullTerminated String Encoder Error, returning non UTF-8 version");
                    return new String(bArr2);
                }
            }
        }
        return "";
    }

    public static String a(byte[] bArr) {
        for (int i2 = 0; i2 < bArr.length + 1; i2++) {
            if (i2 == bArr.length || 0 == bArr[i2]) {
                byte[] bArr2 = new byte[i2];
                System.arraycopy(bArr, 0, bArr2, 0, bArr2.length);
                try {
                    return new String(bArr2, "UTF-8");
                } catch (UnsupportedEncodingException e2) {
                    C.c("getNullTerminated String Encoder Error, returning non UTF-8 version");
                    return new String(bArr2);
                }
            }
        }
        return "";
    }

    public static boolean a(String[] strArr, String str) {
        for (String str2 : strArr) {
            if (str2.equalsIgnoreCase(str)) {
                return true;
            }
        }
        return false;
    }
}
