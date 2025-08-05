package bL;

import G.C0126i;
import G.aI;
import bH.C;
import com.efiAnalytics.ui.InterfaceC1662et;

/* loaded from: TunerStudioMS.jar:bL/k.class */
public class k {

    /* renamed from: b, reason: collision with root package name */
    private String f7149b = null;

    /* renamed from: c, reason: collision with root package name */
    private String f7150c = null;

    /* renamed from: d, reason: collision with root package name */
    private String f7151d = "";

    /* renamed from: e, reason: collision with root package name */
    private double f7152e = 0.0d;

    /* renamed from: f, reason: collision with root package name */
    private int f7153f = 1;

    /* renamed from: g, reason: collision with root package name */
    private String f7154g = null;

    /* renamed from: a, reason: collision with root package name */
    protected int f7155a = 0;

    /* renamed from: h, reason: collision with root package name */
    private InterfaceC1662et f7156h = null;

    /* renamed from: i, reason: collision with root package name */
    private boolean f7157i = true;

    public boolean a(aI aIVar, byte[] bArr) {
        boolean zA;
        if (this.f7153f != 128) {
            try {
                zA = a(aIVar.g(this.f7149b).b(bArr), this.f7152e, this.f7153f);
            } catch (Exception e2) {
                C.b("unable to do simple compare for channels: " + this.f7149b);
                return false;
            }
        } else {
            if (this.f7150c == null || this.f7150c.equals("")) {
                return false;
            }
            try {
                zA = C0126i.a(this.f7150c, aIVar, bArr) != 0.0d;
            } catch (Exception e3) {
                C.b("Unable to evaluate filter condition, not filtering:" + this.f7150c + "\n\t" + ((String) null));
                return false;
            }
        }
        if (zA) {
            this.f7155a++;
        }
        return zA;
    }

    private boolean a(double d2, double d3, int i2) {
        int i3;
        return i2 == 1 ? d2 == d3 : i2 == 2 ? d2 > d3 : i2 == 16 ? d2 >= d3 : i2 == 4 ? d2 < d3 : i2 == 8 ? d2 <= d3 : i2 == 32 && (i3 = (int) d3) == (((int) d2) & i3);
    }

    public String b() {
        return this.f7149b;
    }

    public void a(String str) {
        this.f7149b = str;
    }

    public double c() {
        return this.f7152e;
    }

    public void b(String str) {
        if (str == null || str.equals("")) {
            return;
        }
        a(Double.parseDouble(str));
    }

    public void a(double d2) {
        this.f7152e = d2;
        if (this.f7156h != null) {
            this.f7156h.a(g(), d2 + "");
        }
    }

    public void d() {
        String strA;
        if (this.f7156h == null || (strA = this.f7156h.a(g())) == null || strA.equals("")) {
            return;
        }
        if (this.f7153f == 128) {
            c(strA);
        } else {
            b(strA);
        }
    }

    public int e() {
        return this.f7153f;
    }

    public void a(int i2) {
        this.f7153f = i2;
    }

    public String toString() {
        return this.f7154g + "\nChannel=" + this.f7149b + "\noperator=" + this.f7153f + "\ncompareValue=" + this.f7152e;
    }

    public String f() {
        return this.f7150c;
    }

    public void c(String str) {
        this.f7150c = str;
        if (this.f7156h != null) {
            this.f7156h.a(g(), str + "");
        }
    }

    public String a() {
        return (this.f7154g == null || this.f7154g.equals("")) ? this.f7153f == 128 ? this.f7150c : this.f7149b != null ? this.f7149b : "Undefined" : this.f7154g;
    }

    public void d(String str) {
        this.f7154g = str;
    }

    public String g() {
        return this.f7151d;
    }

    public void e(String str) {
        this.f7151d = str;
    }

    public void a(InterfaceC1662et interfaceC1662et) {
        this.f7156h = interfaceC1662et;
    }

    public boolean h() {
        return this.f7157i;
    }

    public void a(boolean z2) {
        this.f7157i = z2;
    }
}
