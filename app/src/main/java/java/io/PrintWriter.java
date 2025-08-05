package java.io;

import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.security.AccessController;
import java.util.Formatter;
import java.util.Locale;
import java.util.Objects;
import javafx.fxml.FXMLLoader;
import sun.security.action.GetPropertyAction;

/* loaded from: rt.jar:java/io/PrintWriter.class */
public class PrintWriter extends Writer {
    protected Writer out;
    private final boolean autoFlush;
    private boolean trouble;
    private Formatter formatter;
    private PrintStream psOut;
    private final String lineSeparator;

    private static Charset toCharset(String str) throws UnsupportedEncodingException {
        Objects.requireNonNull(str, "charsetName");
        try {
            return Charset.forName(str);
        } catch (IllegalCharsetNameException | UnsupportedCharsetException e2) {
            throw new UnsupportedEncodingException(str);
        }
    }

    public PrintWriter(Writer writer) {
        this(writer, false);
    }

    public PrintWriter(Writer writer, boolean z2) {
        super(writer);
        this.trouble = false;
        this.psOut = null;
        this.out = writer;
        this.autoFlush = z2;
        this.lineSeparator = (String) AccessController.doPrivileged(new GetPropertyAction("line.separator"));
    }

    public PrintWriter(OutputStream outputStream) {
        this(outputStream, false);
    }

    public PrintWriter(OutputStream outputStream, boolean z2) {
        this(new BufferedWriter(new OutputStreamWriter(outputStream)), z2);
        if (outputStream instanceof PrintStream) {
            this.psOut = (PrintStream) outputStream;
        }
    }

    public PrintWriter(String str) throws FileNotFoundException {
        this((Writer) new BufferedWriter(new OutputStreamWriter(new FileOutputStream(str))), false);
    }

    private PrintWriter(Charset charset, File file) throws FileNotFoundException {
        this((Writer) new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), charset)), false);
    }

    public PrintWriter(String str, String str2) throws UnsupportedEncodingException, FileNotFoundException {
        this(toCharset(str2), new File(str));
    }

    public PrintWriter(File file) throws FileNotFoundException {
        this((Writer) new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file))), false);
    }

    public PrintWriter(File file, String str) throws UnsupportedEncodingException, FileNotFoundException {
        this(toCharset(str), file);
    }

    private void ensureOpen() throws IOException {
        if (this.out == null) {
            throw new IOException("Stream closed");
        }
    }

    @Override // java.io.Writer, java.io.Flushable
    public void flush() {
        try {
            synchronized (this.lock) {
                ensureOpen();
                this.out.flush();
            }
        } catch (IOException e2) {
            this.trouble = true;
        }
    }

    @Override // java.io.Writer, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        try {
            synchronized (this.lock) {
                if (this.out == null) {
                    return;
                }
                this.out.close();
                this.out = null;
            }
        } catch (IOException e2) {
            this.trouble = true;
        }
    }

    public boolean checkError() {
        if (this.out != null) {
            flush();
        }
        if (this.out instanceof PrintWriter) {
            return ((PrintWriter) this.out).checkError();
        }
        if (this.psOut != null) {
            return this.psOut.checkError();
        }
        return this.trouble;
    }

    protected void setError() {
        this.trouble = true;
    }

    protected void clearError() {
        this.trouble = false;
    }

    @Override // java.io.Writer
    public void write(int i2) {
        try {
            synchronized (this.lock) {
                ensureOpen();
                this.out.write(i2);
            }
        } catch (InterruptedIOException e2) {
            Thread.currentThread().interrupt();
        } catch (IOException e3) {
            this.trouble = true;
        }
    }

    @Override // java.io.Writer
    public void write(char[] cArr, int i2, int i3) {
        try {
            synchronized (this.lock) {
                ensureOpen();
                this.out.write(cArr, i2, i3);
            }
        } catch (InterruptedIOException e2) {
            Thread.currentThread().interrupt();
        } catch (IOException e3) {
            this.trouble = true;
        }
    }

    @Override // java.io.Writer
    public void write(char[] cArr) {
        write(cArr, 0, cArr.length);
    }

    @Override // java.io.Writer
    public void write(String str, int i2, int i3) {
        try {
            synchronized (this.lock) {
                ensureOpen();
                this.out.write(str, i2, i3);
            }
        } catch (InterruptedIOException e2) {
            Thread.currentThread().interrupt();
        } catch (IOException e3) {
            this.trouble = true;
        }
    }

    @Override // java.io.Writer, com.sun.org.apache.xml.internal.serializer.WriterChain
    public void write(String str) {
        write(str, 0, str.length());
    }

    private void newLine() {
        try {
            synchronized (this.lock) {
                ensureOpen();
                this.out.write(this.lineSeparator);
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
        write(c2);
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
        synchronized (this.lock) {
            print(z2);
            println();
        }
    }

    public void println(char c2) {
        synchronized (this.lock) {
            print(c2);
            println();
        }
    }

    public void println(int i2) {
        synchronized (this.lock) {
            print(i2);
            println();
        }
    }

    public void println(long j2) {
        synchronized (this.lock) {
            print(j2);
            println();
        }
    }

    public void println(float f2) {
        synchronized (this.lock) {
            print(f2);
            println();
        }
    }

    public void println(double d2) {
        synchronized (this.lock) {
            print(d2);
            println();
        }
    }

    public void println(char[] cArr) {
        synchronized (this.lock) {
            print(cArr);
            println();
        }
    }

    public void println(String str) {
        synchronized (this.lock) {
            print(str);
            println();
        }
    }

    public void println(Object obj) {
        String strValueOf = String.valueOf(obj);
        synchronized (this.lock) {
            print(strValueOf);
            println();
        }
    }

    public PrintWriter printf(String str, Object... objArr) {
        return format(str, objArr);
    }

    public PrintWriter printf(Locale locale, String str, Object... objArr) {
        return format(locale, str, objArr);
    }

    public PrintWriter format(String str, Object... objArr) {
        try {
            synchronized (this.lock) {
                ensureOpen();
                if (this.formatter == null || this.formatter.locale() != Locale.getDefault()) {
                    this.formatter = new Formatter(this);
                }
                this.formatter.format(Locale.getDefault(), str, objArr);
                if (this.autoFlush) {
                    this.out.flush();
                }
            }
        } catch (InterruptedIOException e2) {
            Thread.currentThread().interrupt();
        } catch (IOException e3) {
            this.trouble = true;
        }
        return this;
    }

    public PrintWriter format(Locale locale, String str, Object... objArr) {
        try {
            synchronized (this.lock) {
                ensureOpen();
                if (this.formatter == null || this.formatter.locale() != locale) {
                    this.formatter = new Formatter(this, locale);
                }
                this.formatter.format(locale, str, objArr);
                if (this.autoFlush) {
                    this.out.flush();
                }
            }
        } catch (InterruptedIOException e2) {
            Thread.currentThread().interrupt();
        } catch (IOException e3) {
            this.trouble = true;
        }
        return this;
    }

    @Override // java.io.Writer, java.lang.Appendable
    public PrintWriter append(CharSequence charSequence) {
        if (charSequence == null) {
            write(FXMLLoader.NULL_KEYWORD);
        } else {
            write(charSequence.toString());
        }
        return this;
    }

    @Override // java.io.Writer, java.lang.Appendable
    public PrintWriter append(CharSequence charSequence, int i2, int i3) {
        write((charSequence == null ? FXMLLoader.NULL_KEYWORD : charSequence).subSequence(i2, i3).toString());
        return this;
    }

    @Override // java.io.Writer, java.lang.Appendable
    public PrintWriter append(char c2) {
        write(c2);
        return this;
    }
}
