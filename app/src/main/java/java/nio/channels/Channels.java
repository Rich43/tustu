package java.nio.channels;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.channels.spi.AbstractInterruptibleChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.concurrent.ExecutionException;
import org.icepdf.core.util.PdfOps;
import sun.nio.ch.ChannelInputStream;
import sun.nio.cs.StreamDecoder;
import sun.nio.cs.StreamEncoder;

/* loaded from: rt.jar:java/nio/channels/Channels.class */
public final class Channels {
    private Channels() {
    }

    private static void checkNotNull(Object obj, String str) {
        if (obj == null) {
            throw new NullPointerException(PdfOps.DOUBLE_QUOTE__TOKEN + str + "\" is null!");
        }
    }

    private static void writeFullyImpl(WritableByteChannel writableByteChannel, ByteBuffer byteBuffer) throws IOException {
        while (byteBuffer.remaining() > 0) {
            if (writableByteChannel.write(byteBuffer) <= 0) {
                throw new RuntimeException("no bytes written");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public static void writeFully(WritableByteChannel writableByteChannel, ByteBuffer byteBuffer) throws IOException {
        if (writableByteChannel instanceof SelectableChannel) {
            SelectableChannel selectableChannel = (SelectableChannel) writableByteChannel;
            synchronized (selectableChannel.blockingLock()) {
                if (!selectableChannel.isBlocking()) {
                    throw new IllegalBlockingModeException();
                }
                writeFullyImpl(writableByteChannel, byteBuffer);
            }
            return;
        }
        writeFullyImpl(writableByteChannel, byteBuffer);
    }

    public static InputStream newInputStream(ReadableByteChannel readableByteChannel) {
        checkNotNull(readableByteChannel, "ch");
        return new ChannelInputStream(readableByteChannel);
    }

    public static OutputStream newOutputStream(final WritableByteChannel writableByteChannel) {
        checkNotNull(writableByteChannel, "ch");
        return new OutputStream() { // from class: java.nio.channels.Channels.1

            /* renamed from: bb, reason: collision with root package name */
            private ByteBuffer f12462bb = null;

            /* renamed from: bs, reason: collision with root package name */
            private byte[] f12463bs = null;
            private byte[] b1 = null;

            @Override // java.io.OutputStream
            public synchronized void write(int i2) throws IOException {
                if (this.b1 == null) {
                    this.b1 = new byte[1];
                }
                this.b1[0] = (byte) i2;
                write(this.b1);
            }

            @Override // java.io.OutputStream
            public synchronized void write(byte[] bArr, int i2, int i3) throws IOException {
                if (i2 < 0 || i2 > bArr.length || i3 < 0 || i2 + i3 > bArr.length || i2 + i3 < 0) {
                    throw new IndexOutOfBoundsException();
                }
                if (i3 == 0) {
                    return;
                }
                ByteBuffer byteBufferWrap = this.f12463bs == bArr ? this.f12462bb : ByteBuffer.wrap(bArr);
                byteBufferWrap.limit(Math.min(i2 + i3, byteBufferWrap.capacity()));
                byteBufferWrap.position(i2);
                this.f12462bb = byteBufferWrap;
                this.f12463bs = bArr;
                Channels.writeFully(writableByteChannel, byteBufferWrap);
            }

            @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
            public void close() throws IOException {
                writableByteChannel.close();
            }
        };
    }

    public static InputStream newInputStream(final AsynchronousByteChannel asynchronousByteChannel) {
        checkNotNull(asynchronousByteChannel, "ch");
        return new InputStream() { // from class: java.nio.channels.Channels.2

            /* renamed from: bb, reason: collision with root package name */
            private ByteBuffer f12464bb = null;

            /* renamed from: bs, reason: collision with root package name */
            private byte[] f12465bs = null;
            private byte[] b1 = null;

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
                int iIntValue;
                if (i2 < 0 || i2 > bArr.length || i3 < 0 || i2 + i3 > bArr.length || i2 + i3 < 0) {
                    throw new IndexOutOfBoundsException();
                }
                if (i3 == 0) {
                    return 0;
                }
                ByteBuffer byteBufferWrap = this.f12465bs == bArr ? this.f12464bb : ByteBuffer.wrap(bArr);
                byteBufferWrap.position(i2);
                byteBufferWrap.limit(Math.min(i2 + i3, byteBufferWrap.capacity()));
                this.f12464bb = byteBufferWrap;
                this.f12465bs = bArr;
                boolean z2 = false;
                while (true) {
                    try {
                        try {
                            iIntValue = asynchronousByteChannel.read(byteBufferWrap).get().intValue();
                            break;
                        } catch (InterruptedException e2) {
                            z2 = true;
                        } catch (ExecutionException e3) {
                            throw new IOException(e3.getCause());
                        }
                    } catch (Throwable th) {
                        if (z2) {
                            Thread.currentThread().interrupt();
                        }
                        throw th;
                    }
                }
                if (z2) {
                    Thread.currentThread().interrupt();
                }
                return iIntValue;
            }

            @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
            public void close() throws IOException {
                asynchronousByteChannel.close();
            }
        };
    }

    public static OutputStream newOutputStream(final AsynchronousByteChannel asynchronousByteChannel) {
        checkNotNull(asynchronousByteChannel, "ch");
        return new OutputStream() { // from class: java.nio.channels.Channels.3

            /* renamed from: bb, reason: collision with root package name */
            private ByteBuffer f12466bb = null;

            /* renamed from: bs, reason: collision with root package name */
            private byte[] f12467bs = null;
            private byte[] b1 = null;

            @Override // java.io.OutputStream
            public synchronized void write(int i2) throws IOException {
                if (this.b1 == null) {
                    this.b1 = new byte[1];
                }
                this.b1[0] = (byte) i2;
                write(this.b1);
            }

            @Override // java.io.OutputStream
            public synchronized void write(byte[] bArr, int i2, int i3) throws IOException {
                if (i2 < 0 || i2 > bArr.length || i3 < 0 || i2 + i3 > bArr.length || i2 + i3 < 0) {
                    throw new IndexOutOfBoundsException();
                }
                if (i3 == 0) {
                    return;
                }
                ByteBuffer byteBufferWrap = this.f12467bs == bArr ? this.f12466bb : ByteBuffer.wrap(bArr);
                byteBufferWrap.limit(Math.min(i2 + i3, byteBufferWrap.capacity()));
                byteBufferWrap.position(i2);
                this.f12466bb = byteBufferWrap;
                this.f12467bs = bArr;
                boolean z2 = false;
                while (byteBufferWrap.remaining() > 0) {
                    try {
                        try {
                            asynchronousByteChannel.write(byteBufferWrap).get();
                        } catch (InterruptedException e2) {
                            z2 = true;
                        } catch (ExecutionException e3) {
                            throw new IOException(e3.getCause());
                        }
                    } finally {
                        if (z2) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
            }

            @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
            public void close() throws IOException {
                asynchronousByteChannel.close();
            }
        };
    }

    public static ReadableByteChannel newChannel(InputStream inputStream) {
        checkNotNull(inputStream, "in");
        if ((inputStream instanceof FileInputStream) && FileInputStream.class.equals(inputStream.getClass())) {
            return ((FileInputStream) inputStream).getChannel();
        }
        return new ReadableByteChannelImpl(inputStream);
    }

    /* loaded from: rt.jar:java/nio/channels/Channels$ReadableByteChannelImpl.class */
    private static class ReadableByteChannelImpl extends AbstractInterruptibleChannel implements ReadableByteChannel {
        InputStream in;
        private static final int TRANSFER_SIZE = 8192;
        private byte[] buf = new byte[0];
        private boolean open = true;
        private Object readLock = new Object();

        ReadableByteChannelImpl(InputStream inputStream) {
            this.in = inputStream;
        }

        @Override // java.nio.channels.ReadableByteChannel
        public int read(ByteBuffer byteBuffer) throws IOException {
            int iRemaining = byteBuffer.remaining();
            int i2 = 0;
            int i3 = 0;
            synchronized (this.readLock) {
                while (i2 < iRemaining) {
                    int iMin = Math.min(iRemaining - i2, 8192);
                    if (this.buf.length < iMin) {
                        this.buf = new byte[iMin];
                    }
                    if (i2 > 0 && this.in.available() <= 0) {
                        break;
                    }
                    try {
                        begin();
                        i3 = this.in.read(this.buf, 0, iMin);
                        end(i3 > 0);
                        if (i3 < 0) {
                            break;
                        }
                        i2 += i3;
                        byteBuffer.put(this.buf, 0, i3);
                    } catch (Throwable th) {
                        end(i3 > 0);
                        throw th;
                    }
                }
                if (i3 < 0 && i2 == 0) {
                    return -1;
                }
                return i2;
            }
        }

        @Override // java.nio.channels.spi.AbstractInterruptibleChannel
        protected void implCloseChannel() throws IOException {
            this.in.close();
            this.open = false;
        }
    }

    public static WritableByteChannel newChannel(OutputStream outputStream) {
        checkNotNull(outputStream, "out");
        if ((outputStream instanceof FileOutputStream) && FileOutputStream.class.equals(outputStream.getClass())) {
            return ((FileOutputStream) outputStream).getChannel();
        }
        return new WritableByteChannelImpl(outputStream);
    }

    /* loaded from: rt.jar:java/nio/channels/Channels$WritableByteChannelImpl.class */
    private static class WritableByteChannelImpl extends AbstractInterruptibleChannel implements WritableByteChannel {
        OutputStream out;
        private static final int TRANSFER_SIZE = 8192;
        private byte[] buf = new byte[0];
        private boolean open = true;
        private Object writeLock = new Object();

        WritableByteChannelImpl(OutputStream outputStream) {
            this.out = outputStream;
        }

        @Override // java.nio.channels.WritableByteChannel
        public int write(ByteBuffer byteBuffer) throws IOException {
            int i2;
            int iRemaining = byteBuffer.remaining();
            int i3 = 0;
            synchronized (this.writeLock) {
                while (i3 < iRemaining) {
                    int iMin = Math.min(iRemaining - i3, 8192);
                    if (this.buf.length < iMin) {
                        this.buf = new byte[iMin];
                    }
                    byteBuffer.get(this.buf, 0, iMin);
                    try {
                        begin();
                        this.out.write(this.buf, 0, iMin);
                        end(iMin > 0);
                        i3 += iMin;
                    } catch (Throwable th) {
                        end(iMin > 0);
                        throw th;
                    }
                }
                i2 = i3;
            }
            return i2;
        }

        @Override // java.nio.channels.spi.AbstractInterruptibleChannel
        protected void implCloseChannel() throws IOException {
            this.out.close();
            this.open = false;
        }
    }

    public static Reader newReader(ReadableByteChannel readableByteChannel, CharsetDecoder charsetDecoder, int i2) {
        checkNotNull(readableByteChannel, "ch");
        return StreamDecoder.forDecoder(readableByteChannel, charsetDecoder.reset(), i2);
    }

    public static Reader newReader(ReadableByteChannel readableByteChannel, String str) {
        checkNotNull(str, "csName");
        return newReader(readableByteChannel, Charset.forName(str).newDecoder(), -1);
    }

    public static Writer newWriter(WritableByteChannel writableByteChannel, CharsetEncoder charsetEncoder, int i2) {
        checkNotNull(writableByteChannel, "ch");
        return StreamEncoder.forEncoder(writableByteChannel, charsetEncoder.reset(), i2);
    }

    public static Writer newWriter(WritableByteChannel writableByteChannel, String str) {
        checkNotNull(str, "csName");
        return newWriter(writableByteChannel, Charset.forName(str).newEncoder(), -1);
    }
}
