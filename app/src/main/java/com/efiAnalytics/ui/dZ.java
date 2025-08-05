package com.efiAnalytics.ui;

import java.awt.BorderLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/dZ.class */
public class dZ extends JPanel {

    /* renamed from: a, reason: collision with root package name */
    JComboBox f11390a = new JComboBox();

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ dS f11391b;

    public dZ(dS dSVar, String str, String[] strArr) {
        this.f11391b = dSVar;
        JLabel jLabel = new JLabel(str + " ");
        jLabel.setHorizontalAlignment(4);
        add(BorderLayout.CENTER, jLabel);
        add("East", this.f11390a);
        for (String str2 : strArr) {
            this.f11390a.addItem(str2);
        }
    }

    public String a() {
        return (String) this.f11390a.getSelectedItem();
    }
}
