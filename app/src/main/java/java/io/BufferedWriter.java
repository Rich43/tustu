package java.io;

import java.security.AccessController;
import sun.security.action.GetPropertyAction;

/* loaded from: rt.jar:java/io/BufferedWriter.class */
public class BufferedWriter extends Writer {
    private Writer out;
    private char[] cb;
    private int nChars;
    private int nextChar;
    private static int defaultCharBufferSize = 8192;
    private String lineSeparator;

    public BufferedWriter(Writer writer) {
        this(writer, defaultCharBufferSize);
    }

    public BufferedWriter(Writer writer, int i2) {
        super(writer);
        if (i2 <= 0) {
            throw new IllegalArgumentException("Buffer size <= 0");
        }
        this.out = writer;
        this.cb = new char[i2];
        this.nChars = i2;
        this.nextChar = 0;
        this.lineSeparator = (String) AccessController.doPrivileged(new GetPropertyAction("line.separator"));
    }

    private void ensureOpen() throws IOException {
        if (this.out == null) {
            throw new IOException("Stream closed");
        }
    }

    void flushBuffer() throws IOException {
        synchronized (this.lock) {
            ensureOpen();
            if (this.nextChar == 0) {
                return;
            }
            this.out.write(this.cb, 0, this.nextChar);
            this.nextChar = 0;
        }
    }

    @Override // java.io.Writer
    public void write(int i2) throws IOException {
        synchronized (this.lock) {
            ensureOpen();
            if (this.nextChar >= this.nChars) {
                flushBuffer();
            }
            char[] cArr = this.cb;
            int i3 = this.nextChar;
            this.nextChar = i3 + 1;
            cArr[i3] = (char) i2;
        }
    }

    private int min(int i2, int i3) {
        return i2 < i3 ? i2 : i3;
    }

    @Override // java.io.Writer
    public void write(char[] cArr, int i2, int i3) throws IOException {
        synchronized (this.lock) {
            ensureOpen();
            if (i2 < 0 || i2 > cArr.length || i3 < 0 || i2 + i3 > cArr.length || i2 + i3 < 0) {
                throw new IndexOutOfBoundsException();
            }
            if (i3 == 0) {
                return;
            }
            if (i3 >= this.nChars) {
                flushBuffer();
                this.out.write(cArr, i2, i3);
                return;
            }
            int i4 = i2;
            int i5 = i2 + i3;
            while (i4 < i5) {
                int iMin = min(this.nChars - this.nextChar, i5 - i4);
                System.arraycopy(cArr, i4, this.cb, this.nextChar, iMin);
                i4 += iMin;
                this.nextChar += iMin;
                if (this.nextChar >= this.nChars) {
                    flushBuffer();
                }
            }
        }
    }

    @Override // java.io.Writer
    public void write(String str, int i2, int i3) throws IOException {
        synchronized (this.lock) {
            ensureOpen();
            int i4 = i2;
            int i5 = i2 + i3;
            while (i4 < i5) {
                int iMin = min(this.nChars - this.nextChar, i5 - i4);
                str.getChars(i4, i4 + iMin, this.cb, this.nextChar);
                i4 += iMin;
                this.nextChar += iMin;
                if (this.nextChar >= this.nChars) {
                    flushBuffer();
                }
            }
        }
    }

    public void newLine() throws IOException {
        write(this.lineSeparator);
    }

    @Override // java.io.Writer, java.io.Flushable
    public void flush() throws IOException {
        synchronized (this.lock) {
            flushBuffer();
            this.out.flush();
        }
    }

    /* JADX WARN: Finally extract failed */
    @Override // java.io.Writer, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        synchronized (this.lock) {
            if (this.out == null) {
                return;
            }
            try {
                Writer writer = this.out;
                Throwable th = null;
                try {
                    try {
                        flushBuffer();
                        if (writer != null) {
                            if (0 != 0) {
                                try {
                                    writer.close();
                                } catch (Throwable th2) {
                                    th.addSuppressed(th2);
                                }
                            } else {
                                writer.close();
                            }
                        }
                        this.out = null;
                        this.cb = null;
                    } catch (Throwable th3) {
                        if (writer != null) {
                            if (th != null) {
                                try {
                                    writer.close();
                                } catch (Throwable th4) {
                                    th.addSuppressed(th4);
                                }
                            } else {
                                writer.close();
                            }
                        }
                        throw th3;
                    }
                } finally {
                }
            } catch (Throwable th5) {
                this.out = null;
                this.cb = null;
                throw th5;
            }
        }
    }
}
