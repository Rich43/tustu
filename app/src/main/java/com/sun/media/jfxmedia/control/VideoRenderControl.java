package com.sun.media.jfxmedia.control;

import com.sun.media.jfxmedia.events.VideoFrameRateListener;
import com.sun.media.jfxmedia.events.VideoRendererListener;

/* loaded from: jfxrt.jar:com/sun/media/jfxmedia/control/VideoRenderControl.class */
public interface VideoRenderControl {
    void addVideoRendererListener(VideoRendererListener videoRendererListener);

    void removeVideoRendererListener(VideoRendererListener videoRendererListener);

    void addVideoFrameRateListener(VideoFrameRateListener videoFrameRateListener);

    void removeVideoFrameRateListener(VideoFrameRateListener videoFrameRateListener);

    int getFrameWidth();

    int getFrameHeight();
}
