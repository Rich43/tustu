package bD;

import java.util.List;

/* loaded from: TunerStudioMS.jar:bD/z.class */
class z extends Thread {

    /* renamed from: a, reason: collision with root package name */
    List f6705a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ r f6706b;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    z(r rVar, List list) {
        super("Delete");
        this.f6706b = rVar;
        this.f6705a = null;
        setDaemon(true);
        this.f6705a = list;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        try {
            this.f6706b.f6687a.c();
            this.f6706b.b(this.f6705a);
        } finally {
            this.f6706b.f6687a.d();
            this.f6706b.setEnabled(true);
        }
    }
}
