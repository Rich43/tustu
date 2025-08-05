package T;

import G.C0113cs;
import G.R;
import G.T;
import G.Y;
import bH.C;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:T/c.class */
class c extends Thread {

    /* renamed from: a, reason: collision with root package name */
    Map f1862a;

    /* renamed from: b, reason: collision with root package name */
    Object f1863b;

    /* renamed from: c, reason: collision with root package name */
    long f1864c;

    /* renamed from: d, reason: collision with root package name */
    int f1865d;

    /* renamed from: e, reason: collision with root package name */
    final /* synthetic */ a f1866e;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    c(a aVar) {
        super("Cal Monitor");
        this.f1866e = aVar;
        this.f1862a = new HashMap();
        this.f1863b = new Object();
        this.f1864c = Long.MAX_VALUE;
        this.f1865d = 250;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        while (true) {
            if (this.f1864c <= System.currentTimeMillis()) {
                for (String str : this.f1862a.keySet()) {
                    if (a(str)) {
                        ArrayList arrayList = new ArrayList(this.f1866e.a(str));
                        List listB = b(str);
                        Iterator it = arrayList.iterator();
                        while (it.hasNext()) {
                            e eVar = (e) it.next();
                            if (a(listB, eVar)) {
                                C0113cs.a().a(eVar.a(), 1.0d);
                            } else {
                                C0113cs.a().a(eVar.a(), 0.0d);
                            }
                        }
                        listB.clear();
                    }
                }
                this.f1864c = Long.MAX_VALUE;
            } else {
                synchronized (this.f1863b) {
                    try {
                        this.f1863b.wait(this.f1864c - System.currentTimeMillis());
                    } catch (InterruptedException e2) {
                        Logger.getLogger(a.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                    }
                }
            }
        }
    }

    private boolean a(String str) {
        return b(str).size() > 0;
    }

    private boolean a(List list, e eVar) {
        R rC;
        if (!list.isEmpty() && (rC = T.a().c(eVar.f1872a)) != null) {
            Y yH = rC.h();
            for (int i2 = 0; i2 < list.size(); i2++) {
                if (!eVar.a(yH)) {
                    C.d(eVar.a() + " Does not match. $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
                    return false;
                }
            }
        }
        C.d(eVar.a() + " matches. $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(d dVar) {
        b(dVar.f1867a).add(dVar);
        this.f1864c = System.currentTimeMillis() + this.f1865d;
        synchronized (this.f1863b) {
            this.f1863b.notify();
        }
    }

    private synchronized List b(String str) {
        List arrayList = (List) this.f1862a.get(str);
        if (arrayList == null) {
            arrayList = new ArrayList();
            this.f1862a.put(str, arrayList);
        }
        return arrayList;
    }
}
