package br;

import com.efiAnalytics.ui.eJ;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import s.C1818g;

/* renamed from: br.I, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:br/I.class */
class C1231I extends JPanel implements bL.l {

    /* renamed from: a, reason: collision with root package name */
    JLabel f8349a = new JLabel();

    /* renamed from: b, reason: collision with root package name */
    JLabel f8350b = new JLabel();

    /* renamed from: c, reason: collision with root package name */
    JLabel f8351c = new JLabel();

    /* renamed from: d, reason: collision with root package name */
    JLabel f8352d = new JLabel();

    /* renamed from: e, reason: collision with root package name */
    JLabel f8353e = new JLabel();

    /* renamed from: f, reason: collision with root package name */
    JLabel f8354f = new JLabel();

    /* renamed from: g, reason: collision with root package name */
    JLabel f8355g = new JLabel();

    /* renamed from: h, reason: collision with root package name */
    JLabel f8356h = new JLabel();

    /* renamed from: i, reason: collision with root package name */
    JLabel f8357i;

    /* renamed from: j, reason: collision with root package name */
    final /* synthetic */ C1255s f8358j;

    C1231I(C1255s c1255s) {
        this.f8358j = c1255s;
        this.f8357i = new C1229G(this.f8358j);
        setBorder(BorderFactory.createTitledBorder("VeAnalyze Stats"));
        setLayout(new GridLayout(0, 3, 6, 2));
        add(a("Total Records", this.f8349a));
        add(a("Filtered Records", this.f8350b));
        add(a("Used Records", this.f8351c));
        add(a("Total Table Cells", this.f8352d));
        add(a("Cells Altered", this.f8355g));
        add(a("Average Cell Weight", this.f8353e));
        add(a("Average Cell Change", this.f8354f));
        add(a("Max Cell Change", this.f8356h));
        add(a("Active Filter", this.f8357i));
    }

    private JPanel a(String str, JLabel jLabel) {
        String str2 = C1818g.b(str) + ": ";
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridLayout(1, 0));
        jLabel.setMinimumSize(new Dimension(eJ.a(60), eJ.a(14)));
        jLabel.setHorizontalAlignment(2);
        jPanel.add(new JLabel(str2, 4));
        jPanel.add(jLabel);
        return jPanel;
    }

    @Override // bL.l
    public void a(bL.m mVar) throws IllegalArgumentException {
        this.f8349a.setText(mVar.a() + "");
        this.f8350b.setText(mVar.b() + "");
        this.f8351c.setText((mVar.a() - mVar.b()) + "");
        this.f8352d.setText(mVar.f() + "");
        this.f8353e.setText(bH.W.b(mVar.d(), 2));
        this.f8355g.setText(mVar.c() + "");
        this.f8356h.setText(bH.W.a(mVar.g()));
        this.f8354f.setText(bH.W.b(mVar.e(), 1));
        if (this.f8358j.f8519h.k().isEmpty()) {
            this.f8357i.setBackground(this.f8354f.getBackground());
            this.f8357i.setForeground(this.f8354f.getForeground());
            if (!this.f8358j.f8521j.isEnabled()) {
                this.f8358j.f8521j.setEnabled(true);
            }
        } else {
            this.f8357i.setBackground(Color.yellow);
            this.f8357i.setForeground(Color.BLACK);
        }
        this.f8357i.setText(this.f8358j.f8519h.k());
    }
}
