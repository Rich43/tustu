package sun.security.krb5.internal;

import sun.security.krb5.KrbException;

/* loaded from: rt.jar:sun/security/krb5/internal/KrbApErrException.class */
public class KrbApErrException extends KrbException {
    private static final long serialVersionUID = 7545264413323118315L;

    public KrbApErrException(int i2) {
        super(i2);
    }

    public KrbApErrException(int i2, String str) {
        super(i2, str);
    }
}
