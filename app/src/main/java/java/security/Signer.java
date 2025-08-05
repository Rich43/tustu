package java.security;

@Deprecated
/* loaded from: rt.jar:java/security/Signer.class */
public abstract class Signer extends Identity {
    private static final long serialVersionUID = -1763464102261361480L;
    private PrivateKey privateKey;

    protected Signer() {
    }

    public Signer(String str) {
        super(str);
    }

    public Signer(String str, IdentityScope identityScope) throws KeyManagementException {
        super(str, identityScope);
    }

    public PrivateKey getPrivateKey() {
        check("getSignerPrivateKey");
        return this.privateKey;
    }

    public final void setKeyPair(KeyPair keyPair) throws InvalidParameterException, KeyException {
        check("setSignerKeyPair");
        final PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();
        if (publicKey == null || privateKey == null) {
            throw new InvalidParameterException();
        }
        try {
            AccessController.doPrivileged(new PrivilegedExceptionAction<Void>() { // from class: java.security.Signer.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedExceptionAction
                public Void run() throws KeyManagementException {
                    Signer.this.setPublicKey(publicKey);
                    return null;
                }
            });
            this.privateKey = privateKey;
        } catch (PrivilegedActionException e2) {
            throw ((KeyManagementException) e2.getException());
        }
    }

    @Override // java.security.Identity
    String printKeys() {
        String str;
        if (getPublicKey() != null && this.privateKey != null) {
            str = "\tpublic and private keys initialized";
        } else {
            str = "\tno keys";
        }
        return str;
    }

    @Override // java.security.Identity, java.security.Principal
    public String toString() {
        return "[Signer]" + super.toString();
    }

    private static void check(String str) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkSecurityAccess(str);
        }
    }
}
