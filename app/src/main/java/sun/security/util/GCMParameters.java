package sun.security.util;

import java.io.IOException;
import java.security.AlgorithmParametersSpi;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidParameterSpecException;
import javax.crypto.spec.GCMParameterSpec;
import sun.misc.HexDumpEncoder;

/* loaded from: rt.jar:sun/security/util/GCMParameters.class */
public final class GCMParameters extends AlgorithmParametersSpi {
    private byte[] iv;
    private int tLen;

    @Override // java.security.AlgorithmParametersSpi
    protected void engineInit(AlgorithmParameterSpec algorithmParameterSpec) throws InvalidParameterSpecException {
        if (!(algorithmParameterSpec instanceof GCMParameterSpec)) {
            throw new InvalidParameterSpecException("Inappropriate parameter specification");
        }
        GCMParameterSpec gCMParameterSpec = (GCMParameterSpec) algorithmParameterSpec;
        this.tLen = gCMParameterSpec.getTLen() / 8;
        if (this.tLen < 12 || this.tLen > 16) {
            throw new InvalidParameterSpecException("GCM parameter parsing error: unsupported tag len: " + this.tLen);
        }
        this.iv = gCMParameterSpec.getIV();
    }

    @Override // java.security.AlgorithmParametersSpi
    protected void engineInit(byte[] bArr) throws IOException {
        int integer;
        DerValue derValue = new DerValue(bArr);
        if (derValue.tag == 48) {
            byte[] octetString = derValue.data.getOctetString();
            if (derValue.data.available() != 0) {
                integer = derValue.data.getInteger();
                if (integer < 12 || integer > 16) {
                    throw new IOException("GCM parameter parsing error: unsupported tag len: " + integer);
                }
                if (derValue.data.available() != 0) {
                    throw new IOException("GCM parameter parsing error: extra data");
                }
            } else {
                integer = 12;
            }
            this.iv = (byte[]) octetString.clone();
            this.tLen = integer;
            return;
        }
        throw new IOException("GCM parameter parsing error: no SEQ tag");
    }

    @Override // java.security.AlgorithmParametersSpi
    protected void engineInit(byte[] bArr, String str) throws IOException {
        engineInit(bArr);
    }

    @Override // java.security.AlgorithmParametersSpi
    protected <T extends AlgorithmParameterSpec> T engineGetParameterSpec(Class<T> cls) throws InvalidParameterSpecException {
        if (GCMParameterSpec.class.isAssignableFrom(cls)) {
            return cls.cast(new GCMParameterSpec(this.tLen * 8, this.iv));
        }
        throw new InvalidParameterSpecException("Inappropriate parameter specification");
    }

    @Override // java.security.AlgorithmParametersSpi
    protected byte[] engineGetEncoded() throws IOException {
        DerOutputStream derOutputStream = new DerOutputStream();
        DerOutputStream derOutputStream2 = new DerOutputStream();
        derOutputStream2.putOctetString(this.iv);
        if (this.tLen != 12) {
            derOutputStream2.putInteger(this.tLen);
        }
        derOutputStream.write((byte) 48, derOutputStream2);
        return derOutputStream.toByteArray();
    }

    @Override // java.security.AlgorithmParametersSpi
    protected byte[] engineGetEncoded(String str) throws IOException {
        return engineGetEncoded();
    }

    @Override // java.security.AlgorithmParametersSpi
    protected String engineToString() {
        String strLineSeparator = System.lineSeparator();
        StringBuilder sb = new StringBuilder(strLineSeparator + "    iv:" + strLineSeparator + "[" + new HexDumpEncoder().encodeBuffer(this.iv) + "]");
        sb.append(strLineSeparator + "tLen(bits):" + strLineSeparator + (this.tLen * 8) + strLineSeparator);
        return sb.toString();
    }
}
