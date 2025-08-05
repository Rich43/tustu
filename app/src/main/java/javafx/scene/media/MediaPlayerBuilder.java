package javafx.scene.media;

import javafx.event.EventHandler;
import javafx.util.Builder;
import javafx.util.Duration;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/media/MediaPlayerBuilder.class */
public final class MediaPlayerBuilder implements Builder<MediaPlayer> {
    private int __set;
    private double audioSpectrumInterval;
    private AudioSpectrumListener audioSpectrumListener;
    private int audioSpectrumNumBands;
    private int audioSpectrumThreshold;
    private boolean autoPlay;
    private double balance;
    private int cycleCount;
    private Media media;
    private boolean mute;
    private Runnable onEndOfMedia;
    private Runnable onError;
    private Runnable onHalted;
    private EventHandler<MediaMarkerEvent> onMarker;
    private Runnable onPaused;
    private Runnable onPlaying;
    private Runnable onReady;
    private Runnable onRepeat;
    private Runnable onStalled;
    private Runnable onStopped;
    private double rate;
    private Duration startTime;
    private Duration stopTime;
    private double volume;

    protected MediaPlayerBuilder() {
    }

    public static MediaPlayerBuilder create() {
        return new MediaPlayerBuilder();
    }

    private void __set(int i2) {
        this.__set |= 1 << i2;
    }

    public void applyTo(MediaPlayer x2) {
        int set = this.__set;
        while (set != 0) {
            int i2 = Integer.numberOfTrailingZeros(set);
            set &= (1 << i2) ^ (-1);
            switch (i2) {
                case 0:
                    x2.setAudioSpectrumInterval(this.audioSpectrumInterval);
                    break;
                case 1:
                    x2.setAudioSpectrumListener(this.audioSpectrumListener);
                    break;
                case 2:
                    x2.setAudioSpectrumNumBands(this.audioSpectrumNumBands);
                    break;
                case 3:
                    x2.setAudioSpectrumThreshold(this.audioSpectrumThreshold);
                    break;
                case 4:
                    x2.setAutoPlay(this.autoPlay);
                    break;
                case 5:
                    x2.setBalance(this.balance);
                    break;
                case 6:
                    x2.setCycleCount(this.cycleCount);
                    break;
                case 7:
                    x2.setMute(this.mute);
                    break;
                case 8:
                    x2.setOnEndOfMedia(this.onEndOfMedia);
                    break;
                case 9:
                    x2.setOnError(this.onError);
                    break;
                case 10:
                    x2.setOnHalted(this.onHalted);
                    break;
                case 11:
                    x2.setOnMarker(this.onMarker);
                    break;
                case 12:
                    x2.setOnPaused(this.onPaused);
                    break;
                case 13:
                    x2.setOnPlaying(this.onPlaying);
                    break;
                case 14:
                    x2.setOnReady(this.onReady);
                    break;
                case 15:
                    x2.setOnRepeat(this.onRepeat);
                    break;
                case 16:
                    x2.setOnStalled(this.onStalled);
                    break;
                case 17:
                    x2.setOnStopped(this.onStopped);
                    break;
                case 18:
                    x2.setRate(this.rate);
                    break;
                case 19:
                    x2.setStartTime(this.startTime);
                    break;
                case 20:
                    x2.setStopTime(this.stopTime);
                    break;
                case 21:
                    x2.setVolume(this.volume);
                    break;
            }
        }
    }

    public MediaPlayerBuilder audioSpectrumInterval(double x2) {
        this.audioSpectrumInterval = x2;
        __set(0);
        return this;
    }

    public MediaPlayerBuilder audioSpectrumListener(AudioSpectrumListener x2) {
        this.audioSpectrumListener = x2;
        __set(1);
        return this;
    }

    public MediaPlayerBuilder audioSpectrumNumBands(int x2) {
        this.audioSpectrumNumBands = x2;
        __set(2);
        return this;
    }

    public MediaPlayerBuilder audioSpectrumThreshold(int x2) {
        this.audioSpectrumThreshold = x2;
        __set(3);
        return this;
    }

    public MediaPlayerBuilder autoPlay(boolean x2) {
        this.autoPlay = x2;
        __set(4);
        return this;
    }

    public MediaPlayerBuilder balance(double x2) {
        this.balance = x2;
        __set(5);
        return this;
    }

    public MediaPlayerBuilder cycleCount(int x2) {
        this.cycleCount = x2;
        __set(6);
        return this;
    }

    public MediaPlayerBuilder media(Media x2) {
        this.media = x2;
        return this;
    }

    public MediaPlayerBuilder mute(boolean x2) {
        this.mute = x2;
        __set(7);
        return this;
    }

    public MediaPlayerBuilder onEndOfMedia(Runnable x2) {
        this.onEndOfMedia = x2;
        __set(8);
        return this;
    }

    public MediaPlayerBuilder onError(Runnable x2) {
        this.onError = x2;
        __set(9);
        return this;
    }

    public MediaPlayerBuilder onHalted(Runnable x2) {
        this.onHalted = x2;
        __set(10);
        return this;
    }

    public MediaPlayerBuilder onMarker(EventHandler<MediaMarkerEvent> x2) {
        this.onMarker = x2;
        __set(11);
        return this;
    }

    public MediaPlayerBuilder onPaused(Runnable x2) {
        this.onPaused = x2;
        __set(12);
        return this;
    }

    public MediaPlayerBuilder onPlaying(Runnable x2) {
        this.onPlaying = x2;
        __set(13);
        return this;
    }

    public MediaPlayerBuilder onReady(Runnable x2) {
        this.onReady = x2;
        __set(14);
        return this;
    }

    public MediaPlayerBuilder onRepeat(Runnable x2) {
        this.onRepeat = x2;
        __set(15);
        return this;
    }

    public MediaPlayerBuilder onStalled(Runnable x2) {
        this.onStalled = x2;
        __set(16);
        return this;
    }

    public MediaPlayerBuilder onStopped(Runnable x2) {
        this.onStopped = x2;
        __set(17);
        return this;
    }

    public MediaPlayerBuilder rate(double x2) {
        this.rate = x2;
        __set(18);
        return this;
    }

    public MediaPlayerBuilder startTime(Duration x2) {
        this.startTime = x2;
        __set(19);
        return this;
    }

    public MediaPlayerBuilder stopTime(Duration x2) {
        this.stopTime = x2;
        __set(20);
        return this;
    }

    public MediaPlayerBuilder volume(double x2) {
        this.volume = x2;
        __set(21);
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    /* renamed from: build */
    public MediaPlayer build2() {
        MediaPlayer x2 = new MediaPlayer(this.media);
        applyTo(x2);
        return x2;
    }
}
