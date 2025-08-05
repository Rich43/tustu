package net.lingala.zip4j.io;

import java.io.IOException;
import java.io.InputStream;
import net.lingala.zip4j.unzip.UnzipEngine;

/* loaded from: zip4j_1.3.1.jar:net/lingala/zip4j/io/BaseInputStream.class */
public abstract class BaseInputStream extends InputStream {
    @Override // java.io.InputStream
    public int read() throws IOException {
        return 0;
    }

    public void seek(long pos) throws IOException {
    }

    @Override // java.io.InputStream
    public int available() throws IOException {
        return 0;
    }

    public UnzipEngine getUnzipEngine() {
        return null;
    }
}
