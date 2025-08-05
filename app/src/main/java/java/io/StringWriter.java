package java.io;

import javafx.fxml.FXMLLoader;

/* loaded from: rt.jar:java/io/StringWriter.class */
public class StringWriter extends Writer {
    private StringBuffer buf;

    public StringWriter() {
        this.buf = new StringBuffer();
        this.lock = this.buf;
    }

    public StringWriter(int i2) {
        if (i2 < 0) {
            throw new IllegalArgumentException("Negative buffer size");
        }
        this.buf = new StringBuffer(i2);
        this.lock = this.buf;
    }

    @Override // java.io.Writer
    public void write(int i2) {
        this.buf.append((char) i2);
    }

    @Override // java.io.Writer
    public void write(char[] cArr, int i2, int i3) {
        if (i2 < 0 || i2 > cArr.length || i3 < 0 || i2 + i3 > cArr.length || i2 + i3 < 0) {
            throw new IndexOutOfBoundsException();
        }
        if (i3 == 0) {
            return;
        }
        this.buf.append(cArr, i2, i3);
    }

    @Override // java.io.Writer, com.sun.org.apache.xml.internal.serializer.WriterChain
    public void write(String str) {
        this.buf.append(str);
    }

    @Override // java.io.Writer
    public void write(String str, int i2, int i3) {
        this.buf.append(str.substring(i2, i2 + i3));
    }

    @Override // java.io.Writer, java.lang.Appendable
    public StringWriter append(CharSequence charSequence) {
        if (charSequence == null) {
            write(FXMLLoader.NULL_KEYWORD);
        } else {
            write(charSequence.toString());
        }
        return this;
    }

    @Override // java.io.Writer, java.lang.Appendable
    public StringWriter append(CharSequence charSequence, int i2, int i3) {
        write((charSequence == null ? FXMLLoader.NULL_KEYWORD : charSequence).subSequence(i2, i3).toString());
        return this;
    }

    @Override // java.io.Writer, java.lang.Appendable
    public StringWriter append(char c2) {
        write(c2);
        return this;
    }

    public String toString() {
        return this.buf.toString();
    }

    public StringBuffer getBuffer() {
        return this.buf;
    }

    @Override // java.io.Writer, java.io.Flushable
    public void flush() {
    }

    @Override // java.io.Writer, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
    }
}
