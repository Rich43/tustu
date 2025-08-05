package com.sun.media.jfxmediaimpl;

import com.sun.media.jfxmedia.Media;
import com.sun.media.jfxmedia.MediaError;
import com.sun.media.jfxmedia.MediaException;
import com.sun.media.jfxmedia.MediaPlayer;
import com.sun.media.jfxmedia.control.VideoRenderControl;
import com.sun.media.jfxmedia.effects.AudioEqualizer;
import com.sun.media.jfxmedia.effects.AudioSpectrum;
import com.sun.media.jfxmedia.events.AudioSpectrumEvent;
import com.sun.media.jfxmedia.events.AudioSpectrumListener;
import com.sun.media.jfxmedia.events.BufferListener;
import com.sun.media.jfxmedia.events.BufferProgressEvent;
import com.sun.media.jfxmedia.events.MarkerEvent;
import com.sun.media.jfxmedia.events.MarkerListener;
import com.sun.media.jfxmedia.events.MediaErrorListener;
import com.sun.media.jfxmedia.events.NewFrameEvent;
import com.sun.media.jfxmedia.events.PlayerEvent;
import com.sun.media.jfxmedia.events.PlayerStateEvent;
import com.sun.media.jfxmedia.events.PlayerStateListener;
import com.sun.media.jfxmedia.events.PlayerTimeListener;
import com.sun.media.jfxmedia.events.VideoFrameRateListener;
import com.sun.media.jfxmedia.events.VideoRendererListener;
import com.sun.media.jfxmedia.events.VideoTrackSizeListener;
import com.sun.media.jfxmedia.logging.Logger;
import com.sun.media.jfxmedia.track.AudioTrack;
import com.sun.media.jfxmedia.track.SubtitleTrack;
import com.sun.media.jfxmedia.track.Track;
import com.sun.media.jfxmedia.track.VideoResolution;
import com.sun.media.jfxmedia.track.VideoTrack;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import sun.util.locale.LanguageTag;

/*  JADX ERROR: NullPointerException in pass: ClassModifier
    java.lang.NullPointerException
    */
/* loaded from: jfxrt.jar:com/sun/media/jfxmediaimpl/NativeMediaPlayer.class */
public abstract class NativeMediaPlayer implements MediaPlayer, MarkerStateListener {
    public static final int eventPlayerUnknown = 100;
    public static final int eventPlayerReady = 101;
    public static final int eventPlayerPlaying = 102;
    public static final int eventPlayerPaused = 103;
    public static final int eventPlayerStopped = 104;
    public static final int eventPlayerStalled = 105;
    public static final int eventPlayerFinished = 106;
    public static final int eventPlayerError = 107;
    private static final int NOMINAL_VIDEO_FPS = 30;
    public static final long ONE_SECOND = 1000000000;
    private NativeMedia media;
    private VideoRenderControl videoRenderControl;
    private double firstFrameTime;
    private Timer mediaPulseTimer;
    private double previousFrameTime;
    private long numFramesSincePlaying;
    private double meanFrameDuration;
    private double decodedFrameRate;
    private Runnable onDispose;
    private final List<WeakReference<MediaErrorListener>> errorListeners = new ArrayList();
    private final List<WeakReference<PlayerStateListener>> playerStateListeners = new ArrayList();
    private final List<WeakReference<PlayerTimeListener>> playerTimeListeners = new ArrayList();
    private final List<WeakReference<VideoTrackSizeListener>> videoTrackSizeListeners = new ArrayList();
    private final List<WeakReference<VideoRendererListener>> videoUpdateListeners = new ArrayList();
    private final List<WeakReference<VideoFrameRateListener>> videoFrameRateListeners = new ArrayList();
    private final List<WeakReference<MarkerListener>> markerListeners = new ArrayList();
    private final List<WeakReference<BufferListener>> bufferListeners = new ArrayList();
    private final List<WeakReference<AudioSpectrumListener>> audioSpectrumListeners = new ArrayList();
    private final List<PlayerStateEvent> cachedStateEvents = new ArrayList();
    private final List<PlayerTimeEvent> cachedTimeEvents = new ArrayList();
    private final List<BufferProgressEvent> cachedBufferEvents = new ArrayList();
    private final List<MediaErrorEvent> cachedErrorEvents = new ArrayList();
    private boolean isFirstFrame = true;
    private NewFrameEvent firstFrameEvent = null;
    private final Object firstFrameLock = new Object();
    private EventQueueThread eventLoop = new EventQueueThread();
    private int frameWidth = -1;
    private int frameHeight = -1;
    private final AtomicBoolean isMediaPulseEnabled = new AtomicBoolean(false);
    private final Lock mediaPulseLock = new ReentrantLock();
    private final Lock markerLock = new ReentrantLock();
    private boolean checkSeek = false;
    private double timeBeforeSeek = 0.0d;
    private double timeAfterSeek = 0.0d;
    private double previousTime = 0.0d;
    private double firedMarkerTime = -1.0d;
    private double startTime = 0.0d;
    private double stopTime = Double.POSITIVE_INFINITY;
    private boolean isStartTimeUpdated = false;
    private boolean isStopTimeSet = false;
    private double encodedFrameRate = 0.0d;
    private boolean recomputeFrameRate = true;
    private PlayerStateEvent.PlayerState playerState = PlayerStateEvent.PlayerState.UNKNOWN;
    private final Lock disposeLock = new ReentrantLock();
    private boolean isDisposed = false;

    @Override // com.sun.media.jfxmedia.MediaPlayer
    public abstract AudioEqualizer getEqualizer();

    @Override // com.sun.media.jfxmedia.MediaPlayer
    public abstract AudioSpectrum getAudioSpectrum();

    protected abstract long playerGetAudioSyncDelay() throws MediaException;

    protected abstract void playerSetAudioSyncDelay(long j2) throws MediaException;

    protected abstract void playerPlay() throws MediaException;

    protected abstract void playerStop() throws MediaException;

    protected abstract void playerPause() throws MediaException;

    protected abstract void playerFinish() throws MediaException;

    protected abstract float playerGetRate() throws MediaException;

    protected abstract void playerSetRate(float f2) throws MediaException;

    protected abstract double playerGetPresentationTime() throws MediaException;

    protected abstract boolean playerGetMute() throws MediaException;

    protected abstract void playerSetMute(boolean z2) throws MediaException;

    protected abstract float playerGetVolume() throws MediaException;

    protected abstract void playerSetVolume(float f2) throws MediaException;

    protected abstract float playerGetBalance() throws MediaException;

    protected abstract void playerSetBalance(float f2) throws MediaException;

    protected abstract double playerGetDuration() throws MediaException;

    protected abstract void playerSeek(double d2) throws MediaException;

    protected abstract void playerInit() throws MediaException;

    protected abstract void playerDispose();

    /*  JADX ERROR: Failed to decode insn: 0x0002: MOVE_MULTI
        java.lang.ArrayIndexOutOfBoundsException: arraycopy: source index -1 out of bounds for object array[6]
        	at java.base/java.lang.System.arraycopy(Native Method)
        	at jadx.plugins.input.java.data.code.StackState.insert(StackState.java:52)
        	at jadx.plugins.input.java.data.code.CodeDecodeState.insert(CodeDecodeState.java:137)
        	at jadx.plugins.input.java.data.code.JavaInsnsRegister.dup2x1(JavaInsnsRegister.java:313)
        	at jadx.plugins.input.java.data.code.JavaInsnData.decode(JavaInsnData.java:46)
        	at jadx.core.dex.instructions.InsnDecoder.lambda$process$0(InsnDecoder.java:50)
        	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:85)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:46)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:158)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:458)
        	at jadx.core.ProcessClass.process(ProcessClass.java:69)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:117)
        	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
        	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
        */
    static /* synthetic */ double access$802(com.sun.media.jfxmediaimpl.NativeMediaPlayer r6, double r7) {
        /*
            r0 = r6
            r1 = r7
            // decode failed: arraycopy: source index -1 out of bounds for object array[6]
            r0.firstFrameTime = r1
            return r-1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.media.jfxmediaimpl.NativeMediaPlayer.access$802(com.sun.media.jfxmediaimpl.NativeMediaPlayer, double):double");
    }

    /*  JADX ERROR: Failed to decode insn: 0x0002: MOVE_MULTI
        java.lang.ArrayIndexOutOfBoundsException: arraycopy: source index -1 out of bounds for object array[6]
        	at java.base/java.lang.System.arraycopy(Native Method)
        	at jadx.plugins.input.java.data.code.StackState.insert(StackState.java:52)
        	at jadx.plugins.input.java.data.code.CodeDecodeState.insert(CodeDecodeState.java:137)
        	at jadx.plugins.input.java.data.code.JavaInsnsRegister.dup2x1(JavaInsnsRegister.java:313)
        	at jadx.plugins.input.java.data.code.JavaInsnData.decode(JavaInsnData.java:46)
        	at jadx.core.dex.instructions.InsnDecoder.lambda$process$0(InsnDecoder.java:50)
        	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:85)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:46)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:158)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:458)
        	at jadx.core.ProcessClass.process(ProcessClass.java:69)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:117)
        	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
        	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
        */
    static /* synthetic */ double access$1002(com.sun.media.jfxmediaimpl.NativeMediaPlayer r6, double r7) {
        /*
            r0 = r6
            r1 = r7
            // decode failed: arraycopy: source index -1 out of bounds for object array[6]
            r0.previousFrameTime = r1
            return r-1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.media.jfxmediaimpl.NativeMediaPlayer.access$1002(com.sun.media.jfxmediaimpl.NativeMediaPlayer, double):double");
    }

    /*  JADX ERROR: Failed to decode insn: 0x0002: MOVE_MULTI
        java.lang.ArrayIndexOutOfBoundsException: arraycopy: source index -1 out of bounds for object array[6]
        	at java.base/java.lang.System.arraycopy(Native Method)
        	at jadx.plugins.input.java.data.code.StackState.insert(StackState.java:52)
        	at jadx.plugins.input.java.data.code.CodeDecodeState.insert(CodeDecodeState.java:137)
        	at jadx.plugins.input.java.data.code.JavaInsnsRegister.dup2x1(JavaInsnsRegister.java:313)
        	at jadx.plugins.input.java.data.code.JavaInsnData.decode(JavaInsnData.java:46)
        	at jadx.core.dex.instructions.InsnDecoder.lambda$process$0(InsnDecoder.java:50)
        	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:85)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:46)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:158)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:458)
        	at jadx.core.ProcessClass.process(ProcessClass.java:69)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:117)
        	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
        	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
        */
    static /* synthetic */ long access$1102(com.sun.media.jfxmediaimpl.NativeMediaPlayer r6, long r7) {
        /*
            r0 = r6
            r1 = r7
            // decode failed: arraycopy: source index -1 out of bounds for object array[6]
            r0.numFramesSincePlaying = r1
            return r-1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.media.jfxmediaimpl.NativeMediaPlayer.access$1102(com.sun.media.jfxmediaimpl.NativeMediaPlayer, long):long");
    }

    /*  JADX ERROR: Failed to decode insn: 0x0002: MOVE_MULTI
        java.lang.ArrayIndexOutOfBoundsException: arraycopy: source index -1 out of bounds for object array[6]
        	at java.base/java.lang.System.arraycopy(Native Method)
        	at jadx.plugins.input.java.data.code.StackState.insert(StackState.java:52)
        	at jadx.plugins.input.java.data.code.CodeDecodeState.insert(CodeDecodeState.java:137)
        	at jadx.plugins.input.java.data.code.JavaInsnsRegister.dup2x1(JavaInsnsRegister.java:313)
        	at jadx.plugins.input.java.data.code.JavaInsnData.decode(JavaInsnData.java:46)
        	at jadx.core.dex.instructions.InsnDecoder.lambda$process$0(InsnDecoder.java:50)
        	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:85)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:46)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:158)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:458)
        	at jadx.core.ProcessClass.process(ProcessClass.java:69)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:117)
        	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
        	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
        */
    static /* synthetic */ double access$1202(com.sun.media.jfxmediaimpl.NativeMediaPlayer r6, double r7) {
        /*
            r0 = r6
            r1 = r7
            // decode failed: arraycopy: source index -1 out of bounds for object array[6]
            r0.meanFrameDuration = r1
            return r-1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.media.jfxmediaimpl.NativeMediaPlayer.access$1202(com.sun.media.jfxmediaimpl.NativeMediaPlayer, double):double");
    }

    /*  JADX ERROR: Failed to decode insn: 0x0002: MOVE_MULTI
        java.lang.ArrayIndexOutOfBoundsException: arraycopy: source index -1 out of bounds for object array[6]
        	at java.base/java.lang.System.arraycopy(Native Method)
        	at jadx.plugins.input.java.data.code.StackState.insert(StackState.java:52)
        	at jadx.plugins.input.java.data.code.CodeDecodeState.insert(CodeDecodeState.java:137)
        	at jadx.plugins.input.java.data.code.JavaInsnsRegister.dup2x1(JavaInsnsRegister.java:313)
        	at jadx.plugins.input.java.data.code.JavaInsnData.decode(JavaInsnData.java:46)
        	at jadx.core.dex.instructions.InsnDecoder.lambda$process$0(InsnDecoder.java:50)
        	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:85)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:46)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:158)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:458)
        	at jadx.core.ProcessClass.process(ProcessClass.java:69)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:117)
        	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
        	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
        */
    static /* synthetic */ double access$1302(com.sun.media.jfxmediaimpl.NativeMediaPlayer r6, double r7) {
        /*
            r0 = r6
            r1 = r7
            // decode failed: arraycopy: source index -1 out of bounds for object array[6]
            r0.decodedFrameRate = r1
            return r-1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.media.jfxmediaimpl.NativeMediaPlayer.access$1302(com.sun.media.jfxmediaimpl.NativeMediaPlayer, double):double");
    }

    /*  JADX ERROR: Failed to decode insn: 0x0005: MOVE_MULTI
        java.lang.ArrayIndexOutOfBoundsException: arraycopy: source index -1 out of bounds for object array[8]
        	at java.base/java.lang.System.arraycopy(Native Method)
        	at jadx.plugins.input.java.data.code.StackState.insert(StackState.java:52)
        	at jadx.plugins.input.java.data.code.CodeDecodeState.insert(CodeDecodeState.java:137)
        	at jadx.plugins.input.java.data.code.JavaInsnsRegister.dup2x1(JavaInsnsRegister.java:313)
        	at jadx.plugins.input.java.data.code.JavaInsnData.decode(JavaInsnData.java:46)
        	at jadx.core.dex.instructions.InsnDecoder.lambda$process$0(InsnDecoder.java:50)
        	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:85)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:46)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:158)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:458)
        	at jadx.core.ProcessClass.process(ProcessClass.java:69)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:117)
        	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
        	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
        */
    static /* synthetic */ long access$1108(com.sun.media.jfxmediaimpl.NativeMediaPlayer r8) {
        /*
            r0 = r8
            r1 = r0
            long r1 = r1.numFramesSincePlaying
            // decode failed: arraycopy: source index -1 out of bounds for object array[8]
            r2 = 1
            long r1 = r1 + r2
            r0.numFramesSincePlaying = r1
            return r-1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.media.jfxmediaimpl.NativeMediaPlayer.access$1108(com.sun.media.jfxmediaimpl.NativeMediaPlayer):long");
    }

    /*  JADX ERROR: Failed to decode insn: 0x0002: MOVE_MULTI
        java.lang.ArrayIndexOutOfBoundsException: arraycopy: source index -1 out of bounds for object array[6]
        	at java.base/java.lang.System.arraycopy(Native Method)
        	at jadx.plugins.input.java.data.code.StackState.insert(StackState.java:52)
        	at jadx.plugins.input.java.data.code.CodeDecodeState.insert(CodeDecodeState.java:137)
        	at jadx.plugins.input.java.data.code.JavaInsnsRegister.dup2x1(JavaInsnsRegister.java:313)
        	at jadx.plugins.input.java.data.code.JavaInsnData.decode(JavaInsnData.java:46)
        	at jadx.core.dex.instructions.InsnDecoder.lambda$process$0(InsnDecoder.java:50)
        	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:85)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:46)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:158)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:458)
        	at jadx.core.ProcessClass.process(ProcessClass.java:69)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:117)
        	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
        	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
        */
    static /* synthetic */ double access$1402(com.sun.media.jfxmediaimpl.NativeMediaPlayer r6, double r7) {
        /*
            r0 = r6
            r1 = r7
            // decode failed: arraycopy: source index -1 out of bounds for object array[6]
            r0.encodedFrameRate = r1
            return r-1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.media.jfxmediaimpl.NativeMediaPlayer.access$1402(com.sun.media.jfxmediaimpl.NativeMediaPlayer, double):double");
    }

    protected NativeMediaPlayer(NativeMedia clip) {
        if (clip == null) {
            throw new IllegalArgumentException("clip == null!");
        }
        this.media = clip;
        this.videoRenderControl = new VideoRenderer();
    }

    protected void init() {
        this.media.addMarkerStateListener(this);
        this.eventLoop.start();
    }

    void setOnDispose(Runnable onDispose) {
        this.disposeLock.lock();
        try {
            if (!this.isDisposed) {
                this.onDispose = onDispose;
            }
        } finally {
            this.disposeLock.unlock();
        }
    }

    /* loaded from: jfxrt.jar:com/sun/media/jfxmediaimpl/NativeMediaPlayer$WarningEvent.class */
    private static class WarningEvent extends PlayerEvent {
        private final Object source;
        private final String message;

        WarningEvent(Object source, String message) {
            this.source = source;
            this.message = message;
        }

        public Object getSource() {
            return this.source;
        }

        public String getMessage() {
            return this.message;
        }
    }

    /* loaded from: jfxrt.jar:com/sun/media/jfxmediaimpl/NativeMediaPlayer$MediaErrorEvent.class */
    public static class MediaErrorEvent extends PlayerEvent {
        private final Object source;
        private final MediaError error;

        public MediaErrorEvent(Object source, MediaError error) {
            this.source = source;
            this.error = error;
        }

        public Object getSource() {
            return this.source;
        }

        public String getMessage() {
            return this.error.description();
        }

        public int getErrorCode() {
            return this.error.code();
        }
    }

    /* loaded from: jfxrt.jar:com/sun/media/jfxmediaimpl/NativeMediaPlayer$PlayerTimeEvent.class */
    private static class PlayerTimeEvent extends PlayerEvent {
        private final double time;

        public PlayerTimeEvent(double time) {
            this.time = time;
        }

        public double getTime() {
            return this.time;
        }
    }

    /* loaded from: jfxrt.jar:com/sun/media/jfxmediaimpl/NativeMediaPlayer$TrackEvent.class */
    private static class TrackEvent extends PlayerEvent {
        private final Track track;

        TrackEvent(Track track) {
            this.track = track;
        }

        public Track getTrack() {
            return this.track;
        }
    }

    /* loaded from: jfxrt.jar:com/sun/media/jfxmediaimpl/NativeMediaPlayer$FrameSizeChangedEvent.class */
    private static class FrameSizeChangedEvent extends PlayerEvent {
        private final int width;
        private final int height;

        public FrameSizeChangedEvent(int width, int height) {
            if (width > 0) {
                this.width = width;
            } else {
                this.width = 0;
            }
            if (height > 0) {
                this.height = height;
            } else {
                this.height = 0;
            }
        }

        public int getWidth() {
            return this.width;
        }

        public int getHeight() {
            return this.height;
        }
    }

    /* loaded from: jfxrt.jar:com/sun/media/jfxmediaimpl/NativeMediaPlayer$VideoRenderer.class */
    private class VideoRenderer implements VideoRenderControl {
        private VideoRenderer() {
        }

        @Override // com.sun.media.jfxmedia.control.VideoRenderControl
        public void addVideoRendererListener(VideoRendererListener listener) {
            if (listener != null) {
                synchronized (NativeMediaPlayer.this.firstFrameLock) {
                    if (NativeMediaPlayer.this.firstFrameEvent != null) {
                        listener.videoFrameUpdated(NativeMediaPlayer.this.firstFrameEvent);
                    }
                }
                NativeMediaPlayer.this.videoUpdateListeners.add(new WeakReference(listener));
            }
        }

        @Override // com.sun.media.jfxmedia.control.VideoRenderControl
        public void removeVideoRendererListener(VideoRendererListener listener) {
            if (listener != null) {
                ListIterator<WeakReference<VideoRendererListener>> it = NativeMediaPlayer.this.videoUpdateListeners.listIterator();
                while (it.hasNext()) {
                    VideoRendererListener l2 = it.next().get();
                    if (l2 == null || l2 == listener) {
                        it.remove();
                    }
                }
            }
        }

        @Override // com.sun.media.jfxmedia.control.VideoRenderControl
        public void addVideoFrameRateListener(VideoFrameRateListener listener) {
            if (listener != null) {
                NativeMediaPlayer.this.videoFrameRateListeners.add(new WeakReference(listener));
            }
        }

        @Override // com.sun.media.jfxmedia.control.VideoRenderControl
        public void removeVideoFrameRateListener(VideoFrameRateListener listener) {
            if (listener != null) {
                ListIterator<WeakReference<VideoFrameRateListener>> it = NativeMediaPlayer.this.videoFrameRateListeners.listIterator();
                while (it.hasNext()) {
                    VideoFrameRateListener l2 = it.next().get();
                    if (l2 == null || l2 == listener) {
                        it.remove();
                    }
                }
            }
        }

        @Override // com.sun.media.jfxmedia.control.VideoRenderControl
        public int getFrameWidth() {
            return NativeMediaPlayer.this.frameWidth;
        }

        @Override // com.sun.media.jfxmedia.control.VideoRenderControl
        public int getFrameHeight() {
            return NativeMediaPlayer.this.frameHeight;
        }
    }

    /* loaded from: jfxrt.jar:com/sun/media/jfxmediaimpl/NativeMediaPlayer$EventQueueThread.class */
    private class EventQueueThread extends Thread {
        private final BlockingQueue<PlayerEvent> eventQueue = new LinkedBlockingQueue();
        private volatile boolean stopped = false;

        EventQueueThread() {
            setName("JFXMedia Player EventQueueThread");
            setDaemon(true);
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            while (!this.stopped) {
                try {
                    PlayerEvent evt = this.eventQueue.take2();
                    if (!this.stopped) {
                        if (evt instanceof NewFrameEvent) {
                            try {
                                HandleRendererEvents((NewFrameEvent) evt);
                            } catch (Throwable t2) {
                                if (Logger.canLog(4)) {
                                    Logger.logMsg(4, "Caught exception in HandleRendererEvents: " + t2.toString());
                                }
                            }
                        } else if (evt instanceof PlayerStateEvent) {
                            HandleStateEvents((PlayerStateEvent) evt);
                        } else if (evt instanceof FrameSizeChangedEvent) {
                            HandleFrameSizeChangedEvents((FrameSizeChangedEvent) evt);
                        } else if (evt instanceof TrackEvent) {
                            HandleTrackEvents((TrackEvent) evt);
                        } else if (evt instanceof MarkerEvent) {
                            HandleMarkerEvents((MarkerEvent) evt);
                        } else if (evt instanceof WarningEvent) {
                            HandleWarningEvents((WarningEvent) evt);
                        } else if (evt instanceof PlayerTimeEvent) {
                            HandlePlayerTimeEvents((PlayerTimeEvent) evt);
                        } else if (evt instanceof BufferProgressEvent) {
                            HandleBufferEvents((BufferProgressEvent) evt);
                        } else if (evt instanceof AudioSpectrumEvent) {
                            HandleAudioSpectrumEvents((AudioSpectrumEvent) evt);
                        } else if (evt instanceof MediaErrorEvent) {
                            HandleErrorEvents((MediaErrorEvent) evt);
                        }
                    }
                } catch (Exception e2) {
                }
            }
            this.eventQueue.clear();
        }

        /* JADX WARN: Failed to check method for inline after forced processcom.sun.media.jfxmediaimpl.NativeMediaPlayer.access$1002(com.sun.media.jfxmediaimpl.NativeMediaPlayer, double):double */
        /* JADX WARN: Failed to check method for inline after forced processcom.sun.media.jfxmediaimpl.NativeMediaPlayer.access$1102(com.sun.media.jfxmediaimpl.NativeMediaPlayer, long):long */
        /* JADX WARN: Failed to check method for inline after forced processcom.sun.media.jfxmediaimpl.NativeMediaPlayer.access$1108(com.sun.media.jfxmediaimpl.NativeMediaPlayer):long */
        /* JADX WARN: Failed to check method for inline after forced processcom.sun.media.jfxmediaimpl.NativeMediaPlayer.access$1202(com.sun.media.jfxmediaimpl.NativeMediaPlayer, double):double */
        /* JADX WARN: Failed to check method for inline after forced processcom.sun.media.jfxmediaimpl.NativeMediaPlayer.access$1302(com.sun.media.jfxmediaimpl.NativeMediaPlayer, double):double */
        /* JADX WARN: Failed to check method for inline after forced processcom.sun.media.jfxmediaimpl.NativeMediaPlayer.access$802(com.sun.media.jfxmediaimpl.NativeMediaPlayer, double):double */
        private void HandleRendererEvents(NewFrameEvent evt) {
            if (NativeMediaPlayer.this.isFirstFrame) {
                NativeMediaPlayer.this.isFirstFrame = false;
                synchronized (NativeMediaPlayer.this.firstFrameLock) {
                    NativeMediaPlayer.this.firstFrameEvent = evt;
                    NativeMediaPlayer.access$802(NativeMediaPlayer.this, NativeMediaPlayer.this.firstFrameEvent.getFrameData().getTimestamp());
                    NativeMediaPlayer.this.firstFrameEvent.getFrameData().holdFrame();
                }
            } else if (NativeMediaPlayer.this.firstFrameEvent != null && NativeMediaPlayer.this.firstFrameTime != evt.getFrameData().getTimestamp()) {
                synchronized (NativeMediaPlayer.this.firstFrameLock) {
                    NativeMediaPlayer.this.firstFrameEvent.getFrameData().releaseFrame();
                    NativeMediaPlayer.this.firstFrameEvent = null;
                }
            }
            ListIterator<WeakReference<VideoRendererListener>> it = NativeMediaPlayer.this.videoUpdateListeners.listIterator();
            while (it.hasNext()) {
                VideoRendererListener l2 = it.next().get();
                if (l2 != null) {
                    l2.videoFrameUpdated(evt);
                } else {
                    it.remove();
                }
            }
            evt.getFrameData().releaseFrame();
            if (!NativeMediaPlayer.this.videoFrameRateListeners.isEmpty()) {
                double currentFrameTime = System.nanoTime() / 1.0E9d;
                if (NativeMediaPlayer.this.recomputeFrameRate) {
                    NativeMediaPlayer.this.recomputeFrameRate = false;
                    NativeMediaPlayer.access$1002(NativeMediaPlayer.this, currentFrameTime);
                    NativeMediaPlayer.access$1102(NativeMediaPlayer.this, 1L);
                    return;
                }
                boolean fireFrameRateEvent = false;
                if (NativeMediaPlayer.this.numFramesSincePlaying == 1) {
                    NativeMediaPlayer.access$1202(NativeMediaPlayer.this, currentFrameTime - NativeMediaPlayer.this.previousFrameTime);
                    if (NativeMediaPlayer.this.meanFrameDuration > 0.0d) {
                        NativeMediaPlayer.access$1302(NativeMediaPlayer.this, 1.0d / NativeMediaPlayer.this.meanFrameDuration);
                        fireFrameRateEvent = true;
                    }
                } else {
                    double previousMeanFrameDuration = NativeMediaPlayer.this.meanFrameDuration;
                    int movingAverageLength = NativeMediaPlayer.this.encodedFrameRate != 0.0d ? (int) (NativeMediaPlayer.this.encodedFrameRate + 0.5d) : 30;
                    long numFrames = NativeMediaPlayer.this.numFramesSincePlaying < ((long) movingAverageLength) ? NativeMediaPlayer.this.numFramesSincePlaying : movingAverageLength;
                    NativeMediaPlayer.access$1202(NativeMediaPlayer.this, ((((numFrames - 1) * previousMeanFrameDuration) + currentFrameTime) - NativeMediaPlayer.this.previousFrameTime) / numFrames);
                    if (NativeMediaPlayer.this.meanFrameDuration > 0.0d && Math.abs(NativeMediaPlayer.this.decodedFrameRate - (1.0d / NativeMediaPlayer.this.meanFrameDuration)) > 0.5d) {
                        NativeMediaPlayer.access$1302(NativeMediaPlayer.this, 1.0d / NativeMediaPlayer.this.meanFrameDuration);
                        fireFrameRateEvent = true;
                    }
                }
                if (fireFrameRateEvent) {
                    ListIterator<WeakReference<VideoFrameRateListener>> it2 = NativeMediaPlayer.this.videoFrameRateListeners.listIterator();
                    while (it2.hasNext()) {
                        VideoFrameRateListener l3 = it2.next().get();
                        if (l3 != null) {
                            l3.onFrameRateChanged(NativeMediaPlayer.this.decodedFrameRate);
                        } else {
                            it2.remove();
                        }
                    }
                }
                NativeMediaPlayer.access$1002(NativeMediaPlayer.this, currentFrameTime);
                NativeMediaPlayer.access$1108(NativeMediaPlayer.this);
            }
        }

        private void HandleStateEvents(PlayerStateEvent evt) {
            NativeMediaPlayer.this.playerState = evt.getState();
            NativeMediaPlayer.this.recomputeFrameRate = PlayerStateEvent.PlayerState.PLAYING == evt.getState();
            switch (NativeMediaPlayer.this.playerState) {
                case READY:
                    NativeMediaPlayer.this.onNativeInit();
                    sendFakeBufferProgressEvent();
                    break;
                case PLAYING:
                    NativeMediaPlayer.this.isMediaPulseEnabled.set(true);
                    break;
                case STOPPED:
                case FINISHED:
                    NativeMediaPlayer.this.doMediaPulseTask();
                case PAUSED:
                case STALLED:
                case HALTED:
                    NativeMediaPlayer.this.isMediaPulseEnabled.set(false);
                    break;
            }
            synchronized (NativeMediaPlayer.this.cachedStateEvents) {
                if (NativeMediaPlayer.this.playerStateListeners.isEmpty()) {
                    NativeMediaPlayer.this.cachedStateEvents.add(evt);
                    return;
                }
                ListIterator<WeakReference<PlayerStateListener>> it = NativeMediaPlayer.this.playerStateListeners.listIterator();
                while (it.hasNext()) {
                    PlayerStateListener listener = it.next().get();
                    if (listener != null) {
                        switch (NativeMediaPlayer.this.playerState) {
                            case READY:
                                NativeMediaPlayer.this.onNativeInit();
                                sendFakeBufferProgressEvent();
                                listener.onReady(evt);
                                break;
                            case PLAYING:
                                listener.onPlaying(evt);
                                break;
                            case STOPPED:
                                listener.onStop(evt);
                                break;
                            case FINISHED:
                                listener.onFinish(evt);
                                break;
                            case PAUSED:
                                listener.onPause(evt);
                                break;
                            case STALLED:
                                listener.onStall(evt);
                                break;
                            case HALTED:
                                listener.onHalt(evt);
                                break;
                        }
                    } else {
                        it.remove();
                    }
                }
            }
        }

        private void HandlePlayerTimeEvents(PlayerTimeEvent evt) {
            synchronized (NativeMediaPlayer.this.cachedTimeEvents) {
                if (NativeMediaPlayer.this.playerTimeListeners.isEmpty()) {
                    NativeMediaPlayer.this.cachedTimeEvents.add(evt);
                    return;
                }
                ListIterator<WeakReference<PlayerTimeListener>> it = NativeMediaPlayer.this.playerTimeListeners.listIterator();
                while (it.hasNext()) {
                    PlayerTimeListener listener = it.next().get();
                    if (listener != null) {
                        listener.onDurationChanged(evt.getTime());
                    } else {
                        it.remove();
                    }
                }
            }
        }

        private void HandleFrameSizeChangedEvents(FrameSizeChangedEvent evt) {
            NativeMediaPlayer.this.frameWidth = evt.getWidth();
            NativeMediaPlayer.this.frameHeight = evt.getHeight();
            Logger.logMsg(1, "** Frame size changed (" + NativeMediaPlayer.this.frameWidth + ", " + NativeMediaPlayer.this.frameHeight + ")");
            ListIterator<WeakReference<VideoTrackSizeListener>> it = NativeMediaPlayer.this.videoTrackSizeListeners.listIterator();
            while (it.hasNext()) {
                VideoTrackSizeListener listener = it.next().get();
                if (listener != null) {
                    listener.onSizeChanged(NativeMediaPlayer.this.frameWidth, NativeMediaPlayer.this.frameHeight);
                } else {
                    it.remove();
                }
            }
        }

        /* JADX WARN: Failed to check method for inline after forced processcom.sun.media.jfxmediaimpl.NativeMediaPlayer.access$1402(com.sun.media.jfxmediaimpl.NativeMediaPlayer, double):double */
        private void HandleTrackEvents(TrackEvent evt) {
            NativeMediaPlayer.this.media.addTrack(evt.getTrack());
            if (evt.getTrack() instanceof VideoTrack) {
                NativeMediaPlayer.access$1402(NativeMediaPlayer.this, ((VideoTrack) evt.getTrack()).getEncodedFrameRate());
            }
        }

        private void HandleMarkerEvents(MarkerEvent evt) {
            ListIterator<WeakReference<MarkerListener>> it = NativeMediaPlayer.this.markerListeners.listIterator();
            while (it.hasNext()) {
                MarkerListener listener = it.next().get();
                if (listener != null) {
                    listener.onMarker(evt);
                } else {
                    it.remove();
                }
            }
        }

        private void HandleWarningEvents(WarningEvent evt) {
            Logger.logMsg(3, evt.getSource() + evt.getMessage());
        }

        private void HandleErrorEvents(MediaErrorEvent evt) {
            Logger.logMsg(4, evt.getMessage());
            synchronized (NativeMediaPlayer.this.cachedErrorEvents) {
                if (NativeMediaPlayer.this.errorListeners.isEmpty()) {
                    NativeMediaPlayer.this.cachedErrorEvents.add(evt);
                    return;
                }
                ListIterator<WeakReference<MediaErrorListener>> it = NativeMediaPlayer.this.errorListeners.listIterator();
                while (it.hasNext()) {
                    MediaErrorListener l2 = it.next().get();
                    if (l2 != null) {
                        l2.onError(evt.getSource(), evt.getErrorCode(), evt.getMessage());
                    } else {
                        it.remove();
                    }
                }
            }
        }

        private void HandleBufferEvents(BufferProgressEvent evt) {
            synchronized (NativeMediaPlayer.this.cachedBufferEvents) {
                if (NativeMediaPlayer.this.bufferListeners.isEmpty()) {
                    NativeMediaPlayer.this.cachedBufferEvents.add(evt);
                    return;
                }
                ListIterator<WeakReference<BufferListener>> it = NativeMediaPlayer.this.bufferListeners.listIterator();
                while (it.hasNext()) {
                    BufferListener listener = it.next().get();
                    if (listener != null) {
                        listener.onBufferProgress(evt);
                    } else {
                        it.remove();
                    }
                }
            }
        }

        private void HandleAudioSpectrumEvents(AudioSpectrumEvent evt) throws MediaException {
            ListIterator<WeakReference<AudioSpectrumListener>> it = NativeMediaPlayer.this.audioSpectrumListeners.listIterator();
            while (it.hasNext()) {
                AudioSpectrumListener listener = it.next().get();
                if (listener != null) {
                    if (evt.queryTimestamp()) {
                        double timestamp = NativeMediaPlayer.this.playerGetPresentationTime();
                        evt.setTimestamp(timestamp);
                    }
                    listener.onAudioSpectrumEvent(evt);
                } else {
                    it.remove();
                }
            }
        }

        public void postEvent(PlayerEvent event) {
            if (this.eventQueue != null) {
                this.eventQueue.offer(event);
            }
        }

        public void terminateLoop() {
            this.stopped = true;
            try {
                this.eventQueue.put(new PlayerEvent());
            } catch (InterruptedException e2) {
            }
        }

        private void sendFakeBufferProgressEvent() {
            String contentType = NativeMediaPlayer.this.media.getLocator().getContentType();
            String protocol = NativeMediaPlayer.this.media.getLocator().getProtocol();
            if ((contentType != null && (contentType.equals(MediaUtils.CONTENT_TYPE_M3U) || contentType.equals(MediaUtils.CONTENT_TYPE_M3U8))) || (protocol != null && !protocol.equals("http") && !protocol.equals("https"))) {
                HandleBufferEvents(new BufferProgressEvent(NativeMediaPlayer.this.getDuration(), 0L, 1L, 1L));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void onNativeInit() {
        try {
            playerInit();
        } catch (MediaException me) {
            sendPlayerMediaErrorEvent(me.getMediaError().code());
        }
    }

    @Override // com.sun.media.jfxmedia.MediaPlayer
    public void addMediaErrorListener(MediaErrorListener listener) {
        if (listener != null) {
            this.errorListeners.add(new WeakReference<>(listener));
            synchronized (this.cachedErrorEvents) {
                if (!this.cachedErrorEvents.isEmpty() && !this.errorListeners.isEmpty()) {
                    this.cachedErrorEvents.stream().forEach(evt -> {
                        sendPlayerEvent(evt);
                    });
                    this.cachedErrorEvents.clear();
                }
            }
        }
    }

    @Override // com.sun.media.jfxmedia.MediaPlayer
    public void removeMediaErrorListener(MediaErrorListener listener) {
        if (listener != null) {
            ListIterator<WeakReference<MediaErrorListener>> it = this.errorListeners.listIterator();
            while (it.hasNext()) {
                MediaErrorListener l2 = it.next().get();
                if (l2 == null || l2 == listener) {
                    it.remove();
                }
            }
        }
    }

    @Override // com.sun.media.jfxmedia.MediaPlayer
    public void addMediaPlayerListener(PlayerStateListener listener) {
        if (listener != null) {
            synchronized (this.cachedStateEvents) {
                if (!this.cachedStateEvents.isEmpty() && this.playerStateListeners.isEmpty()) {
                    for (PlayerStateEvent evt : this.cachedStateEvents) {
                        switch (evt.getState()) {
                            case READY:
                                listener.onReady(evt);
                                break;
                            case PLAYING:
                                listener.onPlaying(evt);
                                break;
                            case STOPPED:
                                listener.onStop(evt);
                                break;
                            case FINISHED:
                                listener.onFinish(evt);
                                break;
                            case PAUSED:
                                listener.onPause(evt);
                                break;
                            case STALLED:
                                listener.onStall(evt);
                                break;
                            case HALTED:
                                listener.onHalt(evt);
                                break;
                        }
                    }
                    this.cachedStateEvents.clear();
                }
                this.playerStateListeners.add(new WeakReference<>(listener));
            }
        }
    }

    @Override // com.sun.media.jfxmedia.MediaPlayer
    public void removeMediaPlayerListener(PlayerStateListener listener) {
        if (listener != null) {
            ListIterator<WeakReference<PlayerStateListener>> it = this.playerStateListeners.listIterator();
            while (it.hasNext()) {
                PlayerStateListener l2 = it.next().get();
                if (l2 == null || l2 == listener) {
                    it.remove();
                }
            }
        }
    }

    @Override // com.sun.media.jfxmedia.MediaPlayer
    public void addMediaTimeListener(PlayerTimeListener listener) {
        if (listener != null) {
            synchronized (this.cachedTimeEvents) {
                if (!this.cachedTimeEvents.isEmpty() && this.playerTimeListeners.isEmpty()) {
                    for (PlayerTimeEvent evt : this.cachedTimeEvents) {
                        listener.onDurationChanged(evt.getTime());
                    }
                    this.cachedTimeEvents.clear();
                } else {
                    double duration = getDuration();
                    if (duration != Double.POSITIVE_INFINITY) {
                        listener.onDurationChanged(duration);
                    }
                }
                this.playerTimeListeners.add(new WeakReference<>(listener));
            }
        }
    }

    @Override // com.sun.media.jfxmedia.MediaPlayer
    public void removeMediaTimeListener(PlayerTimeListener listener) {
        if (listener != null) {
            ListIterator<WeakReference<PlayerTimeListener>> it = this.playerTimeListeners.listIterator();
            while (it.hasNext()) {
                PlayerTimeListener l2 = it.next().get();
                if (l2 == null || l2 == listener) {
                    it.remove();
                }
            }
        }
    }

    @Override // com.sun.media.jfxmedia.MediaPlayer
    public void addVideoTrackSizeListener(VideoTrackSizeListener listener) {
        if (listener != null) {
            if (this.frameWidth != -1 && this.frameHeight != -1) {
                listener.onSizeChanged(this.frameWidth, this.frameHeight);
            }
            this.videoTrackSizeListeners.add(new WeakReference<>(listener));
        }
    }

    @Override // com.sun.media.jfxmedia.MediaPlayer
    public void removeVideoTrackSizeListener(VideoTrackSizeListener listener) {
        if (listener != null) {
            ListIterator<WeakReference<VideoTrackSizeListener>> it = this.videoTrackSizeListeners.listIterator();
            while (it.hasNext()) {
                VideoTrackSizeListener l2 = it.next().get();
                if (l2 == null || l2 == listener) {
                    it.remove();
                }
            }
        }
    }

    @Override // com.sun.media.jfxmedia.MediaPlayer
    public void addMarkerListener(MarkerListener listener) {
        if (listener != null) {
            this.markerListeners.add(new WeakReference<>(listener));
        }
    }

    @Override // com.sun.media.jfxmedia.MediaPlayer
    public void removeMarkerListener(MarkerListener listener) {
        if (listener != null) {
            ListIterator<WeakReference<MarkerListener>> it = this.markerListeners.listIterator();
            while (it.hasNext()) {
                MarkerListener l2 = it.next().get();
                if (l2 == null || l2 == listener) {
                    it.remove();
                }
            }
        }
    }

    @Override // com.sun.media.jfxmedia.MediaPlayer
    public void addBufferListener(BufferListener listener) {
        if (listener != null) {
            synchronized (this.cachedBufferEvents) {
                if (!this.cachedBufferEvents.isEmpty() && this.bufferListeners.isEmpty()) {
                    this.cachedBufferEvents.stream().forEach(evt -> {
                        listener.onBufferProgress(evt);
                    });
                    this.cachedBufferEvents.clear();
                }
                this.bufferListeners.add(new WeakReference<>(listener));
            }
        }
    }

    @Override // com.sun.media.jfxmedia.MediaPlayer
    public void removeBufferListener(BufferListener listener) {
        if (listener != null) {
            ListIterator<WeakReference<BufferListener>> it = this.bufferListeners.listIterator();
            while (it.hasNext()) {
                BufferListener l2 = it.next().get();
                if (l2 == null || l2 == listener) {
                    it.remove();
                }
            }
        }
    }

    @Override // com.sun.media.jfxmedia.MediaPlayer
    public void addAudioSpectrumListener(AudioSpectrumListener listener) {
        if (listener != null) {
            this.audioSpectrumListeners.add(new WeakReference<>(listener));
        }
    }

    @Override // com.sun.media.jfxmedia.MediaPlayer
    public void removeAudioSpectrumListener(AudioSpectrumListener listener) {
        if (listener != null) {
            ListIterator<WeakReference<AudioSpectrumListener>> it = this.audioSpectrumListeners.listIterator();
            while (it.hasNext()) {
                AudioSpectrumListener l2 = it.next().get();
                if (l2 == null || l2 == listener) {
                    it.remove();
                }
            }
        }
    }

    @Override // com.sun.media.jfxmedia.MediaPlayer
    public VideoRenderControl getVideoRenderControl() {
        return this.videoRenderControl;
    }

    @Override // com.sun.media.jfxmedia.MediaPlayer
    public Media getMedia() {
        return this.media;
    }

    @Override // com.sun.media.jfxmedia.MediaPlayer
    public void setAudioSyncDelay(long delay) {
        try {
            playerSetAudioSyncDelay(delay);
        } catch (MediaException me) {
            sendPlayerEvent(new MediaErrorEvent(this, me.getMediaError()));
        }
    }

    @Override // com.sun.media.jfxmedia.MediaPlayer
    public long getAudioSyncDelay() {
        try {
            return playerGetAudioSyncDelay();
        } catch (MediaException me) {
            sendPlayerEvent(new MediaErrorEvent(this, me.getMediaError()));
            return 0L;
        }
    }

    @Override // com.sun.media.jfxmedia.MediaPlayer
    public void play() {
        try {
            if (this.isStartTimeUpdated) {
                playerSeek(this.startTime);
            }
            this.isMediaPulseEnabled.set(true);
            playerPlay();
        } catch (MediaException me) {
            sendPlayerEvent(new MediaErrorEvent(this, me.getMediaError()));
        }
    }

    @Override // com.sun.media.jfxmedia.MediaPlayer
    public void stop() {
        try {
            playerStop();
            playerSeek(this.startTime);
        } catch (MediaException e2) {
            MediaUtils.warning(this, "stop() failed!");
        }
    }

    @Override // com.sun.media.jfxmedia.MediaPlayer
    public void pause() {
        try {
            playerPause();
        } catch (MediaException me) {
            sendPlayerEvent(new MediaErrorEvent(this, me.getMediaError()));
        }
    }

    @Override // com.sun.media.jfxmedia.MediaPlayer
    public float getRate() {
        try {
            return playerGetRate();
        } catch (MediaException me) {
            sendPlayerEvent(new MediaErrorEvent(this, me.getMediaError()));
            return 0.0f;
        }
    }

    @Override // com.sun.media.jfxmedia.MediaPlayer
    public void setRate(float rate) {
        try {
            playerSetRate(rate);
        } catch (MediaException e2) {
            MediaUtils.warning(this, "setRate(" + rate + ") failed!");
        }
    }

    @Override // com.sun.media.jfxmedia.MediaPlayer
    public double getPresentationTime() {
        try {
            return playerGetPresentationTime();
        } catch (MediaException e2) {
            return -1.0d;
        }
    }

    @Override // com.sun.media.jfxmedia.MediaPlayer
    public float getVolume() {
        try {
            return playerGetVolume();
        } catch (MediaException me) {
            sendPlayerEvent(new MediaErrorEvent(this, me.getMediaError()));
            return 0.0f;
        }
    }

    @Override // com.sun.media.jfxmedia.MediaPlayer
    public void setVolume(float vol) {
        if (vol < 0.0f) {
            vol = 0.0f;
        } else if (vol > 1.0f) {
            vol = 1.0f;
        }
        try {
            playerSetVolume(vol);
        } catch (MediaException me) {
            sendPlayerEvent(new MediaErrorEvent(this, me.getMediaError()));
        }
    }

    @Override // com.sun.media.jfxmedia.MediaPlayer
    public boolean getMute() {
        try {
            return playerGetMute();
        } catch (MediaException me) {
            sendPlayerEvent(new MediaErrorEvent(this, me.getMediaError()));
            return false;
        }
    }

    @Override // com.sun.media.jfxmedia.MediaPlayer
    public void setMute(boolean enable) {
        try {
            playerSetMute(enable);
        } catch (MediaException me) {
            sendPlayerEvent(new MediaErrorEvent(this, me.getMediaError()));
        }
    }

    @Override // com.sun.media.jfxmedia.MediaPlayer
    public float getBalance() {
        try {
            return playerGetBalance();
        } catch (MediaException me) {
            sendPlayerEvent(new MediaErrorEvent(this, me.getMediaError()));
            return 0.0f;
        }
    }

    @Override // com.sun.media.jfxmedia.MediaPlayer
    public void setBalance(float bal) {
        if (bal < -1.0f) {
            bal = -1.0f;
        } else if (bal > 1.0f) {
            bal = 1.0f;
        }
        try {
            playerSetBalance(bal);
        } catch (MediaException me) {
            sendPlayerEvent(new MediaErrorEvent(this, me.getMediaError()));
        }
    }

    @Override // com.sun.media.jfxmedia.MediaPlayer
    public double getDuration() {
        try {
            return playerGetDuration();
        } catch (MediaException e2) {
            return Double.POSITIVE_INFINITY;
        }
    }

    @Override // com.sun.media.jfxmedia.MediaPlayer
    public double getStartTime() {
        return this.startTime;
    }

    @Override // com.sun.media.jfxmedia.MediaPlayer
    public void setStartTime(double startTime) {
        try {
            this.markerLock.lock();
            this.startTime = startTime;
            if (this.playerState != PlayerStateEvent.PlayerState.PLAYING && this.playerState != PlayerStateEvent.PlayerState.FINISHED && this.playerState != PlayerStateEvent.PlayerState.STOPPED) {
                playerSeek(startTime);
            } else if (this.playerState == PlayerStateEvent.PlayerState.STOPPED) {
                this.isStartTimeUpdated = true;
            }
        } finally {
            this.markerLock.unlock();
        }
    }

    @Override // com.sun.media.jfxmedia.MediaPlayer
    public double getStopTime() {
        return this.stopTime;
    }

    @Override // com.sun.media.jfxmedia.MediaPlayer
    public void setStopTime(double stopTime) {
        try {
            this.markerLock.lock();
            this.stopTime = stopTime;
            this.isStopTimeSet = true;
            createMediaPulse();
        } finally {
            this.markerLock.unlock();
        }
    }

    @Override // com.sun.media.jfxmedia.MediaPlayer
    public void seek(double streamTime) {
        if (this.playerState == PlayerStateEvent.PlayerState.STOPPED) {
            return;
        }
        if (streamTime < 0.0d) {
            streamTime = 0.0d;
        } else {
            double duration = getDuration();
            if (duration >= 0.0d && streamTime > duration) {
                streamTime = duration;
            }
        }
        if (!this.isMediaPulseEnabled.get() && ((this.playerState == PlayerStateEvent.PlayerState.PLAYING || this.playerState == PlayerStateEvent.PlayerState.PAUSED || this.playerState == PlayerStateEvent.PlayerState.FINISHED) && getStartTime() <= streamTime && streamTime <= getStopTime())) {
            this.isMediaPulseEnabled.set(true);
        }
        this.markerLock.lock();
        try {
            this.timeBeforeSeek = getPresentationTime();
            this.timeAfterSeek = streamTime;
            this.checkSeek = this.timeBeforeSeek != this.timeAfterSeek;
            this.previousTime = streamTime;
            this.firedMarkerTime = -1.0d;
            try {
                playerSeek(streamTime);
            } catch (MediaException e2) {
                MediaUtils.warning(this, "seek(" + streamTime + ") failed!");
            }
        } finally {
            this.markerLock.unlock();
        }
    }

    @Override // com.sun.media.jfxmedia.MediaPlayer
    public PlayerStateEvent.PlayerState getState() {
        return this.playerState;
    }

    @Override // com.sun.media.jfxmedia.MediaPlayer
    public final void dispose() {
        this.disposeLock.lock();
        try {
            if (!this.isDisposed) {
                destroyMediaPulse();
                if (this.eventLoop != null) {
                    this.eventLoop.terminateLoop();
                    this.eventLoop = null;
                }
                synchronized (this.firstFrameLock) {
                    if (this.firstFrameEvent != null) {
                        this.firstFrameEvent.getFrameData().releaseFrame();
                        this.firstFrameEvent = null;
                    }
                }
                playerDispose();
                if (this.media != null) {
                    this.media.dispose();
                    this.media = null;
                }
                if (this.videoUpdateListeners != null) {
                    ListIterator<WeakReference<VideoRendererListener>> it = this.videoUpdateListeners.listIterator();
                    while (it.hasNext()) {
                        VideoRendererListener l2 = it.next().get();
                        if (l2 != null) {
                            l2.releaseVideoFrames();
                        } else {
                            it.remove();
                        }
                    }
                    this.videoUpdateListeners.clear();
                }
                if (this.playerStateListeners != null) {
                    this.playerStateListeners.clear();
                }
                if (this.videoTrackSizeListeners != null) {
                    this.videoTrackSizeListeners.clear();
                }
                if (this.videoFrameRateListeners != null) {
                    this.videoFrameRateListeners.clear();
                }
                if (this.cachedStateEvents != null) {
                    this.cachedStateEvents.clear();
                }
                if (this.cachedTimeEvents != null) {
                    this.cachedTimeEvents.clear();
                }
                if (this.cachedBufferEvents != null) {
                    this.cachedBufferEvents.clear();
                }
                if (this.errorListeners != null) {
                    this.errorListeners.clear();
                }
                if (this.playerTimeListeners != null) {
                    this.playerTimeListeners.clear();
                }
                if (this.markerListeners != null) {
                    this.markerListeners.clear();
                }
                if (this.bufferListeners != null) {
                    this.bufferListeners.clear();
                }
                if (this.audioSpectrumListeners != null) {
                    this.audioSpectrumListeners.clear();
                }
                if (this.videoRenderControl != null) {
                    this.videoRenderControl = null;
                }
                if (this.onDispose != null) {
                    this.onDispose.run();
                }
                this.isDisposed = true;
            }
        } finally {
            this.disposeLock.unlock();
        }
    }

    @Override // com.sun.media.jfxmedia.MediaPlayer
    public boolean isErrorEventCached() {
        synchronized (this.cachedErrorEvents) {
            if (this.cachedErrorEvents.isEmpty()) {
                return false;
            }
            return true;
        }
    }

    protected void sendWarning(int warningCode, String warningMessage) {
        if (this.eventLoop != null) {
            String message = String.format("Internal media warning: %d", Integer.valueOf(warningCode));
            if (warningMessage != null) {
                message = message + ": " + warningMessage;
            }
            this.eventLoop.postEvent(new WarningEvent(this, message));
        }
    }

    protected void sendPlayerEvent(PlayerEvent evt) {
        if (this.eventLoop != null) {
            this.eventLoop.postEvent(evt);
        }
    }

    protected void sendPlayerHaltEvent(String message, double time) {
        Logger.logMsg(4, message);
        if (this.eventLoop != null) {
            this.eventLoop.postEvent(new PlayerStateEvent(PlayerStateEvent.PlayerState.HALTED, time, message));
        }
    }

    protected void sendPlayerMediaErrorEvent(int errorCode) {
        sendPlayerEvent(new MediaErrorEvent(this, MediaError.getFromCode(errorCode)));
    }

    protected void sendPlayerStateEvent(int eventID, double time) {
        switch (eventID) {
            case 101:
                sendPlayerEvent(new PlayerStateEvent(PlayerStateEvent.PlayerState.READY, time));
                break;
            case 102:
                sendPlayerEvent(new PlayerStateEvent(PlayerStateEvent.PlayerState.PLAYING, time));
                break;
            case 103:
                sendPlayerEvent(new PlayerStateEvent(PlayerStateEvent.PlayerState.PAUSED, time));
                break;
            case 104:
                sendPlayerEvent(new PlayerStateEvent(PlayerStateEvent.PlayerState.STOPPED, time));
                break;
            case 105:
                sendPlayerEvent(new PlayerStateEvent(PlayerStateEvent.PlayerState.STALLED, time));
                break;
            case 106:
                sendPlayerEvent(new PlayerStateEvent(PlayerStateEvent.PlayerState.FINISHED, time));
                break;
        }
    }

    protected void sendNewFrameEvent(long nativeRef) {
        NativeVideoBuffer newFrameData = NativeVideoBuffer.createVideoBuffer(nativeRef);
        sendPlayerEvent(new NewFrameEvent(newFrameData));
    }

    protected void sendFrameSizeChangedEvent(int width, int height) {
        sendPlayerEvent(new FrameSizeChangedEvent(width, height));
    }

    protected void sendAudioTrack(boolean enabled, long trackID, String name, int encoding, String language, int numChannels, int channelMask, float sampleRate) {
        Locale locale = null;
        if (!language.equals(LanguageTag.UNDETERMINED)) {
            locale = new Locale(language);
        }
        Track track = new AudioTrack(enabled, trackID, name, locale, Track.Encoding.toEncoding(encoding), numChannels, channelMask, sampleRate);
        TrackEvent evt = new TrackEvent(track);
        sendPlayerEvent(evt);
    }

    protected void sendVideoTrack(boolean enabled, long trackID, String name, int encoding, int width, int height, float frameRate, boolean hasAlphaChannel) {
        Track track = new VideoTrack(enabled, trackID, name, null, Track.Encoding.toEncoding(encoding), new VideoResolution(width, height), frameRate, hasAlphaChannel);
        TrackEvent evt = new TrackEvent(track);
        sendPlayerEvent(evt);
    }

    protected void sendSubtitleTrack(boolean enabled, long trackID, String name, int encoding, String language) {
        Locale locale = null;
        if (null != language) {
            locale = new Locale(language);
        }
        Track track = new SubtitleTrack(enabled, trackID, name, locale, Track.Encoding.toEncoding(encoding));
        sendPlayerEvent(new TrackEvent(track));
    }

    protected void sendMarkerEvent(String name, double time) {
        sendPlayerEvent(new MarkerEvent(name, time));
    }

    protected void sendDurationUpdateEvent(double duration) {
        sendPlayerEvent(new PlayerTimeEvent(duration));
    }

    protected void sendBufferProgressEvent(double clipDuration, long bufferStart, long bufferStop, long bufferPosition) {
        sendPlayerEvent(new BufferProgressEvent(clipDuration, bufferStart, bufferStop, bufferPosition));
    }

    protected void sendAudioSpectrumEvent(double timestamp, double duration, boolean queryTimestamp) {
        sendPlayerEvent(new AudioSpectrumEvent(getAudioSpectrum(), timestamp, duration, queryTimestamp));
    }

    @Override // com.sun.media.jfxmediaimpl.MarkerStateListener
    public void markerStateChanged(boolean hasMarkers) {
        if (hasMarkers) {
            this.markerLock.lock();
            try {
                this.previousTime = getPresentationTime();
                createMediaPulse();
                return;
            } finally {
                this.markerLock.unlock();
            }
        }
        if (!this.isStopTimeSet) {
            destroyMediaPulse();
        }
    }

    private void createMediaPulse() {
        this.mediaPulseLock.lock();
        try {
            if (this.mediaPulseTimer == null) {
                this.mediaPulseTimer = new Timer(true);
                this.mediaPulseTimer.scheduleAtFixedRate(new MediaPulseTask(this), 0L, 40L);
            }
        } finally {
            this.mediaPulseLock.unlock();
        }
    }

    private void destroyMediaPulse() {
        this.mediaPulseLock.lock();
        try {
            if (this.mediaPulseTimer != null) {
                this.mediaPulseTimer.cancel();
                this.mediaPulseTimer = null;
            }
        } finally {
            this.mediaPulseLock.unlock();
        }
    }

    boolean doMediaPulseTask() {
        if (!this.isMediaPulseEnabled.get()) {
            return true;
        }
        this.disposeLock.lock();
        if (this.isDisposed) {
            this.disposeLock.unlock();
            return false;
        }
        double thisTime = getPresentationTime();
        this.markerLock.lock();
        try {
            if (this.checkSeek) {
                if (this.timeAfterSeek > this.timeBeforeSeek) {
                    if (thisTime < this.timeAfterSeek) {
                        return true;
                    }
                    this.checkSeek = false;
                } else if (this.timeAfterSeek < this.timeBeforeSeek) {
                    if (thisTime >= this.timeBeforeSeek) {
                        this.disposeLock.unlock();
                        this.markerLock.unlock();
                        return true;
                    }
                    this.checkSeek = false;
                }
            }
            Map.Entry<Double, String> marker = this.media.getNextMarker(this.previousTime, true);
            while (marker != null) {
                double nextMarkerTime = marker.getKey().doubleValue();
                if (nextMarkerTime > thisTime) {
                    break;
                }
                if (nextMarkerTime != this.firedMarkerTime && nextMarkerTime >= this.previousTime && nextMarkerTime >= getStartTime() && nextMarkerTime <= getStopTime()) {
                    MarkerEvent evt = new MarkerEvent(marker.getValue(), nextMarkerTime);
                    ListIterator<WeakReference<MarkerListener>> it = this.markerListeners.listIterator();
                    while (it.hasNext()) {
                        MarkerListener listener = it.next().get();
                        if (listener != null) {
                            listener.onMarker(evt);
                        } else {
                            it.remove();
                        }
                    }
                    this.firedMarkerTime = nextMarkerTime;
                }
                marker = this.media.getNextMarker(nextMarkerTime, false);
            }
            this.previousTime = thisTime;
            if (this.isStopTimeSet && thisTime >= this.stopTime) {
                playerFinish();
            }
            this.disposeLock.unlock();
            this.markerLock.unlock();
            return true;
        } finally {
            this.disposeLock.unlock();
            this.markerLock.unlock();
        }
    }

    protected AudioEqualizer createNativeAudioEqualizer(long nativeRef) {
        return new NativeAudioEqualizer(nativeRef);
    }

    protected AudioSpectrum createNativeAudioSpectrum(long nativeRef) {
        return new NativeAudioSpectrum(nativeRef);
    }
}
