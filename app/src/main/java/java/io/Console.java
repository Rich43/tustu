package java.io;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Formatter;
import sun.misc.JavaIOAccess;
import sun.misc.SharedSecrets;
import sun.nio.cs.StreamDecoder;
import sun.nio.cs.StreamEncoder;

/* loaded from: rt.jar:java/io/Console.class */
public final class Console implements Flushable {
    private Object readLock;
    private Object writeLock;
    private Reader reader;
    private Writer out;
    private PrintWriter pw;
    private Formatter formatter;
    private Charset cs;
    private char[] rcb;
    private static boolean echoOff;
    private static Console cons;
    static final /* synthetic */ boolean $assertionsDisabled;

    private static native String encoding();

    /* JADX INFO: Access modifiers changed from: private */
    public static native boolean echo(boolean z2) throws IOException;

    /* JADX INFO: Access modifiers changed from: private */
    public static native boolean istty();

    static {
        $assertionsDisabled = !Console.class.desiredAssertionStatus();
        try {
            SharedSecrets.getJavaLangAccess().registerShutdownHook(0, false, new Runnable() { // from class: java.io.Console.1
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        if (Console.echoOff) {
                            Console.echo(true);
                        }
                    } catch (IOException e2) {
                    }
                }
            });
        } catch (IllegalStateException e2) {
        }
        SharedSecrets.setJavaIOAccess(new JavaIOAccess() { // from class: java.io.Console.2
            @Override // sun.misc.JavaIOAccess
            public Console console() {
                if (Console.istty()) {
                    if (Console.cons == null) {
                        Console unused = Console.cons = new Console();
                    }
                    return Console.cons;
                }
                return null;
            }

            @Override // sun.misc.JavaIOAccess
            public Charset charset() {
                return Console.cons.cs;
            }
        });
    }

    public PrintWriter writer() {
        return this.pw;
    }

    public Reader reader() {
        return this.reader;
    }

    public Console format(String str, Object... objArr) {
        this.formatter.format(str, objArr).flush();
        return this;
    }

    public Console printf(String str, Object... objArr) {
        return format(str, objArr);
    }

    public String readLine(String str, Object... objArr) {
        String str2 = null;
        synchronized (this.writeLock) {
            synchronized (this.readLock) {
                if (str.length() != 0) {
                    this.pw.format(str, objArr);
                }
                try {
                    char[] cArr = readline(false);
                    if (cArr != null) {
                        str2 = new String(cArr);
                    }
                } catch (IOException e2) {
                    throw new IOError(e2);
                }
            }
        }
        return str2;
    }

    public String readLine() {
        return readLine("", new Object[0]);
    }

    public char[] readPassword(String str, Object... objArr) {
        char[] cArr = null;
        synchronized (this.writeLock) {
            synchronized (this.readLock) {
                try {
                    echoOff = echo(false);
                    IOError iOError = null;
                    try {
                        try {
                            if (str.length() != 0) {
                                this.pw.format(str, objArr);
                            }
                            cArr = readline(true);
                            try {
                                echoOff = echo(true);
                            } catch (IOException e2) {
                                if (0 == 0) {
                                    iOError = new IOError(e2);
                                } else {
                                    iOError.addSuppressed(e2);
                                }
                            }
                            if (iOError != null) {
                                throw iOError;
                            }
                        } catch (IOException e3) {
                            IOError iOError2 = new IOError(e3);
                            try {
                                echoOff = echo(true);
                            } catch (IOException e4) {
                                if (iOError2 == null) {
                                    iOError2 = new IOError(e4);
                                } else {
                                    iOError2.addSuppressed(e4);
                                }
                            }
                            if (iOError2 != null) {
                                throw iOError2;
                            }
                        }
                        this.pw.println();
                    } catch (Throwable th) {
                        try {
                            echoOff = echo(true);
                        } catch (IOException e5) {
                            if (0 == 0) {
                                iOError = new IOError(e5);
                            } else {
                                iOError.addSuppressed(e5);
                            }
                        }
                        if (iOError != null) {
                            throw iOError;
                        }
                        throw th;
                    }
                } catch (IOException e6) {
                    throw new IOError(e6);
                }
            }
        }
        return cArr;
    }

    public char[] readPassword() {
        return readPassword("", new Object[0]);
    }

    @Override // java.io.Flushable
    public void flush() {
        this.pw.flush();
    }

    private char[] readline(boolean z2) throws IOException {
        int i2 = this.reader.read(this.rcb, 0, this.rcb.length);
        if (i2 < 0) {
            return null;
        }
        if (this.rcb[i2 - 1] == '\r') {
            i2--;
        } else if (this.rcb[i2 - 1] == '\n') {
            i2--;
            if (i2 > 0 && this.rcb[i2 - 1] == '\r') {
                i2--;
            }
        }
        char[] cArr = new char[i2];
        if (i2 > 0) {
            System.arraycopy(this.rcb, 0, cArr, 0, i2);
            if (z2) {
                Arrays.fill(this.rcb, 0, i2, ' ');
            }
        }
        return cArr;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public char[] grow() {
        if (!$assertionsDisabled && !Thread.holdsLock(this.readLock)) {
            throw new AssertionError();
        }
        char[] cArr = new char[this.rcb.length * 2];
        System.arraycopy(this.rcb, 0, cArr, 0, this.rcb.length);
        this.rcb = cArr;
        return this.rcb;
    }

    /* loaded from: rt.jar:java/io/Console$LineReader.class */
    class LineReader extends Reader {
        private Reader in;
        private char[] cb = new char[1024];
        private int nChars = 0;
        private int nextChar = 0;
        boolean leftoverLF = false;

        LineReader(Reader reader) {
            this.in = reader;
        }

        @Override // java.io.Reader, java.io.Closeable, java.lang.AutoCloseable
        public void close() {
        }

        @Override // java.io.Reader
        public boolean ready() throws IOException {
            return this.in.ready();
        }

        @Override // java.io.Reader
        public int read(char[] cArr, int i2, int i3) throws IOException {
            int i4;
            int i5 = i2;
            int length = i2 + i3;
            if (i2 >= 0 && i2 <= cArr.length && i3 >= 0 && length >= 0 && length <= cArr.length) {
                synchronized (Console.this.readLock) {
                    boolean z2 = false;
                    do {
                        if (this.nextChar >= this.nChars) {
                            do {
                                i4 = this.in.read(this.cb, 0, this.cb.length);
                            } while (i4 == 0);
                            if (i4 > 0) {
                                this.nChars = i4;
                                this.nextChar = 0;
                                if (i4 < this.cb.length && this.cb[i4 - 1] != '\n' && this.cb[i4 - 1] != '\r') {
                                    z2 = true;
                                }
                            } else {
                                if (i5 - i2 == 0) {
                                    return -1;
                                }
                                return i5 - i2;
                            }
                        }
                        if (this.leftoverLF && cArr == Console.this.rcb && this.cb[this.nextChar] == '\n') {
                            this.nextChar++;
                        }
                        this.leftoverLF = false;
                        while (this.nextChar < this.nChars) {
                            int i6 = i5;
                            i5++;
                            char c2 = this.cb[this.nextChar];
                            cArr[i6] = c2;
                            char[] cArr2 = this.cb;
                            int i7 = this.nextChar;
                            this.nextChar = i7 + 1;
                            cArr2[i7] = 0;
                            if (c2 == '\n') {
                                return i5 - i2;
                            }
                            if (c2 == '\r') {
                                if (i5 == length) {
                                    if (cArr == Console.this.rcb) {
                                        cArr = Console.this.grow();
                                        int length2 = cArr.length;
                                    } else {
                                        this.leftoverLF = true;
                                        return i5 - i2;
                                    }
                                }
                                if (this.nextChar == this.nChars && this.in.ready()) {
                                    this.nChars = this.in.read(this.cb, 0, this.cb.length);
                                    this.nextChar = 0;
                                }
                                if (this.nextChar < this.nChars && this.cb[this.nextChar] == '\n') {
                                    i5++;
                                    cArr[i5] = '\n';
                                    this.nextChar++;
                                }
                                return i5 - i2;
                            }
                            if (i5 == length) {
                                if (cArr == Console.this.rcb) {
                                    cArr = Console.this.grow();
                                    length = cArr.length;
                                } else {
                                    return i5 - i2;
                                }
                            }
                        }
                    } while (!z2);
                    return i5 - i2;
                }
            }
            throw new IndexOutOfBoundsException();
        }
    }

    private Console() {
        this.readLock = new Object();
        this.writeLock = new Object();
        String strEncoding = encoding();
        if (strEncoding != null) {
            try {
                this.cs = Charset.forName(strEncoding);
            } catch (Exception e2) {
            }
        }
        if (this.cs == null) {
            this.cs = Charset.defaultCharset();
        }
        this.out = StreamEncoder.forOutputStreamWriter(new FileOutputStream(FileDescriptor.out), this.writeLock, this.cs);
        this.pw = new PrintWriter(this.out, true) { // from class: java.io.Console.3
            @Override // java.io.PrintWriter, java.io.Writer, java.io.Closeable, java.lang.AutoCloseable
            public void close() {
            }
        };
        this.formatter = new Formatter(this.out);
        this.reader = new LineReader(StreamDecoder.forInputStreamReader(new FileInputStream(FileDescriptor.in), this.readLock, this.cs));
        this.rcb = new char[1024];
    }
}
