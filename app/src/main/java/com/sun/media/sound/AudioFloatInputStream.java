package com.sun.media.sound;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

/* loaded from: rt.jar:com/sun/media/sound/AudioFloatInputStream.class */
public abstract class AudioFloatInputStream {
    public abstract AudioFormat getFormat();

    public abstract long getFrameLength();

    public abstract int read(float[] fArr, int i2, int i3) throws IOException;

    public abstract long skip(long j2) throws IOException;

    public abstract int available() throws IOException;

    public abstract void close() throws IOException;

    public abstract void mark(int i2);

    public abstract boolean markSupported();

    public abstract void reset() throws IOException;

    /* loaded from: rt.jar:com/sun/media/sound/AudioFloatInputStream$BytaArrayAudioFloatInputStream.class */
    private static class BytaArrayAudioFloatInputStream extends AudioFloatInputStream {
        private int pos = 0;
        private int markpos = 0;
        private final AudioFloatConverter converter;
        private final AudioFormat format;
        private final byte[] buffer;
        private final int buffer_offset;
        private final int buffer_len;
        private final int framesize_pc;

        BytaArrayAudioFloatInputStream(AudioFloatConverter audioFloatConverter, byte[] bArr, int i2, int i3) {
            this.converter = audioFloatConverter;
            this.format = audioFloatConverter.getFormat();
            this.buffer = bArr;
            this.buffer_offset = i2;
            this.framesize_pc = this.format.getFrameSize() / this.format.getChannels();
            this.buffer_len = i3 / this.framesize_pc;
        }

        @Override // com.sun.media.sound.AudioFloatInputStream
        public AudioFormat getFormat() {
            return this.format;
        }

        @Override // com.sun.media.sound.AudioFloatInputStream
        public long getFrameLength() {
            return this.buffer_len;
        }

        @Override // com.sun.media.sound.AudioFloatInputStream
        public int read(float[] fArr, int i2, int i3) throws IOException {
            if (fArr == null) {
                throw new NullPointerException();
            }
            if (i2 < 0 || i3 < 0 || i3 > fArr.length - i2) {
                throw new IndexOutOfBoundsException();
            }
            if (this.pos >= this.buffer_len) {
                return -1;
            }
            if (i3 == 0) {
                return 0;
            }
            if (this.pos + i3 > this.buffer_len) {
                i3 = this.buffer_len - this.pos;
            }
            this.converter.toFloatArray(this.buffer, this.buffer_offset + (this.pos * this.framesize_pc), fArr, i2, i3);
            this.pos += i3;
            return i3;
        }

        @Override // com.sun.media.sound.AudioFloatInputStream
        public long skip(long j2) throws IOException {
            if (this.pos >= this.buffer_len) {
                return -1L;
            }
            if (j2 <= 0) {
                return 0L;
            }
            if (this.pos + j2 > this.buffer_len) {
                j2 = this.buffer_len - this.pos;
            }
            this.pos = (int) (this.pos + j2);
            return j2;
        }

        @Override // com.sun.media.sound.AudioFloatInputStream
        public int available() throws IOException {
            return this.buffer_len - this.pos;
        }

        @Override // com.sun.media.sound.AudioFloatInputStream
        public void close() throws IOException {
        }

        @Override // com.sun.media.sound.AudioFloatInputStream
        public void mark(int i2) {
            this.markpos = this.pos;
        }

        @Override // com.sun.media.sound.AudioFloatInputStream
        public boolean markSupported() {
            return true;
        }

        @Override // com.sun.media.sound.AudioFloatInputStream
        public void reset() throws IOException {
            this.pos = this.markpos;
        }
    }

    /* loaded from: rt.jar:com/sun/media/sound/AudioFloatInputStream$DirectAudioFloatInputStream.class */
    private static class DirectAudioFloatInputStream extends AudioFloatInputStream {
        private final AudioInputStream stream;
        private AudioFloatConverter converter;
        private final int framesize_pc;
        private byte[] buffer;

        DirectAudioFloatInputStream(AudioInputStream audioInputStream) {
            AudioFormat audioFormat;
            this.converter = AudioFloatConverter.getConverter(audioInputStream.getFormat());
            if (this.converter == null) {
                AudioFormat format = audioInputStream.getFormat();
                AudioFormat[] targetFormats = AudioSystem.getTargetFormats(AudioFormat.Encoding.PCM_SIGNED, format);
                if (targetFormats.length != 0) {
                    audioFormat = targetFormats[0];
                } else {
                    float sampleRate = format.getSampleRate();
                    format.getSampleSizeInBits();
                    format.getFrameSize();
                    format.getFrameRate();
                    audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, sampleRate, 16, format.getChannels(), format.getChannels() * (16 / 8), sampleRate, false);
                }
                audioInputStream = AudioSystem.getAudioInputStream(audioFormat, audioInputStream);
                this.converter = AudioFloatConverter.getConverter(audioInputStream.getFormat());
            }
            this.framesize_pc = audioInputStream.getFormat().getFrameSize() / audioInputStream.getFormat().getChannels();
            this.stream = audioInputStream;
        }

        @Override // com.sun.media.sound.AudioFloatInputStream
        public AudioFormat getFormat() {
            return this.stream.getFormat();
        }

        @Override // com.sun.media.sound.AudioFloatInputStream
        public long getFrameLength() {
            return this.stream.getFrameLength();
        }

        @Override // com.sun.media.sound.AudioFloatInputStream
        public int read(float[] fArr, int i2, int i3) throws IOException {
            int i4 = i3 * this.framesize_pc;
            if (this.buffer == null || this.buffer.length < i4) {
                this.buffer = new byte[i4];
            }
            int i5 = this.stream.read(this.buffer, 0, i4);
            if (i5 == -1) {
                return -1;
            }
            this.converter.toFloatArray(this.buffer, fArr, i2, i5 / this.framesize_pc);
            return i5 / this.framesize_pc;
        }

        @Override // com.sun.media.sound.AudioFloatInputStream
        public long skip(long j2) throws IOException {
            long jSkip = this.stream.skip(j2 * this.framesize_pc);
            if (jSkip == -1) {
                return -1L;
            }
            return jSkip / this.framesize_pc;
        }

        @Override // com.sun.media.sound.AudioFloatInputStream
        public int available() throws IOException {
            return this.stream.available() / this.framesize_pc;
        }

        @Override // com.sun.media.sound.AudioFloatInputStream
        public void close() throws IOException {
            this.stream.close();
        }

        @Override // com.sun.media.sound.AudioFloatInputStream
        public void mark(int i2) {
            this.stream.mark(i2 * this.framesize_pc);
        }

        @Override // com.sun.media.sound.AudioFloatInputStream
        public boolean markSupported() {
            return this.stream.markSupported();
        }

        @Override // com.sun.media.sound.AudioFloatInputStream
        public void reset() throws IOException {
            this.stream.reset();
        }
    }

    public static AudioFloatInputStream getInputStream(URL url) throws UnsupportedAudioFileException, IOException {
        return new DirectAudioFloatInputStream(AudioSystem.getAudioInputStream(url));
    }

    public static AudioFloatInputStream getInputStream(File file) throws UnsupportedAudioFileException, IOException {
        return new DirectAudioFloatInputStream(AudioSystem.getAudioInputStream(file));
    }

    public static AudioFloatInputStream getInputStream(InputStream inputStream) throws UnsupportedAudioFileException, IOException {
        return new DirectAudioFloatInputStream(AudioSystem.getAudioInputStream(inputStream));
    }

    public static AudioFloatInputStream getInputStream(AudioInputStream audioInputStream) {
        return new DirectAudioFloatInputStream(audioInputStream);
    }

    public static AudioFloatInputStream getInputStream(AudioFormat audioFormat, byte[] bArr, int i2, int i3) {
        AudioFloatConverter converter = AudioFloatConverter.getConverter(audioFormat);
        if (converter != null) {
            return new BytaArrayAudioFloatInputStream(converter, bArr, i2, i3);
        }
        return getInputStream(new AudioInputStream(new ByteArrayInputStream(bArr, i2, i3), audioFormat, audioFormat.getFrameSize() == -1 ? -1L : i3 / audioFormat.getFrameSize()));
    }

    public final int read(float[] fArr) throws IOException {
        return read(fArr, 0, fArr.length);
    }

    public final float read() throws IOException {
        float[] fArr = new float[1];
        int i2 = read(fArr, 0, 1);
        if (i2 == -1 || i2 == 0) {
            return 0.0f;
        }
        return fArr[0];
    }
}
