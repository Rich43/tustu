package com.efiAnalytics.tunerStudio.search;

import G.AbstractC0093bz;
import G.C0072be;
import G.C0079bl;
import G.C0083bp;
import G.C0088bu;
import G.C0126i;
import G.R;
import G.aA;
import G.aI;
import ax.U;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tunerStudio/search/g.class */
public class g {
    public static ArrayList a(R r2, String str) {
        ArrayList arrayList = new ArrayList();
        List<aA> listA = a(r2);
        String lowerCase = str.toLowerCase();
        for (aA aAVar : listA) {
            C0088bu c0088buC = r2.e().c(aAVar.d());
            if (c0088buC instanceof C0088bu) {
                E e2 = new E(r2, c0088buC);
                ArrayList arrayListA = a(c0088buC, str);
                if (C1818g.b(aAVar.e()).toLowerCase().contains(lowerCase)) {
                    B b2 = new B();
                    b2.a(C1818g.b(aAVar.e()));
                    b2.c(C1818g.b("Menu"));
                    b2.b(str);
                    b2.a(e2);
                    arrayListA.add(b2);
                }
                if (!arrayListA.isEmpty()) {
                    for (int i2 = 0; i2 < arrayListA.size(); i2++) {
                        B b3 = (B) arrayListA.get(i2);
                        if (C1818g.b(b3.a()).toLowerCase().startsWith(str) && i2 > 0) {
                            arrayListA.remove(i2);
                            arrayListA.add(0, b3);
                        }
                    }
                    A a2 = new A();
                    a2.b(C1818g.b((c0088buC.M() == null || c0088buC.M().isEmpty()) ? aAVar.e() : c0088buC.M()));
                    Iterator it = arrayListA.iterator();
                    while (it.hasNext()) {
                        ((B) it.next()).a(e2);
                    }
                    a2.addAll(arrayListA);
                    arrayList.add(a2);
                }
            } else if (c0088buC instanceof C0072be) {
                bH.C.d("Search not checking Table: " + aAVar.d());
            } else if (c0088buC instanceof C0079bl) {
                bH.C.d("Search not checking curve: " + aAVar.d());
            }
        }
        for (int i3 = 0; i3 < arrayList.size(); i3++) {
            A a3 = (A) arrayList.get(i3);
            if (a3.a(str) && i3 > 0) {
                arrayList.remove(i3);
                arrayList.add(0, a3);
            }
        }
        return arrayList;
    }

    public static List a(R r2) {
        ArrayList arrayList = new ArrayList();
        Iterator itB = r2.e().b();
        while (itB.hasNext()) {
            arrayList.addAll(a(r2, (aA) itB.next()));
        }
        return arrayList;
    }

    private static List a(R r2, aA aAVar) {
        ArrayList arrayList = new ArrayList();
        if (aAVar.b()) {
            Iterator itA = aAVar.a();
            while (itA.hasNext()) {
                arrayList.addAll(a(r2, (aA) itA.next()));
            }
        } else {
            try {
                if (aAVar.aH() == null || aAVar.aH().isEmpty() || C0126i.a(aAVar.aH(), (aI) r2) != 0.0d) {
                    arrayList.add(aAVar);
                }
            } catch (U e2) {
                Logger.getLogger(g.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
        }
        return arrayList;
    }

    private static ArrayList a(C0088bu c0088bu, String str) {
        A a2 = new A();
        String lowerCase = str.toLowerCase();
        if (c0088bu.M() != null && C1818g.b(c0088bu.M()).toLowerCase().contains(lowerCase) && !a2.c(C1818g.b(C1818g.b(c0088bu.M())))) {
            B b2 = new B();
            b2.a(C1818g.b(c0088bu.M()));
            b2.c(C1818g.b("Section"));
            b2.b(str);
            a2.add(b2);
        }
        Iterator itF = c0088bu.F();
        while (itF.hasNext()) {
            AbstractC0093bz abstractC0093bz = (AbstractC0093bz) itF.next();
            if (abstractC0093bz instanceof C0083bp) {
                C0083bp c0083bp = (C0083bp) abstractC0093bz;
                if (c0083bp.l() != null && C1818g.b(c0083bp.l()).toLowerCase().contains(lowerCase) && !a2.c(C1818g.b(c0083bp.l()))) {
                    B b3 = new B();
                    if (c0083bp.b() == null || c0083bp.b().length() <= 0) {
                        b3.c(C1818g.b("Label"));
                    } else {
                        b3.c(C1818g.b("Setting"));
                        b3.a(C1818g.b(c0083bp.l()));
                    }
                    b3.a(C1818g.b(c0083bp.l()));
                    b3.b(str);
                    a2.add(b3);
                }
            }
        }
        Iterator itK = c0088bu.K();
        while (itK.hasNext()) {
            ArrayList arrayListA = a((C0088bu) itK.next(), str);
            if (arrayListA != null) {
                a2.addAll(arrayListA);
            }
        }
        return a2;
    }
}
