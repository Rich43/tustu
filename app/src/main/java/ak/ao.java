package ak;

import bH.C0995c;

/* loaded from: TunerStudioMS.jar:ak/ao.class */
class ao implements ar {

    /* renamed from: a, reason: collision with root package name */
    int f4790a;

    /* renamed from: b, reason: collision with root package name */
    int f4791b;

    /* renamed from: c, reason: collision with root package name */
    int f4792c;

    /* renamed from: d, reason: collision with root package name */
    final /* synthetic */ an f4793d;

    public ao(an anVar, int i2, int i3, int i4) {
        this.f4793d = anVar;
        this.f4790a = i2;
        this.f4791b = i3;
        this.f4792c = 1 << i4;
    }

    @Override // ak.ar
    public float a(byte[] bArr) {
        return (C0995c.a(bArr, this.f4790a, this.f4791b, true, true) & this.f4792c) != 0 ? 1.0f : 0.0f;
    }
}
