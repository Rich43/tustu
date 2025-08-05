package jdk.management.jfr;

import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: jfr.jar:jdk/management/jfr/Stream.class */
final class Stream implements Closeable {
    private final long identifier;
    private final BufferedInputStream inputStream;
    private final byte[] buffer;
    private volatile long time;

    Stream(InputStream inputStream, long j2, int i2) {
        this.inputStream = new BufferedInputStream(inputStream, StreamManager.DEFAULT_BLOCK_SIZE);
        this.identifier = j2;
        this.buffer = new byte[i2];
    }

    private void touch() {
        this.time = System.currentTimeMillis();
    }

    public long getLastTouched() {
        return this.time;
    }

    public byte[] read() throws IOException {
        touch();
        int i2 = this.inputStream.read(this.buffer);
        if (i2 == -1) {
            return null;
        }
        if (i2 != this.buffer.length) {
            byte[] bArr = new byte[i2];
            System.arraycopy(this.buffer, 0, bArr, 0, i2);
            return bArr;
        }
        return this.buffer;
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.inputStream.close();
    }

    public long getId() {
        return this.identifier;
    }
}
