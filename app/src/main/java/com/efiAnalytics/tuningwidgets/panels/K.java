package com.efiAnalytics.tuningwidgets.panels;

import W.C0184j;
import g.C1733k;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tuningwidgets/panels/K.class */
class K implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0184j f10261a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ Component f10262b;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ I f10263c;

    K(I i2, C0184j c0184j, Component component) {
        this.f10263c = i2;
        this.f10261a = c0184j;
        this.f10262b = component;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) throws NumberFormatException {
        String strA = C1733k.a("{Set " + this.f10261a.a() + " Smoothing Factor}", true, "Smoothing Factor: Larger Number for more Smoothing", true, this.f10262b);
        if (strA != null && !strA.isEmpty()) {
            int i2 = Integer.parseInt(strA);
            if (i2 < 1) {
                this.f10263c.a(this.f10261a);
            } else {
                this.f10263c.a(this.f10261a, i2);
            }
        }
        this.f10262b.repaint();
    }
}
