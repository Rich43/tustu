package jdk.jfr.internal.instrument;

import java.io.IOException;
import jdk.jfr.events.FileWriteEvent;

@JIInstrumentationTarget("java.io.FileOutputStream")
/* loaded from: jfr.jar:jdk/jfr/internal/instrument/FileOutputStreamInstrumentor.class */
final class FileOutputStreamInstrumentor {
    private String path;

    private FileOutputStreamInstrumentor() {
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
