package sun.security.pkcs;

import com.sun.org.apache.xml.internal.security.keys.content.x509.XMLX509Certificate;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.SecureRandom;
import java.security.SignatureException;
import java.security.cert.CRLException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Vector;
import sun.security.timestamp.HttpTimestamper;
import sun.security.timestamp.TSRequest;
import sun.security.timestamp.TSResponse;
import sun.security.timestamp.TimestampToken;
import sun.security.timestamp.Timestamper;
import sun.security.util.Debug;
import sun.security.util.DerEncoder;
import sun.security.util.DerInputStream;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;
import sun.security.util.ObjectIdentifier;
import sun.security.x509.AlgorithmId;
import sun.security.x509.X500Name;
import sun.security.x509.X509CRLImpl;
import sun.security.x509.X509CertImpl;
import sun.security.x509.X509CertInfo;

/* loaded from: rt.jar:sun/security/pkcs/PKCS7.class */
public class PKCS7 {
    private ObjectIdentifier contentType;
    private BigInteger version;
    private AlgorithmId[] digestAlgorithmIds;
    private ContentInfo contentInfo;
    private X509Certificate[] certificates;
    private X509CRL[] crls;
    private SignerInfo[] signerInfos;
    private boolean oldStyle;
    private Principal[] certIssuerNames;
    private static final String KP_TIMESTAMPING_OID = "1.3.6.1.5.5.7.3.8";
    private static final String EXTENDED_KEY_USAGE_OID = "2.5.29.37";

    /* loaded from: rt.jar:sun/security/pkcs/PKCS7$SecureRandomHolder.class */
    private static class SecureRandomHolder {
        static final SecureRandom RANDOM;

        private SecureRandomHolder() {
        }

        static {
            SecureRandom secureRandom = null;
            try {
                secureRandom = SecureRandom.getInstance("SHA1PRNG");
            } catch (NoSuchAlgorithmException e2) {
            }
            RANDOM = secureRandom;
        }
    }

    public PKCS7(InputStream inputStream) throws IOException {
        this.version = null;
        this.digestAlgorithmIds = null;
        this.contentInfo = null;
        this.certificates = null;
        this.crls = null;
        this.signerInfos = null;
        this.oldStyle = false;
        DataInputStream dataInputStream = new DataInputStream(inputStream);
        byte[] bArr = new byte[dataInputStream.available()];
        dataInputStream.readFully(bArr);
        parse(new DerInputStream(bArr));
    }

    public PKCS7(DerInputStream derInputStream) throws ParsingException {
        this.version = null;
        this.digestAlgorithmIds = null;
        this.contentInfo = null;
        this.certificates = null;
        this.crls = null;
        this.signerInfos = null;
        this.oldStyle = false;
        parse(derInputStream);
    }

    public PKCS7(byte[] bArr) throws ParsingException {
        this.version = null;
        this.digestAlgorithmIds = null;
        this.contentInfo = null;
        this.certificates = null;
        this.crls = null;
        this.signerInfos = null;
        this.oldStyle = false;
        try {
            parse(new DerInputStream(bArr));
        } catch (IOException e2) {
            ParsingException parsingException = new ParsingException("Unable to parse the encoded bytes");
            parsingException.initCause(e2);
            throw parsingException;
        }
    }

    private void parse(DerInputStream derInputStream) throws ParsingException {
        try {
            derInputStream.mark(derInputStream.available());
            parse(derInputStream, false);
        } catch (IOException e2) {
            try {
                derInputStream.reset();
                parse(derInputStream, true);
                this.oldStyle = true;
            } catch (IOException e3) {
                ParsingException parsingException = new ParsingException(e3.getMessage());
                parsingException.initCause(e2);
                parsingException.addSuppressed(e3);
                throw parsingException;
            }
        }
    }

    private void parse(DerInputStream derInputStream, boolean z2) throws IOException {
        this.contentInfo = new ContentInfo(derInputStream, z2);
        this.contentType = this.contentInfo.contentType;
        DerValue content = this.contentInfo.getContent();
        if (this.contentType.equals((Object) ContentInfo.SIGNED_DATA_OID)) {
            parseSignedData(content);
        } else if (this.contentType.equals((Object) ContentInfo.OLD_SIGNED_DATA_OID)) {
            parseOldSignedData(content);
        } else {
            if (this.contentType.equals((Object) ContentInfo.NETSCAPE_CERT_SEQUENCE_OID)) {
                parseNetscapeCertChain(content);
                return;
            }
            throw new ParsingException("content type " + ((Object) this.contentType) + " not supported.");
        }
    }

    public PKCS7(AlgorithmId[] algorithmIdArr, ContentInfo contentInfo, X509Certificate[] x509CertificateArr, X509CRL[] x509crlArr, SignerInfo[] signerInfoArr) {
        this.version = null;
        this.digestAlgorithmIds = null;
        this.contentInfo = null;
        this.certificates = null;
        this.crls = null;
        this.signerInfos = null;
        this.oldStyle = false;
        this.version = BigInteger.ONE;
        this.digestAlgorithmIds = algorithmIdArr;
        this.contentInfo = contentInfo;
        this.certificates = x509CertificateArr;
        this.crls = x509crlArr;
        this.signerInfos = signerInfoArr;
    }

    public PKCS7(AlgorithmId[] algorithmIdArr, ContentInfo contentInfo, X509Certificate[] x509CertificateArr, SignerInfo[] signerInfoArr) {
        this(algorithmIdArr, contentInfo, x509CertificateArr, null, signerInfoArr);
    }

    private void parseNetscapeCertChain(DerValue derValue) throws IOException {
        DerValue[] sequence = new DerInputStream(derValue.toByteArray()).getSequence(2);
        this.certificates = new X509Certificate[sequence.length];
        CertificateFactory certificateFactory = null;
        try {
            certificateFactory = CertificateFactory.getInstance(XMLX509Certificate.JCA_CERT_ID);
        } catch (CertificateException e2) {
        }
        for (int i2 = 0; i2 < sequence.length; i2++) {
            AutoCloseable autoCloseable = null;
            if (certificateFactory == null) {
                try {
                    try {
                        try {
                            this.certificates[i2] = new X509CertImpl(sequence[i2]);
                        } catch (IOException e3) {
                            ParsingException parsingException = new ParsingException(e3.getMessage());
                            parsingException.initCause(e3);
                            throw parsingException;
                        }
                    } catch (CertificateException e4) {
                        ParsingException parsingException2 = new ParsingException(e4.getMessage());
                        parsingException2.initCause(e4);
                        throw parsingException2;
                    }
                } catch (Throwable th) {
                    if (0 != 0) {
                        autoCloseable.close();
                    }
                    throw th;
                }
            } else {
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(sequence[i2].toByteArray());
                this.certificates[i2] = (X509Certificate) certificateFactory.generateCertificate(byteArrayInputStream);
                byteArrayInputStream.close();
                autoCloseable = null;
            }
            if (autoCloseable != null) {
                autoCloseable.close();
            }
        }
    }

    private void parseSignedData(DerValue derValue) throws IOException {
        AutoCloseable autoCloseable;
        AutoCloseable autoCloseable2;
        DerInputStream derInputStream = derValue.toDerInputStream();
        this.version = derInputStream.getBigInteger();
        DerValue[] set = derInputStream.getSet(1);
        int length = set.length;
        this.digestAlgorithmIds = new AlgorithmId[length];
        for (int i2 = 0; i2 < length; i2++) {
            try {
                this.digestAlgorithmIds[i2] = AlgorithmId.parse(set[i2]);
            } catch (IOException e2) {
                ParsingException parsingException = new ParsingException("Error parsing digest AlgorithmId IDs: " + e2.getMessage());
                parsingException.initCause(e2);
                throw parsingException;
            }
        }
        this.contentInfo = new ContentInfo(derInputStream);
        CertificateFactory certificateFactory = null;
        try {
            certificateFactory = CertificateFactory.getInstance(XMLX509Certificate.JCA_CERT_ID);
        } catch (CertificateException e3) {
        }
        if (((byte) derInputStream.peekByte()) == -96) {
            DerValue[] set2 = derInputStream.getSet(2, true);
            int length2 = set2.length;
            this.certificates = new X509Certificate[length2];
            int i3 = 0;
            for (int i4 = 0; i4 < length2; i4++) {
                autoCloseable = null;
                try {
                    try {
                        if (set2[i4].getTag() == 48) {
                            if (certificateFactory == null) {
                                this.certificates[i3] = new X509CertImpl(set2[i4]);
                            } else {
                                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(set2[i4].toByteArray());
                                this.certificates[i3] = (X509Certificate) certificateFactory.generateCertificate(byteArrayInputStream);
                                byteArrayInputStream.close();
                                autoCloseable2 = null;
                            }
                            i3++;
                        }
                        if (autoCloseable2 != null) {
                            autoCloseable2.close();
                        }
                    } catch (IOException e4) {
                        ParsingException parsingException2 = new ParsingException(e4.getMessage());
                        parsingException2.initCause(e4);
                        throw parsingException2;
                    } catch (CertificateException e5) {
                        ParsingException parsingException3 = new ParsingException(e5.getMessage());
                        parsingException3.initCause(e5);
                        throw parsingException3;
                    }
                } finally {
                }
            }
            if (i3 != length2) {
                this.certificates = (X509Certificate[]) Arrays.copyOf(this.certificates, i3);
            }
        }
        if (((byte) derInputStream.peekByte()) == -95) {
            DerValue[] set3 = derInputStream.getSet(1, true);
            int length3 = set3.length;
            this.crls = new X509CRL[length3];
            for (int i5 = 0; i5 < length3; i5++) {
                autoCloseable = null;
                if (certificateFactory == null) {
                    try {
                        try {
                            this.crls[i5] = new X509CRLImpl(set3[i5]);
                        } catch (CRLException e6) {
                            ParsingException parsingException4 = new ParsingException(e6.getMessage());
                            parsingException4.initCause(e6);
                            throw parsingException4;
                        }
                    } finally {
                    }
                } else {
                    ByteArrayInputStream byteArrayInputStream2 = new ByteArrayInputStream(set3[i5].toByteArray());
                    this.crls[i5] = (X509CRL) certificateFactory.generateCRL(byteArrayInputStream2);
                    byteArrayInputStream2.close();
                    autoCloseable = null;
                }
                if (autoCloseable != null) {
                    autoCloseable.close();
                }
            }
        }
        DerValue[] set4 = derInputStream.getSet(1);
        int length4 = set4.length;
        this.signerInfos = new SignerInfo[length4];
        for (int i6 = 0; i6 < length4; i6++) {
            this.signerInfos[i6] = new SignerInfo(set4[i6].toDerInputStream());
        }
    }

    private void parseOldSignedData(DerValue derValue) throws IOException {
        DerInputStream derInputStream = derValue.toDerInputStream();
        this.version = derInputStream.getBigInteger();
        DerValue[] set = derInputStream.getSet(1);
        int length = set.length;
        this.digestAlgorithmIds = new AlgorithmId[length];
        for (int i2 = 0; i2 < length; i2++) {
            try {
                this.digestAlgorithmIds[i2] = AlgorithmId.parse(set[i2]);
            } catch (IOException e2) {
                throw new ParsingException("Error parsing digest AlgorithmId IDs");
            }
        }
        this.contentInfo = new ContentInfo(derInputStream, true);
        CertificateFactory certificateFactory = null;
        try {
            certificateFactory = CertificateFactory.getInstance(XMLX509Certificate.JCA_CERT_ID);
        } catch (CertificateException e3) {
        }
        DerValue[] set2 = derInputStream.getSet(2);
        int length2 = set2.length;
        this.certificates = new X509Certificate[length2];
        for (int i3 = 0; i3 < length2; i3++) {
            AutoCloseable autoCloseable = null;
            if (certificateFactory == null) {
                try {
                    try {
                        try {
                            this.certificates[i3] = new X509CertImpl(set2[i3]);
                        } catch (CertificateException e4) {
                            ParsingException parsingException = new ParsingException(e4.getMessage());
                            parsingException.initCause(e4);
                            throw parsingException;
                        }
                    } catch (IOException e5) {
                        ParsingException parsingException2 = new ParsingException(e5.getMessage());
                        parsingException2.initCause(e5);
                        throw parsingException2;
                    }
                } catch (Throwable th) {
                    if (0 != 0) {
                        autoCloseable.close();
                    }
                    throw th;
                }
            } else {
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(set2[i3].toByteArray());
                this.certificates[i3] = (X509Certificate) certificateFactory.generateCertificate(byteArrayInputStream);
                byteArrayInputStream.close();
                autoCloseable = null;
            }
            if (autoCloseable != null) {
                autoCloseable.close();
            }
        }
        derInputStream.getSet(0);
        DerValue[] set3 = derInputStream.getSet(1);
        int length3 = set3.length;
        this.signerInfos = new SignerInfo[length3];
        for (int i4 = 0; i4 < length3; i4++) {
            this.signerInfos[i4] = new SignerInfo(set3[i4].toDerInputStream(), true);
        }
    }

    public void encodeSignedData(OutputStream outputStream) throws IOException {
        DerOutputStream derOutputStream = new DerOutputStream();
        encodeSignedData(derOutputStream);
        outputStream.write(derOutputStream.toByteArray());
    }

    public void encodeSignedData(DerOutputStream derOutputStream) throws IOException {
        DerOutputStream derOutputStream2 = new DerOutputStream();
        derOutputStream2.putInteger(this.version);
        derOutputStream2.putOrderedSetOf((byte) 49, this.digestAlgorithmIds);
        this.contentInfo.encode(derOutputStream2);
        if (this.certificates != null && this.certificates.length != 0) {
            X509CertImpl[] x509CertImplArr = new X509CertImpl[this.certificates.length];
            for (int i2 = 0; i2 < this.certificates.length; i2++) {
                if (this.certificates[i2] instanceof X509CertImpl) {
                    x509CertImplArr[i2] = (X509CertImpl) this.certificates[i2];
                } else {
                    try {
                        x509CertImplArr[i2] = new X509CertImpl(this.certificates[i2].getEncoded());
                    } catch (CertificateException e2) {
                        throw new IOException(e2);
                    }
                }
            }
            derOutputStream2.putOrderedSetOf((byte) -96, x509CertImplArr);
        }
        if (this.crls != null && this.crls.length != 0) {
            HashSet hashSet = new HashSet(this.crls.length);
            for (X509CRL x509crl : this.crls) {
                if (x509crl instanceof X509CRLImpl) {
                    hashSet.add((X509CRLImpl) x509crl);
                } else {
                    try {
                        hashSet.add(new X509CRLImpl(x509crl.getEncoded()));
                    } catch (CRLException e3) {
                        throw new IOException(e3);
                    }
                }
            }
            derOutputStream2.putOrderedSetOf((byte) -95, (DerEncoder[]) hashSet.toArray(new X509CRLImpl[hashSet.size()]));
        }
        derOutputStream2.putOrderedSetOf((byte) 49, this.signerInfos);
        new ContentInfo(ContentInfo.SIGNED_DATA_OID, new DerValue((byte) 48, derOutputStream2.toByteArray())).encode(derOutputStream);
    }

    public SignerInfo verify(SignerInfo signerInfo, byte[] bArr) throws NoSuchAlgorithmException, SignatureException {
        return signerInfo.verify(this, bArr);
    }

    public SignerInfo[] verify(byte[] bArr) throws NoSuchAlgorithmException, SignatureException {
        Vector vector = new Vector();
        for (int i2 = 0; i2 < this.signerInfos.length; i2++) {
            SignerInfo signerInfoVerify = verify(this.signerInfos[i2], bArr);
            if (signerInfoVerify != null) {
                vector.addElement(signerInfoVerify);
            }
        }
        if (!vector.isEmpty()) {
            SignerInfo[] signerInfoArr = new SignerInfo[vector.size()];
            vector.copyInto(signerInfoArr);
            return signerInfoArr;
        }
        return null;
    }

    public SignerInfo[] verify() throws NoSuchAlgorithmException, SignatureException {
        return verify(null);
    }

    public BigInteger getVersion() {
        return this.version;
    }

    public AlgorithmId[] getDigestAlgorithmIds() {
        return this.digestAlgorithmIds;
    }

    public ContentInfo getContentInfo() {
        return this.contentInfo;
    }

    public X509Certificate[] getCertificates() {
        if (this.certificates != null) {
            return (X509Certificate[]) this.certificates.clone();
        }
        return null;
    }

    public X509CRL[] getCRLs() {
        if (this.crls != null) {
            return (X509CRL[]) this.crls.clone();
        }
        return null;
    }

    public SignerInfo[] getSignerInfos() {
        return this.signerInfos;
    }

    public X509Certificate getCertificate(BigInteger bigInteger, X500Name x500Name) {
        if (this.certificates != null) {
            if (this.certIssuerNames == null) {
                populateCertIssuerNames();
            }
            for (int i2 = 0; i2 < this.certificates.length; i2++) {
                X509Certificate x509Certificate = this.certificates[i2];
                if (bigInteger.equals(x509Certificate.getSerialNumber()) && x500Name.equals(this.certIssuerNames[i2])) {
                    return x509Certificate;
                }
            }
            return null;
        }
        return null;
    }

    private void populateCertIssuerNames() {
        if (this.certificates == null) {
            return;
        }
        this.certIssuerNames = new Principal[this.certificates.length];
        for (int i2 = 0; i2 < this.certificates.length; i2++) {
            X509Certificate x509Certificate = this.certificates[i2];
            Principal issuerDN = x509Certificate.getIssuerDN();
            if (!(issuerDN instanceof X500Name)) {
                try {
                    issuerDN = (Principal) new X509CertInfo(x509Certificate.getTBSCertificate()).get("issuer.dname");
                } catch (Exception e2) {
                }
            }
            this.certIssuerNames[i2] = issuerDN;
        }
    }

    public String toString() {
        String str = "" + ((Object) this.contentInfo) + "\n";
        if (this.version != null) {
            str = str + "PKCS7 :: version: " + Debug.toHexString(this.version) + "\n";
        }
        if (this.digestAlgorithmIds != null) {
            str = str + "PKCS7 :: digest AlgorithmIds: \n";
            for (int i2 = 0; i2 < this.digestAlgorithmIds.length; i2++) {
                str = str + "\t" + ((Object) this.digestAlgorithmIds[i2]) + "\n";
            }
        }
        if (this.certificates != null) {
            str = str + "PKCS7 :: certificates: \n";
            for (int i3 = 0; i3 < this.certificates.length; i3++) {
                str = str + "\t" + i3 + ".   " + ((Object) this.certificates[i3]) + "\n";
            }
        }
        if (this.crls != null) {
            str = str + "PKCS7 :: crls: \n";
            for (int i4 = 0; i4 < this.crls.length; i4++) {
                str = str + "\t" + i4 + ".   " + ((Object) this.crls[i4]) + "\n";
            }
        }
        if (this.signerInfos != null) {
            str = str + "PKCS7 :: signer infos: \n";
            for (int i5 = 0; i5 < this.signerInfos.length; i5++) {
                str = str + "\t" + i5 + ".  " + ((Object) this.signerInfos[i5]) + "\n";
            }
        }
        return str;
    }

    public boolean isOldStyle() {
        return this.oldStyle;
    }

    public static byte[] generateSignedData(byte[] bArr, X509Certificate[] x509CertificateArr, byte[] bArr2, String str, URI uri, String str2, String str3) throws NoSuchAlgorithmException, IOException, CertificateException {
        PKCS9Attributes pKCS9Attributes = null;
        if (uri != null) {
            pKCS9Attributes = new PKCS9Attributes(new PKCS9Attribute[]{new PKCS9Attribute(PKCS9Attribute.SIGNATURE_TIMESTAMP_TOKEN_OID, generateTimestampToken(new HttpTimestamper(uri), str2, str3, bArr))});
        }
        SignerInfo signerInfo = new SignerInfo(X500Name.asX500Name(x509CertificateArr[0].getIssuerX500Principal()), x509CertificateArr[0].getSerialNumber(), AlgorithmId.get(AlgorithmId.getDigAlgFromSigAlg(str)), null, AlgorithmId.get(AlgorithmId.getEncAlgFromSigAlg(str)), bArr, pKCS9Attributes);
        PKCS7 pkcs7 = new PKCS7(new AlgorithmId[]{signerInfo.getDigestAlgorithmId()}, bArr2 == null ? new ContentInfo(ContentInfo.DATA_OID, (DerValue) null) : new ContentInfo(bArr2), x509CertificateArr, new SignerInfo[]{signerInfo});
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        pkcs7.encodeSignedData(byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private static byte[] generateTimestampToken(Timestamper timestamper, String str, String str2, byte[] bArr) throws IOException, CertificateException {
        try {
            TSRequest tSRequest = new TSRequest(str, bArr, MessageDigest.getInstance(str2));
            BigInteger bigInteger = null;
            if (SecureRandomHolder.RANDOM != null) {
                bigInteger = new BigInteger(64, SecureRandomHolder.RANDOM);
                tSRequest.setNonce(bigInteger);
            }
            tSRequest.requestCertificate(true);
            TSResponse tSResponseGenerateTimestamp = timestamper.generateTimestamp(tSRequest);
            int statusCode = tSResponseGenerateTimestamp.getStatusCode();
            if (statusCode != 0 && statusCode != 1) {
                throw new IOException("Error generating timestamp: " + tSResponseGenerateTimestamp.getStatusCodeAsText() + " " + tSResponseGenerateTimestamp.getFailureCodeAsText());
            }
            if (str != null && !str.equals(tSResponseGenerateTimestamp.getTimestampToken().getPolicyID())) {
                throw new IOException("TSAPolicyID changed in timestamp token");
            }
            PKCS7 token = tSResponseGenerateTimestamp.getToken();
            TimestampToken timestampToken = tSResponseGenerateTimestamp.getTimestampToken();
            try {
                if (!timestampToken.getHashAlgorithm().equals(AlgorithmId.get(str2))) {
                    throw new IOException("Digest algorithm not " + str2 + " in timestamp token");
                }
                if (!MessageDigest.isEqual(timestampToken.getHashedMessage(), tSRequest.getHashedMessage())) {
                    throw new IOException("Digest octets changed in timestamp token");
                }
                BigInteger nonce = timestampToken.getNonce();
                if (nonce == null && bigInteger != null) {
                    throw new IOException("Nonce missing in timestamp token");
                }
                if (nonce != null && !nonce.equals(bigInteger)) {
                    throw new IOException("Nonce changed in timestamp token");
                }
                for (SignerInfo signerInfo : token.getSignerInfos()) {
                    X509Certificate certificate = signerInfo.getCertificate(token);
                    if (certificate == null) {
                        throw new CertificateException("Certificate not included in timestamp token");
                    }
                    if (!certificate.getCriticalExtensionOIDs().contains(EXTENDED_KEY_USAGE_OID)) {
                        throw new CertificateException("Certificate is not valid for timestamping");
                    }
                    List<String> extendedKeyUsage = certificate.getExtendedKeyUsage();
                    if (extendedKeyUsage == null || !extendedKeyUsage.contains(KP_TIMESTAMPING_OID)) {
                        throw new CertificateException("Certificate is not valid for timestamping");
                    }
                }
                return tSResponseGenerateTimestamp.getEncodedToken();
            } catch (NoSuchAlgorithmException e2) {
                throw new IllegalArgumentException();
            }
        } catch (NoSuchAlgorithmException e3) {
            throw new IllegalArgumentException(e3);
        }
    }
}
