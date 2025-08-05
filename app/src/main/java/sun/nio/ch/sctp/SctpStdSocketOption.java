package sun.nio.ch.sctp;

import com.sun.nio.sctp.SctpSocketOption;

/* loaded from: rt.jar:sun/nio/ch/sctp/SctpStdSocketOption.class */
public class SctpStdSocketOption<T> implements SctpSocketOption<T> {
    public static final int SCTP_DISABLE_FRAGMENTS = 1;
    public static final int SCTP_EXPLICIT_COMPLETE = 2;
    public static final int SCTP_FRAGMENT_INTERLEAVE = 3;
    public static final int SCTP_NODELAY = 4;
    public static final int SO_SNDBUF = 5;
    public static final int SO_RCVBUF = 6;
    public static final int SO_LINGER = 7;
    private final String name;
    private final Class<T> type;
    private int constValue;

    public SctpStdSocketOption(String str, Class<T> cls) {
        this.name = str;
        this.type = cls;
    }

    public SctpStdSocketOption(String str, Class<T> cls, int i2) {
        this.name = str;
        this.type = cls;
        this.constValue = i2;
    }

    @Override // java.net.SocketOption
    public String name() {
        return this.name;
    }

    @Override // java.net.SocketOption
    public Class<T> type() {
        return this.type;
    }

    public String toString() {
        return this.name;
    }

    int constValue() {
        return this.constValue;
    }
}
