package bL;

import G.aI;
import G.aM;
import bH.C;

/* loaded from: TunerStudioMS.jar:bL/f.class */
public class f extends k {

    /* renamed from: b, reason: collision with root package name */
    private double f7138b = Double.MAX_VALUE;

    /* renamed from: c, reason: collision with root package name */
    private double f7139c = Double.MIN_VALUE;

    /* renamed from: d, reason: collision with root package name */
    private long f7140d = 0;

    /* renamed from: e, reason: collision with root package name */
    private int f7141e = 500;

    public f() {
        d("O2 Out of Range");
        a(false);
    }

    @Override // bL.k
    public boolean a(aI aIVar, byte[] bArr) {
        boolean z2 = false;
        try {
            double dB = aIVar.g(b()).b(bArr);
            double dJ = 14.7d;
            aM aMVarC = aIVar.c("stoich");
            if (aMVarC != null) {
                dJ = aMVarC.j(aIVar.h());
            }
            if (dB < 2.0d) {
                dB *= dJ;
            }
            double d2 = dJ * 1.156d;
            if (dB > 0.843d * dJ && dB < d2) {
                if (dB <= this.f7138b) {
                    this.f7138b = dB;
                }
                if (dB < this.f7139c) {
                    return false;
                }
                this.f7139c = dB;
                return false;
            }
            if (dB < this.f7138b) {
                this.f7138b = dB;
                z2 = true;
            } else if (dB >= this.f7139c) {
                this.f7139c = dB;
                z2 = true;
            } else if (dB <= this.f7138b) {
                z2 = true;
            }
            if (z2) {
                this.f7140d = System.currentTimeMillis();
            }
            if (!z2) {
                z2 = System.currentTimeMillis() - ((long) this.f7141e) < this.f7140d;
            }
            if (z2) {
                this.f7155a++;
            }
            return z2;
        } catch (V.g e2) {
            C.b("unable to do simple compare for channels: " + b());
            return false;
        }
    }

    @Override // bL.k
    public String a() {
        return this.f7138b >= this.f7139c ? "Calibrating O2" : super.a();
    }
}
