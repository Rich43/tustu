package org.apache.commons.net.daytime;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.commons.net.SocketClient;

/* loaded from: commons-net-3.6.jar:org/apache/commons/net/daytime/DaytimeTCPClient.class */
public final class DaytimeTCPClient extends SocketClient {
    public static final int DEFAULT_PORT = 13;
    private final char[] __buffer = new char[64];

    public DaytimeTCPClient() {
        setDefaultPort(13);
    }

    public String getTime() throws IOException {
        StringBuilder result = new StringBuilder(this.__buffer.length);
        BufferedReader reader = new BufferedReader(new InputStreamReader(this._input_, getCharset()));
        while (true) {
            int read = reader.read(this.__buffer, 0, this.__buffer.length);
            if (read > 0) {
                result.append(this.__buffer, 0, read);
            } else {
                return result.toString();
            }
        }
    }
}
