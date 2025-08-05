package W;

import G.C0135r;
import G.C0136s;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/* renamed from: W.v, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:W/v.class */
public class C0196v {

    /* renamed from: a, reason: collision with root package name */
    private static C0196v f2184a = null;

    /* renamed from: b, reason: collision with root package name */
    private C0136s[] f2185b = null;

    /* renamed from: c, reason: collision with root package name */
    private ai f2186c = null;

    private C0196v() {
    }

    public static C0196v a(ai aiVar) {
        if (f2184a == null) {
            f2184a = new C0196v();
            f2184a.f2186c = aiVar;
        }
        return f2184a;
    }

    public static C0196v a() {
        if (f2184a == null) {
            f2184a = new C0196v();
        }
        return f2184a;
    }

    public C0136s[] a(String str) throws V.a {
        I i2 = new I();
        try {
            ArrayList arrayListA = i2.a(new C0172ab().a(str));
            ArrayList arrayListA2 = i2.a(str);
            C0136s[] c0136sArr = new C0136s[arrayListA.size() + arrayListA2.size()];
            for (int i3 = 0; i3 < arrayListA.size(); i3++) {
                c0136sArr[i3] = (C0136s) arrayListA.get(i3);
            }
            for (int i4 = 0; i4 < arrayListA2.size(); i4++) {
                C0136s c0136s = new C0136s();
                H h2 = (H) arrayListA2.get(i4);
                c0136s.b(h2.a());
                c0136s.c(h2.a());
                C0135r c0135r = new C0135r();
                c0135r.a("Activated");
                c0135r.v(h2.a());
                c0135r.a(h2.b());
                c0136s.a(c0135r);
                C0135r c0135r2 = new C0135r();
                c0135r2.a("Deactivated");
                c0135r2.v(h2.a() + "_OFF");
                c0135r2.a(!h2.b());
                c0136s.a(c0135r2);
                c0136sArr[arrayListA.size() + i4] = c0136s;
            }
            return c0136sArr;
        } catch (V.g e2) {
            String str2 = "Error in [SettingGroups]\nReported Error:\n" + e2.getMessage();
            bH.C.a(str2);
            e2.printStackTrace();
            throw new V.a(str2);
        } catch (FileNotFoundException e3) {
            e3.printStackTrace();
            throw new V.a("Ini File\n" + str + "\n not found");
        } catch (IOException e4) {
            e4.printStackTrace();
            throw new V.a("Error reading options in ini file:\n" + str);
        }
    }

    public C0136s[] b(String str) {
        c();
        C0136s[] c0136sArrB = b();
        if (str != null && (str.indexOf(".ini") != -1 || str.indexOf(".ecu") != -1)) {
            C0136s[] c0136sArrA = a(str);
            ArrayList arrayList = new ArrayList();
            for (C0136s c0136s : c0136sArrB) {
                arrayList.add(c0136s);
            }
            for (int i2 = 0; i2 < c0136sArrA.length; i2++) {
                if (arrayList.contains(c0136sArrA[i2])) {
                    arrayList.set(arrayList.indexOf(c0136sArrA[i2]), c0136sArrA[i2]);
                } else {
                    arrayList.add(c0136sArrA[i2]);
                }
            }
            c0136sArrB = (C0136s[]) arrayList.toArray(new C0136s[arrayList.size()]);
        }
        return c0136sArrB;
    }

    public C0136s[] b() {
        if (this.f2185b == null) {
            c();
        }
        return this.f2185b;
    }

    public C0135r c(String str) {
        if (this.f2185b == null) {
            c();
        }
        for (int i2 = 0; i2 < this.f2185b.length; i2++) {
            if (this.f2185b[i2].a(str) != null) {
                return this.f2185b[i2].a(str);
            }
        }
        return null;
    }

    private C0136s[] c() {
        this.f2185b = this.f2186c.a();
        return this.f2185b;
    }

    public C0136s[] a(File[] fileArr) {
        this.f2185b = c();
        for (int i2 = 0; i2 < fileArr.length && fileArr[i2] != null && fileArr[i2].exists(); i2++) {
            this.f2185b = new I().a(this.f2185b, fileArr[i2].getAbsolutePath());
        }
        return this.f2185b;
    }
}
