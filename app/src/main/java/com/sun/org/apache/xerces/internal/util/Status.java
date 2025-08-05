package com.sun.org.apache.xerces.internal.util;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/util/Status.class */
public enum Status {
    SET(-3, false),
    UNKNOWN(-2, false),
    RECOGNIZED(-1, false),
    NOT_SUPPORTED(0, true),
    NOT_RECOGNIZED(1, true),
    NOT_ALLOWED(2, true);

    private final short type;
    private boolean isExceptional;

    Status(short type, boolean isExceptional) {
        this.type = type;
        this.isExceptional = isExceptional;
    }

    public short getType() {
        return this.type;
    }

    public boolean isExceptional() {
        return this.isExceptional;
    }
}
