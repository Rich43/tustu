package sun.security.krb5.internal;

import sun.security.krb5.KrbException;

/* loaded from: rt.jar:sun/security/krb5/internal/KrbErrException.class */
public class KrbErrException extends KrbException {
    private static final long serialVersionUID = 2186533836785448317L;

    public KrbErrException(int i2) {
        super(i2);
    }

    public KrbErrException(int i2, String str) {
        super(i2, str);
    }
}
