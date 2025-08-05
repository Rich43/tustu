package g;

import W.C0188n;
import bH.E;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.slf4j.Marker;
import sun.util.locale.LanguageTag;

/* renamed from: g.d, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:g/d.class */
public class C1726d extends E {
    protected C1726d() {
    }

    public static String a(C0188n c0188n, String str, int i2) throws NumberFormatException {
        String strA;
        String strA2;
        String strSubstring;
        String strSubstring2;
        int iIndexOf = str.indexOf(CallSiteDescriptor.OPERATOR_DELIMITER);
        while (true) {
            int i3 = iIndexOf;
            if (i3 == -1) {
                return str;
            }
            if (i3 < str.indexOf(".inc")) {
                strA = C1733k.a(str.substring(i3 + 1, str.indexOf("]", i3)), "]", "");
                strA2 = C1733k.a(str.substring(str.lastIndexOf("[", i3), i3), "[", "");
                strSubstring = str.substring(0, str.indexOf(strA2 + CallSiteDescriptor.OPERATOR_DELIMITER) - 1);
                strSubstring2 = str.substring(str.indexOf(strA) + strA.length() + 1, str.length());
            } else {
                String strSubstring3 = str.substring(0, i3);
                String strSubstring4 = str.substring(i3 + 1);
                strA = C1733k.a(strSubstring3, "[", "");
                strA2 = C1733k.a(strSubstring4, "]", "");
                strSubstring = str.substring(0, str.indexOf(strA) - 1);
                strSubstring2 = str.substring(str.indexOf(strA2) + strA2.length() + 1, str.length());
            }
            i2 = a(strA, i2, c0188n);
            E eB = b(".", b(strA));
            str = c0188n != null ? strSubstring + eB.a(Float.toString(c0188n.a(h.g.a().a(strA2)).d(i2))) + strSubstring2 : strSubstring + eB.a(strA2) + strSubstring2;
            iIndexOf = str.indexOf(CallSiteDescriptor.OPERATOR_DELIMITER);
        }
    }

    private static int a(String str, int i2, C0188n c0188n) throws NumberFormatException {
        if (str.indexOf(Marker.ANY_NON_NULL_MARKER) != -1 || str.indexOf(LanguageTag.SEP) != -1) {
            boolean z2 = str.indexOf(Marker.ANY_NON_NULL_MARKER) != -1;
            int iIndexOf = z2 ? str.indexOf(Marker.ANY_NON_NULL_MARKER) : str.indexOf(LanguageTag.SEP);
            String strSubstring = str.substring(iIndexOf + 1, str.length());
            C1733k.a(str, str.substring(iIndexOf, str.length()), "");
            int i3 = Integer.parseInt(strSubstring);
            i2 = z2 ? i2 + i3 : i2 - i3;
            if (i2 < 0) {
                i2 = 0;
            }
            if (i2 > c0188n.d() - 1) {
                i2 = c0188n.d() - 1;
            }
        }
        return i2;
    }
}
