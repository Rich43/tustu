package bE;

import bH.aa;
import com.efiAnalytics.ui.Cdo;
import com.efiAnalytics.ui.eJ;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: TunerStudioMS.jar:bE/e.class */
public class e extends JPanel {

    /* renamed from: a, reason: collision with root package name */
    aa f6712a;

    /* renamed from: b, reason: collision with root package name */
    JCheckBox f6713b;

    /* renamed from: c, reason: collision with root package name */
    Cdo f6714c = new Cdo("", 4);

    /* renamed from: d, reason: collision with root package name */
    Cdo f6715d = new Cdo("", 4);

    /* renamed from: e, reason: collision with root package name */
    ArrayList f6716e = new ArrayList();

    public e(aa aaVar) {
        this.f6712a = null;
        this.f6713b = null;
        this.f6712a = aaVar;
        this.f6713b = new JCheckBox(a("Use Default"));
        setLayout(new GridLayout(0, 1, eJ.a(3), eJ.a(3)));
        add(this.f6713b);
        this.f6713b.addActionListener(new f(this));
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        JLabel jLabel = new JLabel(a("Min") + CallSiteDescriptor.TOKEN_DELIMITER, 4);
        jLabel.setPreferredSize(eJ.a(45, 18));
        jPanel.add("West", jLabel);
        jPanel.add(BorderLayout.CENTER, this.f6714c);
        this.f6714c.b(7);
        this.f6715d.b(7);
        add(jPanel);
        this.f6714c.addKeyListener(new g(this));
        this.f6714c.addFocusListener(new h(this));
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new BorderLayout());
        JLabel jLabel2 = new JLabel(a("Max") + CallSiteDescriptor.TOKEN_DELIMITER, 4);
        jLabel2.setPreferredSize(eJ.a(45, 18));
        jPanel2.add("West", jLabel2);
        jPanel2.add(BorderLayout.CENTER, this.f6715d);
        add(jPanel2);
        this.f6715d.addKeyListener(new i(this));
        this.f6715d.addFocusListener(new j(this));
    }

    public void a(boolean z2) {
        this.f6713b.setSelected(z2);
        this.f6714c.setEnabled(!z2);
        this.f6715d.setEnabled(!z2);
    }

    public boolean a() {
        return this.f6713b.isSelected();
    }

    public void a(double d2) {
        this.f6714c.a(d2);
    }

    public double b() {
        return this.f6714c.e();
    }

    public void b(double d2) {
        this.f6715d.a(d2);
    }

    public double c() {
        return this.f6715d.e();
    }

    public void a(d dVar) {
        this.f6716e.add(dVar);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void d() {
        Iterator it = this.f6716e.iterator();
        while (it.hasNext()) {
            ((d) it.next()).a(this.f6713b.isSelected());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void e() {
        double dE = this.f6714c.e();
        Iterator it = this.f6716e.iterator();
        while (it.hasNext()) {
            ((d) it.next()).a(dE);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void f() {
        double dE = this.f6714c.e();
        Iterator it = this.f6716e.iterator();
        while (it.hasNext()) {
            ((d) it.next()).b(dE);
        }
    }

    private String a(String str) {
        return this.f6712a != null ? this.f6712a.a(str) : str;
    }
}
