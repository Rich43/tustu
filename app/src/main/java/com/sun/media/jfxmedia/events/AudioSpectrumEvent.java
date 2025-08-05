package com.sun.media.jfxmedia.events;

import com.sun.media.jfxmedia.effects.AudioSpectrum;

/* loaded from: jfxrt.jar:com/sun/media/jfxmedia/events/AudioSpectrumEvent.class */
public class AudioSpectrumEvent extends PlayerEvent {
    private AudioSpectrum source;
    private double timestamp;
    private double duration;
    private boolean queryTimestamp;

    public AudioSpectrumEvent(AudioSpectrum source, double timestamp, double duration, boolean queryTimestamp) {
        this.source = source;
        this.timestamp = timestamp;
        this.duration = duration;
        this.queryTimestamp = queryTimestamp;
    }

    public final AudioSpectrum getSource() {
        return this.source;
    }

    public final void setTimestamp(double timestamp) {
        this.timestamp = timestamp;
    }

    public final double getTimestamp() {
        return this.timestamp;
    }

    public final double getDuration() {
        return this.duration;
    }

    public final boolean queryTimestamp() {
        return this.queryTimestamp;
    }
}
