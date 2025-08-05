package java.net;

import java.io.IOException;
import sun.net.sdp.SdpSupport;

/* loaded from: rt.jar:java/net/SdpSocketImpl.class */
class SdpSocketImpl extends PlainSocketImpl {
    SdpSocketImpl() {
    }

    @Override // java.net.PlainSocketImpl, java.net.AbstractPlainSocketImpl, java.net.SocketImpl
    protected void create(boolean z2) throws IOException {
        if (!z2) {
            throw new UnsupportedOperationException("Must be a stream socket");
        }
        this.fd = SdpSupport.createSocket();
        if (this.socket != null) {
            this.socket.setCreated();
        }
        if (this.serverSocket != null) {
            this.serverSocket.setCreated();
        }
    }
}
