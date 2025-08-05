package aP;

import G.C0072be;
import G.C0076bi;
import G.C0079bl;
import G.C0088bu;
import G.InterfaceC0042ab;
import ai.C0512b;
import bt.C1324bf;
import bt.C1345d;
import bt.InterfaceC1282J;
import com.efiAnalytics.ui.InterfaceC1565bc;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Window;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import n.C1762b;
import r.C1798a;
import r.C1806i;

/* renamed from: aP.hb, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/hb.class */
public class C0394hb extends C1345d implements InterfaceC0042ab, G.dg, bt.bE, com.efiAnalytics.ui.aO {

    /* renamed from: a, reason: collision with root package name */
    G.R f3556a;

    /* renamed from: b, reason: collision with root package name */
    C0088bu f3557b;

    /* renamed from: c, reason: collision with root package name */
    C1762b f3558c;

    /* renamed from: d, reason: collision with root package name */
    C1324bf f3559d;

    /* renamed from: e, reason: collision with root package name */
    C0400hh f3560e;

    /* renamed from: f, reason: collision with root package name */
    ArrayList f3561f;

    /* renamed from: g, reason: collision with root package name */
    C0398hf f3562g;

    /* renamed from: h, reason: collision with root package name */
    C0402hj f3563h;

    /* renamed from: i, reason: collision with root package name */
    C0401hi f3564i;

    /* renamed from: j, reason: collision with root package name */
    C0399hg f3565j;

    /* renamed from: k, reason: collision with root package name */
    JScrollPane f3566k;

    /* renamed from: l, reason: collision with root package name */
    JPanel f3567l;

    /* renamed from: m, reason: collision with root package name */
    Runnable f3568m;

    public C0394hb(G.R r2, C0088bu c0088bu) {
        this(r2, c0088bu, true);
    }

    public C0394hb(G.R r2, C0088bu c0088bu, boolean z2) {
        this.f3556a = null;
        this.f3557b = null;
        this.f3558c = new C1762b();
        this.f3559d = null;
        this.f3560e = null;
        this.f3561f = null;
        this.f3562g = null;
        this.f3563h = new C0402hj(this);
        this.f3564i = new C0401hi(this);
        this.f3565j = new C0399hg(this);
        this.f3566k = null;
        this.f3567l = new JPanel();
        this.f3568m = new RunnableC0397he(this);
        this.f3556a = r2;
        this.f3557b = c0088bu;
        setLayout(new BorderLayout());
        this.f3559d = new C1324bf(r2, c0088bu);
        this.f3561f = this.f3559d.k();
        List listE = c0088bu.e();
        List listU = c0088bu.U();
        ArrayList arrayListO = c0088bu.O();
        if (c0088bu.J() > 0 || arrayListO.size() > 0 || this.f3561f.size() > 0 || ((!listE.isEmpty() && C1806i.a().a("hlk;rd;tporg;'gd")) || !listU.isEmpty())) {
            add("North", new C0240bi(r2, c0088bu, this.f3561f));
        }
        a(this.f3559d);
        if (this.f3559d.l()) {
            r2.C().f(false);
        }
        this.f3567l.setLayout(new BorderLayout());
        add(BorderLayout.CENTER, this.f3567l);
        if (C1798a.a().c(C1798a.ci, C1798a.cj) && !(c0088bu instanceof C0072be)) {
            a();
        }
        if (a(c0088bu) || c0088bu.R() == 4) {
            this.f3567l.add(BorderLayout.CENTER, this.f3559d);
        } else {
            this.f3567l.add(BorderLayout.CENTER, new JScrollPane(this.f3559d));
        }
        this.f3558c.a(this);
        JPanel jPanel = new JPanel();
        FlowLayout flowLayout = new FlowLayout();
        flowLayout.setAlignment(2);
        jPanel.setLayout(flowLayout);
        jPanel.add(this.f3558c);
        this.f3558c.d(z2);
        add("South", jPanel);
        r2.p().a((InterfaceC0042ab) this);
        j();
        r2.p().a((G.dg) this);
        this.f3560e = new C0400hh(this, this);
        bY.a().a(this.f3560e);
        r2.C().a(this.f3563h);
        r2.C().a(this.f3564i);
        this.f3558c.c(r2.C().C());
        bt.bF.a().a(this);
    }

    public void a() {
        List listP = this.f3559d.p();
        if (a(listP) && this.f3566k == null) {
            int iA = C1798a.a().a(C1798a.f13352aH, C1798a.a().o());
            this.f3566k = new JScrollPane(this.f3565j);
            this.f3566k.setPreferredSize(new Dimension(150, iA * 3));
            Iterator it = listP.iterator();
            while (it.hasNext()) {
                ((InterfaceC1282J) it.next()).a(this.f3565j);
            }
            this.f3567l.add("South", this.f3566k);
            Window windowB = com.efiAnalytics.ui.bV.b(this);
            if (windowB != null) {
                windowB.setSize(windowB.getWidth(), windowB.getHeight() + this.f3566k.getPreferredSize().height);
            } else {
                this.f3567l.validate();
            }
        }
    }

    private boolean a(List list) {
        Iterator it = list.iterator();
        while (it.hasNext()) {
            String strW = this.f3556a.w(((InterfaceC1282J) it.next()).d());
            if (strW != null && !strW.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public void b() {
        if (this.f3566k != null) {
            List listP = this.f3559d.p();
            if (listP.size() > 0) {
                Iterator it = listP.iterator();
                while (it.hasNext()) {
                    ((InterfaceC1282J) it.next()).b(this.f3565j);
                }
            }
            this.f3567l.remove(this.f3566k);
            Window windowB = com.efiAnalytics.ui.bV.b(this);
            if (windowB != null) {
                windowB.setSize(windowB.getWidth(), windowB.getHeight() - this.f3566k.getPreferredSize().height);
            } else {
                this.f3567l.validate();
            }
            this.f3566k = null;
        }
    }

    private boolean a(C0088bu c0088bu) {
        if ((c0088bu instanceof C0072be) || (c0088bu instanceof C0076bi) || (c0088bu instanceof C0079bl)) {
            return true;
        }
        if (c0088bu.H() > 0) {
            return false;
        }
        if (c0088bu.Z() > 1 && c0088bu.R() != 3) {
            return false;
        }
        Iterator itK = c0088bu.K();
        while (itK.hasNext()) {
            if (!a((C0088bu) itK.next())) {
                return false;
            }
        }
        return true;
    }

    public boolean c() {
        if (this.f3557b.O().size() > 0) {
            C0338f.a().a(this.f3556a, (String) this.f3557b.O().get(0), com.efiAnalytics.ui.bV.a(this));
            return true;
        }
        if (this.f3561f.size() <= 0) {
            return false;
        }
        C0338f.a().a((C0512b) this.f3561f.get(0), com.efiAnalytics.ui.bV.a(this));
        return true;
    }

    @Override // com.efiAnalytics.ui.aO
    public void d() {
        try {
            this.f3556a.p().d();
        } catch (V.g e2) {
            bH.C.a("Error performing redo:", e2, this);
        }
        this.f3559d.requestFocus();
        m();
    }

    @Override // com.efiAnalytics.ui.aO
    public void e() {
        try {
            this.f3556a.p().c();
        } catch (V.g e2) {
            bH.C.a("Error performing undo:", e2, this);
        }
        m();
    }

    private void m() {
        for (int i2 = 0; i2 < this.f3559d.getComponentCount(); i2++) {
            if (this.f3559d.getComponent(i2).isFocusable()) {
                this.f3559d.getComponent(i2).requestFocus();
                return;
            }
        }
    }

    @Override // com.efiAnalytics.ui.aO
    public void f() throws IllegalArgumentException {
        g();
        new C0395hc(this).start();
        m();
    }

    private void n() {
        new C0396hd(this).start();
    }

    protected void g() throws IllegalArgumentException {
        Component[] components = getComponents();
        for (int i2 = 0; i2 < components.length; i2++) {
            if (components[i2] instanceof C1324bf) {
                a((C1324bf) components[i2]);
            }
        }
    }

    private void a(C1324bf c1324bf) throws IllegalArgumentException {
        Component[] components = c1324bf.getComponents();
        for (int i2 = 0; i2 < components.length; i2++) {
            if (components[i2] instanceof bt.aT) {
                ((bt.aT) components[i2]).h();
            } else if (components[i2] instanceof C1324bf) {
                a((C1324bf) components[i2]);
            }
        }
    }

    public boolean h() {
        return this.f3559d != null && this.f3559d.n();
    }

    @Override // com.efiAnalytics.ui.aO
    public void i() throws IllegalArgumentException {
        this.f3563h.f3579a = true;
        List listP = this.f3559d.p();
        if (listP.size() > 0) {
            Iterator it = listP.iterator();
            while (it.hasNext()) {
                ((InterfaceC1282J) it.next()).b(this.f3565j);
            }
        }
        bt.bF.a().b(this);
        if (this.f3559d.l()) {
            f();
            C0338f.a().a(this.f3556a, G.bL.b(this.f3556a, this.f3557b));
            n();
            f();
        } else if (!C1798a.a().a(C1798a.cl, "true").equals("false")) {
            f();
        }
        this.f3556a.p().b((InterfaceC0042ab) this);
        this.f3556a.C().c(this.f3563h);
        this.f3556a.C().b(this.f3564i);
        l();
        bY.a().b(this.f3560e);
        a(this);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void a(Component component) {
        if (component instanceof InterfaceC1565bc) {
            ((InterfaceC1565bc) component).close();
        }
        if (component instanceof Container) {
            Container container = (Container) component;
            for (int i2 = 0; i2 < container.getComponentCount(); i2++) {
                a(container.getComponent(i2));
            }
        }
    }

    public void j() {
        if (this.f3562g != null) {
            this.f3562g.a();
            return;
        }
        this.f3562g = new C0398hf(this);
        this.f3562g.a();
        this.f3562g.start();
    }

    public void k() {
        if (this.f3559d != null) {
            this.f3559d.a();
        }
    }

    @Override // G.InterfaceC0042ab
    public void a(String str, int i2, int i3, int[] iArr) {
        if (str.equals(this.f3556a.c())) {
            j();
        }
    }

    @Override // G.dg
    public void a(boolean z2) {
        this.f3558c.a(z2);
    }

    @Override // G.dg
    public void b(boolean z2) {
        this.f3558c.b(z2);
    }

    @Override // bt.bE
    public void c(boolean z2) {
        if (z2) {
            a();
        } else {
            b();
        }
    }
}
