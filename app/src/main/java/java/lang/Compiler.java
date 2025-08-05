package java.lang;

import java.security.AccessController;
import java.security.PrivilegedAction;

/* loaded from: rt.jar:java/lang/Compiler.class */
public final class Compiler {
    /* JADX INFO: Access modifiers changed from: private */
    public static native void initialize();

    private static native void registerNatives();

    public static native boolean compileClass(Class<?> cls);

    public static native boolean compileClasses(String str);

    public static native Object command(Object obj);

    public static native void enable();

    public static native void disable();

    private Compiler() {
    }

    static {
        registerNatives();
        AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: java.lang.Compiler.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Void run2() {
                boolean z2 = false;
                String property = System.getProperty("java.compiler");
                if (property != null && !property.equals("NONE") && !property.equals("")) {
                    try {
                        System.loadLibrary(property);
                        Compiler.initialize();
                        z2 = true;
                    } catch (UnsatisfiedLinkError e2) {
                        System.err.println("Warning: JIT compiler \"" + property + "\" not found. Will use interpreter.");
                    }
                }
                String property2 = System.getProperty("java.vm.info");
                if (z2) {
                    System.setProperty("java.vm.info", property2 + ", " + property);
                    return null;
                }
                System.setProperty("java.vm.info", property2 + ", nojit");
                return null;
            }
        });
    }
}
