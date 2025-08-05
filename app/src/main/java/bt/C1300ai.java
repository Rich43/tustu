package bt;

import G.C0078bk;
import G.C0113cs;
import G.C0126i;
import G.C0130m;
import G.C0134q;
import bH.C1007o;
import com.efiAnalytics.ui.InterfaceC1565bc;
import com.efiAnalytics.ui.dF;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Window;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JPanel;
import s.C1818g;

/* renamed from: bt.ai, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bt/ai.class */
public class C1300ai extends JPanel implements InterfaceC1349h, InterfaceC1565bc {

    /* renamed from: a, reason: collision with root package name */
    G.R f8835a;

    /* renamed from: b, reason: collision with root package name */
    C0078bk f8836b;

    /* renamed from: c, reason: collision with root package name */
    JButton f8837c;

    /* renamed from: d, reason: collision with root package name */
    C1302ak f8838d;

    /* renamed from: e, reason: collision with root package name */
    String f8839e;

    /* renamed from: f, reason: collision with root package name */
    boolean f8840f = false;

    public C1300ai(G.R r2, C0078bk c0078bk) {
        this.f8835a = null;
        this.f8836b = null;
        this.f8839e = null;
        this.f8835a = r2;
        this.f8836b = c0078bk;
        this.f8839e = toString();
        setLayout(new FlowLayout(1));
        this.f8837c = new JButton(C1818g.b(c0078bk.l()));
        this.f8837c.addActionListener(new C1301aj(this));
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout(4, 4));
        jPanel.add(BorderLayout.CENTER, this.f8837c);
        add(jPanel);
        String strW = r2.w(c0078bk.a());
        if (strW != null && !strW.isEmpty()) {
            this.f8837c.setToolTipText(strW);
            jPanel.add("East", new C1353l(r2, c0078bk.a()));
        }
        if (c0078bk.aH() == null || c0078bk.aH().trim().equals("")) {
            return;
        }
        try {
            C0126i.b(this.f8839e, r2.c(), c0078bk.aH());
        } catch (C0134q e2) {
            Logger.getLogger(C1300ai.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b() throws IOException {
        C0130m c0130mA;
        List listB = C0130m.b(this.f8835a.O(), this.f8836b.a());
        if (listB.size() > 1) {
            c0130mA = C0130m.a(this.f8835a.O(), listB);
        } else {
            if (listB.size() != 1) {
                com.efiAnalytics.ui.bV.d("Command " + this.f8836b.a() + " not found in current configuration.", this);
                return;
            }
            c0130mA = (C0130m) listB.get(0);
        }
        this.f8835a.C().b(c0130mA);
        if (this.f8836b.b(C0078bk.f938d)) {
            com.efiAnalytics.ui.bV.d(bH.W.b(this.f8836b.d(), "\\n", "\n"), this);
        }
        if (this.f8836b.b(C0078bk.f939e)) {
            Window windowB = com.efiAnalytics.ui.bV.b(this);
            if (windowB instanceof dF) {
                ((dF) windowB).k();
            }
            windowB.dispose();
        }
    }

    @Override // bt.InterfaceC1349h
    public void a() {
        boolean zC = c();
        if (this.f8837c.isEnabled() != zC) {
            this.f8837c.setEnabled(zC);
        }
    }

    private boolean c() {
        if (this.f8836b.aH() == null || this.f8836b.aH().trim().length() <= 0) {
            return true;
        }
        try {
            return C1007o.a(this.f8836b.aH(), this.f8835a);
        } catch (V.g e2) {
            Logger.getLogger(C1300ai.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            return true;
        }
    }

    @Override // com.efiAnalytics.ui.InterfaceC1565bc
    public void close() throws IOException {
        if (this.f8840f) {
            return;
        }
        if (this.f8838d != null) {
            this.f8838d.a();
            C0126i.a(this.f8839e, this.f8835a.c(), this.f8836b.aH());
            C0113cs.a().a(this.f8838d);
        }
        if ((this.f8836b.c() & C0078bk.f936b) != 0 && c()) {
            b();
        } else if ((this.f8836b.c() & C0078bk.f937c) != 0 && !c()) {
            b();
        }
        this.f8840f = true;
    }
}
