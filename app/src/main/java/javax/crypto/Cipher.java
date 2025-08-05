package javax.crypto;

import java.nio.ByteBuffer;
import java.nio.ReadOnlyBufferException;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.ProviderException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidParameterSpecException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Pattern;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.RC2ParameterSpec;
import javax.crypto.spec.RC5ParameterSpec;
import sun.security.jca.GetInstance;
import sun.security.jca.ServiceId;
import sun.security.util.Debug;

/* loaded from: jce.jar:javax/crypto/Cipher.class */
public class Cipher {
    private static final Debug debug = Debug.getInstance("jca", "Cipher");
    private static final Debug pdebug = Debug.getInstance("provider", "Provider");
    private static final boolean skipDebug;
    public static final int ENCRYPT_MODE = 1;
    public static final int DECRYPT_MODE = 2;
    public static final int WRAP_MODE = 3;
    public static final int UNWRAP_MODE = 4;
    public static final int PUBLIC_KEY = 1;
    public static final int PRIVATE_KEY = 2;
    public static final int SECRET_KEY = 3;
    private Provider provider;
    private CipherSpi spi;
    private String transformation;
    private CryptoPermission cryptoPerm;
    private ExemptionMechanism exmech;
    private boolean initialized;
    private int opmode;
    private static final String KEY_USAGE_EXTENSION_OID = "2.5.29.15";
    private CipherSpi firstSpi;
    private Provider.Service firstService;
    private Iterator<Provider.Service> serviceIterator;
    private List<Transform> transforms;
    private final Object lock;
    private static final String ATTR_MODE = "SupportedModes";
    private static final String ATTR_PAD = "SupportedPaddings";
    private static final int S_NO = 0;
    private static final int S_MAYBE = 1;
    private static final int S_YES = 2;
    private static int warnCount;
    private static final int I_KEY = 1;
    private static final int I_PARAMSPEC = 2;
    private static final int I_PARAMS = 3;
    private static final int I_CERT = 4;

    static {
        skipDebug = Debug.isOn("engine=") && !Debug.isOn("cipher");
        warnCount = 10;
    }

    protected Cipher(CipherSpi cipherSpi, Provider provider, String str) {
        this.initialized = false;
        this.opmode = 0;
        if (!JceSecurityManager.INSTANCE.isCallerTrusted()) {
            throw new NullPointerException();
        }
        this.spi = cipherSpi;
        this.provider = provider;
        this.transformation = str;
        this.cryptoPerm = CryptoAllPermission.INSTANCE;
        this.lock = null;
    }

    Cipher(CipherSpi cipherSpi, String str) {
        this.initialized = false;
        this.opmode = 0;
        this.spi = cipherSpi;
        this.transformation = str;
        this.cryptoPerm = CryptoAllPermission.INSTANCE;
        this.lock = null;
    }

    private Cipher(CipherSpi cipherSpi, Provider.Service service, Iterator<Provider.Service> it, String str, List<Transform> list) {
        this.initialized = false;
        this.opmode = 0;
        this.firstSpi = cipherSpi;
        this.firstService = service;
        this.serviceIterator = it;
        this.transforms = list;
        this.transformation = str;
        this.lock = new Object();
    }

    private static String[] tokenizeTransformation(String str) throws NoSuchAlgorithmException {
        if (str == null) {
            throw new NoSuchAlgorithmException("No transformation given");
        }
        String[] strArr = new String[3];
        int i2 = 0;
        StringTokenizer stringTokenizer = new StringTokenizer(str, "/");
        while (stringTokenizer.hasMoreTokens() && i2 < 3) {
            try {
                int i3 = i2;
                i2++;
                strArr[i3] = stringTokenizer.nextToken().trim();
            } catch (NoSuchElementException e2) {
                throw new NoSuchAlgorithmException("Invalid transformation format:" + str);
            }
        }
        if (i2 == 0 || i2 == 2) {
            throw new NoSuchAlgorithmException("Invalid transformation format:" + str);
        }
        if (i2 == 3 && stringTokenizer.hasMoreTokens()) {
            strArr[2] = strArr[2] + stringTokenizer.nextToken("\r\n");
        }
        if (strArr[0] == null || strArr[0].length() == 0) {
            throw new NoSuchAlgorithmException("Invalid transformation:algorithm not specified-" + str);
        }
        return strArr;
    }

    /* loaded from: jce.jar:javax/crypto/Cipher$Transform.class */
    private static class Transform {
        final String transform;
        final String suffix;
        final String mode;
        final String pad;
        private static final ConcurrentMap<String, Pattern> patternCache = new ConcurrentHashMap();

        Transform(String str, String str2, String str3, String str4) {
            this.transform = str + str2;
            this.suffix = str2.toUpperCase(Locale.ENGLISH);
            this.mode = str3;
            this.pad = str4;
        }

        void setModePadding(CipherSpi cipherSpi) throws NoSuchPaddingException, NoSuchAlgorithmException {
            if (this.mode != null) {
                cipherSpi.engineSetMode(this.mode);
            }
            if (this.pad != null) {
                cipherSpi.engineSetPadding(this.pad);
            }
        }

        int supportsModePadding(Provider.Service service) {
            int iSupportsMode = supportsMode(service);
            if (iSupportsMode == 0) {
                return iSupportsMode;
            }
            return Math.min(iSupportsMode, supportsPadding(service));
        }

        int supportsMode(Provider.Service service) {
            return supports(service, Cipher.ATTR_MODE, this.mode);
        }

        int supportsPadding(Provider.Service service) {
            return supports(service, Cipher.ATTR_PAD, this.pad);
        }

        private static int supports(Provider.Service service, String str, String str2) {
            if (str2 == null) {
                return 2;
            }
            String attribute = service.getAttribute(str);
            if (attribute == null) {
                return 1;
            }
            return matches(attribute, str2) ? 2 : 0;
        }

        private static boolean matches(String str, String str2) {
            Pattern patternCompile = patternCache.get(str);
            if (patternCompile == null) {
                patternCompile = Pattern.compile(str);
                patternCache.putIfAbsent(str, patternCompile);
            }
            return patternCompile.matcher(str2.toUpperCase(Locale.ENGLISH)).matches();
        }
    }

    private static List<Transform> getTransforms(String str) throws NoSuchAlgorithmException {
        String[] strArr = tokenizeTransformation(str);
        String str2 = strArr[0];
        String str3 = strArr[1];
        String str4 = strArr[2];
        if (str3 != null && str3.length() == 0) {
            str3 = null;
        }
        if (str4 != null && str4.length() == 0) {
            str4 = null;
        }
        if (str3 == null && str4 == null) {
            return Collections.singletonList(new Transform(str2, "", null, null));
        }
        ArrayList arrayList = new ArrayList(4);
        arrayList.add(new Transform(str2, "/" + str3 + "/" + str4, null, null));
        arrayList.add(new Transform(str2, "/" + str3, null, str4));
        arrayList.add(new Transform(str2, "//" + str4, str3, null));
        arrayList.add(new Transform(str2, "", str3, str4));
        return arrayList;
    }

    private static Transform getTransform(Provider.Service service, List<Transform> list) {
        String upperCase = service.getAlgorithm().toUpperCase(Locale.ENGLISH);
        for (Transform transform : list) {
            if (upperCase.endsWith(transform.suffix)) {
                return transform;
            }
        }
        return null;
    }

    public static final Cipher getInstance(String str) throws NoSuchPaddingException, NoSuchAlgorithmException {
        Transform transform;
        int iSupportsModePadding;
        List<Transform> transforms = getTransforms(str);
        ArrayList arrayList = new ArrayList(transforms.size());
        Iterator<Transform> it = transforms.iterator();
        while (it.hasNext()) {
            arrayList.add(new ServiceId("Cipher", it.next().transform));
        }
        Iterator<Provider.Service> it2 = GetInstance.getServices(arrayList).iterator();
        Exception exc = null;
        while (it2.hasNext()) {
            Provider.Service next = it2.next();
            if (JceSecurity.canUseProvider(next.getProvider()) && (transform = getTransform(next, transforms)) != null && (iSupportsModePadding = transform.supportsModePadding(next)) != 0) {
                if (iSupportsModePadding == 2) {
                    return new Cipher(null, next, it2, str, transforms);
                }
                try {
                    CipherSpi cipherSpi = (CipherSpi) next.newInstance(null);
                    transform.setModePadding(cipherSpi);
                    return new Cipher(cipherSpi, next, it2, str, transforms);
                } catch (Exception e2) {
                    exc = e2;
                }
            }
        }
        throw new NoSuchAlgorithmException("Cannot find any provider supporting " + str, exc);
    }

    public static final Cipher getInstance(String str, String str2) throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException {
        if (str2 == null || str2.length() == 0) {
            throw new IllegalArgumentException("Missing provider");
        }
        Provider provider = Security.getProvider(str2);
        if (provider == null) {
            throw new NoSuchProviderException("No such provider: " + str2);
        }
        return getInstance(str, provider);
    }

    public static final Cipher getInstance(String str, Provider provider) throws NoSuchPaddingException, NoSuchAlgorithmException {
        if (provider == null) {
            throw new IllegalArgumentException("Missing provider");
        }
        Exception exc = null;
        boolean z2 = false;
        String str2 = null;
        for (Transform transform : getTransforms(str)) {
            Provider.Service service = provider.getService("Cipher", transform.transform);
            if (service != null) {
                if (!z2) {
                    Exception verificationResult = JceSecurity.getVerificationResult(provider);
                    if (verificationResult != null) {
                        throw new SecurityException("JCE cannot authenticate the provider " + provider.getName(), verificationResult);
                    }
                    z2 = true;
                }
                if (transform.supportsMode(service) == 0) {
                    continue;
                } else if (transform.supportsPadding(service) == 0) {
                    str2 = transform.pad;
                } else {
                    try {
                        CipherSpi cipherSpi = (CipherSpi) service.newInstance(null);
                        transform.setModePadding(cipherSpi);
                        Cipher cipher = new Cipher(cipherSpi, str);
                        cipher.provider = service.getProvider();
                        cipher.initCryptoPermission();
                        return cipher;
                    } catch (Exception e2) {
                        exc = e2;
                    }
                }
            }
        }
        if (exc instanceof NoSuchPaddingException) {
            throw ((NoSuchPaddingException) exc);
        }
        if (str2 != null) {
            throw new NoSuchPaddingException("Padding not supported: " + str2);
        }
        throw new NoSuchAlgorithmException("No such algorithm: " + str, exc);
    }

    private void initCryptoPermission() throws NoSuchAlgorithmException {
        if (!JceSecurity.isRestricted()) {
            this.cryptoPerm = CryptoAllPermission.INSTANCE;
            this.exmech = null;
            return;
        }
        this.cryptoPerm = getConfiguredPermission(this.transformation);
        String exemptionMechanism = this.cryptoPerm.getExemptionMechanism();
        if (exemptionMechanism != null) {
            this.exmech = ExemptionMechanism.getInstance(exemptionMechanism);
        }
    }

    void chooseFirstProvider() {
        Provider.Service next;
        CipherSpi cipherSpi;
        Transform transform;
        if (this.spi != null) {
            return;
        }
        synchronized (this.lock) {
            if (this.spi != null) {
                return;
            }
            if (debug != null) {
                int i2 = warnCount - 1;
                warnCount = i2;
                if (i2 >= 0) {
                    debug.println("Cipher.init() not first method called, disabling delayed provider selection");
                    if (i2 == 0) {
                        debug.println("Further warnings of this type will be suppressed");
                    }
                    new Exception("Call trace").printStackTrace();
                }
            }
            Exception exc = null;
            while (true) {
                if (this.firstService != null || this.serviceIterator.hasNext()) {
                    if (this.firstService != null) {
                        next = this.firstService;
                        cipherSpi = this.firstSpi;
                        this.firstService = null;
                        this.firstSpi = null;
                    } else {
                        next = this.serviceIterator.next();
                        cipherSpi = null;
                    }
                    if (JceSecurity.canUseProvider(next.getProvider()) && (transform = getTransform(next, this.transforms)) != null && transform.supportsModePadding(next) != 0) {
                        if (cipherSpi == null) {
                            try {
                                Object objNewInstance = next.newInstance(null);
                                if (objNewInstance instanceof CipherSpi) {
                                    cipherSpi = (CipherSpi) objNewInstance;
                                }
                            } catch (Exception e2) {
                                exc = e2;
                            }
                        }
                        transform.setModePadding(cipherSpi);
                        initCryptoPermission();
                        this.spi = cipherSpi;
                        this.provider = next.getProvider();
                        this.firstService = null;
                        this.serviceIterator = null;
                        this.transforms = null;
                        return;
                    }
                } else {
                    ProviderException providerException = new ProviderException("Could not construct CipherSpi instance");
                    if (exc != null) {
                        providerException.initCause(exc);
                    }
                    throw providerException;
                }
            }
        }
    }

    private void implInit(CipherSpi cipherSpi, int i2, int i3, Key key, AlgorithmParameterSpec algorithmParameterSpec, AlgorithmParameters algorithmParameters, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        switch (i2) {
            case 1:
                checkCryptoPerm(cipherSpi, key);
                cipherSpi.engineInit(i3, key, secureRandom);
                return;
            case 2:
                checkCryptoPerm(cipherSpi, key, algorithmParameterSpec);
                cipherSpi.engineInit(i3, key, algorithmParameterSpec, secureRandom);
                return;
            case 3:
                checkCryptoPerm(cipherSpi, key, algorithmParameters);
                cipherSpi.engineInit(i3, key, algorithmParameters, secureRandom);
                return;
            case 4:
                checkCryptoPerm(cipherSpi, key);
                cipherSpi.engineInit(i3, key, secureRandom);
                return;
            default:
                throw new AssertionError((Object) ("Internal Cipher error: " + i2));
        }
    }

    private void chooseProvider(int i2, int i3, Key key, AlgorithmParameterSpec algorithmParameterSpec, AlgorithmParameters algorithmParameters, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        Provider.Service next;
        CipherSpi cipherSpi;
        Transform transform;
        synchronized (this.lock) {
            if (this.spi != null) {
                implInit(this.spi, i2, i3, key, algorithmParameterSpec, algorithmParameters, secureRandom);
                return;
            }
            Exception exc = null;
            while (true) {
                if (this.firstService != null || this.serviceIterator.hasNext()) {
                    if (this.firstService != null) {
                        next = this.firstService;
                        cipherSpi = this.firstSpi;
                        this.firstService = null;
                        this.firstSpi = null;
                    } else {
                        next = this.serviceIterator.next();
                        cipherSpi = null;
                    }
                    if (next.supportsParameter(key) && JceSecurity.canUseProvider(next.getProvider()) && (transform = getTransform(next, this.transforms)) != null && transform.supportsModePadding(next) != 0) {
                        if (cipherSpi == null) {
                            try {
                                cipherSpi = (CipherSpi) next.newInstance(null);
                            } catch (Exception e2) {
                                if (exc == null) {
                                    exc = e2;
                                }
                            }
                        }
                        transform.setModePadding(cipherSpi);
                        initCryptoPermission();
                        implInit(cipherSpi, i2, i3, key, algorithmParameterSpec, algorithmParameters, secureRandom);
                        this.provider = next.getProvider();
                        this.spi = cipherSpi;
                        this.firstService = null;
                        this.serviceIterator = null;
                        this.transforms = null;
                        return;
                    }
                } else {
                    if (exc instanceof InvalidKeyException) {
                        throw ((InvalidKeyException) exc);
                    }
                    if (exc instanceof InvalidAlgorithmParameterException) {
                        throw ((InvalidAlgorithmParameterException) exc);
                    }
                    if (exc instanceof RuntimeException) {
                        throw ((RuntimeException) exc);
                    }
                    throw new InvalidKeyException("No installed provider supports this key: " + (key != null ? key.getClass().getName() : "(null)"), exc);
                }
            }
        }
    }

    public final Provider getProvider() {
        chooseFirstProvider();
        return this.provider;
    }

    public final String getAlgorithm() {
        return this.transformation;
    }

    public final int getBlockSize() {
        chooseFirstProvider();
        return this.spi.engineGetBlockSize();
    }

    public final int getOutputSize(int i2) {
        if (!this.initialized && !(this instanceof NullCipher)) {
            throw new IllegalStateException("Cipher not initialized");
        }
        if (i2 < 0) {
            throw new IllegalArgumentException("Input size must be equal to or greater than zero");
        }
        chooseFirstProvider();
        return this.spi.engineGetOutputSize(i2);
    }

    public final byte[] getIV() {
        chooseFirstProvider();
        return this.spi.engineGetIV();
    }

    public final AlgorithmParameters getParameters() {
        chooseFirstProvider();
        return this.spi.engineGetParameters();
    }

    public final ExemptionMechanism getExemptionMechanism() {
        chooseFirstProvider();
        return this.exmech;
    }

    private void checkCryptoPerm(CipherSpi cipherSpi, Key key) throws InvalidKeyException {
        if (this.cryptoPerm == CryptoAllPermission.INSTANCE) {
            return;
        }
        try {
            if (!passCryptoPermCheck(cipherSpi, key, getAlgorithmParameterSpec(cipherSpi.engineGetParameters()))) {
                throw new InvalidKeyException("Illegal key size or default parameters");
            }
        } catch (InvalidParameterSpecException e2) {
            throw new InvalidKeyException("Unsupported default algorithm parameters");
        }
    }

    private void checkCryptoPerm(CipherSpi cipherSpi, Key key, AlgorithmParameterSpec algorithmParameterSpec) throws InvalidKeyException, InvalidAlgorithmParameterException {
        if (this.cryptoPerm == CryptoAllPermission.INSTANCE) {
            return;
        }
        if (!passCryptoPermCheck(cipherSpi, key, null)) {
            throw new InvalidKeyException("Illegal key size");
        }
        if (algorithmParameterSpec != null && !passCryptoPermCheck(cipherSpi, key, algorithmParameterSpec)) {
            throw new InvalidAlgorithmParameterException("Illegal parameters");
        }
    }

    private void checkCryptoPerm(CipherSpi cipherSpi, Key key, AlgorithmParameters algorithmParameters) throws InvalidKeyException, InvalidAlgorithmParameterException {
        if (this.cryptoPerm == CryptoAllPermission.INSTANCE) {
            return;
        }
        try {
            checkCryptoPerm(cipherSpi, key, getAlgorithmParameterSpec(algorithmParameters));
        } catch (InvalidParameterSpecException e2) {
            throw new InvalidAlgorithmParameterException("Failed to retrieve algorithm parameter specification");
        }
    }

    private boolean passCryptoPermCheck(CipherSpi cipherSpi, Key key, AlgorithmParameterSpec algorithmParameterSpec) throws InvalidKeyException {
        String strSubstring;
        String exemptionMechanism = this.cryptoPerm.getExemptionMechanism();
        int iEngineGetKeySize = cipherSpi.engineGetKeySize(key);
        int iIndexOf = this.transformation.indexOf(47);
        if (iIndexOf != -1) {
            strSubstring = this.transformation.substring(0, iIndexOf);
        } else {
            strSubstring = this.transformation;
        }
        CryptoPermission cryptoPermission = new CryptoPermission(strSubstring, iEngineGetKeySize, algorithmParameterSpec, exemptionMechanism);
        if (!this.cryptoPerm.implies(cryptoPermission)) {
            if (debug != null) {
                debug.println("Crypto Permission check failed");
                debug.println("granted: " + ((Object) this.cryptoPerm));
                debug.println("requesting: " + ((Object) cryptoPermission));
                return false;
            }
            return false;
        }
        if (this.exmech == null) {
            return true;
        }
        try {
            if (!this.exmech.isCryptoAllowed(key)) {
                if (debug != null) {
                    debug.println(this.exmech.getName() + " isn't enforced");
                    return false;
                }
                return false;
            }
            return true;
        } catch (ExemptionMechanismException e2) {
            if (debug != null) {
                debug.println("Cannot determine whether " + this.exmech.getName() + " has been enforced");
                e2.printStackTrace();
                return false;
            }
            return false;
        }
    }

    private static void checkOpmode(int i2) {
        if (i2 < 1 || i2 > 4) {
            throw new InvalidParameterException("Invalid operation mode");
        }
    }

    private static String getOpmodeString(int i2) {
        switch (i2) {
            case 1:
                return "encryption";
            case 2:
                return "decryption";
            case 3:
                return "key wrapping";
            case 4:
                return "key unwrapping";
            default:
                return "";
        }
    }

    public final void init(int i2, Key key) throws InvalidKeyException {
        init(i2, key, JceSecurity.RANDOM);
    }

    public final void init(int i2, Key key, SecureRandom secureRandom) throws InvalidKeyException {
        this.initialized = false;
        checkOpmode(i2);
        if (this.spi != null) {
            checkCryptoPerm(this.spi, key);
            this.spi.engineInit(i2, key, secureRandom);
        } else {
            try {
                chooseProvider(1, i2, key, null, null, secureRandom);
            } catch (InvalidAlgorithmParameterException e2) {
                throw new InvalidKeyException(e2);
            }
        }
        this.initialized = true;
        this.opmode = i2;
        if (!skipDebug && pdebug != null) {
            pdebug.println("Cipher." + this.transformation + " " + getOpmodeString(i2) + " algorithm from: " + this.provider.getName());
        }
    }

    public final void init(int i2, Key key, AlgorithmParameterSpec algorithmParameterSpec) throws InvalidKeyException, InvalidAlgorithmParameterException {
        init(i2, key, algorithmParameterSpec, JceSecurity.RANDOM);
    }

    public final void init(int i2, Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        this.initialized = false;
        checkOpmode(i2);
        if (this.spi != null) {
            checkCryptoPerm(this.spi, key, algorithmParameterSpec);
            this.spi.engineInit(i2, key, algorithmParameterSpec, secureRandom);
        } else {
            chooseProvider(2, i2, key, algorithmParameterSpec, null, secureRandom);
        }
        this.initialized = true;
        this.opmode = i2;
        if (!skipDebug && pdebug != null) {
            pdebug.println("Cipher." + this.transformation + " " + getOpmodeString(i2) + " algorithm from: " + this.provider.getName());
        }
    }

    public final void init(int i2, Key key, AlgorithmParameters algorithmParameters) throws InvalidKeyException, InvalidAlgorithmParameterException {
        init(i2, key, algorithmParameters, JceSecurity.RANDOM);
    }

    public final void init(int i2, Key key, AlgorithmParameters algorithmParameters, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        this.initialized = false;
        checkOpmode(i2);
        if (this.spi != null) {
            checkCryptoPerm(this.spi, key, algorithmParameters);
            this.spi.engineInit(i2, key, algorithmParameters, secureRandom);
        } else {
            chooseProvider(3, i2, key, null, algorithmParameters, secureRandom);
        }
        this.initialized = true;
        this.opmode = i2;
        if (!skipDebug && pdebug != null) {
            pdebug.println("Cipher." + this.transformation + " " + getOpmodeString(i2) + " algorithm from: " + this.provider.getName());
        }
    }

    public final void init(int i2, Certificate certificate) throws InvalidKeyException {
        init(i2, certificate, JceSecurity.RANDOM);
    }

    public final void init(int i2, Certificate certificate, SecureRandom secureRandom) throws InvalidKeyException {
        X509Certificate x509Certificate;
        Set<String> criticalExtensionOIDs;
        boolean[] keyUsage;
        this.initialized = false;
        checkOpmode(i2);
        if ((certificate instanceof X509Certificate) && (criticalExtensionOIDs = (x509Certificate = (X509Certificate) certificate).getCriticalExtensionOIDs()) != null && !criticalExtensionOIDs.isEmpty() && criticalExtensionOIDs.contains(KEY_USAGE_EXTENSION_OID) && (keyUsage = x509Certificate.getKeyUsage()) != null && ((i2 == 1 && keyUsage.length > 3 && !keyUsage[3]) || (i2 == 3 && keyUsage.length > 2 && !keyUsage[2]))) {
            throw new InvalidKeyException("Wrong key usage");
        }
        PublicKey publicKey = certificate == null ? null : certificate.getPublicKey();
        if (this.spi != null) {
            checkCryptoPerm(this.spi, publicKey);
            this.spi.engineInit(i2, publicKey, secureRandom);
        } else {
            try {
                chooseProvider(4, i2, publicKey, null, null, secureRandom);
            } catch (InvalidAlgorithmParameterException e2) {
                throw new InvalidKeyException(e2);
            }
        }
        this.initialized = true;
        this.opmode = i2;
        if (!skipDebug && pdebug != null) {
            pdebug.println("Cipher." + this.transformation + " " + getOpmodeString(i2) + " algorithm from: " + this.provider.getName());
        }
    }

    private void checkCipherState() {
        if (!(this instanceof NullCipher)) {
            if (!this.initialized) {
                throw new IllegalStateException("Cipher not initialized");
            }
            if (this.opmode != 1 && this.opmode != 2) {
                throw new IllegalStateException("Cipher not initialized for encryption/decryption");
            }
        }
    }

    public final byte[] update(byte[] bArr) {
        checkCipherState();
        if (bArr == null) {
            throw new IllegalArgumentException("Null input buffer");
        }
        chooseFirstProvider();
        if (bArr.length == 0) {
            return null;
        }
        return this.spi.engineUpdate(bArr, 0, bArr.length);
    }

    public final byte[] update(byte[] bArr, int i2, int i3) {
        checkCipherState();
        if (bArr == null || i2 < 0 || i3 > bArr.length - i2 || i3 < 0) {
            throw new IllegalArgumentException("Bad arguments");
        }
        chooseFirstProvider();
        if (i3 == 0) {
            return null;
        }
        return this.spi.engineUpdate(bArr, i2, i3);
    }

    public final int update(byte[] bArr, int i2, int i3, byte[] bArr2) throws ShortBufferException {
        checkCipherState();
        if (bArr == null || i2 < 0 || i3 > bArr.length - i2 || i3 < 0) {
            throw new IllegalArgumentException("Bad arguments");
        }
        chooseFirstProvider();
        if (i3 == 0) {
            return 0;
        }
        return this.spi.engineUpdate(bArr, i2, i3, bArr2, 0);
    }

    public final int update(byte[] bArr, int i2, int i3, byte[] bArr2, int i4) throws ShortBufferException {
        checkCipherState();
        if (bArr == null || i2 < 0 || i3 > bArr.length - i2 || i3 < 0 || i4 < 0) {
            throw new IllegalArgumentException("Bad arguments");
        }
        chooseFirstProvider();
        if (i3 == 0) {
            return 0;
        }
        return this.spi.engineUpdate(bArr, i2, i3, bArr2, i4);
    }

    public final int update(ByteBuffer byteBuffer, ByteBuffer byteBuffer2) throws ShortBufferException {
        checkCipherState();
        if (byteBuffer == null || byteBuffer2 == null) {
            throw new IllegalArgumentException("Buffers must not be null");
        }
        if (byteBuffer == byteBuffer2) {
            throw new IllegalArgumentException("Input and output buffers must not be the same object, consider using buffer.duplicate()");
        }
        if (byteBuffer2.isReadOnly()) {
            throw new ReadOnlyBufferException();
        }
        chooseFirstProvider();
        return this.spi.engineUpdate(byteBuffer, byteBuffer2);
    }

    public final byte[] doFinal() throws BadPaddingException, IllegalBlockSizeException {
        checkCipherState();
        chooseFirstProvider();
        return this.spi.engineDoFinal(null, 0, 0);
    }

    public final int doFinal(byte[] bArr, int i2) throws BadPaddingException, IllegalBlockSizeException, ShortBufferException {
        checkCipherState();
        if (bArr == null || i2 < 0) {
            throw new IllegalArgumentException("Bad arguments");
        }
        chooseFirstProvider();
        return this.spi.engineDoFinal(null, 0, 0, bArr, i2);
    }

    public final byte[] doFinal(byte[] bArr) throws BadPaddingException, IllegalBlockSizeException {
        checkCipherState();
        if (bArr == null) {
            throw new IllegalArgumentException("Null input buffer");
        }
        chooseFirstProvider();
        return this.spi.engineDoFinal(bArr, 0, bArr.length);
    }

    public final byte[] doFinal(byte[] bArr, int i2, int i3) throws BadPaddingException, IllegalBlockSizeException {
        checkCipherState();
        if (bArr == null || i2 < 0 || i3 > bArr.length - i2 || i3 < 0) {
            throw new IllegalArgumentException("Bad arguments");
        }
        chooseFirstProvider();
        return this.spi.engineDoFinal(bArr, i2, i3);
    }

    public final int doFinal(byte[] bArr, int i2, int i3, byte[] bArr2) throws BadPaddingException, IllegalBlockSizeException, ShortBufferException {
        checkCipherState();
        if (bArr == null || i2 < 0 || i3 > bArr.length - i2 || i3 < 0) {
            throw new IllegalArgumentException("Bad arguments");
        }
        chooseFirstProvider();
        return this.spi.engineDoFinal(bArr, i2, i3, bArr2, 0);
    }

    public final int doFinal(byte[] bArr, int i2, int i3, byte[] bArr2, int i4) throws BadPaddingException, IllegalBlockSizeException, ShortBufferException {
        checkCipherState();
        if (bArr == null || i2 < 0 || i3 > bArr.length - i2 || i3 < 0 || i4 < 0) {
            throw new IllegalArgumentException("Bad arguments");
        }
        chooseFirstProvider();
        return this.spi.engineDoFinal(bArr, i2, i3, bArr2, i4);
    }

    public final int doFinal(ByteBuffer byteBuffer, ByteBuffer byteBuffer2) throws BadPaddingException, IllegalBlockSizeException, ShortBufferException {
        checkCipherState();
        if (byteBuffer == null || byteBuffer2 == null) {
            throw new IllegalArgumentException("Buffers must not be null");
        }
        if (byteBuffer == byteBuffer2) {
            throw new IllegalArgumentException("Input and output buffers must not be the same object, consider using buffer.duplicate()");
        }
        if (byteBuffer2.isReadOnly()) {
            throw new ReadOnlyBufferException();
        }
        chooseFirstProvider();
        return this.spi.engineDoFinal(byteBuffer, byteBuffer2);
    }

    public final byte[] wrap(Key key) throws IllegalBlockSizeException, InvalidKeyException {
        if (!(this instanceof NullCipher)) {
            if (!this.initialized) {
                throw new IllegalStateException("Cipher not initialized");
            }
            if (this.opmode != 3) {
                throw new IllegalStateException("Cipher not initialized for wrapping keys");
            }
        }
        chooseFirstProvider();
        return this.spi.engineWrap(key);
    }

    public final Key unwrap(byte[] bArr, String str, int i2) throws NoSuchAlgorithmException, InvalidKeyException {
        if (!(this instanceof NullCipher)) {
            if (!this.initialized) {
                throw new IllegalStateException("Cipher not initialized");
            }
            if (this.opmode != 4) {
                throw new IllegalStateException("Cipher not initialized for unwrapping keys");
            }
        }
        if (i2 != 3 && i2 != 2 && i2 != 1) {
            throw new InvalidParameterException("Invalid key type");
        }
        chooseFirstProvider();
        return this.spi.engineUnwrap(bArr, str, i2);
    }

    private AlgorithmParameterSpec getAlgorithmParameterSpec(AlgorithmParameters algorithmParameters) throws InvalidParameterSpecException {
        if (algorithmParameters == null) {
            return null;
        }
        String upperCase = algorithmParameters.getAlgorithm().toUpperCase(Locale.ENGLISH);
        if (upperCase.equalsIgnoreCase("RC2")) {
            return algorithmParameters.getParameterSpec(RC2ParameterSpec.class);
        }
        if (upperCase.equalsIgnoreCase("RC5")) {
            return algorithmParameters.getParameterSpec(RC5ParameterSpec.class);
        }
        if (upperCase.startsWith("PBE")) {
            return algorithmParameters.getParameterSpec(PBEParameterSpec.class);
        }
        if (upperCase.startsWith("DES")) {
            return algorithmParameters.getParameterSpec(IvParameterSpec.class);
        }
        return null;
    }

    private static CryptoPermission getConfiguredPermission(String str) throws NoSuchAlgorithmException, NullPointerException {
        if (str == null) {
            throw new NullPointerException();
        }
        return JceSecurityManager.INSTANCE.getCryptoPermission(tokenizeTransformation(str)[0]);
    }

    public static final int getMaxAllowedKeyLength(String str) throws NoSuchAlgorithmException {
        return getConfiguredPermission(str).getMaxKeySize();
    }

    public static final AlgorithmParameterSpec getMaxAllowedParameterSpec(String str) throws NoSuchAlgorithmException {
        return getConfiguredPermission(str).getAlgorithmParameterSpec();
    }

    public final void updateAAD(byte[] bArr) {
        if (bArr == null) {
            throw new IllegalArgumentException("src buffer is null");
        }
        updateAAD(bArr, 0, bArr.length);
    }

    public final void updateAAD(byte[] bArr, int i2, int i3) {
        checkCipherState();
        if (bArr == null || i2 < 0 || i3 < 0 || i3 > bArr.length - i2) {
            throw new IllegalArgumentException("Bad arguments");
        }
        chooseFirstProvider();
        if (i3 == 0) {
            return;
        }
        this.spi.engineUpdateAAD(bArr, i2, i3);
    }

    public final void updateAAD(ByteBuffer byteBuffer) {
        checkCipherState();
        if (byteBuffer == null) {
            throw new IllegalArgumentException("src ByteBuffer is null");
        }
        chooseFirstProvider();
        if (byteBuffer.remaining() == 0) {
            return;
        }
        this.spi.engineUpdateAAD(byteBuffer);
    }
}
