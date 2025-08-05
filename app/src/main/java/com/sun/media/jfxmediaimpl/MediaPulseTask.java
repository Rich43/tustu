package com.sun.media.jfxmediaimpl;

import java.lang.ref.WeakReference;
import java.util.TimerTask;

/* compiled from: NativeMediaPlayer.java */
/* loaded from: jfxrt.jar:com/sun/media/jfxmediaimpl/MediaPulseTask.class */
class MediaPulseTask extends TimerTask {
    WeakReference<NativeMediaPlayer> playerRef;

    MediaPulseTask(NativeMediaPlayer player) {
        this.playerRef = new WeakReference<>(player);
    }

    @Override // java.util.TimerTask, java.lang.Runnable
    public void run() {
        NativeMediaPlayer player = this.playerRef.get();
        if (player != null) {
            if (!player.doMediaPulseTask()) {
                cancel();
                return;
            }
            return;
        }
        cancel();
    }
}
