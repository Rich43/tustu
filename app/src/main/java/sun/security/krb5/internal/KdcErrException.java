package sun.security.krb5.internal;

import sun.security.krb5.KrbException;

/* loaded from: rt.jar:sun/security/krb5/internal/KdcErrException.class */
public class KdcErrException extends KrbException {
    private static final long serialVersionUID = -8788186031117310306L;

    public KdcErrException(int i2) {
        super(i2);
    }

    public KdcErrException(int i2, String str) {
        super(i2, str);
    }
}
