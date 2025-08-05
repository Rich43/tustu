package sun.security.krb5.internal.util;

import java.io.IOException;
import java.security.AccessController;
import net.lingala.zip4j.util.InternalZipConstants;
import sun.security.action.GetBooleanAction;
import sun.security.util.DerValue;

/* loaded from: rt.jar:sun/security/krb5/internal/util/KerberosString.class */
public final class KerberosString {
    public static final boolean MSNAME = ((Boolean) AccessController.doPrivileged(new GetBooleanAction("sun.security.krb5.msinterop.kstring"))).booleanValue();

    /* renamed from: s, reason: collision with root package name */
    private final String f13611s;

    public KerberosString(String str) {
        this.f13611s = str;
    }

    public KerberosString(DerValue derValue) throws IOException {
        if (derValue.tag != 27) {
            throw new IOException("KerberosString's tag is incorrect: " + ((int) derValue.tag));
        }
        this.f13611s = new String(derValue.getDataBytes(), MSNAME ? InternalZipConstants.CHARSET_UTF8 : "ASCII");
    }

    public String toString() {
        return this.f13611s;
    }

    public DerValue toDerValue() throws IOException {
        return new DerValue((byte) 27, this.f13611s.getBytes(MSNAME ? InternalZipConstants.CHARSET_UTF8 : "ASCII"));
    }
}
