package com.sun.org.apache.xml.internal.serializer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/serializer/WriterToASCI.class */
class WriterToASCI extends Writer implements WriterChain {
    private final OutputStream m_os;

    public WriterToASCI(OutputStream os) {
        this.m_os = os;
    }

    @Override // java.io.Writer
    public void write(char[] chars, int start, int length) throws IOException {
        int n2 = length + start;
        for (int i2 = start; i2 < n2; i2++) {
            this.m_os.write(chars[i2]);
        }
    }

    @Override // java.io.Writer
    public void write(int c2) throws IOException {
        this.m_os.write(c2);
    }

    @Override // java.io.Writer, com.sun.org.apache.xml.internal.serializer.WriterChain
    public void write(String s2) throws IOException {
        int n2 = s2.length();
        for (int i2 = 0; i2 < n2; i2++) {
            this.m_os.write(s2.charAt(i2));
        }
    }

    @Override // java.io.Writer, java.io.Flushable
    public void flush() throws IOException {
        this.m_os.flush();
    }

    @Override // java.io.Writer, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.m_os.close();
    }

    @Override // com.sun.org.apache.xml.internal.serializer.WriterChain
    public OutputStream getOutputStream() {
        return this.m_os;
    }

    @Override // com.sun.org.apache.xml.internal.serializer.WriterChain
    public Writer getWriter() {
        return null;
    }
}
