package com.efiAnalytics.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Window;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/eB.class */
public class eB extends JDialog {

    /* renamed from: a, reason: collision with root package name */
    dM f11470a;

    /* renamed from: b, reason: collision with root package name */
    ArrayList f11471b;

    /* renamed from: c, reason: collision with root package name */
    JLabel f11472c;

    public eB(Window window, String str, String str2, boolean z2, boolean z3) throws IllegalArgumentException {
        super(window, str, JDialog.DEFAULT_MODALITY_TYPE);
        this.f11470a = new dM();
        this.f11471b = new ArrayList();
        this.f11472c = new JLabel("", 0);
        super.setModal(z3);
        setLayout(new BorderLayout());
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        this.f11472c.setText(str2);
        jPanel.add(BorderLayout.CENTER, this.f11472c);
        jPanel.add("South", this.f11470a);
        add(BorderLayout.CENTER, jPanel);
        add("North", new JLabel("   "));
        add("West", new JLabel("   "));
        add("East", new JLabel("   "));
        if (z2) {
            JPanel jPanel2 = new JPanel();
            jPanel2.setLayout(new FlowLayout(2));
            JButton jButton = new JButton("Cancel");
            jButton.addActionListener(new eC(this));
            jPanel2.add(jButton);
            add("South", jPanel2);
        }
        pack();
        bV.a(window, (Component) this);
    }

    public void a(double d2) {
        this.f11470a.b(d2);
    }

    public void a(String str) throws IllegalArgumentException {
        this.f11472c.setText(str);
    }

    public void a(aQ aQVar) {
        this.f11471b.add(aQVar);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a() {
        Iterator it = this.f11471b.iterator();
        while (it.hasNext()) {
            ((aQ) it.next()).a();
        }
    }
}
