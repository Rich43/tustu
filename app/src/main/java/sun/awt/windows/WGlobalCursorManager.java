package sun.awt.windows;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Point;
import sun.awt.GlobalCursorManager;

/* loaded from: rt.jar:sun/awt/windows/WGlobalCursorManager.class */
final class WGlobalCursorManager extends GlobalCursorManager {
    private static WGlobalCursorManager manager;

    @Override // sun.awt.GlobalCursorManager
    protected native void setCursor(Component component, Cursor cursor, boolean z2);

    @Override // sun.awt.GlobalCursorManager
    protected native void getCursorPos(Point point);

    @Override // sun.awt.GlobalCursorManager
    protected native Component findHeavyweightUnderCursor(boolean z2);

    @Override // sun.awt.GlobalCursorManager
    protected native Point getLocationOnScreen(Component component);

    WGlobalCursorManager() {
    }

    public static GlobalCursorManager getCursorManager() {
        if (manager == null) {
            manager = new WGlobalCursorManager();
        }
        return manager;
    }

    public static void nativeUpdateCursor(Component component) {
        getCursorManager().updateCursorLater(component);
    }
}
