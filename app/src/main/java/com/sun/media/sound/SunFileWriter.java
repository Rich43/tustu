package com.sun.media.sound;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.spi.AudioFileWriter;
import sun.text.normalizer.NormalizerImpl;

/* loaded from: rt.jar:com/sun/media/sound/SunFileWriter.class */
abstract class SunFileWriter extends AudioFileWriter {
    protected static final int bufferSize = 16384;
    protected static final int bisBufferSize = 4096;
    final AudioFileFormat.Type[] types;

    @Override // javax.sound.sampled.spi.AudioFileWriter
    public abstract AudioFileFormat.Type[] getAudioFileTypes(AudioInputStream audioInputStream);

    @Override // javax.sound.sampled.spi.AudioFileWriter
    public abstract int write(AudioInputStream audioInputStream, AudioFileFormat.Type type, OutputStream outputStream) throws IOException;

    @Override // javax.sound.sampled.spi.AudioFileWriter
    public abstract int write(AudioInputStream audioInputStream, AudioFileFormat.Type type, File file) throws IOException;

    SunFileWriter(AudioFileFormat.Type[] typeArr) {
        this.types = typeArr;
    }

    @Override // javax.sound.sampled.spi.AudioFileWriter
    public final AudioFileFormat.Type[] getAudioFileTypes() {
        AudioFileFormat.Type[] typeArr = new AudioFileFormat.Type[this.types.length];
        System.arraycopy(this.types, 0, typeArr, 0, this.types.length);
        return typeArr;
    }

    final int rllong(DataInputStream dataInputStream) throws IOException {
        int i2 = dataInputStream.readInt();
        int i3 = (i2 & 255) << 24;
        int i4 = (i2 & NormalizerImpl.CC_MASK) << 8;
        int i5 = (i2 & 16711680) >> 8;
        return i3 | i4 | i5 | ((i2 & (-16777216)) >>> 24);
    }

    final int big2little(int i2) {
        return ((i2 & 255) << 24) | ((i2 & NormalizerImpl.CC_MASK) << 8) | ((i2 & 16711680) >> 8) | ((i2 & (-16777216)) >>> 24);
    }

    final short rlshort(DataInputStream dataInputStream) throws IOException {
        short s2 = dataInputStream.readShort();
        return (short) (((short) ((s2 & 255) << 8)) | ((short) ((s2 & 65280) >>> 8)));
    }

    final short big2littleShort(short s2) {
        return (short) (((short) ((s2 & 255) << 8)) | ((short) ((s2 & 65280) >>> 8)));
    }

    /* loaded from: rt.jar:com/sun/media/sound/SunFileWriter$NoCloseInputStream.class */
    final class NoCloseInputStream extends InputStream {
        private final InputStream in;

        NoCloseInputStream(InputStream inputStream) {
            this.in = inputStream;
        }

        @Override // java.io.InputStream
        public int read() throws IOException {
            return this.in.read();
        }

        @Override // java.io.InputStream
        public int read(byte[] bArr) throws IOException {
            return this.in.read(bArr);
        }

        @Override // java.io.InputStream
        public int read(byte[] bArr, int i2, int i3) throws IOException {
            return this.in.read(bArr, i2, i3);
        }

        @Override // java.io.InputStream
        public long skip(long j2) throws IOException {
            return this.in.skip(j2);
        }

        @Override // java.io.InputStream
        public int available() throws IOException {
            return this.in.available();
        }

        @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
        }

        @Override // java.io.InputStream
        public void mark(int i2) {
            this.in.mark(i2);
        }

        @Override // java.io.InputStream
        public void reset() throws IOException {
            this.in.reset();
        }

        @Override // java.io.InputStream
        public boolean markSupported() {
            return this.in.markSupported();
        }
    }
}
