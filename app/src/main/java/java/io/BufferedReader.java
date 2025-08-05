package java.io;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/* loaded from: rt.jar:java/io/BufferedReader.class */
public class BufferedReader extends Reader {
    private Reader in;
    private char[] cb;
    private int nChars;
    private int nextChar;
    private static final int INVALIDATED = -2;
    private static final int UNMARKED = -1;
    private int markedChar;
    private int readAheadLimit;
    private boolean skipLF;
    private boolean markedSkipLF;
    private static int defaultCharBufferSize = 8192;
    private static int defaultExpectedLineLength = 80;

    public BufferedReader(Reader reader, int i2) {
        super(reader);
        this.markedChar = -1;
        this.readAheadLimit = 0;
        this.skipLF = false;
        this.markedSkipLF = false;
        if (i2 <= 0) {
            throw new IllegalArgumentException("Buffer size <= 0");
        }
        this.in = reader;
        this.cb = new char[i2];
        this.nChars = 0;
        this.nextChar = 0;
    }

    public BufferedReader(Reader reader) {
        this(reader, defaultCharBufferSize);
    }

    private void ensureOpen() throws IOException {
        if (this.in == null) {
            throw new IOException("Stream closed");
        }
    }

    private void fill() throws IOException {
        int i2;
        int i3;
        if (this.markedChar <= -1) {
            i2 = 0;
        } else {
            int i4 = this.nextChar - this.markedChar;
            if (i4 >= this.readAheadLimit) {
                this.markedChar = -2;
                this.readAheadLimit = 0;
                i2 = 0;
            } else {
                if (this.readAheadLimit <= this.cb.length) {
                    System.arraycopy(this.cb, this.markedChar, this.cb, 0, i4);
                    this.markedChar = 0;
                    i2 = i4;
                } else {
                    char[] cArr = new char[this.readAheadLimit];
                    System.arraycopy(this.cb, this.markedChar, cArr, 0, i4);
                    this.cb = cArr;
                    this.markedChar = 0;
                    i2 = i4;
                }
                this.nChars = i4;
                this.nextChar = i4;
            }
        }
        do {
            i3 = this.in.read(this.cb, i2, this.cb.length - i2);
        } while (i3 == 0);
        if (i3 > 0) {
            this.nChars = i2 + i3;
            this.nextChar = i2;
        }
    }

    @Override // java.io.Reader
    public int read() throws IOException {
        synchronized (this.lock) {
            ensureOpen();
            while (true) {
                if (this.nextChar >= this.nChars) {
                    fill();
                    if (this.nextChar >= this.nChars) {
                        return -1;
                    }
                }
                if (!this.skipLF) {
                    break;
                }
                this.skipLF = false;
                if (this.cb[this.nextChar] != '\n') {
                    break;
                }
                this.nextChar++;
            }
            char[] cArr = this.cb;
            int i2 = this.nextChar;
            this.nextChar = i2 + 1;
            return cArr[i2];
        }
    }

    private int read1(char[] cArr, int i2, int i3) throws IOException {
        if (this.nextChar >= this.nChars) {
            if (i3 >= this.cb.length && this.markedChar <= -1 && !this.skipLF) {
                return this.in.read(cArr, i2, i3);
            }
            fill();
        }
        if (this.nextChar >= this.nChars) {
            return -1;
        }
        if (this.skipLF) {
            this.skipLF = false;
            if (this.cb[this.nextChar] == '\n') {
                this.nextChar++;
                if (this.nextChar >= this.nChars) {
                    fill();
                }
                if (this.nextChar >= this.nChars) {
                    return -1;
                }
            }
        }
        int iMin = Math.min(i3, this.nChars - this.nextChar);
        System.arraycopy(this.cb, this.nextChar, cArr, i2, iMin);
        this.nextChar += iMin;
        return iMin;
    }

    @Override // java.io.Reader
    public int read(char[] cArr, int i2, int i3) throws IOException {
        int i4;
        synchronized (this.lock) {
            ensureOpen();
            if (i2 < 0 || i2 > cArr.length || i3 < 0 || i2 + i3 > cArr.length || i2 + i3 < 0) {
                throw new IndexOutOfBoundsException();
            }
            if (i3 == 0) {
                return 0;
            }
            int i5 = read1(cArr, i2, i3);
            if (i5 <= 0) {
                return i5;
            }
            while (i5 < i3 && this.in.ready() && (i4 = read1(cArr, i2 + i5, i3 - i5)) > 0) {
                i5 += i4;
            }
            return i5;
        }
    }

    String readLine(boolean z2) throws IOException {
        String string;
        StringBuilder sb = null;
        synchronized (this.lock) {
            ensureOpen();
            boolean z3 = z2 || this.skipLF;
            while (true) {
                if (this.nextChar >= this.nChars) {
                    fill();
                }
                if (this.nextChar >= this.nChars) {
                    if (sb != null && sb.length() > 0) {
                        return sb.toString();
                    }
                    return null;
                }
                boolean z4 = false;
                char c2 = 0;
                if (z3 && this.cb[this.nextChar] == '\n') {
                    this.nextChar++;
                }
                this.skipLF = false;
                z3 = false;
                int i2 = this.nextChar;
                while (i2 < this.nChars) {
                    c2 = this.cb[i2];
                    if (c2 != '\n' && c2 != '\r') {
                        i2++;
                    } else {
                        z4 = true;
                        break;
                    }
                }
                int i3 = this.nextChar;
                this.nextChar = i2;
                if (z4) {
                    if (sb == null) {
                        string = new String(this.cb, i3, i2 - i3);
                    } else {
                        sb.append(this.cb, i3, i2 - i3);
                        string = sb.toString();
                    }
                    this.nextChar++;
                    if (c2 == '\r') {
                        this.skipLF = true;
                    }
                    return string;
                }
                if (sb == null) {
                    sb = new StringBuilder(defaultExpectedLineLength);
                }
                sb.append(this.cb, i3, i2 - i3);
            }
        }
    }

    public String readLine() throws IOException {
        return readLine(false);
    }

    @Override // java.io.Reader
    public long skip(long j2) throws IOException {
        long j3;
        if (j2 < 0) {
            throw new IllegalArgumentException("skip value is negative");
        }
        synchronized (this.lock) {
            ensureOpen();
            long j4 = j2;
            while (true) {
                if (j4 > 0) {
                    if (this.nextChar >= this.nChars) {
                        fill();
                    }
                    if (this.nextChar >= this.nChars) {
                        break;
                    }
                    if (this.skipLF) {
                        this.skipLF = false;
                        if (this.cb[this.nextChar] == '\n') {
                            this.nextChar++;
                        }
                    }
                    long j5 = this.nChars - this.nextChar;
                    if (j4 <= j5) {
                        this.nextChar = (int) (this.nextChar + j4);
                        j4 = 0;
                        break;
                    }
                    j4 -= j5;
                    this.nextChar = this.nChars;
                } else {
                    break;
                }
            }
            j3 = j2 - j4;
        }
        return j3;
    }

    @Override // java.io.Reader
    public boolean ready() throws IOException {
        boolean z2;
        synchronized (this.lock) {
            ensureOpen();
            if (this.skipLF) {
                if (this.nextChar >= this.nChars && this.in.ready()) {
                    fill();
                }
                if (this.nextChar < this.nChars) {
                    if (this.cb[this.nextChar] == '\n') {
                        this.nextChar++;
                    }
                    this.skipLF = false;
                }
            }
            z2 = this.nextChar < this.nChars || this.in.ready();
        }
        return z2;
    }

    @Override // java.io.Reader
    public boolean markSupported() {
        return true;
    }

    @Override // java.io.Reader
    public void mark(int i2) throws IOException {
        if (i2 < 0) {
            throw new IllegalArgumentException("Read-ahead limit < 0");
        }
        synchronized (this.lock) {
            ensureOpen();
            this.readAheadLimit = i2;
            this.markedChar = this.nextChar;
            this.markedSkipLF = this.skipLF;
        }
    }

    @Override // java.io.Reader
    public void reset() throws IOException {
        synchronized (this.lock) {
            ensureOpen();
            if (this.markedChar < 0) {
                throw new IOException(this.markedChar == -2 ? "Mark invalid" : "Stream not marked");
            }
            this.nextChar = this.markedChar;
            this.skipLF = this.markedSkipLF;
        }
    }

    @Override // java.io.Reader, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        synchronized (this.lock) {
            if (this.in == null) {
                return;
            }
            try {
                this.in.close();
                this.in = null;
                this.cb = null;
            } catch (Throwable th) {
                this.in = null;
                this.cb = null;
                throw th;
            }
        }
    }

    public Stream<String> lines() {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(new Iterator<String>() { // from class: java.io.BufferedReader.1
            String nextLine = null;

            @Override // java.util.Iterator
            public boolean hasNext() {
                if (this.nextLine != null) {
                    return true;
                }
                try {
                    this.nextLine = BufferedReader.this.readLine();
                    return this.nextLine != null;
                } catch (IOException e2) {
                    throw new UncheckedIOException(e2);
                }
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.Iterator
            public String next() {
                if (this.nextLine != null || hasNext()) {
                    String str = this.nextLine;
                    this.nextLine = null;
                    return str;
                }
                throw new NoSuchElementException();
            }
        }, 272), false);
    }
}
