package com.sun.xml.internal.ws.util;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: rt.jar:com/sun/xml/internal/ws/util/NoCloseInputStream.class */
public class NoCloseInputStream extends FilterInputStream {
    public NoCloseInputStream(InputStream is) {
        super(is);
    }

    @Override // java.io.FilterInputStream, java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
    }

    public void doClose() throws IOException {
        super.close();
    }
}
