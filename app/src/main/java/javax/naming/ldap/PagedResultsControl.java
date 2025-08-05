package javax.naming.ldap;

import com.sun.jndi.ldap.BerEncoder;
import java.io.IOException;

/* loaded from: rt.jar:javax/naming/ldap/PagedResultsControl.class */
public final class PagedResultsControl extends BasicControl {
    public static final String OID = "1.2.840.113556.1.4.319";
    private static final byte[] EMPTY_COOKIE = new byte[0];
    private static final long serialVersionUID = 6684806685736844298L;

    public PagedResultsControl(int i2, boolean z2) throws IOException {
        super("1.2.840.113556.1.4.319", z2, null);
        this.value = setEncodedValue(i2, EMPTY_COOKIE);
    }

    public PagedResultsControl(int i2, byte[] bArr, boolean z2) throws IOException {
        super("1.2.840.113556.1.4.319", z2, null);
        this.value = setEncodedValue(i2, bArr == null ? EMPTY_COOKIE : bArr);
    }

    private byte[] setEncodedValue(int i2, byte[] bArr) throws IOException {
        BerEncoder berEncoder = new BerEncoder(10 + bArr.length);
        berEncoder.beginSeq(48);
        berEncoder.encodeInt(i2);
        berEncoder.encodeOctetString(bArr, 4);
        berEncoder.endSeq();
        return berEncoder.getTrimmedBuf();
    }
}
