package com.efiAnalytics.ui;

import java.awt.Window;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JDialog;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/fP.class */
class fP extends JDialog implements WindowListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ fK f11657a;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    fP(fK fKVar, Window window, String str) {
        super(window, str);
        this.f11657a = fKVar;
        addWindowListener(this);
    }

    @Override // java.awt.event.WindowListener
    public void windowOpened(WindowEvent windowEvent) {
    }

    @Override // java.awt.event.WindowListener
    public void windowClosing(WindowEvent windowEvent) {
        this.f11657a.d();
    }

    @Override // java.awt.event.WindowListener
    public void windowClosed(WindowEvent windowEvent) {
    }

    @Override // java.awt.event.WindowListener
    public void windowIconified(WindowEvent windowEvent) {
    }

    @Override // java.awt.event.WindowListener
    public void windowDeiconified(WindowEvent windowEvent) {
    }

    @Override // java.awt.event.WindowListener
    public void windowActivated(WindowEvent windowEvent) {
    }

    @Override // java.awt.event.WindowListener
    public void windowDeactivated(WindowEvent windowEvent) {
    }
}
