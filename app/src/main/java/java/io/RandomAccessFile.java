package java.io;

import java.nio.channels.FileChannel;
import net.lingala.zip4j.util.InternalZipConstants;
import sun.nio.ch.FileChannelImpl;

/* loaded from: rt.jar:java/io/RandomAccessFile.class */
public class RandomAccessFile implements DataOutput, DataInput, Closeable {
    private FileDescriptor fd;
    private FileChannel channel;
    private boolean rw;
    private final String path;
    private Object closeLock;
    private volatile boolean closed;
    private static final int O_RDONLY = 1;
    private static final int O_RDWR = 2;
    private static final int O_SYNC = 4;
    private static final int O_DSYNC = 8;

    private native void open0(String str, int i2) throws FileNotFoundException;

    private native int read0() throws IOException;

    private native int readBytes(byte[] bArr, int i2, int i3) throws IOException;

    private native void write0(int i2) throws IOException;

    private native void writeBytes(byte[] bArr, int i2, int i3) throws IOException;

    public native long getFilePointer() throws IOException;

    private native void seek0(long j2) throws IOException;

    public native long length() throws IOException;

    public native void setLength(long j2) throws IOException;

    private static native void initIDs();

    /* JADX INFO: Access modifiers changed from: private */
    public native void close0() throws IOException;

    public RandomAccessFile(String str, String str2) throws FileNotFoundException {
        this(str != null ? new File(str) : null, str2);
    }

    public RandomAccessFile(File file, String str) throws FileNotFoundException {
        this.channel = null;
        this.closeLock = new Object();
        this.closed = false;
        String path = file != null ? file.getPath() : null;
        int i2 = -1;
        if (str.equals(InternalZipConstants.READ_MODE)) {
            i2 = 1;
        } else if (str.startsWith(InternalZipConstants.WRITE_MODE)) {
            i2 = 2;
            this.rw = true;
            if (str.length() > 2) {
                if (str.equals("rws")) {
                    i2 = 2 | 4;
                } else if (str.equals("rwd")) {
                    i2 = 2 | 8;
                } else {
                    i2 = -1;
                }
            }
        }
        if (i2 < 0) {
            throw new IllegalArgumentException("Illegal mode \"" + str + "\" must be one of \"r\", \"rw\", \"rws\", or \"rwd\"");
        }
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkRead(path);
            if (this.rw) {
                securityManager.checkWrite(path);
            }
        }
        if (path == null) {
            throw new NullPointerException();
        }
        if (file.isInvalid()) {
            throw new FileNotFoundException("Invalid file path");
        }
        this.fd = new FileDescriptor();
        this.fd.attach(this);
        this.path = path;
        open(path, i2);
    }

    public final FileDescriptor getFD() throws IOException {
        if (this.fd != null) {
            return this.fd;
        }
        throw new IOException();
    }

    public final FileChannel getChannel() {
        FileChannel fileChannel;
        synchronized (this) {
            if (this.channel == null) {
                this.channel = FileChannelImpl.open(this.fd, this.path, true, this.rw, this);
            }
            fileChannel = this.channel;
        }
        return fileChannel;
    }

    private void open(String str, int i2) throws FileNotFoundException {
        open0(str, i2);
    }

    public int read() throws IOException {
        return read0();
    }

    public int read(byte[] bArr, int i2, int i3) throws IOException {
        return readBytes(bArr, i2, i3);
    }

    public int read(byte[] bArr) throws IOException {
        return readBytes(bArr, 0, bArr.length);
    }

    @Override // java.io.DataInput
    public final void readFully(byte[] bArr) throws IOException {
        readFully(bArr, 0, bArr.length);
    }

    @Override // java.io.DataInput
    public final void readFully(byte[] bArr, int i2, int i3) throws IOException {
        int i4 = 0;
        do {
            int i5 = read(bArr, i2 + i4, i3 - i4);
            if (i5 < 0) {
                throw new EOFException();
            }
            i4 += i5;
        } while (i4 < i3);
    }

    @Override // java.io.DataInput
    public int skipBytes(int i2) throws IOException {
        if (i2 <= 0) {
            return 0;
        }
        long filePointer = getFilePointer();
        long length = length();
        long j2 = filePointer + i2;
        if (j2 > length) {
            j2 = length;
        }
        seek(j2);
        return (int) (j2 - filePointer);
    }

    @Override // java.io.DataOutput
    public void write(int i2) throws IOException {
        write0(i2);
    }

    @Override // java.io.DataOutput
    public void write(byte[] bArr) throws IOException {
        writeBytes(bArr, 0, bArr.length);
    }

    @Override // java.io.DataOutput
    public void write(byte[] bArr, int i2, int i3) throws IOException {
        writeBytes(bArr, i2, i3);
    }

    public void seek(long j2) throws IOException {
        if (j2 < 0) {
            throw new IOException("Negative seek offset");
        }
        seek0(j2);
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        synchronized (this.closeLock) {
            if (this.closed) {
                return;
            }
            this.closed = true;
            if (this.channel != null) {
                this.channel.close();
            }
            this.fd.closeAll(new Closeable() { // from class: java.io.RandomAccessFile.1
                @Override // java.io.Closeable, java.lang.AutoCloseable
                public void close() throws IOException {
                    RandomAccessFile.this.close0();
                }
            });
        }
    }

    @Override // java.io.DataInput
    public final boolean readBoolean() throws IOException {
        int i2 = read();
        if (i2 < 0) {
            throw new EOFException();
        }
        return i2 != 0;
    }

    @Override // java.io.DataInput
    public final byte readByte() throws IOException {
        int i2 = read();
        if (i2 < 0) {
            throw new EOFException();
        }
        return (byte) i2;
    }

    @Override // java.io.DataInput
    public final int readUnsignedByte() throws IOException {
        int i2 = read();
        if (i2 < 0) {
            throw new EOFException();
        }
        return i2;
    }

    @Override // java.io.DataInput
    public final short readShort() throws IOException {
        int i2 = read();
        int i3 = read();
        if ((i2 | i3) < 0) {
            throw new EOFException();
        }
        return (short) ((i2 << 8) + (i3 << 0));
    }

    @Override // java.io.DataInput
    public final int readUnsignedShort() throws IOException {
        int i2 = read();
        int i3 = read();
        if ((i2 | i3) < 0) {
            throw new EOFException();
        }
        return (i2 << 8) + (i3 << 0);
    }

    @Override // java.io.DataInput
    public final char readChar() throws IOException {
        int i2 = read();
        int i3 = read();
        if ((i2 | i3) < 0) {
            throw new EOFException();
        }
        return (char) ((i2 << 8) + (i3 << 0));
    }

    @Override // java.io.DataInput
    public final int readInt() throws IOException {
        int i2 = read();
        int i3 = read();
        int i4 = read();
        int i5 = read();
        if ((i2 | i3 | i4 | i5) < 0) {
            throw new EOFException();
        }
        return (i2 << 24) + (i3 << 16) + (i4 << 8) + (i5 << 0);
    }

    @Override // java.io.DataInput
    public final long readLong() throws IOException {
        return (readInt() << 32) + (readInt() & 4294967295L);
    }

    @Override // java.io.DataInput
    public final float readFloat() throws IOException {
        return Float.intBitsToFloat(readInt());
    }

    @Override // java.io.DataInput
    public final double readDouble() throws IOException {
        return Double.longBitsToDouble(readLong());
    }

    @Override // java.io.DataInput
    public final String readLine() throws IOException {
        StringBuffer stringBuffer = new StringBuffer();
        int i2 = -1;
        boolean z2 = false;
        while (!z2) {
            int i3 = read();
            i2 = i3;
            switch (i3) {
                case -1:
                case 10:
                    z2 = true;
                    break;
                case 13:
                    z2 = true;
                    long filePointer = getFilePointer();
                    if (read() == 10) {
                        break;
                    } else {
                        seek(filePointer);
                        break;
                    }
                default:
                    stringBuffer.append((char) i2);
                    break;
            }
        }
        if (i2 == -1 && stringBuffer.length() == 0) {
            return null;
        }
        return stringBuffer.toString();
    }

    @Override // java.io.DataInput
    public final String readUTF() throws IOException {
        return DataInputStream.readUTF(this);
    }

    @Override // java.io.DataOutput
    public final void writeBoolean(boolean z2) throws IOException {
        write(z2 ? 1 : 0);
    }

    @Override // java.io.DataOutput
    public final void writeByte(int i2) throws IOException {
        write(i2);
    }

    @Override // java.io.DataOutput
    public final void writeShort(int i2) throws IOException {
        write((i2 >>> 8) & 255);
        write((i2 >>> 0) & 255);
    }

    @Override // java.io.DataOutput
    public final void writeChar(int i2) throws IOException {
        write((i2 >>> 8) & 255);
        write((i2 >>> 0) & 255);
    }

    @Override // java.io.DataOutput
    public final void writeInt(int i2) throws IOException {
        write((i2 >>> 24) & 255);
        write((i2 >>> 16) & 255);
        write((i2 >>> 8) & 255);
        write((i2 >>> 0) & 255);
    }

    @Override // java.io.DataOutput
    public final void writeLong(long j2) throws IOException {
        write(((int) (j2 >>> 56)) & 255);
        write(((int) (j2 >>> 48)) & 255);
        write(((int) (j2 >>> 40)) & 255);
        write(((int) (j2 >>> 32)) & 255);
        write(((int) (j2 >>> 24)) & 255);
        write(((int) (j2 >>> 16)) & 255);
        write(((int) (j2 >>> 8)) & 255);
        write(((int) (j2 >>> 0)) & 255);
    }

    @Override // java.io.DataOutput
    public final void writeFloat(float f2) throws IOException {
        writeInt(Float.floatToIntBits(f2));
    }

    @Override // java.io.DataOutput
    public final void writeDouble(double d2) throws IOException {
        writeLong(Double.doubleToLongBits(d2));
    }

    @Override // java.io.DataOutput
    public final void writeBytes(String str) throws IOException {
        int length = str.length();
        byte[] bArr = new byte[length];
        str.getBytes(0, length, bArr, 0);
        writeBytes(bArr, 0, length);
    }

    @Override // java.io.DataOutput
    public final void writeChars(String str) throws IOException {
        int length = str.length();
        int i2 = 2 * length;
        byte[] bArr = new byte[i2];
        char[] cArr = new char[length];
        str.getChars(0, length, cArr, 0);
        int i3 = 0;
        for (int i4 = 0; i4 < length; i4++) {
            int i5 = i3;
            int i6 = i3 + 1;
            bArr[i5] = (byte) (cArr[i4] >>> '\b');
            i3 = i6 + 1;
            bArr[i6] = (byte) (cArr[i4] >>> 0);
        }
        writeBytes(bArr, 0, i2);
    }

    @Override // java.io.DataOutput
    public final void writeUTF(String str) throws IOException {
        DataOutputStream.writeUTF(str, this);
    }

    static {
        initIDs();
    }
}
