package g;

import W.C0184j;
import W.C0188n;
import bH.F;
import bH.W;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;
import javafx.fxml.FXMLLoader;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.slf4j.Marker;
import sun.util.locale.LanguageTag;

/* renamed from: g.e, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:g/e.class */
public class C1727e extends F {
    public static String a(C0188n c0188n, String str, int i2) {
        return a(b(c0188n, str, i2), (String) null);
    }

    public static String b(C0188n c0188n, String str, int i2) {
        while (str.contains("[")) {
            String strSubstring = str.substring(str.indexOf("[") + 1, str.indexOf("]"));
            String strSubstring2 = str.substring(0, str.indexOf("["));
            String strSubstring3 = str.substring(str.indexOf("]") + 1);
            float fA = a((ArrayList) c0188n, strSubstring, i2 + 1);
            str = (!strSubstring.startsWith("Lambda") || fA <= 2.0f) ? strSubstring2 + fA + strSubstring3 : strSubstring2 + 2 + strSubstring3;
        }
        return str;
    }

    public static float a(ArrayList arrayList, String str, int i2) {
        int i3 = 1;
        int iIndexOf = str.indexOf(LanguageTag.SEP);
        if (iIndexOf > 0) {
            String strSubstring = str.substring(iIndexOf + 1);
            str = str.substring(0, iIndexOf);
            i3 = 1 + Integer.parseInt(strSubstring);
        } else if (str.indexOf(Marker.ANY_NON_NULL_MARKER) > 0) {
            int iIndexOf2 = str.indexOf(Marker.ANY_NON_NULL_MARKER);
            String strSubstring2 = str.substring(iIndexOf2 + 1);
            str = str.substring(0, iIndexOf2);
            i3 = 1 - Integer.parseInt(strSubstring2);
        }
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            C0184j c0184j = (C0184j) it.next();
            if (c0184j.a().equals(str)) {
                if (i2 - i3 < 0) {
                    return c0184j.c(0);
                }
                if (i2 - i3 > c0184j.i() - 1) {
                    return c0184j.c(c0184j.i() - 1);
                }
                if (i2 - i3 >= 0) {
                    return c0184j.c(i2 - i3);
                }
            }
        }
        return 0.0f;
    }

    public static double a(String str) {
        int iIndexOf = str.indexOf("?");
        int iIndexOf2 = str.indexOf(CallSiteDescriptor.TOKEN_DELIMITER);
        if (iIndexOf > 0 && iIndexOf2 > 0) {
            return c(str.substring(0, iIndexOf)) ? a(str.substring(iIndexOf + 1, iIndexOf2)) : a(str.substring(iIndexOf2 + 1));
        }
        String string = str;
        if (string.indexOf("Math.") != -1) {
            string = n(string);
        }
        while (string.indexOf("(") != -1) {
            string = new StringBuffer(string.substring(0, string.lastIndexOf("(", string.indexOf(")")))).append(m(string.substring(string.lastIndexOf("(", string.indexOf(")")) + 1, string.indexOf(")")))).append(string.substring(string.indexOf(")") + 1)).toString();
        }
        return m(string);
    }

    private static double m(String str) throws V.h {
        ArrayList arrayList = new ArrayList();
        double dE = 0.0d;
        while (true) {
            if (str.indexOf(43) <= -1 && str.indexOf(42) <= -1 && str.indexOf(45) <= -1 && str.indexOf(47) <= -1 && str.indexOf(38) <= -1 && str.indexOf(124) <= -1 && str.indexOf(94) <= -1) {
                arrayList.add(str);
                String str2 = null;
                double dE2 = 0.0d;
                for (int i2 = 0; i2 < arrayList.size(); i2++) {
                    if (i2 == 0) {
                        try {
                            if (arrayList.get(i2) != null && !((String) arrayList.get(i2)).trim().equals("")) {
                                dE = e((String) arrayList.get(i2));
                            }
                        } catch (Exception e2) {
                            e2.printStackTrace();
                            throw new V.h("Invalid Formula value " + arrayList.get(i2) + ", Generated Exception " + e2.getMessage());
                        }
                    } else if (i2 % 2 != 0) {
                        str2 = (String) arrayList.get(i2);
                    } else {
                        try {
                            dE2 = e((String) arrayList.get(i2));
                        } catch (Exception e3) {
                        }
                        if (str2 != null && str2.equals(Marker.ANY_NON_NULL_MARKER)) {
                            dE += dE2;
                        } else if (str2 != null && str2.equals("*")) {
                            dE *= dE2;
                        } else if (str2 != null && str2.equals("/")) {
                            dE /= dE2;
                        } else if (str2 != null && str2.equals(LanguageTag.SEP)) {
                            dE -= dE2;
                        } else if (str2 != null && str2.equals("^")) {
                            dE = Math.pow(dE, dE2);
                        } else if (str2 != null && str2.equals(FXMLLoader.RESOURCE_KEY_PREFIX)) {
                            dE %= dE2;
                        } else if (str2 != null && str2.equals("&")) {
                            dE = ((int) dE) & ((int) dE2);
                        } else if (str2 != null && str2.equals(CallSiteDescriptor.OPERATOR_DELIMITER)) {
                            dE = ((int) dE) | ((int) dE2);
                        }
                    }
                }
                return dE;
            }
            int iIndexOf = str.indexOf(Marker.ANY_NON_NULL_MARKER);
            int iIndexOf2 = str.indexOf(LanguageTag.SEP);
            int iIndexOf3 = str.indexOf("/");
            int iIndexOf4 = str.indexOf("*");
            int iIndexOf5 = str.indexOf("^");
            int iIndexOf6 = str.indexOf(FXMLLoader.RESOURCE_KEY_PREFIX);
            int iIndexOf7 = str.indexOf("&");
            int iIndexOf8 = str.indexOf(CallSiteDescriptor.OPERATOR_DELIMITER);
            int length = str.length() - 1;
            int i3 = (iIndexOf == -1 || iIndexOf >= length) ? length : iIndexOf;
            int i4 = (iIndexOf2 == -1 || iIndexOf2 >= i3) ? i3 : iIndexOf2;
            int i5 = (iIndexOf3 == -1 || iIndexOf3 >= i4) ? i4 : iIndexOf3;
            int i6 = (iIndexOf4 == -1 || iIndexOf4 >= i5) ? i5 : iIndexOf4;
            int i7 = (iIndexOf5 == -1 || iIndexOf5 >= i6) ? i6 : iIndexOf5;
            int i8 = (iIndexOf7 == -1 || iIndexOf7 >= i7) ? i7 : iIndexOf7;
            int i9 = (iIndexOf8 == -1 || iIndexOf8 >= i8) ? i8 : iIndexOf8;
            int i10 = (iIndexOf6 == -1 || iIndexOf6 >= i9) ? i9 : iIndexOf6;
            if (iIndexOf2 == 0) {
                str = "0" + str;
                i10 = 1;
            }
            if (i10 == -1) {
                throw new V.h("Invalid formula," + str + " \noperator not found in section " + str);
            }
            if (iIndexOf == 0 || iIndexOf4 == 0 || iIndexOf3 == 0 || iIndexOf5 == 0 || iIndexOf6 == 0 || iIndexOf7 == 0 || iIndexOf8 == 0) {
                break;
            }
            arrayList.add(str.substring(0, i10));
            arrayList.add(str.substring(i10, i10 + 1));
            str = str.substring(i10 + 1);
        }
        throw new V.h("Formula error, 2 operators without value.\n" + str);
    }

    public static boolean b(String str) throws V.h {
        if (str.indexOf("true") != -1 || str.indexOf("false") != -1) {
            str = W.b(W.b(str, "true", "1"), "false", "0");
        }
        int iIndexOf = str.indexOf("==");
        int iIndexOf2 = iIndexOf > 0 ? iIndexOf : str.indexOf("!=");
        int iIndexOf3 = iIndexOf2 > 0 ? iIndexOf2 : str.indexOf("<=");
        int iIndexOf4 = iIndexOf3 > 0 ? iIndexOf3 : str.indexOf(">=");
        if (iIndexOf4 > 0) {
            try {
                double dA = a(str.substring(0, iIndexOf4).trim());
                double dA2 = a(str.substring(iIndexOf4 + 2).trim());
                return str.indexOf("==") != -1 ? dA == dA2 : str.indexOf("!=") != -1 ? dA != dA2 : str.indexOf("<=") != -1 ? dA <= dA2 : str.indexOf(">=") != -1 && dA >= dA2;
            } catch (Exception e2) {
                throw new V.h("Unable to resolve condition for expression: " + str);
            }
        }
        int iIndexOf5 = str.indexOf("&");
        int iIndexOf6 = iIndexOf5 > 0 ? iIndexOf5 : str.indexOf(">");
        int iIndexOf7 = iIndexOf6 > 0 ? iIndexOf6 : str.indexOf("<");
        int iIndexOf8 = iIndexOf7 > 0 ? iIndexOf7 : str.indexOf("=");
        if (iIndexOf8 > 0) {
            double dA3 = a(str.substring(0, iIndexOf8).trim());
            double dA4 = a(str.substring(iIndexOf8 + 1).trim());
            if (str.indexOf("&") <= 0) {
                return str.indexOf("=") > 0 ? dA3 == dA4 : str.indexOf("<") > 0 ? dA3 < dA4 : str.indexOf(">") > 0 && dA3 > dA4;
            }
            int i2 = (int) (dA3 <= dA4 ? dA3 : dA4);
            return i2 != 0 && (((int) dA3) & ((int) dA4)) == i2;
        }
        if (d(str)) {
            return a(str) != 0.0d;
        }
        try {
            boolean z2 = str.indexOf("!") != -1;
            double dE = e(W.b(str.substring(0, str.length()).trim(), "!", ""));
            return z2 ? dE <= 0.0d : dE > 0.0d;
        } catch (Exception e3) {
            throw new V.h("Error evaluating condition:" + str + ", false returned");
        }
    }

    public static boolean c(String str) {
        String string = str;
        if (string.indexOf("Math.") != -1) {
            string = n(string);
        }
        while (string.indexOf("(") != -1) {
            String strSubstring = string.substring(string.indexOf(")") + 1);
            String strSubstring2 = string.substring(string.lastIndexOf("(", string.indexOf(")")) + 1, string.indexOf(")"));
            string = new StringBuffer(string.substring(0, string.lastIndexOf("(", string.indexOf(")")))).append(c(strSubstring2) + "").append(strSubstring).toString();
        }
        String str2 = string;
        if (str2.indexOf("&&") > 0) {
            StringTokenizer stringTokenizer = new StringTokenizer(str2, "&&");
            while (stringTokenizer.hasMoreTokens()) {
                if (!c(stringTokenizer.nextToken())) {
                    return false;
                }
            }
            return true;
        }
        if (str2.indexOf("||") <= 0) {
            return b(str2);
        }
        StringTokenizer stringTokenizer2 = new StringTokenizer(str2, "||");
        while (stringTokenizer2.hasMoreTokens()) {
            if (c(stringTokenizer2.nextToken())) {
                return true;
            }
        }
        return false;
    }

    public static boolean d(String str) {
        for (int i2 = 0; i2 < "+&|-*/".length(); i2++) {
            if (str.indexOf("+&|-*/".charAt(i2)) > 1) {
                return true;
            }
        }
        return false;
    }

    public static double e(String str) {
        if (str.indexOf("0x") != -1) {
            str = Integer.valueOf(W.b(str, "0x", ""), 16).toString();
        } else if (str.indexOf("0b") != -1) {
            str = Integer.valueOf(W.b(str, "0b", ""), 2).toString();
        }
        return Double.parseDouble(str);
    }

    public static String a(String str, String str2) {
        String strSubstring = "" + a(str);
        int iIndexOf = strSubstring.indexOf(46);
        if (iIndexOf != -1 && strSubstring.length() - iIndexOf > 3) {
            strSubstring = strSubstring.substring(0, iIndexOf + 4);
        }
        return strSubstring;
    }

    private static String n(String str) {
        while (str.indexOf("Math.") != -1) {
            String strSubstring = str.substring(0, str.indexOf("Math."));
            int iIndexOf = str.indexOf("Math.");
            int iA = a(str, iIndexOf);
            str = strSubstring + o(str.substring(iIndexOf, iA)) + str.substring(iA);
        }
        return str;
    }

    public static boolean f(String str) {
        try {
            e(str);
            return true;
        } catch (Exception e2) {
            return false;
        }
    }

    private static double o(String str) throws V.h {
        if (str.indexOf("(") == -1) {
            int length = str.length() - 1;
            while (true) {
                if (length <= 0) {
                    break;
                }
                if (!f(str.charAt(length) + "")) {
                    str = str.substring(0, length + 1) + "(" + str.substring(length + 1) + ")";
                    break;
                }
                length--;
            }
        }
        String strSubstring = str.substring(str.indexOf(46) + 1, str.indexOf(40));
        double dA = a(str.substring(str.indexOf(40), str.length()));
        if (strSubstring.equals("log")) {
            if (dA < 0.0d) {
                return 0.0d;
            }
            return Math.log(dA);
        }
        if (strSubstring.equals("sin")) {
            return Math.sin(dA);
        }
        if (strSubstring.equals("cos")) {
            return Math.cos(dA);
        }
        if (strSubstring.equals("tan")) {
            return Math.tan(dA);
        }
        if (strSubstring.equals("atan")) {
            return Math.atan(dA);
        }
        if (!strSubstring.equals("asin") && !strSubstring.equals("acos")) {
            if (strSubstring.equals("abs")) {
                return Math.abs(dA);
            }
            if (strSubstring.equals("sqrt")) {
                return Math.sqrt(dA);
            }
            throw new V.h("Unsupported Math function " + strSubstring);
        }
        return Math.asin(dA);
    }

    private static int a(String str, int i2) {
        int i3 = 0;
        int i4 = 0;
        int i5 = i2;
        while (i5 < str.length() && (i3 == 0 || i3 != i4)) {
            if (str.charAt(i5) == '(') {
                i3++;
            }
            if (str.charAt(i5) == ')') {
                i4++;
            }
            i5++;
        }
        return i5;
    }
}
