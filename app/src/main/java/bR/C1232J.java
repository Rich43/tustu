package br;

import G.dc;
import G.dk;
import W.C0172ab;
import com.efiAnalytics.ui.C1589c;
import com.efiAnalytics.ui.dQ;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import r.C1798a;
import r.C1807j;

/* renamed from: br.J, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:br/J.class */
public class C1232J implements aE.e {

    /* renamed from: b, reason: collision with root package name */
    private static C1232J f8359b = null;

    /* renamed from: a, reason: collision with root package name */
    HashMap f8360a = new HashMap();

    public static C1232J a() {
        if (f8359b == null) {
            f8359b = new C1232J();
        }
        return f8359b;
    }

    public ArrayList a(G.R r2) {
        Iterator itL = r2.L();
        if (!itL.hasNext()) {
            c(r2);
            itL = r2.L();
        }
        ArrayList arrayList = new ArrayList();
        while (itL.hasNext()) {
            arrayList.add((String) itL.next());
        }
        return arrayList;
    }

    public ArrayList b(G.R r2) {
        Iterator itM = r2.M();
        ArrayList arrayList = new ArrayList();
        while (itM.hasNext()) {
            arrayList.add((String) itM.next());
        }
        return arrayList;
    }

    public dk a(G.R r2, String str) {
        dk dkVarR = r2.r(str);
        if (dkVarR != null) {
            return dkVarR;
        }
        c(r2);
        return r2.r(str);
    }

    public dc b(G.R r2, String str) {
        dc dcVarS = r2.s(str);
        return dcVarS != null ? dcVarS : r2.s(str);
    }

    private void c(G.R r2) {
        String strI = r2.i();
        if (strI.startsWith("MS3")) {
            d(r2, C1807j.f13479o);
            return;
        }
        if (strI.startsWith("MS2Extra")) {
            d(r2, C1807j.f13480p);
            return;
        }
        if (strI.startsWith("MS/Extra") || strI.startsWith("MS1/Extra") || strI.startsWith("MSnS-extra")) {
            d(r2, C1807j.f13478n);
            return;
        }
        if (strI.startsWith("MSII Rev")) {
            d(r2, C1807j.f13481q);
            return;
        }
        if (strI.startsWith("20")) {
            d(r2, C1807j.f13477m);
        } else if (strI.startsWith("Monsterfirmware")) {
            d(r2, C1807j.f13482r);
        } else if (strI.startsWith("BigStuff Gen4")) {
            d(r2, C1807j.f13483s);
        }
    }

    private void d(G.R r2, String str) {
        if (C1798a.a().c(C1798a.f13387bq, C1798a.f13388br) && C1798a.a().c(C1798a.f13389bs, C1798a.f13390bt)) {
            return;
        }
        new C0172ab().a(r2, str, false);
    }

    public C1589c c(G.R r2, String str) {
        String str2 = r2.c() + "." + str;
        if (!this.f8360a.containsKey(str2)) {
            dQ dQVar = new dQ(aE.a.A(), str2);
            C1589c c1589c = new C1589c();
            c1589c.e(a(dQVar, "maxPercentChange", c1589c.e()));
            c1589c.f(a(dQVar, "baseWeight", c1589c.h()));
            c1589c.d(a(dQVar, "maxValueChange", c1589c.d()));
            c1589c.c(a(dQVar, "minChangeThreshold", c1589c.c()));
            c1589c.a(a(dQVar, "weightThreshold", c1589c.a()));
            this.f8360a.put(str2, c1589c);
        }
        return (C1589c) this.f8360a.get(str2);
    }

    private double a(dQ dQVar, String str, double d2) {
        String strA = dQVar.a(str);
        return (strA == null || strA.trim().equals("")) ? d2 : Double.parseDouble(strA);
    }

    @Override // aE.e
    public void a(aE.a aVar, G.R r2) {
    }

    @Override // aE.e
    public void e_() {
        this.f8360a.clear();
    }

    @Override // aE.e
    public void a(aE.a aVar) {
    }
}
