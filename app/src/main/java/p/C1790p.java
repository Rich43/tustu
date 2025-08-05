package p;

import G.C0134q;
import G.T;
import bH.aa;
import com.efiAnalytics.ui.InterfaceC1565bc;
import com.efiAnalytics.ui.bV;
import com.efiAnalytics.ui.cO;
import com.efiAnalytics.ui.eJ;
import java.awt.BorderLayout;
import java.awt.Component;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;

/* renamed from: p.p, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:p/p.class */
public class C1790p extends JPanel implements InterfaceC1565bc, InterfaceC1774C {

    /* renamed from: a, reason: collision with root package name */
    aa f13235a;

    /* renamed from: b, reason: collision with root package name */
    S.l f13236b;

    /* renamed from: c, reason: collision with root package name */
    v f13237c;

    /* renamed from: d, reason: collision with root package name */
    C1781g f13238d;

    /* renamed from: e, reason: collision with root package name */
    int f13239e = -1;

    /* renamed from: f, reason: collision with root package name */
    JButton f13240f;

    /* renamed from: g, reason: collision with root package name */
    JButton f13241g;

    public C1790p(S.l lVar, aa aaVar) {
        this.f13235a = null;
        this.f13235a = aaVar;
        this.f13237c = new v(aaVar);
        this.f13236b = lVar;
        setBorder(BorderFactory.createEmptyBorder(eJ.a(10), eJ.a(10), eJ.a(10), eJ.a(10)));
        setLayout(new BorderLayout());
        Iterator it = lVar.b().iterator();
        while (it.hasNext()) {
            this.f13237c.a((S.n) it.next());
        }
        this.f13237c.getSelectionModel().addListSelectionListener(new u(this, null));
        JScrollPane jScrollPane = new JScrollPane(this.f13237c);
        this.f13237c.setFillsViewportHeight(true);
        jScrollPane.setBorder(BorderFactory.createLoweredBevelBorder());
        jScrollPane.setPreferredSize(eJ.a(320, 180));
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        jPanel.setBorder(BorderFactory.createTitledBorder(a("Action Triggers")));
        jPanel.add(BorderLayout.CENTER, jScrollPane);
        add(BorderLayout.CENTER, jPanel);
        JToolBar jToolBar = new JToolBar();
        jToolBar.setFloatable(false);
        JButton jButton = new JButton(a("New Action Trigger"), new ImageIcon(eJ.a(cO.a().a(cO.f11088d, this, 24))));
        jButton.addActionListener(new C1791q(this));
        jToolBar.add(jButton);
        this.f13240f = new JButton(a("Delete Trigger"), new ImageIcon(eJ.a(cO.a().a(cO.f11089e, this, 24))));
        this.f13240f.addActionListener(new C1792r(this));
        jToolBar.add(this.f13240f);
        this.f13240f.setEnabled(false);
        this.f13241g = new JButton(a("Test User Action"), new ImageIcon(eJ.a(cO.a().a(cO.f11111A, this, 24))));
        this.f13241g.addActionListener(new C1793s(this));
        this.f13241g.setEnabled(false);
        jPanel.add("North", jToolBar);
        this.f13238d = new C1781g(aaVar);
        this.f13238d.a(T.a().c());
        this.f13238d.setBorder(BorderFactory.createTitledBorder(a("Action Trigger Editor")));
        this.f13238d.a(new t(this));
        add("South", this.f13238d);
    }

    @Override // p.InterfaceC1774C
    public void a() {
        this.f13238d.j();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b() {
        S.n nVar = new S.n();
        this.f13237c.getSelectionModel().clearSelection();
        this.f13238d.a(nVar);
        this.f13238d.a(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c() {
        if (this.f13237c.getSelectedRow() >= 0) {
            this.f13237c.a(this.f13237c.getSelectedRow());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean d() throws NumberFormatException {
        try {
            S.n nVarF = this.f13238d.f();
            if (nVarF == null) {
                bV.d(a("Editor returned no Action Trigger."), this);
                return false;
            }
            if (this.f13238d.g() && this.f13236b.a(nVarF.a()) != null && !bV.a(a("An Action Trigger with the name already exists.") + "\n" + a("If you save this Action Trigger with this name it will replace the existing Action Trigger.") + "\n\n" + a("Are you sure you wish to replace the existing Action Trigger?"), (Component) this, true)) {
                return false;
            }
            this.f13236b.a(nVarF);
            this.f13238d.h();
            this.f13237c.a(nVarF);
            this.f13237c.a();
            if (!nVarF.c()) {
                String strC = T.a().c().c();
                nVarF.b(false);
                S.e.a().a(strC, nVarF.a());
                return true;
            }
            try {
                String strC2 = T.a().c().c();
                nVarF.b(false);
                S.e.a().a(strC2, nVarF);
                return true;
            } catch (C0134q e2) {
                Logger.getLogger(C1790p.class.getName()).log(Level.WARNING, "Failed to restart monitoring of Action Trigger", (Throwable) e2);
                return true;
            }
        } catch (d.e e3) {
            bV.d(e3.getLocalizedMessage(), this);
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void e() {
        S.n nVarB = this.f13237c.b();
        if (nVarB != null) {
            this.f13237c.a(nVarB.a());
            this.f13236b.b(nVarB.a());
            this.f13237c.a();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String a(String str) {
        return this.f13235a != null ? this.f13235a.a(str) : str;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(int i2) {
        if (this.f13238d.i() && bV.a(a("The Action Trigger currently being edited has not been saved.") + "\n" + a("Would you like to save it now?"), (Component) this.f13238d, true)) {
            if (!d()) {
                if (this.f13239e >= 0) {
                    this.f13237c.getSelectionModel().setSelectionInterval(i2, i2);
                    return;
                }
                return;
            }
            this.f13238d.h();
        }
        this.f13240f.setEnabled(true);
        this.f13241g.setEnabled(this.f13237c.getSelectedRowCount() > 0);
        this.f13239e = i2;
        this.f13238d.b(this.f13237c.a(i2));
    }

    @Override // com.efiAnalytics.ui.InterfaceC1565bc
    public void close() {
    }
}
