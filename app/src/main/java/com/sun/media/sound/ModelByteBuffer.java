package com.sun.media.sound;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.Collection;
import java.util.Iterator;
import net.lingala.zip4j.util.InternalZipConstants;

/* loaded from: rt.jar:com/sun/media/sound/ModelByteBuffer.class */
public final class ModelByteBuffer {
    private ModelByteBuffer root;
    private File file;
    private long fileoffset;
    private byte[] buffer;
    private long offset;
    private final long len;

    /* loaded from: rt.jar:com/sun/media/sound/ModelByteBuffer$RandomFileInputStream.class */
    private class RandomFileInputStream extends InputStream {
        private final RandomAccessFile raf;
        private long left;
        private long mark = 0;
        private long markleft = 0;

        RandomFileInputStream() throws IOException {
            this.raf = new RandomAccessFile(ModelByteBuffer.this.root.file, InternalZipConstants.READ_MODE);
            this.raf.seek(ModelByteBuffer.this.root.fileoffset + ModelByteBuffer.this.arrayOffset());
            this.left = ModelByteBuffer.this.capacity();
        }

        @Override // java.io.InputStream
        public int available() throws IOException {
            if (this.left > 2147483647L) {
                return Integer.MAX_VALUE;
            }
            return (int) this.left;
        }

        @Override // java.io.InputStream
        public synchronized void mark(int i2) {
            try {
                this.mark = this.raf.getFilePointer();
                this.markleft = this.left;
            } catch (IOException e2) {
            }
        }

        @Override // java.io.InputStream
        public boolean markSupported() {
            return true;
        }

        @Override // java.io.InputStream
        public synchronized void reset() throws IOException {
            this.raf.seek(this.mark);
            this.left = this.markleft;
        }

        @Override // java.io.InputStream
        public long skip(long j2) throws IOException {
            if (j2 < 0) {
                return 0L;
            }
            if (j2 > this.left) {
                j2 = this.left;
            }
            this.raf.seek(this.raf.getFilePointer() + j2);
            this.left -= j2;
            return j2;
        }

        @Override // java.io.InputStream
        public int read(byte[] bArr, int i2, int i3) throws IOException {
            int i4;
            if (i3 > this.left) {
                i3 = (int) this.left;
            }
            if (this.left == 0 || (i4 = this.raf.read(bArr, i2, i3)) == -1) {
                return -1;
            }
            this.left -= i4;
            return i4;
        }

        @Override // java.io.InputStream
        public int read(byte[] bArr) throws IOException {
            int i2;
            int length = bArr.length;
            if (length > this.left) {
                length = (int) this.left;
            }
            if (this.left == 0 || (i2 = this.raf.read(bArr, 0, length)) == -1) {
                return -1;
            }
            this.left -= i2;
            return i2;
        }

        @Override // java.io.InputStream
        public int read() throws IOException {
            int i2;
            if (this.left == 0 || (i2 = this.raf.read()) == -1) {
                return -1;
            }
            this.left--;
            return i2;
        }

        @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            this.raf.close();
        }
    }

    private ModelByteBuffer(ModelByteBuffer modelByteBuffer, long j2, long j3, boolean z2) {
        this.root = this;
        this.root = modelByteBuffer.root;
        this.offset = 0L;
        long j4 = modelByteBuffer.len;
        j2 = j2 < 0 ? 0L : j2;
        j2 = j2 > j4 ? j4 : j2;
        j3 = j3 < 0 ? 0L : j3;
        j3 = j3 > j4 ? j4 : j3;
        j2 = j2 > j3 ? j3 : j2;
        this.offset = j2;
        this.len = j3 - j2;
        if (z2) {
            this.buffer = this.root.buffer;
            if (this.root.file != null) {
                this.file = this.root.file;
                this.fileoffset = this.root.fileoffset + arrayOffset();
                this.offset = 0L;
            } else {
                this.offset = arrayOffset();
            }
            this.root = this;
        }
    }

    public ModelByteBuffer(byte[] bArr) {
        this.root = this;
        this.buffer = bArr;
        this.offset = 0L;
        this.len = bArr.length;
    }

    public ModelByteBuffer(byte[] bArr, int i2, int i3) {
        this.root = this;
        this.buffer = bArr;
        this.offset = i2;
        this.len = i3;
    }

    public ModelByteBuffer(File file) {
        this.root = this;
        this.file = file;
        this.fileoffset = 0L;
        this.len = file.length();
    }

    public ModelByteBuffer(File file, long j2, long j3) {
        this.root = this;
        this.file = file;
        this.fileoffset = j2;
        this.len = j3;
    }

    public void writeTo(OutputStream outputStream) throws IOException {
        if (this.root.file != null && this.root.buffer == null) {
            InputStream inputStream = getInputStream();
            Throwable th = null;
            try {
                try {
                    byte[] bArr = new byte[1024];
                    while (true) {
                        int i2 = inputStream.read(bArr);
                        if (i2 == -1) {
                            break;
                        } else {
                            outputStream.write(bArr, 0, i2);
                        }
                    }
                    if (inputStream != null) {
                        if (0 != 0) {
                            try {
                                inputStream.close();
                                return;
                            } catch (Throwable th2) {
                                th.addSuppressed(th2);
                                return;
                            }
                        }
                        inputStream.close();
                        return;
                    }
                    return;
                } catch (Throwable th3) {
                    th = th3;
                    throw th3;
                }
            } catch (Throwable th4) {
                if (inputStream != null) {
                    if (th != null) {
                        try {
                            inputStream.close();
                        } catch (Throwable th5) {
                            th.addSuppressed(th5);
                        }
                    } else {
                        inputStream.close();
                    }
                }
                throw th4;
            }
        }
        outputStream.write(array(), (int) arrayOffset(), (int) capacity());
    }

    public InputStream getInputStream() {
        if (this.root.file != null && this.root.buffer == null) {
            try {
                return new RandomFileInputStream();
            } catch (IOException e2) {
                return null;
            }
        }
        return new ByteArrayInputStream(array(), (int) arrayOffset(), (int) capacity());
    }

    public ModelByteBuffer subbuffer(long j2) {
        return subbuffer(j2, capacity());
    }

    public ModelByteBuffer subbuffer(long j2, long j3) {
        return subbuffer(j2, j3, false);
    }

    public ModelByteBuffer subbuffer(long j2, long j3, boolean z2) {
        return new ModelByteBuffer(this, j2, j3, z2);
    }

    public byte[] array() {
        return this.root.buffer;
    }

    public long arrayOffset() {
        if (this.root != this) {
            return this.root.arrayOffset() + this.offset;
        }
        return this.offset;
    }

    public long capacity() {
        return this.len;
    }

    public ModelByteBuffer getRoot() {
        return this.root;
    }

    public File getFile() {
        return this.file;
    }

    public long getFilePointer() {
        return this.fileoffset;
    }

    public static void loadAll(Collection<ModelByteBuffer> collection) throws IOException {
        File file = null;
        RandomAccessFile randomAccessFile = null;
        try {
            Iterator<ModelByteBuffer> it = collection.iterator();
            while (it.hasNext()) {
                ModelByteBuffer modelByteBuffer = it.next().root;
                if (modelByteBuffer.file != null && modelByteBuffer.buffer == null) {
                    if (file == null || !file.equals(modelByteBuffer.file)) {
                        if (randomAccessFile != null) {
                            randomAccessFile.close();
                        }
                        file = modelByteBuffer.file;
                        randomAccessFile = new RandomAccessFile(modelByteBuffer.file, InternalZipConstants.READ_MODE);
                    }
                    randomAccessFile.seek(modelByteBuffer.fileoffset);
                    byte[] bArr = new byte[(int) modelByteBuffer.capacity()];
                    int i2 = 0;
                    int length = bArr.length;
                    while (i2 != length) {
                        if (length - i2 > 65536) {
                            randomAccessFile.readFully(bArr, i2, 65536);
                            i2 += 65536;
                        } else {
                            randomAccessFile.readFully(bArr, i2, length - i2);
                            i2 = length;
                        }
                    }
                    modelByteBuffer.buffer = bArr;
                    modelByteBuffer.offset = 0L;
                }
            }
        } finally {
            if (randomAccessFile != null) {
                randomAccessFile.close();
            }
        }
    }

    public void load() throws IOException {
        if (this.root != this) {
            this.root.load();
            return;
        }
        if (this.buffer != null) {
            return;
        }
        if (this.file == null) {
            throw new IllegalStateException("No file associated with this ByteBuffer!");
        }
        DataInputStream dataInputStream = new DataInputStream(getInputStream());
        this.buffer = new byte[(int) capacity()];
        this.offset = 0L;
        dataInputStream.readFully(this.buffer);
        dataInputStream.close();
    }

    public void unload() {
        if (this.root != this) {
            this.root.unload();
        } else {
            if (this.file == null) {
                throw new IllegalStateException("No file associated with this ByteBuffer!");
            }
            this.root.buffer = null;
        }
    }
}
