package s;

import R.k;
import R.m;
import W.C0181g;
import bH.C;
import bH.aa;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.jar.Pack200;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.icepdf.core.util.PdfOps;
import r.C1798a;
import r.C1807j;

/* renamed from: s.g, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:s/g.class */
public class C1818g {

    /* renamed from: a, reason: collision with root package name */
    static C1821j f13525a = null;

    /* renamed from: b, reason: collision with root package name */
    static C1812a f13526b = null;

    /* renamed from: c, reason: collision with root package name */
    static Map f13527c = new HashMap();

    /* renamed from: d, reason: collision with root package name */
    static String f13528d = "userAliases.txt";

    /* renamed from: e, reason: collision with root package name */
    static aa f13529e = null;

    public static void a() {
        f13525a = new C1821j();
        C.a.a(f13525a);
    }

    public static void a(String str) throws V.a {
        Locale[] availableLocales = Locale.getAvailableLocales();
        for (int i2 = 0; i2 < availableLocales.length; i2++) {
            if (availableLocales[i2].getLanguage().equals(str)) {
                C.a.a().a(availableLocales[i2]);
                return;
            }
        }
        throw new V.a("Language not found for language code '" + str + PdfOps.SINGLE_QUOTE_TOKEN);
    }

    public static String b(String str) {
        if (str == null || str.trim().length() == 0) {
            return str;
        }
        try {
            String str2 = (String) f13527c.get(str);
            if (str2 != null) {
                return str2;
            }
            String strA = C.a.a().a("configFileText", str);
            if (strA == null || strA.equals("")) {
                C.d("Translation unavailable for \"" + str + PdfOps.DOUBLE_QUOTE__TOKEN);
            }
            return strA;
        } catch (C.b e2) {
            C.a(e2.getMessage(), e2, null);
            return str;
        }
    }

    public static String a(String str, String str2) {
        String strB = b(str);
        if (str == null || (str.equals(strB) && str2 != null)) {
            strB = str2;
        }
        return strB;
    }

    public static String c(String str) {
        return aE.a.A() != null ? C.a.a().b("configFileText", str) : str;
    }

    public static ArrayList b() {
        ArrayList arrayList = new ArrayList();
        File fileA = C1807j.a();
        arrayList.add(f("en"));
        if (!fileA.exists()) {
            return arrayList;
        }
        for (String str : fileA.list(new C1819h())) {
            arrayList.add(f(str));
        }
        return arrayList;
    }

    private static C1816e f(String str) {
        if (str.equals("en")) {
            return new C1816e(str, "English");
        }
        if (str.equals("he")) {
            return new C1816e("iw", "Hebrew");
        }
        Locale[] availableLocales = Locale.getAvailableLocales();
        for (int i2 = 0; i2 < availableLocales.length; i2++) {
            if (availableLocales[i2].getLanguage().equals(str)) {
                return new C1816e(str, availableLocales[i2].getDisplayLanguage());
            }
        }
        return new C1816e(str, "Unknown - " + str);
    }

    public static Locale c() {
        return C.a.a().b();
    }

    public static void b(String str, String str2) {
        Locale localeB = C.a.a().b();
        Locale localeC = C.a.a().c();
        f13525a.a("configFileText", localeB, str, str2);
        f13525a.a(localeB);
        try {
            C.c("Translation submittion finished: " + new m().a("lt401vette", Pack200.Packer.PASS, "TunerStudioMS", C1798a.a().c(C1798a.f13358aN, ""), C1798a.a().c(C1798a.cF, ""), localeC.getLanguage(), localeB.getLanguage(), str, str2));
        } catch (k e2) {
            Logger.getLogger(C1818g.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
    }

    public static void a(aE.a aVar) {
        f13527c.clear();
        File file = new File(aVar.w(), f13528d);
        if (file.exists()) {
            f13527c = new C0181g().a(f13527c, file);
        }
    }

    public static void b(aE.a aVar) {
        if (f13527c.isEmpty()) {
            return;
        }
        new C0181g().b(f13527c, new File(aVar.w(), f13528d));
    }

    public static void c(String str, String str2) {
        f13527c.put(str, str2);
    }

    public static void d(String str) {
        f13527c.remove(str);
    }

    public static boolean e(String str) {
        return f13527c.containsKey(str);
    }

    public static aa d() {
        if (f13529e == null) {
            f13529e = new C1820i();
        }
        return f13529e;
    }
}
