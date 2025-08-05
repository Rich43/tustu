package org.icepdf.core.pobjects.security;

import java.io.InputStream;
import java.util.HashMap;
import org.icepdf.core.pobjects.Name;
import org.icepdf.core.pobjects.Reference;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/security/StandardSecurityHandler.class */
public class StandardSecurityHandler extends SecurityHandler {
    public static final Name NAME_KEY = new Name("Name");
    public static final Name IDENTITY_KEY = new Name("Identity");
    private StandardEncryption standardEncryption;
    private byte[] encryptionKey;
    private boolean initiated;
    private String password;

    public StandardSecurityHandler(EncryptionDictionary encryptionDictionary) {
        super(encryptionDictionary);
        this.standardEncryption = null;
        this.handlerName = "Adobe Standard Security";
    }

    @Override // org.icepdf.core.pobjects.security.SecurityHandler, org.icepdf.core.pobjects.security.SecurityHandlerInterface
    public boolean isAuthorized(String password) {
        if (this.encryptionDictionary.getRevisionNumber() < 5) {
            boolean value = this.standardEncryption.authenticateUserPassword(password);
            if (!value) {
                value = this.standardEncryption.authenticateOwnerPassword(password);
                if (value) {
                    this.password = this.standardEncryption.getUserPassword();
                }
            } else {
                this.password = password;
            }
            return value;
        }
        if (this.encryptionDictionary.getRevisionNumber() == 5) {
            byte[] encryptionKey = this.standardEncryption.encryptionKeyAlgorithm(password, this.encryptionDictionary.getKeyLength());
            this.password = password;
            return encryptionKey != null;
        }
        return false;
    }

    @Override // org.icepdf.core.pobjects.security.SecurityHandler, org.icepdf.core.pobjects.security.SecurityHandlerInterface
    public boolean isOwnerAuthorized(String password) {
        if (this.encryptionDictionary.getRevisionNumber() < 5) {
            return this.standardEncryption.authenticateOwnerPassword(password);
        }
        return this.encryptionDictionary.isAuthenticatedOwnerPassword();
    }

    @Override // org.icepdf.core.pobjects.security.SecurityHandler, org.icepdf.core.pobjects.security.SecurityHandlerInterface
    public boolean isUserAuthorized(String password) {
        if (this.encryptionDictionary.getRevisionNumber() < 5) {
            boolean value = this.standardEncryption.authenticateUserPassword(password);
            if (value) {
                this.password = password;
            }
            return value;
        }
        return this.encryptionDictionary.isAuthenticatedUserPassword();
    }

    @Override // org.icepdf.core.pobjects.security.SecurityHandler, org.icepdf.core.pobjects.security.SecurityHandlerInterface
    public byte[] encrypt(Reference objectReference, byte[] encryptionKey, byte[] data) {
        String algorithmType;
        if (this.encryptionDictionary.getCryptFilter() != null) {
            CryptFilterEntry cryptFilterEntry = this.encryptionDictionary.getCryptFilter().getCryptFilterByName(this.encryptionDictionary.getStrF());
            algorithmType = cryptFilterEntry.getCryptFilterMethod().getName();
        } else {
            algorithmType = StandardEncryption.ENCRYPTION_TYPE_V2;
        }
        return this.standardEncryption.generalEncryptionAlgorithm(objectReference, encryptionKey, algorithmType, data);
    }

    @Override // org.icepdf.core.pobjects.security.SecurityHandler, org.icepdf.core.pobjects.security.SecurityHandlerInterface
    public byte[] decrypt(Reference objectReference, byte[] encryptionKey, byte[] data) {
        return encrypt(objectReference, encryptionKey, data);
    }

    @Override // org.icepdf.core.pobjects.security.SecurityHandler, org.icepdf.core.pobjects.security.SecurityHandlerInterface
    public synchronized InputStream getEncryptionInputStream(Reference objectReference, byte[] encryptionKey, HashMap decodeParams, InputStream input) {
        String algorithmType;
        CryptFilterEntry cryptFilter = null;
        if (decodeParams != null) {
            Name filterName = (Name) decodeParams.get(NAME_KEY);
            if (filterName != null) {
                if (filterName.equals(IDENTITY_KEY)) {
                    return input;
                }
                cryptFilter = this.encryptionDictionary.getCryptFilter().getCryptFilterByName(filterName);
            } else if (this.encryptionDictionary.getCryptFilter() != null) {
                cryptFilter = this.encryptionDictionary.getCryptFilter().getCryptFilterByName(this.encryptionDictionary.getStmF());
            }
        } else if (this.encryptionDictionary.getCryptFilter() != null) {
            cryptFilter = this.encryptionDictionary.getCryptFilter().getCryptFilterByName(this.encryptionDictionary.getStmF());
        }
        if (cryptFilter != null) {
            algorithmType = cryptFilter.getCryptFilterMethod().getName();
        } else {
            algorithmType = StandardEncryption.ENCRYPTION_TYPE_V2;
        }
        return this.standardEncryption.generalEncryptionInputStream(objectReference, encryptionKey, algorithmType, input);
    }

    @Override // org.icepdf.core.pobjects.security.SecurityHandler, org.icepdf.core.pobjects.security.SecurityHandlerInterface
    public byte[] getEncryptionKey() {
        if (!this.initiated) {
            init();
        }
        this.encryptionKey = this.standardEncryption.encryptionKeyAlgorithm(this.password, this.encryptionDictionary.getKeyLength());
        return this.encryptionKey;
    }

    @Override // org.icepdf.core.pobjects.security.SecurityHandler, org.icepdf.core.pobjects.security.SecurityHandlerInterface
    public byte[] getDecryptionKey() {
        return getEncryptionKey();
    }

    @Override // org.icepdf.core.pobjects.security.SecurityHandler, org.icepdf.core.pobjects.security.SecurityHandlerInterface
    public Permissions getPermissions() {
        if (!this.initiated) {
            init();
        }
        return this.permissions;
    }

    @Override // org.icepdf.core.pobjects.security.SecurityHandler, org.icepdf.core.pobjects.security.SecurityHandlerInterface
    public String getHandlerName() {
        return this.handlerName;
    }

    @Override // org.icepdf.core.pobjects.security.SecurityHandler, org.icepdf.core.pobjects.security.SecurityHandlerInterface
    public void init() {
        this.standardEncryption = new StandardEncryption(this.encryptionDictionary);
        this.permissions = new Permissions(this.encryptionDictionary);
        this.permissions.init();
        this.initiated = true;
    }

    @Override // org.icepdf.core.pobjects.security.SecurityHandler, org.icepdf.core.pobjects.security.SecurityHandlerInterface
    public void dispose() {
        this.standardEncryption = null;
        this.encryptionKey = null;
        this.permissions = null;
        this.initiated = false;
    }
}
