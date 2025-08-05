package com.efiAnalytics.ui;

import java.awt.Component;
import java.awt.HeadlessException;
import javax.swing.JOptionPane;

/* renamed from: com.efiAnalytics.ui.cb, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/cb.class */
final class RunnableC1591cb implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ Component f11240a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ String f11241b;

    RunnableC1591cb(Component component, String str) {
        this.f11240a = component;
        this.f11241b = str;
    }

    @Override // java.lang.Runnable
    public void run() throws HeadlessException {
        JOptionPane.showMessageDialog(this.f11240a, this.f11241b);
    }
}
