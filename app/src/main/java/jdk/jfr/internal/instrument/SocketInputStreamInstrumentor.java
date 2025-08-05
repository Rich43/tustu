package jdk.jfr.internal.instrument;

import java.io.IOException;
import java.net.InetAddress;
import jdk.jfr.events.SocketReadEvent;

@JIInstrumentationTarget("java.net.SocketInputStream")
@JITypeMapping(from = "jdk.jfr.internal.instrument.SocketInputStreamInstrumentor$AbstractPlainSocketImpl", to = "java.net.AbstractPlainSocketImpl")
/* loaded from: jfr.jar:jdk/jfr/internal/instrument/SocketInputStreamInstrumentor.class */
final class SocketInputStreamInstrumentor {
    private AbstractPlainSocketImpl impl = null;

    private SocketInputStreamInstrumentor() {
    }

    @JIInstrumentationMethod
    int read(byte[] bArr, int i2, int i3, int i4) throws IOException {
        SocketReadEvent socketReadEvent = SocketReadEvent.EVENT.get();
        if (!socketReadEvent.isEnabled()) {
            return read(bArr, i2, i3, i4);
        }
        int i5 = 0;
        try {
            socketReadEvent.begin();
            i5 = read(bArr, i2, i3, i4);
            socketReadEvent.end();
            if (socketReadEvent.shouldCommit()) {
                String string = this.impl.address.toString();
                int iLastIndexOf = string.lastIndexOf(47);
                socketReadEvent.host = string.substring(0, iLastIndexOf);
                socketReadEvent.address = string.substring(iLastIndexOf + 1);
                socketReadEvent.port = this.impl.port;
                if (i5 < 0) {
                    socketReadEvent.endOfStream = true;
                } else {
                    socketReadEvent.bytesRead = i5;
                }
                socketReadEvent.timeout = i4;
                socketReadEvent.commit();
                socketReadEvent.reset();
            }
            return i5;
        } catch (Throwable th) {
            socketReadEvent.end();
            if (socketReadEvent.shouldCommit()) {
                String string2 = this.impl.address.toString();
                int iLastIndexOf2 = string2.lastIndexOf(47);
                socketReadEvent.host = string2.substring(0, iLastIndexOf2);
                socketReadEvent.address = string2.substring(iLastIndexOf2 + 1);
                socketReadEvent.port = this.impl.port;
                if (i5 < 0) {
                    socketReadEvent.endOfStream = true;
                } else {
                    socketReadEvent.bytesRead = i5;
                }
                socketReadEvent.timeout = i4;
                socketReadEvent.commit();
                socketReadEvent.reset();
            }
            throw th;
        }
    }

    void silenceFindBugsUnwrittenField(InetAddress inetAddress) {
        this.impl.address = inetAddress;
    }

    /* loaded from: jfr.jar:jdk/jfr/internal/instrument/SocketInputStreamInstrumentor$AbstractPlainSocketImpl.class */
    static class AbstractPlainSocketImpl {
        InetAddress address;
        int port;

        AbstractPlainSocketImpl() {
        }
    }
}
