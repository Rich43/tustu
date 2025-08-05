package com.sun.xml.internal.ws.util;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: rt.jar:com/sun/xml/internal/ws/util/ReadAllStream.class */
public class ReadAllStream extends InputStream {

    @NotNull
    private final MemoryStream memStream = new MemoryStream();

    @NotNull
    private final FileStream fileStream = new FileStream();
    private boolean readAll;
    private boolean closed;
    private static final Logger LOGGER;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ReadAllStream.class.desiredAssertionStatus();
        LOGGER = Logger.getLogger(ReadAllStream.class.getName());
    }

    public void readAll(InputStream in, long inMemory) throws IOException {
        if (!$assertionsDisabled && this.readAll) {
            throw new AssertionError();
        }
        this.readAll = true;
        boolean eof = this.memStream.readAll(in, inMemory);
        if (!eof) {
            this.fileStream.readAll(in);
        }
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        int ch = this.memStream.read();
        if (ch == -1) {
            ch = this.fileStream.read();
        }
        return ch;
    }

    @Override // java.io.InputStream
    public int read(byte[] b2, int off, int sz) throws IOException {
        int len = this.memStream.read(b2, off, sz);
        if (len == -1) {
            len = this.fileStream.read(b2, off, sz);
        }
        return len;
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (!this.closed) {
            this.memStream.close();
            this.fileStream.close();
            this.closed = true;
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/util/ReadAllStream$FileStream.class */
    private static class FileStream extends InputStream {

        @Nullable
        private File tempFile;

        @Nullable
        private FileInputStream fin;

        private FileStream() {
        }

        void readAll(InputStream in) throws IOException {
            this.tempFile = File.createTempFile("jaxws", ".bin");
            FileOutputStream fileOut = new FileOutputStream(this.tempFile);
            try {
                byte[] buf = new byte[8192];
                while (true) {
                    int len = in.read(buf);
                    if (len != -1) {
                        fileOut.write(buf, 0, len);
                    } else {
                        this.fin = new FileInputStream(this.tempFile);
                        return;
                    }
                }
            } finally {
                fileOut.close();
            }
        }

        @Override // java.io.InputStream
        public int read() throws IOException {
            if (this.fin != null) {
                return this.fin.read();
            }
            return -1;
        }

        @Override // java.io.InputStream
        public int read(byte[] b2, int off, int sz) throws IOException {
            if (this.fin != null) {
                return this.fin.read(b2, off, sz);
            }
            return -1;
        }

        @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            if (this.fin != null) {
                this.fin.close();
            }
            if (this.tempFile != null) {
                boolean success = this.tempFile.delete();
                if (!success) {
                    ReadAllStream.LOGGER.log(Level.INFO, "File {0} could not be deleted", this.tempFile);
                }
            }
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/util/ReadAllStream$MemoryStream.class */
    private static class MemoryStream extends InputStream {
        private Chunk head;
        private Chunk tail;
        private int curOff;

        private MemoryStream() {
        }

        private void add(byte[] buf, int len) {
            if (this.tail != null) {
                this.tail = this.tail.createNext(buf, 0, len);
                return;
            }
            Chunk chunk = new Chunk(buf, 0, len);
            this.tail = chunk;
            this.head = chunk;
        }

        boolean readAll(InputStream in, long inMemory) throws IOException {
            long total = 0;
            do {
                byte[] buf = new byte[8192];
                int read = fill(in, buf);
                total += read;
                if (read != 0) {
                    add(buf, read);
                }
                if (read != buf.length) {
                    return true;
                }
            } while (total <= inMemory);
            return false;
        }

        private int fill(InputStream in, byte[] buf) throws IOException {
            int total;
            int read;
            int i2 = 0;
            while (true) {
                total = i2;
                if (total >= buf.length || (read = in.read(buf, total, buf.length - total)) == -1) {
                    break;
                }
                i2 = total + read;
            }
            return total;
        }

        @Override // java.io.InputStream
        public int read() throws IOException {
            if (!fetch()) {
                return -1;
            }
            byte[] bArr = this.head.buf;
            int i2 = this.curOff;
            this.curOff = i2 + 1;
            return bArr[i2] & 255;
        }

        @Override // java.io.InputStream
        public int read(byte[] b2, int off, int sz) throws IOException {
            if (!fetch()) {
                return -1;
            }
            int sz2 = Math.min(sz, this.head.len - (this.curOff - this.head.off));
            System.arraycopy(this.head.buf, this.curOff, b2, off, sz2);
            this.curOff += sz2;
            return sz2;
        }

        private boolean fetch() {
            if (this.head == null) {
                return false;
            }
            if (this.curOff == this.head.off + this.head.len) {
                this.head = this.head.next;
                if (this.head == null) {
                    return false;
                }
                this.curOff = this.head.off;
                return true;
            }
            return true;
        }

        /* loaded from: rt.jar:com/sun/xml/internal/ws/util/ReadAllStream$MemoryStream$Chunk.class */
        private static final class Chunk {
            Chunk next;
            final byte[] buf;
            final int off;
            final int len;

            public Chunk(byte[] buf, int off, int len) {
                this.buf = buf;
                this.off = off;
                this.len = len;
            }

            public Chunk createNext(byte[] buf, int off, int len) {
                Chunk chunk = new Chunk(buf, off, len);
                this.next = chunk;
                return chunk;
            }
        }
    }
}
