package an;

import am.C0574b;
import am.C0576d;
import bH.C;
import java.io.IOException;

/* renamed from: an.b, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:an/b.class */
public class C0579b extends AbstractC0578a {

    /* renamed from: d, reason: collision with root package name */
    double f5054d;

    /* renamed from: e, reason: collision with root package name */
    double f5055e;

    C0579b(C0576d c0576d, C0574b c0574b) throws IOException {
        super(c0576d, c0574b);
        if (c0574b == null) {
            throw new IOException("CCBLOCK Cannot be null for Linear Convertion");
        }
        double[] dArrH = c0574b.h();
        if (dArrH.length > 0) {
            this.f5055e = dArrH[0];
        } else {
            C.b("LinearConversion no shift, using default 0.0");
            this.f5055e = 0.0d;
        }
        if (dArrH.length > 1) {
            this.f5054d = dArrH[1];
        } else {
            C.b("LinearConversion no vals, using defaul 1.0");
            this.f5054d = 1.0d;
        }
    }

    @Override // an.AbstractC0578a
    public double a(byte[] bArr) {
        return (b(bArr) * this.f5054d) + this.f5055e;
    }
}
