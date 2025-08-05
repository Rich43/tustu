package com.sun.corba.se.spi.protocol;

/* loaded from: rt.jar:com/sun/corba/se/spi/protocol/RetryType.class */
public enum RetryType {
    NONE(false),
    BEFORE_RESPONSE(true),
    AFTER_RESPONSE(true);

    private final boolean isRetry;

    RetryType(boolean z2) {
        this.isRetry = z2;
    }

    public boolean isRetry() {
        return this.isRetry;
    }
}
