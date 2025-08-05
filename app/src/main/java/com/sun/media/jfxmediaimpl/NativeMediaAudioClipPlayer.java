package com.sun.media.jfxmediaimpl;

import com.sun.media.jfxmedia.MediaManager;
import com.sun.media.jfxmedia.MediaPlayer;
import com.sun.media.jfxmedia.events.MediaErrorListener;
import com.sun.media.jfxmedia.events.PlayerStateEvent;
import com.sun.media.jfxmedia.events.PlayerStateListener;
import com.sun.media.jfxmedia.locator.Locator;
import com.sun.media.jfxmedia.logging.Logger;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/* loaded from: jfxrt.jar:com/sun/media/jfxmediaimpl/NativeMediaAudioClipPlayer.class */
final class NativeMediaAudioClipPlayer implements PlayerStateListener, MediaErrorListener {
    private MediaPlayer mediaPlayer;
    private int playCount;
    private int loopCount;
    private boolean playing;
    private NativeMediaAudioClip sourceClip;
    private double volume;
    private double balance;
    private double pan;
    private double rate;
    private int priority;
    private static final int MAX_PLAYER_COUNT = 16;
    private static final List<NativeMediaAudioClipPlayer> activePlayers = new ArrayList(16);
    private static final ReentrantLock playerListLock = new ReentrantLock();
    private static final LinkedBlockingQueue<SchedulerEntry> schedule = new LinkedBlockingQueue<>();
    private final ReentrantLock playerStateLock = new ReentrantLock();
    private boolean ready = false;

    public static int getPlayerLimit() {
        return 16;
    }

    public static int getPlayerCount() {
        return activePlayers.size();
    }

    /* loaded from: jfxrt.jar:com/sun/media/jfxmediaimpl/NativeMediaAudioClipPlayer$Enthreaderator.class */
    private static class Enthreaderator {
        private static final Thread schedulerThread = new Thread(() -> {
            NativeMediaAudioClipPlayer.clipScheduler();
        });

        private Enthreaderator() {
        }

        static {
            schedulerThread.setDaemon(true);
            schedulerThread.start();
        }

        public static Thread getSchedulerThread() {
            return schedulerThread;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void clipScheduler() {
        while (true) {
            SchedulerEntry entry = null;
            try {
                entry = schedule.take2();
            } catch (InterruptedException e2) {
            }
            if (null != entry) {
                if (entry.getCommand() == 0) {
                    NativeMediaAudioClipPlayer player = entry.getPlayer();
                    if (null != player) {
                        if (addPlayer(player)) {
                            player.play();
                        } else {
                            player.sourceClip.playFinished();
                        }
                    }
                } else if (entry.getCommand() == 1) {
                    URI sourceURI = entry.getClipURI();
                    playerListLock.lock();
                    try {
                        NativeMediaAudioClipPlayer[] players = (NativeMediaAudioClipPlayer[]) activePlayers.toArray(new NativeMediaAudioClipPlayer[16]);
                        if (null != players) {
                            for (int index = 0; index < players.length; index++) {
                                if (null != players[index] && (null == sourceURI || players[index].source().getURI().equals(sourceURI))) {
                                    players[index].invalidate();
                                }
                            }
                        }
                        playerListLock.unlock();
                        boolean clearSchedule = null == sourceURI;
                        Iterator<SchedulerEntry> it = schedule.iterator();
                        while (it.hasNext()) {
                            SchedulerEntry killEntry = it.next();
                            NativeMediaAudioClipPlayer player2 = killEntry.getPlayer();
                            if (clearSchedule || (null != player2 && player2.sourceClip.getLocator().getURI().equals(sourceURI))) {
                                schedule.remove(killEntry);
                                player2.sourceClip.playFinished();
                            }
                        }
                    } catch (Throwable th) {
                        playerListLock.unlock();
                        throw th;
                    }
                } else if (entry.getCommand() == 2) {
                    entry.getMediaPlayer().dispose();
                }
                entry.signal();
            }
        }
    }

    public static void playClip(NativeMediaAudioClip clip, double volume, double balance, double rate, double pan, int loopCount, int priority) {
        Enthreaderator.getSchedulerThread();
        NativeMediaAudioClipPlayer newPlayer = new NativeMediaAudioClipPlayer(clip, volume, balance, rate, pan, loopCount, priority);
        SchedulerEntry entry = new SchedulerEntry(newPlayer);
        boolean scheduled = schedule.contains(entry);
        if (scheduled || !schedule.offer(entry)) {
            if (Logger.canLog(1) && !scheduled) {
                Logger.logMsg(1, "AudioClip could not be scheduled for playback!");
            }
            clip.playFinished();
        }
    }

    private static boolean addPlayer(NativeMediaAudioClipPlayer newPlayer) {
        playerListLock.lock();
        try {
            int priority = newPlayer.priority();
            while (activePlayers.size() >= 16) {
                NativeMediaAudioClipPlayer target = null;
                for (NativeMediaAudioClipPlayer player : activePlayers) {
                    if (player.priority() <= priority && (target == null || (target.isReady() && player.priority() < target.priority()))) {
                        target = player;
                    }
                }
                if (null != target) {
                    target.invalidate();
                } else {
                    playerListLock.unlock();
                    return false;
                }
            }
            activePlayers.add(newPlayer);
            playerListLock.unlock();
            return true;
        } catch (Throwable th) {
            playerListLock.unlock();
            throw th;
        }
    }

    public static void stopPlayers(Locator source) {
        URI sourceURI = source != null ? source.getURI() : null;
        if (null != Enthreaderator.getSchedulerThread()) {
            CountDownLatch stopSignal = new CountDownLatch(1);
            SchedulerEntry entry = new SchedulerEntry(sourceURI, stopSignal);
            if (schedule.offer(entry)) {
                try {
                    stopSignal.await(5L, TimeUnit.SECONDS);
                } catch (InterruptedException e2) {
                }
            }
        }
    }

    private NativeMediaAudioClipPlayer(NativeMediaAudioClip clip, double volume, double balance, double rate, double pan, int loopCount, int priority) {
        this.sourceClip = clip;
        this.volume = volume;
        this.balance = balance;
        this.pan = pan;
        this.rate = rate;
        this.loopCount = loopCount;
        this.priority = priority;
    }

    private Locator source() {
        return this.sourceClip.getLocator();
    }

    public double volume() {
        return this.volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public double balance() {
        return this.balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double pan() {
        return this.pan;
    }

    public void setPan(double pan) {
        this.pan = pan;
    }

    public double playbackRate() {
        return this.rate;
    }

    public void setPlaybackRate(double rate) {
        this.rate = rate;
    }

    public int priority() {
        return this.priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int loopCount() {
        return this.loopCount;
    }

    public void setLoopCount(int loopCount) {
        this.loopCount = loopCount;
    }

    public boolean isPlaying() {
        return this.playing;
    }

    private boolean isReady() {
        return this.ready;
    }

    public synchronized void play() {
        this.playerStateLock.lock();
        try {
            this.playing = true;
            this.playCount = 0;
            if (null == this.mediaPlayer) {
                this.mediaPlayer = MediaManager.getPlayer(source());
                this.mediaPlayer.addMediaPlayerListener(this);
                this.mediaPlayer.addMediaErrorListener(this);
            } else {
                this.mediaPlayer.play();
            }
        } finally {
            this.playerStateLock.unlock();
        }
    }

    public void stop() {
        invalidate();
    }

    public synchronized void invalidate() {
        this.playerStateLock.lock();
        playerListLock.lock();
        try {
            this.playing = false;
            this.playCount = 0;
            this.ready = false;
            activePlayers.remove(this);
            this.sourceClip.playFinished();
            if (null != this.mediaPlayer) {
                this.mediaPlayer.removeMediaPlayerListener(this);
                this.mediaPlayer.setMute(true);
                SchedulerEntry entry = new SchedulerEntry(this.mediaPlayer);
                if (!schedule.offer(entry)) {
                    this.mediaPlayer.dispose();
                }
                this.mediaPlayer = null;
            }
            playerListLock.unlock();
            this.playerStateLock.unlock();
        } catch (Throwable th) {
            playerListLock.unlock();
            this.playerStateLock.unlock();
        }
    }

    @Override // com.sun.media.jfxmedia.events.PlayerStateListener
    public void onReady(PlayerStateEvent evt) {
        this.playerStateLock.lock();
        try {
            this.ready = true;
            if (this.playing) {
                this.mediaPlayer.setVolume((float) this.volume);
                this.mediaPlayer.setBalance((float) this.balance);
                this.mediaPlayer.setRate((float) this.rate);
                this.mediaPlayer.play();
            }
        } finally {
            this.playerStateLock.unlock();
        }
    }

    @Override // com.sun.media.jfxmedia.events.PlayerStateListener
    public void onPlaying(PlayerStateEvent evt) {
    }

    @Override // com.sun.media.jfxmedia.events.PlayerStateListener
    public void onPause(PlayerStateEvent evt) {
    }

    @Override // com.sun.media.jfxmedia.events.PlayerStateListener
    public void onStop(PlayerStateEvent evt) {
        invalidate();
    }

    @Override // com.sun.media.jfxmedia.events.PlayerStateListener
    public void onStall(PlayerStateEvent evt) {
    }

    @Override // com.sun.media.jfxmedia.events.PlayerStateListener
    public void onFinish(PlayerStateEvent evt) {
        this.playerStateLock.lock();
        try {
            if (this.playing) {
                if (this.loopCount != -1) {
                    this.playCount++;
                    if (this.playCount <= this.loopCount) {
                        this.mediaPlayer.seek(0.0d);
                    } else {
                        invalidate();
                    }
                } else {
                    this.mediaPlayer.seek(0.0d);
                }
            }
        } finally {
            this.playerStateLock.unlock();
        }
    }

    @Override // com.sun.media.jfxmedia.events.PlayerStateListener
    public void onHalt(PlayerStateEvent evt) {
        invalidate();
    }

    public void onWarning(Object source, String message) {
    }

    @Override // com.sun.media.jfxmedia.events.MediaErrorListener
    public void onError(Object source, int errorCode, String message) {
        if (Logger.canLog(4)) {
            Logger.logMsg(4, "Error with AudioClip player: code " + errorCode + " : " + message);
        }
        invalidate();
    }

    public boolean equals(Object that) {
        if (that == this) {
            return true;
        }
        if (that instanceof NativeMediaAudioClipPlayer) {
            NativeMediaAudioClipPlayer otherPlayer = (NativeMediaAudioClipPlayer) that;
            URI myURI = this.sourceClip.getLocator().getURI();
            URI otherURI = otherPlayer.sourceClip.getLocator().getURI();
            return myURI.equals(otherURI) && this.priority == otherPlayer.priority && this.loopCount == otherPlayer.loopCount && Double.compare(this.volume, otherPlayer.volume) == 0 && Double.compare(this.balance, otherPlayer.balance) == 0 && Double.compare(this.rate, otherPlayer.rate) == 0 && Double.compare(this.pan, otherPlayer.pan) == 0;
        }
        return false;
    }

    /* loaded from: jfxrt.jar:com/sun/media/jfxmediaimpl/NativeMediaAudioClipPlayer$SchedulerEntry.class */
    private static class SchedulerEntry {
        private final int command;
        private final NativeMediaAudioClipPlayer player;
        private final URI clipURI;
        private final CountDownLatch commandSignal;
        private final MediaPlayer mediaPlayer;

        public SchedulerEntry(NativeMediaAudioClipPlayer player) {
            this.command = 0;
            this.player = player;
            this.clipURI = null;
            this.commandSignal = null;
            this.mediaPlayer = null;
        }

        public SchedulerEntry(URI sourceURI, CountDownLatch signal) {
            this.command = 1;
            this.player = null;
            this.clipURI = sourceURI;
            this.commandSignal = signal;
            this.mediaPlayer = null;
        }

        public SchedulerEntry(MediaPlayer mediaPlayer) {
            this.command = 2;
            this.player = null;
            this.clipURI = null;
            this.commandSignal = null;
            this.mediaPlayer = mediaPlayer;
        }

        public int getCommand() {
            return this.command;
        }

        public NativeMediaAudioClipPlayer getPlayer() {
            return this.player;
        }

        public URI getClipURI() {
            return this.clipURI;
        }

        public MediaPlayer getMediaPlayer() {
            return this.mediaPlayer;
        }

        public void signal() {
            if (null != this.commandSignal) {
                this.commandSignal.countDown();
            }
        }

        public boolean equals(Object other) {
            if ((other instanceof SchedulerEntry) && null != this.player) {
                return this.player.equals(((SchedulerEntry) other).getPlayer());
            }
            return false;
        }
    }
}
