package G;

import bH.C1018z;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/* loaded from: TunerStudioMS.jar:G/aR.class */
public class aR implements InterfaceC0042ab {

    /* renamed from: a, reason: collision with root package name */
    private static aR f635a = null;

    /* renamed from: b, reason: collision with root package name */
    private ConcurrentHashMap f636b = new ConcurrentHashMap();

    /* renamed from: c, reason: collision with root package name */
    private List f637c = new ArrayList();

    private aR() {
    }

    public static aR a() {
        if (f635a == null && C1018z.i().b()) {
            f635a = new aR();
        }
        return f635a;
    }

    public void a(cG cGVar) {
        this.f637c.add(cGVar);
    }

    public void b(cG cGVar) {
        this.f637c.remove(cGVar);
    }

    private void c(String str, String str2) {
        Iterator it = this.f637c.iterator();
        while (it.hasNext()) {
            ((cG) it.next()).a(str, str2);
        }
    }

    public void a(String str, String str2) {
        c(str, str2);
    }

    @Override // G.InterfaceC0042ab
    public void a(String str, int i2, int i3, int[] iArr) {
        a(str, i2, i3, iArr.length);
    }

    private void a(String str, int i2, int i3, int i4) {
        ConcurrentHashMap concurrentHashMapA = a(str);
        if (concurrentHashMapA == null) {
            return;
        }
        R rC = b().c(str);
        Enumeration enumerationKeys = concurrentHashMapA.keys();
        while (enumerationKeys.hasMoreElements()) {
            String str2 = (String) enumerationKeys.nextElement2();
            aM aMVarC = rC.c(str2);
            if (aMVarC != null && aMVarC.a(i2, i3, i4)) {
                Iterator it = ((List) concurrentHashMapA.get(str2)).iterator();
                while (it.hasNext()) {
                    try {
                        ((aN) it.next()).a(str, str2);
                    } catch (Exception e2) {
                        bH.C.b("Exception occured while publishing a value changed. This was caught and continued to additional listeners. Stack for debug:");
                        e2.printStackTrace();
                    }
                }
            }
        }
    }

    public void b(String str, String str2) {
        d(str, str2);
    }

    private synchronized void d(String str, String str2) {
        List list;
        ConcurrentHashMap concurrentHashMapA = a(str);
        if (concurrentHashMapA == null || str2 == null || (list = (List) concurrentHashMapA.get(str2)) == null) {
            return;
        }
        Iterator it = list.iterator();
        while (it.hasNext()) {
            try {
                ((aN) it.next()).a(str, str2);
            } catch (Exception e2) {
                bH.C.b("Exception occured while publishing a value changed. This was caught and continued to additional listeners. Stack for debug:");
                e2.printStackTrace();
            }
        }
    }

    protected ConcurrentHashMap a(String str) {
        return (ConcurrentHashMap) this.f636b.get(str);
    }

    public synchronized void a(String str, String str2, aN aNVar) {
        ConcurrentHashMap concurrentHashMapA = a(str);
        R rC = b().c(str);
        if (concurrentHashMapA == null) {
            if (rC == null) {
                throw new V.a("Can not subscribe to ECU Configuration " + str + "\n It does not appear to be loaded.");
            }
            concurrentHashMapA = new ConcurrentHashMap();
        }
        if (rC == null) {
            bH.C.d("ECU Configuration: " + str + " is no longer valid, not subscribing to " + str2);
            return;
        }
        if (rC.c(str2) == null) {
            throw new V.a("Can not subscribe to EcuParameter " + str2 + "\n It is not defined in ECU Configuration " + str + ".");
        }
        List listSynchronizedList = (List) concurrentHashMapA.get(str2);
        if (listSynchronizedList == null) {
            listSynchronizedList = Collections.synchronizedList(new ArrayList());
        }
        if (!listSynchronizedList.contains(aNVar)) {
            listSynchronizedList.add(aNVar);
        }
        concurrentHashMapA.put(str2, listSynchronizedList);
        this.f636b.put(str, concurrentHashMapA);
    }

    public synchronized void a(aN aNVar) {
        Iterator it = this.f636b.values().iterator();
        while (it.hasNext()) {
            Iterator it2 = ((ConcurrentHashMap) it.next()).values().iterator();
            while (it2.hasNext()) {
                ((List) it2.next()).remove(aNVar);
            }
        }
    }

    private W b() {
        return T.a();
    }

    public void b(String str) {
        this.f636b.remove(str);
    }
}
