package com.efiAnalytics.ui;

import java.awt.Component;
import java.awt.HeadlessException;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

/* renamed from: com.efiAnalytics.ui.ca, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/ca.class */
final class RunnableC1590ca implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ String f11190a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ Component f11191b;

    RunnableC1590ca(String str, Component component) {
        this.f11190a = str;
        this.f11191b = component;
    }

    @Override // java.lang.Runnable
    public void run() throws HeadlessException {
        JDialog jDialogCreateDialog = new JOptionPane(this.f11190a, 3).createDialog(this.f11191b, bV.b("Information"));
        jDialogCreateDialog.setModal(false);
        jDialogCreateDialog.setVisible(true);
    }
}
