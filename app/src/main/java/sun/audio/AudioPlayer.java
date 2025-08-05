package sun.audio;

import java.io.InputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import sun.java2d.marlin.MarlinConst;

/* loaded from: rt.jar:sun/audio/AudioPlayer.class */
public final class AudioPlayer extends Thread {
    private final AudioDevice devAudio;
    private static final boolean DEBUG = false;
    public static final AudioPlayer player = getAudioPlayer();

    private static ThreadGroup getAudioThreadGroup() {
        ThreadGroup threadGroup;
        ThreadGroup threadGroup2 = currentThread().getThreadGroup();
        while (true) {
            threadGroup = threadGroup2;
            if (threadGroup.getParent() == null || threadGroup.getParent().getParent() == null) {
                break;
            }
            threadGroup2 = threadGroup.getParent();
        }
        return threadGroup;
    }

    private static AudioPlayer getAudioPlayer() {
        return (AudioPlayer) AccessController.doPrivileged(new PrivilegedAction() { // from class: sun.audio.AudioPlayer.1
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object run2() {
                AudioPlayer audioPlayer = new AudioPlayer();
                audioPlayer.setPriority(10);
                audioPlayer.setDaemon(true);
                audioPlayer.start();
                return audioPlayer;
            }
        });
    }

    private AudioPlayer() {
        super(getAudioThreadGroup(), "Audio Player");
        this.devAudio = AudioDevice.device;
        this.devAudio.open();
    }

    public synchronized void start(InputStream inputStream) {
        this.devAudio.openChannel(inputStream);
        notify();
    }

    public synchronized void stop(InputStream inputStream) {
        this.devAudio.closeChannel(inputStream);
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        this.devAudio.play();
        while (true) {
            try {
                Thread.sleep(MarlinConst.statDump);
            } catch (Exception e2) {
                return;
            }
        }
    }
}
