package com.sun.media.jfxmediaimpl;

import com.sun.media.jfxmedia.AudioClip;
import com.sun.media.jfxmedia.locator.Locator;
import com.sun.media.jfxmedia.logging.Logger;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.atomic.AtomicInteger;

/* loaded from: jfxrt.jar:com/sun/media/jfxmediaimpl/NativeMediaAudioClip.class */
final class NativeMediaAudioClip extends AudioClip {
    private URI sourceURI;
    private Locator mediaLocator;
    private AtomicInteger playCount = new AtomicInteger(0);

    private NativeMediaAudioClip(URI source) throws URISyntaxException, IOException {
        this.sourceURI = source;
        if (Logger.canLog(1)) {
            Logger.logMsg(1, "Creating AudioClip for URI " + ((Object) source));
        }
        this.mediaLocator = new Locator(this.sourceURI);
        this.mediaLocator.init();
        this.mediaLocator.cacheMedia();
    }

    Locator getLocator() {
        return this.mediaLocator;
    }

    public static AudioClip load(URI source) throws URISyntaxException, IOException {
        return new NativeMediaAudioClip(source);
    }

    public static AudioClip create(byte[] data, int dataOffset, int sampleCount, int sampleFormat, int channels, int sampleRate) {
        throw new UnsupportedOperationException("NativeMediaAudioClip does not support creating clips from raw sample data");
    }

    @Override // com.sun.media.jfxmedia.AudioClip
    public AudioClip createSegment(double startTime, double stopTime) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // com.sun.media.jfxmedia.AudioClip
    public AudioClip createSegment(int startSample, int endSample) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // com.sun.media.jfxmedia.AudioClip
    public AudioClip resample(int startSample, int endSample, int newSampleRate) throws IOException, IllegalArgumentException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // com.sun.media.jfxmedia.AudioClip
    public AudioClip append(AudioClip clip) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // com.sun.media.jfxmedia.AudioClip
    public AudioClip flatten() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // com.sun.media.jfxmedia.AudioClip
    public boolean isPlaying() {
        return this.playCount.get() > 0;
    }

    @Override // com.sun.media.jfxmedia.AudioClip
    public void play() {
        play(this.clipVolume, this.clipBalance, this.clipRate, this.clipPan, this.loopCount, this.clipPriority);
    }

    @Override // com.sun.media.jfxmedia.AudioClip
    public void play(double volume) {
        play(volume, this.clipBalance, this.clipRate, this.clipPan, this.loopCount, this.clipPriority);
    }

    @Override // com.sun.media.jfxmedia.AudioClip
    public void play(double volume, double balance, double rate, double pan, int loopCount, int priority) {
        this.playCount.getAndIncrement();
        NativeMediaAudioClipPlayer.playClip(this, volume, balance, rate, pan, loopCount, priority);
    }

    @Override // com.sun.media.jfxmedia.AudioClip
    public void stop() {
        NativeMediaAudioClipPlayer.stopPlayers(this.mediaLocator);
    }

    public static void stopAllClips() {
        NativeMediaAudioClipPlayer.stopPlayers(null);
    }

    void playFinished() {
        this.playCount.decrementAndGet();
    }
}
