package bD;

import java.util.List;

/* loaded from: TunerStudioMS.jar:bD/A.class */
class A extends Thread {

    /* renamed from: a, reason: collision with root package name */
    List f6606a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ r f6607b;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    A(r rVar, List list) {
        super("Download");
        this.f6607b = rVar;
        this.f6606a = null;
        setDaemon(true);
        this.f6606a = list;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        try {
            this.f6607b.f6687a.c();
            this.f6607b.a(this.f6606a);
        } finally {
            this.f6607b.f6687a.d();
            this.f6607b.setEnabled(true);
        }
    }
}
