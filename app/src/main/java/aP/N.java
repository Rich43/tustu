package aP;

import W.C0200z;
import java.awt.HeadlessException;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:aP/N.class */
public class N extends Thread {

    /* renamed from: a, reason: collision with root package name */
    ArrayList f2762a;

    /* renamed from: b, reason: collision with root package name */
    int f2763b;

    /* renamed from: c, reason: collision with root package name */
    String[] f2764c;

    /* renamed from: d, reason: collision with root package name */
    G.R f2765d;

    /* renamed from: e, reason: collision with root package name */
    boolean f2766e;

    /* renamed from: f, reason: collision with root package name */
    boolean f2767f;

    /* renamed from: g, reason: collision with root package name */
    int f2768g;

    /* renamed from: h, reason: collision with root package name */
    long f2769h;

    public N(G.R r2) {
        super("AutomatedMsqLoadTest");
        this.f2762a = new ArrayList();
        this.f2763b = 0;
        this.f2764c = new String[]{"", ""};
        this.f2765d = null;
        this.f2766e = false;
        this.f2767f = true;
        this.f2768g = 1000;
        this.f2769h = System.currentTimeMillis();
        this.f2765d = r2;
        setDaemon(true);
        r2.C().a(new O(this));
    }

    public void a(P p2) {
        this.f2762a.add(p2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(int i2) {
        Iterator it = this.f2762a.iterator();
        while (it.hasNext()) {
            ((P) it.next()).a(i2, this.f2763b);
        }
    }

    public void a(File file) {
        this.f2764c[0] = file.getAbsolutePath();
    }

    public void b(File file) {
        this.f2764c[1] = file.getAbsolutePath();
    }

    public boolean a() throws V.a {
        if (this.f2764c[0] == null || !new File(this.f2764c[0]).exists()) {
            throw new V.a("File 1 is not a valid file.");
        }
        if (this.f2764c[1] == null || !new File(this.f2764c[1]).exists()) {
            throw new V.a("File 2 is not a valid file.");
        }
        String strA = C0200z.a(new File(this.f2764c[0]));
        String strA2 = C0200z.a(new File(this.f2764c[1]));
        if (strA == null || strA.equals("")) {
            throw new V.a("No signature found in file 1.");
        }
        if (strA2 == null || strA2.equals("")) {
            throw new V.a("No signature found in file 2.");
        }
        return true;
    }

    public void b() {
        this.f2766e = true;
        bH.C.d("msqTester Asked to stop.");
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() throws HeadlessException {
        int i2 = 0;
        boolean zV = this.f2765d.O().v();
        this.f2765d.O().b(true);
        while (!this.f2766e) {
            if (this.f2767f) {
                this.f2767f = false;
                this.f2769h = System.currentTimeMillis();
                String str = this.f2764c[i2 % this.f2764c.length];
                K.b bVar = new K.b();
                this.f2765d.p().a(bVar);
                C0338f.a().a(cZ.a().c(), this.f2765d, str);
                this.f2763b = bVar.a();
                this.f2765d.p().b(bVar);
                if (this.f2763b == 0) {
                    this.f2767f = true;
                } else {
                    bH.C.d("!!!!!!!!!!!! Loaded file " + i2);
                }
                i2++;
            }
            try {
                Thread.sleep(this.f2768g);
            } catch (InterruptedException e2) {
                Logger.getLogger(N.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
        }
        this.f2765d.O().b(zV);
    }
}
