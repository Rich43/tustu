package aZ;

import bH.C0995c;

/* loaded from: TunerStudioMS.jar:aZ/k.class */
class k {

    /* renamed from: a, reason: collision with root package name */
    int f4159a;

    /* renamed from: b, reason: collision with root package name */
    int f4160b;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ j f4161c;

    k(j jVar, int i2, int i3) {
        this.f4161c = jVar;
        this.f4159a = -1;
        this.f4160b = -1;
        this.f4159a = i2;
        this.f4160b = i3;
    }

    public int a(int[] iArr) {
        return C0995c.b(iArr, this.f4159a, this.f4160b, true, false);
    }
}
