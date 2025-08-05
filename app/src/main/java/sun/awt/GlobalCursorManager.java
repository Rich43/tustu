package sun.awt;

import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.IllegalComponentStateException;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.InputEvent;
import java.awt.event.InvocationEvent;

/* loaded from: rt.jar:sun/awt/GlobalCursorManager.class */
public abstract class GlobalCursorManager {
    private long lastUpdateMillis;
    private final NativeUpdater nativeUpdater = new NativeUpdater();
    private final Object lastUpdateLock = new Object();

    protected abstract void setCursor(Component component, Cursor cursor, boolean z2);

    protected abstract void getCursorPos(Point point);

    protected abstract Point getLocationOnScreen(Component component);

    protected abstract Component findHeavyweightUnderCursor(boolean z2);

    /* loaded from: rt.jar:sun/awt/GlobalCursorManager$NativeUpdater.class */
    class NativeUpdater implements Runnable {
        boolean pending = false;

        NativeUpdater() {
        }

        @Override // java.lang.Runnable
        public void run() {
            boolean z2 = false;
            synchronized (this) {
                if (this.pending) {
                    this.pending = false;
                    z2 = true;
                }
            }
            if (z2) {
                GlobalCursorManager.this._updateCursor(false);
            }
        }

        public void postIfNotPending(Component component, InvocationEvent invocationEvent) {
            boolean z2 = false;
            synchronized (this) {
                if (!this.pending) {
                    z2 = true;
                    this.pending = true;
                }
            }
            if (z2) {
                SunToolkit.postEvent(SunToolkit.targetToAppContext(component), invocationEvent);
            }
        }
    }

    public void updateCursorImmediately() {
        synchronized (this.nativeUpdater) {
            this.nativeUpdater.pending = false;
        }
        _updateCursor(false);
    }

    public void updateCursorImmediately(InputEvent inputEvent) {
        boolean z2;
        synchronized (this.lastUpdateLock) {
            z2 = inputEvent.getWhen() >= this.lastUpdateMillis;
        }
        if (z2) {
            _updateCursor(true);
        }
    }

    public void updateCursorLater(Component component) {
        this.nativeUpdater.postIfNotPending(component, new InvocationEvent(Toolkit.getDefaultToolkit(), this.nativeUpdater));
    }

    protected GlobalCursorManager() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void _updateCursor(boolean z2) {
        synchronized (this.lastUpdateLock) {
            this.lastUpdateMillis = System.currentTimeMillis();
        }
        Point locationOnScreen = null;
        try {
            Component componentFindHeavyweightUnderCursor = findHeavyweightUnderCursor(z2);
            if (componentFindHeavyweightUnderCursor == null) {
                updateCursorOutOfJava();
                return;
            }
            if (componentFindHeavyweightUnderCursor instanceof Window) {
                locationOnScreen = AWTAccessor.getComponentAccessor().getLocation(componentFindHeavyweightUnderCursor);
            } else if (componentFindHeavyweightUnderCursor instanceof Container) {
                locationOnScreen = getLocationOnScreen(componentFindHeavyweightUnderCursor);
            }
            if (locationOnScreen != null) {
                Point point = new Point();
                getCursorPos(point);
                Component componentFindComponentAt = AWTAccessor.getContainerAccessor().findComponentAt((Container) componentFindHeavyweightUnderCursor, point.f12370x - locationOnScreen.f12370x, point.f12371y - locationOnScreen.f12371y, false);
                if (componentFindComponentAt != null) {
                    componentFindHeavyweightUnderCursor = componentFindComponentAt;
                }
            }
            setCursor(componentFindHeavyweightUnderCursor, AWTAccessor.getComponentAccessor().getCursor(componentFindHeavyweightUnderCursor), z2);
        } catch (IllegalComponentStateException e2) {
        }
    }

    protected void updateCursorOutOfJava() {
    }
}
