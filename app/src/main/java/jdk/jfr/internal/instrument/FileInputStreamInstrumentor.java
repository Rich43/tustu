package jdk.jfr.internal.instrument;

import java.io.IOException;
import jdk.jfr.events.FileReadEvent;

@JIInstrumentationTarget("java.io.FileInputStream")
/* loaded from: jfr.jar:jdk/jfr/internal/instrument/FileInputStreamInstrumentor.class */
final class FileInputStreamInstrumentor {
    private String path;

    private FileInputStreamInstrumentor() {
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
}
