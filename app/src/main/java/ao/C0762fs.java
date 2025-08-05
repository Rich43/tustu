package ao;

import java.util.ArrayList;
import java.util.List;

/* renamed from: ao.fs, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/fs.class */
public class C0762fs {

    /* renamed from: a, reason: collision with root package name */
    static String f5837a = "^|^";

    /* renamed from: b, reason: collision with root package name */
    static String f5838b = ",";

    public static void a(C0761fr c0761fr) {
        StringBuilder sb = new StringBuilder();
        sb.append(c0761fr.b()).append(f5837a);
        sb.append(c0761fr.e()).append(f5837a);
        sb.append(c0761fr.f()).append(f5837a);
        sb.append(c0761fr.g()).append(f5837a);
        sb.append(c0761fr.c()).append(f5837a);
        sb.append(c0761fr.h()).append(f5837a);
        sb.append(c0761fr.i()).append(f5837a);
        sb.append(c0761fr.j()).append(f5837a);
        sb.append(c0761fr.d()).append(f5837a);
        sb.append(c0761fr.k()).append(f5837a);
        sb.append(c0761fr.l()).append(f5837a);
        sb.append(c0761fr.m()).append(f5837a);
        h.i.c("SCATTER_PLOT_VIEW_" + c0761fr.a(), sb.toString());
    }

    public static C0761fr a(String str) {
        String strF = h.i.f("SCATTER_PLOT_VIEW_" + str, null);
        if (strF == null) {
            return null;
        }
        C0761fr c0761fr = new C0761fr(str);
        try {
            ak.aD aDVar = new ak.aD(strF, f5837a);
            if (aDVar.a()) {
                c0761fr.b(aDVar.b());
            }
            if (aDVar.a()) {
                c0761fr.a(Double.parseDouble(aDVar.b()));
            }
            if (aDVar.a()) {
                c0761fr.b(Double.parseDouble(aDVar.b()));
            }
            if (aDVar.a()) {
                c0761fr.a(Boolean.valueOf(aDVar.b()).booleanValue());
            }
            if (aDVar.a()) {
                c0761fr.c(aDVar.b());
            }
            if (aDVar.a()) {
                c0761fr.c(Double.parseDouble(aDVar.b()));
            }
            if (aDVar.a()) {
                c0761fr.d(Double.parseDouble(aDVar.b()));
            }
            if (aDVar.a()) {
                c0761fr.b(Boolean.valueOf(aDVar.b()).booleanValue());
            }
            if (aDVar.a()) {
                c0761fr.d(aDVar.b());
            }
            if (aDVar.a()) {
                c0761fr.e(Double.parseDouble(aDVar.b()));
            }
            if (aDVar.a()) {
                c0761fr.f(Double.parseDouble(aDVar.b()));
            }
            if (aDVar.a()) {
                c0761fr.c(Boolean.valueOf(aDVar.b()).booleanValue());
            }
            return c0761fr;
        } catch (Exception e2) {
            e2.printStackTrace();
            throw new V.a("Unable to load Histogram View: " + str);
        }
    }

    public static List a() {
        String[] strArrF = h.i.f("SCATTER_PLOT_VIEW_");
        ArrayList arrayList = new ArrayList();
        for (String str : bH.W.a(strArrF)) {
            arrayList.add(bH.W.b(str, "SCATTER_PLOT_VIEW_", ""));
        }
        return arrayList;
    }

    public static void b(String str) {
        h.i.d("SCATTER_PLOT_VIEW_" + str);
    }
}
