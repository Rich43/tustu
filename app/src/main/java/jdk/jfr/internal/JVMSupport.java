package jdk.jfr.internal;

import java.io.IOException;

/* loaded from: jfr.jar:jdk/jfr/internal/JVMSupport.class */
public final class JVMSupport {
    private static final String UNSUPPORTED_VM_MESSAGE = "Flight Recorder is not supported on this VM";
    private static final boolean notAvailable;

    static {
        notAvailable = !checkAvailability();
    }

    private static boolean checkAvailability() {
        try {
            if (SecuritySupport.getBooleanProperty("jfr.unsupported.vm")) {
                return false;
            }
            try {
                JVM.getJVM().isAvailable();
                return true;
            } catch (Throwable th) {
                return false;
            }
        } catch (NoClassDefFoundError e2) {
            return false;
        }
    }

    public static void ensureWithInternalError() {
        if (notAvailable) {
            throw new InternalError(UNSUPPORTED_VM_MESSAGE);
        }
    }

    public static void ensureWithIOException() throws IOException {
        if (notAvailable) {
            throw new IOException(UNSUPPORTED_VM_MESSAGE);
        }
    }

    public static void ensureWithIllegalStateException() {
        if (notAvailable) {
            throw new IllegalStateException(UNSUPPORTED_VM_MESSAGE);
        }
    }

    public static boolean isNotAvailable() {
        return notAvailable;
    }

    public static void tryToInitializeJVM() {
    }
}
