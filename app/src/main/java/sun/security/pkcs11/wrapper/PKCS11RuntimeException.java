package sun.security.pkcs11.wrapper;

/* loaded from: sunpkcs11.jar:sun/security/pkcs11/wrapper/PKCS11RuntimeException.class */
public class PKCS11RuntimeException extends RuntimeException {
    private static final long serialVersionUID = 7889842162743590564L;

    public PKCS11RuntimeException() {
    }

    public PKCS11RuntimeException(String str) {
        super(str);
    }

    public PKCS11RuntimeException(Exception exc) {
        super(exc);
    }

    public PKCS11RuntimeException(String str, Exception exc) {
        super(str, exc);
    }
}
