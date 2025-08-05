package com.sun.media.jfxmedia.track;

import com.sun.media.jfxmedia.track.Track;
import java.util.Locale;

/* loaded from: jfxrt.jar:com/sun/media/jfxmedia/track/VideoTrack.class */
public class VideoTrack extends Track {
    private VideoResolution frameSize;
    private float encodedFrameRate;
    private boolean hasAlphaChannel;

    public VideoTrack(boolean enabled, long trackID, String name, Locale locale, Track.Encoding encoding, VideoResolution frameSize, float encodedFrameRate, boolean hasAlphaChannel) {
        super(enabled, trackID, name, locale, encoding);
        if (frameSize == null) {
            throw new IllegalArgumentException("frameSize == null!");
        }
        if (frameSize.width <= 0) {
            throw new IllegalArgumentException("frameSize.width <= 0!");
        }
        if (frameSize.height <= 0) {
            throw new IllegalArgumentException("frameSize.height <= 0!");
        }
        if (encodedFrameRate < 0.0f) {
            throw new IllegalArgumentException("encodedFrameRate < 0.0!");
        }
        this.frameSize = frameSize;
        this.encodedFrameRate = encodedFrameRate;
        this.hasAlphaChannel = hasAlphaChannel;
    }

    public boolean hasAlphaChannel() {
        return this.hasAlphaChannel;
    }

    public float getEncodedFrameRate() {
        return this.encodedFrameRate;
    }

    public VideoResolution getFrameSize() {
        return this.frameSize;
    }

    public final String toString() {
        return "VideoTrack {\n    name: " + getName() + "\n    encoding: " + ((Object) getEncodingType()) + "\n    frameSize: " + ((Object) this.frameSize) + "\n    encodedFrameRate: " + this.encodedFrameRate + "\n    hasAlphaChannel: " + this.hasAlphaChannel + "\n}";
    }
}
