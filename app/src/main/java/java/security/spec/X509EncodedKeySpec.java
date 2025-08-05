package java.security.spec;

import com.sun.org.apache.xml.internal.security.keys.content.x509.XMLX509Certificate;

/* loaded from: rt.jar:java/security/spec/X509EncodedKeySpec.class */
public class X509EncodedKeySpec extends EncodedKeySpec {
    public X509EncodedKeySpec(byte[] bArr) {
        super(bArr);
    }

    @Override // java.security.spec.EncodedKeySpec
    public byte[] getEncoded() {
        return super.getEncoded();
    }

    @Override // java.security.spec.EncodedKeySpec
    public final String getFormat() {
        return XMLX509Certificate.JCA_CERT_ID;
    }
}
