package java.io;

import java.util.Arrays;
import javafx.fxml.FXMLLoader;

/* loaded from: rt.jar:java/io/CharArrayWriter.class */
public class CharArrayWriter extends Writer {
    protected char[] buf;
    protected int count;

    public CharArrayWriter() {
        this(32);
    }

    public CharArrayWriter(int i2) {
        if (i2 < 0) {
            throw new IllegalArgumentException("Negative initial size: " + i2);
        }
        this.buf = new char[i2];
    }

    @Override // java.io.Writer
    public void write(int i2) {
        synchronized (this.lock) {
            int i3 = this.count + 1;
            if (i3 > this.buf.length) {
                this.buf = Arrays.copyOf(this.buf, Math.max(this.buf.length << 1, i3));
            }
            this.buf[this.count] = (char) i2;
            this.count = i3;
        }
    }

    @Override // java.io.Writer
    public void write(char[] cArr, int i2, int i3) {
        if (i2 < 0 || i2 > cArr.length || i3 < 0 || i2 + i3 > cArr.length || i2 + i3 < 0) {
            throw new IndexOutOfBoundsException();
        }
        if (i3 == 0) {
            return;
        }
        synchronized (this.lock) {
            int i4 = this.count + i3;
            if (i4 > this.buf.length) {
                this.buf = Arrays.copyOf(this.buf, Math.max(this.buf.length << 1, i4));
            }
            System.arraycopy(cArr, i2, this.buf, this.count, i3);
            this.count = i4;
        }
    }

    @Override // java.io.Writer
    public void write(String str, int i2, int i3) {
        synchronized (this.lock) {
            int i4 = this.count + i3;
            if (i4 > this.buf.length) {
                this.buf = Arrays.copyOf(this.buf, Math.max(this.buf.length << 1, i4));
            }
            str.getChars(i2, i2 + i3, this.buf, this.count);
            this.count = i4;
        }
    }

    public void writeTo(Writer writer) throws IOException {
        synchronized (this.lock) {
            writer.write(this.buf, 0, this.count);
        }
    }

    @Override // java.io.Writer, java.lang.Appendable
    public CharArrayWriter append(CharSequence charSequence) {
        String string = charSequence == null ? FXMLLoader.NULL_KEYWORD : charSequence.toString();
        write(string, 0, string.length());
        return this;
    }

    @Override // java.io.Writer, java.lang.Appendable
    public CharArrayWriter append(CharSequence charSequence, int i2, int i3) {
        String string = (charSequence == null ? FXMLLoader.NULL_KEYWORD : charSequence).subSequence(i2, i3).toString();
        write(string, 0, string.length());
        return this;
    }

    @Override // java.io.Writer, java.lang.Appendable
    public CharArrayWriter append(char c2) {
        write(c2);
        return this;
    }

    public void reset() {
        this.count = 0;
    }

    public char[] toCharArray() {
        char[] cArrCopyOf;
        synchronized (this.lock) {
            cArrCopyOf = Arrays.copyOf(this.buf, this.count);
        }
        return cArrCopyOf;
    }

    public int size() {
        return this.count;
    }

    public String toString() {
        String str;
        synchronized (this.lock) {
            str = new String(this.buf, 0, this.count);
        }
        return str;
    }

    @Override // java.io.Writer, java.io.Flushable
    public void flush() {
    }

    @Override // java.io.Writer, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
    }
}
