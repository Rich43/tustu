package com.efiAnalytics.ui;

import javax.swing.text.JTextComponent;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/Z.class */
class Z implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ JTextComponent f10716a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ S f10717b;

    Z(S s2, JTextComponent jTextComponent) {
        this.f10717b = s2;
        this.f10716a = jTextComponent;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.f10716a.selectAll();
    }
}
