package com.efiAnalytics.tuningwidgets.panels;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tuningwidgets/panels/aQ.class */
public class aQ extends JPanel {

    /* renamed from: a, reason: collision with root package name */
    JTextField f10375a;

    /* renamed from: b, reason: collision with root package name */
    JTextField f10376b;

    public aQ(String str) {
        setBorder(BorderFactory.createTitledBorder(str));
        aT aTVar = new aT(this);
        setLayout(new GridLayout(1, 2, 14, 14));
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        jPanel.add("East", new JLabel("lbs."));
        this.f10375a = new JTextField("", 6);
        this.f10375a.addKeyListener(new aR(this));
        this.f10375a.addFocusListener(aTVar);
        jPanel.add(BorderLayout.CENTER, this.f10375a);
        add(jPanel);
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new BorderLayout());
        jPanel2.add("East", new JLabel("kg."));
        this.f10376b = new JTextField("", 6);
        this.f10376b.addKeyListener(new aS(this));
        this.f10376b.addFocusListener(aTVar);
        jPanel2.add(BorderLayout.CENTER, this.f10376b);
        add(jPanel2);
    }

    public String a() {
        return this.f10375a.getText();
    }

    public void a(String str) {
        try {
            int i2 = (Integer.parseInt(str) * 10) / 22;
            this.f10375a.setText(str);
            this.f10376b.setText(i2 + "");
        } catch (NumberFormatException e2) {
        }
    }
}
