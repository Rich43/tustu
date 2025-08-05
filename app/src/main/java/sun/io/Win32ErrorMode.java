package sun.io;

import sun.misc.VM;

/* loaded from: rt.jar:sun/io/Win32ErrorMode.class */
public class Win32ErrorMode {
    private static final long SEM_FAILCRITICALERRORS = 1;
    private static final long SEM_NOGPFAULTERRORBOX = 2;
    private static final long SEM_NOALIGNMENTFAULTEXCEPT = 4;
    private static final long SEM_NOOPENFILEERRORBOX = 32768;

    private static native long setErrorMode(long j2);

    private Win32ErrorMode() {
    }

    public static void initialize() {
        if (!VM.isBooted()) {
            String property = System.getProperty("sun.io.allowCriticalErrorMessageBox");
            if (property == null || property.equals(Boolean.FALSE.toString())) {
                setErrorMode(setErrorMode(0L) | 1);
            }
        }
    }
}
