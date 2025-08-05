package com.sun.media.jfxmediaimpl;

import com.sun.media.jfxmedia.AudioClip;
import com.sun.media.jfxmedia.logging.Logger;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/* loaded from: jfxrt.jar:com/sun/media/jfxmediaimpl/AudioClipProvider.class */
public class AudioClipProvider {
    private static AudioClipProvider primaDonna;
    private boolean useNative;

    public static synchronized AudioClipProvider getProvider() {
        if (null == primaDonna) {
            primaDonna = new AudioClipProvider();
        }
        return primaDonna;
    }

    private AudioClipProvider() {
        this.useNative = false;
        try {
            this.useNative = NativeAudioClip.init();
        } catch (Exception t2) {
            Logger.logMsg(4, "Exception while loading native AudioClip library: " + ((Object) t2));
        } catch (UnsatisfiedLinkError e2) {
            Logger.logMsg(1, "JavaFX AudioClip native methods not linked, using NativeMedia implementation");
        }
    }

    public AudioClip load(URI source) throws URISyntaxException, IOException {
        if (this.useNative) {
            return NativeAudioClip.load(source);
        }
        return NativeMediaAudioClip.load(source);
    }

    public AudioClip create(byte[] data, int dataOffset, int sampleCount, int sampleFormat, int channels, int sampleRate) throws IllegalArgumentException {
        if (this.useNative) {
            return NativeAudioClip.create(data, dataOffset, sampleCount, sampleFormat, channels, sampleRate);
        }
        return NativeMediaAudioClip.create(data, dataOffset, sampleCount, sampleFormat, channels, sampleRate);
    }

    public void stopAllClips() {
        if (this.useNative) {
            NativeAudioClip.stopAllClips();
        }
        NativeMediaAudioClip.stopAllClips();
    }
}
