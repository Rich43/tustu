package y;

import G.R;
import G.aT;
import java.util.ArrayList;

/* renamed from: y.a, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:y/a.class */
public class C1893a {

    /* renamed from: b, reason: collision with root package name */
    private static C1893a f14032b = null;

    /* renamed from: a, reason: collision with root package name */
    aT f14033a;

    private C1893a(aT aTVar) {
        this.f14033a = null;
        this.f14033a = aTVar;
    }

    public static C1893a a(aT aTVar) {
        if (f14032b == null) {
            f14032b = new C1893a(aTVar);
        }
        return f14032b;
    }

    public ArrayList a(R r2) {
        if (r2.c("inj_trimd") == null || r2.c("spk_trimb") == null || r2.c("log_style_block") == null || r2.c("logFieldOffset") == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add(new C1894b(r2, this.f14033a));
        return arrayList;
    }
}
