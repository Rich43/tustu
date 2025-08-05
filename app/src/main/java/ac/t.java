package ac;

import bH.C0995c;
import com.efiAnalytics.plugin.ecu.ControllerParameter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import sun.text.normalizer.NormalizerImpl;

/* loaded from: TunerStudioMS.jar:ac/t.class */
public class t extends w {

    /* renamed from: s, reason: collision with root package name */
    private q f4266s;

    /* renamed from: t, reason: collision with root package name */
    private final byte[] f4267t;

    /* renamed from: u, reason: collision with root package name */
    private final byte[] f4268u;

    /* renamed from: v, reason: collision with root package name */
    private final byte[] f4269v;

    /* renamed from: w, reason: collision with root package name */
    private final byte[] f4270w;

    /* renamed from: a, reason: collision with root package name */
    List f4271a;

    /* renamed from: b, reason: collision with root package name */
    List f4272b;

    public t() {
        this.f4266s = null;
        this.f4267t = new byte[1];
        this.f4268u = new byte[4];
        this.f4269v = new byte[1];
        this.f4270w = new byte[3];
        this.f4271a = new ArrayList();
        this.f4272b = new ArrayList();
    }

    public t(String str, String str2) throws V.g {
        this.f4266s = null;
        this.f4267t = new byte[1];
        this.f4268u = new byte[4];
        this.f4269v = new byte[1];
        this.f4270w = new byte[3];
        this.f4271a = new ArrayList();
        this.f4272b = new ArrayList();
        b(str);
        d(ControllerParameter.PARAM_CLASS_BITS);
        e(2);
        e(str2);
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
        System.arraycopy(this.f4267t, 0, bArr, length3, this.f4267t.length);
        int length4 = length3 + this.f4267t.length;
        System.arraycopy(this.f4268u, 0, bArr, length4, this.f4268u.length);
        int length5 = length4 + this.f4268u.length;
        System.arraycopy(this.f4269v, 0, bArr, length5, this.f4269v.length);
        int length6 = length5 + this.f4269v.length;
        System.arraycopy(this.f4270w, 0, bArr, length6, this.f4270w.length);
        int length7 = length6 + this.f4270w.length;
        if (i2 > 1) {
            System.arraycopy(this.f4313o, 0, bArr, length7, this.f4313o.length);
        }
        return bArr;
    }

    public void b(int i2) {
        a(i2, this.f4268u, true);
    }

    public int a() {
        return C0995c.a(this.f4268u, 0, this.f4268u.length, true, true);
    }

    public void c(int i2) {
        this.f4267t[0] = (byte) (255 & i2);
    }

    public byte b() {
        return this.f4267t[0];
    }

    public void a(q qVar) {
        this.f4271a.add(qVar);
        this.f4272b.add(qVar.a());
    }

    public List c() {
        return this.f4271a;
    }

    public void a(String str) {
        this.f4272b.add(str);
    }

    public List d() {
        return this.f4272b;
    }

    private void e(String str) throws V.g {
        if (str.equals("U08")) {
            b(f4308k);
        } else if (str.equals("U16")) {
            b(f4309l);
        } else {
            if (!str.equals("U32")) {
                throw new V.g("Unsupported Logger Field Type: " + str);
            }
            b(f4310m);
        }
    }

    public void a(byte b2) {
        this.f4269v[0] = b2;
    }

    public byte e() {
        return this.f4269v[0];
    }

    @Override // ac.w
    public int d(int i2) {
        return i2 == 1 ? 1 + this.f4312n.length + this.f4314p.length + this.f4315q.length + this.f4267t.length + this.f4268u.length + this.f4269v.length + this.f4270w.length : 1 + this.f4312n.length + this.f4314p.length + this.f4315q.length + this.f4267t.length + this.f4268u.length + this.f4269v.length + this.f4270w.length + this.f4313o.length;
    }

    @Override // ac.w
    public byte[] a(double d2) throws V.g {
        byte bG2 = g();
        if (bG2 == f4308k) {
            return new byte[]{(byte) d2};
        }
        if (bG2 == f4309l) {
            int i2 = (int) d2;
            return new byte[]{(byte) ((i2 & NormalizerImpl.CC_MASK) >> 8), (byte) (i2 & 255)};
        }
        if (bG2 == f4310m) {
            return f((int) d2);
        }
        throw new V.g("Unsupported BitField type: " + ((int) bG2));
    }

    public byte[] f() {
        ArrayList arrayList = new ArrayList();
        int length = 0;
        for (q qVar : this.f4271a) {
            arrayList.add(qVar.a());
            length += qVar.a().length() + 1;
        }
        byte[] bArr = new byte[length];
        int length2 = 0;
        Iterator<E> it = arrayList.iterator();
        while (it.hasNext()) {
            byte[] bytes = ((String) it.next()).getBytes();
            System.arraycopy(bytes, 0, bArr, length2, bytes.length);
            int length3 = length2 + bytes.length;
            System.arraycopy(f4316r, 0, bArr, length3, f4316r.length);
            length2 = length3 + f4316r.length;
        }
        return bArr;
    }

    @Override // ac.w
    public double a(byte[][] bArr) {
        int i2 = 0;
        int i3 = 0;
        for (q qVar : this.f4271a) {
            if (qVar.a(bArr[qVar.d()]) != 0.0d) {
                i3 |= 1 << i2;
            }
            i2++;
        }
        return i3;
    }
}
