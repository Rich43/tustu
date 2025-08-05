package sun.security.x509;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import sun.security.util.DerOutputStream;

/* loaded from: rt.jar:sun/security/x509/DeltaCRLIndicatorExtension.class */
public class DeltaCRLIndicatorExtension extends CRLNumberExtension {
    public static final String NAME = "DeltaCRLIndicator";
    private static final String LABEL = "Base CRL Number";

    public DeltaCRLIndicatorExtension(int i2) throws IOException {
        super(PKIXExtensions.DeltaCRLIndicator_Id, true, BigInteger.valueOf(i2), NAME, LABEL);
    }

    public DeltaCRLIndicatorExtension(BigInteger bigInteger) throws IOException {
        super(PKIXExtensions.DeltaCRLIndicator_Id, true, bigInteger, NAME, LABEL);
    }

    public DeltaCRLIndicatorExtension(Boolean bool, Object obj) throws IOException {
        super(PKIXExtensions.DeltaCRLIndicator_Id, Boolean.valueOf(bool.booleanValue()), obj, NAME, LABEL);
    }

    @Override // sun.security.x509.CRLNumberExtension, sun.security.x509.Extension, java.security.cert.Extension, sun.security.x509.CertAttrSet
    public void encode(OutputStream outputStream) throws IOException {
        new DerOutputStream();
        super.encode(outputStream, PKIXExtensions.DeltaCRLIndicator_Id, true);
    }
}
