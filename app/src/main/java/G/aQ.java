package G;

import com.efiAnalytics.plugin.ecu.ControllerParameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:G/aQ.class */
public class aQ implements S {

    /* renamed from: b, reason: collision with root package name */
    private static aQ f633b = null;

    /* renamed from: a, reason: collision with root package name */
    HashMap f634a = new HashMap();

    public static aQ a() {
        if (f633b == null) {
            f633b = new aQ();
            T.a().a(f633b);
        }
        return f633b;
    }

    public void a(aM aMVar, R r2, String str) {
        if (aMVar == null) {
            throw new V.g("EcuPrameter not optional.");
        }
        if (!aMVar.i().equals(ControllerParameter.PARAM_CLASS_SCALAR) && !aMVar.i().equals(ControllerParameter.PARAM_CLASS_BITS)) {
            throw new V.g("Only scalar and bit param class supported.");
        }
        a(r2.c()).add(new aO(aMVar, r2, str));
    }

    private ArrayList a(String str) {
        ArrayList arrayList = (ArrayList) this.f634a.get(str);
        if (arrayList == null) {
            arrayList = new ArrayList();
            this.f634a.put(str, arrayList);
        }
        return arrayList;
    }

    @Override // G.S
    public void a(R r2) {
    }

    @Override // G.S
    public void b(R r2) {
        Iterator it = a(r2.c()).iterator();
        while (it.hasNext()) {
            aR.a().a((aO) it.next());
        }
        a(r2.c()).clear();
    }

    @Override // G.S
    public void c(R r2) {
        Iterator it = a(r2.c()).iterator();
        while (it.hasNext()) {
            aO aOVar = (aO) it.next();
            for (String str : C0126i.h(aOVar.a(), r2)) {
                try {
                    aR.a().a(r2.c(), str, aOVar);
                } catch (V.a e2) {
                    Logger.getLogger(aQ.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                }
            }
            for (String str2 : C0126i.e(aOVar.a(), r2)) {
                try {
                    C0113cs.a().a(r2.c(), str2, aOVar);
                } catch (V.a e3) {
                    Logger.getLogger(aQ.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
                }
            }
        }
    }
}
