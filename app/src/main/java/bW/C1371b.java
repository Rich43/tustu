package bw;

import bH.ab;
import com.efiAnalytics.ui.Cdo;
import com.efiAnalytics.ui.cF;
import com.efiAnalytics.ui.eJ;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import s.C1818g;

/* renamed from: bw.b, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bw/b.class */
public class C1371b extends JPanel {

    /* renamed from: a, reason: collision with root package name */
    Cdo f9153a = new Cdo();

    /* renamed from: b, reason: collision with root package name */
    Cdo f9154b = new Cdo();

    /* renamed from: c, reason: collision with root package name */
    Cdo f9155c = new Cdo();

    /* renamed from: d, reason: collision with root package name */
    Cdo f9156d = new Cdo();

    /* renamed from: e, reason: collision with root package name */
    Cdo f9157e = new Cdo();

    /* renamed from: f, reason: collision with root package name */
    Cdo f9158f = new Cdo();

    /* renamed from: g, reason: collision with root package name */
    Cdo f9159g = new Cdo();

    /* renamed from: h, reason: collision with root package name */
    Cdo f9160h = new Cdo();

    /* renamed from: i, reason: collision with root package name */
    C1372c f9161i = new C1372c(this);

    /* renamed from: j, reason: collision with root package name */
    C1374e f9162j = new C1374e(this);

    /* renamed from: k, reason: collision with root package name */
    String f9163k = ab.a().a("To use:") + " \n" + ab.a().a("Enter the voltage and values measured or from your data sheet into the top 4 boxes. In the lower 2 Volt boxes enter the target voltages (Typically 0-5 Volts or 0-12V).");

    public C1371b() {
        c();
        b();
    }

    private void b() {
        setBorder(BorderFactory.createTitledBorder(C1818g.b("Two Point Calculator")));
        setLayout(new GridLayout(0, 3, eJ.a(3), eJ.a(3)));
        add(new C1373d(this, "", 4));
        add(new C1373d(this, "", 4));
        add(new cF(this.f9163k, ab.a().b()));
        add(new C1373d(this, "", 4));
        add(new C1373d(this, "Volts", 0));
        add(new C1373d(this, "Value", 0));
        add(new C1373d(this, "Input Point 1", 4));
        add(this.f9153a);
        add(this.f9155c);
        add(new C1373d(this, "Input Point 2", 4));
        add(this.f9154b);
        add(this.f9156d);
        this.f9153a.b(2);
        this.f9155c.b(2);
        this.f9154b.b(2);
        this.f9156d.b(2);
        this.f9157e.b(2);
        this.f9159g.b(2);
        this.f9158f.b(2);
        this.f9160h.b(2);
        this.f9153a.addFocusListener(this.f9162j);
        this.f9155c.addFocusListener(this.f9162j);
        this.f9154b.addFocusListener(this.f9162j);
        this.f9156d.addFocusListener(this.f9162j);
        this.f9157e.addFocusListener(this.f9162j);
        this.f9159g.addFocusListener(this.f9162j);
        this.f9158f.addFocusListener(this.f9162j);
        this.f9160h.addFocusListener(this.f9162j);
        add(new C1373d(this, "", 4));
        add(new C1373d(this, "", 4));
        add(new C1373d(this, "", 4));
        add(new C1373d(this, "", 4));
        add(new C1373d(this, "Volts", 0));
        add(new C1373d(this, "Value", 0));
        add(new C1373d(this, "Output Point 1", 4));
        add(this.f9157e);
        add(this.f9159g);
        add(new C1373d(this, "Output Point 2", 4));
        add(this.f9158f);
        add(this.f9160h);
        this.f9153a.addKeyListener(this.f9161i);
        this.f9155c.addKeyListener(this.f9161i);
        this.f9154b.addKeyListener(this.f9161i);
        this.f9156d.addKeyListener(this.f9161i);
        this.f9157e.addKeyListener(this.f9161i);
        this.f9158f.addKeyListener(this.f9161i);
    }

    public void a() {
        if (this.f9153a.getText().isEmpty() || this.f9155c.getText().isEmpty() || this.f9156d.getText().isEmpty() || this.f9154b.getText().isEmpty() || this.f9157e.getText().isEmpty() || this.f9158f.getText().isEmpty()) {
            return;
        }
        double dE = (this.f9156d.e() - this.f9155c.e()) / (this.f9154b.e() - this.f9153a.e());
        double dE2 = this.f9155c.e() - (this.f9153a.e() * dE);
        double dE3 = (this.f9157e.e() * dE) + dE2;
        double dE4 = (this.f9158f.e() * dE) + dE2;
        this.f9159g.a(dE3);
        this.f9160h.a(dE4);
    }

    private void c() {
        this.f9153a.a(0.5d);
        this.f9154b.a(4.5d);
        this.f9155c.setText("");
        this.f9156d.setText("");
        this.f9157e.a(0.0d);
        this.f9158f.a(5.0d);
        this.f9159g.setEditable(false);
        this.f9160h.setEditable(false);
    }
}
