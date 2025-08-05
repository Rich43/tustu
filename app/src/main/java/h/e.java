package H;

import G.C0069bb;
import G.C0135r;
import G.R;
import G.T;
import G.Y;
import G.aH;
import G.aI;
import G.aM;
import java.io.Serializable;

/* loaded from: TunerStudioMS.jar:H/e.class */
public class e implements aI, Serializable {

    /* renamed from: a, reason: collision with root package name */
    private String f1336a;

    /* renamed from: b, reason: collision with root package name */
    private String f1337b;

    public e(R r2) {
        this.f1336a = r2.c();
        this.f1337b = r2.c() + "_Replay";
    }

    @Override // G.aI
    public aH g(String str) {
        C0069bb c0069bbH = ((R) a()).h(str);
        return c0069bbH != null ? c0069bbH : a().g(str);
    }

    protected aI a() {
        return T.a().c(this.f1336a);
    }

    @Override // G.aI
    public aM c(String str) {
        return a().c(str);
    }

    @Override // G.aI
    public C0135r d(String str) {
        return a().d(str);
    }

    @Override // G.aI
    public Y h() {
        return a().h();
    }

    @Override // G.aI, G.InterfaceC0110cp
    public String c() {
        return a().c();
    }

    @Override // G.aI
    public R K() {
        return a().K();
    }

    @Override // G.aI
    public boolean R() {
        return a().R();
    }

    @Override // G.aI
    public String ac() {
        return this.f1337b;
    }

    public void a(String str) {
        this.f1337b = str;
    }
}
