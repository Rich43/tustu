package com.sun.org.apache.xml.internal.serializer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/serializer/SerializerTraceWriter.class */
final class SerializerTraceWriter extends Writer implements WriterChain {
    private final Writer m_writer;
    private final SerializerTrace m_tracer;
    private int buf_length;
    private byte[] buf;
    private int count;

    private void setBufferSize(int size) {
        this.buf = new byte[size + 3];
        this.buf_length = size;
        this.count = 0;
    }

    public SerializerTraceWriter(Writer out, SerializerTrace tracer) {
        this.m_writer = out;
        this.m_tracer = tracer;
        setBufferSize(1024);
    }

    private void flushBuffer() throws IOException {
        if (this.count > 0) {
            char[] chars = new char[this.count];
            for (int i2 = 0; i2 < this.count; i2++) {
                chars[i2] = (char) this.buf[i2];
            }
            if (this.m_tracer != null) {
                this.m_tracer.fireGenerateEvent(12, chars, 0, chars.length);
            }
            this.count = 0;
        }
    }

    @Override // java.io.Writer, java.io.Flushable
    public void flush() throws IOException {
        if (this.m_writer != null) {
            this.m_writer.flush();
        }
        flushBuffer();
    }

    @Override // java.io.Writer, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (this.m_writer != null) {
            this.m_writer.close();
        }
        flushBuffer();
    }

    @Override // java.io.Writer
    public void write(int c2) throws IOException {
        if (this.m_writer != null) {
            this.m_writer.write(c2);
        }
        if (this.count >= this.buf_length) {
            flushBuffer();
        }
        if (c2 < 128) {
            byte[] bArr = this.buf;
            int i2 = this.count;
            this.count = i2 + 1;
            bArr[i2] = (byte) c2;
            return;
        }
        if (c2 < 2048) {
            byte[] bArr2 = this.buf;
            int i3 = this.count;
            this.count = i3 + 1;
            bArr2[i3] = (byte) (192 + (c2 >> 6));
            byte[] bArr3 = this.buf;
            int i4 = this.count;
            this.count = i4 + 1;
            bArr3[i4] = (byte) (128 + (c2 & 63));
            return;
        }
        byte[] bArr4 = this.buf;
        int i5 = this.count;
        this.count = i5 + 1;
        bArr4[i5] = (byte) (224 + (c2 >> 12));
        byte[] bArr5 = this.buf;
        int i6 = this.count;
        this.count = i6 + 1;
        bArr5[i6] = (byte) (128 + ((c2 >> 6) & 63));
        byte[] bArr6 = this.buf;
        int i7 = this.count;
        this.count = i7 + 1;
        bArr6[i7] = (byte) (128 + (c2 & 63));
    }

    @Override // java.io.Writer
    public void write(char[] chars, int start, int length) throws IOException {
        if (this.m_writer != null) {
            this.m_writer.write(chars, start, length);
        }
        int lengthx3 = (length << 1) + length;
        if (lengthx3 >= this.buf_length) {
            flushBuffer();
            setBufferSize(2 * lengthx3);
        }
        if (lengthx3 > this.buf_length - this.count) {
            flushBuffer();
        }
        int n2 = length + start;
        for (int i2 = start; i2 < n2; i2++) {
            char c2 = chars[i2];
            if (c2 < 128) {
                byte[] bArr = this.buf;
                int i3 = this.count;
                this.count = i3 + 1;
                bArr[i3] = (byte) c2;
            } else if (c2 < 2048) {
                byte[] bArr2 = this.buf;
                int i4 = this.count;
                this.count = i4 + 1;
                bArr2[i4] = (byte) (192 + (c2 >> 6));
                byte[] bArr3 = this.buf;
                int i5 = this.count;
                this.count = i5 + 1;
                bArr3[i5] = (byte) (128 + (c2 & '?'));
            } else {
                byte[] bArr4 = this.buf;
                int i6 = this.count;
                this.count = i6 + 1;
                bArr4[i6] = (byte) (224 + (c2 >> '\f'));
                byte[] bArr5 = this.buf;
                int i7 = this.count;
                this.count = i7 + 1;
                bArr5[i7] = (byte) (128 + ((c2 >> 6) & 63));
                byte[] bArr6 = this.buf;
                int i8 = this.count;
                this.count = i8 + 1;
                bArr6[i8] = (byte) (128 + (c2 & '?'));
            }
        }
    }

    @Override // java.io.Writer, com.sun.org.apache.xml.internal.serializer.WriterChain
    public void write(String s2) throws IOException {
        if (this.m_writer != null) {
            this.m_writer.write(s2);
        }
        int length = s2.length();
        int lengthx3 = (length << 1) + length;
        if (lengthx3 >= this.buf_length) {
            flushBuffer();
            setBufferSize(2 * lengthx3);
        }
        if (lengthx3 > this.buf_length - this.count) {
            flushBuffer();
        }
        for (int i2 = 0; i2 < length; i2++) {
            char c2 = s2.charAt(i2);
            if (c2 < 128) {
                byte[] bArr = this.buf;
                int i3 = this.count;
                this.count = i3 + 1;
                bArr[i3] = (byte) c2;
            } else if (c2 < 2048) {
                byte[] bArr2 = this.buf;
                int i4 = this.count;
                this.count = i4 + 1;
                bArr2[i4] = (byte) (192 + (c2 >> 6));
                byte[] bArr3 = this.buf;
                int i5 = this.count;
                this.count = i5 + 1;
                bArr3[i5] = (byte) (128 + (c2 & '?'));
            } else {
                byte[] bArr4 = this.buf;
                int i6 = this.count;
                this.count = i6 + 1;
                bArr4[i6] = (byte) (224 + (c2 >> '\f'));
                byte[] bArr5 = this.buf;
                int i7 = this.count;
                this.count = i7 + 1;
                bArr5[i7] = (byte) (128 + ((c2 >> 6) & 63));
                byte[] bArr6 = this.buf;
                int i8 = this.count;
                this.count = i8 + 1;
                bArr6[i8] = (byte) (128 + (c2 & '?'));
            }
        }
    }

    @Override // com.sun.org.apache.xml.internal.serializer.WriterChain
    public Writer getWriter() {
        return this.m_writer;
    }

    @Override // com.sun.org.apache.xml.internal.serializer.WriterChain
    public OutputStream getOutputStream() {
        OutputStream retval = null;
        if (this.m_writer instanceof WriterChain) {
            retval = ((WriterChain) this.m_writer).getOutputStream();
        }
        return retval;
    }
}
