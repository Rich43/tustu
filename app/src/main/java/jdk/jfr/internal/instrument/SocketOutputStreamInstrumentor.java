package jdk.jfr.internal.instrument;

import java.io.IOException;
import java.net.InetAddress;
import jdk.jfr.events.SocketWriteEvent;

@JIInstrumentationTarget("java.net.SocketOutputStream")
@JITypeMapping(from = "jdk.jfr.internal.instrument.SocketOutputStreamInstrumentor$AbstractPlainSocketImpl", to = "java.net.AbstractPlainSocketImpl")
/* loaded from: jfr.jar:jdk/jfr/internal/instrument/SocketOutputStreamInstrumentor.class */
final class SocketOutputStreamInstrumentor {
    private AbstractPlainSocketImpl impl = null;

    private SocketOutputStreamInstrumentor() {
    }

    @JIInstrumentationMethod
    private void socketWrite(byte[] bArr, int i2, int i3) throws IOException {
        SocketWriteEvent socketWriteEvent = SocketWriteEvent.EVENT.get();
        if (!socketWriteEvent.isEnabled()) {
            socketWrite(bArr, i2, i3);
            return;
        }
        int i4 = 0;
        try {
            socketWriteEvent.begin();
            socketWrite(bArr, i2, i3);
            i4 = i3;
            socketWriteEvent.end();
            if (socketWriteEvent.shouldCommit()) {
                String string = this.impl.address.toString();
                int iLastIndexOf = string.lastIndexOf(47);
                socketWriteEvent.host = string.substring(0, iLastIndexOf);
                socketWriteEvent.address = string.substring(iLastIndexOf + 1);
                socketWriteEvent.port = this.impl.port;
                socketWriteEvent.bytesWritten = i4 < 0 ? 0L : i4;
                socketWriteEvent.commit();
                socketWriteEvent.reset();
            }
        } catch (Throwable th) {
            socketWriteEvent.end();
            if (socketWriteEvent.shouldCommit()) {
                String string2 = this.impl.address.toString();
                int iLastIndexOf2 = string2.lastIndexOf(47);
                socketWriteEvent.host = string2.substring(0, iLastIndexOf2);
                socketWriteEvent.address = string2.substring(iLastIndexOf2 + 1);
                socketWriteEvent.port = this.impl.port;
                socketWriteEvent.bytesWritten = i4 < 0 ? 0L : i4;
                socketWriteEvent.commit();
                socketWriteEvent.reset();
            }
            throw th;
        }
    }

    void silenceFindBugsUnwrittenField(InetAddress inetAddress) {
        this.impl.address = inetAddress;
    }

    /* loaded from: jfr.jar:jdk/jfr/internal/instrument/SocketOutputStreamInstrumentor$AbstractPlainSocketImpl.class */
    static class AbstractPlainSocketImpl {
        InetAddress address;
        int port;

        AbstractPlainSocketImpl() {
        }
    }
}
