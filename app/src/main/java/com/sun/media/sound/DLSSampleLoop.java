package com.sun.media.sound;

/* loaded from: rt.jar:com/sun/media/sound/DLSSampleLoop.class */
public final class DLSSampleLoop {
    public static final int LOOP_TYPE_FORWARD = 0;
    public static final int LOOP_TYPE_RELEASE = 1;
    long type;
    long start;
    long length;

    public long getLength() {
        return this.length;
    }

    public void setLength(long j2) {
        this.length = j2;
    }

    public long getStart() {
        return this.start;
    }

    public void setStart(long j2) {
        this.start = j2;
    }

    public long getType() {
        return this.type;
    }

    public void setType(long j2) {
        this.type = j2;
    }
}
