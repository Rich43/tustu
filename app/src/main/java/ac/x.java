package ac;

import G.C0043ac;
import G.aH;
import sun.text.normalizer.NormalizerImpl;

/* loaded from: TunerStudioMS.jar:ac/x.class */
public class x extends w {

    /* renamed from: a, reason: collision with root package name */
    private q f4317a;

    /* renamed from: b, reason: collision with root package name */
    private byte[] f4318b;

    /* renamed from: s, reason: collision with root package name */
    private byte[] f4319s;

    /* renamed from: t, reason: collision with root package name */
    private byte f4320t;

    /* renamed from: u, reason: collision with root package name */
    private float f4321u;

    /* renamed from: v, reason: collision with root package name */
    private float f4322v;

    public x() {
        this.f4317a = null;
        this.f4318b = new byte[4];
        this.f4319s = new byte[4];
        this.f4320t = (byte) 0;
        this.f4321u = 1.0f;
        this.f4322v = 0.0f;
    }

    public x(q qVar) throws V.g {
        this.f4317a = null;
        this.f4318b = new byte[4];
        this.f4319s = new byte[4];
        this.f4320t = (byte) 0;
        this.f4321u = 1.0f;
        this.f4322v = 0.0f;
        a(qVar);
    }

    public void a(q qVar) throws V.g {
        this.f4317a = qVar;
        aH aHVarC = qVar.c();
        if (!aHVarC.b().equals("formula")) {
            this.f4321u = (float) aHVarC.h();
            this.f4322v = (float) aHVarC.i();
            a(aHVarC.c());
        } else if (qVar.b().e() == 1 || qVar.b().e() == 2) {
            a("U32");
        } else if (qVar.b().e() == 3) {
            a("S64");
        } else {
            a("F32");
        }
        d(qVar.f());
        a((byte) qVar.e());
        b(this.f4321u);
        c(this.f4322v);
        C0043ac c0043acB = qVar.b();
        b(qVar.a());
        e(c0043acB.e());
        c(qVar.i());
    }

    @Override // ac.w
    public byte[] a(int i2) {
        byte[] bArr = new byte[d(i2)];
        bArr[0] = g();
        int i3 = 0 + 1;
        System.arraycopy(this.f4312n, 0, bArr, i3, this.f4312n.length);
        int length = i3 + this.f4312n.length;
        System.arraycopy(this.f4314p, 0, bArr, length, this.f4314p.length);
        int length2 = length + this.f4314p.length;
        System.arraycopy(this.f4315q, 0, bArr, length2, this.f4315q.length);
        int length3 = length2 + this.f4315q.length;
        System.arraycopy(this.f4318b, 0, bArr, length3, this.f4318b.length);
        int length4 = length3 + this.f4318b.length;
        System.arraycopy(this.f4319s, 0, bArr, length4, this.f4319s.length);
        int length5 = length4 + this.f4319s.length;
        bArr[length5] = this.f4320t;
        if (i2 > 1) {
            System.arraycopy(this.f4313o, 0, bArr, length5 + 1, this.f4313o.length);
        }
        return bArr;
    }

    @Override // ac.w
    public byte[] a(double d2) throws V.g {
        byte bG2 = g();
        if (bG2 == f4301d || bG2 == f4300c) {
            return new byte[]{(byte) Math.round((d2 / this.f4321u) - this.f4322v)};
        }
        if (bG2 == f4303f || bG2 == f4302e) {
            int iRound = (int) Math.round((d2 / this.f4321u) - this.f4322v);
            return new byte[]{(byte) ((iRound & NormalizerImpl.CC_MASK) >> 8), (byte) (iRound & 255)};
        }
        if (bG2 == f4305h || bG2 == f4304g) {
            return f((int) Math.round((d2 / this.f4321u) - this.f4322v));
        }
        if (bG2 == f4307j) {
            return a((float) ((d2 / this.f4321u) - this.f4322v));
        }
        if (bG2 == f4306i) {
            return a(Math.round((d2 / this.f4321u) - this.f4322v));
        }
        throw new V.g("Unsupported Field type: " + ((int) bG2));
    }

    @Override // ac.w
    public int d(int i2) {
        return i2 == 1 ? 1 + this.f4312n.length + this.f4314p.length + this.f4315q.length + this.f4318b.length + this.f4319s.length + 1 : 1 + this.f4312n.length + this.f4314p.length + this.f4315q.length + this.f4318b.length + this.f4319s.length + this.f4313o.length + 1;
    }

    private void a(String str) throws V.g {
        if (str.equals("U08")) {
            b(f4300c);
            return;
        }
        if (str.equals("S08")) {
            b(f4301d);
            return;
        }
        if (str.equals("U16")) {
            b(f4302e);
            return;
        }
        if (str.equals("S16")) {
            b(f4303f);
            return;
        }
        if (str.equals("U32")) {
            b(f4304g);
        } else if (str.equals("S32")) {
            b(f4305h);
        } else {
            if (!str.equals("F32")) {
                throw new V.g("Unsupported Logger Field Type: " + str);
            }
            b(f4307j);
        }
    }

    public void b(float f2) {
        this.f4321u = f2;
        this.f4318b = a(f2, this.f4318b);
    }

    public float a() {
        return this.f4321u;
    }

    public void c(float f2) {
        this.f4322v = f2;
        this.f4319s = a(f2, this.f4319s);
    }

    public float b() {
        return this.f4322v;
    }

    public void a(byte b2) {
        this.f4320t = b2;
    }

    public byte c() {
        return this.f4320t;
    }

    @Override // ac.w
    public double a(byte[][] bArr) {
        return this.f4317a.a(bArr[this.f4317a.d()]);
    }
}
