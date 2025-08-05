package sun.security.provider.certpath;

import java.io.IOException;
import java.security.cert.Extension;
import java.util.Collections;
import java.util.List;
import sun.misc.HexDumpEncoder;
import sun.security.util.Debug;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;
import sun.security.x509.PKIXExtensions;

/* loaded from: rt.jar:sun/security/provider/certpath/OCSPRequest.class */
class OCSPRequest {
    private static final Debug debug = Debug.getInstance("certpath");
    private static final boolean dump;
    private final List<CertId> certIds;
    private final List<Extension> extensions;
    private byte[] nonce;

    static {
        dump = debug != null && Debug.isOn("ocsp");
    }

    OCSPRequest(CertId certId) {
        this((List<CertId>) Collections.singletonList(certId));
    }

    OCSPRequest(List<CertId> list) {
        this.certIds = list;
        this.extensions = Collections.emptyList();
    }

    OCSPRequest(List<CertId> list, List<Extension> list2) {
        this.certIds = list;
        this.extensions = list2;
    }

    byte[] encodeBytes() throws IOException {
        DerOutputStream derOutputStream = new DerOutputStream();
        DerOutputStream derOutputStream2 = new DerOutputStream();
        for (CertId certId : this.certIds) {
            DerOutputStream derOutputStream3 = new DerOutputStream();
            certId.encode(derOutputStream3);
            derOutputStream2.write((byte) 48, derOutputStream3);
        }
        derOutputStream.write((byte) 48, derOutputStream2);
        if (!this.extensions.isEmpty()) {
            DerOutputStream derOutputStream4 = new DerOutputStream();
            for (Extension extension : this.extensions) {
                extension.encode(derOutputStream4);
                if (extension.getId().equals(PKIXExtensions.OCSPNonce_Id.toString())) {
                    this.nonce = extension.getValue();
                }
            }
            DerOutputStream derOutputStream5 = new DerOutputStream();
            derOutputStream5.write((byte) 48, derOutputStream4);
            derOutputStream.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 2), derOutputStream5);
        }
        DerOutputStream derOutputStream6 = new DerOutputStream();
        derOutputStream6.write((byte) 48, derOutputStream);
        DerOutputStream derOutputStream7 = new DerOutputStream();
        derOutputStream7.write((byte) 48, derOutputStream6);
        byte[] byteArray = derOutputStream7.toByteArray();
        if (dump) {
            debug.println("OCSPRequest bytes...\n\n" + new HexDumpEncoder().encode(byteArray) + "\n");
        }
        return byteArray;
    }

    List<CertId> getCertIds() {
        return this.certIds;
    }

    byte[] getNonce() {
        return this.nonce;
    }
}
