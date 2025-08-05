package sun.security.provider;

/* loaded from: rt.jar:sun/security/provider/NativePRNG.class */
public final class NativePRNG {
    static boolean isAvailable() {
        return false;
    }

    /* loaded from: rt.jar:sun/security/provider/NativePRNG$NonBlocking.class */
    public static final class NonBlocking {
        static boolean isAvailable() {
            return false;
        }
    }

    /* loaded from: rt.jar:sun/security/provider/NativePRNG$Blocking.class */
    public static final class Blocking {
        static boolean isAvailable() {
            return false;
        }
    }
}
