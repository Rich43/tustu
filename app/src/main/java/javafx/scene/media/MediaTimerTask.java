package javafx.scene.media;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;

/* compiled from: MediaPlayer.java */
/* loaded from: jfxrt.jar:javafx/scene/media/MediaTimerTask.class */
class MediaTimerTask extends TimerTask {
    private Timer mediaTimer = null;
    static final Object timerLock = new Object();
    private WeakReference<MediaPlayer> playerRef;

    MediaTimerTask(MediaPlayer player) {
        this.playerRef = new WeakReference<>(player);
    }

    void start() {
        if (this.mediaTimer == null) {
            this.mediaTimer = new Timer(true);
            this.mediaTimer.scheduleAtFixedRate(this, 0L, 100L);
        }
    }

    void stop() {
        if (this.mediaTimer != null) {
            this.mediaTimer.cancel();
            this.mediaTimer = null;
        }
    }

    @Override // java.util.TimerTask, java.lang.Runnable
    public void run() {
        synchronized (timerLock) {
            MediaPlayer player = this.playerRef.get();
            if (player != null) {
                Platform.runLater(() -> {
                    synchronized (timerLock) {
                        player.updateTime();
                    }
                });
            } else {
                cancel();
            }
        }
    }
}
