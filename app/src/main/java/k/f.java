package K;

import G.C0116cv;
import G.R;
import G.dh;
import java.io.IOException;

/* loaded from: TunerStudioMS.jar:K/f.class */
public class f {

    /* renamed from: a, reason: collision with root package name */
    private C0116cv f1503a;

    /* renamed from: b, reason: collision with root package name */
    private R f1504b;

    /* renamed from: c, reason: collision with root package name */
    private int f1505c;

    /* renamed from: d, reason: collision with root package name */
    private dh f1506d;

    /* renamed from: e, reason: collision with root package name */
    private g f1507e = null;

    public f(R r2, int i2, dh dhVar) {
        this.f1504b = r2;
        this.f1505c = i2;
        this.f1506d = dhVar;
        this.f1503a = new C0116cv(r2);
    }

    public void a() {
        if (this.f1507e == null || !this.f1507e.isAlive()) {
            this.f1507e = new g(this);
            this.f1507e.start();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b() throws IOException {
        if (this.f1504b.W() && this.f1507e != null) {
            this.f1507e.a();
        } else {
            if (!this.f1504b.R() || this.f1504b.C().w() || this.f1504b.C().N() || this.f1504b.C().M() >= System.currentTimeMillis()) {
                return;
            }
            this.f1503a.a(this.f1505c);
        }
    }
}
