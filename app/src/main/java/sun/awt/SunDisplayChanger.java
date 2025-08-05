package sun.awt;

import java.awt.IllegalComponentStateException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.WeakHashMap;
import sun.util.logging.PlatformLogger;

/* loaded from: rt.jar:sun/awt/SunDisplayChanger.class */
public class SunDisplayChanger {
    private static final PlatformLogger log = PlatformLogger.getLogger("sun.awt.multiscreen.SunDisplayChanger");
    private Map<DisplayChangedListener, Void> listeners = Collections.synchronizedMap(new WeakHashMap(1));

    public void add(DisplayChangedListener displayChangedListener) {
        if (log.isLoggable(PlatformLogger.Level.FINE) && displayChangedListener == null) {
            log.fine("Assertion (theListener != null) failed");
        }
        if (log.isLoggable(PlatformLogger.Level.FINER)) {
            log.finer("Adding listener: " + ((Object) displayChangedListener));
        }
        this.listeners.put(displayChangedListener, null);
    }

    public void remove(DisplayChangedListener displayChangedListener) {
        if (log.isLoggable(PlatformLogger.Level.FINE) && displayChangedListener == null) {
            log.fine("Assertion (theListener != null) failed");
        }
        if (log.isLoggable(PlatformLogger.Level.FINER)) {
            log.finer("Removing listener: " + ((Object) displayChangedListener));
        }
        this.listeners.remove(displayChangedListener);
    }

    public void notifyListeners() {
        HashSet<DisplayChangedListener> hashSet;
        if (log.isLoggable(PlatformLogger.Level.FINEST)) {
            log.finest("notifyListeners");
        }
        synchronized (this.listeners) {
            hashSet = new HashSet(this.listeners.keySet());
        }
        for (DisplayChangedListener displayChangedListener : hashSet) {
            try {
                if (log.isLoggable(PlatformLogger.Level.FINEST)) {
                    log.finest("displayChanged for listener: " + ((Object) displayChangedListener));
                }
                displayChangedListener.displayChanged();
            } catch (IllegalComponentStateException e2) {
                this.listeners.remove(displayChangedListener);
            }
        }
    }

    public void notifyPaletteChanged() {
        HashSet<DisplayChangedListener> hashSet;
        if (log.isLoggable(PlatformLogger.Level.FINEST)) {
            log.finest("notifyPaletteChanged");
        }
        synchronized (this.listeners) {
            hashSet = new HashSet(this.listeners.keySet());
        }
        for (DisplayChangedListener displayChangedListener : hashSet) {
            try {
                if (log.isLoggable(PlatformLogger.Level.FINEST)) {
                    log.finest("paletteChanged for listener: " + ((Object) displayChangedListener));
                }
                displayChangedListener.paletteChanged();
            } catch (IllegalComponentStateException e2) {
                this.listeners.remove(displayChangedListener);
            }
        }
    }
}
