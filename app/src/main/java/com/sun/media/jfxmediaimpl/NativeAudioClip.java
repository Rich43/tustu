package com.sun.media.jfxmediaimpl;

import com.sun.media.jfxmedia.AudioClip;
import com.sun.media.jfxmedia.MediaException;
import com.sun.media.jfxmedia.locator.Locator;
import com.sun.media.jfxmediaimpl.MediaDisposer;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/* loaded from: jfxrt.jar:com/sun/media/jfxmediaimpl/NativeAudioClip.class */
final class NativeAudioClip extends AudioClip {
    private final Locator mediaSource;
    private long nativeHandle;
    private static NativeAudioClipDisposer clipDisposer = new NativeAudioClipDisposer();

    private static native boolean nacInit();

    private static native long nacLoad(Locator locator);

    private static native long nacCreate(byte[] bArr, int i2, int i3, int i4, int i5, int i6);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void nacUnload(long j2);

    private static native void nacStopAll();

    private native NativeAudioClip nacCreateSegment(long j2, double d2, double d3);

    private native NativeAudioClip nacCreateSegment(long j2, int i2, int i3);

    private native NativeAudioClip nacResample(long j2, int i2, int i3, int i4);

    private native NativeAudioClip nacAppend(long j2, long j3);

    private native boolean nacIsPlaying(long j2);

    private native void nacPlay(long j2, double d2, double d3, double d4, double d5, int i2, int i3);

    private native void nacStop(long j2);

    public static synchronized boolean init() {
        return nacInit();
    }

    public static AudioClip load(URI source) {
        try {
            Locator locator = new Locator(source);
            locator.init();
            NativeAudioClip newClip = new NativeAudioClip(locator);
            if (null != newClip && 0 != newClip.getNativeHandle()) {
                MediaDisposer.addResourceDisposer(newClip, Long.valueOf(newClip.getNativeHandle()), clipDisposer);
                return newClip;
            }
            throw new MediaException("Cannot create audio clip");
        } catch (IOException ex) {
            throw new MediaException("Cannot connect to media", ex);
        } catch (URISyntaxException ex2) {
            throw new MediaException("Non-compliant URI", ex2);
        }
    }

    public static AudioClip create(byte[] data, int dataOffset, int sampleCount, int sampleFormat, int channels, int sampleRate) {
        NativeAudioClip newClip = new NativeAudioClip(data, dataOffset, sampleCount, sampleFormat, channels, sampleRate);
        if (null != newClip && 0 != newClip.getNativeHandle()) {
            MediaDisposer.addResourceDisposer(newClip, Long.valueOf(newClip.getNativeHandle()), clipDisposer);
            return newClip;
        }
        throw new MediaException("Cannot create audio clip");
    }

    private NativeAudioClip(Locator source) {
        this.nativeHandle = 0L;
        this.mediaSource = source;
        this.nativeHandle = nacLoad(this.mediaSource);
    }

    private NativeAudioClip(byte[] data, int dataOffset, int sampleCount, int sampleFormat, int channels, int sampleRate) {
        this.nativeHandle = 0L;
        this.mediaSource = null;
        this.nativeHandle = nacCreate(data, dataOffset, sampleCount, sampleFormat, channels, sampleRate);
    }

    long getNativeHandle() {
        return this.nativeHandle;
    }

    @Override // com.sun.media.jfxmedia.AudioClip
    public AudioClip createSegment(double startTime, double stopTime) {
        return nacCreateSegment(this.nativeHandle, startTime, stopTime);
    }

    @Override // com.sun.media.jfxmedia.AudioClip
    public AudioClip createSegment(int startSample, int endSample) {
        return nacCreateSegment(this.nativeHandle, startSample, endSample);
    }

    @Override // com.sun.media.jfxmedia.AudioClip
    public AudioClip resample(int startSample, int endSample, int newSampleRate) {
        return nacResample(this.nativeHandle, startSample, endSample, newSampleRate);
    }

    @Override // com.sun.media.jfxmedia.AudioClip
    public AudioClip append(AudioClip clip) {
        if (!(clip instanceof NativeAudioClip)) {
            throw new IllegalArgumentException("AudioClip type mismatch, cannot append");
        }
        return nacAppend(this.nativeHandle, ((NativeAudioClip) clip).getNativeHandle());
    }

    @Override // com.sun.media.jfxmedia.AudioClip
    public AudioClip flatten() {
        return this;
    }

    @Override // com.sun.media.jfxmedia.AudioClip
    public boolean isPlaying() {
        return nacIsPlaying(this.nativeHandle);
    }

    @Override // com.sun.media.jfxmedia.AudioClip
    public void play() {
        nacPlay(this.nativeHandle, this.clipVolume, this.clipBalance, this.clipPan, this.clipRate, this.loopCount, this.clipPriority);
    }

    @Override // com.sun.media.jfxmedia.AudioClip
    public void play(double volume) {
        nacPlay(this.nativeHandle, volume, this.clipBalance, this.clipPan, this.clipRate, this.loopCount, this.clipPriority);
    }

    @Override // com.sun.media.jfxmedia.AudioClip
    public void play(double volume, double balance, double rate, double pan, int loopCount, int priority) {
        nacPlay(this.nativeHandle, volume, balance, pan, rate, loopCount, priority);
    }

    @Override // com.sun.media.jfxmedia.AudioClip
    public void stop() {
        nacStop(this.nativeHandle);
    }

    public static void stopAllClips() {
        nacStopAll();
    }

    /* loaded from: jfxrt.jar:com/sun/media/jfxmediaimpl/NativeAudioClip$NativeAudioClipDisposer.class */
    private static class NativeAudioClipDisposer implements MediaDisposer.ResourceDisposer {
        private NativeAudioClipDisposer() {
        }

        @Override // com.sun.media.jfxmediaimpl.MediaDisposer.ResourceDisposer
        public void disposeResource(Object resource) {
            long nativeHandle = ((Long) resource).longValue();
            if (0 != nativeHandle) {
                NativeAudioClip.nacUnload(nativeHandle);
            }
        }
    }
}
