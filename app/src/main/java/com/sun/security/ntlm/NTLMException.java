package com.sun.security.ntlm;

import java.security.GeneralSecurityException;

/* loaded from: rt.jar:com/sun/security/ntlm/NTLMException.class */
public final class NTLMException extends GeneralSecurityException {
    private static final long serialVersionUID = -3298539507906689430L;
    public static final int PACKET_READ_ERROR = 1;
    public static final int NO_DOMAIN_INFO = 2;
    public static final int USER_UNKNOWN = 3;
    public static final int AUTH_FAILED = 4;
    public static final int BAD_VERSION = 5;
    public static final int PROTOCOL = 6;
    public static final int INVALID_INPUT = 7;
    private int errorCode;

    public NTLMException(int i2, String str) {
        super(str);
        this.errorCode = i2;
    }

    public int errorCode() {
        return this.errorCode;
    }
}
