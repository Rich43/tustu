package com.sun.media.sound;

import com.sun.media.sound.SoftMixingDataLine;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineUnavailableException;

/* loaded from: rt.jar:com/sun/media/sound/SoftMixingClip.class */
public final class SoftMixingClip extends SoftMixingDataLine implements Clip {
    private AudioFormat format;
    private int framesize;
    private byte[] data;
    private final InputStream datastream;
    private int offset;
    private int bufferSize;
    private float[] readbuffer;
    private boolean open;
    private AudioFormat outputformat;
    private int out_nrofchannels;
    private int in_nrofchannels;
    private int frameposition;
    private boolean frameposition_sg;
    private boolean active_sg;
    private int loopstart;
    private int loopend;
    private boolean active;
    private int loopcount;
    private boolean _active;
    private int _frameposition;
    private boolean loop_sg;
    private int _loopcount;
    private int _loopstart;
    private int _loopend;
    private float _rightgain;
    private float _leftgain;
    private float _eff1gain;
    private float _eff2gain;
    private AudioFloatInputStream afis;

    static /* synthetic */ int access$010(SoftMixingClip softMixingClip) {
        int i2 = softMixingClip._loopcount;
        softMixingClip._loopcount = i2 - 1;
        return i2;
    }

    SoftMixingClip(SoftMixingMixer softMixingMixer, DataLine.Info info) {
        super(softMixingMixer, info);
        this.datastream = new InputStream() { // from class: com.sun.media.sound.SoftMixingClip.1
            @Override // java.io.InputStream
            public int read() throws IOException {
                byte[] bArr = new byte[1];
                int i2 = read(bArr);
                if (i2 < 0) {
                    return i2;
                }
                return bArr[0] & 255;
            }

            @Override // java.io.InputStream
            public int read(byte[] bArr, int i2, int i3) throws IOException {
                if (SoftMixingClip.this._loopcount != 0) {
                    int i4 = SoftMixingClip.this._loopend * SoftMixingClip.this.framesize;
                    int i5 = SoftMixingClip.this._loopstart * SoftMixingClip.this.framesize;
                    int i6 = SoftMixingClip.this._frameposition * SoftMixingClip.this.framesize;
                    if (i6 + i3 >= i4 && i6 < i4) {
                        int i7 = i2 + i3;
                        while (i2 != i7) {
                            if (i6 == i4) {
                                if (SoftMixingClip.this._loopcount == 0) {
                                    break;
                                }
                                i6 = i5;
                                if (SoftMixingClip.this._loopcount != -1) {
                                    SoftMixingClip.access$010(SoftMixingClip.this);
                                }
                            }
                            int i8 = i7 - i2;
                            int i9 = i4 - i6;
                            if (i8 > i9) {
                                i8 = i9;
                            }
                            System.arraycopy(SoftMixingClip.this.data, i6, bArr, i2, i8);
                            i2 += i8;
                        }
                        if (SoftMixingClip.this._loopcount == 0) {
                            int i10 = i7 - i2;
                            int i11 = i4 - i6;
                            if (i10 > i11) {
                                i10 = i11;
                            }
                            System.arraycopy(SoftMixingClip.this.data, i6, bArr, i2, i10);
                            i2 += i10;
                        }
                        SoftMixingClip.this._frameposition = i6 / SoftMixingClip.this.framesize;
                        return i2 - i2;
                    }
                }
                int i12 = SoftMixingClip.this._frameposition * SoftMixingClip.this.framesize;
                int i13 = SoftMixingClip.this.bufferSize - i12;
                if (i13 == 0) {
                    return -1;
                }
                if (i3 > i13) {
                    i3 = i13;
                }
                System.arraycopy(SoftMixingClip.this.data, i12, bArr, i2, i3);
                SoftMixingClip.this._frameposition += i3 / SoftMixingClip.this.framesize;
                return i3;
            }
        };
        this.open = false;
        this.frameposition = 0;
        this.frameposition_sg = false;
        this.active_sg = false;
        this.loopstart = 0;
        this.loopend = -1;
        this.active = false;
        this.loopcount = 0;
        this._active = false;
        this._frameposition = 0;
        this.loop_sg = false;
        this._loopcount = 0;
        this._loopstart = 0;
        this._loopend = -1;
    }

    @Override // com.sun.media.sound.SoftMixingDataLine
    protected void processControlLogic() {
        this._rightgain = this.rightgain;
        this._leftgain = this.leftgain;
        this._eff1gain = this.eff1gain;
        this._eff2gain = this.eff2gain;
        if (this.active_sg) {
            this._active = this.active;
            this.active_sg = false;
        } else {
            this.active = this._active;
        }
        if (this.frameposition_sg) {
            this._frameposition = this.frameposition;
            this.frameposition_sg = false;
            this.afis = null;
        } else {
            this.frameposition = this._frameposition;
        }
        if (this.loop_sg) {
            this._loopcount = this.loopcount;
            this._loopstart = this.loopstart;
            this._loopend = this.loopend;
        }
        if (this.afis == null) {
            this.afis = AudioFloatInputStream.getInputStream(new AudioInputStream(this.datastream, this.format, -1L));
            if (Math.abs(this.format.getSampleRate() - this.outputformat.getSampleRate()) > 1.0E-6d) {
                this.afis = new SoftMixingDataLine.AudioFloatInputStreamResampler(this.afis, this.outputformat);
            }
        }
    }

    @Override // com.sun.media.sound.SoftMixingDataLine
    protected void processAudioLogic(SoftAudioBuffer[] softAudioBufferArr) {
        int i2;
        if (this._active) {
            float[] fArrArray = softAudioBufferArr[0].array();
            float[] fArrArray2 = softAudioBufferArr[1].array();
            int size = softAudioBufferArr[0].getSize();
            int i3 = size * this.in_nrofchannels;
            if (this.readbuffer == null || this.readbuffer.length < i3) {
                this.readbuffer = new float[i3];
            }
            try {
                i2 = this.afis.read(this.readbuffer);
            } catch (IOException e2) {
            }
            if (i2 == -1) {
                this._active = false;
                return;
            }
            if (i2 != this.in_nrofchannels) {
                Arrays.fill(this.readbuffer, i2, i3, 0.0f);
            }
            int i4 = this.in_nrofchannels;
            int i5 = 0;
            int i6 = 0;
            while (true) {
                int i7 = i6;
                if (i5 >= size) {
                    break;
                }
                int i8 = i5;
                fArrArray[i8] = fArrArray[i8] + (this.readbuffer[i7] * this._leftgain);
                i5++;
                i6 = i7 + i4;
            }
            if (this.out_nrofchannels != 1) {
                if (this.in_nrofchannels == 1) {
                    int i9 = 0;
                    int i10 = 0;
                    while (true) {
                        int i11 = i10;
                        if (i9 >= size) {
                            break;
                        }
                        int i12 = i9;
                        fArrArray2[i12] = fArrArray2[i12] + (this.readbuffer[i11] * this._rightgain);
                        i9++;
                        i10 = i11 + i4;
                    }
                } else {
                    int i13 = 0;
                    int i14 = 1;
                    while (true) {
                        int i15 = i14;
                        if (i13 >= size) {
                            break;
                        }
                        int i16 = i13;
                        fArrArray2[i16] = fArrArray2[i16] + (this.readbuffer[i15] * this._rightgain);
                        i13++;
                        i14 = i15 + i4;
                    }
                }
            }
            if (this._eff1gain > 2.0E-4d) {
                float[] fArrArray3 = softAudioBufferArr[2].array();
                int i17 = 0;
                int i18 = 0;
                while (true) {
                    int i19 = i18;
                    if (i17 >= size) {
                        break;
                    }
                    int i20 = i17;
                    fArrArray3[i20] = fArrArray3[i20] + (this.readbuffer[i19] * this._eff1gain);
                    i17++;
                    i18 = i19 + i4;
                }
                if (this.in_nrofchannels == 2) {
                    int i21 = 0;
                    int i22 = 1;
                    while (true) {
                        int i23 = i22;
                        if (i21 >= size) {
                            break;
                        }
                        int i24 = i21;
                        fArrArray3[i24] = fArrArray3[i24] + (this.readbuffer[i23] * this._eff1gain);
                        i21++;
                        i22 = i23 + i4;
                    }
                }
            }
            if (this._eff2gain > 2.0E-4d) {
                float[] fArrArray4 = softAudioBufferArr[3].array();
                int i25 = 0;
                int i26 = 0;
                while (true) {
                    int i27 = i26;
                    if (i25 >= size) {
                        break;
                    }
                    int i28 = i25;
                    fArrArray4[i28] = fArrArray4[i28] + (this.readbuffer[i27] * this._eff2gain);
                    i25++;
                    i26 = i27 + i4;
                }
                if (this.in_nrofchannels == 2) {
                    int i29 = 0;
                    int i30 = 1;
                    while (true) {
                        int i31 = i30;
                        if (i29 < size) {
                            int i32 = i29;
                            fArrArray4[i32] = fArrArray4[i32] + (this.readbuffer[i31] * this._eff2gain);
                            i29++;
                            i30 = i31 + i4;
                        } else {
                            return;
                        }
                    }
                }
            }
        }
    }

    @Override // javax.sound.sampled.Clip
    public int getFrameLength() {
        return this.bufferSize / this.format.getFrameSize();
    }

    @Override // javax.sound.sampled.Clip
    public long getMicrosecondLength() {
        return (long) (getFrameLength() * (1000000.0d / getFormat().getSampleRate()));
    }

    @Override // javax.sound.sampled.Clip
    public void loop(int i2) {
        LineEvent lineEvent = null;
        synchronized (this.control_mutex) {
            if (isOpen()) {
                if (this.active) {
                    return;
                }
                this.active = true;
                this.active_sg = true;
                this.loopcount = i2;
                lineEvent = new LineEvent(this, LineEvent.Type.START, getLongFramePosition());
            }
            if (lineEvent != null) {
                sendEvent(lineEvent);
            }
        }
    }

    @Override // javax.sound.sampled.Clip
    public void open(AudioInputStream audioInputStream) throws LineUnavailableException, IOException {
        int i2;
        if (isOpen()) {
            throw new IllegalStateException("Clip is already open with format " + ((Object) getFormat()) + " and frame lengh of " + getFrameLength());
        }
        if (AudioFloatConverter.getConverter(audioInputStream.getFormat()) == null) {
            throw new IllegalArgumentException("Invalid format : " + audioInputStream.getFormat().toString());
        }
        if (audioInputStream.getFrameLength() != -1) {
            byte[] bArr = new byte[((int) audioInputStream.getFrameLength()) * audioInputStream.getFormat().getFrameSize()];
            int frameSize = 512 * audioInputStream.getFormat().getFrameSize();
            int i3 = 0;
            while (true) {
                i2 = i3;
                if (i2 == bArr.length) {
                    break;
                }
                if (frameSize > bArr.length - i2) {
                    frameSize = bArr.length - i2;
                }
                int i4 = audioInputStream.read(bArr, i2, frameSize);
                if (i4 == -1) {
                    break;
                }
                if (i4 == 0) {
                    Thread.yield();
                }
                i3 = i2 + i4;
            }
            open(audioInputStream.getFormat(), bArr, 0, i2);
            return;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bArr2 = new byte[512 * audioInputStream.getFormat().getFrameSize()];
        while (true) {
            int i5 = audioInputStream.read(bArr2);
            if (i5 != -1) {
                if (i5 == 0) {
                    Thread.yield();
                }
                byteArrayOutputStream.write(bArr2, 0, i5);
            } else {
                open(audioInputStream.getFormat(), byteArrayOutputStream.toByteArray(), 0, byteArrayOutputStream.size());
                return;
            }
        }
    }

    @Override // javax.sound.sampled.Clip
    public void open(AudioFormat audioFormat, byte[] bArr, int i2, int i3) throws LineUnavailableException {
        synchronized (this.control_mutex) {
            if (isOpen()) {
                throw new IllegalStateException("Clip is already open with format " + ((Object) getFormat()) + " and frame lengh of " + getFrameLength());
            }
            if (AudioFloatConverter.getConverter(audioFormat) == null) {
                throw new IllegalArgumentException("Invalid format : " + audioFormat.toString());
            }
            if (i3 % audioFormat.getFrameSize() != 0) {
                throw new IllegalArgumentException("Buffer size does not represent an integral number of sample frames!");
            }
            if (bArr != null) {
                this.data = Arrays.copyOf(bArr, bArr.length);
            }
            this.offset = i2;
            this.bufferSize = i3;
            this.format = audioFormat;
            this.framesize = audioFormat.getFrameSize();
            this.loopstart = 0;
            this.loopend = -1;
            this.loop_sg = true;
            if (!this.mixer.isOpen()) {
                this.mixer.open();
                this.mixer.implicitOpen = true;
            }
            this.outputformat = this.mixer.getFormat();
            this.out_nrofchannels = this.outputformat.getChannels();
            this.in_nrofchannels = audioFormat.getChannels();
            this.open = true;
            this.mixer.getMainMixer().openLine(this);
        }
    }

    @Override // javax.sound.sampled.Clip
    public void setFramePosition(int i2) {
        synchronized (this.control_mutex) {
            this.frameposition_sg = true;
            this.frameposition = i2;
        }
    }

    @Override // javax.sound.sampled.Clip
    public void setLoopPoints(int i2, int i3) {
        synchronized (this.control_mutex) {
            if (i3 != -1) {
                if (i3 < i2) {
                    throw new IllegalArgumentException("Invalid loop points : " + i2 + " - " + i3);
                }
                if (i3 * this.framesize > this.bufferSize) {
                    throw new IllegalArgumentException("Invalid loop points : " + i2 + " - " + i3);
                }
            }
            if (i2 * this.framesize > this.bufferSize) {
                throw new IllegalArgumentException("Invalid loop points : " + i2 + " - " + i3);
            }
            if (0 < i2) {
                throw new IllegalArgumentException("Invalid loop points : " + i2 + " - " + i3);
            }
            this.loopstart = i2;
            this.loopend = i3;
            this.loop_sg = true;
        }
    }

    @Override // javax.sound.sampled.Clip
    public void setMicrosecondPosition(long j2) {
        setFramePosition((int) (j2 * (getFormat().getSampleRate() / 1000000.0d)));
    }

    @Override // javax.sound.sampled.DataLine
    public int available() {
        return 0;
    }

    @Override // javax.sound.sampled.DataLine
    public void drain() {
    }

    @Override // javax.sound.sampled.DataLine
    public void flush() {
    }

    @Override // javax.sound.sampled.DataLine
    public int getBufferSize() {
        return this.bufferSize;
    }

    @Override // javax.sound.sampled.DataLine
    public AudioFormat getFormat() {
        return this.format;
    }

    @Override // javax.sound.sampled.DataLine
    public int getFramePosition() {
        int i2;
        synchronized (this.control_mutex) {
            i2 = this.frameposition;
        }
        return i2;
    }

    @Override // javax.sound.sampled.DataLine
    public float getLevel() {
        return -1.0f;
    }

    @Override // javax.sound.sampled.DataLine
    public long getLongFramePosition() {
        return getFramePosition();
    }

    @Override // javax.sound.sampled.DataLine
    public long getMicrosecondPosition() {
        return (long) (getFramePosition() * (1000000.0d / getFormat().getSampleRate()));
    }

    @Override // javax.sound.sampled.DataLine
    public boolean isActive() {
        boolean z2;
        synchronized (this.control_mutex) {
            z2 = this.active;
        }
        return z2;
    }

    @Override // javax.sound.sampled.DataLine
    public boolean isRunning() {
        boolean z2;
        synchronized (this.control_mutex) {
            z2 = this.active;
        }
        return z2;
    }

    @Override // javax.sound.sampled.DataLine
    public void start() {
        LineEvent lineEvent = null;
        synchronized (this.control_mutex) {
            if (isOpen()) {
                if (this.active) {
                    return;
                }
                this.active = true;
                this.active_sg = true;
                this.loopcount = 0;
                lineEvent = new LineEvent(this, LineEvent.Type.START, getLongFramePosition());
            }
            if (lineEvent != null) {
                sendEvent(lineEvent);
            }
        }
    }

    @Override // javax.sound.sampled.DataLine
    public void stop() {
        LineEvent lineEvent = null;
        synchronized (this.control_mutex) {
            if (isOpen()) {
                if (!this.active) {
                    return;
                }
                this.active = false;
                this.active_sg = true;
                lineEvent = new LineEvent(this, LineEvent.Type.STOP, getLongFramePosition());
            }
            if (lineEvent != null) {
                sendEvent(lineEvent);
            }
        }
    }

    @Override // javax.sound.sampled.Line, java.lang.AutoCloseable
    public void close() {
        synchronized (this.control_mutex) {
            if (isOpen()) {
                stop();
                LineEvent lineEvent = new LineEvent(this, LineEvent.Type.CLOSE, getLongFramePosition());
                this.open = false;
                this.mixer.getMainMixer().closeLine(this);
                if (lineEvent != null) {
                    sendEvent(lineEvent);
                }
            }
        }
    }

    @Override // javax.sound.sampled.Line
    public boolean isOpen() {
        return this.open;
    }

    @Override // javax.sound.sampled.Line
    public void open() throws LineUnavailableException {
        if (this.data == null) {
            throw new IllegalArgumentException("Illegal call to open() in interface Clip");
        }
        open(this.format, this.data, this.offset, this.bufferSize);
    }
}
