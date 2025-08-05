package javax.net.ssl;

/* loaded from: rt.jar:javax/net/ssl/SNIMatcher.class */
public abstract class SNIMatcher {
    private final int type;

    public abstract boolean matches(SNIServerName sNIServerName);

    protected SNIMatcher(int i2) {
        if (i2 < 0) {
            throw new IllegalArgumentException("Server name type cannot be less than zero");
        }
        if (i2 > 255) {
            throw new IllegalArgumentException("Server name type cannot be greater than 255");
        }
        this.type = i2;
    }

    public final int getType() {
        return this.type;
    }
}
