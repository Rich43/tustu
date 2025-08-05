package sun.security.jca;

import java.security.SecureRandom;

/* loaded from: rt.jar:sun/security/jca/JCAUtil.class */
public final class JCAUtil {
    private static final int ARRAY_SIZE = 4096;

    private JCAUtil() {
    }

    public static int getTempArraySize(int i2) {
        return Math.min(4096, i2);
    }

    /* loaded from: rt.jar:sun/security/jca/JCAUtil$CachedSecureRandomHolder.class */
    private static class CachedSecureRandomHolder {
        public static SecureRandom instance = new SecureRandom();

        private CachedSecureRandomHolder() {
        }
    }

    public static SecureRandom getSecureRandom() {
        return CachedSecureRandomHolder.instance;
    }
}
