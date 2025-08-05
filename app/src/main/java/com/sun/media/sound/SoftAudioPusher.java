package com.sun.media.sound;

import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.SourceDataLine;

/* loaded from: rt.jar:com/sun/media/sound/SoftAudioPusher.class */
public final class SoftAudioPusher implements Runnable {
    private volatile boolean active = false;
    private SourceDataLine sourceDataLine;
    private Thread audiothread;
    private final AudioInputStream ais;
    private final byte[] buffer;

    public SoftAudioPusher(SourceDataLine sourceDataLine, AudioInputStream audioInputStream, int i2) {
        this.sourceDataLine = null;
        this.ais = audioInputStream;
        this.buffer = new byte[i2];
        this.sourceDataLine = sourceDataLine;
    }

    public synchronized void start() {
        if (this.active) {
            return;
        }
        this.active = true;
        this.audiothread = new Thread(this);
        this.audiothread.setDaemon(true);
        this.audiothread.setPriority(10);
        this.audiothread.start();
    }

    public synchronized void stop() {
        if (!this.active) {
            return;
        }
        this.active = false;
        try {
            this.audiothread.join();
        } catch (InterruptedException e2) {
        }
    }

    @Override // java.lang.Runnable
    public void run() {
        int i2;
        byte[] bArr = this.buffer;
        AudioInputStream audioInputStream = this.ais;
        SourceDataLine sourceDataLine = this.sourceDataLine;
        while (this.active && (i2 = audioInputStream.read(bArr)) >= 0) {
            try {
                sourceDataLine.write(bArr, 0, i2);
            } catch (IOException e2) {
                this.active = false;
                return;
            }
        }
    }
}
