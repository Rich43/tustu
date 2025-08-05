package br;

import G.C0072be;
import G.aM;
import G.aW;
import G.dk;
import com.efiAnalytics.ui.InterfaceC1662et;
import com.efiAnalytics.ui.dQ;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import s.C1818g;

/* renamed from: br.e, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:br/e.class */
public class C1241e {

    /* renamed from: a, reason: collision with root package name */
    private static C1241e f8447a = null;

    /* renamed from: b, reason: collision with root package name */
    private HashMap f8448b = new HashMap();

    private C1241e() {
    }

    public static C1241e a() {
        if (f8447a == null) {
            f8447a = new C1241e();
        }
        return f8447a;
    }

    public ArrayList a(G.R r2, dk dkVar) {
        String str = r2.c() + "." + dkVar.b();
        if (!this.f8448b.containsKey(str)) {
            dQ dQVar = new dQ(aE.a.A(), r2.c() + "." + dkVar.b());
            ArrayList arrayList = new ArrayList();
            Iterator itI = dkVar.i();
            while (itI.hasNext()) {
                aW aWVar = (aW) itI.next();
                bL.k kVarA = null;
                if (aWVar.aJ().startsWith("std_")) {
                    try {
                        kVarA = a(r2, dkVar, aWVar, dQVar);
                    } catch (V.a e2) {
                        bH.C.b("Unable to create Standard VE Analyze Filter: " + aWVar.aJ() + ", will try as a normal filter.");
                        e2.printStackTrace();
                    }
                }
                if (kVarA == null) {
                    kVarA = new bL.k();
                    kVarA.a(aWVar.a());
                    kVarA.c(aWVar.b());
                    kVarA.a(aWVar.c());
                    if (aWVar.g()) {
                        kVarA.a(Double.parseDouble(a(dQVar, kVarA.g(), aWVar.c() + "")));
                        kVarA.a(dQVar);
                    }
                    kVarA.a(aWVar.d());
                    kVarA.d(aWVar.e());
                    kVarA.e(aWVar.aJ());
                    kVarA.a(aWVar.g());
                }
                kVarA.d();
                arrayList.add(kVarA);
            }
            for (int i2 = 0; i2 < arrayList.size() - 1; i2++) {
                if (((bL.k) arrayList.get(i2)).g().equals("std_DeadLambda")) {
                    arrayList.add((bL.k) arrayList.remove(i2));
                }
            }
            this.f8448b.put(str, arrayList);
        }
        return (ArrayList) this.f8448b.get(str);
    }

    private bL.k a(G.R r2, dk dkVar, aW aWVar, InterfaceC1662et interfaceC1662et) throws V.a {
        C0072be c0072be = (C0072be) r2.e().c(dkVar.b());
        if (c0072be == null) {
            throw new V.a("Can not find VE table: " + dkVar.b() + " in current configuration.");
        }
        bL.k kVar = new bL.k();
        if (aWVar.aJ().equals("std_xAxisMin")) {
            aM aMVarC = r2.c(c0072be.a());
            if (aMVarC == null) {
                throw new V.a("X Axis Parameter " + c0072be.a() + " not found in current Configuration.");
            }
            kVar.e(aWVar.aJ());
            kVar.a(c0072be.d());
            kVar.a(4);
            kVar.a(Double.parseDouble(a(interfaceC1662et, kVar.g(), aMVarC.q() + "")));
            kVar.a(interfaceC1662et);
            kVar.d("Min " + c0072be.d());
            return kVar;
        }
        if (aWVar.aJ().equals("std_xAxisMax")) {
            aM aMVarC2 = r2.c(c0072be.a());
            if (aMVarC2 == null) {
                throw new V.a("X Axis Parameter " + c0072be.a() + " not found in current Configuration.");
            }
            kVar.e(aWVar.aJ());
            kVar.a(c0072be.d());
            kVar.a(2);
            kVar.a(Double.parseDouble(a(interfaceC1662et, kVar.g(), aMVarC2.r() + "")));
            kVar.a(interfaceC1662et);
            kVar.d("Max " + c0072be.d());
            return kVar;
        }
        if (aWVar.aJ().equals("std_yAxisMin")) {
            aM aMVarC3 = r2.c(c0072be.b());
            if (aMVarC3 == null) {
                throw new V.a("Y Axis Parameter " + c0072be.b() + " not found in current Configuration.");
            }
            kVar.e(aWVar.aJ());
            kVar.a(c0072be.f());
            kVar.a(4);
            kVar.a(Double.parseDouble(a(interfaceC1662et, kVar.g(), aMVarC3.q() + "")));
            kVar.a(interfaceC1662et);
            kVar.d("Min " + c0072be.f());
            return kVar;
        }
        if (aWVar.aJ().equals("std_yAxisMax")) {
            aM aMVarC4 = r2.c(c0072be.b());
            if (aMVarC4 == null) {
                throw new V.a("Y Axis Parameter " + c0072be.b() + " not found in current Configuration.");
            }
            kVar.e(aWVar.aJ());
            kVar.a(c0072be.f());
            kVar.a(2);
            kVar.a(Double.parseDouble(a(interfaceC1662et, kVar.g(), aMVarC4.r() + "")));
            kVar.a(interfaceC1662et);
            kVar.d("Max " + c0072be.f());
            return kVar;
        }
        if (aWVar.aJ().equals("std_Custom")) {
            kVar.e(aWVar.aJ());
            kVar.a(128);
            kVar.c(a(interfaceC1662et, kVar.g(), ""));
            kVar.a(interfaceC1662et);
            kVar.d(C1818g.b("Custom Filter") + ": ");
            return kVar;
        }
        if (aWVar.aJ().equals("std_Expression")) {
            kVar.e(aWVar.aJ());
            kVar.a(128);
            kVar.c(aWVar.f());
            kVar.d(aWVar.e());
            kVar.a(false);
            return kVar;
        }
        if (!aWVar.aJ().equals("std_DeadLambda")) {
            throw new V.a("Unknown Standard Record Filter type: " + aWVar.aJ());
        }
        bL.f fVar = new bL.f();
        fVar.e(aWVar.aJ());
        fVar.a(dkVar.e());
        return fVar;
    }

    private String a(InterfaceC1662et interfaceC1662et, String str, String str2) {
        if (interfaceC1662et == null) {
            return str2;
        }
        String strA = interfaceC1662et.a(str);
        return (strA == null || strA.equals("")) ? str2 : strA;
    }
}
