package G;

import bH.C1007o;
import com.efiAnalytics.plugin.ecu.ControllerParameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:G/bL.class */
public class bL {

    /* renamed from: a, reason: collision with root package name */
    private static bH.aa f854a = null;

    public static String a(R r2, String str) {
        String strA = null;
        Iterator itB = r2.e().b();
        while (true) {
            if (!itB.hasNext()) {
                break;
            }
            aA aAVar = (aA) itB.next();
            if (aAVar.d() == null || !aAVar.d().equals(str)) {
                Iterator itA = aAVar.a();
                while (itA.hasNext()) {
                    aA aAVar2 = (aA) itA.next();
                    C0088bu c0088buC = aAVar2.d() == null ? null : r2.e().c(aAVar2.d());
                    if ((aAVar2.d() != null && aAVar2.d().equals(str)) || (c0088buC != null && c0088buC.L() && a(c0088buC, str))) {
                        strA = a(aAVar.aH(), aAVar2.aH());
                        break;
                    }
                }
            } else {
                strA = aAVar.aH();
                C0088bu c0088buC2 = r2.e().c(str);
                if (c0088buC2 != null && c0088buC2.aH() != null && !c0088buC2.aH().isEmpty()) {
                    strA = a(strA, c0088buC2.aH());
                }
            }
        }
        C0088bu c0088buC3 = r2.e().c(str);
        if (c0088buC3 != null && c0088buC3.aH() != null && !c0088buC3.aH().isEmpty()) {
            strA = a(strA, c0088buC3.aH());
        }
        return strA;
    }

    public static String b(R r2, String str) {
        Iterator itB = r2.e().b();
        while (itB.hasNext()) {
            aA aAVar = (aA) itB.next();
            Iterator itA = aAVar.a();
            while (itA.hasNext()) {
                aA aAVar2 = (aA) itA.next();
                C0088bu c0088buC = aAVar2.d() == null ? null : r2.e().c(aAVar2.d());
                if (c0088buC != null && b(c0088buC, str) != null) {
                    return a(aAVar.e()) + " --> " + a(aAVar2.e()) + " --> " + a(g(r2, str));
                }
            }
        }
        return null;
    }

    private static String a(String str, String str2) {
        return (str == null || str.isEmpty() || str2 == null || str2.isEmpty()) ? (str == null || str.isEmpty()) ? str2 : str : "( " + str + ") && ( " + str2 + ")";
    }

    public static boolean a(C0088bu c0088bu, String str) {
        Iterator itK = c0088bu.K();
        while (itK.hasNext()) {
            C0088bu c0088bu2 = (C0088bu) itK.next();
            if (c0088bu2.aJ() != null && c0088bu2.aJ().equals(str)) {
                return true;
            }
            if (c0088bu2.L() && a(c0088bu2, str)) {
                return true;
            }
        }
        return false;
    }

    public static String c(R r2, String str) {
        C0088bu c0088buC = r2.e().c(str);
        String strM = c0088buC == null ? "" : c0088buC instanceof C0079bl ? ((C0079bl) c0088buC).M() : c0088buC instanceof C0072be ? ((C0072be) c0088buC).M() : (c0088buC.M() == null || c0088buC.M().length() <= 1) ? str : c0088buC.M();
        Iterator itB = r2.e().b();
        while (true) {
            if (!itB.hasNext()) {
                break;
            }
            aA aAVar = (aA) itB.next();
            if (aAVar.d() != null && aAVar.d().equals(str)) {
                strM = aAVar.e();
                break;
            }
            Iterator itA = aAVar.a();
            while (true) {
                if (itA.hasNext()) {
                    aA aAVar2 = (aA) itA.next();
                    if (aAVar2.d() != null && aAVar2.d().equals(str)) {
                        strM = aAVar2.e();
                        break;
                    }
                }
            }
        }
        return strM;
    }

    public static ArrayList a(R r2, ArrayList arrayList) {
        ArrayList arrayList2 = new ArrayList();
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            C0088bu c0088buJ = (C0088bu) it.next();
            if ((c0088buJ.aJ() == null || c0088buJ.aJ().isEmpty()) && c0088buJ.Z() == 1) {
                c0088buJ = c0088buJ.j(0);
            }
            String strA = a(r2, (c0088buJ.aJ() == null || c0088buJ.aJ().length() <= 0) ? c0088buJ.M() : c0088buJ.aJ());
            if (strA != null) {
                try {
                    if (strA.equals("") || C1007o.a(strA, r2)) {
                    }
                } catch (V.g e2) {
                    bH.C.a("An Error occured while evaluating " + strA);
                    e2.printStackTrace();
                }
            }
            arrayList2.add(c0088buJ);
        }
        return arrayList2;
    }

    public static ArrayList a(R r2, C0088bu c0088bu) {
        String[] strArrA;
        ArrayList arrayList = new ArrayList();
        if (c0088bu instanceof C0072be) {
            C0072be c0072be = (C0072be) c0088bu;
            String strA = c0072be.a();
            if (!arrayList.contains(strA)) {
                arrayList.add(strA);
            }
            String strB = c0072be.b();
            if (!arrayList.contains(strB)) {
                arrayList.add(strB);
            }
            String strC = c0072be.c();
            if (!arrayList.contains(strC)) {
                arrayList.add(strC);
            }
        } else if (c0088bu instanceof C0079bl) {
            C0079bl c0079bl = (C0079bl) c0088bu;
            for (int i2 = 0; i2 < c0079bl.j(); i2++) {
                String strD = c0079bl.d(i2);
                if (!arrayList.contains(strD)) {
                    arrayList.add(strD);
                }
            }
            for (int i3 = 0; i3 < c0079bl.d(); i3++) {
                String strB2 = c0079bl.b(i3);
                if (!arrayList.contains(strB2)) {
                    arrayList.add(strB2);
                }
            }
        } else if (c0088bu instanceof aS) {
            aS aSVar = (aS) c0088bu;
            if (aSVar.m() != null) {
                arrayList.add(aSVar.m());
            }
            if (aSVar.d() != null) {
                arrayList.add(aSVar.d());
            }
            if (aSVar.j() != null) {
                arrayList.add(aSVar.j());
            }
            if (aSVar.h() != null) {
                arrayList.add(aSVar.h());
            }
            if (aSVar.o() != null) {
                arrayList.add(aSVar.o());
            }
            if (aSVar.g() != null) {
                arrayList.add(aSVar.g());
            }
            if (aSVar.f() != null) {
                arrayList.add(aSVar.f());
            }
            if (aSVar.k() != null) {
                arrayList.add(aSVar.k());
            }
            if (aSVar.i() != null) {
                arrayList.add(aSVar.i());
            }
            if (aSVar.l() != null) {
                arrayList.add(aSVar.l());
            }
        } else if (c0088bu instanceof C0047ag) {
            C0047ag c0047ag = (C0047ag) c0088bu;
            if (c0047ag.a() != null) {
                arrayList.add(c0047ag.a());
            }
            if (c0047ag.b() != null) {
                arrayList.add(c0047ag.b());
            }
        } else {
            Iterator itF = c0088bu.F();
            while (itF.hasNext()) {
                AbstractC0093bz abstractC0093bz = (AbstractC0093bz) itF.next();
                if ((abstractC0093bz instanceof C0083bp) && (strArrA = ((C0083bp) abstractC0093bz).a()) != null) {
                    for (int i4 = 0; i4 < strArrA.length; i4++) {
                        String strSubstring = strArrA[i4];
                        if (strSubstring.contains("[")) {
                            strSubstring = strSubstring.substring(0, strSubstring.indexOf("["));
                        }
                        if (strSubstring != null && strSubstring.length() > 0 && !arrayList.contains(strSubstring)) {
                            arrayList.add(strSubstring);
                        }
                    }
                }
            }
        }
        Iterator itK = c0088bu.K();
        while (itK.hasNext()) {
            Iterator it = a(r2, (C0088bu) itK.next()).iterator();
            while (it.hasNext()) {
                String str = (String) it.next();
                if (!arrayList.contains(str)) {
                    arrayList.add(str);
                }
            }
        }
        return arrayList;
    }

    public static C0102ch[] b(R r2, ArrayList arrayList) {
        HashMap map = new HashMap();
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            String str = (String) it.next();
            aM aMVarC = r2.c(str);
            if (aMVarC == null) {
                bH.C.c("EcuParameter: '" + str + "' not found in Configuration " + r2.c());
            } else if (map.get(Integer.valueOf(aMVarC.d())) != null) {
                C0102ch c0102ch = (C0102ch) map.get(Integer.valueOf(aMVarC.d()));
                int iMax = Math.max(c0102ch.b() + c0102ch.c(), aMVarC.g() + aMVarC.y());
                int iMin = Math.min(c0102ch.b(), aMVarC.g());
                c0102ch.a(iMin);
                c0102ch.b(iMax - iMin);
            } else {
                C0102ch c0102ch2 = new C0102ch(aMVarC.d());
                c0102ch2.a(aMVarC.g());
                c0102ch2.b(aMVarC.y());
                map.put(Integer.valueOf(aMVarC.d()), c0102ch2);
            }
        }
        if (map.size() == 0) {
            return null;
        }
        return (C0102ch[]) map.values().toArray(new C0102ch[map.size()]);
    }

    public static C0102ch[] b(R r2, C0088bu c0088bu) {
        return b(r2, a(r2, c0088bu));
    }

    public static aM a(R r2, int i2, int i3, int i4) {
        Iterator itA = r2.a(i2);
        while (itA.hasNext()) {
            aM aMVar = (aM) itA.next();
            if (aMVar != null && aMVar.a(i2, i3, i4)) {
                return aMVar;
            }
        }
        return null;
    }

    public static C0083bp d(R r2, String str) {
        Iterator itC = r2.e().c();
        while (itC.hasNext()) {
            C0083bp c0083bpB = b((C0088bu) itC.next(), str);
            if (c0083bpB != null) {
                return c0083bpB;
            }
        }
        return null;
    }

    public static C0083bp b(C0088bu c0088bu, String str) {
        if (str.equals("adc_enable")) {
        }
        Iterator itF = c0088bu.F();
        while (itF.hasNext()) {
            AbstractC0093bz abstractC0093bz = (AbstractC0093bz) itF.next();
            if (abstractC0093bz instanceof C0083bp) {
                C0083bp c0083bp = (C0083bp) abstractC0093bz;
                if (c0083bp.b() != null && c0083bp.b().equals(str)) {
                    return c0083bp;
                }
            }
        }
        try {
            Iterator itK = c0088bu.K();
            while (itK.hasNext()) {
                C0083bp c0083bpB = b((C0088bu) itK.next(), str);
                if (c0083bpB != null) {
                    return c0083bpB;
                }
            }
            return null;
        } catch (StackOverflowError e2) {
            bH.C.c("Boom 45486");
            throw e2;
        }
    }

    public static String e(R r2, String str) {
        ax.Q q2 = new ax.Q();
        try {
            q2.a(str);
            StringBuilder sb = new StringBuilder(f(r2, str));
            String[] strArrA = q2.a();
            String[] strArr = new String[strArrA.length];
            for (int i2 = 0; i2 < strArrA.length; i2++) {
                strArr[i2] = g(r2, strArrA[i2]);
                sb = bH.W.a(sb, strArrA[i2], strArr[i2]);
            }
            return bH.W.a(bH.W.a(bH.W.a(sb, "&&", " AND "), "||", " OR "), "==", "=").toString();
        } catch (ax.U e2) {
            Logger.getLogger(bL.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            return str;
        }
    }

    public static String f(R r2, String str) {
        int i2 = 0;
        if (str.indexOf("==") == -1) {
            return str;
        }
        StringBuilder sb = new StringBuilder(str);
        while (sb.indexOf("==", i2) != -1) {
            int iIndexOf = sb.indexOf("==", i2);
            String strA = a(sb, iIndexOf);
            String strB = b(sb, iIndexOf + 2);
            aM aMVarC = null;
            String str2 = null;
            boolean z2 = false;
            if (bH.H.a(strA)) {
                str2 = strA;
                aMVarC = r2.c(strB);
            } else if (bH.H.a(strB)) {
                str2 = strB;
                aMVarC = r2.c(strA);
                z2 = true;
            }
            if (str2 != null && aMVarC != null && bH.H.a(str2) && aMVarC.i().equals(ControllerParameter.PARAM_CLASS_BITS)) {
                double dK = bH.F.k(str2);
                ArrayList arrayListX = aMVarC.x();
                String str3 = (dK < 0.0d || dK >= ((double) arrayListX.size())) ? "INVALID" : (String) arrayListX.get((int) dK);
                if (z2) {
                    int iIndexOf2 = sb.indexOf(str2, iIndexOf);
                    int length = iIndexOf2 + str2.length();
                    sb.replace(iIndexOf2, length, str3);
                    iIndexOf = length;
                } else {
                    int iLastIndexOf = sb.lastIndexOf(str2, iIndexOf);
                    sb.replace(iLastIndexOf, iLastIndexOf + str2.length(), str3);
                    iIndexOf++;
                }
            }
            i2 = iIndexOf + 1;
        }
        return sb.toString();
    }

    private static String a(StringBuilder sb, int i2) {
        if (i2 <= 1) {
            return "";
        }
        String strSubstring = sb.substring(i2 - 1, i2);
        while (strSubstring.equals(" ") && i2 > 0) {
            i2--;
            strSubstring = sb.substring(i2 - 1, i2);
        }
        StringBuilder sb2 = new StringBuilder();
        while (i2 > 0 && C0126i.f1244a.indexOf(sb.charAt(i2 - 1)) == -1) {
            i2--;
            sb2.insert(0, sb.charAt(i2));
        }
        return sb2.toString();
    }

    private static String b(StringBuilder sb, int i2) {
        String str;
        if (i2 > sb.length()) {
            return "";
        }
        String strSubstring = sb.substring(i2, i2 + 1);
        while (true) {
            str = strSubstring;
            if (!str.equals(" ") || i2 + 1 >= sb.length()) {
                break;
            }
            i2++;
            strSubstring = sb.substring(i2, i2 + 1);
        }
        StringBuilder sb2 = new StringBuilder(str);
        while (i2 < sb.length() && C0126i.f1244a.indexOf(sb.charAt(i2)) != -1) {
            sb2.insert(0, sb.charAt(i2));
            i2++;
        }
        return sb2.toString();
    }

    public static String g(R r2, String str) {
        C0083bp c0083bpD = d(r2, str);
        if (c0083bpD != null && c0083bpD.l().length() > 0) {
            return c0083bpD.l();
        }
        C0043ac c0043acH = h(r2, str);
        return (c0043acH == null || c0043acH.b().length() <= 0) ? str : c0043acH.b();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static C0043ac h(R r2, String str) {
        ArrayList arrayListG = r2.g();
        for (int i2 = 0; i2 < arrayListG.size(); i2++) {
            C0043ac c0043ac = (C0043ac) arrayListG.get(i2);
            if (c0043ac != null && c0043ac.a() != null && c0043ac.a().equals(str)) {
                return c0043ac;
            }
        }
        return null;
    }

    public static boolean a(C0088bu c0088bu) {
        if ((c0088bu instanceof C0079bl) || (c0088bu instanceof C0076bi) || (c0088bu instanceof C0072be)) {
            return true;
        }
        if (!c0088bu.L()) {
            return false;
        }
        Iterator itK = c0088bu.K();
        while (itK.hasNext()) {
            if (a((C0088bu) itK.next())) {
                return true;
            }
        }
        return false;
    }

    public static boolean i(R r2, String str) {
        ArrayList arrayList = new ArrayList();
        Iterator itC = r2.e().c();
        while (itC.hasNext()) {
            arrayList.add((C0088bu) itC.next());
        }
        String strA = null;
        Iterator it = arrayList.iterator();
        while (true) {
            if (it.hasNext()) {
                C0088bu c0088bu = (C0088bu) it.next();
                if (c0088bu.e().contains(str)) {
                    strA = a(r2, c0088bu.aJ());
                    String strI = c0088bu.i(str);
                    if (strA != null && !strA.equals("") && strI != null && !strI.equals("")) {
                        strA = "( " + strA + " ) && ( " + strI + " )";
                    } else if (strI != null && !strI.equals("")) {
                        strA = strI;
                    }
                }
            }
        }
        try {
            return C1007o.a(strA, r2);
        } catch (V.g e2) {
            return true;
        }
    }

    public static List c(R r2, C0088bu c0088bu) {
        List listE = c0088bu.e();
        for (int i2 = 0; i2 < listE.size(); i2++) {
            String str = (String) listE.get(i2);
            aM aMVarC = r2.c(str);
            if (aMVarC != null) {
                listE = a(a(a(a(a(listE, aMVarC.F()), aMVarC.E()), aMVarC.t()), aMVarC.s()), aMVarC.v());
            } else {
                bH.C.b("Unknown EcuParameter: " + str);
            }
        }
        return listE;
    }

    private static List a(List list, dh dhVar) {
        if (dhVar instanceof bQ) {
            String[] strArrB = ((bQ) dhVar).b();
            for (int i2 = 0; i2 < strArrB.length; i2++) {
                if (!list.contains(strArrB[i2])) {
                    list.add(strArrB[i2]);
                }
            }
        }
        return list;
    }

    public static String j(R r2, String str) {
        Iterator it = r2.g().iterator();
        while (it.hasNext()) {
            C0043ac c0043ac = (C0043ac) it.next();
            if (c0043ac.a() != null && c0043ac.a().equals(str)) {
                return c0043ac.b();
            }
        }
        return null;
    }

    public static void a(bH.aa aaVar) {
        f854a = aaVar;
    }

    private static String a(String str) {
        return f854a != null ? f854a.a(str) : str;
    }

    public static int k(R r2, String str) {
        int iF = -1;
        Iterator itB = r2.e().b();
        while (true) {
            if (!itB.hasNext()) {
                break;
            }
            aA aAVar = (aA) itB.next();
            if (aAVar.d() == null || !aAVar.d().equals(str)) {
                Iterator itA = aAVar.a();
                while (itA.hasNext()) {
                    aA aAVar2 = (aA) itA.next();
                    C0088bu c0088buC = aAVar2.d() == null ? null : r2.e().c(aAVar2.d());
                    if ((aAVar2.d() != null && aAVar2.d().equals(str)) || (c0088buC != null && c0088buC.L() && a(c0088buC, str))) {
                        iF = aAVar2.f();
                        break;
                    }
                }
            } else {
                C0088bu c0088buC2 = r2.e().c(str);
                if (c0088buC2 != null && c0088buC2.aH() != null && !c0088buC2.aH().isEmpty()) {
                    iF = aAVar.f();
                }
            }
        }
        return iF;
    }

    public static List a(R r2) {
        ArrayList arrayList = new ArrayList();
        Iterator itB = r2.e().b();
        while (itB.hasNext()) {
            aA aAVar = (aA) itB.next();
            try {
                if (!aAVar.c() && C1007o.a(aAVar.i(), r2) && C1007o.a(aAVar.aH(), r2)) {
                    if (aAVar.b()) {
                        a(r2, aAVar.a(), arrayList);
                    } else if (aAVar.d() != null && !aAVar.d().isEmpty()) {
                        arrayList.add(aAVar);
                    }
                }
            } catch (V.g e2) {
            }
        }
        return arrayList;
    }

    private static void a(R r2, Iterator it, List list) {
        aA aAVar = null;
        while (it.hasNext()) {
            aA aAVar2 = (aA) it.next();
            try {
                if (!aAVar2.c() && C1007o.a(aAVar2.i(), r2)) {
                    r2.e().c(aAVar2.d());
                    if (aAVar2.b()) {
                        a(r2, aAVar2.a(), list);
                    } else {
                        boolean zA = true;
                        try {
                            zA = C1007o.a(aAVar2.aH(), r2);
                        } catch (Exception e2) {
                        }
                        if (zA) {
                            list.add(aAVar2);
                            aAVar = aAVar2;
                        }
                    }
                } else if (aAVar2.c() && aAVar != null && !aAVar.c() && it.hasNext()) {
                    aAVar = aAVar2;
                }
            } catch (V.g e3) {
            }
        }
    }
}
