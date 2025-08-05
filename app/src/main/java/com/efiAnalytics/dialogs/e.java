package com.efiAnalytics.dialogs;

import bH.W;
import bH.aa;
import bu.C1368a;
import com.efiAnalytics.ui.Cdo;
import com.efiAnalytics.ui.eJ;
import h.i;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/dialogs/e.class */
public class e extends JPanel {

    /* renamed from: a, reason: collision with root package name */
    aa f9893a;

    /* renamed from: b, reason: collision with root package name */
    Cdo f9894b;

    /* renamed from: c, reason: collision with root package name */
    Cdo f9895c;

    /* renamed from: d, reason: collision with root package name */
    Cdo f9896d;

    /* renamed from: e, reason: collision with root package name */
    Cdo f9897e;

    /* renamed from: f, reason: collision with root package name */
    Cdo f9898f;

    /* renamed from: g, reason: collision with root package name */
    Cdo f9899g;

    /* renamed from: h, reason: collision with root package name */
    Cdo f9900h;

    /* renamed from: i, reason: collision with root package name */
    Cdo f9901i;

    /* renamed from: j, reason: collision with root package name */
    Cdo f9902j;

    /* renamed from: k, reason: collision with root package name */
    JTextPane f9903k = new JTextPane();

    /* renamed from: l, reason: collision with root package name */
    JLabel f9904l = new JLabel("", 0);

    public e(String str, aa aaVar) throws IllegalArgumentException {
        this.f9893a = null;
        this.f9893a = aaVar;
        setLayout(new BorderLayout(5, 5));
        JPanel jPanel = new JPanel();
        jPanel.setBorder(BorderFactory.createTitledBorder(str));
        jPanel.setLayout(new GridLayout(0, 1));
        f fVar = new f(this);
        this.f9894b = new Cdo("", 5);
        this.f9894b.b(3);
        this.f9894b.addFocusListener(fVar);
        jPanel.add(a(this.f9894b, a("60ft Time (s)")));
        this.f9895c = new Cdo("", 5);
        this.f9895c.b(3);
        this.f9895c.addFocusListener(fVar);
        jPanel.add(a(this.f9895c, a("330 ft ET (s)")));
        this.f9896d = new Cdo("", 5);
        this.f9896d.b(3);
        this.f9896d.addFocusListener(fVar);
        jPanel.add(a(this.f9896d, a("660 ft ET (s)")));
        this.f9897e = new Cdo("", 5);
        this.f9897e.b(3);
        this.f9897e.addFocusListener(fVar);
        jPanel.add(a(this.f9897e, a("660 ft Speed")));
        this.f9898f = new Cdo("", 5);
        this.f9898f.b(3);
        this.f9898f.addFocusListener(fVar);
        jPanel.add(a(this.f9898f, a("1000 ft ET (s)")));
        this.f9899g = new Cdo("", 5);
        this.f9899g.b(3);
        this.f9899g.addFocusListener(fVar);
        jPanel.add(a(this.f9899g, a("1320 ft ET (s)")));
        this.f9900h = new Cdo("", 5);
        this.f9900h.b(3);
        this.f9900h.addFocusListener(fVar);
        jPanel.add(a(this.f9900h, a("1320 ft Speed")));
        this.f9901i = new Cdo("", 5);
        this.f9901i.b(0);
        this.f9901i.addFocusListener(fVar);
        jPanel.add(a(this.f9901i, a("Density Altitude")));
        add("West", jPanel);
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new BorderLayout(5, 5));
        add(BorderLayout.CENTER, jPanel2);
        JPanel jPanel3 = new JPanel();
        jPanel3.setLayout(new BorderLayout(8, 8));
        jPanel3.setBorder(BorderFactory.createTitledBorder(a("Rollout Time")));
        JLabel jLabel = new JLabel();
        jLabel.setText(a("The delay in seconds between releasing the trans brake and the car breaking the beam."));
        jPanel3.add(BorderLayout.CENTER, jLabel);
        this.f9902j = new Cdo("", 5);
        this.f9902j.b(3);
        jPanel3.add("East", this.f9902j);
        this.f9902j.setText(i.e(i.f12330aw, "0.300"));
        jPanel2.add("North", jPanel3);
        JPanel jPanel4 = new JPanel();
        jPanel4.setLayout(new BorderLayout());
        jPanel4.setBorder(BorderFactory.createTitledBorder(a("Pass Notes")));
        this.f9903k.setPreferredSize(new Dimension(this.f9903k.getFont().getSize() * 8, 100));
        jPanel4.add(BorderLayout.CENTER, new JScrollPane(this.f9903k));
        jPanel2.add(BorderLayout.CENTER, jPanel4);
        add("North", this.f9904l);
    }

    private JPanel a(Cdo cdo, String str) {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout(eJ.a(5), eJ.a(5)));
        jPanel.add(new JLabel(a(str), 4), BorderLayout.CENTER);
        jPanel.add(cdo, "East");
        return jPanel;
    }

    private String a(String str) {
        if (this.f9893a != null) {
            str = this.f9893a.a(str);
        }
        return str;
    }

    public String a() {
        return this.f9902j.getText();
    }

    public void a(C1368a c1368a) throws IllegalArgumentException {
        this.f9894b.a(c1368a.a());
        this.f9895c.a(c1368a.g());
        this.f9896d.a(c1368a.b());
        this.f9897e.a(c1368a.c());
        this.f9898f.a(c1368a.i());
        this.f9899g.a(c1368a.d());
        this.f9900h.a(c1368a.e());
        this.f9901i.a(c1368a.f());
        this.f9903k.setText(W.b(c1368a.j(), "\\n", "\n"));
        if (!Double.isNaN(c1368a.k())) {
            this.f9902j.a(c1368a.k());
        }
        if (c1368a.l()) {
            this.f9904l.setText("<html><center>This timeslip data was <b>generated</b> by " + i.f12255b + ".<br>Change to actual timeslip values and click Apply to save real vaules to log file.<center>");
        } else {
            this.f9904l.setText("<html><center>Timeslip data from log file.</center>");
        }
        doLayout();
    }

    public C1368a b() {
        C1368a c1368a = new C1368a();
        c1368a.a(this.f9894b.e());
        c1368a.g(this.f9895c.e());
        c1368a.b(this.f9896d.e());
        c1368a.c(this.f9897e.e());
        c1368a.h(this.f9898f.e());
        c1368a.d(this.f9899g.e());
        c1368a.e(this.f9900h.e());
        c1368a.f(this.f9901i.e());
        c1368a.a(W.b(this.f9903k.getText(), "\n", "\\n"));
        return c1368a;
    }
}
