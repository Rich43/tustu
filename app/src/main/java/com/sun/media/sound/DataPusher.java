package com.sun.media.sound;

import java.io.IOException;
import java.util.Arrays;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.SourceDataLine;
import sun.java2d.marlin.MarlinConst;

/* loaded from: rt.jar:com/sun/media/sound/DataPusher.class */
public final class DataPusher implements Runnable {
    private static final int AUTO_CLOSE_TIME = 5000;
    private static final boolean DEBUG = false;
    private final SourceDataLine source;
    private final AudioFormat format;
    private final AudioInputStream ais;
    private final byte[] audioData;
    private final int audioDataByteLength;
    private int pos;
    private int newPos;
    private boolean looping;
    private Thread pushThread;
    private int wantedState;
    private int threadState;
    private final int STATE_NONE = 0;
    private final int STATE_PLAYING = 1;
    private final int STATE_WAITING = 2;
    private final int STATE_STOPPING = 3;
    private final int STATE_STOPPED = 4;
    private final int BUFFER_SIZE = 16384;

    public DataPusher(SourceDataLine sourceDataLine, AudioFormat audioFormat, byte[] bArr, int i2) {
        this(sourceDataLine, audioFormat, null, bArr, i2);
    }

    public DataPusher(SourceDataLine sourceDataLine, AudioInputStream audioInputStream) {
        this(sourceDataLine, audioInputStream.getFormat(), audioInputStream, null, 0);
    }

    private DataPusher(SourceDataLine sourceDataLine, AudioFormat audioFormat, AudioInputStream audioInputStream, byte[] bArr, int i2) {
        this.newPos = -1;
        this.pushThread = null;
        this.STATE_NONE = 0;
        this.STATE_PLAYING = 1;
        this.STATE_WAITING = 2;
        this.STATE_STOPPING = 3;
        this.STATE_STOPPED = 4;
        this.BUFFER_SIZE = 16384;
        this.source = sourceDataLine;
        this.format = audioFormat;
        this.ais = audioInputStream;
        this.audioDataByteLength = i2;
        this.audioData = bArr == null ? null : Arrays.copyOf(bArr, bArr.length);
    }

    public synchronized void start() {
        start(false);
    }

    public synchronized void start(boolean z2) {
        try {
            if (this.threadState == 3) {
                stop();
            }
            this.looping = z2;
            this.newPos = 0;
            this.wantedState = 1;
            if (!this.source.isOpen()) {
                this.source.open(this.format);
            }
            this.source.flush();
            this.source.start();
            if (this.pushThread == null) {
                this.pushThread = JSSecurityManager.createThread(this, null, false, -1, true);
            }
            notifyAll();
        } catch (Exception e2) {
        }
    }

    public synchronized void stop() {
        if (this.threadState == 3 || this.threadState == 4 || this.pushThread == null) {
            return;
        }
        this.wantedState = 2;
        if (this.source != null) {
            this.source.flush();
        }
        notifyAll();
        int i2 = 50;
        while (true) {
            int i3 = i2;
            i2--;
            if (i3 >= 0 && this.threadState == 1) {
                try {
                    wait(100L);
                } catch (InterruptedException e2) {
                }
            } else {
                return;
            }
        }
    }

    synchronized void close() {
        if (this.source != null) {
            this.source.close();
        }
    }

    @Override // java.lang.Runnable
    public void run() {
        byte[] bArr;
        boolean z2 = this.ais != null;
        if (z2) {
            bArr = new byte[16384];
        } else {
            bArr = this.audioData;
        }
        while (this.wantedState != 3) {
            if (this.wantedState == 2) {
                try {
                    synchronized (this) {
                        this.threadState = 2;
                        this.wantedState = 3;
                        wait(MarlinConst.statDump);
                    }
                } catch (InterruptedException e2) {
                }
            } else {
                if (this.newPos >= 0) {
                    this.pos = this.newPos;
                    this.newPos = -1;
                }
                this.threadState = 1;
                int i2 = 16384;
                if (z2) {
                    try {
                        this.pos = 0;
                        i2 = this.ais.read(bArr, 0, bArr.length);
                    } catch (IOException e3) {
                        i2 = -1;
                    }
                } else {
                    if (16384 > this.audioDataByteLength - this.pos) {
                        i2 = this.audioDataByteLength - this.pos;
                    }
                    if (i2 == 0) {
                        i2 = -1;
                    }
                }
                if (i2 < 0) {
                    if (!z2 && this.looping) {
                        this.pos = 0;
                    } else {
                        this.wantedState = 2;
                        this.source.drain();
                    }
                } else {
                    this.pos += this.source.write(bArr, this.pos, i2);
                }
            }
        }
        this.threadState = 3;
        this.source.flush();
        this.source.stop();
        this.source.flush();
        this.source.close();
        this.threadState = 4;
        synchronized (this) {
            this.pushThread = null;
            notifyAll();
        }
    }
}
