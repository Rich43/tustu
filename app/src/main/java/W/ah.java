package W;

import java.io.IOException;
import java.io.Reader;

/* loaded from: TunerStudioMS.jar:W/ah.class */
public class ah extends Reader {

    /* renamed from: a, reason: collision with root package name */
    private Reader f2076a;

    /* renamed from: b, reason: collision with root package name */
    private char[] f2077b;

    /* renamed from: c, reason: collision with root package name */
    private int f2078c;

    /* renamed from: d, reason: collision with root package name */
    private int f2079d;

    /* renamed from: e, reason: collision with root package name */
    private int f2080e;

    /* renamed from: f, reason: collision with root package name */
    private int f2081f;

    /* renamed from: g, reason: collision with root package name */
    private boolean f2082g;

    /* renamed from: h, reason: collision with root package name */
    private boolean f2083h;

    /* renamed from: i, reason: collision with root package name */
    private static int f2084i = 8192;

    /* renamed from: j, reason: collision with root package name */
    private static int f2085j = 80;

    public ah(Reader reader, int i2) {
        super(reader);
        this.f2080e = -1;
        this.f2081f = 0;
        this.f2082g = false;
        this.f2083h = false;
        if (i2 <= 0) {
            throw new IllegalArgumentException("Buffer size <= 0");
        }
        this.f2076a = reader;
        this.f2077b = new char[i2];
        this.f2078c = 0;
        this.f2079d = 0;
    }

    public ah(Reader reader) {
        this(reader, f2084i);
    }

    private void b() throws IOException {
        if (this.f2076a == null) {
            throw new IOException("Stream closed");
        }
    }

    private void c() throws IOException {
        int i2;
        int i3;
        if (this.f2080e <= -1) {
            i2 = 0;
        } else {
            int i4 = this.f2079d - this.f2080e;
            if (i4 >= this.f2081f) {
                this.f2080e = -2;
                this.f2081f = 0;
                i2 = 0;
            } else {
                if (this.f2081f <= this.f2077b.length) {
                    System.arraycopy(this.f2077b, this.f2080e, this.f2077b, 0, i4);
                    this.f2080e = 0;
                    i2 = i4;
                } else {
                    char[] cArr = new char[this.f2081f];
                    System.arraycopy(this.f2077b, this.f2080e, cArr, 0, i4);
                    this.f2077b = cArr;
                    this.f2080e = 0;
                    i2 = i4;
                }
                this.f2078c = i4;
                this.f2079d = i4;
            }
        }
        do {
            i3 = this.f2076a.read(this.f2077b, i2, this.f2077b.length - i2);
        } while (i3 == 0);
        if (i3 > 0) {
            this.f2078c = i2 + i3;
            this.f2079d = i2;
        }
    }

    @Override // java.io.Reader
    public int read() {
        synchronized (this.lock) {
            b();
            while (true) {
                if (this.f2079d >= this.f2078c) {
                    c();
                    if (this.f2079d >= this.f2078c) {
                        return -1;
                    }
                }
                if (!this.f2082g) {
                    break;
                }
                this.f2082g = false;
                if (this.f2077b[this.f2079d] != '\n') {
                    break;
                }
                this.f2079d++;
            }
            char[] cArr = this.f2077b;
            int i2 = this.f2079d;
            this.f2079d = i2 + 1;
            return cArr[i2];
        }
    }

    private int a(char[] cArr, int i2, int i3) throws IOException {
        if (this.f2079d >= this.f2078c) {
            if (i3 >= this.f2077b.length && this.f2080e <= -1 && !this.f2082g) {
                return this.f2076a.read(cArr, i2, i3);
            }
            c();
        }
        if (this.f2079d >= this.f2078c) {
            return -1;
        }
        if (this.f2082g) {
            this.f2082g = false;
            if (this.f2077b[this.f2079d] == '\n') {
                this.f2079d++;
                if (this.f2079d >= this.f2078c) {
                    c();
                }
                if (this.f2079d >= this.f2078c) {
                    return -1;
                }
            }
        }
        int iMin = Math.min(i3, this.f2078c - this.f2079d);
        System.arraycopy(this.f2077b, this.f2079d, cArr, i2, iMin);
        this.f2079d += iMin;
        return iMin;
    }

    @Override // java.io.Reader
    public int read(char[] cArr, int i2, int i3) {
        int iA;
        synchronized (this.lock) {
            b();
            if (i2 < 0 || i2 > cArr.length || i3 < 0 || i2 + i3 > cArr.length || i2 + i3 < 0) {
                throw new IndexOutOfBoundsException();
            }
            if (i3 == 0) {
                return 0;
            }
            int iA2 = a(cArr, i2, i3);
            if (iA2 <= 0) {
                return iA2;
            }
            while (iA2 < i3 && this.f2076a.ready() && (iA = a(cArr, i2 + iA2, i3 - iA2)) > 0) {
                iA2 += iA;
            }
            return iA2;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:41:0x00c4 A[PHI: r16
  0x00c4: PHI (r16v3 int) = (r16v1 int), (r16v4 int), (r16v4 int) binds: [B:35:0x009c, B:37:0x00aa, B:39:0x00b8] A[DONT_GENERATE, DONT_INLINE]] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    java.lang.String a(boolean r8) {
        /*
            Method dump skipped, instructions count: 350
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: W.ah.a(boolean):java.lang.String");
    }

    public String a() {
        return a(false);
    }

    @Override // java.io.Reader
    public long skip(long j2) {
        long j3;
        if (j2 < 0) {
            throw new IllegalArgumentException("skip value is negative");
        }
        synchronized (this.lock) {
            b();
            long j4 = j2;
            while (true) {
                if (j4 > 0) {
                    if (this.f2079d >= this.f2078c) {
                        c();
                    }
                    if (this.f2079d >= this.f2078c) {
                        break;
                    }
                    if (this.f2082g) {
                        this.f2082g = false;
                        if (this.f2077b[this.f2079d] == '\n') {
                            this.f2079d++;
                        }
                    }
                    long j5 = this.f2078c - this.f2079d;
                    if (j4 <= j5) {
                        this.f2079d = (int) (this.f2079d + j4);
                        j4 = 0;
                        break;
                    }
                    j4 -= j5;
                    this.f2079d = this.f2078c;
                } else {
                    break;
                }
            }
            j3 = j2 - j4;
        }
        return j3;
    }

    @Override // java.io.Reader
    public boolean ready() {
        boolean z2;
        synchronized (this.lock) {
            b();
            if (this.f2082g) {
                if (this.f2079d >= this.f2078c && this.f2076a.ready()) {
                    c();
                }
                if (this.f2079d < this.f2078c) {
                    if (this.f2077b[this.f2079d] == '\n') {
                        this.f2079d++;
                    }
                    this.f2082g = false;
                }
            }
            z2 = this.f2079d < this.f2078c || this.f2076a.ready();
        }
        return z2;
    }

    @Override // java.io.Reader
    public boolean markSupported() {
        return true;
    }

    @Override // java.io.Reader
    public void mark(int i2) {
        if (i2 < 0) {
            throw new IllegalArgumentException("Read-ahead limit < 0");
        }
        synchronized (this.lock) {
            b();
            this.f2081f = i2;
            this.f2080e = this.f2079d;
            this.f2083h = this.f2082g;
        }
    }

    @Override // java.io.Reader
    public void reset() {
        synchronized (this.lock) {
            b();
            if (this.f2080e < 0) {
                throw new IOException(this.f2080e == -2 ? "Mark invalid" : "Stream not marked");
            }
            this.f2079d = this.f2080e;
            this.f2082g = this.f2083h;
        }
    }

    @Override // java.io.Reader, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        synchronized (this.lock) {
            if (this.f2076a == null) {
                return;
            }
            this.f2076a.close();
            this.f2076a = null;
            this.f2077b = null;
        }
    }
}
