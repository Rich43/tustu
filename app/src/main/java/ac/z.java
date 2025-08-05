package ac;

import sun.text.normalizer.NormalizerImpl;

/* loaded from: TunerStudioMS.jar:ac/z.class */
public class z extends w {

    /* renamed from: a, reason: collision with root package name */
    C0488D f4323a;

    /* renamed from: b, reason: collision with root package name */
    private byte[] f4324b = new byte[4];

    /* renamed from: s, reason: collision with root package name */
    private byte[] f4325s = new byte[4];

    /* renamed from: t, reason: collision with root package name */
    private byte f4326t = 0;

    /* renamed from: u, reason: collision with root package name */
    private float f4327u = 1.0f;

    /* renamed from: v, reason: collision with root package name */
    private float f4328v = 0.0f;

    public z(C0488D c0488d) throws V.g {
        a(c0488d);
    }

    public void a(C0488D c0488d) throws V.g {
        this.f4323a = c0488d;
        this.f4327u = (float) c0488d.g();
        this.f4328v = (float) c0488d.h();
        b(c0488d.f());
        if (c0488d.f() == 1) {
            a("U32");
        } else if (c0488d.f() == 3) {
            a("S64");
        } else {
            a("S32");
        }
        d(c0488d.b());
        a(c0488d.i());
        b(this.f4327u);
        c(this.f4328v);
        b(c0488d.a());
        e(c0488d.k());
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
        System.arraycopy(this.f4324b, 0, bArr, length3, this.f4324b.length);
        int length4 = length3 + this.f4324b.length;
        System.arraycopy(this.f4325s, 0, bArr, length4, this.f4325s.length);
        int length5 = length4 + this.f4325s.length;
        bArr[length5] = this.f4326t;
        if (i2 > 1) {
            System.arraycopy(this.f4313o, 0, bArr, length5 + 1, this.f4313o.length);
        }
        return bArr;
    }

    @Override // ac.w
    public byte[] a(double d2) throws V.g {
        byte bG2 = g();
        if (bG2 == f4301d || bG2 == f4300c) {
            return new byte[]{(byte) Math.round((d2 / this.f4327u) - this.f4328v)};
        }
        if (bG2 == f4303f || bG2 == f4302e) {
            int iRound = (int) Math.round((d2 / this.f4327u) - this.f4328v);
            return new byte[]{(byte) ((iRound & NormalizerImpl.CC_MASK) >> 8), (byte) (iRound & 255)};
        }
        if (bG2 == f4305h || bG2 == f4304g) {
            return f((int) Math.round((d2 / this.f4327u) - this.f4328v));
        }
        if (bG2 == f4307j) {
            return a((float) ((d2 / this.f4327u) - this.f4328v));
        }
        if (bG2 == f4306i) {
            return a(Math.round((d2 / this.f4327u) - this.f4328v));
        }
        throw new V.g("Unsupported Field type: " + ((int) bG2));
    }

    @Override // ac.w
    public int d(int i2) {
        return i2 == 1 ? 1 + this.f4312n.length + this.f4314p.length + this.f4315q.length + this.f4324b.length + this.f4325s.length + 1 : 1 + this.f4312n.length + this.f4314p.length + this.f4315q.length + this.f4324b.length + this.f4325s.length + this.f4313o.length + 1;
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
        this.f4327u = f2;
        this.f4324b = a(f2, this.f4324b);
    }

    public void c(float f2) {
        this.f4328v = f2;
        this.f4325s = a(f2, this.f4325s);
    }

    public void a(byte b2) {
        this.f4326t = b2;
    }

    @Override // ac.w
    public double a(byte[][] bArr) {
        return this.f4323a.j();
    }
}
