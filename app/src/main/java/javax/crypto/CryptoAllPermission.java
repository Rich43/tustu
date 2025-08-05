package javax.crypto;

import java.security.Permission;
import java.security.PermissionCollection;

/* loaded from: jce.jar:javax/crypto/CryptoAllPermission.class */
final class CryptoAllPermission extends CryptoPermission {
    private static final long serialVersionUID = -5066513634293192112L;
    static final String ALG_NAME = "CryptoAllPermission";
    static final CryptoAllPermission INSTANCE = new CryptoAllPermission();

    private CryptoAllPermission() {
        super(ALG_NAME);
    }

    @Override // javax.crypto.CryptoPermission, java.security.Permission
    public boolean implies(Permission permission) {
        return permission instanceof CryptoPermission;
    }

    @Override // javax.crypto.CryptoPermission, java.security.Permission
    public boolean equals(Object obj) {
        return obj == INSTANCE;
    }

    @Override // javax.crypto.CryptoPermission, java.security.Permission
    public int hashCode() {
        return 1;
    }

    @Override // javax.crypto.CryptoPermission, java.security.Permission
    public PermissionCollection newPermissionCollection() {
        return new CryptoAllPermissionCollection();
    }
}
