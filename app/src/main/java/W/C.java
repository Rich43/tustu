package W;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:W/C.class */
public class C extends Thread {

    /* renamed from: a, reason: collision with root package name */
    private static C f1915a = null;

    /* renamed from: b, reason: collision with root package name */
    private List f1916b;

    private C() {
        super("File Change monitor");
        this.f1916b = Collections.synchronizedList(new ArrayList());
        setDaemon(true);
    }

    public static C a() {
        if (f1915a == null) {
            f1915a = new C();
            f1915a.start();
        }
        return f1915a;
    }

    public void a(File file) {
        this.f1916b.add(new C0175ae(file));
    }

    public boolean a(File file, B b2) {
        C0175ae c0175aeF = f(file);
        boolean z2 = false;
        if (c0175aeF == null) {
            a(file);
            c0175aeF = f(file);
            z2 = true;
        }
        c0175aeF.a(b2);
        return z2;
    }

    public void b(File file) {
        C0175ae c0175aeF = f(file);
        if (c0175aeF != null) {
            c0175aeF.h();
            this.f1916b.remove(c0175aeF);
        }
    }

    public void c(File file) {
        C0175ae c0175aeF = f(file);
        if (c0175aeF != null) {
            c0175aeF.c();
        } else {
            bH.C.d("file:" + file.getName() + " is not Monitored, cannot pause Monitoring of it.");
        }
    }

    public void d(File file) {
        C0175ae c0175aeF = f(file);
        if (c0175aeF != null) {
            c0175aeF.d();
        } else {
            bH.C.d("file:" + file.getName() + " is not Monitored, cannot unpause Monitoring of it.");
        }
    }

    public void e(File file) {
        if (g(file)) {
            return;
        }
        bH.C.d("file:" + file.getName() + " is not Monitored, cannot stop Monitoring it.");
    }

    private C0175ae f(File file) {
        for (C0175ae c0175ae : this.f1916b) {
            if (c0175ae.g().equals(file)) {
                return c0175ae;
            }
        }
        return null;
    }

    private boolean g(File file) {
        Iterator it = this.f1916b.iterator();
        while (it.hasNext()) {
            if (((C0175ae) it.next()).g().equals(file)) {
                it.remove();
                return true;
            }
        }
        return false;
    }

    private synchronized void a(int i2) {
        try {
            wait(i2);
        } catch (InterruptedException e2) {
            Logger.getLogger(C.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        while (true) {
            a(1000);
            for (C0175ae c0175ae : this.f1916b) {
                if (c0175ae.e()) {
                    c0175ae.f();
                    c0175ae.b();
                }
            }
        }
    }
}
