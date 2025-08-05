package sun.security.pkcs;

import java.io.IOException;
import sun.security.util.DerValue;

/* loaded from: rt.jar:sun/security/pkcs/SigningCertificateInfo.class */
public class SigningCertificateInfo {
    private byte[] ber = null;
    private ESSCertId[] certId = null;

    public SigningCertificateInfo(byte[] bArr) throws IOException {
        parse(bArr);
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("[\n");
        for (int i2 = 0; i2 < this.certId.length; i2++) {
            stringBuffer.append(this.certId[i2].toString());
        }
        stringBuffer.append("\n]");
        return stringBuffer.toString();
    }

    public void parse(byte[] bArr) throws IOException {
        DerValue derValue = new DerValue(bArr);
        if (derValue.tag != 48) {
            throw new IOException("Bad encoding for signingCertificate");
        }
        DerValue[] sequence = derValue.data.getSequence(1);
        this.certId = new ESSCertId[sequence.length];
        for (int i2 = 0; i2 < sequence.length; i2++) {
            this.certId[i2] = new ESSCertId(sequence[i2]);
        }
        if (derValue.data.available() > 0) {
            for (int i3 = 0; i3 < derValue.data.getSequence(1).length; i3++) {
            }
        }
    }
}
