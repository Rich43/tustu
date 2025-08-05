package jdk.net;

import jdk.Exported;

@Exported
/* loaded from: rt.jar:jdk/net/SocketFlow.class */
public class SocketFlow {
    private static final int UNSET = -1;
    public static final int NORMAL_PRIORITY = 1;
    public static final int HIGH_PRIORITY = 2;
    private int priority = 1;
    private long bandwidth = -1;
    private Status status = Status.NO_STATUS;

    @Exported
    /* loaded from: rt.jar:jdk/net/SocketFlow$Status.class */
    public enum Status {
        NO_STATUS,
        OK,
        NO_PERMISSION,
        NOT_CONNECTED,
        NOT_SUPPORTED,
        ALREADY_CREATED,
        IN_PROGRESS,
        OTHER
    }

    private SocketFlow() {
    }

    public static SocketFlow create() {
        return new SocketFlow();
    }

    public SocketFlow priority(int i2) {
        if (i2 != 1 && i2 != 2) {
            throw new IllegalArgumentException("invalid priority");
        }
        this.priority = i2;
        return this;
    }

    public SocketFlow bandwidth(long j2) {
        if (j2 < 0) {
            throw new IllegalArgumentException("invalid bandwidth");
        }
        this.bandwidth = j2;
        return this;
    }

    public int priority() {
        return this.priority;
    }

    public long bandwidth() {
        return this.bandwidth;
    }

    public Status status() {
        return this.status;
    }
}
