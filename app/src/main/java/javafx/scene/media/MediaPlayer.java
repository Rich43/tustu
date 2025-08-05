package javafx.scene.media;

import com.sun.javafx.tk.TKPulseListener;
import com.sun.javafx.tk.Toolkit;
import com.sun.media.jfxmedia.MediaManager;
import com.sun.media.jfxmedia.control.VideoDataBuffer;
import com.sun.media.jfxmedia.events.AudioSpectrumEvent;
import com.sun.media.jfxmedia.events.BufferListener;
import com.sun.media.jfxmedia.events.BufferProgressEvent;
import com.sun.media.jfxmedia.events.MarkerEvent;
import com.sun.media.jfxmedia.events.MarkerListener;
import com.sun.media.jfxmedia.events.MediaErrorListener;
import com.sun.media.jfxmedia.events.NewFrameEvent;
import com.sun.media.jfxmedia.events.PlayerStateEvent;
import com.sun.media.jfxmedia.events.PlayerStateListener;
import com.sun.media.jfxmedia.events.PlayerTimeListener;
import com.sun.media.jfxmedia.events.VideoRendererListener;
import com.sun.media.jfxmedia.events.VideoTrackSizeListener;
import com.sun.media.jfxmedia.locator.Locator;
import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.jar.Pack200;
import javafx.application.Platform;
import javafx.beans.NamedArg;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.IntegerPropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.event.EventHandler;
import javafx.scene.media.MediaException;
import javafx.util.Duration;
import javafx.util.Pair;

/* loaded from: jfxrt.jar:javafx/scene/media/MediaPlayer.class */
public final class MediaPlayer {
    public static final int INDEFINITE = -1;
    private static final double RATE_MIN = 0.0d;
    private static final double RATE_MAX = 8.0d;
    private static final int AUDIOSPECTRUM_THRESHOLD_MAX = 0;
    private static final double AUDIOSPECTRUM_INTERVAL_MIN = 1.0E-9d;
    private static final int AUDIOSPECTRUM_NUMBANDS_MIN = 2;
    private com.sun.media.jfxmedia.MediaPlayer jfxPlayer;
    private MediaErrorListener errorListener;
    private static final int DEFAULT_SPECTRUM_BAND_COUNT = 128;
    private static final double DEFAULT_SPECTRUM_INTERVAL = 0.1d;
    private static final int DEFAULT_SPECTRUM_THRESHOLD = -60;
    private AudioEqualizer audioEqualizer;
    private ReadOnlyObjectWrapper<MediaException> error;
    private ObjectProperty<Runnable> onError;
    private Media media;
    private BooleanProperty autoPlay;
    private boolean playerReady;
    private DoubleProperty rate;
    private ReadOnlyDoubleWrapper currentRate;
    private DoubleProperty volume;
    private DoubleProperty balance;
    private ObjectProperty<Duration> startTime;
    private ObjectProperty<Duration> stopTime;
    private ReadOnlyObjectWrapper<Duration> cycleDuration;
    private ReadOnlyObjectWrapper<Duration> totalDuration;
    private ReadOnlyObjectWrapper<Duration> currentTime;
    private ReadOnlyObjectWrapper<Status> status;
    private ReadOnlyObjectWrapper<Duration> bufferProgressTime;
    private IntegerProperty cycleCount;
    private ReadOnlyIntegerWrapper currentCount;
    private BooleanProperty mute;
    private ObjectProperty<EventHandler<MediaMarkerEvent>> onMarker;
    private ObjectProperty<Runnable> onEndOfMedia;
    private ObjectProperty<Runnable> onReady;
    private ObjectProperty<Runnable> onPlaying;
    private ObjectProperty<Runnable> onPaused;
    private ObjectProperty<Runnable> onStopped;
    private ObjectProperty<Runnable> onHalted;
    private ObjectProperty<Runnable> onRepeat;
    private ObjectProperty<Runnable> onStalled;
    private IntegerProperty audioSpectrumNumBands;
    private DoubleProperty audioSpectrumInterval;
    private IntegerProperty audioSpectrumThreshold;
    private ObjectProperty<AudioSpectrumListener> audioSpectrumListener;
    private VideoDataBuffer currentRenderFrame;
    private VideoDataBuffer nextRenderFrame;
    private MapChangeListener<String, Duration> markerMapListener = null;
    private MarkerListener markerEventListener = null;
    private PlayerStateListener stateListener = null;
    private PlayerTimeListener timeListener = null;
    private VideoTrackSizeListener sizeListener = null;
    private BufferListener bufferListener = null;
    private com.sun.media.jfxmedia.events.AudioSpectrumListener spectrumListener = null;
    private RendererListener rendererListener = null;
    private boolean rateChangeRequested = false;
    private boolean volumeChangeRequested = false;
    private boolean balanceChangeRequested = false;
    private boolean startTimeChangeRequested = false;
    private boolean stopTimeChangeRequested = false;
    private boolean muteChangeRequested = false;
    private boolean playRequested = false;
    private boolean audioSpectrumNumBandsChangeRequested = false;
    private boolean audioSpectrumIntervalChangeRequested = false;
    private boolean audioSpectrumThresholdChangeRequested = false;
    private boolean audioSpectrumEnabledChangeRequested = false;
    private MediaTimerTask mediaTimerTask = null;
    private double prevTimeMs = -1.0d;
    private boolean isUpdateTimeEnabled = false;
    private BufferProgressEvent lastBufferEvent = null;
    private Duration startTimeAtStop = null;
    private boolean isEOS = false;
    private final Object disposeLock = new Object();
    private final Set<WeakReference<MediaView>> viewRefs = new HashSet();
    private final Object renderLock = new Object();

    /* loaded from: jfxrt.jar:javafx/scene/media/MediaPlayer$Status.class */
    public enum Status {
        UNKNOWN,
        READY,
        PAUSED,
        PLAYING,
        STOPPED,
        STALLED,
        HALTED,
        DISPOSED
    }

    com.sun.media.jfxmedia.MediaPlayer retrieveJfxPlayer() {
        com.sun.media.jfxmedia.MediaPlayer mediaPlayer;
        synchronized (this.disposeLock) {
            mediaPlayer = this.jfxPlayer;
        }
        return mediaPlayer;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static double clamp(double dvalue, double dmin, double dmax) {
        if (dmin != Double.MIN_VALUE && dvalue < dmin) {
            return dmin;
        }
        if (dmax != Double.MAX_VALUE && dvalue > dmax) {
            return dmax;
        }
        return dvalue;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int clamp(int ivalue, int imin, int imax) {
        if (imin != Integer.MIN_VALUE && ivalue < imin) {
            return imin;
        }
        if (imax != Integer.MAX_VALUE && ivalue > imax) {
            return imax;
        }
        return ivalue;
    }

    public final AudioEqualizer getAudioEqualizer() {
        synchronized (this.disposeLock) {
            if (getStatus() == Status.DISPOSED) {
                return null;
            }
            if (this.audioEqualizer == null) {
                this.audioEqualizer = new AudioEqualizer();
                if (this.jfxPlayer != null) {
                    this.audioEqualizer.setAudioEqualizer(this.jfxPlayer.getEqualizer());
                }
                this.audioEqualizer.setEnabled(true);
            }
            return this.audioEqualizer;
        }
    }

    public MediaPlayer(@NamedArg("media") Media media) {
        this.errorListener = null;
        if (null == media) {
            throw new NullPointerException("media == null!");
        }
        this.media = media;
        this.errorListener = new _MediaErrorListener();
        MediaManager.addMediaErrorListener(this.errorListener);
        try {
            Locator locator = media.retrieveJfxLocator();
            if (locator.canBlock()) {
                InitMediaPlayer initMediaPlayer = new InitMediaPlayer();
                Thread t2 = new Thread(initMediaPlayer);
                t2.setDaemon(true);
                t2.start();
            } else {
                init();
            }
        } catch (com.sun.media.jfxmedia.MediaException e2) {
            throw MediaException.exceptionToMediaException(e2);
        } catch (MediaException e3) {
            throw e3;
        }
    }

    void registerListeners() {
        synchronized (this.disposeLock) {
            if (getStatus() == Status.DISPOSED) {
                return;
            }
            if (this.jfxPlayer != null) {
                MediaManager.registerMediaPlayerForDispose(this, this.jfxPlayer);
                this.jfxPlayer.addMediaErrorListener(this.errorListener);
                this.jfxPlayer.addMediaTimeListener(this.timeListener);
                this.jfxPlayer.addVideoTrackSizeListener(this.sizeListener);
                this.jfxPlayer.addBufferListener(this.bufferListener);
                this.jfxPlayer.addMarkerListener(this.markerEventListener);
                this.jfxPlayer.addAudioSpectrumListener(this.spectrumListener);
                this.jfxPlayer.getVideoRenderControl().addVideoRendererListener(this.rendererListener);
                this.jfxPlayer.addMediaPlayerListener(this.stateListener);
            }
            if (null != this.rendererListener) {
                Toolkit.getToolkit().addStageTkPulseListener(this.rendererListener);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void init() throws MediaException {
        Duration markerTime;
        try {
            Locator locator = this.media.retrieveJfxLocator();
            locator.waitForReadySignal();
            synchronized (this.disposeLock) {
                if (getStatus() == Status.DISPOSED) {
                    return;
                }
                this.jfxPlayer = MediaManager.getPlayer(locator);
                if (this.jfxPlayer != null) {
                    MediaPlayerShutdownHook.addMediaPlayer(this);
                    this.jfxPlayer.setBalance((float) getBalance());
                    this.jfxPlayer.setMute(isMute());
                    this.jfxPlayer.setVolume((float) getVolume());
                    this.sizeListener = new _VideoTrackSizeListener();
                    this.stateListener = new _PlayerStateListener();
                    this.timeListener = new _PlayerTimeListener();
                    this.bufferListener = new _BufferListener();
                    this.markerEventListener = new _MarkerListener();
                    this.spectrumListener = new _SpectrumListener();
                    this.rendererListener = new RendererListener();
                }
                this.markerMapListener = new MarkerMapChangeListener();
                ObservableMap<String, Duration> markers = this.media.getMarkers();
                markers.addListener(this.markerMapListener);
                com.sun.media.jfxmedia.Media jfxMedia = this.jfxPlayer.getMedia();
                for (Map.Entry<String, Duration> entry : markers.entrySet()) {
                    String markerName = entry.getKey();
                    if (markerName != null && (markerTime = entry.getValue()) != null) {
                        double msec = markerTime.toMillis();
                        if (msec >= 0.0d) {
                            jfxMedia.addMarker(markerName, msec / 1000.0d);
                        }
                    }
                }
                Platform.runLater(() -> {
                    registerListeners();
                });
            }
        } catch (com.sun.media.jfxmedia.MediaException e2) {
            throw MediaException.exceptionToMediaException(e2);
        }
    }

    /* loaded from: jfxrt.jar:javafx/scene/media/MediaPlayer$InitMediaPlayer.class */
    private class InitMediaPlayer implements Runnable {
        private InitMediaPlayer() {
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                MediaPlayer.this.init();
            } catch (com.sun.media.jfxmedia.MediaException e2) {
                MediaPlayer.this.handleError(MediaException.exceptionToMediaException(e2));
            } catch (MediaException e3) {
                if (MediaPlayer.this.media.getError() != null) {
                    MediaPlayer.this.handleError(MediaPlayer.this.media.getError());
                } else {
                    MediaPlayer.this.handleError(e3);
                }
            } catch (Exception e4) {
                MediaPlayer.this.handleError(new MediaException(MediaException.Type.UNKNOWN, e4.getMessage()));
            }
        }
    }

    private void setError(MediaException value) {
        if (getError() == null) {
            errorPropertyImpl().set(value);
        }
    }

    public final MediaException getError() {
        if (this.error == null) {
            return null;
        }
        return this.error.get();
    }

    public ReadOnlyObjectProperty<MediaException> errorProperty() {
        return errorPropertyImpl().getReadOnlyProperty();
    }

    private ReadOnlyObjectWrapper<MediaException> errorPropertyImpl() {
        if (this.error == null) {
            this.error = new ReadOnlyObjectWrapper<MediaException>() { // from class: javafx.scene.media.MediaPlayer.1
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    if (MediaPlayer.this.getOnError() != null) {
                        Platform.runLater(MediaPlayer.this.getOnError());
                    }
                }

                @Override // javafx.beans.property.SimpleObjectProperty, javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return MediaPlayer.this;
                }

                @Override // javafx.beans.property.SimpleObjectProperty, javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return Pack200.Packer.ERROR;
                }
            };
        }
        return this.error;
    }

    public final void setOnError(Runnable value) {
        onErrorProperty().set(value);
    }

    public final Runnable getOnError() {
        if (this.onError == null) {
            return null;
        }
        return this.onError.get();
    }

    public ObjectProperty<Runnable> onErrorProperty() {
        if (this.onError == null) {
            this.onError = new ObjectPropertyBase<Runnable>() { // from class: javafx.scene.media.MediaPlayer.2
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    if (get() != null && MediaPlayer.this.getError() != null) {
                        Platform.runLater(get());
                    }
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return MediaPlayer.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "onError";
                }
            };
        }
        return this.onError;
    }

    public final Media getMedia() {
        return this.media;
    }

    public final void setAutoPlay(boolean value) {
        autoPlayProperty().set(value);
    }

    public final boolean isAutoPlay() {
        if (this.autoPlay == null) {
            return false;
        }
        return this.autoPlay.get();
    }

    public BooleanProperty autoPlayProperty() {
        if (this.autoPlay == null) {
            this.autoPlay = new BooleanPropertyBase() { // from class: javafx.scene.media.MediaPlayer.3
                @Override // javafx.beans.property.BooleanPropertyBase
                protected void invalidated() {
                    if (!MediaPlayer.this.autoPlay.get()) {
                        MediaPlayer.this.playRequested = false;
                    } else {
                        MediaPlayer.this.play();
                    }
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return MediaPlayer.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "autoPlay";
                }
            };
        }
        return this.autoPlay;
    }

    public void play() {
        synchronized (this.disposeLock) {
            if (getStatus() != Status.DISPOSED) {
                if (this.playerReady) {
                    this.jfxPlayer.play();
                } else {
                    this.playRequested = true;
                }
            }
        }
    }

    public void pause() {
        synchronized (this.disposeLock) {
            if (getStatus() != Status.DISPOSED) {
                if (this.playerReady) {
                    this.jfxPlayer.pause();
                } else {
                    this.playRequested = false;
                }
            }
        }
    }

    public void stop() {
        synchronized (this.disposeLock) {
            if (getStatus() != Status.DISPOSED) {
                if (this.playerReady) {
                    this.jfxPlayer.stop();
                    setCurrentCount(0);
                    destroyMediaTimer();
                } else {
                    this.playRequested = false;
                }
            }
        }
    }

    public final void setRate(double value) {
        rateProperty().set(value);
    }

    public final double getRate() {
        if (this.rate == null) {
            return 1.0d;
        }
        return this.rate.get();
    }

    public DoubleProperty rateProperty() {
        if (this.rate == null) {
            this.rate = new DoublePropertyBase(1.0d) { // from class: javafx.scene.media.MediaPlayer.4
                @Override // javafx.beans.property.DoublePropertyBase
                protected void invalidated() {
                    synchronized (MediaPlayer.this.disposeLock) {
                        if (MediaPlayer.this.getStatus() != Status.DISPOSED) {
                            if (!MediaPlayer.this.playerReady) {
                                MediaPlayer.this.rateChangeRequested = true;
                            } else if (MediaPlayer.this.jfxPlayer.getDuration() != Double.POSITIVE_INFINITY) {
                                MediaPlayer.this.jfxPlayer.setRate((float) MediaPlayer.clamp(MediaPlayer.this.rate.get(), 0.0d, MediaPlayer.RATE_MAX));
                            }
                        }
                    }
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return MediaPlayer.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "rate";
                }
            };
        }
        return this.rate;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setCurrentRate(double value) {
        currentRatePropertyImpl().set(value);
    }

    public final double getCurrentRate() {
        if (this.currentRate == null) {
            return 0.0d;
        }
        return this.currentRate.get();
    }

    public ReadOnlyDoubleProperty currentRateProperty() {
        return currentRatePropertyImpl().getReadOnlyProperty();
    }

    private ReadOnlyDoubleWrapper currentRatePropertyImpl() {
        if (this.currentRate == null) {
            this.currentRate = new ReadOnlyDoubleWrapper(this, "currentRate");
        }
        return this.currentRate;
    }

    public final void setVolume(double value) {
        volumeProperty().set(value);
    }

    public final double getVolume() {
        if (this.volume == null) {
            return 1.0d;
        }
        return this.volume.get();
    }

    public DoubleProperty volumeProperty() {
        if (this.volume == null) {
            this.volume = new DoublePropertyBase(1.0d) { // from class: javafx.scene.media.MediaPlayer.5
                @Override // javafx.beans.property.DoublePropertyBase
                protected void invalidated() {
                    synchronized (MediaPlayer.this.disposeLock) {
                        if (MediaPlayer.this.getStatus() != Status.DISPOSED) {
                            if (MediaPlayer.this.playerReady) {
                                MediaPlayer.this.jfxPlayer.setVolume((float) MediaPlayer.clamp(MediaPlayer.this.volume.get(), 0.0d, 1.0d));
                            } else {
                                MediaPlayer.this.volumeChangeRequested = true;
                            }
                        }
                    }
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return MediaPlayer.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "volume";
                }
            };
        }
        return this.volume;
    }

    public final void setBalance(double value) {
        balanceProperty().set(value);
    }

    public final double getBalance() {
        if (this.balance == null) {
            return 0.0d;
        }
        return this.balance.get();
    }

    public DoubleProperty balanceProperty() {
        if (this.balance == null) {
            this.balance = new DoublePropertyBase() { // from class: javafx.scene.media.MediaPlayer.6
                @Override // javafx.beans.property.DoublePropertyBase
                protected void invalidated() {
                    synchronized (MediaPlayer.this.disposeLock) {
                        if (MediaPlayer.this.getStatus() != Status.DISPOSED) {
                            if (MediaPlayer.this.playerReady) {
                                MediaPlayer.this.jfxPlayer.setBalance((float) MediaPlayer.clamp(MediaPlayer.this.balance.get(), -1.0d, 1.0d));
                            } else {
                                MediaPlayer.this.balanceChangeRequested = true;
                            }
                        }
                    }
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return MediaPlayer.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "balance";
                }
            };
        }
        return this.balance;
    }

    private double[] calculateStartStopTimes(Duration startValue, Duration stopValue) {
        double newStart;
        double newStop;
        if (startValue == null || startValue.lessThan(Duration.ZERO) || startValue.equals(Duration.UNKNOWN)) {
            newStart = 0.0d;
        } else if (startValue.equals(Duration.INDEFINITE)) {
            newStart = Double.MAX_VALUE;
        } else {
            newStart = startValue.toMillis() / 1000.0d;
        }
        if (stopValue == null || stopValue.equals(Duration.UNKNOWN) || stopValue.equals(Duration.INDEFINITE)) {
            newStop = Double.MAX_VALUE;
        } else if (stopValue.lessThan(Duration.ZERO)) {
            newStop = 0.0d;
        } else {
            newStop = stopValue.toMillis() / 1000.0d;
        }
        Duration mediaDuration = this.media.getDuration();
        double duration = mediaDuration == Duration.UNKNOWN ? Double.MAX_VALUE : mediaDuration.toMillis() / 1000.0d;
        double actualStart = clamp(newStart, 0.0d, duration);
        double actualStop = clamp(newStop, 0.0d, duration);
        if (actualStart > actualStop) {
            actualStop = actualStart;
        }
        return new double[]{actualStart, actualStop};
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setStartStopTimes(Duration startValue, boolean isStartValueSet, Duration stopValue, boolean isStopValueSet) {
        if (this.jfxPlayer.getDuration() == Double.POSITIVE_INFINITY) {
            return;
        }
        double[] startStop = calculateStartStopTimes(startValue, stopValue);
        if (isStartValueSet) {
            this.jfxPlayer.setStartTime(startStop[0]);
            if (getStatus() == Status.READY || getStatus() == Status.PAUSED) {
                Platform.runLater(() -> {
                    setCurrentTime(getStartTime());
                });
            }
        }
        if (isStopValueSet) {
            this.jfxPlayer.setStopTime(startStop[1]);
        }
    }

    public final void setStartTime(Duration value) {
        startTimeProperty().set(value);
    }

    public final Duration getStartTime() {
        return this.startTime == null ? Duration.ZERO : this.startTime.get();
    }

    public ObjectProperty<Duration> startTimeProperty() {
        if (this.startTime == null) {
            this.startTime = new ObjectPropertyBase<Duration>() { // from class: javafx.scene.media.MediaPlayer.7
                /* JADX WARN: Multi-variable type inference failed */
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    synchronized (MediaPlayer.this.disposeLock) {
                        if (MediaPlayer.this.getStatus() != Status.DISPOSED) {
                            if (MediaPlayer.this.playerReady) {
                                MediaPlayer.this.setStartStopTimes((Duration) MediaPlayer.this.startTime.get(), true, MediaPlayer.this.getStopTime(), false);
                            } else {
                                MediaPlayer.this.startTimeChangeRequested = true;
                            }
                            MediaPlayer.this.calculateCycleDuration();
                        }
                    }
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return MediaPlayer.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "startTime";
                }
            };
        }
        return this.startTime;
    }

    public final void setStopTime(Duration value) {
        stopTimeProperty().set(value);
    }

    public final Duration getStopTime() {
        return this.stopTime == null ? this.media.getDuration() : this.stopTime.get();
    }

    public ObjectProperty<Duration> stopTimeProperty() {
        if (this.stopTime == null) {
            this.stopTime = new ObjectPropertyBase<Duration>() { // from class: javafx.scene.media.MediaPlayer.8
                /* JADX WARN: Multi-variable type inference failed */
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    synchronized (MediaPlayer.this.disposeLock) {
                        if (MediaPlayer.this.getStatus() != Status.DISPOSED) {
                            if (MediaPlayer.this.playerReady) {
                                MediaPlayer.this.setStartStopTimes(MediaPlayer.this.getStartTime(), false, (Duration) MediaPlayer.this.stopTime.get(), true);
                            } else {
                                MediaPlayer.this.stopTimeChangeRequested = true;
                            }
                            MediaPlayer.this.calculateCycleDuration();
                        }
                    }
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return MediaPlayer.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "stopTime";
                }
            };
        }
        return this.stopTime;
    }

    private void setCycleDuration(Duration value) {
        cycleDurationPropertyImpl().set(value);
    }

    public final Duration getCycleDuration() {
        return this.cycleDuration == null ? Duration.UNKNOWN : this.cycleDuration.get();
    }

    public ReadOnlyObjectProperty<Duration> cycleDurationProperty() {
        return cycleDurationPropertyImpl().getReadOnlyProperty();
    }

    private ReadOnlyObjectWrapper<Duration> cycleDurationPropertyImpl() {
        if (this.cycleDuration == null) {
            this.cycleDuration = new ReadOnlyObjectWrapper<>(this, "cycleDuration");
        }
        return this.cycleDuration;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void calculateCycleDuration() {
        Duration endTime;
        Duration mediaDuration = this.media.getDuration();
        if (!getStopTime().isUnknown()) {
            endTime = getStopTime();
        } else {
            endTime = mediaDuration;
        }
        if (endTime.greaterThan(mediaDuration)) {
            endTime = mediaDuration;
        }
        if ((endTime.isUnknown() || getStartTime().isUnknown() || getStartTime().isIndefinite()) && !getCycleDuration().isUnknown()) {
            setCycleDuration(Duration.UNKNOWN);
        }
        setCycleDuration(endTime.subtract(getStartTime()));
        calculateTotalDuration();
    }

    private void setTotalDuration(Duration value) {
        totalDurationPropertyImpl().set(value);
    }

    public final Duration getTotalDuration() {
        return this.totalDuration == null ? Duration.UNKNOWN : this.totalDuration.get();
    }

    public ReadOnlyObjectProperty<Duration> totalDurationProperty() {
        return totalDurationPropertyImpl().getReadOnlyProperty();
    }

    private ReadOnlyObjectWrapper<Duration> totalDurationPropertyImpl() {
        if (this.totalDuration == null) {
            this.totalDuration = new ReadOnlyObjectWrapper<>(this, "totalDuration");
        }
        return this.totalDuration;
    }

    private void calculateTotalDuration() {
        if (getCycleCount() == -1) {
            setTotalDuration(Duration.INDEFINITE);
        } else if (getCycleDuration().isUnknown()) {
            setTotalDuration(Duration.UNKNOWN);
        } else {
            setTotalDuration(getCycleDuration().multiply(getCycleCount()));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setCurrentTime(Duration value) {
        currentTimePropertyImpl().set(value);
    }

    public final Duration getCurrentTime() {
        synchronized (this.disposeLock) {
            if (getStatus() == Status.DISPOSED) {
                return Duration.ZERO;
            }
            if (getStatus() == Status.STOPPED) {
                return Duration.millis(getStartTime().toMillis());
            }
            if (this.isEOS) {
                Duration duration = this.media.getDuration();
                Duration stopTime = getStopTime();
                if (stopTime != Duration.UNKNOWN && duration != Duration.UNKNOWN) {
                    if (stopTime.greaterThan(duration)) {
                        return Duration.millis(duration.toMillis());
                    }
                    return Duration.millis(stopTime.toMillis());
                }
            }
            Duration theCurrentTime = currentTimeProperty().get();
            if (this.playerReady) {
                double timeSeconds = this.jfxPlayer.getPresentationTime();
                if (timeSeconds >= 0.0d) {
                    theCurrentTime = Duration.seconds(timeSeconds);
                }
            }
            return theCurrentTime;
        }
    }

    public ReadOnlyObjectProperty<Duration> currentTimeProperty() {
        return currentTimePropertyImpl().getReadOnlyProperty();
    }

    private ReadOnlyObjectWrapper<Duration> currentTimePropertyImpl() {
        if (this.currentTime == null) {
            this.currentTime = new ReadOnlyObjectWrapper<>(this, "currentTime");
            this.currentTime.setValue(Duration.ZERO);
            updateTime();
        }
        return this.currentTime;
    }

    public void seek(Duration seekTime) {
        double seekSeconds;
        Status playerStatus;
        synchronized (this.disposeLock) {
            if (getStatus() == Status.DISPOSED) {
                return;
            }
            if (this.playerReady && seekTime != null && !seekTime.isUnknown()) {
                if (this.jfxPlayer.getDuration() == Double.POSITIVE_INFINITY) {
                    return;
                }
                if (seekTime.isIndefinite()) {
                    Duration duration = this.media.getDuration();
                    if (duration == null || duration.isUnknown() || duration.isIndefinite()) {
                        duration = Duration.millis(Double.MAX_VALUE);
                    }
                    seekSeconds = duration.toMillis() / 1000.0d;
                } else {
                    seekSeconds = seekTime.toMillis() / 1000.0d;
                    double[] startStop = calculateStartStopTimes(getStartTime(), getStopTime());
                    if (seekSeconds < startStop[0]) {
                        seekSeconds = startStop[0];
                    } else if (seekSeconds > startStop[1]) {
                        seekSeconds = startStop[1];
                    }
                }
                if (!this.isUpdateTimeEnabled && (((playerStatus = getStatus()) == Status.PLAYING || playerStatus == Status.PAUSED) && getStartTime().toSeconds() <= seekSeconds && seekSeconds <= getStopTime().toSeconds())) {
                    this.isEOS = false;
                    this.isUpdateTimeEnabled = true;
                    setCurrentRate(getRate());
                }
                this.jfxPlayer.seek(seekSeconds);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setStatus(Status value) {
        statusPropertyImpl().set(value);
    }

    public final Status getStatus() {
        return this.status == null ? Status.UNKNOWN : this.status.get();
    }

    public ReadOnlyObjectProperty<Status> statusProperty() {
        return statusPropertyImpl().getReadOnlyProperty();
    }

    private ReadOnlyObjectWrapper<Status> statusPropertyImpl() {
        if (this.status == null) {
            this.status = new ReadOnlyObjectWrapper<Status>() { // from class: javafx.scene.media.MediaPlayer.9
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    if (get() == Status.PLAYING) {
                        MediaPlayer.this.setCurrentRate(MediaPlayer.this.getRate());
                    } else {
                        MediaPlayer.this.setCurrentRate(0.0d);
                    }
                    if (get() == Status.READY) {
                        if (MediaPlayer.this.getOnReady() != null) {
                            Platform.runLater(MediaPlayer.this.getOnReady());
                            return;
                        }
                        return;
                    }
                    if (get() == Status.PLAYING) {
                        if (MediaPlayer.this.getOnPlaying() != null) {
                            Platform.runLater(MediaPlayer.this.getOnPlaying());
                        }
                    } else if (get() == Status.PAUSED) {
                        if (MediaPlayer.this.getOnPaused() != null) {
                            Platform.runLater(MediaPlayer.this.getOnPaused());
                        }
                    } else if (get() == Status.STOPPED) {
                        if (MediaPlayer.this.getOnStopped() != null) {
                            Platform.runLater(MediaPlayer.this.getOnStopped());
                        }
                    } else if (get() == Status.STALLED && MediaPlayer.this.getOnStalled() != null) {
                        Platform.runLater(MediaPlayer.this.getOnStalled());
                    }
                }

                @Override // javafx.beans.property.SimpleObjectProperty, javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return MediaPlayer.this;
                }

                @Override // javafx.beans.property.SimpleObjectProperty, javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "status";
                }
            };
        }
        return this.status;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setBufferProgressTime(Duration value) {
        bufferProgressTimePropertyImpl().set(value);
    }

    public final Duration getBufferProgressTime() {
        if (this.bufferProgressTime == null) {
            return null;
        }
        return this.bufferProgressTime.get();
    }

    public ReadOnlyObjectProperty<Duration> bufferProgressTimeProperty() {
        return bufferProgressTimePropertyImpl().getReadOnlyProperty();
    }

    private ReadOnlyObjectWrapper<Duration> bufferProgressTimePropertyImpl() {
        if (this.bufferProgressTime == null) {
            this.bufferProgressTime = new ReadOnlyObjectWrapper<>(this, "bufferProgressTime");
        }
        return this.bufferProgressTime;
    }

    public final void setCycleCount(int value) {
        cycleCountProperty().set(value);
    }

    public final int getCycleCount() {
        if (this.cycleCount == null) {
            return 1;
        }
        return this.cycleCount.get();
    }

    public IntegerProperty cycleCountProperty() {
        if (this.cycleCount == null) {
            this.cycleCount = new IntegerPropertyBase(1) { // from class: javafx.scene.media.MediaPlayer.10
                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return MediaPlayer.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "cycleCount";
                }
            };
        }
        return this.cycleCount;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setCurrentCount(int value) {
        currentCountPropertyImpl().set(value);
    }

    public final int getCurrentCount() {
        if (this.currentCount == null) {
            return 0;
        }
        return this.currentCount.get();
    }

    public ReadOnlyIntegerProperty currentCountProperty() {
        return currentCountPropertyImpl().getReadOnlyProperty();
    }

    private ReadOnlyIntegerWrapper currentCountPropertyImpl() {
        if (this.currentCount == null) {
            this.currentCount = new ReadOnlyIntegerWrapper(this, "currentCount");
        }
        return this.currentCount;
    }

    public final void setMute(boolean value) {
        muteProperty().set(value);
    }

    public final boolean isMute() {
        if (this.mute == null) {
            return false;
        }
        return this.mute.get();
    }

    public BooleanProperty muteProperty() {
        if (this.mute == null) {
            this.mute = new BooleanPropertyBase() { // from class: javafx.scene.media.MediaPlayer.11
                @Override // javafx.beans.property.BooleanPropertyBase
                protected void invalidated() {
                    synchronized (MediaPlayer.this.disposeLock) {
                        if (MediaPlayer.this.getStatus() != Status.DISPOSED) {
                            if (MediaPlayer.this.playerReady) {
                                MediaPlayer.this.jfxPlayer.setMute(get());
                            } else {
                                MediaPlayer.this.muteChangeRequested = true;
                            }
                        }
                    }
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return MediaPlayer.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "mute";
                }
            };
        }
        return this.mute;
    }

    public final void setOnMarker(EventHandler<MediaMarkerEvent> onMarker) {
        onMarkerProperty().set(onMarker);
    }

    public final EventHandler<MediaMarkerEvent> getOnMarker() {
        if (this.onMarker == null) {
            return null;
        }
        return this.onMarker.get();
    }

    public ObjectProperty<EventHandler<MediaMarkerEvent>> onMarkerProperty() {
        if (this.onMarker == null) {
            this.onMarker = new SimpleObjectProperty(this, "onMarker");
        }
        return this.onMarker;
    }

    void addView(MediaView view) {
        WeakReference<MediaView> vref = new WeakReference<>(view);
        synchronized (this.viewRefs) {
            this.viewRefs.add(vref);
        }
    }

    void removeView(MediaView view) {
        synchronized (this.viewRefs) {
            for (WeakReference<MediaView> vref : this.viewRefs) {
                MediaView v2 = vref.get();
                if (v2 != null && v2.equals(view)) {
                    this.viewRefs.remove(vref);
                }
            }
        }
    }

    void handleError(MediaException error) {
        Platform.runLater(() -> {
            setError(error);
            if (error.getType() == MediaException.Type.MEDIA_CORRUPTED || error.getType() == MediaException.Type.MEDIA_UNSUPPORTED || error.getType() == MediaException.Type.MEDIA_INACCESSIBLE || error.getType() == MediaException.Type.MEDIA_UNAVAILABLE) {
                this.media._setError(error.getType(), error.getMessage());
            }
        });
    }

    void createMediaTimer() {
        synchronized (MediaTimerTask.timerLock) {
            if (this.mediaTimerTask == null) {
                this.mediaTimerTask = new MediaTimerTask(this);
                this.mediaTimerTask.start();
            }
            this.isUpdateTimeEnabled = true;
        }
    }

    void destroyMediaTimer() {
        synchronized (MediaTimerTask.timerLock) {
            if (this.mediaTimerTask != null) {
                this.isUpdateTimeEnabled = false;
                this.mediaTimerTask.stop();
                this.mediaTimerTask = null;
            }
        }
    }

    void updateTime() {
        if (this.playerReady && this.isUpdateTimeEnabled && this.jfxPlayer != null) {
            double timeSeconds = this.jfxPlayer.getPresentationTime();
            if (timeSeconds >= 0.0d) {
                double newTimeMs = timeSeconds * 1000.0d;
                if (Double.compare(newTimeMs, this.prevTimeMs) != 0) {
                    setCurrentTime(Duration.millis(newTimeMs));
                    this.prevTimeMs = newTimeMs;
                }
            }
        }
    }

    void loopPlayback() {
        seek(getStartTime());
    }

    void handleRequestedChanges() {
        if (this.rateChangeRequested) {
            if (this.jfxPlayer.getDuration() != Double.POSITIVE_INFINITY) {
                this.jfxPlayer.setRate((float) clamp(getRate(), 0.0d, RATE_MAX));
            }
            this.rateChangeRequested = false;
        }
        if (this.volumeChangeRequested) {
            this.jfxPlayer.setVolume((float) clamp(getVolume(), 0.0d, 1.0d));
            this.volumeChangeRequested = false;
        }
        if (this.balanceChangeRequested) {
            this.jfxPlayer.setBalance((float) clamp(getBalance(), -1.0d, 1.0d));
            this.balanceChangeRequested = false;
        }
        if (this.startTimeChangeRequested || this.stopTimeChangeRequested) {
            setStartStopTimes(getStartTime(), this.startTimeChangeRequested, getStopTime(), this.stopTimeChangeRequested);
            this.stopTimeChangeRequested = false;
            this.startTimeChangeRequested = false;
        }
        if (this.muteChangeRequested) {
            this.jfxPlayer.setMute(isMute());
            this.muteChangeRequested = false;
        }
        if (this.audioSpectrumNumBandsChangeRequested) {
            this.jfxPlayer.getAudioSpectrum().setBandCount(clamp(getAudioSpectrumNumBands(), 2, Integer.MAX_VALUE));
            this.audioSpectrumNumBandsChangeRequested = false;
        }
        if (this.audioSpectrumIntervalChangeRequested) {
            this.jfxPlayer.getAudioSpectrum().setInterval(clamp(getAudioSpectrumInterval(), 1.0E-9d, Double.MAX_VALUE));
            this.audioSpectrumIntervalChangeRequested = false;
        }
        if (this.audioSpectrumThresholdChangeRequested) {
            this.jfxPlayer.getAudioSpectrum().setSensitivityThreshold(clamp(getAudioSpectrumThreshold(), Integer.MIN_VALUE, 0));
            this.audioSpectrumThresholdChangeRequested = false;
        }
        if (this.audioSpectrumEnabledChangeRequested) {
            boolean enabled = getAudioSpectrumListener() != null;
            this.jfxPlayer.getAudioSpectrum().setEnabled(enabled);
            this.audioSpectrumEnabledChangeRequested = false;
        }
        if (this.playRequested) {
            this.jfxPlayer.play();
            this.playRequested = false;
        }
    }

    void preReady() {
        Duration duration;
        synchronized (this.viewRefs) {
            for (WeakReference<MediaView> vref : this.viewRefs) {
                MediaView v2 = vref.get();
                if (v2 != null) {
                    v2._mediaPlayerOnReady();
                }
            }
        }
        if (this.audioEqualizer != null) {
            this.audioEqualizer.setAudioEqualizer(this.jfxPlayer.getEqualizer());
        }
        double durationSeconds = this.jfxPlayer.getDuration();
        if (durationSeconds >= 0.0d && !Double.isNaN(durationSeconds)) {
            duration = Duration.millis(durationSeconds * 1000.0d);
        } else {
            duration = Duration.UNKNOWN;
        }
        this.playerReady = true;
        this.media.setDuration(duration);
        this.media._updateMedia(this.jfxPlayer.getMedia());
        handleRequestedChanges();
        calculateCycleDuration();
        if (this.lastBufferEvent != null && duration.toMillis() > 0.0d) {
            double position = this.lastBufferEvent.getBufferPosition();
            double stop = this.lastBufferEvent.getBufferStop();
            double bufferedTime = (position / stop) * duration.toMillis();
            this.lastBufferEvent = null;
            setBufferProgressTime(Duration.millis(bufferedTime));
        }
        setStatus(Status.READY);
    }

    public final void setOnEndOfMedia(Runnable value) {
        onEndOfMediaProperty().set(value);
    }

    public final Runnable getOnEndOfMedia() {
        if (this.onEndOfMedia == null) {
            return null;
        }
        return this.onEndOfMedia.get();
    }

    public ObjectProperty<Runnable> onEndOfMediaProperty() {
        if (this.onEndOfMedia == null) {
            this.onEndOfMedia = new SimpleObjectProperty(this, "onEndOfMedia");
        }
        return this.onEndOfMedia;
    }

    public final void setOnReady(Runnable value) {
        onReadyProperty().set(value);
    }

    public final Runnable getOnReady() {
        if (this.onReady == null) {
            return null;
        }
        return this.onReady.get();
    }

    public ObjectProperty<Runnable> onReadyProperty() {
        if (this.onReady == null) {
            this.onReady = new SimpleObjectProperty(this, "onReady");
        }
        return this.onReady;
    }

    public final void setOnPlaying(Runnable value) {
        onPlayingProperty().set(value);
    }

    public final Runnable getOnPlaying() {
        if (this.onPlaying == null) {
            return null;
        }
        return this.onPlaying.get();
    }

    public ObjectProperty<Runnable> onPlayingProperty() {
        if (this.onPlaying == null) {
            this.onPlaying = new SimpleObjectProperty(this, "onPlaying");
        }
        return this.onPlaying;
    }

    public final void setOnPaused(Runnable value) {
        onPausedProperty().set(value);
    }

    public final Runnable getOnPaused() {
        if (this.onPaused == null) {
            return null;
        }
        return this.onPaused.get();
    }

    public ObjectProperty<Runnable> onPausedProperty() {
        if (this.onPaused == null) {
            this.onPaused = new SimpleObjectProperty(this, "onPaused");
        }
        return this.onPaused;
    }

    public final void setOnStopped(Runnable value) {
        onStoppedProperty().set(value);
    }

    public final Runnable getOnStopped() {
        if (this.onStopped == null) {
            return null;
        }
        return this.onStopped.get();
    }

    public ObjectProperty<Runnable> onStoppedProperty() {
        if (this.onStopped == null) {
            this.onStopped = new SimpleObjectProperty(this, "onStopped");
        }
        return this.onStopped;
    }

    public final void setOnHalted(Runnable value) {
        onHaltedProperty().set(value);
    }

    public final Runnable getOnHalted() {
        if (this.onHalted == null) {
            return null;
        }
        return this.onHalted.get();
    }

    public ObjectProperty<Runnable> onHaltedProperty() {
        if (this.onHalted == null) {
            this.onHalted = new SimpleObjectProperty(this, "onHalted");
        }
        return this.onHalted;
    }

    public final void setOnRepeat(Runnable value) {
        onRepeatProperty().set(value);
    }

    public final Runnable getOnRepeat() {
        if (this.onRepeat == null) {
            return null;
        }
        return this.onRepeat.get();
    }

    public ObjectProperty<Runnable> onRepeatProperty() {
        if (this.onRepeat == null) {
            this.onRepeat = new SimpleObjectProperty(this, "onRepeat");
        }
        return this.onRepeat;
    }

    public final void setOnStalled(Runnable value) {
        onStalledProperty().set(value);
    }

    public final Runnable getOnStalled() {
        if (this.onStalled == null) {
            return null;
        }
        return this.onStalled.get();
    }

    public ObjectProperty<Runnable> onStalledProperty() {
        if (this.onStalled == null) {
            this.onStalled = new SimpleObjectProperty(this, "onStalled");
        }
        return this.onStalled;
    }

    public final void setAudioSpectrumNumBands(int value) {
        audioSpectrumNumBandsProperty().setValue((Number) Integer.valueOf(value));
    }

    public final int getAudioSpectrumNumBands() {
        return audioSpectrumNumBandsProperty().getValue2().intValue();
    }

    public IntegerProperty audioSpectrumNumBandsProperty() {
        if (this.audioSpectrumNumBands == null) {
            this.audioSpectrumNumBands = new IntegerPropertyBase(128) { // from class: javafx.scene.media.MediaPlayer.12
                @Override // javafx.beans.property.IntegerPropertyBase
                protected void invalidated() {
                    synchronized (MediaPlayer.this.disposeLock) {
                        if (MediaPlayer.this.getStatus() != Status.DISPOSED) {
                            if (MediaPlayer.this.playerReady) {
                                MediaPlayer.this.jfxPlayer.getAudioSpectrum().setBandCount(MediaPlayer.clamp(MediaPlayer.this.audioSpectrumNumBands.get(), 2, Integer.MAX_VALUE));
                            } else {
                                MediaPlayer.this.audioSpectrumNumBandsChangeRequested = true;
                            }
                        }
                    }
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return MediaPlayer.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "audioSpectrumNumBands";
                }
            };
        }
        return this.audioSpectrumNumBands;
    }

    public final void setAudioSpectrumInterval(double value) {
        audioSpectrumIntervalProperty().set(value);
    }

    public final double getAudioSpectrumInterval() {
        return audioSpectrumIntervalProperty().get();
    }

    public DoubleProperty audioSpectrumIntervalProperty() {
        if (this.audioSpectrumInterval == null) {
            this.audioSpectrumInterval = new DoublePropertyBase(0.1d) { // from class: javafx.scene.media.MediaPlayer.13
                @Override // javafx.beans.property.DoublePropertyBase
                protected void invalidated() {
                    synchronized (MediaPlayer.this.disposeLock) {
                        if (MediaPlayer.this.getStatus() != Status.DISPOSED) {
                            if (MediaPlayer.this.playerReady) {
                                MediaPlayer.this.jfxPlayer.getAudioSpectrum().setInterval(MediaPlayer.clamp(MediaPlayer.this.audioSpectrumInterval.get(), 1.0E-9d, Double.MAX_VALUE));
                            } else {
                                MediaPlayer.this.audioSpectrumIntervalChangeRequested = true;
                            }
                        }
                    }
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return MediaPlayer.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "audioSpectrumInterval";
                }
            };
        }
        return this.audioSpectrumInterval;
    }

    public final void setAudioSpectrumThreshold(int value) {
        audioSpectrumThresholdProperty().set(value);
    }

    public final int getAudioSpectrumThreshold() {
        return audioSpectrumThresholdProperty().get();
    }

    public IntegerProperty audioSpectrumThresholdProperty() {
        if (this.audioSpectrumThreshold == null) {
            this.audioSpectrumThreshold = new IntegerPropertyBase(-60) { // from class: javafx.scene.media.MediaPlayer.14
                @Override // javafx.beans.property.IntegerPropertyBase
                protected void invalidated() {
                    synchronized (MediaPlayer.this.disposeLock) {
                        if (MediaPlayer.this.getStatus() != Status.DISPOSED) {
                            if (MediaPlayer.this.playerReady) {
                                MediaPlayer.this.jfxPlayer.getAudioSpectrum().setSensitivityThreshold(MediaPlayer.clamp(MediaPlayer.this.audioSpectrumThreshold.get(), Integer.MIN_VALUE, 0));
                            } else {
                                MediaPlayer.this.audioSpectrumThresholdChangeRequested = true;
                            }
                        }
                    }
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return MediaPlayer.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "audioSpectrumThreshold";
                }
            };
        }
        return this.audioSpectrumThreshold;
    }

    public final void setAudioSpectrumListener(AudioSpectrumListener listener) {
        audioSpectrumListenerProperty().set(listener);
    }

    public final AudioSpectrumListener getAudioSpectrumListener() {
        return audioSpectrumListenerProperty().get();
    }

    public ObjectProperty<AudioSpectrumListener> audioSpectrumListenerProperty() {
        if (this.audioSpectrumListener == null) {
            this.audioSpectrumListener = new ObjectPropertyBase<AudioSpectrumListener>() { // from class: javafx.scene.media.MediaPlayer.15
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    synchronized (MediaPlayer.this.disposeLock) {
                        if (MediaPlayer.this.getStatus() != Status.DISPOSED) {
                            if (MediaPlayer.this.playerReady) {
                                boolean enabled = MediaPlayer.this.audioSpectrumListener.get() != 0;
                                MediaPlayer.this.jfxPlayer.getAudioSpectrum().setEnabled(enabled);
                            } else {
                                MediaPlayer.this.audioSpectrumEnabledChangeRequested = true;
                            }
                        }
                    }
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return MediaPlayer.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "audioSpectrumListener";
                }
            };
        }
        return this.audioSpectrumListener;
    }

    public synchronized void dispose() {
        synchronized (this.disposeLock) {
            setStatus(Status.DISPOSED);
            destroyMediaTimer();
            if (this.audioEqualizer != null) {
                this.audioEqualizer.setAudioEqualizer(null);
                this.audioEqualizer = null;
            }
            if (this.jfxPlayer != null) {
                this.jfxPlayer.dispose();
                synchronized (this.renderLock) {
                    if (this.rendererListener != null) {
                        Toolkit.getToolkit().removeStageTkPulseListener(this.rendererListener);
                        this.rendererListener = null;
                    }
                }
                this.jfxPlayer = null;
            }
        }
    }

    /* loaded from: jfxrt.jar:javafx/scene/media/MediaPlayer$MarkerMapChangeListener.class */
    private class MarkerMapChangeListener implements MapChangeListener<String, Duration> {
        private MarkerMapChangeListener() {
        }

        @Override // javafx.collections.MapChangeListener
        public void onChanged(MapChangeListener.Change<? extends String, ? extends Duration> change) {
            synchronized (MediaPlayer.this.disposeLock) {
                if (MediaPlayer.this.getStatus() != Status.DISPOSED) {
                    String key = change.getKey();
                    if (key == null) {
                        return;
                    }
                    com.sun.media.jfxmedia.Media jfxMedia = MediaPlayer.this.jfxPlayer.getMedia();
                    if (change.wasAdded()) {
                        if (change.wasRemoved()) {
                            jfxMedia.removeMarker(key);
                        }
                        Duration value = change.getValueAdded();
                        if (value != null && value.greaterThanOrEqualTo(Duration.ZERO)) {
                            jfxMedia.addMarker(key, change.getValueAdded().toMillis() / 1000.0d);
                        }
                    } else if (change.wasRemoved()) {
                        jfxMedia.removeMarker(key);
                    }
                }
            }
        }
    }

    /* loaded from: jfxrt.jar:javafx/scene/media/MediaPlayer$_MarkerListener.class */
    private class _MarkerListener implements MarkerListener {
        private _MarkerListener() {
        }

        @Override // com.sun.media.jfxmedia.events.MarkerListener
        public void onMarker(MarkerEvent evt) {
            Platform.runLater(() -> {
                Duration markerTime = Duration.millis(evt.getPresentationTime() * 1000.0d);
                if (MediaPlayer.this.getOnMarker() != null) {
                    MediaPlayer.this.getOnMarker().handle(new MediaMarkerEvent(new Pair(evt.getMarkerName(), markerTime)));
                }
            });
        }
    }

    /* loaded from: jfxrt.jar:javafx/scene/media/MediaPlayer$_PlayerStateListener.class */
    private class _PlayerStateListener implements PlayerStateListener {
        private _PlayerStateListener() {
        }

        @Override // com.sun.media.jfxmedia.events.PlayerStateListener
        public void onReady(PlayerStateEvent evt) {
            Platform.runLater(() -> {
                synchronized (MediaPlayer.this.disposeLock) {
                    if (MediaPlayer.this.getStatus() == Status.DISPOSED) {
                        return;
                    }
                    MediaPlayer.this.preReady();
                }
            });
        }

        @Override // com.sun.media.jfxmedia.events.PlayerStateListener
        public void onPlaying(PlayerStateEvent evt) {
            MediaPlayer.this.startTimeAtStop = null;
            Platform.runLater(() -> {
                MediaPlayer.this.createMediaTimer();
                MediaPlayer.this.setStatus(Status.PLAYING);
            });
        }

        @Override // com.sun.media.jfxmedia.events.PlayerStateListener
        public void onPause(PlayerStateEvent evt) {
            Platform.runLater(() -> {
                MediaPlayer.this.isUpdateTimeEnabled = false;
                MediaPlayer.this.setStatus(Status.PAUSED);
            });
            if (MediaPlayer.this.startTimeAtStop != null && MediaPlayer.this.startTimeAtStop != MediaPlayer.this.getStartTime()) {
                MediaPlayer.this.startTimeAtStop = null;
                Platform.runLater(() -> {
                    MediaPlayer.this.setCurrentTime(MediaPlayer.this.getStartTime());
                });
            }
        }

        @Override // com.sun.media.jfxmedia.events.PlayerStateListener
        public void onStop(PlayerStateEvent evt) {
            Platform.runLater(() -> {
                MediaPlayer.this.destroyMediaTimer();
                MediaPlayer.this.startTimeAtStop = MediaPlayer.this.getStartTime();
                MediaPlayer.this.setCurrentTime(MediaPlayer.this.getStartTime());
                MediaPlayer.this.setStatus(Status.STOPPED);
            });
        }

        @Override // com.sun.media.jfxmedia.events.PlayerStateListener
        public void onStall(PlayerStateEvent evt) {
            Platform.runLater(() -> {
                MediaPlayer.this.isUpdateTimeEnabled = false;
                MediaPlayer.this.setStatus(Status.STALLED);
            });
        }

        void handleFinish() {
            MediaPlayer.this.setCurrentCount(MediaPlayer.this.getCurrentCount() + 1);
            if (MediaPlayer.this.getCurrentCount() >= MediaPlayer.this.getCycleCount() && MediaPlayer.this.getCycleCount() != -1) {
                MediaPlayer.this.isUpdateTimeEnabled = false;
                MediaPlayer.this.setCurrentRate(0.0d);
                MediaPlayer.this.isEOS = true;
                if (MediaPlayer.this.getOnEndOfMedia() != null) {
                    Platform.runLater(MediaPlayer.this.getOnEndOfMedia());
                    return;
                }
                return;
            }
            if (MediaPlayer.this.getOnEndOfMedia() != null) {
                Platform.runLater(MediaPlayer.this.getOnEndOfMedia());
            }
            MediaPlayer.this.loopPlayback();
            if (MediaPlayer.this.getOnRepeat() != null) {
                Platform.runLater(MediaPlayer.this.getOnRepeat());
            }
        }

        @Override // com.sun.media.jfxmedia.events.PlayerStateListener
        public void onFinish(PlayerStateEvent evt) {
            MediaPlayer.this.startTimeAtStop = null;
            Platform.runLater(() -> {
                handleFinish();
            });
        }

        @Override // com.sun.media.jfxmedia.events.PlayerStateListener
        public void onHalt(PlayerStateEvent evt) {
            Platform.runLater(() -> {
                MediaPlayer.this.setStatus(Status.HALTED);
                MediaPlayer.this.handleError(MediaException.haltException(evt.getMessage()));
                MediaPlayer.this.isUpdateTimeEnabled = false;
            });
        }
    }

    /* loaded from: jfxrt.jar:javafx/scene/media/MediaPlayer$_PlayerTimeListener.class */
    private class _PlayerTimeListener implements PlayerTimeListener {
        double theDuration;

        private _PlayerTimeListener() {
        }

        void handleDurationChanged() {
            MediaPlayer.this.media.setDuration(Duration.millis(this.theDuration * 1000.0d));
        }

        @Override // com.sun.media.jfxmedia.events.PlayerTimeListener
        public void onDurationChanged(double duration) {
            Platform.runLater(() -> {
                this.theDuration = duration;
                handleDurationChanged();
            });
        }
    }

    /* loaded from: jfxrt.jar:javafx/scene/media/MediaPlayer$_VideoTrackSizeListener.class */
    private class _VideoTrackSizeListener implements VideoTrackSizeListener {
        int trackWidth;
        int trackHeight;

        private _VideoTrackSizeListener() {
        }

        @Override // com.sun.media.jfxmedia.events.VideoTrackSizeListener
        public void onSizeChanged(int width, int height) {
            Platform.runLater(() -> {
                if (MediaPlayer.this.media != null) {
                    this.trackWidth = width;
                    this.trackHeight = height;
                    setSize();
                }
            });
        }

        void setSize() {
            MediaPlayer.this.media.setWidth(this.trackWidth);
            MediaPlayer.this.media.setHeight(this.trackHeight);
            synchronized (MediaPlayer.this.viewRefs) {
                for (WeakReference<MediaView> vref : MediaPlayer.this.viewRefs) {
                    MediaView v2 = vref.get();
                    if (v2 != null) {
                        v2.notifyMediaSizeChange();
                    }
                }
            }
        }
    }

    /* loaded from: jfxrt.jar:javafx/scene/media/MediaPlayer$_MediaErrorListener.class */
    private class _MediaErrorListener implements MediaErrorListener {
        private _MediaErrorListener() {
        }

        @Override // com.sun.media.jfxmedia.events.MediaErrorListener
        public void onError(Object source, int errorCode, String message) {
            MediaException error = MediaException.getMediaException(source, errorCode, message);
            MediaPlayer.this.handleError(error);
        }
    }

    /* loaded from: jfxrt.jar:javafx/scene/media/MediaPlayer$_BufferListener.class */
    private class _BufferListener implements BufferListener {
        double bufferedTime;

        private _BufferListener() {
        }

        @Override // com.sun.media.jfxmedia.events.BufferListener
        public void onBufferProgress(BufferProgressEvent evt) {
            if (MediaPlayer.this.media != null) {
                if (evt.getDuration() <= 0.0d) {
                    MediaPlayer.this.lastBufferEvent = evt;
                    return;
                }
                double position = evt.getBufferPosition();
                double stop = evt.getBufferStop();
                this.bufferedTime = (position / stop) * evt.getDuration() * 1000.0d;
                MediaPlayer.this.lastBufferEvent = null;
                Platform.runLater(() -> {
                    MediaPlayer.this.setBufferProgressTime(Duration.millis(this.bufferedTime));
                });
            }
        }
    }

    /* loaded from: jfxrt.jar:javafx/scene/media/MediaPlayer$_SpectrumListener.class */
    private class _SpectrumListener implements com.sun.media.jfxmedia.events.AudioSpectrumListener {
        private float[] magnitudes;
        private float[] phases;

        private _SpectrumListener() {
        }

        @Override // com.sun.media.jfxmedia.events.AudioSpectrumListener
        public void onAudioSpectrumEvent(AudioSpectrumEvent evt) {
            Platform.runLater(() -> {
                AudioSpectrumListener listener = MediaPlayer.this.getAudioSpectrumListener();
                if (listener != null) {
                    double timestamp = evt.getTimestamp();
                    double duration = evt.getDuration();
                    float[] magnitudes = evt.getSource().getMagnitudes(this.magnitudes);
                    this.magnitudes = magnitudes;
                    float[] phases = evt.getSource().getPhases(this.phases);
                    this.phases = phases;
                    listener.spectrumDataUpdate(timestamp, duration, magnitudes, phases);
                }
            });
        }
    }

    @Deprecated
    public VideoDataBuffer impl_getLatestFrame() {
        VideoDataBuffer videoDataBuffer;
        synchronized (this.renderLock) {
            if (null != this.currentRenderFrame) {
                this.currentRenderFrame.holdFrame();
            }
            videoDataBuffer = this.currentRenderFrame;
        }
        return videoDataBuffer;
    }

    /* loaded from: jfxrt.jar:javafx/scene/media/MediaPlayer$RendererListener.class */
    private class RendererListener implements VideoRendererListener, TKPulseListener {
        boolean updateMediaViews;

        private RendererListener() {
        }

        @Override // com.sun.media.jfxmedia.events.VideoRendererListener
        public void videoFrameUpdated(NewFrameEvent nfe) {
            VideoDataBuffer vdb = nfe.getFrameData();
            if (null != vdb) {
                Duration frameTS = new Duration(vdb.getTimestamp() * 1000.0d);
                Duration stopTime = MediaPlayer.this.getStopTime();
                if (frameTS.greaterThanOrEqualTo(MediaPlayer.this.getStartTime()) && (stopTime.isUnknown() || frameTS.lessThanOrEqualTo(stopTime))) {
                    this.updateMediaViews = true;
                    synchronized (MediaPlayer.this.renderLock) {
                        vdb.holdFrame();
                        if (null != MediaPlayer.this.nextRenderFrame) {
                            MediaPlayer.this.nextRenderFrame.releaseFrame();
                        }
                        MediaPlayer.this.nextRenderFrame = vdb;
                    }
                    Toolkit.getToolkit().requestNextPulse();
                    return;
                }
                vdb.releaseFrame();
            }
        }

        @Override // com.sun.media.jfxmedia.events.VideoRendererListener
        public void releaseVideoFrames() {
            synchronized (MediaPlayer.this.renderLock) {
                if (null != MediaPlayer.this.currentRenderFrame) {
                    MediaPlayer.this.currentRenderFrame.releaseFrame();
                    MediaPlayer.this.currentRenderFrame = null;
                }
                if (null != MediaPlayer.this.nextRenderFrame) {
                    MediaPlayer.this.nextRenderFrame.releaseFrame();
                    MediaPlayer.this.nextRenderFrame = null;
                }
            }
        }

        @Override // com.sun.javafx.tk.TKPulseListener
        public void pulse() {
            if (this.updateMediaViews) {
                this.updateMediaViews = false;
                synchronized (MediaPlayer.this.renderLock) {
                    if (null != MediaPlayer.this.nextRenderFrame) {
                        if (null != MediaPlayer.this.currentRenderFrame) {
                            MediaPlayer.this.currentRenderFrame.releaseFrame();
                        }
                        MediaPlayer.this.currentRenderFrame = MediaPlayer.this.nextRenderFrame;
                        MediaPlayer.this.nextRenderFrame = null;
                    }
                }
                synchronized (MediaPlayer.this.viewRefs) {
                    Iterator<WeakReference<MediaView>> iter = MediaPlayer.this.viewRefs.iterator();
                    while (iter.hasNext()) {
                        MediaView view = iter.next().get();
                        if (null != view) {
                            view.notifyMediaFrameUpdated();
                        } else {
                            iter.remove();
                        }
                    }
                }
            }
        }
    }
}
