package af;

import ad.C0496d;

/* loaded from: TunerStudioMS.jar:af/g.class */
class g implements o {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ boolean f4475a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ h f4476b;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ f f4477c;

    g(f fVar, boolean z2, h hVar) {
        this.f4477c = fVar;
        this.f4475a = z2;
        this.f4476b = hVar;
    }

    @Override // af.o
    public boolean a(C0496d c0496d) {
        if (this.f4475a && (c0496d.d() & 16711680) == 1048576) {
            return !this.f4476b.a((c0496d.d() & 65535) / 1024);
        }
        return true;
    }
}
