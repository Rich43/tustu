package bG;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* renamed from: bG.b, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bG/b.class */
public class C0987b implements i, l {

    /* renamed from: a, reason: collision with root package name */
    ArrayList f6913a = new ArrayList();

    /* renamed from: b, reason: collision with root package name */
    List f6914b = new ArrayList();

    /* renamed from: c, reason: collision with root package name */
    double f6915c = 360.0d;

    /* renamed from: d, reason: collision with root package name */
    double f6916d = Double.NaN;

    /* renamed from: g, reason: collision with root package name */
    private double f6917g = Double.NaN;

    /* renamed from: e, reason: collision with root package name */
    int f6918e = 720;

    /* renamed from: f, reason: collision with root package name */
    byte[] f6919f = {0, 1, -1, -4, 0, 0, 0, 0, 0, 0, 0, 0, 0, Byte.MAX_VALUE, -1, 0, 1, -1, -4, 0, 7, -1, -16, 0, 31, -1, -64, 0, Byte.MAX_VALUE, -1, 0, 1, -1, -4, 0, 7, -1, -16, 0, 31, -1, -64, 0, Byte.MAX_VALUE, -1, 0, 1, -1, -4, 0, 7, -1, -16, 0, 31, -1, -64, 0, Byte.MAX_VALUE, -1, 0, 1, -1, -4, 0, 7, -1, -16, 0, 31, -1, -64, 0, Byte.MAX_VALUE, -1, 0, 1, -1, -4, 0, 7, -1, -16, 0, 31, -1, -64, 0, Byte.MAX_VALUE, -1};

    /* renamed from: h, reason: collision with root package name */
    private BigInteger f6920h = null;

    public C0987b() {
        a(this.f6919f);
    }

    public void a(E e2) {
        this.f6914b.add(e2);
    }

    private void e() {
        Iterator it = this.f6914b.iterator();
        while (it.hasNext()) {
            ((E) it.next()).d();
        }
    }

    @Override // bG.l
    public ArrayList a() {
        return this.f6913a;
    }

    @Override // bG.l
    public double b() {
        return Double.isNaN(this.f6917g) ? this.f6916d : this.f6917g;
    }

    private void f() {
        double d2 = this.f6915c / this.f6918e;
        this.f6913a.clear();
        k kVar = new k();
        boolean zTestBit = this.f6920h.testBit(0);
        this.f6916d = Double.NaN;
        for (int i2 = 0; i2 < 2; i2++) {
            int i3 = 0;
            while (true) {
                if (i3 >= this.f6918e) {
                    break;
                }
                if (zTestBit != this.f6920h.testBit((this.f6918e - i3) - 1)) {
                    double d3 = i3 * d2;
                    if (i2 <= 0 || d3 < this.f6916d) {
                        if (this.f6920h.testBit((this.f6918e - i3) - 1)) {
                            kVar.a(d3);
                            if (Double.isNaN(this.f6916d) || d3 < this.f6916d) {
                                this.f6916d = d3;
                            }
                        } else {
                            kVar.b(((360.0d + d3) - kVar.a()) % 360.0d);
                        }
                        if (kVar.c()) {
                            this.f6913a.add(kVar);
                            if (i2 > 0) {
                                break;
                            } else {
                                kVar = new k();
                            }
                        }
                        if (kVar.a() + kVar.b() > 360.0d) {
                            zTestBit = false;
                            break;
                        }
                        zTestBit = this.f6920h.testBit((this.f6918e - i3) - 1);
                    }
                }
                i3++;
            }
        }
    }

    @Override // bG.i
    public void a(List list) {
        int i2 = this.f6918e;
        double d2 = this.f6915c / i2;
        this.f6920h = this.f6920h.and(new BigInteger(new byte[]{0}));
        Iterator it = list.iterator();
        while (it.hasNext()) {
            k kVar = (k) it.next();
            int iRound = (int) Math.round(kVar.a() / d2);
            for (int iRound2 = (iRound + ((int) Math.round(kVar.b() / d2))) - 1; iRound2 >= iRound; iRound2--) {
                this.f6920h = this.f6920h.setBit((i2 - (iRound2 % i2)) - 1);
            }
        }
        f();
        e();
    }

    public byte[] c() {
        byte[] byteArray = this.f6920h.toByteArray();
        int i2 = this.f6918e / 8;
        if (byteArray.length < i2) {
            byte[] bArr = new byte[i2];
            System.arraycopy(byteArray, 0, bArr, i2 - byteArray.length, byteArray.length);
            return bArr;
        }
        if (byteArray.length <= i2) {
            return byteArray;
        }
        byte[] bArr2 = new byte[i2];
        System.arraycopy(byteArray, byteArray.length - bArr2.length, bArr2, 0, bArr2.length);
        return bArr2;
    }

    public void a(byte[] bArr) {
        this.f6918e = bArr.length * 8;
        this.f6920h = new BigInteger(bArr);
        f();
    }

    public void a(int i2) {
        if (i2 < this.f6913a.size()) {
            this.f6913a.remove(i2);
        }
    }

    public double d() {
        return this.f6915c / this.f6918e;
    }
}
