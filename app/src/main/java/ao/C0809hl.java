package ao;

import java.util.ArrayList;
import java.util.List;

/* renamed from: ao.hl, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/hl.class */
public class C0809hl {

    /* renamed from: a, reason: collision with root package name */
    static String f6089a = "^|^";

    /* renamed from: b, reason: collision with root package name */
    static String f6090b = ",";

    public static void a(C0810hm c0810hm) {
        if (c0810hm.c().equals(" ") && c0810hm.d().equals(" ") && c0810hm.e().equals(" ")) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(c0810hm.a()).append(f6089a);
        sb.append(c0810hm.b()).append(f6089a);
        sb.append(c0810hm.j()).append(f6089a);
        sb.append(c0810hm.k()).append(f6089a);
        sb.append(c0810hm.l()).append(f6089a);
        sb.append(c0810hm.c()).append(f6089a);
        sb.append(c0810hm.d()).append(f6089a);
        sb.append(c0810hm.e()).append(f6089a);
        sb.append(c0810hm.h()).append(f6089a);
        if (c0810hm.f() == null || c0810hm.g() == null) {
            throw new V.a("Can not save without X & Y Axis.");
        }
        for (int i2 = 0; i2 < c0810hm.f().length; i2++) {
            sb.append(c0810hm.f()[i2]);
            if (i2 < c0810hm.f().length - 1) {
                sb.append(f6090b);
            }
        }
        sb.append(f6089a);
        for (int i3 = 0; i3 < c0810hm.g().length; i3++) {
            sb.append(c0810hm.g()[i3]);
            if (i3 < c0810hm.g().length - 1) {
                sb.append(f6090b);
            }
        }
        sb.append(f6089a);
        sb.append(c0810hm.m());
        sb.append(f6089a);
        sb.append(c0810hm.n());
        sb.append(f6089a);
        sb.append(c0810hm.o());
        sb.append(f6089a);
        sb.append(c0810hm.p());
        sb.append(f6089a);
        sb.append(c0810hm.q());
        sb.append(f6089a);
        sb.append(c0810hm.r());
        h.i.c("TABLE_GEN_VIEW_" + c0810hm.i(), sb.toString());
    }

    public static C0810hm a(String str) {
        String strF = h.i.f("TABLE_GEN_VIEW_" + str, null);
        if (strF == null) {
            return null;
        }
        C0810hm c0810hm = new C0810hm();
        try {
            ak.aD aDVar = new ak.aD(strF, f6089a);
            c0810hm.a(Integer.parseInt(aDVar.b()));
            c0810hm.b(Integer.parseInt(aDVar.b()));
            c0810hm.c(Integer.parseInt(aDVar.b()));
            c0810hm.d(Integer.parseInt(aDVar.b()));
            c0810hm.e(Integer.parseInt(aDVar.b()));
            c0810hm.a(aDVar.b());
            c0810hm.b(aDVar.b());
            c0810hm.c(aDVar.b());
            c0810hm.d(aDVar.b());
            c0810hm.a(bH.W.c(aDVar.b(), f6090b));
            String[] strArrC = bH.W.c(aDVar.b(), f6090b);
            String[] strArr = new String[strArrC.length];
            for (int i2 = 0; i2 < strArr.length; i2++) {
                strArr[i2] = strArrC[(strArrC.length - 1) - i2];
            }
            c0810hm.b(strArrC);
            if (aDVar.a()) {
                c0810hm.f(aDVar.b());
            }
            if (aDVar.a()) {
                try {
                    c0810hm.f(Integer.parseInt(aDVar.b()));
                } catch (Exception e2) {
                    bH.C.c("Failed to parse Color Mode");
                    e2.printStackTrace();
                }
                try {
                    c0810hm.g(Integer.parseInt(aDVar.b()));
                } catch (Exception e3) {
                    bH.C.c("Failed to parse Display Mode");
                    e3.printStackTrace();
                }
                c0810hm.g(aDVar.b());
                try {
                    c0810hm.h(Integer.parseInt(aDVar.b()));
                } catch (Exception e4) {
                    bH.C.c("Failed to parse Min Window Weight");
                    e4.printStackTrace();
                }
                try {
                    c0810hm.i(Integer.parseInt(aDVar.b()));
                } catch (Exception e5) {
                    bH.C.c("Failed to parse Min Total Weight");
                    e5.printStackTrace();
                }
            }
            return c0810hm;
        } catch (Exception e6) {
            e6.printStackTrace();
            throw new V.a("Unable to load Histogram View: " + str);
        }
    }

    public static List a() {
        String[] strArrF = h.i.f("TABLE_GEN_VIEW_");
        ArrayList arrayList = new ArrayList();
        for (String str : bH.W.a(strArrF)) {
            arrayList.add(bH.W.b(str, "TABLE_GEN_VIEW_", ""));
        }
        return arrayList;
    }

    public static void b(String str) {
        h.i.d("TABLE_GEN_VIEW_" + str);
    }
}
