package com.sun.media.jfxmedia;

import com.sun.media.jfxmediaimpl.AudioClipProvider;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/* loaded from: jfxrt.jar:com/sun/media/jfxmedia/AudioClip.class */
public abstract class AudioClip {
    protected int clipPriority = 0;
    protected int loopCount = 0;
    protected double clipVolume = 1.0d;
    protected double clipBalance = 0.0d;
    protected double clipRate = 1.0d;
    protected double clipPan = 0.0d;
    public static final int SAMPLE_FORMAT_S8 = 0;
    public static final int SAMPLE_FORMAT_U8 = 1;
    public static final int SAMPLE_FORMAT_S16BE = 2;
    public static final int SAMPLE_FORMAT_U16BE = 3;
    public static final int SAMPLE_FORMAT_S16LE = 4;
    public static final int SAMPLE_FORMAT_U16LE = 5;
    public static final int SAMPLE_FORMAT_S24BE = 6;
    public static final int SAMPLE_FORMAT_U24BE = 7;
    public static final int SAMPLE_FORMAT_S24LE = 8;
    public static final int SAMPLE_FORMAT_U24LE = 9;

    public abstract AudioClip createSegment(double d2, double d3) throws IllegalArgumentException;

    public abstract AudioClip createSegment(int i2, int i3) throws IllegalArgumentException;

    public abstract AudioClip resample(int i2, int i3, int i4) throws IOException, IllegalArgumentException;

    public abstract AudioClip append(AudioClip audioClip) throws IOException;

    public abstract AudioClip flatten();

    public abstract boolean isPlaying();

    public abstract void play();

    public abstract void play(double d2);

    public abstract void play(double d2, double d3, double d4, double d5, int i2, int i3);

    public abstract void stop();

    public static AudioClip load(URI source) throws URISyntaxException, IOException {
        return AudioClipProvider.getProvider().load(source);
    }

    public static AudioClip create(byte[] data, int dataOffset, int sampleCount, int sampleFormat, int channels, int sampleRate) throws IllegalArgumentException {
        return AudioClipProvider.getProvider().create(data, dataOffset, sampleCount, sampleFormat, channels, sampleRate);
    }

    public static void stopAllClips() {
        AudioClipProvider.getProvider().stopAllClips();
    }

    public int priority() {
        return this.clipPriority;
    }

    public void setPriority(int prio) {
        this.clipPriority = prio;
    }

    public int loopCount() {
        return this.loopCount;
    }

    public void setLoopCount(int loopCount) {
        this.loopCount = loopCount;
    }

    public double volume() {
        return this.clipVolume;
    }

    public void setVolume(double vol) {
        this.clipVolume = vol;
    }

    public double balance() {
        return this.clipBalance;
    }

    public void setBalance(double bal) {
        this.clipBalance = bal;
    }

    public double playbackRate() {
        return this.clipRate;
    }

    public void setPlaybackRate(double rate) {
        this.clipRate = rate;
    }

    public double pan() {
        return this.clipPan;
    }

    public void setPan(double pan) {
        this.clipPan = pan;
    }
}
