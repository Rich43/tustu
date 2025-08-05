package an;

import al.InterfaceC0569c;
import am.C0574b;
import am.C0576d;
import bH.C;
import bH.C0995c;
import java.util.List;

/* renamed from: an.a, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:an/a.class */
public abstract class AbstractC0578a implements InterfaceC0569c {

    /* renamed from: a, reason: collision with root package name */
    protected C0576d f5048a;

    /* renamed from: b, reason: collision with root package name */
    protected C0574b f5049b;

    /* renamed from: c, reason: collision with root package name */
    boolean f5050c;

    /* renamed from: d, reason: collision with root package name */
    private List f5051d = null;

    /* renamed from: e, reason: collision with root package name */
    private int f5052e;

    /* renamed from: f, reason: collision with root package name */
    private int f5053f;

    AbstractC0578a(C0576d c0576d, C0574b c0574b) {
        this.f5048a = null;
        this.f5050c = false;
        this.f5048a = c0576d;
        this.f5049b = c0574b;
        this.f5050c = a(c0576d.e());
        this.f5052e = (int) (c0576d.h() / 8);
        this.f5053f = (int) c0576d.g();
    }

    public abstract double a(byte[] bArr);

    public static AbstractC0578a a(C0576d c0576d) {
        C0574b c0574bP = c0576d.p();
        if (c0574bP == null) {
            return new C0580c(c0576d, c0574bP);
        }
        switch (c0574bP.e()) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
            case 4:
            case 5:
            case 6:
            default:
                C.b("Unsupported Conversion: " + ((int) c0574bP.e()));
                break;
            case 7:
                break;
        }
        return new C0580c(c0576d, c0574bP);
    }

    public double a(double d2) {
        return d2;
    }

    public double b(byte[] bArr) {
        double dIntBitsToFloat;
        switch (this.f5048a.e()) {
            case 0:
                dIntBitsToFloat = this.f5052e > 4 ? C0995c.b(bArr, this.f5053f, this.f5052e, this.f5050c, false) : C0995c.a(bArr, this.f5053f, this.f5052e, this.f5050c, false);
                break;
            case 1:
                dIntBitsToFloat = this.f5052e > 4 ? C0995c.b(bArr, this.f5053f, this.f5052e, this.f5050c, false) : C0995c.a(bArr, this.f5053f, this.f5052e, this.f5050c, false);
                break;
            case 2:
                dIntBitsToFloat = this.f5052e > 4 ? C0995c.b(bArr, this.f5053f, this.f5052e, this.f5050c, true) : C0995c.a(bArr, this.f5053f, this.f5052e, this.f5050c, true);
                break;
            case 3:
                dIntBitsToFloat = this.f5052e > 4 ? C0995c.b(bArr, this.f5053f, this.f5052e, this.f5050c, true) : C0995c.a(bArr, this.f5053f, this.f5052e, this.f5050c, true);
                break;
            case 4:
                dIntBitsToFloat = this.f5052e == 4 ? Float.intBitsToFloat(C0995c.a(bArr, (int) this.f5048a.g(), this.f5052e, this.f5050c, false)) : Double.longBitsToDouble(C0995c.b(bArr, (int) this.f5048a.g(), this.f5052e, this.f5050c, false));
                break;
            case 5:
                dIntBitsToFloat = this.f5052e == 4 ? Float.intBitsToFloat(C0995c.a(bArr, (int) this.f5048a.g(), this.f5052e, this.f5050c, false)) : Double.longBitsToDouble(C0995c.b(bArr, (int) this.f5048a.g(), this.f5052e, this.f5050c, false));
                break;
            default:
                dIntBitsToFloat = Double.NaN;
                break;
        }
        return dIntBitsToFloat;
    }

    public List a() {
        return this.f5051d;
    }

    public void a(List list) {
        this.f5051d = list;
    }
}
