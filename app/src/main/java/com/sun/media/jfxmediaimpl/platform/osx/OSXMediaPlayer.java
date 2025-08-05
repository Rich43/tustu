package com.sun.media.jfxmediaimpl.platform.osx;

import com.sun.media.jfxmedia.MediaException;
import com.sun.media.jfxmedia.control.MediaPlayerOverlay;
import com.sun.media.jfxmedia.effects.AudioEqualizer;
import com.sun.media.jfxmedia.effects.AudioSpectrum;
import com.sun.media.jfxmedia.locator.Locator;
import com.sun.media.jfxmediaimpl.NativeMedia;
import com.sun.media.jfxmediaimpl.NativeMediaPlayer;

/* loaded from: jfxrt.jar:com/sun/media/jfxmediaimpl/platform/osx/OSXMediaPlayer.class */
final class OSXMediaPlayer extends NativeMediaPlayer {
    private final AudioEqualizer audioEq;
    private final AudioSpectrum audioSpectrum;
    private final Locator mediaLocator;

    private native void osxCreatePlayer(String str) throws MediaException;

    private native long osxGetAudioEqualizerRef();

    private native long osxGetAudioSpectrumRef();

    private native long osxGetAudioSyncDelay() throws MediaException;

    private native void osxSetAudioSyncDelay(long j2) throws MediaException;

    private native void osxPlay() throws MediaException;

    private native void osxStop() throws MediaException;

    private native void osxPause() throws MediaException;

    private native void osxFinish() throws MediaException;

    private native float osxGetRate() throws MediaException;

    private native void osxSetRate(float f2) throws MediaException;

    private native double osxGetPresentationTime() throws MediaException;

    private native boolean osxGetMute() throws MediaException;

    private native void osxSetMute(boolean z2) throws MediaException;

    private native float osxGetVolume() throws MediaException;

    private native void osxSetVolume(float f2) throws MediaException;

    private native float osxGetBalance() throws MediaException;

    private native void osxSetBalance(float f2) throws MediaException;

    private native double osxGetDuration() throws MediaException;

    private native void osxSeek(double d2) throws MediaException;

    private native void osxDispose();

    OSXMediaPlayer(NativeMedia sourceMedia) throws MediaException {
        super(sourceMedia);
        init();
        this.mediaLocator = sourceMedia.getLocator();
        osxCreatePlayer(this.mediaLocator.getStringLocation());
        this.audioEq = createNativeAudioEqualizer(osxGetAudioEqualizerRef());
        this.audioSpectrum = createNativeAudioSpectrum(osxGetAudioSpectrumRef());
    }

    OSXMediaPlayer(Locator source) {
        this(new OSXMedia(source));
    }

    @Override // com.sun.media.jfxmediaimpl.NativeMediaPlayer, com.sun.media.jfxmedia.MediaPlayer
    public AudioEqualizer getEqualizer() {
        return this.audioEq;
    }

    @Override // com.sun.media.jfxmediaimpl.NativeMediaPlayer, com.sun.media.jfxmedia.MediaPlayer
    public AudioSpectrum getAudioSpectrum() {
        return this.audioSpectrum;
    }

    @Override // com.sun.media.jfxmedia.MediaPlayer
    public MediaPlayerOverlay getMediaPlayerOverlay() {
        return null;
    }

    @Override // com.sun.media.jfxmediaimpl.NativeMediaPlayer
    protected long playerGetAudioSyncDelay() throws MediaException {
        return osxGetAudioSyncDelay();
    }

    @Override // com.sun.media.jfxmediaimpl.NativeMediaPlayer
    protected void playerSetAudioSyncDelay(long delay) throws MediaException {
        osxSetAudioSyncDelay(delay);
    }

    @Override // com.sun.media.jfxmediaimpl.NativeMediaPlayer
    protected void playerPlay() throws MediaException {
        osxPlay();
    }

    @Override // com.sun.media.jfxmediaimpl.NativeMediaPlayer
    protected void playerStop() throws MediaException {
        osxStop();
    }

    @Override // com.sun.media.jfxmediaimpl.NativeMediaPlayer
    protected void playerPause() throws MediaException {
        osxPause();
    }

    @Override // com.sun.media.jfxmediaimpl.NativeMediaPlayer
    protected void playerFinish() throws MediaException {
        osxFinish();
    }

    @Override // com.sun.media.jfxmediaimpl.NativeMediaPlayer
    protected float playerGetRate() throws MediaException {
        return osxGetRate();
    }

    @Override // com.sun.media.jfxmediaimpl.NativeMediaPlayer
    protected void playerSetRate(float rate) throws MediaException {
        osxSetRate(rate);
    }

    @Override // com.sun.media.jfxmediaimpl.NativeMediaPlayer
    protected double playerGetPresentationTime() throws MediaException {
        return osxGetPresentationTime();
    }

    @Override // com.sun.media.jfxmediaimpl.NativeMediaPlayer
    protected boolean playerGetMute() throws MediaException {
        return osxGetMute();
    }

    @Override // com.sun.media.jfxmediaimpl.NativeMediaPlayer
    protected void playerSetMute(boolean state) throws MediaException {
        osxSetMute(state);
    }

    @Override // com.sun.media.jfxmediaimpl.NativeMediaPlayer
    protected float playerGetVolume() throws MediaException {
        return osxGetVolume();
    }

    @Override // com.sun.media.jfxmediaimpl.NativeMediaPlayer
    protected void playerSetVolume(float volume) throws MediaException {
        osxSetVolume(volume);
    }

    @Override // com.sun.media.jfxmediaimpl.NativeMediaPlayer
    protected float playerGetBalance() throws MediaException {
        return osxGetBalance();
    }

    @Override // com.sun.media.jfxmediaimpl.NativeMediaPlayer
    protected void playerSetBalance(float balance) throws MediaException {
        osxSetBalance(balance);
    }

    @Override // com.sun.media.jfxmediaimpl.NativeMediaPlayer
    protected double playerGetDuration() throws MediaException {
        double duration = osxGetDuration();
        if (duration == -1.0d) {
            return Double.POSITIVE_INFINITY;
        }
        return duration;
    }

    @Override // com.sun.media.jfxmediaimpl.NativeMediaPlayer
    protected void playerSeek(double streamTime) throws MediaException {
        osxSeek(streamTime);
    }

    @Override // com.sun.media.jfxmediaimpl.NativeMediaPlayer
    protected void playerDispose() {
        osxDispose();
    }

    @Override // com.sun.media.jfxmediaimpl.NativeMediaPlayer
    public void playerInit() throws MediaException {
    }
}
