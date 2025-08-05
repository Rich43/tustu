package com.sun.javafx.webkit.prism;

import com.sun.javafx.media.PrismMediaFrameHandler;
import com.sun.media.jfxmedia.Media;
import com.sun.media.jfxmedia.MediaManager;
import com.sun.media.jfxmedia.MediaPlayer;
import com.sun.media.jfxmedia.control.VideoDataBuffer;
import com.sun.media.jfxmedia.events.BufferListener;
import com.sun.media.jfxmedia.events.BufferProgressEvent;
import com.sun.media.jfxmedia.events.MediaErrorListener;
import com.sun.media.jfxmedia.events.NewFrameEvent;
import com.sun.media.jfxmedia.events.PlayerStateEvent;
import com.sun.media.jfxmedia.events.PlayerStateListener;
import com.sun.media.jfxmedia.events.PlayerTimeListener;
import com.sun.media.jfxmedia.events.VideoRendererListener;
import com.sun.media.jfxmedia.events.VideoTrackSizeListener;
import com.sun.media.jfxmedia.locator.Locator;
import com.sun.media.jfxmedia.track.AudioTrack;
import com.sun.media.jfxmedia.track.Track;
import com.sun.media.jfxmedia.track.VideoTrack;
import com.sun.prism.Graphics;
import com.sun.prism.Texture;
import com.sun.prism.paint.Color;
import com.sun.webkit.graphics.WCGraphicsContext;
import com.sun.webkit.graphics.WCMediaPlayer;
import java.lang.Thread;
import java.net.URI;
import java.util.List;
import java.util.logging.Level;

/* loaded from: jfxrt.jar:com/sun/javafx/webkit/prism/WCMediaPlayerImpl.class */
final class WCMediaPlayerImpl extends WCMediaPlayer implements PlayerStateListener, MediaErrorListener, VideoTrackSizeListener, BufferListener, PlayerTimeListener {
    private volatile MediaPlayer player;
    private volatile CreateThread createThread;
    private volatile PrismMediaFrameHandler frameHandler;
    private final Object lock = new Object();
    private boolean gotFirstFrame = false;
    private int finished = 0;
    private float bufferedStart = 0.0f;
    private float bufferedEnd = 0.0f;
    private boolean buffering = false;
    private final MediaFrameListener frameListener = new MediaFrameListener();

    WCMediaPlayerImpl() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public MediaPlayer getPlayer() {
        synchronized (this.lock) {
            if (this.createThread != null) {
                return null;
            }
            return this.player;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setPlayer(MediaPlayer p2) {
        synchronized (this.lock) {
            this.player = p2;
            installListeners();
            this.frameHandler = PrismMediaFrameHandler.getHandler(this.player);
        }
        this.finished = 0;
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/webkit/prism/WCMediaPlayerImpl$CreateThread.class */
    private final class CreateThread extends Thread {
        private boolean cancelled;
        private final String url;
        private final String userAgent;

        private CreateThread(String url, String userAgent) {
            this.cancelled = false;
            this.url = url;
            this.userAgent = userAgent;
            WCMediaPlayerImpl.this.gotFirstFrame = false;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            if (WCMediaPlayerImpl.verbose) {
                WCMediaPlayerImpl.log.log(Level.FINE, "CreateThread: started, url={0}", this.url);
            }
            WCMediaPlayerImpl.this.notifyNetworkStateChanged(2);
            WCMediaPlayerImpl.this.notifyReadyStateChanged(0);
            try {
                Locator locator = new Locator(new URI(this.url));
                if (this.userAgent != null) {
                    locator.setConnectionProperty("User-Agent", this.userAgent);
                }
                locator.init();
                if (WCMediaPlayerImpl.verbose) {
                    WCMediaPlayerImpl.log.fine("CreateThread: locator created");
                }
                MediaPlayer p2 = MediaManager.getPlayer(locator);
                synchronized (WCMediaPlayerImpl.this.lock) {
                    if (this.cancelled) {
                        if (WCMediaPlayerImpl.verbose) {
                            WCMediaPlayerImpl.log.log(Level.FINE, "CreateThread: cancelled");
                        }
                        p2.dispose();
                    } else {
                        WCMediaPlayerImpl.this.createThread = null;
                        WCMediaPlayerImpl.this.setPlayer(p2);
                        if (WCMediaPlayerImpl.verbose) {
                            WCMediaPlayerImpl.log.log(Level.FINE, "CreateThread: completed");
                        }
                    }
                }
            } catch (Exception ex) {
                if (WCMediaPlayerImpl.verbose) {
                    WCMediaPlayerImpl.log.log(Level.WARNING, "CreateThread ERROR: {0}", ex.toString());
                    ex.printStackTrace(System.out);
                }
                WCMediaPlayerImpl.this.onError(this, 0, ex.getMessage());
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void cancel() {
            synchronized (WCMediaPlayerImpl.this.lock) {
                this.cancelled = true;
            }
        }
    }

    @Override // com.sun.webkit.graphics.WCMediaPlayer
    protected void load(String url, String userAgent) {
        synchronized (this.lock) {
            if (this.createThread != null) {
                this.createThread.cancel();
            }
            disposePlayer();
            this.createThread = new CreateThread(url, userAgent);
        }
        if (getPreload() != 0) {
            this.createThread.start();
        }
    }

    @Override // com.sun.webkit.graphics.WCMediaPlayer
    protected void cancelLoad() {
        synchronized (this.lock) {
            if (this.createThread != null) {
                this.createThread.cancel();
            }
        }
        MediaPlayer p2 = getPlayer();
        if (p2 != null) {
            p2.stop();
        }
        notifyNetworkStateChanged(0);
        notifyReadyStateChanged(0);
    }

    @Override // com.sun.webkit.graphics.WCMediaPlayer
    protected void disposePlayer() {
        MediaPlayer old;
        synchronized (this.lock) {
            removeListeners();
            old = this.player;
            this.player = null;
            if (this.frameHandler != null) {
                this.frameHandler.releaseTextures();
                this.frameHandler = null;
            }
        }
        if (old != null) {
            old.stop();
            old.dispose();
            if (this.frameListener != null) {
                this.frameListener.releaseVideoFrames();
            }
        }
    }

    private void installListeners() {
        if (null != this.player) {
            this.player.addMediaPlayerListener(this);
            this.player.addMediaErrorListener(this);
            this.player.addVideoTrackSizeListener(this);
            this.player.addBufferListener(this);
            this.player.getVideoRenderControl().addVideoRendererListener(this.frameListener);
        }
    }

    private void removeListeners() {
        if (null != this.player) {
            this.player.removeMediaPlayerListener(this);
            this.player.removeMediaErrorListener(this);
            this.player.removeVideoTrackSizeListener(this);
            this.player.removeBufferListener(this);
            this.player.getVideoRenderControl().removeVideoRendererListener(this.frameListener);
        }
    }

    @Override // com.sun.webkit.graphics.WCMediaPlayer
    protected void prepareToPlay() {
        Thread t2;
        synchronized (this.lock) {
            if (this.player == null && (t2 = this.createThread) != null && t2.getState() == Thread.State.NEW) {
                t2.start();
            }
        }
    }

    @Override // com.sun.webkit.graphics.WCMediaPlayer
    protected void play() {
        MediaPlayer p2 = getPlayer();
        if (p2 != null) {
            p2.play();
            notifyPaused(false);
        }
    }

    @Override // com.sun.webkit.graphics.WCMediaPlayer
    protected void pause() {
        MediaPlayer p2 = getPlayer();
        if (p2 != null) {
            p2.pause();
            notifyPaused(true);
        }
    }

    @Override // com.sun.webkit.graphics.WCMediaPlayer
    protected float getCurrentTime() {
        MediaPlayer p2 = getPlayer();
        if (p2 == null) {
            return 0.0f;
        }
        if (this.finished == 0) {
            return (float) p2.getPresentationTime();
        }
        if (this.finished > 0) {
            return (float) p2.getDuration();
        }
        return 0.0f;
    }

    @Override // com.sun.webkit.graphics.WCMediaPlayer
    protected void seek(final float time) {
        MediaPlayer p2 = getPlayer();
        if (p2 != null) {
            this.finished = 0;
            if (getReadyState() >= 1) {
                notifySeeking(true, 1);
            } else {
                notifySeeking(true, 0);
            }
            p2.seek(time);
            Thread seekCompletedThread = new Thread(new Runnable() { // from class: com.sun.javafx.webkit.prism.WCMediaPlayerImpl.1
                @Override // java.lang.Runnable
                public void run() {
                    MediaPlayer p3;
                    while (WCMediaPlayerImpl.this.isSeeking() && (p3 = WCMediaPlayerImpl.this.getPlayer()) != null) {
                        double cur = p3.getPresentationTime();
                        if (time < 0.01d || Math.abs(cur) >= 0.01d) {
                            WCMediaPlayerImpl.this.notifySeeking(false, 4);
                            return;
                        }
                        try {
                            Thread.sleep(10L);
                        } catch (InterruptedException e2) {
                        }
                    }
                }
            });
            seekCompletedThread.setDaemon(true);
            seekCompletedThread.start();
        }
    }

    @Override // com.sun.webkit.graphics.WCMediaPlayer
    protected void setRate(float rate) {
        MediaPlayer p2 = getPlayer();
        if (p2 != null) {
            p2.setRate(rate);
        }
    }

    @Override // com.sun.webkit.graphics.WCMediaPlayer
    protected void setVolume(float volume) {
        MediaPlayer p2 = getPlayer();
        if (p2 != null) {
            p2.setVolume(volume);
        }
    }

    @Override // com.sun.webkit.graphics.WCMediaPlayer
    protected void setMute(boolean mute) {
        MediaPlayer p2 = getPlayer();
        if (p2 != null) {
            p2.setMute(mute);
        }
    }

    @Override // com.sun.webkit.graphics.WCMediaPlayer
    protected void setSize(int w2, int h2) {
    }

    @Override // com.sun.webkit.graphics.WCMediaPlayer
    protected void setPreservesPitch(boolean preserve) {
    }

    @Override // com.sun.webkit.graphics.WCMediaPlayer
    protected void renderCurrentFrame(WCGraphicsContext gc, int x2, int y2, int w2, int h2) {
        synchronized (this.lock) {
            renderImpl(gc, x2, y2, w2, h2);
        }
    }

    private void renderImpl(WCGraphicsContext gc, int x2, int y2, int w2, int h2) {
        if (verbose) {
            log.log(Level.FINER, ">>(Prism)renderImpl");
        }
        Graphics g2 = (Graphics) gc.getPlatformGraphics();
        Texture texture = null;
        VideoDataBuffer currentFrame = this.frameListener.getLatestFrame();
        if (null != currentFrame) {
            if (null != this.frameHandler) {
                texture = this.frameHandler.getTexture(g2, currentFrame);
            }
            currentFrame.releaseFrame();
        }
        if (texture != null) {
            g2.drawTexture(texture, x2, y2, x2 + w2, y2 + h2, 0.0f, 0.0f, texture.getContentWidth(), texture.getContentHeight());
            texture.unlock();
        } else {
            if (verbose) {
                log.log(Level.FINEST, "  (Prism)renderImpl, texture is null, draw black rect");
            }
            gc.fillRect(x2, y2, w2, h2, Color.BLACK);
        }
        if (verbose) {
            log.log(Level.FINER, "<<(Prism)renderImpl");
        }
    }

    @Override // com.sun.media.jfxmedia.events.PlayerStateListener
    public void onReady(PlayerStateEvent pse) {
        MediaPlayer p2 = getPlayer();
        if (verbose) {
            log.log(Level.FINE, "onReady");
        }
        Media media = p2.getMedia();
        boolean hasVideo = false;
        boolean hasAudio = false;
        if (media != null) {
            List<Track> tracks = media.getTracks();
            if (tracks != null) {
                if (verbose) {
                    log.log(Level.INFO, "{0} track(s) detected:", Integer.valueOf(tracks.size()));
                }
                for (Track track : tracks) {
                    if (track instanceof VideoTrack) {
                        hasVideo = true;
                    } else if (track instanceof AudioTrack) {
                        hasAudio = true;
                    }
                    if (verbose) {
                        log.log(Level.INFO, "track: {0}", track);
                    }
                }
            } else if (verbose) {
                log.log(Level.WARNING, "onReady, tracks IS NULL");
            }
        } else if (verbose) {
            log.log(Level.WARNING, "onReady, media IS NULL");
        }
        if (verbose) {
            log.log(Level.FINE, "onReady, hasVideo:{0}, hasAudio: {1}", new Object[]{Boolean.valueOf(hasVideo), Boolean.valueOf(hasAudio)});
        }
        notifyReady(hasVideo, hasAudio, (float) p2.getDuration());
        if (!hasVideo) {
            notifyReadyStateChanged(4);
        } else if (getReadyState() < 1) {
            if (this.gotFirstFrame) {
                notifyReadyStateChanged(4);
            } else {
                notifyReadyStateChanged(1);
            }
        }
    }

    @Override // com.sun.media.jfxmedia.events.PlayerStateListener
    public void onPlaying(PlayerStateEvent pse) {
        if (verbose) {
            log.log(Level.FINE, "onPlaying");
        }
        notifyPaused(false);
    }

    @Override // com.sun.media.jfxmedia.events.PlayerStateListener
    public void onPause(PlayerStateEvent pse) {
        if (verbose) {
            log.log(Level.FINE, "onPause, time: {0}", Double.valueOf(pse.getTime()));
        }
        notifyPaused(true);
    }

    @Override // com.sun.media.jfxmedia.events.PlayerStateListener
    public void onStop(PlayerStateEvent pse) {
        if (verbose) {
            log.log(Level.FINE, "onStop");
        }
        notifyPaused(true);
    }

    @Override // com.sun.media.jfxmedia.events.PlayerStateListener
    public void onStall(PlayerStateEvent pse) {
        if (verbose) {
            log.log(Level.FINE, "onStall");
        }
    }

    @Override // com.sun.media.jfxmedia.events.PlayerStateListener
    public void onFinish(PlayerStateEvent pse) {
        MediaPlayer p2 = getPlayer();
        if (p2 != null) {
            this.finished = p2.getRate() > 0.0f ? 1 : -1;
            if (verbose) {
                log.log(Level.FINE, "onFinish, time: {0}", Double.valueOf(pse.getTime()));
            }
            notifyFinished();
        }
    }

    @Override // com.sun.media.jfxmedia.events.PlayerStateListener
    public void onHalt(PlayerStateEvent pse) {
        if (verbose) {
            log.log(Level.FINE, "onHalt");
        }
    }

    @Override // com.sun.media.jfxmedia.events.MediaErrorListener
    public void onError(Object source, int errCode, String message) {
        if (verbose) {
            log.log(Level.WARNING, "onError, errCode={0}, msg={1}", new Object[]{Integer.valueOf(errCode), message});
        }
        notifyNetworkStateChanged(5);
        notifyReadyStateChanged(0);
    }

    @Override // com.sun.media.jfxmedia.events.PlayerTimeListener
    public void onDurationChanged(double duration) {
        if (verbose) {
            log.log(Level.FINE, "onDurationChanged, duration={0}", Double.valueOf(duration));
        }
        notifyDurationChanged((float) duration);
    }

    @Override // com.sun.media.jfxmedia.events.VideoTrackSizeListener
    public void onSizeChanged(int width, int height) {
        if (verbose) {
            log.log(Level.FINE, "onSizeChanged, new size = {0} x {1}", new Object[]{Integer.valueOf(width), Integer.valueOf(height)});
        }
        notifySizeChanged(width, height);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyFrameArrived() {
        if (!this.gotFirstFrame) {
            if (getReadyState() >= 1) {
                notifyReadyStateChanged(4);
            }
            this.gotFirstFrame = true;
        }
        if (verbose && this.finished != 0) {
            log.log(Level.FINE, "notifyFrameArrived (after finished) time: {0}", Double.valueOf(getPlayer().getPresentationTime()));
        }
        notifyNewFrame();
    }

    private void updateBufferingStatus() {
        int newNetworkState = this.buffering ? 2 : this.bufferedStart > 0.0f ? 1 : 3;
        if (verbose) {
            log.log(Level.FINE, "updateBufferingStatus, buffered: [{0} - {1}], buffering = {2}", new Object[]{Float.valueOf(this.bufferedStart), Float.valueOf(this.bufferedEnd), Boolean.valueOf(this.buffering)});
        }
        notifyNetworkStateChanged(newNetworkState);
    }

    @Override // com.sun.media.jfxmedia.events.BufferListener
    public void onBufferProgress(BufferProgressEvent event) {
        if (event.getDuration() < 0.0d) {
            return;
        }
        double bytes2seconds = event.getDuration() / event.getBufferStop();
        this.bufferedStart = (float) (bytes2seconds * event.getBufferStart());
        this.bufferedEnd = (float) (bytes2seconds * event.getBufferPosition());
        this.buffering = event.getBufferPosition() < event.getBufferStop();
        float[] ranges = {this.bufferedStart, this.bufferedEnd};
        int bytesLoaded = (int) (event.getBufferPosition() - event.getBufferStart());
        if (verbose) {
            log.log(Level.FINER, "onBufferProgress, bufferStart={0}, bufferStop={1}, bufferPos={2}, duration={3}; notify range [{4},[5]], bytesLoaded: {6}", new Object[]{Long.valueOf(event.getBufferStart()), Long.valueOf(event.getBufferStop()), Long.valueOf(event.getBufferPosition()), Double.valueOf(event.getDuration()), Float.valueOf(ranges[0]), Float.valueOf(ranges[1]), Integer.valueOf(bytesLoaded)});
        }
        notifyBufferChanged(ranges, bytesLoaded);
        updateBufferingStatus();
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/webkit/prism/WCMediaPlayerImpl$MediaFrameListener.class */
    private final class MediaFrameListener implements VideoRendererListener {
        private final Object frameLock;
        private VideoDataBuffer currentFrame;
        private VideoDataBuffer nextFrame;

        private MediaFrameListener() {
            this.frameLock = new Object();
        }

        @Override // com.sun.media.jfxmedia.events.VideoRendererListener
        public void videoFrameUpdated(NewFrameEvent nfe) {
            synchronized (this.frameLock) {
                if (null != this.nextFrame) {
                    this.nextFrame.releaseFrame();
                }
                this.nextFrame = nfe.getFrameData();
                if (null != this.nextFrame) {
                    this.nextFrame.holdFrame();
                }
            }
            WCMediaPlayerImpl.this.notifyFrameArrived();
        }

        @Override // com.sun.media.jfxmedia.events.VideoRendererListener
        public void releaseVideoFrames() {
            synchronized (this.frameLock) {
                if (null != this.nextFrame) {
                    this.nextFrame.releaseFrame();
                    this.nextFrame = null;
                }
                if (null != this.currentFrame) {
                    this.currentFrame.releaseFrame();
                    this.currentFrame = null;
                }
            }
        }

        public VideoDataBuffer getLatestFrame() {
            VideoDataBuffer videoDataBuffer;
            synchronized (this.frameLock) {
                if (null != this.nextFrame) {
                    if (null != this.currentFrame) {
                        this.currentFrame.releaseFrame();
                    }
                    this.currentFrame = this.nextFrame;
                    this.nextFrame = null;
                }
                if (null != this.currentFrame) {
                    this.currentFrame.holdFrame();
                }
                videoDataBuffer = this.currentFrame;
            }
            return videoDataBuffer;
        }
    }
}
