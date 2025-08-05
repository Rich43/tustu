package ak;

import bH.C0995c;

/* loaded from: TunerStudioMS.jar:ak/ap.class */
class ap implements ar {

    /* renamed from: a, reason: collision with root package name */
    int f4794a;

    /* renamed from: b, reason: collision with root package name */
    int f4795b;

    /* renamed from: c, reason: collision with root package name */
    boolean f4796c;

    /* renamed from: d, reason: collision with root package name */
    boolean f4797d;

    /* renamed from: e, reason: collision with root package name */
    final /* synthetic */ an f4798e;

    public ap(an anVar, int i2, int i3, boolean z2, boolean z3) {
        this.f4798e = anVar;
        this.f4794a = i2;
        this.f4795b = i3;
        this.f4796c = z2;
        this.f4797d = z3;
    }

    @Override // ak.ar
    public float a(byte[] bArr) {
        int iA = C0995c.a(bArr, this.f4794a, this.f4795b, true, this.f4797d);
        return this.f4796c ? Float.intBitsToFloat(iA) : iA;
    }
}
