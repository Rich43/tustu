package W;

import java.io.File;

/* renamed from: W.x, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:W/x.class */
class C0198x extends Thread {

    /* renamed from: a, reason: collision with root package name */
    String f2187a;

    /* renamed from: b, reason: collision with root package name */
    double f2188b;

    /* renamed from: c, reason: collision with root package name */
    File f2189c;

    public C0198x(String str, double d2, File file) {
        this.f2187a = str;
        this.f2188b = d2;
        this.f2189c = file;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        R rA = C0197w.a(this.f2187a, this.f2188b);
        if (rA.a()) {
            bH.C.d("Newer ECU Def found for signature: " + this.f2187a + ", iniVersion: " + rA.e() + ". downloading it now..");
            R rA2 = C0197w.a(this.f2187a, this.f2189c);
            if (rA2.a()) {
                bH.C.d("downloaded ECU Definition to: " + rA2.c().getAbsolutePath());
            } else {
                bH.C.d(rA2.d());
            }
        }
    }
}
