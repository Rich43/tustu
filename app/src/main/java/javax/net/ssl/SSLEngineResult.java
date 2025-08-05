package javax.net.ssl;

/* loaded from: rt.jar:javax/net/ssl/SSLEngineResult.class */
public class SSLEngineResult {
    private final Status status;
    private final HandshakeStatus handshakeStatus;
    private final int bytesConsumed;
    private final int bytesProduced;

    /* loaded from: rt.jar:javax/net/ssl/SSLEngineResult$HandshakeStatus.class */
    public enum HandshakeStatus {
        NOT_HANDSHAKING,
        FINISHED,
        NEED_TASK,
        NEED_WRAP,
        NEED_UNWRAP
    }

    /* loaded from: rt.jar:javax/net/ssl/SSLEngineResult$Status.class */
    public enum Status {
        BUFFER_UNDERFLOW,
        BUFFER_OVERFLOW,
        OK,
        CLOSED
    }

    public SSLEngineResult(Status status, HandshakeStatus handshakeStatus, int i2, int i3) {
        if (status == null || handshakeStatus == null || i2 < 0 || i3 < 0) {
            throw new IllegalArgumentException("Invalid Parameter(s)");
        }
        this.status = status;
        this.handshakeStatus = handshakeStatus;
        this.bytesConsumed = i2;
        this.bytesProduced = i3;
    }

    public final Status getStatus() {
        return this.status;
    }

    public final HandshakeStatus getHandshakeStatus() {
        return this.handshakeStatus;
    }

    public final int bytesConsumed() {
        return this.bytesConsumed;
    }

    public final int bytesProduced() {
        return this.bytesProduced;
    }

    public String toString() {
        return "Status = " + ((Object) this.status) + " HandshakeStatus = " + ((Object) this.handshakeStatus) + "\nbytesConsumed = " + this.bytesConsumed + " bytesProduced = " + this.bytesProduced;
    }
}
