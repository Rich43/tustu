package bR;

import bH.C;
import bH.C0995c;

/* loaded from: TunerStudioMS.jar:bR/a.class */
public class a implements e {

    /* renamed from: a, reason: collision with root package name */
    private boolean f7498a = false;

    @Override // bR.e
    public synchronized void a(int i2, byte[] bArr) {
        if (i2 != 3 || bArr.length <= 0) {
            return;
        }
        if (bArr[0] == 0) {
            C.d("Burn Page successful");
            this.f7498a = true;
        } else if (bArr[0] == 1) {
            C.a("Burn Page failed during erase! " + C0995c.d(bArr));
        } else if (bArr[0] == 0) {
            C.a("Burn Page failed during programming: " + C0995c.d(bArr));
        } else {
            C.b("Unknown status code: " + ((int) bArr[0]) + ", " + C0995c.d(bArr));
        }
        notify();
    }

    public boolean a() {
        return this.f7498a;
    }
}
