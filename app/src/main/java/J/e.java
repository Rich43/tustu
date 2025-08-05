package J;

import bH.C0995c;

/* loaded from: TunerStudioMS.jar:J/e.class */
class e {

    /* renamed from: a, reason: collision with root package name */
    int f1424a = Integer.MAX_VALUE;

    /* renamed from: b, reason: collision with root package name */
    int f1425b = Integer.MAX_VALUE;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ d f1426c;

    e(d dVar) {
        this.f1426c = dVar;
    }

    void a(int[] iArr) {
        this.f1424a = C0995c.b(iArr, 1, 2, true, false);
        this.f1425b = C0995c.b(iArr, 3, 2, true, false);
    }

    public String toString() {
        return "blockingFactor: " + this.f1424a + ", tableBlockingFactor: " + this.f1425b;
    }
}
