package com.sun.crypto.provider;

import java.io.IOException;
import java.security.AlgorithmParametersSpi;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidParameterSpecException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.xml.datatype.DatatypeConstants;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;
import sun.security.util.ObjectIdentifier;

/* loaded from: sunjce_provider.jar:com/sun/crypto/provider/PBES2Parameters.class */
abstract class PBES2Parameters extends AlgorithmParametersSpi {
    private static final int[] pkcs5PBKDF2 = {1, 2, DatatypeConstants.MIN_TIMEZONE_OFFSET, 113549, 1, 5, 12};
    private static final int[] pkcs5PBES2 = {1, 2, DatatypeConstants.MIN_TIMEZONE_OFFSET, 113549, 1, 5, 13};
    private static final int[] hmacWithSHA1 = {1, 2, DatatypeConstants.MIN_TIMEZONE_OFFSET, 113549, 2, 7};
    private static final int[] hmacWithSHA224 = {1, 2, DatatypeConstants.MIN_TIMEZONE_OFFSET, 113549, 2, 8};
    private static final int[] hmacWithSHA256 = {1, 2, DatatypeConstants.MIN_TIMEZONE_OFFSET, 113549, 2, 9};
    private static final int[] hmacWithSHA384 = {1, 2, DatatypeConstants.MIN_TIMEZONE_OFFSET, 113549, 2, 10};
    private static final int[] hmacWithSHA512 = {1, 2, DatatypeConstants.MIN_TIMEZONE_OFFSET, 113549, 2, 11};
    private static final int[] aes128CBC = {2, 16, DatatypeConstants.MIN_TIMEZONE_OFFSET, 1, 101, 3, 4, 1, 2};
    private static final int[] aes192CBC = {2, 16, DatatypeConstants.MIN_TIMEZONE_OFFSET, 1, 101, 3, 4, 1, 22};
    private static final int[] aes256CBC = {2, 16, DatatypeConstants.MIN_TIMEZONE_OFFSET, 1, 101, 3, 4, 1, 42};
    private static ObjectIdentifier pkcs5PBKDF2_OID;
    private static ObjectIdentifier pkcs5PBES2_OID;
    private static ObjectIdentifier hmacWithSHA1_OID;
    private static ObjectIdentifier hmacWithSHA224_OID;
    private static ObjectIdentifier hmacWithSHA256_OID;
    private static ObjectIdentifier hmacWithSHA384_OID;
    private static ObjectIdentifier hmacWithSHA512_OID;
    private static ObjectIdentifier aes128CBC_OID;
    private static ObjectIdentifier aes192CBC_OID;
    private static ObjectIdentifier aes256CBC_OID;
    private String pbes2AlgorithmName;
    private byte[] salt;
    private int iCount;
    private AlgorithmParameterSpec cipherParam;
    private ObjectIdentifier kdfAlgo_OID;
    private ObjectIdentifier cipherAlgo_OID;
    private int keysize;

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/PBES2Parameters$General.class */
    public static final class General extends PBES2Parameters {
    }

    static {
        try {
            pkcs5PBKDF2_OID = new ObjectIdentifier(pkcs5PBKDF2);
            pkcs5PBES2_OID = new ObjectIdentifier(pkcs5PBES2);
            hmacWithSHA1_OID = new ObjectIdentifier(hmacWithSHA1);
            hmacWithSHA224_OID = new ObjectIdentifier(hmacWithSHA224);
            hmacWithSHA256_OID = new ObjectIdentifier(hmacWithSHA256);
            hmacWithSHA384_OID = new ObjectIdentifier(hmacWithSHA384);
            hmacWithSHA512_OID = new ObjectIdentifier(hmacWithSHA512);
            aes128CBC_OID = new ObjectIdentifier(aes128CBC);
            aes192CBC_OID = new ObjectIdentifier(aes192CBC);
            aes256CBC_OID = new ObjectIdentifier(aes256CBC);
        } catch (IOException e2) {
        }
    }

    PBES2Parameters() {
        this.pbes2AlgorithmName = null;
        this.salt = null;
        this.iCount = 0;
        this.cipherParam = null;
        this.kdfAlgo_OID = hmacWithSHA1_OID;
        this.cipherAlgo_OID = null;
        this.keysize = -1;
    }

    PBES2Parameters(String str) throws NoSuchAlgorithmException {
        int iIndexOf;
        String strSubstring;
        this.pbes2AlgorithmName = null;
        this.salt = null;
        this.iCount = 0;
        this.cipherParam = null;
        this.kdfAlgo_OID = hmacWithSHA1_OID;
        this.cipherAlgo_OID = null;
        this.keysize = -1;
        this.pbes2AlgorithmName = str;
        if (str.startsWith("PBEWith") && (iIndexOf = str.indexOf("And", 8)) > 0) {
            strSubstring = str.substring(7, iIndexOf);
            String strSubstring2 = str.substring(iIndexOf + 3);
            int iIndexOf2 = strSubstring2.indexOf(95);
            if (iIndexOf2 > 0) {
                int iIndexOf3 = strSubstring2.indexOf(47, iIndexOf2 + 1);
                if (iIndexOf3 > 0) {
                    this.keysize = Integer.parseInt(strSubstring2.substring(iIndexOf2 + 1, iIndexOf3));
                } else {
                    this.keysize = Integer.parseInt(strSubstring2.substring(iIndexOf2 + 1));
                }
                strSubstring2 = strSubstring2.substring(0, iIndexOf2);
            }
            switch (strSubstring) {
                case "HmacSHA1":
                    this.kdfAlgo_OID = hmacWithSHA1_OID;
                    break;
                case "HmacSHA224":
                    this.kdfAlgo_OID = hmacWithSHA224_OID;
                    break;
                case "HmacSHA256":
                    this.kdfAlgo_OID = hmacWithSHA256_OID;
                    break;
                case "HmacSHA384":
                    this.kdfAlgo_OID = hmacWithSHA384_OID;
                    break;
                case "HmacSHA512":
                    this.kdfAlgo_OID = hmacWithSHA512_OID;
                    break;
                default:
                    throw new NoSuchAlgorithmException("No crypto implementation for " + strSubstring);
            }
            if (strSubstring2.equals("AES")) {
                this.keysize = this.keysize;
                switch (this.keysize) {
                    case 128:
                        this.cipherAlgo_OID = aes128CBC_OID;
                        return;
                    case 256:
                        this.cipherAlgo_OID = aes256CBC_OID;
                        return;
                    default:
                        throw new NoSuchAlgorithmException("No Cipher implementation for " + this.keysize + "-bit " + strSubstring2);
                }
            }
            throw new NoSuchAlgorithmException("No Cipher implementation for " + strSubstring2);
        }
        throw new NoSuchAlgorithmException("No crypto implementation for " + str);
    }

    @Override // java.security.AlgorithmParametersSpi
    protected void engineInit(AlgorithmParameterSpec algorithmParameterSpec) throws InvalidParameterSpecException {
        if (!(algorithmParameterSpec instanceof PBEParameterSpec)) {
            throw new InvalidParameterSpecException("Inappropriate parameter specification");
        }
        this.salt = (byte[]) ((PBEParameterSpec) algorithmParameterSpec).getSalt().clone();
        this.iCount = ((PBEParameterSpec) algorithmParameterSpec).getIterationCount();
        this.cipherParam = ((PBEParameterSpec) algorithmParameterSpec).getParameterSpec();
    }

    @Override // java.security.AlgorithmParametersSpi
    protected void engineInit(byte[] bArr) throws IOException {
        DerValue derValue = new DerValue(bArr);
        if (derValue.tag != 48) {
            throw new IOException("PBE parameter parsing error: not an ASN.1 SEQUENCE tag");
        }
        DerValue derValue2 = derValue.data.getDerValue();
        if (derValue2.getTag() == 6) {
            derValue = derValue.data.getDerValue();
            derValue2 = derValue.data.getDerValue();
        }
        String kdf = parseKDF(derValue2);
        if (derValue.tag != 48) {
            throw new IOException("PBE parameter parsing error: not an ASN.1 SEQUENCE tag");
        }
        this.pbes2AlgorithmName = "PBEWith" + kdf + "And" + parseES(derValue.data.getDerValue());
    }

    private String parseKDF(DerValue derValue) throws IOException {
        if (!pkcs5PBKDF2_OID.equals(derValue.data.getOID())) {
            throw new IOException("PBE parameter parsing error: expecting the object identifier for PBKDF2");
        }
        if (derValue.tag != 48) {
            throw new IOException("PBE parameter parsing error: not an ASN.1 SEQUENCE tag");
        }
        DerValue derValue2 = derValue.data.getDerValue();
        if (derValue2.tag != 48) {
            throw new IOException("PBE parameter parsing error: not an ASN.1 SEQUENCE tag");
        }
        DerValue derValue3 = derValue2.data.getDerValue();
        if (derValue3.tag == 4) {
            this.salt = derValue3.getOctetString();
            this.iCount = derValue2.data.getInteger();
            DerValue derValue4 = null;
            if (derValue2.data.available() > 0) {
                DerValue derValue5 = derValue2.data.getDerValue();
                if (derValue5.tag == 2) {
                    this.keysize = derValue5.getInteger() * 8;
                } else {
                    derValue4 = derValue5;
                }
            }
            String str = "HmacSHA1";
            if (derValue4 == null && derValue2.data.available() > 0) {
                derValue4 = derValue2.data.getDerValue();
            }
            if (derValue4 != null) {
                this.kdfAlgo_OID = derValue4.data.getOID();
                if (hmacWithSHA1_OID.equals(this.kdfAlgo_OID)) {
                    str = "HmacSHA1";
                } else if (hmacWithSHA224_OID.equals(this.kdfAlgo_OID)) {
                    str = "HmacSHA224";
                } else if (hmacWithSHA256_OID.equals(this.kdfAlgo_OID)) {
                    str = "HmacSHA256";
                } else if (hmacWithSHA384_OID.equals(this.kdfAlgo_OID)) {
                    str = "HmacSHA384";
                } else if (hmacWithSHA512_OID.equals(this.kdfAlgo_OID)) {
                    str = "HmacSHA512";
                } else {
                    throw new IOException("PBE parameter parsing error: expecting the object identifier for a HmacSHA key derivation function");
                }
                if (derValue4.data.available() != 0 && derValue4.data.getDerValue().tag != 5) {
                    throw new IOException("PBE parameter parsing error: not an ASN.1 NULL tag");
                }
            }
            return str;
        }
        throw new IOException("PBE parameter parsing error: not an ASN.1 OCTET STRING tag");
    }

    private String parseES(DerValue derValue) throws IOException {
        String str;
        this.cipherAlgo_OID = derValue.data.getOID();
        if (aes128CBC_OID.equals(this.cipherAlgo_OID)) {
            str = "AES_128";
            this.cipherParam = new IvParameterSpec(derValue.data.getOctetString());
            this.keysize = 128;
        } else if (aes256CBC_OID.equals(this.cipherAlgo_OID)) {
            str = "AES_256";
            this.cipherParam = new IvParameterSpec(derValue.data.getOctetString());
            this.keysize = 256;
        } else {
            throw new IOException("PBE parameter parsing error: expecting the object identifier for AES cipher");
        }
        return str;
    }

    @Override // java.security.AlgorithmParametersSpi
    protected void engineInit(byte[] bArr, String str) throws IOException {
        engineInit(bArr);
    }

    @Override // java.security.AlgorithmParametersSpi
    protected <T extends AlgorithmParameterSpec> T engineGetParameterSpec(Class<T> cls) throws InvalidParameterSpecException {
        if (PBEParameterSpec.class.isAssignableFrom(cls)) {
            return cls.cast(new PBEParameterSpec(this.salt, this.iCount, this.cipherParam));
        }
        throw new InvalidParameterSpecException("Inappropriate parameter specification");
    }

    @Override // java.security.AlgorithmParametersSpi
    protected byte[] engineGetEncoded() throws IOException {
        DerOutputStream derOutputStream = new DerOutputStream();
        DerOutputStream derOutputStream2 = new DerOutputStream();
        DerOutputStream derOutputStream3 = new DerOutputStream();
        derOutputStream3.putOID(pkcs5PBKDF2_OID);
        DerOutputStream derOutputStream4 = new DerOutputStream();
        derOutputStream4.putOctetString(this.salt);
        derOutputStream4.putInteger(this.iCount);
        if (this.keysize > 0) {
            derOutputStream4.putInteger(this.keysize / 8);
        }
        DerOutputStream derOutputStream5 = new DerOutputStream();
        derOutputStream5.putOID(this.kdfAlgo_OID);
        derOutputStream5.putNull();
        derOutputStream4.write((byte) 48, derOutputStream5);
        derOutputStream3.write((byte) 48, derOutputStream4);
        derOutputStream2.write((byte) 48, derOutputStream3);
        DerOutputStream derOutputStream6 = new DerOutputStream();
        derOutputStream6.putOID(this.cipherAlgo_OID);
        if (this.cipherParam != null && (this.cipherParam instanceof IvParameterSpec)) {
            derOutputStream6.putOctetString(((IvParameterSpec) this.cipherParam).getIV());
            derOutputStream2.write((byte) 48, derOutputStream6);
            derOutputStream.write((byte) 48, derOutputStream2);
            return derOutputStream.toByteArray();
        }
        throw new IOException("Wrong parameter type: IV expected");
    }

    @Override // java.security.AlgorithmParametersSpi
    protected byte[] engineGetEncoded(String str) throws IOException {
        return engineGetEncoded();
    }

    @Override // java.security.AlgorithmParametersSpi
    protected String engineToString() {
        return this.pbes2AlgorithmName;
    }

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/PBES2Parameters$HmacSHA1AndAES_128.class */
    public static final class HmacSHA1AndAES_128 extends PBES2Parameters {
        public HmacSHA1AndAES_128() throws NoSuchAlgorithmException {
            super("PBEWithHmacSHA1AndAES_128");
        }
    }

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/PBES2Parameters$HmacSHA224AndAES_128.class */
    public static final class HmacSHA224AndAES_128 extends PBES2Parameters {
        public HmacSHA224AndAES_128() throws NoSuchAlgorithmException {
            super("PBEWithHmacSHA224AndAES_128");
        }
    }

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/PBES2Parameters$HmacSHA256AndAES_128.class */
    public static final class HmacSHA256AndAES_128 extends PBES2Parameters {
        public HmacSHA256AndAES_128() throws NoSuchAlgorithmException {
            super("PBEWithHmacSHA256AndAES_128");
        }
    }

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/PBES2Parameters$HmacSHA384AndAES_128.class */
    public static final class HmacSHA384AndAES_128 extends PBES2Parameters {
        public HmacSHA384AndAES_128() throws NoSuchAlgorithmException {
            super("PBEWithHmacSHA384AndAES_128");
        }
    }

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/PBES2Parameters$HmacSHA512AndAES_128.class */
    public static final class HmacSHA512AndAES_128 extends PBES2Parameters {
        public HmacSHA512AndAES_128() throws NoSuchAlgorithmException {
            super("PBEWithHmacSHA512AndAES_128");
        }
    }

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/PBES2Parameters$HmacSHA1AndAES_256.class */
    public static final class HmacSHA1AndAES_256 extends PBES2Parameters {
        public HmacSHA1AndAES_256() throws NoSuchAlgorithmException {
            super("PBEWithHmacSHA1AndAES_256");
        }
    }

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/PBES2Parameters$HmacSHA224AndAES_256.class */
    public static final class HmacSHA224AndAES_256 extends PBES2Parameters {
        public HmacSHA224AndAES_256() throws NoSuchAlgorithmException {
            super("PBEWithHmacSHA224AndAES_256");
        }
    }

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/PBES2Parameters$HmacSHA256AndAES_256.class */
    public static final class HmacSHA256AndAES_256 extends PBES2Parameters {
        public HmacSHA256AndAES_256() throws NoSuchAlgorithmException {
            super("PBEWithHmacSHA256AndAES_256");
        }
    }

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/PBES2Parameters$HmacSHA384AndAES_256.class */
    public static final class HmacSHA384AndAES_256 extends PBES2Parameters {
        public HmacSHA384AndAES_256() throws NoSuchAlgorithmException {
            super("PBEWithHmacSHA384AndAES_256");
        }
    }

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/PBES2Parameters$HmacSHA512AndAES_256.class */
    public static final class HmacSHA512AndAES_256 extends PBES2Parameters {
        public HmacSHA512AndAES_256() throws NoSuchAlgorithmException {
            super("PBEWithHmacSHA512AndAES_256");
        }
    }
}
