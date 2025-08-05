package javax.sound.sampled;

import java.io.IOException;
import java.io.InputStream;
import javax.sound.sampled.AudioFormat;

/* loaded from: rt.jar:javax/sound/sampled/AudioInputStream.class */
public class AudioInputStream extends InputStream {
    private InputStream stream;
    protected AudioFormat format;
    protected long frameLength;
    protected int frameSize;
    protected long framePos;
    private long markpos;
    private byte[] pushBackBuffer = null;
    private int pushBackLen = 0;
    private byte[] markPushBackBuffer = null;
    private int markPushBackLen = 0;

    public AudioInputStream(InputStream inputStream, AudioFormat audioFormat, long j2) {
        this.format = audioFormat;
        this.frameLength = j2;
        this.frameSize = audioFormat.getFrameSize();
        if (this.frameSize == -1 || this.frameSize <= 0) {
            this.frameSize = 1;
        }
        this.stream = inputStream;
        this.framePos = 0L;
        this.markpos = 0L;
    }

    public AudioInputStream(TargetDataLine targetDataLine) {
        TargetDataLineInputStream targetDataLineInputStream = new TargetDataLineInputStream(targetDataLine);
        this.format = targetDataLine.getFormat();
        this.frameLength = -1L;
        this.frameSize = this.format.getFrameSize();
        if (this.frameSize == -1 || this.frameSize <= 0) {
            this.frameSize = 1;
        }
        this.stream = targetDataLineInputStream;
        this.framePos = 0L;
        this.markpos = 0L;
    }

    public AudioFormat getFormat() {
        return this.format;
    }

    public long getFrameLength() {
        return this.frameLength;
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        if (this.frameSize != 1) {
            throw new IOException("cannot read a single byte if frame size > 1");
        }
        byte[] bArr = new byte[1];
        if (read(bArr) <= 0) {
            return -1;
        }
        return bArr[0] & 255;
    }

    @Override // java.io.InputStream
    public int read(byte[] bArr) throws IOException {
        return read(bArr, 0, bArr.length);
    }

    @Override // java.io.InputStream
    public int read(byte[] bArr, int i2, int i3) throws IOException {
        if (i3 % this.frameSize != 0) {
            i3 -= i3 % this.frameSize;
            if (i3 == 0) {
                return 0;
            }
        }
        if (this.frameLength != -1) {
            if (this.framePos >= this.frameLength) {
                return -1;
            }
            if (i3 / this.frameSize > this.frameLength - this.framePos) {
                i3 = ((int) (this.frameLength - this.framePos)) * this.frameSize;
            }
        }
        int i4 = 0;
        int i5 = i2;
        if (this.pushBackLen > 0 && i3 >= this.pushBackLen) {
            System.arraycopy(this.pushBackBuffer, 0, bArr, i2, this.pushBackLen);
            i5 += this.pushBackLen;
            i3 -= this.pushBackLen;
            i4 = 0 + this.pushBackLen;
            this.pushBackLen = 0;
        }
        int i6 = this.stream.read(bArr, i5, i3);
        if (i6 == -1) {
            return -1;
        }
        if (i6 > 0) {
            i4 += i6;
        }
        if (i4 > 0) {
            this.pushBackLen = i4 % this.frameSize;
            if (this.pushBackLen > 0) {
                if (this.pushBackBuffer == null) {
                    this.pushBackBuffer = new byte[this.frameSize];
                }
                System.arraycopy(bArr, (i2 + i4) - this.pushBackLen, this.pushBackBuffer, 0, this.pushBackLen);
                i4 -= this.pushBackLen;
            }
            this.framePos += i4 / this.frameSize;
        }
        return i4;
    }

    @Override // java.io.InputStream
    public long skip(long j2) throws IOException {
        if (j2 % this.frameSize != 0) {
            j2 -= j2 % this.frameSize;
        }
        if (this.frameLength != -1 && j2 / this.frameSize > this.frameLength - this.framePos) {
            j2 = (this.frameLength - this.framePos) * this.frameSize;
        }
        long jSkip = this.stream.skip(j2);
        if (jSkip % this.frameSize != 0) {
            throw new IOException("Could not skip an integer number of frames.");
        }
        if (jSkip >= 0) {
            this.framePos += jSkip / this.frameSize;
        }
        return jSkip;
    }

    @Override // java.io.InputStream
    public int available() throws IOException {
        int iAvailable = this.stream.available();
        if (this.frameLength != -1 && iAvailable / this.frameSize > this.frameLength - this.framePos) {
            return ((int) (this.frameLength - this.framePos)) * this.frameSize;
        }
        return iAvailable;
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.stream.close();
    }

    @Override // java.io.InputStream
    public void mark(int i2) {
        this.stream.mark(i2);
        if (markSupported()) {
            this.markpos = this.framePos;
            this.markPushBackLen = this.pushBackLen;
            if (this.markPushBackLen > 0) {
                if (this.markPushBackBuffer == null) {
                    this.markPushBackBuffer = new byte[this.frameSize];
                }
                System.arraycopy(this.pushBackBuffer, 0, this.markPushBackBuffer, 0, this.markPushBackLen);
            }
        }
    }

    @Override // java.io.InputStream
    public void reset() throws IOException {
        this.stream.reset();
        this.framePos = this.markpos;
        this.pushBackLen = this.markPushBackLen;
        if (this.pushBackLen > 0) {
            if (this.pushBackBuffer == null) {
                this.pushBackBuffer = new byte[this.frameSize - 1];
            }
            System.arraycopy(this.markPushBackBuffer, 0, this.pushBackBuffer, 0, this.pushBackLen);
        }
    }

    @Override // java.io.InputStream
    public boolean markSupported() {
        return this.stream.markSupported();
    }

    /* loaded from: rt.jar:javax/sound/sampled/AudioInputStream$TargetDataLineInputStream.class */
    private class TargetDataLineInputStream extends InputStream {
        TargetDataLine line;

        TargetDataLineInputStream(TargetDataLine targetDataLine) {
            this.line = targetDataLine;
        }

        @Override // java.io.InputStream
        public int available() throws IOException {
            return this.line.available();
        }

        @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            if (this.line.isActive()) {
                this.line.flush();
                this.line.stop();
            }
            this.line.close();
        }

        @Override // java.io.InputStream
        public int read() throws IOException {
            byte[] bArr = new byte[1];
            if (read(bArr, 0, 1) == -1) {
                return -1;
            }
            int i2 = bArr[0];
            if (this.line.getFormat().getEncoding().equals(AudioFormat.Encoding.PCM_SIGNED)) {
                i2 += 128;
            }
            return i2;
        }

        @Override // java.io.InputStream
        public int read(byte[] bArr, int i2, int i3) throws IOException {
            try {
                return this.line.read(bArr, i2, i3);
            } catch (IllegalArgumentException e2) {
                throw new IOException(e2.getMessage());
            }
        }
    }
}
