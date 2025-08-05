package org.icepdf.core.pobjects.security;

import java.io.InputStream;
import java.util.HashMap;
import org.icepdf.core.pobjects.Reference;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/security/SecurityHandlerInterface.class */
public interface SecurityHandlerInterface {
    boolean isAuthorized(String str);

    boolean isUserAuthorized(String str);

    boolean isOwnerAuthorized(String str);

    byte[] encrypt(Reference reference, byte[] bArr, byte[] bArr2);

    byte[] decrypt(Reference reference, byte[] bArr, byte[] bArr2);

    InputStream getEncryptionInputStream(Reference reference, byte[] bArr, HashMap map, InputStream inputStream);

    byte[] getEncryptionKey();

    byte[] getDecryptionKey();

    String getHandlerName();

    Permissions getPermissions();

    void init();

    void dispose();
}
