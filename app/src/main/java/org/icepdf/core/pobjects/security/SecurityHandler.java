package org.icepdf.core.pobjects.security;

import java.io.InputStream;
import java.util.HashMap;
import org.icepdf.core.pobjects.Reference;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/security/SecurityHandler.class */
public abstract class SecurityHandler implements SecurityHandlerInterface {
    protected EncryptionDictionary encryptionDictionary;
    protected String handlerName = null;
    protected Permissions permissions = null;

    @Override // org.icepdf.core.pobjects.security.SecurityHandlerInterface
    public abstract boolean isAuthorized(String str);

    @Override // org.icepdf.core.pobjects.security.SecurityHandlerInterface
    public abstract boolean isUserAuthorized(String str);

    @Override // org.icepdf.core.pobjects.security.SecurityHandlerInterface
    public abstract boolean isOwnerAuthorized(String str);

    @Override // org.icepdf.core.pobjects.security.SecurityHandlerInterface
    public abstract byte[] encrypt(Reference reference, byte[] bArr, byte[] bArr2);

    @Override // org.icepdf.core.pobjects.security.SecurityHandlerInterface
    public abstract byte[] decrypt(Reference reference, byte[] bArr, byte[] bArr2);

    @Override // org.icepdf.core.pobjects.security.SecurityHandlerInterface
    public abstract InputStream getEncryptionInputStream(Reference reference, byte[] bArr, HashMap map, InputStream inputStream);

    @Override // org.icepdf.core.pobjects.security.SecurityHandlerInterface
    public abstract byte[] getEncryptionKey();

    @Override // org.icepdf.core.pobjects.security.SecurityHandlerInterface
    public abstract byte[] getDecryptionKey();

    @Override // org.icepdf.core.pobjects.security.SecurityHandlerInterface
    public abstract String getHandlerName();

    @Override // org.icepdf.core.pobjects.security.SecurityHandlerInterface
    public abstract Permissions getPermissions();

    @Override // org.icepdf.core.pobjects.security.SecurityHandlerInterface
    public abstract void init();

    @Override // org.icepdf.core.pobjects.security.SecurityHandlerInterface
    public abstract void dispose();

    public SecurityHandler(EncryptionDictionary encryptionDictionary) {
        this.encryptionDictionary = null;
        this.encryptionDictionary = encryptionDictionary;
    }
}
