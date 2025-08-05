package sun.security.krb5;

import java.security.SecureRandom;

/* loaded from: rt.jar:sun/security/krb5/Confounder.class */
public final class Confounder {
    private static SecureRandom srand = new SecureRandom();

    private Confounder() {
    }

    public static byte[] bytes(int i2) {
        byte[] bArr = new byte[i2];
        srand.nextBytes(bArr);
        return bArr;
    }

    public static int intValue() {
        return srand.nextInt();
    }

    public static long longValue() {
        return srand.nextLong();
    }
}
