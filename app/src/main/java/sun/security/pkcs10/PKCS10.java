package sun.security.pkcs10;

import java.io.IOException;
import java.io.PrintStream;
import java.math.BigInteger;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.ProviderException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.util.Arrays;
import java.util.Base64;
import sun.security.util.DerInputStream;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;
import sun.security.util.SignatureUtil;
import sun.security.x509.AlgorithmId;
import sun.security.x509.X500Name;
import sun.security.x509.X509Key;

/* loaded from: rt.jar:sun/security/pkcs10/PKCS10.class */
public class PKCS10 {
    private X500Name subject;
    private PublicKey subjectPublicKeyInfo;
    private String sigAlg;
    private PKCS10Attributes attributeSet;
    private byte[] encoded;

    public PKCS10(PublicKey publicKey) {
        this.subjectPublicKeyInfo = publicKey;
        this.attributeSet = new PKCS10Attributes();
    }

    public PKCS10(PublicKey publicKey, PKCS10Attributes pKCS10Attributes) {
        this.subjectPublicKeyInfo = publicKey;
        this.attributeSet = pKCS10Attributes;
    }

    public PKCS10(byte[] bArr) throws NoSuchAlgorithmException, SignatureException, IOException {
        this.encoded = bArr;
        DerValue[] sequence = new DerInputStream(bArr).getSequence(3);
        if (sequence.length != 3) {
            throw new IllegalArgumentException("not a PKCS #10 request");
        }
        byte[] byteArray = sequence[0].toByteArray();
        AlgorithmId algorithmId = AlgorithmId.parse(sequence[1]);
        byte[] bitString = sequence[2].getBitString();
        if (!sequence[0].data.getBigInteger().equals(BigInteger.ZERO)) {
            throw new IllegalArgumentException("not PKCS #10 v1");
        }
        this.subject = new X500Name(sequence[0].data);
        this.subjectPublicKeyInfo = X509Key.parse(sequence[0].data.getDerValue());
        if (sequence[0].data.available() != 0) {
            this.attributeSet = new PKCS10Attributes(sequence[0].data);
        } else {
            this.attributeSet = new PKCS10Attributes();
        }
        if (sequence[0].data.available() != 0) {
            throw new IllegalArgumentException("illegal PKCS #10 data");
        }
        try {
            this.sigAlg = algorithmId.getName();
            Signature signature = Signature.getInstance(this.sigAlg);
            SignatureUtil.initVerifyWithParam(signature, this.subjectPublicKeyInfo, SignatureUtil.getParamSpec(this.sigAlg, algorithmId.getParameters()));
            signature.update(byteArray);
            if (!signature.verify(bitString)) {
                throw new SignatureException("Invalid PKCS #10 signature");
            }
        } catch (InvalidAlgorithmParameterException e2) {
            throw new SignatureException("Invalid signature parameters", e2);
        } catch (InvalidKeyException e3) {
            throw new SignatureException("Invalid key");
        } catch (ProviderException e4) {
            throw new SignatureException("Error parsing signature parameters", e4.getCause());
        }
    }

    public void encodeAndSign(X500Name x500Name, Signature signature) throws SignatureException, IOException, CertificateException {
        AlgorithmId algorithmId;
        if (this.encoded != null) {
            throw new SignatureException("request is already signed");
        }
        this.subject = x500Name;
        DerOutputStream derOutputStream = new DerOutputStream();
        derOutputStream.putInteger(BigInteger.ZERO);
        x500Name.encode(derOutputStream);
        derOutputStream.write(this.subjectPublicKeyInfo.getEncoded());
        this.attributeSet.encode(derOutputStream);
        DerOutputStream derOutputStream2 = new DerOutputStream();
        derOutputStream2.write((byte) 48, derOutputStream);
        byte[] byteArray = derOutputStream2.toByteArray();
        signature.update(byteArray, 0, byteArray.length);
        byte[] bArrSign = signature.sign();
        this.sigAlg = signature.getAlgorithm();
        try {
            AlgorithmParameters parameters = signature.getParameters();
            if (parameters == null) {
                algorithmId = AlgorithmId.get(signature.getAlgorithm());
            } else {
                algorithmId = AlgorithmId.get(parameters);
            }
            algorithmId.encode(derOutputStream2);
            derOutputStream2.putBitString(bArrSign);
            DerOutputStream derOutputStream3 = new DerOutputStream();
            derOutputStream3.write((byte) 48, derOutputStream2);
            this.encoded = derOutputStream3.toByteArray();
        } catch (NoSuchAlgorithmException e2) {
            throw new SignatureException(e2);
        }
    }

    public X500Name getSubjectName() {
        return this.subject;
    }

    public PublicKey getSubjectPublicKeyInfo() {
        return this.subjectPublicKeyInfo;
    }

    public String getSigAlg() {
        return this.sigAlg;
    }

    public PKCS10Attributes getAttributes() {
        return this.attributeSet;
    }

    public byte[] getEncoded() {
        if (this.encoded != null) {
            return (byte[]) this.encoded.clone();
        }
        return null;
    }

    public void print(PrintStream printStream) throws SignatureException, IOException {
        if (this.encoded == null) {
            throw new SignatureException("Cert request was not signed");
        }
        printStream.println("-----BEGIN NEW CERTIFICATE REQUEST-----");
        printStream.println(Base64.getMimeEncoder(64, new byte[]{13, 10}).encodeToString(this.encoded));
        printStream.println("-----END NEW CERTIFICATE REQUEST-----");
    }

    public String toString() {
        return "[PKCS #10 certificate request:\n" + this.subjectPublicKeyInfo.toString() + " subject: <" + ((Object) this.subject) + ">\n attributes: " + this.attributeSet.toString() + "\n]";
    }

    public boolean equals(Object obj) {
        byte[] encoded;
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof PKCS10) || this.encoded == null || (encoded = ((PKCS10) obj).getEncoded()) == null) {
            return false;
        }
        return Arrays.equals(this.encoded, encoded);
    }

    public int hashCode() {
        int i2 = 0;
        if (this.encoded != null) {
            for (int i3 = 1; i3 < this.encoded.length; i3++) {
                i2 += this.encoded[i3] * i3;
            }
        }
        return i2;
    }
}
