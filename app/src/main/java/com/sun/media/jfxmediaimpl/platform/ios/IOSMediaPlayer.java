package com.sun.media.jfxmediaimpl.platform.ios;

import com.sun.media.jfxmedia.MediaError;
import com.sun.media.jfxmedia.MediaException;
import com.sun.media.jfxmedia.control.MediaPlayerOverlay;
import com.sun.media.jfxmedia.effects.AudioEqualizer;
import com.sun.media.jfxmedia.effects.AudioSpectrum;
import com.sun.media.jfxmedia.effects.EqualizerBand;
import com.sun.media.jfxmedia.locator.Locator;
import com.sun.media.jfxmediaimpl.NativeMediaPlayer;
import java.util.HashMap;
import java.util.Map;

/* loaded from: jfxrt.jar:com/sun/media/jfxmediaimpl/platform/ios/IOSMediaPlayer.class */
public final class IOSMediaPlayer extends NativeMediaPlayer {
    private IOSMedia iosMedia;
    private final NullAudioEQ audioEqualizer;
    private final NullAudioSpectrum audioSpectrum;
    private final MediaPlayerOverlay mediaPlayerOverlay;
    private float mutedVolume;
    private boolean muteEnabled;

    private native int iosInitPlayer(long j2);

    private native int iosGetAudioSyncDelay(long j2, long[] jArr);

    private native int iosSetAudioSyncDelay(long j2, long j3);

    private native int iosPlay(long j2);

    private native int iosPause(long j2);

    private native int iosStop(long j2);

    private native int iosGetRate(long j2, float[] fArr);

    private native int iosSetRate(long j2, float f2);

    private native int iosGetPresentationTime(long j2, double[] dArr);

    private native int iosGetVolume(long j2, float[] fArr);

    private native int iosSetVolume(long j2, float f2);

    private native int iosGetBalance(long j2, float[] fArr);

    private native int iosSetBalance(long j2, float f2);

    private native int iosGetDuration(long j2, double[] dArr);

    private native int iosSeek(long j2, double d2);

    private native void iosDispose(long j2);

    private native int iosFinish(long j2);

    /* JADX INFO: Access modifiers changed from: private */
    public native int iosSetOverlayX(long j2, double d2);

    /* JADX INFO: Access modifiers changed from: private */
    public native int iosSetOverlayY(long j2, double d2);

    /* JADX INFO: Access modifiers changed from: private */
    public native int iosSetOverlayVisible(long j2, boolean z2);

    /* JADX INFO: Access modifiers changed from: private */
    public native int iosSetOverlayWidth(long j2, double d2);

    /* JADX INFO: Access modifiers changed from: private */
    public native int iosSetOverlayHeight(long j2, double d2);

    /* JADX INFO: Access modifiers changed from: private */
    public native int iosSetOverlayPreserveRatio(long j2, boolean z2);

    /* JADX INFO: Access modifiers changed from: private */
    public native int iosSetOverlayOpacity(long j2, double d2);

    /* JADX INFO: Access modifiers changed from: private */
    public native int iosSetOverlayTransform(long j2, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, double d10, double d11, double d12, double d13);

    private IOSMediaPlayer(IOSMedia sourceMedia) throws MediaException {
        super(sourceMedia);
        this.mutedVolume = 1.0f;
        this.iosMedia = sourceMedia;
        init();
        handleError(iosInitPlayer(this.iosMedia.getNativeMediaRef()));
        this.audioEqualizer = new NullAudioEQ();
        this.audioSpectrum = new NullAudioSpectrum();
        this.mediaPlayerOverlay = new MediaPlayerOverlayImpl();
    }

    IOSMediaPlayer(Locator source) {
        this(new IOSMedia(source));
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
        return this.mediaPlayerOverlay;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleError(int err) throws MediaException {
        if (0 != err) {
            MediaError me = MediaError.getFromCode(err);
            throw new MediaException("Media error occurred", null, me);
        }
    }

    @Override // com.sun.media.jfxmediaimpl.NativeMediaPlayer
    protected long playerGetAudioSyncDelay() throws MediaException {
        long[] audioSyncDelay = new long[1];
        handleError(iosGetAudioSyncDelay(this.iosMedia.getNativeMediaRef(), audioSyncDelay));
        return audioSyncDelay[0];
    }

    @Override // com.sun.media.jfxmediaimpl.NativeMediaPlayer
    protected void playerSetAudioSyncDelay(long delay) throws MediaException {
        handleError(iosSetAudioSyncDelay(this.iosMedia.getNativeMediaRef(), delay));
    }

    @Override // com.sun.media.jfxmediaimpl.NativeMediaPlayer
    protected void playerPlay() throws MediaException {
        handleError(iosPlay(this.iosMedia.getNativeMediaRef()));
    }

    @Override // com.sun.media.jfxmediaimpl.NativeMediaPlayer
    protected void playerStop() throws MediaException {
        handleError(iosStop(this.iosMedia.getNativeMediaRef()));
    }

    @Override // com.sun.media.jfxmediaimpl.NativeMediaPlayer
    protected void playerPause() throws MediaException {
        handleError(iosPause(this.iosMedia.getNativeMediaRef()));
    }

    @Override // com.sun.media.jfxmediaimpl.NativeMediaPlayer
    protected float playerGetRate() throws MediaException {
        float[] rate = new float[1];
        handleError(iosGetRate(this.iosMedia.getNativeMediaRef(), rate));
        return rate[0];
    }

    @Override // com.sun.media.jfxmediaimpl.NativeMediaPlayer
    protected void playerSetRate(float rate) throws MediaException {
        handleError(iosSetRate(this.iosMedia.getNativeMediaRef(), rate));
    }

    @Override // com.sun.media.jfxmediaimpl.NativeMediaPlayer
    protected double playerGetPresentationTime() throws MediaException {
        double[] presentationTime = new double[1];
        handleError(iosGetPresentationTime(this.iosMedia.getNativeMediaRef(), presentationTime));
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
            handleError(iosGetVolume(this.iosMedia.getNativeMediaRef(), volume));
            return volume[0];
        }
    }

    @Override // com.sun.media.jfxmediaimpl.NativeMediaPlayer
    protected synchronized void playerSetVolume(float volume) throws MediaException {
        if (!this.muteEnabled) {
            int err = iosSetVolume(this.iosMedia.getNativeMediaRef(), volume);
            if (0 != err) {
                handleError(err);
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
        handleError(iosGetBalance(this.iosMedia.getNativeMediaRef(), balance));
        return balance[0];
    }

    @Override // com.sun.media.jfxmediaimpl.NativeMediaPlayer
    protected void playerSetBalance(float balance) throws MediaException {
        handleError(iosSetBalance(this.iosMedia.getNativeMediaRef(), balance));
    }

    @Override // com.sun.media.jfxmediaimpl.NativeMediaPlayer
    protected double playerGetDuration() throws MediaException {
        double duration;
        double[] durationArr = new double[1];
        handleError(iosGetDuration(this.iosMedia.getNativeMediaRef(), durationArr));
        if (durationArr[0] == -1.0d) {
            duration = Double.POSITIVE_INFINITY;
        } else {
            duration = durationArr[0];
        }
        return duration;
    }

    @Override // com.sun.media.jfxmediaimpl.NativeMediaPlayer
    protected void playerSeek(double streamTime) throws MediaException {
        handleError(iosSeek(this.iosMedia.getNativeMediaRef(), streamTime));
    }

    @Override // com.sun.media.jfxmediaimpl.NativeMediaPlayer
    protected void playerInit() throws MediaException {
    }

    @Override // com.sun.media.jfxmediaimpl.NativeMediaPlayer
    protected void playerFinish() throws MediaException {
        handleError(iosFinish(this.iosMedia.getNativeMediaRef()));
    }

    @Override // com.sun.media.jfxmediaimpl.NativeMediaPlayer
    protected void playerDispose() {
        iosDispose(this.iosMedia.getNativeMediaRef());
        this.iosMedia = null;
    }

    /* loaded from: jfxrt.jar:com/sun/media/jfxmediaimpl/platform/ios/IOSMediaPlayer$NullAudioEQ.class */
    private static final class NullAudioEQ implements AudioEqualizer {
        private boolean enabled;
        private Map<Double, EqualizerBand> bands;

        private NullAudioEQ() {
            this.enabled = false;
            this.bands = new HashMap();
        }

        @Override // com.sun.media.jfxmedia.effects.AudioEqualizer
        public boolean getEnabled() {
            return this.enabled;
        }

        @Override // com.sun.media.jfxmedia.effects.AudioEqualizer
        public void setEnabled(boolean bEnable) {
            this.enabled = bEnable;
        }

        @Override // com.sun.media.jfxmedia.effects.AudioEqualizer
        public EqualizerBand addBand(double centerFrequency, double bandwidth, double gain) {
            Double key = new Double(centerFrequency);
            if (this.bands.containsKey(key)) {
                removeBand(centerFrequency);
            }
            EqualizerBand newBand = new NullEQBand(centerFrequency, bandwidth, gain);
            this.bands.put(key, newBand);
            return newBand;
        }

        @Override // com.sun.media.jfxmedia.effects.AudioEqualizer
        public boolean removeBand(double centerFrequency) {
            Double key = new Double(centerFrequency);
            if (this.bands.containsKey(key)) {
                this.bands.remove(key);
                return true;
            }
            return false;
        }
    }

    /* loaded from: jfxrt.jar:com/sun/media/jfxmediaimpl/platform/ios/IOSMediaPlayer$NullAudioSpectrum.class */
    private static final class NullAudioSpectrum implements AudioSpectrum {
        private boolean enabled;
        private int bandCount;
        private double interval;
        private int threshold;
        private float[] fakeData;

        private NullAudioSpectrum() {
            this.enabled = false;
            this.bandCount = 128;
            this.interval = 0.1d;
            this.threshold = 60;
        }

        @Override // com.sun.media.jfxmedia.effects.AudioSpectrum
        public boolean getEnabled() {
            return this.enabled;
        }

        @Override // com.sun.media.jfxmedia.effects.AudioSpectrum
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        @Override // com.sun.media.jfxmedia.effects.AudioSpectrum
        public int getBandCount() {
            return this.bandCount;
        }

        @Override // com.sun.media.jfxmedia.effects.AudioSpectrum
        public void setBandCount(int bands) {
            this.bandCount = bands;
            this.fakeData = new float[this.bandCount];
        }

        @Override // com.sun.media.jfxmedia.effects.AudioSpectrum
        public double getInterval() {
            return this.interval;
        }

        @Override // com.sun.media.jfxmedia.effects.AudioSpectrum
        public void setInterval(double interval) {
            this.interval = interval;
        }

        @Override // com.sun.media.jfxmedia.effects.AudioSpectrum
        public int getSensitivityThreshold() {
            return this.threshold;
        }

        @Override // com.sun.media.jfxmedia.effects.AudioSpectrum
        public void setSensitivityThreshold(int threshold) {
            this.threshold = threshold;
        }

        @Override // com.sun.media.jfxmedia.effects.AudioSpectrum
        public float[] getMagnitudes(float[] mag) {
            int size = this.fakeData.length;
            if (mag == null || mag.length < size) {
                mag = new float[size];
            }
            System.arraycopy(this.fakeData, 0, mag, 0, size);
            return mag;
        }

        @Override // com.sun.media.jfxmedia.effects.AudioSpectrum
        public float[] getPhases(float[] phs) {
            int size = this.fakeData.length;
            if (phs == null || phs.length < size) {
                phs = new float[size];
            }
            System.arraycopy(this.fakeData, 0, phs, 0, size);
            return phs;
        }
    }

    /* loaded from: jfxrt.jar:com/sun/media/jfxmediaimpl/platform/ios/IOSMediaPlayer$NullEQBand.class */
    private static final class NullEQBand implements EqualizerBand {
        private double center;
        private double bandwidth;
        private double gain;

        NullEQBand(double center, double bandwidth, double gain) {
            this.center = center;
            this.bandwidth = bandwidth;
            this.gain = gain;
        }

        @Override // com.sun.media.jfxmedia.effects.EqualizerBand
        public double getCenterFrequency() {
            return this.center;
        }

        @Override // com.sun.media.jfxmedia.effects.EqualizerBand
        public void setCenterFrequency(double centerFrequency) {
            this.center = centerFrequency;
        }

        @Override // com.sun.media.jfxmedia.effects.EqualizerBand
        public double getBandwidth() {
            return this.bandwidth;
        }

        @Override // com.sun.media.jfxmedia.effects.EqualizerBand
        public void setBandwidth(double bandwidth) {
            this.bandwidth = bandwidth;
        }

        @Override // com.sun.media.jfxmedia.effects.EqualizerBand
        public double getGain() {
            return this.gain;
        }

        @Override // com.sun.media.jfxmedia.effects.EqualizerBand
        public void setGain(double gain) {
            this.gain = gain;
        }
    }

    /* loaded from: jfxrt.jar:com/sun/media/jfxmediaimpl/platform/ios/IOSMediaPlayer$MediaPlayerOverlayImpl.class */
    private final class MediaPlayerOverlayImpl implements MediaPlayerOverlay {
        private MediaPlayerOverlayImpl() {
        }

        @Override // com.sun.media.jfxmedia.control.MediaPlayerOverlay
        public void setOverlayX(double x2) throws MediaException {
            IOSMediaPlayer.this.handleError(IOSMediaPlayer.this.iosSetOverlayX(IOSMediaPlayer.this.iosMedia.getNativeMediaRef(), x2));
        }

        @Override // com.sun.media.jfxmedia.control.MediaPlayerOverlay
        public void setOverlayY(double y2) throws MediaException {
            IOSMediaPlayer.this.handleError(IOSMediaPlayer.this.iosSetOverlayY(IOSMediaPlayer.this.iosMedia.getNativeMediaRef(), y2));
        }

        @Override // com.sun.media.jfxmedia.control.MediaPlayerOverlay
        public void setOverlayVisible(boolean visible) throws MediaException {
            IOSMediaPlayer.this.handleError(IOSMediaPlayer.this.iosSetOverlayVisible(IOSMediaPlayer.this.iosMedia.getNativeMediaRef(), visible));
        }

        @Override // com.sun.media.jfxmedia.control.MediaPlayerOverlay
        public void setOverlayWidth(double width) throws MediaException {
            IOSMediaPlayer.this.handleError(IOSMediaPlayer.this.iosSetOverlayWidth(IOSMediaPlayer.this.iosMedia.getNativeMediaRef(), width));
        }

        @Override // com.sun.media.jfxmedia.control.MediaPlayerOverlay
        public void setOverlayHeight(double height) throws MediaException {
            IOSMediaPlayer.this.handleError(IOSMediaPlayer.this.iosSetOverlayHeight(IOSMediaPlayer.this.iosMedia.getNativeMediaRef(), height));
        }

        @Override // com.sun.media.jfxmedia.control.MediaPlayerOverlay
        public void setOverlayPreserveRatio(boolean preserveRatio) throws MediaException {
            IOSMediaPlayer.this.handleError(IOSMediaPlayer.this.iosSetOverlayPreserveRatio(IOSMediaPlayer.this.iosMedia.getNativeMediaRef(), preserveRatio));
        }

        @Override // com.sun.media.jfxmedia.control.MediaPlayerOverlay
        public void setOverlayOpacity(double opacity) throws MediaException {
            IOSMediaPlayer.this.handleError(IOSMediaPlayer.this.iosSetOverlayOpacity(IOSMediaPlayer.this.iosMedia.getNativeMediaRef(), opacity));
        }

        @Override // com.sun.media.jfxmedia.control.MediaPlayerOverlay
        public void setOverlayTransform(double mxx, double mxy, double mxz, double mxt, double myx, double myy, double myz, double myt, double mzx, double mzy, double mzz, double mzt) throws MediaException {
            IOSMediaPlayer.this.handleError(IOSMediaPlayer.this.iosSetOverlayTransform(IOSMediaPlayer.this.iosMedia.getNativeMediaRef(), mxx, mxy, mxz, mxt, myx, myy, myz, myt, mzx, mzy, mzz, mzt));
        }
    }
}
