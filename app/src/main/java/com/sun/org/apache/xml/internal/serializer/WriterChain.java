package com.sun.org.apache.xml.internal.serializer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/serializer/WriterChain.class */
interface WriterChain {
    void write(int i2) throws IOException;

    void write(char[] cArr) throws IOException;

    void write(char[] cArr, int i2, int i3) throws IOException;

    void write(String str) throws IOException;

    void write(String str, int i2, int i3) throws IOException;

    void flush() throws IOException;

    void close() throws IOException;

    Writer getWriter();

    OutputStream getOutputStream();
}
