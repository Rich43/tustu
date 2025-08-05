package aH;

import G.C0130m;
import G.C0132o;
import G.F;
import G.R;
import G.da;
import bH.C;
import bH.C0995c;
import bH.W;
import bQ.l;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:aH/b.class */
public class b {

    /* renamed from: c, reason: collision with root package name */
    private static b f2411c = null;

    /* renamed from: d, reason: collision with root package name */
    private final Object f2412d = new Object();

    /* renamed from: e, reason: collision with root package name */
    private boolean f2413e = false;

    /* renamed from: a, reason: collision with root package name */
    List f2414a = null;

    /* renamed from: b, reason: collision with root package name */
    List f2415b = new ArrayList();

    public static b a() {
        if (f2411c == null) {
            f2411c = new b();
        }
        return f2411c;
    }

    public void a(d dVar) {
        this.f2415b.add(dVar);
    }

    public void b(d dVar) {
        this.f2415b.add(dVar);
    }

    private void a(List list) {
        Iterator it = this.f2415b.iterator();
        while (it.hasNext()) {
            ((d) it.next()).a(list);
        }
    }

    public void a(R r2) {
        if (this.f2413e || !r2.b()) {
            return;
        }
        new c(this, r2).start();
    }

    public List b(R r2) {
        if (this.f2413e) {
            b();
        } else {
            this.f2413e = true;
            this.f2414a = c(r2);
            this.f2413e = false;
            a(this.f2414a);
            c();
        }
        return this.f2414a;
    }

    private void b() {
        if (this.f2413e) {
            synchronized (this.f2412d) {
                if (this.f2413e) {
                    try {
                        this.f2412d.wait(10000L);
                    } catch (InterruptedException e2) {
                        Logger.getLogger(b.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                    }
                }
            }
        }
    }

    private void c() {
        synchronized (this.f2412d) {
            this.f2412d.notifyAll();
        }
    }

    private List c(R r2) {
        ArrayList arrayList = new ArrayList();
        if (r2.R()) {
            da daVar = new da();
            for (int i2 = 0; i2 <= 14; i2++) {
                if (i2 != r2.O().x()) {
                    a aVar = new a();
                    aVar.a(i2);
                    C0130m c0130mA = C0130m.a(r2.O(), C0995c.b(F.d("r\\0\\x0f\\x00\\x00\\x00\\x14", i2)));
                    c0130mA.a(150);
                    c0130mA.b(20);
                    c0130mA.i(10);
                    c0130mA.b(true);
                    c0130mA.c(false);
                    c0130mA.v("Query Signature CAN ID " + i2);
                    if (i2 == 11) {
                    }
                    C0132o c0132oA = daVar.a(r2, c0130mA, 500);
                    if (c0132oA.a() == 1) {
                        aVar.b(W.a(c0132oA.g()));
                        aVar.a(true);
                        int[] iArrB = C0995c.b(F.d("r\\0\\x0e\\x00\\x00\\x00\\x3c", i2));
                        C0130m c0130mA2 = C0130m.a(r2.O(), iArrB);
                        c0130mA2.a(240);
                        c0130mA2.b(iArrB[iArrB.length - 1]);
                        c0130mA2.i(30);
                        c0130mA2.b(true);
                        c0130mA2.c(false);
                        c0130mA2.v("Query Info CAN ID " + i2);
                        C0132o c0132oA2 = daVar.a(r2, c0130mA2, 500);
                        if (c0132oA2.a() == 1) {
                            aVar.a(W.a(c0132oA2.g()));
                        }
                    } else {
                        C.c("CAN ID: " + i2 + ", " + c0132oA.c());
                        aVar.a("Not Detected");
                        aVar.a(false);
                    }
                    if (i2 == 12) {
                    }
                    arrayList.add(aVar);
                }
            }
        } else if (r2.C() instanceof l) {
            for (int i3 = 0; i3 <= 14; i3++) {
                if (i3 != r2.O().x()) {
                    a aVar2 = new a();
                    aVar2.a(i3);
                    aVar2.a("Unsupported on Dash Echo");
                    aVar2.a(false);
                }
            }
        } else {
            for (int i4 = 0; i4 <= 14; i4++) {
                if (i4 != r2.O().x()) {
                    a aVar3 = new a();
                    aVar3.a(i4);
                    aVar3.a(false);
                }
            }
        }
        return arrayList;
    }
}
