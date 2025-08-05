package org.apache.commons.net.discard;

import java.io.OutputStream;
import org.apache.commons.net.SocketClient;

/* loaded from: commons-net-3.6.jar:org/apache/commons/net/discard/DiscardTCPClient.class */
public class DiscardTCPClient extends SocketClient {
    public static final int DEFAULT_PORT = 9;

    public DiscardTCPClient() {
        setDefaultPort(9);
    }

    public OutputStream getOutputStream() {
        return this._output_;
    }
}
