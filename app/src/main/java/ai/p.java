package aI;

import G.C0130m;
import G.C0132o;
import G.R;
import G.df;
import bH.C0995c;
import bH.L;
import com.efiAnalytics.remotefileaccess.RemoteAccessException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:aI/p.class */
public class p implements q {

    /* renamed from: a, reason: collision with root package name */
    R f2502a;

    /* renamed from: b, reason: collision with root package name */
    boolean f2503b = false;

    /* renamed from: c, reason: collision with root package name */
    boolean f2504c = true;

    /* renamed from: d, reason: collision with root package name */
    int f2505d = 500;

    /* renamed from: e, reason: collision with root package name */
    o f2506e = null;

    public p(R r2) {
        this.f2502a = null;
        this.f2502a = r2;
        h();
    }

    private void h() {
        this.f2506e = o.d(this.f2502a);
        this.f2506e.a(this);
    }

    public void a() {
        this.f2506e.b(this);
    }

    public void b() throws w {
        i();
        try {
            C0130m c0130mA = d.a(this.f2502a.O(), 3);
            c0130mA.v("SD Start Logging");
            C0132o c0132oA = this.f2506e.a(c0130mA, false, this.f2505d);
            if (c0132oA.a() == 3) {
                throw new w(c0132oA.c());
            }
        } catch (x e2) {
            Logger.getLogger(p.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        } catch (RemoteAccessException e3) {
            throw new w(e3.getMessage());
        }
    }

    public void c() throws w {
        if (!this.f2502a.R()) {
            throw new w(a("Not currently connected to an MS3."));
        }
        if (!this.f2504c) {
            throw new w(a("MS3 is not currently capturing an SD Log, can not stop."));
        }
        try {
            C0130m c0130mA = d.a(this.f2502a.O(), 2);
            c0130mA.v("SD Stop Logging");
            C0132o c0132oA = this.f2506e.a(c0130mA, true, this.f2505d);
            if (c0132oA.a() == 3) {
                throw new w(c0132oA.c());
            }
        } catch (x e2) {
            Logger.getLogger(p.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        } catch (RemoteAccessException e3) {
            throw new w(e3.getMessage());
        }
    }

    public void d() throws w {
        i();
        try {
            C0130m c0130mA = d.a(this.f2502a.O(), 0);
            c0130mA.v("SD Reset Return to Normal");
            C0132o c0132oA = this.f2506e.a(c0130mA, true, this.f2505d);
            if (c0132oA.a() == 3) {
                throw new w(c0132oA.c());
            }
        } catch (x e2) {
            Logger.getLogger(p.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        } catch (RemoteAccessException e3) {
            throw new w(e3.getMessage());
        }
    }

    public void e() throws w {
        i();
        try {
            C0130m c0130mA = d.a(this.f2502a.O(), 5);
            c0130mA.v("SD Read Start Logging");
            C0132o c0132oA = this.f2506e.a(c0130mA, true, this.f2505d);
            if (c0132oA.a() == 3) {
                throw new w(c0132oA.c());
            }
        } catch (x e2) {
            Logger.getLogger(p.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        } catch (RemoteAccessException e3) {
            throw new w(e3.getMessage());
        }
    }

    public void f() throws w {
        i();
        try {
            C0130m c0130mA = d.a(this.f2502a.O(), 1);
            c0130mA.v("SD Reset & wait");
            C0132o c0132oA = this.f2506e.a(c0130mA, true, this.f2505d);
            if (c0132oA.a() == 3) {
                throw new w(c0132oA.c());
            }
        } catch (x e2) {
            Logger.getLogger(p.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        } catch (RemoteAccessException e3) {
            throw new w(e3.getMessage());
        }
    }

    public void a(Date date) throws RemoteAccessException {
        ArrayList arrayList = new ArrayList();
        arrayList.add(d.f(this.f2502a.O()));
        arrayList.add(d.a(this.f2502a.O(), date));
        C0130m c0130mA = C0130m.a(this.f2502a.O(), arrayList);
        c0130mA.v("RTC Set Command ");
        C0132o c0132oA = this.f2506e.a(c0130mA, false, 1500);
        if (c0132oA.a() == 3) {
            throw new RemoteAccessException(c0132oA.c());
        }
    }

    public Date g() throws RemoteAccessException {
        C0132o c0132oA = this.f2506e.a(d.e(this.f2502a.O()), false, 2000);
        if (c0132oA.a() == 3) {
            throw new RemoteAccessException(c0132oA.c());
        }
        if (c0132oA.a() == 2 && c0132oA.e() == null) {
            throw new RemoteAccessException(c0132oA.c());
        }
        int[] iArrE = c0132oA.e();
        Calendar calendar = Calendar.getInstance();
        calendar.set(1, C0995c.b(iArrE, 6, 2, true, false));
        calendar.set(2, C0995c.b(iArrE, 5, 1, true, false) - 1);
        calendar.set(5, C0995c.b(iArrE, 4, 1, true, false));
        calendar.set(11, C0995c.b(iArrE, 2, 1, true, false));
        calendar.set(12, C0995c.b(iArrE, 1, 1, true, false));
        calendar.set(13, C0995c.b(iArrE, 0, 1, true, false));
        return calendar.getTime();
    }

    public void a(u uVar, L l2) {
        try {
            new n(this.f2502a).a(uVar, l2);
            try {
                if (this.f2502a.O().T()) {
                    df.b(this.f2502a);
                }
            } catch (V.g e2) {
                Logger.getLogger(p.class.getName()).log(Level.WARNING, "Failed to stop turbo baud.", (Throwable) e2);
            }
        } catch (Throwable th) {
            try {
                if (this.f2502a.O().T()) {
                    df.b(this.f2502a);
                }
            } catch (V.g e3) {
                Logger.getLogger(p.class.getName()).log(Level.WARNING, "Failed to stop turbo baud.", (Throwable) e3);
            }
            throw th;
        }
    }

    private void i() throws w {
        if (!this.f2502a.R()) {
            throw new w(a("Not currently connected to an MS3."));
        }
    }

    private String a(String str) {
        try {
            return C.a.a().a("", str);
        } catch (C.b e2) {
            Logger.getLogger(p.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            return str;
        }
    }

    public void a(q qVar) {
        this.f2506e.a(qVar);
    }

    public void b(q qVar) {
        this.f2506e.b(qVar);
    }

    @Override // aI.q
    public void a(boolean z2) {
        this.f2503b = z2;
    }

    @Override // aI.q
    public void b(boolean z2) {
        this.f2504c = z2;
    }
}
