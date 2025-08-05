package S;

import G.C0134q;
import G.InterfaceC0109co;
import G.aF;
import bH.C;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

/* loaded from: TunerStudioMS.jar:S/e.class */
public class e implements aF, InterfaceC0109co {

    /* renamed from: a, reason: collision with root package name */
    HashMap f1824a = new HashMap();

    /* renamed from: c, reason: collision with root package name */
    private static e f1825c = null;

    /* renamed from: b, reason: collision with root package name */
    f f1826b;

    private e() {
        this.f1826b = null;
        this.f1826b = new f(this);
        this.f1826b.start();
    }

    public static e a() {
        if (f1825c == null) {
            f1825c = new e();
        }
        return f1825c;
    }

    public Collection a(String str) {
        return c(str).values();
    }

    public void a(String str, String str2) {
        HashMap mapC = c(str);
        a aVar = (a) mapC.get(str2);
        if (aVar != null) {
            aVar.a(false);
            try {
                aVar.d(str);
            } catch (C0134q e2) {
                C.a("ECU Config not loaded?? " + str);
            }
            mapC.remove(str2);
            C.c("Deactivating Event Trigger: " + aVar.a());
        }
    }

    public void a(String str, a aVar) {
        a(str, aVar.a());
        HashMap mapC = c(str);
        aVar.c(str);
        mapC.put(aVar.a(), aVar);
        aVar.a(true);
        C.c("Activating Event Trigger: " + aVar.a());
    }

    public void b(String str) {
        Iterator it = c(str).values().iterator();
        while (it.hasNext()) {
            try {
                ((a) it.next()).d(str);
            } catch (C0134q e2) {
                C.b("Unsubscribe EventTrigger failed, could not find config: " + str);
            }
        }
        this.f1824a.remove(str);
    }

    private HashMap c(String str) {
        HashMap map = (HashMap) this.f1824a.get(str);
        if (map == null) {
            map = new HashMap();
            this.f1824a.put(str, map);
        }
        return map;
    }

    @Override // G.aF
    public void a(String str, byte[] bArr) {
        for (a aVar : c(str).values()) {
            if (aVar.b()) {
                aVar.b(str, bArr);
            } else {
                aVar.a(str, bArr);
            }
        }
        this.f1826b.a();
    }

    @Override // G.InterfaceC0109co
    public void setCurrentOutputChannelValue(String str, double d2) {
        b();
    }

    public void b() {
        for (String str : this.f1824a.keySet()) {
            for (a aVar : c(str).values()) {
                if (aVar.b()) {
                    aVar.b(str);
                } else {
                    aVar.a(str);
                }
            }
        }
        this.f1826b.a();
    }
}
