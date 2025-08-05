package aP;

import java.util.List;

/* renamed from: aP.bc, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/bc.class */
class C0234bc extends Thread {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ A.j f3060a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ List f3061b;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ aV f3062c;

    C0234bc(aV aVVar, A.j jVar, List list) {
        this.f3062c = aVVar;
        this.f3060a = jVar;
        this.f3061b = list;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        this.f3060a.a(this.f3061b);
    }
}
