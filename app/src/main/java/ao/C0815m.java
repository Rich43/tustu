package ao;

import i.InterfaceC1741a;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JPanel;

/* renamed from: ao.m, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/m.class */
public class C0815m extends JPanel implements InterfaceC0814l, InterfaceC1741a {

    /* renamed from: b, reason: collision with root package name */
    C0804hg f6152b;

    /* renamed from: a, reason: collision with root package name */
    C0746fc f6151a = new C0746fc("", 5);

    /* renamed from: c, reason: collision with root package name */
    int f6153c = 0;

    /* renamed from: d, reason: collision with root package name */
    boolean f6154d = true;

    /* renamed from: e, reason: collision with root package name */
    ArrayList f6155e = new ArrayList();

    /* renamed from: f, reason: collision with root package name */
    gP f6156f = new gP();

    public C0815m(C0804hg c0804hg) {
        this.f6152b = null;
        this.f6152b = c0804hg;
        setLayout(new BorderLayout());
        this.f6156f.a(this);
        add(this.f6156f, BorderLayout.CENTER);
        JPanel jPanel = new JPanel();
        C0618ai c0618ai = new C0618ai(null, Toolkit.getDefaultToolkit().getImage(getClass().getResource("resources/down.gif")), null, com.efiAnalytics.ui.eJ.a(16, 16));
        c0618ai.setBackground(Color.GRAY);
        c0618ai.a(new C0816n(this));
        jPanel.add(c0618ai);
        this.f6151a.addKeyListener(new C0817o(this));
        jPanel.add(this.f6151a);
        C0618ai c0618ai2 = new C0618ai(null, Toolkit.getDefaultToolkit().getImage(getClass().getResource("resources/up.gif")), null, com.efiAnalytics.ui.eJ.a(16, 16));
        c0618ai2.setBackground(Color.GRAY);
        c0618ai2.a(new C0818p(this));
        jPanel.add(c0618ai2);
        C0618ai c0618ai3 = new C0618ai(null, Toolkit.getDefaultToolkit().getImage(getClass().getResource("resources/compare.gif")), null, com.efiAnalytics.ui.eJ.a(16, 16));
        c0618ai3.a(new C0819q(this));
        jPanel.add(c0618ai3);
        add(jPanel, "East");
    }

    public void a(String str) {
        if (str == null || str.equals("")) {
            str = "0";
        }
        c(Integer.parseInt(str));
    }

    public void c(int i2) {
        this.f6153c = i2;
        this.f6151a.setText("" + i2);
        this.f6156f.c(i2);
        f(i2);
    }

    public int a() {
        return this.f6153c;
    }

    public void b() {
        int i2 = this.f6153c + 1;
        this.f6153c = i2;
        c(i2);
    }

    public void c() {
        int i2 = this.f6153c - 1;
        this.f6153c = i2;
        c(i2);
    }

    public void a(InterfaceC0814l interfaceC0814l) {
        this.f6155e.add(interfaceC0814l);
    }

    public void d() {
        boolean z2 = !this.f6154d;
        this.f6154d = z2;
        b(z2);
    }

    @Override // i.InterfaceC1741a
    public void a(int i2) {
        c(i2);
    }

    @Override // ao.InterfaceC0814l
    public void b(int i2) {
        a(i2);
    }

    @Override // ao.InterfaceC0814l
    public void a(boolean z2) {
    }

    public void d(int i2) {
        this.f6156f.a(i2);
    }

    public void e(int i2) {
        this.f6156f.b(i2);
    }

    protected void b(boolean z2) {
        Iterator it = this.f6155e.iterator();
        while (it.hasNext()) {
            InterfaceC0814l interfaceC0814l = (InterfaceC0814l) it.next();
            if (interfaceC0814l != null) {
                interfaceC0814l.a(z2);
            }
        }
    }

    protected void f(int i2) {
        Iterator it = this.f6155e.iterator();
        while (it.hasNext()) {
            InterfaceC0814l interfaceC0814l = (InterfaceC0814l) it.next();
            if (interfaceC0814l != null) {
                interfaceC0814l.b(i2);
            }
        }
    }

    @Override // java.awt.Component
    public void addKeyListener(KeyListener keyListener) {
        for (Component component : getComponents()) {
            component.addKeyListener(keyListener);
        }
    }

    @Override // javax.swing.JComponent, java.awt.Component
    public void setBackground(Color color) {
        for (Component component : getComponents()) {
            component.setBackground(color);
        }
    }
}
