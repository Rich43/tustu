package com.sun.media.jfxmedia.events;

import com.sun.media.jfxmedia.control.VideoDataBuffer;

/* loaded from: jfxrt.jar:com/sun/media/jfxmedia/events/NewFrameEvent.class */
public class NewFrameEvent extends PlayerEvent {
    private VideoDataBuffer frameData;

    public NewFrameEvent(VideoDataBuffer buffer) {
        if (buffer == null) {
            throw new IllegalArgumentException("buffer == null!");
        }
        this.frameData = buffer;
    }

    public VideoDataBuffer getFrameData() {
        return this.frameData;
    }
}
