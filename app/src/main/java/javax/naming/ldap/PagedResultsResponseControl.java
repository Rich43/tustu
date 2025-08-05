package javax.naming.ldap;

import com.sun.jndi.ldap.BerDecoder;
import java.io.IOException;

/* loaded from: rt.jar:javax/naming/ldap/PagedResultsResponseControl.class */
public final class PagedResultsResponseControl extends BasicControl {
    public static final String OID = "1.2.840.113556.1.4.319";
    private static final long serialVersionUID = -8819778744844514666L;
    private int resultSize;
    private byte[] cookie;

    public PagedResultsResponseControl(String str, boolean z2, byte[] bArr) throws IOException {
        super(str, z2, bArr);
        BerDecoder berDecoder = new BerDecoder(bArr, 0, bArr.length);
        berDecoder.parseSeq(null);
        this.resultSize = berDecoder.parseInt();
        this.cookie = berDecoder.parseOctetString(4, null);
    }

    public int getResultSize() {
        return this.resultSize;
    }

    public byte[] getCookie() {
        if (this.cookie.length == 0) {
            return null;
        }
        return this.cookie;
    }
}
