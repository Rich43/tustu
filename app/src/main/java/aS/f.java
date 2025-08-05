package aS;

import G.C0113cs;
import L.C0168y;
import java.io.File;

/* loaded from: TunerStudioMS.jar:aS/f.class */
public class f implements ac.f {

    /* renamed from: a, reason: collision with root package name */
    int f3910a = 0;

    /* renamed from: b, reason: collision with root package name */
    int f3911b = 0;

    /* renamed from: c, reason: collision with root package name */
    long f3912c = 0;

    public f() {
        C0113cs.a().d("dataLoggingActive");
        C0113cs.a().d("dataLogTime");
        d();
        C0113cs.a().d("dataLogRecordCount");
        C0113cs.a().a("dataLogRecordCount", 0.0d);
    }

    @Override // ac.f
    public void a(File file) {
        C0113cs.a().a("dataLoggingActive", 1.0d);
        C0113cs.a().a("dataLogTime", 0.0d);
        this.f3910a = 0;
    }

    @Override // ac.f
    public void c() {
        C0113cs c0113csA = C0113cs.a();
        int i2 = this.f3910a;
        this.f3910a = i2 + 1;
        c0113csA.a("dataLogRecordCount", i2);
        if (ac.h.s() == null) {
            C0113cs.a().a("dataLogTime", C0168y.c());
        }
    }

    @Override // ac.f
    public void d() {
        C0113cs.a().a("dataLoggingActive", 0.0d);
        this.f3912c = 0L;
    }

    @Override // ac.f
    public void b(String str) {
        C0113cs c0113csA = C0113cs.a();
        int i2 = this.f3911b;
        this.f3911b = i2 + 1;
        c0113csA.a("dataLogMarkCount", i2);
    }
}
