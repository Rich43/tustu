package sun.security.pkcs11;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.Provider;
import java.security.ProviderException;
import java.security.Security;
import net.lingala.zip4j.util.InternalZipConstants;

/* loaded from: sunpkcs11.jar:sun/security/pkcs11/P11Util.class */
public final class P11Util {

    /* renamed from: sun, reason: collision with root package name */
    private static volatile Provider f13628sun;
    private static volatile Provider sunRsaSign;
    private static volatile Provider sunJce;
    private static Object LOCK = new Object();
    private static final char[] hexDigits = "0123456789abcdef".toCharArray();

    private P11Util() {
    }

    static Provider getSunProvider() {
        Provider provider = f13628sun;
        if (provider == null) {
            synchronized (LOCK) {
                provider = getProvider(f13628sun, "SUN", "sun.security.provider.Sun");
                f13628sun = provider;
            }
        }
        return provider;
    }

    static Provider getSunRsaSignProvider() {
        Provider provider = sunRsaSign;
        if (provider == null) {
            synchronized (LOCK) {
                provider = getProvider(sunRsaSign, "SunRsaSign", "sun.security.rsa.SunRsaSign");
                sunRsaSign = provider;
            }
        }
        return provider;
    }

    static Provider getSunJceProvider() {
        Provider provider = sunJce;
        if (provider == null) {
            synchronized (LOCK) {
                provider = getProvider(sunJce, "SunJCE", "com.sun.crypto.provider.SunJCE");
                sunJce = provider;
            }
        }
        return provider;
    }

    private static Provider getProvider(Provider provider, String str, String str2) {
        if (provider != null) {
            return provider;
        }
        Provider provider2 = Security.getProvider(str);
        if (provider2 == null) {
            try {
                provider2 = (Provider) Class.forName(str2).newInstance();
            } catch (Exception e2) {
                throw new ProviderException("Could not find provider " + str, e2);
            }
        }
        return provider2;
    }

    static byte[] convert(byte[] bArr, int i2, int i3) {
        if (i2 == 0 && i3 == bArr.length) {
            return bArr;
        }
        byte[] bArr2 = new byte[i3];
        System.arraycopy(bArr, i2, bArr2, 0, i3);
        return bArr2;
    }

    static byte[] subarray(byte[] bArr, int i2, int i3) {
        byte[] bArr2 = new byte[i3];
        System.arraycopy(bArr, i2, bArr2, 0, i3);
        return bArr2;
    }

    static byte[] concat(byte[] bArr, byte[] bArr2) {
        byte[] bArr3 = new byte[bArr.length + bArr2.length];
        System.arraycopy(bArr, 0, bArr3, 0, bArr.length);
        System.arraycopy(bArr2, 0, bArr3, bArr.length, bArr2.length);
        return bArr3;
    }

    static long[] concat(long[] jArr, long[] jArr2) {
        if (jArr.length == 0) {
            return jArr2;
        }
        long[] jArr3 = new long[jArr.length + jArr2.length];
        System.arraycopy(jArr, 0, jArr3, 0, jArr.length);
        System.arraycopy(jArr2, 0, jArr3, jArr.length, jArr2.length);
        return jArr3;
    }

    public static byte[] getMagnitude(BigInteger bigInteger) {
        byte[] byteArray = bigInteger.toByteArray();
        if (byteArray.length > 1 && byteArray[0] == 0) {
            int length = byteArray.length - 1;
            byte[] bArr = new byte[length];
            System.arraycopy(byteArray, 1, bArr, 0, length);
            byteArray = bArr;
        }
        return byteArray;
    }

    static byte[] getBytesUTF8(String str) {
        try {
            return str.getBytes(InternalZipConstants.CHARSET_UTF8);
        } catch (UnsupportedEncodingException e2) {
            throw new RuntimeException(e2);
        }
    }

    static byte[] sha1(byte[] bArr) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            messageDigest.update(bArr);
            return messageDigest.digest();
        } catch (GeneralSecurityException e2) {
            throw new ProviderException(e2);
        }
    }

    static String toString(byte[] bArr) {
        if (bArr == null) {
            return "(null)";
        }
        StringBuffer stringBuffer = new StringBuffer(bArr.length * 3);
        for (int i2 = 0; i2 < bArr.length; i2++) {
            int i3 = bArr[i2] & 255;
            if (i2 != 0) {
                stringBuffer.append(':');
            }
            stringBuffer.append(hexDigits[i3 >>> 4]);
            stringBuffer.append(hexDigits[i3 & 15]);
        }
        return stringBuffer.toString();
    }
}
