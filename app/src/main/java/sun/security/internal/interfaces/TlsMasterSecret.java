package sun.security.internal.interfaces;

import javax.crypto.SecretKey;

@Deprecated
/* loaded from: jce.jar:sun/security/internal/interfaces/TlsMasterSecret.class */
public interface TlsMasterSecret extends SecretKey {
    public static final long serialVersionUID = -461748105810469773L;

    int getMajorVersion();

    int getMinorVersion();
}
