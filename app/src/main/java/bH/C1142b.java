package bh;

import G.R;
import G.aG;
import G.bS;
import W.C0184j;
import ac.C0491c;
import ao.C0645bi;
import ao.C0804hg;
import ao.aQ;
import bH.C;
import com.efiAnalytics.ui.InterfaceC1565bc;
import com.efiAnalytics.ui.dQ;
import com.efiAnalytics.ui.eJ;
import com.efiAnalytics.ui.fB;
import java.awt.BorderLayout;
import java.io.File;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/* renamed from: bh.b, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bh/b.class */
public class C1142b extends aQ implements aG, ac.f, InterfaceC1565bc {

    /* renamed from: n, reason: collision with root package name */
    C1154n f8098n = null;

    /* renamed from: q, reason: collision with root package name */
    private R f8099q = null;

    /* renamed from: o, reason: collision with root package name */
    File f8100o = null;

    /* renamed from: r, reason: collision with root package name */
    private int f8101r = 0;

    /* renamed from: s, reason: collision with root package name */
    private boolean f8102s = false;

    /* renamed from: p, reason: collision with root package name */
    RunnableC1151k f8103p = new RunnableC1151k(this);

    public C1142b() {
        C0491c.a().a(this);
        e(h.i.a("hideSelector", h.i.f12270q));
    }

    @Override // ao.aQ
    protected JPanel e() {
        return this.f8098n;
    }

    @Override // com.efiAnalytics.ui.InterfaceC1565bc
    public void close() {
        h();
        if (this.f8098n != null) {
            this.f8098n.close();
            this.f8098n.getParent().remove(this.f8098n);
            this.f8098n = null;
        }
        C0804hg.a().B();
    }

    @Override // ao.aQ
    public boolean b(boolean z2) {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        if (this.f8098n == null && aE.a.A() != null) {
            this.f8098n = new C1154n(w(), new dQ(aE.a.A(), "logTuningPanel"));
            if (n().r() != null) {
            }
            new fB().a(new C1143c(this));
            jPanel.add(BorderLayout.CENTER, this.f8098n);
            this.f8098n.setVisible(z2);
            String strA = h.i.a(h.i.f12293N, h.i.f12297R);
            if (strA.equals(h.i.f12295P) || strA.equals(h.i.f12294O)) {
                this.f8098n.a(this.f5143b.d());
                super.q().setBottomComponent(jPanel);
            } else {
                super.q().setBottomComponent(jPanel);
            }
            e(strA);
            this.f5156l = true;
        }
        try {
            this.f8098n.setVisible(z2);
        } catch (Exception e2) {
        }
        if (z2) {
            u();
        } else {
            q().setDividerLocation(1.0d);
        }
        h.i.c("showTuningConsole", "" + z2);
        n().a(new C1144d(this));
        return z2;
    }

    @Override // ao.aQ
    public void u() {
        if (this.f8098n == null || !h.i.a("showTuningConsole", h.i.f12269p)) {
            q().setDividerLocation(1.0d);
            return;
        }
        double dA = h.i.a(h.i.f12303X, -1.0d);
        if (dA < 0.05d || dA > 0.97d) {
            q().setDividerLocation((getWidth() - (getWidth() / 2 < this.f8098n.getPreferredSize().width ? getWidth() / 2 : this.f8098n.getPreferredSize().width)) - q().getDividerSize());
        } else {
            q().setDividerLocation(dA);
        }
    }

    public R w() {
        return this.f8099q;
    }

    public void a(R r2) {
        this.f8099q = r2;
        if (this.f8098n != null) {
            remove(this.f8098n);
            this.f8098n = null;
        }
        b(h.i.a("showTuningConsole", h.i.f12269p));
        if (ac.r.a()) {
            this.f8102s = true;
            C0491c.a().a(this);
        }
    }

    public void e(boolean z2) {
        n().a(z2);
        h.i.c("hideSelector", z2 + "");
    }

    @Override // G.aG
    public boolean a(String str, bS bSVar) {
        return true;
    }

    @Override // G.aG
    public void a(String str) {
    }

    @Override // ao.aQ, i.InterfaceC1742b
    public void a(double d2) {
        if (this.f8102s && n().r() != null && n().r().d() > 0 && n().r().d() < 500) {
            SwingUtilities.invokeLater(new RunnableC1145e(this));
            this.f8102s = false;
        }
        super.a(d2);
    }

    @Override // ac.f
    public void a(File file) {
        this.f8100o = file;
        f(true);
    }

    private int x() {
        return C0491c.a().r() + 7;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void y() {
        double dA = U.b.a(this.f8099q);
        if (dA > 0.0d) {
            int iD = C0804hg.a().r().d() - 1;
            C0184j c0184jA = C0804hg.a().r().a("Time");
            int iX = x();
            if (c0184jA != null && iD > iX + 1) {
                float fD = c0184jA.d(iD) - c0184jA.d(iX);
                int iD2 = ((int) ((dA - (c0184jA.d(iX) - c0184jA.d(0))) * ((iD - iX) / fD))) + iX;
                a(iD2);
                C.c("Predictive Scaling Graph for " + iD2 + " in time period of " + dA);
                return;
            }
            if (c0184jA == null || iD <= 0) {
                C.b("Could not get time column or stabilizationRecords not reached. currentRecord=" + iD + ", stabilizationRecords=" + iX);
                return;
            }
            int iD3 = (int) (dA * (iD / (c0184jA.d(iD) - c0184jA.d(0))));
            a(iD3);
            C.c("Predictive Scaling Graph for " + iD3 + " in time period of " + dA);
        }
    }

    @Override // ac.f
    public void c() {
        int i2 = this.f8101r;
        this.f8101r = i2 + 1;
        if (i2 == 12 + C0491c.a().r()) {
            new C1147g(this, new RunnableC1146f(this)).start();
        } else if (this.f8101r <= 0 || this.f8101r >= 500 || this.f8101r % 100 != 0) {
            if (!this.f8102s || !ac.r.a() || this.f8101r <= 20 || this.f8101r >= 500 || C0804hg.a().D() || C0804hg.a().r() == null || C0804hg.a().r().a("Time") == null) {
                C0645bi.a().c().i();
                C0645bi.a().c().repaint();
            } else {
                C0804hg.a().c(C0804hg.a().r().d() - 1);
                C0804hg.a().e();
                C.d("Playback found stopped during data log capture, restarting.");
            }
        } else if (h.i.a(h.i.f12300U, h.i.f12301V)) {
            SwingUtilities.invokeLater(this.f8103p);
        }
        if (C0804hg.a().r() == null || this.f8101r >= 50 || this.f8101r != x() + 16) {
            return;
        }
        SwingUtilities.invokeLater(this.f8103p);
    }

    @Override // ac.f
    public void d() {
        this.f8101r = 0;
        if (s() != null) {
            s().c();
        }
        if (C0804hg.a().r() == null || C0804hg.a().r().d() <= 10 || !h.i.a(h.i.f12300U, h.i.f12301V)) {
            return;
        }
        int iD = C0804hg.a().r().d();
        a(iD);
        C.c("Scaling Graph for " + iD);
        C0804hg.a().j();
        C0804hg.a().c(iD / 2);
    }

    @Override // ac.f
    public void b(String str) {
    }

    public void f(boolean z2) {
        this.f8102s = z2;
    }

    @Override // ao.aQ
    public void e(String str) {
        if (str.equals(h.i.f12294O)) {
            this.f8098n.a(this.f5143b.d());
            this.f8098n.a(0);
            this.f5143b.doLayout();
            SwingUtilities.invokeLater(new RunnableC1148h(this));
            C0804hg.a().c(true);
            q().setDividerLocation(getWidth() - eJ.a(320));
            r().setDividerLocation(1.0d);
        } else if (str.equals(h.i.f12295P)) {
            this.f8098n.a(this.f5143b.d());
            this.f8098n.a(1);
            this.f5143b.doLayout();
            SwingUtilities.invokeLater(new RunnableC1149i(this));
            C0804hg.a().c(true);
            r().setDividerLocation(1.0d);
        } else {
            this.f5143b.a(this.f5143b.d());
            this.f8098n.a(2);
            RunnableC1150j runnableC1150j = new RunnableC1150j(this);
            r().setDividerLocation(this.f5143b.getPreferredSize().height > getHeight() / 5 ? (getHeight() * 4) / 5 : (getHeight() - this.f5143b.getPreferredSize().height) - r().getDividerSize());
            SwingUtilities.invokeLater(runnableC1150j);
        }
        validate();
    }
}
