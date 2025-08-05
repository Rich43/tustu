package javax.swing;

import java.awt.Component;
import java.awt.Container;
import java.awt.Rectangle;
import java.awt.event.PaintEvent;
import java.security.AccessController;
import sun.awt.AppContext;
import sun.awt.PaintEventDispatcher;
import sun.awt.SunToolkit;
import sun.awt.event.IgnorePaintEvent;
import sun.security.action.GetBooleanAction;
import sun.security.action.GetPropertyAction;

/* loaded from: rt.jar:javax/swing/SwingPaintEventDispatcher.class */
class SwingPaintEventDispatcher extends PaintEventDispatcher {
    private static final boolean SHOW_FROM_DOUBLE_BUFFER = "true".equals(AccessController.doPrivileged(new GetPropertyAction("swing.showFromDoubleBuffer", "true")));
    private static final boolean ERASE_BACKGROUND = ((Boolean) AccessController.doPrivileged(new GetBooleanAction("swing.nativeErase"))).booleanValue();

    SwingPaintEventDispatcher() {
    }

    @Override // sun.awt.PaintEventDispatcher
    public PaintEvent createPaintEvent(Component component, int i2, int i3, int i4, int i5) {
        if (component instanceof RootPaneContainer) {
            AppContext appContextTargetToAppContext = SunToolkit.targetToAppContext(component);
            RepaintManager repaintManagerCurrentManager = RepaintManager.currentManager(appContextTargetToAppContext);
            if (!SHOW_FROM_DOUBLE_BUFFER || !repaintManagerCurrentManager.show((Container) component, i2, i3, i4, i5)) {
                repaintManagerCurrentManager.nativeAddDirtyRegion(appContextTargetToAppContext, (Container) component, i2, i3, i4, i5);
            }
            return new IgnorePaintEvent(component, 800, new Rectangle(i2, i3, i4, i5));
        }
        if (component instanceof SwingHeavyWeight) {
            AppContext appContextTargetToAppContext2 = SunToolkit.targetToAppContext(component);
            RepaintManager.currentManager(appContextTargetToAppContext2).nativeAddDirtyRegion(appContextTargetToAppContext2, (Container) component, i2, i3, i4, i5);
            return new IgnorePaintEvent(component, 800, new Rectangle(i2, i3, i4, i5));
        }
        return super.createPaintEvent(component, i2, i3, i4, i5);
    }

    @Override // sun.awt.PaintEventDispatcher
    public boolean shouldDoNativeBackgroundErase(Component component) {
        return ERASE_BACKGROUND || !(component instanceof RootPaneContainer);
    }

    @Override // sun.awt.PaintEventDispatcher
    public boolean queueSurfaceDataReplacing(Component component, Runnable runnable) {
        if (component instanceof RootPaneContainer) {
            AppContext appContextTargetToAppContext = SunToolkit.targetToAppContext(component);
            RepaintManager.currentManager(appContextTargetToAppContext).nativeQueueSurfaceDataRunnable(appContextTargetToAppContext, component, runnable);
            return true;
        }
        return super.queueSurfaceDataReplacing(component, runnable);
    }
}
