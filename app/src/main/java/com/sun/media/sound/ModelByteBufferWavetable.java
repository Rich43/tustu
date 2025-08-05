package com.sun.media.sound;

import java.io.IOException;
import java.io.InputStream;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

/* loaded from: rt.jar:com/sun/media/sound/ModelByteBufferWavetable.class */
public final class ModelByteBufferWavetable implements ModelWavetable {
    private float loopStart;
    private float loopLength;
    private final ModelByteBuffer buffer;
    private ModelByteBuffer buffer8;
    private AudioFormat format;
    private float pitchcorrection;
    private float attenuation;
    private int loopType;

    /* loaded from: rt.jar:com/sun/media/sound/ModelByteBufferWavetable$Buffer8PlusInputStream.class */
    private class Buffer8PlusInputStream extends InputStream {
        private final boolean bigendian;
        private final int framesize_pc;
        int pos = 0;
        int pos2 = 0;
        int markpos = 0;
        int markpos2 = 0;

        Buffer8PlusInputStream() {
            this.framesize_pc = ModelByteBufferWavetable.this.format.getFrameSize() / ModelByteBufferWavetable.this.format.getChannels();
            this.bigendian = ModelByteBufferWavetable.this.format.isBigEndian();
        }

        @Override // java.io.InputStream
        public int read(byte[] bArr, int i2, int i3) throws IOException {
            int iAvailable = available();
            if (iAvailable <= 0) {
                return -1;
            }
            if (i3 > iAvailable) {
                i3 = iAvailable;
            }
            byte[] bArrArray = ModelByteBufferWavetable.this.buffer.array();
            byte[] bArrArray2 = ModelByteBufferWavetable.this.buffer8.array();
            this.pos = (int) (this.pos + ModelByteBufferWavetable.this.buffer.arrayOffset());
            this.pos2 = (int) (this.pos2 + ModelByteBufferWavetable.this.buffer8.arrayOffset());
            if (this.bigendian) {
                int i4 = 0;
                while (true) {
                    int i5 = i4;
                    if (i5 >= i3) {
                        break;
                    }
                    System.arraycopy(bArrArray, this.pos, bArr, i5, this.framesize_pc);
                    System.arraycopy(bArrArray2, this.pos2, bArr, i5 + this.framesize_pc, 1);
                    this.pos += this.framesize_pc;
                    this.pos2++;
                    i4 = i5 + this.framesize_pc + 1;
                }
            } else {
                int i6 = 0;
                while (true) {
                    int i7 = i6;
                    if (i7 >= i3) {
                        break;
                    }
                    System.arraycopy(bArrArray2, this.pos2, bArr, i7, 1);
                    System.arraycopy(bArrArray, this.pos, bArr, i7 + 1, this.framesize_pc);
                    this.pos += this.framesize_pc;
                    this.pos2++;
                    i6 = i7 + this.framesize_pc + 1;
                }
            }
            this.pos = (int) (this.pos - ModelByteBufferWavetable.this.buffer.arrayOffset());
            this.pos2 = (int) (this.pos2 - ModelByteBufferWavetable.this.buffer8.arrayOffset());
            return i3;
        }

        @Override // java.io.InputStream
        public long skip(long j2) throws IOException {
            int iAvailable = available();
            if (iAvailable <= 0) {
                return -1L;
            }
            if (j2 > iAvailable) {
                j2 = iAvailable;
            }
            this.pos = (int) (this.pos + ((j2 / (this.framesize_pc + 1)) * this.framesize_pc));
            this.pos2 = (int) (this.pos2 + (j2 / (this.framesize_pc + 1)));
            return super.skip(j2);
        }

        @Override // java.io.InputStream
        public int read(byte[] bArr) throws IOException {
            return read(bArr, 0, bArr.length);
        }

        @Override // java.io.InputStream
        public int read() throws IOException {
            if (read(new byte[1], 0, 1) == -1) {
                return -1;
            }
            return 0;
        }

        @Override // java.io.InputStream
        public boolean markSupported() {
            return true;
        }

        @Override // java.io.InputStream
        public int available() throws IOException {
            return ((((int) ModelByteBufferWavetable.this.buffer.capacity()) + ((int) ModelByteBufferWavetable.this.buffer8.capacity())) - this.pos) - this.pos2;
        }

        @Override // java.io.InputStream
        public synchronized void mark(int i2) {
            this.markpos = this.pos;
            this.markpos2 = this.pos2;
        }

        @Override // java.io.InputStream
        public synchronized void reset() throws IOException {
            this.pos = this.markpos;
            this.pos2 = this.markpos2;
        }
    }

    public ModelByteBufferWavetable(ModelByteBuffer modelByteBuffer) {
        this.loopStart = -1.0f;
        this.loopLength = -1.0f;
        this.buffer8 = null;
        this.format = null;
        this.pitchcorrection = 0.0f;
        this.attenuation = 0.0f;
        this.loopType = 0;
        this.buffer = modelByteBuffer;
    }

    public ModelByteBufferWavetable(ModelByteBuffer modelByteBuffer, float f2) {
        this.loopStart = -1.0f;
        this.loopLength = -1.0f;
        this.buffer8 = null;
        this.format = null;
        this.pitchcorrection = 0.0f;
        this.attenuation = 0.0f;
        this.loopType = 0;
        this.buffer = modelByteBuffer;
        this.pitchcorrection = f2;
    }

    public ModelByteBufferWavetable(ModelByteBuffer modelByteBuffer, AudioFormat audioFormat) {
        this.loopStart = -1.0f;
        this.loopLength = -1.0f;
        this.buffer8 = null;
        this.format = null;
        this.pitchcorrection = 0.0f;
        this.attenuation = 0.0f;
        this.loopType = 0;
        this.format = audioFormat;
        this.buffer = modelByteBuffer;
    }

    public ModelByteBufferWavetable(ModelByteBuffer modelByteBuffer, AudioFormat audioFormat, float f2) {
        this.loopStart = -1.0f;
        this.loopLength = -1.0f;
        this.buffer8 = null;
        this.format = null;
        this.pitchcorrection = 0.0f;
        this.attenuation = 0.0f;
        this.loopType = 0;
        this.format = audioFormat;
        this.buffer = modelByteBuffer;
        this.pitchcorrection = f2;
    }

    public void set8BitExtensionBuffer(ModelByteBuffer modelByteBuffer) {
        this.buffer8 = modelByteBuffer;
    }

    public ModelByteBuffer get8BitExtensionBuffer() {
        return this.buffer8;
    }

    public ModelByteBuffer getBuffer() {
        return this.buffer;
    }

    public AudioFormat getFormat() {
        if (this.format == null) {
            if (this.buffer == null) {
                return null;
            }
            InputStream inputStream = this.buffer.getInputStream();
            AudioFormat format = null;
            try {
                format = AudioSystem.getAudioFileFormat(inputStream).getFormat();
            } catch (Exception e2) {
            }
            try {
                inputStream.close();
            } catch (IOException e3) {
            }
            return format;
        }
        return this.format;
    }

    @Override // com.sun.media.sound.ModelWavetable
    public AudioFloatInputStream openStream() {
        if (this.buffer == null) {
            return null;
        }
        if (this.format == null) {
            try {
                return AudioFloatInputStream.getInputStream(AudioSystem.getAudioInputStream(this.buffer.getInputStream()));
            } catch (Exception e2) {
                return null;
            }
        }
        if (this.buffer.array() == null) {
            return AudioFloatInputStream.getInputStream(new AudioInputStream(this.buffer.getInputStream(), this.format, this.buffer.capacity() / this.format.getFrameSize()));
        }
        if (this.buffer8 != null && (this.format.getEncoding().equals(AudioFormat.Encoding.PCM_SIGNED) || this.format.getEncoding().equals(AudioFormat.Encoding.PCM_UNSIGNED))) {
            return AudioFloatInputStream.getInputStream(new AudioInputStream(new Buffer8PlusInputStream(), new AudioFormat(this.format.getEncoding(), this.format.getSampleRate(), this.format.getSampleSizeInBits() + 8, this.format.getChannels(), this.format.getFrameSize() + (1 * this.format.getChannels()), this.format.getFrameRate(), this.format.isBigEndian()), this.buffer.capacity() / this.format.getFrameSize()));
        }
        return AudioFloatInputStream.getInputStream(this.format, this.buffer.array(), (int) this.buffer.arrayOffset(), (int) this.buffer.capacity());
    }

    @Override // com.sun.media.sound.ModelOscillator
    public int getChannels() {
        return getFormat().getChannels();
    }

    @Override // com.sun.media.sound.ModelOscillator
    public ModelOscillatorStream open(float f2) {
        return null;
    }

    @Override // com.sun.media.sound.ModelOscillator
    public float getAttenuation() {
        return this.attenuation;
    }

    public void setAttenuation(float f2) {
        this.attenuation = f2;
    }

    @Override // com.sun.media.sound.ModelWavetable
    public float getLoopLength() {
        return this.loopLength;
    }

    public void setLoopLength(float f2) {
        this.loopLength = f2;
    }

    @Override // com.sun.media.sound.ModelWavetable
    public float getLoopStart() {
        return this.loopStart;
    }

    public void setLoopStart(float f2) {
        this.loopStart = f2;
    }

    public void setLoopType(int i2) {
        this.loopType = i2;
    }

    @Override // com.sun.media.sound.ModelWavetable
    public int getLoopType() {
        return this.loopType;
    }

    @Override // com.sun.media.sound.ModelWavetable
    public float getPitchcorrection() {
        return this.pitchcorrection;
    }

    public void setPitchcorrection(float f2) {
        this.pitchcorrection = f2;
    }
}
