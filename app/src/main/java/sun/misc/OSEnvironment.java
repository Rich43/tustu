package sun.misc;

import sun.io.Win32ErrorMode;

/* loaded from: rt.jar:sun/misc/OSEnvironment.class */
public class OSEnvironment {
    public static void initialize() {
        Win32ErrorMode.initialize();
    }
}
