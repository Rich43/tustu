package ak;

import W.InterfaceC0185k;
import bH.C0995c;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Objects;
import javafx.fxml.FXMLLoader;

/* renamed from: ak.ae, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ak/ae.class */
class C0536ae extends C0541aj implements W.T {

    /* renamed from: a, reason: collision with root package name */
    protected int f4703a;

    /* renamed from: b, reason: collision with root package name */
    protected int f4704b;

    /* renamed from: c, reason: collision with root package name */
    protected int f4705c;

    /* renamed from: d, reason: collision with root package name */
    protected boolean f4706d;

    /* renamed from: e, reason: collision with root package name */
    protected String f4707e;

    /* renamed from: f, reason: collision with root package name */
    protected int f4708f;

    /* renamed from: g, reason: collision with root package name */
    protected int f4709g;

    /* renamed from: h, reason: collision with root package name */
    protected int f4710h;

    /* renamed from: i, reason: collision with root package name */
    protected double f4711i;

    /* renamed from: j, reason: collision with root package name */
    protected int f4712j;

    /* renamed from: k, reason: collision with root package name */
    protected int f4713k;

    /* renamed from: l, reason: collision with root package name */
    public C0537af f4714l;

    /* renamed from: m, reason: collision with root package name */
    protected al f4715m;

    /* renamed from: n, reason: collision with root package name */
    protected al f4716n;

    /* renamed from: o, reason: collision with root package name */
    protected al f4717o;

    /* renamed from: p, reason: collision with root package name */
    int f4718p;

    /* renamed from: q, reason: collision with root package name */
    double f4719q;

    /* renamed from: r, reason: collision with root package name */
    double f4720r;

    /* renamed from: s, reason: collision with root package name */
    final /* synthetic */ C0535ad f4721s;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public C0536ae(C0535ad c0535ad, int i2) {
        super(c0535ad.f4702m.f4689f.f4683o, i2);
        this.f4721s = c0535ad;
        this.f4718p = 0;
        this.f4719q = Double.NaN;
        this.f4720r = Double.NaN;
        this.f4714l = new C0537af(this, this.f4704b);
        if (this.f4705c != 0) {
            this.f4715m = new al(c0535ad.f4702m.f4689f.f4683o, this.f4705c);
        }
        if (this.f4712j != 0) {
            this.f4716n = new al(c0535ad.f4702m.f4689f.f4683o, this.f4712j);
        }
        if (this.f4713k != 0) {
            this.f4717o = new al(c0535ad.f4702m.f4689f.f4683o, this.f4713k);
        }
    }

    @Override // ak.C0541aj
    public String toString() {
        return "CNBlock{name=" + String.format("%-30s", '\"' + this.f4707e + '\"') + ", unit=" + this.f4714l.f4722a + ", sr=" + this.f4711i + ", dt=" + Z.f4653s[this.f4710h] + ", cnvT=" + this.f4714l.f4723b + ", sizeInfo=" + this.f4714l.f4724c + ", rParams=" + (this.f4714l.f4725d != null ? Arrays.toString(this.f4714l.f4725d) : FXMLLoader.NULL_KEYWORD) + ", sParams=" + (this.f4714l.f4726e != null ? Arrays.toString(this.f4714l.f4726e) : FXMLLoader.NULL_KEYWORD) + ", offsetBits=" + this.f4708f + ", signalBits=" + this.f4709g + '}';
    }

    @Override // W.T
    public String a() {
        return this.f4707e;
    }

    @Override // W.T
    public String b() {
        return this.f4714l.f4722a;
    }

    @Override // W.T
    public int e() {
        return -1;
    }

    @Override // W.T
    public int f() {
        return this.f4714l.f4723b == 11 ? 255 : 0;
    }

    @Override // W.T
    public float d() {
        return Float.NaN;
    }

    @Override // W.T
    public float c() {
        return Float.NaN;
    }

    int k() {
        return this.f4721s.f4699j + this.f4718p;
    }

    double a(int i2) {
        double dB;
        int i3 = this.f4721s.f4702m.f4686c + (this.f4708f / 8) + (i2 * this.f4721s.f4695f);
        int i4 = this.f4709g / 8;
        try {
            switch (this.f4710h) {
                case 0:
                case 1:
                    dB = i4 > 4 ? C0995c.b(this.f4721s.f4702m.f4689f.f4683o.f4639e, i3, i4, this.f4721s.f4702m.f4689f.f4683o.f4645k, Z.f4654t[this.f4710h]) : C0995c.a(this.f4721s.f4702m.f4689f.f4683o.f4639e, i3, i4, this.f4721s.f4702m.f4689f.f4683o.f4645k, Z.f4654t[this.f4710h]);
                    break;
                case 2:
                    dB = ByteBuffer.wrap(this.f4721s.f4702m.f4689f.f4683o.f4639e, i3, 4).order(this.f4721s.f4702m.f4689f.f4683o.f4645k ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN).getFloat();
                    break;
                case 3:
                    dB = ByteBuffer.wrap(this.f4721s.f4702m.f4689f.f4683o.f4639e, i3, 8).order(this.f4721s.f4702m.f4689f.f4683o.f4645k ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN).getDouble();
                    break;
                case 4:
                case 5:
                case 6:
                default:
                    throw new IllegalArgumentException("Channel data type " + Z.f4653s[this.f4710h] + " is not supported");
                case 7:
                case 8:
                    dB = Double.NaN;
                    break;
                case 9:
                case 10:
                    dB = i4 > 4 ? C0995c.b(this.f4721s.f4702m.f4689f.f4683o.f4639e, i3, i4, true, Z.f4654t[this.f4710h]) : C0995c.a(this.f4721s.f4702m.f4689f.f4683o.f4639e, i3, i4, true, Z.f4654t[this.f4710h]);
                    break;
                case 11:
                    dB = ByteBuffer.wrap(this.f4721s.f4702m.f4689f.f4683o.f4639e, i3, 4).order(ByteOrder.BIG_ENDIAN).getFloat();
                    break;
                case 12:
                    dB = ByteBuffer.wrap(this.f4721s.f4702m.f4689f.f4683o.f4639e, i3, 8).order(ByteOrder.BIG_ENDIAN).getDouble();
                    break;
                case 13:
                case 14:
                    dB = i4 > 4 ? C0995c.b(this.f4721s.f4702m.f4689f.f4683o.f4639e, i3, i4, false, Z.f4654t[this.f4710h]) : C0995c.a(this.f4721s.f4702m.f4689f.f4683o.f4639e, i3, i4, false, Z.f4654t[this.f4710h]);
                    break;
                case 15:
                    dB = ByteBuffer.wrap(this.f4721s.f4702m.f4689f.f4683o.f4639e, i3, 4).order(ByteOrder.LITTLE_ENDIAN).getFloat();
                    break;
                case 16:
                    dB = ByteBuffer.wrap(this.f4721s.f4702m.f4689f.f4683o.f4639e, i3, 8).order(ByteOrder.LITTLE_ENDIAN).getDouble();
                    break;
            }
            switch (this.f4714l.f4723b) {
                case 0:
                    return this.f4714l.f4725d != null ? this.f4714l.f4725d[0] + (this.f4714l.f4725d[1] * dB) : dB;
                case 1:
                case 2:
                case 6:
                case 9:
                case 132:
                case 133:
                    return dB;
                case 11:
                    return dB;
                case 65535:
                    return dB;
                default:
                    throw new IllegalArgumentException("Channel conversion type " + this.f4714l.f4723b + " is not supported");
            }
        } catch (IndexOutOfBoundsException e2) {
            bH.C.a(e2.getMessage());
            return Double.NaN;
        }
    }

    public int hashCode() {
        return (37 * 7) + Objects.hashCode(a());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj != null && getClass() == obj.getClass()) {
            return Objects.equals(a(), ((C0536ae) obj).a());
        }
        return false;
    }

    @Override // W.T
    public InterfaceC0185k g() {
        if (this.f4714l.f4723b == 11) {
            return new C0540ai(this.f4721s.f4702m.f4689f.f4683o, this.f4714l.f4725d, this.f4714l.f4726e);
        }
        return null;
    }

    @Override // W.T
    public float h() {
        return Float.NaN;
    }

    @Override // W.T
    public float i() {
        return Float.NaN;
    }

    @Override // W.T
    public String j() {
        return null;
    }
}
