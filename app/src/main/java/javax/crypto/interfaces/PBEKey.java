package javax.crypto.interfaces;

import javax.crypto.SecretKey;

/* loaded from: jce.jar:javax/crypto/interfaces/PBEKey.class */
public interface PBEKey extends SecretKey {
    public static final long serialVersionUID = -1430015993304333921L;

    char[] getPassword();

    byte[] getSalt();

    int getIterationCount();
}
