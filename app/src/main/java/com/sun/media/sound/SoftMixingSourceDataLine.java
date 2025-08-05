package com.sun.media.sound;

import com.sun.media.sound.SoftMixingDataLine;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

/*  JADX ERROR: NullPointerException in pass: ClassModifier
    java.lang.NullPointerException
    */
/* loaded from: rt.jar:com/sun/media/sound/SoftMixingSourceDataLine.class */
public final class SoftMixingSourceDataLine extends SoftMixingDataLine implements SourceDataLine {
    private boolean open;
    private AudioFormat format;
    private int framesize;
    private int bufferSize;
    private float[] readbuffer;
    private boolean active;
    private byte[] cycling_buffer;
    private int cycling_read_pos;
    private int cycling_write_pos;
    private int cycling_avail;
    private long cycling_framepos;
    private AudioFloatInputStream afis;
    private boolean _active;
    private AudioFormat outputformat;
    private int out_nrofchannels;
    private int in_nrofchannels;
    private float _rightgain;
    private float _leftgain;
    private float _eff1gain;
    private float _eff2gain;

    /*  JADX ERROR: Failed to decode insn: 0x0002: MOVE_MULTI
        java.lang.ArrayIndexOutOfBoundsException: arraycopy: source index -1 out of bounds for object array[6]
        	at java.base/java.lang.System.arraycopy(Native Method)
        	at jadx.plugins.input.java.data.code.StackState.insert(StackState.java:52)
        	at jadx.plugins.input.java.data.code.CodeDecodeState.insert(CodeDecodeState.java:137)
        	at jadx.plugins.input.java.data.code.JavaInsnsRegister.dup2x1(JavaInsnsRegister.java:313)
        	at jadx.plugins.input.java.data.code.JavaInsnData.decode(JavaInsnData.java:46)
        	at jadx.core.dex.instructions.InsnDecoder.lambda$process$0(InsnDecoder.java:50)
        	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:85)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:46)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:158)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:458)
        	at jadx.core.ProcessClass.process(ProcessClass.java:69)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:109)
        	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
        	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
        */
    static /* synthetic */ long access$302(com.sun.media.sound.SoftMixingSourceDataLine r6, long r7) {
        /*
            r0 = r6
            r1 = r7
            // decode failed: arraycopy: source index -1 out of bounds for object array[6]
            r0.cycling_framepos = r1
            return r-1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.media.sound.SoftMixingSourceDataLine.access$302(com.sun.media.sound.SoftMixingSourceDataLine, long):long");
    }

    /* loaded from: rt.jar:com/sun/media/sound/SoftMixingSourceDataLine$NonBlockingFloatInputStream.class */
    private static class NonBlockingFloatInputStream extends AudioFloatInputStream {
        AudioFloatInputStream ais;

        NonBlockingFloatInputStream(AudioFloatInputStream audioFloatInputStream) {
            this.ais = audioFloatInputStream;
        }

        @Override // com.sun.media.sound.AudioFloatInputStream
        public int available() throws IOException {
            return this.ais.available();
        }

        @Override // com.sun.media.sound.AudioFloatInputStream
        public void close() throws IOException {
            this.ais.close();
        }

        @Override // com.sun.media.sound.AudioFloatInputStream
        public AudioFormat getFormat() {
            return this.ais.getFormat();
        }

        @Override // com.sun.media.sound.AudioFloatInputStream
        public long getFrameLength() {
            return this.ais.getFrameLength();
        }

        @Override // com.sun.media.sound.AudioFloatInputStream
        public void mark(int i2) {
            this.ais.mark(i2);
        }

        @Override // com.sun.media.sound.AudioFloatInputStream
        public boolean markSupported() {
            return this.ais.markSupported();
        }

        @Override // com.sun.media.sound.AudioFloatInputStream
        public int read(float[] fArr, int i2, int i3) throws IOException {
            int iAvailable = available();
            if (i3 > iAvailable) {
                Arrays.fill(fArr, i2 + this.ais.read(fArr, i2, iAvailable), i2 + i3, 0.0f);
                return i3;
            }
            return this.ais.read(fArr, i2, i3);
        }

        @Override // com.sun.media.sound.AudioFloatInputStream
        public void reset() throws IOException {
            this.ais.reset();
        }

        @Override // com.sun.media.sound.AudioFloatInputStream
        public long skip(long j2) throws IOException {
            return this.ais.skip(j2);
        }
    }

    SoftMixingSourceDataLine(SoftMixingMixer softMixingMixer, DataLine.Info info) {
        super(softMixingMixer, info);
        this.open = false;
        this.format = new AudioFormat(44100.0f, 16, 2, true, false);
        this.bufferSize = -1;
        this.active = false;
        this.cycling_read_pos = 0;
        this.cycling_write_pos = 0;
        this.cycling_avail = 0;
        this.cycling_framepos = 0L;
        this._active = false;
    }

    @Override // javax.sound.sampled.SourceDataLine
    public int write(byte[] bArr, int i2, int i3) {
        if (!isOpen()) {
            return 0;
        }
        if (i3 % this.framesize != 0) {
            throw new IllegalArgumentException("Number of bytes does not represent an integral number of sample frames.");
        }
        if (i2 < 0) {
            throw new ArrayIndexOutOfBoundsException(i2);
        }
        if (i2 + i3 > bArr.length) {
            throw new ArrayIndexOutOfBoundsException(bArr.length);
        }
        byte[] bArr2 = this.cycling_buffer;
        int length = this.cycling_buffer.length;
        int i4 = 0;
        while (i4 != i3) {
            synchronized (this.cycling_buffer) {
                int i5 = this.cycling_write_pos;
                int i6 = this.cycling_avail;
                while (i4 != i3 && i6 != length) {
                    int i7 = i5;
                    i5++;
                    int i8 = i2;
                    i2++;
                    bArr2[i7] = bArr[i8];
                    i4++;
                    i6++;
                    if (i5 == length) {
                        i5 = 0;
                    }
                }
                this.cycling_avail = i6;
                this.cycling_write_pos = i5;
                if (i4 == i3) {
                    return i4;
                }
                if (i6 == length) {
                    try {
                        Thread.sleep(1L);
                        if (!isRunning()) {
                            return i4;
                        }
                    } catch (InterruptedException e2) {
                        return i4;
                    }
                }
            }
        }
        return i4;
    }

    @Override // com.sun.media.sound.SoftMixingDataLine
    protected void processControlLogic() {
        this._active = this.active;
        this._rightgain = this.rightgain;
        this._leftgain = this.leftgain;
        this._eff1gain = this.eff1gain;
        this._eff2gain = this.eff2gain;
    }

    @Override // com.sun.media.sound.SoftMixingDataLine
    protected void processAudioLogic(SoftAudioBuffer[] softAudioBufferArr) {
        if (this._active) {
            float[] fArrArray = softAudioBufferArr[0].array();
            float[] fArrArray2 = softAudioBufferArr[1].array();
            int size = softAudioBufferArr[0].getSize();
            int i2 = size * this.in_nrofchannels;
            if (this.readbuffer == null || this.readbuffer.length < i2) {
                this.readbuffer = new float[i2];
            }
            try {
                int i3 = this.afis.read(this.readbuffer);
                if (i3 != this.in_nrofchannels) {
                    Arrays.fill(this.readbuffer, i3, i2, 0.0f);
                }
            } catch (IOException e2) {
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
            if (this._eff1gain > 1.0E-4d) {
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
            if (this._eff2gain > 1.0E-4d) {
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

    @Override // javax.sound.sampled.Line
    public void open() throws LineUnavailableException {
        open(this.format);
    }

    @Override // javax.sound.sampled.SourceDataLine
    public void open(AudioFormat audioFormat) throws LineUnavailableException {
        if (this.bufferSize == -1) {
            this.bufferSize = ((int) (audioFormat.getFrameRate() / 2.0f)) * audioFormat.getFrameSize();
        }
        open(audioFormat, this.bufferSize);
    }

    @Override // javax.sound.sampled.SourceDataLine
    public void open(AudioFormat audioFormat, int i2) throws LineUnavailableException {
        LineEvent lineEvent = null;
        if (i2 < audioFormat.getFrameSize() * 32) {
            i2 = audioFormat.getFrameSize() * 32;
        }
        synchronized (this.control_mutex) {
            if (!isOpen()) {
                if (!this.mixer.isOpen()) {
                    this.mixer.open();
                    this.mixer.implicitOpen = true;
                }
                lineEvent = new LineEvent(this, LineEvent.Type.OPEN, 0L);
                this.bufferSize = i2 - (i2 % audioFormat.getFrameSize());
                this.format = audioFormat;
                this.framesize = audioFormat.getFrameSize();
                this.outputformat = this.mixer.getFormat();
                this.out_nrofchannels = this.outputformat.getChannels();
                this.in_nrofchannels = audioFormat.getChannels();
                this.open = true;
                this.mixer.getMainMixer().openLine(this);
                this.cycling_buffer = new byte[this.framesize * i2];
                this.cycling_read_pos = 0;
                this.cycling_write_pos = 0;
                this.cycling_avail = 0;
                this.cycling_framepos = 0L;
                this.afis = AudioFloatInputStream.getInputStream(new AudioInputStream(new InputStream() { // from class: com.sun.media.sound.SoftMixingSourceDataLine.1
                    @Override // java.io.InputStream
                    public int read() throws IOException {
                        byte[] bArr = new byte[1];
                        int i3 = read(bArr);
                        if (i3 < 0) {
                            return i3;
                        }
                        return bArr[0] & 255;
                    }

                    @Override // java.io.InputStream
                    public int available() throws IOException {
                        int i3;
                        synchronized (SoftMixingSourceDataLine.this.cycling_buffer) {
                            i3 = SoftMixingSourceDataLine.this.cycling_avail;
                        }
                        return i3;
                    }

                    /* JADX WARN: Failed to check method for inline after forced processcom.sun.media.sound.SoftMixingSourceDataLine.access$302(com.sun.media.sound.SoftMixingSourceDataLine, long):long */
                    @Override // java.io.InputStream
                    public int read(byte[] bArr, int i3, int i4) throws IOException {
                        synchronized (SoftMixingSourceDataLine.this.cycling_buffer) {
                            if (i4 > SoftMixingSourceDataLine.this.cycling_avail) {
                                i4 = SoftMixingSourceDataLine.this.cycling_avail;
                            }
                            int i5 = SoftMixingSourceDataLine.this.cycling_read_pos;
                            byte[] bArr2 = SoftMixingSourceDataLine.this.cycling_buffer;
                            int length = bArr2.length;
                            for (int i6 = 0; i6 < i4; i6++) {
                                int i7 = i3;
                                i3++;
                                bArr[i7] = bArr2[i5];
                                i5++;
                                if (i5 == length) {
                                    i5 = 0;
                                }
                            }
                            SoftMixingSourceDataLine.this.cycling_read_pos = i5;
                            SoftMixingSourceDataLine.this.cycling_avail -= i4;
                            SoftMixingSourceDataLine.access$302(SoftMixingSourceDataLine.this, SoftMixingSourceDataLine.this.cycling_framepos + (i4 / SoftMixingSourceDataLine.this.framesize));
                        }
                        return i4;
                    }
                }, audioFormat, -1L));
                this.afis = new NonBlockingFloatInputStream(this.afis);
                if (Math.abs(audioFormat.getSampleRate() - this.outputformat.getSampleRate()) > 1.0E-6d) {
                    this.afis = new SoftMixingDataLine.AudioFloatInputStreamResampler(this.afis, this.outputformat);
                }
            } else if (!audioFormat.matches(getFormat())) {
                throw new IllegalStateException("Line is already open with format " + ((Object) getFormat()) + " and bufferSize " + getBufferSize());
            }
        }
        if (lineEvent != null) {
            sendEvent(lineEvent);
        }
    }

    @Override // javax.sound.sampled.DataLine
    public int available() {
        int length;
        synchronized (this.cycling_buffer) {
            length = this.cycling_buffer.length - this.cycling_avail;
        }
        return length;
    }

    @Override // javax.sound.sampled.DataLine
    public void drain() {
        int i2;
        while (true) {
            synchronized (this.cycling_buffer) {
                i2 = this.cycling_avail;
            }
            if (i2 != 0) {
                return;
            }
            try {
                Thread.sleep(1L);
            } catch (InterruptedException e2) {
                return;
            }
        }
    }

    @Override // javax.sound.sampled.DataLine
    public void flush() {
        synchronized (this.cycling_buffer) {
            this.cycling_read_pos = 0;
            this.cycling_write_pos = 0;
            this.cycling_avail = 0;
        }
    }

    @Override // javax.sound.sampled.DataLine
    public int getBufferSize() {
        int i2;
        synchronized (this.control_mutex) {
            i2 = this.bufferSize;
        }
        return i2;
    }

    @Override // javax.sound.sampled.DataLine
    public AudioFormat getFormat() {
        AudioFormat audioFormat;
        synchronized (this.control_mutex) {
            audioFormat = this.format;
        }
        return audioFormat;
    }

    @Override // javax.sound.sampled.DataLine
    public int getFramePosition() {
        return (int) getLongFramePosition();
    }

    @Override // javax.sound.sampled.DataLine
    public float getLevel() {
        return -1.0f;
    }

    @Override // javax.sound.sampled.DataLine
    public long getLongFramePosition() {
        long j2;
        synchronized (this.cycling_buffer) {
            j2 = this.cycling_framepos;
        }
        return j2;
    }

    @Override // javax.sound.sampled.DataLine
    public long getMicrosecondPosition() {
        return (long) (getLongFramePosition() * (1000000.0d / getFormat().getSampleRate()));
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
        boolean z2;
        synchronized (this.control_mutex) {
            z2 = this.open;
        }
        return z2;
    }
}
