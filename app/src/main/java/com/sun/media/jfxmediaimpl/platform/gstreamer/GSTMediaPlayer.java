package com.sun.media.jfxmediaimpl.platform.gstreamer;

import com.sun.media.jfxmedia.MediaError;
import com.sun.media.jfxmedia.MediaException;
import com.sun.media.jfxmedia.control.MediaPlayerOverlay;
import com.sun.media.jfxmedia.effects.AudioEqualizer;
import com.sun.media.jfxmedia.effects.AudioSpectrum;
import com.sun.media.jfxmedia.locator.Locator;
import com.sun.media.jfxmediaimpl.NativeMediaPlayer;

/* loaded from: jfxrt.jar:com/sun/media/jfxmediaimpl/platform/gstreamer/GSTMediaPlayer.class */
final class GSTMediaPlayer extends NativeMediaPlayer {
    private GSTMedia gstMedia;
    private float mutedVolume;
    private boolean muteEnabled;
    private AudioEqualizer audioEqualizer;
    private AudioSpectrum audioSpectrum;

    private native int gstInitPlayer(long j2);

    private native long gstGetAudioEqualizer(long j2);

    private native long gstGetAudioSpectrum(long j2);

    private native int gstGetAudioSyncDelay(long j2, long[] jArr);

    private native int gstSetAudioSyncDelay(long j2, long j3);

    private native int gstPlay(long j2);

    private native int gstPause(long j2);

    private native int gstStop(long j2);

    private native int gstFinish(long j2);

    private native int gstGetRate(long j2, float[] fArr);

    private native int gstSetRate(long j2, float f2);

    private native int gstGetPresentationTime(long j2, double[] dArr);

    private native int gstGetVolume(long j2, float[] fArr);

    private native int gstSetVolume(long j2, float f2);

    private native int gstGetBalance(long j2, float[] fArr);

    private native int gstSetBalance(long j2, float f2);

    private native int gstGetDuration(long j2, double[] dArr);

    private native int gstSeek(long j2, double d2);

    private GSTMediaPlayer(GSTMedia sourceMedia) throws MediaException {
        super(sourceMedia);
        this.gstMedia = null;
        this.mutedVolume = 1.0f;
        this.muteEnabled = false;
        init();
        this.gstMedia = sourceMedia;
        int rc = gstInitPlayer(this.gstMedia.getNativeMediaRef());
        if (0 != rc) {
            dispose();
            throwMediaErrorException(rc, null);
        }
        long mediaRef = this.gstMedia.getNativeMediaRef();
        this.audioSpectrum = createNativeAudioSpectrum(gstGetAudioSpectrum(mediaRef));
        this.audioEqualizer = createNativeAudioEqualizer(gstGetAudioEqualizer(mediaRef));
    }

    GSTMediaPlayer(Locator source) {
        this(new GSTMedia(source));
    }

    @Override // com.sun.media.jfxmediaimpl.NativeMediaPlayer, com.sun.media.jfxmedia.MediaPlayer
    public AudioEqualizer getEqualizer() {
        return this.audioEqualizer;
    }

    @Override // com.sun.media.jfxmediaimpl.NativeMediaPlayer, com.sun.media.jfxmedia.MediaPlayer
    public AudioSpectrum getAudioSpectrum() {
        return this.audioSpectrum;
    }

    @Override // com.sun.media.jfxmedia.MediaPlayer
    public MediaPlayerOverlay getMediaPlayerOverlay() {
        return null;
    }

    private void throwMediaErrorException(int code, String message) throws MediaException {
        MediaError me = MediaError.getFromCode(code);
        throw new MediaException(message, null, me);
    }

    @Override // com.sun.media.jfxmediaimpl.NativeMediaPlayer
    protected long playerGetAudioSyncDelay() throws MediaException {
        long[] audioSyncDelay = new long[1];
        int rc = gstGetAudioSyncDelay(this.gstMedia.getNativeMediaRef(), audioSyncDelay);
        if (0 != rc) {
            throwMediaErrorException(rc, null);
        }
        return audioSyncDelay[0];
    }

    @Override // com.sun.media.jfxmediaimpl.NativeMediaPlayer
    protected void playerSetAudioSyncDelay(long delay) throws MediaException {
        int rc = gstSetAudioSyncDelay(this.gstMedia.getNativeMediaRef(), delay);
        if (0 != rc) {
            throwMediaErrorException(rc, null);
        }
    }

    @Override // com.sun.media.jfxmediaimpl.NativeMediaPlayer
    protected void playerPlay() throws MediaException {
        int rc = gstPlay(this.gstMedia.getNativeMediaRef());
        if (0 != rc) {
            throwMediaErrorException(rc, null);
        }
    }

    @Override // com.sun.media.jfxmediaimpl.NativeMediaPlayer
    protected void playerStop() throws MediaException {
        int rc = gstStop(this.gstMedia.getNativeMediaRef());
        if (0 != rc) {
            throwMediaErrorException(rc, null);
        }
    }

    @Override // com.sun.media.jfxmediaimpl.NativeMediaPlayer
    protected void playerPause() throws MediaException {
        int rc = gstPause(this.gstMedia.getNativeMediaRef());
        if (0 != rc) {
            throwMediaErrorException(rc, null);
        }
    }

    @Override // com.sun.media.jfxmediaimpl.NativeMediaPlayer
    protected void playerFinish() throws MediaException {
        int rc = gstFinish(this.gstMedia.getNativeMediaRef());
        if (0 != rc) {
            throwMediaErrorException(rc, null);
        }
    }

    @Override // com.sun.media.jfxmediaimpl.NativeMediaPlayer
    protected float playerGetRate() throws MediaException {
        float[] rate = new float[1];
        int rc = gstGetRate(this.gstMedia.getNativeMediaRef(), rate);
        if (0 != rc) {
            throwMediaErrorException(rc, null);
        }
        return rate[0];
    }

    @Override // com.sun.media.jfxmediaimpl.NativeMediaPlayer
    protected void playerSetRate(float rate) throws MediaException {
        int rc = gstSetRate(this.gstMedia.getNativeMediaRef(), rate);
        if (0 != rc) {
            throwMediaErrorException(rc, null);
        }
    }

    @Override // com.sun.media.jfxmediaimpl.NativeMediaPlayer
    protected double playerGetPresentationTime() throws MediaException {
        double[] presentationTime = new double[1];
        int rc = gstGetPresentationTime(this.gstMedia.getNativeMediaRef(), presentationTime);
        if (0 != rc) {
            throwMediaErrorException(rc, null);
        }
        return presentationTime[0];
    }

    @Override // com.sun.media.jfxmediaimpl.NativeMediaPlayer
    protected boolean playerGetMute() throws MediaException {
        return this.muteEnabled;
    }

    @Override // com.sun.media.jfxmediaimpl.NativeMediaPlayer
    protected synchronized void playerSetMute(boolean enable) throws MediaException {
        if (enable != this.muteEnabled) {
            if (enable) {
                float currentVolume = getVolume();
                playerSetVolume(0.0f);
                this.muteEnabled = true;
                this.mutedVolume = currentVolume;
                return;
            }
            this.muteEnabled = false;
            playerSetVolume(this.mutedVolume);
        }
    }

    @Override // com.sun.media.jfxmediaimpl.NativeMediaPlayer
    protected float playerGetVolume() throws MediaException {
        synchronized (this) {
            if (this.muteEnabled) {
                return this.mutedVolume;
            }
            float[] volume = new float[1];
            int rc = gstGetVolume(this.gstMedia.getNativeMediaRef(), volume);
            if (0 != rc) {
                throwMediaErrorException(rc, null);
            }
            return volume[0];
        }
    }

    @Override // com.sun.media.jfxmediaimpl.NativeMediaPlayer
    protected synchronized void playerSetVolume(float volume) throws MediaException {
        if (!this.muteEnabled) {
            int rc = gstSetVolume(this.gstMedia.getNativeMediaRef(), volume);
            if (0 != rc) {
                throwMediaErrorException(rc, null);
                return;
            } else {
                this.mutedVolume = volume;
                return;
            }
        }
        this.mutedVolume = volume;
    }

    @Override // com.sun.media.jfxmediaimpl.NativeMediaPlayer
    protected float playerGetBalance() throws MediaException {
        float[] balance = new float[1];
        int rc = gstGetBalance(this.gstMedia.getNativeMediaRef(), balance);
        if (0 != rc) {
            throwMediaErrorException(rc, null);
        }
        return balance[0];
    }

    @Override // com.sun.media.jfxmediaimpl.NativeMediaPlayer
    protected void playerSetBalance(float balance) throws MediaException {
        int rc = gstSetBalance(this.gstMedia.getNativeMediaRef(), balance);
        if (0 != rc) {
            throwMediaErrorException(rc, null);
        }
    }

    @Override // com.sun.media.jfxmediaimpl.NativeMediaPlayer
    protected double playerGetDuration() throws MediaException {
        double[] duration = new double[1];
        int rc = gstGetDuration(this.gstMedia.getNativeMediaRef(), duration);
        if (0 != rc) {
            throwMediaErrorException(rc, null);
        }
        if (duration[0] == -1.0d) {
            return Double.POSITIVE_INFINITY;
        }
        return duration[0];
    }

    @Override // com.sun.media.jfxmediaimpl.NativeMediaPlayer
    protected void playerSeek(double streamTime) throws MediaException {
        int rc = gstSeek(this.gstMedia.getNativeMediaRef(), streamTime);
        if (0 != rc) {
            throwMediaErrorException(rc, null);
        }
    }

    @Override // com.sun.media.jfxmediaimpl.NativeMediaPlayer
    protected void playerInit() throws MediaException {
    }

    @Override // com.sun.media.jfxmediaimpl.NativeMediaPlayer
    protected void playerDispose() {
        this.audioEqualizer = null;
        this.audioSpectrum = null;
        this.gstMedia = null;
    }
}
