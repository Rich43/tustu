package java.security.spec;

/* loaded from: rt.jar:java/security/spec/ECGenParameterSpec.class */
public class ECGenParameterSpec implements AlgorithmParameterSpec {
    private String name;

    public ECGenParameterSpec(String str) {
        if (str == null) {
            throw new NullPointerException("stdName is null");
        }
        this.name = str;
    }

    public String getName() {
        return this.name;
    }
}
