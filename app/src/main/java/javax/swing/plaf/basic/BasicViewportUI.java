package javax.swing.plaf.basic;

import javax.swing.JComponent;
import javax.swing.LookAndFeel;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.ViewportUI;

/* loaded from: rt.jar:javax/swing/plaf/basic/BasicViewportUI.class */
public class BasicViewportUI extends ViewportUI {
    private static ViewportUI viewportUI;

    public static ComponentUI createUI(JComponent jComponent) {
        if (viewportUI == null) {
            viewportUI = new BasicViewportUI();
        }
        return viewportUI;
    }

    @Override // javax.swing.plaf.ComponentUI
    public void installUI(JComponent jComponent) {
        super.installUI(jComponent);
        installDefaults(jComponent);
    }

    @Override // javax.swing.plaf.ComponentUI
    public void uninstallUI(JComponent jComponent) {
        uninstallDefaults(jComponent);
        super.uninstallUI(jComponent);
    }

    protected void installDefaults(JComponent jComponent) {
        LookAndFeel.installColorsAndFont(jComponent, "Viewport.background", "Viewport.foreground", "Viewport.font");
        LookAndFeel.installProperty(jComponent, "opaque", Boolean.TRUE);
    }

    protected void uninstallDefaults(JComponent jComponent) {
    }
}
