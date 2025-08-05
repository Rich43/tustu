package an;

import am.C0574b;
import am.C0576d;
import java.io.IOException;

/* renamed from: an.d, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:an/d.class */
public class C0581d extends AbstractC0578a {

    /* renamed from: d, reason: collision with root package name */
    double f5056d;

    /* renamed from: e, reason: collision with root package name */
    double f5057e;

    /* renamed from: f, reason: collision with root package name */
    double f5058f;

    /* renamed from: g, reason: collision with root package name */
    double f5059g;

    /* renamed from: h, reason: collision with root package name */
    double f5060h;

    /* renamed from: i, reason: collision with root package name */
    double f5061i;

    C0581d(C0576d c0576d, C0574b c0574b) throws IOException {
        super(c0576d, c0574b);
        if (c0574b == null) {
            throw new IOException("CCBLOCK Cannot be null for Rational Convertion");
        }
        double[] dArrH = c0574b.h();
        if (dArrH.length < 6) {
            throw new IOException("CCBLOCK parameter count should be 6 for Rational Convertion");
        }
        this.f5056d = dArrH[0];
        this.f5057e = dArrH[1];
        this.f5058f = dArrH[2];
        this.f5059g = dArrH[3];
        this.f5060h = dArrH[4];
        this.f5061i = dArrH[5];
    }

    @Override // an.AbstractC0578a
    public double a(byte[] bArr) {
        double dB = b(bArr);
        return ((((this.f5056d * dB) * dB) + (this.f5057e * dB)) + this.f5058f) / ((((this.f5059g * dB) * dB) + (this.f5060h * dB)) + this.f5061i);
    }

    @Override // an.AbstractC0578a
    public double a(double d2) {
        return ((((this.f5056d * d2) * d2) + (this.f5057e * d2)) + this.f5058f) / ((((this.f5059g * d2) * d2) + (this.f5060h * d2)) + this.f5061i);
    }
}
