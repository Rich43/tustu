package sun.security.pkcs;

import java.io.IOException;
import sun.misc.HexDumpEncoder;
import sun.security.util.DerValue;
import sun.security.x509.GeneralNames;
import sun.security.x509.SerialNumber;

/* compiled from: SigningCertificateInfo.java */
/* loaded from: rt.jar:sun/security/pkcs/ESSCertId.class */
class ESSCertId {
    private static volatile HexDumpEncoder hexDumper;
    private byte[] certHash;
    private GeneralNames issuer;
    private SerialNumber serialNumber;

    ESSCertId(DerValue derValue) throws IOException {
        this.certHash = derValue.data.getDerValue().toByteArray();
        if (derValue.data.available() > 0) {
            DerValue derValue2 = derValue.data.getDerValue();
            this.issuer = new GeneralNames(derValue2.data.getDerValue());
            this.serialNumber = new SerialNumber(derValue2.data.getDerValue());
        }
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("[\n\tCertificate hash (SHA-1):\n");
        if (hexDumper == null) {
            hexDumper = new HexDumpEncoder();
        }
        stringBuffer.append(hexDumper.encode(this.certHash));
        if (this.issuer != null && this.serialNumber != null) {
            stringBuffer.append("\n\tIssuer: " + ((Object) this.issuer) + "\n");
            stringBuffer.append("\t" + ((Object) this.serialNumber));
        }
        stringBuffer.append("\n]");
        return stringBuffer.toString();
    }
}
