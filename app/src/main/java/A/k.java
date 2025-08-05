package A;

import java.util.List;

/* loaded from: TunerStudioMS.jar:A/k.class */
class k extends Thread {

    /* renamed from: a, reason: collision with root package name */
    List f32a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ j f33b;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    k(j jVar, List list) {
        super("DeviceSearch");
        this.f33b = jVar;
        this.f32a = list;
        setDaemon(true);
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        this.f33b.b(this.f32a);
    }
}
