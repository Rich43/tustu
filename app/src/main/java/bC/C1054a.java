package bc;

import ae.t;
import java.awt.BorderLayout;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import s.C1818g;

/* renamed from: bc.a, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bc/a.class */
class C1054a extends JPanel {

    /* renamed from: a, reason: collision with root package name */
    t f7861a;

    /* renamed from: b, reason: collision with root package name */
    InterfaceC1062i f7862b;

    /* renamed from: c, reason: collision with root package name */
    JPanel f7863c = new JPanel();

    /* renamed from: d, reason: collision with root package name */
    C1056c f7864d = null;

    /* renamed from: e, reason: collision with root package name */
    List f7865e = new ArrayList();

    public C1054a(t tVar) {
        this.f7861a = null;
        this.f7862b = null;
        this.f7861a = tVar;
        setLayout(new BorderLayout());
        add(BorderLayout.CENTER, new C1059f(this, C1818g.b(this.f7861a.a()) + ": "));
        C1055b c1055b = new C1055b(this);
        if (this.f7861a.b() == 2 || this.f7861a.b() == 0) {
            C1060g c1060g = new C1060g(this);
            this.f7862b = c1060g;
            List listE = this.f7861a.e();
            for (int i2 = 0; i2 < listE.size(); i2++) {
                c1060g.addItem(listE.get(i2));
            }
            a((Component) c1060g);
            c1060g.addActionListener(c1055b);
        } else if (this.f7861a.b() == 3) {
            C1058e c1058e = new C1058e(this);
            this.f7862b = c1058e;
            a((Component) c1058e);
        } else if (this.f7861a.b() == 1) {
            C1061h c1061h = new C1061h(this);
            this.f7862b = c1061h;
            a((Component) c1061h);
        } else if (this.f7861a.b() == 4) {
            C1057d c1057d = new C1057d(this);
            this.f7862b = c1057d;
            a((Component) c1057d);
            c1057d.addActionListener(c1055b);
        } else {
            a(new JLabel(C1818g.b("Unknown Setting Type.")));
        }
        a(tVar.f());
    }

    private void a(Component component) {
        this.f7863c.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
        this.f7863c.add(BorderLayout.CENTER, component);
        add("East", this.f7863c);
    }

    public Object a() {
        if (this.f7862b != null) {
            return this.f7862b.a();
        }
        return null;
    }

    public void a(Object obj) {
        if (this.f7862b != null) {
            this.f7862b.a(obj);
        }
    }

    public void b() {
        c();
        this.f7864d = new C1056c(this);
        this.f7864d.start();
    }

    public void c() {
        if (this.f7864d != null) {
            this.f7864d.a();
            this.f7864d = null;
        }
    }

    public void a(InterfaceC1066m interfaceC1066m) {
        this.f7865e.add(interfaceC1066m);
    }

    @Override // java.awt.Component
    public String getName() {
        return this.f7861a.a();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(String str, Object obj) {
        Iterator it = this.f7865e.iterator();
        while (it.hasNext()) {
            ((InterfaceC1066m) it.next()).a(str, obj);
        }
    }

    public boolean d() {
        if (this.f7862b != null) {
            return this.f7862b.b();
        }
        return false;
    }
}
