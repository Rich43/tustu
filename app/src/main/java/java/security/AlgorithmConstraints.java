package java.security;

import java.util.Set;

/* loaded from: rt.jar:java/security/AlgorithmConstraints.class */
public interface AlgorithmConstraints {
    boolean permits(Set<CryptoPrimitive> set, String str, AlgorithmParameters algorithmParameters);

    boolean permits(Set<CryptoPrimitive> set, Key key);

    boolean permits(Set<CryptoPrimitive> set, String str, Key key, AlgorithmParameters algorithmParameters);
}
