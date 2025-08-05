package m;

import al.AbstractC0570d;
import am.C0575c;
import am.C0576d;
import am.C0577e;
import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import java.util.ArrayList;
import java.util.List;

/* loaded from: TunerStudioMS.jar:m/j.class */
class j {

    /* renamed from: a, reason: collision with root package name */
    C0577e f12923a;

    /* renamed from: b, reason: collision with root package name */
    C0575c f12924b;

    /* renamed from: c, reason: collision with root package name */
    List f12925c;

    /* renamed from: d, reason: collision with root package name */
    List f12926d = new ArrayList();

    /* renamed from: e, reason: collision with root package name */
    C0576d f12927e;

    /* renamed from: f, reason: collision with root package name */
    final /* synthetic */ d f12928f;

    j(d dVar, C0577e c0577e) {
        this.f12928f = dVar;
        this.f12927e = null;
        this.f12923a = c0577e;
        this.f12924b = c0577e.f();
        this.f12925c = AbstractC0570d.a(this.f12924b);
        for (C0576d c0576d : this.f12925c) {
            String strE = c0576d.o().e();
            if (strE.equalsIgnoreCase(SchemaSymbols.ATTVAL_TIME)) {
                this.f12927e = c0576d;
            }
            this.f12926d.add(strE);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int c() {
        return this.f12926d.size();
    }

    public double a() {
        if (this.f12927e == null || this.f12927e.m() <= 0.0d) {
            return Double.NaN;
        }
        return (this.f12927e.l() >= 0.5d || this.f12927e.l() < 0.0d) ? this.f12924b.e() / (this.f12927e.m() - this.f12927e.l()) : this.f12924b.e() / this.f12927e.m();
    }

    public long b() {
        return this.f12924b.e();
    }
}
