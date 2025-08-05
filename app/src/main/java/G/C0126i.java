package G;

import L.C0154k;
import L.C0168y;
import com.sun.corba.se.impl.util.Version;
import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.icepdf.core.util.PdfOps;

/* renamed from: G.i, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:G/i.class */
public class C0126i {

    /* renamed from: a, reason: collision with root package name */
    public static String f1244a = "+-/=&<>*^!,% [{()}]|\n\t?:";

    /* renamed from: e, reason: collision with root package name */
    private static Map f1245e = new HashMap();

    /* renamed from: f, reason: collision with root package name */
    private static Map f1246f = new HashMap();

    /* renamed from: b, reason: collision with root package name */
    public static long f1247b = System.currentTimeMillis();

    /* renamed from: c, reason: collision with root package name */
    public static boolean f1248c = false;

    /* renamed from: d, reason: collision with root package name */
    public static boolean f1249d = false;

    public static double a(String str, aI aIVar, byte[] bArr) throws ax.U, NumberFormatException {
        C0154k c0154kC = c(aIVar.c(), str);
        if (c0154kC == null) {
            c0154kC = new C0154k(aIVar);
            c0154kC.a(str);
            a(aIVar.c(), str, c0154kC);
        }
        String[] strArrA = c0154kC.a();
        for (int i2 = 0; i2 < strArrA.length; i2++) {
            try {
                c0154kC.a(strArrA[i2], b(strArrA[i2], aIVar, bArr));
            } catch (V.g e2) {
                if (aIVar.R()) {
                    bH.C.b(e2.getMessage());
                }
                throw new ax.U(e2.getMessage());
            }
        }
        return c0154kC.d();
    }

    public static double b(String str, aI aIVar, byte[] bArr) throws NumberFormatException, V.g {
        if (str.indexOf("[") > 0) {
            String strSubstring = str.substring(0, str.indexOf("["));
            String strC = bH.W.c(str.substring(str.indexOf("[")), "[", "");
            int i2 = Integer.parseInt(strC.substring(0, strC.indexOf("]")));
            int i3 = 0;
            if (strC.indexOf("[") != -1) {
                i3 = Integer.parseInt(strC.substring(strC.indexOf("[") + 1, strC.lastIndexOf("]")));
            }
            aM aMVarC = aIVar.c(strSubstring);
            if (aMVarC != null) {
                return aMVarC.i(aIVar.h())[i2][i3];
            }
            throw new V.g("Variable '" + strSubstring + "' used in expression, but not defined as OutputChannel or Setting Parameter.");
        }
        aH aHVarG = aIVar.g(str);
        if (aHVarG != null) {
            return aHVarG.b(bArr);
        }
        aM aMVarC2 = aIVar.c(str);
        if (aMVarC2 != null) {
            return aMVarC2.j(aIVar.h());
        }
        if ((f1248c || a(str)) && str.contains(".")) {
            String strSubstring2 = str.substring(0, str.indexOf("."));
            str = str.substring(str.indexOf(".") + 1, str.length());
            R rC = T.a().c(strSubstring2);
            if (rC != null) {
                return b(str, rC);
            }
            if (C0113cs.a().e(str)) {
                return C0113cs.a().g(str);
            }
        }
        throw new V.g("Variable '" + str + "' used in expression, but not defined as OutputChannel or Setting Parameter.");
    }

    public static double a(String str, aI aIVar) throws ax.U, NumberFormatException {
        C0154k c0154kC = c(aIVar.c(), str);
        if (c0154kC == null) {
            c0154kC = new C0154k(aIVar);
            c0154kC.a(str);
            a(aIVar.c(), str, c0154kC);
        }
        String[] strArrA = c0154kC.a();
        for (int i2 = 0; i2 < strArrA.length; i2++) {
            try {
                c0154kC.a(strArrA[i2], b(strArrA[i2], aIVar));
            } catch (V.g e2) {
                throw new ax.U(e2.getMessage());
            }
        }
        return c0154kC.d();
    }

    public static double b(String str, aI aIVar) throws NumberFormatException, V.g {
        if (str.indexOf("[") > 0) {
            String strSubstring = str.substring(0, str.indexOf("["));
            String strC = bH.W.c(str.substring(str.indexOf("[")), "[", "");
            int i2 = Integer.parseInt(strC.substring(0, strC.indexOf("]")));
            int i3 = 0;
            if (strC.indexOf("[") != -1) {
                i3 = Integer.parseInt(strC.substring(strC.indexOf("[") + 1, strC.lastIndexOf("]")));
            }
            aM aMVarC = aIVar.c(strSubstring);
            if (aMVarC != null) {
                return aMVarC.i(aIVar.h())[i2][i3];
            }
            throw new V.g("Variable '" + strSubstring + "' used in expression, but not defined as OutputChannel or Setting Parameter.");
        }
        aH aHVarG = aIVar.g(str);
        if (aHVarG != null) {
            return aHVarG.o();
        }
        aM aMVarC2 = aIVar.c(str);
        if (aMVarC2 != null) {
            return aMVarC2.j(aIVar.h());
        }
        if ((f1248c || a(str)) && str.contains(".")) {
            String strSubstring2 = str.substring(0, str.indexOf("."));
            str = str.substring(str.indexOf(".") + 1, str.length());
            R rC = T.a().c(strSubstring2);
            if (rC != null) {
                return b(str, rC);
            }
            if (C0113cs.a().e(str)) {
                return C0113cs.a().g(str);
            }
        }
        throw new V.g("Variable '" + str + "' used in expression, but not defined as OutputChannel or Setting Parameter in " + aIVar.c());
    }

    private static C0154k c(String str, String str2) {
        return (C0154k) f1245e.get(d(str, str2));
    }

    private static String d(String str, String str2) {
        return str2;
    }

    private static C0154k a(String str, String str2, C0154k c0154k) {
        return (C0154k) f1245e.put(d(str, str2), c0154k);
    }

    public static void a() {
        f1245e.clear();
    }

    public static void a(String str, String str2) throws V.g {
        R rC = (str == null || str.isEmpty()) ? T.a().c() : T.a().c(str);
        if (rC == null) {
            if (str != null && !str.isEmpty()) {
                throw new V.g("Configuration Name not found: " + str);
            }
            throw new V.g("No working configuration and no config name requested");
        }
        aH aHVarG = rC.g(str2);
        if (aHVarG == null) {
            throw new V.g("OutputChannel not found: " + str2);
        }
        String strK = aHVarG.k();
        if (strK == null || strK.length() <= 0) {
            return;
        }
        b(rC.c(), strK);
        I.k.a().a(rC.c(), aHVarG.aJ());
    }

    public static void b(String str, String str2) {
        aH aHVarG;
        String[] strArrA;
        aH aHVarG2;
        if (str == null || str.isEmpty()) {
            try {
                str = T.a().c().c();
            } catch (Exception e2) {
            }
        }
        String strD = d(str, str2);
        C0154k c0154k = (C0154k) f1245e.get(strD);
        R rC = T.a().c(str);
        if (c0154k != null && (strArrA = c0154k.a()) != null && strArrA.length == 1 && (aHVarG2 = rC.g(strArrA[0])) != null && aHVarG2.b().equals("formula")) {
            I.k.a().a(str, aHVarG2.k());
            b(str, aHVarG2.k());
        }
        f1245e.remove(strD);
        if (rC == null || (aHVarG = rC.g(str2)) == null || !aHVarG.b().equals("formula")) {
            return;
        }
        b(str, aHVarG.k());
    }

    public static void b() {
        f1247b = System.currentTimeMillis();
        C0168y.a();
    }

    public static ArrayList a(R r2, ArrayList arrayList) {
        return a(r2, (List) a(arrayList));
    }

    public static ArrayList a(R r2, List list) {
        ArrayList arrayList = new ArrayList();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            int iIntValue = ((Integer) it.next()).intValue();
            aH aHVarA = a(r2, iIntValue);
            if (aHVarA == null) {
                aHVarA = b(r2, iIntValue);
            }
            if (aHVarA != null && !arrayList.contains(aHVarA)) {
                arrayList.add(aHVarA);
            }
        }
        return arrayList;
    }

    private static aH b(R r2, int i2) {
        Iterator it = r2.f().iterator();
        while (it.hasNext()) {
            C0052al c0052al = (C0052al) it.next();
            if (c0052al.a() == i2) {
                return c0052al;
            }
        }
        return null;
    }

    public static aH a(R r2, int i2) {
        aH aHVar = null;
        Iterator itQ = r2.q();
        while (itQ.hasNext()) {
            aH aHVar2 = (aH) itQ.next();
            if (aHVar2.a() <= i2 && (aHVar2.a() + aHVar2.l()) - 1 >= i2 && (aHVar == null || aHVar.l() < aHVar2.l())) {
                aHVar = aHVar2;
            }
        }
        return aHVar;
    }

    public static ArrayList a(ArrayList arrayList) {
        ArrayList arrayList2 = new ArrayList();
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            aH aHVar = (aH) it.next();
            if (!arrayList2.contains(Integer.valueOf(aHVar.a()))) {
                arrayList2.add(Integer.valueOf(aHVar.a()));
            }
        }
        return arrayList2;
    }

    private C0126i() {
    }

    public static String c(String str, aI aIVar, byte[] bArr) throws V.g {
        if (str.indexOf("table(") == -1) {
            return str;
        }
        String strSubstring = str.substring(0, str.indexOf("table("));
        int iIndexOf = str.indexOf("(", str.indexOf("table")) + 1;
        int iIndexOf2 = str.indexOf(",", iIndexOf);
        String strTrim = str.substring(iIndexOf, iIndexOf2).trim();
        int iIndexOf3 = str.indexOf(")", iIndexOf2);
        String strTrim2 = str.substring(iIndexOf2 + 1, iIndexOf3).trim();
        String strF = aIVar.K().F();
        String strB = bH.W.b(strTrim2, PdfOps.DOUBLE_QUOTE__TOKEN, "");
        try {
            return (strSubstring + "" + bH.E.b(strF, strB).a(Double.parseDouble(bArr != null ? d(strTrim, aIVar, bArr) : d(strTrim, aIVar)))) + str.substring(iIndexOf3 + 1);
        } catch (IOException e2) {
            throw new V.g("Error loading inc Mapping File:" + strB, e2);
        } catch (Exception e3) {
            throw new V.g("Error parsing inc Mapping File:" + strB + "\n\t" + e3.getMessage());
        }
    }

    public static String c(String str, aI aIVar) {
        return c(str, aIVar, null);
    }

    public static double a(aI aIVar, String str) throws V.g {
        try {
            return a(str, aIVar);
        } catch (ax.U e2) {
            throw new V.g("EcuOutputChannel::Error executing formula:" + str + " \nError:\n" + e2.getMessage());
        }
    }

    public static String d(String str, aI aIVar, byte[] bArr) throws V.g {
        String strE = e(str, aIVar, bArr);
        byte[] bytes = strE.getBytes();
        int i2 = 0;
        while (bytes.length > i2 && f1244a.indexOf((char) bytes[i2]) != -1) {
            i2++;
        }
        for (int i3 = i2 + 1; i3 <= bytes.length; i3++) {
            boolean z2 = strE.length() <= i3 + 1 || f1244a.indexOf(strE.charAt(i3 + 1)) != -1;
            if ((i3 == bytes.length && i2 < i3 - 1) || (i3 != bytes.length && f1244a.indexOf((char) bytes[i3]) != -1)) {
                String strTrim = strE.substring(i2, i3).trim();
                if (strTrim.length() > 1 && !b(strTrim) && !c(strTrim)) {
                    aH aHVarG = aIVar.g(strTrim);
                    String str2 = null;
                    if (aHVarG != null) {
                        try {
                            str2 = "" + aHVarG.b(bArr);
                        } catch (Exception e2) {
                            bH.C.c("ChannelUtil::replaceChannelNamesWithValues() Error getting value");
                            e2.printStackTrace();
                        }
                    } else {
                        aM aMVarC = aIVar.c(strTrim);
                        if (aMVarC == null) {
                            str2 = Version.BUILD;
                            bH.C.c("couldn't get value for " + strTrim + " Filling with 0.0, formula:\n\t" + strE);
                        } else {
                            try {
                                str2 = "" + aMVarC.j(aIVar.h());
                            } catch (Exception e3) {
                                bH.C.b("Could not get value for " + strTrim + ", formula:\n\t" + strE);
                            }
                        }
                    }
                    return d(bH.W.c(strE, strTrim, str2), aIVar, bArr);
                }
                i2 = i3 + 1;
            }
        }
        return strE;
    }

    public static String d(String str, aI aIVar) throws V.g {
        String strI = i(str, aIVar);
        byte[] bytes = strI.getBytes();
        int i2 = 0;
        while (bytes.length > i2 && f1244a.indexOf((char) bytes[i2]) != -1) {
            i2++;
        }
        for (int i3 = i2 + 1; i3 <= bytes.length; i3++) {
            if ((i3 == bytes.length && i2 < i3 - 1) || (i3 != bytes.length && f1244a.indexOf((char) bytes[i3]) != -1)) {
                String strTrim = strI.substring(i2, i3).trim();
                if (strTrim.length() > 1 && !b(strTrim) && !c(strTrim)) {
                    aH aHVarG = aIVar.g(strTrim);
                    String str2 = null;
                    if (aHVarG != null) {
                        try {
                            str2 = "" + aHVarG.o();
                        } catch (Exception e2) {
                            bH.C.c("ChannelUtil::replaceChannelNamesWithValues() Error getting value");
                            e2.printStackTrace();
                        }
                    } else {
                        aM aMVarC = aIVar.c(strTrim);
                        if (aMVarC == null) {
                            throw new V.g(" Variable '" + strTrim + "' is not defined in current configuration.");
                        }
                        try {
                            str2 = "" + aMVarC.j(aIVar.h());
                        } catch (Exception e3) {
                            bH.C.b("Could not get value for " + strTrim + ", formula:\n\t" + strI);
                        }
                    }
                    return d(bH.W.c(strI, strTrim, str2), aIVar);
                }
                i2 = i3 + 1;
            }
        }
        return strI;
    }

    public static String a(String str, R r2) throws V.g {
        int length = 0;
        while (str.length() > length && f1244a.indexOf(str.charAt(length)) != -1) {
            length++;
        }
        int i2 = length + 1;
        while (i2 <= str.length()) {
            if ((i2 == str.length() && length < i2 - 1) || (i2 <= str.length() && (f1244a.indexOf(str.charAt(i2 - 1)) != -1 || i2 == str.length() || f1244a.indexOf(str.charAt(i2)) != -1))) {
                String strTrim = str.substring(length, i2).trim();
                if (strTrim.length() <= 1 || b(strTrim) || c(strTrim)) {
                    length = i2;
                } else {
                    String str2 = null;
                    if (r2.g(strTrim) != null) {
                        C0043ac c0043acA = a(r2, strTrim);
                        if (c0043acA == null) {
                            throw new V.g("No DataLogField for channel " + strTrim);
                        }
                        try {
                            str2 = "[" + c0043acA.b() + "]";
                        } catch (Exception e2) {
                            bH.C.c("ChannelUtil::convertToMlvFormula() Error getting value");
                            e2.printStackTrace();
                        }
                    } else {
                        aM aMVarC = r2.c(strTrim);
                        if (aMVarC == null) {
                            throw new V.g(" Variable '" + strTrim + "' is not defined in current configuration.");
                        }
                        try {
                            str2 = "" + aMVarC.j(r2.h());
                        } catch (Exception e3) {
                            bH.C.b("Could not get value for " + strTrim + ", formula:\n\t" + str);
                        }
                    }
                    str = bH.W.c(str, strTrim, str2);
                    length = i2 - (strTrim.length() - str2.length());
                    i2 = length;
                }
            }
            i2++;
        }
        return str;
    }

    public static C0043ac a(R r2, String str) {
        Iterator it = r2.g().iterator();
        while (it.hasNext()) {
            C0043ac c0043ac = (C0043ac) it.next();
            if (c0043ac.a().equals(str)) {
                return c0043ac;
            }
        }
        return null;
    }

    private static String i(String str, aI aIVar) {
        while (str != null && str.indexOf("table(") != -1) {
            str = c(str, aIVar);
        }
        if (str.indexOf("timeNow") != -1) {
            str = bH.W.b(str, "timeNow", "" + ((System.currentTimeMillis() - f1247b) / 1000.0d));
        }
        if (str.indexOf("tempCvt(") != -1) {
            str = a(str, aIVar.d("CELSIUS") != null);
        }
        return str;
    }

    private static String e(String str, aI aIVar, byte[] bArr) throws V.g {
        while (str != null && str.indexOf("table(") != -1) {
            str = c(str, aIVar, bArr);
        }
        if (str.indexOf("timeNow") != -1) {
            str = bH.W.b(str, "timeNow", "" + ((System.currentTimeMillis() - f1247b) / 1000.0d));
        }
        if (str.indexOf("tempCvt(") != -1) {
            str = a(str, aIVar.d("CELSIUS") != null);
        }
        return str;
    }

    public static boolean a(String str) {
        if (str != null) {
            return str.equals(SchemaSymbols.ATTVAL_TIME) || str.equals("dataLogTime") || str.equals("GPS.gpsSpeedMPH") || str.contains("AppEvent.dataLogTime") || str.contains("AppEvent.dataLoggingActive");
        }
        return false;
    }

    public static String[] e(String str, aI aIVar) {
        byte[] bytes = str.getBytes();
        ArrayList arrayList = new ArrayList();
        C0154k c0154kC = c(aIVar.c(), str);
        if (c0154kC != null) {
            for (String str2 : c0154kC.a()) {
                if (aIVar.g(str2) != null) {
                    arrayList.add(str2);
                }
            }
            return (String[]) arrayList.toArray(new String[arrayList.size()]);
        }
        int i2 = 0;
        for (int i3 = 1; i3 < bytes.length; i3++) {
            if ((i3 == bytes.length && i2 < i3 - 1) || (i3 != bytes.length && f1244a.indexOf((char) bytes[i3]) != -1)) {
                String strTrim = str.substring(i2, i3).trim();
                if (strTrim.length() <= 1 || b(strTrim) || c(strTrim)) {
                    i2 = i3 + 1;
                } else {
                    if (aIVar.g(strTrim) != null) {
                        arrayList.add(strTrim);
                    }
                    i2 = i3 + 1;
                }
            }
        }
        if (arrayList.isEmpty() && aIVar.g(str.trim()) != null) {
            arrayList.add(str.trim());
        }
        return (String[]) arrayList.toArray(new String[arrayList.size()]);
    }

    public static String[] f(String str, aI aIVar) {
        C0154k c0154kC = c(aIVar.c(), str);
        if (c0154kC == null) {
            if (str.contains("%INDEX%")) {
                str = bH.W.b(str, "%INDEX%", "0");
            }
            c0154kC = new C0154k(aIVar);
            c0154kC.a(str);
            a(aIVar.c(), str, c0154kC);
        }
        return c0154kC.a();
    }

    public static String[] g(String str, aI aIVar) {
        ArrayList arrayList = new ArrayList();
        String[] strArrF = f(str, aIVar);
        for (int i2 = 0; i2 < strArrF.length; i2++) {
            if (!arrayList.contains(strArrF[i2])) {
                if (aIVar.g(strArrF[i2]) != null) {
                    aH aHVarG = aIVar.g(strArrF[i2]);
                    if (aHVarG.b().equals("formula")) {
                        for (String str2 : g(aHVarG.k(), aIVar)) {
                            arrayList.add(str2);
                        }
                    }
                } else {
                    arrayList.add(strArrF[i2]);
                }
            }
        }
        return (String[]) arrayList.toArray(new String[arrayList.size()]);
    }

    public static String[] h(String str, aI aIVar) {
        try {
            String[] strArrG = g(str, aIVar);
            ArrayList arrayList = new ArrayList();
            for (String str2 : strArrG) {
                if (aIVar.c(str2) != null) {
                    arrayList.add(str2);
                }
            }
            return (String[]) arrayList.toArray(new String[arrayList.size()]);
        } catch (ax.U e2) {
            Logger.getLogger(C0126i.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            return new String[0];
        }
    }

    public static void a(String str, aM aMVar, aN aNVar) {
        a(str, aMVar.F(), aNVar);
        a(str, aMVar.E(), aNVar);
        a(str, aMVar.v(), aNVar);
        a(str, aMVar.J(), aNVar);
        a(str, aMVar.L(), aNVar);
        a(str, aMVar.t(), aNVar);
        a(str, aMVar.s(), aNVar);
        aR.a().a(str, aMVar.aJ(), aNVar);
    }

    public static void a(String str, dh dhVar, aN aNVar) {
        if (dhVar instanceof bQ) {
            for (String str2 : ((bQ) dhVar).b()) {
                aR.a().a(str, str2, aNVar);
            }
        }
    }

    public static void a(R r2, String str, aN aNVar) {
        if (str == null || str.isEmpty()) {
            return;
        }
        for (String str2 : h(str, r2)) {
            aR.a().a(r2.c(), str2, aNVar);
        }
    }

    public static void a(String str, String str2, InterfaceC0109co interfaceC0109co) {
        R rC = T.a().c(str);
        if (rC == null) {
            rC = T.a().c();
        }
        if (str2 == null || str2.isEmpty()) {
            return;
        }
        for (String str3 : e(str2, rC)) {
            try {
                C0113cs.a().a(str, str3, interfaceC0109co);
            } catch (V.a e2) {
                Logger.getLogger(C0126i.class.getName()).log(Level.WARNING, "Unable to suppcribe Channel from expression: " + str2, (Throwable) e2);
            }
        }
    }

    public static void a(String str, cZ cZVar, aN aNVar) {
        if (cZVar != null) {
            String[] strArrB = cZVar.b();
            for (int i2 = 0; strArrB != null && i2 < strArrB.length; i2++) {
                String strSubstring = strArrB[i2];
                int iIndexOf = strSubstring.indexOf("[");
                if (iIndexOf != -1) {
                    strSubstring = strSubstring.substring(0, iIndexOf);
                }
                aR.a().a(str, strSubstring, aNVar);
            }
        }
    }

    public static String a(String str, boolean z2) {
        int iIndexOf = str.indexOf("tempCvt");
        if (iIndexOf != -1) {
            String strSubstring = str.substring(0, iIndexOf);
            int length = iIndexOf + "tempCvt".length();
            int iA = a(str, length) + 1;
            String strSubstring2 = str.substring(length, iA);
            String strSubstring3 = str.substring(iA);
            if (z2) {
                strSubstring2 = "(" + strSubstring2 + "-32)*5/9";
            }
            str = strSubstring + strSubstring2 + strSubstring3;
        }
        return str;
    }

    public static int a(String str, int i2) {
        int i3 = 0;
        boolean z2 = false;
        int i4 = -1;
        for (int i5 = i2; i5 < str.length() && (!z2 || i4 == -1); i5++) {
            if (str.charAt(i5) == '(') {
                i3++;
                z2 = true;
            } else if (z2 && str.charAt(i5) == ')') {
                i3--;
            }
            if (i3 < 0) {
                bH.C.b("Found Close Parenthesis before open starting from index: " + i2 + ", in Expression:" + str);
            }
            if (z2 && i3 == 0) {
                i4 = i5;
            }
        }
        return i4;
    }

    public static boolean b(String str) {
        try {
            if (str.startsWith("0b")) {
                Integer.parseInt(str.substring(2), 2);
                return true;
            }
            if (str.startsWith("0x")) {
                Integer.parseInt(str.substring(2), 16);
                return true;
            }
            Double.parseDouble(str);
            return true;
        } catch (Exception e2) {
            return false;
        }
    }

    public static void a(String str, String str2, String str3) {
        InterfaceC0109co interfaceC0109coA = a(str, str2, str3, false);
        if (interfaceC0109coA != null) {
            C0113cs.a().a(interfaceC0109coA);
        }
        e(str2, str3);
    }

    public static void b(String str, String str2, String str3) throws C0134q {
        if (str3 == null || str3.equals("")) {
            return;
        }
        InterfaceC0109co interfaceC0109coA = a(str, str2, str3, true);
        R rC = T.a().c(str2);
        if (rC == null) {
            throw new C0134q("Ecuconfiguration not loadeed: " + str2);
        }
        try {
            String[] strArrF = f(str3, rC);
            for (int i2 = 0; i2 < strArrF.length; i2++) {
                if (rC.g(strArrF[i2]) != null) {
                    try {
                        C0113cs.a().a(str2, strArrF[i2], interfaceC0109coA);
                    } catch (V.a e2) {
                        Logger.getLogger(C0126i.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                    }
                } else if (C0113cs.a().e(strArrF[i2])) {
                    C0113cs.a().a(strArrF[i2], interfaceC0109coA);
                } else if (strArrF[i2].contains("AppEvent.")) {
                    String strB = bH.W.b(strArrF[i2], "AppEvent.", "");
                    if (C0113cs.a().e(strB)) {
                        C0113cs.a().a(strB, interfaceC0109coA);
                    }
                }
            }
        } catch (ax.U e3) {
            e3.printStackTrace();
        }
    }

    private static InterfaceC0109co a(String str, String str2, String str3, boolean z2) {
        String str4 = str + str2 + str3;
        InterfaceC0109co c0127j = (InterfaceC0109co) f1246f.get(str4);
        if (c0127j == null && z2) {
            c0127j = new C0127j();
            f1246f.put(str4, c0127j);
        }
        return c0127j;
    }

    private static void e(String str, String str2) {
        f1246f.remove(str + str2);
    }

    public static boolean c(String str) {
        return a(str, (char[]) null);
    }

    public static boolean a(String str, char[] cArr) {
        for (int i2 = 0; i2 < f1244a.length(); i2++) {
            if (!a(f1244a.charAt(i2), cArr) && str.indexOf(f1244a.charAt(i2)) != -1) {
                return true;
            }
        }
        return false;
    }

    private static boolean a(char c2, char[] cArr) {
        if (cArr == null) {
            return false;
        }
        for (char c3 : cArr) {
            if (c3 == c2) {
                return true;
            }
        }
        return false;
    }

    public static ArrayList a(aI aIVar, aH aHVar) {
        ArrayList arrayList = new ArrayList();
        if (aHVar.b().equals("formula")) {
            String[] strArrE = e(aHVar.k(), aIVar);
            if (strArrE != null) {
                for (int i2 = 0; i2 < strArrE.length; i2++) {
                    if (strArrE[i2].equals(aHVar.aJ())) {
                        arrayList.add(aHVar);
                    } else {
                        ArrayList arrayListA = a(aIVar, aIVar.g(strArrE[i2]));
                        if (arrayListA != null) {
                            for (int i3 = 0; i3 < arrayListA.size(); i3++) {
                                if (!arrayList.contains(arrayListA.get(i3))) {
                                    arrayList.add(arrayListA.get(i3));
                                }
                            }
                        }
                    }
                }
            }
        } else if (!arrayList.contains(aHVar)) {
            arrayList.add(aHVar);
        }
        return arrayList;
    }
}
