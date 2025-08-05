package bL;

import bH.C;
import com.efiAnalytics.ui.C1562b;
import com.efiAnalytics.ui.C1589c;
import com.efiAnalytics.ui.C1701s;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/* loaded from: TunerStudioMS.jar:bL/n.class */
public class n {

    /* renamed from: a, reason: collision with root package name */
    q f7165a;

    /* renamed from: b, reason: collision with root package name */
    List f7166b = Collections.synchronizedList(new ArrayList());

    /* renamed from: c, reason: collision with root package name */
    o f7167c = null;

    /* renamed from: d, reason: collision with root package name */
    ArrayList f7168d = new ArrayList();

    /* renamed from: e, reason: collision with root package name */
    int f7169e = 0;

    /* renamed from: f, reason: collision with root package name */
    int f7170f = 0;

    /* renamed from: g, reason: collision with root package name */
    private C1589c f7171g;

    public n(C1701s c1701s, C1701s c1701s2, C1701s c1701s3, C1701s c1701s4, C1589c c1589c) {
        this.f7165a = null;
        this.f7171g = null;
        this.f7171g = c1589c;
        this.f7165a = new q(c1701s, c1701s2, c1701s3, c1701s4, c1589c);
    }

    public void a(l lVar) {
        this.f7168d.add(lVar);
    }

    public void b(l lVar) {
        this.f7168d.remove(lVar);
    }

    public m a() {
        m mVar = new m();
        C1701s c1701sA = this.f7165a.a();
        mVar.d(c1701sA.t());
        mVar.a(this.f7169e);
        mVar.b(this.f7170f);
        int i2 = 0;
        double d2 = 0.0d;
        int i3 = 0;
        double dK = 0.0d;
        double d3 = 0.0d;
        C1562b[][] c1562bArrD = c1701sA.D();
        for (int length = c1562bArrD.length - 1; length >= 0; length--) {
            for (int i4 = 0; i4 < c1562bArrD[length].length; i4++) {
                if (c1562bArrD[(c1562bArrD.length - length) - 1][i4].j() > 0) {
                    i3++;
                    dK += c1562bArrD[length][i4].k();
                    double dAbs = Math.abs(c1562bArrD[(c1562bArrD.length - length) - 1][i4].i().doubleValue() - c1701sA.c(length, i4).doubleValue());
                    d3 += dAbs;
                    i2++;
                    if (dAbs > d2) {
                        d2 = dAbs;
                    }
                }
            }
        }
        mVar.c(i2);
        mVar.c(d2);
        mVar.b(d3 / i2);
        mVar.a(dK / i3);
        return mVar;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void g() {
        m mVarA = a();
        Iterator it = this.f7168d.iterator();
        while (it.hasNext()) {
            try {
                ((l) it.next()).a(mVarA);
            } catch (Exception e2) {
                C.c("Exception caught publishing VE analyze metrics, continuing...");
                e2.printStackTrace();
            }
        }
    }

    public void a(p pVar) {
        this.f7166b.add(pVar);
    }

    public void b() {
        this.f7169e = 0;
        this.f7170f = 0;
        C1701s c1701sA = this.f7165a.a();
        C1701s c1701sB = this.f7165a.b();
        C1701s c1701sC = this.f7165a.c();
        c1701sA.a(c1701sB, this.f7171g);
        c1701sA.q();
        c1701sA.a(c1701sC.e(0, 0), 0, 0);
    }

    public void c() {
        if (this.f7167c != null) {
            this.f7167c.a(false);
        }
        this.f7167c = new o(this);
        this.f7167c.start();
    }

    public void d() {
        if (this.f7167c != null) {
            this.f7167c.a(false);
            g();
        }
    }

    public boolean e() {
        return this.f7167c != null && this.f7167c.b();
    }

    public C1589c f() {
        return this.f7171g;
    }

    public void a(C1701s c1701s) {
        this.f7165a.a(c1701s);
    }

    public void a(float f2) {
        this.f7165a.a(f2);
    }
}
