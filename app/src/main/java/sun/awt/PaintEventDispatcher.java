package sun.awt;

import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.PaintEvent;

/* loaded from: rt.jar:sun/awt/PaintEventDispatcher.class */
public class PaintEventDispatcher {
    private static PaintEventDispatcher dispatcher;

    public static void setPaintEventDispatcher(PaintEventDispatcher paintEventDispatcher) {
        synchronized (PaintEventDispatcher.class) {
            dispatcher = paintEventDispatcher;
        }
    }

    public static PaintEventDispatcher getPaintEventDispatcher() {
        PaintEventDispatcher paintEventDispatcher;
        synchronized (PaintEventDispatcher.class) {
            if (dispatcher == null) {
                dispatcher = new PaintEventDispatcher();
            }
            paintEventDispatcher = dispatcher;
        }
        return paintEventDispatcher;
    }

    public PaintEvent createPaintEvent(Component component, int i2, int i3, int i4, int i5) {
        return new PaintEvent(component, 800, new Rectangle(i2, i3, i4, i5));
    }

    public boolean shouldDoNativeBackgroundErase(Component component) {
        return true;
    }

    public boolean queueSurfaceDataReplacing(Component component, Runnable runnable) {
        return false;
    }
}
