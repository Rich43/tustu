package br;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:br/af.class */
class af extends JPanel implements bL.l {

    /* renamed from: a, reason: collision with root package name */
    JLabel f8415a = new JLabel();

    /* renamed from: b, reason: collision with root package name */
    JLabel f8416b = new JLabel();

    /* renamed from: c, reason: collision with root package name */
    JLabel f8417c = new JLabel();

    /* renamed from: d, reason: collision with root package name */
    JLabel f8418d = new JLabel();

    /* renamed from: e, reason: collision with root package name */
    JLabel f8419e = new JLabel();

    /* renamed from: f, reason: collision with root package name */
    JLabel f8420f = new JLabel();

    /* renamed from: g, reason: collision with root package name */
    JLabel f8421g = new JLabel();

    /* renamed from: h, reason: collision with root package name */
    JLabel f8422h = new JLabel();

    /* renamed from: i, reason: collision with root package name */
    JLabel f8423i;

    /* renamed from: j, reason: collision with root package name */
    final /* synthetic */ P f8424j;

    af(P p2) {
        this.f8424j = p2;
        this.f8423i = new ad(this.f8424j);
        setBorder(BorderFactory.createTitledBorder("VeAnalyze Stats"));
        setLayout(new GridLayout(0, 3, 6, 2));
        add(a("Total Records", this.f8415a));
        add(a("Filtered Records", this.f8416b));
        add(a("Used Records", this.f8417c));
        add(a("Total Table Cells", this.f8418d));
        add(a("Cells Altered", this.f8421g));
        add(a("Average Cell Weight", this.f8419e));
        add(a("Average Cell Change", this.f8420f));
        add(a("Max Cell Change", this.f8422h));
        add(a("Active Filter", this.f8423i));
    }

    private JPanel a(String str, JLabel jLabel) {
        String str2 = C1818g.b(str) + ": ";
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridLayout(1, 0));
        jLabel.setMinimumSize(new Dimension(60, 14));
        jLabel.setHorizontalAlignment(2);
        jPanel.add(new JLabel(str2, 4));
        jPanel.add(jLabel);
        return jPanel;
    }

    @Override // bL.l
    public void a(bL.m mVar) throws IllegalArgumentException {
        this.f8415a.setText(mVar.a() + "");
        this.f8416b.setText(mVar.b() + "");
        this.f8417c.setText((mVar.a() - mVar.b()) + "");
        this.f8418d.setText(mVar.f() + "");
        this.f8419e.setText(bH.W.b(mVar.d(), 2));
        this.f8421g.setText(mVar.c() + "");
        this.f8422h.setText(bH.W.a(mVar.g()));
        this.f8420f.setText(bH.W.b(mVar.e(), 1));
        if (((ag) this.f8424j.f8389p.get(0)).f8425a.k().equals("")) {
            this.f8423i.setBackground(this.f8420f.getBackground());
            this.f8423i.setForeground(this.f8420f.getForeground());
        } else {
            this.f8423i.setBackground(Color.yellow);
            this.f8423i.setForeground(Color.BLACK);
        }
        this.f8423i.setText(((ag) this.f8424j.f8389p.get(0)).f8425a.k());
    }
}
