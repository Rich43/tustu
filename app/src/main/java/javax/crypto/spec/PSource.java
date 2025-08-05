package javax.crypto.spec;

/* loaded from: jce.jar:javax/crypto/spec/PSource.class */
public class PSource {
    private String pSrcName;

    protected PSource(String str) {
        if (str == null) {
            throw new NullPointerException("pSource algorithm is null");
        }
        this.pSrcName = str;
    }

    public String getAlgorithm() {
        return this.pSrcName;
    }

    /* loaded from: jce.jar:javax/crypto/spec/PSource$PSpecified.class */
    public static final class PSpecified extends PSource {

        /* renamed from: p, reason: collision with root package name */
        private byte[] f12778p;
        public static final PSpecified DEFAULT = new PSpecified(new byte[0]);

        public PSpecified(byte[] bArr) {
            super("PSpecified");
            this.f12778p = new byte[0];
            this.f12778p = (byte[]) bArr.clone();
        }

        public byte[] getValue() {
            return this.f12778p.length == 0 ? this.f12778p : (byte[]) this.f12778p.clone();
        }
    }
}
