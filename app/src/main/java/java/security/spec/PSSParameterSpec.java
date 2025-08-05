package java.security.spec;

import java.util.Objects;

/* loaded from: rt.jar:java/security/spec/PSSParameterSpec.class */
public class PSSParameterSpec implements AlgorithmParameterSpec {
    private final String mdName;
    private final String mgfName;
    private final AlgorithmParameterSpec mgfSpec;
    private final int saltLen;
    private final int trailerField;
    public static final int TRAILER_FIELD_BC = 1;
    public static final PSSParameterSpec DEFAULT = new PSSParameterSpec("SHA-1", "MGF1", MGF1ParameterSpec.SHA1, 20, 1);

    private PSSParameterSpec() {
        throw new RuntimeException("default constructor not allowed");
    }

    public PSSParameterSpec(String str, String str2, AlgorithmParameterSpec algorithmParameterSpec, int i2, int i3) {
        Objects.requireNonNull(str, "digest algorithm is null");
        Objects.requireNonNull(str2, "mask generation function algorithm is null");
        if (i2 < 0) {
            throw new IllegalArgumentException("negative saltLen value: " + i2);
        }
        if (i3 < 0) {
            throw new IllegalArgumentException("negative trailerField: " + i3);
        }
        this.mdName = str;
        this.mgfName = str2;
        this.mgfSpec = algorithmParameterSpec;
        this.saltLen = i2;
        this.trailerField = i3;
    }

    public PSSParameterSpec(int i2) {
        this("SHA-1", "MGF1", MGF1ParameterSpec.SHA1, i2, 1);
    }

    public String getDigestAlgorithm() {
        return this.mdName;
    }

    public String getMGFAlgorithm() {
        return this.mgfName;
    }

    public AlgorithmParameterSpec getMGFParameters() {
        return this.mgfSpec;
    }

    public int getSaltLength() {
        return this.saltLen;
    }

    public int getTrailerField() {
        return this.trailerField;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("MD: " + this.mdName + "\n").append("MGF: " + ((Object) this.mgfSpec) + "\n").append("SaltLength: " + this.saltLen + "\n").append("TrailerField: " + this.trailerField + "\n");
        return sb.toString();
    }
}
