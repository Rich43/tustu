package java.io;

import javafx.fxml.FXMLLoader;

/* loaded from: rt.jar:java/io/Writer.class */
public abstract class Writer implements Appendable, Closeable, Flushable {
    private char[] writeBuffer;
    private static final int WRITE_BUFFER_SIZE = 1024;
    protected Object lock;

    public abstract void write(char[] cArr, int i2, int i3) throws IOException;

    public abstract void flush() throws IOException;

    public abstract void close() throws IOException;

    protected Writer() {
        this.lock = this;
    }

    protected Writer(Object obj) {
        if (obj == null) {
            throw new NullPointerException();
        }
        this.lock = obj;
    }

    public void write(int i2) throws IOException {
        synchronized (this.lock) {
            if (this.writeBuffer == null) {
                this.writeBuffer = new char[1024];
            }
            this.writeBuffer[0] = (char) i2;
            write(this.writeBuffer, 0, 1);
        }
    }

    public void write(char[] cArr) throws IOException {
        write(cArr, 0, cArr.length);
    }

    public void write(String str) throws IOException {
        write(str, 0, str.length());
    }

    public void write(String str, int i2, int i3) throws IOException {
        char[] cArr;
        synchronized (this.lock) {
            if (i3 <= 1024) {
                if (this.writeBuffer == null) {
                    this.writeBuffer = new char[1024];
                }
                cArr = this.writeBuffer;
            } else {
                cArr = new char[i3];
            }
            str.getChars(i2, i2 + i3, cArr, 0);
            write(cArr, 0, i3);
        }
    }

    @Override // java.lang.Appendable
    public Writer append(CharSequence charSequence) throws IOException {
        if (charSequence == null) {
            write(FXMLLoader.NULL_KEYWORD);
        } else {
            write(charSequence.toString());
        }
        return this;
    }

    @Override // java.lang.Appendable
    public Writer append(CharSequence charSequence, int i2, int i3) throws IOException {
        write((charSequence == null ? FXMLLoader.NULL_KEYWORD : charSequence).subSequence(i2, i3).toString());
        return this;
    }

    @Override // java.lang.Appendable
    public Writer append(char c2) throws IOException {
        write(c2);
        return this;
    }
}
