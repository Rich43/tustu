package java.io;

import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Formatter;
import java.util.Locale;
import javafx.fxml.FXMLLoader;

/* loaded from: rt.jar:java/io/PrintStream.class */
public class PrintStream extends FilterOutputStream implements Appendable, Closeable {
    private final boolean autoFlush;
    private boolean trouble;
    private Formatter formatter;
    private BufferedWriter textOut;
    private OutputStreamWriter charOut;
    private boolean closing;

    private static <T> T requireNonNull(T t2, String str) {
        if (t2 == null) {
            throw new NullPointerException(str);
        }
        return t2;
    }

    private static Charset toCharset(String str) throws UnsupportedEncodingException {
        requireNonNull(str, "charsetName");
        try {
            return Charset.forName(str);
        } catch (IllegalCharsetNameException | UnsupportedCharsetException e2) {
            throw new UnsupportedEncodingException(str);
        }
    }

    private PrintStream(boolean z2, OutputStream outputStream) {
        super(outputStream);
        this.trouble = false;
        this.closing = false;
        this.autoFlush = z2;
        this.charOut = new OutputStreamWriter(this);
        this.textOut = new BufferedWriter(this.charOut);
    }

    private PrintStream(boolean z2, OutputStream outputStream, Charset charset) {
        super(outputStream);
        this.trouble = false;
        this.closing = false;
        this.autoFlush = z2;
        this.charOut = new OutputStreamWriter(this, charset);
        this.textOut = new BufferedWriter(this.charOut);
    }

    private PrintStream(boolean z2, Charset charset, OutputStream outputStream) throws UnsupportedEncodingException {
        this(z2, outputStream, charset);
    }

    public PrintStream(OutputStream outputStream) {
        this(outputStream, false);
    }

    public PrintStream(OutputStream outputStream, boolean z2) {
        this(z2, (OutputStream) requireNonNull(outputStream, "Null output stream"));
    }

    public PrintStream(OutputStream outputStream, boolean z2, String str) throws UnsupportedEncodingException {
        this(z2, (OutputStream) requireNonNull(outputStream, "Null output stream"), toCharset(str));
    }

    public PrintStream(String str) throws FileNotFoundException {
        this(false, (OutputStream) new FileOutputStream(str));
    }

    public PrintStream(String str, String str2) throws UnsupportedEncodingException, FileNotFoundException {
        this(false, toCharset(str2), (OutputStream) new FileOutputStream(str));
    }

    public PrintStream(File file) throws FileNotFoundException {
        this(false, (OutputStream) new FileOutputStream(file));
    }

    public PrintStream(File file, String str) throws UnsupportedEncodingException, FileNotFoundException {
        this(false, toCharset(str), (OutputStream) new FileOutputStream(file));
    }

    private void ensureOpen() throws IOException {
        if (this.out == null) {
            throw new IOException("Stream closed");
        }
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream, java.io.Flushable
    public void flush() {
        synchronized (this) {
            try {
                ensureOpen();
                this.out.flush();
            } catch (IOException e2) {
                this.trouble = true;
            }
        }
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        synchronized (this) {
            if (!this.closing) {
                this.closing = true;
                try {
                    this.textOut.close();
                    this.out.close();
                } catch (IOException e2) {
                    this.trouble = true;
                }
                this.textOut = null;
                this.charOut = null;
                this.out = null;
            }
        }
    }

    public boolean checkError() {
        if (this.out != null) {
            flush();
        }
        if (this.out instanceof PrintStream) {
            return ((PrintStream) this.out).checkError();
        }
        return this.trouble;
    }

    protected void setError() {
        this.trouble = true;
    }

    protected void clearError() {
        this.trouble = false;
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public void write(int i2) {
        try {
            synchronized (this) {
                ensureOpen();
                this.out.write(i2);
                if (i2 == 10 && this.autoFlush) {
                    this.out.flush();
                }
            }
        } catch (InterruptedIOException e2) {
            Thread.currentThread().interrupt();
        } catch (IOException e3) {
            this.trouble = true;
        }
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public void write(byte[] bArr, int i2, int i3) {
        try {
            synchronized (this) {
                ensureOpen();
                this.out.write(bArr, i2, i3);
                if (this.autoFlush) {
                    this.out.flush();
                }
            }
        } catch (InterruptedIOException e2) {
            Thread.currentThread().interrupt();
        } catch (IOException e3) {
            this.trouble = true;
        }
    }

    private void write(char[] cArr) {
        try {
            synchronized (this) {
                ensureOpen();
                this.textOut.write(cArr);
                this.textOut.flushBuffer();
                this.charOut.flushBuffer();
                if (this.autoFlush) {
                    for (char c2 : cArr) {
                        if (c2 == '\n') {
                            this.out.flush();
                        }
                    }
                }
            }
        } catch (InterruptedIOException e2) {
            Thread.currentThread().interrupt();
        } catch (IOException e3) {
            this.trouble = true;
        }
    }

    private void write(String str) {
        try {
            synchronized (this) {
                ensureOpen();
                this.textOut.write(str);
                this.textOut.flushBuffer();
                this.charOut.flushBuffer();
                if (this.autoFlush && str.indexOf(10) >= 0) {
                    this.out.flush();
                }
            }
        } catch (InterruptedIOException e2) {
            Thread.currentThread().interrupt();
        } catch (IOException e3) {
            this.trouble = true;
        }
    }

    private void newLine() {
        try {
            synchronized (this) {
                ensureOpen();
                this.textOut.newLine();
                this.textOut.flushBuffer();
                this.charOut.flushBuffer();
                if (this.autoFlush) {
                    this.out.flush();
                }
            }
        } catch (InterruptedIOException e2) {
            Thread.currentThread().interrupt();
        } catch (IOException e3) {
            this.trouble = true;
        }
    }

    public void print(boolean z2) {
        write(z2 ? "true" : "false");
    }

    public void print(char c2) {
        write(String.valueOf(c2));
    }

    public void print(int i2) {
        write(String.valueOf(i2));
    }

    public void print(long j2) {
        write(String.valueOf(j2));
    }

    public void print(float f2) {
        write(String.valueOf(f2));
    }

    public void print(double d2) {
        write(String.valueOf(d2));
    }

    public void print(char[] cArr) {
        write(cArr);
    }

    public void print(String str) {
        if (str == null) {
            str = FXMLLoader.NULL_KEYWORD;
        }
        write(str);
    }

    public void print(Object obj) {
        write(String.valueOf(obj));
    }

    public void println() {
        newLine();
    }

    public void println(boolean z2) {
        synchronized (this) {
            print(z2);
            newLine();
        }
    }

    public void println(char c2) {
        synchronized (this) {
            print(c2);
            newLine();
        }
    }

    public void println(int i2) {
        synchronized (this) {
            print(i2);
            newLine();
        }
    }

    public void println(long j2) {
        synchronized (this) {
            print(j2);
            newLine();
        }
    }

    public void println(float f2) {
        synchronized (this) {
            print(f2);
            newLine();
        }
    }

    public void println(double d2) {
        synchronized (this) {
            print(d2);
            newLine();
        }
    }

    public void println(char[] cArr) {
        synchronized (this) {
            print(cArr);
            newLine();
        }
    }

    public void println(String str) {
        synchronized (this) {
            print(str);
            newLine();
        }
    }

    public void println(Object obj) {
        String strValueOf = String.valueOf(obj);
        synchronized (this) {
            print(strValueOf);
            newLine();
        }
    }

    public PrintStream printf(String str, Object... objArr) {
        return format(str, objArr);
    }

    public PrintStream printf(Locale locale, String str, Object... objArr) {
        return format(locale, str, objArr);
    }

    public PrintStream format(String str, Object... objArr) {
        try {
            synchronized (this) {
                ensureOpen();
                if (this.formatter == null || this.formatter.locale() != Locale.getDefault()) {
                    this.formatter = new Formatter((Appendable) this);
                }
                this.formatter.format(Locale.getDefault(), str, objArr);
            }
        } catch (InterruptedIOException e2) {
            Thread.currentThread().interrupt();
        } catch (IOException e3) {
            this.trouble = true;
        }
        return this;
    }

    public PrintStream format(Locale locale, String str, Object... objArr) {
        try {
            synchronized (this) {
                ensureOpen();
                if (this.formatter == null || this.formatter.locale() != locale) {
                    this.formatter = new Formatter(this, locale);
                }
                this.formatter.format(locale, str, objArr);
            }
        } catch (InterruptedIOException e2) {
            Thread.currentThread().interrupt();
        } catch (IOException e3) {
            this.trouble = true;
        }
        return this;
    }

    @Override // java.lang.Appendable
    public PrintStream append(CharSequence charSequence) {
        if (charSequence == null) {
            print(FXMLLoader.NULL_KEYWORD);
        } else {
            print(charSequence.toString());
        }
        return this;
    }

    @Override // java.lang.Appendable
    public PrintStream append(CharSequence charSequence, int i2, int i3) {
        write((charSequence == null ? FXMLLoader.NULL_KEYWORD : charSequence).subSequence(i2, i3).toString());
        return this;
    }

    @Override // java.lang.Appendable
    public PrintStream append(char c2) {
        print(c2);
        return this;
    }
}
