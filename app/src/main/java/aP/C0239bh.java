package aP;

import java.awt.KeyboardFocusManager;
import java.awt.Window;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JFrame;

/* renamed from: aP.bh, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/bh.class */
public class C0239bh implements WindowListener {

    /* renamed from: a, reason: collision with root package name */
    JFrame f3073a;

    /* renamed from: b, reason: collision with root package name */
    Window f3074b;

    @Override // java.awt.event.WindowListener
    public void windowOpened(WindowEvent windowEvent) {
    }

    @Override // java.awt.event.WindowListener
    public void windowClosing(WindowEvent windowEvent) {
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
        if (!windowEvent.getSource().equals(this.f3073a) || this.f3074b == null || this.f3074b.equals(this.f3073a)) {
            return;
        }
        this.f3074b.requestFocusInWindow();
    }

    @Override // java.awt.event.WindowListener
    public void windowDeactivated(WindowEvent windowEvent) {
        this.f3074b = KeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow();
    }
}
