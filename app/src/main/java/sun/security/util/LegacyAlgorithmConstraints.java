package sun.security.util;

import java.security.AlgorithmParameters;
import java.security.CryptoPrimitive;
import java.security.Key;
import java.util.List;
import java.util.Set;

/* loaded from: rt.jar:sun/security/util/LegacyAlgorithmConstraints.class */
public class LegacyAlgorithmConstraints extends AbstractAlgorithmConstraints {
    public static final String PROPERTY_TLS_LEGACY_ALGS = "jdk.tls.legacyAlgorithms";
    private final List<String> legacyAlgorithms;

    public LegacyAlgorithmConstraints(String str, AlgorithmDecomposer algorithmDecomposer) {
        super(algorithmDecomposer);
        this.legacyAlgorithms = getAlgorithms(str);
    }

    @Override // java.security.AlgorithmConstraints
    public final boolean permits(Set<CryptoPrimitive> set, String str, AlgorithmParameters algorithmParameters) {
        return checkAlgorithm(this.legacyAlgorithms, str, this.decomposer);
    }

    @Override // java.security.AlgorithmConstraints
    public final boolean permits(Set<CryptoPrimitive> set, Key key) {
        return true;
    }

    @Override // java.security.AlgorithmConstraints
    public final boolean permits(Set<CryptoPrimitive> set, String str, Key key, AlgorithmParameters algorithmParameters) {
        return checkAlgorithm(this.legacyAlgorithms, str, this.decomposer);
    }
}
