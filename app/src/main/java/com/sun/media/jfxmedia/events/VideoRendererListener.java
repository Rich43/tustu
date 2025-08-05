package com.sun.media.jfxmedia.events;

/* loaded from: jfxrt.jar:com/sun/media/jfxmedia/events/VideoRendererListener.class */
public interface VideoRendererListener {
    void videoFrameUpdated(NewFrameEvent newFrameEvent);

    void releaseVideoFrames();
}
