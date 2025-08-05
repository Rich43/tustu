package bs;

import G.C0079bl;
import G.R;
import G.aW;
import G.dn;
import com.efiAnalytics.ui.InterfaceC1662et;
import com.efiAnalytics.ui.dQ;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import s.C1818g;

/* renamed from: bs.e, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bs/e.class */
public class C1272e {

    /* renamed from: a, reason: collision with root package name */
    private static C1272e f8578a = null;

    /* renamed from: b, reason: collision with root package name */
    private HashMap f8579b = new HashMap();

    private C1272e() {
    }

    public static C1272e a() {
        if (f8578a == null) {
            f8578a = new C1272e();
        }
        return f8578a;
    }

    public ArrayList a(R r2, dn dnVar) {
        String str = r2.c() + "." + dnVar.c();
        if (!this.f8579b.containsKey(str)) {
            dQ dQVar = new dQ(aE.a.A(), r2.c() + "." + dnVar.c());
            ArrayList arrayList = new ArrayList();
            Iterator itI = dnVar.i();
            while (itI.hasNext()) {
                aW aWVar = (aW) itI.next();
                bL.k kVarA = null;
                if (aWVar.aJ().startsWith("std_")) {
                    try {
                        kVarA = a(r2, dnVar, aWVar, dQVar);
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
            this.f8579b.put(str, arrayList);
        }
        return (ArrayList) this.f8579b.get(str);
    }

    private bL.k a(R r2, dn dnVar, aW aWVar, InterfaceC1662et interfaceC1662et) throws V.a {
        if (((C0079bl) r2.e().c(dnVar.c())) == null) {
            throw new V.a("Can not find WUE Curve: " + dnVar.c() + " in current configuration.");
        }
        bL.k kVar = new bL.k();
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
            throw new V.a("Unknown Standard Record Filter type for WUE: " + aWVar.aJ());
        }
        bL.f fVar = new bL.f();
        fVar.e(aWVar.aJ());
        fVar.a(dnVar.d());
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
