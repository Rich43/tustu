package be;

import G.C0043ac;
import G.C0048ah;
import com.efiAnalytics.ui.C1685fp;
import com.efiAnalytics.ui.bV;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:be/T.class */
public class T extends JPanel {

    /* renamed from: a, reason: collision with root package name */
    C1091g f7943a;

    /* renamed from: b, reason: collision with root package name */
    y f7944b;

    /* renamed from: c, reason: collision with root package name */
    JCheckBox f7945c;

    /* renamed from: d, reason: collision with root package name */
    JCheckBox f7946d;

    /* renamed from: e, reason: collision with root package name */
    G.R f7947e;

    public T(G.R r2) {
        this.f7947e = r2;
        FlowLayout flowLayout = new FlowLayout(1);
        flowLayout.setAlignOnBaseline(true);
        setLayout(flowLayout);
        W w2 = new W(this);
        w2.setLayout(new BorderLayout());
        this.f7945c = new JCheckBox(C1818g.b("Add Gauge Template"));
        this.f7945c.addActionListener(new U(this));
        w2.add(this.f7945c, "North");
        this.f7944b = new y(r2);
        w2.add(this.f7944b, BorderLayout.CENTER);
        add(w2);
        W w3 = new W(this);
        w3.setLayout(new BorderLayout());
        this.f7946d = new JCheckBox(C1818g.b("Add Data Log Field"));
        this.f7946d.addActionListener(new V(this));
        w3.add(this.f7946d, "North");
        this.f7943a = new C1091g(r2);
        w3.add(this.f7943a, BorderLayout.CENTER);
        add(w3);
        C1685fp.a((Component) this.f7944b, false);
        C1685fp.a((Component) this.f7943a, false);
    }

    public void a(String str) {
        this.f7943a.a(str);
        this.f7944b.a(str);
    }

    public boolean a() throws NumberFormatException {
        if (this.f7945c.isSelected()) {
            R rB = this.f7944b.b();
            if (!rB.a()) {
                bV.d(C1818g.b(rB.d()), this);
                return false;
            }
        }
        if (!this.f7946d.isSelected()) {
            return true;
        }
        R rB2 = this.f7943a.b();
        if (rB2.a()) {
            return true;
        }
        bV.d(C1818g.b(rB2.d()), this);
        return false;
    }

    public C0043ac b() {
        if (this.f7946d.isSelected()) {
            return this.f7943a.a();
        }
        return null;
    }

    public C0048ah c() {
        if (this.f7945c.isSelected()) {
            return this.f7944b.a();
        }
        return null;
    }
}
