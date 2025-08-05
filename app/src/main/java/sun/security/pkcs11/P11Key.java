package sun.security.pkcs11;

import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import com.sun.org.apache.xml.internal.security.keys.content.x509.XMLX509Certificate;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectStreamException;
import java.math.BigInteger;
import java.security.AccessController;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyRep;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.ProviderException;
import java.security.PublicKey;
import java.security.interfaces.DSAParams;
import java.security.interfaces.DSAPrivateKey;
import java.security.interfaces.DSAPublicKey;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.DSAParameterSpec;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;
import java.security.spec.InvalidKeySpecException;
import java.util.Objects;
import javax.crypto.SecretKey;
import javax.crypto.interfaces.DHPrivateKey;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;
import javax.crypto.spec.DHPrivateKeySpec;
import javax.crypto.spec.DHPublicKeySpec;
import sun.security.internal.interfaces.TlsMasterSecret;
import sun.security.pkcs11.wrapper.CK_ATTRIBUTE;
import sun.security.pkcs11.wrapper.PKCS11Exception;
import sun.security.rsa.RSAPrivateCrtKeyImpl;
import sun.security.rsa.RSAPublicKeyImpl;
import sun.security.rsa.RSAUtil;
import sun.security.util.DerValue;
import sun.security.util.Length;

/* loaded from: sunpkcs11.jar:sun/security/pkcs11/P11Key.class */
abstract class P11Key implements Key, Length {
    private static final long serialVersionUID = -2575874101938349339L;
    private static final String PUBLIC = "public";
    private static final String PRIVATE = "private";
    private static final String SECRET = "secret";
    final String type;
    final Token token;
    final String algorithm;
    final int keyLength;
    final boolean tokenObject;
    final boolean sensitive;
    final boolean extractable;
    private final NativeKeyHolder keyIDHolder;
    private static final boolean DISABLE_NATIVE_KEYS_EXTRACTION = "true".equalsIgnoreCase((String) AccessController.doPrivileged(() -> {
        return System.getProperty("sun.security.pkcs11.disableKeyExtraction", "false");
    }));
    private static final CK_ATTRIBUTE[] A0 = new CK_ATTRIBUTE[0];

    abstract byte[] getEncodedInternal();

    P11Key(String str, Session session, long j2, String str2, int i2, CK_ATTRIBUTE[] ck_attributeArr) {
        this.type = str;
        this.token = session.token;
        this.algorithm = str2;
        this.keyLength = i2;
        boolean z2 = false;
        boolean z3 = false;
        boolean z4 = true;
        int length = ck_attributeArr == null ? 0 : ck_attributeArr.length;
        for (int i3 = 0; i3 < length; i3++) {
            CK_ATTRIBUTE ck_attribute = ck_attributeArr[i3];
            if (ck_attribute.type == 1) {
                z2 = ck_attribute.getBoolean();
            } else if (ck_attribute.type == 259) {
                z3 = ck_attribute.getBoolean();
            } else if (ck_attribute.type == 354) {
                z4 = ck_attribute.getBoolean();
            }
        }
        this.tokenObject = z2;
        this.sensitive = z3;
        this.extractable = z4;
        char[] cArr = this.token.tokenInfo.label;
        this.keyIDHolder = new NativeKeyHolder(this, j2, session, !DISABLE_NATIVE_KEYS_EXTRACTION && (cArr[0] == 'N' && cArr[1] == 'S' && cArr[2] == 'S') && z4 && !z2, z2);
    }

    public long getKeyID() {
        return this.keyIDHolder.getKeyID();
    }

    public void releaseKeyID() {
        this.keyIDHolder.releaseKeyID();
    }

    @Override // java.security.Key
    public final String getAlgorithm() {
        this.token.ensureValid();
        return this.algorithm;
    }

    @Override // java.security.Key
    public final byte[] getEncoded() {
        byte[] encodedInternal = getEncodedInternal();
        if (encodedInternal == null) {
            return null;
        }
        return (byte[]) encodedInternal.clone();
    }

    public boolean equals(Object obj) {
        String format;
        byte[] encoded;
        if (this == obj) {
            return true;
        }
        if (!this.token.isValid() || !(obj instanceof Key) || (format = getFormat()) == null) {
            return false;
        }
        Key key = (Key) obj;
        if (!format.equals(key.getFormat())) {
            return false;
        }
        byte[] encodedInternal = getEncodedInternal();
        if (obj instanceof P11Key) {
            encoded = ((P11Key) key).getEncodedInternal();
        } else {
            encoded = key.getEncoded();
        }
        return MessageDigest.isEqual(encodedInternal, encoded);
    }

    public int hashCode() {
        byte[] encodedInternal;
        if (!this.token.isValid() || (encodedInternal = getEncodedInternal()) == null) {
            return 0;
        }
        int length = encodedInternal.length;
        for (byte b2 : encodedInternal) {
            length += (b2 & 255) * 37;
        }
        return length;
    }

    protected Object writeReplace() throws ObjectStreamException {
        KeyRep.Type type;
        String format = getFormat();
        if (isPrivate() && "PKCS#8".equals(format)) {
            type = KeyRep.Type.PRIVATE;
        } else if (isPublic() && XMLX509Certificate.JCA_CERT_ID.equals(format)) {
            type = KeyRep.Type.PUBLIC;
        } else if (isSecret() && "RAW".equals(format)) {
            type = KeyRep.Type.SECRET;
        } else {
            throw new NotSerializableException("Cannot serialize sensitive and unextractable keys");
        }
        return new KeyRep(type, getAlgorithm(), format, getEncoded());
    }

    public String toString() {
        String str;
        this.token.ensureValid();
        String str2 = (this.token.provider.getName() + " " + this.algorithm + " " + this.type + " key, " + this.keyLength + " bits") + (this.tokenObject ? SchemaSymbols.ATTVAL_TOKEN : "session") + " object";
        if (isPublic()) {
            str = str2 + ")";
        } else {
            str = (str2 + ", " + (this.sensitive ? "" : "not ") + "sensitive") + ", " + (this.extractable ? "" : "un") + "extractable)";
        }
        return str;
    }

    @Override // sun.security.util.Length
    public int length() {
        return this.keyLength;
    }

    boolean isPublic() {
        return this.type == PUBLIC;
    }

    boolean isPrivate() {
        return this.type == "private";
    }

    boolean isSecret() {
        return this.type == SECRET;
    }

    void fetchAttributes(CK_ATTRIBUTE[] ck_attributeArr) {
        Session opSession = null;
        long keyID = getKeyID();
        try {
            try {
                opSession = this.token.getOpSession();
                this.token.p11.C_GetAttributeValue(opSession.id(), keyID, ck_attributeArr);
                releaseKeyID();
                this.token.releaseSession(opSession);
            } catch (PKCS11Exception e2) {
                throw new ProviderException(e2);
            }
        } catch (Throwable th) {
            releaseKeyID();
            this.token.releaseSession(opSession);
            throw th;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:29:0x00a5, code lost:
    
        return r11;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static sun.security.pkcs11.wrapper.CK_ATTRIBUTE[] getAttributes(sun.security.pkcs11.Session r7, long r8, sun.security.pkcs11.wrapper.CK_ATTRIBUTE[] r10, sun.security.pkcs11.wrapper.CK_ATTRIBUTE[] r11) {
        /*
            r0 = r10
            if (r0 != 0) goto L8
            sun.security.pkcs11.wrapper.CK_ATTRIBUTE[] r0 = sun.security.pkcs11.P11Key.A0
            r10 = r0
        L8:
            r0 = 0
            r12 = r0
        Lb:
            r0 = r12
            r1 = r11
            int r1 = r1.length
            if (r0 >= r1) goto La3
            r0 = r11
            r1 = r12
            r0 = r0[r1]
            r13 = r0
            r0 = r10
            r14 = r0
            r0 = r14
            int r0 = r0.length
            r15 = r0
            r0 = 0
            r16 = r0
        L25:
            r0 = r16
            r1 = r15
            if (r0 >= r1) goto L5c
            r0 = r14
            r1 = r16
            r0 = r0[r1]
            r17 = r0
            r0 = r13
            long r0 = r0.type
            r1 = r17
            long r1 = r1.type
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 != 0) goto L56
            r0 = r17
            java.lang.Object r0 = r0.pValue
            if (r0 == 0) goto L56
            r0 = r13
            r1 = r17
            java.lang.Object r1 = r1.pValue
            r0.pValue = r1
            goto L5c
        L56:
            int r16 = r16 + 1
            goto L25
        L5c:
            r0 = r13
            java.lang.Object r0 = r0.pValue
            if (r0 != 0) goto L9d
            r0 = 0
            r14 = r0
        L67:
            r0 = r14
            r1 = r12
            if (r0 >= r1) goto L7d
            r0 = r11
            r1 = r14
            r0 = r0[r1]
            r1 = 0
            r0.pValue = r1
            int r14 = r14 + 1
            goto L67
        L7d:
            r0 = r7
            sun.security.pkcs11.Token r0 = r0.token     // Catch: sun.security.pkcs11.wrapper.PKCS11Exception -> L91
            sun.security.pkcs11.wrapper.PKCS11 r0 = r0.p11     // Catch: sun.security.pkcs11.wrapper.PKCS11Exception -> L91
            r1 = r7
            long r1 = r1.id()     // Catch: sun.security.pkcs11.wrapper.PKCS11Exception -> L91
            r2 = r8
            r3 = r11
            r0.C_GetAttributeValue(r1, r2, r3)     // Catch: sun.security.pkcs11.wrapper.PKCS11Exception -> L91
            goto La3
        L91:
            r14 = move-exception
            java.security.ProviderException r0 = new java.security.ProviderException
            r1 = r0
            r2 = r14
            r1.<init>(r2)
            throw r0
        L9d:
            int r12 = r12 + 1
            goto Lb
        La3:
            r0 = r11
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.security.pkcs11.P11Key.getAttributes(sun.security.pkcs11.Session, long, sun.security.pkcs11.wrapper.CK_ATTRIBUTE[], sun.security.pkcs11.wrapper.CK_ATTRIBUTE[]):sun.security.pkcs11.wrapper.CK_ATTRIBUTE[]");
    }

    static SecretKey secretKey(Session session, long j2, String str, int i2, CK_ATTRIBUTE[] ck_attributeArr) {
        return new P11SecretKey(session, j2, str, i2, getAttributes(session, j2, ck_attributeArr, new CK_ATTRIBUTE[]{new CK_ATTRIBUTE(1L), new CK_ATTRIBUTE(259L), new CK_ATTRIBUTE(354L)}));
    }

    static SecretKey masterSecretKey(Session session, long j2, String str, int i2, CK_ATTRIBUTE[] ck_attributeArr, int i3, int i4) {
        return new P11TlsMasterSecretKey(session, j2, str, i2, getAttributes(session, j2, ck_attributeArr, new CK_ATTRIBUTE[]{new CK_ATTRIBUTE(1L), new CK_ATTRIBUTE(259L), new CK_ATTRIBUTE(354L)}), i3, i4);
    }

    static PublicKey publicKey(Session session, long j2, String str, int i2, CK_ATTRIBUTE[] ck_attributeArr) {
        switch (str) {
            case "RSA":
                return new P11RSAPublicKey(session, j2, str, i2, ck_attributeArr);
            case "DSA":
                return new P11DSAPublicKey(session, j2, str, i2, ck_attributeArr);
            case "DH":
                return new P11DHPublicKey(session, j2, str, i2, ck_attributeArr);
            case "EC":
                return new P11ECPublicKey(session, j2, str, i2, ck_attributeArr);
            default:
                throw new ProviderException("Unknown public key algorithm " + str);
        }
    }

    static PrivateKey privateKey(Session session, long j2, String str, int i2, CK_ATTRIBUTE[] ck_attributeArr) {
        boolean z2;
        CK_ATTRIBUTE[] attributes = getAttributes(session, j2, ck_attributeArr, new CK_ATTRIBUTE[]{new CK_ATTRIBUTE(1L), new CK_ATTRIBUTE(259L), new CK_ATTRIBUTE(354L)});
        if (attributes[1].getBoolean() || !attributes[2].getBoolean()) {
            return new P11PrivateKey(session, j2, str, i2, attributes);
        }
        switch (str) {
            case "RSA":
                CK_ATTRIBUTE[] ck_attributeArr2 = {new CK_ATTRIBUTE(290L)};
                try {
                    session.token.p11.C_GetAttributeValue(session.id(), j2, ck_attributeArr2);
                    z2 = ck_attributeArr2[0].pValue instanceof byte[];
                } catch (PKCS11Exception e2) {
                    z2 = false;
                }
                if (z2) {
                    return new P11RSAPrivateKey(session, j2, str, i2, attributes);
                }
                return new P11RSAPrivateNonCRTKey(session, j2, str, i2, attributes);
            case "DSA":
                return new P11DSAPrivateKey(session, j2, str, i2, attributes);
            case "DH":
                return new P11DHPrivateKey(session, j2, str, i2, attributes);
            case "EC":
                return new P11ECPrivateKey(session, j2, str, i2, attributes);
            default:
                throw new ProviderException("Unknown private key algorithm " + str);
        }
    }

    /* loaded from: sunpkcs11.jar:sun/security/pkcs11/P11Key$P11PrivateKey.class */
    private static final class P11PrivateKey extends P11Key implements PrivateKey {
        private static final long serialVersionUID = -2138581185214187615L;

        P11PrivateKey(Session session, long j2, String str, int i2, CK_ATTRIBUTE[] ck_attributeArr) {
            super("private", session, j2, str, i2, ck_attributeArr);
        }

        @Override // java.security.Key
        public String getFormat() {
            this.token.ensureValid();
            return null;
        }

        @Override // sun.security.pkcs11.P11Key
        byte[] getEncodedInternal() {
            this.token.ensureValid();
            return null;
        }
    }

    /* loaded from: sunpkcs11.jar:sun/security/pkcs11/P11Key$P11SecretKey.class */
    private static class P11SecretKey extends P11Key implements SecretKey {
        private static final long serialVersionUID = -7828241727014329084L;
        private volatile byte[] encoded;

        P11SecretKey(Session session, long j2, String str, int i2, CK_ATTRIBUTE[] ck_attributeArr) {
            super(P11Key.SECRET, session, j2, str, i2, ck_attributeArr);
        }

        @Override // java.security.Key
        public String getFormat() {
            this.token.ensureValid();
            if (this.sensitive || !this.extractable) {
                return null;
            }
            return "RAW";
        }

        @Override // sun.security.pkcs11.P11Key
        byte[] getEncodedInternal() {
            this.token.ensureValid();
            if (getFormat() == null) {
                return null;
            }
            byte[] byteArray = this.encoded;
            if (byteArray == null) {
                synchronized (this) {
                    byteArray = this.encoded;
                    if (byteArray == null) {
                        Session opSession = null;
                        long keyID = getKeyID();
                        try {
                            try {
                                opSession = this.token.getOpSession();
                                CK_ATTRIBUTE[] ck_attributeArr = {new CK_ATTRIBUTE(17L)};
                                this.token.p11.C_GetAttributeValue(opSession.id(), keyID, ck_attributeArr);
                                byteArray = ck_attributeArr[0].getByteArray();
                                releaseKeyID();
                                this.token.releaseSession(opSession);
                                this.encoded = byteArray;
                            } catch (Throwable th) {
                                releaseKeyID();
                                this.token.releaseSession(opSession);
                                throw th;
                            }
                        } catch (PKCS11Exception e2) {
                            throw new ProviderException(e2);
                        }
                    }
                }
            }
            return byteArray;
        }
    }

    /* loaded from: sunpkcs11.jar:sun/security/pkcs11/P11Key$P11TlsMasterSecretKey.class */
    private static class P11TlsMasterSecretKey extends P11SecretKey implements TlsMasterSecret {
        private static final long serialVersionUID = -1318560923770573441L;
        private final int majorVersion;
        private final int minorVersion;

        P11TlsMasterSecretKey(Session session, long j2, String str, int i2, CK_ATTRIBUTE[] ck_attributeArr, int i3, int i4) {
            super(session, j2, str, i2, ck_attributeArr);
            this.majorVersion = i3;
            this.minorVersion = i4;
        }

        @Override // sun.security.internal.interfaces.TlsMasterSecret
        public int getMajorVersion() {
            return this.majorVersion;
        }

        @Override // sun.security.internal.interfaces.TlsMasterSecret
        public int getMinorVersion() {
            return this.minorVersion;
        }
    }

    /* loaded from: sunpkcs11.jar:sun/security/pkcs11/P11Key$P11RSAPrivateKey.class */
    private static final class P11RSAPrivateKey extends P11Key implements RSAPrivateCrtKey {
        private static final long serialVersionUID = 9215872438913515220L;

        /* renamed from: n, reason: collision with root package name */
        private BigInteger f13619n;

        /* renamed from: e, reason: collision with root package name */
        private BigInteger f13620e;

        /* renamed from: d, reason: collision with root package name */
        private BigInteger f13621d;

        /* renamed from: p, reason: collision with root package name */
        private BigInteger f13622p;

        /* renamed from: q, reason: collision with root package name */
        private BigInteger f13623q;
        private BigInteger pe;
        private BigInteger qe;
        private BigInteger coeff;
        private byte[] encoded;

        P11RSAPrivateKey(Session session, long j2, String str, int i2, CK_ATTRIBUTE[] ck_attributeArr) {
            super("private", session, j2, str, i2, ck_attributeArr);
        }

        private synchronized void fetchValues() {
            this.token.ensureValid();
            if (this.f13619n != null) {
                return;
            }
            CK_ATTRIBUTE[] ck_attributeArr = {new CK_ATTRIBUTE(288L), new CK_ATTRIBUTE(290L), new CK_ATTRIBUTE(291L), new CK_ATTRIBUTE(292L), new CK_ATTRIBUTE(293L), new CK_ATTRIBUTE(294L), new CK_ATTRIBUTE(295L), new CK_ATTRIBUTE(296L)};
            fetchAttributes(ck_attributeArr);
            this.f13619n = ck_attributeArr[0].getBigInteger();
            this.f13620e = ck_attributeArr[1].getBigInteger();
            this.f13621d = ck_attributeArr[2].getBigInteger();
            this.f13622p = ck_attributeArr[3].getBigInteger();
            this.f13623q = ck_attributeArr[4].getBigInteger();
            this.pe = ck_attributeArr[5].getBigInteger();
            this.qe = ck_attributeArr[6].getBigInteger();
            this.coeff = ck_attributeArr[7].getBigInteger();
        }

        @Override // java.security.Key
        public String getFormat() {
            this.token.ensureValid();
            return "PKCS#8";
        }

        @Override // sun.security.pkcs11.P11Key
        synchronized byte[] getEncodedInternal() {
            this.token.ensureValid();
            if (this.encoded == null) {
                fetchValues();
                try {
                    this.encoded = RSAPrivateCrtKeyImpl.newKey(RSAUtil.KeyType.RSA, null, this.f13619n, this.f13620e, this.f13621d, this.f13622p, this.f13623q, this.pe, this.qe, this.coeff).getEncoded();
                } catch (GeneralSecurityException e2) {
                    throw new ProviderException(e2);
                }
            }
            return this.encoded;
        }

        @Override // java.security.interfaces.RSAKey
        public BigInteger getModulus() {
            fetchValues();
            return this.f13619n;
        }

        @Override // java.security.interfaces.RSAPrivateCrtKey
        public BigInteger getPublicExponent() {
            fetchValues();
            return this.f13620e;
        }

        @Override // java.security.interfaces.RSAPrivateKey
        public BigInteger getPrivateExponent() {
            fetchValues();
            return this.f13621d;
        }

        @Override // java.security.interfaces.RSAPrivateCrtKey
        public BigInteger getPrimeP() {
            fetchValues();
            return this.f13622p;
        }

        @Override // java.security.interfaces.RSAPrivateCrtKey
        public BigInteger getPrimeQ() {
            fetchValues();
            return this.f13623q;
        }

        @Override // java.security.interfaces.RSAPrivateCrtKey
        public BigInteger getPrimeExponentP() {
            fetchValues();
            return this.pe;
        }

        @Override // java.security.interfaces.RSAPrivateCrtKey
        public BigInteger getPrimeExponentQ() {
            fetchValues();
            return this.qe;
        }

        @Override // java.security.interfaces.RSAPrivateCrtKey
        public BigInteger getCrtCoefficient() {
            fetchValues();
            return this.coeff;
        }
    }

    /* loaded from: sunpkcs11.jar:sun/security/pkcs11/P11Key$P11RSAPrivateNonCRTKey.class */
    private static final class P11RSAPrivateNonCRTKey extends P11Key implements RSAPrivateKey {
        private static final long serialVersionUID = 1137764983777411481L;

        /* renamed from: n, reason: collision with root package name */
        private BigInteger f13624n;

        /* renamed from: d, reason: collision with root package name */
        private BigInteger f13625d;
        private byte[] encoded;

        P11RSAPrivateNonCRTKey(Session session, long j2, String str, int i2, CK_ATTRIBUTE[] ck_attributeArr) {
            super("private", session, j2, str, i2, ck_attributeArr);
        }

        private synchronized void fetchValues() {
            this.token.ensureValid();
            if (this.f13624n != null) {
                return;
            }
            CK_ATTRIBUTE[] ck_attributeArr = {new CK_ATTRIBUTE(288L), new CK_ATTRIBUTE(291L)};
            fetchAttributes(ck_attributeArr);
            this.f13624n = ck_attributeArr[0].getBigInteger();
            this.f13625d = ck_attributeArr[1].getBigInteger();
        }

        @Override // java.security.Key
        public String getFormat() {
            this.token.ensureValid();
            return "PKCS#8";
        }

        @Override // sun.security.pkcs11.P11Key
        synchronized byte[] getEncodedInternal() {
            this.token.ensureValid();
            if (this.encoded == null) {
                fetchValues();
                try {
                    this.encoded = KeyFactory.getInstance("RSA", P11Util.getSunRsaSignProvider()).translateKey(this).getEncoded();
                } catch (GeneralSecurityException e2) {
                    throw new ProviderException(e2);
                }
            }
            return this.encoded;
        }

        @Override // java.security.interfaces.RSAKey
        public BigInteger getModulus() {
            fetchValues();
            return this.f13624n;
        }

        @Override // java.security.interfaces.RSAPrivateKey
        public BigInteger getPrivateExponent() {
            fetchValues();
            return this.f13625d;
        }
    }

    /* loaded from: sunpkcs11.jar:sun/security/pkcs11/P11Key$P11RSAPublicKey.class */
    private static final class P11RSAPublicKey extends P11Key implements RSAPublicKey {
        private static final long serialVersionUID = -826726289023854455L;

        /* renamed from: n, reason: collision with root package name */
        private BigInteger f13626n;

        /* renamed from: e, reason: collision with root package name */
        private BigInteger f13627e;
        private byte[] encoded;

        P11RSAPublicKey(Session session, long j2, String str, int i2, CK_ATTRIBUTE[] ck_attributeArr) {
            super(P11Key.PUBLIC, session, j2, str, i2, ck_attributeArr);
        }

        private synchronized void fetchValues() {
            this.token.ensureValid();
            if (this.f13626n != null) {
                return;
            }
            CK_ATTRIBUTE[] ck_attributeArr = {new CK_ATTRIBUTE(288L), new CK_ATTRIBUTE(290L)};
            fetchAttributes(ck_attributeArr);
            this.f13626n = ck_attributeArr[0].getBigInteger();
            this.f13627e = ck_attributeArr[1].getBigInteger();
        }

        @Override // java.security.Key
        public String getFormat() {
            this.token.ensureValid();
            return XMLX509Certificate.JCA_CERT_ID;
        }

        @Override // sun.security.pkcs11.P11Key
        synchronized byte[] getEncodedInternal() {
            this.token.ensureValid();
            if (this.encoded == null) {
                fetchValues();
                try {
                    this.encoded = RSAPublicKeyImpl.newKey(RSAUtil.KeyType.RSA, null, this.f13626n, this.f13627e).getEncoded();
                } catch (InvalidKeyException e2) {
                    throw new ProviderException(e2);
                }
            }
            return this.encoded;
        }

        @Override // java.security.interfaces.RSAKey
        public BigInteger getModulus() {
            fetchValues();
            return this.f13626n;
        }

        @Override // java.security.interfaces.RSAPublicKey
        public BigInteger getPublicExponent() {
            fetchValues();
            return this.f13627e;
        }

        @Override // sun.security.pkcs11.P11Key
        public String toString() {
            fetchValues();
            return super.toString() + "\n  modulus: " + ((Object) this.f13626n) + "\n  public exponent: " + ((Object) this.f13627e);
        }
    }

    /* loaded from: sunpkcs11.jar:sun/security/pkcs11/P11Key$P11DSAPublicKey.class */
    private static final class P11DSAPublicKey extends P11Key implements DSAPublicKey {
        private static final long serialVersionUID = 5989753793316396637L;

        /* renamed from: y, reason: collision with root package name */
        private BigInteger f13616y;
        private DSAParams params;
        private byte[] encoded;

        P11DSAPublicKey(Session session, long j2, String str, int i2, CK_ATTRIBUTE[] ck_attributeArr) {
            super(P11Key.PUBLIC, session, j2, str, i2, ck_attributeArr);
        }

        private synchronized void fetchValues() {
            this.token.ensureValid();
            if (this.f13616y != null) {
                return;
            }
            CK_ATTRIBUTE[] ck_attributeArr = {new CK_ATTRIBUTE(17L), new CK_ATTRIBUTE(304L), new CK_ATTRIBUTE(305L), new CK_ATTRIBUTE(306L)};
            fetchAttributes(ck_attributeArr);
            this.f13616y = ck_attributeArr[0].getBigInteger();
            this.params = new DSAParameterSpec(ck_attributeArr[1].getBigInteger(), ck_attributeArr[2].getBigInteger(), ck_attributeArr[3].getBigInteger());
        }

        @Override // java.security.Key
        public String getFormat() {
            this.token.ensureValid();
            return XMLX509Certificate.JCA_CERT_ID;
        }

        @Override // sun.security.pkcs11.P11Key
        synchronized byte[] getEncodedInternal() {
            this.token.ensureValid();
            if (this.encoded == null) {
                fetchValues();
                try {
                    this.encoded = new sun.security.provider.DSAPublicKey(this.f13616y, this.params.getP(), this.params.getQ(), this.params.getG()).getEncoded();
                } catch (InvalidKeyException e2) {
                    throw new ProviderException(e2);
                }
            }
            return this.encoded;
        }

        @Override // java.security.interfaces.DSAPublicKey
        public BigInteger getY() {
            fetchValues();
            return this.f13616y;
        }

        @Override // java.security.interfaces.DSAKey
        public DSAParams getParams() {
            fetchValues();
            return this.params;
        }

        @Override // sun.security.pkcs11.P11Key
        public String toString() {
            fetchValues();
            return super.toString() + "\n  y: " + ((Object) this.f13616y) + "\n  p: " + ((Object) this.params.getP()) + "\n  q: " + ((Object) this.params.getQ()) + "\n  g: " + ((Object) this.params.getG());
        }
    }

    /* loaded from: sunpkcs11.jar:sun/security/pkcs11/P11Key$P11DSAPrivateKey.class */
    private static final class P11DSAPrivateKey extends P11Key implements DSAPrivateKey {
        private static final long serialVersionUID = 3119629997181999389L;

        /* renamed from: x, reason: collision with root package name */
        private BigInteger f13615x;
        private DSAParams params;
        private byte[] encoded;

        P11DSAPrivateKey(Session session, long j2, String str, int i2, CK_ATTRIBUTE[] ck_attributeArr) {
            super("private", session, j2, str, i2, ck_attributeArr);
        }

        private synchronized void fetchValues() {
            this.token.ensureValid();
            if (this.f13615x != null) {
                return;
            }
            CK_ATTRIBUTE[] ck_attributeArr = {new CK_ATTRIBUTE(17L), new CK_ATTRIBUTE(304L), new CK_ATTRIBUTE(305L), new CK_ATTRIBUTE(306L)};
            fetchAttributes(ck_attributeArr);
            this.f13615x = ck_attributeArr[0].getBigInteger();
            this.params = new DSAParameterSpec(ck_attributeArr[1].getBigInteger(), ck_attributeArr[2].getBigInteger(), ck_attributeArr[3].getBigInteger());
        }

        @Override // java.security.Key
        public String getFormat() {
            this.token.ensureValid();
            return "PKCS#8";
        }

        @Override // sun.security.pkcs11.P11Key
        synchronized byte[] getEncodedInternal() {
            this.token.ensureValid();
            if (this.encoded == null) {
                fetchValues();
                try {
                    this.encoded = new sun.security.provider.DSAPrivateKey(this.f13615x, this.params.getP(), this.params.getQ(), this.params.getG()).getEncoded();
                } catch (InvalidKeyException e2) {
                    throw new ProviderException(e2);
                }
            }
            return this.encoded;
        }

        @Override // java.security.interfaces.DSAPrivateKey
        public BigInteger getX() {
            fetchValues();
            return this.f13615x;
        }

        @Override // java.security.interfaces.DSAKey
        public DSAParams getParams() {
            fetchValues();
            return this.params;
        }
    }

    /* loaded from: sunpkcs11.jar:sun/security/pkcs11/P11Key$P11DHPrivateKey.class */
    private static final class P11DHPrivateKey extends P11Key implements DHPrivateKey {
        private static final long serialVersionUID = -1698576167364928838L;

        /* renamed from: x, reason: collision with root package name */
        private BigInteger f13613x;
        private DHParameterSpec params;
        private byte[] encoded;

        P11DHPrivateKey(Session session, long j2, String str, int i2, CK_ATTRIBUTE[] ck_attributeArr) {
            super("private", session, j2, str, i2, ck_attributeArr);
        }

        private synchronized void fetchValues() {
            this.token.ensureValid();
            if (this.f13613x != null) {
                return;
            }
            CK_ATTRIBUTE[] ck_attributeArr = {new CK_ATTRIBUTE(17L), new CK_ATTRIBUTE(304L), new CK_ATTRIBUTE(306L)};
            fetchAttributes(ck_attributeArr);
            this.f13613x = ck_attributeArr[0].getBigInteger();
            this.params = new DHParameterSpec(ck_attributeArr[1].getBigInteger(), ck_attributeArr[2].getBigInteger());
        }

        @Override // java.security.Key
        public String getFormat() {
            this.token.ensureValid();
            return "PKCS#8";
        }

        @Override // sun.security.pkcs11.P11Key
        synchronized byte[] getEncodedInternal() {
            this.token.ensureValid();
            if (this.encoded == null) {
                fetchValues();
                try {
                    this.encoded = KeyFactory.getInstance("DH", P11Util.getSunJceProvider()).generatePrivate(new DHPrivateKeySpec(this.f13613x, this.params.getP(), this.params.getG())).getEncoded();
                } catch (GeneralSecurityException e2) {
                    throw new ProviderException(e2);
                }
            }
            return this.encoded;
        }

        @Override // javax.crypto.interfaces.DHPrivateKey
        public BigInteger getX() {
            fetchValues();
            return this.f13613x;
        }

        @Override // javax.crypto.interfaces.DHKey
        public DHParameterSpec getParams() {
            fetchValues();
            return this.params;
        }

        @Override // sun.security.pkcs11.P11Key
        public int hashCode() {
            if (!this.token.isValid()) {
                return 0;
            }
            fetchValues();
            return Objects.hash(this.f13613x, this.params.getP(), this.params.getG());
        }

        @Override // sun.security.pkcs11.P11Key
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!this.token.isValid() || !(obj instanceof DHPrivateKey)) {
                return false;
            }
            fetchValues();
            DHPrivateKey dHPrivateKey = (DHPrivateKey) obj;
            DHParameterSpec params = dHPrivateKey.getParams();
            return this.f13613x.compareTo(dHPrivateKey.getX()) == 0 && this.params.getP().compareTo(params.getP()) == 0 && this.params.getG().compareTo(params.getG()) == 0;
        }
    }

    /* loaded from: sunpkcs11.jar:sun/security/pkcs11/P11Key$P11DHPublicKey.class */
    private static final class P11DHPublicKey extends P11Key implements DHPublicKey {
        static final long serialVersionUID = -598383872153843657L;

        /* renamed from: y, reason: collision with root package name */
        private BigInteger f13614y;
        private DHParameterSpec params;
        private byte[] encoded;

        P11DHPublicKey(Session session, long j2, String str, int i2, CK_ATTRIBUTE[] ck_attributeArr) {
            super(P11Key.PUBLIC, session, j2, str, i2, ck_attributeArr);
        }

        private synchronized void fetchValues() {
            this.token.ensureValid();
            if (this.f13614y != null) {
                return;
            }
            CK_ATTRIBUTE[] ck_attributeArr = {new CK_ATTRIBUTE(17L), new CK_ATTRIBUTE(304L), new CK_ATTRIBUTE(306L)};
            fetchAttributes(ck_attributeArr);
            this.f13614y = ck_attributeArr[0].getBigInteger();
            this.params = new DHParameterSpec(ck_attributeArr[1].getBigInteger(), ck_attributeArr[2].getBigInteger());
        }

        @Override // java.security.Key
        public String getFormat() {
            this.token.ensureValid();
            return XMLX509Certificate.JCA_CERT_ID;
        }

        @Override // sun.security.pkcs11.P11Key
        synchronized byte[] getEncodedInternal() {
            this.token.ensureValid();
            if (this.encoded == null) {
                fetchValues();
                try {
                    this.encoded = KeyFactory.getInstance("DH", P11Util.getSunJceProvider()).generatePublic(new DHPublicKeySpec(this.f13614y, this.params.getP(), this.params.getG())).getEncoded();
                } catch (GeneralSecurityException e2) {
                    throw new ProviderException(e2);
                }
            }
            return this.encoded;
        }

        @Override // javax.crypto.interfaces.DHPublicKey
        public BigInteger getY() {
            fetchValues();
            return this.f13614y;
        }

        @Override // javax.crypto.interfaces.DHKey
        public DHParameterSpec getParams() {
            fetchValues();
            return this.params;
        }

        @Override // sun.security.pkcs11.P11Key
        public String toString() {
            fetchValues();
            return super.toString() + "\n  y: " + ((Object) this.f13614y) + "\n  p: " + ((Object) this.params.getP()) + "\n  g: " + ((Object) this.params.getG());
        }

        @Override // sun.security.pkcs11.P11Key
        public int hashCode() {
            if (!this.token.isValid()) {
                return 0;
            }
            fetchValues();
            return Objects.hash(this.f13614y, this.params.getP(), this.params.getG());
        }

        @Override // sun.security.pkcs11.P11Key
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!this.token.isValid() || !(obj instanceof DHPublicKey)) {
                return false;
            }
            fetchValues();
            DHPublicKey dHPublicKey = (DHPublicKey) obj;
            DHParameterSpec params = dHPublicKey.getParams();
            return this.f13614y.compareTo(dHPublicKey.getY()) == 0 && this.params.getP().compareTo(params.getP()) == 0 && this.params.getG().compareTo(params.getG()) == 0;
        }
    }

    /* loaded from: sunpkcs11.jar:sun/security/pkcs11/P11Key$P11ECPrivateKey.class */
    private static final class P11ECPrivateKey extends P11Key implements ECPrivateKey {
        private static final long serialVersionUID = -7786054399510515515L;

        /* renamed from: s, reason: collision with root package name */
        private BigInteger f13617s;
        private ECParameterSpec params;
        private byte[] encoded;

        P11ECPrivateKey(Session session, long j2, String str, int i2, CK_ATTRIBUTE[] ck_attributeArr) {
            super("private", session, j2, str, i2, ck_attributeArr);
        }

        private synchronized void fetchValues() {
            this.token.ensureValid();
            if (this.f13617s != null) {
                return;
            }
            CK_ATTRIBUTE[] ck_attributeArr = {new CK_ATTRIBUTE(17L), new CK_ATTRIBUTE(384L, this.params)};
            fetchAttributes(ck_attributeArr);
            this.f13617s = ck_attributeArr[0].getBigInteger();
            try {
                this.params = P11ECKeyFactory.decodeParameters(ck_attributeArr[1].getByteArray());
            } catch (Exception e2) {
                throw new RuntimeException("Could not parse key values", e2);
            }
        }

        @Override // java.security.Key
        public String getFormat() {
            this.token.ensureValid();
            return "PKCS#8";
        }

        @Override // sun.security.pkcs11.P11Key
        synchronized byte[] getEncodedInternal() {
            this.token.ensureValid();
            if (this.encoded == null) {
                fetchValues();
                try {
                    this.encoded = P11ECUtil.generateECPrivateKey(this.f13617s, this.params).getEncoded();
                } catch (InvalidKeySpecException e2) {
                    throw new ProviderException(e2);
                }
            }
            return this.encoded;
        }

        @Override // java.security.interfaces.ECPrivateKey
        public BigInteger getS() {
            fetchValues();
            return this.f13617s;
        }

        @Override // java.security.interfaces.ECKey
        public ECParameterSpec getParams() {
            fetchValues();
            return this.params;
        }
    }

    /* loaded from: sunpkcs11.jar:sun/security/pkcs11/P11Key$P11ECPublicKey.class */
    private static final class P11ECPublicKey extends P11Key implements ECPublicKey {
        private static final long serialVersionUID = -6371481375154806089L;

        /* renamed from: w, reason: collision with root package name */
        private ECPoint f13618w;
        private ECParameterSpec params;
        private byte[] encoded;

        P11ECPublicKey(Session session, long j2, String str, int i2, CK_ATTRIBUTE[] ck_attributeArr) {
            super(P11Key.PUBLIC, session, j2, str, i2, ck_attributeArr);
        }

        private synchronized void fetchValues() throws IOException {
            this.token.ensureValid();
            if (this.f13618w != null) {
                return;
            }
            CK_ATTRIBUTE[] ck_attributeArr = {new CK_ATTRIBUTE(385L), new CK_ATTRIBUTE(384L)};
            fetchAttributes(ck_attributeArr);
            try {
                this.params = P11ECKeyFactory.decodeParameters(ck_attributeArr[1].getByteArray());
                byte[] byteArray = ck_attributeArr[0].getByteArray();
                if (!this.token.config.getUseEcX963Encoding()) {
                    DerValue derValue = new DerValue(byteArray);
                    if (derValue.getTag() != 4) {
                        throw new IOException("Could not DER decode EC point. Unexpected tag: " + ((int) derValue.getTag()));
                    }
                    this.f13618w = P11ECKeyFactory.decodePoint(derValue.getDataBytes(), this.params.getCurve());
                } else {
                    this.f13618w = P11ECKeyFactory.decodePoint(byteArray, this.params.getCurve());
                }
            } catch (Exception e2) {
                throw new RuntimeException("Could not parse key values", e2);
            }
        }

        @Override // java.security.Key
        public String getFormat() {
            this.token.ensureValid();
            return XMLX509Certificate.JCA_CERT_ID;
        }

        @Override // sun.security.pkcs11.P11Key
        synchronized byte[] getEncodedInternal() throws IOException {
            this.token.ensureValid();
            if (this.encoded == null) {
                fetchValues();
                try {
                    return P11ECUtil.x509EncodeECPublicKey(this.f13618w, this.params);
                } catch (InvalidKeySpecException e2) {
                    throw new ProviderException(e2);
                }
            }
            return this.encoded;
        }

        @Override // java.security.interfaces.ECPublicKey
        public ECPoint getW() throws IOException {
            fetchValues();
            return this.f13618w;
        }

        @Override // java.security.interfaces.ECKey
        public ECParameterSpec getParams() throws IOException {
            fetchValues();
            return this.params;
        }

        @Override // sun.security.pkcs11.P11Key
        public String toString() throws IOException {
            fetchValues();
            return super.toString() + "\n  public x coord: " + ((Object) this.f13618w.getAffineX()) + "\n  public y coord: " + ((Object) this.f13618w.getAffineY()) + "\n  parameters: " + ((Object) this.params);
        }
    }
}
