package aW;

import bt.C1351j;
import java.awt.BorderLayout;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:aW/e.class */
class e extends JPanel {

    /* renamed from: a, reason: collision with root package name */
    A.r f3973a;

    /* renamed from: b, reason: collision with root package name */
    n f3974b;

    /* renamed from: c, reason: collision with root package name */
    JPanel f3975c = new JPanel();

    /* renamed from: d, reason: collision with root package name */
    h f3976d = null;

    /* renamed from: e, reason: collision with root package name */
    List f3977e = new ArrayList();

    /* renamed from: f, reason: collision with root package name */
    a f3978f;

    public e(a aVar, A.r rVar) {
        this.f3973a = null;
        this.f3974b = null;
        this.f3978f = null;
        this.f3973a = rVar;
        this.f3978f = aVar;
        setLayout(new BorderLayout());
        k kVar = new k(this, C1818g.b(this.f3973a.c()) + ": ");
        if (this.f3973a.d() != null && !this.f3973a.d().isEmpty()) {
            C1351j c1351j = new C1351j(C1818g.b(this.f3973a.d()));
            c1351j.setOpaque(false);
            JPanel jPanel = new JPanel();
            jPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            jPanel.setLayout(new BorderLayout());
            jPanel.add("North", c1351j);
            add(jPanel, "East");
        }
        JPanel jPanel2 = new JPanel();
        jPanel2.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        jPanel2.setLayout(new BorderLayout());
        jPanel2.add("North", kVar);
        add("West", jPanel2);
        if (this.f3973a.a() == 2 || this.f3973a.a() == 0 || this.f3973a.a() == 4) {
            l lVar = new l(this);
            lVar.setEditable(this.f3973a.a() == 0);
            this.f3974b = lVar;
            List listB = this.f3973a.b();
            for (int i2 = 0; i2 < listB.size(); i2++) {
                lVar.addItem(listB.get(i2));
            }
            a((Component) lVar);
            lVar.addActionListener(new f(this));
            return;
        }
        if (this.f3973a.a() == 3) {
            j jVar = new j(this);
            this.f3974b = jVar;
            a((Component) jVar);
            return;
        }
        if (this.f3973a.a() == 1) {
            m mVar = new m(this);
            this.f3974b = mVar;
            a((Component) mVar);
        } else {
            if (this.f3973a.a() == 6) {
                i iVar = new i(this, C1818g.b(this.f3973a.c()));
                iVar.setToolTipText(kVar.getToolTipText());
                kVar.setText("");
                this.f3974b = iVar;
                a((Component) iVar);
                return;
            }
            if (this.f3973a.a() != 5 || !r.a().a(this.f3973a.c())) {
                a(new JLabel(C1818g.b("Unknown Setting Type.")));
                return;
            }
            JButton jButton = new JButton(C1818g.b(this.f3973a.d()));
            jButton.addActionListener(new g(this, aVar));
            a((Component) jButton);
        }
    }

    private void a(Component component) {
        this.f3975c.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        this.f3975c.setLayout(new BorderLayout());
        this.f3975c.add("North", component);
        add(BorderLayout.CENTER, this.f3975c);
    }

    public Object a() {
        if (this.f3974b != null) {
            return this.f3974b.a();
        }
        return null;
    }

    public void a(Object obj) {
        if (this.f3974b != null) {
            if (a() == null || !a().equals(obj)) {
                this.f3974b.a(obj);
            }
        }
    }

    public void b() {
        c();
        this.f3976d = new h(this);
        this.f3976d.start();
    }

    public void c() {
        if (this.f3976d != null) {
            this.f3976d.a();
            this.f3976d = null;
        }
    }

    public void a(p pVar) {
        this.f3977e.add(pVar);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(String str, String str2) {
        Iterator it = this.f3977e.iterator();
        while (it.hasNext()) {
            ((p) it.next()).a(str, str2);
        }
    }

    public boolean d() {
        return this.f3974b != null ? this.f3974b.b() : this.f3973a != null && this.f3973a.a() == 5;
    }
}
