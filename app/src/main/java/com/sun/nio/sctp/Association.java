package com.sun.nio.sctp;

import jdk.Exported;

@Exported
/* loaded from: rt.jar:com/sun/nio/sctp/Association.class */
public class Association {
    private final int associationID;
    private final int maxInStreams;
    private final int maxOutStreams;

    protected Association(int i2, int i3, int i4) {
        this.associationID = i2;
        this.maxInStreams = i3;
        this.maxOutStreams = i4;
    }

    public final int associationID() {
        return this.associationID;
    }

    public final int maxInboundStreams() {
        return this.maxInStreams;
    }

    public final int maxOutboundStreams() {
        return this.maxOutStreams;
    }
}
