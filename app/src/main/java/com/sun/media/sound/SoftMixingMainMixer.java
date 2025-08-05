package com.sun.media.sound;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.sound.sampled.AudioInputStream;

/* loaded from: rt.jar:com/sun/media/sound/SoftMixingMainMixer.class */
public final class SoftMixingMainMixer {
    public static final int CHANNEL_LEFT = 0;
    public static final int CHANNEL_RIGHT = 1;
    public static final int CHANNEL_EFFECT1 = 2;
    public static final int CHANNEL_EFFECT2 = 3;
    public static final int CHANNEL_EFFECT3 = 4;
    public static final int CHANNEL_EFFECT4 = 5;
    public static final int CHANNEL_LEFT_DRY = 10;
    public static final int CHANNEL_RIGHT_DRY = 11;
    public static final int CHANNEL_SCRATCH1 = 12;
    public static final int CHANNEL_SCRATCH2 = 13;
    public static final int CHANNEL_CHANNELMIXER_LEFT = 14;
    public static final int CHANNEL_CHANNELMIXER_RIGHT = 15;
    private final SoftMixingMixer mixer;
    private final AudioInputStream ais;
    private final SoftAudioBuffer[] buffers;
    private final SoftAudioProcessor reverb;
    private final SoftAudioProcessor chorus;
    private final SoftAudioProcessor agc;
    private final int nrofchannels;
    private final Object control_mutex;
    private final List<SoftMixingDataLine> openLinesList = new ArrayList();
    private SoftMixingDataLine[] openLines = new SoftMixingDataLine[0];

    public AudioInputStream getInputStream() {
        return this.ais;
    }

    void processAudioBuffers() {
        SoftMixingDataLine[] softMixingDataLineArr;
        for (int i2 = 0; i2 < this.buffers.length; i2++) {
            this.buffers[i2].clear();
        }
        synchronized (this.control_mutex) {
            softMixingDataLineArr = this.openLines;
            for (SoftMixingDataLine softMixingDataLine : softMixingDataLineArr) {
                softMixingDataLine.processControlLogic();
            }
            this.chorus.processControlLogic();
            this.reverb.processControlLogic();
            this.agc.processControlLogic();
        }
        for (SoftMixingDataLine softMixingDataLine2 : softMixingDataLineArr) {
            softMixingDataLine2.processAudioLogic(this.buffers);
        }
        this.chorus.processAudio();
        this.reverb.processAudio();
        this.agc.processAudio();
    }

    public SoftMixingMainMixer(SoftMixingMixer softMixingMixer) {
        this.mixer = softMixingMixer;
        this.nrofchannels = softMixingMixer.getFormat().getChannels();
        int sampleRate = (int) (softMixingMixer.getFormat().getSampleRate() / softMixingMixer.getControlRate());
        this.control_mutex = softMixingMixer.control_mutex;
        this.buffers = new SoftAudioBuffer[16];
        for (int i2 = 0; i2 < this.buffers.length; i2++) {
            this.buffers[i2] = new SoftAudioBuffer(sampleRate, softMixingMixer.getFormat());
        }
        this.reverb = new SoftReverb();
        this.chorus = new SoftChorus();
        this.agc = new SoftLimiter();
        float sampleRate2 = softMixingMixer.getFormat().getSampleRate();
        float controlRate = softMixingMixer.getControlRate();
        this.reverb.init(sampleRate2, controlRate);
        this.chorus.init(sampleRate2, controlRate);
        this.agc.init(sampleRate2, controlRate);
        this.reverb.setMixMode(true);
        this.chorus.setMixMode(true);
        this.agc.setMixMode(false);
        this.chorus.setInput(0, this.buffers[3]);
        this.chorus.setOutput(0, this.buffers[0]);
        if (this.nrofchannels != 1) {
            this.chorus.setOutput(1, this.buffers[1]);
        }
        this.chorus.setOutput(2, this.buffers[2]);
        this.reverb.setInput(0, this.buffers[2]);
        this.reverb.setOutput(0, this.buffers[0]);
        if (this.nrofchannels != 1) {
            this.reverb.setOutput(1, this.buffers[1]);
        }
        this.agc.setInput(0, this.buffers[0]);
        if (this.nrofchannels != 1) {
            this.agc.setInput(1, this.buffers[1]);
        }
        this.agc.setOutput(0, this.buffers[0]);
        if (this.nrofchannels != 1) {
            this.agc.setOutput(1, this.buffers[1]);
        }
        this.ais = new AudioInputStream(new InputStream() { // from class: com.sun.media.sound.SoftMixingMainMixer.1
            private final SoftAudioBuffer[] buffers;
            private final int nrofchannels;
            private final int buffersize;
            private final byte[] bbuffer;
            private int bbuffer_pos = 0;
            private final byte[] single = new byte[1];

            {
                this.buffers = SoftMixingMainMixer.this.buffers;
                this.nrofchannels = SoftMixingMainMixer.this.mixer.getFormat().getChannels();
                this.buffersize = this.buffers[0].getSize();
                this.bbuffer = new byte[this.buffersize * (SoftMixingMainMixer.this.mixer.getFormat().getSampleSizeInBits() / 8) * this.nrofchannels];
            }

            public void fillBuffer() {
                SoftMixingMainMixer.this.processAudioBuffers();
                for (int i3 = 0; i3 < this.nrofchannels; i3++) {
                    this.buffers[i3].get(this.bbuffer, i3);
                }
                this.bbuffer_pos = 0;
            }

            @Override // java.io.InputStream
            public int read(byte[] bArr, int i3, int i4) {
                int length = this.bbuffer.length;
                int i5 = i3 + i4;
                byte[] bArr2 = this.bbuffer;
                while (i3 < i5) {
                    if (available() == 0) {
                        fillBuffer();
                    } else {
                        int i6 = this.bbuffer_pos;
                        while (i3 < i5 && i6 < length) {
                            int i7 = i3;
                            i3++;
                            int i8 = i6;
                            i6++;
                            bArr[i7] = bArr2[i8];
                        }
                        this.bbuffer_pos = i6;
                    }
                }
                return i4;
            }

            @Override // java.io.InputStream
            public int read() throws IOException {
                if (read(this.single) == -1) {
                    return -1;
                }
                return this.single[0] & 255;
            }

            @Override // java.io.InputStream
            public int available() {
                return this.bbuffer.length - this.bbuffer_pos;
            }

            @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
            public void close() {
                SoftMixingMainMixer.this.mixer.close();
            }
        }, softMixingMixer.getFormat(), -1L);
    }

    public void openLine(SoftMixingDataLine softMixingDataLine) {
        synchronized (this.control_mutex) {
            this.openLinesList.add(softMixingDataLine);
            this.openLines = (SoftMixingDataLine[]) this.openLinesList.toArray(new SoftMixingDataLine[this.openLinesList.size()]);
        }
    }

    public void closeLine(SoftMixingDataLine softMixingDataLine) {
        synchronized (this.control_mutex) {
            this.openLinesList.remove(softMixingDataLine);
            this.openLines = (SoftMixingDataLine[]) this.openLinesList.toArray(new SoftMixingDataLine[this.openLinesList.size()]);
            if (this.openLines.length == 0 && this.mixer.implicitOpen) {
                this.mixer.close();
            }
        }
    }

    public SoftMixingDataLine[] getOpenLines() {
        SoftMixingDataLine[] softMixingDataLineArr;
        synchronized (this.control_mutex) {
            softMixingDataLineArr = this.openLines;
        }
        return softMixingDataLineArr;
    }

    public void close() {
        for (SoftMixingDataLine softMixingDataLine : this.openLines) {
            softMixingDataLine.close();
        }
    }
}
