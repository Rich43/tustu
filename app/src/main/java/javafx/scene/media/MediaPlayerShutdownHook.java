package javafx.scene.media;

import com.sun.javafx.tk.Toolkit;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/* compiled from: MediaPlayer.java */
/* loaded from: jfxrt.jar:javafx/scene/media/MediaPlayerShutdownHook.class */
class MediaPlayerShutdownHook implements Runnable {
    private static final List<WeakReference<MediaPlayer>> playerRefs = new ArrayList();
    private static boolean isShutdown = false;

    MediaPlayerShutdownHook() {
    }

    static {
        Toolkit.getToolkit().addShutdownHook(new MediaPlayerShutdownHook());
    }

    public static void addMediaPlayer(MediaPlayer player) {
        synchronized (playerRefs) {
            if (isShutdown) {
                com.sun.media.jfxmedia.MediaPlayer jfxPlayer = player.retrieveJfxPlayer();
                if (jfxPlayer != null) {
                    jfxPlayer.dispose();
                }
            } else {
                ListIterator<WeakReference<MediaPlayer>> it = playerRefs.listIterator();
                while (it.hasNext()) {
                    MediaPlayer l2 = it.next().get();
                    if (l2 == null) {
                        it.remove();
                    }
                }
                playerRefs.add(new WeakReference<>(player));
            }
        }
    }

    @Override // java.lang.Runnable
    public void run() {
        synchronized (playerRefs) {
            ListIterator<WeakReference<MediaPlayer>> it = playerRefs.listIterator();
            while (it.hasNext()) {
                MediaPlayer player = it.next().get();
                if (player != null) {
                    player.destroyMediaTimer();
                    com.sun.media.jfxmedia.MediaPlayer jfxPlayer = player.retrieveJfxPlayer();
                    if (jfxPlayer != null) {
                        jfxPlayer.dispose();
                    }
                } else {
                    it.remove();
                }
            }
            isShutdown = true;
        }
    }
}
