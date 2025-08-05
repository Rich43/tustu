package aP;

import bt.C1366y;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JLabel;
import javax.swing.JPanel;
import jdk.internal.dynalink.CallSiteDescriptor;
import r.C1798a;
import s.C1818g;

/* renamed from: aP.ht, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/ht.class */
class C0412ht extends JPanel implements ItemListener {

    /* renamed from: b, reason: collision with root package name */
    private Frame f3612b;

    /* renamed from: c, reason: collision with root package name */
    private JLabel f3613c;

    /* renamed from: d, reason: collision with root package name */
    private C1366y f3614d;

    /* renamed from: e, reason: collision with root package name */
    private JLabel f3615e;

    /* renamed from: f, reason: collision with root package name */
    private C1366y f3616f;

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0406hn f3617a;

    public C0412ht(C0406hn c0406hn, Frame frame) {
        this.f3617a = c0406hn;
        this.f3612b = frame;
        setLayout(new GridLayout(1, 4, 10, 10));
        this.f3613c = new JLabel(C1818g.b("Port Name") + CallSiteDescriptor.TOKEN_DELIMITER, 0);
        add(this.f3613c);
        this.f3614d = new C1366y();
        this.f3614d.setEditable(true);
        this.f3614d.addItemListener(this);
        add(this.f3614d);
        c();
        this.f3614d.a(c0406hn.f3602m.a());
        this.f3615e = new JLabel(C1818g.b("Baud Rate") + CallSiteDescriptor.TOKEN_DELIMITER, 0);
        add(this.f3615e);
        this.f3616f = new C1366y();
        for (String str : new z.i().c()) {
            this.f3616f.addItem(str);
        }
        this.f3616f.a(Integer.toString(c0406hn.f3602m.b()));
        this.f3616f.addItemListener(this);
        add(this.f3616f);
    }

    public void a(String str) {
        this.f3614d.a(str);
    }

    public void b(String str) {
        this.f3616f.a(str);
    }

    public void a() {
        this.f3614d.a(this.f3617a.f3602m.a());
        this.f3616f.a(this.f3617a.f3602m.c());
    }

    public void b() {
        if (this.f3616f == null || this.f3614d == null) {
            return;
        }
        this.f3617a.f3602m.a(this.f3614d.a());
        C1798a c1798aA = C1798a.a();
        c1798aA.b(C1798a.f13344az, this.f3617a.f3602m.a());
        this.f3617a.f3602m.b(this.f3616f.a());
        c1798aA.b(C1798a.f13343ay, this.f3617a.f3602m.c());
    }

    void c() {
        for (String str : new z.i().a()) {
            this.f3614d.addItem(str);
        }
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) {
        if (this.f3617a.f3603n == null || !this.f3617a.f3603n.d()) {
            b();
            return;
        }
        if (itemEvent.getItemSelectable() == this.f3614d) {
            this.f3617a.e();
            com.efiAnalytics.ui.bV.d("Port Open!\nPort can not\nbe changed\nwhile a port is open.", this.f3612b);
            a();
        } else {
            b();
            try {
                this.f3617a.f3603n.b();
            } catch (z.m e2) {
                com.efiAnalytics.ui.bV.d("Unsupported Configuration!\nConfiguration Parameter unsupported,\nselect new value.\nReturning to previous configuration.", this.f3612b);
                a();
            }
        }
    }
}
