package ao;

import W.C0184j;
import W.C0188n;
import bH.C1012t;
import bH.InterfaceC1014v;
import java.util.ArrayList;
import java.util.List;

/* renamed from: ao.L, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/L.class */
public class C0594L {

    /* renamed from: a, reason: collision with root package name */
    C0188n f5097a;

    /* renamed from: b, reason: collision with root package name */
    C0184j f5098b;

    /* renamed from: c, reason: collision with root package name */
    C0184j f5099c;

    /* renamed from: d, reason: collision with root package name */
    C0184j f5100d;

    /* renamed from: e, reason: collision with root package name */
    C0184j f5101e;

    /* renamed from: f, reason: collision with root package name */
    int f5102f;

    C0594L(C0188n c0188n) {
        this.f5097a = c0188n;
        this.f5098b = c0188n.a(h.g.a().a("Time"));
        this.f5099c = c0188n.a(h.g.a().a("TraveledFeet"));
        this.f5101e = C0586D.b(c0188n);
        this.f5100d = c0188n.a(h.g.a().a("Field.Launch timer"));
        a(0);
    }

    /* JADX WARN: Removed duplicated region for block: B:149:0x06a8  */
    /* JADX WARN: Removed duplicated region for block: B:163:0x0768  */
    /* JADX WARN: Removed duplicated region for block: B:177:0x083f  */
    /* JADX WARN: Removed duplicated region for block: B:191:0x08ff  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void a(int r8) {
        /*
            Method dump skipped, instructions count: 2513
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: ao.C0594L.a(int):void");
    }

    private float a(C0188n c0188n, String str) {
        String strF = c0188n.f(str);
        if (str == null) {
            return Float.NaN;
        }
        try {
            return Float.parseFloat(strF);
        } catch (Exception e2) {
            return Float.NaN;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void a() {
        if (2 == 0) {
            return;
        }
        if (2 == 1) {
            C0184j c0184jA = a(false);
            C0184j c0184jA2 = this.f5097a.a("TPS");
            C1012t c1012t = new C1012t();
            ArrayList arrayList = new ArrayList();
            ArrayList arrayList2 = new ArrayList();
            int i2 = 0;
            while (i2 < this.f5097a.d()) {
                float fC = c0184jA2.c(i2);
                while (fC > 80.0f) {
                    if (this.f5101e.d(i2) > 0.0f) {
                        if (arrayList.size() == 0) {
                            arrayList.add(Double.valueOf(arrayList.size()));
                            if (i2 > 0) {
                                arrayList2.add(Double.valueOf(this.f5098b.c(i2 - 1)));
                            }
                        }
                        arrayList.add(Double.valueOf(this.f5098b.c(i2)));
                        arrayList2.add(Double.valueOf(this.f5101e.c(i2)));
                    } else {
                        arrayList.clear();
                        arrayList2.clear();
                    }
                    i2++;
                    fC = c0184jA2.c(i2);
                }
                if (arrayList.size() > 2 && ((Double) arrayList.get(arrayList.size() - 1)).doubleValue() - ((Double) arrayList.get(0)).doubleValue() > 2.0d) {
                    double[] dArr = new double[arrayList.size()];
                    double[] dArr2 = new double[arrayList.size()];
                    for (int i3 = 0; i3 < dArr.length; i3++) {
                        dArr[i3] = ((Double) arrayList.get(i3)).doubleValue();
                        dArr2[i3] = ((Double) arrayList2.get(i3)).doubleValue();
                    }
                    List listA = c1012t.a(arrayList, arrayList2, dArr);
                    for (int i4 = 0; i4 < listA.size(); i4++) {
                        float fA = (float) ((InterfaceC1014v) listA.get(i4)).a();
                        if (fA > 0.0f) {
                            c0184jA.b((i4 + i2) - dArr.length, fA);
                        } else {
                            c0184jA.b((i4 + i2) - dArr.length, 0.0f);
                        }
                    }
                    arrayList.clear();
                    arrayList2.clear();
                }
                i2++;
            }
            return;
        }
        if (2 == 2) {
            C0184j c0184jA3 = a(true);
            C0184j c0184jA4 = this.f5097a.a("TPS");
            ArrayList arrayList3 = new ArrayList();
            ArrayList arrayList4 = new ArrayList();
            int i5 = 0;
            while (i5 < this.f5097a.d()) {
                float fC2 = c0184jA4.c(i5);
                while (fC2 > 80.0f) {
                    if (c0184jA3.d(i5) > 0.0f) {
                        if (arrayList3.size() == 0) {
                            if (i5 > 0) {
                                arrayList3.add(Double.valueOf(this.f5098b.c(i5 - 1)));
                            } else {
                                arrayList3.add(Double.valueOf(arrayList3.size()));
                            }
                            arrayList4.add(Double.valueOf(0.0d));
                        }
                        arrayList3.add(Double.valueOf(this.f5098b.c(i5)));
                        arrayList4.add(Double.valueOf(c0184jA3.c(i5)));
                    } else {
                        arrayList3.clear();
                        arrayList4.clear();
                    }
                    i5++;
                    fC2 = c0184jA4.c(i5);
                }
                if (arrayList3.size() > 2 && ((Double) arrayList3.get(arrayList3.size() - 1)).doubleValue() - ((Double) arrayList3.get(0)).doubleValue() > 2.0d) {
                    int size = i5 - arrayList3.size();
                    int iB = b(size);
                    float fC3 = c0184jA3.c(iB);
                    float fD = this.f5098b.d(iB);
                    double dDoubleValue = ((Double) arrayList3.get(0)).doubleValue();
                    if (this.f5100d != null) {
                    }
                    float fPow = (float) (fC3 / Math.pow((fD - 0.0f) - dDoubleValue, 0.82d));
                    while (size <= iB) {
                        double d2 = (this.f5098b.d(size) - 0.0f) - dDoubleValue;
                        c0184jA3.b(size, (float) (Math.pow((this.f5098b.d(size) - 0.0f) - dDoubleValue, 0.82d) * fPow));
                        size++;
                    }
                    arrayList3.clear();
                    arrayList4.clear();
                    return;
                }
                i5++;
            }
        }
    }

    private int b(int i2) {
        C0184j c0184jA = this.f5097a.a(h.g.a().a(h.g.f12247f));
        double dC = Double.NaN;
        double dC2 = Double.NaN;
        int i3 = 0;
        int iS = this.f5101e.s();
        boolean zR = this.f5101e.r();
        this.f5101e.b(true);
        this.f5101e.g(3);
        try {
            int i4 = i2 + 1;
            while (i4 < this.f5097a.d() - ((2 * 6) / 2)) {
                float fC = c0184jA.c(i4);
                while (fC > 80.0f && this.f5101e.c(i4) == 0.0f) {
                    i4++;
                    fC = c0184jA.c(i4);
                }
                while (fC > 80.0f) {
                    int i5 = 2 / 2;
                    double dC3 = (this.f5101e.c(i4) - this.f5101e.c(i2)) / (this.f5098b.c(i4) - this.f5098b.c(i2));
                    if (Double.isNaN(dC)) {
                        dC = (this.f5101e.c(i4) - this.f5101e.c(i2)) / (this.f5098b.c(i4) - this.f5098b.c(i2));
                        dC2 = (this.f5101e.c(i4) - this.f5101e.c(i2)) / (this.f5098b.c(i4) - this.f5098b.c(i2));
                    } else if (6 * 2 < i4 - i2) {
                        dC = (this.f5101e.c(i4 + i5) - this.f5101e.c(i4 - i5)) / (this.f5098b.c(i4 + i5) - this.f5098b.c(i4 - i5));
                        dC2 = (this.f5101e.c(i4 + (i5 * 6)) - this.f5101e.c(i4 - (i5 * 6))) / (this.f5098b.c(i4 + (i5 * 6)) - this.f5098b.c(i4 - (i5 * 6)));
                    } else {
                        dC = ((dC * (2 - 1.0f)) + ((this.f5101e.c(i4) - this.f5101e.c(i4 - 1)) / (this.f5098b.c(i4) - this.f5098b.c(i4 - 1)))) / 2;
                        dC2 = ((dC2 * ((5 * 2) - 1.0f)) + ((this.f5101e.c(i4) - this.f5101e.c(i4 - 1)) / (this.f5098b.c(i4) - this.f5098b.c(i4 - 1)))) / (5.0f * 2);
                    }
                    i3 = (dC <= 0.0d || dC2 <= 0.0d || Math.abs(dC - dC2) >= ((double) this.f5101e.c(i4)) * 0.07d) ? 0 : i3 + 1;
                    if (i3 > 4) {
                        bH.C.c("Within Range! " + i4);
                        int i6 = i4;
                        this.f5101e.b(zR);
                        this.f5101e.g(iS);
                        return i6;
                    }
                    i4++;
                    fC = c0184jA.c(i4);
                }
                i4++;
            }
            return -1;
        } finally {
            this.f5101e.b(zR);
            this.f5101e.g(iS);
        }
    }

    private C0184j a(boolean z2) {
        if (this.f5101e == null) {
            return null;
        }
        C0184j c0184jA = this.f5097a.a("CorrectedMPH");
        if (z2 || c0184jA == null) {
            if (c0184jA == null) {
                c0184jA = new C0184j("CorrectedMPH");
                this.f5097a.add(c0184jA);
            } else {
                c0184jA.w();
            }
            boolean zR = this.f5101e.r();
            this.f5101e.b(false);
            for (int i2 = 0; i2 < this.f5097a.d(); i2++) {
                c0184jA.a(this.f5101e.c(i2));
            }
            this.f5101e.b(zR);
        }
        return c0184jA;
    }

    private boolean b() {
        if (this.f5099c == null && this.f5098b != null && this.f5101e != null) {
            a();
            this.f5099c = new C0184j("TraveledFeet");
            this.f5099c.a(0.0f);
            C0184j c0184jA = a(false);
            for (int i2 = 1; i2 < this.f5098b.v(); i2++) {
                this.f5099c.a(this.f5099c.d(i2 - 1) + (((5280.0f * c0184jA.d(i2)) / 3600.0f) * (this.f5098b.d(i2) - this.f5098b.d(i2 - 1))));
            }
            this.f5097a.add(this.f5099c);
        }
        return this.f5099c != null;
    }
}
