package org.apache.commons.net.smtp;

import java.io.IOException;

/* loaded from: commons-net-3.6.jar:org/apache/commons/net/smtp/SMTPConnectionClosedException.class */
public final class SMTPConnectionClosedException extends IOException {
    private static final long serialVersionUID = 626520434326660627L;

    public SMTPConnectionClosedException() {
    }

    public SMTPConnectionClosedException(String message) {
        super(message);
    }
}
