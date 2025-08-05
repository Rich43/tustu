package aw;

import bH.aa;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

/* renamed from: aw.c, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aw/c.class */
public class C0880c extends JPanel {

    /* renamed from: a, reason: collision with root package name */
    aa f6309a;

    /* renamed from: b, reason: collision with root package name */
    bE.e f6310b;

    /* renamed from: c, reason: collision with root package name */
    bE.e f6311c;

    /* renamed from: d, reason: collision with root package name */
    bE.e f6312d;

    /* renamed from: e, reason: collision with root package name */
    ArrayList f6313e = new ArrayList();

    public C0880c(aa aaVar) {
        this.f6309a = null;
        this.f6310b = null;
        this.f6311c = null;
        this.f6312d = null;
        this.f6309a = aaVar;
        setLayout(new BorderLayout());
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridLayout(1, 0));
        this.f6310b = new bE.e(aaVar);
        this.f6310b.setBorder(BorderFactory.createTitledBorder(a("X Axis Scale")));
        jPanel.add(this.f6310b);
        this.f6310b.a(new C0882e(this));
        this.f6310b.a(true);
        this.f6311c = new bE.e(aaVar);
        this.f6311c.setBorder(BorderFactory.createTitledBorder(a("Y Axis Scale")));
        jPanel.add(this.f6311c);
        this.f6311c.a(new C0883f(this));
        this.f6311c.a(true);
        this.f6312d = new bE.e(aaVar);
        this.f6312d.setBorder(BorderFactory.createTitledBorder(a("Z Axis Scale")));
        jPanel.add(this.f6312d);
        this.f6312d.a(new C0884g(this));
        this.f6312d.a(true);
        add("North", jPanel);
    }

    public void a(boolean z2) {
        this.f6311c.a(z2);
    }

    public void a(double d2, double d3) {
        this.f6311c.a(d2);
        this.f6311c.b(d3);
    }

    public void b(boolean z2) {
        this.f6310b.a(z2);
    }

    public void b(double d2, double d3) {
        this.f6310b.a(d2);
        this.f6310b.b(d3);
    }

    public void c(boolean z2) {
        this.f6312d.a(z2);
    }

    public void c(double d2, double d3) {
        this.f6312d.a(d2);
        this.f6312d.b(d3);
    }

    public boolean a() {
        return this.f6310b.a();
    }

    public double b() {
        return this.f6310b.b();
    }

    public double c() {
        return this.f6310b.c();
    }

    public boolean d() {
        return this.f6311c.a();
    }

    public double e() {
        return this.f6311c.b();
    }

    public double f() {
        return this.f6311c.c();
    }

    public boolean g() {
        return this.f6312d.a();
    }

    public double h() {
        return this.f6312d.b();
    }

    public double i() {
        return this.f6312d.c();
    }

    public void a(InterfaceC0879b interfaceC0879b) {
        this.f6313e.add(interfaceC0879b);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void j() {
        Iterator it = this.f6313e.iterator();
        while (it.hasNext()) {
            ((InterfaceC0879b) it.next()).a(this.f6310b.b(), this.f6310b.c());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void d(boolean z2) {
        Iterator it = this.f6313e.iterator();
        while (it.hasNext()) {
            ((InterfaceC0879b) it.next()).a(z2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void k() {
        Iterator it = this.f6313e.iterator();
        while (it.hasNext()) {
            ((InterfaceC0879b) it.next()).b(this.f6311c.b(), this.f6311c.c());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void e(boolean z2) {
        Iterator it = this.f6313e.iterator();
        while (it.hasNext()) {
            ((InterfaceC0879b) it.next()).b(z2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void l() {
        Iterator it = this.f6313e.iterator();
        while (it.hasNext()) {
            ((InterfaceC0879b) it.next()).c(this.f6312d.b(), this.f6312d.c());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void f(boolean z2) {
        Iterator it = this.f6313e.iterator();
        while (it.hasNext()) {
            ((InterfaceC0879b) it.next()).c(z2);
        }
    }

    private String a(String str) {
        return this.f6309a != null ? this.f6309a.a(str) : str;
    }
}
