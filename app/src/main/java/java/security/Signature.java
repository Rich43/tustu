package java.security;

import com.sun.org.apache.xml.internal.security.utils.Constants;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.security.Provider;
import java.security.cert.X509Certificate;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import sun.misc.JavaSecuritySignatureAccess;
import sun.misc.SharedSecrets;
import sun.security.jca.GetInstance;
import sun.security.jca.ServiceId;
import sun.security.util.Debug;
import sun.security.x509.X509CertImpl;

/* loaded from: rt.jar:java/security/Signature.class */
public abstract class Signature extends SignatureSpi {
    private static final Debug debug;
    private static final Debug pdebug;
    private static final boolean skipDebug;
    private String algorithm;
    Provider provider;
    protected static final int UNINITIALIZED = 0;
    protected static final int SIGN = 2;
    protected static final int VERIFY = 3;
    protected int state = 0;
    private static final String RSA_SIGNATURE = "NONEwithRSA";
    private static final String RSA_CIPHER = "RSA/ECB/PKCS1Padding";
    private static final List<ServiceId> rsaIds;
    private static final Map<String, Boolean> signatureInfo;

    static {
        SharedSecrets.setJavaSecuritySignatureAccess(new JavaSecuritySignatureAccess() { // from class: java.security.Signature.1
            @Override // sun.misc.JavaSecuritySignatureAccess
            public void initVerify(Signature signature, PublicKey publicKey, AlgorithmParameterSpec algorithmParameterSpec) throws InvalidKeyException, InvalidAlgorithmParameterException {
                signature.initVerify(publicKey, algorithmParameterSpec);
            }

            @Override // sun.misc.JavaSecuritySignatureAccess
            public void initVerify(Signature signature, java.security.cert.Certificate certificate, AlgorithmParameterSpec algorithmParameterSpec) throws InvalidKeyException, InvalidAlgorithmParameterException {
                signature.initVerify(certificate, algorithmParameterSpec);
            }

            @Override // sun.misc.JavaSecuritySignatureAccess
            public void initSign(Signature signature, PrivateKey privateKey, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
                signature.initSign(privateKey, algorithmParameterSpec, secureRandom);
            }
        });
        debug = Debug.getInstance("jca", Constants._TAG_SIGNATURE);
        pdebug = Debug.getInstance("provider", "Provider");
        skipDebug = Debug.isOn("engine=") && !Debug.isOn(X509CertImpl.SIGNATURE);
        rsaIds = Arrays.asList(new ServiceId(Constants._TAG_SIGNATURE, RSA_SIGNATURE), new ServiceId("Cipher", RSA_CIPHER), new ServiceId("Cipher", "RSA/ECB"), new ServiceId("Cipher", "RSA//PKCS1Padding"), new ServiceId("Cipher", "RSA"));
        signatureInfo = new ConcurrentHashMap();
        Boolean bool = Boolean.TRUE;
        signatureInfo.put("sun.security.provider.DSA$RawDSA", bool);
        signatureInfo.put("sun.security.provider.DSA$SHA1withDSA", bool);
        signatureInfo.put("sun.security.rsa.RSASignature$MD2withRSA", bool);
        signatureInfo.put("sun.security.rsa.RSASignature$MD5withRSA", bool);
        signatureInfo.put("sun.security.rsa.RSASignature$SHA1withRSA", bool);
        signatureInfo.put("sun.security.rsa.RSASignature$SHA256withRSA", bool);
        signatureInfo.put("sun.security.rsa.RSASignature$SHA384withRSA", bool);
        signatureInfo.put("sun.security.rsa.RSASignature$SHA512withRSA", bool);
        signatureInfo.put("sun.security.rsa.RSAPSSSignature", bool);
        signatureInfo.put("com.sun.net.ssl.internal.ssl.RSASignature", bool);
        signatureInfo.put("sun.security.pkcs11.P11Signature", bool);
    }

    protected Signature(String str) {
        this.algorithm = str;
    }

    public static Signature getInstance(String str) throws NoSuchAlgorithmException {
        List<Provider.Service> services;
        if (str.equalsIgnoreCase(RSA_SIGNATURE)) {
            services = GetInstance.getServices(rsaIds);
        } else {
            services = GetInstance.getServices(Constants._TAG_SIGNATURE, str);
        }
        Iterator<Provider.Service> it = services.iterator();
        if (!it.hasNext()) {
            throw new NoSuchAlgorithmException(str + " Signature not available");
        }
        do {
            Provider.Service next = it.next();
            if (isSpi(next)) {
                return new Delegate(next, it, str);
            }
            try {
                return getInstance(GetInstance.getInstance(next, SignatureSpi.class), str);
            } catch (NoSuchAlgorithmException e2) {
            }
        } while (it.hasNext());
        throw e2;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v11, types: [java.security.Signature] */
    private static Signature getInstance(GetInstance.Instance instance, String str) {
        Delegate delegate;
        if (instance.impl instanceof Signature) {
            delegate = (Signature) instance.impl;
            ((Signature) delegate).algorithm = str;
        } else {
            delegate = new Delegate((SignatureSpi) instance.impl, str);
        }
        delegate.provider = instance.provider;
        return delegate;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isSpi(Provider.Service service) {
        if (service.getType().equals("Cipher")) {
            return true;
        }
        String className = service.getClassName();
        Boolean boolValueOf = signatureInfo.get(className);
        if (boolValueOf == null) {
            try {
                Object objNewInstance = service.newInstance(null);
                boolean z2 = (objNewInstance instanceof SignatureSpi) && !(objNewInstance instanceof Signature);
                if (debug != null && !z2) {
                    debug.println("Not a SignatureSpi " + className);
                    debug.println("Delayed provider selection may not be available for algorithm " + service.getAlgorithm());
                }
                boolValueOf = Boolean.valueOf(z2);
                signatureInfo.put(className, boolValueOf);
            } catch (Exception e2) {
                return false;
            }
        }
        return boolValueOf.booleanValue();
    }

    public static Signature getInstance(String str, String str2) throws NoSuchAlgorithmException, NoSuchProviderException {
        if (str.equalsIgnoreCase(RSA_SIGNATURE)) {
            if (str2 == null || str2.length() == 0) {
                throw new IllegalArgumentException("missing provider");
            }
            Provider provider = Security.getProvider(str2);
            if (provider == null) {
                throw new NoSuchProviderException("no such provider: " + str2);
            }
            return getInstanceRSA(provider);
        }
        return getInstance(GetInstance.getInstance(Constants._TAG_SIGNATURE, (Class<?>) SignatureSpi.class, str, str2), str);
    }

    public static Signature getInstance(String str, Provider provider) throws NoSuchAlgorithmException {
        if (str.equalsIgnoreCase(RSA_SIGNATURE)) {
            if (provider == null) {
                throw new IllegalArgumentException("missing provider");
            }
            return getInstanceRSA(provider);
        }
        return getInstance(GetInstance.getInstance(Constants._TAG_SIGNATURE, (Class<?>) SignatureSpi.class, str, provider), str);
    }

    private static Signature getInstanceRSA(Provider provider) throws NoSuchAlgorithmException {
        Provider.Service service = provider.getService(Constants._TAG_SIGNATURE, RSA_SIGNATURE);
        if (service != null) {
            return getInstance(GetInstance.getInstance(service, SignatureSpi.class), RSA_SIGNATURE);
        }
        try {
            return new Delegate(new CipherAdapter(Cipher.getInstance(RSA_CIPHER, provider)), RSA_SIGNATURE);
        } catch (GeneralSecurityException e2) {
            throw new NoSuchAlgorithmException("no such algorithm: NONEwithRSA for provider " + provider.getName(), e2);
        }
    }

    public final Provider getProvider() {
        chooseFirstProvider();
        return this.provider;
    }

    private String getProviderName() {
        return this.provider == null ? "(no provider)" : this.provider.getName();
    }

    void chooseFirstProvider() {
    }

    public final void initVerify(PublicKey publicKey) throws InvalidKeyException {
        engineInitVerify(publicKey);
        this.state = 3;
        if (!skipDebug && pdebug != null) {
            pdebug.println("Signature." + this.algorithm + " verification algorithm from: " + getProviderName());
        }
    }

    final void initVerify(PublicKey publicKey, AlgorithmParameterSpec algorithmParameterSpec) throws InvalidKeyException, InvalidAlgorithmParameterException {
        engineInitVerify(publicKey, algorithmParameterSpec);
        this.state = 3;
        if (!skipDebug && pdebug != null) {
            pdebug.println("Signature." + this.algorithm + " verification algorithm from: " + getProviderName());
        }
    }

    private static PublicKey getPublicKeyFromCert(java.security.cert.Certificate certificate) throws InvalidKeyException {
        X509Certificate x509Certificate;
        Set<String> criticalExtensionOIDs;
        boolean[] keyUsage;
        if ((certificate instanceof X509Certificate) && (criticalExtensionOIDs = (x509Certificate = (X509Certificate) certificate).getCriticalExtensionOIDs()) != null && !criticalExtensionOIDs.isEmpty() && criticalExtensionOIDs.contains("2.5.29.15") && (keyUsage = x509Certificate.getKeyUsage()) != null && !keyUsage[0]) {
            throw new InvalidKeyException("Wrong key usage");
        }
        return certificate.getPublicKey();
    }

    public final void initVerify(java.security.cert.Certificate certificate) throws InvalidKeyException {
        engineInitVerify(getPublicKeyFromCert(certificate));
        this.state = 3;
        if (!skipDebug && pdebug != null) {
            pdebug.println("Signature." + this.algorithm + " verification algorithm from: " + getProviderName());
        }
    }

    final void initVerify(java.security.cert.Certificate certificate, AlgorithmParameterSpec algorithmParameterSpec) throws InvalidKeyException, InvalidAlgorithmParameterException {
        engineInitVerify(getPublicKeyFromCert(certificate), algorithmParameterSpec);
        this.state = 3;
        if (!skipDebug && pdebug != null) {
            pdebug.println("Signature." + this.algorithm + " verification algorithm from: " + getProviderName());
        }
    }

    public final void initSign(PrivateKey privateKey) throws InvalidKeyException {
        engineInitSign(privateKey);
        this.state = 2;
        if (!skipDebug && pdebug != null) {
            pdebug.println("Signature." + this.algorithm + " signing algorithm from: " + getProviderName());
        }
    }

    public final void initSign(PrivateKey privateKey, SecureRandom secureRandom) throws InvalidKeyException {
        engineInitSign(privateKey, secureRandom);
        this.state = 2;
        if (!skipDebug && pdebug != null) {
            pdebug.println("Signature." + this.algorithm + " signing algorithm from: " + getProviderName());
        }
    }

    final void initSign(PrivateKey privateKey, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        engineInitSign(privateKey, algorithmParameterSpec, secureRandom);
        this.state = 2;
        if (!skipDebug && pdebug != null) {
            pdebug.println("Signature." + this.algorithm + " signing algorithm from: " + getProviderName());
        }
    }

    public final byte[] sign() throws SignatureException {
        if (this.state == 2) {
            return engineSign();
        }
        throw new SignatureException("object not initialized for signing");
    }

    public final int sign(byte[] bArr, int i2, int i3) throws SignatureException {
        if (bArr == null) {
            throw new IllegalArgumentException("No output buffer given");
        }
        if (i2 < 0 || i3 < 0) {
            throw new IllegalArgumentException("offset or len is less than 0");
        }
        if (bArr.length - i2 < i3) {
            throw new IllegalArgumentException("Output buffer too small for specified offset and length");
        }
        if (this.state != 2) {
            throw new SignatureException("object not initialized for signing");
        }
        return engineSign(bArr, i2, i3);
    }

    public final boolean verify(byte[] bArr) throws SignatureException {
        if (this.state == 3) {
            return engineVerify(bArr);
        }
        throw new SignatureException("object not initialized for verification");
    }

    public final boolean verify(byte[] bArr, int i2, int i3) throws SignatureException {
        if (this.state == 3) {
            if (bArr == null) {
                throw new IllegalArgumentException("signature is null");
            }
            if (i2 < 0 || i3 < 0) {
                throw new IllegalArgumentException("offset or length is less than 0");
            }
            if (bArr.length - i2 < i3) {
                throw new IllegalArgumentException("signature too small for specified offset and length");
            }
            return engineVerify(bArr, i2, i3);
        }
        throw new SignatureException("object not initialized for verification");
    }

    public final void update(byte b2) throws SignatureException {
        if (this.state == 3 || this.state == 2) {
            engineUpdate(b2);
            return;
        }
        throw new SignatureException("object not initialized for signature or verification");
    }

    public final void update(byte[] bArr) throws SignatureException {
        update(bArr, 0, bArr.length);
    }

    public final void update(byte[] bArr, int i2, int i3) throws SignatureException {
        if (this.state == 2 || this.state == 3) {
            if (bArr == null) {
                throw new IllegalArgumentException("data is null");
            }
            if (i2 < 0 || i3 < 0) {
                throw new IllegalArgumentException("off or len is less than 0");
            }
            if (bArr.length - i2 < i3) {
                throw new IllegalArgumentException("data too small for specified offset and length");
            }
            engineUpdate(bArr, i2, i3);
            return;
        }
        throw new SignatureException("object not initialized for signature or verification");
    }

    public final void update(ByteBuffer byteBuffer) throws SignatureException {
        if (this.state != 2 && this.state != 3) {
            throw new SignatureException("object not initialized for signature or verification");
        }
        if (byteBuffer == null) {
            throw new NullPointerException();
        }
        engineUpdate(byteBuffer);
    }

    public final String getAlgorithm() {
        return this.algorithm;
    }

    public String toString() {
        String str = "";
        switch (this.state) {
            case 0:
                str = "<not initialized>";
                break;
            case 2:
                str = "<initialized for signing>";
                break;
            case 3:
                str = "<initialized for verifying>";
                break;
        }
        return "Signature object: " + getAlgorithm() + str;
    }

    @Deprecated
    public final void setParameter(String str, Object obj) throws InvalidParameterException {
        engineSetParameter(str, obj);
    }

    public final void setParameter(AlgorithmParameterSpec algorithmParameterSpec) throws InvalidAlgorithmParameterException {
        engineSetParameter(algorithmParameterSpec);
    }

    public final AlgorithmParameters getParameters() {
        return engineGetParameters();
    }

    @Deprecated
    public final Object getParameter(String str) throws InvalidParameterException {
        return engineGetParameter(str);
    }

    @Override // java.security.SignatureSpi
    public Object clone() throws CloneNotSupportedException {
        if (this instanceof Cloneable) {
            return super.clone();
        }
        throw new CloneNotSupportedException();
    }

    /* loaded from: rt.jar:java/security/Signature$Delegate.class */
    private static class Delegate extends Signature {
        private SignatureSpi sigSpi;
        private final Object lock;
        private Provider.Service firstService;
        private Iterator<Provider.Service> serviceIterator;
        private static int warnCount = 10;
        private static final int I_PUB = 1;
        private static final int I_PRIV = 2;
        private static final int I_PRIV_SR = 3;
        private static final int I_PUB_PARAM = 4;
        private static final int I_PRIV_PARAM_SR = 5;
        private static final int S_PARAM = 6;

        Delegate(SignatureSpi signatureSpi, String str) {
            super(str);
            this.sigSpi = signatureSpi;
            this.lock = null;
        }

        Delegate(Provider.Service service, Iterator<Provider.Service> it, String str) {
            super(str);
            this.firstService = service;
            this.serviceIterator = it;
            this.lock = new Object();
        }

        @Override // java.security.Signature, java.security.SignatureSpi
        public Object clone() throws CloneNotSupportedException {
            chooseFirstProvider();
            if (this.sigSpi instanceof Cloneable) {
                Delegate delegate = new Delegate((SignatureSpi) this.sigSpi.clone(), ((Signature) this).algorithm);
                delegate.provider = this.provider;
                return delegate;
            }
            throw new CloneNotSupportedException();
        }

        private static SignatureSpi newInstance(Provider.Service service) throws NoSuchAlgorithmException {
            if (service.getType().equals("Cipher")) {
                try {
                    return new CipherAdapter(Cipher.getInstance(Signature.RSA_CIPHER, service.getProvider()));
                } catch (NoSuchPaddingException e2) {
                    throw new NoSuchAlgorithmException(e2);
                }
            }
            Object objNewInstance = service.newInstance(null);
            if (!(objNewInstance instanceof SignatureSpi)) {
                throw new NoSuchAlgorithmException("Not a SignatureSpi: " + objNewInstance.getClass().getName());
            }
            return (SignatureSpi) objNewInstance;
        }

        @Override // java.security.Signature
        void chooseFirstProvider() {
            Provider.Service next;
            if (this.sigSpi != null) {
                return;
            }
            synchronized (this.lock) {
                if (this.sigSpi != null) {
                    return;
                }
                if (Signature.debug != null) {
                    int i2 = warnCount - 1;
                    warnCount = i2;
                    if (i2 >= 0) {
                        Signature.debug.println("Signature.init() not first method called, disabling delayed provider selection");
                        if (i2 == 0) {
                            Signature.debug.println("Further warnings of this type will be suppressed");
                        }
                        new Exception("Debug call trace").printStackTrace();
                    }
                }
                NoSuchAlgorithmException noSuchAlgorithmException = null;
                while (true) {
                    if (this.firstService != null || this.serviceIterator.hasNext()) {
                        if (this.firstService != null) {
                            next = this.firstService;
                            this.firstService = null;
                        } else {
                            next = this.serviceIterator.next();
                        }
                        if (Signature.isSpi(next)) {
                            try {
                                this.sigSpi = newInstance(next);
                                this.provider = next.getProvider();
                                this.firstService = null;
                                this.serviceIterator = null;
                                return;
                            } catch (NoSuchAlgorithmException e2) {
                                noSuchAlgorithmException = e2;
                            }
                        }
                    } else {
                        ProviderException providerException = new ProviderException("Could not construct SignatureSpi instance");
                        if (noSuchAlgorithmException != null) {
                            providerException.initCause(noSuchAlgorithmException);
                        }
                        throw providerException;
                    }
                }
            }
        }

        private void chooseProvider(int i2, Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
            Provider.Service next;
            synchronized (this.lock) {
                if (this.sigSpi != null) {
                    return;
                }
                Exception exc = null;
                while (true) {
                    if (this.firstService != null || this.serviceIterator.hasNext()) {
                        if (this.firstService != null) {
                            next = this.firstService;
                            this.firstService = null;
                        } else {
                            next = this.serviceIterator.next();
                        }
                        if (key == null || next.supportsParameter(key)) {
                            if (Signature.isSpi(next)) {
                                try {
                                    SignatureSpi signatureSpiNewInstance = newInstance(next);
                                    tryOperation(signatureSpiNewInstance, i2, key, algorithmParameterSpec, secureRandom);
                                    this.provider = next.getProvider();
                                    this.sigSpi = signatureSpiNewInstance;
                                    this.firstService = null;
                                    this.serviceIterator = null;
                                    return;
                                } catch (Exception e2) {
                                    if (exc == null) {
                                        exc = e2;
                                    }
                                }
                            }
                        }
                    } else {
                        if (exc instanceof InvalidKeyException) {
                            throw ((InvalidKeyException) exc);
                        }
                        if (exc instanceof RuntimeException) {
                            throw ((RuntimeException) exc);
                        }
                        if (exc instanceof InvalidAlgorithmParameterException) {
                            throw ((InvalidAlgorithmParameterException) exc);
                        }
                        throw new InvalidKeyException("No installed provider supports this key: " + (key != null ? key.getClass().getName() : "(null)"), exc);
                    }
                }
            }
        }

        private void tryOperation(SignatureSpi signatureSpi, int i2, Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
            switch (i2) {
                case 1:
                    signatureSpi.engineInitVerify((PublicKey) key);
                    return;
                case 2:
                    signatureSpi.engineInitSign((PrivateKey) key);
                    return;
                case 3:
                    signatureSpi.engineInitSign((PrivateKey) key, secureRandom);
                    return;
                case 4:
                    signatureSpi.engineInitVerify((PublicKey) key, algorithmParameterSpec);
                    return;
                case 5:
                    signatureSpi.engineInitSign((PrivateKey) key, algorithmParameterSpec, secureRandom);
                    return;
                case 6:
                    signatureSpi.engineSetParameter(algorithmParameterSpec);
                    return;
                default:
                    throw new AssertionError((Object) ("Internal error: " + i2));
            }
        }

        @Override // java.security.SignatureSpi
        protected void engineInitVerify(PublicKey publicKey) throws InvalidKeyException {
            if (this.sigSpi != null) {
                this.sigSpi.engineInitVerify(publicKey);
                return;
            }
            try {
                chooseProvider(1, publicKey, null, null);
            } catch (InvalidAlgorithmParameterException e2) {
                throw new InvalidKeyException(e2);
            }
        }

        @Override // java.security.SignatureSpi
        void engineInitVerify(PublicKey publicKey, AlgorithmParameterSpec algorithmParameterSpec) throws InvalidKeyException, InvalidAlgorithmParameterException {
            if (this.sigSpi != null) {
                this.sigSpi.engineInitVerify(publicKey, algorithmParameterSpec);
            } else {
                chooseProvider(4, publicKey, algorithmParameterSpec, null);
            }
        }

        @Override // java.security.SignatureSpi
        protected void engineInitSign(PrivateKey privateKey) throws InvalidKeyException {
            if (this.sigSpi != null) {
                this.sigSpi.engineInitSign(privateKey);
                return;
            }
            try {
                chooseProvider(2, privateKey, null, null);
            } catch (InvalidAlgorithmParameterException e2) {
                throw new InvalidKeyException(e2);
            }
        }

        @Override // java.security.SignatureSpi
        protected void engineInitSign(PrivateKey privateKey, SecureRandom secureRandom) throws InvalidKeyException {
            if (this.sigSpi != null) {
                this.sigSpi.engineInitSign(privateKey, secureRandom);
                return;
            }
            try {
                chooseProvider(3, privateKey, null, secureRandom);
            } catch (InvalidAlgorithmParameterException e2) {
                throw new InvalidKeyException(e2);
            }
        }

        @Override // java.security.SignatureSpi
        void engineInitSign(PrivateKey privateKey, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
            if (this.sigSpi != null) {
                this.sigSpi.engineInitSign(privateKey, algorithmParameterSpec, secureRandom);
            } else {
                chooseProvider(5, privateKey, algorithmParameterSpec, secureRandom);
            }
        }

        @Override // java.security.SignatureSpi
        protected void engineUpdate(byte b2) throws SignatureException {
            chooseFirstProvider();
            this.sigSpi.engineUpdate(b2);
        }

        @Override // java.security.SignatureSpi
        protected void engineUpdate(byte[] bArr, int i2, int i3) throws SignatureException {
            chooseFirstProvider();
            this.sigSpi.engineUpdate(bArr, i2, i3);
        }

        @Override // java.security.SignatureSpi
        protected void engineUpdate(ByteBuffer byteBuffer) {
            chooseFirstProvider();
            this.sigSpi.engineUpdate(byteBuffer);
        }

        @Override // java.security.SignatureSpi
        protected byte[] engineSign() throws SignatureException {
            chooseFirstProvider();
            return this.sigSpi.engineSign();
        }

        @Override // java.security.SignatureSpi
        protected int engineSign(byte[] bArr, int i2, int i3) throws SignatureException {
            chooseFirstProvider();
            return this.sigSpi.engineSign(bArr, i2, i3);
        }

        @Override // java.security.SignatureSpi
        protected boolean engineVerify(byte[] bArr) throws SignatureException {
            chooseFirstProvider();
            return this.sigSpi.engineVerify(bArr);
        }

        @Override // java.security.SignatureSpi
        protected boolean engineVerify(byte[] bArr, int i2, int i3) throws SignatureException {
            chooseFirstProvider();
            return this.sigSpi.engineVerify(bArr, i2, i3);
        }

        @Override // java.security.SignatureSpi
        protected void engineSetParameter(String str, Object obj) throws InvalidParameterException {
            chooseFirstProvider();
            this.sigSpi.engineSetParameter(str, obj);
        }

        @Override // java.security.SignatureSpi
        protected void engineSetParameter(AlgorithmParameterSpec algorithmParameterSpec) throws InvalidAlgorithmParameterException {
            if (this.sigSpi != null) {
                this.sigSpi.engineSetParameter(algorithmParameterSpec);
                return;
            }
            try {
                chooseProvider(6, null, algorithmParameterSpec, null);
            } catch (InvalidKeyException e2) {
                throw new InvalidAlgorithmParameterException(e2);
            }
        }

        @Override // java.security.SignatureSpi
        protected Object engineGetParameter(String str) throws InvalidParameterException {
            chooseFirstProvider();
            return this.sigSpi.engineGetParameter(str);
        }

        @Override // java.security.SignatureSpi
        protected AlgorithmParameters engineGetParameters() {
            chooseFirstProvider();
            return this.sigSpi.engineGetParameters();
        }
    }

    /* loaded from: rt.jar:java/security/Signature$CipherAdapter.class */
    private static class CipherAdapter extends SignatureSpi {
        private final Cipher cipher;
        private ByteArrayOutputStream data;

        CipherAdapter(Cipher cipher) {
            this.cipher = cipher;
        }

        @Override // java.security.SignatureSpi
        protected void engineInitVerify(PublicKey publicKey) throws InvalidKeyException {
            this.cipher.init(2, publicKey);
            if (this.data == null) {
                this.data = new ByteArrayOutputStream(128);
            } else {
                this.data.reset();
            }
        }

        @Override // java.security.SignatureSpi
        protected void engineInitSign(PrivateKey privateKey) throws InvalidKeyException {
            this.cipher.init(1, privateKey);
            this.data = null;
        }

        @Override // java.security.SignatureSpi
        protected void engineInitSign(PrivateKey privateKey, SecureRandom secureRandom) throws InvalidKeyException {
            this.cipher.init(1, privateKey, secureRandom);
            this.data = null;
        }

        @Override // java.security.SignatureSpi
        protected void engineUpdate(byte b2) throws SignatureException {
            engineUpdate(new byte[]{b2}, 0, 1);
        }

        @Override // java.security.SignatureSpi
        protected void engineUpdate(byte[] bArr, int i2, int i3) throws SignatureException {
            if (this.data != null) {
                this.data.write(bArr, i2, i3);
                return;
            }
            byte[] bArrUpdate = this.cipher.update(bArr, i2, i3);
            if (bArrUpdate != null && bArrUpdate.length != 0) {
                throw new SignatureException("Cipher unexpectedly returned data");
            }
        }

        @Override // java.security.SignatureSpi
        protected byte[] engineSign() throws SignatureException {
            try {
                return this.cipher.doFinal();
            } catch (BadPaddingException e2) {
                throw new SignatureException("doFinal() failed", e2);
            } catch (IllegalBlockSizeException e3) {
                throw new SignatureException("doFinal() failed", e3);
            }
        }

        @Override // java.security.SignatureSpi
        protected boolean engineVerify(byte[] bArr) throws SignatureException {
            try {
                byte[] bArrDoFinal = this.cipher.doFinal(bArr);
                byte[] byteArray = this.data.toByteArray();
                this.data.reset();
                return MessageDigest.isEqual(bArrDoFinal, byteArray);
            } catch (BadPaddingException e2) {
                return false;
            } catch (IllegalBlockSizeException e3) {
                throw new SignatureException("doFinal() failed", e3);
            }
        }

        @Override // java.security.SignatureSpi
        protected void engineSetParameter(String str, Object obj) throws InvalidParameterException {
            throw new InvalidParameterException("Parameters not supported");
        }

        @Override // java.security.SignatureSpi
        protected Object engineGetParameter(String str) throws InvalidParameterException {
            throw new InvalidParameterException("Parameters not supported");
        }
    }
}
