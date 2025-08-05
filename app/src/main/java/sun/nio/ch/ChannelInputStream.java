package sun.nio.ch;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.IllegalBlockingModeException;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.channels.SelectableChannel;

/* loaded from: rt.jar:sun/nio/ch/ChannelInputStream.class */
public class ChannelInputStream extends InputStream {
    protected final ReadableByteChannel ch;

    /* renamed from: bb, reason: collision with root package name */
    private ByteBuffer f13585bb = null;

    /* renamed from: bs, reason: collision with root package name */
    private byte[] f13586bs = null;
    private byte[] b1 = null;

    /* JADX WARN: Multi-variable type inference failed */
    public static int read(ReadableByteChannel readableByteChannel, ByteBuffer byteBuffer, boolean z2) throws IOException {
        int i2;
        if (readableByteChannel instanceof SelectableChannel) {
            SelectableChannel selectableChannel = (SelectableChannel) readableByteChannel;
            synchronized (selectableChannel.blockingLock()) {
                boolean zIsBlocking = selectableChannel.isBlocking();
                if (!zIsBlocking) {
                    throw new IllegalBlockingModeException();
                }
                if (zIsBlocking != z2) {
                    selectableChannel.configureBlocking(z2);
                }
                i2 = readableByteChannel.read(byteBuffer);
                if (zIsBlocking != z2) {
                    selectableChannel.configureBlocking(zIsBlocking);
                }
            }
            return i2;
        }
        return readableByteChannel.read(byteBuffer);
    }

    public ChannelInputStream(ReadableByteChannel readableByteChannel) {
        this.ch = readableByteChannel;
    }

    @Override // java.io.InputStream
    public synchronized int read() throws IOException {
        if (this.b1 == null) {
            this.b1 = new byte[1];
        }
        if (read(this.b1) == 1) {
            return this.b1[0] & 255;
        }
        return -1;
    }

    @Override // java.io.InputStream
    public synchronized int read(byte[] bArr, int i2, int i3) throws IOException {
        if (i2 < 0 || i2 > bArr.length || i3 < 0 || i2 + i3 > bArr.length || i2 + i3 < 0) {
            throw new IndexOutOfBoundsException();
        }
        if (i3 == 0) {
            return 0;
        }
        ByteBuffer byteBufferWrap = this.f13586bs == bArr ? this.f13585bb : ByteBuffer.wrap(bArr);
        byteBufferWrap.limit(Math.min(i2 + i3, byteBufferWrap.capacity()));
        byteBufferWrap.position(i2);
        this.f13585bb = byteBufferWrap;
        this.f13586bs = bArr;
        return read(byteBufferWrap);
    }

    protected int read(ByteBuffer byteBuffer) throws IOException {
        return read(this.ch, byteBuffer, true);
    }

    @Override // java.io.InputStream
    public int available() throws IOException {
        if (this.ch instanceof SeekableByteChannel) {
            SeekableByteChannel seekableByteChannel = (SeekableByteChannel) this.ch;
            long jMax = Math.max(0L, seekableByteChannel.size() - seekableByteChannel.position());
            if (jMax > 2147483647L) {
                return Integer.MAX_VALUE;
            }
            return (int) jMax;
        }
        return 0;
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.ch.close();
    }
}
