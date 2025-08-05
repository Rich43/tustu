package bH;

import java.util.ArrayList;
import java.util.StringTokenizer;
import javafx.fxml.FXMLLoader;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.slf4j.Marker;
import sun.util.locale.LanguageTag;

/* loaded from: TunerStudioMS.jar:bH/F.class */
public class F {
    public static double g(String str) {
        if (str.indexOf("Math.") != -1) {
            str = b(str);
        }
        int iIndexOf = str.indexOf("||");
        int iIndexOf2 = str.indexOf("&&");
        if (iIndexOf != -1 || iIndexOf2 != -1) {
            str = d(str);
        }
        while (str.indexOf("(") != -1) {
            str = new StringBuffer(str.substring(0, str.lastIndexOf("(", str.indexOf(")")))).append(g(str.substring(str.lastIndexOf("(", str.indexOf(")")) + 1, str.indexOf(")")))).append(str.substring(str.indexOf(")") + 1)).toString();
        }
        int iIndexOf3 = str.indexOf("?");
        int iIndexOf4 = str.indexOf(CallSiteDescriptor.TOKEN_DELIMITER);
        if (iIndexOf3 <= 0 || iIndexOf4 <= 0) {
            return a(str);
        }
        return i(str.substring(0, iIndexOf3)) ? g(str.substring(iIndexOf3 + 1, iIndexOf4)) : g(str.substring(iIndexOf4 + 1));
    }

    private static double a(String str) throws V.h {
        String strB = W.b(W.b(W.b(W.b(W.b(W.b(str, "!=", FXMLLoader.EXPRESSION_PREFIX), "==", "="), "||", CallSiteDescriptor.OPERATOR_DELIMITER), "&&", "&"), ">=", FXMLLoader.CONTROLLER_METHOD_PREFIX), "<=", "@");
        ArrayList arrayList = new ArrayList();
        double dK = 0.0d;
        while (true) {
            if (strB.indexOf(43) <= -1 && strB.indexOf(42) <= -1 && strB.indexOf(45) <= -1 && strB.indexOf(47) <= -1 && strB.indexOf(38) <= -1 && strB.indexOf(124) <= -1 && strB.indexOf(94) <= -1 && strB.indexOf(37) <= -1 && strB.indexOf(60) <= -1 && strB.indexOf(62) <= -1 && strB.indexOf("=") <= -1 && strB.indexOf(FXMLLoader.EXPRESSION_PREFIX) <= -1 && strB.indexOf("@") <= -1 && strB.indexOf(FXMLLoader.CONTROLLER_METHOD_PREFIX) <= -1 && strB.indexOf("~") <= -1 && strB.indexOf("`") <= -1 && strB.indexOf("!") <= -1) {
                arrayList.add(strB);
                String str2 = null;
                double dK2 = 0.0d;
                int i2 = 0;
                while (i2 < arrayList.size()) {
                    if (i2 == 0) {
                        try {
                            if (arrayList.get(i2) != null && !((String) arrayList.get(i2)).trim().equals("")) {
                                dK = k((String) arrayList.get(i2));
                            }
                        } catch (Exception e2) {
                            e2.printStackTrace();
                            throw new V.h("Invalid Formula value " + arrayList.get(i2) + ", Generated Exception " + e2.getMessage() + ", Original formula:" + str);
                        }
                    } else if (i2 % 2 != 0) {
                        str2 = (String) arrayList.get(i2);
                    } else {
                        try {
                            dK2 = k((String) arrayList.get(i2));
                        } catch (Exception e3) {
                            try {
                                String str3 = (String) arrayList.get(i2);
                                if (str3.endsWith("E") && arrayList.size() > 3) {
                                    String strSubstring = str3.substring(0, str3.length() - 1);
                                    int i3 = i2 + 1;
                                    String str4 = (String) arrayList.get(i3);
                                    i2 = i3 + 1;
                                    double d2 = Double.parseDouble((String) arrayList.get(i2));
                                    if (str4.equals(LanguageTag.SEP)) {
                                        d2 *= -1.0d;
                                    }
                                    dK2 = Double.parseDouble(strSubstring) * Math.pow(10.0d, d2);
                                }
                            } catch (Exception e4) {
                                String str5 = "Failed to get value for:" + arrayList.get(i2) + ", in formula:" + strB;
                                C.b(str5);
                                throw new V.h(str5);
                            }
                        }
                        if (str2 != null && str2.equals(Marker.ANY_NON_NULL_MARKER)) {
                            dK += dK2;
                        } else if (str2 != null && str2.equals("*")) {
                            dK *= dK2;
                        } else if (str2 != null && str2.equals("/")) {
                            dK /= dK2;
                        } else if (str2 != null && str2.equals(LanguageTag.SEP)) {
                            dK -= dK2;
                        } else if (str2 != null && str2.equals("^")) {
                            dK = Math.pow(dK, dK2);
                        } else if (str2 != null && str2.equals(FXMLLoader.RESOURCE_KEY_PREFIX)) {
                            dK %= dK2;
                        } else if (str2 != null && str2.equals("&")) {
                            dK = ((int) dK) & ((int) dK2);
                        } else if (str2 != null && str2.equals(CallSiteDescriptor.OPERATOR_DELIMITER)) {
                            dK = ((int) dK) | ((int) dK2);
                        } else if (str2 != null && str2.equals("~")) {
                            dK = (dK == 0.0d || dK2 == 0.0d) ? 0.0d : 1.0d;
                        } else if (str2 != null && str2.equals("`")) {
                            dK = (dK == 0.0d && dK2 == 0.0d) ? 0.0d : 1.0d;
                        } else if (str2 != null && str2.equals(">")) {
                            dK = dK > dK2 ? 1.0d : 0.0d;
                        } else if (str2 != null && str2.equals("<")) {
                            dK = dK < dK2 ? 1.0d : 0.0d;
                        } else if (str2 != null && str2.equals("=")) {
                            dK = dK == dK2 ? 1.0d : 0.0d;
                        } else if (str2 != null && str2.equals(FXMLLoader.EXPRESSION_PREFIX)) {
                            dK = dK != dK2 ? 1.0d : 0.0d;
                        } else if (str2 != null && str2.equals(FXMLLoader.CONTROLLER_METHOD_PREFIX)) {
                            dK = dK >= dK2 ? 1.0d : 0.0d;
                        } else if (str2 != null && str2.equals("@")) {
                            dK = dK <= dK2 ? 1.0d : 0.0d;
                        } else if (str2 != null && str2.equals("!")) {
                            dK = dK2 == 0.0d ? 1.0d : 0.0d;
                        }
                    }
                    i2++;
                }
                return dK;
            }
            int iIndexOf = strB.indexOf(Marker.ANY_NON_NULL_MARKER);
            int iIndexOf2 = strB.indexOf(LanguageTag.SEP);
            int iIndexOf3 = strB.indexOf("/");
            int iIndexOf4 = strB.indexOf("*");
            int iIndexOf5 = strB.indexOf("^");
            int iIndexOf6 = strB.indexOf(FXMLLoader.RESOURCE_KEY_PREFIX);
            int iIndexOf7 = strB.indexOf("&");
            int iIndexOf8 = strB.indexOf(CallSiteDescriptor.OPERATOR_DELIMITER);
            int iIndexOf9 = strB.indexOf(">");
            int iIndexOf10 = strB.indexOf("<");
            int iIndexOf11 = strB.indexOf("=");
            int iIndexOf12 = strB.indexOf(FXMLLoader.EXPRESSION_PREFIX);
            int iIndexOf13 = strB.indexOf(FXMLLoader.CONTROLLER_METHOD_PREFIX);
            int iIndexOf14 = strB.indexOf("@");
            int iIndexOf15 = strB.indexOf("!");
            int iIndexOf16 = strB.indexOf("~");
            int iIndexOf17 = strB.indexOf("`");
            int length = strB.length();
            int i4 = (iIndexOf == -1 || iIndexOf >= length) ? length : iIndexOf;
            int i5 = (iIndexOf2 == -1 || iIndexOf2 >= i4 || iIndexOf2 <= 0) ? i4 : iIndexOf2;
            int i6 = (iIndexOf3 == -1 || iIndexOf3 >= i5) ? i5 : iIndexOf3;
            int i7 = (iIndexOf4 == -1 || iIndexOf4 >= i6) ? i6 : iIndexOf4;
            int i8 = (iIndexOf5 == -1 || iIndexOf5 >= i7) ? i7 : iIndexOf5;
            int i9 = (iIndexOf7 == -1 || iIndexOf7 >= i8) ? i8 : iIndexOf7;
            int i10 = (iIndexOf8 == -1 || iIndexOf8 >= i9) ? i9 : iIndexOf8;
            int i11 = (iIndexOf6 == -1 || iIndexOf6 >= i10) ? i10 : iIndexOf6;
            int i12 = (iIndexOf10 == -1 || iIndexOf10 >= i11) ? i11 : iIndexOf10;
            int i13 = (iIndexOf9 == -1 || iIndexOf9 >= i12) ? i12 : iIndexOf9;
            int i14 = (iIndexOf11 == -1 || iIndexOf11 >= i13) ? i13 : iIndexOf11;
            int i15 = (iIndexOf12 == -1 || iIndexOf12 >= i14) ? i14 : iIndexOf12;
            int i16 = (iIndexOf13 == -1 || iIndexOf13 >= i15) ? i15 : iIndexOf13;
            int i17 = (iIndexOf14 == -1 || iIndexOf14 >= i16) ? i16 : iIndexOf14;
            int i18 = (iIndexOf15 == -1 || iIndexOf15 >= i17) ? i17 : iIndexOf15;
            int i19 = (iIndexOf16 == -1 || iIndexOf16 >= i18) ? i18 : iIndexOf16;
            int i20 = (iIndexOf17 == -1 || iIndexOf17 >= i19) ? i19 : iIndexOf17;
            if (i20 == -1) {
                throw new V.h("Invalid formula," + str + " \noperator not found in section " + strB);
            }
            if (iIndexOf == 0 || iIndexOf4 == 0 || iIndexOf3 == 0 || iIndexOf5 == 0 || iIndexOf6 == 0 || iIndexOf7 == 0 || iIndexOf8 == 0 || iIndexOf16 == 0 || iIndexOf17 == 0) {
                break;
            }
            arrayList.add(strB.substring(0, i20));
            if (strB.length() > i20) {
                arrayList.add(strB.substring(i20, i20 + 1));
                strB = strB.substring(i20 + 1).trim();
            } else {
                strB = "";
            }
        }
        throw new V.h("Formula error, 2 operators without value.\n" + str);
    }

    public static boolean h(String str) throws V.h {
        if (str.indexOf("true") != -1 || str.indexOf("false") != -1) {
            str = W.b(W.b(str, "true", "1"), "false", "0");
        }
        int iIndexOf = str.indexOf("==");
        int iIndexOf2 = iIndexOf > 0 ? iIndexOf : str.indexOf("!=");
        int iIndexOf3 = iIndexOf2 > 0 ? iIndexOf2 : str.indexOf("<=");
        int iIndexOf4 = iIndexOf3 > 0 ? iIndexOf3 : str.indexOf(">=");
        if (iIndexOf4 > 0) {
            try {
                double dG = g(str.substring(0, iIndexOf4).trim());
                double dG2 = g(str.substring(iIndexOf4 + 2).trim());
                return str.indexOf("==") != -1 ? dG == dG2 : str.indexOf("!=") != -1 ? dG != dG2 : str.indexOf("<=") != -1 ? dG <= dG2 : str.indexOf(">=") != -1 && dG >= dG2;
            } catch (Exception e2) {
                throw new V.h("Unable to resolve condition for expression: " + str);
            }
        }
        int iIndexOf5 = str.indexOf("&");
        int iIndexOf6 = iIndexOf5 > 0 ? iIndexOf5 : str.indexOf(">");
        int iIndexOf7 = iIndexOf6 > 0 ? iIndexOf6 : str.indexOf("<");
        int iIndexOf8 = iIndexOf7 > 0 ? iIndexOf7 : str.indexOf("=");
        if (iIndexOf8 > 0) {
            double dG3 = g(str.substring(0, iIndexOf8).trim());
            double dG4 = g(str.substring(iIndexOf8 + 1).trim());
            if (str.indexOf("&") <= 0) {
                return str.indexOf("=") > 0 ? dG3 == dG4 : str.indexOf("<") > 0 ? dG3 < dG4 : str.indexOf(">") > 0 && dG3 > dG4;
            }
            int i2 = (int) (dG3 <= dG4 ? dG3 : dG4);
            return i2 != 0 && (((int) dG3) & ((int) dG4)) == i2;
        }
        if (j(str)) {
            return g(str) != 0.0d;
        }
        try {
            boolean z2 = str.indexOf("!") != -1;
            double dK = k(W.b(str.substring(0, str.length()).trim(), "!", ""));
            return z2 ? dK <= 0.0d : dK > 0.0d;
        } catch (Exception e3) {
            throw new V.h("Error evaluating condition:" + str + ", false returned");
        }
    }

    public static boolean i(String str) {
        String string = str;
        if (string.indexOf("Math.") != -1) {
            string = b(string);
        }
        while (string.indexOf("(") != -1) {
            String strSubstring = string.substring(string.indexOf(")") + 1);
            String strSubstring2 = string.substring(string.lastIndexOf("(", string.indexOf(")")) + 1, string.indexOf(")"));
            string = new StringBuffer(string.substring(0, string.lastIndexOf("(", string.indexOf(")")))).append(i(strSubstring2) + "").append(strSubstring).toString();
        }
        String str2 = string;
        if (str2.indexOf("&&") > 0) {
            StringTokenizer stringTokenizer = new StringTokenizer(str2, "&&");
            while (stringTokenizer.hasMoreTokens()) {
                if (!i(stringTokenizer.nextToken())) {
                    return false;
                }
            }
            return true;
        }
        if (str2.indexOf("||") <= 0) {
            return h(str2);
        }
        StringTokenizer stringTokenizer2 = new StringTokenizer(str2, "||");
        while (stringTokenizer2.hasMoreTokens()) {
            if (i(stringTokenizer2.nextToken())) {
                return true;
            }
        }
        return false;
    }

    public static boolean j(String str) {
        for (int i2 = 0; i2 < "+&|-*/".length(); i2++) {
            if (str.indexOf("+&|-*/".charAt(i2)) > 1) {
                return true;
            }
        }
        return false;
    }

    public static double k(String str) {
        if (str.indexOf("0x") != -1) {
            str = Integer.valueOf(W.b(str, "0x", "").trim(), 16).toString();
        } else if (str.indexOf("0b") != -1) {
            str = Integer.valueOf(W.b(str, "0b", "").trim(), 2).toString();
        }
        return Double.parseDouble(str);
    }

    private static String b(String str) {
        while (str.indexOf("Math.") != -1) {
            String strSubstring = str.substring(0, str.indexOf("Math."));
            int iIndexOf = str.indexOf("Math.");
            int iA = a(str, iIndexOf);
            str = strSubstring + c(str.substring(iIndexOf, iA)) + str.substring(iA);
        }
        return str;
    }

    public static boolean l(String str) {
        try {
            k(str);
            return true;
        } catch (Exception e2) {
            return false;
        }
    }

    private static double c(String str) throws V.h {
        if (str.indexOf("(") == -1) {
            int length = str.length() - 1;
            while (true) {
                if (length <= 0) {
                    break;
                }
                if (!l(str.charAt(length) + "")) {
                    str = str.substring(0, length + 1) + "(" + str.substring(length + 1) + ")";
                    break;
                }
                length--;
            }
        }
        String strSubstring = str.substring(str.indexOf(46) + 1, str.indexOf(40));
        double dG = g(str.substring(str.indexOf(40), str.length()));
        if (strSubstring.equals("log")) {
            if (dG < 0.0d) {
                return 0.0d;
            }
            return Math.log(dG);
        }
        if (strSubstring.equals("sin")) {
            return Math.sin(dG);
        }
        if (strSubstring.equals("cos")) {
            return Math.cos(dG);
        }
        if (strSubstring.equals("tan")) {
            return Math.tan(dG);
        }
        if (strSubstring.equals("atan")) {
            return Math.atan(dG);
        }
        if (!strSubstring.equals("asin") && !strSubstring.equals("acos")) {
            if (strSubstring.equals("abs")) {
                return Math.abs(dG);
            }
            if (strSubstring.equals("sqrt")) {
                return Math.sqrt(dG);
            }
            throw new V.h("Unsupported Math function " + strSubstring);
        }
        return Math.asin(dG);
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

    private static String d(String str) {
        return W.b(W.b(a(a(str, "||"), "&&"), "||", "`"), "&&", "~");
    }

    private static String a(String str, String str2) {
        int iIndexOf = str.indexOf(str2);
        while (true) {
            int i2 = iIndexOf;
            if (i2 != -1 && i2 != -1) {
                String strTrim = str.substring(0, i2).trim();
                String strTrim2 = str.substring(i2 + str2.length(), str.length()).trim();
                int iLastIndexOf = strTrim.lastIndexOf("(", i2);
                if (strTrim.lastIndexOf(")", i2) > iLastIndexOf) {
                    iLastIndexOf = -1;
                }
                String str3 = iLastIndexOf == -1 ? "(" + strTrim + ")" : strTrim.substring(0, iLastIndexOf) + "(" + strTrim.substring(iLastIndexOf) + ")";
                int iIndexOf2 = strTrim2.indexOf(")", i2);
                int iIndexOf3 = strTrim2.indexOf("(", i2);
                if (iIndexOf3 > -1 && iIndexOf3 < iIndexOf2) {
                    iIndexOf2 = -1;
                }
                str = str3 + str2 + (iIndexOf2 == -1 ? "(" + strTrim2 + ")" : "(" + strTrim2.substring(0, iIndexOf2) + ")" + strTrim2.substring(iIndexOf2));
                iIndexOf = str.indexOf(str2, i2 + str2.length() + 1);
            }
            return str;
        }
    }
}
