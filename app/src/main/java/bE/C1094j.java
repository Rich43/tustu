package be;

import G.C0043ac;
import G.C0051ak;
import com.efiAnalytics.ui.C1580br;
import com.efiAnalytics.ui.C1685fp;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import r.C1806i;
import s.C1818g;

/* renamed from: be.j, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:be/j.class */
public class C1094j extends JPanel implements InterfaceC1099o {

    /* renamed from: a, reason: collision with root package name */
    static String f7981a = "Log Other OutputChannels";

    /* renamed from: b, reason: collision with root package name */
    static String f7982b = "Log Indicator";

    /* renamed from: c, reason: collision with root package name */
    CardLayout f7983c = new CardLayout();

    /* renamed from: d, reason: collision with root package name */
    C1091g f7984d;

    /* renamed from: e, reason: collision with root package name */
    G.R f7985e;

    /* renamed from: f, reason: collision with root package name */
    JButton f7986f;

    public C1094j(G.R r2) {
        this.f7985e = r2;
        setLayout(new BorderLayout());
        this.f7986f = new JButton(C1818g.b("Templates"));
        this.f7986f.addActionListener(new C1095k(this));
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        jPanel.add("East", this.f7986f);
        add("North", jPanel);
        this.f7984d = new C1091g(r2);
        if (!C1806i.a().a("09ggdslkmkgoiu")) {
            C1685fp.a((Component) this.f7984d, false);
            this.f7986f.setEnabled(false);
        }
        add(BorderLayout.CENTER, this.f7984d);
    }

    public void a() {
        C1580br c1580br = new C1580br();
        ArrayList<C0051ak> arrayList = new ArrayList();
        Iterator itA = this.f7985e.A();
        while (itA.hasNext()) {
            arrayList.add(itA.next());
        }
        Collections.sort(arrayList, new C1096l(this));
        if (!arrayList.isEmpty()) {
            C1097m c1097m = new C1097m(this);
            JMenu jMenu = new JMenu(C1818g.b("Log Indicator State"));
            c1580br.add((JMenuItem) jMenu);
            int i2 = 0;
            JMenu jMenu2 = jMenu;
            if (arrayList.size() > 25) {
                jMenu2 = new JMenu(C1818g.b("Predefined Indicators 1"));
            }
            for (C0051ak c0051ak : arrayList) {
                int i3 = i2;
                i2++;
                if (i3 % 25 == 0) {
                    jMenu2 = new JMenu(C1818g.b("Predefined Indicators") + " " + ((i2 / 25) + 1));
                    jMenu.add((JMenuItem) jMenu2);
                }
                JMenuItem jMenuItem = new JMenuItem(C1818g.b(c0051ak.b()));
                jMenuItem.addActionListener(c1097m);
                jMenuItem.setActionCommand(c0051ak.aJ());
                jMenu2.add(jMenuItem);
            }
        }
        this.f7986f.add(c1580br);
        c1580br.show(this.f7986f, 0, this.f7986f.getHeight());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(C0051ak c0051ak) {
        try {
            C0043ac c0043acA = this.f7984d.a();
            c0043acA.c(c0051ak.b());
            c0043acA.b(4);
            c0043acA.b(c0051ak.f());
            this.f7984d.a(c0043acA);
        } catch (V.a e2) {
            bH.C.a(e2);
        }
    }

    @Override // be.InterfaceC1099o
    public boolean c() {
        return this.f7984d.c();
    }

    public void a(G.R r2) {
        this.f7984d.a(r2);
    }

    public void a(C0043ac c0043ac) {
        this.f7984d.a(c0043ac);
    }

    void a(boolean z2) {
        if (C1806i.a().a("09ggdslkmkgoiu")) {
            this.f7986f.setEnabled(z2);
        } else {
            this.f7986f.setEnabled(false);
        }
    }
}
