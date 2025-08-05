package java.io;

/* loaded from: rt.jar:java/io/LineNumberReader.class */
public class LineNumberReader extends BufferedReader {
    private int lineNumber;
    private int markedLineNumber;
    private boolean skipLF;
    private boolean markedSkipLF;
    private static final int maxSkipBufferSize = 8192;
    private char[] skipBuffer;

    public LineNumberReader(Reader reader) {
        super(reader);
        this.lineNumber = 0;
        this.skipBuffer = null;
    }

    public LineNumberReader(Reader reader, int i2) {
        super(reader, i2);
        this.lineNumber = 0;
        this.skipBuffer = null;
    }

    public void setLineNumber(int i2) {
        this.lineNumber = i2;
    }

    public int getLineNumber() {
        return this.lineNumber;
    }

    @Override // java.io.BufferedReader, java.io.Reader
    public int read() throws IOException {
        synchronized (this.lock) {
            int i2 = super.read();
            if (this.skipLF) {
                if (i2 == 10) {
                    i2 = super.read();
                }
                this.skipLF = false;
            }
            switch (i2) {
                case 10:
                    break;
                case 13:
                    this.skipLF = true;
                    break;
                default:
                    return i2;
            }
            this.lineNumber++;
            return 10;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x0039  */
    @Override // java.io.BufferedReader, java.io.Reader
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public int read(char[] r6, int r7, int r8) throws java.io.IOException {
        /*
            r5 = this;
            r0 = r5
            java.lang.Object r0 = r0.lock
            r1 = r0
            r9 = r1
            monitor-enter(r0)
            r0 = r5
            r1 = r6
            r2 = r7
            r3 = r8
            int r0 = super.read(r1, r2, r3)     // Catch: java.lang.Throwable -> L6f
            r10 = r0
            r0 = r7
            r11 = r0
        L14:
            r0 = r11
            r1 = r7
            r2 = r10
            int r1 = r1 + r2
            if (r0 >= r1) goto L69
            r0 = r6
            r1 = r11
            char r0 = r0[r1]     // Catch: java.lang.Throwable -> L6f
            r12 = r0
            r0 = r5
            boolean r0 = r0.skipLF     // Catch: java.lang.Throwable -> L6f
            if (r0 == 0) goto L39
            r0 = r5
            r1 = 0
            r0.skipLF = r1     // Catch: java.lang.Throwable -> L6f
            r0 = r12
            r1 = 10
            if (r0 != r1) goto L39
            goto L63
        L39:
            r0 = r12
            switch(r0) {
                case 10: goto L59;
                case 13: goto L54;
                default: goto L63;
            }     // Catch: java.lang.Throwable -> L6f
        L54:
            r0 = r5
            r1 = 1
            r0.skipLF = r1     // Catch: java.lang.Throwable -> L6f
        L59:
            r0 = r5
            r1 = r0
            int r1 = r1.lineNumber     // Catch: java.lang.Throwable -> L6f
            r2 = 1
            int r1 = r1 + r2
            r0.lineNumber = r1     // Catch: java.lang.Throwable -> L6f
        L63:
            int r11 = r11 + 1
            goto L14
        L69:
            r0 = r10
            r1 = r9
            monitor-exit(r1)     // Catch: java.lang.Throwable -> L6f
            return r0
        L6f:
            r13 = move-exception
            r0 = r9
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L6f
            r0 = r13
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: java.io.LineNumberReader.read(char[], int, int):int");
    }

    @Override // java.io.BufferedReader
    public String readLine() throws IOException {
        String line;
        synchronized (this.lock) {
            line = super.readLine(this.skipLF);
            this.skipLF = false;
            if (line != null) {
                this.lineNumber++;
            }
        }
        return line;
    }

    @Override // java.io.BufferedReader, java.io.Reader
    public long skip(long j2) throws IOException {
        long j3;
        int i2;
        if (j2 < 0) {
            throw new IllegalArgumentException("skip() value is negative");
        }
        int iMin = (int) Math.min(j2, 8192L);
        synchronized (this.lock) {
            if (this.skipBuffer == null || this.skipBuffer.length < iMin) {
                this.skipBuffer = new char[iMin];
            }
            long j4 = j2;
            while (j4 > 0 && (i2 = read(this.skipBuffer, 0, (int) Math.min(j4, iMin))) != -1) {
                j4 -= i2;
            }
            j3 = j2 - j4;
        }
        return j3;
    }

    @Override // java.io.BufferedReader, java.io.Reader
    public void mark(int i2) throws IOException {
        synchronized (this.lock) {
            if (this.skipLF) {
                i2++;
            }
            super.mark(i2);
            this.markedLineNumber = this.lineNumber;
            this.markedSkipLF = this.skipLF;
        }
    }

    @Override // java.io.BufferedReader, java.io.Reader
    public void reset() throws IOException {
        synchronized (this.lock) {
            super.reset();
            this.lineNumber = this.markedLineNumber;
            this.skipLF = this.markedSkipLF;
        }
    }
}
