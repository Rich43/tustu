package jdk.jfr.internal.instrument;

import java.io.IOException;
import jdk.jfr.events.FileReadEvent;
import jdk.jfr.events.FileWriteEvent;

@JIInstrumentationTarget("java.io.RandomAccessFile")
/* loaded from: jfr.jar:jdk/jfr/internal/instrument/RandomAccessFileInstrumentor.class */
final class RandomAccessFileInstrumentor {
    private String path;

    private RandomAccessFileInstrumentor() {
    }

    @JIInstrumentationMethod
    public int read() throws IOException {
        FileReadEvent fileReadEvent = FileReadEvent.EVENT.get();
        if (!fileReadEvent.isEnabled()) {
            return read();
        }
        try {
            fileReadEvent.begin();
            int i2 = read();
            if (i2 < 0) {
                fileReadEvent.endOfFile = true;
            } else {
                fileReadEvent.bytesRead = 1L;
            }
            return i2;
        } finally {
            fileReadEvent.path = this.path;
            fileReadEvent.commit();
            fileReadEvent.reset();
        }
    }

    @JIInstrumentationMethod
    public int read(byte[] bArr) throws IOException {
        FileReadEvent fileReadEvent = FileReadEvent.EVENT.get();
        if (!fileReadEvent.isEnabled()) {
            return read(bArr);
        }
        int i2 = 0;
        try {
            fileReadEvent.begin();
            i2 = read(bArr);
            if (i2 < 0) {
                fileReadEvent.endOfFile = true;
            } else {
                fileReadEvent.bytesRead = i2;
            }
            fileReadEvent.path = this.path;
            fileReadEvent.commit();
            fileReadEvent.reset();
            return i2;
        } catch (Throwable th) {
            if (i2 < 0) {
                fileReadEvent.endOfFile = true;
            } else {
                fileReadEvent.bytesRead = i2;
            }
            fileReadEvent.path = this.path;
            fileReadEvent.commit();
            fileReadEvent.reset();
            throw th;
        }
    }

    @JIInstrumentationMethod
    public int read(byte[] bArr, int i2, int i3) throws IOException {
        FileReadEvent fileReadEvent = FileReadEvent.EVENT.get();
        if (!fileReadEvent.isEnabled()) {
            return read(bArr, i2, i3);
        }
        int i4 = 0;
        try {
            fileReadEvent.begin();
            i4 = read(bArr, i2, i3);
            if (i4 < 0) {
                fileReadEvent.endOfFile = true;
            } else {
                fileReadEvent.bytesRead = i4;
            }
            fileReadEvent.path = this.path;
            fileReadEvent.commit();
            fileReadEvent.reset();
            return i4;
        } catch (Throwable th) {
            if (i4 < 0) {
                fileReadEvent.endOfFile = true;
            } else {
                fileReadEvent.bytesRead = i4;
            }
            fileReadEvent.path = this.path;
            fileReadEvent.commit();
            fileReadEvent.reset();
            throw th;
        }
    }

    @JIInstrumentationMethod
    public void write(int i2) throws IOException {
        FileWriteEvent fileWriteEvent = FileWriteEvent.EVENT.get();
        if (!fileWriteEvent.isEnabled()) {
            write(i2);
            return;
        }
        try {
            fileWriteEvent.begin();
            write(i2);
            fileWriteEvent.bytesWritten = 1L;
        } finally {
            fileWriteEvent.path = this.path;
            fileWriteEvent.commit();
            fileWriteEvent.reset();
        }
    }

    @JIInstrumentationMethod
    public void write(byte[] bArr) throws IOException {
        FileWriteEvent fileWriteEvent = FileWriteEvent.EVENT.get();
        if (!fileWriteEvent.isEnabled()) {
            write(bArr);
            return;
        }
        try {
            fileWriteEvent.begin();
            write(bArr);
            fileWriteEvent.bytesWritten = bArr.length;
        } finally {
            fileWriteEvent.path = this.path;
            fileWriteEvent.commit();
            fileWriteEvent.reset();
        }
    }

    @JIInstrumentationMethod
    public void write(byte[] bArr, int i2, int i3) throws IOException {
        FileWriteEvent fileWriteEvent = FileWriteEvent.EVENT.get();
        if (!fileWriteEvent.isEnabled()) {
            write(bArr, i2, i3);
            return;
        }
        try {
            fileWriteEvent.begin();
            write(bArr, i2, i3);
            fileWriteEvent.bytesWritten = i3;
            fileWriteEvent.path = this.path;
            fileWriteEvent.commit();
            fileWriteEvent.reset();
        } catch (Throwable th) {
            fileWriteEvent.path = this.path;
            fileWriteEvent.commit();
            fileWriteEvent.reset();
            throw th;
        }
    }
}
