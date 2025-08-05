package com.sun.media.jfxmedia.events;

/* loaded from: jfxrt.jar:com/sun/media/jfxmedia/events/BufferProgressEvent.class */
public class BufferProgressEvent extends PlayerEvent {
    private double duration;
    private long start;
    private long stop;
    private long position;

    public BufferProgressEvent(double duration, long start, long stop, long position) {
        this.duration = duration;
        this.start = start;
        this.stop = stop;
        this.position = position;
    }

    public double getDuration() {
        return this.duration;
    }

    public long getBufferStart() {
        return this.start;
    }

    public long getBufferStop() {
        return this.stop;
    }

    public long getBufferPosition() {
        return this.position;
    }
}
