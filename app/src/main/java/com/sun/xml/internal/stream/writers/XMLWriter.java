package com.sun.xml.internal.stream.writers;

import com.sun.org.apache.xerces.internal.util.XMLStringBuffer;
import java.io.IOException;
import java.io.Writer;
import jdk.nashorn.internal.runtime.regexp.joni.constants.StackType;

/* loaded from: rt.jar:com/sun/xml/internal/stream/writers/XMLWriter.class */
public class XMLWriter extends Writer {
    private Writer writer;
    private int size;
    private XMLStringBuffer buffer;
    private static final int THRESHHOLD_LENGTH = 4096;
    private static final boolean DEBUG = false;

    public XMLWriter(Writer writer) {
        this(writer, 4096);
    }

    public XMLWriter(Writer writer, int size) {
        this.buffer = new XMLStringBuffer(StackType.NULL_CHECK_START);
        this.writer = writer;
        this.size = size;
    }

    @Override // java.io.Writer
    public void write(int c2) throws IOException {
        ensureOpen();
        this.buffer.append((char) c2);
        conditionalWrite();
    }

    @Override // java.io.Writer
    public void write(char[] cbuf) throws IOException {
        write(cbuf, 0, cbuf.length);
    }

    @Override // java.io.Writer
    public void write(char[] cbuf, int off, int len) throws IOException {
        ensureOpen();
        if (len > this.size) {
            writeBufferedData();
            this.writer.write(cbuf, off, len);
        } else {
            this.buffer.append(cbuf, off, len);
            conditionalWrite();
        }
    }

    @Override // java.io.Writer
    public void write(String str, int off, int len) throws IOException {
        write(str.toCharArray(), off, len);
    }

    @Override // java.io.Writer, com.sun.org.apache.xml.internal.serializer.WriterChain
    public void write(String str) throws IOException {
        if (str.length() > this.size) {
            writeBufferedData();
            this.writer.write(str);
        } else {
            this.buffer.append(str);
            conditionalWrite();
        }
    }

    @Override // java.io.Writer, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (this.writer == null) {
            return;
        }
        flush();
        this.writer.close();
        this.writer = null;
    }

    @Override // java.io.Writer, java.io.Flushable
    public void flush() throws IOException {
        ensureOpen();
        writeBufferedData();
        this.writer.flush();
    }

    public void reset() {
        this.writer = null;
        this.buffer.clear();
        this.size = 4096;
    }

    public void setWriter(Writer writer) {
        this.writer = writer;
        this.buffer.clear();
        this.size = 4096;
    }

    public void setWriter(Writer writer, int size) {
        this.writer = writer;
        this.size = size;
    }

    protected Writer getWriter() {
        return this.writer;
    }

    private void conditionalWrite() throws IOException {
        if (this.buffer.length > this.size) {
            writeBufferedData();
        }
    }

    private void writeBufferedData() throws IOException {
        this.writer.write(this.buffer.ch, this.buffer.offset, this.buffer.length);
        this.buffer.clear();
    }

    private void ensureOpen() throws IOException {
        if (this.writer == null) {
            throw new IOException("Stream closed");
        }
    }
}
