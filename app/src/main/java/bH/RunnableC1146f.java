package bh;

import java.io.File;

/* renamed from: bh.f, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bh/f.class */
class RunnableC1146f implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1142b f8108a;

    RunnableC1146f(C1142b c1142b) {
        this.f8108a = c1142b;
    }

    @Override // java.lang.Runnable
    public void run() {
        String[] strArr = new String[1];
        String strB = ac.r.b();
        if (strB != null) {
            this.f8108a.f8100o = new File(strB);
            strArr[0] = this.f8108a.f8100o.getAbsolutePath();
            this.f8108a.a(strArr, true);
        }
    }
}
