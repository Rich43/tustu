package G;

import L.C0157n;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:G/T.class */
public class T implements W {

    /* renamed from: a, reason: collision with root package name */
    HashMap f494a = new HashMap();

    /* renamed from: b, reason: collision with root package name */
    R f495b = null;

    /* renamed from: c, reason: collision with root package name */
    List f496c = new CopyOnWriteArrayList(new ArrayList());

    /* renamed from: d, reason: collision with root package name */
    U f497d = new U(this);

    /* renamed from: e, reason: collision with root package name */
    private static T f498e = null;

    private T() {
    }

    public static T a() {
        if (f498e == null) {
            f498e = new T();
        }
        return f498e;
    }

    public void a(R r2) {
        bH.C.c("Adding Configuration: " + r2.c());
        this.f494a.put(r2.c(), r2);
        d(r2);
        if (r2.C() != null) {
            r2.C().a(C0113cs.a());
            r2.C().a(S.e.a());
            C0113cs.a().a("controllerOnline", S.e.a());
        }
        I.f fVar = new I.f();
        C0113cs.a().d(I.f.f1358a);
        if (r2.C() != null) {
            r2.C().a((aF) fVar);
            r2.C().a((aG) fVar);
        }
        I.i iVar = new I.i();
        C0113cs.a().d("interrogationProgress");
        r2.a(iVar);
        if (r2.C() != null) {
            I.g gVar = new I.g();
            C0113cs.a().d("controllerOnline");
            C0113cs.a().a("controllerOnline", r2.C().q() ? 1.0d : 0.0d);
            r2.C().a(gVar);
        }
        if (r2.O().Y() != null) {
            try {
                new cQ(r2);
            } catch (V.a e2) {
                aB.a().a("Failed to add Scattered Reset Manager");
                Logger.getLogger(T.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
        }
        r2.p().a(aR.a());
        r2.a(new K.e(r2));
        if (r2.C() != null && C0117cw.b(r2.c())) {
            C0117cw.a(r2).a();
        }
        b(r2);
    }

    public void a(String str) throws V.a {
        R r2 = (R) this.f494a.get(str);
        if (r2 == null) {
            throw new V.a(str + " is not a currently loaded configuration.");
        }
        this.f495b = r2;
        if (r2.C() != null) {
            r2.C().a(this.f497d);
        } else {
            bH.C.d("EcuCommunicationManager not initialized.");
        }
        e();
    }

    public void b(String str) {
        bH.C.c("removing Configuration: " + str);
        R rC = c(str);
        if (rC == null) {
            return;
        }
        C0113cs.a().i(str);
        if (rC.C() != null) {
            rC.C().J();
        }
        if (rC.O() != null) {
            rC.O().a();
        }
        rC.p().b(aR.a());
        if (rC.C() != null) {
            rC.C().c(C0113cs.a());
            J.c(str);
        }
        S.b.a().a(str);
        S.e.a().b(str);
        if (rC.C() != null) {
            rC.C().c(this.f497d);
        }
        this.f494a.remove(str);
        if (this.f495b != null && this.f495b.equals(rC)) {
            this.f495b = null;
        }
        c(rC);
        aR.a().b(str);
        A.v.a().a(str);
        bQ.j.a().a(str);
        if (C0117cw.b(rC.c())) {
            C0117cw.c(rC.c());
        }
        rC.T();
        C0157n.a().a(0);
    }

    public void b() {
        for (Object obj : this.f494a.keySet().toArray()) {
            b((String) obj);
        }
    }

    @Override // G.W
    public R c(String str) {
        return (R) this.f494a.get(str);
    }

    public R c() {
        return this.f495b;
    }

    public String[] d() {
        Object[] array = this.f494a.keySet().toArray();
        String[] strArr = new String[array.length];
        for (int i2 = 0; i2 < array.length; i2++) {
            strArr[i2] = (String) array[i2];
            if (i2 > 0 && this.f495b != null && strArr[i2].equals(this.f495b.c())) {
                strArr[i2] = strArr[0];
                strArr[0] = this.f495b.c();
            }
        }
        return strArr;
    }

    public void a(S s2) {
        if (this.f496c.contains(s2)) {
            return;
        }
        this.f496c.add(s2);
    }

    private void b(R r2) {
        Iterator it = this.f496c.iterator();
        while (it.hasNext()) {
            ((S) it.next()).c(r2);
        }
    }

    private void c(R r2) {
        Iterator it = this.f496c.iterator();
        while (it.hasNext()) {
            try {
                ((S) it.next()).b(r2);
            } catch (Exception e2) {
            }
        }
    }

    private void e() {
        Iterator it = this.f496c.iterator();
        while (it.hasNext()) {
            try {
                ((S) it.next()).a(c());
            } catch (Exception e2) {
                bH.C.c("Exception caught notifying Working Configuration Listeners");
                e2.printStackTrace();
            }
        }
    }

    private void d(R r2) {
        for (String str : r2.k()) {
            a(r2.c(), r2.c(str));
        }
    }

    private void a(String str, aM aMVar) {
        String[] strArrB;
        String[] strArrB2;
        if ((aMVar.E() instanceof bQ) && (strArrB2 = ((bQ) aMVar.E()).b()) != null) {
            for (String str2 : strArrB2) {
                try {
                    aR.a().a(str, str2, new V(this, aMVar.aJ()));
                } catch (V.a e2) {
                    Logger.getLogger(T.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                    bH.C.a("Unable to subscribe to " + aMVar.aJ() + " for changes that impact scale.", e2, null);
                }
            }
        }
        if (!(aMVar.F() instanceof bQ) || (strArrB = ((bQ) aMVar.F()).b()) == null) {
            return;
        }
        for (String str3 : strArrB) {
            try {
                aR.a().a(str, str3, new V(this, aMVar.aJ()));
            } catch (V.a e3) {
                Logger.getLogger(T.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
                bH.C.a("Unable to subscribe to " + aMVar.aJ() + " for changes that impact scale.", e3, null);
            }
        }
    }
}
