package java.security.spec;

/* loaded from: rt.jar:java/security/spec/DSAGenParameterSpec.class */
public final class DSAGenParameterSpec implements AlgorithmParameterSpec {
    private final int pLen;
    private final int qLen;
    private final int seedLen;

    public DSAGenParameterSpec(int i2, int i3) {
        this(i2, i3, i3);
    }

    public DSAGenParameterSpec(int i2, int i3, int i4) {
        switch (i2) {
            case 1024:
                if (i3 != 160) {
                    throw new IllegalArgumentException("subprimeQLen must be 160 when primePLen=1024");
                }
                break;
            case 2048:
                if (i3 != 224 && i3 != 256) {
                    throw new IllegalArgumentException("subprimeQLen must be 224 or 256 when primePLen=2048");
                }
                break;
            case 3072:
                if (i3 != 256) {
                    throw new IllegalArgumentException("subprimeQLen must be 256 when primePLen=3072");
                }
                break;
            default:
                throw new IllegalArgumentException("primePLen must be 1024, 2048, or 3072");
        }
        if (i4 < i3) {
            throw new IllegalArgumentException("seedLen must be equal to or greater than subprimeQLen");
        }
        this.pLen = i2;
        this.qLen = i3;
        this.seedLen = i4;
    }

    public int getPrimePLength() {
        return this.pLen;
    }

    public int getSubprimeQLength() {
        return this.qLen;
    }

    public int getSeedLength() {
        return this.seedLen;
    }
}
