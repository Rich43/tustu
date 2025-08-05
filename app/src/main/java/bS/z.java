package bs;

import bH.W;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:bs/z.class */
class z extends JPanel implements bM.e {

    /* renamed from: a, reason: collision with root package name */
    JLabel f8641a = new JLabel();

    /* renamed from: b, reason: collision with root package name */
    JLabel f8642b = new JLabel();

    /* renamed from: c, reason: collision with root package name */
    JLabel f8643c = new JLabel();

    /* renamed from: d, reason: collision with root package name */
    JLabel f8644d = new JLabel();

    /* renamed from: e, reason: collision with root package name */
    JLabel f8645e = new JLabel();

    /* renamed from: f, reason: collision with root package name */
    JLabel f8646f = new JLabel();

    /* renamed from: g, reason: collision with root package name */
    JLabel f8647g = new JLabel();

    /* renamed from: h, reason: collision with root package name */
    JLabel f8648h = new JLabel();

    /* renamed from: i, reason: collision with root package name */
    JLabel f8649i = new JLabel();

    /* renamed from: j, reason: collision with root package name */
    JLabel f8650j = new JLabel();

    /* renamed from: k, reason: collision with root package name */
    final /* synthetic */ k f8651k;

    z(k kVar) {
        this.f8651k = kVar;
        setBorder(BorderFactory.createTitledBorder(C1818g.b("Warmup Analyze Stats")));
        setLayout(new GridLayout(0, 3, 6, 2));
        add(a("Total Records", this.f8641a));
        add(a("Filtered Records", this.f8642b));
        add(a("Used Records", this.f8643c));
        add(a("Target Lambda", this.f8648h));
        add(a("Current Lambda", this.f8649i));
        add(a("Lambda Error %", this.f8650j));
        add(a("Start Temperature", this.f8644d));
        add(a("Maximum temperature", this.f8645e));
        add(a("Coolant Temperature", this.f8646f));
        add(a("Active Filter", this.f8647g));
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

    @Override // bM.e
    public void a(bM.d dVar) throws IllegalArgumentException {
        this.f8641a.setText(dVar.a() + "");
        this.f8642b.setText(dVar.b() + "");
        this.f8643c.setText((dVar.a() - dVar.b()) + "");
        this.f8644d.setText(dVar.f() + "");
        this.f8645e.setText(dVar.g() + "");
        this.f8646f.setText(W.b(dVar.e(), 1));
        if (this.f8651k.f8605b.b().equals("")) {
            this.f8647g.setBackground(this.f8643c.getBackground());
            this.f8647g.setOpaque(false);
        } else {
            this.f8647g.setBackground(Color.yellow);
            this.f8647g.setOpaque(true);
        }
        this.f8647g.setText(this.f8651k.f8605b.b());
        this.f8648h.setText(W.a(dVar.c()));
        this.f8649i.setText(W.a(dVar.d()));
        this.f8650j.setText(W.c(((dVar.d() / dVar.c()) - 1.0f) * 100.0f, 2));
    }
}
