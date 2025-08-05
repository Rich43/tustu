package aS;

import G.C0113cs;
import G.R;
import G.S;
import G.aM;
import G.aN;
import G.aR;
import bH.C;
import com.efiAnalytics.plugin.ecu.ControllerParameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:aS/g.class */
public class g implements S, aN {

    /* renamed from: a, reason: collision with root package name */
    String f3913a = "seconds";

    /* renamed from: b, reason: collision with root package name */
    HashMap f3914b = new HashMap();

    /* renamed from: c, reason: collision with root package name */
    double f3915c = 1.0d;

    /* renamed from: d, reason: collision with root package name */
    Map f3916d = new HashMap();

    /* renamed from: e, reason: collision with root package name */
    Map f3917e = new HashMap();

    protected void d(R r2) throws V.a {
        String[] strArrK = r2.k();
        ArrayList arrayListB = b(r2.c());
        Map mapA = a(r2.c());
        for (int i2 = 0; i2 < strArrK.length; i2++) {
            aM aMVarC = r2.c(strArrK[i2]);
            if (aMVarC.D()) {
                arrayListB.add(strArrK[i2]);
                aR.a().a(r2.c(), strArrK[i2], this);
                if (aMVarC.i().equals(ControllerParameter.PARAM_CLASS_BITS)) {
                    try {
                        mapA.put(aMVarC.aJ(), aMVarC.f(r2.h()));
                    } catch (V.g e2) {
                        Logger.getLogger(g.class.getName()).log(Level.WARNING, "PowerCycleMonitor unable to store bit param value", (Throwable) e2);
                    }
                }
            }
        }
        if (arrayListB.isEmpty()) {
            return;
        }
        C0113cs.a().d("powerCycleRequired");
        j jVar = new j(this);
        r2.a(jVar);
        if (r2.g(this.f3913a) != null) {
            C0113cs.a().a(r2.c(), this.f3913a, jVar);
        } else {
            C.a("Failed to subscribe PowerCycle Monitor to outputchannel: " + this.f3913a);
        }
        C0113cs.a().a("controllerSettingsLoaded", jVar);
        C0113cs.a().a("powerCycleRequired", new i(this, r2));
        this.f3916d.put(r2.c(), r2);
    }

    private Map a(String str) {
        Map map = (Map) this.f3917e.get(str);
        if (map == null) {
            map = new HashMap();
            this.f3917e.put(str, map);
        }
        return map;
    }

    private ArrayList b(String str) {
        ArrayList arrayList = (ArrayList) this.f3914b.get(str);
        if (arrayList == null) {
            arrayList = new ArrayList();
            this.f3914b.put(str, arrayList);
        }
        return arrayList;
    }

    @Override // G.S
    public void a(R r2) {
    }

    @Override // G.S
    public void b(R r2) {
        C0113cs.a().i(r2.c());
        this.f3914b.remove(r2.c());
        if (this.f3914b.isEmpty()) {
            C0113cs.a().f("powerCycleRequired");
        }
        this.f3917e.remove(r2.c());
        this.f3916d.remove(r2.c());
        aR.a().a(this);
    }

    @Override // G.S
    public void c(R r2) {
        try {
            d(r2);
        } catch (V.a e2) {
            C.a(e2.getMessage(), e2, null);
        }
    }

    @Override // G.aN
    public void a(String str, String str2) {
        R r2 = (R) this.f3916d.get(str);
        aM aMVarC = r2.c(str2);
        if (aMVarC.i().equals(ControllerParameter.PARAM_CLASS_BITS)) {
            try {
                Map mapA = a(r2.c());
                String str3 = (String) mapA.get(str2);
                String strF = aMVarC.f(r2.h());
                mapA.put(str2, strF);
                if (str3 != null && strF != null) {
                    if (str3.equals(strF)) {
                        return;
                    }
                }
            } catch (V.g e2) {
                Logger.getLogger(g.class.getName()).log(Level.INFO, "Strange, PowerCycleMonitor can't get last bit value.", (Throwable) e2);
            }
        }
        if (r2 == null || !r2.R()) {
            return;
        }
        C0113cs.a().a("powerCycleRequired", 1.0d);
        if (this.f3915c == 0.0d) {
            a();
        }
        this.f3915c = 1.0d;
    }

    private void a() {
        new h(this).start();
    }
}
