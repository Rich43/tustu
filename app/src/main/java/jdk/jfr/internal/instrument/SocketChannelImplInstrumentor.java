package jdk.jfr.internal.instrument;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import jdk.jfr.events.SocketReadEvent;
import jdk.jfr.events.SocketWriteEvent;

@JIInstrumentationTarget("sun.nio.ch.SocketChannelImpl")
/* loaded from: jfr.jar:jdk/jfr/internal/instrument/SocketChannelImplInstrumentor.class */
final class SocketChannelImplInstrumentor {
    private InetSocketAddress remoteAddress;

    private SocketChannelImplInstrumentor() {
    }

    @JIInstrumentationMethod
    public int read(ByteBuffer byteBuffer) throws IOException {
        SocketReadEvent socketReadEvent = SocketReadEvent.EVENT.get();
        if (!socketReadEvent.isEnabled()) {
            return read(byteBuffer);
        }
        int i2 = 0;
        try {
            socketReadEvent.begin();
            i2 = read(byteBuffer);
            socketReadEvent.end();
            if (socketReadEvent.shouldCommit()) {
                String string = this.remoteAddress.getAddress().toString();
                int iLastIndexOf = string.lastIndexOf(47);
                socketReadEvent.host = string.substring(0, iLastIndexOf);
                socketReadEvent.address = string.substring(iLastIndexOf + 1);
                socketReadEvent.port = this.remoteAddress.getPort();
                if (i2 < 0) {
                    socketReadEvent.endOfStream = true;
                } else {
                    socketReadEvent.bytesRead = i2;
                }
                socketReadEvent.timeout = 0L;
                socketReadEvent.commit();
                socketReadEvent.reset();
            }
            return i2;
        } catch (Throwable th) {
            socketReadEvent.end();
            if (socketReadEvent.shouldCommit()) {
                String string2 = this.remoteAddress.getAddress().toString();
                int iLastIndexOf2 = string2.lastIndexOf(47);
                socketReadEvent.host = string2.substring(0, iLastIndexOf2);
                socketReadEvent.address = string2.substring(iLastIndexOf2 + 1);
                socketReadEvent.port = this.remoteAddress.getPort();
                if (i2 < 0) {
                    socketReadEvent.endOfStream = true;
                } else {
                    socketReadEvent.bytesRead = i2;
                }
                socketReadEvent.timeout = 0L;
                socketReadEvent.commit();
                socketReadEvent.reset();
            }
            throw th;
        }
    }

    @JIInstrumentationMethod
    public long read(ByteBuffer[] byteBufferArr, int i2, int i3) throws IOException {
        SocketReadEvent socketReadEvent = SocketReadEvent.EVENT.get();
        if (!socketReadEvent.isEnabled()) {
            return read(byteBufferArr, i2, i3);
        }
        long j2 = 0;
        try {
            socketReadEvent.begin();
            j2 = read(byteBufferArr, i2, i3);
            socketReadEvent.end();
            if (socketReadEvent.shouldCommit()) {
                String string = this.remoteAddress.getAddress().toString();
                int iLastIndexOf = string.lastIndexOf(47);
                socketReadEvent.host = string.substring(0, iLastIndexOf);
                socketReadEvent.address = string.substring(iLastIndexOf + 1);
                socketReadEvent.port = this.remoteAddress.getPort();
                if (j2 < 0) {
                    socketReadEvent.endOfStream = true;
                } else {
                    socketReadEvent.bytesRead = j2;
                }
                socketReadEvent.timeout = 0L;
                socketReadEvent.commit();
                socketReadEvent.reset();
            }
            return j2;
        } catch (Throwable th) {
            socketReadEvent.end();
            if (socketReadEvent.shouldCommit()) {
                String string2 = this.remoteAddress.getAddress().toString();
                int iLastIndexOf2 = string2.lastIndexOf(47);
                socketReadEvent.host = string2.substring(0, iLastIndexOf2);
                socketReadEvent.address = string2.substring(iLastIndexOf2 + 1);
                socketReadEvent.port = this.remoteAddress.getPort();
                if (j2 < 0) {
                    socketReadEvent.endOfStream = true;
                } else {
                    socketReadEvent.bytesRead = j2;
                }
                socketReadEvent.timeout = 0L;
                socketReadEvent.commit();
                socketReadEvent.reset();
            }
            throw th;
        }
    }

    @JIInstrumentationMethod
    public int write(ByteBuffer byteBuffer) throws IOException {
        SocketWriteEvent socketWriteEvent = SocketWriteEvent.EVENT.get();
        if (!socketWriteEvent.isEnabled()) {
            return write(byteBuffer);
        }
        int iWrite = 0;
        try {
            socketWriteEvent.begin();
            iWrite = write(byteBuffer);
            socketWriteEvent.end();
            if (socketWriteEvent.shouldCommit()) {
                String string = this.remoteAddress.getAddress().toString();
                int iLastIndexOf = string.lastIndexOf(47);
                socketWriteEvent.host = string.substring(0, iLastIndexOf);
                socketWriteEvent.address = string.substring(iLastIndexOf + 1);
                socketWriteEvent.port = this.remoteAddress.getPort();
                socketWriteEvent.bytesWritten = iWrite < 0 ? 0L : iWrite;
                socketWriteEvent.commit();
                socketWriteEvent.reset();
            }
            return iWrite;
        } catch (Throwable th) {
            socketWriteEvent.end();
            if (socketWriteEvent.shouldCommit()) {
                String string2 = this.remoteAddress.getAddress().toString();
                int iLastIndexOf2 = string2.lastIndexOf(47);
                socketWriteEvent.host = string2.substring(0, iLastIndexOf2);
                socketWriteEvent.address = string2.substring(iLastIndexOf2 + 1);
                socketWriteEvent.port = this.remoteAddress.getPort();
                socketWriteEvent.bytesWritten = iWrite < 0 ? 0L : iWrite;
                socketWriteEvent.commit();
                socketWriteEvent.reset();
            }
            throw th;
        }
    }

    @JIInstrumentationMethod
    public long write(ByteBuffer[] byteBufferArr, int i2, int i3) throws IOException {
        SocketWriteEvent socketWriteEvent = SocketWriteEvent.EVENT.get();
        if (!socketWriteEvent.isEnabled()) {
            return write(byteBufferArr, i2, i3);
        }
        long jWrite = 0;
        try {
            socketWriteEvent.begin();
            jWrite = write(byteBufferArr, i2, i3);
            socketWriteEvent.end();
            if (socketWriteEvent.shouldCommit()) {
                String string = this.remoteAddress.getAddress().toString();
                int iLastIndexOf = string.lastIndexOf(47);
                socketWriteEvent.host = string.substring(0, iLastIndexOf);
                socketWriteEvent.address = string.substring(iLastIndexOf + 1);
                socketWriteEvent.port = this.remoteAddress.getPort();
                socketWriteEvent.bytesWritten = jWrite < 0 ? 0L : jWrite;
                socketWriteEvent.commit();
                socketWriteEvent.reset();
            }
            return jWrite;
        } catch (Throwable th) {
            socketWriteEvent.end();
            if (socketWriteEvent.shouldCommit()) {
                String string2 = this.remoteAddress.getAddress().toString();
                int iLastIndexOf2 = string2.lastIndexOf(47);
                socketWriteEvent.host = string2.substring(0, iLastIndexOf2);
                socketWriteEvent.address = string2.substring(iLastIndexOf2 + 1);
                socketWriteEvent.port = this.remoteAddress.getPort();
                socketWriteEvent.bytesWritten = jWrite < 0 ? 0L : jWrite;
                socketWriteEvent.commit();
                socketWriteEvent.reset();
            }
            throw th;
        }
    }
}
