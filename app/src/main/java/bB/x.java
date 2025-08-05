package bb;

import G.C0129l;
import G.C0130m;
import G.R;
import G.T;
import G.bS;
import aP.C0338f;
import af.InterfaceC0504a;
import bH.C0995c;
import bH.C1011s;
import bH.W;
import com.efiAnalytics.ui.InterfaceC1565bc;
import com.efiAnalytics.ui.bV;
import com.efiAnalytics.ui.eJ;
import com.efiAnalytics.ui.fS;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JPanel;
import org.apache.commons.math3.optimization.direct.CMAESOptimizer;
import r.C1798a;
import r.C1807j;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:bb/x.class */
public class x extends JPanel implements aE.e, InterfaceC1565bc, fS {

    /* renamed from: c, reason: collision with root package name */
    C1046k f7841c;

    /* renamed from: q, reason: collision with root package name */
    private static final File f7849q = new File(C1807j.C(), "firmwareLoader/tuneRestore.html");

    /* renamed from: r, reason: collision with root package name */
    private static final File f7850r = new File(C1807j.C(), "firmwareLoader/tuneRestoring.html");

    /* renamed from: s, reason: collision with root package name */
    private static final File f7851s = new File(C1807j.C(), "firmwareLoader/tuneRestored.html");

    /* renamed from: t, reason: collision with root package name */
    private static final File f7852t = new File(C1807j.C(), "firmwareLoader/tuneRestoredNoPC.html");

    /* renamed from: a, reason: collision with root package name */
    File f7839a = null;

    /* renamed from: b, reason: collision with root package name */
    R f7840b = null;

    /* renamed from: d, reason: collision with root package name */
    JButton f7842d = new JButton(C1818g.b("Restore Project Tune Settings"));

    /* renamed from: e, reason: collision with root package name */
    C1026C f7843e = new C1026C(this);

    /* renamed from: f, reason: collision with root package name */
    C1027D f7844f = new C1027D(this);

    /* renamed from: g, reason: collision with root package name */
    C1024A f7845g = new C1024A(this);

    /* renamed from: h, reason: collision with root package name */
    boolean f7846h = false;

    /* renamed from: i, reason: collision with root package name */
    boolean f7847i = true;

    /* renamed from: j, reason: collision with root package name */
    boolean f7848j = true;

    /* renamed from: k, reason: collision with root package name */
    ae.k f7853k = null;

    /* renamed from: l, reason: collision with root package name */
    ae.q f7854l = null;

    /* renamed from: m, reason: collision with root package name */
    ae.p f7855m = null;

    /* renamed from: n, reason: collision with root package name */
    long f7856n = 0;

    /* renamed from: o, reason: collision with root package name */
    int f7857o = CMAESOptimizer.DEFAULT_MAXITERATIONS;

    /* renamed from: p, reason: collision with root package name */
    boolean f7858p = false;

    public x() {
        setLayout(new BorderLayout());
        this.f7841c = new C1046k("", false);
        this.f7841c.a(true);
        add(BorderLayout.CENTER, this.f7841c);
        this.f7841c.setPreferredSize(eJ.a(600, 250));
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new FlowLayout(2));
        jPanel.add(this.f7842d);
        add("South", jPanel);
        this.f7842d.addActionListener(new y(this));
    }

    private void a(File file) {
        try {
            this.f7841c.a(file);
        } catch (V.a e2) {
            Logger.getLogger(x.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            bV.d(e2.getMessage(), this);
        }
    }

    @Override // com.efiAnalytics.ui.InterfaceC1565bc
    public void close() {
        C0338f.a().b(this);
        C1798a.a().b(C1798a.f13391bu, Boolean.toString(this.f7847i));
        C1798a.a().b(C1798a.f13393bw, Boolean.toString(this.f7848j));
        C0338f.a().c(false);
        if (this.f7840b != null) {
            this.f7840b.b(this.f7843e);
            this.f7840b.b(this.f7844f);
            this.f7840b.C().b(this.f7845g);
        }
    }

    public boolean a() {
        aE.a aVarA = aE.a.A();
        if (aVarA == null) {
            bH.C.c("No Project Loaded, can not save calibration.");
            return false;
        }
        this.f7840b = T.a().c();
        if (this.f7840b == null) {
            bH.C.d("No Main Configuration, can not save calibration.");
            return false;
        }
        if (this.f7840b.h().h()) {
            bH.C.d("No Calibration Loaded, can not save calibration.");
            return false;
        }
        this.f7839a = new File(aVarA.t(), "PreFirmwareUpgrade_" + W.a(new Date()) + "." + C1798a.cw);
        C0338f.a().e(this.f7840b, this.f7839a.getAbsolutePath());
        this.f7839a.deleteOnExit();
        W.D.a().a(this.f7839a);
        return true;
    }

    private void a(ae.k kVar, bS bSVar) throws V.a {
        File fileA = kVar.a(bSVar);
        if (fileA == null) {
            bV.d("Unable to update project ECU Definition file. No proper match found in this firmware package.\nThe proper ECU Definition will be required on connect before reloading your tune.", this);
            return;
        }
        aE.a aVarA = aE.a.A();
        if (aVarA == null) {
            throw new V.a("Unable to update project ECU Definition file. No Project open.");
        }
        C1011s.a(fileA, new File(aVarA.v()));
        for (File file : kVar.c()) {
            C1011s.a(file, new File(C1807j.c(), file.getName()));
        }
    }

    private void f() {
        this.f7847i = C1798a.a().c(C1798a.f13391bu, C1798a.f13392bv);
        C1798a.a().b(C1798a.f13391bu, Boolean.toString(false));
        this.f7848j = C1798a.a().c(C1798a.f13393bw, true);
        C1798a.a().b(C1798a.f13393bw, Boolean.toString(true));
        C0338f.a().c(true);
        aE.a.c(false);
        C0338f.a().z();
    }

    public void c() {
        this.f7842d.setEnabled(false);
        a(f7852t);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void g() throws HeadlessException {
        this.f7840b.C().a(this.f7845g);
        C0338f.a().c(false);
        C0338f.a().a(bV.b(this), this.f7840b, this.f7839a.getAbsolutePath());
    }

    public void a(ae.k kVar, ae.q qVar, ae.p pVar) {
        this.f7853k = kVar;
        this.f7854l = qVar;
        this.f7855m = pVar;
        this.f7841c.a(qVar);
        a(f7849q);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void h() {
        a(f7850r);
        this.f7842d.setEnabled(false);
        if (this.f7854l.g()) {
            c();
        }
        new z(this).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean i() throws V.a {
        if (!this.f7854l.g()) {
            this.f7856n = System.currentTimeMillis();
        }
        this.f7858p = true;
        C0338f.a().a(this);
        if (this.f7839a == null) {
            throw new V.a("There is no saved calibration, can not load it.");
        }
        if (this.f7840b == null) {
            throw new V.a("There is no Main Configuration set, can not load calibration.");
        }
        this.f7840b.C().a(this.f7845g);
        this.f7840b.O().b(false);
        bS bSVarA = null;
        try {
            try {
                try {
                    if (this.f7855m.a().k() != 3) {
                        this.f7855m.a().f();
                    }
                    for (int i2 = 0; i2 < 3; i2++) {
                        bSVarA = this.f7854l.a(this.f7855m);
                        if (bSVarA != null && bSVarA.a().length != 3) {
                            break;
                        }
                        bV.d(C1818g.b("Is your Controller powered and boot jumper removed?") + "\n" + C1818g.b("If so, try power cycling your controller."), this);
                    }
                    if (this.f7855m.a().k() == 3) {
                        this.f7855m.a().g();
                    }
                    if (bSVarA == null) {
                        throw new V.a("Unable to read serial signature, can not restore calibration.");
                    }
                    a(this.f7853k, bSVarA);
                    if (T.a().c() != null) {
                        T.a().c().C().c();
                    }
                    f();
                    return true;
                } catch (C0129l e2) {
                    Logger.getLogger(x.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                    throw new V.a("Unable to open connection, can not restore calibration.");
                }
            } catch (IOException e3) {
                Logger.getLogger(x.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
                throw new V.a("Unable to read serial signature, can not restore calibration.");
            }
        } catch (Throwable th) {
            if (this.f7855m.a().k() == 3) {
                this.f7855m.a().g();
            }
            throw th;
        }
    }

    @Override // com.efiAnalytics.ui.fS
    public boolean g_() {
        return true;
    }

    public boolean d() {
        return System.currentTimeMillis() - this.f7856n < ((long) this.f7857o);
    }

    private void a(R r2) {
        if (this.f7840b != null) {
            this.f7840b.b(this.f7843e);
            this.f7840b.b(this.f7844f);
        }
        this.f7840b = r2;
        if (r2 != null) {
            r2.a(this.f7843e);
            this.f7840b.a(this.f7844f);
        }
    }

    @Override // aE.e
    public synchronized void a(aE.a aVar, R r2) {
        a(r2);
        notify();
    }

    @Override // aE.e
    public void e_() {
    }

    @Override // aE.e
    public void a(aE.a aVar) {
    }

    public boolean e() {
        return this.f7858p;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void j() throws IOException {
        String strI = this.f7840b.i();
        if (!strI.startsWith("MS2Extra") && !strI.startsWith("MS3 Format")) {
            a(f7851s);
            return;
        }
        this.f7840b.C().d(System.currentTimeMillis() + 2000);
        C0130m c0130mA = C0130m.a(this.f7840b.O(), C0995c.b(InterfaceC0504a.f4434a));
        c0130mA.a(true);
        c0130mA.b(0);
        C0130m c0130mA2 = C0130m.a(this.f7840b.O(), new int[]{13});
        c0130mA2.a(true);
        c0130mA2.b(3);
        c0130mA2.i(50);
        c0130mA2.a(1000);
        C0130m c0130mA3 = C0130m.a(this.f7840b.O(), new int[]{180});
        c0130mA3.a(true);
        c0130mA3.b(0);
        this.f7840b.C().b(c0130mA);
        this.f7840b.C().b(c0130mA2);
        this.f7840b.C().b(c0130mA3);
        a(f7852t);
    }
}
