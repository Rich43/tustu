package sun.java2d.pipe.hw;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/* loaded from: rt.jar:sun/java2d/pipe/hw/AccelDeviceEventNotifier.class */
public class AccelDeviceEventNotifier {
    private static AccelDeviceEventNotifier theInstance;
    public static final int DEVICE_RESET = 0;
    public static final int DEVICE_DISPOSED = 1;
    private final Map<AccelDeviceEventListener, Integer> listeners = Collections.synchronizedMap(new HashMap(1));

    private AccelDeviceEventNotifier() {
    }

    private static synchronized AccelDeviceEventNotifier getInstance(boolean z2) {
        if (theInstance == null && z2) {
            theInstance = new AccelDeviceEventNotifier();
        }
        return theInstance;
    }

    public static final void eventOccured(int i2, int i3) {
        AccelDeviceEventNotifier accelDeviceEventNotifier = getInstance(false);
        if (accelDeviceEventNotifier != null) {
            accelDeviceEventNotifier.notifyListeners(i3, i2);
        }
    }

    public static final void addListener(AccelDeviceEventListener accelDeviceEventListener, int i2) {
        getInstance(true).add(accelDeviceEventListener, i2);
    }

    public static final void removeListener(AccelDeviceEventListener accelDeviceEventListener) {
        getInstance(true).remove(accelDeviceEventListener);
    }

    private final void add(AccelDeviceEventListener accelDeviceEventListener, int i2) {
        this.listeners.put(accelDeviceEventListener, Integer.valueOf(i2));
    }

    private final void remove(AccelDeviceEventListener accelDeviceEventListener) {
        this.listeners.remove(accelDeviceEventListener);
    }

    private final void notifyListeners(int i2, int i3) {
        HashMap map;
        synchronized (this.listeners) {
            map = new HashMap(this.listeners);
        }
        for (AccelDeviceEventListener accelDeviceEventListener : map.keySet()) {
            Integer num = (Integer) map.get(accelDeviceEventListener);
            if (num == null || num.intValue() == i3) {
                if (i2 == 0) {
                    accelDeviceEventListener.onDeviceReset();
                } else if (i2 == 1) {
                    accelDeviceEventListener.onDeviceDispose();
                }
            }
        }
    }
}
