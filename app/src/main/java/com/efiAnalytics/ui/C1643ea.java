package com.efiAnalytics.ui;

import java.awt.BorderLayout;
import java.awt.TextField;
import javax.swing.JLabel;
import javax.swing.JPanel;

/* renamed from: com.efiAnalytics.ui.ea, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/ea.class */
class C1643ea extends JPanel {

    /* renamed from: a, reason: collision with root package name */
    TextField f11569a;

    /* renamed from: b, reason: collision with root package name */
    JLabel f11570b;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ dS f11571c;

    C1643ea(dS dSVar, String str, String str2) {
        this(dSVar, str, str2, false);
    }

    C1643ea(dS dSVar, String str, String str2, boolean z2) {
        this.f11571c = dSVar;
        this.f11569a = null;
        this.f11570b = null;
        setLayout(new BorderLayout());
        JLabel jLabel = new JLabel(str + " ");
        jLabel.setHorizontalAlignment(4);
        add(BorderLayout.CENTER, jLabel);
        if (z2) {
            this.f11570b = new JLabel(str2);
            add("East", this.f11570b);
        } else {
            this.f11569a = new TextField(str2);
            this.f11569a.addFocusListener(new C1644eb(this));
            this.f11569a.setColumns(35);
            add("East", this.f11569a);
        }
    }

    @Override // javax.swing.JComponent, java.awt.Component
    public void requestFocus() {
        if (this.f11569a != null) {
            this.f11569a.requestFocus();
        } else {
            super.requestFocus();
        }
    }

    public String a() {
        return this.f11570b == null ? this.f11569a.getText() : this.f11570b.getText();
    }
}
