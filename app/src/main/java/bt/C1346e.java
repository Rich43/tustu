package bt;

import G.InterfaceC0042ab;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/* renamed from: bt.e, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bt/e.class */
public class C1346e implements G.S, InterfaceC0042ab {

    /* renamed from: d, reason: collision with root package name */
    private static C1346e f9090d = null;

    /* renamed from: a, reason: collision with root package name */
    HashMap f9091a = new HashMap();

    /* renamed from: b, reason: collision with root package name */
    List f9092b = Collections.synchronizedList(new ArrayList());

    /* renamed from: c, reason: collision with root package name */
    C1347f f9093c = null;

    private C1346e() {
    }

    public static C1346e a() {
        if (f9090d == null) {
            f9090d = new C1346e();
            for (String str : G.T.a().d()) {
                G.T.a().c(str).h().a(f9090d);
            }
            G.T.a().a(f9090d);
        }
        return f9090d;
    }

    public void a(String str, C1324bf c1324bf) {
        c(str).add(c1324bf);
        b(str);
    }

    public boolean b(String str, C1324bf c1324bf) {
        return c(str).remove(c1324bf);
    }

    public void a(String str) {
        this.f9091a.remove(str);
    }

    private List c(String str) {
        List arrayList = (List) this.f9091a.get(str);
        if (arrayList == null) {
            arrayList = new ArrayList();
            this.f9091a.put(str, arrayList);
        }
        return arrayList;
    }

    public synchronized void b(String str) {
        this.f9092b.add(str);
        if (this.f9093c != null) {
            this.f9093c.a();
            return;
        }
        this.f9093c = new C1347f(this);
        this.f9093c.a();
        this.f9093c.start();
    }

    public void b() {
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(this.f9092b);
        this.f9092b.clear();
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            try {
                for (C1324bf c1324bf : c((String) it.next())) {
                    c1324bf.a();
                    c1324bf.b();
                }
            } catch (Exception e2) {
            }
        }
    }

    @Override // G.InterfaceC0042ab
    public void a(String str, int i2, int i3, int[] iArr) {
        b(str);
    }

    @Override // G.S
    public void a(G.R r2) {
    }

    @Override // G.S
    public void b(G.R r2) {
        r2.h().b(f9090d);
    }

    @Override // G.S
    public void c(G.R r2) {
        r2.h().a(f9090d);
        a(r2.c());
    }
}
