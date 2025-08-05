package javax.net.ssl;

import java.util.EventListener;

/* loaded from: rt.jar:javax/net/ssl/HandshakeCompletedListener.class */
public interface HandshakeCompletedListener extends EventListener {
    void handshakeCompleted(HandshakeCompletedEvent handshakeCompletedEvent);
}
