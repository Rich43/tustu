package com.efiAnalytics.apps.ts.dashboard;

import com.efiAnalytics.ui.Cdo;
import com.efiAnalytics.ui.bV;
import com.efiAnalytics.ui.cF;
import com.efiAnalytics.ui.eJ;
import com.sun.org.apache.xerces.internal.xinclude.XIncludeHandler;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Window;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import s.C1818g;

/* renamed from: com.efiAnalytics.apps.ts.dashboard.b, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/dashboard/b.class */
public class C1403b extends JPanel {

    /* renamed from: a, reason: collision with root package name */
    Cdo f9488a = new Cdo("", 4);

    /* renamed from: b, reason: collision with root package name */
    Cdo f9489b = new Cdo("", 4);

    /* renamed from: c, reason: collision with root package name */
    Dimension f9490c = eJ.a(200, 10);

    /* renamed from: d, reason: collision with root package name */
    JDialog f9491d = null;

    /* renamed from: e, reason: collision with root package name */
    C1406e f9492e = null;

    public C1403b() {
        setBorder(BorderFactory.createTitledBorder(a("Forced Dash Aspect Ratio")));
        setLayout(new GridLayout(0, 1));
        add(new JLabel(a("Set the width and height or ratio you want for this dash.")));
        add(new JLabel(a("The actual dimensions will not be forced.")));
        add(new JLabel(a("The ratio between them will be.")));
        add(a("Forced Width", this.f9488a, "The width part of the Width to Heigh ratio this dashboard will be forced to. The dash will not be forced to this specific width, but the raio between width and height will be enforced to ensure consistent scaling."));
        add(a("Forced Height", this.f9489b, "The height part of the Width to Heigh ratio this dashboard will be forced to. The dash will not be forced to this specific height, but the raio between width and height will be enforced to ensure consistent scaling."));
    }

    public void a(int i2, int i3) {
        this.f9488a.setText(i2 + "");
        this.f9489b.setText(i3 + "");
    }

    @Override // java.awt.Container, bA.f
    public final Component add(Component component) {
        return super.add(component);
    }

    private JPanel a(String str, Component component, String str2) {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout(eJ.a(5), eJ.a(5)));
        JLabel jLabel = new JLabel(a(str), 4);
        jLabel.setPreferredSize(this.f9490c);
        jLabel.setMinimumSize(this.f9490c);
        jPanel.add("West", jLabel);
        jPanel.add(BorderLayout.CENTER, component);
        jPanel.add("East", new cF(str2, bV.a()));
        return jPanel;
    }

    private String a(String str) {
        return bV.a() != null ? bV.a().a(str) : str;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a() {
        if (this.f9491d != null) {
            this.f9492e = null;
            this.f9491d.dispose();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b() {
        if (!c()) {
            bV.d(a("Width and Height must be valid integer values!"), this);
            return;
        }
        this.f9492e = new C1406e(this, Integer.parseInt(this.f9488a.getText()), Integer.parseInt(this.f9489b.getText()));
        if (this.f9491d != null) {
            this.f9491d.dispose();
        }
    }

    private boolean c() {
        try {
            Integer.parseInt(this.f9488a.getText());
            Integer.parseInt(this.f9489b.getText());
            return true;
        } catch (Exception e2) {
            return false;
        }
    }

    public C1406e a(Component component) {
        this.f9491d = new JDialog(bV.a(component), C1818g.b("Dashboard Forced Aspect Ratio"));
        this.f9491d.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        this.f9491d.add(BorderLayout.CENTER, this);
        JButton jButton = new JButton(C1818g.b("Cancel"));
        jButton.addActionListener(new C1404c(this));
        JButton jButton2 = new JButton(C1818g.b(XIncludeHandler.HTTP_ACCEPT));
        jButton2.addActionListener(new C1405d(this));
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new FlowLayout(2));
        if (bV.d()) {
            jPanel.add(jButton2);
            jPanel.add(jButton);
        } else {
            jPanel.add(jButton);
            jPanel.add(jButton2);
        }
        this.f9491d.add("South", jPanel);
        this.f9491d.pack();
        bV.a((Window) bV.a(component), (Component) this.f9491d);
        this.f9491d.setVisible(true);
        return this.f9492e;
    }
}
