package java.security.spec;

/* loaded from: rt.jar:java/security/spec/MGF1ParameterSpec.class */
public class MGF1ParameterSpec implements AlgorithmParameterSpec {
    public static final MGF1ParameterSpec SHA1 = new MGF1ParameterSpec("SHA-1");
    public static final MGF1ParameterSpec SHA224 = new MGF1ParameterSpec("SHA-224");
    public static final MGF1ParameterSpec SHA256 = new MGF1ParameterSpec("SHA-256");
    public static final MGF1ParameterSpec SHA384 = new MGF1ParameterSpec("SHA-384");
    public static final MGF1ParameterSpec SHA512 = new MGF1ParameterSpec("SHA-512");
    public static final MGF1ParameterSpec SHA512_224 = new MGF1ParameterSpec("SHA-512/224");
    public static final MGF1ParameterSpec SHA512_256 = new MGF1ParameterSpec("SHA-512/256");
    private String mdName;

    public MGF1ParameterSpec(String str) {
        if (str == null) {
            throw new NullPointerException("digest algorithm is null");
        }
        this.mdName = str;
    }

    public String getDigestAlgorithm() {
        return this.mdName;
    }
}
