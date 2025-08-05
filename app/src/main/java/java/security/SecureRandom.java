package java.security;

import java.security.Provider;
import java.util.Iterator;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import sun.security.jca.GetInstance;
import sun.security.jca.Providers;
import sun.security.util.Debug;

/* loaded from: rt.jar:java/security/SecureRandom.class */
public class SecureRandom extends Random {
    private static final Debug pdebug = Debug.getInstance("provider", "Provider");
    private static final boolean skipDebug;
    private Provider provider;
    private SecureRandomSpi secureRandomSpi;
    private String algorithm;
    private static volatile SecureRandom seedGenerator;
    static final long serialVersionUID = 4940670005562187L;
    private byte[] state;
    private MessageDigest digest;
    private byte[] randomBytes;
    private int randomBytesUsed;
    private long counter;

    static {
        skipDebug = Debug.isOn("engine=") && !Debug.isOn("securerandom");
        seedGenerator = null;
    }

    public SecureRandom() {
        super(0L);
        this.provider = null;
        this.secureRandomSpi = null;
        this.digest = null;
        getDefaultPRNG(false, null);
    }

    public SecureRandom(byte[] bArr) {
        super(0L);
        this.provider = null;
        this.secureRandomSpi = null;
        this.digest = null;
        getDefaultPRNG(true, bArr);
    }

    private void getDefaultPRNG(boolean z2, byte[] bArr) {
        String prngAlgorithm = getPrngAlgorithm();
        if (prngAlgorithm == null) {
            prngAlgorithm = "SHA1PRNG";
            this.secureRandomSpi = new sun.security.provider.SecureRandom();
            this.provider = Providers.getSunProvider();
            if (z2) {
                this.secureRandomSpi.engineSetSeed(bArr);
            }
        } else {
            try {
                SecureRandom secureRandom = getInstance(prngAlgorithm);
                this.secureRandomSpi = secureRandom.getSecureRandomSpi();
                this.provider = secureRandom.getProvider();
                if (z2) {
                    this.secureRandomSpi.engineSetSeed(bArr);
                }
            } catch (NoSuchAlgorithmException e2) {
                throw new RuntimeException(e2);
            }
        }
        if (getClass() == SecureRandom.class) {
            this.algorithm = prngAlgorithm;
        }
    }

    protected SecureRandom(SecureRandomSpi secureRandomSpi, Provider provider) {
        this(secureRandomSpi, provider, null);
    }

    private SecureRandom(SecureRandomSpi secureRandomSpi, Provider provider, String str) {
        super(0L);
        this.provider = null;
        this.secureRandomSpi = null;
        this.digest = null;
        this.secureRandomSpi = secureRandomSpi;
        this.provider = provider;
        this.algorithm = str;
        if (!skipDebug && pdebug != null) {
            pdebug.println("SecureRandom." + str + " algorithm from: " + this.provider.getName());
        }
    }

    public static SecureRandom getInstance(String str) throws NoSuchAlgorithmException {
        GetInstance.Instance getInstance = GetInstance.getInstance("SecureRandom", (Class<?>) SecureRandomSpi.class, str);
        return new SecureRandom((SecureRandomSpi) getInstance.impl, getInstance.provider, str);
    }

    public static SecureRandom getInstance(String str, String str2) throws NoSuchAlgorithmException, NoSuchProviderException {
        GetInstance.Instance getInstance = GetInstance.getInstance("SecureRandom", (Class<?>) SecureRandomSpi.class, str, str2);
        return new SecureRandom((SecureRandomSpi) getInstance.impl, getInstance.provider, str);
    }

    public static SecureRandom getInstance(String str, Provider provider) throws NoSuchAlgorithmException {
        GetInstance.Instance getInstance = GetInstance.getInstance("SecureRandom", (Class<?>) SecureRandomSpi.class, str, provider);
        return new SecureRandom((SecureRandomSpi) getInstance.impl, getInstance.provider, str);
    }

    SecureRandomSpi getSecureRandomSpi() {
        return this.secureRandomSpi;
    }

    public final Provider getProvider() {
        return this.provider;
    }

    public String getAlgorithm() {
        return this.algorithm != null ? this.algorithm : "unknown";
    }

    public synchronized void setSeed(byte[] bArr) {
        this.secureRandomSpi.engineSetSeed(bArr);
    }

    @Override // java.util.Random
    public void setSeed(long j2) {
        if (j2 != 0) {
            this.secureRandomSpi.engineSetSeed(longToByteArray(j2));
        }
    }

    @Override // java.util.Random
    public void nextBytes(byte[] bArr) {
        this.secureRandomSpi.engineNextBytes(bArr);
    }

    @Override // java.util.Random
    protected final int next(int i2) {
        int i3 = (i2 + 7) / 8;
        byte[] bArr = new byte[i3];
        int i4 = 0;
        nextBytes(bArr);
        for (int i5 = 0; i5 < i3; i5++) {
            i4 = (i4 << 8) + (bArr[i5] & 255);
        }
        return i4 >>> ((i3 * 8) - i2);
    }

    public static byte[] getSeed(int i2) {
        if (seedGenerator == null) {
            seedGenerator = new SecureRandom();
        }
        return seedGenerator.generateSeed(i2);
    }

    public byte[] generateSeed(int i2) {
        return this.secureRandomSpi.engineGenerateSeed(i2);
    }

    private static byte[] longToByteArray(long j2) {
        byte[] bArr = new byte[8];
        for (int i2 = 0; i2 < 8; i2++) {
            bArr[i2] = (byte) j2;
            j2 >>= 8;
        }
        return bArr;
    }

    private static String getPrngAlgorithm() {
        Iterator<Provider> it = Providers.getProviderList().providers().iterator();
        while (it.hasNext()) {
            for (Provider.Service service : it.next().getServices()) {
                if (service.getType().equals("SecureRandom")) {
                    return service.getAlgorithm();
                }
            }
        }
        return null;
    }

    /* loaded from: rt.jar:java/security/SecureRandom$StrongPatternHolder.class */
    private static final class StrongPatternHolder {
        private static Pattern pattern = Pattern.compile("\\s*([\\S&&[^:,]]*)(\\:([\\S&&[^,]]*))?\\s*(\\,(.*))?");

        private StrongPatternHolder() {
        }
    }

    public static SecureRandom getInstanceStrong() throws NoSuchAlgorithmException {
        String str = (String) AccessController.doPrivileged(new PrivilegedAction<String>() { // from class: java.security.SecureRandom.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public String run() {
                return Security.getProperty("securerandom.strongAlgorithms");
            }
        });
        if (str == null || str.length() == 0) {
            throw new NoSuchAlgorithmException("Null/empty securerandom.strongAlgorithms Security Property");
        }
        String strGroup = str;
        while (true) {
            String str2 = strGroup;
            if (str2 != null) {
                Matcher matcher = StrongPatternHolder.pattern.matcher(str2);
                if (matcher.matches()) {
                    String strGroup2 = matcher.group(1);
                    String strGroup3 = matcher.group(3);
                    try {
                        if (strGroup3 == null) {
                            return getInstance(strGroup2);
                        }
                        return getInstance(strGroup2, strGroup3);
                    } catch (NoSuchAlgorithmException | NoSuchProviderException e2) {
                        strGroup = matcher.group(5);
                    }
                } else {
                    strGroup = null;
                }
            } else {
                throw new NoSuchAlgorithmException("No strong SecureRandom impls available: " + str);
            }
        }
    }
}
