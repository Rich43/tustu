package org.icepdf.core.io;

import java.io.IOException;
import java.io.InputStream;

/* loaded from: icepdf-core.jar:org/icepdf/core/io/SeekableInput.class */
public interface SeekableInput {
    int read() throws IOException;

    int read(byte[] bArr) throws IOException;

    int read(byte[] bArr, int i2, int i3) throws IOException;

    void close() throws IOException;

    int available();

    void mark(int i2);

    boolean markSupported();

    void reset() throws IOException;

    long skip(long j2) throws IOException;

    void seekAbsolute(long j2) throws IOException;

    void seekRelative(long j2) throws IOException;

    void seekEnd() throws IOException;

    long getAbsolutePosition() throws IOException;

    long getLength() throws IOException;

    InputStream getInputStream();

    void beginThreadAccess();

    void endThreadAccess();
}
