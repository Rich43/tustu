package javax.imageio.stream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/* loaded from: rt.jar:javax/imageio/stream/MemoryCache.class */
class MemoryCache {
    private static final int BUFFER_LENGTH = 8192;
    private ArrayList cache = new ArrayList();
    private long cacheStart = 0;
    private long length = 0;

    MemoryCache() {
    }

    private byte[] getCacheBlock(long j2) throws IOException {
        long j3 = j2 - this.cacheStart;
        if (j3 > 2147483647L) {
            throw new IOException("Cache addressing limit exceeded!");
        }
        return (byte[]) this.cache.get((int) j3);
    }

    public long loadFromStream(InputStream inputStream, long j2) throws IOException {
        if (j2 < this.length) {
            return j2;
        }
        int i2 = (int) (this.length % 8192);
        byte[] cacheBlock = null;
        long j3 = j2 - this.length;
        if (i2 != 0) {
            cacheBlock = getCacheBlock(this.length / 8192);
        }
        while (j3 > 0) {
            if (cacheBlock == null) {
                try {
                    cacheBlock = new byte[8192];
                    i2 = 0;
                } catch (OutOfMemoryError e2) {
                    throw new IOException("No memory left for cache!");
                }
            }
            int i3 = inputStream.read(cacheBlock, i2, (int) Math.min(j3, 8192 - i2));
            if (i3 == -1) {
                return this.length;
            }
            if (i2 == 0) {
                this.cache.add(cacheBlock);
            }
            j3 -= i3;
            this.length += i3;
            i2 += i3;
            if (i2 >= 8192) {
                cacheBlock = null;
            }
        }
        return j2;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void writeToStream(OutputStream outputStream, long j2, long j3) throws IOException {
        if (j2 + j3 > this.length) {
            throw new IndexOutOfBoundsException("Argument out of cache");
        }
        if (j2 < 0 || j3 < 0) {
            throw new IndexOutOfBoundsException("Negative pos or len");
        }
        if (j3 == 0) {
            return;
        }
        long j4 = j2 / 8192;
        if (j4 < this.cacheStart) {
            throw new IndexOutOfBoundsException("pos already disposed");
        }
        int i2 = (int) (j2 % 8192);
        long j5 = j4 + 1;
        byte[] cacheBlock = getCacheBlock(this);
        while (j3 > 0) {
            if (cacheBlock == null) {
                j5++;
                cacheBlock = getCacheBlock(this);
                i2 = 0;
            }
            int iMin = (int) Math.min(j3, 8192 - i2);
            outputStream.write(cacheBlock, i2, iMin);
            cacheBlock = null;
            j3 -= iMin;
        }
    }

    private void pad(long j2) throws IOException {
        long size = (j2 / 8192) - ((this.cacheStart + this.cache.size()) - 1);
        long j3 = 0;
        while (true) {
            long j4 = j3;
            if (j4 < size) {
                try {
                    this.cache.add(new byte[8192]);
                    j3 = j4 + 1;
                } catch (OutOfMemoryError e2) {
                    throw new IOException("No memory left for cache!");
                }
            } else {
                return;
            }
        }
    }

    public void write(byte[] bArr, int i2, int i3, long j2) throws IOException {
        if (bArr == null) {
            throw new NullPointerException("b == null!");
        }
        if (i2 < 0 || i3 < 0 || j2 < 0 || i2 + i3 > bArr.length || i2 + i3 < 0) {
            throw new IndexOutOfBoundsException();
        }
        long j3 = (j2 + i3) - 1;
        if (j3 >= this.length) {
            pad(j3);
            this.length = j3 + 1;
        }
        int i4 = (int) (j2 % 8192);
        while (true) {
            int i5 = i4;
            if (i3 > 0) {
                byte[] cacheBlock = getCacheBlock(j2 / 8192);
                int iMin = Math.min(i3, 8192 - i5);
                System.arraycopy(bArr, i2, cacheBlock, i5, iMin);
                j2 += iMin;
                i2 += iMin;
                i3 -= iMin;
                i4 = 0;
            } else {
                return;
            }
        }
    }

    public void write(int i2, long j2) throws IOException {
        if (j2 < 0) {
            throw new ArrayIndexOutOfBoundsException("pos < 0");
        }
        if (j2 >= this.length) {
            pad(j2);
            this.length = j2 + 1;
        }
        getCacheBlock(j2 / 8192)[(int) (j2 % 8192)] = (byte) i2;
    }

    public long getLength() {
        return this.length;
    }

    public int read(long j2) throws IOException {
        byte[] cacheBlock;
        if (j2 >= this.length || (cacheBlock = getCacheBlock(j2 / 8192)) == null) {
            return -1;
        }
        return cacheBlock[(int) (j2 % 8192)] & 255;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void read(byte[] bArr, int i2, int i3, long j2) throws IOException {
        if (bArr == null) {
            throw new NullPointerException("b == null!");
        }
        if (i2 < 0 || i3 < 0 || j2 < 0 || i2 + i3 > bArr.length || i2 + i3 < 0) {
            throw new IndexOutOfBoundsException();
        }
        if (j2 + i3 > this.length) {
            throw new IndexOutOfBoundsException();
        }
        long j3 = j2 / 8192;
        int i4 = ((int) j2) % 8192;
        while (true) {
            int i5 = i4;
            if (i3 > 0) {
                int iMin = Math.min(i3, 8192 - i5);
                long j4 = j3;
                j3 = this + 1;
                System.arraycopy(getCacheBlock(j4), i5, bArr, i2, iMin);
                i3 -= iMin;
                i2 += iMin;
                i4 = 0;
            } else {
                return;
            }
        }
    }

    public void disposeBefore(long j2) {
        long j3 = j2 / 8192;
        if (j3 < this.cacheStart) {
            throw new IndexOutOfBoundsException("pos already disposed");
        }
        long jMin = Math.min(j3 - this.cacheStart, this.cache.size());
        long j4 = 0;
        while (true) {
            long j5 = j4;
            if (j5 < jMin) {
                this.cache.remove(0);
                j4 = j5 + 1;
            } else {
                this.cacheStart = j3;
                return;
            }
        }
    }

    public void reset() {
        this.cache.clear();
        this.cacheStart = 0L;
        this.length = 0L;
    }
}
