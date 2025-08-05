package s;

import W.C0181g;
import bH.C;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import r.C1807j;

/* renamed from: s.j, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:s/j.class */
public class C1821j implements C.e {

    /* renamed from: a, reason: collision with root package name */
    Locale f13530a = null;

    /* renamed from: b, reason: collision with root package name */
    String f13531b = "staticText";

    /* renamed from: c, reason: collision with root package name */
    Map f13532c = new HashMap();

    /* renamed from: d, reason: collision with root package name */
    Map f13533d = new HashMap();

    @Override // C.e
    public Map a(String str, Locale locale) throws C.b {
        if (locale.getLanguage().equals("en")) {
            return null;
        }
        File fileC = C1807j.c(locale);
        C0181g c0181g = new C0181g();
        try {
            Map mapA = c0181g.a(fileC);
            File fileD = C1807j.d(locale);
            if (fileD.exists()) {
                try {
                    Map mapA2 = c0181g.a(fileD);
                    this.f13532c.put(locale, mapA2);
                    a(mapA, mapA2);
                    mapA.putAll(mapA2);
                } catch (FileNotFoundException e2) {
                    C.b("Content File not found. \n" + fileD.getAbsolutePath());
                } catch (IOException e3) {
                    C.a("Error loading content file. \n" + fileD.getAbsolutePath());
                    e3.printStackTrace();
                }
            } else {
                this.f13532c.remove(locale);
            }
            this.f13533d.put(locale, mapA);
            return mapA;
        } catch (FileNotFoundException e4) {
            throw new C.b("Content File not found. \n" + fileC.getAbsolutePath());
        } catch (IOException e5) {
            throw new C.b("Error loading content file. \n" + fileC.getAbsolutePath());
        }
    }

    public void a(String str, Locale locale, String str2, String str3) {
        if (locale.getLanguage().equals("en")) {
            C.c("C'mon, why are you updating english?");
        } else {
            b(locale).put(str2, str3);
            c(locale).put(str2, str3);
        }
    }

    public void a(Locale locale) {
        File fileD = C1807j.d(locale);
        try {
            new C0181g().b(b(locale), fileD);
        } catch (IOException e2) {
            C.a(C1818g.b("Unable to save translation updates to local disk."), e2, null);
        }
    }

    private Map b(Locale locale) {
        Map map = (Map) this.f13532c.get(locale);
        if (map == null) {
            map = new HashMap();
            this.f13532c.put(locale, map);
        }
        return map;
    }

    private Map c(Locale locale) {
        Map map = (Map) this.f13533d.get(locale);
        if (map == null) {
            map = new HashMap();
            this.f13533d.put(locale, map);
        }
        return map;
    }

    private void a(Map map, Map map2) {
        Set setKeySet = map2.keySet();
        String[] strArr = (String[]) setKeySet.toArray(new String[setKeySet.size()]);
        int i2 = 0;
        while (i2 < strArr.length) {
            String str = strArr[i2];
            String str2 = (String) map.get(str);
            String str3 = (String) map2.get(str);
            if (str2 == null || str3 == null || !str2.equals(str3)) {
                i2++;
            } else {
                map2.remove(str);
            }
        }
    }
}
