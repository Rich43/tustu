package sun.security.pkcs12;

import java.io.IOException;
import java.security.AlgorithmParameters;
import java.security.NoSuchAlgorithmException;
import sun.security.pkcs.ParsingException;
import sun.security.util.DerInputStream;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;
import sun.security.x509.AlgorithmId;

/* loaded from: rt.jar:sun/security/pkcs12/MacData.class */
class MacData {
    private String digestAlgorithmName;
    private AlgorithmParameters digestAlgorithmParams;
    private byte[] digest;
    private byte[] macSalt;
    private int iterations;
    private byte[] encoded;

    MacData(DerInputStream derInputStream) throws IOException {
        this.encoded = null;
        DerValue[] sequence = derInputStream.getSequence(2);
        if (sequence.length < 2 || sequence.length > 3) {
            throw new ParsingException("Invalid length for MacData");
        }
        DerValue[] sequence2 = new DerInputStream(sequence[0].toByteArray()).getSequence(2);
        if (sequence2.length != 2) {
            throw new ParsingException("Invalid length for DigestInfo");
        }
        AlgorithmId algorithmId = AlgorithmId.parse(sequence2[0]);
        this.digestAlgorithmName = algorithmId.getName();
        this.digestAlgorithmParams = algorithmId.getParameters();
        this.digest = sequence2[1].getOctetString();
        this.macSalt = sequence[1].getOctetString();
        if (sequence.length > 2) {
            this.iterations = sequence[2].getInteger();
        } else {
            this.iterations = 1;
        }
    }

    MacData(String str, byte[] bArr, byte[] bArr2, int i2) throws NoSuchAlgorithmException {
        this.encoded = null;
        if (str == null) {
            throw new NullPointerException("the algName parameter must be non-null");
        }
        AlgorithmId algorithmId = AlgorithmId.get(str);
        this.digestAlgorithmName = algorithmId.getName();
        this.digestAlgorithmParams = algorithmId.getParameters();
        if (bArr == null) {
            throw new NullPointerException("the digest parameter must be non-null");
        }
        if (bArr.length == 0) {
            throw new IllegalArgumentException("the digest parameter must not be empty");
        }
        this.digest = (byte[]) bArr.clone();
        this.macSalt = bArr2;
        this.iterations = i2;
        this.encoded = null;
    }

    MacData(AlgorithmParameters algorithmParameters, byte[] bArr, byte[] bArr2, int i2) throws NoSuchAlgorithmException {
        this.encoded = null;
        if (algorithmParameters == null) {
            throw new NullPointerException("the algParams parameter must be non-null");
        }
        AlgorithmId algorithmId = AlgorithmId.get(algorithmParameters);
        this.digestAlgorithmName = algorithmId.getName();
        this.digestAlgorithmParams = algorithmId.getParameters();
        if (bArr == null) {
            throw new NullPointerException("the digest parameter must be non-null");
        }
        if (bArr.length == 0) {
            throw new IllegalArgumentException("the digest parameter must not be empty");
        }
        this.digest = (byte[]) bArr.clone();
        this.macSalt = bArr2;
        this.iterations = i2;
        this.encoded = null;
    }

    String getDigestAlgName() {
        return this.digestAlgorithmName;
    }

    byte[] getSalt() {
        return this.macSalt;
    }

    int getIterations() {
        return this.iterations;
    }

    byte[] getDigest() {
        return this.digest;
    }

    public byte[] getEncoded() throws NoSuchAlgorithmException, IOException {
        if (this.encoded != null) {
            return (byte[]) this.encoded.clone();
        }
        DerOutputStream derOutputStream = new DerOutputStream();
        DerOutputStream derOutputStream2 = new DerOutputStream();
        DerOutputStream derOutputStream3 = new DerOutputStream();
        AlgorithmId.get(this.digestAlgorithmName).encode(derOutputStream3);
        derOutputStream3.putOctetString(this.digest);
        derOutputStream2.write((byte) 48, derOutputStream3);
        derOutputStream2.putOctetString(this.macSalt);
        derOutputStream2.putInteger(this.iterations);
        derOutputStream.write((byte) 48, derOutputStream2);
        this.encoded = derOutputStream.toByteArray();
        return (byte[]) this.encoded.clone();
    }
}
