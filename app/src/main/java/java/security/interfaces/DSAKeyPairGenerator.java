package java.security.interfaces;

import java.security.InvalidParameterException;
import java.security.SecureRandom;

/* loaded from: rt.jar:java/security/interfaces/DSAKeyPairGenerator.class */
public interface DSAKeyPairGenerator {
    void initialize(DSAParams dSAParams, SecureRandom secureRandom) throws InvalidParameterException;

    void initialize(int i2, boolean z2, SecureRandom secureRandom) throws InvalidParameterException;
}
