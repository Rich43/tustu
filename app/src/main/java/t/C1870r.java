package t;

import com.efiAnalytics.apps.ts.dashboard.AbstractC1420s;
import com.efiAnalytics.apps.ts.dashboard.InterfaceC1407f;
import com.efiAnalytics.ui.Cdo;
import com.efiAnalytics.ui.eJ;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Window;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import s.C1818g;

/* renamed from: t.r, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:t/r.class */
public class C1870r extends AbstractC1827a implements InterfaceC1407f {

    /* renamed from: a, reason: collision with root package name */
    JPanel f13901a;

    /* renamed from: b, reason: collision with root package name */
    Cdo f13902b;

    /* renamed from: c, reason: collision with root package name */
    Cdo f13903c;

    /* renamed from: d, reason: collision with root package name */
    Cdo f13904d;

    /* renamed from: e, reason: collision with root package name */
    Cdo f13905e;

    public C1870r(Window window, C1836ai c1836ai, String str) {
        super(window, str);
        this.f13901a = new JPanel();
        this.f13902b = new Cdo();
        this.f13903c = new Cdo();
        this.f13904d = new Cdo();
        this.f13905e = new Cdo();
        super.a(c1836ai);
        setLayout(new BorderLayout());
        add(this.f13901a, BorderLayout.CENTER);
        this.f13902b.b(0);
        this.f13903c.b(0);
        this.f13904d.b(0);
        this.f13905e.b(0);
        this.f13901a.setBorder(BorderFactory.createTitledBorder(C1818g.b("Component Position")));
        this.f13901a.setLayout(new GridLayout(0, 2, eJ.a(3), eJ.a(3)));
        this.f13901a.add(new JLabel(C1818g.b("Component X :"), 4));
        this.f13901a.add(this.f13902b);
        this.f13901a.add(new JLabel(C1818g.b("Component Y :"), 4));
        this.f13901a.add(this.f13903c);
        this.f13901a.add(new JLabel(C1818g.b("Component Width :"), 4));
        this.f13901a.add(this.f13904d);
        this.f13901a.add(new JLabel(C1818g.b("Component Height :"), 4));
        this.f13901a.add(this.f13905e);
        this.f13902b.addKeyListener(new C1871s(this));
        this.f13903c.addKeyListener(new C1872t(this));
        this.f13904d.addKeyListener(new C1873u(this));
        this.f13905e.addKeyListener(new C1874v(this));
    }

    public void e(ArrayList arrayList) {
        if (arrayList.isEmpty()) {
            setEnabled(false);
        } else if (arrayList.get(0) != null) {
            setEnabled(true);
            AbstractC1420s abstractC1420s = (AbstractC1420s) arrayList.get(0);
            this.f13902b.a(abstractC1420s.getX());
            this.f13903c.a(abstractC1420s.getY());
            this.f13904d.a(abstractC1420s.getWidth());
            this.f13905e.a(abstractC1420s.getHeight());
        }
        f(arrayList);
    }

    private void f(ArrayList arrayList) {
        Color color = UIManager.getColor("TextField.foreground");
        Color color2 = Color.GRAY;
        this.f13902b.setForeground(color);
        this.f13903c.setForeground(color);
        this.f13904d.setForeground(color);
        this.f13905e.setForeground(color);
        if (color2 == null) {
            return;
        }
        Iterator it = arrayList.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            } else if (((AbstractC1420s) it.next()).getX() != this.f13902b.e()) {
                this.f13902b.setForeground(color2);
                break;
            }
        }
        Iterator it2 = arrayList.iterator();
        while (true) {
            if (!it2.hasNext()) {
                break;
            } else if (((AbstractC1420s) it2.next()).getY() != this.f13903c.e()) {
                this.f13903c.setForeground(color2);
                break;
            }
        }
        Iterator it3 = arrayList.iterator();
        while (true) {
            if (!it3.hasNext()) {
                break;
            } else if (((AbstractC1420s) it3.next()).getWidth() != this.f13904d.e()) {
                this.f13904d.setForeground(color2);
                break;
            }
        }
        Iterator it4 = arrayList.iterator();
        while (it4.hasNext()) {
            if (((AbstractC1420s) it4.next()).getHeight() != this.f13905e.e()) {
                this.f13905e.setForeground(color2);
                return;
            }
        }
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.InterfaceC1407f
    public void a(ArrayList arrayList) {
        e(arrayList);
    }

    @Override // java.awt.Component, c.InterfaceC1385d
    public void setEnabled(boolean z2) {
        for (Component component : this.f13901a.getComponents()) {
            component.setEnabled(z2);
        }
        this.f13901a.setEnabled(z2);
    }

    public JPanel a() {
        return this.f13901a;
    }
}
