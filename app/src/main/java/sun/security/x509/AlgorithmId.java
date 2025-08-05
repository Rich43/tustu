package sun.security.x509;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.security.AlgorithmParameters;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.ProviderException;
import java.security.Security;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PSSParameterSpec;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import javax.xml.datatype.DatatypeConstants;
import sun.security.rsa.PSSParameters;
import sun.security.util.DerEncoder;
import sun.security.util.DerInputStream;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;
import sun.security.util.KeyUtil;
import sun.security.util.ObjectIdentifier;
import sun.util.locale.LanguageTag;

/* loaded from: rt.jar:sun/security/x509/AlgorithmId.class */
public class AlgorithmId implements Serializable, DerEncoder {
    private static final long serialVersionUID = 7205873507486557157L;
    private ObjectIdentifier algid;
    private AlgorithmParameters algParams;
    private boolean constructedFromDer;
    protected DerValue params;
    private transient byte[] encodedParams;
    private static volatile Map<String, ObjectIdentifier> oidTable;
    private static final Map<ObjectIdentifier, String> nameTable;
    public static final ObjectIdentifier MD2_oid;
    public static final ObjectIdentifier MD5_oid;
    public static final ObjectIdentifier SHA_oid;
    public static final ObjectIdentifier SHA224_oid;
    public static final ObjectIdentifier SHA256_oid;
    public static final ObjectIdentifier SHA384_oid;
    public static final ObjectIdentifier SHA512_oid;
    public static final ObjectIdentifier SHA512_224_oid;
    public static final ObjectIdentifier SHA512_256_oid;
    private static final int[] DH_data;
    private static final int[] DH_PKIX_data;
    private static final int[] DSA_OIW_data;
    private static final int[] DSA_PKIX_data;
    private static final int[] RSA_data;
    public static final ObjectIdentifier DH_oid;
    public static final ObjectIdentifier DH_PKIX_oid;
    public static final ObjectIdentifier DSA_oid;
    public static final ObjectIdentifier DSA_OIW_oid;
    public static final ObjectIdentifier EC_oid;
    public static final ObjectIdentifier ECDH_oid;
    public static final ObjectIdentifier RSA_oid;
    public static final ObjectIdentifier RSAEncryption_oid;
    public static final ObjectIdentifier RSAES_OAEP_oid;
    public static final ObjectIdentifier mgf1_oid;
    public static final ObjectIdentifier RSASSA_PSS_oid;
    public static final ObjectIdentifier AES_oid;
    private static final int[] md2WithRSAEncryption_data;
    private static final int[] md5WithRSAEncryption_data;
    private static final int[] sha1WithRSAEncryption_data;
    private static final int[] sha1WithRSAEncryption_OIW_data;
    private static final int[] sha224WithRSAEncryption_data;
    private static final int[] sha256WithRSAEncryption_data;
    private static final int[] sha384WithRSAEncryption_data;
    private static final int[] sha512WithRSAEncryption_data;
    private static final int[] shaWithDSA_OIW_data;
    private static final int[] sha1WithDSA_OIW_data;
    private static final int[] dsaWithSHA1_PKIX_data;
    public static final ObjectIdentifier md2WithRSAEncryption_oid;
    public static final ObjectIdentifier md5WithRSAEncryption_oid;
    public static final ObjectIdentifier sha1WithRSAEncryption_oid;
    public static final ObjectIdentifier sha1WithRSAEncryption_OIW_oid;
    public static final ObjectIdentifier sha224WithRSAEncryption_oid;
    public static final ObjectIdentifier sha256WithRSAEncryption_oid;
    public static final ObjectIdentifier sha384WithRSAEncryption_oid;
    public static final ObjectIdentifier sha512WithRSAEncryption_oid;
    public static final ObjectIdentifier sha512_224WithRSAEncryption_oid;
    public static final ObjectIdentifier sha512_256WithRSAEncryption_oid;
    public static final ObjectIdentifier shaWithDSA_OIW_oid;
    public static final ObjectIdentifier sha1WithDSA_OIW_oid;
    public static final ObjectIdentifier sha1WithDSA_oid;
    public static final ObjectIdentifier sha224WithDSA_oid;
    public static final ObjectIdentifier sha256WithDSA_oid;
    public static final ObjectIdentifier sha1WithECDSA_oid;
    public static final ObjectIdentifier sha224WithECDSA_oid;
    public static final ObjectIdentifier sha256WithECDSA_oid;
    public static final ObjectIdentifier sha384WithECDSA_oid;
    public static final ObjectIdentifier sha512WithECDSA_oid;
    public static final ObjectIdentifier specifiedWithECDSA_oid;
    public static final ObjectIdentifier pbeWithMD5AndDES_oid;
    public static final ObjectIdentifier pbeWithMD5AndRC2_oid;
    public static final ObjectIdentifier pbeWithSHA1AndDES_oid;
    public static final ObjectIdentifier pbeWithSHA1AndRC2_oid;
    public static ObjectIdentifier pbeWithSHA1AndRC4_128_oid;
    public static ObjectIdentifier pbeWithSHA1AndRC4_40_oid;
    public static ObjectIdentifier pbeWithSHA1AndDESede_oid;
    public static ObjectIdentifier pbeWithSHA1AndRC2_128_oid;
    public static ObjectIdentifier pbeWithSHA1AndRC2_40_oid;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !AlgorithmId.class.desiredAssertionStatus();
        MD2_oid = ObjectIdentifier.newInternal(new int[]{1, 2, DatatypeConstants.MIN_TIMEZONE_OFFSET, 113549, 2, 2});
        MD5_oid = ObjectIdentifier.newInternal(new int[]{1, 2, DatatypeConstants.MIN_TIMEZONE_OFFSET, 113549, 2, 5});
        SHA_oid = ObjectIdentifier.newInternal(new int[]{1, 3, 14, 3, 2, 26});
        SHA224_oid = ObjectIdentifier.newInternal(new int[]{2, 16, DatatypeConstants.MIN_TIMEZONE_OFFSET, 1, 101, 3, 4, 2, 4});
        SHA256_oid = ObjectIdentifier.newInternal(new int[]{2, 16, DatatypeConstants.MIN_TIMEZONE_OFFSET, 1, 101, 3, 4, 2, 1});
        SHA384_oid = ObjectIdentifier.newInternal(new int[]{2, 16, DatatypeConstants.MIN_TIMEZONE_OFFSET, 1, 101, 3, 4, 2, 2});
        SHA512_oid = ObjectIdentifier.newInternal(new int[]{2, 16, DatatypeConstants.MIN_TIMEZONE_OFFSET, 1, 101, 3, 4, 2, 3});
        SHA512_224_oid = ObjectIdentifier.newInternal(new int[]{2, 16, DatatypeConstants.MIN_TIMEZONE_OFFSET, 1, 101, 3, 4, 2, 5});
        SHA512_256_oid = ObjectIdentifier.newInternal(new int[]{2, 16, DatatypeConstants.MIN_TIMEZONE_OFFSET, 1, 101, 3, 4, 2, 6});
        DH_data = new int[]{1, 2, DatatypeConstants.MIN_TIMEZONE_OFFSET, 113549, 1, 3, 1};
        DH_PKIX_data = new int[]{1, 2, DatatypeConstants.MIN_TIMEZONE_OFFSET, 10046, 2, 1};
        DSA_OIW_data = new int[]{1, 3, 14, 3, 2, 12};
        DSA_PKIX_data = new int[]{1, 2, DatatypeConstants.MIN_TIMEZONE_OFFSET, 10040, 4, 1};
        RSA_data = new int[]{2, 5, 8, 1, 1};
        EC_oid = oid(1, 2, DatatypeConstants.MIN_TIMEZONE_OFFSET, 10045, 2, 1);
        ECDH_oid = oid(1, 3, 132, 1, 12);
        RSAEncryption_oid = oid(1, 2, DatatypeConstants.MIN_TIMEZONE_OFFSET, 113549, 1, 1, 1);
        RSAES_OAEP_oid = oid(1, 2, DatatypeConstants.MIN_TIMEZONE_OFFSET, 113549, 1, 1, 7);
        mgf1_oid = oid(1, 2, DatatypeConstants.MIN_TIMEZONE_OFFSET, 113549, 1, 1, 8);
        RSASSA_PSS_oid = oid(1, 2, DatatypeConstants.MIN_TIMEZONE_OFFSET, 113549, 1, 1, 10);
        AES_oid = oid(2, 16, DatatypeConstants.MIN_TIMEZONE_OFFSET, 1, 101, 3, 4, 1);
        md2WithRSAEncryption_data = new int[]{1, 2, DatatypeConstants.MIN_TIMEZONE_OFFSET, 113549, 1, 1, 2};
        md5WithRSAEncryption_data = new int[]{1, 2, DatatypeConstants.MIN_TIMEZONE_OFFSET, 113549, 1, 1, 4};
        sha1WithRSAEncryption_data = new int[]{1, 2, DatatypeConstants.MIN_TIMEZONE_OFFSET, 113549, 1, 1, 5};
        sha1WithRSAEncryption_OIW_data = new int[]{1, 3, 14, 3, 2, 29};
        sha224WithRSAEncryption_data = new int[]{1, 2, DatatypeConstants.MIN_TIMEZONE_OFFSET, 113549, 1, 1, 14};
        sha256WithRSAEncryption_data = new int[]{1, 2, DatatypeConstants.MIN_TIMEZONE_OFFSET, 113549, 1, 1, 11};
        sha384WithRSAEncryption_data = new int[]{1, 2, DatatypeConstants.MIN_TIMEZONE_OFFSET, 113549, 1, 1, 12};
        sha512WithRSAEncryption_data = new int[]{1, 2, DatatypeConstants.MIN_TIMEZONE_OFFSET, 113549, 1, 1, 13};
        shaWithDSA_OIW_data = new int[]{1, 3, 14, 3, 2, 13};
        sha1WithDSA_OIW_data = new int[]{1, 3, 14, 3, 2, 27};
        dsaWithSHA1_PKIX_data = new int[]{1, 2, DatatypeConstants.MIN_TIMEZONE_OFFSET, 10040, 4, 3};
        sha512_224WithRSAEncryption_oid = oid(1, 2, DatatypeConstants.MIN_TIMEZONE_OFFSET, 113549, 1, 1, 15);
        sha512_256WithRSAEncryption_oid = oid(1, 2, DatatypeConstants.MIN_TIMEZONE_OFFSET, 113549, 1, 1, 16);
        sha224WithDSA_oid = oid(2, 16, DatatypeConstants.MIN_TIMEZONE_OFFSET, 1, 101, 3, 4, 3, 1);
        sha256WithDSA_oid = oid(2, 16, DatatypeConstants.MIN_TIMEZONE_OFFSET, 1, 101, 3, 4, 3, 2);
        sha1WithECDSA_oid = oid(1, 2, DatatypeConstants.MIN_TIMEZONE_OFFSET, 10045, 4, 1);
        sha224WithECDSA_oid = oid(1, 2, DatatypeConstants.MIN_TIMEZONE_OFFSET, 10045, 4, 3, 1);
        sha256WithECDSA_oid = oid(1, 2, DatatypeConstants.MIN_TIMEZONE_OFFSET, 10045, 4, 3, 2);
        sha384WithECDSA_oid = oid(1, 2, DatatypeConstants.MIN_TIMEZONE_OFFSET, 10045, 4, 3, 3);
        sha512WithECDSA_oid = oid(1, 2, DatatypeConstants.MIN_TIMEZONE_OFFSET, 10045, 4, 3, 4);
        specifiedWithECDSA_oid = oid(1, 2, DatatypeConstants.MIN_TIMEZONE_OFFSET, 10045, 4, 3);
        pbeWithMD5AndDES_oid = ObjectIdentifier.newInternal(new int[]{1, 2, DatatypeConstants.MIN_TIMEZONE_OFFSET, 113549, 1, 5, 3});
        pbeWithMD5AndRC2_oid = ObjectIdentifier.newInternal(new int[]{1, 2, DatatypeConstants.MIN_TIMEZONE_OFFSET, 113549, 1, 5, 6});
        pbeWithSHA1AndDES_oid = ObjectIdentifier.newInternal(new int[]{1, 2, DatatypeConstants.MIN_TIMEZONE_OFFSET, 113549, 1, 5, 10});
        pbeWithSHA1AndRC2_oid = ObjectIdentifier.newInternal(new int[]{1, 2, DatatypeConstants.MIN_TIMEZONE_OFFSET, 113549, 1, 5, 11});
        pbeWithSHA1AndRC4_128_oid = ObjectIdentifier.newInternal(new int[]{1, 2, DatatypeConstants.MIN_TIMEZONE_OFFSET, 113549, 1, 12, 1, 1});
        pbeWithSHA1AndRC4_40_oid = ObjectIdentifier.newInternal(new int[]{1, 2, DatatypeConstants.MIN_TIMEZONE_OFFSET, 113549, 1, 12, 1, 2});
        pbeWithSHA1AndDESede_oid = ObjectIdentifier.newInternal(new int[]{1, 2, DatatypeConstants.MIN_TIMEZONE_OFFSET, 113549, 1, 12, 1, 3});
        pbeWithSHA1AndRC2_128_oid = ObjectIdentifier.newInternal(new int[]{1, 2, DatatypeConstants.MIN_TIMEZONE_OFFSET, 113549, 1, 12, 1, 5});
        pbeWithSHA1AndRC2_40_oid = ObjectIdentifier.newInternal(new int[]{1, 2, DatatypeConstants.MIN_TIMEZONE_OFFSET, 113549, 1, 12, 1, 6});
        DH_oid = ObjectIdentifier.newInternal(DH_data);
        DH_PKIX_oid = ObjectIdentifier.newInternal(DH_PKIX_data);
        DSA_OIW_oid = ObjectIdentifier.newInternal(DSA_OIW_data);
        DSA_oid = ObjectIdentifier.newInternal(DSA_PKIX_data);
        RSA_oid = ObjectIdentifier.newInternal(RSA_data);
        md2WithRSAEncryption_oid = ObjectIdentifier.newInternal(md2WithRSAEncryption_data);
        md5WithRSAEncryption_oid = ObjectIdentifier.newInternal(md5WithRSAEncryption_data);
        sha1WithRSAEncryption_oid = ObjectIdentifier.newInternal(sha1WithRSAEncryption_data);
        sha1WithRSAEncryption_OIW_oid = ObjectIdentifier.newInternal(sha1WithRSAEncryption_OIW_data);
        sha224WithRSAEncryption_oid = ObjectIdentifier.newInternal(sha224WithRSAEncryption_data);
        sha256WithRSAEncryption_oid = ObjectIdentifier.newInternal(sha256WithRSAEncryption_data);
        sha384WithRSAEncryption_oid = ObjectIdentifier.newInternal(sha384WithRSAEncryption_data);
        sha512WithRSAEncryption_oid = ObjectIdentifier.newInternal(sha512WithRSAEncryption_data);
        shaWithDSA_OIW_oid = ObjectIdentifier.newInternal(shaWithDSA_OIW_data);
        sha1WithDSA_OIW_oid = ObjectIdentifier.newInternal(sha1WithDSA_OIW_data);
        sha1WithDSA_oid = ObjectIdentifier.newInternal(dsaWithSHA1_PKIX_data);
        nameTable = new HashMap();
        nameTable.put(MD5_oid, "MD5");
        nameTable.put(MD2_oid, "MD2");
        nameTable.put(SHA_oid, "SHA-1");
        nameTable.put(SHA224_oid, "SHA-224");
        nameTable.put(SHA256_oid, "SHA-256");
        nameTable.put(SHA384_oid, "SHA-384");
        nameTable.put(SHA512_oid, "SHA-512");
        nameTable.put(SHA512_224_oid, "SHA-512/224");
        nameTable.put(SHA512_256_oid, "SHA-512/256");
        nameTable.put(RSAEncryption_oid, "RSA");
        nameTable.put(RSA_oid, "RSA");
        nameTable.put(DH_oid, "Diffie-Hellman");
        nameTable.put(DH_PKIX_oid, "Diffie-Hellman");
        nameTable.put(DSA_oid, "DSA");
        nameTable.put(DSA_OIW_oid, "DSA");
        nameTable.put(EC_oid, "EC");
        nameTable.put(ECDH_oid, "ECDH");
        nameTable.put(AES_oid, "AES");
        nameTable.put(sha1WithECDSA_oid, "SHA1withECDSA");
        nameTable.put(sha224WithECDSA_oid, "SHA224withECDSA");
        nameTable.put(sha256WithECDSA_oid, "SHA256withECDSA");
        nameTable.put(sha384WithECDSA_oid, "SHA384withECDSA");
        nameTable.put(sha512WithECDSA_oid, "SHA512withECDSA");
        nameTable.put(md5WithRSAEncryption_oid, "MD5withRSA");
        nameTable.put(md2WithRSAEncryption_oid, "MD2withRSA");
        nameTable.put(sha1WithDSA_oid, "SHA1withDSA");
        nameTable.put(sha1WithDSA_OIW_oid, "SHA1withDSA");
        nameTable.put(shaWithDSA_OIW_oid, "SHA1withDSA");
        nameTable.put(sha224WithDSA_oid, "SHA224withDSA");
        nameTable.put(sha256WithDSA_oid, "SHA256withDSA");
        nameTable.put(sha1WithRSAEncryption_oid, "SHA1withRSA");
        nameTable.put(sha1WithRSAEncryption_OIW_oid, "SHA1withRSA");
        nameTable.put(sha224WithRSAEncryption_oid, "SHA224withRSA");
        nameTable.put(sha256WithRSAEncryption_oid, "SHA256withRSA");
        nameTable.put(sha384WithRSAEncryption_oid, "SHA384withRSA");
        nameTable.put(sha512WithRSAEncryption_oid, "SHA512withRSA");
        nameTable.put(sha512_224WithRSAEncryption_oid, "SHA512/224withRSA");
        nameTable.put(sha512_256WithRSAEncryption_oid, "SHA512/256withRSA");
        nameTable.put(RSASSA_PSS_oid, "RSASSA-PSS");
        nameTable.put(RSAES_OAEP_oid, "RSAES-OAEP");
        nameTable.put(pbeWithMD5AndDES_oid, "PBEWithMD5AndDES");
        nameTable.put(pbeWithMD5AndRC2_oid, "PBEWithMD5AndRC2");
        nameTable.put(pbeWithSHA1AndDES_oid, "PBEWithSHA1AndDES");
        nameTable.put(pbeWithSHA1AndRC2_oid, "PBEWithSHA1AndRC2");
        nameTable.put(pbeWithSHA1AndRC4_128_oid, "PBEWithSHA1AndRC4_128");
        nameTable.put(pbeWithSHA1AndRC4_40_oid, "PBEWithSHA1AndRC4_40");
        nameTable.put(pbeWithSHA1AndDESede_oid, "PBEWithSHA1AndDESede");
        nameTable.put(pbeWithSHA1AndRC2_128_oid, "PBEWithSHA1AndRC2_128");
        nameTable.put(pbeWithSHA1AndRC2_40_oid, "PBEWithSHA1AndRC2_40");
    }

    @Deprecated
    public AlgorithmId() {
        this.constructedFromDer = true;
    }

    public AlgorithmId(ObjectIdentifier objectIdentifier) {
        this.constructedFromDer = true;
        this.algid = objectIdentifier;
    }

    public AlgorithmId(ObjectIdentifier objectIdentifier, AlgorithmParameters algorithmParameters) {
        this.constructedFromDer = true;
        this.algid = objectIdentifier;
        this.algParams = algorithmParameters;
        this.constructedFromDer = false;
        if (this.algParams != null) {
            try {
                this.encodedParams = this.algParams.getEncoded();
            } catch (IOException e2) {
                if (!$assertionsDisabled) {
                    throw new AssertionError();
                }
            }
        }
    }

    private AlgorithmId(ObjectIdentifier objectIdentifier, DerValue derValue) throws IOException {
        this.constructedFromDer = true;
        this.algid = objectIdentifier;
        this.params = derValue;
        if (this.params != null) {
            this.encodedParams = derValue.toByteArray();
            decodeParams();
        }
    }

    protected void decodeParams() throws IOException {
        try {
            this.algParams = AlgorithmParameters.getInstance(getName());
            this.algParams.init((byte[]) this.encodedParams.clone());
        } catch (NoSuchAlgorithmException e2) {
            this.algParams = null;
        }
    }

    public final void encode(DerOutputStream derOutputStream) throws IOException {
        derEncode(derOutputStream);
    }

    @Override // sun.security.util.DerEncoder
    public void derEncode(OutputStream outputStream) throws IOException {
        DerOutputStream derOutputStream = new DerOutputStream();
        DerOutputStream derOutputStream2 = new DerOutputStream();
        derOutputStream.putOID(this.algid);
        if (!this.constructedFromDer) {
            if (this.encodedParams != null) {
                this.params = new DerValue(this.encodedParams);
            } else {
                this.params = null;
            }
        }
        if (this.params == null) {
            if (!this.algid.equals(RSASSA_PSS_oid)) {
                derOutputStream.putNull();
            }
        } else {
            derOutputStream.putDerValue(this.params);
        }
        derOutputStream2.write((byte) 48, derOutputStream);
        outputStream.write(derOutputStream2.toByteArray());
    }

    public final byte[] encode() throws IOException {
        DerOutputStream derOutputStream = new DerOutputStream();
        derEncode(derOutputStream);
        return derOutputStream.toByteArray();
    }

    public final ObjectIdentifier getOID() {
        return this.algid;
    }

    public String getName() {
        String strMakeSigAlg = nameTable.get(this.algid);
        if (strMakeSigAlg != null) {
            return strMakeSigAlg;
        }
        if (this.params != null && this.algid.equals((Object) specifiedWithECDSA_oid)) {
            try {
                strMakeSigAlg = makeSigAlg(parse(new DerValue(this.encodedParams)).getName(), "EC");
            } catch (IOException e2) {
            }
        }
        return strMakeSigAlg == null ? this.algid.toString() : strMakeSigAlg;
    }

    public AlgorithmParameters getParameters() {
        return this.algParams;
    }

    public byte[] getEncodedParams() throws IOException {
        if (this.encodedParams == null || this.algid.equals(specifiedWithECDSA_oid)) {
            return null;
        }
        return (byte[]) this.encodedParams.clone();
    }

    public boolean equals(AlgorithmId algorithmId) {
        return this.algid.equals((Object) algorithmId.algid) && Arrays.equals(this.encodedParams, algorithmId.encodedParams);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof AlgorithmId) {
            return equals((AlgorithmId) obj);
        }
        if (obj instanceof ObjectIdentifier) {
            return equals((ObjectIdentifier) obj);
        }
        return false;
    }

    public final boolean equals(ObjectIdentifier objectIdentifier) {
        return this.algid.equals((Object) objectIdentifier);
    }

    public int hashCode() {
        return (31 * this.algid.hashCode()) + Arrays.hashCode(this.encodedParams);
    }

    protected String paramsToString() {
        if (this.encodedParams == null) {
            return "";
        }
        if (this.algParams != null) {
            return ", " + this.algParams.toString();
        }
        return ", params unparsed";
    }

    public String toString() {
        return getName() + paramsToString();
    }

    public static AlgorithmId parse(DerValue derValue) throws IOException {
        DerValue derValue2;
        if (derValue.tag != 48) {
            throw new IOException("algid parse error, not a sequence");
        }
        DerInputStream derInputStream = derValue.toDerInputStream();
        ObjectIdentifier oid = derInputStream.getOID();
        if (derInputStream.available() == 0) {
            derValue2 = null;
        } else {
            derValue2 = derInputStream.getDerValue();
            if (derValue2.tag == 5) {
                if (derValue2.length() != 0) {
                    throw new IOException("invalid NULL");
                }
                derValue2 = null;
            }
            if (derInputStream.available() != 0) {
                throw new IOException("Invalid AlgorithmIdentifier: extra data");
            }
        }
        return new AlgorithmId(oid, derValue2);
    }

    @Deprecated
    public static AlgorithmId getAlgorithmId(String str) throws NoSuchAlgorithmException {
        return get(str);
    }

    public static AlgorithmId get(String str) throws NoSuchAlgorithmException {
        try {
            ObjectIdentifier objectIdentifierAlgOID = algOID(str);
            if (objectIdentifierAlgOID == null) {
                throw new NoSuchAlgorithmException("unrecognized algorithm name: " + str);
            }
            return new AlgorithmId(objectIdentifierAlgOID);
        } catch (IOException e2) {
            throw new NoSuchAlgorithmException("Invalid ObjectIdentifier " + str);
        }
    }

    public static AlgorithmId get(AlgorithmParameters algorithmParameters) throws NoSuchAlgorithmException {
        String algorithm = algorithmParameters.getAlgorithm();
        try {
            ObjectIdentifier objectIdentifierAlgOID = algOID(algorithm);
            if (objectIdentifierAlgOID == null) {
                throw new NoSuchAlgorithmException("unrecognized algorithm name: " + algorithm);
            }
            return new AlgorithmId(objectIdentifierAlgOID, algorithmParameters);
        } catch (IOException e2) {
            throw new NoSuchAlgorithmException("Invalid ObjectIdentifier " + algorithm);
        }
    }

    private static ObjectIdentifier algOID(String str) throws IOException {
        if (str.indexOf(46) != -1) {
            if (str.startsWith("OID.")) {
                return new ObjectIdentifier(str.substring("OID.".length()));
            }
            return new ObjectIdentifier(str);
        }
        if (str.equalsIgnoreCase("MD5")) {
            return MD5_oid;
        }
        if (str.equalsIgnoreCase("MD2")) {
            return MD2_oid;
        }
        if (str.equalsIgnoreCase("SHA") || str.equalsIgnoreCase("SHA1") || str.equalsIgnoreCase("SHA-1")) {
            return SHA_oid;
        }
        if (str.equalsIgnoreCase("SHA-256") || str.equalsIgnoreCase("SHA256")) {
            return SHA256_oid;
        }
        if (str.equalsIgnoreCase("SHA-384") || str.equalsIgnoreCase("SHA384")) {
            return SHA384_oid;
        }
        if (str.equalsIgnoreCase("SHA-512") || str.equalsIgnoreCase("SHA512")) {
            return SHA512_oid;
        }
        if (str.equalsIgnoreCase("SHA-224") || str.equalsIgnoreCase("SHA224")) {
            return SHA224_oid;
        }
        if (str.equalsIgnoreCase("SHA-512/224") || str.equalsIgnoreCase("SHA512/224")) {
            return SHA512_224_oid;
        }
        if (str.equalsIgnoreCase("SHA-512/256") || str.equalsIgnoreCase("SHA512/256")) {
            return SHA512_256_oid;
        }
        if (str.equalsIgnoreCase("RSA")) {
            return RSAEncryption_oid;
        }
        if (str.equalsIgnoreCase("RSASSA-PSS")) {
            return RSASSA_PSS_oid;
        }
        if (str.equalsIgnoreCase("RSAES-OAEP")) {
            return RSAES_OAEP_oid;
        }
        if (str.equalsIgnoreCase("Diffie-Hellman") || str.equalsIgnoreCase("DH")) {
            return DH_oid;
        }
        if (str.equalsIgnoreCase("DSA")) {
            return DSA_oid;
        }
        if (str.equalsIgnoreCase("EC")) {
            return EC_oid;
        }
        if (str.equalsIgnoreCase("ECDH")) {
            return ECDH_oid;
        }
        if (str.equalsIgnoreCase("AES")) {
            return AES_oid;
        }
        if (str.equalsIgnoreCase("MD5withRSA") || str.equalsIgnoreCase("MD5/RSA")) {
            return md5WithRSAEncryption_oid;
        }
        if (str.equalsIgnoreCase("MD2withRSA") || str.equalsIgnoreCase("MD2/RSA")) {
            return md2WithRSAEncryption_oid;
        }
        if (str.equalsIgnoreCase("SHAwithDSA") || str.equalsIgnoreCase("SHA1withDSA") || str.equalsIgnoreCase("SHA/DSA") || str.equalsIgnoreCase("SHA1/DSA") || str.equalsIgnoreCase("DSAWithSHA1") || str.equalsIgnoreCase("DSS") || str.equalsIgnoreCase("SHA-1/DSA")) {
            return sha1WithDSA_oid;
        }
        if (str.equalsIgnoreCase("SHA224WithDSA")) {
            return sha224WithDSA_oid;
        }
        if (str.equalsIgnoreCase("SHA256WithDSA")) {
            return sha256WithDSA_oid;
        }
        if (str.equalsIgnoreCase("SHA1WithRSA") || str.equalsIgnoreCase("SHA1/RSA")) {
            return sha1WithRSAEncryption_oid;
        }
        if (str.equalsIgnoreCase("SHA256WithRSA")) {
            return sha256WithRSAEncryption_oid;
        }
        if (str.equalsIgnoreCase("SHA384WithRSA")) {
            return sha384WithRSAEncryption_oid;
        }
        if (str.equalsIgnoreCase("SHA512WithRSA")) {
            return sha512WithRSAEncryption_oid;
        }
        if (str.equalsIgnoreCase("SHA1withECDSA") || str.equalsIgnoreCase("ECDSA")) {
            return sha1WithECDSA_oid;
        }
        if (str.equalsIgnoreCase("SHA224withECDSA")) {
            return sha224WithECDSA_oid;
        }
        if (str.equalsIgnoreCase("SHA256withECDSA")) {
            return sha256WithECDSA_oid;
        }
        if (str.equalsIgnoreCase("SHA384withECDSA")) {
            return sha384WithECDSA_oid;
        }
        if (str.equalsIgnoreCase("SHA512withECDSA")) {
            return sha512WithECDSA_oid;
        }
        return oidTable().get(str.toUpperCase(Locale.ENGLISH));
    }

    private static ObjectIdentifier oid(int... iArr) {
        return ObjectIdentifier.newInternal(iArr);
    }

    private static Map<String, ObjectIdentifier> oidTable() throws IOException {
        Map<String, ObjectIdentifier> map = oidTable;
        Map<String, ObjectIdentifier> map2 = map;
        if (map == null) {
            synchronized (AlgorithmId.class) {
                Map<String, ObjectIdentifier> map3 = oidTable;
                map2 = map3;
                if (map3 == null) {
                    HashMap<String, ObjectIdentifier> mapComputeOidTable = computeOidTable();
                    map2 = mapComputeOidTable;
                    oidTable = mapComputeOidTable;
                }
            }
        }
        return map2;
    }

    private static HashMap<String, ObjectIdentifier> computeOidTable() throws IOException {
        int iIndexOf;
        HashMap<String, ObjectIdentifier> map = new HashMap<>();
        for (Provider provider : Security.getProviders()) {
            Iterator<Object> it = provider.keySet().iterator();
            while (it.hasNext()) {
                String str = (String) it.next();
                String upperCase = str.toUpperCase(Locale.ENGLISH);
                if (upperCase.startsWith("ALG.ALIAS") && (iIndexOf = upperCase.indexOf("OID.", 0)) != -1) {
                    int length = iIndexOf + "OID.".length();
                    if (length == str.length()) {
                        break;
                    }
                    String strSubstring = str.substring(length);
                    String property = provider.getProperty(str);
                    if (property != null) {
                        property = property.toUpperCase(Locale.ENGLISH);
                    }
                    if (property != null && map.get(property) == null) {
                        map.put(property, new ObjectIdentifier(strSubstring));
                    }
                }
            }
        }
        return map;
    }

    public static String makeSigAlg(String str, String str2) {
        String strReplace = str.replace(LanguageTag.SEP, "");
        if (str2.equalsIgnoreCase("EC")) {
            str2 = "ECDSA";
        }
        return strReplace + "with" + str2;
    }

    public static String getEncAlgFromSigAlg(String str) {
        String upperCase = str.toUpperCase(Locale.ENGLISH);
        int iIndexOf = upperCase.indexOf("WITH");
        String strSubstring = null;
        if (iIndexOf > 0) {
            int iIndexOf2 = upperCase.indexOf("AND", iIndexOf + 4);
            if (iIndexOf2 > 0) {
                strSubstring = upperCase.substring(iIndexOf + 4, iIndexOf2);
            } else {
                strSubstring = upperCase.substring(iIndexOf + 4);
            }
            if (strSubstring.equalsIgnoreCase("ECDSA")) {
                strSubstring = "EC";
            }
        }
        return strSubstring;
    }

    public static String getDigAlgFromSigAlg(String str) {
        String upperCase = str.toUpperCase(Locale.ENGLISH);
        int iIndexOf = upperCase.indexOf("WITH");
        if (iIndexOf > 0) {
            return upperCase.substring(0, iIndexOf);
        }
        return null;
    }

    /* loaded from: rt.jar:sun/security/x509/AlgorithmId$PSSParamsHolder.class */
    private static class PSSParamsHolder {
        static final PSSParameterSpec PSS_256_SPEC = new PSSParameterSpec("SHA-256", "MGF1", new MGF1ParameterSpec("SHA-256"), 32, 1);
        static final PSSParameterSpec PSS_384_SPEC = new PSSParameterSpec("SHA-384", "MGF1", new MGF1ParameterSpec("SHA-384"), 48, 1);
        static final PSSParameterSpec PSS_512_SPEC = new PSSParameterSpec("SHA-512", "MGF1", new MGF1ParameterSpec("SHA-512"), 64, 1);
        static final AlgorithmId PSS_256_ID;
        static final AlgorithmId PSS_384_ID;
        static final AlgorithmId PSS_512_ID;

        private PSSParamsHolder() {
        }

        static {
            try {
                PSS_256_ID = new AlgorithmId(AlgorithmId.RSASSA_PSS_oid, new DerValue(PSSParameters.getEncoded(PSS_256_SPEC)));
                PSS_384_ID = new AlgorithmId(AlgorithmId.RSASSA_PSS_oid, new DerValue(PSSParameters.getEncoded(PSS_384_SPEC)));
                PSS_512_ID = new AlgorithmId(AlgorithmId.RSASSA_PSS_oid, new DerValue(PSSParameters.getEncoded(PSS_512_SPEC)));
            } catch (IOException e2) {
                throw new AssertionError("Should not happen", e2);
            }
        }
    }

    public static AlgorithmId getWithParameterSpec(String str, AlgorithmParameterSpec algorithmParameterSpec) throws NoSuchAlgorithmException {
        if (algorithmParameterSpec == null) {
            return get(str);
        }
        if (algorithmParameterSpec == PSSParamsHolder.PSS_256_SPEC) {
            return PSSParamsHolder.PSS_256_ID;
        }
        if (algorithmParameterSpec == PSSParamsHolder.PSS_384_SPEC) {
            return PSSParamsHolder.PSS_384_ID;
        }
        if (algorithmParameterSpec == PSSParamsHolder.PSS_512_SPEC) {
            return PSSParamsHolder.PSS_512_ID;
        }
        try {
            AlgorithmParameters algorithmParameters = AlgorithmParameters.getInstance(str);
            algorithmParameters.init(algorithmParameterSpec);
            return get(algorithmParameters);
        } catch (NoSuchAlgorithmException | InvalidParameterSpecException e2) {
            throw new ProviderException(e2);
        }
    }

    public static PSSParameterSpec getDefaultAlgorithmParameterSpec(String str, PrivateKey privateKey) {
        if (str.equalsIgnoreCase("RSASSA-PSS")) {
            switch (ifcFfcStrength(KeyUtil.getKeySize(privateKey))) {
                case "SHA256":
                    return PSSParamsHolder.PSS_256_SPEC;
                case "SHA384":
                    return PSSParamsHolder.PSS_384_SPEC;
                case "SHA512":
                    return PSSParamsHolder.PSS_512_SPEC;
                default:
                    throw new AssertionError((Object) "Should not happen");
            }
        }
        return null;
    }

    private static String ifcFfcStrength(int i2) {
        if (i2 > 7680) {
            return "SHA512";
        }
        if (i2 > 3072) {
            return "SHA384";
        }
        return "SHA256";
    }
}
