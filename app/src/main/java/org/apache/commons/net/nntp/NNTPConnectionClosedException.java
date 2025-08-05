package org.apache.commons.net.nntp;

import java.io.IOException;

/* loaded from: commons-net-3.6.jar:org/apache/commons/net/nntp/NNTPConnectionClosedException.class */
public final class NNTPConnectionClosedException extends IOException {
    private static final long serialVersionUID = 1029785635891040770L;

    public NNTPConnectionClosedException() {
    }

    public NNTPConnectionClosedException(String message) {
        super(message);
    }
}
