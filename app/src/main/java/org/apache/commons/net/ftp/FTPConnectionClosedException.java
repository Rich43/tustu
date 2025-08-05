package org.apache.commons.net.ftp;

import java.io.IOException;

/* loaded from: commons-net-3.6.jar:org/apache/commons/net/ftp/FTPConnectionClosedException.class */
public class FTPConnectionClosedException extends IOException {
    private static final long serialVersionUID = 3500547241659379952L;

    public FTPConnectionClosedException() {
    }

    public FTPConnectionClosedException(String message) {
        super(message);
    }
}
