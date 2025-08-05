package t;

import com.efiAnalytics.apps.ts.dashboard.InterfaceC1407f;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import s.C1818g;

/* renamed from: t.av, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:t/av.class */
public class C1849av extends AbstractC1827a implements InterfaceC1407f {

    /* renamed from: a, reason: collision with root package name */
    JPanel f13808a;

    /* renamed from: b, reason: collision with root package name */
    C1870r f13809b;

    /* renamed from: c, reason: collision with root package name */
    C1846as f13810c;

    /* renamed from: d, reason: collision with root package name */
    aK f13811d;

    /* renamed from: e, reason: collision with root package name */
    be f13812e;

    /* renamed from: g, reason: collision with root package name */
    aA f13813g;

    /* renamed from: h, reason: collision with root package name */
    C1852ay f13814h;

    /* renamed from: i, reason: collision with root package name */
    aO f13815i;

    /* renamed from: j, reason: collision with root package name */
    aF f13816j;

    /* renamed from: k, reason: collision with root package name */
    bi f13817k;

    /* renamed from: l, reason: collision with root package name */
    bn f13818l;

    /* renamed from: m, reason: collision with root package name */
    bn f13819m;

    /* renamed from: n, reason: collision with root package name */
    bn f13820n;

    public C1849av(Window window, C1836ai c1836ai, String str) {
        super(window, str);
        this.f13808a = new JPanel();
        setLayout(new GridLayout(1, 1));
        this.f13808a.setLayout(new BorderLayout());
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridLayout(1, 0, 5, 5));
        this.f13808a.add(jPanel);
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new BoxLayout(jPanel2, 1));
        jPanel.add(jPanel2);
        this.f13809b = new C1870r(window, c1836ai, str);
        this.f13817k = new bi(window, c1836ai);
        this.f13813g = new aA(window, c1836ai);
        this.f13814h = new C1852ay(window, c1836ai);
        this.f13819m = new aD(window, C1818g.b("Font Size Adjustment"));
        this.f13819m.a(c1836ai);
        this.f13820n = new C1850aw(window, C1818g.b("Display Digits"));
        this.f13820n.a(c1836ai);
        this.f13811d = new aK(window, c1836ai);
        this.f13810c = new C1846as(window, c1836ai);
        this.f13812e = new be(window, c1836ai);
        this.f13818l = new C1854b(window, C1818g.b("Border Size"));
        this.f13818l.a(c1836ai);
        this.f13815i = new aO(window, c1836ai);
        this.f13816j = new aF(window, c1836ai);
        jPanel2.add(this.f13809b.a());
        jPanel2.add(this.f13817k.a());
        jPanel2.add(this.f13813g.a());
        jPanel2.add(this.f13819m.a());
        jPanel2.add(this.f13814h.a());
        jPanel2.add(this.f13811d.a());
        jPanel2.add(this.f13820n.a());
        jPanel2.add(this.f13818l.a());
        JPanel jPanel3 = new JPanel();
        jPanel3.setLayout(new BoxLayout(jPanel3, 1));
        jPanel.add(jPanel3);
        jPanel3.add(this.f13815i.a());
        jPanel3.add(this.f13816j.a());
        jPanel3.add(this.f13812e.a());
        jPanel3.add(this.f13810c.a());
        add(new JScrollPane(this.f13808a));
        pack();
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.InterfaceC1407f
    public void a(ArrayList arrayList) throws IllegalArgumentException {
        this.f13809b.a(arrayList);
        this.f13810c.a(arrayList);
        this.f13811d.a(arrayList);
        this.f13812e.a(arrayList);
        this.f13813g.a(arrayList);
        this.f13815i.a(arrayList);
        this.f13816j.a(arrayList);
        try {
            this.f13817k.a(arrayList);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        this.f13818l.a(arrayList);
        this.f13819m.a(arrayList);
        this.f13814h.a(arrayList);
        this.f13820n.a(arrayList);
    }
}
