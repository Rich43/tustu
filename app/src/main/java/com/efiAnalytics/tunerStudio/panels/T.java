package com.efiAnalytics.tunerStudio.panels;

import W.C0184j;
import W.C0188n;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import r.C1798a;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tunerStudio/panels/T.class */
public class T implements bB.q {

    /* renamed from: a, reason: collision with root package name */
    static String f9973a = "Min";

    /* renamed from: b, reason: collision with root package name */
    static String f9974b = "Max";

    /* renamed from: d, reason: collision with root package name */
    private C0188n f9971d = null;

    /* renamed from: c, reason: collision with root package name */
    List f9975c = new ArrayList();

    /* renamed from: e, reason: collision with root package name */
    private W.ap f9972e = new W.ar(C1798a.a().f13332an, "HighSpeedLoggerFieldLimits");

    @Override // bB.q
    public void a(bB.r rVar) {
        if (rVar.c()) {
            this.f9972e.a(f9973a + rVar.e(), Double.toString(Double.NaN));
        } else {
            this.f9972e.a(f9973a + rVar.e(), Double.toString(rVar.a()));
        }
        if (rVar.d()) {
            this.f9972e.a(f9974b + rVar.e(), Double.toString(Double.NaN));
        } else {
            this.f9972e.a(f9974b + rVar.e(), Double.toString(rVar.b()));
        }
        a(rVar.e(), rVar.a(), rVar.b());
    }

    private void a(String str, double d2, double d3) {
        Iterator it = this.f9975c.iterator();
        while (it.hasNext()) {
            ((U) it.next()).a(str, d2, d3);
        }
    }

    public void a(U u2) {
        this.f9975c.add(u2);
    }

    @Override // bB.q
    public void a(String str) {
        this.f9972e.a(f9973a + str, Double.toString(Double.NaN));
        this.f9972e.a(f9974b + str, Double.toString(Double.NaN));
        a(str, Double.NaN, Double.NaN);
    }

    public double c(String str) {
        double d2 = Double.NaN;
        try {
            d2 = Double.parseDouble(this.f9972e.b(f9973a + str, Double.toString(Double.NaN)));
        } catch (Exception e2) {
            bH.C.b("Unable to load " + str + " Min/Max");
        }
        return d2;
    }

    public double d(String str) {
        double d2 = Double.NaN;
        try {
            d2 = Double.parseDouble(this.f9972e.b(f9974b + str, Double.toString(Double.NaN)));
        } catch (Exception e2) {
            bH.C.b("Unable to load " + str + " Min/Max");
        }
        return d2;
    }

    @Override // bB.q
    public List a() {
        ArrayList arrayList = new ArrayList();
        Iterator it = c().iterator();
        while (it.hasNext()) {
            C0184j c0184j = (C0184j) it.next();
            double dC = Double.NaN;
            double d2 = Double.NaN;
            try {
                dC = c(c0184j.a());
                d2 = d(c0184j.a());
            } catch (Exception e2) {
                bH.C.b("Unable to load Chart Min/Max");
            }
            bB.a aVar = new bB.a();
            aVar.a(c0184j.a());
            aVar.a(dC);
            aVar.b(d2);
            arrayList.add(aVar);
        }
        return arrayList;
    }

    @Override // bB.q
    public List b() {
        ArrayList arrayList = new ArrayList();
        Iterator it = c().iterator();
        while (it.hasNext()) {
            C0184j c0184j = (C0184j) it.next();
            if (!arrayList.contains(c0184j.a())) {
                bH.C.c("Adding: " + c0184j.a());
                arrayList.add(c0184j.a());
            }
        }
        return arrayList;
    }

    public C0188n c() {
        return this.f9971d;
    }

    public void a(C0188n c0188n) {
        this.f9971d = c0188n;
    }

    @Override // bB.q
    public bB.r b(bB.r rVar) {
        return null;
    }

    @Override // bB.q
    public bB.r b(String str) {
        bB.a aVar = new bB.a();
        aVar.a(str);
        aVar.a(c(str));
        aVar.b(d(str));
        return aVar;
    }
}
