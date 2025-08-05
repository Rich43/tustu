package com.sun.webkit.graphics;

import com.sun.webkit.Invoker;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: jfxrt.jar:com/sun/webkit/graphics/WCMediaPlayer.class */
public abstract class WCMediaPlayer extends Ref {
    protected static final Logger log = Logger.getLogger("webkit.mediaplayer");
    protected static final boolean verbose;
    private long nPtr;
    protected static final int NETWORK_STATE_EMPTY = 0;
    protected static final int NETWORK_STATE_IDLE = 1;
    protected static final int NETWORK_STATE_LOADING = 2;
    protected static final int NETWORK_STATE_LOADED = 3;
    protected static final int NETWORK_STATE_FORMAT_ERROR = 4;
    protected static final int NETWORK_STATE_NETWORK_ERROR = 5;
    protected static final int NETWORK_STATE_DECODE_ERROR = 6;
    protected static final int READY_STATE_HAVE_NOTHING = 0;
    protected static final int READY_STATE_HAVE_METADATA = 1;
    protected static final int READY_STATE_HAVE_CURRENT_DATA = 2;
    protected static final int READY_STATE_HAVE_FUTURE_DATA = 3;
    protected static final int READY_STATE_HAVE_ENOUGH_DATA = 4;
    protected static final int PRELOAD_NONE = 0;
    protected static final int PRELOAD_METADATA = 1;
    protected static final int PRELOAD_AUTO = 2;
    private int networkState = 0;
    private int readyState = 0;
    private int preload = 2;
    private boolean paused = true;
    private boolean seeking = false;
    private Runnable newFrameNotifier = () -> {
        if (this.nPtr != 0) {
            notifyNewFrame(this.nPtr);
        }
    };
    private boolean preserve = true;

    protected abstract void load(String str, String str2);

    protected abstract void cancelLoad();

    protected abstract void disposePlayer();

    protected abstract void prepareToPlay();

    protected abstract void play();

    protected abstract void pause();

    protected abstract float getCurrentTime();

    protected abstract void seek(float f2);

    protected abstract void setRate(float f2);

    protected abstract void setVolume(float f2);

    protected abstract void setMute(boolean z2);

    protected abstract void setSize(int i2, int i3);

    protected abstract void setPreservesPitch(boolean z2);

    protected abstract void renderCurrentFrame(WCGraphicsContext wCGraphicsContext, int i2, int i3, int i4, int i5);

    private native void notifyNetworkStateChanged(long j2, int i2);

    private native void notifyReadyStateChanged(long j2, int i2);

    private native void notifyPaused(long j2, boolean z2);

    private native void notifySeeking(long j2, boolean z2, int i2);

    private native void notifyFinished(long j2);

    private native void notifyReady(long j2, boolean z2, boolean z3, float f2);

    private native void notifyDurationChanged(long j2, float f2);

    private native void notifySizeChanged(long j2, int i2, int i3);

    private native void notifyNewFrame(long j2);

    private native void notifyBufferChanged(long j2, float[] fArr, int i2);

    static {
        if (log.getLevel() == null) {
            verbose = false;
            log.setLevel(Level.OFF);
        } else {
            verbose = true;
            log.log(Level.CONFIG, "webkit.mediaplayer logging is ON, level: {0}", log.getLevel());
        }
    }

    protected WCMediaPlayer() {
    }

    void setNativePointer(long nativePointer) {
        if (nativePointer == 0) {
            throw new IllegalArgumentException("nativePointer is 0");
        }
        if (this.nPtr != 0) {
            throw new IllegalStateException("nPtr is not 0");
        }
        this.nPtr = nativePointer;
    }

    protected boolean getPreservesPitch() {
        return this.preserve;
    }

    protected int getNetworkState() {
        return this.networkState;
    }

    protected int getReadyState() {
        return this.readyState;
    }

    protected int getPreload() {
        return this.preload;
    }

    protected boolean isPaused() {
        return this.paused;
    }

    protected boolean isSeeking() {
        return this.seeking;
    }

    protected void notifyNetworkStateChanged(int networkState) {
        if (this.networkState != networkState) {
            this.networkState = networkState;
            Invoker.getInvoker().invokeOnEventThread(() -> {
                if (this.nPtr != 0) {
                    notifyNetworkStateChanged(this.nPtr, networkState);
                }
            });
        }
    }

    protected void notifyReadyStateChanged(int readyState) {
        if (this.readyState != readyState) {
            this.readyState = readyState;
            Invoker.getInvoker().invokeOnEventThread(() -> {
                if (this.nPtr != 0) {
                    notifyReadyStateChanged(this.nPtr, readyState);
                }
            });
        }
    }

    protected void notifyPaused(boolean paused) {
        if (verbose) {
            log.log(Level.FINE, "notifyPaused, {0} => {1}", new Object[]{Boolean.valueOf(this.paused), Boolean.valueOf(paused)});
        }
        if (this.paused != paused) {
            this.paused = paused;
            Invoker.getInvoker().invokeOnEventThread(() -> {
                if (this.nPtr != 0) {
                    notifyPaused(this.nPtr, paused);
                }
            });
        }
    }

    protected void notifySeeking(boolean seeking, int readyState) {
        if (verbose) {
            log.log(Level.FINE, "notifySeeking, {0} => {1}", new Object[]{Boolean.valueOf(this.seeking), Boolean.valueOf(seeking)});
        }
        if (this.seeking != seeking || this.readyState != readyState) {
            this.seeking = seeking;
            this.readyState = readyState;
            Invoker.getInvoker().invokeOnEventThread(() -> {
                if (this.nPtr != 0) {
                    notifySeeking(this.nPtr, seeking, readyState);
                }
            });
        }
    }

    protected void notifyFinished() {
        Invoker.getInvoker().invokeOnEventThread(() -> {
            if (this.nPtr != 0) {
                notifyFinished(this.nPtr);
            }
        });
    }

    protected void notifyReady(boolean hasVideo, boolean hasAudio, float duration) {
        Invoker.getInvoker().invokeOnEventThread(() -> {
            if (this.nPtr != 0) {
                notifyReady(this.nPtr, hasVideo, hasAudio, duration);
            }
        });
    }

    protected void notifyDurationChanged(float newDuration) {
        Invoker.getInvoker().invokeOnEventThread(() -> {
            if (this.nPtr != 0) {
                notifyDurationChanged(this.nPtr, newDuration);
            }
        });
    }

    protected void notifySizeChanged(int width, int height) {
        Invoker.getInvoker().invokeOnEventThread(() -> {
            if (this.nPtr != 0) {
                notifySizeChanged(this.nPtr, width, height);
            }
        });
    }

    protected void notifyNewFrame() {
        Invoker.getInvoker().invokeOnEventThread(this.newFrameNotifier);
    }

    protected void notifyBufferChanged(float[] ranges, int bytesLoaded) {
        Invoker.getInvoker().invokeOnEventThread(() -> {
            if (this.nPtr != 0) {
                notifyBufferChanged(this.nPtr, ranges, bytesLoaded);
            }
        });
    }

    private void fwkLoad(String url, String userAgent) {
        if (verbose) {
            log.log(Level.FINE, "fwkLoad, url={0}, userAgent={1}", new Object[]{url, userAgent});
        }
        load(url, userAgent);
    }

    private void fwkCancelLoad() {
        if (verbose) {
            log.log(Level.FINE, "fwkCancelLoad");
        }
        cancelLoad();
    }

    private void fwkPrepareToPlay() {
        if (verbose) {
            log.log(Level.FINE, "fwkPrepareToPlay");
        }
        prepareToPlay();
    }

    private void fwkDispose() {
        if (verbose) {
            log.log(Level.FINE, "fwkDispose");
        }
        this.nPtr = 0L;
        cancelLoad();
        disposePlayer();
    }

    private void fwkPlay() {
        if (verbose) {
            log.log(Level.FINE, "fwkPlay");
        }
        play();
    }

    private void fwkPause() {
        if (verbose) {
            log.log(Level.FINE, "fwkPause");
        }
        pause();
    }

    private float fwkGetCurrentTime() {
        float res = getCurrentTime();
        if (verbose) {
            log.log(Level.FINER, "fwkGetCurrentTime(), return {0}", Float.valueOf(res));
        }
        return res;
    }

    private void fwkSeek(float time) {
        if (verbose) {
            log.log(Level.FINE, "fwkSeek({0})", Float.valueOf(time));
        }
        seek(time);
    }

    private void fwkSetRate(float rate) {
        if (verbose) {
            log.log(Level.FINE, "fwkSetRate({0})", Float.valueOf(rate));
        }
        setRate(rate);
    }

    private void fwkSetVolume(float volume) {
        if (verbose) {
            log.log(Level.FINE, "fwkSetVolume({0})", Float.valueOf(volume));
        }
        setVolume(volume);
    }

    private void fwkSetMute(boolean mute) {
        if (verbose) {
            log.log(Level.FINE, "fwkSetMute({0})", Boolean.valueOf(mute));
        }
        setMute(mute);
    }

    private void fwkSetSize(int w2, int h2) {
        setSize(w2, h2);
    }

    private void fwkSetPreservesPitch(boolean preserve) {
        if (verbose) {
            log.log(Level.FINE, "setPreservesPitch({0})", Boolean.valueOf(preserve));
        }
        this.preserve = preserve;
        setPreservesPitch(preserve);
    }

    private void fwkSetPreload(int preload) {
        if (verbose) {
            log.log(Level.FINE, "fwkSetPreload({0})", preload == 0 ? "PRELOAD_NONE" : preload == 1 ? "PRELOAD_METADATA" : preload == 2 ? "PRELOAD_AUTO" : "INVALID VALUE: " + preload);
        }
        this.preload = preload;
    }

    void render(WCGraphicsContext gc, int x2, int y2, int w2, int h2) {
        if (verbose) {
            log.log(Level.FINER, "render(x={0}, y={1}, w={2}, h={3}", new Object[]{Integer.valueOf(x2), Integer.valueOf(y2), Integer.valueOf(w2), Integer.valueOf(h2)});
        }
        renderCurrentFrame(gc, x2, y2, w2, h2);
    }
}
