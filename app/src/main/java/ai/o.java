package aI;

import G.C0113cs;
import G.C0130m;
import G.C0132o;
import G.InterfaceC0109co;
import G.R;
import G.S;
import G.T;
import G.da;
import bH.C;
import com.efiAnalytics.remotefileaccess.RemoteAccessException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.net.tftp.TFTP;

/* loaded from: TunerStudioMS.jar:aI/o.class */
public class o implements S, InterfaceC0109co {

    /* renamed from: a, reason: collision with root package name */
    R f2484a;

    /* renamed from: r, reason: collision with root package name */
    private static Map f2499r = new HashMap();

    /* renamed from: b, reason: collision with root package name */
    da f2485b = new da();

    /* renamed from: p, reason: collision with root package name */
    private boolean f2486p = true;

    /* renamed from: q, reason: collision with root package name */
    private boolean f2487q = false;

    /* renamed from: c, reason: collision with root package name */
    boolean f2488c = false;

    /* renamed from: d, reason: collision with root package name */
    List f2489d = Collections.synchronizedList(new ArrayList());

    /* renamed from: e, reason: collision with root package name */
    int f2490e = TFTP.DEFAULT_TIMEOUT;

    /* renamed from: f, reason: collision with root package name */
    long f2491f = Long.MAX_VALUE;

    /* renamed from: g, reason: collision with root package name */
    int f2492g = 20;

    /* renamed from: h, reason: collision with root package name */
    long f2493h = System.currentTimeMillis();

    /* renamed from: i, reason: collision with root package name */
    long f2494i = System.currentTimeMillis();

    /* renamed from: j, reason: collision with root package name */
    int f2495j = 10;

    /* renamed from: k, reason: collision with root package name */
    int f2496k = 1500;

    /* renamed from: l, reason: collision with root package name */
    long f2497l = System.currentTimeMillis();

    /* renamed from: m, reason: collision with root package name */
    boolean f2498m = false;

    /* renamed from: n, reason: collision with root package name */
    long f2500n = 0;

    /* renamed from: o, reason: collision with root package name */
    boolean f2501o = false;

    private o(R r2) {
        this.f2484a = null;
        this.f2484a = r2;
        e();
    }

    public static o d(R r2) {
        if (f2499r.get(r2.c()) == null) {
            f2499r.put(r2.c(), new o(r2));
        }
        return (o) f2499r.get(r2.c());
    }

    private void e() {
        try {
            C0113cs.a().a(this.f2484a.c(), d.f2436a, this);
            T.a().a(this);
        } catch (V.a e2) {
            Logger.getLogger(l.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
    }

    public synchronized C0132o a(C0130m c0130m, boolean z2, int i2) throws RemoteAccessException {
        long jCurrentTimeMillis = System.currentTimeMillis();
        if (this.f2498m) {
            a("Instruction Entering " + c0130m.aJ());
        }
        while (this.f2494i > System.currentTimeMillis()) {
            if (System.currentTimeMillis() - jCurrentTimeMillis > this.f2490e) {
                throw new RemoteAccessException("Timeout waiting for SD to become Ready while executing " + c0130m.aJ());
            }
            try {
                Thread.sleep(40L);
                if (this.f2498m) {
                    a("under processing hysteresis, sleeping.");
                }
            } catch (InterruptedException e2) {
                Logger.getLogger(o.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
        }
        if (this.f2498m) {
            a("Instruction Released for Processing. " + c0130m.aJ() + ", triggerNotReady=" + z2);
        }
        if (!z2) {
            try {
                if (this.f2498m) {
                    a("Instruction About to process " + c0130m.aJ() + ", triggerNotReady=" + z2);
                }
                C0132o c0132oA = this.f2485b.a(this.f2484a, c0130m, i2);
                if (this.f2498m) {
                    a("Instruction Completed " + c0130m.aJ() + ", triggerNotReady=" + z2);
                }
                return c0132oA;
            } catch (Throwable th) {
                if (this.f2498m) {
                    a("Instruction Completed " + c0130m.aJ() + ", triggerNotReady=" + z2);
                }
                throw th;
            }
        }
        this.f2488c = true;
        try {
            a(false);
            if (this.f2498m) {
                a("Instruction About to process. " + c0130m.aJ());
            }
            this.f2494i = System.currentTimeMillis() + this.f2495j;
            C0132o c0132oA2 = this.f2485b.a(this.f2484a, c0130m, i2);
            this.f2486p = false;
            this.f2488c = false;
            if (this.f2498m) {
                a("Instruction Completed. " + c0130m.aJ());
            }
            this.f2494i = System.currentTimeMillis() + this.f2495j;
            return c0132oA2;
        } catch (Throwable th2) {
            this.f2486p = false;
            this.f2488c = false;
            if (this.f2498m) {
                a("Instruction Completed. " + c0130m.aJ());
            }
            this.f2494i = System.currentTimeMillis() + this.f2495j;
            throw th2;
        }
    }

    public void a() {
        this.f2486p = false;
        this.f2488c = false;
        a(false);
        this.f2494i = System.currentTimeMillis() + this.f2495j;
    }

    @Override // G.InterfaceC0109co
    public void setCurrentOutputChannelValue(String str, double d2) {
        boolean z2 = (((int) d2) & d.f2438c) == d.f2438c;
        if (this.f2484a.C() instanceof bQ.l) {
            if (z2 ^ this.f2501o) {
                this.f2500n = System.currentTimeMillis() + 50;
                this.f2501o = z2;
                z2 = this.f2486p;
            } else if (this.f2500n < System.currentTimeMillis()) {
                this.f2501o = z2;
                z2 = this.f2486p;
            } else {
                this.f2501o = z2;
            }
        }
        if (!this.f2488c && !this.f2486p && z2) {
            if (this.f2498m) {
                a("SD Status Changed to Ready. Will notify after hysterisis: " + this.f2495j);
            }
            this.f2486p = true;
            this.f2491f = System.currentTimeMillis() + this.f2492g;
            this.f2494i = System.currentTimeMillis() + this.f2495j;
        } else if (this.f2486p && !z2) {
            if (this.f2498m) {
                a("SD Status Changed to Not Ready.");
            }
            this.f2486p = false;
            a(this.f2486p);
            this.f2491f = Long.MAX_VALUE;
        }
        boolean z3 = (((int) d2) & d.f2439d) == d.f2439d;
        if (!this.f2488c && this.f2487q && !z3) {
            if (this.f2498m) {
                a("SD Status Changed to Not Logging. Will notify after hysterisis");
            }
            this.f2487q = false;
            this.f2493h = System.currentTimeMillis() + this.f2496k;
        } else if (!this.f2487q && z3) {
            if (this.f2498m) {
                a("Told, SD Status Changed to Logging. ");
            }
            this.f2487q = true;
            b(this.f2487q);
            this.f2493h = Long.MAX_VALUE;
        }
        if (this.f2486p && this.f2491f < System.currentTimeMillis()) {
            a(this.f2486p);
            this.f2491f = Long.MAX_VALUE;
        }
        if (this.f2487q || this.f2493h >= System.currentTimeMillis()) {
            return;
        }
        if (this.f2498m) {
            a("publishing logging status: " + System.currentTimeMillis());
        }
        b(this.f2487q);
        this.f2493h = Long.MAX_VALUE;
    }

    private void a(String str) {
        C.c(((System.currentTimeMillis() - this.f2497l) / 1000.0f) + "s. :" + str);
    }

    public void a(q qVar) {
        this.f2489d.add(qVar);
        qVar.a(this.f2486p);
        qVar.b(this.f2487q);
    }

    public void b(q qVar) {
        this.f2489d.remove(qVar);
    }

    protected void a(boolean z2) {
        if (z2) {
            a("Notifying SD Ready.");
        } else {
            a("Notifying SD Not Ready.");
        }
        Iterator it = this.f2489d.iterator();
        while (it.hasNext()) {
            ((q) it.next()).a(z2);
        }
    }

    @Override // G.S
    public void a(R r2) {
    }

    @Override // G.S
    public void b(R r2) {
        if (r2.equals(this.f2484a)) {
            C0113cs.a().a(this);
            f2499r.remove(r2.c());
        }
    }

    @Override // G.S
    public void c(R r2) {
    }

    protected void a(int i2) {
        this.f2484a.C().d(System.currentTimeMillis() + i2);
    }

    protected void b() {
        this.f2484a.C().d(0L);
    }

    private void b(boolean z2) {
        if (z2) {
            a("Notifying SD Logging On.");
        } else {
            a("Notifying SD Logging Off.");
        }
        Iterator it = this.f2489d.iterator();
        while (it.hasNext()) {
            ((q) it.next()).b(z2);
        }
    }

    public boolean c() {
        return this.f2486p;
    }

    public boolean d() {
        return this.f2487q;
    }
}
