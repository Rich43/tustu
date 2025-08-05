package ac;

import G.aG;
import G.bS;

/* loaded from: TunerStudioMS.jar:ac/g.class */
public class g implements aG {

    /* renamed from: a, reason: collision with root package name */
    int f4194a = 500;

    /* renamed from: b, reason: collision with root package name */
    long f4195b = 0;

    /* renamed from: c, reason: collision with root package name */
    long f4196c = 0;

    @Override // G.aG
    public boolean a(String str, bS bSVar) {
        h hVarK = h.k();
        if (hVarK == null || !hVarK.u() || System.currentTimeMillis() - this.f4195b <= this.f4194a) {
            return true;
        }
        this.f4195b = System.currentTimeMillis();
        hVarK.d("Going Online: " + str);
        return true;
    }

    @Override // G.aG
    public void a(String str) {
        h hVarK = h.k();
        if (hVarK == null || !hVarK.u() || System.currentTimeMillis() - this.f4196c <= this.f4194a) {
            return;
        }
        this.f4196c = System.currentTimeMillis();
        hVarK.d("Going Offline: " + str);
    }
}
