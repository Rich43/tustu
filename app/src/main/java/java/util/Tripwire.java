package java.util;

import java.security.AccessController;
import sun.util.logging.PlatformLogger;

/* loaded from: rt.jar:java/util/Tripwire.class */
final class Tripwire {
    private static final String TRIPWIRE_PROPERTY = "org.openjdk.java.util.stream.tripwire";
    static final boolean ENABLED = ((Boolean) AccessController.doPrivileged(() -> {
        return Boolean.valueOf(Boolean.getBoolean(TRIPWIRE_PROPERTY));
    })).booleanValue();

    private Tripwire() {
    }

    static void trip(Class<?> cls, String str) {
        PlatformLogger.getLogger(cls.getName()).warning(str, cls.getName());
    }
}
