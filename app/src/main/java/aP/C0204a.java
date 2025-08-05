package aP;

import java.awt.KeyboardFocusManager;
import java.awt.Window;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeListener;
import javax.swing.JDialog;

/* renamed from: aP.a, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/a.class */
public class C0204a {

    /* renamed from: a, reason: collision with root package name */
    static Window f2793a;

    /* renamed from: b, reason: collision with root package name */
    private static WindowListener f2794b = new C0231b();

    /* renamed from: c, reason: collision with root package name */
    private static PropertyChangeListener f2795c = new C0258c();

    public static Window a() {
        if (f2793a != null) {
            return f2793a;
        }
        JDialog jDialog = new JDialog();
        Window owner = new JDialog().getOwner();
        jDialog.dispose();
        return owner;
    }

    static {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addPropertyChangeListener("activeWindow", f2795c);
    }
}
