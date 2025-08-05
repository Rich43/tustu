package al;

import am.C0575c;
import am.C0576d;
import am.C0577e;
import bH.C;
import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import java.nio.channels.SeekableByteChannel;
import java.util.ArrayList;
import java.util.List;

/* renamed from: al.b, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:al/b.class */
public class C0568b implements InterfaceC0569c {

    /* renamed from: f, reason: collision with root package name */
    private C0577e f4924f;

    /* renamed from: g, reason: collision with root package name */
    private C0575c f4925g;

    /* renamed from: a, reason: collision with root package name */
    C0572f f4927a;

    /* renamed from: c, reason: collision with root package name */
    C0567a f4929c;

    /* renamed from: d, reason: collision with root package name */
    byte[] f4930d;

    /* renamed from: h, reason: collision with root package name */
    private String f4926h = "";

    /* renamed from: b, reason: collision with root package name */
    List f4928b = new ArrayList();

    /* renamed from: e, reason: collision with root package name */
    boolean f4931e = false;

    /* renamed from: i, reason: collision with root package name */
    private boolean f4932i = false;

    /* renamed from: j, reason: collision with root package name */
    private int f4933j = 0;

    public C0568b(SeekableByteChannel seekableByteChannel, C0577e c0577e) {
        this.f4929c = null;
        this.f4930d = null;
        this.f4924f = c0577e;
        this.f4925g = c0577e.f();
        this.f4927a = new C0572f(seekableByteChannel, c0577e, this.f4925g);
        for (C0576d c0576d : AbstractC0570d.a(this.f4925g)) {
            if (b(c0576d.e())) {
                C0567a c0567a = new C0567a(c0576d);
                if (c0567a.e().equalsIgnoreCase(SchemaSymbols.ATTVAL_TIME)) {
                    this.f4929c = c0567a;
                }
                this.f4928b.add(c0567a);
            }
        }
        this.f4930d = this.f4927a.a();
    }

    public double a() {
        if (this.f4929c == null || this.f4929c.a().m() <= 0.0d) {
            return Double.NaN;
        }
        return (this.f4929c.a().l() >= 0.5d || this.f4929c.a().l() < 0.0d) ? this.f4925g.e() / (this.f4929c.a().m() - this.f4929c.a().l()) : this.f4925g.e() / this.f4929c.a().m();
    }

    public long b() {
        return this.f4925g.e();
    }

    public float[] a(double d2, int i2, float[] fArr) {
        if (this.f4933j == 0 && this.f4926h.equals("Data Group 132")) {
            C.c(this.f4926h);
        }
        if (!this.f4932i && this.f4930d != null && (d2 == 0.0d || this.f4929c == null || this.f4929c.a(this.f4930d) <= d2 || !this.f4931e)) {
            for (int i3 = 0; i3 < this.f4928b.size(); i3++) {
                fArr[i3 + i2] = (float) ((C0567a) this.f4928b.get(i3)).a(this.f4930d);
            }
            this.f4933j++;
            this.f4931e = true;
            this.f4930d = this.f4927a.a();
        }
        return fArr;
    }

    public boolean c() {
        return this.f4929c != null;
    }

    public double d() {
        if (this.f4929c == null || this.f4930d == null) {
            return Double.NaN;
        }
        return this.f4929c.a(this.f4930d);
    }

    public List e() {
        return this.f4928b;
    }

    public int f() {
        return this.f4928b.size();
    }

    public void a(boolean z2) {
        if (z2) {
            if (this.f4929c == null || this.f4928b.contains(this.f4929c)) {
                return;
            }
            this.f4928b.add(0, this.f4929c);
            return;
        }
        for (int i2 = 0; i2 < this.f4928b.size(); i2++) {
            if (((C0567a) this.f4928b.get(i2)).e().equalsIgnoreCase(SchemaSymbols.ATTVAL_TIME)) {
                this.f4928b.remove(i2);
                return;
            }
        }
    }

    public C0575c g() {
        return this.f4925g;
    }

    public void b(boolean z2) {
        this.f4932i = z2;
    }

    public String h() {
        return this.f4926h;
    }

    public void a(String str) {
        this.f4926h = str;
    }
}
