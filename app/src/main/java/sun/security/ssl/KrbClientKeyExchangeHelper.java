package sun.security.ssl;

import java.io.IOException;
import java.security.AccessControlContext;
import java.security.Principal;

/* loaded from: jsse.jar:sun/security/ssl/KrbClientKeyExchangeHelper.class */
public interface KrbClientKeyExchangeHelper {
    void init(byte[] bArr, String str, AccessControlContext accessControlContext) throws IOException;

    void init(byte[] bArr, byte[] bArr2, Object obj, AccessControlContext accessControlContext) throws IOException;

    byte[] getEncodedTicket();

    byte[] getEncryptedPreMasterSecret();

    byte[] getPlainPreMasterSecret();

    Principal getPeerPrincipal();

    Principal getLocalPrincipal();
}
