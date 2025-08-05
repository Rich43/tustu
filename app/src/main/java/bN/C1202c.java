package bn;

import G.R;
import G.bH;
import G.bL;
import V.g;
import bH.C;
import bH.W;
import bo.C1206b;
import bt.bO;
import com.efiAnalytics.ui.C1701s;
import com.efiAnalytics.ui.dQ;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import s.C1818g;

/* renamed from: bn.c, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bn/c.class */
public class C1202c {

    /* renamed from: a, reason: collision with root package name */
    public static String f8269a = "veTableGenerator";

    /* renamed from: b, reason: collision with root package name */
    public static String f8270b = "afrTableGenerator";

    /* renamed from: c, reason: collision with root package name */
    public static String f8271c = "Two Point Calculator";

    /* renamed from: d, reason: collision with root package name */
    public static String f8272d = "Calculator";

    /* renamed from: f, reason: collision with root package name */
    private static C1202c f8273f = null;

    /* renamed from: e, reason: collision with root package name */
    HashMap f8274e = new HashMap();

    private C1202c() {
    }

    public static C1202c a() {
        if (f8273f == null) {
            f8273f = new C1202c();
        }
        return f8273f;
    }

    public InterfaceC1200a a(R r2, bH bHVar) throws C1204e {
        String strB = W.b(bHVar.aJ(), " ", "");
        if (this.f8274e.get(strB) != null) {
            return (InterfaceC1200a) this.f8274e.get(strB);
        }
        if (strB.equals(f8269a)) {
            try {
                return new C1206b(C1818g.b(bL.c(r2, bHVar.a())), r2, bHVar.a(), bO.a().a(r2, bHVar.a(), "", ""), new dQ(aE.a.A(), "veGenerator"), C1206b.f8297p);
            } catch (g e2) {
                Logger.getLogger(C1202c.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                throw new C1204e("Unable to create VE Table Generator for " + bHVar.a());
            } catch (Exception e3) {
                Logger.getLogger(C1202c.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
                throw new C1204e("Unable to create VE Table Generator for " + bHVar.a());
            }
        }
        if (!strB.equals(f8270b)) {
            throw new C1204e("Unknown Tool:" + strB);
        }
        try {
            C1701s c1701sA = bO.a().a(r2, bHVar.a(), "", "");
            String strB2 = C1818g.b(bL.c(r2, bHVar.a()));
            if (r2.d("NARROW_BAND_EGO") != null) {
                C.a("Narrow band AFR target generation is are not currently supported - Use at own risk", null, null);
            }
            return new C1206b(strB2, r2, bHVar.a(), c1701sA, new dQ(aE.a.A(), "veGenerator"), r2.d("LAMBDA") != null ? C1206b.f8299r : C1206b.f8298q);
        } catch (g e4) {
            Logger.getLogger(C1202c.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e4);
            throw new C1204e("Unable to create AFR Table Generator for " + bHVar.a());
        } catch (Exception e5) {
            Logger.getLogger(C1202c.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e5);
            throw new C1204e("Unable to create AFR Table Generator for " + bHVar.a());
        }
    }

    public void a(String str, InterfaceC1200a interfaceC1200a) {
        this.f8274e.put(W.b(str, " ", ""), interfaceC1200a);
    }
}
