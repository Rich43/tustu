package sun.security.pkcs;

import com.sun.org.apache.xml.internal.security.keys.content.x509.XMLX509Certificate;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.ProviderException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.Timestamp;
import java.security.cert.CertPath;
import java.security.cert.CertPathValidatorException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import sun.misc.HexDumpEncoder;
import sun.security.timestamp.TimestampToken;
import sun.security.util.Debug;
import sun.security.util.DerEncoder;
import sun.security.util.DerInputStream;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;
import sun.security.util.DisabledAlgorithmConstraints;
import sun.security.util.JarConstraintsParameters;
import sun.security.util.ObjectIdentifier;
import sun.security.util.SignatureUtil;
import sun.security.x509.AlgorithmId;
import sun.security.x509.KeyUsageExtension;
import sun.security.x509.X500Name;

/* loaded from: rt.jar:sun/security/pkcs/SignerInfo.class */
public class SignerInfo implements DerEncoder {
    BigInteger version;
    X500Name issuerName;
    BigInteger certificateSerialNumber;
    AlgorithmId digestAlgorithmId;
    AlgorithmId digestEncryptionAlgorithmId;
    byte[] encryptedDigest;
    Timestamp timestamp;
    private boolean hasTimestamp;
    PKCS9Attributes authenticatedAttributes;
    PKCS9Attributes unauthenticatedAttributes;
    private Map<AlgorithmId, AlgorithmInfo> algorithms;
    private static final DisabledAlgorithmConstraints JAR_DISABLED_CHECK = DisabledAlgorithmConstraints.jarConstraints();
    private static final Debug debug = Debug.getInstance("jar");

    /* loaded from: rt.jar:sun/security/pkcs/SignerInfo$AlgorithmInfo.class */
    private class AlgorithmInfo {
        String field;
        boolean checkKey;

        private AlgorithmInfo(String str, boolean z2) {
            this.field = str;
            this.checkKey = z2;
        }

        String field() {
            return this.field;
        }

        boolean checkKey() {
            return this.checkKey;
        }
    }

    public SignerInfo(X500Name x500Name, BigInteger bigInteger, AlgorithmId algorithmId, AlgorithmId algorithmId2, byte[] bArr) {
        this.hasTimestamp = true;
        this.algorithms = new HashMap();
        this.version = BigInteger.ONE;
        this.issuerName = x500Name;
        this.certificateSerialNumber = bigInteger;
        this.digestAlgorithmId = algorithmId;
        this.digestEncryptionAlgorithmId = algorithmId2;
        this.encryptedDigest = bArr;
    }

    public SignerInfo(X500Name x500Name, BigInteger bigInteger, AlgorithmId algorithmId, PKCS9Attributes pKCS9Attributes, AlgorithmId algorithmId2, byte[] bArr, PKCS9Attributes pKCS9Attributes2) {
        this.hasTimestamp = true;
        this.algorithms = new HashMap();
        this.version = BigInteger.ONE;
        this.issuerName = x500Name;
        this.certificateSerialNumber = bigInteger;
        this.digestAlgorithmId = algorithmId;
        this.authenticatedAttributes = pKCS9Attributes;
        this.digestEncryptionAlgorithmId = algorithmId2;
        this.encryptedDigest = bArr;
        this.unauthenticatedAttributes = pKCS9Attributes2;
    }

    public SignerInfo(DerInputStream derInputStream) throws IOException {
        this(derInputStream, false);
    }

    public SignerInfo(DerInputStream derInputStream, boolean z2) throws IOException {
        this.hasTimestamp = true;
        this.algorithms = new HashMap();
        this.version = derInputStream.getBigInteger();
        DerValue[] sequence = derInputStream.getSequence(2);
        if (sequence.length != 2) {
            throw new ParsingException("Invalid length for IssuerAndSerialNumber");
        }
        this.issuerName = new X500Name(new DerValue((byte) 48, sequence[0].toByteArray()));
        this.certificateSerialNumber = sequence[1].getBigInteger();
        this.digestAlgorithmId = AlgorithmId.parse(derInputStream.getDerValue());
        if (z2) {
            derInputStream.getSet(0);
        } else if (((byte) derInputStream.peekByte()) == -96) {
            this.authenticatedAttributes = new PKCS9Attributes(derInputStream);
        }
        this.digestEncryptionAlgorithmId = AlgorithmId.parse(derInputStream.getDerValue());
        this.encryptedDigest = derInputStream.getOctetString();
        if (z2) {
            derInputStream.getSet(0);
        } else if (derInputStream.available() != 0 && ((byte) derInputStream.peekByte()) == -95) {
            this.unauthenticatedAttributes = new PKCS9Attributes(derInputStream, true);
        }
        if (derInputStream.available() != 0) {
            throw new ParsingException("extra data at the end");
        }
    }

    public void encode(DerOutputStream derOutputStream) throws IOException {
        derEncode(derOutputStream);
    }

    @Override // sun.security.util.DerEncoder
    public void derEncode(OutputStream outputStream) throws IOException {
        DerOutputStream derOutputStream = new DerOutputStream();
        derOutputStream.putInteger(this.version);
        DerOutputStream derOutputStream2 = new DerOutputStream();
        this.issuerName.encode(derOutputStream2);
        derOutputStream2.putInteger(this.certificateSerialNumber);
        derOutputStream.write((byte) 48, derOutputStream2);
        this.digestAlgorithmId.encode(derOutputStream);
        if (this.authenticatedAttributes != null) {
            this.authenticatedAttributes.encode((byte) -96, derOutputStream);
        }
        this.digestEncryptionAlgorithmId.encode(derOutputStream);
        derOutputStream.putOctetString(this.encryptedDigest);
        if (this.unauthenticatedAttributes != null) {
            this.unauthenticatedAttributes.encode((byte) -95, derOutputStream);
        }
        DerOutputStream derOutputStream3 = new DerOutputStream();
        derOutputStream3.write((byte) 48, derOutputStream);
        outputStream.write(derOutputStream3.toByteArray());
    }

    public X509Certificate getCertificate(PKCS7 pkcs7) throws IOException {
        return pkcs7.getCertificate(this.certificateSerialNumber, this.issuerName);
    }

    public ArrayList<X509Certificate> getCertificateChain(PKCS7 pkcs7) throws IOException {
        boolean z2;
        X509Certificate certificate = pkcs7.getCertificate(this.certificateSerialNumber, this.issuerName);
        if (certificate == null) {
            return null;
        }
        ArrayList<X509Certificate> arrayList = new ArrayList<>();
        arrayList.add(certificate);
        X509Certificate[] certificates = pkcs7.getCertificates();
        if (certificates == null || certificate.getSubjectDN().equals(certificate.getIssuerDN())) {
            return arrayList;
        }
        Principal issuerDN = certificate.getIssuerDN();
        int length = 0;
        do {
            z2 = false;
            int i2 = length;
            while (true) {
                if (i2 >= certificates.length) {
                    break;
                }
                if (issuerDN.equals(certificates[i2].getSubjectDN())) {
                    arrayList.add(certificates[i2]);
                    if (certificates[i2].getSubjectDN().equals(certificates[i2].getIssuerDN())) {
                        length = certificates.length;
                    } else {
                        issuerDN = certificates[i2].getIssuerDN();
                        X509Certificate x509Certificate = certificates[length];
                        certificates[length] = certificates[i2];
                        certificates[i2] = x509Certificate;
                        length++;
                    }
                    z2 = true;
                } else {
                    i2++;
                }
            }
        } while (z2);
        return arrayList;
    }

    SignerInfo verify(PKCS7 pkcs7, byte[] bArr) throws SignatureException, NoSuchAlgorithmException {
        byte[] bArr2;
        byte[] derEncoding;
        try {
            getTimestamp();
            ContentInfo contentInfo = pkcs7.getContentInfo();
            if (bArr == null) {
                bArr = contentInfo.getContentBytes();
            }
            String name = this.digestAlgorithmId.getName();
            this.algorithms.put(this.digestAlgorithmId, new AlgorithmInfo("SignerInfo digestAlgorithm field", false));
            if (this.authenticatedAttributes == null) {
                derEncoding = bArr;
            } else {
                ObjectIdentifier objectIdentifier = (ObjectIdentifier) this.authenticatedAttributes.getAttributeValue(PKCS9Attribute.CONTENT_TYPE_OID);
                if (objectIdentifier == null || !objectIdentifier.equals((Object) contentInfo.contentType) || (bArr2 = (byte[]) this.authenticatedAttributes.getAttributeValue(PKCS9Attribute.MESSAGE_DIGEST_OID)) == null || !MessageDigest.isEqual(bArr2, MessageDigest.getInstance(name).digest(bArr))) {
                    return null;
                }
                derEncoding = this.authenticatedAttributes.getDerEncoding();
            }
            String name2 = getDigestEncryptionAlgorithmId().getName();
            String encAlgFromSigAlg = AlgorithmId.getEncAlgFromSigAlg(name2);
            if (encAlgFromSigAlg != null) {
                name2 = encAlgFromSigAlg;
            }
            String strMakeSigAlg = AlgorithmId.makeSigAlg(name, name2);
            try {
                this.algorithms.put(new AlgorithmId(AlgorithmId.get(strMakeSigAlg).getOID(), this.digestEncryptionAlgorithmId.getParameters()), new AlgorithmInfo("SignerInfo digestEncryptionAlgorithm field", true));
            } catch (NoSuchAlgorithmException e2) {
            }
            X509Certificate certificate = getCertificate(pkcs7);
            if (certificate == null) {
                return null;
            }
            PublicKey publicKey = certificate.getPublicKey();
            if (certificate.hasUnsupportedCriticalExtension()) {
                throw new SignatureException("Certificate has unsupported critical extension(s)");
            }
            boolean[] keyUsage = certificate.getKeyUsage();
            if (keyUsage != null) {
                try {
                    KeyUsageExtension keyUsageExtension = new KeyUsageExtension(keyUsage);
                    boolean zBooleanValue = keyUsageExtension.get(KeyUsageExtension.DIGITAL_SIGNATURE).booleanValue();
                    boolean zBooleanValue2 = keyUsageExtension.get(KeyUsageExtension.NON_REPUDIATION).booleanValue();
                    if (!zBooleanValue && !zBooleanValue2) {
                        throw new SignatureException("Key usage restricted: cannot be used for digital signatures");
                    }
                } catch (IOException e3) {
                    throw new SignatureException("Failed to parse keyUsage extension");
                }
            }
            Signature signature = Signature.getInstance(strMakeSigAlg);
            try {
                SignatureUtil.initVerifyWithParam(signature, publicKey, SignatureUtil.getParamSpec(strMakeSigAlg, this.digestEncryptionAlgorithmId.getParameters()));
                signature.update(derEncoding);
                if (signature.verify(this.encryptedDigest)) {
                    return this;
                }
                return null;
            } catch (InvalidAlgorithmParameterException | InvalidKeyException | ProviderException e4) {
                throw new SignatureException(e4.getMessage(), e4);
            }
        } catch (IOException | CertificateException e5) {
            throw new SignatureException("Error verifying signature", e5);
        }
    }

    SignerInfo verify(PKCS7 pkcs7) throws NoSuchAlgorithmException, SignatureException {
        return verify(pkcs7, null);
    }

    public BigInteger getVersion() {
        return this.version;
    }

    public X500Name getIssuerName() {
        return this.issuerName;
    }

    public BigInteger getCertificateSerialNumber() {
        return this.certificateSerialNumber;
    }

    public AlgorithmId getDigestAlgorithmId() {
        return this.digestAlgorithmId;
    }

    public PKCS9Attributes getAuthenticatedAttributes() {
        return this.authenticatedAttributes;
    }

    public AlgorithmId getDigestEncryptionAlgorithmId() {
        return this.digestEncryptionAlgorithmId;
    }

    public byte[] getEncryptedDigest() {
        return this.encryptedDigest;
    }

    public PKCS9Attributes getUnauthenticatedAttributes() {
        return this.unauthenticatedAttributes;
    }

    public PKCS7 getTsToken() throws IOException {
        PKCS9Attribute attribute;
        if (this.unauthenticatedAttributes == null || (attribute = this.unauthenticatedAttributes.getAttribute(PKCS9Attribute.SIGNATURE_TIMESTAMP_TOKEN_OID)) == null) {
            return null;
        }
        return new PKCS7((byte[]) attribute.getValue());
    }

    public Timestamp getTimestamp() throws NoSuchAlgorithmException, SignatureException, IOException, CertificateException {
        if (this.timestamp != null || !this.hasTimestamp) {
            return this.timestamp;
        }
        PKCS7 tsToken = getTsToken();
        if (tsToken == null) {
            this.hasTimestamp = false;
            return null;
        }
        byte[] data = tsToken.getContentInfo().getData();
        SignerInfo[] signerInfoArrVerify = tsToken.verify(data);
        if (signerInfoArrVerify == null || signerInfoArrVerify.length == 0) {
            throw new SignatureException("Unable to verify timestamp");
        }
        CertPath certPathGenerateCertPath = CertificateFactory.getInstance(XMLX509Certificate.JCA_CERT_ID).generateCertPath(signerInfoArrVerify[0].getCertificateChain(tsToken));
        TimestampToken timestampToken = new TimestampToken(data);
        verifyTimestamp(timestampToken);
        this.algorithms.putAll(signerInfoArrVerify[0].algorithms);
        this.timestamp = new Timestamp(timestampToken.getDate(), certPathGenerateCertPath);
        return this.timestamp;
    }

    private void verifyTimestamp(TimestampToken timestampToken) throws NoSuchAlgorithmException, SignatureException {
        AlgorithmId hashAlgorithm = timestampToken.getHashAlgorithm();
        this.algorithms.put(hashAlgorithm, new AlgorithmInfo("TimestampToken digestAlgorithm field", false));
        if (!MessageDigest.isEqual(timestampToken.getHashedMessage(), MessageDigest.getInstance(hashAlgorithm.getName()).digest(this.encryptedDigest))) {
            throw new SignatureException("Signature timestamp (#" + ((Object) timestampToken.getSerialNumber()) + ") generated on " + ((Object) timestampToken.getDate()) + " is inapplicable");
        }
        if (debug != null) {
            debug.println();
            debug.println("Detected signature timestamp (#" + ((Object) timestampToken.getSerialNumber()) + ") generated on " + ((Object) timestampToken.getDate()));
            debug.println();
        }
    }

    public String toString() {
        HexDumpEncoder hexDumpEncoder = new HexDumpEncoder();
        String str = ((("Signer Info for (issuer): " + ((Object) this.issuerName) + "\n") + "\tversion: " + Debug.toHexString(this.version) + "\n") + "\tcertificateSerialNumber: " + Debug.toHexString(this.certificateSerialNumber) + "\n") + "\tdigestAlgorithmId: " + ((Object) this.digestAlgorithmId) + "\n";
        if (this.authenticatedAttributes != null) {
            str = str + "\tauthenticatedAttributes: " + ((Object) this.authenticatedAttributes) + "\n";
        }
        String str2 = (str + "\tdigestEncryptionAlgorithmId: " + ((Object) this.digestEncryptionAlgorithmId) + "\n") + "\tencryptedDigest: \n" + hexDumpEncoder.encodeBuffer(this.encryptedDigest) + "\n";
        if (this.unauthenticatedAttributes != null) {
            str2 = str2 + "\tunauthenticatedAttributes: " + ((Object) this.unauthenticatedAttributes) + "\n";
        }
        return str2;
    }

    public static Set<String> verifyAlgorithms(SignerInfo[] signerInfoArr, JarConstraintsParameters jarConstraintsParameters, String str) throws SignatureException {
        HashMap map = new HashMap();
        for (SignerInfo signerInfo : signerInfoArr) {
            map.putAll(signerInfo.algorithms);
        }
        HashSet hashSet = new HashSet();
        try {
            Iterator it = map.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                AlgorithmInfo algorithmInfo = (AlgorithmInfo) entry.getValue();
                jarConstraintsParameters.setExtendedExceptionMsg(str, algorithmInfo.field());
                AlgorithmId algorithmId = (AlgorithmId) entry.getKey();
                JAR_DISABLED_CHECK.permits(algorithmId.getName(), algorithmId.getParameters(), jarConstraintsParameters, algorithmInfo.checkKey());
                hashSet.add(algorithmId.getName());
            }
            return hashSet;
        } catch (CertPathValidatorException e2) {
            throw new SignatureException(e2);
        }
    }
}
