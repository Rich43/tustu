package com.efiAnalytics.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/U.class */
class U implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ int[] f10705a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ T f10706b;

    U(T t2, int[] iArr) {
        this.f10706b = t2;
        this.f10705a = iArr;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        for (int i2 = 0; i2 < this.f10705a.length; i2++) {
            this.f10706b.f10704c.getModel().setValueAt(this.f10706b.f10704c.f10698b[this.f10705a[i2]], this.f10706b.f10703b ? this.f10705a[i2] : 0, this.f10706b.f10703b ? 0 : this.f10705a[i2]);
        }
    }
}
