package com.sun.media.jfxmedia.track;

import com.sun.media.jfxmedia.track.Track;
import java.util.Locale;

/* loaded from: jfxrt.jar:com/sun/media/jfxmedia/track/AudioTrack.class */
public class AudioTrack extends Track {
    public static final int UNKNOWN = 0;
    public static final int FRONT_LEFT = 1;
    public static final int FRONT_RIGHT = 2;
    public static final int FRONT_CENTER = 4;
    public static final int REAR_LEFT = 8;
    public static final int REAR_RIGHT = 16;
    public static final int REAR_CENTER = 32;
    private int numChannels;
    private int channelMask;
    private float encodedSampleRate;

    public AudioTrack(boolean enabled, long trackID, String name, Locale locale, Track.Encoding encoding, int numChannels, int channelMask, float encodedSampleRate) {
        super(enabled, trackID, name, locale, encoding);
        if (numChannels < 1) {
            throw new IllegalArgumentException("numChannels < 1!");
        }
        if (encodedSampleRate <= 0.0f) {
            throw new IllegalArgumentException("encodedSampleRate <= 0.0");
        }
        this.numChannels = numChannels;
        this.channelMask = channelMask;
        this.encodedSampleRate = encodedSampleRate;
    }

    public int getNumChannels() {
        return this.numChannels;
    }

    public int getChannelMask() {
        return this.channelMask;
    }

    public float getEncodedSampleRate() {
        return this.encodedSampleRate;
    }

    public final String toString() {
        return "AudioTrack {\n    name: " + getName() + "\n    encoding: " + ((Object) getEncodingType()) + "\n    language: " + ((Object) getLocale()) + "\n    numChannels: " + this.numChannels + "\n    channelMask: " + this.channelMask + "\n    encodedSampleRate: " + this.encodedSampleRate + "\n}";
    }
}
