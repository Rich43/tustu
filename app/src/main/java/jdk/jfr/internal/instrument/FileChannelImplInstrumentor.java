package jdk.jfr.internal.instrument;

import java.io.IOException;
import java.nio.ByteBuffer;
import jdk.jfr.events.FileForceEvent;
import jdk.jfr.events.FileReadEvent;
import jdk.jfr.events.FileWriteEvent;

@JIInstrumentationTarget("sun.nio.ch.FileChannelImpl")
/* loaded from: jfr.jar:jdk/jfr/internal/instrument/FileChannelImplInstrumentor.class */
final class FileChannelImplInstrumentor {
    private String path;

    private FileChannelImplInstrumentor() {
    }

    @JIInstrumentationMethod
    public void force(boolean z2) throws IOException {
        FileForceEvent fileForceEvent = FileForceEvent.EVENT.get();
        if (!fileForceEvent.isEnabled()) {
            force(z2);
            return;
        }
        try {
            fileForceEvent.begin();
            force(z2);
        } finally {
            fileForceEvent.path = this.path;
            fileForceEvent.metaData = z2;
            fileForceEvent.commit();
            fileForceEvent.reset();
        }
    }

    @JIInstrumentationMethod
    public int read(ByteBuffer byteBuffer) throws IOException {
        FileReadEvent fileReadEvent = FileReadEvent.EVENT.get();
        if (!fileReadEvent.isEnabled()) {
            return read(byteBuffer);
        }
        int i2 = 0;
        try {
            fileReadEvent.begin();
            i2 = read(byteBuffer);
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
    public int read(ByteBuffer byteBuffer, long j2) throws IOException {
        FileReadEvent fileReadEvent = FileReadEvent.EVENT.get();
        if (!fileReadEvent.isEnabled()) {
            return read(byteBuffer, j2);
        }
        int i2 = 0;
        try {
            fileReadEvent.begin();
            i2 = read(byteBuffer, j2);
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
    public long read(ByteBuffer[] byteBufferArr, int i2, int i3) throws IOException {
        FileReadEvent fileReadEvent = FileReadEvent.EVENT.get();
        if (!fileReadEvent.isEnabled()) {
            return read(byteBufferArr, i2, i3);
        }
        long j2 = 0;
        try {
            fileReadEvent.begin();
            j2 = read(byteBufferArr, i2, i3);
            if (j2 < 0) {
                fileReadEvent.endOfFile = true;
            } else {
                fileReadEvent.bytesRead = j2;
            }
            fileReadEvent.path = this.path;
            fileReadEvent.commit();
            fileReadEvent.reset();
            return j2;
        } catch (Throwable th) {
            if (j2 < 0) {
                fileReadEvent.endOfFile = true;
            } else {
                fileReadEvent.bytesRead = j2;
            }
            fileReadEvent.path = this.path;
            fileReadEvent.commit();
            fileReadEvent.reset();
            throw th;
        }
    }

    @JIInstrumentationMethod
    public int write(ByteBuffer byteBuffer) throws IOException {
        FileWriteEvent fileWriteEvent = FileWriteEvent.EVENT.get();
        if (!fileWriteEvent.isEnabled()) {
            return write(byteBuffer);
        }
        int iWrite = 0;
        try {
            fileWriteEvent.begin();
            iWrite = write(byteBuffer);
            fileWriteEvent.bytesWritten = iWrite > 0 ? iWrite : 0L;
            fileWriteEvent.path = this.path;
            fileWriteEvent.commit();
            fileWriteEvent.reset();
            return iWrite;
        } catch (Throwable th) {
            fileWriteEvent.bytesWritten = iWrite > 0 ? iWrite : 0L;
            fileWriteEvent.path = this.path;
            fileWriteEvent.commit();
            fileWriteEvent.reset();
            throw th;
        }
    }

    @JIInstrumentationMethod
    public int write(ByteBuffer byteBuffer, long j2) throws IOException {
        FileWriteEvent fileWriteEvent = FileWriteEvent.EVENT.get();
        if (!fileWriteEvent.isEnabled()) {
            return write(byteBuffer, j2);
        }
        int iWrite = 0;
        try {
            fileWriteEvent.begin();
            iWrite = write(byteBuffer, j2);
            fileWriteEvent.bytesWritten = iWrite > 0 ? iWrite : 0L;
            fileWriteEvent.path = this.path;
            fileWriteEvent.commit();
            fileWriteEvent.reset();
            return iWrite;
        } catch (Throwable th) {
            fileWriteEvent.bytesWritten = iWrite > 0 ? iWrite : 0L;
            fileWriteEvent.path = this.path;
            fileWriteEvent.commit();
            fileWriteEvent.reset();
            throw th;
        }
    }

    @JIInstrumentationMethod
    public long write(ByteBuffer[] byteBufferArr, int i2, int i3) throws IOException {
        FileWriteEvent fileWriteEvent = FileWriteEvent.EVENT.get();
        if (!fileWriteEvent.isEnabled()) {
            return write(byteBufferArr, i2, i3);
        }
        long jWrite = 0;
        try {
            fileWriteEvent.begin();
            jWrite = write(byteBufferArr, i2, i3);
            fileWriteEvent.bytesWritten = jWrite > 0 ? jWrite : 0L;
            fileWriteEvent.path = this.path;
            fileWriteEvent.commit();
            fileWriteEvent.reset();
            return jWrite;
        } catch (Throwable th) {
            fileWriteEvent.bytesWritten = jWrite > 0 ? jWrite : 0L;
            fileWriteEvent.path = this.path;
            fileWriteEvent.commit();
            fileWriteEvent.reset();
            throw th;
        }
    }
}
