package sun.security.util;

import java.security.InvalidKeyException;
import javax.crypto.SecretKey;

/* loaded from: rt.jar:sun/security/util/MessageDigestSpi2.class */
public interface MessageDigestSpi2 {
    void engineUpdate(SecretKey secretKey) throws InvalidKeyException;
}
