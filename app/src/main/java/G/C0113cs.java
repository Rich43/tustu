package G;

import bH.C0995c;
import bH.C1018z;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/* renamed from: G.cs, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:G/cs.class */
public class C0113cs implements aF {

    /* renamed from: f, reason: collision with root package name */
    private final ConcurrentHashMap f1145f = new ConcurrentHashMap();

    /* renamed from: g, reason: collision with root package name */
    private final ConcurrentHashMap f1146g = new ConcurrentHashMap();

    /* renamed from: h, reason: collision with root package name */
    private final ArrayList f1147h = new ArrayList();

    /* renamed from: i, reason: collision with root package name */
    private final ConcurrentHashMap f1148i = new ConcurrentHashMap();

    /* renamed from: j, reason: collision with root package name */
    private final ConcurrentHashMap f1149j = new ConcurrentHashMap();

    /* renamed from: k, reason: collision with root package name */
    private final ConcurrentHashMap f1150k = new ConcurrentHashMap();

    /* renamed from: l, reason: collision with root package name */
    private final List f1151l = new ArrayList();

    /* renamed from: m, reason: collision with root package name */
    private C0115cu f1152m = null;

    /* renamed from: n, reason: collision with root package name */
    private boolean f1153n = true;

    /* renamed from: o, reason: collision with root package name */
    private boolean f1156o = false;

    /* renamed from: c, reason: collision with root package name */
    List f1157c = new ArrayList();

    /* renamed from: d, reason: collision with root package name */
    int f1158d = 0;

    /* renamed from: e, reason: collision with root package name */
    private static C0113cs f1144e = null;

    /* renamed from: a, reason: collision with root package name */
    public static String f1154a = "Application Events";

    /* renamed from: b, reason: collision with root package name */
    public static String f1155b = "AppEvent.";

    private C0113cs() {
    }

    public static C0113cs a() {
        if (f1144e == null && C1018z.i().b()) {
            f1144e = new C0113cs();
        }
        return f1144e;
    }

    public ConcurrentHashMap a(String str) {
        return (ConcurrentHashMap) this.f1145f.get(str);
    }

    public int b(String str) {
        return a(str).keySet().size();
    }

    public ArrayList a(String str, String str2) {
        return (ArrayList) a(str).get(str2);
    }

    protected ArrayList c(String str) {
        ArrayList arrayList = (ArrayList) this.f1146g.get(str);
        if (arrayList == null) {
            arrayList = new ArrayList();
            this.f1146g.put(str, arrayList);
        }
        return arrayList;
    }

    public void d(String str) {
        if (this.f1147h.contains(str)) {
            return;
        }
        this.f1147h.add(str);
    }

    public boolean e(String str) {
        return this.f1147h.contains(str);
    }

    public void f(String str) {
        this.f1147h.remove(str);
    }

    public String[] b() {
        return (String[]) this.f1147h.toArray(new String[this.f1147h.size()]);
    }

    public void a(String str, dh dhVar) {
        this.f1149j.put(str, dhVar);
    }

    public boolean a(String str, InterfaceC0109co interfaceC0109co) {
        ArrayList arrayListC = c(str);
        boolean zIsEmpty = arrayListC.isEmpty();
        if (!arrayListC.contains(interfaceC0109co)) {
            arrayListC.add(interfaceC0109co);
        }
        Double d2 = (Double) this.f1148i.get(str);
        if (d2 != null) {
            a(str, d2.doubleValue());
        }
        return zIsEmpty;
    }

    public double g(String str) {
        dh dhVar = (dh) this.f1149j.get(str);
        if (dhVar != null) {
            return dhVar.a();
        }
        Double d2 = (Double) this.f1148i.get(str);
        if (d2 != null) {
            return d2.doubleValue();
        }
        return Double.NaN;
    }

    public List h(String str) {
        ConcurrentHashMap concurrentHashMapA;
        C0108cn c0108cn = (C0108cn) this.f1150k.get(str);
        if (c0108cn == null || (concurrentHashMapA = a(str)) == null) {
            return null;
        }
        return c0108cn.a(concurrentHashMapA.keySet());
    }

    private C0108cn b(R r2) {
        C0108cn c0108cn = (C0108cn) this.f1150k.get(r2.c());
        if (c0108cn == null) {
            c0108cn = new C0108cn(r2);
            c0108cn.a(r2.O());
            if (r2.C() instanceof bQ.l) {
                c0108cn.b(((bQ.l) r2.C()).a(r2.c()));
            } else if (r2.O().Z() != null) {
                c0108cn.b(new cR(r2));
            }
            this.f1150k.put(r2.c(), c0108cn);
        }
        return c0108cn;
    }

    public void a(String str, List list, InterfaceC0109co interfaceC0109co) {
        boolean z2 = false;
        Iterator it = list.iterator();
        while (it.hasNext()) {
            if (b(str, (String) it.next(), interfaceC0109co) && !z2) {
                z2 = true;
            }
        }
        if (this.f1156o) {
            if (this.f1157c.contains(str)) {
                return;
            }
            this.f1157c.add(str);
        } else {
            R rC = c().c(str);
            if (!z2 || rC == null) {
                return;
            }
            b(rC).b();
        }
    }

    public void a(String str, String str2, InterfaceC0109co interfaceC0109co) throws V.a {
        boolean zB = b(str, str2, interfaceC0109co);
        if (this.f1156o) {
            if (this.f1157c.contains(str)) {
                return;
            }
            this.f1157c.add(str);
        } else {
            R rC = c().c(str);
            if (!zB || rC == null) {
                return;
            }
            b(rC).b();
        }
    }

    private boolean b(String str, String str2, InterfaceC0109co interfaceC0109co) throws V.a {
        boolean zA;
        if (str.equals(f1154a)) {
            try {
                zA = a(str2, interfaceC0109co);
            } catch (Exception e2) {
                throw new V.a("Invalid OutputChannel Name: " + str2);
            }
        } else {
            ConcurrentHashMap concurrentHashMapA = a(str);
            R rC = c().c(str);
            if (concurrentHashMapA == null) {
                if (rC == null) {
                    throw new V.a("Can not subscribe to ECU Configuration " + str + "\n It does not appear to be loaded.");
                }
                concurrentHashMapA = new ConcurrentHashMap();
                synchronized (this.f1145f) {
                    this.f1145f.put(str, concurrentHashMapA);
                }
            }
            if (rC.g(str2) == null) {
                throw new V.a("Can not subscribe to OutputChannel " + str2 + "\n It is not defined in ECU Configuration " + str + ".");
            }
            ArrayList arrayList = (ArrayList) concurrentHashMapA.get(str2);
            if (arrayList == null) {
                arrayList = new ArrayList();
                synchronized (concurrentHashMapA) {
                    concurrentHashMapA.put(str2, arrayList);
                }
            }
            zA = arrayList.isEmpty();
            synchronized (arrayList) {
                arrayList.add(interfaceC0109co);
            }
        }
        return zA;
    }

    public void a(R r2) {
        b(r2).b();
    }

    public void a(InterfaceC0109co interfaceC0109co) {
        boolean z2 = false;
        synchronized (this.f1145f) {
            Iterator it = this.f1145f.values().iterator();
            while (it.hasNext()) {
                Iterator it2 = ((ConcurrentHashMap) it.next()).values().iterator();
                while (it2.hasNext()) {
                    ArrayList arrayList = (ArrayList) it2.next();
                    synchronized (arrayList) {
                        while (arrayList.remove(interfaceC0109co)) {
                            if (!z2) {
                                z2 = true;
                            }
                        }
                        if (arrayList.isEmpty()) {
                            it2.remove();
                        }
                    }
                }
            }
        }
        synchronized (this.f1146g) {
            for (ArrayList arrayList2 : this.f1146g.values()) {
                while (arrayList2.contains(interfaceC0109co)) {
                    if (arrayList2.remove(interfaceC0109co) && !z2) {
                        z2 = true;
                    }
                }
            }
        }
        if (z2) {
            Iterator it3 = this.f1150k.values().iterator();
            while (it3.hasNext()) {
                ((C0108cn) it3.next()).b();
            }
        }
    }

    public void i(String str) {
        synchronized (this.f1145f) {
            this.f1145f.remove(str);
        }
        b(T.a().c(str)).a();
        this.f1150k.remove(str);
    }

    public void a(String str, double d2) {
        if (!this.f1153n) {
            b(str, d2);
            return;
        }
        if (this.f1152m == null || !this.f1152m.isAlive()) {
            this.f1152m = new C0115cu(this);
            this.f1152m.start();
        }
        this.f1152m.a(str, d2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b(String str, double d2) {
        CopyOnWriteArrayList<InterfaceC0109co> copyOnWriteArrayList = new CopyOnWriteArrayList(c(str));
        InterfaceC0109co interfaceC0109co = null;
        for (InterfaceC0109co interfaceC0109co2 : copyOnWriteArrayList) {
            try {
                interfaceC0109co2.setCurrentOutputChannelValue(str, d2);
            } catch (Exception e2) {
                interfaceC0109co = interfaceC0109co2;
                this.f1158d++;
                bH.C.b("Caught Exception in Auxiliary OutputChannelListener: " + str + ", value:" + d2 + ", error:" + e2.getLocalizedMessage() + ", listener: " + ((Object) interfaceC0109co2));
            }
        }
        if (this.f1158d > 20 && interfaceC0109co != null) {
            bH.C.b("20 failures publishing to listener, unsubscribing: " + ((Object) interfaceC0109co));
            copyOnWriteArrayList.remove(interfaceC0109co);
            this.f1158d = 0;
        }
        this.f1148i.put(str, Double.valueOf(d2));
    }

    public void a(InterfaceC0112cr interfaceC0112cr) {
        this.f1151l.add(interfaceC0112cr);
    }

    public boolean a(R r2, aH aHVar) {
        Iterator it = this.f1151l.iterator();
        while (it.hasNext()) {
            if (!((InterfaceC0112cr) it.next()).a(r2, aHVar)) {
                return false;
            }
        }
        return true;
    }

    @Override // G.aF
    public void a(String str, byte[] bArr) {
        if (!this.f1153n) {
            b(str, bArr);
            return;
        }
        if (this.f1152m == null || !this.f1152m.isAlive()) {
            this.f1152m = new C0115cu(this);
            this.f1152m.start();
        }
        this.f1152m.a(str, bArr);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b(String str, byte[] bArr) {
        ArrayList arrayList;
        ConcurrentHashMap concurrentHashMapA = a(str);
        if (concurrentHashMapA == null) {
            return;
        }
        R rC = c().c(str);
        synchronized (concurrentHashMapA) {
            try {
                Iterator it = concurrentHashMapA.keySet().iterator();
                while (it.hasNext()) {
                    String str2 = (String) it.next();
                    aH aHVarG = rC.g(str2);
                    if (a(rC, aHVarG) && (arrayList = (ArrayList) concurrentHashMapA.get(str2)) != null && !arrayList.isEmpty()) {
                        double dB = 0.0d;
                        try {
                            dB = aHVarG.b(bArr);
                        } catch (V.g e2) {
                            bH.C.c("Could not get value for OutputChannel:" + str2);
                        } catch (Exception e3) {
                            if (rC.R()) {
                                bH.C.b("Failed to get value for OutputChannel: " + aHVarG.aJ() + " using och:\n" + C0995c.d(bArr));
                            } else {
                                bH.C.b("Failed to get value for OutputChannel: " + aHVarG.aJ());
                            }
                        }
                        synchronized (arrayList) {
                            Iterator it2 = arrayList.iterator();
                            while (it2.hasNext()) {
                                try {
                                    ((InterfaceC0109co) it2.next()).setCurrentOutputChannelValue(str2, dB);
                                } catch (Exception e4) {
                                    bH.C.c("OutputChannelListener caused Error, continuing.");
                                    e4.printStackTrace();
                                }
                            }
                        }
                    }
                }
            } catch (ConcurrentModificationException e5) {
            }
        }
        h(str);
    }

    public W c() {
        return T.a();
    }

    public void a(String str, String str2, double d2) throws V.a {
        ArrayList arrayListA = a().a(str, str2);
        if (arrayListA == null) {
            throw new V.a("EcuConfiguration and/or OutputChannel not found.");
        }
        Iterator it = arrayListA.iterator();
        while (it.hasNext()) {
            ((InterfaceC0109co) it.next()).setCurrentOutputChannelValue(str2, d2);
        }
    }

    public void a(boolean z2) {
        this.f1153n = z2;
    }

    public void d() {
        this.f1156o = true;
    }

    public void e() {
        this.f1156o = true;
        Iterator it = this.f1157c.iterator();
        while (it.hasNext()) {
            R rC = c().c((String) it.next());
            if (rC != null) {
                b(rC).b();
            }
        }
    }
}
