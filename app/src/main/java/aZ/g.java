package aZ;

import bH.C0995c;

/* loaded from: TunerStudioMS.jar:aZ/g.class */
class g {

    /* renamed from: a, reason: collision with root package name */
    int f4120a;

    /* renamed from: b, reason: collision with root package name */
    int f4121b;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ f f4122c;

    g(f fVar, int i2, int i3) {
        this.f4122c = fVar;
        this.f4120a = -1;
        this.f4121b = -1;
        this.f4120a = i2;
        this.f4121b = i3;
    }

    public int a(int[] iArr) {
        return C0995c.b(iArr, this.f4120a, this.f4121b, true, false);
    }
}
